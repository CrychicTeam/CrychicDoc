package net.minecraft.client.server;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.DefaultUncaughtExceptionHandler;
import org.slf4j.Logger;

public class LanServerDetection {

    static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);

    static final Logger LOGGER = LogUtils.getLogger();

    public static class LanServerDetector extends Thread {

        private final LanServerDetection.LanServerList serverList;

        private final InetAddress pingGroup;

        private final MulticastSocket socket;

        public LanServerDetector(LanServerDetection.LanServerList lanServerDetectionLanServerList0) throws IOException {
            super("LanServerDetector #" + LanServerDetection.UNIQUE_THREAD_ID.incrementAndGet());
            this.serverList = lanServerDetectionLanServerList0;
            this.setDaemon(true);
            this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LanServerDetection.LOGGER));
            this.socket = new MulticastSocket(4445);
            this.pingGroup = InetAddress.getByName("224.0.2.60");
            this.socket.setSoTimeout(5000);
            this.socket.joinGroup(this.pingGroup);
        }

        public void run() {
            byte[] $$0 = new byte[1024];
            while (!this.isInterrupted()) {
                DatagramPacket $$1 = new DatagramPacket($$0, $$0.length);
                try {
                    this.socket.receive($$1);
                } catch (SocketTimeoutException var5) {
                    continue;
                } catch (IOException var6) {
                    LanServerDetection.LOGGER.error("Couldn't ping server", var6);
                    break;
                }
                String $$4 = new String($$1.getData(), $$1.getOffset(), $$1.getLength(), StandardCharsets.UTF_8);
                LanServerDetection.LOGGER.debug("{}: {}", $$1.getAddress(), $$4);
                this.serverList.addServer($$4, $$1.getAddress());
            }
            try {
                this.socket.leaveGroup(this.pingGroup);
            } catch (IOException var4) {
            }
            this.socket.close();
        }
    }

    public static class LanServerList {

        private final List<LanServer> servers = Lists.newArrayList();

        private boolean isDirty;

        @Nullable
        public synchronized List<LanServer> takeDirtyServers() {
            if (this.isDirty) {
                List<LanServer> $$0 = List.copyOf(this.servers);
                this.isDirty = false;
                return $$0;
            } else {
                return null;
            }
        }

        public synchronized void addServer(String string0, InetAddress inetAddress1) {
            String $$2 = LanServerPinger.parseMotd(string0);
            String $$3 = LanServerPinger.parseAddress(string0);
            if ($$3 != null) {
                $$3 = inetAddress1.getHostAddress() + ":" + $$3;
                boolean $$4 = false;
                for (LanServer $$5 : this.servers) {
                    if ($$5.getAddress().equals($$3)) {
                        $$5.updatePingTime();
                        $$4 = true;
                        break;
                    }
                }
                if (!$$4) {
                    this.servers.add(new LanServer($$2, $$3));
                    this.isDirty = true;
                }
            }
        }
    }
}