package fuzs.puzzleslib.api.client.core.v1.context;

import org.jetbrains.annotations.Nullable;

public interface ColorProvidersContext<T, P> {

    void registerColorProvider(P var1, T... var2);

    @Nullable
    P getProvider(T var1);
}