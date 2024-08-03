package com.github.alexthe666.iceandfire.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleHydraBreath extends TextureSheetParticle {

    private static final ResourceLocation HYDRA_POISON = new ResourceLocation("iceandfire:textures/particles/hydra_poison.png");

    float reddustParticleScale;

    public ParticleHydraBreath(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected ParticleHydraBreath(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float scale, float red, float green, float blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        float f = (float) Math.random() * 0.4F + 0.6F;
        this.f_107227_ = ((float) (Math.random() * 0.2F) + 0.8F) * red * f;
        this.f_107228_ = ((float) (Math.random() * 0.2F) + 0.8F) * green * f;
        this.f_107229_ = ((float) (Math.random() * 0.2F) + 0.8F) * blue * f;
        this.f_107663_ *= scale;
        this.reddustParticleScale = this.f_107663_;
        this.f_107225_ = (int) (4.0 / (Math.random() * 0.8 + 0.2));
        this.f_107225_ = (int) ((float) this.f_107225_ * scale);
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 inerp = renderInfo.getPosition();
        float scaley = ((float) this.f_107224_ + partialTicks) / (float) this.f_107225_ * 32.0F;
        scaley = Mth.clamp(scaley, 0.0F, 1.0F);
        this.f_107663_ = this.reddustParticleScale * scaley;
        float width = this.f_107663_ * 0.09F;
        if (this.f_107224_ > this.m_107273_()) {
            this.m_107274_();
        }
        Vec3 Vector3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - Vector3d.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - Vector3d.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - Vector3d.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = renderInfo.rotation();
        } else {
            quaternion = new Quaternionf(renderInfo.rotation());
            float f3 = Mth.lerp(partialTicks, this.f_107204_, this.f_107231_);
            quaternion.mul(Axis.ZP.rotation(f3));
        }
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1 = quaternion.transform(vector3f1);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f4 = this.m_5902_(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f = quaternion.transform(vector3f);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0.0F;
        float f8 = 1.0F;
        float f5 = 0.0F;
        float f6 = 1.0F;
        RenderSystem.setShaderTexture(0, HYDRA_POISON);
        int j = this.getLightColor(partialTicks);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuilder();
        vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        vertexbuffer.m_5483_((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexbuffer.m_5483_((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexbuffer.m_5483_((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexbuffer.m_5483_((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        Tesselator.getInstance().end();
    }

    @Override
    public int getLightColor(float partialTick) {
        return super.m_6355_(partialTick);
    }

    public void onUpdate() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        }
        this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
        if (this.f_107213_ == this.f_107210_) {
            this.f_107215_ *= 1.1;
            this.f_107217_ *= 1.1;
        }
        this.f_107215_ *= 0.96F;
        this.f_107216_ *= 0.96F;
        this.f_107217_ *= 0.96F;
        if (this.f_107218_) {
            this.f_107215_ *= 0.7F;
            this.f_107217_ *= 0.7F;
        }
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}