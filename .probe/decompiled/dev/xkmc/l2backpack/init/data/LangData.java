package dev.xkmc.l2backpack.init.data;

import dev.xkmc.l2backpack.init.loot.LootGen;
import dev.xkmc.l2itemselector.init.data.L2Keys;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.ModList;

public class LangData {

    public static void addInfo(List<Component> list, LangData.Info... text) {
        if (Screen.hasShiftDown()) {
            boolean col = false;
            for (LangData.Info info : text) {
                list.add(info.get().withStyle(col ? Style.EMPTY.withColor(6995504) : Style.EMPTY.withColor(6278628)));
                col = !col;
            }
        } else {
            list.add(LangData.Info.SHIFT.get().withStyle(ChatFormatting.GRAY));
            list.add(LangData.Info.PATCHOULI.get().withStyle(ModList.get().isLoaded("patchouli") ? ChatFormatting.GRAY : ChatFormatting.YELLOW));
        }
    }

    public static void altInsert(List<Component> list) {
        if (!Screen.hasShiftDown()) {
            LocalPlayer player = Minecraft.getInstance().player;
            AbstractContainerMenu menu = player.f_36096_;
            if (!menu.getCarried().isEmpty()) {
                list.add(LangData.Info.ALT_INSERT.get().withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    public static void addTranslations(BiConsumer<String, String> pvd) {
        for (LangData.IDS id : LangData.IDS.values()) {
            pvd.accept("l2backpack." + id.id, id.def);
        }
        for (LangData.Info id : LangData.Info.values()) {
            pvd.accept("l2backpack." + id.id, id.def);
        }
        for (LootGen.HiddenPlayer pl : LootGen.HiddenPlayer.values()) {
            pvd.accept("l2backpack.loot." + pl.id + ".name", pl.pname);
            pvd.accept("l2backpack.loot." + pl.id + ".item", pl.bname);
        }
        pvd.accept(BackpackKeys.OPEN.id, "Open backpack on back");
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static enum IDS {

        BACKPACK_SLOT("tooltip.backpack_slot", 2, "Upgrade: %s/%s"),
        STORAGE_OWNER("tooltip.owner", 1, "Owner: %s"),
        BAG_SIZE("tooltip.item.size", 2, "Content: %s/%s"),
        DRAWER_CONTENT("tooltip.drawer.content", 2, "Content: %s x%s"),
        DUMP_FEEDBACK("chat.feedback.dump", 1, "Dumped %s items into target block"),
        LOAD_FEEDBACK("chat.feedback.load", 1, "Loaded %s items from target block"),
        EXTRACT_FEEDBACK("chat.feedback.extract", 1, "Extracted %s items"),
        COLLECT_FEEDBACK("chat.feedback.collect", 1, "Collected %s items"),
        NO_ITEM("chat.feedback.no_item", 0, "No item set for ender drawer. Cannot be placed."),
        LOOT("tooltip.info.loot", 0, "It may have loots inside"),
        MODE_NONE("tooltip.mode.none", 0, "No Pickup"),
        MODE_STACKING("tooltip.mode.stacking", 0, "Pickup to Stacking Slots Only"),
        MODE_ALL("tooltip.mode.all", 0, "Pickup all Fitting Items"),
        DESTROY_NONE("tooltip.destroy.none", 0, "No Destroy"),
        DESTROY_EXCESS("tooltip.destroy.excess", 0, "Destroy excess item"),
        DESTROY_MATCH("tooltip.destroy.matching", 0, "Destroy matching item"),
        DESTROY_ALL("tooltip.destroy.all", 0, "Destroy all items inserted");

        final String id;

        final String def;

        final int count;

        private IDS(String id, int count, String def) {
            this.id = id;
            this.def = def;
            this.count = count;
        }

        public MutableComponent get(Object... objs) {
            if (objs.length != this.count) {
                throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
            } else {
                return Component.translatable("l2backpack." + this.id, objs);
            }
        }
    }

    public static enum Info {

        SHIFT("tooltip.shift", "Press [SHIFT] to show usage"),
        ALT_CONTENT("tooltip.alt_content", "Press [ALT] to show content"),
        ALT_INSERT("tooltip.alt_insert", "Left Click with [ALT] to insert as Pickup"),
        PATCHOULI("tooltip.patchouli", "Read Patchouli Book for details"),
        COLLECT_BAG("tooltip.collect.item", "Right click to store matching items in inventory, other than hotbar"),
        COLLECT_DRAWER("tooltip.collect.drawer", "Shift + right click to store matching items on inventory"),
        EXTRACT_BAG("tooltip.extract.item", "Shift + right click to throw out stored items. Would at most throw 16 at a time."),
        EXTRACT_DRAWER("tooltip.extract.drawer", "Right click to take one stack item out"),
        LOAD("tooltip.load", "Supports Load / Dump with Chest"),
        PLACE("tooltip.place", "Shift + right click to place it as a block"),
        KEYBIND("tooltip.keybind", "This can be put on chest slot (or back slot of Curios), and can be opened via key bind."),
        QUICK_INV_ACCESS("tooltip.info.quick_inv", "Right click to open. Or right click in inventory / ender chest / dimensional storage GUI to open directly."),
        QUICK_ANY_ACCESS("tooltip.info.quick_any", "Right click to open. Or right click in any GUI to open directly."),
        ARROW_INFO("tooltip.info.arrow_bag", "Put in off hand or chest slot (or back slot of Curios) and hold bow in main hand to preview, choose, and shoot arrows from quiver. Press up/down or [%s] + number to switch arrows", L2Keys.SHIFT),
        DRAWER_USE("tooltip.info.drawer", "In inventory, left click drawer with a stack to store item. Right click drawer to take item out. Drawer can only store 1 kind of simple item that has no NBT, but can store up to 64 stacks, or 512 stacks with full upgrades."),
        ENDER_DRAWER_USE("tooltip.info.ender_drawer_block", "For ender drawer block, right click it with item to store, and right click it with empty hand to retrieve a stack. For bulk transport, use drawer item to interact with it."),
        DIMENSIONAL("tooltip.info.dimensional", "All dimensional storage with the same color and owned by the same player shares the same inventory space, for both item and block form."),
        ENDER_DRAWER("tooltip.info.ender_drawer", "Same usage as drawer. All ender drawer set to the same item and owned by the same player shares the same inventory space, for both item and block form. Has same pickup option as regular drawer."),
        UPGRADE("tooltip.info.upgrade", "Upgrade by applying an Ender Pocket in Smithing Table. Content and name will be preserved."),
        EXIT("tooltip.info.exit", "When exiting GUI, it will return to the previous GUI if opened in accessible GUI. Press Shift + Esc to close all."),
        SCABBARD_INFO("tooltip.info.tool_bag", "Put in off hand or chest slot (or back slot of Curios).  Hold tools or weapons in main hand and press [%1$s] (or hold nothing and press [%2$s]) to preview, choose, and swap tools from scabbard. Press up/down or [%1$s] + number to switch tools. Press %3$s to swap", L2Keys.SHIFT, L2Keys.ALT, L2Keys.SWAP),
        ARMORBAG_INFO("tooltip.info.armor_bag", "Put in off hand or chest slot (or back slot of Curios). Hold nothing in main hand and press [%1$s] to preview, choose, and swap armors. Press up/down or [%1$s] + number to switch armors. Press %2$s to swap", L2Keys.SHIFT, L2Keys.SWAP),
        SUIT_BAG_INFO("tooltip.info.suit_bag", "Same as Armor Swap but swaps full set at a time. It will exchange equipped items and selected items. Takes down player armor if the selected row has empty slot."),
        MULTI_SWITCH_INFO("tooltip.info.multi_switch", "This is a Quiver, a Tool Swap, and an Armor Swap at the same time. Sneak and hold respective items to trigger each mode. When holding nothing and pressing alt, tool swap mode is activated."),
        ENDER_SWITCH_INFO("tooltip.info.ender_switch", "This is a Combined Swap and an Ender Backpack at the same time. Note that the arrows, tools, and armors are stored within this item still, not in remote inventory. It inherits all properties of a backpack and an ender backpack."),
        INHERIT("tooltip.info.inherit", "Inherit all properties of a regular backpack, except that it cannot be upgraded. Can be placed in regular backpacks, but cannot open directly in regular backpack. Put it in dimensional storage for quick access."),
        PICKUP("tooltip.info.pickup", "Supports recursive pickup"),
        PICKUP_TWEAKER("tooltip.info.pickup_tweaker", "Right click backpack / drawer / bag in inventory with this item to switch pickup mode."),
        DESTROY_TWEAKER("tooltip.info.destroy_tweaker", "Right click backpack / drawer / bag in inventory with this item to switch destroy mode."),
        TWEAKER_BACK("tooltip.info.tweaker_back", "Right click in hand to adjust mode for backpacks on your back."),
        TWEAKER_BLOCK("tooltip.info.tweaker_block", "Right click dimensional storage block to adjust mode for that block.");

        private final String id;

        private final String def;

        private final L2Keys[] key;

        private Info(String id, String def, L2Keys... key) {
            this.id = id;
            this.def = def;
            this.key = key;
        }

        public MutableComponent get() {
            Object[] arr = new Object[this.key.length];
            for (int i = 0; i < this.key.length; i++) {
                arr[i] = this.key[i].map.getKey().getDisplayName().copy().withStyle(ChatFormatting.YELLOW);
            }
            return Component.translatable("l2backpack." + this.id, arr);
        }
    }
}