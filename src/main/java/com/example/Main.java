package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];

            String header;
            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());
            System.out.println("Richiesta terminata\n");

            if (resource.equals("/") || resource.equals("/index.html")) {
                
                File file = new File("htdocs/index.html");
                InputStream input = new FileInputStream(file);

                out.writeBytes(version + " 200 OK\n");
                out.writeBytes("Content-Type: text/html\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                byte[] buf = new byte[8192];
                int n;
                while((n = input.read(buf)) != -1){
                    out.write(buf, 0, n);
                }
                input.close();
                
            } else if (resource.equals("/file.txt")) {
                String responseBody = "ecco il testo in txt";
                out.writeBytes(version + " 200 OK\n");
                out.writeBytes("Content-Type: text/plain\n");
                out.writeBytes("Content-Length: " + responseBody.length() + "\n");
                out.writeBytes("\n");
                out.writeBytes(responseBody);
            } else {
                out.writeBytes(version + " 404 Not found\n");
                out.writeBytes("Content-Length: 0\n");
                out.writeBytes("\n");
            }
        }
    }
    private String getContentType(File f){
            String[] s = f.getName().split("\\.");
            String ext = s[s.length - 1];
            switch (ext) {
                case "html":
                    return "text/html";
                case "png":
                    return "image/png";
                case "txt":
                    return "text/plain";
                default:
                    break;
            }
    }
}