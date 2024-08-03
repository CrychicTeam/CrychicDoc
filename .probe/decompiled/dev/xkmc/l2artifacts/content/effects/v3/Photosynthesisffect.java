package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class Photosynthesisffect extends PlayerOnlySetEffect {

    private final LinearFuncEntry period;

    private final LinearFuncEntry lightLow;

    private final LinearFuncEntry lightHigh;

    public Photosynthesisffect(LinearFuncEntry period, LinearFuncEntry lightLow, LinearFuncEntry lightHigh) {
        super(0);
        this.period = period;
        this.lightLow = lightLow;
        this.lightHigh = lightHigh;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double time = this.period.getFromRank(rank) / 20.0;
        int lo = (int) Math.round(this.lightLow.getFromRank(rank));
        int hi = (int) Math.round(this.lightHigh.getFromRank(rank));
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", time, hi, lo));
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            if (!player.m_9236_().isClientSide()) {
                if ((double) player.f_19797_ % this.period.getFromRank(rank) == 0.0) {
                    int sun = PlayerLight.playerUnderSun(player);
                    int light = PlayerLight.playerLight(player);
                    if ((double) sun >= this.lightHigh.getFromRank(rank)) {
                        float sat = player.getFoodData().getSaturationLevel();
                        int food = player.getFoodData().getFoodLevel();
                        if (sat < (float) food) {
                            player.getFoodData().setSaturation(Math.min((float) food, sat + 1.0F));
                        } else {
                            player.getFoodData().setFoodLevel(Math.min(20, food + 1));
                        }
                    } else if ((double) light < this.lightLow.getFromRank(rank)) {
                        player.getFoodData().addExhaustion(0.1F);
                    }
                }
            }
        }
    }
}