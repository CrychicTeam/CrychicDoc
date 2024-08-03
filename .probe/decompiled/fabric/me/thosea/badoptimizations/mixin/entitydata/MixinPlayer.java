package fabric.me.thosea.badoptimizations.mixin.entitydata;

import net.minecraft.class_1306;
import net.minecraft.class_1657;
import net.minecraft.class_1664;
import net.minecraft.class_2487;
import net.minecraft.class_2940;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { class_1657.class }, priority = 700)
public abstract class MixinPlayer extends MixinEntity {

    @Shadow
    @Final
    private static class_2940<Float> field_7491;

    @Shadow
    @Final
    protected static class_2940<class_2487> field_7496;

    @Shadow
    @Final
    protected static class_2940<class_2487> field_7506;

    @Shadow
    @Final
    protected static class_2940<Byte> field_7518;

    @Shadow
    @Final
    private static class_2940<Integer> field_7511;

    @Shadow
    @Final
    protected static class_2940<Byte> field_7488;

    private boolean bo$isCapeEnabled = false;

    private boolean bo$isJacketEnabled = false;

    private boolean bo$isLeftSleeveEnabled = false;

    private boolean bo$isRightSleeveEnabled = false;

    private boolean bo$isLeftPantsLegEnabled = false;

    private boolean bo$isRightPantsLegEnabled = false;

    private boolean bo$isHatEnabled = false;

    private class_1306 bo$mainArm = class_1306.field_6183;

    private float bo$absorptionAmount = 0.0F;

    private int bo$score = 0;

    private class_2487 bo$shoulderEntityLeft = new class_2487();

    private class_2487 bo$shoulderEntityRight = new class_2487();

    @Overwrite
    public boolean method_7348(class_1664 modelPart) {
        return switch(modelPart) {
            case field_7559 ->
                this.bo$isCapeEnabled;
            case field_7564 ->
                this.bo$isJacketEnabled;
            case field_7568 ->
                this.bo$isLeftSleeveEnabled;
            case field_7570 ->
                this.bo$isRightSleeveEnabled;
            case field_7566 ->
                this.bo$isLeftPantsLegEnabled;
            case field_7565 ->
                this.bo$isRightPantsLegEnabled;
            case field_7563 ->
                this.bo$isHatEnabled;
            default ->
                throw new IncompatibleClassChangeError();
        };
    }

    @Overwrite
    public float method_6067() {
        return this.bo$absorptionAmount;
    }

    @Overwrite
    public int method_7272() {
        return this.bo$score;
    }

    @Overwrite
    public class_1306 method_6068() {
        return this.bo$mainArm;
    }

    @Overwrite
    public class_2487 method_7356() {
        return this.bo$shoulderEntityLeft;
    }

    @Overwrite
    public class_2487 method_7308() {
        return this.bo$shoulderEntityRight;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        super.bo$refreshEntityData(data);
        if (data == field_7491.method_12713()) {
            this.bo$absorptionAmount = (Float) this.field_6011.method_12789(field_7491);
        } else if (data == field_7511.method_12713()) {
            this.bo$score = (Integer) this.field_6011.method_12789(field_7511);
        } else if (data == field_7518.method_12713()) {
            byte parts = (Byte) this.field_6011.method_12789(field_7518);
            this.bo$isCapeEnabled = (parts & 1) == 1;
            this.bo$isJacketEnabled = (parts & 2) == 2;
            this.bo$isLeftSleeveEnabled = (parts & 4) == 4;
            this.bo$isRightSleeveEnabled = (parts & 8) == 8;
            this.bo$isLeftPantsLegEnabled = (parts & 16) == 16;
            this.bo$isRightPantsLegEnabled = (parts & 32) == 32;
            this.bo$isHatEnabled = (parts & 64) == 64;
        } else if (data == field_7488.method_12713()) {
            this.bo$mainArm = this.field_6011.method_12789(field_7488) == 0 ? class_1306.field_6182 : class_1306.field_6183;
        } else if (data == field_7496.method_12713()) {
            this.bo$shoulderEntityLeft = (class_2487) this.field_6011.method_12789(field_7496);
        } else if (data == field_7506.method_12713()) {
            this.bo$shoulderEntityRight = (class_2487) this.field_6011.method_12789(field_7506);
        }
    }
}