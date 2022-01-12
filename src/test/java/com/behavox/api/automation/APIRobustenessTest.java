package com.behavox.api.automation;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APIRobustenessTest extends BaseTest{


    @BeforeClass
    public void setBaseProperties() {
        setBasicProperties();
    }

    @Test
    public void APIRobustnessTest() {

        String groovyScript = String.format("%s+%s",Math.random(),Math.random());
        RequestSpecification httpRequest = getRequest("user_1","pass_1");
        //Execute submit request for user_1
        Response submitResponseUser1 = executeSubmitReq(httpRequest, groovyScript);

        RequestSpecification httpRequestUser2 = getRequest("user_2","pass_2");
        //Execute submit request with same grovvy script for user_2
        Response submitResponseUser2 = executeSubmitReq(httpRequestUser2, groovyScript);

        Assert.assertEquals(submitResponseUser1.getStatusCode(), 200, "User_1 is able to run the script");
        Assert.assertEquals(submitResponseUser2.getStatusCode(), 200,"User_2 ought to be eligible to run same script as user_1");

    }
}
