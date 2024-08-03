package net.blay09.mods.craftingtweaks.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;

@Config("craftingtweaks")
public class CraftingTweaksConfigData implements BalmConfigData {

    public CraftingTweaksConfigData.Common common = new CraftingTweaksConfigData.Common();

    public CraftingTweaksConfigData.Client client = new CraftingTweaksConfigData.Client();

    public CraftingTweaksMode getCraftingTweaksMode(String modId) {
        return this.client.disabledAddons.contains(modId) ? CraftingTweaksMode.DISABLED : this.client.mode;
    }

    public static class Client {

        @Comment("If set to true, right-clicking the result slot in a crafting table will craft a full stack.")
        public boolean rightClickCraftsStack = true;

        @Comment("We both know JEI is much better. This option hides Vanilla's crafting book button instead of moving it.")
        public boolean hideVanillaCraftingGuide = false;

        @Comment("Set to 'DEFAULT' to enable both buttons and hotkeys. Set to 'BUTTONS' to enable buttons only. Set to 'HOTKEYS' to enable hotkeys only. Set to 'DISABLED' to disable completely.")
        public CraftingTweaksMode mode = CraftingTweaksMode.DEFAULT;

        @ExpectedType(String.class)
        @Comment("Add mod ids here of mods that you wish to disable Crafting Tweaks support for.")
        public List<String> disabledAddons = new ArrayList();
    }

    public static class Common {

        @Comment("Set this to false if you want the (de)compress feature to work outside of crafting GUIs (only works if installed on server while in the player inventory)")
        public boolean compressRequiresCraftingGrid = true;

        @ExpectedType(String.class)
        @Comment("A list of modid:name entries that will not be crafted by the compress key.")
        public List<String> compressDenylist = Arrays.asList("minecraft:sandstone", "minecraft:iron_trapdoor");
    }
}