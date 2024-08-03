package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.core.shader.WorldProgram;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;

public class ContraptionProgram extends WorldProgram {

    protected final int uLightBoxSize = this.getUniformLocation("uLightBoxSize");

    protected final int uLightBoxMin = this.getUniformLocation("uLightBoxMin");

    protected final int uModel = this.getUniformLocation("uModel");

    protected int uLightVolume;

    public ContraptionProgram(ResourceLocation name, int handle) {
        super(name, handle);
    }

    protected void registerSamplers() {
        super.registerSamplers();
        this.uLightVolume = this.setSamplerBinding("uLightVolume", 4);
    }

    public void bind(Matrix4f model, AABB lightVolume) {
        double sizeX = lightVolume.maxX - lightVolume.minX;
        double sizeY = lightVolume.maxY - lightVolume.minY;
        double sizeZ = lightVolume.maxZ - lightVolume.minZ;
        GL20.glUniform3f(this.uLightBoxSize, (float) sizeX, (float) sizeY, (float) sizeZ);
        GL20.glUniform3f(this.uLightBoxMin, (float) lightVolume.minX, (float) lightVolume.minY, (float) lightVolume.minZ);
        uploadMatrixUniform(this.uModel, model);
    }
}