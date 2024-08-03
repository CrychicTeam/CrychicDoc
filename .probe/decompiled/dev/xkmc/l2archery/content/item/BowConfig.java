package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import java.util.HashMap;
import java.util.List;

public record BowConfig(GenericBowItem id, int rank, List<BowArrowFeature> feature) implements IBowConfig {

    private double getValue(BowArrowStatType type) {
        HashMap<BowArrowStatType, Double> map = (HashMap<BowArrowStatType, Double>) BowArrowStatConfig.get().bow_stats.get(this.id);
        return map == null ? type.getDefault() : (Double) map.getOrDefault(type, type.getDefault());
    }

    @Override
    public PotionArrowFeature getEffects() {
        return BowArrowStatConfig.get().getBowEffects(this.id);
    }

    @Override
    public float damage() {
        return (float) this.getValue((BowArrowStatType) ArcheryRegister.DAMAGE.get());
    }

    @Override
    public int punch() {
        return (int) this.getValue((BowArrowStatType) ArcheryRegister.PUNCH.get());
    }

    @Override
    public int pull_time() {
        return (int) this.getValue((BowArrowStatType) ArcheryRegister.PULL_TIME.get());
    }

    @Override
    public float speed() {
        return (float) this.getValue((BowArrowStatType) ArcheryRegister.SPEED.get());
    }

    @Override
    public int fov_time() {
        return (int) this.getValue((BowArrowStatType) ArcheryRegister.FOV_TIME.get());
    }

    @Override
    public float fov() {
        return (float) this.getValue((BowArrowStatType) ArcheryRegister.FOV.get());
    }
}