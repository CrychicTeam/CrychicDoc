package dev.xkmc.l2artifacts.init.registrate.entries;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2library.base.L2Registrate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.resources.ResourceLocation;

public class ArtifactRegistrate extends L2Registrate {

    public final TreeMap<ResourceLocation, SetEntry<?>> SET_MAP = new TreeMap();

    public final List<SetEntry<?>> SET_LIST = new ArrayList();

    public final Multimap<ResourceLocation, LinearFuncEntry> LINEAR_LIST = LinkedListMultimap.create();

    public ArtifactRegistrate() {
        super("l2artifacts");
    }

    final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ArtifactRegistrate> regSet(String id, NonNullSupplier<T> sup, int min_rank, int max_rank, String name) {
        return ((SetBuilder) this.entry(id, cb -> new SetBuilder<>(this, this, id, cb, sup, min_rank, max_rank))).lang(name);
    }

    final LinearFuncEntry regLinear(String id, SetRegHelper set, double base, double slope) {
        return (LinearFuncEntry) ((LinearFuncBuilder) this.entry(id, cb -> new LinearFuncBuilder<>(this, this, id, cb, set, LinearFuncHandle::new, base, slope))).register();
    }

    <T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
        return (SetEffectBuilder<T, ArtifactRegistrate>) this.entry(id, cb -> new SetEffectBuilder<>(this, this, id, cb, sup));
    }

    public SetRegHelper getSetHelper(String id) {
        return new SetRegHelper(this, id);
    }
}