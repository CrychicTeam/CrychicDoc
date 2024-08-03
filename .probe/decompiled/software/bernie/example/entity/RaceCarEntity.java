package software.bernie.example.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RaceCarEntity extends Animal implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RaceCarEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.m_20160_()) {
            player.m_20329_(this);
            return super.mobInteract(player, hand);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
    }

    @Override
    public void travel(Vec3 pos) {
        if (this.m_6084_() && this.m_20160_()) {
            LivingEntity passenger = this.getControllingPassenger();
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
            this.m_146922_(passenger.m_146908_());
            this.m_146926_(passenger.m_146909_() * 0.5F);
            this.m_19915_(this.m_146908_(), this.m_146909_());
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.f_20883_;
            float x = passenger.xxa * 0.5F;
            float z = passenger.zza;
            if (z <= 0.0F) {
                z *= 0.25F;
            }
            this.m_7910_(0.3F);
            super.m_7023_(new Vec3((double) x, pos.y, (double) z));
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.m_146895_() instanceof LivingEntity entity ? entity : null;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return true;
    }

    @Override
    public void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
        if (entity instanceof LivingEntity passenger) {
            moveFunction.accept(entity, this.m_20185_(), this.m_20186_() - 0.1F, this.m_20189_());
            this.f_19860_ = passenger.f_19860_;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, state -> state.isMoving() && this.getControllingPassenger() != null ? state.setAndContinue(DefaultAnimations.DRIVE) : state.setAndContinue(DefaultAnimations.IDLE)).setSoundKeyframeHandler(event -> {
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5F;
    }
}