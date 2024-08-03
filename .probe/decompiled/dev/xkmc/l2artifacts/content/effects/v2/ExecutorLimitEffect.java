package dev.xkmc.l2artifacts.content.effects.v2;

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

public class ExecutorLimitEffect extends SetEffect {

    private final LinearFuncEntry factor;

    public ExecutorLimitEffect(LinearFuncEntry factor) {
        super(0);
        this.factor = factor;
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.getEntity() == player) {
            cache.addDealtModifier(DamageModifier.nonlinearPre(546, f -> Math.min(f, player.getMaxHealth()) * (float) this.factor.getFromRank(rank)));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double amount = this.factor.getFromRank(rank) * 100.0;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", (int) Math.round(amount)));
    }
}