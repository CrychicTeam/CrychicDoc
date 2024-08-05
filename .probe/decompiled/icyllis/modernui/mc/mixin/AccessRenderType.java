package icyllis.modernui.mc.mixin;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Deprecated
@Mixin({ RenderType.class })
public interface AccessRenderType {

    @Accessor("sortOnUpload")
    boolean isSortOnUpload();
}