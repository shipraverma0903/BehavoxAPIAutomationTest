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

public class Submit_APITest extends BaseTest {

	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
	}

	/**
	 * Method verify groovy/submit API is working as expected for simple input
	 *
	 */
	@Test
	public void SubmitAPITest() {

		double number1 = Math.random();
		double number2 = Math.random();
		String groovyScript = String.format("%s+%s", number1, number2);
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "User_1 is able to run the script");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));

	}

	/**
	 * Method verify groovy/submit API is working as expected for simple string type
	 * input
	 *
	 */
	// @Test
	public void SubmitEPStringTest_Success() {

		String groovyScript = "'a'+'b'";
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response submitResponse = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(submitResponse.asString(), SubmitEPResponse.class);

		Assert.assertEquals(submitResponse.getStatusCode(), 200, "User_1 is able to run the script");
		Assert.assertTrue(Strings.isNotNullAndNotEmpty(submitEPResponse.getId()));

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);

	}
}
