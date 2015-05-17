package com.osu.suzy.papernet;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.osu.suzy.papernet.http.HttpGet;
import com.osu.suzy.papernet.http.HttpPost;
import com.osu.suzy.papernet.http.HttpUpload;

public class Uploader {
	private static final String BASE_URL = Constants.BASE_URL;
	private static final String USER_URL = BASE_URL + "/user";
	private static final String AUTH_URL = BASE_URL + "/auth";
	private static final String PTO_URL = BASE_URL + "/pet";
	private static final String CHARSET = "UTF-8";

	public static boolean upload(PtoItem pto) throws Exception {

        JSONObject ptoJson = uploadPto(pto);
        if (ptoJson.getBoolean("success"))
            return true;
		return false;
	}

	private static JSONObject uploadPto(PtoItem pto)
			throws IOException, JSONException {
		HttpUpload createPto = new HttpUpload(PTO_URL, CHARSET);
		createPto.addFormField("op", "create");
		createPto.addFilePart("pic", "paper.jpg", pto.getPhoto());
		String petResult = createPto.finish();
		JSONObject ptoJson = new JSONObject(petResult);
		return ptoJson;
	}


}
