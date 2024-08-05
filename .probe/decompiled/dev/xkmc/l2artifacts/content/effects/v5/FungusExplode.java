package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class FungusExplode extends SetEffect {

    private final LinearFuncEntry range;

    private final LinearFuncEntry rate;

    public FungusExplode(LinearFuncEntry range, LinearFuncEntry rate) {
        super(0);
        this.range = range;
        this.rate = rate;
    }

    @Override
    public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
        LivingDamageEvent event = cache.getLivingDamageEvent();
        assert event != null;
        if (!event.getSource().is(L2DamageTypes.MAGIC)) {
            int r = (int) this.range.getFromRank(rank);
            float dmg = cache.getDamageDealt() * (float) this.rate.getFromRank(rank);
            GeneralEventHandler.schedule(() -> this.explode(player, cache.getAttackTarget(), r, dmg));
        }
    }

    private void explode(LivingEntity player, LivingEntity target, int r, float dmg) {
        for (Entity e : target.m_9236_().m_45933_(target, target.m_20191_().inflate((double) r))) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) e;
                if (le.hasEffect((MobEffect) ArtifactEffects.FUNGUS.get())) {
                    le.hurt(target.m_269291_().indirectMagic(target, player), dmg);
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int r = (int) this.range.getFromRank(rank);
        int p = (int) Math.round(this.rate.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", p, r));
    }
}