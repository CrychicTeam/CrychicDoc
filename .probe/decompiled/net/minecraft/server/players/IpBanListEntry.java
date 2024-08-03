package net.minecraft.server.players;

import com.google.gson.JsonObject;
import java.util.Date;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class IpBanListEntry extends BanListEntry<String> {

    public IpBanListEntry(String string0) {
        this(string0, null, null, null, null);
    }

    public IpBanListEntry(String string0, @Nullable Date date1, @Nullable String string2, @Nullable Date date3, @Nullable String string4) {
        super(string0, date1, string2, date3, string4);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(String.valueOf(this.m_11373_()));
    }

    public IpBanListEntry(JsonObject jsonObject0) {
        super(createIpInfo(jsonObject0), jsonObject0);
    }

    private static String createIpInfo(JsonObject jsonObject0) {
        return jsonObject0.has("ip") ? jsonObject0.get("ip").getAsString() : null;
    }

    @Override
    protected void serialize(JsonObject jsonObject0) {
        if (this.m_11373_() != null) {
            jsonObject0.addProperty("ip", (String) this.m_11373_());
            super.serialize(jsonObject0);
        }
    }
}