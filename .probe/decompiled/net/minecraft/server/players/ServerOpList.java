package net.minecraft.server.players;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Objects;

public class ServerOpList extends StoredUserList<GameProfile, ServerOpListEntry> {

    public ServerOpList(File file0) {
        super(file0);
    }

    @Override
    protected StoredUserEntry<GameProfile> createEntry(JsonObject jsonObject0) {
        return new ServerOpListEntry(jsonObject0);
    }

    @Override
    public String[] getUserList() {
        return (String[]) this.m_11395_().stream().map(StoredUserEntry::m_11373_).filter(Objects::nonNull).map(GameProfile::getName).toArray(String[]::new);
    }

    public boolean canBypassPlayerLimit(GameProfile gameProfile0) {
        ServerOpListEntry $$1 = (ServerOpListEntry) this.m_11388_(gameProfile0);
        return $$1 != null ? $$1.getBypassesPlayerLimit() : false;
    }

    protected String getKeyForUser(GameProfile gameProfile0) {
        return gameProfile0.getId().toString();
    }
}