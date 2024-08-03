package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ConduitModifier extends GolemModifier {

    private static final String STR_ATK = "l2complements:conduit_attack";

    private static final String STR_SPEED = "l2complements:conduit_speed";

    private static final String STR_ARMOR = "l2complements:conduit_armor";

    private static final String STR_TOUGH = "l2complements:conduit_toughness";

    private static final UUID ID_ATK = MathHelper.getUUIDFromString("l2complements:conduit_attack");

    private static final UUID ID_SPEED = MathHelper.getUUIDFromString("l2complements:conduit_speed");

    private static final UUID ID_ARMOR = MathHelper.getUUIDFromString("l2complements:conduit_armor");

    private static final UUID ID_TOUGH = MathHelper.getUUIDFromString("l2complements:conduit_toughness");

    public ConduitModifier() {
        super(StatFilterType.MASS, 4);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && entity.m_20071_()) {
            event.setAmount((float) ((double) event.getAmount() * Math.pow(1.0 - MGConfig.COMMON.conduitBoostReduction.get(), (double) level)));
        }
    }

    @Override
    public List<MutableComponent> getDetail(int level) {
        int red = (int) Math.round(100.0 * Math.pow(1.0 - MGConfig.COMMON.conduitBoostReduction.get(), (double) level));
        int atk = (int) Math.round(MGConfig.COMMON.conduitBoostAttack.get() * (double) level * 100.0);
        int spe = (int) Math.round(MGConfig.COMMON.conduitBoostSpeed.get() * (double) level * 100.0);
        int armor = MGConfig.COMMON.conduitBoostArmor.get() * level;
        int tough = MGConfig.COMMON.conduitBoostToughness.get() * level;
        int damage = MGConfig.COMMON.conduitDamage.get() * level;
        int freq = MGConfig.COMMON.conduitCooldown.get() / 20;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", red, freq, damage).withStyle(ChatFormatting.GREEN), Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()).append(": +" + atk + "%").withStyle(ChatFormatting.BLUE), Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()).append(": +" + spe + "%").withStyle(ChatFormatting.BLUE), Component.translatable(Attributes.ARMOR.getDescriptionId()).append(": +" + armor).withStyle(ChatFormatting.BLUE), Component.translatable(Attributes.ARMOR_TOUGHNESS.getDescriptionId()).append(": +" + tough).withStyle(ChatFormatting.BLUE));
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        AttributeInstance gatk = golem.m_21051_(Attributes.ATTACK_DAMAGE);
        AttributeInstance gspe = golem.m_21051_(Attributes.MOVEMENT_SPEED);
        AttributeInstance garm = golem.m_21051_(Attributes.ARMOR);
        AttributeInstance gtgh = golem.m_21051_(Attributes.ARMOR_TOUGHNESS);
        if (!golem.m_20071_()) {
            if (gatk != null && gatk.getModifier(ID_ATK) != null) {
                gatk.removeModifier(ID_ATK);
            }
            if (gspe != null && gspe.getModifier(ID_SPEED) != null) {
                gspe.removeModifier(ID_SPEED);
            }
            if (garm != null && garm.getModifier(ID_ARMOR) != null) {
                garm.removeModifier(ID_ARMOR);
            }
            if (gtgh != null && gtgh.getModifier(ID_TOUGH) != null) {
                gtgh.removeModifier(ID_TOUGH);
            }
        } else {
            double atk = MGConfig.COMMON.conduitBoostAttack.get() * (double) level;
            double spe = MGConfig.COMMON.conduitBoostSpeed.get() * (double) level;
            int armor = MGConfig.COMMON.conduitBoostArmor.get() * level;
            double tough = (double) (MGConfig.COMMON.conduitBoostToughness.get() * level);
            if (gatk != null && gatk.getModifier(ID_ATK) == null) {
                gatk.addTransientModifier(new AttributeModifier(ID_ATK, "l2complements:conduit_attack", atk, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (gspe != null && gspe.getModifier(ID_SPEED) == null) {
                gspe.addTransientModifier(new AttributeModifier(ID_SPEED, "l2complements:conduit_speed", spe, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (garm != null && garm.getModifier(ID_ARMOR) == null) {
                garm.addTransientModifier(new AttributeModifier(ID_ARMOR, "l2complements:conduit_armor", (double) armor, AttributeModifier.Operation.ADDITION));
            }
            if (gtgh != null && gtgh.getModifier(ID_TOUGH) == null) {
                gtgh.addTransientModifier(new AttributeModifier(ID_TOUGH, "l2complements:conduit_toughness", tough, AttributeModifier.Operation.ADDITION));
            }
            LivingEntity target = golem.m_5448_();
            if (level > 0 && target != null && target.hurtTime == 0 && target.m_20071_() && golem.f_19797_ % MGConfig.COMMON.conduitCooldown.get() == 0) {
                int damage = MGConfig.COMMON.conduitDamage.get() * level;
                Level pLevel = golem.m_9236_();
                pLevel.playSound(null, target.m_20185_(), target.m_20186_(), target.m_20189_(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.NEUTRAL, 1.0F, 1.0F);
                golem.m_5448_().hurt(pLevel.damageSources().indirectMagic(golem, golem), (float) damage);
            }
        }
    }
}