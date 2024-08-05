package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ThermalMotive extends SetEffect {

    private final LinearFuncEntry atk;

    private final LinearFuncEntry duration;

    public ThermalMotive(LinearFuncEntry atk, LinearFuncEntry duration) {
        super(0);
        this.atk = atk;
        this.duration = duration;
    }

    @Override
    public boolean playerAttackedCancel(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            player.addEffect(new MobEffectInstance((MobEffect) ArtifactEffects.THERMAL_MOTIVE.get(), (int) this.duration.getFromRank(rank), rank - 1));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        MobEffectInstance ins = player.getEffect((MobEffect) ArtifactEffects.THERMAL_MOTIVE.get());
        if (ins != null) {
            event.addHurtModifier(DamageModifier.multBase((float) (1.0 + this.atk.getFromRank(rank))));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.atk.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val));
    }
}