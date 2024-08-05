package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FrozeSlowEffect extends SetEffect {

    private final LinearFuncEntry factor;

    private final LinearFuncEntry period;

    private final LinearFuncEntry level;

    public FrozeSlowEffect(LinearFuncEntry factor, LinearFuncEntry period, LinearFuncEntry level) {
        super(0);
        this.factor = factor;
        this.period = period;
        this.level = level;
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if (!player.m_6060_()) {
            EffectUtil.addEffect(event.getAttackTarget(), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) this.period.getFromRank(rank), (int) this.level.getFromRank(rank)), EffectUtil.AddReason.NONE, player);
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double dmg = this.factor.getFromRank(rank) * 100.0;
        double period = this.period.getFromRank(rank) / 20.0;
        Component level = Component.translatable("potion.potency." + (int) this.level.getFromRank(rank));
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", (int) Math.round(dmg), level, period));
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            cache.addDealtModifier(DamageModifier.multTotal((float) this.factor.getFromRank(rank)));
        }
    }
}