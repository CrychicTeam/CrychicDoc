package net.minecraftforge.common.capabilities;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface ICapabilityProviderImpl<B extends ICapabilityProviderImpl<B>> extends ICapabilityProvider {

    boolean areCapsCompatible(CapabilityProvider<B> var1);

    boolean areCapsCompatible(@Nullable CapabilityDispatcher var1);

    void invalidateCaps();

    void reviveCaps();
}