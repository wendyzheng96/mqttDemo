package com.zyf.ws.http;

/**
 * Created by zyf on 2019/1/31.
 */
public class UrlConstant {

    public static final String BASE_URL = "http://10.0.0.33:1883/";

    public static final String GET_CLIENTS = "api/v2/nodes/{nodeId}/clients";//获取指定节点的客户端连接列表

    public static final String GET_SESSIONS = "api/v2/nodes/emq@127.0.0.1/sessions";//获取指定节点的会话列表

}
