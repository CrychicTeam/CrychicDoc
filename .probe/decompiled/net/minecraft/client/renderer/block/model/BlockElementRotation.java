package net.minecraft.client.renderer.block.model;

import net.minecraft.core.Direction;
import org.joml.Vector3f;

public record BlockElementRotation(Vector3f f_111378_, Direction.Axis f_111379_, float f_111380_, boolean f_111381_) {

    private final Vector3f origin;

    private final Direction.Axis axis;

    private final float angle;

    private final boolean rescale;

    public BlockElementRotation(Vector3f f_111378_, Direction.Axis f_111379_, float f_111380_, boolean f_111381_) {
        this.origin = f_111378_;
        this.axis = f_111379_;
        this.angle = f_111380_;
        this.rescale = f_111381_;
    }
}