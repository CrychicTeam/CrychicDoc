package fuzs.puzzleslib.impl.network;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.puzzleslib.api.network.v3.NetworkHandlerV3;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public abstract class NetworkHandlerRegistryImpl implements NetworkHandlerV3.Builder {

    final ResourceLocation channelIdentifier;

    private final List<Class<?>> clientboundMessages = Lists.newArrayList();

    private final List<Class<?>> serverboundMessages = Lists.newArrayList();

    public boolean clientAcceptsVanillaOrMissing;

    public boolean serverAcceptsVanillaOrMissing;

    NetworkHandlerRegistryImpl(ResourceLocation channelIdentifier) {
        this.channelIdentifier = channelIdentifier;
    }

    @Override
    public <T extends Record & ClientboundMessage<T>> NetworkHandlerV3.Builder registerClientbound(Class<T> clazz) {
        if (this.clientboundMessages.contains(clazz)) {
            throw new IllegalStateException("Duplicate message of type %s".formatted(clazz));
        } else {
            this.clientboundMessages.add(clazz);
            return this;
        }
    }

    @Override
    public <T extends Record & ServerboundMessage<T>> NetworkHandlerV3.Builder registerServerbound(Class<T> clazz) {
        if (this.serverboundMessages.contains(clazz)) {
            throw new IllegalStateException("Duplicate message of type %s".formatted(clazz));
        } else {
            this.serverboundMessages.add(clazz);
            return this;
        }
    }

    @Override
    public NetworkHandlerV3.Builder clientAcceptsVanillaOrMissing() {
        this.clientAcceptsVanillaOrMissing = true;
        return this;
    }

    @Override
    public NetworkHandlerV3.Builder serverAcceptsVanillaOrMissing() {
        this.serverAcceptsVanillaOrMissing = true;
        return this;
    }

    @Override
    public void build() {
        for (Class<?> message : this.clientboundMessages) {
            this.registerClientbound$Internal(message);
        }
        for (Class<?> message : this.serverboundMessages) {
            this.registerServerbound$Internal(message);
        }
        this.clientboundMessages.clear();
        this.serverboundMessages.clear();
    }

    abstract <T extends Record & ClientboundMessage<T>> void registerClientbound$Internal(Class<?> var1);

    abstract <T extends Record & ServerboundMessage<T>> void registerServerbound$Internal(Class<?> var1);
}