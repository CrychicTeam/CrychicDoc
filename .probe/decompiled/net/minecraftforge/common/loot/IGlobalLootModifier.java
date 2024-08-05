package net.minecraftforge.common.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

public interface IGlobalLootModifier {

    Codec<IGlobalLootModifier> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> ((IForgeRegistry) ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get()).getCodec()).dispatch(IGlobalLootModifier::codec, Function.identity());

    Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = Codec.PASSTHROUGH.flatXmap(d -> {
        try {
            LootItemCondition[] conditions = (LootItemCondition[]) LootModifierManager.GSON_INSTANCE.fromJson(getJson(d), LootItemCondition[].class);
            return DataResult.success(conditions);
        } catch (JsonSyntaxException var2) {
            LootModifierManager.LOGGER.warn("Unable to decode loot conditions", var2);
            return DataResult.error(var2::getMessage);
        }
    }, conditions -> {
        try {
            JsonElement element = LootModifierManager.GSON_INSTANCE.toJsonTree(conditions);
            return DataResult.success(new Dynamic(JsonOps.INSTANCE, element));
        } catch (JsonSyntaxException var2) {
            LootModifierManager.LOGGER.warn("Unable to encode loot conditions", var2);
            return DataResult.error(var2::getMessage);
        }
    });

    static <U> JsonElement getJson(Dynamic<?> dynamic) {
        return dynamic.getValue() instanceof JsonElement ? (JsonElement) dynamic.getValue() : (JsonElement) dynamic.getOps().convertTo(JsonOps.INSTANCE, dynamic.getValue());
    }

    @NotNull
    ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> var1, LootContext var2);

    Codec<? extends IGlobalLootModifier> codec();
}