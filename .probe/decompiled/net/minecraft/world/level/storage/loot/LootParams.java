package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class LootParams {

    private final ServerLevel level;

    private final Map<LootContextParam<?>, Object> params;

    private final Map<ResourceLocation, LootParams.DynamicDrop> dynamicDrops;

    private final float luck;

    public LootParams(ServerLevel serverLevel0, Map<LootContextParam<?>, Object> mapLootContextParamObject1, Map<ResourceLocation, LootParams.DynamicDrop> mapResourceLocationLootParamsDynamicDrop2, float float3) {
        this.level = serverLevel0;
        this.params = mapLootContextParamObject1;
        this.dynamicDrops = mapResourceLocationLootParamsDynamicDrop2;
        this.luck = float3;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public boolean hasParam(LootContextParam<?> lootContextParam0) {
        return this.params.containsKey(lootContextParam0);
    }

    public <T> T getParameter(LootContextParam<T> lootContextParamT0) {
        T $$1 = (T) this.params.get(lootContextParamT0);
        if ($$1 == null) {
            throw new NoSuchElementException(lootContextParamT0.getName().toString());
        } else {
            return $$1;
        }
    }

    @Nullable
    public <T> T getOptionalParameter(LootContextParam<T> lootContextParamT0) {
        return (T) this.params.get(lootContextParamT0);
    }

    @Nullable
    public <T> T getParamOrNull(LootContextParam<T> lootContextParamT0) {
        return (T) this.params.get(lootContextParamT0);
    }

    public void addDynamicDrops(ResourceLocation resourceLocation0, Consumer<ItemStack> consumerItemStack1) {
        LootParams.DynamicDrop $$2 = (LootParams.DynamicDrop) this.dynamicDrops.get(resourceLocation0);
        if ($$2 != null) {
            $$2.add(consumerItemStack1);
        }
    }

    public float getLuck() {
        return this.luck;
    }

    public static class Builder {

        private final ServerLevel level;

        private final Map<LootContextParam<?>, Object> params = Maps.newIdentityHashMap();

        private final Map<ResourceLocation, LootParams.DynamicDrop> dynamicDrops = Maps.newHashMap();

        private float luck;

        public Builder(ServerLevel serverLevel0) {
            this.level = serverLevel0;
        }

        public ServerLevel getLevel() {
            return this.level;
        }

        public <T> LootParams.Builder withParameter(LootContextParam<T> lootContextParamT0, T t1) {
            this.params.put(lootContextParamT0, t1);
            return this;
        }

        public <T> LootParams.Builder withOptionalParameter(LootContextParam<T> lootContextParamT0, @Nullable T t1) {
            if (t1 == null) {
                this.params.remove(lootContextParamT0);
            } else {
                this.params.put(lootContextParamT0, t1);
            }
            return this;
        }

        public <T> T getParameter(LootContextParam<T> lootContextParamT0) {
            T $$1 = (T) this.params.get(lootContextParamT0);
            if ($$1 == null) {
                throw new NoSuchElementException(lootContextParamT0.getName().toString());
            } else {
                return $$1;
            }
        }

        @Nullable
        public <T> T getOptionalParameter(LootContextParam<T> lootContextParamT0) {
            return (T) this.params.get(lootContextParamT0);
        }

        public LootParams.Builder withDynamicDrop(ResourceLocation resourceLocation0, LootParams.DynamicDrop lootParamsDynamicDrop1) {
            LootParams.DynamicDrop $$2 = (LootParams.DynamicDrop) this.dynamicDrops.put(resourceLocation0, lootParamsDynamicDrop1);
            if ($$2 != null) {
                throw new IllegalStateException("Duplicated dynamic drop '" + this.dynamicDrops + "'");
            } else {
                return this;
            }
        }

        public LootParams.Builder withLuck(float float0) {
            this.luck = float0;
            return this;
        }

        public LootParams create(LootContextParamSet lootContextParamSet0) {
            Set<LootContextParam<?>> $$1 = Sets.difference(this.params.keySet(), lootContextParamSet0.getAllowed());
            if (!$$1.isEmpty()) {
                throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + $$1);
            } else {
                Set<LootContextParam<?>> $$2 = Sets.difference(lootContextParamSet0.getRequired(), this.params.keySet());
                if (!$$2.isEmpty()) {
                    throw new IllegalArgumentException("Missing required parameters: " + $$2);
                } else {
                    return new LootParams(this.level, this.params, this.dynamicDrops, this.luck);
                }
            }
        }
    }

    @FunctionalInterface
    public interface DynamicDrop {

        void add(Consumer<ItemStack> var1);
    }
}