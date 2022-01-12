package com.behavox.api.automation;

import com.behavox.api.automation.response.domain.StatusEPResponse;
import com.behavox.api.automation.response.domain.SubmitEPResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.util.Strings;

public class APISecurityTest extends BaseTest {

	String statusId;

	/**
	 * Before class Method setting basic root EP Created on submit request and
	 * capturing ID form response in StatusID String
	 * 
	 * @author shipra.verma
	 */
	@BeforeClass
	public void setBaseProperties() {
		setBasicProperties();
		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		String groovyScript = String.format("%s+%s", 123.0, Math.random());
		Response response = executeSubmitReq(httpRequest, groovyScript);
		SubmitEPResponse submitEPResponse = getGson().fromJson(response.asString(), SubmitEPResponse.class);
		statusId = submitEPResponse.getId();
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_1, pass_1
	 */
	@Test
	public void AuthorizedTest_User1() {

		RequestSpecification httpRequest = getRequest("user_1", "pass_1");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertNotEquals(response.getStatusCode(), 500);
		Assert.assertNotEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_2, pass_2
	 */
	@Test
	public void AuthorizedTest_User2() {

		RequestSpecification httpRequest = getRequest("user_2", "pass_2");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertNotEquals(response.getStatusCode(), 500);
		Assert.assertNotEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_3, pass_3
	 */
	@Test
	public void AuthorizedTest_User3() {

		RequestSpecification httpRequest = getRequest("user_3", "pass_3");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertNotEquals(response.getStatusCode(), 500);
		Assert.assertNotEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_4, pass_4
	 */
	@Test
	public void AuthorizedTest_User4() {

		RequestSpecification httpRequest = getRequest("user_4", "pass_4");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertNotEquals(response.getStatusCode(), 500);
		Assert.assertNotEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_5, pass_5
	 */

	@Test
	public void AuthorizedTest_User5() {

		RequestSpecification httpRequest = getRequest("user_5", "pass_5");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertNotEquals(response.getStatusCode(), 500);
		Assert.assertNotEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides incorrect ID in
	 * status request Credentials user- user_1, pass_5 Incorrect password provided
	 */

	@Test
	public void UnAuthorizedTest() {

		RequestSpecification httpRequest = getRequest("user_1", "pass_5");
		Response response = executeGetStatusReq(httpRequest, "test-1234");
		Assert.assertEquals(response.getStatusCode(), 401);
	}

	/**
	 * Method to verify response of the request when user provides correct ID in
	 * status request generated for the user_1 Credentials user- user_5, pass_5 User
	 * 5 trying to access user 1 data
	 */
	@Test
	public void MessageSecurityTest() {

		RequestSpecification httpRequest = getRequest("user_5", "pass_5");
		Response response = executeGetStatusReq(httpRequest, statusId);
		StatusEPResponse statusEPResponse = getGson().fromJson(response.asString(), StatusEPResponse.class);
		Assert.assertTrue(Strings.isNullOrEmpty(statusEPResponse.getResult()),
				"User 5 should not be allowed to access user_1's data, User data security Failed!!");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		printTestResults(result);
	}

}
