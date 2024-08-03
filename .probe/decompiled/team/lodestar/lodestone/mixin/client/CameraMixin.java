package team.lodestar.lodestone.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;

@Mixin({ Camera.class })
public class CameraMixin {

    @Inject(method = { "setup" }, at = { @At("RETURN") })
    private void lodestone$Screenshake(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue() > 0.0) {
            ScreenshakeHandler.cameraTick((Camera) this, LodestoneLib.RANDOM);
        }
    }
}