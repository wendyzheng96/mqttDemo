package com.zyf.ws.http;

import com.zyf.ws.bean.GetClientsBean;
import com.zyf.ws.bean.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zyf on 2019/1/31.
 */
public interface ApiService {

    @GET("api/v2/nodes/{nodeId}/clients")
    Observable<HttpResult<GetClientsBean>> getClient(@Path("nodeId") String nodeId);
}
