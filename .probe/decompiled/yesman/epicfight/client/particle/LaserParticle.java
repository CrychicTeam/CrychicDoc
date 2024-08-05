package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.QuaternionUtils;

@OnlyIn(Dist.CLIENT)
public class LaserParticle extends CustomModelParticle<Mesh.RawMesh> {

    private final float length;

    private final float xRot;

    private final float yRot;

    public LaserParticle(ClientLevel level, double x, double y, double z, double toX, double toY, double toZ) {
        super(level, x, y, z, 0.0, 0.0, 0.0, Meshes.LASER);
        this.f_107225_ = 5;
        Vec3 direction = new Vec3(toX - x, toY - y, toZ - z);
        Vec3 start = new Vec3(x, y, z);
        Vec3 destination = start.add(direction.normalize().scale(200.0));
        BlockHitResult hitResult = level.m_45547_(new ClipContext(start, destination, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        double xLength = hitResult.m_82450_().x - x;
        double yLength = hitResult.m_82450_().y - y;
        double zLength = hitResult.m_82450_().z - z;
        double horizontalDistance = (double) ((float) Math.sqrt(xLength * xLength + zLength * zLength));
        this.length = (float) Math.sqrt(xLength * xLength + yLength * yLength + zLength * zLength);
        this.yRot = (float) (-Math.atan2(zLength, xLength) * (180.0 / Math.PI)) - 90.0F;
        this.xRot = (float) (Math.atan2(yLength, horizontalDistance) * (180.0 / Math.PI));
        int smokeCount = (int) this.length * 4;
        for (int i = 0; i < smokeCount; i++) {
            level.addParticle(ParticleTypes.SMOKE, x + xLength / (double) smokeCount * (double) i, y + yLength / (double) smokeCount * (double) i, z + zLength / (double) smokeCount * (double) i, 0.0, 0.0, 0.0);
        }
        this.m_107259_(new AABB(x, y, z, toX, toY, toZ));
    }

    @Override
    public void prepareDraw(PoseStack poseStack, float partialTicks) {
        poseStack.mulPose(QuaternionUtils.YP.rotationDegrees(this.yRot));
        poseStack.mulPose(QuaternionUtils.XP.rotationDegrees(this.xRot));
        float progression = ((float) this.f_107224_ + partialTicks) / (float) (this.f_107225_ + 1);
        float scale = Mth.sin(progression * (float) Math.PI);
        float zScale = progression > 0.5F ? 1.0F : Mth.sin(progression * (float) Math.PI);
        poseStack.scale(scale, scale, zScale * this.length);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        super.render(vertexConsumer, camera, partialTicks);
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        this.prepareDraw(poseStack, partialTicks);
        poseStack.scale(1.1F, 1.1F, 1.1F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRANSLUCENT_GLOWING;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double startX, double startY, double startZ, double endX, double endY, double endZ) {
            return new LaserParticle(level, startX, startY, startZ, endX, endY, endZ);
        }
    }
}