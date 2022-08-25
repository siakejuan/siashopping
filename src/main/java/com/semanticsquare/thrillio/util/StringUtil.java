package com.semanticsquare.thrillio.util;

import java.security.MessageDigest;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StringUtil {
	private final static String ENC_ALGO = "SHA";

	public static String encodePassword(String password) {
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(ENC_ALGO);

		} catch (Exception e) {
			// log.error("Exception: " + e);
			e.printStackTrace();
			return password;
		}

		md.reset();
		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// calculate the hash
		byte[] encodedPassword = md.digest();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}
	
}
