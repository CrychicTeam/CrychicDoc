package com.mna.gui;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.tools.RLoc;
import com.mna.items.ItemInit;
import java.awt.Point;
import java.util.HashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiTextures {

    public static HashMap<Affinity, ItemStack> affinityIcons = new HashMap<Affinity, ItemStack>() {

        private static final long serialVersionUID = 1L;

        {
            this.put(Affinity.ARCANE, new ItemStack(ItemInit.GREATER_MOTE_ARCANE.get()).setHoverName(Component.translatable("affinity.mna.arcane")));
            this.put(Affinity.WIND, new ItemStack(ItemInit.GREATER_MOTE_AIR.get()).setHoverName(Component.translatable("affinity.mna.air")));
            this.put(Affinity.EARTH, new ItemStack(ItemInit.GREATER_MOTE_EARTH.get()).setHoverName(Component.translatable("affinity.mna.earth")));
            this.put(Affinity.WATER, new ItemStack(ItemInit.GREATER_MOTE_WATER.get()).setHoverName(Component.translatable("affinity.mna.water")));
            this.put(Affinity.FIRE, new ItemStack(ItemInit.GREATER_MOTE_FIRE.get()).setHoverName(Component.translatable("affinity.mna.fire")));
            this.put(Affinity.ENDER, new ItemStack(ItemInit.GREATER_MOTE_ENDER.get()).setHoverName(Component.translatable("affinity.mna.ender")));
            this.put(Affinity.UNKNOWN, ItemStack.EMPTY);
            this.put(Affinity.HELLFIRE, ItemStack.EMPTY);
            this.put(Affinity.ICE, new ItemStack(ItemInit.GREATER_MOTE_ICE.get()).setHoverName(Component.translatable("affinity.mna.ice")));
            this.put(Affinity.LIGHTNING, new ItemStack(ItemInit.GREATER_MOTE_LIGHTNING.get()).setHoverName(Component.translatable("affinity.mna.lightning")));
        }
    };

    public static HashMap<Affinity, ResourceLocation> affinityBadges = new HashMap<Affinity, ResourceLocation>() {

        private static final long serialVersionUID = 1L;

        {
            this.put(Affinity.UNKNOWN, RLoc.create("textures/gui/affinity/arcane.png"));
            this.put(Affinity.ARCANE, RLoc.create("textures/gui/affinity/arcane.png"));
            this.put(Affinity.EARTH, RLoc.create("textures/gui/affinity/earth.png"));
            this.put(Affinity.ENDER, RLoc.create("textures/gui/affinity/ender.png"));
            this.put(Affinity.FIRE, RLoc.create("textures/gui/affinity/fire.png"));
            this.put(Affinity.HELLFIRE, RLoc.create("textures/gui/affinity/fire.png"));
            this.put(Affinity.LIGHTNING, RLoc.create("textures/gui/affinity/fire.png"));
            this.put(Affinity.WATER, RLoc.create("textures/gui/affinity/water.png"));
            this.put(Affinity.ICE, RLoc.create("textures/gui/affinity/water.png"));
            this.put(Affinity.WIND, RLoc.create("textures/gui/affinity/air.png"));
        }
    };

    public static final HashMap<Attribute, Point> Attribute_Icon_Mappings = new HashMap<Attribute, Point>() {

        {
            this.put(Attribute.RANGE, new Point(0, 0));
            this.put(Attribute.RADIUS, new Point(52, 104));
            this.put(Attribute.SPEED, new Point(104, 0));
            this.put(Attribute.WIDTH, new Point(52, 0));
            this.put(Attribute.HEIGHT, new Point(156, 0));
            this.put(Attribute.DEPTH, new Point(156, 52));
            this.put(Attribute.DURATION, new Point(0, 52));
            this.put(Attribute.DAMAGE, new Point(104, 52));
            this.put(Attribute.MAGNITUDE, new Point(0, 104));
            this.put(Attribute.DELAY, new Point(104, 104));
            this.put(Attribute.LESSER_MAGNITUDE, new Point(156, 104));
            this.put(Attribute.PRECISION, new Point(0, 156));
        }
    };

    public static final Point Cantrip_Icon_Size = new Point(32, 32);

    public static final Point Cantrip_Chevron_Mappings = new Point(6, 242);

    public static final Point Cantrip_Chevron_Size = new Point(14, 8);

    public static class Blocks {

        public static final ResourceLocation BOOKSHELF = RLoc.create("textures/gui/gui_bookshelf.png");

        public static final ResourceLocation SCROLLSHELF = RLoc.create("textures/gui/gui_scrollshelf.png");

        public static final ResourceLocation SEER_STONE = RLoc.create("textures/gui/seer_stone.png");

        public static final ResourceLocation MANAWEAVING_ALTAR = RLoc.create("textures/gui/jei/manaweaving_altar.png");

        public static final ResourceLocation LODESTAR_MAIN = RLoc.create("textures/gui/lodestar_main.png");

        public static final ResourceLocation LODESTAR_BACKGROUND = RLoc.create("textures/gui/lodestar_background.png");

        public static final ResourceLocation LODESTAR_EXTENSION = RLoc.create("textures/gui/lodestar_extension.png");

        public static final ResourceLocation LODESTAR_LESSER_MAIN = RLoc.create("textures/gui/lodestar_main_lesser.png");

        public static final ResourceLocation LODESTAR_LESSER_BACKGROUND = RLoc.create("textures/gui/lodestar_background_lesser.png");

        public static final ResourceLocation LODESTAR_LESSER_EXTENSION = RLoc.create("textures/gui/lodestar_extension_lesser.png");

        public static final ResourceLocation ARCANE_SENTRY = RLoc.create("textures/gui/arcane_sentry.png");

        public static final ResourceLocation OCCULUS_BACKGROUND = RLoc.create("textures/gui/occulus_background.png");

        public static final ResourceLocation OCCULUS_BORDER = RLoc.create("textures/gui/occulus_border.png");

        public static final ResourceLocation OCCULUS_ICONS = RLoc.create("textures/gui/occulus_icons.png");
    }

    public static class Entities {

        public static final ResourceLocation WANDERING_WIZARD = RLoc.create("textures/gui/wandering_wizard.png");

        public static final ResourceLocation CONSTRUCT_DIAGNOSTICS = RLoc.create("textures/gui/construct_diagnostics.png");

        public static final ResourceLocation RIFT = RLoc.create("textures/gui/rift.png");
    }

    public static class Hud {

        public static final ResourceLocation BARS = RLoc.create("textures/gui/gui_manabars.png");

        public static final ResourceLocation OVERLAY = RLoc.create("textures/gui/hud_overlay.png");
    }

    public static class Items {

        public static final ResourceLocation SPELL_CUSTOMIZE = RLoc.create("textures/gui/spell_naming.png");

        public static final ResourceLocation SPELL_BOOK = RLoc.create("textures/gui/spell_book.png");

        public static final ResourceLocation GRIMOIRE = RLoc.create("textures/gui/grimoire.png");

        public static final ResourceLocation BOOK_OF_ROTE = RLoc.create("textures/gui/book_of_rote.png");

        public static final ResourceLocation BOOK_OF_ROTE_EXTRAS = RLoc.create("textures/gui/book_of_rote_extras.png");

        public static final ResourceLocation CANTRIPS = RLoc.create("textures/gui/cantrips.png");

        public static final ResourceLocation CANTRIPS_WANDS = RLoc.create("textures/gui/cantrips_wands.png");

        public static final ResourceLocation STAFF_OF_CALLING = RLoc.create("textures/gui/gui_staff_of_calling.png");

        public static final ResourceLocation BOOK_MARKS = RLoc.create("textures/gui/gui_bookofmarks.png");

        public static final ResourceLocation PRACTITIONERS_POUCH = RLoc.create("textures/gui/practitioners_pouch.png");

        public static final ResourceLocation FILTER_ITEM = RLoc.create("textures/gui/filter_item.png");

        public static final ResourceLocation PRACTITIONERS_POUCH_2 = RLoc.create("textures/gui/practitioners_pouch_2.png");

        public static final ResourceLocation ENDER_DISC = RLoc.create("textures/gui/ender_disc.png");

        public static final ResourceLocation RUNIC_MALUS = RLoc.create("textures/gui/gui_malus.png");

        public static final ResourceLocation SPELL_VALUES = RLoc.create("textures/gui/vellum.png");

        public static final ResourceLocation SPELL_RECIPE = RLoc.create("textures/gui/vellum_lg.png");

        public static final ResourceLocation GUIDE_BOOK = RLoc.create("textures/gui/guide_book.png");

        public static final ResourceLocation GUIDE_BOOK_WIDGETS = RLoc.create("textures/gui/guide_book_widgets.png");
    }

    public static class Jei {

        public static final ResourceLocation RITUAL = RLoc.create("textures/guide/jei/blank.png");

        public static final ResourceLocation RUNEFORGE = RLoc.create("textures/guide/jei/runeforge.png");

        public static final ResourceLocation CRUSHING = RLoc.create("textures/guide/jei/crushing.png");

        public static final ResourceLocation ELDRIN = RLoc.create("textures/guide/jei/eldrin.png");

        public static final ResourceLocation MANAWEAVING_ALTAR = RLoc.create("textures/guide/jei/manaweaving_altar.png");

        public static final ResourceLocation RUNESMITHING = RLoc.create("textures/guide/jei/runesmithing.png");

        public static final ResourceLocation SPELLPART = RLoc.create("textures/guide/jei/spell_part.png");

        public static final ResourceLocation RUNESCRIBING = RLoc.create("textures/guide/jei/runescribing.png");

        public static final ResourceLocation TRANSMUTATION_SINGLE = RLoc.create("textures/guide/jei/transmutation_single.png");

        public static final ResourceLocation TRANSMUTATION_MULTI = RLoc.create("textures/guide/jei/transmutation_multi.png");

        public static final ResourceLocation FUME = RLoc.create("textures/guide/jei/fume_filter.png");
    }

    public static class Overlay {

        public static final ResourceLocation MIST_FORM = RLoc.create("textures/gui/overlay/mist_form.png");

        public static final ResourceLocation TELEPORTING = RLoc.create("textures/gui/overlay/teleporting.png");
    }

    public static class Recipe {

        public static final ResourceLocation BLANK = RLoc.create("textures/guide/recipe/blank.png");

        public static final ResourceLocation RUNEFORGE = RLoc.create("textures/guide/recipe/runeforge.png");

        public static final ResourceLocation CRUSHING = RLoc.create("textures/guide/recipe/crushing.png");

        public static final ResourceLocation TRANSMUTATION_SINGLE = RLoc.create("textures/guide/recipe/transmutation_single.png");

        public static final ResourceLocation TRANSMUTATION_MULTI = RLoc.create("textures/guide/recipe/transmutation_multi.png");

        public static final ResourceLocation ELDRIN = RLoc.create("textures/guide/recipe/eldrin.png");

        public static final ResourceLocation MANAWEAVING_ALTAR = RLoc.create("textures/guide/recipe/manaweaving_altar.png");

        public static final ResourceLocation RUNESMITHING = RLoc.create("textures/guide/recipe/runesmithing.png");

        public static final ResourceLocation SPELLPART = RLoc.create("textures/guide/recipe/spell_part.png");

        public static final ResourceLocation CRAFT_3X3 = RLoc.create("textures/guide/recipe/crafting_3x3.png");

        public static final ResourceLocation RUNESCRIBING = RLoc.create("textures/guide/recipe/runescribing.png");

        public static final ResourceLocation FUME_FILTER = RLoc.create("textures/guide/recipe/fume_filter.png");
    }

    public static class Widgets {

        public static final int WIDGETS_TEX_SIZE = 128;

        public static final int ATTRIBUTE_ICON_TEX_SIZE = 208;

        public static final ResourceLocation GUIDE_WIDGETS = RLoc.create("textures/guide/widgets.png");

        public static final ResourceLocation VISUALIZE_MULTIBLOCK = RLoc.create("textures/guide/visualize_multiblock.png");

        public static final ResourceLocation STANDALONE_INVENTORY_TEXTURE = RLoc.create("textures/gui/standalone_player_inventory.png");

        public static final ResourceLocation WIDGETS = RLoc.create("textures/gui/widgets.png");

        public static final ResourceLocation ATTRIBUTE_ICONS = RLoc.create("textures/gui/attribute_icons.png");

        public static final ResourceLocation FACTION_ICON_COUNCIL = RLoc.create("textures/guide/faction_icon_council.png");

        public static final ResourceLocation FACTION_ICON_DEMONS = RLoc.create("textures/guide/faction_icon_demons.png");

        public static final ResourceLocation FACTION_ICON_FEY = RLoc.create("textures/guide/faction_icon_fey.png");

        public static final ResourceLocation FACTION_ICON_UNDEAD = RLoc.create("textures/guide/faction_icon_undead.png");
    }

    public static class WizardLab {

        public static final ResourceLocation AFFINITY_TINKER = RLoc.create("textures/gui/wizard_lab/affinity_tinker.png");

        public static final ResourceLocation DISENCHANTER = RLoc.create("textures/gui//wizard_lab/disenchanter.png");

        public static final ResourceLocation ELDRIN_FUME = RLoc.create("textures/gui/wizard_lab/eldrin_fume.png");

        public static final ResourceLocation SPELL_SPECIALIZATION = RLoc.create("textures/gui/wizard_lab/spell_specialization.png");

        public static final ResourceLocation SPELL_SPECIALIZATION_EXTENSION = RLoc.create("textures/gui/wizard_lab/spell_specialization_extension.png");

        public static final ResourceLocation STUDY_DESK = RLoc.create("textures/gui/wizard_lab/study_desk.png");

        public static final ResourceLocation ALTAR_OF_ARCANA = RLoc.create("textures/gui/wizard_lab/arcana_altar.png");

        public static final ResourceLocation ALTAR_OF_ARCANA_EXT = RLoc.create("textures/gui/wizard_lab/arcana_altar_ext.png");

        public static final ResourceLocation THESIS_DESK = RLoc.create("textures/gui/wizard_lab/thesis_desk.png");

        public static final ResourceLocation THESIS_DESK_EXTENSION = RLoc.create("textures/gui/wizard_lab/thesis_desk_extension.png");

        public static final ResourceLocation TRANSCRIPTION_TABLE = RLoc.create("textures/gui/wizard_lab/transcription_table.png");

        public static final ResourceLocation INSCRIPTION_TABLE = RLoc.create("textures/gui/wizard_lab/inscription_table.png");

        public static final ResourceLocation MAGICIANS_WORKBENCH = RLoc.create("textures/gui/wizard_lab/magicians_workbench.png");

        public static final ResourceLocation INSCRIPTION_TABLE_WIDGETS = RLoc.create("textures/gui/wizard_lab/inscription_table_widgets.png");

        public static final ResourceLocation RUNESCRIBING_TABLE = RLoc.create("textures/gui/wizard_lab/runescribing_table.png");

        public static final ResourceLocation RUNESCRIBING_EXTRAS = RLoc.create("textures/gui/wizard_lab/runescribing_extension.png");
    }
}