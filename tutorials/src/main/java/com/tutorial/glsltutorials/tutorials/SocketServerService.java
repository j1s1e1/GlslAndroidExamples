package com.tutorial.glsltutorials.tutorials;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class SocketServerService extends Service {
    public static final int SERVERPORT = 8081;
    public static String SERVERIP = "10.0.2.16";
    private final IBinder mBinder = new SocketServerServiceBinder();
    private ServerSocket serverSocket;
    private Handler handler = new Handler();

    int interfaceCount = 0;
    Thread fst = new Thread(new ServerThread());

    boolean socketOpen = false;

    public SocketServerService() {
        SERVERIP = getLocalIpAddress();
        fst.start();
    }

    private void forwardMessageWithIntent(String message)
    {
        Intent i=new Intent();
        i.setAction("SOCKET_SERVER_SERVICE");
        i.putExtra("Message", message);
        sendBroadcast(i);
    }

    private void forwardMessage(String message)
    {
        forwardMessageWithIntent(message);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class SocketServerServiceBinder extends Binder {
        SocketServerService getService() {
            return SocketServerService.this;
        }

        public String test() {
            return "SocketServerService function";
        }

        public String getIpAddress()
        {
            return SERVERIP;
        }

        public String getPort()
        {
            return String.valueOf(SERVERPORT);
        }

        public void cleanup()
        {
            closeSocket();
        }

    }

    // gets the ip address of your phone's network
    private String getLocalIpAddress() {
        try {
            interfaceCount = 0;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        interfaceCount++;
                        if (interfaceCount > 1)
                            return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (SERVERIP != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            {
                                forwardMessageWithIntent("Listening on IP: " + SERVERIP);
                            }
                        }
                    });
                    if (serverSocket == null) {
                        serverSocket = new ServerSocket(SERVERPORT);
                    }
                    while (true) {
                        // listen for incoming clients
                        Socket client = serverSocket.accept();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                forwardMessageWithIntent("Connected.\n");
                            }
                        });

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                Log.d("ServerActivity", line);
                                final String line_data = line;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        forwardMessageWithIntent(line_data + "\n");
                                        if (line_data.contains("disconnect")) {
                                            StartNewThread();
                                        }
                                    }
                                });
                            }
                            break;
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    forwardMessageWithIntent("Oops. Connection interrupted. Please reconnect your phones.");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            forwardMessageWithIntent("Couldn't detect internet connection.");
                        }
                    });
                }
            } catch (final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        forwardMessageWithIntent("Error " + e.toString());
                    }
                });
                e.printStackTrace();
            }
        }
    }

    private void StartNewThread() {
        try {
            forwardMessageWithIntent("Disconnected.\n");
            if (fst.isAlive()) {
                forwardMessageWithIntent("Thread still alive\n");
            } else {
                fst = new Thread(new ServerThread());
                fst.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        try {
            serverSocket.close();
            forwardMessageWithIntent("Disconnected.\n");
            if (fst.isAlive()) {
                forwardMessageWithIntent("Thread still alive\n");
            } else {
                fst = new Thread(new ServerThread());
                fst.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
