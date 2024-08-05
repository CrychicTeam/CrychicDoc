package dev.xkmc.modulargolems.compat.materials.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.modulargolems.compat.materials.create.automation.DummyFurnace;
import dev.xkmc.modulargolems.compat.materials.create.modifier.CoatingModifier;
import dev.xkmc.modulargolems.compat.materials.create.modifier.MechBodyModifier;
import dev.xkmc.modulargolems.compat.materials.create.modifier.MechForceEffect;
import dev.xkmc.modulargolems.compat.materials.create.modifier.MechForceModifier;
import dev.xkmc.modulargolems.compat.materials.create.modifier.MechMobileEffect;
import dev.xkmc.modulargolems.compat.materials.create.modifier.MechMobileModifier;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.ModList;

public class CreateCompatRegistry {

    public static final RegistryEntry<CoatingModifier> COATING = GolemModifiers.reg("coating", CoatingModifier::new, "Reduce damage taken by %s");

    public static final RegistryEntry<AttributeGolemModifier> PUSH = GolemModifiers.reg("push", () -> new AttributeGolemModifier(1, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATKKB, () -> 1.0))).register();

    public static final RegistryEntry<MechBodyModifier> BODY = GolemModifiers.reg("mechanical_engine", MechBodyModifier::new, "Consumes fuels to power the golem up.");

    public static final RegistryEntry<MechMobileModifier> MOBILE = GolemModifiers.reg("mechanical_mobility", MechMobileModifier::new, "When burning fuels, increase speed by %s%%");

    public static final RegistryEntry<MechForceModifier> FORCE = GolemModifiers.reg("mechanical_force", MechForceModifier::new, "When burning fuels, increase attack damage by %s%%");

    public static final RegistryEntry<MechMobileEffect> EFF_MOBILE = genEffect("mechanical_mobility", () -> new MechMobileEffect(MobEffectCategory.BENEFICIAL, -1), "Increase golem movement speed");

    public static final RegistryEntry<MechForceEffect> EFF_FORCE = genEffect("mechanical_force", () -> new MechForceEffect(MobEffectCategory.BENEFICIAL, -1), "Increase golem attack damage");

    public static final ItemEntry<SimpleUpgradeItem> UP_COATING = GolemItems.regModUpgrade("coating", () -> COATING, "create").lang("Zinc Upgrade").register();

    public static final ItemEntry<SimpleUpgradeItem> UP_PUSH = GolemItems.regModUpgrade("push", () -> PUSH, "create").lang("Extendo Upgrade").register();

    public static final ItemEntry<DummyFurnace> DUMMY = ModularGolems.REGISTRATE.item("dummy_furnace", p -> new DummyFurnace()).model((ctx, pvd) -> pvd.withExistingParent("item/" + ctx.getName(), "block/air")).removeTab(GolemItems.TAB.getKey()).register();

    private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
        return ((NoConfigBuilder) ModularGolems.REGISTRATE.effect(name, sup, desc).lang(MobEffect::m_19481_)).register();
    }

    public static void register() {
        MGTagGen.OPTIONAL_ITEM.add((Consumer) e -> e.addTag(MGTagGen.SPECIAL_CRAFT).m_176839_(AllItems.ANDESITE_ALLOY.getId()).addOptionalTag(new ResourceLocation("forge", "ingots/brass")).addOptional(AllBlocks.RAILWAY_CASING.getId()));
        if (ModList.get().isLoaded("l2complements")) {
            MGTagGen.OPTIONAL_EFF.add((Consumer) e -> e.addTag(TagGen.SKILL_EFFECT).addOptional(EFF_MOBILE.getId()).addOptional(EFF_FORCE.getId()));
        }
    }
}