package icyllis.modernui.mc.mixin;

import net.minecraft.client.gui.Font;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Deprecated
@Mixin({ Font.class })
public interface AccessFontRenderer {

    @Accessor("SHADOW_OFFSET")
    static Vector3f shadowLifting() {
        throw new IllegalStateException();
    }
}