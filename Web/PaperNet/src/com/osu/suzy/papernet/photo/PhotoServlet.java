package com.osu.suzy.papernet.photo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.*;

import com.osu.suzy.papernet.UtilFileUpload;
import com.osu.suzy.papernet.UtilJson;

@SuppressWarnings("serial")
public class PhotoServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doOp(false, req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doOp(true, req, resp);
	}
	private void doOp(boolean ispost, HttpServletRequest reqx,
			HttpServletResponse resp) {
		try {
			Map<String, Object> params = UtilFileUpload.read(reqx,
					getServletContext()); 
			try {
				String op = UtilFileUpload.get(params, "op", "");

				Writer out = !"photo".equals(op) ? resp.getWriter() : null;
 
				byte[] pic = UtilFileUpload.get(params, "pic[]", new byte[0]);
				String pt = UtilFileUpload.get(params, "pic", "image/jpeg");
				//Files.write(Paths.get("pic.jpeg"), pic); 
				//InputStream in = new ByteArrayInputStream(pic);
				
			} finally {
			 
			}
				 
			} catch (IllegalArgumentException iae) {
			try {
				UtilJson.sendMessage(iae.getMessage(), false, resp.getWriter());
				} catch (IOException ignoreThis) {
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			try {
				UtilJson.sendMessage("Internal error occurred: "
						+ ioe.getClass().getName(), false, resp.getWriter());
			} catch (IOException ignoreThis) {
			}
		}
	 
	}
}
