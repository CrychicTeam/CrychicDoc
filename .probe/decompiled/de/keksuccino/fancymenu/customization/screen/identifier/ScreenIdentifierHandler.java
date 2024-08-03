package de.keksuccino.fancymenu.customization.screen.identifier;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.customgui.CustomGuiBaseScreen;
import de.keksuccino.fancymenu.customization.customgui.CustomGuiHandler;
import de.keksuccino.fancymenu.util.properties.PropertiesParser;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.fancymenu.util.properties.PropertyContainerSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenIdentifierHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ScreenIdentifierHandler.ScreenClasspathDatabase SCREEN_CLASSPATH_DATABASE = new ScreenIdentifierHandler.ScreenClasspathDatabase();

    private static final String SCREEN_CLASSPATH_DATABASE_SOURCE = "type = screen_classpath_database\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiMainMenu\n  forge_1.16 = net.minecraft.client.gui.screen.MainMenuScreen\n  forge_1.17 = net.minecraft.client.gui.screens.TitleScreen\n  fabric = net.minecraft.class_442\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiWorldSelection\n  forge_1.16 = net.minecraft.client.gui.screen.WorldSelectionScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.SelectWorldScreen\n  fabric = net.minecraft.class_526\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiCreateWorld\n  forge_1.16 = net.minecraft.client.gui.screen.CreateWorldScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.CreateWorldScreen\n  fabric = net.minecraft.class_525\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.MultiplayerWarningScreen\n  forge_1.17 = net.minecraft.client.gui.screens.multiplayer.SafetyScreen\n  fabric = net.minecraft.class_4749\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiMultiplayer\n  forge_1.16 = net.minecraft.client.gui.screen.MultiplayerScreen\n  forge_1.17 = net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen\n  fabric = net.minecraft.class_500\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenAddServer\n  forge_1.16 = net.minecraft.client.gui.screen.AddServerScreen\n  forge_1.17 = net.minecraft.client.gui.screens.EditServerScreen\n  fabric = net.minecraft.class_422\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenRealmsProxy\n  forge_1.16 = com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen\n  forge_1.17 = [placeholder]\n  fabric = net.minecraft.class_4387\n}\n\nidentifier-group {\n  forge_1.17 = com.mojang.realmsclient.RealmsMainScreen\n  fabric = net.minecraft.class_4325\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraftforge.fml.client.GuiModList\n  forge_1.16 = net.minecraftforge.fml.client.gui.screen.ModListScreen\n  forge_1.17 = net.minecraftforge.fmlclient.gui.screen.ModListScreen\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiOptions\n  forge_1.16 = net.minecraft.client.gui.screen.OptionsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.OptionsScreen\n  fabric = net.minecraft.class_429\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiCustomizeSkin\n  forge_1.16 = net.minecraft.client.gui.screen.CustomizeSkinScreen\n  forge_1.17 = net.minecraft.client.gui.screens.SkinCustomizationScreen\n  fabric = net.minecraft.class_440\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenOptionsSounds\n  forge_1.16 = net.minecraft.client.gui.screen.OptionsSoundsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.SoundOptionsScreen\n  fabric = net.minecraft.class_443\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiVideoSettings\n  forge_1.16 = net.minecraft.client.gui.screen.VideoSettingsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.VideoSettingsScreen\n  fabric = net.minecraft.class_446\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiControls\n  forge_1.16 = net.minecraft.client.gui.screen.ControlsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.controls.ControlsScreen\n  fabric = net.minecraft.class_458\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.MouseSettingsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.MouseSettingsScreen\n  fabric = net.minecraft.class_4288\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiLanguage\n  forge_1.16 = net.minecraft.client.gui.screen.LanguageScreen\n  forge_1.17 = net.minecraft.client.gui.screens.LanguageSelectScreen\n  fabric = net.minecraft.class_426\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.ScreenChatOptions\n  forge_1.16 = net.minecraft.client.gui.screen.ChatOptionsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ChatOptionsScreen\n  fabric = net.minecraft.class_404\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenResourcePacks\n  forge_1.16 = net.minecraft.client.gui.screen.PackScreen\n  forge_1.17 = net.minecraft.client.gui.screens.packs.PackSelectionScreen\n  fabric = net.minecraft.class_5375\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.AccessibilityScreen\n  forge_1.17 = net.minecraft.client.gui.screens.AccessibilityOptionsScreen\n  fabric = net.minecraft.class_4189\n}\n\nidentifier-group {\n  forge_1.12 = [placeholder]\n  forge_1.16 = net.minecraft.client.gui.screen.ConfirmOpenLinkScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ConfirmLinkScreen\n  fabric = net.minecraft.class_407\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.EditGamerulesScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen\n  fabric = net.minecraft.class_5235\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.advancements.GuiScreenAdvancements\n  forge_1.16 = net.minecraft.client.gui.advancements.AdvancementsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.advancements.AdvancementsScreen\n  fabric = net.minecraft.class_457\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.achievement.GuiStats\n  forge_1.16 = net.minecraft.client.gui.screen.StatsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.achievement.StatsScreen\n  fabric = net.minecraft.class_447\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiContainerCreative\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.CreativeScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen\n  fabric = net.minecraft.class_481\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiInventory\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.InventoryScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen\n  fabric = net.minecraft.class_490\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiCrafting\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.CraftingScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CraftingScreen\n  fabric = net.minecraft.class_479\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiEnchantment\n  forge_1.16 = net.minecraft.client.gui.screen.EnchantmentScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.EnchantmentScreen\n  fabric = net.minecraft.class_486\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiRepair\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.AnvilScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.AnvilScreen\n  fabric = net.minecraft.class_471\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiChest\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.ChestScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.ContainerScreen\n  fabric = net.minecraft.class_476\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiBrewingStand\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.BrewingStandScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.BrewingStandScreen\n  fabric = net.minecraft.class_472\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.SmithingTableScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.SmithingScreen\n  fabric = net.minecraft.class_4895\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.BlastFurnaceScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen\n  fabric = net.minecraft.class_3871\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiFurnace\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.FurnaceScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.FurnaceScreen\n  fabric = net.minecraft.class_3873\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiHopper\n  forge_1.16 = net.minecraft.client.gui.screen.HopperScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.HopperScreen\n  fabric = net.minecraft.class_488\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiDispenser\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.DispenserScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.DispenserScreen\n  fabric = net.minecraft.class_480\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiGameOver\n  forge_1.16 = net.minecraft.client.gui.screen.DeathScreen\n  forge_1.17 = net.minecraft.client.gui.screens.DeathScreen\n  fabric = net.minecraft.class_418\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiYesNo\n  forge_1.16 = net.minecraft.client.gui.screen.ConfirmScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ConfirmScreen\n  fabric = net.minecraft.class_410\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiIngameMenu\n  forge_1.16 = net.minecraft.client.gui.screen.IngameMenuScreen\n  forge_1.17 = net.minecraft.client.gui.screens.PauseScreen\n  fabric = net.minecraft.class_433\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.WorldLoadProgressScreen\n  forge_1.17 = net.minecraft.client.gui.screens.LevelLoadingScreen\n  fabric = net.minecraft.class_3928\n}\n\nidentifier-group {\n  forge_1.12 = [placeholder]\n  forge_1.16 = net.minecraft.client.gui.screen.DirtMessageScreen\n  forge_1.17 = net.minecraft.client.gui.screens.GenericDirtMessageScreen\n  fabric = net.minecraft.class_424\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.GamemodeSelectionScreen\n  forge_1.17 = net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen\n  fabric = net.minecraft.class_5289\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.LoomScreen\n  forge_1.18 = net.minecraft.client.gui.screens.inventory.LoomScreen\n  fabric = net.minecraft.class_494\n}\n\nidentifier-group {\n  forge_1.18 = net.minecraft.client.gui.screens.controls.KeyBindsScreen\n  fabric = net.minecraft.class_6599\n}\n\nidentifier-group {\n  forge_1.18 = net.minecraft.client.gui.screens.MouseSettingsScreen\n  fabric = net.minecraft.class_4288\n}\n";

    public static boolean isIdentifierOfScreen(@NotNull String screenIdentifier, @NotNull Screen screen) {
        return equalIdentifiers(screenIdentifier, getIdentifierOfScreen(screen));
    }

    @NotNull
    public static String getIdentifierOfScreen(@NotNull Screen screen) {
        if (screen instanceof CustomGuiBaseScreen c) {
            return c.getIdentifier();
        } else {
            String universal = UniversalScreenIdentifierRegistry.getUniversalIdentifierFor(screen);
            return universal != null ? universal : screen.getClass().getName();
        }
    }

    public static boolean isValidIdentifier(@NotNull String screenIdentifier) {
        if (CustomGuiHandler.guiExists(screenIdentifier)) {
            return true;
        } else if (UniversalScreenIdentifierRegistry.universalIdentifierExists(screenIdentifier)) {
            return true;
        } else if (ScreenCustomization.isScreenBlacklisted(screenIdentifier)) {
            return false;
        } else {
            try {
                Class.forName(screenIdentifier, false, ScreenIdentifierHandler.class.getClassLoader());
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static boolean equalIdentifiers(@Nullable String firstScreenIdentifier, @Nullable String secondScreenIdentifier) {
        if (firstScreenIdentifier == null || secondScreenIdentifier == null) {
            return false;
        } else {
            return firstScreenIdentifier.equals(secondScreenIdentifier) ? true : getBestIdentifier(firstScreenIdentifier).equals(getBestIdentifier(secondScreenIdentifier));
        }
    }

    @NotNull
    public static String getBestIdentifier(@NotNull String screenIdentifier) {
        if (CustomGuiHandler.guiExists(screenIdentifier)) {
            return screenIdentifier;
        } else {
            screenIdentifier = tryFixInvalidIdentifierWithNonUniversal(screenIdentifier);
            String universal = UniversalScreenIdentifierRegistry.getUniversalIdentifierFor(screenIdentifier);
            return universal != null ? universal : screenIdentifier;
        }
    }

    @NotNull
    public static String tryFixInvalidIdentifierWithNonUniversal(@NotNull String potentiallyInvalidScreenIdentifier) {
        if (isValidIdentifier(potentiallyInvalidScreenIdentifier)) {
            return potentiallyInvalidScreenIdentifier;
        } else {
            String fixed = SCREEN_CLASSPATH_DATABASE.getValidScreenClasspathFor(potentiallyInvalidScreenIdentifier);
            return fixed != null ? fixed : potentiallyInvalidScreenIdentifier;
        }
    }

    @NotNull
    public static String tryConvertToNonUniversal(@NotNull String screenIdentifier) {
        return UniversalScreenIdentifierRegistry.universalIdentifierExists(screenIdentifier) ? (String) Objects.requireNonNull(UniversalScreenIdentifierRegistry.getScreenForUniversalIdentifier(screenIdentifier)) : tryFixInvalidIdentifierWithNonUniversal(screenIdentifier);
    }

    protected static class ScreenClasspathDatabase {

        protected List<List<String>> classpathGroups = new ArrayList();

        public ScreenClasspathDatabase() {
            try {
                PropertyContainerSet set = PropertiesParser.deserializeSetFromFancyString("type = screen_classpath_database\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiMainMenu\n  forge_1.16 = net.minecraft.client.gui.screen.MainMenuScreen\n  forge_1.17 = net.minecraft.client.gui.screens.TitleScreen\n  fabric = net.minecraft.class_442\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiWorldSelection\n  forge_1.16 = net.minecraft.client.gui.screen.WorldSelectionScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.SelectWorldScreen\n  fabric = net.minecraft.class_526\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiCreateWorld\n  forge_1.16 = net.minecraft.client.gui.screen.CreateWorldScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.CreateWorldScreen\n  fabric = net.minecraft.class_525\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.MultiplayerWarningScreen\n  forge_1.17 = net.minecraft.client.gui.screens.multiplayer.SafetyScreen\n  fabric = net.minecraft.class_4749\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiMultiplayer\n  forge_1.16 = net.minecraft.client.gui.screen.MultiplayerScreen\n  forge_1.17 = net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen\n  fabric = net.minecraft.class_500\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenAddServer\n  forge_1.16 = net.minecraft.client.gui.screen.AddServerScreen\n  forge_1.17 = net.minecraft.client.gui.screens.EditServerScreen\n  fabric = net.minecraft.class_422\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenRealmsProxy\n  forge_1.16 = com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen\n  forge_1.17 = [placeholder]\n  fabric = net.minecraft.class_4387\n}\n\nidentifier-group {\n  forge_1.17 = com.mojang.realmsclient.RealmsMainScreen\n  fabric = net.minecraft.class_4325\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraftforge.fml.client.GuiModList\n  forge_1.16 = net.minecraftforge.fml.client.gui.screen.ModListScreen\n  forge_1.17 = net.minecraftforge.fmlclient.gui.screen.ModListScreen\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiOptions\n  forge_1.16 = net.minecraft.client.gui.screen.OptionsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.OptionsScreen\n  fabric = net.minecraft.class_429\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiCustomizeSkin\n  forge_1.16 = net.minecraft.client.gui.screen.CustomizeSkinScreen\n  forge_1.17 = net.minecraft.client.gui.screens.SkinCustomizationScreen\n  fabric = net.minecraft.class_440\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenOptionsSounds\n  forge_1.16 = net.minecraft.client.gui.screen.OptionsSoundsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.SoundOptionsScreen\n  fabric = net.minecraft.class_443\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiVideoSettings\n  forge_1.16 = net.minecraft.client.gui.screen.VideoSettingsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.VideoSettingsScreen\n  fabric = net.minecraft.class_446\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiControls\n  forge_1.16 = net.minecraft.client.gui.screen.ControlsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.controls.ControlsScreen\n  fabric = net.minecraft.class_458\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.MouseSettingsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.MouseSettingsScreen\n  fabric = net.minecraft.class_4288\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiLanguage\n  forge_1.16 = net.minecraft.client.gui.screen.LanguageScreen\n  forge_1.17 = net.minecraft.client.gui.screens.LanguageSelectScreen\n  fabric = net.minecraft.class_426\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.ScreenChatOptions\n  forge_1.16 = net.minecraft.client.gui.screen.ChatOptionsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ChatOptionsScreen\n  fabric = net.minecraft.class_404\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiScreenResourcePacks\n  forge_1.16 = net.minecraft.client.gui.screen.PackScreen\n  forge_1.17 = net.minecraft.client.gui.screens.packs.PackSelectionScreen\n  fabric = net.minecraft.class_5375\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.AccessibilityScreen\n  forge_1.17 = net.minecraft.client.gui.screens.AccessibilityOptionsScreen\n  fabric = net.minecraft.class_4189\n}\n\nidentifier-group {\n  forge_1.12 = [placeholder]\n  forge_1.16 = net.minecraft.client.gui.screen.ConfirmOpenLinkScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ConfirmLinkScreen\n  fabric = net.minecraft.class_407\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.EditGamerulesScreen\n  forge_1.17 = net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen\n  fabric = net.minecraft.class_5235\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.advancements.GuiScreenAdvancements\n  forge_1.16 = net.minecraft.client.gui.advancements.AdvancementsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.advancements.AdvancementsScreen\n  fabric = net.minecraft.class_457\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.achievement.GuiStats\n  forge_1.16 = net.minecraft.client.gui.screen.StatsScreen\n  forge_1.17 = net.minecraft.client.gui.screens.achievement.StatsScreen\n  fabric = net.minecraft.class_447\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiContainerCreative\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.CreativeScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen\n  fabric = net.minecraft.class_481\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiInventory\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.InventoryScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen\n  fabric = net.minecraft.class_490\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiCrafting\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.CraftingScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.CraftingScreen\n  fabric = net.minecraft.class_479\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiEnchantment\n  forge_1.16 = net.minecraft.client.gui.screen.EnchantmentScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.EnchantmentScreen\n  fabric = net.minecraft.class_486\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiRepair\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.AnvilScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.AnvilScreen\n  fabric = net.minecraft.class_471\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiChest\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.ChestScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.ContainerScreen\n  fabric = net.minecraft.class_476\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiBrewingStand\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.BrewingStandScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.BrewingStandScreen\n  fabric = net.minecraft.class_472\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.SmithingTableScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.SmithingScreen\n  fabric = net.minecraft.class_4895\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.BlastFurnaceScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen\n  fabric = net.minecraft.class_3871\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiFurnace\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.FurnaceScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.FurnaceScreen\n  fabric = net.minecraft.class_3873\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiHopper\n  forge_1.16 = net.minecraft.client.gui.screen.HopperScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.HopperScreen\n  fabric = net.minecraft.class_488\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.inventory.GuiDispenser\n  forge_1.16 = net.minecraft.client.gui.screen.inventory.DispenserScreen\n  forge_1.17 = net.minecraft.client.gui.screens.inventory.DispenserScreen\n  fabric = net.minecraft.class_480\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiGameOver\n  forge_1.16 = net.minecraft.client.gui.screen.DeathScreen\n  forge_1.17 = net.minecraft.client.gui.screens.DeathScreen\n  fabric = net.minecraft.class_418\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiYesNo\n  forge_1.16 = net.minecraft.client.gui.screen.ConfirmScreen\n  forge_1.17 = net.minecraft.client.gui.screens.ConfirmScreen\n  fabric = net.minecraft.class_410\n}\n\nidentifier-group {\n  forge_1.12 = net.minecraft.client.gui.GuiIngameMenu\n  forge_1.16 = net.minecraft.client.gui.screen.IngameMenuScreen\n  forge_1.17 = net.minecraft.client.gui.screens.PauseScreen\n  fabric = net.minecraft.class_433\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.WorldLoadProgressScreen\n  forge_1.17 = net.minecraft.client.gui.screens.LevelLoadingScreen\n  fabric = net.minecraft.class_3928\n}\n\nidentifier-group {\n  forge_1.12 = [placeholder]\n  forge_1.16 = net.minecraft.client.gui.screen.DirtMessageScreen\n  forge_1.17 = net.minecraft.client.gui.screens.GenericDirtMessageScreen\n  fabric = net.minecraft.class_424\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.GamemodeSelectionScreen\n  forge_1.17 = net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen\n  fabric = net.minecraft.class_5289\n}\n\nidentifier-group {\n  forge_1.16 = net.minecraft.client.gui.screen.LoomScreen\n  forge_1.18 = net.minecraft.client.gui.screens.inventory.LoomScreen\n  fabric = net.minecraft.class_494\n}\n\nidentifier-group {\n  forge_1.18 = net.minecraft.client.gui.screens.controls.KeyBindsScreen\n  fabric = net.minecraft.class_6599\n}\n\nidentifier-group {\n  forge_1.18 = net.minecraft.client.gui.screens.MouseSettingsScreen\n  fabric = net.minecraft.class_4288\n}\n");
                if (set != null) {
                    for (PropertyContainer s : set.getContainersOfType("identifier-group")) {
                        List<String> l = new ArrayList();
                        for (Entry<String, String> m : s.getProperties().entrySet()) {
                            l.add((String) m.getValue());
                        }
                        if (!l.isEmpty()) {
                            this.classpathGroups.add(l);
                        }
                    }
                } else {
                    ScreenIdentifierHandler.LOGGER.error("[FANCYMENU] Failed to load screen classpath database source! This could lead to some layouts not loading correctly!");
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        @Nullable
        public String getValidScreenClasspathFor(@NotNull String potentiallyInvalidScreenClasspath) {
            try {
                for (List<String> l : this.classpathGroups) {
                    if (l.contains(potentiallyInvalidScreenClasspath)) {
                        for (String s : l) {
                            if (!ScreenCustomization.isScreenBlacklisted(s)) {
                                try {
                                    Class.forName(s, false, ScreenIdentifierHandler.class.getClassLoader());
                                    return s;
                                } catch (Exception var7) {
                                }
                            }
                        }
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
            return null;
        }
    }
}