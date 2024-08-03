package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public class FleshAttack extends SetEffect {

    private final LinearFuncEntry thr;

    private final LinearFuncEntry atk;

    public FleshAttack(LinearFuncEntry thr, LinearFuncEntry atk) {
        super(0);
        this.thr = thr;
        this.atk = atk;
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if ((double) event.getAttackTarget().getHealth() <= (double) event.getAttackTarget().getMaxHealth() * this.thr.getFromRank(rank)) {
            event.addHurtModifier(DamageModifier.multBase((float) this.atk.getFromRank(rank)));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int php = (int) Math.round(this.thr.getFromRank(rank) * 100.0);
        int val = (int) Math.round(this.atk.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", php, val));
    }
}