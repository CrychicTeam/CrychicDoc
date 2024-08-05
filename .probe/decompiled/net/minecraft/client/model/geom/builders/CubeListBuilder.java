package net.minecraft.client.model.geom.builders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Direction;

public class CubeListBuilder {

    private static final Set<Direction> ALL_VISIBLE = EnumSet.allOf(Direction.class);

    private final List<CubeDefinition> cubes = Lists.newArrayList();

    private int xTexOffs;

    private int yTexOffs;

    private boolean mirror;

    public CubeListBuilder texOffs(int int0, int int1) {
        this.xTexOffs = int0;
        this.yTexOffs = int1;
        return this;
    }

    public CubeListBuilder mirror() {
        return this.mirror(true);
    }

    public CubeListBuilder mirror(boolean boolean0) {
        this.mirror = boolean0;
        return this;
    }

    public CubeListBuilder addBox(String string0, float float1, float float2, float float3, int int4, int int5, int int6, CubeDeformation cubeDeformation7, int int8, int int9) {
        this.texOffs(int8, int9);
        this.cubes.add(new CubeDefinition(string0, (float) this.xTexOffs, (float) this.yTexOffs, float1, float2, float3, (float) int4, (float) int5, (float) int6, cubeDeformation7, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(String string0, float float1, float float2, float float3, int int4, int int5, int int6, int int7, int int8) {
        this.texOffs(int7, int8);
        this.cubes.add(new CubeDefinition(string0, (float) this.xTexOffs, (float) this.yTexOffs, float1, float2, float3, (float) int4, (float) int5, (float) int6, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(float float0, float float1, float float2, float float3, float float4, float float5) {
        this.cubes.add(new CubeDefinition(null, (float) this.xTexOffs, (float) this.yTexOffs, float0, float1, float2, float3, float4, float5, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(float float0, float float1, float float2, float float3, float float4, float float5, Set<Direction> setDirection6) {
        this.cubes.add(new CubeDefinition(null, (float) this.xTexOffs, (float) this.yTexOffs, float0, float1, float2, float3, float4, float5, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, setDirection6));
        return this;
    }

    public CubeListBuilder addBox(String string0, float float1, float float2, float float3, float float4, float float5, float float6) {
        this.cubes.add(new CubeDefinition(string0, (float) this.xTexOffs, (float) this.yTexOffs, float1, float2, float3, float4, float5, float6, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(String string0, float float1, float float2, float float3, float float4, float float5, float float6, CubeDeformation cubeDeformation7) {
        this.cubes.add(new CubeDefinition(string0, (float) this.xTexOffs, (float) this.yTexOffs, float1, float2, float3, float4, float5, float6, cubeDeformation7, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(float float0, float float1, float float2, float float3, float float4, float float5, boolean boolean6) {
        this.cubes.add(new CubeDefinition(null, (float) this.xTexOffs, (float) this.yTexOffs, float0, float1, float2, float3, float4, float5, CubeDeformation.NONE, boolean6, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(float float0, float float1, float float2, float float3, float float4, float float5, CubeDeformation cubeDeformation6, float float7, float float8) {
        this.cubes.add(new CubeDefinition(null, (float) this.xTexOffs, (float) this.yTexOffs, float0, float1, float2, float3, float4, float5, cubeDeformation6, this.mirror, float7, float8, ALL_VISIBLE));
        return this;
    }

    public CubeListBuilder addBox(float float0, float float1, float float2, float float3, float float4, float float5, CubeDeformation cubeDeformation6) {
        this.cubes.add(new CubeDefinition(null, (float) this.xTexOffs, (float) this.yTexOffs, float0, float1, float2, float3, float4, float5, cubeDeformation6, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
        return this;
    }

    public List<CubeDefinition> getCubes() {
        return ImmutableList.copyOf(this.cubes);
    }

    public static CubeListBuilder create() {
        return new CubeListBuilder();
    }
}