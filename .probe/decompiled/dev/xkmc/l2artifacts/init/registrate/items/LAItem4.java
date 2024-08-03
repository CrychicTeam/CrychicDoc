package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.TimedCASetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.effects.persistent.SimpleCPSetEffect;
import dev.xkmc.l2artifacts.content.effects.v4.AbyssAttackEffect;
import dev.xkmc.l2artifacts.content.effects.v4.AttackStrikeEffect;
import dev.xkmc.l2artifacts.content.effects.v4.ImmobileEffect;
import dev.xkmc.l2artifacts.content.effects.v4.LongShooterEffect;
import dev.xkmc.l2artifacts.content.effects.v4.LongShooterPersistentEffect;
import dev.xkmc.l2artifacts.content.effects.v4.LuckAttackEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LAItem4 {

    public static final SetEntry<ArtifactSet> SET_ANCIENT;

    public static final SetEntry<ArtifactSet> SET_LUCKCLOVER;

    public static final SetEntry<ArtifactSet> SET_ABYSSMEDAL;

    public static final SetEntry<ArtifactSet> SET_LONGSHOOTER;

    public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_1;

    public static final RegistryEntry<SimpleCPSetEffect> EFF_ANCIENT_2;

    public static final RegistryEntry<AttackStrikeEffect> EFF_ANCIENT_3;

    public static final RegistryEntry<ImmobileEffect> EFF_ANCIENT_4;

    public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_5;

    public static final RegistryEntry<LongShooterEffect> EFF_LONGSHOOTER_3;

    public static final RegistryEntry<LongShooterPersistentEffect> EFF_LONGSHOOTER_4;

    public static final RegistryEntry<LuckAttackEffect> EFF_LUCKCLOVER_3;

    public static final RegistryEntry<LuckAttackEffect> EFF_LUCKCLOVER_4;

    public static final RegistryEntry<AttributeSetEffect> EFF_ABYSSMEDAL_3;

    public static final RegistryEntry<AbyssAttackEffect> EFF_ABYSSMEDAL_5;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("ancient_scroll");
        LinearFuncEntry threshold = helper.regLinear("ancient_threshold", 20.0, 0.0);
        LinearFuncEntry speed = helper.regLinear("ancient_speed", 0.2, 0.1);
        LinearFuncEntry period = helper.regLinear("ancient_heal_period", 60.0, 0.0);
        LinearFuncEntry heal = helper.regLinear("ancient_heal", 2.0, 1.0);
        LinearFuncEntry duration = helper.regLinear("ancient_strike_duration", 40.0, 20.0);
        LinearFuncEntry count = helper.regLinear("ancient_strike_count", 3.0, 0.0);
        LinearFuncEntry attack = helper.regLinear("ancient_attack", 0.2, 0.1);
        LinearFuncEntry protection = helper.regLinear("ancient_protection", 0.8, -0.1);
        LinearFuncEntry speed5 = helper.regLinear("ancient_speed_5", 0.2, 0.1);
        LinearFuncEntry attack5 = helper.regLinear("ancient_attack_5", 0.2, 0.1);
        EFF_ANCIENT_1 = helper.setEffect("ancient_scroll_1", () -> new TimedCASetEffect(Entity::m_20142_, threshold, new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, speed, true))).desc("Run like wind", "After sprinting for %s seconds:").register();
        EFF_ANCIENT_2 = helper.setEffect("ancient_scroll_2", () -> new SimpleCPSetEffect(period, e -> !e.m_20142_(), (e, rank) -> e.m_5634_((float) heal.getFromRank(rank)), (rank, id) -> Component.translatable(id, period.getFromRank(rank) / 20.0, heal.getFromRank(rank)))).desc("Recover like plant", "Every %s seconds, heal %s health point").register();
        EFF_ANCIENT_3 = helper.setEffect("ancient_scroll_3", () -> new AttackStrikeEffect(duration, count, new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, attack, true))).desc("Plunder like fire", "After attacking with full power for %s strikes with interval less than %s seconds:").register();
        EFF_ANCIENT_4 = helper.setEffect("ancient_scroll_4", () -> new ImmobileEffect(protection, threshold)).desc("Solid as mountain", "After stay still for %s seconds: Damage taken is reduced to %s%% of original").register();
        EFF_ANCIENT_5 = helper.setEffect("ancient_scroll_5", () -> new TimedCASetEffect(Entity::m_6144_, threshold, new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, speed5, true), new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, attack5, true))).desc("Dark as night sky", "After sneaking for %s seconds:").register();
        SET_ANCIENT = helper.regSet(1, 5, "Ancient Scroll").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(1, (SetEffect) LAItem4.EFF_ANCIENT_1.get()).add(2, (SetEffect) LAItem4.EFF_ANCIENT_2.get()).add(3, (SetEffect) LAItem4.EFF_ANCIENT_3.get()).add(4, (SetEffect) LAItem4.EFF_ANCIENT_4.get()).add(5, (SetEffect) LAItem4.EFF_ANCIENT_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("luck_clover");
        threshold = helper.regLinear("luck_threshold", 40.0, 0.0);
        speed = helper.regLinear("luck_count_3", 3.0, 0.0);
        period = helper.regLinear("luck_count_4", 4.0, 0.0);
        heal = helper.regLinear("luck_rate", 1.0, 0.0);
        duration = helper.regLinear("luck_dmg", 1.0, 0.2);
        EFF_LUCKCLOVER_3 = helper.setEffect("luck_clover_3", () -> new LuckAttackEffect(threshold, speed, new AttrSetEntry(L2DamageTracker.CRIT_DMG::get, AttributeModifier.Operation.ADDITION, duration, true))).desc("Lucky number : 3", "The %s consecutive attacks are all within %s second:").register();
        EFF_LUCKCLOVER_4 = helper.setEffect("luck_clover_4", () -> new LuckAttackEffect(threshold, period, new AttrSetEntry(L2DamageTracker.CRIT_RATE::get, AttributeModifier.Operation.ADDITION, heal, true))).desc("Lucky number : 4", "The %s consecutive attacks are all within %s second:").register();
        SET_LUCKCLOVER = helper.regSet(4, 4, "Luck Clover").setSlots(ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem4.EFF_LUCKCLOVER_3.get()).add(4, (SetEffect) LAItem4.EFF_LUCKCLOVER_4.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("abyss_medal");
        threshold = helper.regLinear("abyss_level", 0.0, 0.2);
        speed = helper.regLinear("abyss_health", 0.4, 0.2);
        period = helper.regLinear("abyss_duration", 80.0, 20.0);
        heal = helper.regLinear("abyss_hurt", 0.2, 0.1);
        EFF_ABYSSMEDAL_3 = helper.setEffect("abyss_medal_3", () -> new AttributeSetEffect(new AttrSetEntry(() -> Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE, speed, true))).lang("Abyss Aggregate").register();
        EFF_ABYSSMEDAL_5 = helper.setEffect("abyss_medal_5", () -> new AbyssAttackEffect(period, threshold, heal, 0)).desc("Abyss Eclipse", "On attack, inflict %s and %s. -%s%% magic damage you take.").register();
        SET_ABYSSMEDAL = helper.regSet(1, 5, "Abyss Medal").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem4.EFF_ABYSSMEDAL_3.get()).add(5, (SetEffect) LAItem4.EFF_ABYSSMEDAL_5.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("long_shooter");
        threshold = helper.regLinear("long_shooter_atk", 0.6, 0.3);
        EFF_LONGSHOOTER_3 = helper.setEffect("long_shooter_3", () -> new LongShooterEffect(new AttrSetEntry(L2DamageTracker.BOW_STRENGTH::get, AttributeModifier.Operation.ADDITION, threshold, true))).desc("Focus of the long-range shooter", "When there is no Monster in the nearby 8 cells:").register();
        EFF_LONGSHOOTER_4 = helper.setEffect("long_shooter_4", () -> new LongShooterPersistentEffect(new AttrSetEntry(L2DamageTracker.BOW_STRENGTH::get, AttributeModifier.Operation.ADDITION, threshold, true))).desc("Last chance", "Set the effect of suit 3 to 6 squares, when approached, it still lasts for two seconds and gains two second acceleration").register();
        SET_LONGSHOOTER = helper.regSet(1, 5, "Long Shooter").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(3, (SetEffect) LAItem4.EFF_LONGSHOOTER_3.get()).add(4, (SetEffect) LAItem4.EFF_LONGSHOOTER_4.get())).register();
    }
}