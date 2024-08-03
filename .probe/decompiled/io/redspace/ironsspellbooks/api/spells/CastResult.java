package io.redspace.ironsspellbooks.api.spells;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class CastResult {

    public final CastResult.Type type;

    @Nullable
    public final Component message;

    public CastResult(CastResult.Type type) {
        this(type, null);
    }

    public CastResult(CastResult.Type type, Component message) {
        this.type = type;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.type == CastResult.Type.SUCCESS;
    }

    public static enum Type {

        SUCCESS, FAILURE
    }
}