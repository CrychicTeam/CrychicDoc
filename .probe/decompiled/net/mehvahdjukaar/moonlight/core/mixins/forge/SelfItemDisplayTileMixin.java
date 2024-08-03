package net.mehvahdjukaar.moonlight.core.mixins.forge;

import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ ItemDisplayTile.class })
public abstract class SelfItemDisplayTileMixin extends RandomizableContainerBlockEntity {

    @Unique
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create((WorldlyContainer) this, Direction.values());

    protected SelfItemDisplayTileMixin(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return !this.f_58859_ && facing != null && capability == ForgeCapabilities.ITEM_HANDLER ? this.handlers[facing.ordinal()].cast() : super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.m_7651_();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }
}