package info.journeymap.shaded.org.eclipse.jetty.io;

import java.io.Closeable;
import java.nio.ByteBuffer;

public interface Connection extends Closeable {

    void addListener(Connection.Listener var1);

    void removeListener(Connection.Listener var1);

    void onOpen();

    void onClose();

    EndPoint getEndPoint();

    void close();

    boolean onIdleExpired();

    long getMessagesIn();

    long getMessagesOut();

    long getBytesIn();

    long getBytesOut();

    long getCreatedTimeStamp();

    public interface Listener {

        void onOpened(Connection var1);

        void onClosed(Connection var1);

        public static class Adapter implements Connection.Listener {

            @Override
            public void onOpened(Connection connection) {
            }

            @Override
            public void onClosed(Connection connection) {
            }
        }
    }

    public interface UpgradeFrom {

        ByteBuffer onUpgradeFrom();
    }

    public interface UpgradeTo {

        void onUpgradeTo(ByteBuffer var1);
    }
}