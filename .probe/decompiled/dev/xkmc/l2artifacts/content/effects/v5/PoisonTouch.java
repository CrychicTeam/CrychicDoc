package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PoisonTouch extends SetEffect {

    private static final List<MobEffect> LIST = List.of(MobEffects.POISON, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS);

    private final LinearFuncEntry chance;

    private final LinearFuncEntry duration;

    public PoisonTouch(LinearFuncEntry chance, LinearFuncEntry duration) {
        super(0);
        this.chance = chance;
        this.duration = duration;
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        for (MobEffect e : LIST) {
            if (player.getRandom().nextDouble() < this.chance.getFromRank(rank)) {
                event.getAttackTarget().addEffect(new MobEffectInstance(e, (int) this.duration.getFromRank(rank)), player);
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", Stream.concat(Stream.of(Component.literal((int) Math.round(this.chance.getFromRank(rank) * 100.0) + "")), LIST.stream().map(e -> EffectDesc.getDesc(new MobEffectInstance(e, (int) this.duration.getFromRank(rank)), true))).toArray()));
    }
}