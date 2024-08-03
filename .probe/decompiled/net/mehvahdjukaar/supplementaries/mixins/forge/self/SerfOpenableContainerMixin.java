package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.supplementaries.common.block.tiles.OpeneableContainerBlockEntity;
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

@Mixin({ OpeneableContainerBlockEntity.class })
public abstract class SerfOpenableContainerMixin extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    protected SerfOpenableContainerMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
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