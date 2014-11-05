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
        response.setVersionCode(1);
        response.setUrl("https://raw.github.com/wiki/nakaken0629/iped/apk/app-server-debug-0.9.0.apk");
        return response;
    }
}