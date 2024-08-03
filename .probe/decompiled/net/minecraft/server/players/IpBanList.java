package net.minecraft.server.players;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;
import javax.annotation.Nullable;

public class IpBanList extends StoredUserList<String, IpBanListEntry> {

    public IpBanList(File file0) {
        super(file0);
    }

    @Override
    protected StoredUserEntry<String> createEntry(JsonObject jsonObject0) {
        return new IpBanListEntry(jsonObject0);
    }

    public boolean isBanned(SocketAddress socketAddress0) {
        String $$1 = this.getIpFromAddress(socketAddress0);
        return this.m_11396_($$1);
    }

    public boolean isBanned(String string0) {
        return this.m_11396_(string0);
    }

    @Nullable
    public IpBanListEntry get(SocketAddress socketAddress0) {
        String $$1 = this.getIpFromAddress(socketAddress0);
        return (IpBanListEntry) this.m_11388_($$1);
    }

    private String getIpFromAddress(SocketAddress socketAddress0) {
        String $$1 = socketAddress0.toString();
        if ($$1.contains("/")) {
            $$1 = $$1.substring($$1.indexOf(47) + 1);
        }
        if ($$1.contains(":")) {
            $$1 = $$1.substring(0, $$1.indexOf(58));
        }
        return $$1;
    }
}