package com.behavox.api.automation.response.domain;

/**
 * Class to represent JSON response of groovy/status API
 * 
 * @author shipra.verma
 *
 */
public class StatusEPResponse {
	private String id;
	private String status;
	private String result;

	public StatusEPResponse() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResponseStatus{" + "id='" + id + '\'' + ", status='" + status + '\'' + ", result='" + result + '\''
				+ '}';
	}

}
