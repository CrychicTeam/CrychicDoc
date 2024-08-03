package fabric.me.thosea.badoptimizations.mixin.renderer.entity;

import fabric.me.thosea.badoptimizations.interfaces.EntityTypeMethods;
import net.minecraft.class_1299;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1299.class })
public class MixinEntityType implements EntityTypeMethods {

    private class_897<?> bo$renderer;

    @Override
    public class_897<?> bo$getRenderer() {
        return this.bo$renderer;
    }

    @Override
    public void bo$setRenderer(class_897<?> renderer) {
        this.bo$renderer = renderer;
    }
}