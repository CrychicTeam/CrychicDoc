package me.jellysquid.mods.lithium.mixin.collections.mob_spawning;

import me.jellysquid.mods.lithium.common.world.PotentialSpawnsExtended;
import net.minecraftforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LevelEvent.PotentialSpawns.class })
public class PotentialSpawnsEventMixin implements PotentialSpawnsExtended {

    private boolean radium$listModified;

    @Inject(method = { "addSpawnerData" }, at = { @At("RETURN") })
    private void onAdd(CallbackInfo ci) {
        this.radium$listModified = true;
    }

    @Inject(method = { "removeSpawnerData" }, at = { @At("RETURN") })
    private void onRemove(CallbackInfoReturnable<Boolean> ci) {
        this.radium$listModified = true;
    }

    @Override
    public boolean radium$wasListModified() {
        return this.radium$listModified;
    }
}