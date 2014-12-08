package com.tutorial.glsltutorials.tutorials.Movement;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jamie on 11/29/14.
 */
public class SocketListener extends AsyncTask<Void, byte[], String> {
    public static final String TAG = "SocketListener";
    private Context context;
    private static OutputStream socketData;
    private int debug = 0;

    /**
     * @param context
     * @param socketData
     */
    public SocketListener(Context context, OutputStream socketData){
        this.context = context;
        this.socketData = socketData;
    }

    @Override
    protected String doInBackground(Void... params){
        try {
            ServerSocket serverSocket = new ServerSocket(2121);
            Log.d(SocketListener.TAG, "Server: Socket opened");
            Socket client = serverSocket.accept();
            Log.d(SocketListener.TAG, "Server: connection done");
            InputStream inputStream = client.getInputStream();
            int len;
            byte buf[] = new byte[1024];
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    byte[] progress = new byte[len];
                    System.arraycopy(buf, 0, progress, 0, progress.length);
                    publishProgress(progress);
                }
                inputStream.close();
            } catch (IOException e) {
                Log.d(SocketListener.TAG, e.toString());
            }
            serverSocket.close();
            return "SocketClosed";
        } catch (IOException e) {
            Log.e(SocketListener.TAG, e.getMessage());
            return "Error " + e.toString();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String result) {
        new SocketListener(context, socketData).execute();
    }

    @Override
    protected void onProgressUpdate(byte[]... newData)
    {
        for (byte[] bytes: newData) {
            try {
                socketData.write(bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        debug = 1;
    }

}
