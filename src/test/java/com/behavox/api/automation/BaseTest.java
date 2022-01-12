package com.behavox.api.automation;

import org.testng.ITestResult;

import com.behavox.api.automation.response.domain.SubmitRequest;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Class containing all the reusable methods
 *
 */
public class BaseTest {

	private Gson gson;

	protected Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	/**
	 * Method to provide root API EP
	 *
	 */
	protected void setBasicProperties() {
		RestAssured.baseURI = "http://localhost:8080/groovy/";
	}

	/**
	 * Method to execute POST request for submit API
	 **/
	protected Response executeSubmitReq(RequestSpecification httpRequest, String grovvyScript) {
		SubmitRequest submitRequest = new SubmitRequest();
		submitRequest.setCode(grovvyScript);
		httpRequest.body(submitRequest);
		Response response = httpRequest.request(Method.POST, "/submit");
		return response;
	}

	/**
	 * Method to execute GET request for status API
	 **/
	protected Response executeGetStatusReq(RequestSpecification httpRequest, String id) {
		Response response = httpRequest.request(Method.GET, "/status?id=" + id);
		return response;
	}

	/**
	 * Method to provide basic authentication to the request
	 **/
	protected RequestSpecification getRequest(String user, String pass) {
		return RestAssured.given().contentType(ContentType.JSON).auth().preemptive().basic(user, pass);
	}


	public void printTestResults(ITestResult result) {
		try {
			if (result.getStatus() == ITestResult.SUCCESS) {
				System.out.println(result.getMethod().getMethodName() + " **********Passed **********");
			} else if (result.getStatus() == ITestResult.FAILURE) {
				System.out.println(result.getMethod().getMethodName() + " **********Failed ***********");
			} else if (result.getStatus() == ITestResult.SKIP) {
				System.out.println(result.getMethod().getMethodName() + " **********Skiped***********");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
