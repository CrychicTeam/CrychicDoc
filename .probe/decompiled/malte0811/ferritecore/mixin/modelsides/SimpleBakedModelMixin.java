package malte0811.ferritecore.mixin.modelsides;

import java.util.List;
import java.util.Map;
import malte0811.ferritecore.impl.ModelSidesImpl;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SimpleBakedModel.class })
public class SimpleBakedModelMixin {

    @Shadow
    @Final
    @Mutable
    protected Map<Direction, List<BakedQuad>> culledFaces;

    @Shadow
    @Final
    @Mutable
    protected List<BakedQuad> unculledFaces;

    @Inject(method = { "/<init>/" }, at = { @At("TAIL") })
    private void minimizeFaceLists(CallbackInfo ci) {
        this.unculledFaces = ModelSidesImpl.minimizeUnculled(this.unculledFaces);
        this.culledFaces = ModelSidesImpl.minimizeCulled(this.culledFaces);
    }
}