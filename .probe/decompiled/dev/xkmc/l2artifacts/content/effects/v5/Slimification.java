package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class Slimification extends SetEffect {

    private final LinearFuncEntry reduction;

    private final LinearFuncEntry penalty;

    public Slimification(LinearFuncEntry reduction, LinearFuncEntry penalty) {
        super(0);
        this.reduction = reduction;
        this.penalty = penalty;
    }

    @Override
    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (source.is(L2DamageTypes.DIRECT) || source.is(DamageTypeTags.IS_PROJECTILE)) {
            cache.addDealtModifier(DamageModifier.multTotal(1.0F - (float) this.reduction.getFromRank(rank)));
        }
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_FREEZING) || source.is(DamageTypeTags.IS_EXPLOSION) || source.is(L2DamageTypes.MAGIC)) {
            cache.addDealtModifier(DamageModifier.multTotal(1.0F + (float) this.penalty.getFromRank(rank)));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.reduction.getFromRank(rank) * 100.0);
        int pen = (int) Math.round(this.penalty.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val, pen));
    }
}