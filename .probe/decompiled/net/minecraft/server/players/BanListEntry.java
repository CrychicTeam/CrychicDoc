package net.minecraft.server.players;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public abstract class BanListEntry<T> extends StoredUserEntry<T> {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);

    public static final String EXPIRES_NEVER = "forever";

    protected final Date created;

    protected final String source;

    @Nullable
    protected final Date expires;

    protected final String reason;

    public BanListEntry(T t0, @Nullable Date date1, @Nullable String string2, @Nullable Date date3, @Nullable String string4) {
        super(t0);
        this.created = date1 == null ? new Date() : date1;
        this.source = string2 == null ? "(Unknown)" : string2;
        this.expires = date3;
        this.reason = string4 == null ? "Banned by an operator." : string4;
    }

    protected BanListEntry(T t0, JsonObject jsonObject1) {
        super(t0);
        Date $$2;
        try {
            $$2 = jsonObject1.has("created") ? DATE_FORMAT.parse(jsonObject1.get("created").getAsString()) : new Date();
        } catch (ParseException var7) {
            $$2 = new Date();
        }
        this.created = $$2;
        this.source = jsonObject1.has("source") ? jsonObject1.get("source").getAsString() : "(Unknown)";
        Date $$5;
        try {
            $$5 = jsonObject1.has("expires") ? DATE_FORMAT.parse(jsonObject1.get("expires").getAsString()) : null;
        } catch (ParseException var6) {
            $$5 = null;
        }
        this.expires = $$5;
        this.reason = jsonObject1.has("reason") ? jsonObject1.get("reason").getAsString() : "Banned by an operator.";
    }

    public Date getCreated() {
        return this.created;
    }

    public String getSource() {
        return this.source;
    }

    @Nullable
    public Date getExpires() {
        return this.expires;
    }

    public String getReason() {
        return this.reason;
    }

    public abstract Component getDisplayName();

    @Override
    boolean hasExpired() {
        return this.expires == null ? false : this.expires.before(new Date());
    }

    @Override
    protected void serialize(JsonObject jsonObject0) {
        jsonObject0.addProperty("created", DATE_FORMAT.format(this.created));
        jsonObject0.addProperty("source", this.source);
        jsonObject0.addProperty("expires", this.expires == null ? "forever" : DATE_FORMAT.format(this.expires));
        jsonObject0.addProperty("reason", this.reason);
    }
}