package info.journeymap.shaded.org.eclipse.jetty.http;

public interface HttpFieldPreEncoder {

    HttpVersion getHttpVersion();

    byte[] getEncodedField(HttpHeader var1, String var2, String var3);
}