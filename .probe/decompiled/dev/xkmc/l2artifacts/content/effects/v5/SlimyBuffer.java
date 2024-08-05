package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class SlimyBuffer extends SetEffect {

    private final LinearFuncEntry reduction;

    public SlimyBuffer(LinearFuncEntry reduction) {
        super(0);
        this.reduction = reduction;
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL)) {
            cache.addDealtModifier(DamageModifier.multTotal(1.0F - (float) this.reduction.getFromRank(rank)));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.reduction.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val));
    }
}