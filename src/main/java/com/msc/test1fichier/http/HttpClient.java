package com.msc.test1fichier.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author Michael
 */
public class HttpClient {

    static InputStream download(URL url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public enum Content {
        JSON("application/json"), FORM("application/x-www-form-urlencoded");
        String str;

        private Content(String str) {
            this.str = str;
        }

        public String getContent() {
            return this.str;
        }
    }

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private static HttpResponse send(URL url, String bodyInJson, HttpOptions opts, Method method, Content content) {
        HttpResponse resp = new HttpResponse();
        try {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession sslSession) {
                        return true;
                    }
                });
            }
            if (opts != null) {
                if (opts.autorization != null) {
                    conn.setRequestProperty("Authorization", opts.getAutorization());
                }
            }

            switch (method) {
                case GET:
                case DELETE:
                    ((HttpURLConnection) conn).setRequestMethod(method.name());
                    break;
                case POST:
                case PUT:
                    ((HttpURLConnection) conn).setRequestMethod(method.name());
                    conn.setRequestProperty("Content-Type", content.str);
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(bodyInJson.length()));
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.write(bodyInJson.getBytes());
                    break;

            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            resp.headers = ((HttpURLConnection) conn).getHeaderFields();
            resp.status = ((HttpURLConnection) conn).getResponseCode();
            resp.response = sb.toString();
            ((HttpURLConnection) conn).disconnect();
            return resp;
        } catch (Exception e) {
            StringWriter w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            resp.response = w.toString();
        }
        resp.status = 500;
        return resp;
    }

    public static HttpResponse get(String url, HttpOptions opts) throws MalformedURLException {
        return get(new URL(url), opts);
    }

    public static HttpResponse get(String url) throws MalformedURLException {
        return get(new URL(url));
    }

    public static HttpResponse get(URL url) {
        return get(url, null);
    }

    public static HttpResponse get(URL url, HttpOptions opts) {
        return send(url, null, opts, Method.GET, Content.JSON);
    }

    public static HttpResponse post(String url, String bodyInJson, Content content) throws MalformedURLException {
        return post(new URL(url), bodyInJson, content);
    }

    public static HttpResponse post(String url, String bodyInJson, HttpOptions opts, Content content) throws MalformedURLException {
        return post(new URL(url), bodyInJson, opts, content);
    }

    public static HttpResponse post(URL url, String bodyInJson, Content content) {
        return post(url, bodyInJson, null, content);
    }

    public static HttpResponse post(URL url, String bodyInJson, HttpOptions opts, Content content) {
        return send(url, bodyInJson, opts, Method.POST, content);
    }

    public static HttpResponse put(String url, String bodyInJson) throws MalformedURLException {
        return put(new URL(url), bodyInJson);
    }

    public static HttpResponse put(String url, String bodyInJson, HttpOptions opts) throws MalformedURLException {
        return put(new URL(url), bodyInJson, opts);
    }

    public static HttpResponse put(URL url, String bodyInJson) {
        return put(url, bodyInJson, null);
    }

    public static HttpResponse put(URL url, String bodyInJson, HttpOptions opts) {
        return send(url, bodyInJson, opts, Method.PUT, Content.JSON);
    }

    public static HttpResponse delete(String url, HttpOptions opts) throws MalformedURLException {
        return delete(new URL(url), opts);
    }

    public static HttpResponse delete(String url) throws MalformedURLException {
        return delete(new URL(url));
    }

    public static HttpResponse delete(URL url) {
        return delete(url, null);
    }

    public static HttpResponse delete(URL url, HttpOptions opts) {
        return send(url, null, opts, Method.DELETE, Content.JSON);
    }

    public static InputStream download(URL url, List<Header> headers) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        conn.setDoInput(true);
        conn.setUseCaches(false);
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return true;
                }
            });
        }
        try {
            ((HttpURLConnection) conn).setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
            //silent error
        }
        for (Header h : headers) {
            ((HttpURLConnection) conn).setRequestProperty(h.key, h.value);
        }
        try {
            return conn.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static class HttpOptions {

        private String autorization;

        public void setAutorization(String login, String password) {
            String basic = login + ":" + password;
            this.autorization = "Basic " + Base64.getEncoder().encodeToString(basic.getBytes());
        }

        public String getAutorization() {
            return this.autorization;
        }
    }

    public static class HttpResponse {

        public int status;
        public String response;
        public Map<String, List<String>> headers;
    }

    public static class Header {

        public String key;
        public String value;
    }

}
