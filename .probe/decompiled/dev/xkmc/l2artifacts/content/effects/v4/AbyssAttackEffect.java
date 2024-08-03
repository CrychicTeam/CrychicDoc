package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class AbyssAttackEffect extends SetEffect {

    private final LinearFuncEntry duration;

    private final LinearFuncEntry level;

    private final LinearFuncEntry hurt;

    public AbyssAttackEffect(LinearFuncEntry duration, LinearFuncEntry level, LinearFuncEntry hurt, int ids) {
        super(ids);
        this.duration = duration;
        this.level = level;
        this.hurt = hurt;
    }

    private MobEffectInstance weak(int rank) {
        return new MobEffectInstance(MobEffects.WEAKNESS, (int) this.duration.getFromRank(rank), (int) this.level.getFromRank(rank));
    }

    private MobEffectInstance wither(int rank) {
        return new MobEffectInstance(MobEffects.WITHER, (int) this.duration.getFromRank(rank), (int) this.level.getFromRank(rank));
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        event.getAttackTarget().addEffect(this.weak(rank), player);
        event.getAttackTarget().addEffect(this.wither(rank), player);
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.is(L2DamageTypes.MAGIC)) {
            cache.addDealtModifier(DamageModifier.multTotal(1.0F - (float) this.hurt.getFromRank(rank)));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.hurt.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", EffectDesc.getDesc(this.weak(rank), true), EffectDesc.getDesc(this.wither(rank), true), val));
    }
}