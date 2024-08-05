package net.minecraft.server.players;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserWhiteListEntry extends StoredUserEntry<GameProfile> {

    public UserWhiteListEntry(GameProfile gameProfile0) {
        super(gameProfile0);
    }

    public UserWhiteListEntry(JsonObject jsonObject0) {
        super(createGameProfile(jsonObject0));
    }

    @Override
    protected void serialize(JsonObject jsonObject0) {
        if (this.m_11373_() != null) {
            jsonObject0.addProperty("uuid", ((GameProfile) this.m_11373_()).getId() == null ? "" : ((GameProfile) this.m_11373_()).getId().toString());
            jsonObject0.addProperty("name", ((GameProfile) this.m_11373_()).getName());
        }
    }

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