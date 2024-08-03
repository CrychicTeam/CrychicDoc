package io.github.lightman314.lightmanscurrency.common.core;

import com.mojang.serialization.Codec;
import io.github.lightman314.lightmanscurrency.ModCreativeGroups;
import io.github.lightman314.lightmanscurrency.common.crafting.RecipeTypes;
import io.github.lightman314.lightmanscurrency.common.loot.LootModifiers;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.CustomPointsOfInterest;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.CustomProfessions;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRegistries {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "lightmanscurrency");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "lightmanscurrency");

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "lightmanscurrency");

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "lightmanscurrency");

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "lightmanscurrency");

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "lightmanscurrency");

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "lightmanscurrency");

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, "lightmanscurrency");

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, "lightmanscurrency");

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "lightmanscurrency");

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "lightmanscurrency");

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.Keys.COMMAND_ARGUMENT_TYPES, "lightmanscurrency");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "lightmanscurrency");

    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, "lightmanscurrency");

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        ModItems.init();
        BLOCKS.register(bus);
        ModBlocks.init();
        BLOCK_ENTITIES.register(bus);
        ModBlockEntities.init();
        ENCHANTMENTS.register(bus);
        ModEnchantments.init();
        MENUS.register(bus);
        ModMenus.init();
        RECIPE_TYPES.register(bus);
        RecipeTypes.init();
        RECIPE_SERIALIZERS.register(bus);
        ModRecipes.init();
        PROFESSIONS.register(bus);
        CustomProfessions.init();
        POI_TYPES.register(bus);
        CustomPointsOfInterest.init();
        SOUND_EVENTS.register(bus);
        ModSounds.init();
        GLOBAL_LOOT_MODIFIERS.register(bus);
        LootModifiers.init();
        COMMAND_ARGUMENT_TYPES.register(bus);
        ModCommandArguments.init();
        CREATIVE_TABS.register(bus);
        ModCreativeGroups.init();
        LOOT_POOL_ENTRY_TYPES.register(bus);
        ModLootPoolEntryTypes.init();
    }
}