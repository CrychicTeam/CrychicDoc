package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import java.util.HashMap;
import java.util.List;
import net.minecraft.network.chat.Component;

public record ArrowConfig(GenericArrowItem id, int infLevel, List<BowArrowFeature> feature) implements IGeneralConfig {

    private double getValue(BowArrowStatType type) {
        HashMap<BowArrowStatType, Double> map = (HashMap<BowArrowStatType, Double>) BowArrowStatConfig.get().arrow_stats.get(this.id);
        return map == null ? type.getDefault() : (Double) map.getOrDefault(type, type.getDefault());
    }

    public PotionArrowFeature getEffects() {
        return BowArrowStatConfig.get().getArrowEffects(this.id);
    }

    @Override
    public float damage() {
        return (float) this.getValue((BowArrowStatType) ArcheryRegister.DAMAGE.get());
    }

    @Override
    public int punch() {
        return (int) this.getValue((BowArrowStatType) ArcheryRegister.PUNCH.get());
    }

    public void addTooltip(List<Component> list) {
        LangData.STAT_DAMAGE.getWithSign(list, (double) this.damage());
        LangData.STAT_PUNCH.getWithSign(list, (double) this.punch());
        if (this.infLevel() == 2) {
            list.add(LangData.FEATURE_INFINITY.get());
        } else if (this.infLevel() == 1) {
            list.add(LangData.FEATURE_INFINITY_ADV.get());
        }
        PotionArrowFeature.addTooltip(this.getEffects().instances(), list);
    }
}