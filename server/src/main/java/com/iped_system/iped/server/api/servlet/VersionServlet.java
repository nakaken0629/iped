package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.login.VersionRequest;
import com.iped_system.iped.common.login.VersionResponse;

public class VersionServlet extends BaseServlet {
    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return VersionRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        VersionResponse response = new VersionResponse();
        response.setVersionCode(2);
        response.setUrl("http://192.168.11.103/app-local-debug.apk");
        return response;
    }
}