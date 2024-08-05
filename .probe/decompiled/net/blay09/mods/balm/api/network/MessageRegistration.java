package net.blay09.mods.balm.api.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MessageRegistration<T> {

    private final ResourceLocation identifier;

    private final Class<T> clazz;

    private final BiConsumer<T, FriendlyByteBuf> encodeFunc;

    private final Function<FriendlyByteBuf, T> decodeFunc;

    public MessageRegistration(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc) {
        this.identifier = identifier;
        this.clazz = clazz;
        this.encodeFunc = encodeFunc;
        this.decodeFunc = decodeFunc;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public BiConsumer<T, FriendlyByteBuf> getEncodeFunc() {
        return this.encodeFunc;
    }

    public Function<FriendlyByteBuf, T> getDecodeFunc() {
        return this.decodeFunc;
    }
}