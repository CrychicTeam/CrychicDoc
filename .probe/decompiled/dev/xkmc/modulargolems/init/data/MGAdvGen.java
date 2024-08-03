package dev.xkmc.modulargolems.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.init.advancement.GolemAnvilFixTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemBreakToolTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemEquipTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemHotFixTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemKillTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemMassSummonTrigger;
import dev.xkmc.modulargolems.init.advancement.GolemThunderTrigger;
import dev.xkmc.modulargolems.init.advancement.PartCraftTrigger;
import dev.xkmc.modulargolems.init.advancement.UpgradeApplyTrigger;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MGAdvGen {

    public static void genAdvancements(RegistrateAdvancementProvider pvd) {
        AdvancementGenerator gen = new AdvancementGenerator(pvd, "modulargolems");
        AdvancementGenerator.TabBuilder.Entry root = gen.new TabBuilder("golems").root("root", ((GolemHolder) GolemItems.HOLDER_GOLEM.get()).withUniformMaterial(new ResourceLocation("modulargolems", "iron")), CriterionBuilder.item(Items.CLAY_BALL), "Welcome to Modular Golems", "Craft your army!");
        AdvancementGenerator.TabBuilder.Entry arm = root.create("start", (Item) GolemItems.GOLEM_BODY.get(), CriterionBuilder.item(MGTagGen.GOLEM_PARTS), "The Beginning of Everything", "Craft a Golem Template and use Stone Cutter to cut it into a golem part").create("apply", GolemPart.setMaterial(GolemItems.GOLEM_BODY.asStack(), new ResourceLocation("modulargolems", "iron")), CriterionBuilder.one(PartCraftTrigger.ins()), "Heavy Metal Piece", "Apply metal ingots onto golem parts in an anvil.");
        arm.create("apply_sculk", GolemPart.setMaterial(GolemItems.GOLEM_BODY.asStack(), new ResourceLocation("modulargolems", "sculk")), CriterionBuilder.one(PartCraftTrigger.withMat(new ResourceLocation("modulargolems", "sculk"))), "Bad Memories", "Make a sculk golem part.").type(FrameType.CHALLENGE);
        arm.create("dog", ((GolemHolder) GolemItems.HOLDER_DOG.get()).withUniformMaterial(new ResourceLocation("modulargolems", "iron")), CriterionBuilder.item((Item) GolemItems.HOLDER_DOG.get()), "Royal Cold Doggy", "Craft a Dog Golem.");
        arm.create("humanoid", ((GolemHolder) GolemItems.HOLDER_HUMANOID.get()).withUniformMaterial(new ResourceLocation("modulargolems", "iron")), CriterionBuilder.item((Item) GolemItems.HOLDER_HUMANOID.get()), "You but Tougher", "Craft a Humaniod Golem.").create("fully_equipped", GolemHolder.toEntityIcon(((GolemHolder) GolemItems.HOLDER_HUMANOID.get()).withUniformMaterial(new ResourceLocation("modulargolems", "iron")), Items.DIAMOND_HELMET.getDefaultInstance(), Items.DIAMOND_CHESTPLATE.getDefaultInstance(), Items.DIAMOND_LEGGINGS.getDefaultInstance(), Items.DIAMOND_BOOTS.getDefaultInstance(), Items.DIAMOND_SWORD.getDefaultInstance(), Items.SHIELD.getDefaultInstance()), CriterionBuilder.one(GolemEquipTrigger.ins(6)), "Fully Equipped", "Give Humaniod Golem full armor set, a sword, and a shield.").type(FrameType.GOAL).create("oops", ((GolemHolder) GolemItems.HOLDER_HUMANOID.get()).withUniformMaterial(new ResourceLocation("modulargolems", "gold")), CriterionBuilder.one(GolemBreakToolTrigger.ins()), "Oops...", "Let Humanoid Golem break a piece of equipment").type(FrameType.CHALLENGE);
        AdvancementGenerator.TabBuilder.Entry golem = arm.create("craft", ((GolemHolder) GolemItems.HOLDER_GOLEM.get()).withUniformMaterial(new ResourceLocation("modulargolems", "iron")), CriterionBuilder.item(MGTagGen.GOLEM_HOLDERS), "A Brand New Golem", "Craft a Golem Holder with metal golem parts.");
        golem.create("thunder", Items.LIGHTNING_ROD, CriterionBuilder.one(GolemThunderTrigger.ins()), "Walking Lightning Rod", "Let a golem with thunder immune be struck with a lightning bolt").type(FrameType.CHALLENGE);
        golem.create("anvil_fix", Items.ANVIL, CriterionBuilder.one(GolemAnvilFixTrigger.ins()), "Healing the Wounds", "Repair a metal golem with ingots in an anvil").create("hot_fix", Items.IRON_INGOT, CriterionBuilder.one(GolemHotFixTrigger.ins()), "Repair in Battle", "Repair a metal golem with ingots directly").create("kill_warden", Items.SCULK_CATALYST, CriterionBuilder.one(GolemKillTrigger.byType(EntityType.WARDEN)), "Ship of Theseus", "Let a golem kill a Warden").type(FrameType.CHALLENGE);
        golem.create("summon", (Item) GolemItems.DISPENSE_WAND.get(), CriterionBuilder.item((Item) GolemItems.DISPENSE_WAND.get()), "Airborne Battalion", "Craft a summon wand").create("summon_mass", (Item) GolemItems.DISPENSE_WAND.get(), CriterionBuilder.one(GolemMassSummonTrigger.atLeast(24)), "Portable Military", "Summon at least 24 golems at once").type(FrameType.CHALLENGE);
        golem.create("retrieve", (Item) GolemItems.RETRIEVAL_WAND.get(), CriterionBuilder.item((Item) GolemItems.RETRIEVAL_WAND.get()), "Everyone Comes Back", "Craft a retrieval wand");
        golem.create("command", (Item) GolemItems.COMMAND_WAND.get(), CriterionBuilder.item((Item) GolemItems.COMMAND_WAND.get()), "Guard Your Home", "Craft a command wand");
        AdvancementGenerator.TabBuilder.Entry upgrade = golem.create("upgrade", (Item) GolemItems.EMPTY_UPGRADE.get(), CriterionBuilder.item(MGTagGen.GOLEM_UPGRADES), "Upgrade Your Golem", "Obtain an upgrade");
        upgrade.create("full", (Item) GolemItems.EMPTY_UPGRADE.get(), CriterionBuilder.one(UpgradeApplyTrigger.withRemain(0)), "No More Room", "Use up all upgrade slots").type(FrameType.TASK).create("max", (Item) GolemItems.TALENTED.get(), CriterionBuilder.one(UpgradeApplyTrigger.withTotal(12)), "Above and Beyond", "Apply 12 upgrades on a golem").type(FrameType.CHALLENGE);
        upgrade.create("recycle", (Item) GolemItems.RECYCLE.get(), CriterionBuilder.one(UpgradeApplyTrigger.withUpgrade((UpgradeItem) GolemItems.RECYCLE.get())), "Immortal Golem", "Apply a recycle upgrade on a golem").type(FrameType.CHALLENGE);
        upgrade.create("sponge", (Item) GolemItems.SPONGE.get(), CriterionBuilder.one(UpgradeApplyTrigger.withUpgrade((UpgradeItem) GolemItems.SPONGE.get())), "Waterlogged Golem", "Apply a sponge upgrade on a golem").type(FrameType.GOAL).create("kill_creeper", Items.TNT, CriterionBuilder.one(GolemKillTrigger.byType(EntityType.CREEPER)), "Anti-Terrorism Operation", "Let a golem kill a Creeper").type(FrameType.CHALLENGE);
        upgrade.create("swim", (Item) GolemItems.SWIM.get(), CriterionBuilder.one(UpgradeApplyTrigger.withUpgrade((UpgradeItem) GolemItems.SWIM.get())), "Undersea Warrior", "Apply a swim upgrade on a golem").type(FrameType.GOAL).create("kill_guardian", Items.PRISMARINE_SHARD, CriterionBuilder.one(GolemKillTrigger.byType(EntityType.GUARDIAN)), "Legend of the Atlantis", "Let a golem kill a Guardian").type(FrameType.CHALLENGE);
        root.finish();
    }
}