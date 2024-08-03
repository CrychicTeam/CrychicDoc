package dev.xkmc.l2archery.content.upgrade;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.base.NamedEntry;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraftforge.common.util.Lazy;

public class Upgrade extends NamedEntry<Upgrade> {

    private final Supplier<BowArrowFeature> feature;

    public Upgrade(Supplier<BowArrowFeature> feature) {
        super(ArcheryRegister.UPGRADE);
        this.feature = Lazy.of(feature);
    }

    public Upgrade(Function<Upgrade, BowArrowFeature> feature) {
        super(ArcheryRegister.UPGRADE);
        this.feature = () -> (BowArrowFeature) feature.apply(this);
    }

    public BowArrowFeature getFeature() {
        return (BowArrowFeature) this.feature.get();
    }

    public boolean allow(GenericBowItem bow) {
        return ((BowArrowFeature) this.feature.get()).allow(bow.config);
    }
}