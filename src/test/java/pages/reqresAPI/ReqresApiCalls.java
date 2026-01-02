package pages.reqresAPI;

import engine.api.ApiRequestFactory;
import engine.api.HttpMethods;
import engine.api.ResponseActions;
import engine.assertions.HardAssertions;
import engine.utils.PropertyReader;
import io.restassured.response.Response;

import java.util.List;

public class ReqresApiCalls {
    private final ApiRequestFactory factory;

    public ReqresApiCalls(ApiRequestFactory factory){
        this.factory=factory;
    }
    public Response getObjectsEndPoint(String idPath, List<String> queryIds){
        return factory.executeWithoutRetry(req ->{
            req.setBasePathParameter(idPath==null?PropertyReader.get("objectsEndPoint", String.class)
                    :PropertyReader.get("objectsEndPoint", String.class)+idPath);
            req.addQueryParam("id",queryIds);
            return req.performRequest(HttpMethods.GET);
        });
    }

public void validateResponseSuccessfully(Response res){
    ResponseActions.checkResponseStatus(res,200);
}
public void validateBodyContainsText(String actualText,String text){
    HardAssertions.assertTextContains(actualText,text);
}
}
