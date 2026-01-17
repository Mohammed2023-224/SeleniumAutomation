package tests.api_tests;

import engine.assertions.HardAssertions;
import tests.baseTest.APIBaseTest;
import engine.api.ApiRequestFactory;
import engine.api.NoAuthTokenProvider;
import engine.api.ResponseActions;
import engine.utils.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.reqresAPI.ReqresApiCalls;

import java.util.ArrayList;
import java.util.List;

public class ReqresTests extends APIBaseTest {
    private ReqresApiCalls apiCalls;
    @Test
    public void getAllObjects(){
    Response res=apiCalls.getObjectsEndPoint(null,null);
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(2),"Apple iPhone 12 Pro Max");
    }
    @Test
    public void getSingleObject(){
    Response res=apiCalls.getObjectsEndPoint("/3",null);
    ResponseActions.checkResponseStatus(res,200);
       HardAssertions.assertTextContains(  ResponseActions.getValueByPath(res,"name",String.class),"Apple iPhone 12 Pro Max");
    }

    @Test
    public void getMultipleObjects(){
        Response res=apiCalls.getObjectsEndPoint(null,  List.of("4", "3"));
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(1),"Apple iPhone 12 Pro Max");
    }
    @Test
    public void postObject(){
        Response res=apiCalls.postObject("postObject.json");
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"Apple");
    }
    @Test
    public void updateObject(){
        Response res=apiCalls.updateObject("updateObject.json","ff8081819782e69e019b98e775176b6e");
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"nar");
    }
    @Test
    public void updateObjectPartially(){
        Response res=apiCalls.updatePartialObject("partialUpdate.json","ff8081819782e69e019b98e775176b6e");
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"name",String.class)),"nar");
    }

    @Test
    public void deleteObject(){
        Response res=apiCalls.deleteObject("ff8081819782e69e019b990dbf406bcb");
        ResponseActions.checkResponseStatus(res,200);
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res,"message",String.class)),"deleted");

    }

    @Test
    public void crudFlow(){
        Response res2=apiCalls.postObject("src/test/resources/postObject.json");
        ResponseActions.checkResponseStatus(res2,200);
        String id=(ResponseActions.getValueByPath(res2,"id",String.class));
         HardAssertions.assertTextContains((ResponseActions.getValueByPath(res2,"name",String.class)),"Apple");
        Response res3=apiCalls.updateObject("src/test/resources/updateObject.json",id);
        ResponseActions.checkResponseStatus(res3,200);
        Response res4=apiCalls.updatePartialObject("src/test/resources/partialUpdate.json",id);
        ResponseActions.checkResponseStatus(res4,200);
        Response res5=apiCalls.deleteObject(id);
        ResponseActions.checkResponseStatus(res5,200);
        Response res6=apiCalls.getObjectsEndPoint(id,null);
        ResponseActions.checkResponseStatus(res6,404);
    }

    @BeforeClass
    private void handleAuthorization(){
        NoAuthTokenProvider noAuthTokenProvider = new NoAuthTokenProvider();
        ApiRequestFactory apiRequestFactory=new ApiRequestFactory(PropertyReader.get("main_reqres_url", String.class),noAuthTokenProvider);
        apiCalls=new ReqresApiCalls(apiRequestFactory);
    }
}
