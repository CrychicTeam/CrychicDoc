package lio.playeranimatorapi.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ Camera.class })
public interface CameraAccessor {

    @Invoker
    void callSetPosition(double var1, double var3, double var5);

    @Accessor
    float getEyeHeightOld();

    @Accessor
    float getEyeHeight();
}