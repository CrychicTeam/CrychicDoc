package dev.xkmc.l2archery.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import net.minecraft.advancements.FrameType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class AdvGen {

    public static void genAdvancements(RegistrateAdvancementProvider pvd) {
        AdvancementGenerator gen = new AdvancementGenerator(pvd, "l2archery");
        AdvancementGenerator.TabBuilder.Entry root = gen.new TabBuilder("archery").root("root", (Item) ArcheryItems.SUN_BOW.get(), CriterionBuilder.item(Items.ARROW), "Welcome to L2Archery", "Explore the variety of bows and arrows");
        AdvancementGenerator.TabBuilder.Entry start = create(root, ArcheryItems.STARTER_BOW, "Professional Equipment", "Craft a starter bow");
        AdvancementGenerator.TabBuilder.Entry sniper = create(start, ArcheryItems.GLOW_AIM_BOW, "It doesn't Fall", "Craft a sniper bow");
        create(sniper, ArcheryItems.MAGNIFY_BOW, "Zoom In", "Add lens to a sniper bow");
        create(sniper, ArcheryItems.ENDER_AIM_BOW, "How am I Shot?", "Craft an ender bow").type(FrameType.CHALLENGE);
        AdvancementGenerator.TabBuilder.Entry iron = create(start, ArcheryItems.IRON_BOW, "Strong Arm", "Craft an iron bow");
        create(iron, ArcheryItems.EAGLE_BOW, "Armor Penetration", "Craft an eagle bow");
        create(create(create(iron, ArcheryItems.TURTLE_BOW, "Stand Still, Shoot Stronger", "Craft a turtle bow"), ArcheryItems.EARTH_BOW, "Power of the Earth", "Craft the bow of the Earth").type(FrameType.GOAL), ArcheryItems.GAIA_BOW, "Indestructable Archer", "Craft the Bless of Gaia").type(FrameType.CHALLENGE);
        AdvancementGenerator.TabBuilder.Entry master = create(start, ArcheryItems.MASTER_BOW, "Master of Archery", "Craft a master bow");
        create(create(create(master, ArcheryItems.FLAME_BOW, "Magical Ignition", "Craft a blazing bow"), ArcheryItems.EXPLOSION_BOW, "Bow of Destruction", "Craft an explosion bow").type(FrameType.GOAL), ArcheryItems.SUN_BOW, "Descending Sun", "Craft the Bless of Helios").type(FrameType.CHALLENGE);
        create(create(master, ArcheryItems.FROZE_BOW, "Watch it Shake", "Craft a freezing bow"), ArcheryItems.WINTER_BOW, "It's Snowing", "Craft the Ever Freezing Night").type(FrameType.GOAL);
        create(create(master, ArcheryItems.STORM_BOW, "Pair it with Tipped Arrow", "Craft a storm bow"), ArcheryItems.WIND_BOW, "Legendary Archer", "Craft the Bless of Favonius").type(FrameType.CHALLENGE);
        create(master, ArcheryItems.BLACKSTONE_BOW, "Lock it There", "Craft the bow of seal");
        root.create("upgrade", (Item) ArcheryItems.UPGRADE.get(), CriterionBuilder.item((Item) ArcheryItems.UPGRADE.get()), "Mystical Crystal", "Obtain an upgrade").create("binding", Items.ENCHANTED_BOOK, CriterionBuilder.enchanted(ArcheryTagGen.PROF_BOWS, Enchantments.BINDING_CURSE), "Extra Slots", "Put curse of binding on a bow to increase upgrade slot").type(FrameType.CHALLENGE);
        root.enter().create("void", (Item) LCItems.SPACE_SHARD.get(), CriterionBuilder.item((Item) LCItems.SPACE_SHARD.get()), "Halfway Godhood", "Obtain a space shard").type(FrameType.CHALLENGE, true, true, true).create("void_bow", (Item) ArcheryItems.VOID_BOW.get(), CriterionBuilder.item((Item) ArcheryItems.VOID_BOW.get()), "Is this Creative Item?", "Obtain the Sight of the Void").type(FrameType.CHALLENGE).create("void_arrow", (Item) ArcheryItems.VOID_ARROW.get(), CriterionBuilder.item((Item) ArcheryItems.VOID_ARROW.get()), "Delete that Entity", "Obtain the Void Arrow").type(FrameType.CHALLENGE);
        root.finish();
    }

    private static AdvancementGenerator.TabBuilder.Entry create(AdvancementGenerator.TabBuilder.Entry parent, ItemEntry<GenericBowItem> bow, String title, String desc) {
        return parent.create(bow.getId().getPath(), (Item) bow.get(), CriterionBuilder.item((Item) bow.get()), title, desc);
    }
}