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

import java.lang.System.Logger;
import java.util.Random;

public class APIParallelExecutionTest extends BaseTest {

	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
	}

	/**
	 * Method to verify Second request Processing when first request is in progress
	 * state Second request is processed parallely
	 * 
	 * @author shipra.verma
	 */

	@Test(priority = 1)
	public void ExecuteTwoRequestsTest_WhileFirstOneInProgress() throws InterruptedException {

		Random r = new Random();
		String groovyScript = String.format("Thread.sleep(%s)", r.nextInt(7000, 8000));
		// Execute first submit request
		executeSubmitReq(getRequest("user_1", "pass_1"), groovyScript);

		RequestSpecification httpRequestTwo = getRequest("user_2", "pass_2");
		// Execute second submit request
		Response secondCallResponse = executeSubmitReq(httpRequestTwo, String.format("%s+%s", 212.0, Math.random()));
		SubmitEPResponse secondSubmitCallResponse = new Gson().fromJson(secondCallResponse.getBody().asString(),
				SubmitEPResponse.class);

		Response getStatusResponse = executeGetStatusReq(httpRequestTwo, secondSubmitCallResponse.getId());
		StatusEPResponse secondCallStatusResponse = getGson().fromJson(getStatusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertEquals(secondCallResponse.getStatusCode(), 200);
		Assert.assertNotNull(secondSubmitCallResponse);
		Assert.assertNotEquals(secondCallStatusResponse.getStatus(), "PENDING",
				"Second request is processed while first one is still IN-PROGRESS, Parallel execution for 2 requests passed!!");
		Thread.sleep(8000);
	}

	/**
	 * Method to verify third request Processing when two request is in progress
	 * state Third request is processed queued with the pending status
	 * 
	 * @author shipra.verma
	 */

	@Test(priority = 2)
	public void ExecuteThreeRequestsTest_WhileFirstTwoInProgress() throws InterruptedException {

		Random r = new Random();
		String groovyScript1 = String.format("Thread.sleep(%s)", r.nextInt(8000, 9000));
		String groovyScript2 = String.format("Thread.sleep(%s)", r.nextInt(9000, 10000));
		String groovyScript3 = String.format("%s+%s", Math.random(), Math.random());
		// Execute first submit request
		executeSubmitReq(getRequest("user_1", "pass_1"), groovyScript1);
		// Execute second submit request
		executeSubmitReq(getRequest("user_1", "pass_1"), groovyScript2);

		RequestSpecification httpRequestTwo = getRequest("user_2", "pass_2");
		// Execute third submit request
		Response thirdCallResponse = executeSubmitReq(httpRequestTwo, groovyScript3);
		SubmitEPResponse thirdSubmitCallResponse = getGson().fromJson(thirdCallResponse.getBody().asString(),
				SubmitEPResponse.class);

		Response getStatusResponse = executeGetStatusReq(httpRequestTwo, thirdSubmitCallResponse.getId());
		StatusEPResponse thirdSubmitStatusResponse = new Gson().fromJson(getStatusResponse.getBody().asString(),
				StatusEPResponse.class);

		Assert.assertEquals(thirdCallResponse.getStatusCode(), 200);
		Assert.assertNotNull(thirdSubmitCallResponse);
		Assert.assertEquals(thirdSubmitStatusResponse.getStatus(), "PENDING",
				"Third request is queued, Request queue functionality is  passed!!");
		Thread.sleep(10000);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}
}