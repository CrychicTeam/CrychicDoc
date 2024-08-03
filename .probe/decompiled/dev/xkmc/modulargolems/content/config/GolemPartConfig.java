package dev.xkmc.modulargolems.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SerialClass
public class GolemPartConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Item, HashMap<StatFilterType, Double>> filters = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ResourceLocation, HashMap<GolemStatType, Double>> magnifiers = new HashMap();

    public static GolemPartConfig get() {
        return ModularGolems.PARTS.getMerged();
    }

    public HashMap<StatFilterType, Double> getFilter(GolemPart<?, ?> part) {
        return (HashMap<StatFilterType, Double>) this.filters.get(part);
    }

    public HashMap<GolemStatType, Double> getMagnifier(GolemType<?, ?> part) {
        return (HashMap<GolemStatType, Double>) this.magnifiers.get(part.getRegistryName());
    }

    public GolemPartConfig.PartBuilder addPart(GolemPart<?, ?> part) {
        return new GolemPartConfig.PartBuilder(this, part);
    }

    public GolemPartConfig.HolderBuilder addEntity(GolemType<?, ?> part) {
        return new GolemPartConfig.HolderBuilder(this, part);
    }

    public static class HolderBuilder {

        private final GolemPartConfig parent;

        private final GolemType<?, ?> part;

        private final HashMap<GolemStatType, Double> filter = new HashMap();

        private HolderBuilder(GolemPartConfig parent, GolemType<?, ?> part) {
            this.parent = parent;
            this.part = part;
        }

        public GolemPartConfig.HolderBuilder addFilter(GolemStatType type, double val) {
            this.filter.put(type, val);
            return this;
        }

        public GolemPartConfig end() {
            this.parent.magnifiers.put(this.part.getRegistryName(), this.filter);
            return this.parent;
        }
    }

    public static class PartBuilder {

        private final GolemPartConfig parent;

        private final GolemPart<?, ?> part;

        private final HashMap<StatFilterType, Double> filter = new HashMap();

        private PartBuilder(GolemPartConfig parent, GolemPart<?, ?> part) {
            this.parent = parent;
            this.part = part;
        }

        public GolemPartConfig.PartBuilder addFilter(StatFilterType type, double val) {
            this.filter.put(type, val);
            return this;
        }

        public GolemPartConfig end() {
            this.parent.filters.put(this.part, this.filter);
            return this.parent;
        }
    }
}