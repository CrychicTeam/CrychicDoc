package net.minecraft.world.level.gameevent;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface PositionSourceType<T extends PositionSource> {

    PositionSourceType<BlockPositionSource> BLOCK = register("block", new BlockPositionSource.Type());

    PositionSourceType<EntityPositionSource> ENTITY = register("entity", new EntityPositionSource.Type());

    T read(FriendlyByteBuf var1);

    void write(FriendlyByteBuf var1, T var2);

    Codec<T> codec();

    static <S extends PositionSourceType<T>, T extends PositionSource> S register(String string0, S s1) {
        return Registry.register(BuiltInRegistries.POSITION_SOURCE_TYPE, string0, s1);
    }

    static PositionSource fromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        ResourceLocation $$1 = friendlyByteBuf0.readResourceLocation();
        return ((PositionSourceType) BuiltInRegistries.POSITION_SOURCE_TYPE.getOptional($$1).orElseThrow(() -> new IllegalArgumentException("Unknown position source type " + $$1))).read(friendlyByteBuf0);
    }

    static <T extends PositionSource> void toNetwork(T t0, FriendlyByteBuf friendlyByteBuf1) {
        friendlyByteBuf1.writeResourceLocation(BuiltInRegistries.POSITION_SOURCE_TYPE.getKey(t0.getType()));
        ((PositionSourceType<T>) t0.getType()).write(friendlyByteBuf1, t0);
    }
}