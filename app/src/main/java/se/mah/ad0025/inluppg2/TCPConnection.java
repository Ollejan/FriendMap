package se.mah.ad0025.inluppg2;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Klass som ansluter till server och hanterar in- och utströmmar.
 * @author Jonas Dahlström on 2014-10-12.
 */
public class TCPConnection {
    private RunOnThread thread;
    private Receive receive;
    private ReceiveListener listener;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private int connectionPort;
    private String ip;
    private Exception exception;
    public static InetAddress address;

    public TCPConnection(String ip, int connectionPort, ReceiveListener listener) {
        this.ip = ip;
        this.connectionPort = connectionPort;
        this.listener = listener;
        thread = new RunOnThread();

    }

    public void connect() {
        thread.start();
        thread.execute(new Connect());
    }

    public void disconnect() {
        thread.execute(new Disconnect());
    }

    public void send(String str) {
        thread.execute(new Send(str));
    }

    private class Receive extends Thread {
        public void run() {
            String result;
            JSONObject json;
            try {
                while (receive != null) {
                    result = input.readUTF();
                    json = new JSONObject(result);
                    listener.newMessage(result);
                    listener.newJsonMessage(json);
                }
            } catch (Exception e) {
                receive = null;
            }
        }
    }

    public Exception getException() {
        Exception result = exception;
        exception = null;
        return result;
    }

    private class Connect implements Runnable {
        public void run() {
            try {
                address = InetAddress.getByName(ip);
                socket = new Socket(address, connectionPort);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                output.flush();
                listener.newMessage("CONNECTED");
                receive = new Receive();
                receive.start();
            } catch (Exception e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Disconnect implements Runnable {
        public void run() {
            try {
                if (socket != null)
                    socket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                thread.stop();
                listener.newMessage("CLOSED");
            } catch(IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Send implements Runnable {
        private String str;

        public Send(String str) {
            this.str = str;
        }

        public void run() {
            try {
                output.writeUTF(str);
                output.flush();
            } catch (IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }
}
