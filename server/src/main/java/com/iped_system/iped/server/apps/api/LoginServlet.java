package com.iped_system.iped.server.apps.api;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;

/**
 * Created by kenji on 2014/08/04.
 */
public class LoginServlet extends BaseServlet {

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        LoginRequest request = (LoginRequest) baseRequest;
        String username = request.getUsername();
        String password = request.getPassword();

        LoginResponse response = new LoginResponse();
        response.setStatus("password".equals(password) ? "SUCCESS" : "FAIL");
        return response;
    }
}
