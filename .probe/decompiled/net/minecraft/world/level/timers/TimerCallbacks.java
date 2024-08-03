package net.minecraft.world.level.timers;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

public class TimerCallbacks<C> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final TimerCallbacks<MinecraftServer> SERVER_CALLBACKS = new TimerCallbacks<MinecraftServer>().register(new FunctionCallback.Serializer()).register(new FunctionTagCallback.Serializer());

    private final Map<ResourceLocation, TimerCallback.Serializer<C, ?>> idToSerializer = Maps.newHashMap();

    private final Map<Class<?>, TimerCallback.Serializer<C, ?>> classToSerializer = Maps.newHashMap();

    public TimerCallbacks<C> register(TimerCallback.Serializer<C, ?> timerCallbackSerializerC0) {
        this.idToSerializer.put(timerCallbackSerializerC0.getId(), timerCallbackSerializerC0);
        this.classToSerializer.put(timerCallbackSerializerC0.getCls(), timerCallbackSerializerC0);
        return this;
    }

    private <T extends TimerCallback<C>> TimerCallback.Serializer<C, T> getSerializer(Class<?> class0) {
        return (TimerCallback.Serializer<C, T>) this.classToSerializer.get(class0);
    }

    public <T extends TimerCallback<C>> CompoundTag serialize(T t0) {
        TimerCallback.Serializer<C, T> $$1 = this.getSerializer(t0.getClass());
        CompoundTag $$2 = new CompoundTag();
        $$1.serialize($$2, t0);
        $$2.putString("Type", $$1.getId().toString());
        return $$2;
    }

    @Nullable
    public TimerCallback<C> deserialize(CompoundTag compoundTag0) {
        ResourceLocation $$1 = ResourceLocation.tryParse(compoundTag0.getString("Type"));
        TimerCallback.Serializer<C, ?> $$2 = (TimerCallback.Serializer<C, ?>) this.idToSerializer.get($$1);
        if ($$2 == null) {
            LOGGER.error("Failed to deserialize timer callback: {}", compoundTag0);
            return null;
        } else {
            try {
                return $$2.deserialize(compoundTag0);
            } catch (Exception var5) {
                LOGGER.error("Failed to deserialize timer callback: {}", compoundTag0, var5);
                return null;
            }
        }
    }
}