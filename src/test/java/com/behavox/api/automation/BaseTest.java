package com.behavox.api.automation;

import com.behavox.api.automation.response.domain.SubmitRequest;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

    private Gson gson;

    protected Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    protected void setBasicProperties() {
        RestAssured.baseURI = "http://localhost:8080/groovy/";
    }

    protected Response executeSubmitReq(RequestSpecification httpRequest, String grovvyScript) {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCode(grovvyScript);
        httpRequest.body(submitRequest);
        Response response = httpRequest.request(Method.POST, "/submit");
        return response;
    }

    protected Response executeGetStatusReq(RequestSpecification httpRequest, String id) {
        Response response = httpRequest.request(Method.GET, "/status?id=" + id);
        return response;
    }


    protected RequestSpecification getRequest(String user, String pass) {
        return RestAssured.given().contentType(ContentType.JSON)
                .auth()
                .preemptive()
                .basic(user, pass);
    }


}
