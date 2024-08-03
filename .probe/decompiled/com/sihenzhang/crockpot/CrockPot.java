package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.base.CrockPotSoundEvents;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.inventory.CrockPotMenuTypes;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.loot.CrockPotLootModifiers;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("crockpot")
public final class CrockPot {

    public static final String MOD_ID = "crockpot";

    public static final String MOD_NAME = "Crock Pot";

    public static final Logger LOGGER = LoggerFactory.getLogger("Crock Pot");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "crockpot");

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.crockpot")).icon(() -> CrockPotItems.CROCK_POT.get().getDefaultInstance()).displayItems((params, output) -> CrockPotItems.ITEMS.getEntries().forEach(regObj -> {
        Item item = (Item) regObj.get();
        if (item != CrockPotItems.AVAJ.get()) {
            output.accept(item);
        }
    })).build());

    public CrockPot() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(Type.COMMON, CrockPotConfigs.COMMON_CONFIG);
        modLoadingContext.registerConfig(Type.CLIENT, CrockPotConfigs.CLIENT_CONFIG);
        CREATIVE_MODE_TABS.register(modEventBus);
        CrockPotEffects.EFFECTS.register(modEventBus);
        CrockPotItems.ITEMS.register(modEventBus);
        CrockPotBlocks.BLOCKS.register(modEventBus);
        CrockPotBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        CrockPotMenuTypes.MENU_TYPES.register(modEventBus);
        CrockPotEntities.ENTITIES.register(modEventBus);
        CrockPotLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        CrockPotRecipes.RECIPE_TYPES.register(modEventBus);
        CrockPotRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        CrockPotSoundEvents.SOUND_EVENTS.register(modEventBus);
    }
}