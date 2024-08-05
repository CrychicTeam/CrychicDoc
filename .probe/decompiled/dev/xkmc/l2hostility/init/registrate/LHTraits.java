package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.AdaptingTrait;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.FieryTrait;
import dev.xkmc.l2hostility.content.traits.common.GravityTrait;
import dev.xkmc.l2hostility.content.traits.common.InvisibleTrait;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import dev.xkmc.l2hostility.content.traits.common.ShulkerTrait;
import dev.xkmc.l2hostility.content.traits.goals.CounterStrikeTrait;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ArenaTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.CorrosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.DrainTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ErosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.GrowthTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ReprintTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.SplitTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DispellTrait;
import dev.xkmc.l2hostility.content.traits.legendary.KillerAuraTrait;
import dev.xkmc.l2hostility.content.traits.legendary.MasterTrait;
import dev.xkmc.l2hostility.content.traits.legendary.PullingTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RagnarokTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RepellingTrait;
import dev.xkmc.l2hostility.content.traits.legendary.UndyingTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.entries.TraitBuilder;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.RegistryBuilder;

public class LHTraits {

    public static final L2Registrate.RegistryInstance<MobTrait> TRAITS = L2Hostility.REGISTRATE.newRegistry("trait", MobTrait.class, RegistryBuilder::hasTags);

    public static final ProviderType<IntrinsicImpl<MobTrait>> TRAIT_TAGS = ProviderType.register("tags/trait", type -> (p, e) -> new IntrinsicImpl(p, type, "traits", e.getGenerator().getPackOutput(), TRAITS.key(), e.getLookupProvider(), reg -> ResourceKey.create(TRAITS.key(), reg.getRegistryName()), e.getExistingFileHelper()));

    public static final TagKey<MobTrait> POTION = LHTagGen.createTraitTag("potion_trait");

    public static final RegistryEntry<AttributeTrait> TANK = L2Hostility.REGISTRATE.regTrait("tank", () -> new AttributeTrait(ChatFormatting.GREEN, new AttributeTrait.AttributeEntry("tank_health", () -> Attributes.MAX_HEALTH, LHConfig.COMMON.tankHealth::get, AttributeModifier.Operation.MULTIPLY_TOTAL), new AttributeTrait.AttributeEntry("tank_armor", () -> Attributes.ARMOR, LHConfig.COMMON.tankArmor::get, AttributeModifier.Operation.ADDITION), new AttributeTrait.AttributeEntry("tank_tough", () -> Attributes.ARMOR_TOUGHNESS, LHConfig.COMMON.tankTough::get, AttributeModifier.Operation.ADDITION)), rl -> new TraitConfig(rl, 20, 100, 5, 20)).lang("Tanky").register();

    public static final RegistryEntry<AttributeTrait> SPEEDY = L2Hostility.REGISTRATE.regTrait("speedy", () -> new AttributeTrait(ChatFormatting.AQUA, new AttributeTrait.AttributeEntry("speedy", () -> Attributes.MOVEMENT_SPEED, LHConfig.COMMON.speedy::get, AttributeModifier.Operation.MULTIPLY_TOTAL)), rl -> new TraitConfig(rl, 20, 100, 5, 50)).lang("Speedy").register();

    public static final RegistryEntry<SelfEffectTrait> PROTECTION = L2Hostility.REGISTRATE.regTrait("protection", () -> new SelfEffectTrait(() -> MobEffects.DAMAGE_RESISTANCE), rl -> new TraitConfig(rl, 30, 100, 4, 50)).lang("Protected").register();

