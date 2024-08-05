package me.lucko.spark.lib.bytesocks;

public interface BytesocksClient {

    static BytesocksClient create(String host, String userAgent) {
        return new BytesocksClientImpl(host, userAgent);
    }

    BytesocksClient.Socket createAndConnect(BytesocksClient.Listener var1) throws Exception;

    BytesocksClient.Socket connect(String var1, BytesocksClient.Listener var2) throws Exception;

    public interface Listener {

        default void onOpen() {
        }

        default void onError(Throwable error) {
        }

        default void onText(String data) {
        }

        default void onClose(int statusCode, String reason) {
        }
    }

    public interface Socket {

        String channelId();

        boolean isOpen();

        void send(String var1);

        void close(int var1, String var2);
    }
}