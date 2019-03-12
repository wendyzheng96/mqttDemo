package com.zyf.ws.bean;

import java.util.List;

/**
 * Created by zyf on 2019/1/31.
 */
public class GetClientsBean {

    /**
     * current_page : 1
     * page_size : 20
     * total_num : 2
     * total_page : 1
     * objects : [{"client_id":"861614032442697","username":"admin","ipaddress":"10.0.0.8","port":46703,"clean_sess":true,"proto_ver":4,"keepalive":30,"connected_at":"2019-01-31 11:10:31"},{"client_id":"867960035853008","username":"admin123","ipaddress":"10.0.0.31","port":43606,"clean_sess":true,"proto_ver":4,"keepalive":30,"connected_at":"2019-01-31 10:37:18"}]
     */

    private int current_page;
    private int page_size;
    private int total_num;
    private int total_page;
    private List<ClientBean> clients;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<ClientBean> getClients() {
        return clients;
    }

    public void setClients(List<ClientBean> clients) {
        this.clients = clients;
    }

    public static class ClientBean {
        /**
         * client_id : 861614032442697
         * username : admin
         * ipaddress : 10.0.0.8
         * port : 46703
         * clean_sess : true
         * proto_ver : 4
         * keepalive : 30
         * connected_at : 2019-01-31 11:10:31
         */

        private String client_id;
        private String username;
        private String ipaddress;
        private int port;
        private boolean clean_sess;
        private int proto_ver;
        private int keepalive;
        private String connected_at;

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIpaddress() {
            return ipaddress;
        }

        public void setIpaddress(String ipaddress) {
            this.ipaddress = ipaddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isClean_sess() {
            return clean_sess;
        }

        public void setClean_sess(boolean clean_sess) {
            this.clean_sess = clean_sess;
        }

        public int getProto_ver() {
            return proto_ver;
        }

        public void setProto_ver(int proto_ver) {
            this.proto_ver = proto_ver;
        }

        public int getKeepalive() {
            return keepalive;
        }

        public void setKeepalive(int keepalive) {
            this.keepalive = keepalive;
        }

        public String getConnected_at() {
            return connected_at;
        }

        public void setConnected_at(String connected_at) {
            this.connected_at = connected_at;
        }

        @Override
        public String toString() {
            return "ClientBean{" +
                    "client_id='" + client_id + '\'' +
                    ", username='" + username + '\'' +
                    ", ipaddress='" + ipaddress + '\'' +
                    ", port=" + port +
                    ", clean_sess=" + clean_sess +
                    ", proto_ver=" + proto_ver +
                    ", keepalive=" + keepalive +
                    ", connected_at='" + connected_at + '\'' +
                    '}';
        }
    }
}
