package net.minecraft.client.resources.model;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.Transformation;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public enum BlockModelRotation implements ModelState {

    X0_Y0(0, 0),
    X0_Y90(0, 90),
    X0_Y180(0, 180),
    X0_Y270(0, 270),
    X90_Y0(90, 0),
    X90_Y90(90, 90),
    X90_Y180(90, 180),
    X90_Y270(90, 270),
    X180_Y0(180, 0),
    X180_Y90(180, 90),
    X180_Y180(180, 180),
    X180_Y270(180, 270),
    X270_Y0(270, 0),
    X270_Y90(270, 90),
    X270_Y180(270, 180),
    X270_Y270(270, 270);

    private static final int DEGREES = 360;

    private static final Map<Integer, BlockModelRotation> BY_INDEX = (Map<Integer, BlockModelRotation>) Arrays.stream(values()).collect(Collectors.toMap(p_119163_ -> p_119163_.index, p_119157_ -> p_119157_));

    private final Transformation transformation;

    private final OctahedralGroup actualRotation;

    private final int index;

    private static int getIndex(int p_119160_, int p_119161_) {
        return p_119160_ * 360 + p_119161_;
    }

    private BlockModelRotation(int p_119151_, int p_119152_) {
        this.index = getIndex(p_119151_, p_119152_);
        Quaternionf $$2 = new Quaternionf().rotateYXZ((float) (-p_119152_) * (float) (Math.PI / 180.0), (float) (-p_119151_) * (float) (Math.PI / 180.0), 0.0F);
        OctahedralGroup $$3 = OctahedralGroup.IDENTITY;
        for (int $$4 = 0; $$4 < p_119152_; $$4 += 90) {
            $$3 = $$3.compose(OctahedralGroup.ROT_90_Y_NEG);
        }
        for (int $$5 = 0; $$5 < p_119151_; $$5 += 90) {
            $$3 = $$3.compose(OctahedralGroup.ROT_90_X_NEG);
        }
        this.transformation = new Transformation(null, $$2, null, null);
        this.actualRotation = $$3;
    }

    @Override
    public Transformation getRotation() {
        return this.transformation;
    }

    public static BlockModelRotation by(int p_119154_, int p_119155_) {
        return (BlockModelRotation) BY_INDEX.get(getIndex(Mth.positiveModulo(p_119154_, 360), Mth.positiveModulo(p_119155_, 360)));
    }

    public OctahedralGroup actualRotation() {
        return this.actualRotation;
    }
}