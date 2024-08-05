package me.jellysquid.mods.lithium.mixin.util.block_tracking;

import me.jellysquid.mods.lithium.common.block.BlockCountingSection;
import me.jellysquid.mods.lithium.common.block.BlockListeningSection;
import me.jellysquid.mods.lithium.common.block.BlockStateFlagHolder;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.block.ListeningBlockStatePredicate;
import me.jellysquid.mods.lithium.common.block.TrackedBlockStatePredicate;
import me.jellysquid.mods.lithium.common.entity.block_tracking.ChunkSectionChangeCallback;
import me.jellysquid.mods.lithium.common.entity.block_tracking.SectionedBlockChangeTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ LevelChunkSection.class })
public abstract class ChunkSectionMixin implements BlockCountingSection, BlockListeningSection {

    @Shadow
    @Final
    private PalettedContainer<BlockState> states;

    @Unique
    private short[] countsByFlag = null;

    private ChunkSectionChangeCallback changeListener;

    private short listeningMask;

    @Override
    public boolean mayContainAny(TrackedBlockStatePredicate trackedBlockStatePredicate) {
        if (this.countsByFlag == null) {
            this.fastInitClientCounts();
        }
        return this.countsByFlag[trackedBlockStatePredicate.getIndex()] != 0;
    }

    private void fastInitClientCounts() {
        this.countsByFlag = new short[BlockStateFlags.NUM_TRACKED_FLAGS];
        for (TrackedBlockStatePredicate trackedBlockStatePredicate : BlockStateFlags.TRACKED_FLAGS) {
            if (this.states.maybeHas(trackedBlockStatePredicate)) {
                this.countsByFlag[trackedBlockStatePredicate.getIndex()] = 4096;
            }
        }
    }

    @Redirect(method = { "calculateCounts()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/PalettedContainer;count(Lnet/minecraft/world/chunk/PalettedContainer$Counter;)V"))
    private void initFlagCounters(PalettedContainer<BlockState> palettedContainer, PalettedContainer.CountConsumer<BlockState> consumer) {
        palettedContainer.count((state, count) -> {
            consumer.accept(state, count);
            addToFlagCount(this.countsByFlag, state, count);
        });
    }

    private static void addToFlagCount(short[] countsByFlag, BlockState state, int change) {
        int flags = ((BlockStateFlagHolder) state).getAllFlags();
        int i;
        while ((i = Integer.numberOfTrailingZeros(flags)) < 32 && i < countsByFlag.length) {
            countsByFlag[i] = (short) (countsByFlag[i] + change);
            flags &= ~(1 << i);
        }
    }

    @Inject(method = { "calculateCounts()V" }, at = { @At("HEAD") })
    private void createFlagCounters(CallbackInfo ci) {
        this.countsByFlag = new short[BlockStateFlags.NUM_TRACKED_FLAGS];
    }

    @Inject(method = { "readDataPacket" }, at = { @At("HEAD") })
    private void resetData(FriendlyByteBuf buf, CallbackInfo ci) {
        this.countsByFlag = null;
    }

    @Inject(method = { "setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;", ordinal = 0, shift = Shift.BEFORE) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void updateFlagCounters(int x, int y, int z, BlockState newState, boolean lock, CallbackInfoReturnable<BlockState> cir, BlockState oldState) {
        short[] countsByFlag = this.countsByFlag;
        if (countsByFlag != null) {
            int prevFlags = ((BlockStateFlagHolder) oldState).getAllFlags();
            int flags = ((BlockStateFlagHolder) newState).getAllFlags();
            int flagsXOR = prevFlags ^ flags;
            int iterateFlags = ~BlockStateFlags.LISTENING_MASK_OR & flagsXOR | BlockStateFlags.LISTENING_MASK_OR & this.listeningMask & (prevFlags | flags);
            int flagIndex;
            while ((flagIndex = Integer.numberOfTrailingZeros(iterateFlags)) < 32 && flagIndex < countsByFlag.length) {
                int flagBit = 1 << flagIndex;
                if ((flagsXOR & flagBit) != 0) {
                    countsByFlag[flagIndex] = (short) (countsByFlag[flagIndex] + (1 - ((prevFlags >>> flagIndex & 1) << 1)));
                }
                if ((this.listeningMask & flagBit) != 0) {
                    this.listeningMask = this.changeListener.onBlockChange(flagIndex, this);
                }
                iterateFlags &= ~flagBit;
            }
        }
    }

    @Override
    public void addToCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker) {
        if (this.changeListener == null) {
            this.changeListener = new ChunkSectionChangeCallback();
        }
        this.listeningMask = this.changeListener.addTracker(tracker, blockGroup);
    }

    @Override
    public void removeFromCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker) {
        if (this.changeListener != null) {
            this.listeningMask = this.changeListener.removeTracker(tracker, blockGroup);
        }
    }

    private boolean isListening(ListeningBlockStatePredicate blockGroup) {
        return (this.listeningMask & 1 << blockGroup.getIndex()) != 0;
    }

    public void invalidateSection() {
    }
}