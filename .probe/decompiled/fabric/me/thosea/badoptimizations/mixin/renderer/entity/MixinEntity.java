package fabric.me.thosea.badoptimizations.mixin.renderer.entity;

import fabric.me.thosea.badoptimizations.interfaces.EntityMethods;
import fabric.me.thosea.badoptimizations.interfaces.EntityTypeMethods;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1937;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_1297.class })
public abstract class MixinEntity implements EntityMethods {

    private EntityTypeMethods bo$typeMethods;

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void afterInit(class_1299<?> type, class_1937 world, CallbackInfo ci) {
        this.bo$typeMethods = (EntityTypeMethods) type;
    }

    @Override
    public class_897<?> bo$getRenderer() {
        return this.bo$typeMethods.bo$getRenderer();
    }
}