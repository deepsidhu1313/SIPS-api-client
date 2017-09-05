/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.sips.api.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nika
 */
public class Client {

    private String host;
    private int port;
    private String api_key;
    private Socket socket;

    public Client(String host, int port, String api_key) {
        this.host = host;
        this.port = port;
        this.api_key = api_key;
        System.out.println("" + this.executeRequest("TestConnection"));
    }

    public JSONObject executeRequest(String... request) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            ArrayList<String> commands = new ArrayList<>();
            Collections.addAll(commands, request);
            try (OutputStream os = socket.getOutputStream(); DataInputStream dIn = new DataInputStream(socket.getInputStream()); DataOutputStream outToServer = new DataOutputStream(os)) {
                JSONObject requestJson = new JSONObject();
                requestJson.put("Command", commands.get(0));
                JSONObject pingRequestBody = new JSONObject();
                pingRequestBody.put("UUID", GlobalValues.UUID);
                JSONArray args = new JSONArray();
                for (int i = 1; i < commands.size(); i++) {
                    String get = commands.get(i);
                    args.put(get);
                }
                pingRequestBody.put("ARGS", args);
                pingRequestBody.put("API_KEY", api_key);
                requestJson.put("Body", pingRequestBody);
                String sendmsg = requestJson.toString(4);
                byte[] bytes = sendmsg.getBytes("UTF-8");
                outToServer.writeInt(bytes.length);
                outToServer.write(bytes);

                int length = dIn.readInt();                    // read length of incoming message
                byte[] message = new byte[length];

                if (length > 0) {
                    dIn.readFully(message, 0, message.length); // read the message
                }
                JSONObject reply = new JSONObject(new String(message));
                //System.out.println(""+reply.toString(4));
                return reply.getJSONObject("Body", new JSONObject());
            } catch (Exception e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new JSONObject().put("Error", "Error");
    }

}
