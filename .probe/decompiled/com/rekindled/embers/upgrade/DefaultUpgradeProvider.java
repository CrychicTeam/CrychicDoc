package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.upgrades.IUpgradeProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultUpgradeProvider implements IUpgradeProvider {

    protected final ResourceLocation id;

    protected final BlockEntity tile;

    private final LazyOptional<IUpgradeProvider> holder;

    public DefaultUpgradeProvider(ResourceLocation id, BlockEntity tile) {
        this.id = id;
        this.tile = tile;
        this.holder = LazyOptional.of(() -> this);
    }

    @Override
    public ResourceLocation getUpgradeId() {
        return this.id;
    }

    public void invalidate() {
        this.holder.invalidate();
    }

    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY != null && cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY ? EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY.orEmpty(cap, this.holder) : LazyOptional.empty();
    }
}