package org.violetmoon.zetaimplforge.capability;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.capability.ZetaCapability;
import org.violetmoon.zeta.capability.ZetaCapabilityManager;

public class ForgeCapabilityManager implements ZetaCapabilityManager {

    protected Map<ZetaCapability<?>, Capability<?>> toForge = new IdentityHashMap();

    protected <T> Capability<T> forgify(ZetaCapability<T> zcap) {
        return (Capability<T>) this.toForge.get(zcap);
    }

    public ForgeCapabilityManager register(ZetaCapability<?> cap, Object backing) {
        if (backing instanceof Capability<?> forgecap) {
            this.toForge.put(cap, forgecap);
            return this;
        } else {
            throw new IllegalArgumentException("Can only register Capability<?> objects");
        }
    }

    @Override
    public <T> boolean hasCapability(ZetaCapability<T> cap, ItemStack stack) {
        return stack.getCapability(this.forgify(cap)).isPresent();
    }

    @Override
    public <T> T getCapability(ZetaCapability<T> cap, ItemStack stack) {
        return (T) stack.getCapability(this.forgify(cap)).orElse(null);
    }

    @Override
    public <T> boolean hasCapability(ZetaCapability<T> cap, BlockEntity be) {
        return be.getCapability(this.forgify(cap)).isPresent();
    }

    @Nullable
    @Override
    public <T> T getCapability(ZetaCapability<T> cap, BlockEntity be) {
        return (T) be.getCapability(this.forgify(cap)).orElse(null);
    }

    @Override
    public <T> boolean hasCapability(ZetaCapability<T> cap, Level level) {
        return level.getCapability(this.forgify(cap)).isPresent();
    }

    @Nullable
    @Override
    public <T> T getCapability(ZetaCapability<T> cap, Level level) {
        return (T) level.getCapability(this.forgify(cap)).orElse(null);
    }

    @Override
    public <T> void attachCapability(Object target, ResourceLocation id, ZetaCapability<T> cap, T impl) {
        ((AttachCapabilitiesEvent) target).addCapability(id, new ForgeCapabilityManager.ImmediateProvider<>(this.forgify(cap), impl));
    }

    protected static record ImmediateProvider<C>(Capability<C> cap, LazyOptional<C> impl) implements ICapabilityProvider {

        ImmediateProvider(Capability<C> cap, C impl) {
            this(cap, LazyOptional.of(() -> impl));
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == this.cap ? this.impl.cast() : LazyOptional.empty();
        }
    }
}