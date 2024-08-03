package fabric.me.thosea.badoptimizations.mixin.entitydata;

import fabric.me.thosea.badoptimizations.interfaces.EntityMethods;
import java.util.Optional;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_2940;
import net.minecraft.class_2945;
import net.minecraft.class_4050;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { class_1297.class }, priority = 700)
public abstract class MixinEntity implements EntityMethods {

    @Shadow
    @Final
    private static class_2940<Optional<class_2561>> field_6027;

    @Shadow
    @Final
    protected static class_2940<Byte> field_5990;

    @Shadow
    @Final
    private static class_2940<Boolean> field_5975;

    @Shadow
    @Final
    private static class_2940<Boolean> field_5962;

    @Shadow
    @Final
    private static class_2940<Boolean> field_5995;

    @Shadow
    @Final
    protected static class_2940<class_4050> field_18064;

    @Shadow
    @Final
    private static class_2940<Integer> field_27858;

    @Shadow
    @Final
    private static class_2940<Integer> field_6032;

    @Shadow
    private class_1937 field_6002;

    private boolean bo$glowingClient;

    private boolean bo$onFire = false;

    private boolean bo$sneaking = false;

    private boolean bo$sprinting = false;

    private boolean bo$swimming = false;

    private boolean bo$invisible = false;

    private boolean bo$nameVisible = false;

    private boolean bo$silent = false;

    private boolean bo$noGravity = false;

    private int bo$frozenTicks = 0;

    private class_4050 bo$pose = class_4050.field_18076;

    private Optional<class_2561> bo$customName = Optional.empty();

    private int bo$remainingAirTicks = this.method_5748();

    @Shadow
    private boolean field_5958;

    @Shadow
    @Final
    protected class_2945 field_6011;

    @Shadow
    public abstract int method_5748();

    @Redirect(method = { "isOnFire" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFlag(I)Z"))
    private boolean getIsOnFire(class_1297 instance, int index) {
        return this.bo$onFire;
    }

    @Overwrite
    public boolean method_5851() {
        return this.field_6002.field_9236 ? this.bo$glowingClient : this.field_5958;
    }

    @Overwrite
    public boolean method_5715() {
        return this.bo$sneaking;
    }

    @Overwrite
    public boolean method_5624() {
        return this.bo$sprinting;
    }

    @Overwrite
    public boolean method_5681() {
        return this.bo$swimming;
    }

    @Overwrite
    public boolean method_5767() {
        return this.bo$invisible;
    }

    @Overwrite
    public int method_5669() {
        return this.bo$remainingAirTicks;
    }

    @Overwrite
    @Nullable
    public class_2561 method_5797() {
        return (class_2561) this.bo$customName.orElse(null);
    }

    @Overwrite
    public boolean method_16914() {
        return this.bo$customName.isPresent();
    }

    @Overwrite
    public boolean method_5807() {
        return this.bo$nameVisible;
    }

    @Overwrite
    public boolean method_5701() {
        return this.bo$silent;
    }

    @Overwrite
    public boolean method_5740() {
        return this.bo$noGravity;
    }

    @Overwrite
    public class_4050 method_18376() {
        return this.bo$pose;
    }

    @Overwrite
    public int method_32312() {
        return this.bo$frozenTicks;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        if (data == field_5990.method_12713()) {
            byte flags = (Byte) this.field_6011.method_12789(field_5990);
            this.bo$onFire = this.bo$getFlag(flags, 0);
            this.bo$sneaking = this.bo$getFlag(flags, 1);
            this.bo$sprinting = this.bo$getFlag(flags, 3);
            this.bo$swimming = this.bo$getFlag(flags, 4);
            this.bo$invisible = this.bo$getFlag(flags, 5);
            if (this.field_6002.field_9236) {
                this.bo$glowingClient = this.bo$getFlag(flags, 6);
            }
        } else if (data == field_6032.method_12713()) {
            this.bo$remainingAirTicks = (Integer) this.field_6011.method_12789(field_6032);
        } else if (data == field_6027.method_12713()) {
            this.bo$customName = (Optional<class_2561>) this.field_6011.method_12789(field_6027);
        } else if (data == field_5975.method_12713()) {
            this.bo$nameVisible = (Boolean) this.field_6011.method_12789(field_5975);
        } else if (data == field_5962.method_12713()) {
            this.bo$silent = (Boolean) this.field_6011.method_12789(field_5962);
        } else if (data == field_5995.method_12713()) {
            this.bo$noGravity = (Boolean) this.field_6011.method_12789(field_5995);
        } else if (data == field_18064.method_12713()) {
            this.bo$pose = (class_4050) this.field_6011.method_12789(field_18064);
        } else if (data == field_27858.method_12713()) {
            this.bo$frozenTicks = (Integer) this.field_6011.method_12789(field_27858);
        }
    }

    private boolean bo$getFlag(byte flags, int index) {
        return (flags & 1 << index) != 0;
    }
}