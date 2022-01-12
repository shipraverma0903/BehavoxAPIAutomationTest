package com.behavox.api.automation;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIGuaranteeTest extends BaseTest {

	/**
	 * Setting up root reuestURI
	 * 
	 * @author shipra.verma
	 *
	 */
	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
	}

	/**
	 * Method to Verify the responses from Submit API. All responses should return
	 * status code form 2XX or 4XX Family. When submitted duplicate groovy script by
	 * users in the submit API, API returns 500-'Internal Server Error' response
	 * code This fails the API guarantee
	 */

	@Test
	public void APIGuranteeTest() {
		String groovyScript = String.format("%s+%s", 241.0, Math.random());
		RequestSpecification user_1Request = getRequest("user_2", "pass_2");
		executeSubmitReq(user_1Request, groovyScript);

		RequestSpecification user_2Request = getRequest("user_3", "pass_3");
		Response user_2Response = executeSubmitReq(user_2Request, groovyScript);

		Assert.assertNotEquals(user_2Response.getStatusCode(), 500,
				"Response Code should be in 200 or 400 category, not 500. API Guarantee Failed");

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}
}
