package io.github.apace100.origins.registry;

import io.github.apace100.origins.util.OriginLootCondition;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.RegistryObject;

public class ModLoot {

    public static final RegistryObject<LootItemConditionType> ORIGIN_LOOT_CONDITION = registerLootCondition("origin", new OriginLootCondition.Serializer());

    private static RegistryObject<LootItemConditionType> registerLootCondition(String path, Serializer<? extends LootItemCondition> serializer) {
        return OriginRegisters.LOOT_CONDITION_TYPES.register(path, () -> new LootItemConditionType(serializer));
    }

    public static void register() {
    }
}