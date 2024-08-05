package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
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

public class ParticleDragonFlame extends TextureSheetParticle {

    private static final ResourceLocation DRAGONFLAME = new ResourceLocation("iceandfire:textures/particles/dragon_flame.png");

    private final float dragonSize;

    private final double initialX;

    private final double initialY;

    private final double initialZ;

    private double targetX;

    private double targetY;

    private double targetZ;

    private final int touchedTime = 0;

    private final float speedBonus;

    @Nullable
    private EntityDragonBase dragon;

    public ParticleDragonFlame(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float dragonSize) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.initialX = xCoordIn;
        this.initialY = yCoordIn;
        this.initialZ = zCoordIn;
        this.targetX = xCoordIn + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 1.75F * dragonSize);
        this.targetY = yCoordIn + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 1.75F * dragonSize);
        this.targetZ = zCoordIn + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 1.75F * dragonSize);
        this.m_107264_(this.f_107212_, this.f_107213_, this.f_107214_);
        this.dragonSize = dragonSize;
        this.speedBonus = this.f_107223_.nextFloat() * 0.015F;
    }

    public ParticleDragonFlame(ClientLevel world, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase, int startingAge) {
        this(world, x, y, z, motX, motY, motZ, Mth.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3.0F));
        this.dragon = entityDragonBase;
        this.targetX = this.dragon.burnParticleX + (double) (this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 3.5;
        this.targetY = this.dragon.burnParticleY + (double) (this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 3.5;
        this.targetZ = this.dragon.burnParticleZ + (double) (this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 3.5;
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.f_107224_ = startingAge;
    }

    @Override
    public int getLifetime() {
        return this.dragon == null ? 10 : 30;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 inerp = renderInfo.getPosition();
        if (this.f_107224_ > this.getLifetime()) {
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
        RenderSystem.setShaderTexture(0, DRAGONFLAME);
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
        return 240;
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (this.dragon == null) {
            float distX = (float) (this.initialX - this.f_107212_);
            float distZ = (float) (this.initialZ - this.f_107214_);
            this.f_107215_ = this.f_107215_ + (double) (distX * -0.01F * this.dragonSize * this.f_107223_.nextFloat());
            this.f_107217_ = this.f_107217_ + (double) (distZ * -0.01F * this.dragonSize * this.f_107223_.nextFloat());
            this.f_107216_ = this.f_107216_ + (double) (0.015F * this.f_107223_.nextFloat());
        } else {
            double d2 = this.targetX - this.initialX;
            double d3 = this.targetY - this.initialY;
            double d4 = this.targetZ - this.initialZ;
            double dist = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            float speed = 0.015F + this.speedBonus;
            this.f_107215_ += d2 * (double) speed;
            this.f_107216_ += d3 * (double) speed;
            this.f_107217_ += d4 * (double) speed;
        }
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}