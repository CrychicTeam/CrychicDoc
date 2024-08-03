package com.simibubi.create.foundation.outliner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class ChasingAABBOutline extends AABBOutline {

    AABB targetBB;

    AABB prevBB;

    public ChasingAABBOutline(AABB bb) {
        super(bb);
        this.prevBB = bb.inflate(0.0);
        this.targetBB = bb.inflate(0.0);
    }

    public void target(AABB target) {
        this.targetBB = target;
    }

    @Override
    public void tick() {
        this.prevBB = this.bb;
        this.setBounds(interpolateBBs(this.bb, this.targetBB, 0.5F));
    }

    @Override
    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, float pt) {
        this.params.loadColor(this.colorTemp);
        Vector4f color = this.colorTemp;
        int lightmap = this.params.lightmap;
        boolean disableLineNormals = this.params.disableLineNormals;
        this.renderBox(ms, buffer, camera, interpolateBBs(this.prevBB, this.bb, pt), color, lightmap, disableLineNormals);
    }

    private static AABB interpolateBBs(AABB current, AABB target, float pt) {
        return new AABB(Mth.lerp((double) pt, current.minX, target.minX), Mth.lerp((double) pt, current.minY, target.minY), Mth.lerp((double) pt, current.minZ, target.minZ), Mth.lerp((double) pt, current.maxX, target.maxX), Mth.lerp((double) pt, current.maxY, target.maxY), Mth.lerp((double) pt, current.maxZ, target.maxZ));
    }
}