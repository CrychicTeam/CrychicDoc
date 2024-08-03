package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class ForceFieldParticle extends TexturedCustomModelParticle {

    private LivingEntityPatch<?> caster;

    public ForceFieldParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, Mesh.RawMesh particleMesh, ResourceLocation texture) {
        super(level, x, y, z, xd, yd, zd, particleMesh, texture);
        this.f_107225_ = 158;
        this.f_107219_ = false;
        this.f_107231_ = (float) xd;
        this.pitch = (float) zd;
        Entity entity = level.getEntity((int) Double.doubleToLongBits(yd));
        if (entity != null) {
            this.caster = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.PARTICLE_MODEL_NO_NORMAL;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.yaw += 36.0F;
        this.scale = this.scale + (float) Math.max(30 - this.f_107224_, 0) / 140.0F;
        if (this.caster != null && this.caster.getStunShield() <= 0.0F) {
            this.m_107274_();
        }
        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {
                Vec3 rand = new Vec3(Math.random() * (double) x, Math.random(), Math.random() * (double) z).normalize().scale(10.0);
                this.f_107208_.addParticle(EpicFightParticles.DUST_CONTRACTIVE.get(), this.f_107212_ + rand.x, this.f_107213_ + rand.y - 1.0, this.f_107214_ + rand.z, -rand.x, -rand.y, -rand.z);
            }
        }
    }

    @Override
    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float y = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float z = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        float scale = (float) Mth.lerp((double) partialTicks, (double) this.scaleO, (double) this.scale);
        poseStack.translate(x, y, z);
        poseStack.mulPose(QuaternionUtils.XP.rotationDegrees(this.pitch));
        poseStack.mulPose(QuaternionUtils.ZP.rotationDegrees(this.f_107231_));
        poseStack.mulPose(QuaternionUtils.YP.rotationDegrees(yaw));
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public int getLightColor(float float0) {
        int i = super.m_6355_(float0);
        int k = i >> 16 & 0xFF;
        return 240 | k << 16;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ForceFieldParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, Meshes.FORCE_FIELD, EnderDragonRenderer.CRYSTAL_BEAM_LOCATION);
        }
    }
}