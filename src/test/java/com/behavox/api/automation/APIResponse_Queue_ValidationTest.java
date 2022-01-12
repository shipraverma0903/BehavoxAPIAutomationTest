package com.behavox.api.automation;

import com.behavox.api.automation.response.domain.SubmitEPResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.util.Random;

public class APIResponse_Queue_ValidationTest extends BaseTest {
	Random r = new Random();
	ITestResult result;

	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
	}

	/**
	 * Method to send a submit API request which will keep the processor busy for
	 * certain amount of time
	 * 
	 * @author shipra.verma
	 */

	@Test(priority = 1)
	public void SubmitEPTest_FirstBusyRequest() throws InterruptedException {

		String groovyScript = String.format("Thread.sleep(%s)", r.nextInt(10000, 11000));
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		// Execute first busy request
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "First request to keep processor busy failed");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));
	}

	/**
	 * Method to send another submit API request which will keep the processor busy
	 * for certain amount of time
	 */

	@Test(priority = 2)
	public void SubmitEPTest_SecondBusyRequest() throws InterruptedException {

		String groovyScript = String.format("Thread.sleep(%s)", r.nextInt(11000, 12000));
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		// Execute second busy request
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "Second request to keep processor busy failed");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));
	}

	/**
	 * Method to send third submit API request request should remain queued with
	 * status as "Pending' until process becomes free
	 */

	@Test(priority = 3)
	public void SubmitEPTest_FirstQueueRequest() {

		double number1 = Math.random();
		double number2 = Math.random();
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "First queued request failed");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));
	}

	/**
	 * Method to send fourth submit API request request should remain queued with
	 * status as "Pending' until process becomes free
	 */
	@Test(priority = 4)
	public void SubmitEPTest_SecondQueueRequest() {

		double number1 = Math.random();
		double number2 = Math.random();
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "Second request queuing failed");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));
	}

	/**
	 * Method to send fifth submit API request request should remain queued with
	 * status as "Pending' until process becomes free Test is failed - only 2
	 * requests can remain in queue at a time
	 */
	@Test(priority = 5)
	public void SubmitEPTest_ThirdQueueRequest() throws InterruptedException {

		double number1 = Math.random();
		double number2 = Math.random();
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "Third request queing failed");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));
		Thread.sleep(12000);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}
}
