package com.behavox.api.automation;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APIGuaranteeTest extends BaseTest{


    @BeforeClass
    public void setBaseProperties() {
        //setBasicProperties();
    }

    @Test
    public void APIGuranteeTest() {
    	RestAssured.baseURI = "http://localhost:8080/groovy/";
        String groovyScript = String.format("%s+%s",241.0, Math.random());
        RequestSpecification user_1Request = getRequest("user_2","pass_2");
        executeSubmitReq(user_1Request, groovyScript);

        RequestSpecification user_2Request = getRequest("user_3","pass_3");
        Response user_2Response = executeSubmitReq(user_2Request, groovyScript);


        Assert.assertNotEquals(user_2Response.getStatusCode(),500, "Response Code should be in 200 or 400 category, not 500. API Guarantee Failed");

    }
    
    public static void main(String[] args) {
		Random r = new Random();
		System.out.println(r.nextInt(5000, 6000));
	}
}
