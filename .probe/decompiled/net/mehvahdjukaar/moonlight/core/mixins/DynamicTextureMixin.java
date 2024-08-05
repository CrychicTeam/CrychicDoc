package net.mehvahdjukaar.moonlight.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import java.nio.file.Path;
import net.mehvahdjukaar.moonlight.core.ClientConfigs;
import net.mehvahdjukaar.moonlight.core.MoonlightClient;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.Dumpable;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ DynamicTexture.class })
public abstract class DynamicTextureMixin extends AbstractTexture implements Dumpable {

    @Shadow
    private NativeImage pixels;

    @WrapOperation(method = { "upload" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;upload(IIIZ)V") })
    public void forceMipMap(NativeImage instance, int a, int b, int c, boolean autoClose, Operation<Void> op) {
        if (MoonlightClient.isMapMipMap()) {
            this.f_117952_ = true;
            instance.upload(a, b, c, 0, 0, instance.getWidth(), instance.getHeight(), false, true, true, autoClose);
            if (!autoClose) {
                GL30.glGenerateMipmap(3553);
            }
        } else {
            op.call(new Object[] { instance, a, b, c, autoClose });
        }
    }

    @WrapOperation(method = { "<init>(IIZ)V" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;prepareImage(III)V") })
    public void forceMipMap(int a, int b, int c, Operation<Void> op) {
        if (MoonlightClient.isMapMipMap()) {
            this.f_117952_ = true;
            TextureUtil.prepareImage(a, (Integer) ClientConfigs.MAPS_MIPMAP.get(), b, c);
        } else {
            op.call(new Object[] { a, b, c });
        }
    }

    @Overwrite
    @Override
    public void dumpContents(ResourceLocation pResourceLocation, Path pPath) {
        if (this.pixels != null) {
            String s = pResourceLocation.toDebugFileName();
            TextureUtil.writeAsPNG(pPath, s, this.m_117963_(), this.f_117952_ ? (Integer) ClientConfigs.MAPS_MIPMAP.get() : 0, this.pixels.getWidth(), this.pixels.getHeight());
        }
    }
}