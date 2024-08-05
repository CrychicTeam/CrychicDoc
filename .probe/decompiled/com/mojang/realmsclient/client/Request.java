package com.mojang.realmsclient.client;

import com.mojang.realmsclient.exception.RealmsHttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;

public abstract class Request<T extends Request<T>> {

    protected HttpURLConnection connection;

    private boolean connected;

    protected String url;

    private static final int DEFAULT_READ_TIMEOUT = 60000;

    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

    public Request(String string0, int int1, int int2) {
        try {
            this.url = string0;
            Proxy $$3 = RealmsClientConfig.getProxy();
            if ($$3 != null) {
                this.connection = (HttpURLConnection) new URL(string0).openConnection($$3);
            } else {
                this.connection = (HttpURLConnection) new URL(string0).openConnection();
            }
            this.connection.setConnectTimeout(int1);
            this.connection.setReadTimeout(int2);
        } catch (MalformedURLException var5) {
            throw new RealmsHttpException(var5.getMessage(), var5);
        } catch (IOException var6) {
            throw new RealmsHttpException(var6.getMessage(), var6);
        }
    }

    public void cookie(String string0, String string1) {
        cookie(this.connection, string0, string1);
    }

    public static void cookie(HttpURLConnection httpURLConnection0, String string1, String string2) {
        String $$3 = httpURLConnection0.getRequestProperty("Cookie");
        if ($$3 == null) {
            httpURLConnection0.setRequestProperty("Cookie", string1 + "=" + string2);
        } else {
            httpURLConnection0.setRequestProperty("Cookie", $$3 + ";" + string1 + "=" + string2);
        }
    }

    public T header(String string0, String string1) {
        this.connection.addRequestProperty(string0, string1);
        return (T) this;
    }

    public int getRetryAfterHeader() {
        return getRetryAfterHeader(this.connection);
    }

    public static int getRetryAfterHeader(HttpURLConnection httpURLConnection0) {
        String $$1 = httpURLConnection0.getHeaderField("Retry-After");
        try {
            return Integer.valueOf($$1);
        } catch (Exception var3) {
            return 5;
        }
    }

    public int responseCode() {
        try {
            this.connect();
            return this.connection.getResponseCode();
        } catch (Exception var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
        }
    }

    public String text() {
        try {
            this.connect();
            String $$0;
            if (this.responseCode() >= 400) {
                $$0 = this.read(this.connection.getErrorStream());
            } else {
                $$0 = this.read(this.connection.getInputStream());
            }
            this.dispose();
            return $$0;
        } catch (IOException var2) {
            throw new RealmsHttpException(var2.getMessage(), var2);
        }
    }

    private String read(@Nullable InputStream inputStream0) throws IOException {
        if (inputStream0 == null) {
            return "";
        } else {
            InputStreamReader $$1 = new InputStreamReader(inputStream0, StandardCharsets.UTF_8);
            StringBuilder $$2 = new StringBuilder();
            for (int $$3 = $$1.read(); $$3 != -1; $$3 = $$1.read()) {
                $$2.append((char) $$3);
            }
            return $$2.toString();
        }
    }

    private void dispose() {
        byte[] $$0 = new byte[1024];
        try {
            InputStream $$1 = this.connection.getInputStream();
            while ($$1.read($$0) > 0) {
            }
            $$1.close();
            return;
        } catch (Exception var9) {
            try {
                InputStream $$3 = this.connection.getErrorStream();
                if ($$3 != null) {
                    while ($$3.read($$0) > 0) {
                    }
                    $$3.close();
                    return;
                }
            } catch (IOException var8) {
                return;
            }
        } finally {
            if (this.connection != null) {
                this.connection.disconnect();
            }
        }
    }

    protected T connect() {
        if (this.connected) {
            return (T) this;
        } else {
            T $$0 = this.doConnect();
            this.connected = true;
            return $$0;
        }
    }

    protected abstract T doConnect();

    public static Request<?> get(String string0) {
        return new Request.Get(string0, 5000, 60000);
    }

    public static Request<?> get(String string0, int int1, int int2) {
        return new Request.Get(string0, int1, int2);
    }

    public static Request<?> post(String string0, String string1) {
        return new Request.Post(string0, string1, 5000, 60000);
    }

    public static Request<?> post(String string0, String string1, int int2, int int3) {
        return new Request.Post(string0, string1, int2, int3);
    }

    public static Request<?> delete(String string0) {
        return new Request.Delete(string0, 5000, 60000);
    }

    public static Request<?> put(String string0, String string1) {
        return new Request.Put(string0, string1, 5000, 60000);
    }

    public static Request<?> put(String string0, String string1, int int2, int int3) {
        return new Request.Put(string0, string1, int2, int3);
    }

    public String getHeader(String string0) {
        return getHeader(this.connection, string0);
    }

    public static String getHeader(HttpURLConnection httpURLConnection0, String string1) {
        try {
            return httpURLConnection0.getHeaderField(string1);
        } catch (Exception var3) {
            return "";
        }
    }

    public static class Delete extends Request<Request.Delete> {

        public Delete(String string0, int int1, int int2) {
            super(string0, int1, int2);
        }

        public Request.Delete doConnect() {
            try {
                this.f_87306_.setDoOutput(true);
                this.f_87306_.setRequestMethod("DELETE");
                this.f_87306_.connect();
                return this;
            } catch (Exception var2) {
                throw new RealmsHttpException(var2.getMessage(), var2);
            }
        }
    }

    public static class Get extends Request<Request.Get> {

        public Get(String string0, int int1, int int2) {
            super(string0, int1, int2);
        }

        public Request.Get doConnect() {
            try {
                this.f_87306_.setDoInput(true);
                this.f_87306_.setDoOutput(true);
                this.f_87306_.setUseCaches(false);
                this.f_87306_.setRequestMethod("GET");
                return this;
            } catch (Exception var2) {
                throw new RealmsHttpException(var2.getMessage(), var2);
            }
        }
    }

    public static class Post extends Request<Request.Post> {

        private final String content;

        public Post(String string0, String string1, int int2, int int3) {
            super(string0, int2, int3);
            this.content = string1;
        }

        public Request.Post doConnect() {
            try {
                if (this.content != null) {
                    this.f_87306_.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.f_87306_.setDoInput(true);
                this.f_87306_.setDoOutput(true);
                this.f_87306_.setUseCaches(false);
                this.f_87306_.setRequestMethod("POST");
                OutputStream $$0 = this.f_87306_.getOutputStream();
                OutputStreamWriter $$1 = new OutputStreamWriter($$0, "UTF-8");
                $$1.write(this.content);
                $$1.close();
                $$0.flush();
                return this;
            } catch (Exception var3) {
                throw new RealmsHttpException(var3.getMessage(), var3);
            }
        }
    }

    public static class Put extends Request<Request.Put> {

        private final String content;

        public Put(String string0, String string1, int int2, int int3) {
            super(string0, int2, int3);
            this.content = string1;
        }

        public Request.Put doConnect() {
            try {
                if (this.content != null) {
                    this.f_87306_.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.f_87306_.setDoOutput(true);
                this.f_87306_.setDoInput(true);
                this.f_87306_.setRequestMethod("PUT");
                OutputStream $$0 = this.f_87306_.getOutputStream();
                OutputStreamWriter $$1 = new OutputStreamWriter($$0, "UTF-8");
                $$1.write(this.content);
                $$1.close();
                $$0.flush();
                return this;
            } catch (Exception var3) {
                throw new RealmsHttpException(var3.getMessage(), var3);
            }
        }
    }
}