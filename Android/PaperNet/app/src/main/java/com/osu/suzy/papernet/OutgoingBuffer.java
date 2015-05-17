package com.osu.suzy.papernet;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class OutgoingBuffer {

	private static final String EXT_PREPARING = ".preparing";
	private static final String EXT_READY = ".ready";

	public static void toast(final Activity activity, final String message) {
		try {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void write(final Activity activity, final PtoItem pto) {

		new Thread(new Runnable() {
			public void run() {
				try {
					File dir = computeDirectory(activity);
					long now = System.currentTimeMillis();
					String prepFilename = now + EXT_PREPARING;
					File prepFile = new File(dir, prepFilename);
					writeToFile(pto, prepFile);
					String readyFilename = now + EXT_READY;
					File readyFile = new File(dir, readyFilename);
					prepFile.renameTo(readyFile);
				} catch (IOException e) {
					toast(activity, "Unable to save file");
				}
			}
		}).start();
	}

	private static File computeDirectory(Context context) throws IOException {
		File mainDir = context.getFilesDir();
		File myDir = new File(mainDir, "outgoing");
		myDir.mkdirs();
		if (!myDir.exists())
			throw new FileNotFoundException("Cannot create directory "
					+ myDir.getCanonicalPath());
		return myDir;
	}

	private static void writeToFile(PtoItem pto, File file) throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		try {
			pto.serialize(dos);
		} finally {
			try {
				dos.close();
			} catch (IOException ignoreThis) {
			}
		}
	}

	private static PtoItem readFromFile(File file) {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			try {
				return PtoItem.deserialize(dis);
			} finally {
				try {
					dis.close();
				} catch (IOException ignoreThis) {
				}
			}
		} catch (IOException ioe) {
			return null;
		}
	}

	private static Thread uploaderThread;

	public static synchronized void startUploaderThread(final Activity activity) {
		if (uploaderThread == null) {
			uploaderThread = new Thread(new Runnable() {
				public void run() {
					watchAndUpload(activity);
				}
			});
			uploaderThread.start();
		}
	}

	private static boolean isConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}

	private static void watchAndUpload(Activity activity) {
		while (true) {
			try {
				Thread.sleep(10000);//?
			} catch (InterruptedException e) {
				uploaderThread = null;
				break;
			}
			if (isConnected(activity))
				try {
					File dir = computeDirectory(activity);
					File[] ready = dir.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							return filename != null
									&& filename.endsWith(EXT_READY);
						}
					});
					if (ready == null)
						ready = new File[0];
					for (int i = 0; i < ready.length; i++) {
						File file = ready[i];
						boolean deleteFile = false;

						PtoItem pto = readFromFile(file);
						if (pto != null) {
							boolean uploaded = Uploader.upload(pto);
							if (uploaded)
								deleteFile = true;
						} else {
							// corrupted
							deleteFile = true;
						}

						if (deleteFile)
							file.delete();
					}
				} catch (Exception e) {
					toast(activity, "Unable to read/upload file");
				}

		}
	}

}
