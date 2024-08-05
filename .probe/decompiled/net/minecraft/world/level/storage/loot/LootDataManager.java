package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.slf4j.Logger;

public class LootDataManager implements PreparableReloadListener, LootDataResolver {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final LootDataId<LootTable> EMPTY_LOOT_TABLE_KEY = new LootDataId<>(LootDataType.TABLE, BuiltInLootTables.EMPTY);

    private Map<LootDataId<?>, ?> elements = Map.of();

    private Multimap<LootDataType<?>, ResourceLocation> typeKeys = ImmutableMultimap.of();

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        Map<LootDataType<?>, Map<ResourceLocation, ?>> $$6 = new HashMap();
        CompletableFuture<?>[] $$7 = (CompletableFuture<?>[]) LootDataType.values().map(p_279242_ -> scheduleElementParse(p_279242_, resourceManager1, executor4, $$6)).toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf($$7).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_279096_ -> this.apply($$6), executor5);
    }

    private static <T> CompletableFuture<?> scheduleElementParse(LootDataType<T> lootDataTypeT0, ResourceManager resourceManager1, Executor executor2, Map<LootDataType<?>, Map<ResourceLocation, ?>> mapLootDataTypeMapResourceLocation3) {
        Map<ResourceLocation, T> $$4 = new HashMap();
        mapLootDataTypeMapResourceLocation3.put(lootDataTypeT0, $$4);
        return CompletableFuture.runAsync(() -> {
            Map<ResourceLocation, JsonElement> $$3 = new HashMap();
            SimpleJsonResourceReloadListener.scanDirectory(resourceManager1, lootDataTypeT0.directory(), lootDataTypeT0.parser(), $$3);
            $$3.forEach((p_279416_, p_279151_) -> lootDataTypeT0.deserialize(p_279416_, p_279151_).ifPresent(p_279295_ -> $$4.put(p_279416_, p_279295_)));
        }, executor2);
    }

    private void apply(Map<LootDataType<?>, Map<ResourceLocation, ?>> mapLootDataTypeMapResourceLocation0) {
        Object $$1 = ((Map) mapLootDataTypeMapResourceLocation0.get(LootDataType.TABLE)).remove(BuiltInLootTables.EMPTY);
        if ($$1 != null) {
            LOGGER.warn("Datapack tried to redefine {} loot table, ignoring", BuiltInLootTables.EMPTY);
        }
        Builder<LootDataId<?>, Object> $$2 = ImmutableMap.builder();
        com.google.common.collect.ImmutableMultimap.Builder<LootDataType<?>, ResourceLocation> $$3 = ImmutableMultimap.builder();
        mapLootDataTypeMapResourceLocation0.forEach((p_279449_, p_279262_) -> p_279262_.forEach((p_279130_, p_279313_) -> {
            $$2.put(new LootDataId(p_279449_, p_279130_), p_279313_);
            $$3.put(p_279449_, p_279130_);
        }));
        $$2.put(EMPTY_LOOT_TABLE_KEY, LootTable.EMPTY);
        final Map<LootDataId<?>, ?> $$4 = $$2.build();
        ValidationContext $$5 = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {

            @Nullable
            @Override
            public <T> T getElement(LootDataId<T> p_279194_) {
                return (T) $$4.get(p_279194_);
            }
        });
        $$4.forEach((p_279387_, p_279087_) -> castAndValidate($$5, p_279387_, p_279087_));
        $$5.getProblems().forEach((p_279487_, p_279312_) -> LOGGER.warn("Found loot table element validation problem in {}: {}", p_279487_, p_279312_));
        this.elements = $$4;
        this.typeKeys = $$3.build();
    }

    private static <T> void castAndValidate(ValidationContext validationContext0, LootDataId<T> lootDataIdT1, Object object2) {
        lootDataIdT1.type().runValidation(validationContext0, lootDataIdT1, (T) object2);
    }

    @Nullable
    @Override
    public <T> T getElement(LootDataId<T> lootDataIdT0) {
        return (T) this.elements.get(lootDataIdT0);
    }

    public Collection<ResourceLocation> getKeys(LootDataType<?> lootDataType0) {
        return this.typeKeys.get(lootDataType0);
    }

    public static LootItemCondition createComposite(LootItemCondition[] lootItemCondition0) {
        return new LootDataManager.CompositePredicate(lootItemCondition0);
    }

    public static LootItemFunction createComposite(LootItemFunction[] lootItemFunction0) {
        return new LootDataManager.FunctionSequence(lootItemFunction0);
    }

    static class CompositePredicate implements LootItemCondition {

        private final LootItemCondition[] terms;

        private final Predicate<LootContext> composedPredicate;

        CompositePredicate(LootItemCondition[] lootItemCondition0) {
            this.terms = lootItemCondition0;
            this.composedPredicate = LootItemConditions.andConditions(lootItemCondition0);
        }

        public final boolean test(LootContext lootContext0) {
            return this.composedPredicate.test(lootContext0);
        }

        @Override
        public void validate(ValidationContext validationContext0) {
            LootItemCondition.super.m_6169_(validationContext0);
            for (int $$1 = 0; $$1 < this.terms.length; $$1++) {
                this.terms[$$1].m_6169_(validationContext0.forChild(".term[" + $$1 + "]"));
            }
        }

        @Override
        public LootItemConditionType getType() {
            throw new UnsupportedOperationException();
        }
    }

    static class FunctionSequence implements LootItemFunction {

        protected final LootItemFunction[] functions;

        private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

        public FunctionSequence(LootItemFunction[] lootItemFunction0) {
            this.functions = lootItemFunction0;
            this.compositeFunction = LootItemFunctions.compose(lootItemFunction0);
        }

        public ItemStack apply(ItemStack itemStack0, LootContext lootContext1) {
            return (ItemStack) this.compositeFunction.apply(itemStack0, lootContext1);
        }

        @Override
        public void validate(ValidationContext validationContext0) {
            LootItemFunction.super.m_6169_(validationContext0);
            for (int $$1 = 0; $$1 < this.functions.length; $$1++) {
                this.functions[$$1].m_6169_(validationContext0.forChild(".function[" + $$1 + "]"));
            }
        }

        @Override
        public LootItemFunctionType getType() {
            throw new UnsupportedOperationException();
        }
    }
}