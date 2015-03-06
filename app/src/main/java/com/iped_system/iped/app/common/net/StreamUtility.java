package com.iped_system.iped.app.common.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kenji on 15/03/06.
 */
public class StreamUtility {
    public static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        while(true) {
            int len = inputStream.read(buffer);
            if(len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        return bout.toByteArray();
    }
}
