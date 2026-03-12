package tests.api_tests;

import engine.api.AddedHeaderTokenProvider;
import engine.assertions.HardAssertions;
import engine.api.ApiRequestFactory;
import engine.api.ResponseActions;
import engine.utils.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.reqresAPI.ReqresApiCalls;

import java.util.ArrayList;
import java.util.List;

public class ReqresTests {
    private ReqresApiCalls apiCalls;
    private String newID;
    @Test
    public void getAllObjects(){
    Response res=apiCalls.getObjectsEndPoint(null,null);
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(2),"Apple iPhone 12 Pro Max");
    }
    @Test(dependsOnMethods = "updateObjectPartially")
    public void getSingleObject(){
    Response res=apiCalls.getObjectsEndPoint("/"+newID,null);
    ResponseActions.checkResponseStatus(res,200);
       HardAssertions.assertTextContains(  ResponseActions.getValueByPath(res,"name",String.class),"updated");
    }

    @Test
    public void getMultipleObjects(){
        Response res=apiCalls.getObjectsEndPoint(null,  List.of("4", "3"));
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(1),"Apple iPhone 12 Pro Max");
    }
    @Test(priority = 0)
    public void postObject(){
        Response res=apiCalls.postObject("postObject.json");
        ResponseActions.checkResponseStatus(res,200);
        newID=ResponseActions.getValueByPath(res,"id", String.class);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"Apple");
    }
    @Test(dependsOnMethods = "postObject")
    public void updateObject(){
        Response res=apiCalls.updateObject("updateObject.json",newID);
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"nar");
    }
    @Test(dependsOnMethods = "updateObject")
    public void updateObjectPartially(){
        Response res=apiCalls.updatePartialObject("partialUpdate.json",newID);
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"updated");
    }

    @Test(dependsOnMethods = "getSingleObject")
    public void deleteObject(){
        Response res=apiCalls.deleteObject(newID);
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"message",String.class)),"deleted");

    }

    @Test
    public void crudFlow(){
        Response res2=apiCalls.postObject("postObject.json");
        ResponseActions.checkResponseStatus(res2,200);
        String id=(ResponseActions.getValueByPath(res2,"id",String.class));
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res2,"name",String.class)),"Apple");
        Response res3=apiCalls.updateObject("updateObject.json",id);
        ResponseActions.checkResponseStatus(res3,200);
        Response res4=apiCalls.updatePartialObject("partialUpdate.json",id);
        ResponseActions.checkResponseStatus(res4,200);
        Response res5=apiCalls.deleteObject(id);
        ResponseActions.checkResponseStatus(res5,200);
        Response res6=apiCalls.getObjectsEndPoint(id,null);
        ResponseActions.checkResponseStatus(res6,404);
    }

    @BeforeClass
    private void handleAuthorization(){
        apiCalls=new ReqresApiCalls(new ApiRequestFactory(PropertyReader.get("main_reqres_url", String.class),new AddedHeaderTokenProvider()));

    }
}
