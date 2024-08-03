package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.forge.CCCompatImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ SpeakerBlockTile.class })
public abstract class SelfSpeakerBlockTileMixin extends BlockEntity {

    @Unique
    private LazyOptional<Object> peripheral = null;

    private SelfSpeakerBlockTileMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction direction) {
        if (CompatHandler.COMPUTERCRAFT && CCCompatImpl.isPeripheralCap(cap)) {
            if (this.peripheral == null) {
                this.peripheral = CCCompatImpl.getPeripheralSupplier((SpeakerBlockTile) this);
            }
            return this.peripheral.cast();
        } else {
            return super.getCapability(cap, direction);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.peripheral != null) {
            this.peripheral.invalidate();
        }
    }
}