package snownee.loquat.core;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.client.LoquatClient;
import snownee.loquat.core.area.Area;
import snownee.loquat.duck.LoquatServerPlayer;

public class RestrictInstance {

    @Nullable
    private RestrictInstance fallback;

    @Nullable
    private Object2IntMap<Area> rules;

    public static RestrictInstance of(Player player) {
        if (player instanceof LoquatServerPlayer) {
            return ((LoquatServerPlayer) player).loquat$getRestrictionInstance();
        } else if (player.m_9236_().isClientSide && Minecraft.getInstance().player == player) {
            return LoquatClient.get().restrictInstance;
        } else {
            throw new IllegalArgumentException("Unknown player type: " + player);
        }
    }

    public static RestrictInstance of(ServerLevel level, String player) {
        AreaManager manager = AreaManager.of(level);
        return player.equals("*") ? manager.getFallbackRestriction() : manager.getOrCreateRestrictInstance(player);
    }

    public void restrict(Area area, RestrictInstance.RestrictBehavior behavior, boolean restricted) {
        if (this.rules == null) {
            this.rules = new Object2IntLinkedOpenHashMap();
        }
        this.rules.computeInt(area, (k, v) -> {
            int flags = v == null ? 0 : v;
            if (restricted) {
                flags |= 1 << behavior.ordinal();
            } else {
                flags &= ~(1 << behavior.ordinal());
            }
            return flags == 0 ? null : flags;
        });
    }

    private int getFlags(Area area) {
        if (this.rules != null && this.rules.containsKey(area)) {
            return this.rules.getInt(area);
        } else {
            return this.fallback == null ? 0 : this.fallback.getFlags(area);
        }
    }

    public boolean isRestricted(Area area, RestrictInstance.RestrictBehavior behavior) {
        return (this.getFlags(area) & 1 << behavior.ordinal()) != 0;
    }

    public boolean isRestricted(BlockPos pos, RestrictInstance.RestrictBehavior behavior) {
        return this.areaStream().anyMatch(area -> this.isRestricted(area, RestrictInstance.RestrictBehavior.PLACE) && area.contains(pos));
    }

    public Optional<ListTag> serializeNBT(AreaManager manager) {
        if (this.rules != null && !this.rules.isEmpty()) {
            ListTag listTag = new ListTag();
            this.rules.forEach((area, flags) -> {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("UUID", area.getUuid());
                tag.putInt("Flags", flags);
                listTag.add(tag);
            });
            return Optional.of(listTag);
        } else {
            return Optional.empty();
        }
    }

    public void deserializeNBT(AreaManager manager, ListTag listTag) {
        if (this.rules == null) {
            this.rules = new Object2IntOpenHashMap(listTag.size());
        } else {
            this.rules.clear();
        }
        listTag.forEach(rawtag -> {
            CompoundTag tag = (CompoundTag) rawtag;
            Area area = manager.get(tag.getUUID("UUID"));
            if (area != null) {
                this.rules.put(area, tag.getInt("Flags"));
            }
        });
    }

    public Stream<Area> areaStream() {
        return Stream.concat(this.rules == null ? Stream.empty() : this.rules.keySet().stream(), this.fallback != null && this.fallback.rules != null ? this.fallback.rules.keySet().stream() : Stream.empty()).distinct();
    }

    public void forEachRules(BiConsumer<Area, Integer> consumer) {
        this.areaStream().forEach(area -> consumer.accept(area, this.getFlags(area)));
    }

    public void resetForClient() {
        if (this.rules == null) {
            this.rules = new Object2IntLinkedOpenHashMap();
        } else {
            this.rules.clear();
        }
    }

    public boolean removeArea(Area area) {
        return this.rules == null ? false : this.rules.removeInt(area) != 0;
    }

    public boolean isEmpty() {
        return (this.rules == null || this.rules.isEmpty()) && (this.fallback == null || this.fallback.isEmpty());
    }

    public void setFallback(@Nullable RestrictInstance fallback) {
        this.fallback = fallback;
    }

    @Nullable
    public Object2IntMap<Area> getRules() {
        return this.rules;
    }

    public static enum RestrictBehavior {

        ENTER("enter"), EXIT("exit"), DESTROY("destroy"), PLACE("place");

        public static final RestrictInstance.RestrictBehavior[] VALUES = values();

        public final String name;

        private RestrictBehavior(String name) {
            this.name = name;
        }

        public MutableComponent getDisplayName() {
            return Component.translatable("loquat.restrict.behavior." + this.name);
        }

        public MutableComponent getNotificationMessage() {
            return Component.translatable("loquat.restrict.behavior." + this.name + ".notification");
        }
    }
}