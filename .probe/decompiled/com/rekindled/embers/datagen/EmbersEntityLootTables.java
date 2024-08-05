package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

public class EmbersEntityLootTables extends EntityLootSubProvider {

    public EmbersEntityLootTables() {
        super(FeatureFlags.VANILLA_SET);
    }

    @Nonnull
    public Stream<EntityType<?>> getKnownEntityTypes() {
        return ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entity -> "embers".equals(((ResourceLocation) Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entity))).getNamespace()));
    }

    @Override
    public void generate() {
        this.m_245309_(RegistryManager.ANCIENT_GOLEM.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(RegistryManager.ARCHAIC_BRICK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 11.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(RegistryManager.ANCIENT_MOTIVE_CORE.get()))));
    }
}