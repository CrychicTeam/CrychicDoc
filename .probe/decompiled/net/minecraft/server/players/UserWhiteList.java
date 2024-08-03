package net.minecraft.server.players;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Objects;

public class UserWhiteList extends StoredUserList<GameProfile, UserWhiteListEntry> {

    public UserWhiteList(File file0) {
        super(file0);
    }

    @Override
    protected StoredUserEntry<GameProfile> createEntry(JsonObject jsonObject0) {
        return new UserWhiteListEntry(jsonObject0);
    }

    public boolean isWhiteListed(GameProfile gameProfile0) {
        return this.m_11396_(gameProfile0);
    }

    @Override
    public String[] getUserList() {
        return (String[]) this.m_11395_().stream().map(StoredUserEntry::m_11373_).filter(Objects::nonNull).map(GameProfile::getName).toArray(String[]::new);
    }

    protected String getKeyForUser(GameProfile gameProfile0) {
        return gameProfile0.getId().toString();
    }
}