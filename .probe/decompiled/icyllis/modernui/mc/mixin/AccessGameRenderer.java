package icyllis.modernui.mc.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ GameRenderer.class })
public interface AccessGameRenderer {

    @Invoker
    void callLoadEffect(ResourceLocation var1);
}