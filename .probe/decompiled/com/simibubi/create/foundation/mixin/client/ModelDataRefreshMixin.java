package com.simibubi.create.foundation.mixin.client;

import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin({ ModelDataManager.class })
public class ModelDataRefreshMixin {

    @Inject(method = { "requestModelDataRefresh" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private static void create$requestModelDataRefresh(BlockEntity be, CallbackInfo ci) {
        if (be != null) {
            Level world = be.getLevel();
            if (world != Minecraft.getInstance().level && world instanceof SchematicWorld) {
                ci.cancel();
            }
        }
    }
}