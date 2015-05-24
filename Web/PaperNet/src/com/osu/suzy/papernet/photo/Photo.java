package com.osu.suzy.papernet.photo;

import javax.jdo.annotations.Persistent;
 

public class Photo {
	@Persistent
	private byte[] pic;
	@Persistent
	private String picType;
	
	
	public void setPic(byte[] photo) {
		if (photo == null)
			photo = new byte[0];
		this.pic = photo;
	}
	
	public void setPicType(String picType) {
		this.picType = picType;
	}
}
