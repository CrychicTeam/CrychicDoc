package net.minecraftforge.common.capabilities;

import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class CapabilityProvider<B extends ICapabilityProviderImpl<B>> implements ICapabilityProviderImpl<B> {

    @VisibleForTesting
    static boolean SUPPORTS_LAZY_CAPABILITIES = true;

    @NotNull
    private final Class<B> baseClass;

    @Nullable
    private CapabilityDispatcher capabilities;

    private boolean valid = true;

    private boolean isLazy = false;

    private Supplier<ICapabilityProvider> lazyParentSupplier = null;

    private CompoundTag lazyData = null;

    private boolean initialized = false;

    protected CapabilityProvider(Class<B> baseClass) {
        this(baseClass, false);
    }

    protected CapabilityProvider(Class<B> baseClass, boolean isLazy) {
        this.baseClass = baseClass;
        this.isLazy = SUPPORTS_LAZY_CAPABILITIES && isLazy;
    }

    protected final void gatherCapabilities() {
        this.gatherCapabilities(() -> null);
    }

    protected final void gatherCapabilities(@Nullable ICapabilityProvider parent) {
        this.gatherCapabilities(() -> parent);
    }

    protected final void gatherCapabilities(@Nullable Supplier<ICapabilityProvider> parent) {
        if (this.isLazy && !this.initialized) {
            this.lazyParentSupplier = parent == null ? () -> null : parent;
        } else {
            this.doGatherCapabilities(parent == null ? null : (ICapabilityProvider) parent.get());
        }
    }

    private void doGatherCapabilities(@Nullable ICapabilityProvider parent) {
        this.capabilities = ForgeEventFactory.gatherCapabilities(this.baseClass, this.getProvider(), parent);
        this.initialized = true;
    }

    @NotNull
    B getProvider() {
        return (B) this;
    }

    @Nullable
    protected final CapabilityDispatcher getCapabilities() {
        if (this.isLazy && !this.initialized) {
            this.doGatherCapabilities(this.lazyParentSupplier == null ? null : (ICapabilityProvider) this.lazyParentSupplier.get());
            if (this.lazyData != null) {
                this.deserializeCaps(this.lazyData);
            }
        }
        return this.capabilities;
    }

    @Override
    public final boolean areCapsCompatible(CapabilityProvider<B> other) {
        return this.areCapsCompatible(other.getCapabilities());
    }

    @Override
    public final boolean areCapsCompatible(@Nullable CapabilityDispatcher other) {
        CapabilityDispatcher disp = this.getCapabilities();
        if (disp == null) {
            return other == null ? true : other.areCompatible(null);
        } else {
            return disp.areCompatible(other);
        }
    }

    @Nullable
    protected final CompoundTag serializeCaps() {
        if (this.isLazy && !this.initialized) {
            return this.lazyData;
        } else {
            CapabilityDispatcher disp = this.getCapabilities();
            return disp != null ? disp.serializeNBT() : null;
        }
    }

    protected final void deserializeCaps(CompoundTag tag) {
        if (this.isLazy && !this.initialized) {
            this.lazyData = tag;
        } else {
            CapabilityDispatcher disp = this.getCapabilities();
            if (disp != null) {
                disp.deserializeNBT(tag);
            }
        }
    }

    @Override
    public void invalidateCaps() {
        this.valid = false;
        CapabilityDispatcher disp = this.getCapabilities();
        if (disp != null) {
            disp.invalidate();
        }
    }

    @Override
    public void reviveCaps() {
        this.valid = true;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        CapabilityDispatcher disp = this.getCapabilities();
        return this.valid && disp != null ? disp.getCapability(cap, side) : LazyOptional.empty();
    }

    public static class AsField<B extends ICapabilityProviderImpl<B>> extends CapabilityProvider<B> {

        private final B owner;

        public AsField(Class<B> baseClass, B owner) {
            super(baseClass);
            this.owner = owner;
        }

        public AsField(Class<B> baseClass, B owner, boolean isLazy) {
            super(baseClass, isLazy);
            this.owner = owner;
        }

        public void initInternal() {
            this.gatherCapabilities();
        }

        @Nullable
        public CompoundTag serializeInternal() {
            return this.serializeCaps();
        }

        public void deserializeInternal(CompoundTag tag) {
            this.deserializeCaps(tag);
        }

        @NotNull
        @Override
        B getProvider() {
            return this.owner;
        }
    }
}