package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import net.minecraft.world.item.Item;

public class BowBuilder extends BaseStatBuilder<BowBuilder, GenericBowItem, Item> {

    BowBuilder(BowArrowStatConfig config, RegistryEntry<GenericBowItem> bow) {
        super(config, config.bow_effects, config.bow_stats, bow);
    }

    public BowBuilder damage(double val) {
        return this.putStat((BowArrowStatType) ArcheryRegister.DAMAGE.get(), val);
    }

    public BowBuilder punch(double val) {
        return this.putStat((BowArrowStatType) ArcheryRegister.PUNCH.get(), val);
    }

    public BowBuilder speed(double val) {
        return this.putStat((BowArrowStatType) ArcheryRegister.SPEED.get(), val);
    }

    public BowBuilder bothTimes(double val) {
        this.putStat((BowArrowStatType) ArcheryRegister.PULL_TIME.get(), val);
        this.putStat((BowArrowStatType) ArcheryRegister.FOV_TIME.get(), val);
        return this;
    }

    public BowBuilder fovs(int time, double fov) {
        this.putStat((BowArrowStatType) ArcheryRegister.FOV_TIME.get(), (double) time);
        this.putStat((BowArrowStatType) ArcheryRegister.FOV.get(), fov);
        return this;
    }
}