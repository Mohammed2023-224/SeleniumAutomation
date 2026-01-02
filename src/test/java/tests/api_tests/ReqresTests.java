package tests.api_tests;

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

public class ReqresTests extends APIBaseTest{
    private ReqresApiCalls apiCalls;
    @Test
    public void getAllObjects(){
    Response res=apiCalls.getObjectsEndPoint(null,null);
        apiCalls.validateResponseSuccessfully(res);
        System.out.println( ResponseActions.getValueByPath(res,"id",ArrayList.class));
        apiCalls.validateBodyContainsText((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(2),"Apple iPhone 12 Pro Max");
    }
    @Test
    public void getSingleObject(){
    Response res=apiCalls.getObjectsEndPoint("/3",null);
    apiCalls.validateResponseSuccessfully(res);
      apiCalls.validateBodyContainsText(  ResponseActions.getValueByPath(res,"name",String.class),"Apple iPhone 12 Pro Max");
    }

    @Test
    public void getMultipleObjects(){
        Response res=apiCalls.getObjectsEndPoint(null,  List.of("4", "3"));
        apiCalls.validateResponseSuccessfully(res);
        apiCalls.validateBodyContainsText((String) (ResponseActions.getValueByPath(res,"name",ArrayList.class)).get(1),"Apple iPhone 12 Pro Max");
    }

    @BeforeClass
    private void handleAuthorization(){
        NoAuthTokenProvider noAuthTokenProvider = new NoAuthTokenProvider();
        ApiRequestFactory apiRequestFactory=new ApiRequestFactory(PropertyReader.get("main_reqres_url", String.class),noAuthTokenProvider);
        apiCalls=new ReqresApiCalls(apiRequestFactory);
    }
}
