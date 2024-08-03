package de.keksuccino.fancymenu.customization.screen.identifier;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsBackupInfoScreen;
import com.mojang.realmsclient.gui.screens.RealmsBackupScreen;
import com.mojang.realmsclient.gui.screens.RealmsBrokenWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfirmScreen;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsInviteScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import com.mojang.realmsclient.gui.screens.RealmsPlayerScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetNormalWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsSelectFileToUploadScreen;
import com.mojang.realmsclient.gui.screens.RealmsSelectWorldTemplateScreen;
import com.mojang.realmsclient.gui.screens.RealmsSettingsScreen;
import com.mojang.realmsclient.gui.screens.RealmsSlotOptionsScreen;
import com.mojang.realmsclient.gui.screens.RealmsSubscriptionInfoScreen;
import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
import com.mojang.realmsclient.gui.screens.RealmsUploadScreen;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.CreateBuffetWorldScreen;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.DemoIntroScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.GenericWaitingScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.OutOfMemoryScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.PopupScreen;
import net.minecraft.client.gui.screens.PresetFlatWorldScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.Realms32bitWarningScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;
import net.minecraft.client.gui.screens.reporting.ChatSelectionScreen;
import net.minecraft.client.gui.screens.reporting.ReportReasonSelectionScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.client.gui.screens.worldselection.ConfirmExperimentalFeaturesScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.client.gui.screens.worldselection.ExperimentsScreen;
import net.minecraft.client.gui.screens.worldselection.OptimizeWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UniversalScreenIdentifierRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, String> UNIVERSAL_IDENTIFIERS = new LinkedHashMap();

    public static void register(@NotNull String universalIdentifier, @NotNull String targetScreenClassPath) {
        if (UNIVERSAL_IDENTIFIERS.containsKey(universalIdentifier)) {
            LOGGER.warn("[FANCYMENU] Universal identifier '" + universalIdentifier + "' already registered! Replacing identifier..");
        }
        UNIVERSAL_IDENTIFIERS.put(universalIdentifier, targetScreenClassPath);
    }

    @Nullable
    public static String getScreenForUniversalIdentifier(@NotNull String universalIdentifier) {
        return (String) UNIVERSAL_IDENTIFIERS.get(universalIdentifier);
    }

    @NotNull
    public static String tryGetUniversalIdentifierFor(@NotNull String screenClassPath) {
        String universal = getUniversalIdentifierFor(screenClassPath);
        return universal != null ? universal : screenClassPath;
    }

    @Nullable
    public static String getUniversalIdentifierFor(@NotNull String screenClassPath) {
        if (universalIdentifierExists(screenClassPath)) {
            return screenClassPath;
        } else {
            for (Entry<String, String> m : UNIVERSAL_IDENTIFIERS.entrySet()) {
                if (((String) m.getValue()).equals(screenClassPath)) {
                    return (String) m.getKey();
                }
            }
            return null;
        }
    }

    @Nullable
    public static String getUniversalIdentifierFor(@NotNull Screen screen) {
        return getUniversalIdentifierFor(screen.getClass().getName());
    }

    @NotNull
    public static List<String> getUniversalIdentifiers() {
        return new ArrayList(UNIVERSAL_IDENTIFIERS.keySet());
    }

    public static boolean universalIdentifierExists(@NotNull String identifier) {
        return UNIVERSAL_IDENTIFIERS.containsKey(identifier);
    }

    static {
        register("title_screen", TitleScreen.class.getName());
        register("realms_main_screen", RealmsMainScreen.class.getName());
        register("realms_backup_info_screen", RealmsBackupInfoScreen.class.getName());
        register("realms_backup_screen", RealmsBackupScreen.class.getName());
        register("realms_broken_world_screen", RealmsBrokenWorldScreen.class.getName());
        register("realms_client_outdated_screen", RealmsClientOutdatedScreen.class.getName());
        register("realms_configure_world_screen", RealmsConfigureWorldScreen.class.getName());
        register("realms_confirm_screen", RealmsConfirmScreen.class.getName());
        register("realms_create_realm_screen", RealmsCreateRealmScreen.class.getName());
        register("realms_download_latest_world_screen", RealmsDownloadLatestWorldScreen.class.getName());
        register("realms_generic_error_screen", RealmsGenericErrorScreen.class.getName());
        register("realms_invite_screen", RealmsInviteScreen.class.getName());
        register("realms_long_confirmation_screen", RealmsLongConfirmationScreen.class.getName());
        register("realms_long_running_mco_task_screen", RealmsLongRunningMcoTaskScreen.class.getName());
        register("realms_notifications_screen", RealmsNotificationsScreen.class.getName());
        register("realms_parental_consent_screen", RealmsParentalConsentScreen.class.getName());
        register("realms_pending_invites_screen", RealmsPendingInvitesScreen.class.getName());
        register("realms_player_screen", RealmsPlayerScreen.class.getName());
        register("realms_reset_normal_world_screen", RealmsResetNormalWorldScreen.class.getName());
        register("realms_reset_world_screen", RealmsResetWorldScreen.class.getName());
        register("realms_select_file_to_upload_screen", RealmsSelectFileToUploadScreen.class.getName());
        register("realms_select_world_template_screen", RealmsSelectWorldTemplateScreen.class.getName());
        register("realms_settings_screen", RealmsSettingsScreen.class.getName());
        register("realms_slot_options_screen", RealmsSlotOptionsScreen.class.getName());
        register("realms_subscription_info_screen", RealmsSubscriptionInfoScreen.class.getName());
        register("realms_terms_screen", RealmsTermsScreen.class.getName());
        register("realms_upload_screen", RealmsUploadScreen.class.getName());
        register("accessibility_onboarding_screen", AccessibilityOnboardingScreen.class.getName());
        register("accessibility_options_screen", AccessibilityOptionsScreen.class.getName());
        register("alert_screen", AlertScreen.class.getName());
        register("backup_confirm_screen", BackupConfirmScreen.class.getName());
        register("chat_options_screen", ChatOptionsScreen.class.getName());
        register("chat_screen", ChatScreen.class.getName());
        register("confirm_link_screen", ConfirmLinkScreen.class.getName());
        register("confirm_screen", ConfirmScreen.class.getName());
        register("connect_screen", ConnectScreen.class.getName());
        register("create_buffet_world_screen", CreateBuffetWorldScreen.class.getName());
        register("create_flat_world_screen", CreateFlatWorldScreen.class.getName());
        register("credits_and_attribution_screen", CreditsAndAttributionScreen.class.getName());
        register("datapack_load_failure_screen", DatapackLoadFailureScreen.class.getName());
        register("death_screen", DeathScreen.class.getName());
        register("title_confirm_screen", DeathScreen.TitleConfirmScreen.class.getName());
        register("demo_intro_screen", DemoIntroScreen.class.getName());
        register("direct_join_server_screen", DirectJoinServerScreen.class.getName());
        register("disconnected_screen", DisconnectedScreen.class.getName());
        register("edit_server_screen", EditServerScreen.class.getName());
        register("error_screen", ErrorScreen.class.getName());
        register("generic_dirt_message_screen", GenericDirtMessageScreen.class.getName());
        register("generic_waiting_screen", GenericWaitingScreen.class.getName());
        register("in_bed_chat_screen", InBedChatScreen.class.getName());
        register("language_select_screen", LanguageSelectScreen.class.getName());
        register("level_loading_screen", LevelLoadingScreen.class.getName());
        register("mouse_settings_screen", MouseSettingsScreen.class.getName());
        register("online_options_screen", OnlineOptionsScreen.class.getName());
        register("options_screen", OptionsScreen.class.getName());
        register("options_sub_screen", OptionsSubScreen.class.getName());
        register("out_of_memory_screen", OutOfMemoryScreen.class.getName());
        register("pause_screen", PauseScreen.class.getName());
        register("popup_screen", PopupScreen.class.getName());
        register("preset_flat_world_screen", PresetFlatWorldScreen.class.getName());
        register("progress_screen", ProgressScreen.class.getName());
        register("receiving_level_screen", ReceivingLevelScreen.class.getName());
        register("share_to_lan_screen", ShareToLanScreen.class.getName());
        register("skin_customization_screen", SkinCustomizationScreen.class.getName());
        register("sound_options_screen", SoundOptionsScreen.class.getName());
        register("video_settings_screen", VideoSettingsScreen.class.getName());
        register("win_screen", WinScreen.class.getName());
        register("stats_screen", StatsScreen.class.getName());
        register("advancements_screen", AdvancementsScreen.class.getName());
        register("controls_screen", ControlsScreen.class.getName());
        register("key_binds_screen", KeyBindsList.class.getName());
        register("game_mode_switcher_screen", GameModeSwitcherScreen.class.getName());
        register("anvil_screen", AnvilScreen.class.getName());
        register("beacon_screen", BeaconScreen.class.getName());
        register("blast_furnace_screen", BlastFurnaceScreen.class.getName());
        register("book_edit_screen", BookEditScreen.class.getName());
        register("book_view_screen", BookViewScreen.class.getName());
        register("brewing_stand_screen", BrewingStandScreen.class.getName());
        register("cartography_table_screen", CartographyTableScreen.class.getName());
        register("command_block_edit_screen", CommandBlockEditScreen.class.getName());
        register("container_screen", ContainerScreen.class.getName());
        register("crafting_screen", CraftingScreen.class.getName());
        register("creative_mode_inventory_screen", CreativeModeInventoryScreen.class.getName());
        register("dispenser_screen", DispenserScreen.class.getName());
        register("effect_rendering_inventory_screen", EffectRenderingInventoryScreen.class.getName());
        register("enchantment_screen", EnchantmentScreen.class.getName());
        register("furnace_screen", FurnaceScreen.class.getName());
        register("grindstone_screen", GrindstoneScreen.class.getName());
        register("hanging_sign_edit_screen", HangingSignEditScreen.class.getName());
        register("hopper_screen", HopperScreen.class.getName());
        register("horse_inventory_screen", HorseInventoryScreen.class.getName());
        register("inventory_screen", InventoryScreen.class.getName());
        register("item_combiner_screen", ItemCombinerScreen.class.getName());
        register("jigsaw_block_edit_screen", JigsawBlockEditScreen.class.getName());
        register("lectern_screen", LecternScreen.class.getName());
        register("loom_screen", LoomScreen.class.getName());
        register("merchant_screen", MerchantScreen.class.getName());
        register("minecart_command_block_edit_screen", MinecartCommandBlockEditScreen.class.getName());
        register("shulker_box_screen", ShulkerBoxScreen.class.getName());
        register("sign_edit_screen", SignEditScreen.class.getName());
        register("smithing_screen", SmithingScreen.class.getName());
        register("smoker_screen", SmokerScreen.class.getName());
        register("stonecutter_screen", StonecutterScreen.class.getName());
        register("structure_block_edit_screen", StructureBlockEditScreen.class.getName());
        register("join_multiplayer_screen", JoinMultiplayerScreen.class.getName());
        register("realms_32bit_warning_screen", Realms32bitWarningScreen.class.getName());
        register("safety_screen", SafetyScreen.class.getName());
        register("warning_screen", WarningScreen.class.getName());
        register("pack_selection_screen", PackSelectionScreen.class.getName());
        register("chat_report_screen", ChatReportScreen.class.getName());
        register("chat_selection_screen", ChatSelectionScreen.class.getName());
        register("report_reason_selection_screen", ReportReasonSelectionScreen.class.getName());
        register("social_interactions_screen", SocialInteractionsScreen.class.getName());
        register("telemetry_info_screen", TelemetryInfoScreen.class.getName());
        register("confirm_experimental_features_screen", ConfirmExperimentalFeaturesScreen.class.getName());
        register("create_world_screen", CreateWorldScreen.class.getName());
        register("edit_game_rules_screen", EditGameRulesScreen.class.getName());
        register("edit_world_screen", EditWorldScreen.class.getName());
        register("experiments_screen", ExperimentsScreen.class.getName());
        register("optimize_world_screen", OptimizeWorldScreen.class.getName());
        register("select_world_screen", SelectWorldScreen.class.getName());
        register("disconnected_realms_screen", DisconnectedRealmsScreen.class.getName());
        register("realms_screen", RealmsScreen.class.getName());
    }
}