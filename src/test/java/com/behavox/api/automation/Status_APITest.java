package com.behavox.api.automation;

import com.behavox.api.automation.response.domain.StatusEPResponse;
import com.behavox.api.automation.response.domain.SubmitEPResponse;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

public class Status_APITest extends BaseTest {

	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
	}

	/**
	 * Method verify groovy/status API is working and returning COMPLETED status
	 * when data is processed
	 *
	 */
	@Test(priority = 1)
	public void StatusAPITest_COMPLETED() throws InterruptedException {
		Random r = new Random();
		int number1 = r.nextInt(100);
		int number2 = r.nextInt(150);
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);
		Thread.sleep(3000);
		Response statusResponse = executeGetStatusReq(httpRequest, submitEPResponse.getId());
		StatusEPResponse statusEPResponse = getGson().fromJson(statusResponse.asString(), StatusEPResponse.class);

		Assert.assertEquals(statusResponse.getStatusCode(), 200, "User_1 is able to run the script");
		Assert.assertEquals(statusEPResponse.getStatus(), "COMPLETED");
		Assert.assertEquals(statusEPResponse.getResult(), String.valueOf(number1 + number2));
	}

	/**
	 * Method verify groovy/status API is working and returning IN PROGRESS status
	 * when data from submit request is in processing state
	 *
	 */
	@Test(priority = 2)
	public void StatusAPITest_INPROGRESS() throws InterruptedException {

		Random r = new Random();
		String groovyScript = String.format("Thread.sleep(%s)", r.nextInt(3000, 4000));
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Response statusResponse = executeGetStatusReq(httpRequest, submitEPResponse.getId());
		StatusEPResponse statusEPResponse = getGson().fromJson(statusResponse.asString(), StatusEPResponse.class);

		Assert.assertEquals(statusResponse.getStatusCode(), 200, "User_1 is able to run the script");
		Assert.assertEquals(statusEPResponse.getStatus(), "IN_PROGRESS");
		Thread.sleep(4000);
	}

	/**
	 * Method verify groovy/status API is working and returning PENDING status when
	 * request from submit API is queued
	 *
	 */

	@Test(priority = 3)
	public void StatusAPITest_PENDING() throws InterruptedException {

		Random r = new Random(System.currentTimeMillis());
		String groovyScript3 = String.format("%s+%s", Math.random(), Math.random());
		String groovyScript1 = String.format("Thread.sleep(%s)", r.nextInt(5000, 6000));
		String groovyScript2 = String.format("Thread.sleep(%s)", r.nextInt(6000, 7000));
		executeSubmitReq(getRequest("user_1", "pass_1"), groovyScript1);
		executeSubmitReq(getRequest("user_1", "pass_1"), groovyScript2);

		RequestSpecification httpRequestTwo = getRequest("user_2", "pass_2");
		Response secondCallResponse = executeSubmitReq(httpRequestTwo, groovyScript3);
		SubmitEPResponse submitSubmitCallResponse = getGson().fromJson(secondCallResponse.getBody().asString(),
				SubmitEPResponse.class);

		Response getStatusResponse = executeGetStatusReq(httpRequestTwo, submitSubmitCallResponse.getId());
		StatusEPResponse statusEPResponse = new Gson().fromJson(getStatusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertEquals(secondCallResponse.getStatusCode(), 200);
		Assert.assertNotNull(submitSubmitCallResponse);
		Assert.assertEquals(statusEPResponse.getStatus(), "PENDING",
				"Third request is queued, Request queue functionality is  passed!!");
		Thread.sleep(7000);
	}

	/**
	 * Method verify groovy/status API is working and returning FAILED status when
	 * provided groovy script is not returning any valid response or throws runtime
	 * exception
	 *
	 */
	@Test(priority = 4)
	public void StatusAPITest_FAILED() throws InterruptedException {

		Random r = new Random();
		int number1 = r.nextInt(100, 1000);

		String groovyScript = String.format("%s/0", number1);

		RequestSpecification httpRequest = getRequest("user_2", "pass_2");
		Response callResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitSubmitCallResponse = getGson().fromJson(callResponse.getBody().asString(),
				SubmitEPResponse.class);
		Thread.sleep(3000);
		Response getStatusResponse = executeGetStatusReq(httpRequest, submitSubmitCallResponse.getId());
		StatusEPResponse statusEPResponse = new Gson().fromJson(getStatusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertEquals(callResponse.getStatusCode(), 200);
		Assert.assertNotNull(submitSubmitCallResponse);
		Assert.assertEquals(statusEPResponse.getStatus(), "FAILED",
				"Request is successful with status as failed, Request queue functionality is  passed!!");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}

}
