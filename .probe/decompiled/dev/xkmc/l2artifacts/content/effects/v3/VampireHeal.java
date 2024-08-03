package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public class VampireHeal extends SetEffect {

    private final LinearFuncEntry light;

    private final LinearFuncEntry percent;

    public VampireHeal(LinearFuncEntry light, LinearFuncEntry percent) {
        super(0);
        this.light = light;
        this.percent = percent;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int l = (int) Math.round(this.light.getFromRank(rank));
        int p = (int) Math.round(this.percent.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", l, p));
    }

    @Override
    public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if (!player.m_9236_().isClientSide()) {
            int light = PlayerLight.playerUnderSun(player);
            if ((double) light <= this.light.getFromRank(rank)) {
                player.heal((float) ((double) event.getDamageDealt() * this.percent.getFromRank(rank)));
            }
        }
    }
}