package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FungusInfect extends SetEffect {

    private final LinearFuncEntry duration;

    public FungusInfect(LinearFuncEntry duration) {
        super(0);
        this.duration = duration;
    }

    private MobEffectInstance eff(int rank) {
        return new MobEffectInstance((MobEffect) ArtifactEffects.FUNGUS.get(), (int) this.duration.getFromRank(rank), rank - 1);
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        event.getAttackTarget().addEffect(this.eff(rank), player);
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", EffectDesc.getDesc(this.eff(rank), true)));
    }
}