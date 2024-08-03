package de.keksuccino.fancymenu.util.minecraftuser.v2;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class UserProfile {

    protected String id;

    protected String name;

    @Nullable
    public UUID getUUID() {
        return this.id == null ? null : UUID.fromString(this.id.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

    @Nullable
    public String getName() {
        return this.name;
    }
}