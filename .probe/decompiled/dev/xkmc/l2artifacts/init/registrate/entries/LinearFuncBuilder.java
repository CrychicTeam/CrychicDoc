package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LinearFuncBuilder<P> extends AbstractBuilder<LinearFuncHandle, LinearFuncHandle, P, LinearFuncBuilder<P>> {

    private final NonNullSupplier<LinearFuncHandle> sup;

    private final SetRegHelper set;

    private final double base;

    private final double slope;

    public LinearFuncBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, SetRegHelper set, NonNullSupplier<LinearFuncHandle> sup, double base, double slope) {
        super(owner, parent, name, callback, ArtifactTypeRegistry.LINEAR.key());
        this.sup = sup;
        this.base = base;
        this.slope = slope;
        this.set = set;
    }

    protected RegistryEntry<LinearFuncHandle> createEntryWrapper(RegistryObject<LinearFuncHandle> delegate) {
        return new LinearFuncEntry((ArtifactRegistrate) Wrappers.cast(this.getOwner()), this.set, delegate, this.base, this.slope);
    }

    @NonnullType
    @NotNull
    protected LinearFuncHandle createEntry() {
        return (LinearFuncHandle) this.sup.get();
    }
}