package ru.ifmo.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLReader {
    private static String encoding = null;

    public static String read(String address) {
        String result = "";
        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            List<Byte> bytes = new ArrayList<Byte>();
            while (true) {
                int x = is.read();
                if (x == -1) {
                    break;
                }
                bytes.add((byte) x);
            }
            byte[] byteArray = new byte[bytes.size()];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = bytes.get(i);
            }
            String a = new String(byteArray, "utf-8");
            Pattern pattern = Pattern.compile("encoding=\"(.*?)\"");
            Matcher matcher = pattern.matcher(a);
            if (matcher.find()) {
                encoding = matcher.group(1);
            } else {
                encoding = "utf-8";
            }
            result = new String(byteArray, encoding);
        } catch (MalformedURLException e) {
            result = null;
        } catch (IOException e) {
            result = null;
        }
        return result;
    }

    public static String getEncoding() {
        return encoding;
    }
}
