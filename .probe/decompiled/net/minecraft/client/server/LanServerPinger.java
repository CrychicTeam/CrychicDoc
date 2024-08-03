package net.minecraft.client.server;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.DefaultUncaughtExceptionHandler;
import org.slf4j.Logger;

public class LanServerPinger extends Thread {

    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MULTICAST_GROUP = "224.0.2.60";

    public static final int PING_PORT = 4445;

    private static final long PING_INTERVAL = 1500L;

    private final String motd;

    private final DatagramSocket socket;

    private boolean isRunning = true;

    private final String serverAddress;

    public LanServerPinger(String string0, String string1) throws IOException {
        super("LanServerPinger #" + UNIQUE_THREAD_ID.incrementAndGet());
        this.motd = string0;
        this.serverAddress = string1;
        this.setDaemon(true);
        this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        this.socket = new DatagramSocket();
    }

    public void run() {
        String $$0 = createPingString(this.motd, this.serverAddress);
        byte[] $$1 = $$0.getBytes(StandardCharsets.UTF_8);
        while (!this.isInterrupted() && this.isRunning) {
            try {
                InetAddress $$2 = InetAddress.getByName("224.0.2.60");
                DatagramPacket $$3 = new DatagramPacket($$1, $$1.length, $$2, 4445);
                this.socket.send($$3);
            } catch (IOException var6) {
                LOGGER.warn("LanServerPinger: {}", var6.getMessage());
                break;
            }
            try {
                sleep(1500L);
            } catch (InterruptedException var5) {
            }
        }
    }

    public void interrupt() {
        super.interrupt();
        this.isRunning = false;
    }

    public static String createPingString(String string0, String string1) {
        return "[MOTD]" + string0 + "[/MOTD][AD]" + string1 + "[/AD]";
    }

    public static String parseMotd(String string0) {
        int $$1 = string0.indexOf("[MOTD]");
        if ($$1 < 0) {
            return "missing no";
        } else {
            int $$2 = string0.indexOf("[/MOTD]", $$1 + "[MOTD]".length());
            return $$2 < $$1 ? "missing no" : string0.substring($$1 + "[MOTD]".length(), $$2);
        }
    }

    public static String parseAddress(String string0) {
        int $$1 = string0.indexOf("[/MOTD]");
        if ($$1 < 0) {
            return null;
        } else {
            int $$2 = string0.indexOf("[/MOTD]", $$1 + "[/MOTD]".length());
            if ($$2 >= 0) {
                return null;
            } else {
                int $$3 = string0.indexOf("[AD]", $$1 + "[/MOTD]".length());
                if ($$3 < 0) {
                    return null;
                } else {
                    int $$4 = string0.indexOf("[/AD]", $$3 + "[AD]".length());
                    return $$4 < $$3 ? null : string0.substring($$3 + "[AD]".length(), $$4);
                }
            }
        }
    }
}