package info.journeymap.shaded.org.eclipse.jetty.http;

public interface HttpTokens {

    byte COLON = 58;

    byte TAB = 9;

    byte LINE_FEED = 10;

    byte CARRIAGE_RETURN = 13;

    byte SPACE = 32;

    byte[] CRLF = new byte[] { 13, 10 };

    byte SEMI_COLON = 59;

    public static enum EndOfContent {

        UNKNOWN_CONTENT, NO_CONTENT, EOF_CONTENT, CONTENT_LENGTH, CHUNKED_CONTENT
    }
}