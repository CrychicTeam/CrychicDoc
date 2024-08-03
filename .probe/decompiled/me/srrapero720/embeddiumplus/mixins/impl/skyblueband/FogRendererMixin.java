package me.srrapero720.embeddiumplus.mixins.impl.skyblueband;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ FogRenderer.class })
public class FogRendererMixin {

    @WrapOperation(method = { "setupColor" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;") })
    private static Vec3 redirect$gausanSampleColor(Vec3 vec, CubicSampler.Vec3Fetcher fetcher, Operation<Vec3> original) {
        if (!EmbyConfig.blueBandCache) {
            Minecraft mc = Minecraft.getInstance();
            return mc.level.m_6042_().hasSkyLight() ? mc.level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), mc.getFrameTime()) : vec;
        } else {
            return (Vec3) original.call(new Object[] { vec, fetcher });
        }
    }

    @WrapOperation(method = { "setupColor" }, at = { @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F", remap = false) })
    private static float redirect$dot(Vector3f instance, Vector3fc v, Operation<Float> original) {
        return !EmbyConfig.blueBandCache ? 0.0F : (Float) original.call(new Object[] { instance, v });
    }

    @WrapOperation(method = { "setupColor" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F") })
    private static float redirect$getRainLevel(ClientLevel instance, float v, Operation<Float> original) {
        return !EmbyConfig.blueBandCache ? 0.0F : (Float) original.call(new Object[] { instance, v });
    }

    @WrapOperation(method = { "setupColor" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F") })
    private static float redirect$getThunderLevel(ClientLevel instance, float v, Operation<Float> original) {
        return !EmbyConfig.blueBandCache ? 0.0F : (Float) original.call(new Object[] { instance, v });
    }
}