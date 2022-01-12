package com.behavox.api.automation.response.domain;

/**
 * Class to represent payload of groovy/submit API request
 * 
 * @author shipra.verma
 *
 */

public class SubmitRequest {

	private String code;

	public SubmitRequest() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "SubmitRequest{" + "code='" + code + '\'' + '}';
	}
}
