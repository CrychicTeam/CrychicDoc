package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootContext {

    private final LootParams params;

    private final RandomSource random;

    private final LootDataResolver lootDataResolver;

    private final Set<LootContext.VisitedEntry<?>> visitedElements = Sets.newLinkedHashSet();

    LootContext(LootParams lootParams0, RandomSource randomSource1, LootDataResolver lootDataResolver2) {
        this.params = lootParams0;
        this.random = randomSource1;
        this.lootDataResolver = lootDataResolver2;
    }

    public boolean hasParam(LootContextParam<?> lootContextParam0) {
        return this.params.hasParam(lootContextParam0);
    }

    public <T> T getParam(LootContextParam<T> lootContextParamT0) {
        return this.params.getParameter(lootContextParamT0);
    }

    public void addDynamicDrops(ResourceLocation resourceLocation0, Consumer<ItemStack> consumerItemStack1) {
        this.params.addDynamicDrops(resourceLocation0, consumerItemStack1);
    }

    @Nullable
    public <T> T getParamOrNull(LootContextParam<T> lootContextParamT0) {
        return this.params.getParamOrNull(lootContextParamT0);
    }

    public boolean hasVisitedElement(LootContext.VisitedEntry<?> lootContextVisitedEntry0) {
        return this.visitedElements.contains(lootContextVisitedEntry0);
    }

    public boolean pushVisitedElement(LootContext.VisitedEntry<?> lootContextVisitedEntry0) {
        return this.visitedElements.add(lootContextVisitedEntry0);
    }

    public void popVisitedElement(LootContext.VisitedEntry<?> lootContextVisitedEntry0) {
        this.visitedElements.remove(lootContextVisitedEntry0);
    }

    public LootDataResolver getResolver() {
        return this.lootDataResolver;
    }

    public RandomSource getRandom() {
        return this.random;
    }

    public float getLuck() {
        return this.params.getLuck();
    }

    public ServerLevel getLevel() {
        return this.params.getLevel();
    }

    public static LootContext.VisitedEntry<LootTable> createVisitedEntry(LootTable lootTable0) {
        return new LootContext.VisitedEntry<>(LootDataType.TABLE, lootTable0);
    }

    public static LootContext.VisitedEntry<LootItemCondition> createVisitedEntry(LootItemCondition lootItemCondition0) {
        return new LootContext.VisitedEntry<>(LootDataType.PREDICATE, lootItemCondition0);
    }

    public static LootContext.VisitedEntry<LootItemFunction> createVisitedEntry(LootItemFunction lootItemFunction0) {
        return new LootContext.VisitedEntry<>(LootDataType.MODIFIER, lootItemFunction0);
    }

    public static class Builder {

        private final LootParams params;

        @Nullable
        private RandomSource random;

        public Builder(LootParams lootParams0) {
            this.params = lootParams0;
        }

        public LootContext.Builder withOptionalRandomSeed(long long0) {
            if (long0 != 0L) {
                this.random = RandomSource.create(long0);
            }
            return this;
        }

        public ServerLevel getLevel() {
            return this.params.getLevel();
        }

        public LootContext create(@Nullable ResourceLocation resourceLocation0) {
            ServerLevel $$1 = this.getLevel();
            MinecraftServer $$2 = $$1.getServer();
            RandomSource $$3;
            if (this.random != null) {
                $$3 = this.random;
            } else if (resourceLocation0 != null) {
                $$3 = $$1.getRandomSequence(resourceLocation0);
            } else {
                $$3 = $$1.m_213780_();
            }
            return new LootContext(this.params, $$3, $$2.getLootData());
        }
    }

    public static enum EntityTarget {

        THIS("this", LootContextParams.THIS_ENTITY), KILLER("killer", LootContextParams.KILLER_ENTITY), DIRECT_KILLER("direct_killer", LootContextParams.DIRECT_KILLER_ENTITY), KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER);

        final String name;

        private final LootContextParam<? extends Entity> param;

        private EntityTarget(String p_79001_, LootContextParam<? extends Entity> p_79002_) {
            this.name = p_79001_;
            this.param = p_79002_;
        }

        public LootContextParam<? extends Entity> getParam() {
            return this.param;
        }

        public static LootContext.EntityTarget getByName(String p_79007_) {
            for (LootContext.EntityTarget $$1 : values()) {
                if ($$1.name.equals(p_79007_)) {
                    return $$1;
                }
            }
            throw new IllegalArgumentException("Invalid entity target " + p_79007_);
        }

        public static class Serializer extends TypeAdapter<LootContext.EntityTarget> {

            public void write(JsonWriter jsonWriter0, LootContext.EntityTarget lootContextEntityTarget1) throws IOException {
                jsonWriter0.value(lootContextEntityTarget1.name);
            }

            public LootContext.EntityTarget read(JsonReader jsonReader0) throws IOException {
                return LootContext.EntityTarget.getByName(jsonReader0.nextString());
            }
        }
    }

    public static record VisitedEntry<T>(LootDataType<T> f_278478_, T f_278374_) {

        private final LootDataType<T> type;

        private final T value;

        public VisitedEntry(LootDataType<T> f_278478_, T f_278374_) {
            this.type = f_278478_;
            this.value = f_278374_;
        }
    }
}