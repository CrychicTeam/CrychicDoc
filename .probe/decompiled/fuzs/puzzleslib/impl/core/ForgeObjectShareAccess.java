package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.core.v1.ObjectShareAccess;
import org.jetbrains.annotations.Nullable;

public final class ForgeObjectShareAccess implements ObjectShareAccess {

    public static final ObjectShareAccess INSTANCE = new ForgeObjectShareAccess();

    private ForgeObjectShareAccess() {
    }

    @Nullable
    @Override
    public Object get(String key) {
        return null;
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Nullable
    @Override
    public Object putIfAbsent(String key, Object value) {
        return null;
    }

    @Nullable
    @Override
    public Object remove(String key) {
        return null;
    }
}