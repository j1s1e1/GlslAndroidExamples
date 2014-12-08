package com.tutorial.glsltutorials.tutorials.Movement;

import java.net.Socket;

/**
 * Created by jamie on 11/29/14.
 */
public class StateObject {
    // State object for reading client data asynchronously
    // Client  socket.
    public Socket workSocket = null;
    // Size of receive buffer.
    public static int BufferSize = 1024;
    // Receive buffer.
    public byte[] buffer = new byte[BufferSize];
    // Received data string.
    public StringBuilder sb = new StringBuilder();
}
