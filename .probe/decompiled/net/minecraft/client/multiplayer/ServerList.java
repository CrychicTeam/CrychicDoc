package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.thread.ProcessorMailbox;
import org.slf4j.Logger;

public class ServerList {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ProcessorMailbox<Runnable> IO_MAILBOX = ProcessorMailbox.create(Util.backgroundExecutor(), "server-list-io");

    private static final int MAX_HIDDEN_SERVERS = 16;

    private final Minecraft minecraft;

    private final List<ServerData> serverList = Lists.newArrayList();

    private final List<ServerData> hiddenServerList = Lists.newArrayList();

    public ServerList(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void load() {
        try {
            this.serverList.clear();
            this.hiddenServerList.clear();
            CompoundTag $$0 = NbtIo.read(new File(this.minecraft.gameDirectory, "servers.dat"));
            if ($$0 == null) {
                return;
            }
            ListTag $$1 = $$0.getList("servers", 10);
            for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
                CompoundTag $$3 = $$1.getCompound($$2);
                ServerData $$4 = ServerData.read($$3);
                if ($$3.getBoolean("hidden")) {
                    this.hiddenServerList.add($$4);
                } else {
                    this.serverList.add($$4);
                }
            }
        } catch (Exception var6) {
            LOGGER.error("Couldn't load server list", var6);
        }
    }

    public void save() {
        try {
            ListTag $$0 = new ListTag();
            for (ServerData $$1 : this.serverList) {
                CompoundTag $$2 = $$1.write();
                $$2.putBoolean("hidden", false);
                $$0.add($$2);
            }
            for (ServerData $$3 : this.hiddenServerList) {
                CompoundTag $$4 = $$3.write();
                $$4.putBoolean("hidden", true);
                $$0.add($$4);
            }
            CompoundTag $$5 = new CompoundTag();
            $$5.put("servers", $$0);
            File $$6 = File.createTempFile("servers", ".dat", this.minecraft.gameDirectory);
            NbtIo.write($$5, $$6);
            File $$7 = new File(this.minecraft.gameDirectory, "servers.dat_old");
            File $$8 = new File(this.minecraft.gameDirectory, "servers.dat");
            Util.safeReplaceFile($$8, $$6, $$7);
        } catch (Exception var6) {
            LOGGER.error("Couldn't save server list", var6);
        }
    }

    public ServerData get(int int0) {
        return (ServerData) this.serverList.get(int0);
    }

    @Nullable
    public ServerData get(String string0) {
        for (ServerData $$1 : this.serverList) {
            if ($$1.ip.equals(string0)) {
                return $$1;
            }
        }
        for (ServerData $$2 : this.hiddenServerList) {
            if ($$2.ip.equals(string0)) {
                return $$2;
            }
        }
        return null;
    }

    @Nullable
    public ServerData unhide(String string0) {
        for (int $$1 = 0; $$1 < this.hiddenServerList.size(); $$1++) {
            ServerData $$2 = (ServerData) this.hiddenServerList.get($$1);
            if ($$2.ip.equals(string0)) {
                this.hiddenServerList.remove($$1);
                this.serverList.add($$2);
                return $$2;
            }
        }
        return null;
    }

    public void remove(ServerData serverData0) {
        if (!this.serverList.remove(serverData0)) {
            this.hiddenServerList.remove(serverData0);
        }
    }

    public void add(ServerData serverData0, boolean boolean1) {
        if (boolean1) {
            this.hiddenServerList.add(0, serverData0);
            while (this.hiddenServerList.size() > 16) {
                this.hiddenServerList.remove(this.hiddenServerList.size() - 1);
            }
        } else {
            this.serverList.add(serverData0);
        }
    }

    public int size() {
        return this.serverList.size();
    }

    public void swap(int int0, int int1) {
        ServerData $$2 = this.get(int0);
        this.serverList.set(int0, this.get(int1));
        this.serverList.set(int1, $$2);
        this.save();
    }

    public void replace(int int0, ServerData serverData1) {
        this.serverList.set(int0, serverData1);
    }

    private static boolean set(ServerData serverData0, List<ServerData> listServerData1) {
        for (int $$2 = 0; $$2 < listServerData1.size(); $$2++) {
            ServerData $$3 = (ServerData) listServerData1.get($$2);
            if ($$3.name.equals(serverData0.name) && $$3.ip.equals(serverData0.ip)) {
                listServerData1.set($$2, serverData0);
                return true;
            }
        }
        return false;
    }

    public static void saveSingleServer(ServerData serverData0) {
        IO_MAILBOX.tell(() -> {
            ServerList $$1 = new ServerList(Minecraft.getInstance());
            $$1.load();
            if (!set(serverData0, $$1.serverList)) {
                set(serverData0, $$1.hiddenServerList);
            }
            $$1.save();
        });
    }
}