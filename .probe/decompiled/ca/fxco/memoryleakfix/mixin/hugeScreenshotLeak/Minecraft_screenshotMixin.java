package ca.fxco.memoryleakfix.mixin.hugeScreenshotLeak;

import ca.fxco.memoryleakfix.config.MinecraftRequirement;
import ca.fxco.memoryleakfix.config.VersionRange;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyExpressionValue;
import ca.fxco.memoryleakfix.mixinextras.sugar.Share;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.platform.GlUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@MinecraftRequirement({ @VersionRange(minVersion = "1.17.0") })
@Mixin({ Minecraft.class })
public abstract class Minecraft_screenshotMixin {

    @ModifyExpressionValue(method = { "grabHugeScreenshot" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlUtil;allocateMemory(I)Ljava/nio/ByteBuffer;") })
    private ByteBuffer memoryLeakFix$captureByteBuffer(ByteBuffer byteBuf, @Share("memoryLeakFix$byteBuf") LocalRef<ByteBuffer> bufRef) {
        bufRef.set(byteBuf);
        return byteBuf;
    }

    @Inject(method = { "grabHugeScreenshot" }, at = { @At(value = "CONSTANT", args = { "stringValue=screenshot.failure" }) })
    private void memoryLeakFix$freeByteBuffer(CallbackInfoReturnable<Component> cir, @Share("memoryLeakFix$byteBuf") LocalRef<ByteBuffer> bufRef) {
        GlUtil.freeMemory((Buffer) bufRef.get());
    }
}