package com.picotech.smartdb.app;

import android.content.Context;

import com.picotech.smartdb.utils.SessionPreferences;

public class Endpoints {

    private String url, protocol;
    private SessionPreferences sessionPreferences;

    public Endpoints(String env, Context _context) {

        switch (env) {
            case "development":
                this.protocol = "http://";
//                this.url = "192.168.43.84/smartdbbox/api/public";
                this.url = "192.168.0.56/smarthome/public";

//                this.url = "172.17.80.96/smarthome/public";
//                this.url = "172.20.84.31/smartdbbox/api/public";
                break;

            case "staging":
                this.protocol = "https://";
                this.url = "staging.uni10smarthome.com";
                break;

            case "production":
                this.protocol = "https://";
                this.url = "main.uni10smarthome.com";
                break;
        }

        sessionPreferences = new SessionPreferences(_context);


    }

    private String getUserData(String key) {
        return sessionPreferences.getUserDetails().get(key);
    }

    private String getProtocol() {
        return protocol;
    }

    private String getUrl() {
        return url;
    }

    public String getTempPassApi() {
        return getProtocol() + getUrl() + "/api/mobile/changepass";
    }

    public String getLoginApi() {
        return getProtocol() + getUrl() + "/api/mobile/login";
    }

    public String getLogApi() {
        return getProtocol() + getUrl() + "/api/mobile/log";
    }

    public String getDbStatus() {
        return getProtocol() + getUrl() + "/api/mobile/device/db/stat";
    }

    public String updateFuseState() {
        return getProtocol() + getUrl() + "/api/device/db/update";
    }

    public String getDeviceList(String id) {
        return getProtocol() + getUrl() + "/api/mobile/device/db/list/" + id;
    }

    public String getRegisterApi() {
        return getProtocol() + getUrl() + "/api/tech/register";
    }

    public String getProfileApi() {
        return getProtocol() + getUrl() + "/api/mobile/profile/";
    }
    public String updateProfileApi() {
        return getProtocol() + getUrl() + "/api/mobile/profile/update";
    }

    public String getDeviceProfile() {
        return getProtocol() + getUrl() + "/api/mobile/deviceprofile";
    }

    public String getRegisterPhone(){
        return getProtocol() + getUrl() + "/api/mobile/deviceprofile";
    }

    public String getCheckTacNum(){
        return getProtocol() + getUrl() + "/api/mobile/deviceprofile";
    }


    public String getAddDevice(){
        return getProtocol() + getUrl() + "/api/mobile/device/db/add";
    }

    public String getCheckDeviceAvai(){
        return getProtocol() + getUrl() + "/api/mobile/device/db/check";
    }




}
