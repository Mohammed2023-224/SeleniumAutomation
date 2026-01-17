package pages.reqresAPI;

import engine.api.ApiRequestFactory;
import engine.api.HttpMethods;
import engine.api.ResponseActions;
import engine.assertions.HardAssertions;
import engine.utils.ClassPathLoading;
import engine.utils.PropertyReader;
import io.restassured.response.Response;

import java.util.List;

public class ReqresApiCalls {
    private final ApiRequestFactory factory;
    String getFileClassPath(String file){
        return ClassPathLoading.getResourceAsPath(file,false).toString();
    }
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

    public Response postObject(String filePath){
        return factory.executeWithoutRetry(req ->{
            req.setBasePathParameter(PropertyReader.get("objectsEndPoint", String.class));
            req.setBodyAsFile(getFileClassPath(filePath),"Application/json");
            return req.performRequest(HttpMethods.POST);
        });
    }
    public Response updateObject(String filePath,String id){
        return factory.executeWithoutRetry(req ->{
            req.setBasePathParameter(PropertyReader.get("objectsEndPoint", String.class)+"/"+id);
            req.setBodyAsFile(getFileClassPath(filePath),"Application/json");
            return req.performRequest(HttpMethods.PUT);
        });
    }

    public Response updatePartialObject(String filePath,String id){
        return factory.executeWithoutRetry(req ->{
            req.setBasePathParameter(PropertyReader.get("objectsEndPoint", String.class)+"/"+id);
            req.setBodyAsFile(getFileClassPath(filePath),"Application/json");
            return req.performRequest(HttpMethods.PATCH);
        });
    }
    public Response deleteObject(String id){
        return factory.executeWithoutRetry(req ->{
            req.setBasePathParameter(PropertyReader.get("objectsEndPoint", String.class)+"/"+id);
            return req.performRequest(HttpMethods.DELETE);
        });
    }
}
