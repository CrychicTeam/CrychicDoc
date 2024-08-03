package yesman.epicfight.client.world.capabilites.entitypatch.player;

import javax.annotation.Nonnull;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.forgeevent.RenderEpicFightPlayerEvent;
import yesman.epicfight.api.client.forgeevent.UpdatePlayerMotionEvent;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;

@OnlyIn(Dist.CLIENT)
public class AbstractClientPlayerPatch<T extends AbstractClientPlayer> extends PlayerPatch<T> {

    protected float prevYaw;

    protected float bodyYaw;

    public float prevBodyYaw;

    private Item prevHeldItem;

    private Item prevHeldItemOffHand;

    public void onJoinWorld(T entityIn, EntityJoinLevelEvent event) {
        super.onJoinWorld(entityIn, event);
        this.prevHeldItem = Items.AIR;
        this.prevHeldItemOffHand = Items.AIR;
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (!this.state.updateLivingMotion() && considerInaction) {
            this.currentLivingMotion = LivingMotions.INACTION;
        } else {
            ClientAnimator animator = this.getClientAnimator();
            if (this.original.m_21255_() || this.original.m_21209_()) {
                this.currentLivingMotion = LivingMotions.FLY;
            } else if (this.original.m_20202_() != null) {
                if (this.original.m_20202_() instanceof PlayerRideableJumping) {
                    this.currentLivingMotion = LivingMotions.MOUNT;
                } else {
                    this.currentLivingMotion = LivingMotions.SIT;
                }
            } else if (this.original.m_6067_()) {
                this.currentLivingMotion = LivingMotions.SWIM;
            } else if (this.original.m_5803_()) {
                this.currentLivingMotion = LivingMotions.SLEEP;
            } else if (!this.original.m_20096_() && this.original.m_6147_()) {
                this.currentLivingMotion = LivingMotions.CLIMB;
                double y = this.original.f_36106_ - this.original.f_36103_;
                if (Math.abs(y) < 0.04) {
                    animator.baseLayer.pause();
                } else {
                    animator.baseLayer.resume();
                    animator.baseLayer.animationPlayer.setReversed(y < 0.0);
                }
            } else if (!this.original.m_150110_().flying) {
                if (this.original.m_5842_() && this.original.f_36106_ - this.original.f_36103_ < -0.005) {
                    this.currentLivingMotion = LivingMotions.FLOAT;
                } else if (this.original.f_36106_ - this.original.f_36103_ < -0.4F || this.isAirborneState()) {
                    this.currentLivingMotion = LivingMotions.FALL;
                } else if (this.isMoving()) {
                    if (this.original.m_6047_()) {
                        this.currentLivingMotion = LivingMotions.SNEAK;
                    } else if (this.original.m_20142_()) {
                        this.currentLivingMotion = LivingMotions.RUN;
                    } else {
                        this.currentLivingMotion = LivingMotions.WALK;
                    }
                    animator.baseLayer.animationPlayer.setReversed(this.original.f_20902_ < 0.0F);
                } else {
                    animator.baseLayer.animationPlayer.setReversed(false);
                    if (this.original.m_6047_()) {
                        this.currentLivingMotion = LivingMotions.KNEEL;
                    } else {
                        this.currentLivingMotion = LivingMotions.IDLE;
                    }
                }
            } else if (this.isMoving()) {
                this.currentLivingMotion = LivingMotions.CREATIVE_FLY;
            } else {
                this.currentLivingMotion = LivingMotions.CREATIVE_IDLE;
            }
        }
        MinecraftForge.EVENT_BUS.post(new UpdatePlayerMotionEvent.BaseLayer(this, this.currentLivingMotion));
        CapabilityItem activeItemCap = this.getHoldingItemCapability(this.original.m_7655_());
        if (this.original.m_6117_()) {
            UseAnim useAnim = this.original.m_21211_().getUseAnimation();
            UseAnim capUseAnim = activeItemCap.getUseAnimation(this);
            if (useAnim == UseAnim.BLOCK || capUseAnim == UseAnim.BLOCK) {
                if (activeItemCap.getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD) {
                    this.currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
                } else {
                    this.currentCompositeMotion = LivingMotions.BLOCK;
                }
            } else if (useAnim == UseAnim.BOW || useAnim == UseAnim.SPEAR) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (useAnim == UseAnim.CROSSBOW) {
                this.currentCompositeMotion = LivingMotions.RELOAD;
            } else if (useAnim == UseAnim.DRINK) {
                this.currentCompositeMotion = LivingMotions.DRINK;
            } else if (useAnim == UseAnim.EAT) {
                this.currentCompositeMotion = LivingMotions.EAT;
            } else if (useAnim == UseAnim.SPYGLASS) {
                this.currentCompositeMotion = LivingMotions.SPECTATE;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
        } else {
            if (this.original.m_21205_().getItem() instanceof ProjectileWeaponItem && CrossbowItem.isCharged(this.original.m_21205_())) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation()) {
                this.currentCompositeMotion = LivingMotions.NONE;
            } else if (this.original.f_20911_ && this.original.m_21257_().isEmpty()) {
                this.currentCompositeMotion = LivingMotions.DIGGING;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
            if (this.getClientAnimator().isAiming() && this.currentCompositeMotion != LivingMotions.AIM && activeItemCap instanceof RangedWeaponCapability) {
                this.playReboundAnimation();
            }
        }
        MinecraftForge.EVENT_BUS.post(new UpdatePlayerMotionEvent.CompositeLayer(this, this.currentCompositeMotion));
    }

    @Override
    protected void clientTick(LivingEvent.LivingTickEvent event) {
        this.prevYaw = this.modelYRot;
        this.prevBodyYaw = this.bodyYaw;
        if (!this.getEntityState().updateLivingMotion()) {
            this.original.f_20883_ = this.original.m_146908_();
        }
        this.bodyYaw = this.original.f_20883_;
        boolean isMainHandChanged = this.prevHeldItem != this.original.m_150109_().getSelected().getItem();
        boolean isOffHandChanged = this.prevHeldItemOffHand != this.original.m_150109_().offhand.get(0).getItem();
        if (isMainHandChanged || isOffHandChanged) {
            this.updateHeldItem(this.getHoldingItemCapability(InteractionHand.MAIN_HAND), this.getHoldingItemCapability(InteractionHand.OFF_HAND));
            if (isMainHandChanged) {
                this.prevHeldItem = this.original.m_150109_().getSelected().getItem();
            }
            if (isOffHandChanged) {
                this.prevHeldItemOffHand = this.original.m_150109_().offhand.get(0).getItem();
            }
        }
        if (this.original.f_20919_ == 1) {
            this.getClientAnimator().playDeathAnimation();
        }
    }

    protected boolean isMoving() {
        return Math.abs(this.original.f_20900_) > 0.01F || Math.abs(this.original.f_20902_) > 0.01F;
    }

    public void updateHeldItem(CapabilityItem mainHandCap, CapabilityItem offHandCap) {
        this.cancelAnyAction();
    }

    @Override
    public void reserveAnimation(StaticAnimation animation) {
        this.animator.reserveAnimation(animation);
    }

    @Override
    public void playAnimationSynchronized(StaticAnimation animation, float convertTimeModifier, LivingEntityPatch.AnimationPacketProvider packetProvider) {
    }

    @Override
    public boolean overrideRender() {
        boolean originalShouldRender = this.isBattleMode() || !EpicFightMod.CLIENT_CONFIGS.filterAnimation.getValue();
        RenderEpicFightPlayerEvent renderepicfightplayerevent = new RenderEpicFightPlayerEvent(this, originalShouldRender);
        MinecraftForge.EVENT_BUS.post(renderepicfightplayerevent);
        return renderepicfightplayerevent.getShouldRender();
    }

    @Override
    public boolean consumeStamina(float amount) {
        return true;
    }

    @Override
    public boolean shouldMoveOnCurrentSide(ActionAnimation actionAnimation) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public OpenMatrix4f getHeadMatrix(float partialTick) {
        float yaw = 0.0F;
        float pitch = 0.0F;
        float prvePitch = 0.0F;
        UseAnim useAnim = this.original.m_21211_().getUseAnimation();
        if (!this.getOriginal().m_6117_() || useAnim != UseAnim.DRINK && useAnim != UseAnim.EAT && useAnim != UseAnim.SPYGLASS) {
            if (!this.getEntityState().inaction() && (this.original.m_20202_() == null || !(this.original.m_20202_() instanceof LivingEntity)) && (this.original.m_20096_() || !this.original.m_6147_())) {
                float f = MathUtils.lerpBetween(this.prevBodyYaw, this.bodyYaw, partialTick);
                float f1 = MathUtils.lerpBetween(this.original.f_20886_, this.original.f_20885_, partialTick);
                yaw = f1 - f;
            } else {
                yaw = 0.0F;
            }
            if (!this.original.m_21255_() && !this.original.m_6067_()) {
                prvePitch = this.original.f_19860_;
                pitch = this.original.m_146909_();
            }
        }
        return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, prvePitch, pitch, yaw, yaw, partialTick, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTick) {
        if (this.original.m_21209_()) {
            OpenMatrix4f mat = MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, partialTick, 0.9375F, 0.9375F, 0.9375F);
            float yawDegree = MathUtils.lerpBetween(this.original.f_19859_, this.original.m_146908_(), partialTick);
            float pitchDegree = MathUtils.lerpBetween(this.original.f_19860_, this.original.m_146909_(), partialTick);
            mat.rotateDeg(-yawDegree, Vec3f.Y_AXIS).rotateDeg(-pitchDegree, Vec3f.X_AXIS).rotateDeg(((float) this.original.f_19797_ + partialTick) * -55.0F, Vec3f.Z_AXIS).translate(0.0F, -0.39F, 0.0F);
            return mat;
        } else if (this.original.m_21255_()) {
            OpenMatrix4f mat = MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, partialTick, 0.9375F, 0.9375F, 0.9375F);
            float f1 = (float) this.original.m_21256_() + partialTick;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            mat.rotateDeg(-Mth.rotLerp(partialTick, this.original.f_20884_, this.original.f_20883_), Vec3f.Y_AXIS).rotateDeg(f2 * -this.original.m_146909_(), Vec3f.X_AXIS);
            Vec3 vec3d = this.original.m_20252_(partialTick);
            Vec3 vec3d1 = this.original.m_20184_();
            double d0 = vec3d1.horizontalDistanceSqr();
            double d1 = vec3d.horizontalDistanceSqr();
            if (d0 > 0.0 && d1 > 0.0) {
                double d2 = (vec3d1.x * vec3d.x + vec3d1.z * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = vec3d1.x * vec3d.z - vec3d1.z * vec3d.x;
                mat.rotate((float) (-(Math.signum(d3) * Math.acos(d2))), Vec3f.Z_AXIS);
            }
            return mat;
        } else if (this.original.m_5803_()) {
            BlockState blockstate = this.original.m_146900_();
            float yaw = 0.0F;
            if (blockstate.isBed(this.original.m_9236_(), (BlockPos) this.original.m_21257_().orElse(null), this.original) && blockstate.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                switch((Direction) blockstate.m_61143_(BlockStateProperties.HORIZONTAL_FACING)) {
                    case EAST:
                        yaw = 90.0F;
                        break;
                    case WEST:
                        yaw = -90.0F;
                        break;
                    case SOUTH:
                        yaw = 180.0F;
                }
            }
            return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, yaw, yaw, 0.0F, 0.9375F, 0.9375F, 0.9375F);
        } else {
            Direction direction;
            if ((direction = this.getLadderDirection(this.original.m_146900_(), this.original.m_9236_(), this.original.m_20183_(), this.original)) != Direction.UP) {
                float yaw = 0.0F;
                switch(direction) {
                    case EAST:
                        yaw = 90.0F;
                        break;
                    case WEST:
                        yaw = -90.0F;
                        break;
                    case SOUTH:
                        yaw = 180.0F;
                }
                return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, yaw, yaw, 0.0F, 0.9375F, 0.9375F, 0.9375F);
            } else {
                float prevPitch = 0.0F;
                float pitch = 0.0F;
                float prevRotYaw;
                float rotyaw;
                if (this.original.m_20202_() instanceof LivingEntity ridingEntity) {
                    prevRotYaw = ridingEntity.yBodyRotO;
                    rotyaw = ridingEntity.yBodyRot;
                } else {
                    float yaw = MathUtils.lerpBetween(this.prevYaw, this.modelYRot, partialTick);
                    prevRotYaw = this.prevBodyYaw + yaw;
                    rotyaw = this.bodyYaw + yaw;
                }
                if (!this.getEntityState().inaction() && this.original.m_20089_() == Pose.SWIMMING) {
                    float f = this.original.m_20998_(partialTick);
                    float f3 = this.original.m_20069_() ? this.original.m_146909_() : 0.0F;
                    float f4 = Mth.lerp(f, 0.0F, f3);
                    prevPitch = f4;
                    pitch = f4;
                }
                return MathUtils.getModelMatrixIntegral(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, prevPitch, pitch, prevRotYaw, rotyaw, partialTick, 0.9375F, 0.9375F, 0.9375F);
            }
        }
    }

    public void setYaw(float bodyYaw) {
        this.bodyYaw = bodyYaw;
    }

    public float getBodyYaw() {
        return this.bodyYaw;
    }

    public Direction getLadderDirection(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull LivingEntity entity) {
        boolean isSpectator = entity instanceof Player && entity.m_5833_();
        if (!isSpectator && !this.original.m_20096_() && this.original.m_6084_()) {
            if (ForgeConfig.SERVER.fullBoundingBoxLadders.get()) {
                if (state.isLadder(world, pos, entity)) {
                    if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                        return (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                    }
                    if (state.m_61138_(BlockStateProperties.UP) && (Boolean) state.m_61143_(BlockStateProperties.UP)) {
                        return Direction.UP;
                    }
                    if (state.m_61138_(BlockStateProperties.NORTH) && (Boolean) state.m_61143_(BlockStateProperties.NORTH)) {
                        return Direction.SOUTH;
                    }
                    if (state.m_61138_(BlockStateProperties.WEST) && (Boolean) state.m_61143_(BlockStateProperties.WEST)) {
                        return Direction.EAST;
                    }
                    if (state.m_61138_(BlockStateProperties.SOUTH) && (Boolean) state.m_61143_(BlockStateProperties.SOUTH)) {
                        return Direction.NORTH;
                    }
                    if (state.m_61138_(BlockStateProperties.EAST) && (Boolean) state.m_61143_(BlockStateProperties.EAST)) {
                        return Direction.WEST;
                    }
                }
            } else {
                AABB bb = entity.m_20191_();
                int mX = Mth.floor(bb.minX);
                int mY = Mth.floor(bb.minY);
                int mZ = Mth.floor(bb.minZ);
                for (int y2 = mY; (double) y2 < bb.maxY; y2++) {
                    for (int x2 = mX; (double) x2 < bb.maxX; x2++) {
                        for (int z2 = mZ; (double) z2 < bb.maxZ; z2++) {
                            BlockPos tmp = new BlockPos(x2, y2, z2);
                            state = world.getBlockState(tmp);
                            if (state.isLadder(world, tmp, entity)) {
                                if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                                    return (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                                }
                                if (state.m_61138_(BlockStateProperties.UP) && (Boolean) state.m_61143_(BlockStateProperties.UP)) {
                                    return Direction.UP;
                                }
                                if (state.m_61138_(BlockStateProperties.NORTH) && (Boolean) state.m_61143_(BlockStateProperties.NORTH)) {
                                    return Direction.SOUTH;
                                }
                                if (state.m_61138_(BlockStateProperties.WEST) && (Boolean) state.m_61143_(BlockStateProperties.WEST)) {
                                    return Direction.EAST;
                                }
                                if (state.m_61138_(BlockStateProperties.SOUTH) && (Boolean) state.m_61143_(BlockStateProperties.SOUTH)) {
                                    return Direction.NORTH;
                                }
                                if (state.m_61138_(BlockStateProperties.EAST) && (Boolean) state.m_61143_(BlockStateProperties.EAST)) {
                                    return Direction.WEST;
                                }
                            }
                        }
                    }
                }
            }
            return Direction.UP;
        } else {
            return Direction.UP;
        }
    }
}