    public static final RegistryEntry<TargetEffectTrait> WEAKNESS = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("weakness", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.WEAKNESS, LHConfig.COMMON.weakTime.get(), lv - 1)), rl -> new TraitConfig(rl, 25, 50, 5, 40)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Weakener").register();

    public static final RegistryEntry<TargetEffectTrait> SLOWNESS = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("slowness", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, LHConfig.COMMON.slowTime.get(), lv)), rl -> new TraitConfig(rl, 10, 100, 5, 20)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Stray").register();

    public static final RegistryEntry<TargetEffectTrait> POISON = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("poison", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.POISON, LHConfig.COMMON.poisonTime.get() * lv)), rl -> new TraitConfig(rl, 15, 100, 3, 20)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Poisonous").register();

    public static final RegistryEntry<TargetEffectTrait> WITHER = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("wither", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.WITHER, LHConfig.COMMON.witherTime.get(), lv - 1)), rl -> new TraitConfig(rl, 15, 50, 3, 20)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Withering").register();

    public static final RegistryEntry<TargetEffectTrait> BLIND = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("blindness", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.BLINDNESS, LHConfig.COMMON.blindTime.get() * lv)), rl -> new TraitConfig(rl, 30, 50, 3, 40)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Blinder").register();

    public static final RegistryEntry<TargetEffectTrait> CONFUSION = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("nausea", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.CONFUSION, LHConfig.COMMON.confusionTime.get() * lv)), rl -> new TraitConfig(rl, 30, 50, 3, 40)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Distorter").register();

    public static final RegistryEntry<TargetEffectTrait> LEVITATION = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("levitation", () -> new TargetEffectTrait(lv -> new MobEffectInstance(MobEffects.LEVITATION, LHConfig.COMMON.levitationTime.get() * lv)), rl -> new TraitConfig(rl, 25, 50, 3, 40)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Levitater").register();

    public static final RegistryEntry<TargetEffectTrait> SOUL_BURNER = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("soul_burner", () -> new TargetEffectTrait(lv -> new MobEffectInstance((MobEffect) LCEffects.FLAME.get(), LHConfig.COMMON.soulBurnerTime.get(), lv - 1)), rl -> new TraitConfig(rl, 50, 100, 3, 70)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Soul Burner").register();

    public static final RegistryEntry<TargetEffectTrait> FREEZING = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("freezing", () -> new TargetEffectTrait(lv -> new MobEffectInstance((MobEffect) LCEffects.ICE.get(), LHConfig.COMMON.freezingTime.get() * lv)), rl -> new TraitConfig(rl, 30, 50, 3, 50)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Freezing").register();

    public static final RegistryEntry<TargetEffectTrait> CURSED = ((TraitBuilder) L2Hostility.REGISTRATE.regTrait("cursed", () -> new TargetEffectTrait(lv -> new MobEffectInstance((MobEffect) LCEffects.CURSE.get(), LHConfig.COMMON.curseTime.get() * lv)), rl -> new TraitConfig(rl, 20, 100, 3, 20)).tag(TRAIT_TAGS, new TagKey[] { POTION })).lang("Cursed").register();

    public static final RegistryEntry<InvisibleTrait> INVISIBLE = L2Hostility.REGISTRATE.regTrait("invisible", InvisibleTrait::new, rl -> new TraitConfig(rl, 30, 100, 1, 50).addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))).lang("Invisible").register();

    public static final RegistryEntry<ShulkerTrait> SHULKER = L2Hostility.REGISTRATE.regTrait("shulker", () -> new ShulkerTrait(ChatFormatting.LIGHT_PURPLE, LHConfig.COMMON.shulkerInterval::get, BulletType.PLAIN, 0), rl -> new TraitConfig(rl, 30, 100, 1, 70).addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))).desc("Shoot bullets every %s seconds after the previous bullet disappears.").lang("Shulker").register();

    public static final RegistryEntry<ShulkerTrait> GRENADE = L2Hostility.REGISTRATE.regTrait("grenade", () -> new ShulkerTrait(ChatFormatting.RED, LHConfig.COMMON.grenadeInterval::get, BulletType.EXPLODE, 15), rl -> new TraitConfig(rl, 50, 100, 5, 100).addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))).desc("Shoot explosive bullets every %s seconds after the previous bullet disappears.").lang("Grenade").register();

    public static final RegistryEntry<FieryTrait> FIERY = L2Hostility.REGISTRATE.regTrait("fiery", FieryTrait::new, rl -> new TraitConfig(rl, 20, 100, 1, 20)).desc("Ignite attacker and attack target for %s seconds. Makes mob immune to fire.").lang("Fiery").register();

    public static final RegistryEntry<RegenTrait> REGEN = L2Hostility.REGISTRATE.regTrait("regenerate", () -> new RegenTrait(ChatFormatting.RED), rl -> new TraitConfig(rl, 30, 100, 5, 50)).desc("Heals %s%% of full health every second.").lang("Regenerating").register();

    public static final RegistryEntry<DementorTrait> DEMENTOR = L2Hostility.REGISTRATE.regTrait("dementor", () -> new DementorTrait(ChatFormatting.DARK_GRAY), rl -> new TraitConfig(rl, 120, 50, 1, 150)).desc("Immune to physical damage. Damage bypass armor.").lang("Dementor").register();

    public static final RegistryEntry<DispellTrait> DISPELL = L2Hostility.REGISTRATE.regTrait("dispell", () -> new DispellTrait(ChatFormatting.DARK_PURPLE), rl -> new TraitConfig(rl, 100, 50, 3, 150)).desc("Immune to magic damage. Damage bypass magical protections. Randomly picks %s enchanted equipment and disable enchantments on them for %s seconds.").lang("Dispell").register();

    public static final RegistryEntry<AdaptingTrait> ADAPTIVE = L2Hostility.REGISTRATE.regTrait("adaptive", () -> new AdaptingTrait(ChatFormatting.GOLD), rl -> new TraitConfig(rl, 80, 50, 5, 100)).desc("Memorize damage types taken and stack %s%% damage reduction for those damage every time. Memorizes last %s different damage types.").lang("Adaptive").register();

    public static final RegistryEntry<ReflectTrait> REFLECT = L2Hostility.REGISTRATE.regTrait("reflect", () -> new ReflectTrait(ChatFormatting.DARK_RED), rl -> new TraitConfig(rl, 80, 50, 5, 100)).desc("Reflect direct physical damage as %s%% magical damage").lang("Reflect").register();

    public static final RegistryEntry<UndyingTrait> UNDYING = L2Hostility.REGISTRATE.regTrait("undying", () -> new UndyingTrait(ChatFormatting.DARK_BLUE), rl -> new TraitConfig(rl, 150, 100, 1, 150).addBlacklist(e -> e.addTag(LHTagGen.SEMIBOSS))).desc("Mob will heal to full health every time it dies.").lang("Undying").register();

    public static final RegistryEntry<RepellingTrait> REPELLING = L2Hostility.REGISTRATE.regTrait("repelling", () -> new RepellingTrait(ChatFormatting.DARK_GREEN), rl -> new TraitConfig(rl, 50, 100, 1, 100).addWhitelist(e -> e.add(EntityType.SKELETON, EntityType.STRAY, EntityType.PILLAGER, EntityType.EVOKER, EntityType.WITCH, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.WITHER))).desc("Mob will push away entities hostile to it within %s blocks, and immune to projectiles.").lang("Repelling").register();

    public static final RegistryEntry<PullingTrait> PULLING = L2Hostility.REGISTRATE.regTrait("pulling", () -> new PullingTrait(ChatFormatting.DARK_BLUE), rl -> new TraitConfig(rl, 50, 100, 1, 100).addWhitelist(e -> e.addTag(LHTagGen.MELEE_WEAPON_TARGET))).desc("Mob will pull entities hostile to it within %s blocks.").lang("Pulling").register();

    public static final RegistryEntry<EnderTrait> ENDER = L2Hostility.REGISTRATE.regTrait("teleport", () -> new EnderTrait(ChatFormatting.DARK_PURPLE), rl -> new TraitConfig(rl, 120, 100, 1, 150).addBlacklist(pvd -> pvd.addTag(LHTagGen.SEMIBOSS))).desc("Mob will attempt to teleport to avoid physical damage and track targets.").lang("Teleport").register();

    public static final RegistryEntry<CorrosionTrait> CORROSION = L2Hostility.REGISTRATE.regTrait("corrosion", () -> new CorrosionTrait(ChatFormatting.DARK_RED), rl -> new TraitConfig(rl, 50, 50, 3, 200)).desc("When hit target, randomly picks %s equipments and increase their durability loss by %s. When there aren't enough equipments, increase damage by %s per piece").lang("Corrosion").register();

    public static final RegistryEntry<ErosionTrait> EROSION = L2Hostility.REGISTRATE.regTrait("erosion", () -> new ErosionTrait(ChatFormatting.DARK_BLUE), rl -> new TraitConfig(rl, 50, 50, 3, 200)).desc("When hit target, randomly picks %s equipments and reduce their durability by %s. When there aren't enough equipments, increase damage by %s per piece").lang("Erosion").register();

    public static final RegistryEntry<KillerAuraTrait> KILLER_AURA = L2Hostility.REGISTRATE.regTrait("killer_aura", () -> new KillerAuraTrait(ChatFormatting.DARK_RED), rl -> new TraitConfig(rl, 100, 50, 3, 300)).desc("Deal %s magic damage to players and entities targeting it within %s blocks and apply trait effects for every %ss").lang("Killer Aura").register();

    public static final RegistryEntry<RagnarokTrait> RAGNAROK = L2Hostility.REGISTRATE.regTrait("ragnarok", () -> new RagnarokTrait(ChatFormatting.DARK_BLUE), rl -> new TraitConfig(rl, 200, 50, 3, 600)).desc("When hit target, randomly picks %s equipments and seal them, which takes %ss to unseal.").lang("Ragnarok").register();

    public static final RegistryEntry<GrowthTrait> GROWTH = L2Hostility.REGISTRATE.regTrait("growth", () -> new GrowthTrait(ChatFormatting.DARK_GREEN), rl -> new TraitConfig(rl, 60, 300, 3, 100)).desc("Slime will grow larger when at full health. Automatically gain Regenerate trait.").lang("Growth").register();

    public static final RegistryEntry<SplitTrait> SPLIT = L2Hostility.REGISTRATE.regTrait("split", () -> new SplitTrait(ChatFormatting.GREEN), rl -> new TraitConfig(rl, 50, 100, 3, 120).addWhitelist(e -> e.add(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityType.DROWNED, EntityType.HUSK, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.STRAY, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.VEX, EntityType.SILVERFISH, EntityType.ENDERMITE))).desc("When mob dies, it will split into 2 of itself with half levels but same trait. This trait reduce by 1 when split.").lang("Split").register();

    public static final RegistryEntry<DrainTrait> DRAIN = L2Hostility.REGISTRATE.regTrait("drain", () -> new DrainTrait(ChatFormatting.LIGHT_PURPLE), rl -> new TraitConfig(rl, 80, 100, 3, 100)).desc("Grants a random potion trait with same level. When hit target, remove %s beneficial effects, deal %s more damage for every harmful effects, and increase their duration by %s. At most increase to %ss.").lang("Drain").register();

    public static final RegistryEntry<ReprintTrait> REPRINT = L2Hostility.REGISTRATE.regTrait("reprint", () -> new ReprintTrait(ChatFormatting.LIGHT_PURPLE), rl -> new TraitConfig(rl, 100, 100, 1, 100)).desc("Mob will copy target enchantments, and deal %s more damage per enchantment point").lang("Reprint").register();

    public static final RegistryEntry<CounterStrikeTrait> STRIKE = L2Hostility.REGISTRATE.regTrait("counter_strike", () -> new CounterStrikeTrait(ChatFormatting.WHITE), rl -> new TraitConfig(rl, 50, 100, 1, 60).addWhitelist(e -> e.addTag(LHTagGen.MELEE_WEAPON_TARGET).add(EntityType.WARDEN))).desc("After attacked, it will attempt to perform a counter strike.").lang("Counter Strike").register();

    public static final RegistryEntry<GravityTrait> GRAVITY = L2Hostility.REGISTRATE.regTrait("gravity", () -> new GravityTrait(LHEffects.GRAVITY::get), rl -> new TraitConfig(rl, 50, 100, 3, 80)).desc("Increase gravity for mobs around it. Knock attackers downward when damaged.").lang("Gravity").register();

    public static final RegistryEntry<AuraEffectTrait> MOONWALK = L2Hostility.REGISTRATE.regTrait("moonwalk", () -> new AuraEffectTrait(LHEffects.MOONWALK::get), rl -> new TraitConfig(rl, 50, 100, 3, 80)).desc("Decrease gravity for mobs around it").lang("Moonwalk").register();

    public static final RegistryEntry<ArenaTrait> ARENA = L2Hostility.REGISTRATE.regTrait("arena", ArenaTrait::new, rl -> new TraitConfig(rl, 1000, 1, 1, 50).addWhitelist(pvd -> pvd.addTag(LHTagGen.SEMIBOSS))).desc("Players around it cannot place or break blocks. Immune damage from entities not affected by this.").lang("Arena").register();

    public static final RegistryEntry<MasterTrait> MASTER = L2Hostility.REGISTRATE.regTrait("master", () -> new MasterTrait(ChatFormatting.GOLD), rl -> new TraitConfig(rl, 200, 50, 1, 200)).desc("Summons minions around the mob. Some minions will protect master.").lang("Master").register();

    public static void register() {
    }
}