package com.smartwaste.googlemaps;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Yachen on 4/22/18.
 */

public class DamageReport extends AsyncTask<ReportItem, Void, Integer> {

    private static final String HTTP_POST = "POST";
    private URL serverUrl;

    public DamageReport(String serverUrlStr) throws MalformedURLException {
        this.serverUrl = new URL(serverUrlStr);
    }

    private static int sendPOST(URL url, ReportItem givenReportItem) throws IOException {
        // https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(HTTP_POST);

        Map<String, String> reportItemMap = givenReportItem.getItems();
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : reportItemMap.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("charset", "utf-8");


        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(postDataBytes);
        os.flush();
        os.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
        return responseCode;
    }

    @Override
    protected Integer doInBackground(ReportItem... reportItems) {
        try {
            return sendPOST(serverUrl, reportItems[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HttpURLConnection.HTTP_NOT_FOUND;
    }
}
