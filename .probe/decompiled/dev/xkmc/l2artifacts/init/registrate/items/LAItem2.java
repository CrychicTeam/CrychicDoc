package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.effects.v2.ExecutorLimitEffect;
import dev.xkmc.l2artifacts.content.effects.v2.ExecutorSelfHurtEffect;
import dev.xkmc.l2artifacts.content.effects.v2.FrozeBreakEffect;
import dev.xkmc.l2artifacts.content.effects.v2.FrozeSlowEffect;
import dev.xkmc.l2artifacts.content.effects.v2.PhysicalDamageEffect;
import dev.xkmc.l2artifacts.content.effects.v2.WrathEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LAItem2 {

    public static final SetEntry<ArtifactSet> SET_FROZE;

    public static final SetEntry<ArtifactSet> SET_EXECUTOR;

    public static final SetEntry<ArtifactSet> SET_PHYSICAL;

    public static final SetEntry<ArtifactSet> SET_WRATH;

    public static final RegistryEntry<FrozeSlowEffect> EFF_FROZE_SLOW;

    public static final RegistryEntry<FrozeBreakEffect> EFF_FROZE_BREAK;

    public static final RegistryEntry<ExecutorSelfHurtEffect> EFF_EXECUTOR_SELF_HURT;

    public static final RegistryEntry<ExecutorLimitEffect> EFF_EXECUTOR_LIMIT;

    public static final RegistryEntry<PhysicalDamageEffect> EFF_PHYSICAL_DAMAGE;

    public static final RegistryEntry<AttributeSetEffect> EFF_PHYSICAL_ARMOR;

    public static final RegistryEntry<WrathEffect> EFF_WRATH_POISON;

    public static final RegistryEntry<WrathEffect> EFF_WRATH_SLOW;

    public static final RegistryEntry<WrathEffect> EFF_WRATH_FIRE;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("froze");
        LinearFuncEntry damage = helper.regLinear("froze_slow_fire_damage", 1.2, 0.0);
        LinearFuncEntry period = helper.regLinear("froze_slow_period", 80.0, 40.0);
        LinearFuncEntry level = helper.regLinear("froze_slow_level", 0.0, 1.0);
        LinearFuncEntry factor = helper.regLinear("froze_break", 1.2, 0.1);
        EFF_FROZE_SLOW = helper.setEffect("froze_slow", () -> new FrozeSlowEffect(damage, period, level)).desc("Frozen Blade", "Take %s%% fire damage. When not on fire, apply level %s slow effect on attack target for %s seconds").register();
        EFF_FROZE_BREAK = helper.setEffect("froze_break", () -> new FrozeBreakEffect(factor)).desc("Ice Breaker", "Attacks targetting slowed enemy will have %s%% more damage").register();
        SET_FROZE = helper.regSet(1, 5, "Frozen Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem2.EFF_FROZE_SLOW.get()).add(5, (SetEffect) LAItem2.EFF_FROZE_BREAK.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("executor");
        damage = helper.regLinear("executor_attack", 0.3, 0.15);
        period = helper.regLinear("executor_self_hurt", 0.2, 0.0);
        level = helper.regLinear("executor_limit", 0.3, -0.05);
        EFF_EXECUTOR_SELF_HURT = helper.setEffect("executor_self_hurt", () -> new ExecutorSelfHurtEffect(new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, damage, true), period)).desc("Brutal Execution", "When kill enemies, deal real damage to oneself equal to %s%% of enemies' max health.").register();
        EFF_EXECUTOR_LIMIT = helper.setEffect("executor_limit", () -> new ExecutorLimitEffect(level)).desc("Cold Hearted", "The damage dealt to oneself will be capped to one's max health, and then reduced to %s%%").register();
        SET_EXECUTOR = helper.regSet(1, 5, "Executor Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem2.EFF_EXECUTOR_SELF_HURT.get()).add(5, (SetEffect) LAItem2.EFF_EXECUTOR_LIMIT.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("physical");
        damage = helper.regLinear("physical_attack", 0.2, 0.1);
        period = helper.regLinear("physical_armor", 4.0, 2.0);
        level = helper.regLinear("physical_reduce_magic", 0.5, 0.0);
        EFF_PHYSICAL_DAMAGE = helper.setEffect("physical_damage", () -> new PhysicalDamageEffect(new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, damage, true), level)).desc("Barbaric Attack", "Magical damage dealt will be reduced to %s%%").register();
        EFF_PHYSICAL_ARMOR = helper.setEffect("physical_armor", () -> new AttributeSetEffect(new AttrSetEntry(() -> Attributes.ARMOR, AttributeModifier.Operation.ADDITION, period, false))).lang("Survival Instinct").register();
        SET_PHYSICAL = helper.regSet(1, 5, "Courage Set").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem2.EFF_PHYSICAL_DAMAGE.get()).add(5, (SetEffect) LAItem2.EFF_PHYSICAL_ARMOR.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("wrath");
        damage = helper.regLinear("wrath_decrease", 0.8, 0.0);
        period = helper.regLinear("wrath_increase", 1.2, 0.1);
        EFF_WRATH_POISON = helper.setEffect("wrath_poison", () -> new WrathEffect(e -> e.hasEffect(MobEffects.POISON), damage, period)).desc("Bad Day Encounters Bad Luck", "When target is poisoned, increase damage to %s%%. Otherwise, decrease damage to %s%%.").register();
        EFF_WRATH_SLOW = helper.setEffect("wrath_slow", () -> new WrathEffect(e -> e.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), damage, period)).desc("Snow Storm Encounters Blitz Winter", "When target is slowed, increase damage to %s%%. Otherwise, decrease damage to %s%%.").register();
        EFF_WRATH_FIRE = helper.setEffect("wrath_fire", () -> new WrathEffect(Entity::m_6060_, damage, period)).desc("Emergency Encounters Unwanted Fight", "When target is on fire, increase damage to %s%%. Otherwise, decrease damage to %s%%.").register();
        SET_WRATH = helper.regSet(1, 5, "Curse of Bad Luck").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem2.EFF_WRATH_POISON.get()).add(3, (SetEffect) LAItem2.EFF_WRATH_SLOW.get()).add(5, (SetEffect) LAItem2.EFF_WRATH_FIRE.get())).register();
    }
}