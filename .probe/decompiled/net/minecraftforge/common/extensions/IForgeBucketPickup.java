package net.minecraftforge.common.extensions;

import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;

public interface IForgeBucketPickup {

    private BucketPickup self() {
        return (BucketPickup) this;
    }

    default Optional<SoundEvent> getPickupSound(BlockState state) {
        return this.self().getPickupSound();
    }
}