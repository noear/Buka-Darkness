package io.goshin.bukadarkness.sited;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class Client {
    public static String request(JSONObject params) throws Throwable {
        InetAddress localAddress;
        try {
            localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ignored) {
            localAddress = InetAddress.getByName("127.0.0.1");
        }
        Socket socket = new Socket(localAddress, Server.PORT);
        Writer out = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
        out.write(URLEncoder.encode(params.toString(), "utf-8") + "\n");
        out.flush();
        Reader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
        StringBuilder inputString = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (((char) c) == '\n')
                break;
            inputString.append((char) c);
        }
        socket.close();
        if (inputString.toString().trim().isEmpty()) {
            throw new Exception("没有数据返回，该源可能暂时不可用");
        }
        return URLDecoder.decode(inputString.toString(), "utf-8");
    }
}
