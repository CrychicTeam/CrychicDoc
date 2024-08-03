package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.shaders.Uniform;
import java.util.List;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Deprecated
@Mixin({ ShaderInstance.class })
public class MixinShaderInstance {

    @Shadow
    private boolean dirty;

    @Shadow
    @Final
    private int programId;

    @Shadow
    @Final
    private List<Integer> samplerLocations;

    @Shadow
    @Final
    private List<String> samplerNames;

    @Shadow
    @Final
    private List<Uniform> uniforms;
}