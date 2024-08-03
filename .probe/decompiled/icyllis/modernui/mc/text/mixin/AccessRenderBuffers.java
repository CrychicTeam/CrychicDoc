package icyllis.modernui.mc.text.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.SortedMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ RenderBuffers.class })
public interface AccessRenderBuffers {

    @Accessor("fixedBuffers")
    SortedMap<RenderType, BufferBuilder> getFixedBuffers();
}