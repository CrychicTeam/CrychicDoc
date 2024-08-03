package net.minecraft.world.level.storage.loot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class LootDataType<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final LootDataType<LootItemCondition> PREDICATE = new LootDataType<>(Deserializers.createConditionSerializer().create(), createSingleOrMultipleDeserialiser(LootItemCondition.class, LootDataManager::m_278755_), "predicates", createSimpleValidator());

    public static final LootDataType<LootItemFunction> MODIFIER = new LootDataType<>(Deserializers.createFunctionSerializer().create(), createSingleOrMultipleDeserialiser(LootItemFunction.class, LootDataManager::m_278704_), "item_modifiers", createSimpleValidator());

    public static final LootDataType<LootTable> TABLE = new LootDataType<>(Deserializers.createLootTableSerializer().create(), createSingleDeserialiser(LootTable.class), "loot_tables", createLootTableValidator());

    private final Gson parser;

    private final BiFunction<ResourceLocation, JsonElement, Optional<T>> topDeserializer;

    private final String directory;

    private final LootDataType.Validator<T> validator;

    private LootDataType(Gson gson0, BiFunction<Gson, String, BiFunction<ResourceLocation, JsonElement, Optional<T>>> biFunctionGsonStringBiFunctionResourceLocationJsonElementOptionalT1, String string2, LootDataType.Validator<T> lootDataTypeValidatorT3) {
        this.parser = gson0;
        this.directory = string2;
        this.validator = lootDataTypeValidatorT3;
        this.topDeserializer = (BiFunction<ResourceLocation, JsonElement, Optional<T>>) biFunctionGsonStringBiFunctionResourceLocationJsonElementOptionalT1.apply(gson0, string2);
    }

    public Gson parser() {
        return this.parser;
    }

    public String directory() {
        return this.directory;
    }

    public void runValidation(ValidationContext validationContext0, LootDataId<T> lootDataIdT1, T t2) {
        this.validator.run(validationContext0, lootDataIdT1, t2);
    }

    public Optional<T> deserialize(ResourceLocation resourceLocation0, JsonElement jsonElement1) {
        return (Optional<T>) this.topDeserializer.apply(resourceLocation0, jsonElement1);
    }

    public static Stream<LootDataType<?>> values() {
        return Stream.of(PREDICATE, MODIFIER, TABLE);
    }

    private static <T> BiFunction<Gson, String, BiFunction<ResourceLocation, JsonElement, Optional<T>>> createSingleDeserialiser(Class<T> classT0) {
        return (p_279398_, p_279358_) -> (p_279297_, p_279222_) -> {
            try {
                return Optional.of(p_279398_.fromJson(p_279222_, classT0));
            } catch (Exception var6) {
                LOGGER.error("Couldn't parse element {}:{}", new Object[] { p_279358_, p_279297_, var6 });
                return Optional.empty();
            }
        };
    }

    private static <T> BiFunction<Gson, String, BiFunction<ResourceLocation, JsonElement, Optional<T>>> createSingleOrMultipleDeserialiser(Class<T> classT0, Function<T[], T> functionTT1) {
        Class<T[]> $$2 = classT0.arrayType();
        return (p_279462_, p_279351_) -> (p_279495_, p_279409_) -> {
            try {
                if (p_279409_.isJsonArray()) {
                    T[] $$7 = (T[]) ((Object[]) p_279462_.fromJson(p_279409_, $$2));
                    return Optional.of(functionTT1.apply($$7));
                } else {
                    return Optional.of(p_279462_.fromJson(p_279409_, classT0));
                }
            } catch (Exception var8) {
                LOGGER.error("Couldn't parse element {}:{}", new Object[] { p_279351_, p_279495_, var8 });
                return Optional.empty();
            }
        };
    }

    private static <T extends LootContextUser> LootDataType.Validator<T> createSimpleValidator() {
        return (p_279353_, p_279374_, p_279097_) -> p_279097_.validate(p_279353_.enterElement("{" + p_279374_.type().directory + ":" + p_279374_.location() + "}", p_279374_));
    }

    private static LootDataType.Validator<LootTable> createLootTableValidator() {
        return (p_279333_, p_279227_, p_279406_) -> p_279406_.validate(p_279333_.setParams(p_279406_.getParamSet()).enterElement("{" + p_279227_.type().directory + ":" + p_279227_.location() + "}", p_279227_));
    }

    @FunctionalInterface
    public interface Validator<T> {

        void run(ValidationContext var1, LootDataId<T> var2, T var3);
    }
}