package yesman.epicfight.client.particle;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.types.LinkAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.api.client.model.ItemSkin;
import yesman.epicfight.api.client.model.ItemSkins;
import yesman.epicfight.api.utils.math.CubicBezierCurve;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class TrailParticle extends TextureSheetParticle {

    private final Joint joint;

    private final TrailInfo trailInfo;

    private final StaticAnimation animation;

    private final LivingEntityPatch<?> entitypatch;

    private final List<TrailParticle.TrailEdge> invisibleTrailEdges;

    private final List<TrailParticle.TrailEdge> visibleTrailEdges;

    private boolean animationEnd;

    private float startEdgeCorrection = 0.0F;

    protected TrailParticle(ClientLevel level, LivingEntityPatch<?> entitypatch, Joint joint, StaticAnimation animation, TrailInfo trailInfo, SpriteSet spriteSet) {
        super(level, 0.0, 0.0, 0.0);
        this.joint = joint;
        this.entitypatch = entitypatch;
        this.animation = animation;
        this.invisibleTrailEdges = Lists.newLinkedList();
        this.visibleTrailEdges = Lists.newLinkedList();
        this.f_107219_ = false;
        this.trailInfo = trailInfo;
        Vec3 entityPos = entitypatch.getOriginal().m_20182_();
        float size = (float) Math.max(this.trailInfo.start.length(), this.trailInfo.end.length()) * 2.0F;
        this.m_107250_(size, size);
        this.m_6257_(entityPos.x, entityPos.y + (double) entitypatch.getOriginal().m_20192_(), entityPos.z);
        this.m_108339_(spriteSet);
        Pose prevPose = this.entitypatch.getArmature().getPrevPose();
        Pose middlePose = this.entitypatch.getArmature().getPose(0.5F);
        Pose currentPose = this.entitypatch.getArmature().getCurrentPose();
        Vec3 posOld = this.entitypatch.getOriginal().m_20318_(0.0F);
        Vec3 posMid = this.entitypatch.getOriginal().m_20318_(0.5F);
        Vec3 posCur = this.entitypatch.getOriginal().m_20318_(1.0F);
        OpenMatrix4f prvmodelTf = OpenMatrix4f.createTranslation((float) posOld.x, (float) posOld.y, (float) posOld.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(0.0F)));
        OpenMatrix4f middleModelTf = OpenMatrix4f.createTranslation((float) posMid.x, (float) posMid.y, (float) posMid.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(0.5F)));
        OpenMatrix4f curModelTf = OpenMatrix4f.createTranslation((float) posCur.x, (float) posCur.y, (float) posCur.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(1.0F)));
        OpenMatrix4f prevJointTf = this.entitypatch.getArmature().getBindedTransformFor(prevPose, this.joint).mulFront(prvmodelTf);
        OpenMatrix4f middleJointTf = this.entitypatch.getArmature().getBindedTransformFor(middlePose, this.joint).mulFront(middleModelTf);
        OpenMatrix4f currentJointTf = this.entitypatch.getArmature().getBindedTransformFor(currentPose, this.joint).mulFront(curModelTf);
        Vec3 prevStartPos = OpenMatrix4f.transform(prevJointTf, trailInfo.start);
        Vec3 prevEndPos = OpenMatrix4f.transform(prevJointTf, trailInfo.end);
        Vec3 middleStartPos = OpenMatrix4f.transform(middleJointTf, trailInfo.start);
        Vec3 middleEndPos = OpenMatrix4f.transform(middleJointTf, trailInfo.end);
        Vec3 currentStartPos = OpenMatrix4f.transform(currentJointTf, trailInfo.start);
        Vec3 currentEndPos = OpenMatrix4f.transform(currentJointTf, trailInfo.end);
        this.invisibleTrailEdges.add(new TrailParticle.TrailEdge(prevStartPos, prevEndPos, this.trailInfo.trailLifetime));
        this.invisibleTrailEdges.add(new TrailParticle.TrailEdge(middleStartPos, middleEndPos, this.trailInfo.trailLifetime));
        this.invisibleTrailEdges.add(new TrailParticle.TrailEdge(currentStartPos, currentEndPos, this.trailInfo.trailLifetime));
        this.f_107227_ = this.trailInfo.rCol;
        this.f_107228_ = this.trailInfo.gCol;
        this.f_107229_ = this.trailInfo.bCol;
    }

    @Override
    public void tick() {
        AnimationPlayer animPlayer = this.entitypatch.<Animator>getAnimator().getPlayerFor(this.animation);
        this.visibleTrailEdges.removeIf(v -> !v.isAlive());
        if (this.animationEnd) {
            if (this.f_107225_-- == 0) {
                this.m_107274_();
            }
        } else if (!this.entitypatch.getOriginal().isAlive() || this.animation != animPlayer.getAnimation().getRealAnimation() || animPlayer.getElapsedTime() > this.trailInfo.endTime) {
            this.animationEnd = true;
            this.f_107225_ = this.trailInfo.trailLifetime;
        }
        if (!(this.trailInfo.fadeTime > 0.0F) || !(this.trailInfo.endTime < animPlayer.getElapsedTime())) {
            double xd = Math.pow(this.entitypatch.getOriginal().m_20185_() - this.entitypatch.getOriginal().f_19854_, 2.0);
            double yd = Math.pow(this.entitypatch.getOriginal().m_20186_() - this.entitypatch.getOriginal().f_19855_, 2.0);
            double zd = Math.pow(this.entitypatch.getOriginal().m_20189_() - this.entitypatch.getOriginal().f_19856_, 2.0);
            float move = (float) Math.sqrt(xd + yd + zd) * 2.0F;
            this.m_107250_(this.f_107221_ + move, this.f_107222_ + move);
            boolean isTrailInvisible = animPlayer.getAnimation() instanceof LinkAnimation || animPlayer.getElapsedTime() <= this.trailInfo.startTime;
            boolean isFirstTrail = this.visibleTrailEdges.size() == 0;
            boolean needCorrection = !isTrailInvisible && isFirstTrail;
            if (needCorrection) {
                float startCorrection = Math.max((this.trailInfo.startTime - animPlayer.getPrevElapsedTime()) / (animPlayer.getElapsedTime() - animPlayer.getPrevElapsedTime()), 0.0F);
                this.startEdgeCorrection = (float) (this.trailInfo.interpolateCount * 2) * startCorrection;
            }
            TrailInfo trailInfo = this.trailInfo;
            Pose prevPose = this.entitypatch.getArmature().getPrevPose();
            Pose middlePose = this.entitypatch.getArmature().getPose(0.5F);
            Pose currentPose = this.entitypatch.getArmature().getCurrentPose();
            Vec3 posOld = this.entitypatch.getOriginal().m_20318_(0.0F);
            Vec3 posMid = this.entitypatch.getOriginal().m_20318_(0.5F);
            Vec3 posCur = this.entitypatch.getOriginal().m_20318_(1.0F);
            OpenMatrix4f prvmodelTf = OpenMatrix4f.createTranslation((float) posOld.x, (float) posOld.y, (float) posOld.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(0.0F)));
            OpenMatrix4f middleModelTf = OpenMatrix4f.createTranslation((float) posMid.x, (float) posMid.y, (float) posMid.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(0.5F)));
            OpenMatrix4f curModelTf = OpenMatrix4f.createTranslation((float) posCur.x, (float) posCur.y, (float) posCur.z).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(this.entitypatch.getModelMatrix(1.0F)));
            OpenMatrix4f prevJointTf = this.entitypatch.getArmature().getBindedTransformFor(prevPose, this.joint).mulFront(prvmodelTf);
            OpenMatrix4f middleJointTf = this.entitypatch.getArmature().getBindedTransformFor(middlePose, this.joint).mulFront(middleModelTf);
            OpenMatrix4f currentJointTf = this.entitypatch.getArmature().getBindedTransformFor(currentPose, this.joint).mulFront(curModelTf);
            Vec3 prevStartPos = OpenMatrix4f.transform(prevJointTf, trailInfo.start);
            Vec3 prevEndPos = OpenMatrix4f.transform(prevJointTf, trailInfo.end);
            Vec3 middleStartPos = OpenMatrix4f.transform(middleJointTf, trailInfo.start);
            Vec3 middleEndPos = OpenMatrix4f.transform(middleJointTf, trailInfo.end);
            Vec3 currentStartPos = OpenMatrix4f.transform(currentJointTf, trailInfo.start);
            Vec3 currentEndPos = OpenMatrix4f.transform(currentJointTf, trailInfo.end);
            List<Vec3> finalStartPositions;
            List<Vec3> finalEndPositions;
            boolean visibleTrail;
            if (isTrailInvisible) {
                finalStartPositions = Lists.newArrayList();
                finalEndPositions = Lists.newArrayList();
                finalStartPositions.add(prevStartPos);
                finalStartPositions.add(middleStartPos);
                finalEndPositions.add(prevEndPos);
                finalEndPositions.add(middleEndPos);
                this.invisibleTrailEdges.clear();
                visibleTrail = false;
            } else {
                List<Vec3> startPosList = Lists.newArrayList();
                List<Vec3> endPosList = Lists.newArrayList();
                TrailParticle.TrailEdge edge1;
                TrailParticle.TrailEdge edge2;
                if (isFirstTrail) {
                    int lastIdx = this.invisibleTrailEdges.size() - 1;
                    edge1 = (TrailParticle.TrailEdge) this.invisibleTrailEdges.get(lastIdx);
                    edge2 = new TrailParticle.TrailEdge(prevStartPos, prevEndPos, -1);
                } else {
                    edge1 = (TrailParticle.TrailEdge) this.visibleTrailEdges.get(this.visibleTrailEdges.size() - (this.trailInfo.interpolateCount / 2 + 1));
                    edge2 = (TrailParticle.TrailEdge) this.visibleTrailEdges.get(this.visibleTrailEdges.size() - 1);
                    edge2.lifetime++;
                }
                startPosList.add(edge1.start);
                endPosList.add(edge1.end);
                startPosList.add(edge2.start);
                endPosList.add(edge2.end);
                startPosList.add(middleStartPos);
                endPosList.add(middleEndPos);
                startPosList.add(currentStartPos);
                endPosList.add(currentEndPos);
                finalStartPositions = CubicBezierCurve.getBezierInterpolatedPoints(startPosList, 1, 3, this.trailInfo.interpolateCount);
                finalEndPositions = CubicBezierCurve.getBezierInterpolatedPoints(endPosList, 1, 3, this.trailInfo.interpolateCount);
                if (!isFirstTrail) {
                    finalStartPositions.remove(0);
                    finalEndPositions.remove(0);
                }
                visibleTrail = true;
            }
            this.makeTrailEdges(finalStartPositions, finalEndPositions, visibleTrail ? this.visibleTrailEdges : this.invisibleTrailEdges);
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        if (this.visibleTrailEdges.size() >= 1) {
            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
            AbstractTexture abstracttexture = texturemanager.getTexture(this.trailInfo.texturePath);
            RenderSystem.bindTexture(abstracttexture.getId());
            RenderSystem.texParameter(3553, 10242, 33071);
            RenderSystem.texParameter(3553, 10243, 33071);
            RenderSystem.setShaderTexture(0, abstracttexture.getId());
            PoseStack poseStack = new PoseStack();
            int light = this.m_6355_(partialTick);
            this.setupPoseStack(poseStack, camera, partialTick);
            Matrix4f matrix4f = poseStack.last().pose();
            int edges = this.visibleTrailEdges.size() - 1;
            boolean startFade = ((TrailParticle.TrailEdge) this.visibleTrailEdges.get(0)).lifetime == 1;
            boolean endFade = ((TrailParticle.TrailEdge) this.visibleTrailEdges.get(edges)).lifetime == this.trailInfo.trailLifetime;
            float startEdge = (startFade ? (float) (this.trailInfo.interpolateCount * 2) * partialTick : 0.0F) + this.startEdgeCorrection;
            float endEdge = endFade ? Math.min((float) edges - (float) (this.trailInfo.interpolateCount * 2) * (1.0F - partialTick), (float) (edges - 1)) : (float) (edges - 1);
            float interval = 1.0F / (endEdge - startEdge);
            float fading = 1.0F;
            if (this.animationEnd) {
                if (this.trailInfo.fadeTime > 0.0F) {
                    fading = (float) this.f_107225_ / (float) this.trailInfo.trailLifetime;
                } else {
                    fading = Mth.clamp(((float) this.f_107225_ + (1.0F - partialTick)) / (float) this.trailInfo.trailLifetime, 0.0F, 1.0F);
                }
            }
            float partialStartEdge = interval * (startEdge % 1.0F);
            float from = -partialStartEdge;
            float to = -partialStartEdge + interval;
            for (int i = (int) startEdge; i < (int) endEdge + 1; i++) {
                TrailParticle.TrailEdge e1 = (TrailParticle.TrailEdge) this.visibleTrailEdges.get(i);
                TrailParticle.TrailEdge e2 = (TrailParticle.TrailEdge) this.visibleTrailEdges.get(i + 1);
                Vector4f pos1 = new Vector4f((float) e1.start.x, (float) e1.start.y, (float) e1.start.z, 1.0F);
                Vector4f pos2 = new Vector4f((float) e1.end.x, (float) e1.end.y, (float) e1.end.z, 1.0F);
                Vector4f pos3 = new Vector4f((float) e2.end.x, (float) e2.end.y, (float) e2.end.z, 1.0F);
                Vector4f pos4 = new Vector4f((float) e2.start.x, (float) e2.start.y, (float) e2.start.z, 1.0F);
                pos1.mul(matrix4f);
                pos2.mul(matrix4f);
                pos3.mul(matrix4f);
                pos4.mul(matrix4f);
                float alphaFrom = Mth.clamp(from, 0.0F, 1.0F);
                float alphaTo = Mth.clamp(to, 0.0F, 1.0F);
                vertexConsumer.vertex((double) pos1.x(), (double) pos1.y(), (double) pos1.z()).uv(from, 1.0F).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_ * alphaFrom * fading).uv2(light).endVertex();
                vertexConsumer.vertex((double) pos2.x(), (double) pos2.y(), (double) pos2.z()).uv(from, 0.0F).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_ * alphaFrom * fading).uv2(light).endVertex();
                vertexConsumer.vertex((double) pos3.x(), (double) pos3.y(), (double) pos3.z()).uv(to, 0.0F).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_ * alphaTo * fading).uv2(light).endVertex();
                vertexConsumer.vertex((double) pos4.x(), (double) pos4.y(), (double) pos4.z()).uv(to, 1.0F).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_ * alphaTo * fading).uv2(light).endVertex();
                from += interval;
                to += interval;
            }
        }
    }

    public boolean shouldCull() {
        return false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRAIL;
    }

    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        Vec3 vec3 = camera.getPosition();
        float x = (float) (-vec3.x());
        float y = (float) (-vec3.y());
        float z = (float) (-vec3.z());
        poseStack.translate(x, y, z);
        poseStack.mulPose(rotation);
    }

    private void makeTrailEdges(List<Vec3> startPositions, List<Vec3> endPositions, List<TrailParticle.TrailEdge> dest) {
        for (int i = 0; i < startPositions.size(); i++) {
            dest.add(new TrailParticle.TrailEdge((Vec3) startPositions.get(i), (Vec3) endPositions.get(i), this.trailInfo.trailLifetime));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            int eid = (int) Double.doubleToRawLongBits(x);
            int modid = (int) Double.doubleToRawLongBits(y);
            int animid = (int) Double.doubleToRawLongBits(z);
            int jointId = (int) Double.doubleToRawLongBits(xSpeed);
            int idx = (int) Double.doubleToRawLongBits(ySpeed);
            Entity entity = level.getEntity(eid);
            if (entity != null) {
                LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
                StaticAnimation animation = EpicFightMod.getInstance().animationManager.findAnimationById(modid, animid);
                Optional<List<TrailInfo>> trailInfo = animation.getProperty(ClientAnimationProperties.TRAIL_EFFECT);
                TrailInfo result = (TrailInfo) ((List) trailInfo.get()).get(idx);
                if (result.hand != null) {
                    ItemStack stack = entitypatch.getOriginal().getItemInHand(result.hand);
                    ItemSkin itemSkin = ItemSkins.getItemSkin(stack.getItem());
                    if (itemSkin != null) {
                        result = itemSkin.trailInfo.overwrite(result);
                    }
                }
                if (entitypatch != null && animation != null && trailInfo.isPresent()) {
                    return new TrailParticle(level, entitypatch, entitypatch.getArmature().searchJointById(jointId), animation, result, this.spriteSet);
                }
            }
            return null;
        }
    }

    private static class TrailEdge {

        final Vec3 start;

        final Vec3 end;

        int lifetime;

        public TrailEdge(Vec3 start, Vec3 end, int lifetime) {
            this.start = start;
            this.end = end;
            this.lifetime = lifetime;
        }

        boolean isAlive() {
            return --this.lifetime > 0;
        }
    }
}