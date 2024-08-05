package dev.xkmc.l2backpack.init.registrate;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2backpack.content.backpack.BackpackMenu;
import dev.xkmc.l2backpack.content.backpack.BackpackScreen;
import dev.xkmc.l2backpack.content.common.BaseOpenableScreen;
import dev.xkmc.l2backpack.content.quickswap.armorswap.ArmorBagMenu;
import dev.xkmc.l2backpack.content.quickswap.armorswap.ArmorSetBagMenu;
import dev.xkmc.l2backpack.content.quickswap.merged.EnderSwitchMenu;
import dev.xkmc.l2backpack.content.quickswap.merged.MultiSwitchMenu;
import dev.xkmc.l2backpack.content.quickswap.merged.MultiSwitchScreen;
import dev.xkmc.l2backpack.content.quickswap.quiver.QuiverMenu;
import dev.xkmc.l2backpack.content.quickswap.scabbard.ScabbardMenu;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestContainer;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestScreen;
import dev.xkmc.l2backpack.init.L2Backpack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

public class BackpackMenus {

    public static final MenuEntry<BackpackMenu> MT_BACKPACK = ((MenuBuilder) L2Backpack.REGISTRATE.menu("backpack", BackpackMenu::fromNetwork, () -> BackpackScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<WorldChestContainer> MT_WORLD_CHEST = ((MenuBuilder) L2Backpack.REGISTRATE.menu("dimensional_storage", WorldChestContainer::fromNetwork, () -> WorldChestScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<QuiverMenu> MT_ARROW = ((MenuBuilder) L2Backpack.REGISTRATE.menu("arrow_bag", QuiverMenu::fromNetwork, () -> BaseOpenableScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<ScabbardMenu> MT_TOOL = ((MenuBuilder) L2Backpack.REGISTRATE.menu("tool_bag", ScabbardMenu::fromNetwork, () -> BaseOpenableScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<ArmorBagMenu> MT_ARMOR = ((MenuBuilder) L2Backpack.REGISTRATE.menu("armor_bag", ArmorBagMenu::fromNetwork, () -> BaseOpenableScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<ArmorSetBagMenu> MT_ARMOR_SET = ((MenuBuilder) L2Backpack.REGISTRATE.menu("armor_set", ArmorSetBagMenu::fromNetwork, () -> BaseOpenableScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<MultiSwitchMenu> MT_MULTI = ((MenuBuilder) L2Backpack.REGISTRATE.menu("multi_switch", MultiSwitchMenu::fromNetwork, () -> MultiSwitchScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static final MenuEntry<EnderSwitchMenu> MT_ES = ((MenuBuilder) L2Backpack.REGISTRATE.menu("ender_switch", EnderSwitchMenu::fromNetwork, () -> MultiSwitchScreen::new).lang(BackpackMenus::getLangKey)).register();

    public static void register() {
    }

    public static String getLangKey(MenuType<?> menu) {
        ResourceLocation rl = ForgeRegistries.MENU_TYPES.getKey(menu);
        assert rl != null;
        return "container." + rl.getNamespace() + "." + rl.getPath();
    }
}