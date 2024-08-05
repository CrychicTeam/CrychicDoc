package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public class PerfectionAbsorption extends SetEffect {

    private final LinearFuncEntry period;

    private final LinearFuncEntry max;

    public PerfectionAbsorption(LinearFuncEntry period, LinearFuncEntry max) {
        super(0);
        this.period = period;
        this.max = max;
    }

    @Override
    public void tick(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            int max = (int) this.max.getFromRank(rank);
            if ((double) player.f_19797_ % this.period.getFromRank(rank) == 0.0 && player.getHealth() >= player.getMaxHealth()) {
                double current = (double) player.getAbsorptionAmount();
                if (current < (double) max) {
                    player.setAbsorptionAmount((float) Math.min((double) max, current + 1.0));
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double period = this.period.getFromRank(rank) / 20.0;
        int max = (int) this.max.getFromRank(rank);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", period, max));
    }
}