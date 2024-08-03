package dev.xkmc.l2archery.content.feature.core;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.item.IBowConfig;
import dev.xkmc.l2archery.content.upgrade.StatHolder;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.MutableComponent;

public record StatFeature(float fov, int fov_time, float damage, int punch, float speed) implements BowArrowFeature, IBowConfig {

    public static final StatFeature NOOP = new StatFeature(1.0F, 0, 1.0F, 0, 1.0F);

    @Override
    public int pull_time() {
        return 1;
    }

    @Override
    public PotionArrowFeature getEffects() {
        return PotionArrowFeature.NULL;
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        if (this.damage() != NOOP.damage()) {
            list.add(LangData.STAT_DAMAGE.get("x" + this.damage()));
        }
        if (this.punch() != NOOP.punch()) {
            list.add(LangData.STAT_PUNCH.get("+" + this.punch()));
        }
        if (this.fov() != NOOP.fov()) {
            list.add(LangData.STAT_FOV.get("x" + this.fov()));
        }
        if (this.speed() != NOOP.speed()) {
            list.add(LangData.STAT_SPEED.get("x" + this.speed()));
        }
    }

    @Override
    public boolean allow(IBowConfig config) {
        return this.damage > 1.0F && config.damage() == 0.0F ? false : !(this.fov() > 1.0F) || !(1.0F / (1.0F - config.fov()) >= 9.9F);
    }

    public boolean addStatHolder(Set<StatHolder> set) {
        boolean success = true;
        if (this.damage() != NOOP.damage()) {
            success &= set.add(StatHolder.DAMAGE);
        }
        if (this.punch() != NOOP.punch()) {
            success &= set.add(StatHolder.PUNCH);
        }
        if (this.fov() != NOOP.fov()) {
            success &= set.add(StatHolder.FOV);
        }
        if (this.speed() != NOOP.speed()) {
            success &= set.add(StatHolder.SPEED);
        }
        return success;
    }
}