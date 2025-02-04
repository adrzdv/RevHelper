package com.example.revhelper.server;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {

    public HttpServer(int port) {
        super(port);
    }

    public HttpServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if ("/data".equals(uri)) {
            return newFixedLengthResponse("Привет, клиент!");
        } else  {
            String msg = "<html><body><h1>Hello from Android Hotspot!</h1></body></html>";
            Log.d("HttpServer", "Пришел запрос: " + session.getUri());
            return newFixedLengthResponse(msg);
        }
//        else {
//            return newFixedLengthResponse("404 Not Found");
//        }
    }

}
