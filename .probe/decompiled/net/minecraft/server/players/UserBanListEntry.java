package net.minecraft.server.players;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class UserBanListEntry extends BanListEntry<GameProfile> {

    public UserBanListEntry(GameProfile gameProfile0) {
        this(gameProfile0, null, null, null, null);
    }

    public UserBanListEntry(GameProfile gameProfile0, @Nullable Date date1, @Nullable String string2, @Nullable Date date3, @Nullable String string4) {
        super(gameProfile0, date1, string2, date3, string4);
    }

    public UserBanListEntry(JsonObject jsonObject0) {
        super(createGameProfile(jsonObject0), jsonObject0);
    }

    @Override
    protected void serialize(JsonObject jsonObject0) {
        if (this.m_11373_() != null) {
            jsonObject0.addProperty("uuid", ((GameProfile) this.m_11373_()).getId() == null ? "" : ((GameProfile) this.m_11373_()).getId().toString());
            jsonObject0.addProperty("name", ((GameProfile) this.m_11373_()).getName());
            super.serialize(jsonObject0);
        }
    }

    @Override
    public Component getDisplayName() {
        GameProfile $$0 = (GameProfile) this.m_11373_();
        return Component.literal($$0.getName() != null ? $$0.getName() : Objects.toString($$0.getId(), "(Unknown)"));
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