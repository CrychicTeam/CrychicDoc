package net.minecraft.client.tutorial;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public interface TutorialStepInstance {

    default void clear() {
    }

    default void tick() {
    }

    default void onInput(Input input0) {
    }

    default void onMouse(double double0, double double1) {
    }

    default void onLookAt(ClientLevel clientLevel0, HitResult hitResult1) {
    }

    default void onDestroyBlock(ClientLevel clientLevel0, BlockPos blockPos1, BlockState blockState2, float float3) {
    }

    default void onOpenInventory() {
    }

    default void onGetItem(ItemStack itemStack0) {
    }
}