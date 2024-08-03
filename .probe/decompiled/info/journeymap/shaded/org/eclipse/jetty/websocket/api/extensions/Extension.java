package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

public interface Extension extends IncomingFrames, OutgoingFrames {

    ExtensionConfig getConfig();

    String getName();

    boolean isRsv1User();

    boolean isRsv2User();

    boolean isRsv3User();

    void setNextIncomingFrames(IncomingFrames var1);

    void setNextOutgoingFrames(OutgoingFrames var1);
}