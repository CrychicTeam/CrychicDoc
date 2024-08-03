package dev.xkmc.l2backpack.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2backpack.content.common.BaseBagMenu;
import dev.xkmc.l2backpack.content.recipe.BackpackDyeRecipe;
import dev.xkmc.l2backpack.content.recipe.BackpackUpgradeRecipe;
import dev.xkmc.l2backpack.content.recipe.DrawerUpgradeRecipe;
import dev.xkmc.l2backpack.content.recipe.MultiSwitchCraftRecipe;
import dev.xkmc.l2backpack.content.restore.BackpackTrace;
import dev.xkmc.l2backpack.content.restore.DimensionItemSource;
import dev.xkmc.l2backpack.content.restore.DimensionSourceData;
import dev.xkmc.l2backpack.content.restore.DimensionTrace;
import dev.xkmc.l2backpack.content.restore.DimensionTraceData;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.init.loot.BackpackLootModifier;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2library.serial.recipe.AbstractShapelessRecipe;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.l2screentracker.screen.base.ScreenTrackerRegistry;
import dev.xkmc.l2screentracker.screen.source.ItemSource;
import dev.xkmc.l2screentracker.screen.source.MenuSourceRegistry;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.l2screentracker.screen.source.SimpleSlotData;
import dev.xkmc.l2screentracker.screen.track.ItemBasedTraceData;
import dev.xkmc.l2screentracker.screen.track.MenuTraceRegistry;
import dev.xkmc.l2screentracker.screen.track.TrackedEntry;
import dev.xkmc.l2screentracker.screen.track.TrackedEntryType;
import java.util.Optional;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public class BackpackMisc {

    public static final RegistryEntry<AbstractShapelessRecipe.Serializer<BackpackDyeRecipe>> RSC_BAG_DYE = L2Backpack.REGISTRATE.simple("backpack_dye", ForgeRegistries.Keys.RECIPE_SERIALIZERS, () -> new AbstractShapelessRecipe.Serializer<>(BackpackDyeRecipe::new));

    public static final RegistryEntry<AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe>> RSC_BAG_UPGRADE = L2Backpack.REGISTRATE.simple("backpack_upgrade", ForgeRegistries.Keys.RECIPE_SERIALIZERS, () -> new AbstractSmithingRecipe.Serializer<>(BackpackUpgradeRecipe::new));

    public static final RegistryEntry<AbstractSmithingRecipe.Serializer<DrawerUpgradeRecipe>> RSC_DRAWER_UPGRADE = L2Backpack.REGISTRATE.simple("drawer_upgrade", ForgeRegistries.Keys.RECIPE_SERIALIZERS, () -> new AbstractSmithingRecipe.Serializer<>(DrawerUpgradeRecipe::new));

    public static final RegistryEntry<AbstractShapedRecipe.Serializer<MultiSwitchCraftRecipe>> RSC_BAG_CRAFT = L2Backpack.REGISTRATE.simple("multiswitch_craft", ForgeRegistries.Keys.RECIPE_SERIALIZERS, () -> new AbstractShapedRecipe.Serializer<>(MultiSwitchCraftRecipe::new));

    public static final RegistryEntry<Codec<BackpackLootModifier>> SER = L2Backpack.REGISTRATE.simple("main", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> BackpackLootModifier.CODEC);

    public static final RegistryEntry<DimensionItemSource> IS_DIM = L2Backpack.REGISTRATE.simple("dimension", ScreenTrackerRegistry.ITEM_SOURCE.key(), DimensionItemSource::new);

    public static final RegistryEntry<DimensionTrace> TE_DIM = L2Backpack.REGISTRATE.simple("dimension", ScreenTrackerRegistry.TRACKED_ENTRY_TYPE.key(), DimensionTrace::new);

    public static final RegistryEntry<BackpackTrace> TE_BAG = L2Backpack.REGISTRATE.simple("backpack", ScreenTrackerRegistry.TRACKED_ENTRY_TYPE.key(), BackpackTrace::new);

    public static void register(IEventBus bus) {
    }

    public static void commonSetup() {
        MenuSourceRegistry.register((MenuType) BackpackMenus.MT_ES.get(), (menu, slot, index, wid) -> index >= 36 && index < 63 ? Optional.of(new PlayerSlot((ItemSource) ScreenTrackerRegistry.IS_ENDER.get(), new SimpleSlotData(index - 36))) : Optional.empty());
        MenuSourceRegistry.register((MenuType) BackpackMenus.MT_WORLD_CHEST.get(), (menu, slot, index, wid) -> Optional.of(new PlayerSlot((ItemSource) IS_DIM.get(), new DimensionSourceData(menu.getColor(), index - 36, menu.getOwner()))));
        MenuTraceRegistry.register((MenuType) BackpackMenus.MT_WORLD_CHEST.get(), menu -> Optional.of(TrackedEntry.of((TrackedEntryType) TE_DIM.get(), new DimensionTraceData(menu.getColor(), menu.getOwner()))));
        addBag(BackpackMenus.MT_BACKPACK);
        addBag(BackpackMenus.MT_ARMOR);
        addBag(BackpackMenus.MT_ARROW);
        addBag(BackpackMenus.MT_TOOL);
        addBag(BackpackMenus.MT_MULTI);
        addBag(BackpackMenus.MT_ES);
    }

    private static <T extends BaseBagMenu<T>> void addBag(MenuEntry<T> type) {
        MenuTraceRegistry.register((MenuType) type.get(), menu -> Optional.of(TrackedEntry.of((TrackedEntryType) TE_BAG.get(), new ItemBasedTraceData(menu.item_slot, menu.getStack().getItem()))));
    }
}