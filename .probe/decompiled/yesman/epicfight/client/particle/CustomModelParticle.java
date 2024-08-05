package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.utils.math.QuaternionUtils;

@OnlyIn(Dist.CLIENT)
public abstract class CustomModelParticle<M extends Mesh<?>> extends Particle {

    protected final M particleMesh;

    protected float pitch;

    protected float pitchO;

    protected float yaw;

    protected float yawO;

    protected float scale = 1.0F;

    protected float scaleO = 1.0F;

    public CustomModelParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, M particleMesh) {
        super(level, x, y, z, xd, yd, zd);
        this.particleMesh = particleMesh;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        this.prepareDraw(poseStack, partialTicks);
        this.particleMesh.drawRawModelNormal(poseStack, vertexConsumer, this.m_6355_(partialTicks), this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public void tick() {
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.pitchO = this.pitch;
            this.yawO = this.yaw;
            this.f_107204_ = this.f_107231_;
            this.scaleO = this.scale;
        }
    }

    public void prepareDraw(PoseStack poseStack, float partialTicks) {
    }

    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        float roll = Mth.lerp(partialTicks, this.f_107204_, this.f_107231_);
        float pitch = Mth.lerp(partialTicks, this.pitchO, this.pitch);
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
        rotation.mul(QuaternionUtils.YP.rotationDegrees(yaw));
        rotation.mul(QuaternionUtils.XP.rotationDegrees(pitch));
        rotation.mul(QuaternionUtils.ZP.rotationDegrees(roll));
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float y = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float z = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        float scale = (float) Mth.lerp((double) partialTicks, (double) this.scaleO, (double) this.scale);
        poseStack.translate(x, y, z);
        poseStack.mulPose(rotation);
        poseStack.scale(scale, scale, scale);
    }
}