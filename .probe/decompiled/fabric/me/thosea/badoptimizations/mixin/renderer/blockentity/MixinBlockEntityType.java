package fabric.me.thosea.badoptimizations.mixin.renderer.blockentity;

import fabric.me.thosea.badoptimizations.interfaces.BlockEntityTypeMethods;
import net.minecraft.class_2591;
import net.minecraft.class_827;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2591.class })
public class MixinBlockEntityType implements BlockEntityTypeMethods {

    private class_827<?> bo$renderer;

    @Override
    public class_827<?> bo$getRenderer() {
        return this.bo$renderer;
    }

    @Override
    public void bo$setRenderer(class_827<?> renderer) {
        this.bo$renderer = renderer;
    }
}