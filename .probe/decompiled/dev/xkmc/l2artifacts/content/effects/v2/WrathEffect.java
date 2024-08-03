package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public class WrathEffect extends SetEffect {

    private final Predicate<LivingEntity> pred;

    private final LinearFuncEntry dec;

    private final LinearFuncEntry inc;

    public WrathEffect(Predicate<LivingEntity> pred, LinearFuncEntry dec, LinearFuncEntry inc) {
        super(0);
        this.pred = pred;
        this.dec = dec;
        this.inc = inc;
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        boolean bool = this.pred.test(event.getAttackTarget());
        double factor = bool ? this.inc.getFromRank(rank) : this.dec.getFromRank(rank);
        event.addHurtModifier(DamageModifier.multTotal((float) factor));
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double inc = this.inc.getFromRank(rank) * 100.0;
        double dec = this.dec.getFromRank(rank) * 100.0;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", (int) Math.round(inc), (int) Math.round(dec)));
    }
}