package net.minecraft.client.model.geom.builders;

import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

public final class CubeDefinition {

    @Nullable
    private final String comment;

    private final Vector3f origin;

    private final Vector3f dimensions;

    private final CubeDeformation grow;

    private final boolean mirror;

    private final UVPair texCoord;

    private final UVPair texScale;

    private final Set<Direction> visibleFaces;

    protected CubeDefinition(@Nullable String string0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, CubeDeformation cubeDeformation9, boolean boolean10, float float11, float float12, Set<Direction> setDirection13) {
        this.comment = string0;
        this.texCoord = new UVPair(float1, float2);
        this.origin = new Vector3f(float3, float4, float5);
        this.dimensions = new Vector3f(float6, float7, float8);
        this.grow = cubeDeformation9;
        this.mirror = boolean10;
        this.texScale = new UVPair(float11, float12);
        this.visibleFaces = setDirection13;
    }

    public ModelPart.Cube bake(int int0, int int1) {
        return new ModelPart.Cube((int) this.texCoord.u(), (int) this.texCoord.v(), this.origin.x(), this.origin.y(), this.origin.z(), this.dimensions.x(), this.dimensions.y(), this.dimensions.z(), this.grow.growX, this.grow.growY, this.grow.growZ, this.mirror, (float) int0 * this.texScale.u(), (float) int1 * this.texScale.v(), this.visibleFaces);
    }
}