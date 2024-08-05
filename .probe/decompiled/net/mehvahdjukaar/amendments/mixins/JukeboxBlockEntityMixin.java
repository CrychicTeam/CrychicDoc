package net.mehvahdjukaar.amendments.mixins;

import net.mehvahdjukaar.amendments.common.IBetterJukebox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ JukeboxBlockEntity.class })
public abstract class JukeboxBlockEntityMixin extends BlockEntity implements IBetterJukebox {

    @Shadow
    private boolean isPlaying;

    @Shadow
    @Final
    private NonNullList<ItemStack> items;

    @Unique
    private float amendments$rot = 0.0F;

    @Unique
    private float amendments$prevRot = 0.0F;

    protected JukeboxBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = { "load" }, at = { @At("HEAD") })
    public void amendments$fixItemSync(CompoundTag tag, CallbackInfo ci) {
        this.items.set(0, ItemStack.EMPTY);
    }

    @Inject(method = { "removeItem" }, at = { @At("TAIL") })
    public void notifyRemovedItem(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Inject(method = { "setItem" }, at = { @At("TAIL") })
    public void notifyAddedItem(int slot, ItemStack stack, CallbackInfo ci) {
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    public void amendments$tickAnimation(Level level, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (level.isClientSide) {
            this.amendments$prevRot = this.amendments$rot;
            if (this.isPlaying) {
                this.amendments$rot++;
                this.amendments$rot %= 360.0F;
            } else if ((double) this.amendments$rot > 0.0) {
                this.amendments$rot -= 5.0F;
                if (this.amendments$rot < 0.0F) {
                    this.amendments$rot = 0.0F;
                }
                this.amendments$rot %= 360.0F;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public float amendments$getRotation(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.amendments$prevRot, this.amendments$rot);
    }
}