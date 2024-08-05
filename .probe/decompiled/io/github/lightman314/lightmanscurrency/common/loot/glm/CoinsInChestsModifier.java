package io.github.lightman314.lightmanscurrency.common.loot.glm;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.loot.LootManager;
import io.github.lightman314.lightmanscurrency.common.loot.tiers.ChestPoolLevel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class CoinsInChestsModifier implements IGlobalLootModifier {

    private CoinsInChestsModifier() {
        LightmansCurrency.LogInfo("CoinsInChestModifier was deserialized!");
    }

    @Nonnull
    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!LCConfig.COMMON.enableChestLoot.get()) {
            return generatedLoot;
        } else {
            String lootTable = context.getQueriedLootTableId().toString();
            ChestPoolLevel lootLevel = LootManager.GetChestPoolLevel(lootTable);
            if (lootLevel != null) {
                LightmansCurrency.LogDebug("Loot table '" + lootTable + "' has " + lootLevel + " level chest loot. Adding coins to the spawned loot.");
                for (ItemStack coin : LootManager.getLoot(lootLevel.lootTable, context)) {
                    LightmansCurrency.LogDebug("Adding " + coin.getCount() + "x " + ForgeRegistries.ITEMS.getKey(coin.getItem()).toString() + " to the chest loot.");
                    generatedLoot.add(coin);
                }
            }
            return generatedLoot;
        }
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return new CoinsInChestsModifier.Serializer();
    }

    public static class Serializer implements Codec<CoinsInChestsModifier> {

        public <T> DataResult<T> encode(CoinsInChestsModifier input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(prefix);
        }

        public <T> DataResult<Pair<CoinsInChestsModifier, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(Pair.of(new CoinsInChestsModifier(), input));
        }
    }
}