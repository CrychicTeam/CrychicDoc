package dev.xkmc.l2hostility.init.entries;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public class LHRegistrate extends L2Registrate {

    private final List<String> LIST = new ArrayList();

    public final List<Consumer<ConfigDataProvider.Collector>> CONFIGS = new ArrayList();

    public LHRegistrate(String modid) {
        super(modid);
    }

    public final <T extends MobTrait> TraitBuilder<T, LHRegistrate> regTrait(String id, NonNullSupplier<T> sup, NonNullFunction<ResourceLocation, TraitConfig> config) {
        this.LIST.add(id);
        return (TraitBuilder<T, LHRegistrate>) ((TraitBuilder) this.entry(id, cb -> new TraitBuilder<>(this, this, id, cb, sup, config))).item(TraitSymbol::new).build();
    }

    public List<String> getList() {
        return this.LIST;
    }

    public void addTraitConfig(Consumer<ConfigDataProvider.Collector> cons) {
        this.CONFIGS.add(cons);
    }
}