package io.github.edwinmindcraft.origins.common.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.badge.BadgeFactory;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.registry.OriginsBuiltinRegistries;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class OriginRegisters {

    public static final DeferredRegister<PowerFactory<?>> POWER_FACTORIES = DeferredRegister.create(ApoliRegistries.POWER_FACTORY_KEY, "origins");

    public static final DeferredRegister<Origin> ORIGINS = DeferredRegister.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, "origins");

    public static final DeferredRegister<BadgeFactory> BADGE_FACTORIES = DeferredRegister.create(OriginsBuiltinRegistries.BADGE_FACTORY_KEY, "origins");

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "origins");

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "origins");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "origins");

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "origins");

    public static final DeferredRegister<EntityCondition<?>> ENTITY_CONDITIONS = DeferredRegister.create(ApoliRegistries.ENTITY_CONDITION_KEY, "origins");

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "origins");

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, "origins");

    public static final RegistryObject<Origin> EMPTY = ORIGINS.register("empty", () -> Origin.EMPTY);

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        OriginsBuiltinRegistries.ORIGINS = ORIGINS.makeRegistry(() -> new RegistryBuilder().setDefaultKey(Origins.identifier("empty")));
        OriginsBuiltinRegistries.BADGE_FACTORIES = BADGE_FACTORIES.makeRegistry(RegistryBuilder::new);
        POWER_FACTORIES.register(bus);
        ORIGINS.register(bus);
        ENTITY_TYPES.register(bus);
        ITEMS.register(bus);
        BLOCKS.register(bus);
        ENCHANTMENTS.register(bus);
        ENTITY_CONDITIONS.register(bus);
        BADGE_FACTORIES.register(bus);
        LOOT_CONDITION_TYPES.register(bus);
        ARGUMENT_TYPES.register(bus);
    }
}