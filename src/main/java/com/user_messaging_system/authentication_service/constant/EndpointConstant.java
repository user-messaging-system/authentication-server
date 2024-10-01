package com.user_messaging_system.authentication_service.constant;

public final class EndpointConstant {
    private EndpointConstant(){
        throw new AssertionError();
    }

    private static final String VERSION = "/v1";
    private static final String BASE_URL = VERSION + "/api/auth";

    public static final String USER_LOGIN_URL = BASE_URL + "/login";

}
