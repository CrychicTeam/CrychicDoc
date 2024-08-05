package yesman.epicfight.api.animation.property;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.api.utils.math.Vec4f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class MoveCoordFunctions {

    public static final MoveCoordFunctions.MoveCoordGetter DIFF_FROM_PREV_COORD = (animation, entitypatch, coord) -> {
        LivingEntity livingentity = entitypatch.getOriginal();
        AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(animation);
        JointTransform jt = coord.getInterpolatedTransform(player.getElapsedTime());
        JointTransform prevJt = coord.getInterpolatedTransform(player.getPrevElapsedTime());
        Vec4f currentpos = new Vec4f(jt.translation());
        Vec4f prevpos = new Vec4f(prevJt.translation());
        OpenMatrix4f rotationTransform = entitypatch.getModelMatrix(1.0F).removeTranslation();
        OpenMatrix4f localTransform = entitypatch.getArmature().searchJointByName("Root").getLocalTrasnform().removeTranslation();
        rotationTransform.mulBack(localTransform);
        currentpos.transform(rotationTransform);
        prevpos.transform(rotationTransform);
        boolean hasNoGravity = entitypatch.getOriginal().m_20068_();
        boolean moveVertical = (Boolean) animation.getProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL).orElse(false) || animation.getProperty(AnimationProperty.ActionAnimationProperty.COORD).isPresent();
        float dx = prevpos.x - currentpos.x;
        float dy = !moveVertical && !hasNoGravity ? 0.0F : currentpos.y - prevpos.y;
        float dz = prevpos.z - currentpos.z;
        dx = Math.abs(dx) > 1.0E-4F ? dx : 0.0F;
        dz = Math.abs(dz) > 1.0E-4F ? dz : 0.0F;
        BlockPos blockpos = new BlockPos.MutableBlockPos(livingentity.m_20185_(), livingentity.m_20191_().minY - 1.0, livingentity.m_20189_());
        BlockState blockState = livingentity.m_9236_().getBlockState(blockpos);
        AttributeInstance movementSpeed = livingentity.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean soulboost = blockState.m_204336_(BlockTags.SOUL_SPEED_BLOCKS) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, livingentity) > 0;
        float speedFactor = (float) (soulboost ? 1.0 : (double) livingentity.m_9236_().getBlockState(blockpos).m_60734_().getSpeedFactor());
        float moveMultiplier = (float) (animation.getProperty(AnimationProperty.ActionAnimationProperty.AFFECT_SPEED).orElse(false) ? movementSpeed.getValue() / movementSpeed.getBaseValue() : 1.0);
        return new Vec3f(dx * moveMultiplier * speedFactor, dy, dz * moveMultiplier * speedFactor);
    };

    public static final MoveCoordFunctions.MoveCoordGetter WORLD_COORD = (animation, entitypatch, coord) -> {
        LivingEntity livingentity = entitypatch.getOriginal();
        AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(animation);
        JointTransform jt = coord.getInterpolatedTransform(player.getElapsedTime());
        Vec3 entityPos = livingentity.m_20182_();
        return jt.translation().copy().sub(Vec3f.fromDoubleVector(entityPos));
    };

    public static final MoveCoordFunctions.MoveCoordGetter ATTACHED = (animation, entitypatch, coord) -> {
        LivingEntity target = entitypatch.getGrapplingTarget();
        if (target == null) {
            return DIFF_FROM_PREV_COORD.get(animation, entitypatch, coord);
        } else {
            TransformSheet rootCoord = animation.getCoord();
            LivingEntity livingentity = entitypatch.getOriginal();
            AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(animation);
            Vec3f model = rootCoord.getInterpolatedTransform(player.getElapsedTime()).translation();
            Vec3f world = OpenMatrix4f.transform3v(OpenMatrix4f.createRotatorDeg(-target.m_146908_(), Vec3f.Y_AXIS), model, null);
            Vec3f dst = Vec3f.fromDoubleVector(target.m_20182_()).add(world);
            livingentity.m_146922_(Mth.wrapDegrees(target.m_146908_() + 180.0F));
            return dst.sub(Vec3f.fromDoubleVector(livingentity.m_20182_()));
        }
    };

    public static final MoveCoordFunctions.MoveCoordSetter TRACE_DEST_LOCATION_BEGIN = (self, entitypatch, transformSheet) -> {
        LivingEntity attackTarget = entitypatch.getTarget();
        TransformSheet transform = self.getCoord().copyAll();
        Keyframe[] rootKeyframes = transform.getKeyframes();
        if (attackTarget != null && attackTarget.isAlive()) {
            Vec3 start = entitypatch.getOriginal().m_20182_();
            Vec3 toTarget = attackTarget.m_20182_().subtract(start);
            Vec3f modelDst = rootKeyframes[rootKeyframes.length - 1].transform().translation().copy().multiply(-1.0F, 1.0F, -1.0F);
            float yRot = (float) MathUtils.getYRotOfVector(toTarget);
            modelDst.rotate(-yRot, Vec3f.Y_AXIS);
            Vec3 dst = attackTarget.m_20182_().add((double) modelDst.x, (double) modelDst.y, (double) modelDst.z);
            float clampedXRot = MathUtils.rotlerp(entitypatch.getOriginal().m_146909_(), (float) MathUtils.getXRotOfVector(toTarget), 20.0F);
            float clampedYRot = MathUtils.rotlerp(entitypatch.getOriginal().m_146908_(), yRot, entitypatch.getYRotLimit());
            TransformSheet newTransform = transform.getCorrectedWorldCoord(entitypatch, start, dst, -clampedXRot, clampedYRot, 0, rootKeyframes.length);
            transformSheet.readFrom(newTransform);
        } else {
            transform.transform(jt -> {
                Vec3f firstPos = self.getCoord().getKeyframes()[0].transform().translation().copy();
                jt.translation().sub(firstPos);
                LivingEntity original = entitypatch.getOriginal();
                Vec3 pos = original.m_20182_();
                jt.translation().rotate(-original.m_146908_(), Vec3f.Y_AXIS);
                jt.translation().multiply(-1.0F, 1.0F, -1.0F);
                jt.translation().add(Vec3f.fromDoubleVector(pos));
            });
            transformSheet.readFrom(transform);
        }
    };

    public static final MoveCoordFunctions.MoveCoordSetter TRACE_DEST_LOCATION = (self, entitypatch, transformSheet) -> {
        LivingEntity attackTarget = entitypatch.getTarget();
        if (attackTarget != null && attackTarget.isAlive()) {
            TransformSheet transform = self.getCoord().copyAll();
            Keyframe[] rootKeyframes = transform.getKeyframes();
            Vec3 start = entitypatch.getArmature().getActionAnimationCoord().getKeyframes()[0].transform().translation().toDoubleVector();
            Vec3 toTarget = attackTarget.m_20182_().subtract(start);
            Vec3f modelDst = rootKeyframes[rootKeyframes.length - 1].transform().translation().copy().multiply(1.0F, 1.0F, -1.0F);
            float yRot = (float) MathUtils.getYRotOfVector(toTarget);
            modelDst.rotate(-yRot, Vec3f.Y_AXIS);
            Vec3 dst = attackTarget.m_20182_().add(modelDst.toDoubleVector());
            float clampedXRot = (float) MathUtils.getXRotOfVector(toTarget);
            float clampedYRot = MathUtils.rotlerp(entitypatch.getOriginal().m_146908_(), yRot, entitypatch.getYRotLimit());
            TransformSheet newTransform = transform.getCorrectedWorldCoord(entitypatch, start, dst, -clampedXRot, clampedYRot, 0, rootKeyframes.length);
            entitypatch.getOriginal().m_146922_(clampedYRot);
            transformSheet.readFrom(newTransform);
        }
    };

    public static final MoveCoordFunctions.MoveCoordSetter TRACE_LOC_TARGET = (self, entitypatch, transformSheet) -> {
        LivingEntity attackTarget = entitypatch.getTarget();
        if (attackTarget != null && !(Boolean) self.getRealAnimation().getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false)) {
            TransformSheet transform = self.getCoord().copyAll();
            Keyframe[] keyframes = transform.getKeyframes();
            int startFrame = 0;
            int endFrame = keyframes.length - 1;
            Vec3f keyLast = keyframes[endFrame].transform().translation();
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            Vec3 targetpos = attackTarget.m_20182_();
            Vec3 toTarget = targetpos.subtract(pos);
            Vec3 viewVec = entitypatch.getOriginal().m_20252_(1.0F);
            float horizontalDistance = Math.max((float) toTarget.horizontalDistance() - (attackTarget.m_20205_() + entitypatch.getOriginal().m_20205_()) * 0.75F, 0.0F);
            Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
            float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);
            if (scale > 1.0F) {
                float dot = (float) toTarget.normalize().dot(viewVec.normalize());
                scale = Math.max(scale * dot, 1.0F);
            }
            for (int i = startFrame; i <= endFrame; i++) {
                Vec3f translation = keyframes[i].transform().translation();
                if (translation.z < 0.0F) {
                    translation.z *= scale;
                }
            }
            transformSheet.readFrom(transform);
        } else {
            transformSheet.readFrom(self.getCoord().copyAll());
        }
    };

    public static final MoveCoordFunctions.MoveCoordSetter TRACE_LOCROT_TARGET = (self, entitypatch, transformSheet) -> {
        LivingEntity attackTarget = entitypatch.getTarget();
        if (attackTarget != null) {
            TransformSheet transform = self.getCoord().copyAll();
            Keyframe[] keyframes = transform.getKeyframes();
            int startFrame = 0;
            int endFrame = keyframes.length - 1;
            Vec3f keyLast = keyframes[endFrame].transform().translation();
            Vec3 pos = entitypatch.getOriginal().m_20182_();
            Vec3 targetpos = attackTarget.m_20182_();
            Vec3 toTarget = targetpos.subtract(pos);
            float horizontalDistance = Math.max((float) toTarget.horizontalDistance() - (attackTarget.m_20205_() + entitypatch.getOriginal().m_20205_()) * 0.75F, 0.0F);
            Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
            float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);
            float yRot = (float) MathUtils.getYRotOfVector(toTarget);
            float clampedYRot = MathUtils.rotlerp(entitypatch.getOriginal().m_146908_(), yRot, entitypatch.getYRotLimit());
            entitypatch.getOriginal().m_146922_(clampedYRot);
            for (int i = startFrame; i <= endFrame; i++) {
                Vec3f translation = keyframes[i].transform().translation();
                if (translation.z < 0.0F) {
                    translation.z *= scale;
                }
            }
            transformSheet.readFrom(transform);
        } else {
            transformSheet.readFrom(self.getCoord().copyAll());
        }
    };

    public static final MoveCoordFunctions.MoveCoordSetter RAW_COORD = (self, entitypatch, transformSheet) -> transformSheet.readFrom(self.getCoord().copyAll());

    public static final MoveCoordFunctions.MoveCoordSetter RAW_COORD_WITH_X_ROT = (self, entitypatch, transformSheet) -> {
        float xRot = entitypatch.getOriginal().m_146909_();
        TransformSheet sheet = self.getCoord().copyAll();
        for (Keyframe kf : sheet.getKeyframes()) {
            kf.transform().translation().rotate(-xRot, Vec3f.X_AXIS);
        }
        transformSheet.readFrom(sheet);
    };

    public static final MoveCoordFunctions.MoveCoordSetter VEX_TRACE = (self, entitypatch, transformSheet) -> {
        TransformSheet transform = self.getCoord().copyAll();
        Keyframe[] keyframes = transform.getKeyframes();
        int startFrame = 0;
        int endFrame = 6;
        Vec3 pos = entitypatch.getOriginal().m_20182_();
        Vec3 targetpos = entitypatch.getTarget().m_20182_();
        float verticalDistance = (float) (targetpos.y - pos.y);
        Quaternionf rotator = Vec3f.getRotatorBetween(new Vec3f(0.0F, -verticalDistance, (float) targetpos.subtract(pos).horizontalDistance()), new Vec3f(0.0F, 0.0F, 1.0F));
        for (int i = startFrame; i <= endFrame; i++) {
            Vec3f translation = keyframes[i].transform().translation();
            OpenMatrix4f.transform3v(OpenMatrix4f.fromQuaternion(rotator), translation, translation);
        }
        transformSheet.readFrom(transform);
    };

    @FunctionalInterface
    public interface MoveCoordGetter {

        Vec3f get(DynamicAnimation var1, LivingEntityPatch<?> var2, TransformSheet var3);
    }

    @FunctionalInterface
    public interface MoveCoordSetter {

        void set(DynamicAnimation var1, LivingEntityPatch<?> var2, TransformSheet var3);
    }
}