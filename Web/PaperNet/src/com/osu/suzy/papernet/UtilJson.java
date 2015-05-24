package com.osu.suzy.papernet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class UtilJson {

	public static void sendMessage(String message, boolean success, Writer out)
			throws IOException {
		try {
			JSONObject object = new JSONObject();
			object.put("success", success);
			object.put("info", message);
			object.write(out);
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

	public static void sendRawMessage(String message, boolean success,
			Writer out) throws IOException {
		out.write("{");
		out.write("\"success\":");
		out.write(Boolean.toString(success));
		out.write(",\"info\":");
		out.write(message);
		out.write("}");
	}

	public static String toJsonObject(String... nameValuePairs)
			throws IOException {
		try {
			StringWriter out = new StringWriter();
			JSONObject object = new JSONObject();
			if (nameValuePairs != null)
				for (int i = 0; i < nameValuePairs.length - 1; i += 2) {
					object.put(nameValuePairs[i], nameValuePairs[i + 1]);
				}
			object.write(out);
			return out.toString();
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

}
