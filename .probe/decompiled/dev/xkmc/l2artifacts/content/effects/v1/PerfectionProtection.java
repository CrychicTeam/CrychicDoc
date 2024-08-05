package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class PerfectionProtection extends SetEffect {

    private final LinearFuncEntry reduce;

    public PerfectionProtection(LinearFuncEntry reduce) {
        super(0);
        this.reduce = reduce;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int reduce = (int) Math.round(this.reduce.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", reduce));
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (!(player.getHealth() < player.getMaxHealth())) {
            cache.addDealtModifier(DamageModifier.multTotal((float) (1.0 - this.reduce.getFromRank(rank))));
        }
    }
}