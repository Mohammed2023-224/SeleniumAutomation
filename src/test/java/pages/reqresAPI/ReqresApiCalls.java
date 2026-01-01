package pages.reqresAPI;

import engine.api.ApiRequestFactory;
import engine.api.HttpMethods;
import engine.reporters.Loggers;
import engine.utils.PropertyReader;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Collections;
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
            Loggers.getLogger().info(req.logRequest());
            return req.performRequest(HttpMethods.GET);
        });
    }


}
