package fuzs.puzzleslib.impl.capability.data;

import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class ForgeCapabilityKey<C extends CapabilityComponent> implements CapabilityKey<C> {

    private final ResourceLocation id;

    private final Class<C> componentClass;

    private final ForgeCapabilityKey.CapabilityTokenFactory<C> factory;

    private Capability<C> capability;

    public ForgeCapabilityKey(ResourceLocation id, Class<C> componentClass, ForgeCapabilityKey.CapabilityTokenFactory<C> factory) {
        this.id = id;
        this.componentClass = componentClass;
        this.factory = factory;
        CapabilityController.submit(this);
    }

    public void createCapability(CapabilityToken<C> token) {
        this.capability = this.factory.apply(token);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public Class<C> getComponentClass() {
        return this.componentClass;
    }

    @Nullable
    @Override
    public <V> C get(@Nullable V provider) {
        this.validateCapability();
        if (provider instanceof ICapabilityProvider capabilityProvider) {
            LazyOptional<C> optional = capabilityProvider.getCapability(this.capability);
            if (optional.isPresent()) {
                return optional.orElseThrow(IllegalStateException::new);
            }
        }
        return null;
    }

    @Override
    public <V> Optional<C> maybeGet(@Nullable V provider) {
        this.validateCapability();
        return provider instanceof ICapabilityProvider capabilityProvider ? capabilityProvider.getCapability(this.capability).resolve() : Optional.empty();
    }

    void validateCapability() {
        Objects.requireNonNull(this.capability, "No valid capability implementation registered for %s".formatted(this.id));
    }

    public interface CapabilityTokenFactory<C extends CapabilityComponent> {

        Capability<C> apply(CapabilityToken<C> var1);
    }

    public interface ForgeCapabilityKeyFactory<C extends CapabilityComponent, T extends CapabilityKey<C>> {

        T apply(ResourceLocation var1, Class<C> var2, ForgeCapabilityKey.CapabilityTokenFactory<C> var3);
    }
}