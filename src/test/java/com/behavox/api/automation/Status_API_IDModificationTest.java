package com.behavox.api.automation;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.behavox.api.automation.response.domain.StatusEPResponse;
import com.behavox.api.automation.response.domain.SubmitEPResponse;
import com.google.gson.Gson;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Status_API_IDModificationTest extends BaseTest {

	@BeforeClass

	public void startTest() {
		setBasicProperties();
	}

	/**
	 * Method to verify status API response when ID is converted to uppercase format
	 * When ID is changed to uppercase, user is still able to fetch the correct
	 * results
	 * 
	 * @author shipra.verma
	 */
	@Test

	public void APIInput_UpperCaseTest() {
		double number1 = Math.random();
		double number2 = Math.random();
		String groovyscript = String.format("%s+%s", number1, number2);
		RequestSpecification rs = getRequest("user_1", "pass_1");
		Response submitresposend = executeSubmitReq(rs, groovyscript);

		SubmitEPResponse submitresponse = getGson().fromJson(submitresposend.getBody().asString(),
				SubmitEPResponse.class);

		String id = submitresponse.getId().toUpperCase();
		Response statusResponse = executeGetStatusReq(rs, id);
		StatusEPResponse statusRequestResponse = new Gson().fromJson(statusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertNotEquals(statusRequestResponse.getResult(), number1 + number2 + "");
		Assert.assertNotEquals(statusResponse.getStatusCode(), 200,
				"User is able to fetch records from the uppercase id, Test Failed");

	}

	/**
	 * Method to verify status API response when ID is formatted with adding
	 * trailing zeros When ID is formatted by adding trailing zeros, user is still
	 * able to fetch the correct results
	 * 
	 * @author shipra.verma
	 */
	@Test
	public void APIInput_AppendIDTest() {
		double number1 = Math.random();
		double number2 = Math.random();
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification rs = getRequest("user_1", "Pass_1");
		Response submitResponse = executeSubmitReq(rs, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.getBody().asString(),
				SubmitEPResponse.class);
		String id = "00000000" + submitEPResponse.getId();
		Response statusResponse = executeGetStatusReq(rs, id);
		StatusEPResponse statusEPResponse = getGson().fromJson(statusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertNotEquals(statusEPResponse.getResult(), number1 + number2 + "");
		Assert.assertNotEquals(statusResponse.getStatusCode(), 200,
				"User is able to fetch records from the modified id, Test Failed");

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}

}