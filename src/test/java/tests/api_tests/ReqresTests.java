package tests.api_tests;

import engine.api.ApiRequestFactory;
import engine.api.NoAuthTokenProvider;
import engine.api.ResponseActions;
import engine.utils.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.reqresAPI.ReqresApiCalls;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ReqresTests extends APIBaseTest{
    private ReqresApiCalls apiCalls;
    @Test
    public void getAllObjects(){
    Response res=apiCalls.getObjectsEndPoint(null,null);
    }
    @Test
    public void getSingleObject(){
    Response res=apiCalls.getObjectsEndPoint("/3",null);
    }
    @Test
    public void getMultipleObjects(){
        Response res=apiCalls.getObjectsEndPoint(null,  List.of("4", "3"));

    }

    @BeforeClass
    private void handleAuthorization(){
        NoAuthTokenProvider noAuthTokenProvider = new NoAuthTokenProvider();
        ApiRequestFactory apiRequestFactory=new ApiRequestFactory(PropertyReader.get("main_reqres_url", String.class),noAuthTokenProvider);
        apiCalls=new ReqresApiCalls(apiRequestFactory);
    }
}
