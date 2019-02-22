package com.picotech.smartdb.utils;

import android.content.Context;
import android.icu.text.Replaceable;
import android.util.Log;
import android.widget.Toast;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebsocketPreferences extends WebSocketListener {

    private Context _context;
    public String msg;


    //constructur
    public WebsocketPreferences(Context context) {

        this._context = context;
    }


    //open
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send("SUCCESS CONNECT TO APP");
    }


    //recieve
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        //Toast.makeText(_context, "MSG: " + text, Toast.LENGTH_LONG).show();
        Log.d("SOCKETMSG", "MSG" + msg);

    }


    //fail
    @Override
    public void onFailure(WebSocket websocket, Throwable t, Response response) {
        //   Toast.makeText(_context, "MSG: " + response, Toast.LENGTH_LONG).show();
        Log.e("SOCKETERR", "MSG" + response.toString());

    }


}
