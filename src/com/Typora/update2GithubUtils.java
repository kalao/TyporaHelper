package com.Typora;

import java.io.*;
import java.nio.charset.Charset;

public class update2GithubUtils {

    public static String Read(InputStream in) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        String line;
        String text = "";
        while ((line = input.readLine()) != null) {
            text += line+"\n";
//            System.out.println("Line: " + line);
        }

        return text;
    }
    public static void Write(OutputStream out,String text)throws IOException{
        BufferedWriter output=new BufferedWriter(new OutputStreamWriter(out));
        output.write(text);
        output.flush();
    }
}
