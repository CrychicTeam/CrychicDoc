package ca.fxco.memoryleakfix.mixin.readResourcesLeak;

import ca.fxco.memoryleakfix.config.MinecraftRequirement;
import ca.fxco.memoryleakfix.config.Remap;
import ca.fxco.memoryleakfix.config.Remaps;
import ca.fxco.memoryleakfix.config.SilentClassNotFound;
import ca.fxco.memoryleakfix.config.VersionRange;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.Operation;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperation;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@OnlyIn(Dist.CLIENT)
@Pseudo
@SilentClassNotFound
@Mixin(targets = { "com/mojang/blaze3d/platform/TextureUtil", "net/minecraft/class_4536", "net/minecraft/client/texture/TextureUtil", "net/minecraft/client/renderer/texture/TextureUtil" }, remap = false)
public abstract class TextureUtil_freeBufferMixin {

    @WrapOperation(method = { "readResource(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;" }, at = { @At(value = "INVOKE", target = "Ljava/nio/channels/FileChannel;read(Ljava/nio/ByteBuffer;)I"), @At(value = "INVOKE", target = "Ljava/nio/channels/ReadableByteChannel;read(Ljava/nio/ByteBuffer;)I") })
    @Remaps({ @Remap(fabric = { "method_24962" }, excludeDev = true, mcVersions = @MinecraftRequirement({ @VersionRange(minVersion = "1.16.0", maxVersion = "1.16.5") })), @Remap(mcp = { "func_225684_a_" }, excludeDev = true) })
    private static int memoryLeakFix$readResourceWithoutLeak(@Coerce Channel channel, ByteBuffer byteBuf, Operation<Integer> original) {
        try {
            return original.call(channel, byteBuf);
        } catch (Exception var4) {
            MemoryUtil.memFree(byteBuf);
            throw var4;
        }
    }
}