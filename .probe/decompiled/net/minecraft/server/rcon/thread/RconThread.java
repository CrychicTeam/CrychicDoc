package net.minecraft.server.rcon.thread;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.ServerInterface;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.slf4j.Logger;

public class RconThread extends GenericThread {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ServerSocket socket;

    private final String rconPassword;

    private final List<RconClient> clients = Lists.newArrayList();

    private final ServerInterface serverInterface;

    private RconThread(ServerInterface serverInterface0, ServerSocket serverSocket1, String string2) {
        super("RCON Listener");
        this.serverInterface = serverInterface0;
        this.socket = serverSocket1;
        this.rconPassword = string2;
    }

    private void clearClients() {
        this.clients.removeIf(p_11612_ -> !p_11612_.m_11523_());
    }

    public void run() {
        try {
            while (this.f_11515_) {
                try {
                    Socket $$0 = this.socket.accept();
                    RconClient $$1 = new RconClient(this.serverInterface, this.rconPassword, $$0);
                    $$1.m_7528_();
                    this.clients.add($$1);
                    this.clearClients();
                } catch (SocketTimeoutException var7) {
                    this.clearClients();
                } catch (IOException var8) {
                    if (this.f_11515_) {
                        LOGGER.info("IO exception: ", var8);
                    }
                }
            }
        } finally {
            this.closeSocket(this.socket);
        }
    }

    @Nullable
    public static RconThread create(ServerInterface serverInterface0) {
        DedicatedServerProperties $$1 = serverInterface0.getProperties();
        String $$2 = serverInterface0.getServerIp();
        if ($$2.isEmpty()) {
            $$2 = "0.0.0.0";
        }
        int $$3 = $$1.rconPort;
        if (0 < $$3 && 65535 >= $$3) {
            String $$4 = $$1.rconPassword;
            if ($$4.isEmpty()) {
                LOGGER.warn("No rcon password set in server.properties, rcon disabled!");
                return null;
            } else {
                try {
                    ServerSocket $$5 = new ServerSocket($$3, 0, InetAddress.getByName($$2));
                    $$5.setSoTimeout(500);
                    RconThread $$6 = new RconThread(serverInterface0, $$5, $$4);
                    if (!$$6.m_7528_()) {
                        return null;
                    } else {
                        LOGGER.info("RCON running on {}:{}", $$2, $$3);
                        return $$6;
                    }
                } catch (IOException var7) {
                    LOGGER.warn("Unable to initialise RCON on {}:{}", new Object[] { $$2, $$3, var7 });
                    return null;
                }
            }
        } else {
            LOGGER.warn("Invalid rcon port {} found in server.properties, rcon disabled!", $$3);
            return null;
        }
    }

    @Override
    public void stop() {
        this.f_11515_ = false;
        this.closeSocket(this.socket);
        super.stop();
        for (RconClient $$0 : this.clients) {
            if ($$0.m_11523_()) {
                $$0.stop();
            }
        }
        this.clients.clear();
    }

    private void closeSocket(ServerSocket serverSocket0) {
        LOGGER.debug("closeSocket: {}", serverSocket0);
        try {
            serverSocket0.close();
        } catch (IOException var3) {
            LOGGER.warn("Failed to close socket", var3);
        }
    }
}