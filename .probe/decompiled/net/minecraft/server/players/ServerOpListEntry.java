package net.minecraft.server.players;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import javax.annotation.Nullable;

public class ServerOpListEntry extends StoredUserEntry<GameProfile> {

    private final int level;

    private final boolean bypassesPlayerLimit;

    public ServerOpListEntry(GameProfile gameProfile0, int int1, boolean boolean2) {
        super(gameProfile0);
        this.level = int1;
        this.bypassesPlayerLimit = boolean2;
    }

    public ServerOpListEntry(JsonObject jsonObject0) {
        super(createGameProfile(jsonObject0));
        this.level = jsonObject0.has("level") ? jsonObject0.get("level").getAsInt() : 0;
        this.bypassesPlayerLimit = jsonObject0.has("bypassesPlayerLimit") && jsonObject0.get("bypassesPlayerLimit").getAsBoolean();
    }

    public int getLevel() {
        return this.level;
    }

    public boolean getBypassesPlayerLimit() {
        return this.bypassesPlayerLimit;
    }

    @Override
    protected void serialize(JsonObject jsonObject0) {
        if (this.m_11373_() != null) {
            jsonObject0.addProperty("uuid", ((GameProfile) this.m_11373_()).getId() == null ? "" : ((GameProfile) this.m_11373_()).getId().toString());
            jsonObject0.addProperty("name", ((GameProfile) this.m_11373_()).getName());
            jsonObject0.addProperty("level", this.level);
            jsonObject0.addProperty("bypassesPlayerLimit", this.bypassesPlayerLimit);
        }
    }

    @Nullable
    private static GameProfile createGameProfile(JsonObject jsonObject0) {
        if (jsonObject0.has("uuid") && jsonObject0.has("name")) {
            String $$1 = jsonObject0.get("uuid").getAsString();
            UUID $$2;
            try {
                $$2 = UUID.fromString($$1);
            } catch (Throwable var4) {
                return null;
            }
            return new GameProfile($$2, jsonObject0.get("name").getAsString());
        } else {
            return null;
        }
    }
}