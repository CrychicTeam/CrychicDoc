package dev.xkmc.l2library.capability.player;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilityHolder<T extends PlayerCapabilityTemplate<T>> extends GeneralCapabilityHolder<Player, T> {

    public static final Map<ResourceLocation, PlayerCapabilityHolder<?>> INTERNAL_MAP = new ConcurrentHashMap();

    public final PlayerCapabilityNetworkHandler<T> network;

    @OnlyIn(Dist.CLIENT)
    private CompoundTag revive_cache;

    public PlayerCapabilityHolder(ResourceLocation id, Capability<T> capability, Class<T> cls, Supplier<T> sup, Function<PlayerCapabilityHolder<T>, PlayerCapabilityNetworkHandler<T>> network) {
        super(id, capability, cls, sup, Player.class, e -> true);
        this.network = (PlayerCapabilityNetworkHandler<T>) network.apply(this);
        INTERNAL_MAP.put(id, this);
    }

    public T get(Player e) {
        LazyOptional<T> lazyCap = e.getCapability(this.capability);
        T data;
        if (lazyCap.resolve().isEmpty()) {
            e.reviveCaps();
            data = (T) ((PlayerCapabilityTemplate) e.getCapability(this.capability).resolve().get()).check();
            e.invalidateCaps();
        } else {
            data = (T) ((PlayerCapabilityTemplate) lazyCap.resolve().get()).check();
        }
        return data;
    }

    @OnlyIn(Dist.CLIENT)
    public void cacheSet(CompoundTag tag, boolean force) {
        AbstractClientPlayer pl = Proxy.getClientPlayer();
        if (!force && pl != null && pl.getCapability(this.capability).cast().resolve().isPresent()) {
            T m = this.get(pl);
            m.preInject();
            Wrappers.run(() -> TagCodec.fromTag(tag, this.holder_class, m, f -> true));
            m.init();
        } else {
            this.revive_cache = tag;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public CompoundTag getCache(Player pl) {
        CompoundTag tag = this.revive_cache;
        this.revive_cache = null;
        if (tag == null) {
            tag = TagCodec.toTag(new CompoundTag(), this.get(pl));
        }
        return tag;
    }

    public PlayerCapabilitySerializer<T> generateSerializer(Player player) {
        return new PlayerCapabilitySerializer<>(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateTracked(CompoundTag tag, @Nullable Player pl) {
        if (pl instanceof RemotePlayer player) {
            if (player.getCapability(this.capability).cast().resolve().isPresent()) {
                T m = this.get(player);
                m.preInject();
                Wrappers.run(() -> TagCodec.fromTag(tag, this.holder_class, m, SerialField::toTracking));
                m.init();
            }
        }
    }
}