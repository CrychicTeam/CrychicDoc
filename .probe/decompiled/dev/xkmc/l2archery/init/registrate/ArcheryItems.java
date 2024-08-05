package dev.xkmc.l2archery.init.registrate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.BleedingArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.DamageModifierArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.DamageSourceArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.EnderArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.ExplodeArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.ExplosionBreakFeature;
import dev.xkmc.l2archery.content.feature.arrow.FireArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.NoFallArrowFeature;
import dev.xkmc.l2archery.content.feature.arrow.VoidArrowFeature;
import dev.xkmc.l2archery.content.feature.bow.EnderShootFeature;
import dev.xkmc.l2archery.content.feature.bow.FluxFeature;
import dev.xkmc.l2archery.content.feature.bow.GlowTargetAimFeature;
import dev.xkmc.l2archery.content.feature.bow.InfinityFeature;
import dev.xkmc.l2archery.content.feature.bow.PullEffectFeature;
import dev.xkmc.l2archery.content.feature.bow.WindBowFeature;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.feature.core.StatFeature;
import dev.xkmc.l2archery.content.feature.materials.PoseiditeArrowFeature;
import dev.xkmc.l2archery.content.feature.materials.TotemicArrowFeature;
import dev.xkmc.l2archery.content.item.ArrowConfig;
import dev.xkmc.l2archery.content.item.BowConfig;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.content.upgrade.UpgradeItem;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2archery.init.data.ArcheryDamageState;
import dev.xkmc.l2archery.init.data.ArcheryTagGen;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2library.base.L2Registrate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class ArcheryItems {

    public static final List<GenericBowItem> BOW_LIKE = new ArrayList();

    private static final RegistryEntry<CreativeModeTab> TAB = L2Archery.REGISTRATE.buildL2CreativeTab("archery", "L2 Archery", b -> b.icon(ArcheryItems.STARTER_BOW::asStack));

    public static final ItemEntry<GenericBowItem> STARTER_BOW = genBow("starter_bow", 0, 600).register();

    public static final ItemEntry<GenericBowItem> IRON_BOW = genBow("iron_bow", 1, 1200).register();

    public static final ItemEntry<GenericBowItem> MASTER_BOW = genBow("master_bow", 1, 1200).register();

    public static final ItemEntry<GenericBowItem> MAGNIFY_BOW = genBow("magnify_bow", 2, 300, e -> e.add(new NoFallArrowFeature(40)).add(new GlowTargetAimFeature(128))).lang("Sniper Bow with Lens").register();

    public static final ItemEntry<GenericBowItem> GLOW_AIM_BOW = genBow("glow_aim_bow", 1, 300, e -> e.add(new NoFallArrowFeature(40)).add(new GlowTargetAimFeature(128))).lang("Sniper Bow").register();

    public static final ItemEntry<GenericBowItem> ENDER_AIM_BOW = genBow("ender_aim_bow", 2, 600, e -> e.add(new EnderShootFeature(128))).lang("Ender Bow").register();

    public static final ItemEntry<GenericBowItem> EAGLE_BOW = genBow("eagle_bow", 2, 600, e -> e.add(new DamageSourceArrowFeature((a, s) -> s.enable(DefaultDamageState.BYPASS_ARMOR), () -> rec$.get()))).register();

    public static final ItemEntry<GenericBowItem> WIND_BOW = genBow("wind_bow", 3, 600, e -> e.add(new NoFallArrowFeature(40)).add(new WindBowFeature()).add(new PullEffectFeature(List.of((Supplier) () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1))))).lang("Bless of Favonius").register();

    public static final ItemEntry<GenericBowItem> EXPLOSION_BOW = genBow("explosion_bow", 2, 300, e -> e.add(new ExplodeArrowFeature(3.0F, true, false))).register();

    public static final ItemEntry<GenericBowItem> FLAME_BOW = genBow("flame_bow", 2, 600, e -> e.add(new FireArrowFeature(100))).lang("Blazing Bow").register();

    public static final ItemEntry<GenericBowItem> FROZE_BOW = genBow("froze_bow", 2, 600).lang("Freezing Bow").register();

    public static final ItemEntry<GenericBowItem> STORM_BOW = genBow("storm_bow", 2, 600, e -> e.add(new ExplodeArrowFeature(3.0F, false, false))).lang("Approaching Storm").register();

    public static final ItemEntry<GenericBowItem> BLACKSTONE_BOW = genBow("slow_bow", 2, 600).lang("Bow of Seal").register();

    public static final ItemEntry<GenericBowItem> WINTER_BOW = genBow("winter_bow", 3, 600, e -> e.add(new ExplodeArrowFeature(3.0F, false, false))).lang("Ever Freezing Night").register();

    public static final ItemEntry<GenericBowItem> TURTLE_BOW = genBow("turtle_bow", 2, 600, e -> e.add(new PullEffectFeature(List.of((Supplier) () -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3), (Supplier) () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 2))))).register();

    public static final ItemEntry<GenericBowItem> EARTH_BOW = genBow("earth_bow", 2, 600, e -> e.add(new PullEffectFeature(List.of((Supplier) () -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5), (Supplier) () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 3))))).lang("Bow of the Earth").register();

    public static final ItemEntry<GenericBowItem> GAIA_BOW = genBow("gaia_bow", 3, 600, e -> e.add(new PullEffectFeature(List.of((Supplier) () -> new MobEffectInstance((MobEffect) LCEffects.STONE_CAGE.get(), 80, 0), (Supplier) () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 4))))).lang("Bless of Gaia").register();

    public static final ItemEntry<GenericBowItem> VOID_BOW = genBow("void_bow", 3, 300, e -> e.add(new EnderShootFeature(128)).add(new DamageSourceArrowFeature((a, s) -> s.enable(ArcheryDamageState.BYPASS_INVUL), () -> rec$.get()))).lang("Sight of the Void (Creative Only)").register();

    public static final ItemEntry<GenericBowItem> SUN_BOW = genBow("sun_bow", 3, 600, e -> e.add(new FireArrowFeature(200)).add(new ExplodeArrowFeature(4.0F, true, false))).lang("Bless of Helios").register();

    public static final ItemEntry<GenericArrowItem> STARTER_ARROW = genArrow("starter_arrow", 2);

    public static final ItemEntry<GenericArrowItem> COPPER_ARROW = genArrow("copper_arrow", 1);

    public static final ItemEntry<GenericArrowItem> IRON_ARROW = genArrow("iron_arrow", 1);

    public static final ItemEntry<GenericArrowItem> GOLD_ARROW = genArrow("gold_arrow", 1);

    public static final ItemEntry<GenericArrowItem> OBSIDIAN_ARROW = genArrow("obsidian_arrow", 1);

    public static final ItemEntry<GenericArrowItem> NO_FALL_ARROW = genArrow("no_fall_arrow", 1, e -> e.add(new NoFallArrowFeature(40))).lang("Anti-Gravity Arrow").register();

    public static final ItemEntry<GenericArrowItem> ENDER_ARROW = genArrow("ender_arrow", 0, e -> e.add(new EnderArrowFeature())).register();

    public static final ItemEntry<GenericArrowItem> TNT_1_ARROW = genArrow("tnt_arrow_lv1", 1, e -> e.add(new ExplodeArrowFeature(2.0F, true, false))).lang("Explosion Arrow").register();

    public static final ItemEntry<GenericArrowItem> TNT_2_ARROW = genArrow("tnt_arrow_lv2", 0, e -> e.add(new ExplodeArrowFeature(4.0F, true, false))).lang("TNT Arrow").register();

    public static final ItemEntry<GenericArrowItem> TNT_3_ARROW = genArrow("tnt_arrow_lv3", 0, e -> e.add(new ExplodeArrowFeature(6.0F, true, false))).lang("End Crystal Arrow").register();

    public static final ItemEntry<GenericArrowItem> FIRE_1_ARROW = genArrow("fire_arrow_lv1", 1, e -> e.add(new FireArrowFeature(100))).lang("Soul Fire Arrow").register();

    public static final ItemEntry<GenericArrowItem> FIRE_2_ARROW = genArrow("fire_arrow_lv2", 0, e -> e.add(new FireArrowFeature(200))).lang("Cursed Fire Arrow").register();

    public static final ItemEntry<GenericArrowItem> DESTROYER_ARROW = genArrow("destroyer_arrow", 0);

    public static final ItemEntry<GenericArrowItem> ICE_ARROW = genArrow("frozen_arrow", 1);

    public static final ItemEntry<GenericArrowItem> DISPELL_ARROW = genArrow("dispell_arrow", 0, e -> e.add(new DamageSourceArrowFeature((a, s) -> s.enable(DefaultDamageState.BYPASS_MAGIC), () -> rec$.get()))).register();

    public static final ItemEntry<GenericArrowItem> ACID_ARROW = genArrow("acid_arrow", 1);

    public static final ItemEntry<GenericArrowItem> BLACKSTONE_ARROW = genArrow("blackstone_arrow", 1);

    public static final ItemEntry<GenericArrowItem> DIAMOND_ARROW = genArrow("diamond_arrow", 1);

    public static final ItemEntry<GenericArrowItem> QUARTZ_ARROW = genArrow("quartz_arrow", 1);

    public static final ItemEntry<GenericArrowItem> WITHER_ARROW = genArrow("wither_arrow", 1, e -> e.add(new DamageSourceArrowFeature((a, s) -> s.enable(DefaultDamageState.BYPASS_ARMOR), () -> rec$.get()))).register();

    public static final ItemEntry<GenericArrowItem> STORM_ARROW = genArrow("storm_arrow", 1, e -> e.add(new ExplodeArrowFeature(3.0F, false, false))).register();

    public static final ItemEntry<GenericArrowItem> VOID_ARROW = genArrow("void_arrow", 0, e -> e.add(new DamageSourceArrowFeature((a, s) -> s.enable(ArcheryDamageState.BYPASS_INVUL), () -> rec$.get())).add(new VoidArrowFeature())).lang("Void Arrow (Creative Only)").register();

    public static final ItemEntry<GenericArrowItem> TOTEMIC_GOLD_ARROW = genArrow("totemic_gold_arrow", 0, e -> e.add(new DamageModifierArrowFeature((a, s) -> LCMats.TOTEMIC_GOLD.getExtraToolConfig().onDamage(s, ItemStack.EMPTY), list -> LCMats.TOTEMIC_GOLD.getExtraToolConfig().addTooltip(ItemStack.EMPTY, list))).add(new TotemicArrowFeature(4))).register();

    public static final ItemEntry<GenericArrowItem> POSEIDITE_ARROW = genArrow("poseidite_arrow", 0, e -> e.add(new DamageModifierArrowFeature((a, s) -> {
        LCMats.POSEIDITE.getExtraToolConfig().onDamage(s, ItemStack.EMPTY);
        if (a.m_20071_()) {
            s.addHurtModifier(DamageModifier.multAttr(1.5F));
        }
    }, list -> LCMats.POSEIDITE.getExtraToolConfig().addTooltip(ItemStack.EMPTY, list))).add(new PoseiditeArrowFeature())).register();

    public static final ItemEntry<GenericArrowItem> SHULKERATE_ARROW = genArrow("shulkerate_arrow", 0, e -> e.add(new NoFallArrowFeature(40))).register();

    public static final ItemEntry<GenericArrowItem> SCULKIUM_ARROW = genArrow("sculkium_arrow", 0, e -> e.add(new DamageSourceArrowFeature((a, s) -> {
        s.enable(DefaultDamageState.BYPASS_MAGIC);
        s.enable(DefaultDamageState.BYPASS_ARMOR);
    }, () -> rec$.get()))).register();

    public static final ItemEntry<GenericArrowItem> ETERNIUM_ARROW = genArrow("eternium_arrow", 2);

    public static final ItemEntry<GenericArrowItem> TEARING_ARROW = genArrow("tearing_arrow", 0, e -> e.add(new BleedingArrowFeature(100, 9))).register();

    public static final ItemEntry<UpgradeItem> UPGRADE = L2Archery.REGISTRATE.item("upgrade", UpgradeItem::new).defaultModel().defaultLang().tab(TAB.getKey(), e -> ((UpgradeItem) UPGRADE.get()).fillItemCategory(e)).register();

    public static final RegistryEntry<Upgrade> GLOW_UP = genUpgrade("glow", () -> new GlowTargetAimFeature(128));

    public static final RegistryEntry<Upgrade> NO_FALL_UP = genUpgrade("anti_gravity", () -> new NoFallArrowFeature(40));

    public static final RegistryEntry<Upgrade> FIRE_UP = genPotionUpgrade("soul_fire");

    public static final RegistryEntry<Upgrade> ICE_UP = genPotionUpgrade("frozen");

    public static final RegistryEntry<Upgrade> EXPLOSION_UP = genUpgrade("explosion", () -> new ExplodeArrowFeature(3.0F, true, false));

    public static final RegistryEntry<Upgrade> ENDER_UP = genUpgrade("void", () -> new EnderShootFeature(128));

    public static final RegistryEntry<Upgrade> MAGNIFY_UP_1 = genUpgrade("magnify_x2", () -> new StatFeature(2.0F, 10, 1.0F, 0, 1.0F));

    public static final RegistryEntry<Upgrade> MAGNIFY_UP_2 = genUpgrade("magnify_x4", () -> new StatFeature(4.0F, 30, 1.0F, 0, 1.0F));

    public static final RegistryEntry<Upgrade> MAGNIFY_UP_3 = genUpgrade("magnify_x8", () -> new StatFeature(8.0F, 50, 1.0F, 0, 1.0F));

    public static final RegistryEntry<Upgrade> DAMAGE_UP = genUpgrade("damage", () -> new StatFeature(1.0F, 0, 2.0F, 0, 1.0F));

    public static final RegistryEntry<Upgrade> PUNCH_UP = genUpgrade("punch", () -> new StatFeature(1.0F, 0, 1.0F, 3, 1.0F));

    public static final RegistryEntry<Upgrade> BLACKSTONE_UP = genPotionUpgrade("blackstone");

    public static final RegistryEntry<Upgrade> HARM_UP = genPotionUpgrade("harm");

    public static final RegistryEntry<Upgrade> HEAL_UP = genPotionUpgrade("heal");

    public static final RegistryEntry<Upgrade> SHINE_UP = genPotionUpgrade("glowing");

    public static final RegistryEntry<Upgrade> LEVITATE_UP = genPotionUpgrade("levitate");

    public static final RegistryEntry<Upgrade> SUPERDAMAGE_UP = genUpgrade("super_damage", () -> new StatFeature(1.0F, 0, 3.0F, 0, 1.0F));

    public static final RegistryEntry<Upgrade> RAILGUN_UP = genUpgrade("railgun", () -> new StatFeature(1.0F, 1, 1.0F, 0, 100.0F));

    public static final RegistryEntry<Upgrade> FLUX_UP = genUpgrade("flux_up", () -> FluxFeature.DEFAULT);

    public static final RegistryEntry<Upgrade> FLOAT_UP = genPotionUpgrade("levitation");

    public static final RegistryEntry<Upgrade> SLOW_UP = genPotionUpgrade("slowness");

    public static final RegistryEntry<Upgrade> POISON_UP = genPotionUpgrade("poison");

    public static final RegistryEntry<Upgrade> WITHER_UP = genPotionUpgrade("wither");

    public static final RegistryEntry<Upgrade> WEAK_UP = genPotionUpgrade("weak");

    public static final RegistryEntry<Upgrade> CORROSION_UP = genPotionUpgrade("corrosion");

    public static final RegistryEntry<Upgrade> CURSE_UP = genPotionUpgrade("curse");

    public static final RegistryEntry<Upgrade> CLEANSE_UP = genPotionUpgrade("cleanse");

    public static final RegistryEntry<Upgrade> ADVANCED_INFINITY = genUpgrade("advanced_infinity", () -> new InfinityFeature(2));

    public static final RegistryEntry<Upgrade> EXPLOSION_BREAKER = genUpgrade("explosion_breaker", () -> ExplosionBreakFeature.INS);

    private static final float[] BOW_PULL_VALS = new float[] { 0.0F, 0.65F, 0.9F };

    public static void register() {
    }

    public static ItemBuilder<GenericBowItem, L2Registrate> genBow(String id, int rank, int durability) {
        return genBow(id, rank, durability, e -> {
        });
    }

    public static ItemBuilder<GenericBowItem, L2Registrate> genBow(String id, int rank, int durability, Consumer<Builder<BowArrowFeature>> consumer) {
        Builder<BowArrowFeature> f = ImmutableList.builder();
        consumer.accept(f);
        return L2Archery.REGISTRATE.item(id, p -> new GenericBowItem(p.stacksTo(1).durability(durability), e -> new BowConfig(e, rank, f.build()))).model(ArcheryItems::createBowModel).defaultLang().tag(new TagKey[] { ArcheryTagGen.FORGE_BOWS, ArcheryTagGen.PROF_BOWS });
    }

    public static <T extends GenericBowItem> void createBowModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        ItemModelBuilder builder = (ItemModelBuilder) pvd.withExistingParent(ctx.getName(), "minecraft:bow");
        builder.texture("layer0", "item/bow/" + ctx.getName() + "/bow");
        for (int i = 0; i < 3; i++) {
            String name = ctx.getName() + "/bow_pulling_" + i;
            ItemModelBuilder ret = ((ItemModelBuilder) pvd.getBuilder("item/bow/" + name)).parent(new ModelFile.UncheckedModelFile("minecraft:item/bow_pulling_" + i));
            ret.texture("layer0", "item/bow/" + name);
            ItemModelBuilder.OverrideBuilder override = builder.override();
            override.predicate(new ResourceLocation("pulling"), 1.0F);
            if (BOW_PULL_VALS[i] > 0.0F) {
                override.predicate(new ResourceLocation("pull"), BOW_PULL_VALS[i]);
            }
            override.model(new ModelFile.UncheckedModelFile("l2archery:item/bow/" + name));
        }
    }

    public static ItemEntry<GenericArrowItem> genArrow(String id, int infLevel) {
        return genArrow(id, infLevel, e -> {
        }).register();
    }

    public static ItemBuilder<GenericArrowItem, L2Registrate> genArrow(String id, int infLevel, Consumer<Builder<BowArrowFeature>> consumer) {
        Builder<BowArrowFeature> f = ImmutableList.builder();
        consumer.accept(f);
        return L2Archery.REGISTRATE.item(id, p -> new GenericArrowItem(p, e -> new ArrowConfig(e, infLevel, f.build()))).model(ArcheryItems::createArrowModel).tag(new TagKey[] { ArcheryTagGen.FORGE_ARROWS, ArcheryTagGen.PROF_ARROWS }).defaultLang();
    }

    public static <T extends GenericArrowItem> void createArrowModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        pvd.generated(ctx, new ResourceLocation[] { new ResourceLocation("l2archery", "item/arrow/" + ctx.getName()) });
    }

    public static RegistryEntry<Upgrade> genUpgrade(String str, Supplier<BowArrowFeature> sup) {
        return L2Archery.REGISTRATE.generic(ArcheryRegister.UPGRADE, str, () -> new Upgrade(sup)).defaultLang().register();
    }

    public static RegistryEntry<Upgrade> genPotionUpgrade(String str) {
        return L2Archery.REGISTRATE.generic(ArcheryRegister.UPGRADE, str, () -> new Upgrade(PotionArrowFeature::fromUpgradeConfig)).defaultLang().register();
    }
}