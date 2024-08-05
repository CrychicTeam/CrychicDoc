package fabric.me.thosea.badoptimizations.mixin.entitydata;

import java.util.Optional;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_2940;
import net.minecraft.class_2945;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { class_1309.class }, priority = 700)
public abstract class MixinLivingEntity extends MixinEntity {

    @Shadow
    @Final
    protected static class_2940<Byte> field_6257;

    @Shadow
    @Final
    private static class_2940<Float> field_6247;

    @Shadow
    @Final
    private static class_2940<Integer> field_6240;

    @Shadow
    @Final
    private static class_2940<Boolean> field_6214;

    @Shadow
    @Final
    private static class_2940<Integer> field_6219;

    @Shadow
    @Final
    private static class_2940<Integer> field_20348;

    @Shadow
    @Final
    private static class_2940<Optional<class_2338>> field_18073;

    private boolean bo$isUsingItem = false;

    private boolean bo$potionSwirlsAmbient = false;

    private boolean bo$isUsingRiptide = false;

    private class_1268 bo$activeHand = class_1268.field_5808;

    private float bo$health = 1.0F;

    private int bo$potionSwirlsColor = 0;

    private int bo$stuckArrowCount = 0;

    private int bo$stingerCount = 0;

    private Optional<class_2338> bo$sleepingPosition = Optional.empty();

    @Redirect(method = { "tickStatusEffects" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;", ordinal = 0))
    private Object onGetPotionSwirlsColor(class_2945 instance, class_2940<Integer> data) {
        return this.bo$potionSwirlsColor;
    }

    @Redirect(method = { "tickStatusEffects" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;", ordinal = 1))
    private Object onGetPotionSwirlsAmbient(class_2945 instance, class_2940<Boolean> data) {
        return this.bo$potionSwirlsAmbient;
    }

    @Overwrite
    public boolean method_6115() {
        return this.bo$isUsingItem;
    }

    @Overwrite
    public class_1268 method_6058() {
        return this.bo$activeHand;
    }

    @Overwrite
    public boolean method_6123() {
        return this.bo$isUsingRiptide;
    }

    @Overwrite
    public float method_6032() {
        return this.bo$health;
    }

    @Overwrite
    public final int method_6022() {
        return this.bo$stuckArrowCount;
    }

    @Overwrite
    public final int method_21753() {
        return this.bo$stingerCount;
    }

    @Overwrite
    public Optional<class_2338> method_18398() {
        return this.bo$sleepingPosition;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        super.bo$refreshEntityData(data);
        if (data == field_6257.method_12713()) {
            this.bo$isUsingItem = ((Byte) this.field_6011.method_12789(field_6257) & 1) > 0;
            this.bo$activeHand = (this.field_6011.method_12789(field_6257) & 2) > 0 ? class_1268.field_5810 : class_1268.field_5808;
            this.bo$isUsingRiptide = ((Byte) this.field_6011.method_12789(field_6257) & 4) != 0;
        } else if (data == field_6247.method_12713()) {
            this.bo$health = (Float) this.field_6011.method_12789(field_6247);
        } else if (data == field_6240.method_12713()) {
            this.bo$potionSwirlsColor = (Integer) this.field_6011.method_12789(field_6240);
        } else if (data == field_6214.method_12713()) {
            this.bo$potionSwirlsAmbient = (Boolean) this.field_6011.method_12789(field_6214);
        } else if (data == field_6219.method_12713()) {
            this.bo$stuckArrowCount = (Integer) this.field_6011.method_12789(field_6219);
        } else if (data == field_20348.method_12713()) {
            this.bo$stingerCount = (Integer) this.field_6011.method_12789(field_20348);
        } else if (data == field_18073.method_12713()) {
            this.bo$sleepingPosition = (Optional<class_2338>) this.field_6011.method_12789(field_18073);
        }
    }
}