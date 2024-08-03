package software.bernie.example.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DynamicExampleEntity extends PathfinderMob implements GeoEntity {

    private static final RawAnimation BLOCK_LEFT = RawAnimation.begin().thenPlay("attack.block.left");

    private static final RawAnimation BLOCK_RIGHT = RawAnimation.begin().thenPlay("attack.block.right");

    private static final RawAnimation AIM_LEFT_HAND = RawAnimation.begin().thenPlay("pose.aim.left");

    private static final RawAnimation AIM_RIGHT_HAND = RawAnimation.begin().thenPlay("pose.aim.right");

    private static final RawAnimation SPEAR_LEFT_HAND = RawAnimation.begin().thenPlay("pose.spear.left");

    private static final RawAnimation SPEAR_RIGHT_HAND = RawAnimation.begin().thenPlay("pose.spear.right");

    private static final RawAnimation INTERACT_LEFT = RawAnimation.begin().thenPlay("misc.interact.right");

    private static final RawAnimation INTERACT_RIGHT = RawAnimation.begin().thenPlay("misc.interact.right");

    private static final RawAnimation SPEAR_SWING = RawAnimation.begin().thenPlay("attack.spear");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DynamicExampleEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController(this), new AnimationController<>(this, "Body", 20, this::poseBody), new AnimationController<>(this, "Left Hand", 10, state -> this.predicateHandPose(this.getLeftHand(), state)).triggerableAnim("interact", INTERACT_LEFT), new AnimationController<>(this, "Right Hand", 10, state -> this.predicateHandPose(this.getRightHand(), state)).triggerableAnim("interact", INTERACT_RIGHT), new AnimationController<>(this, "Dual Wield Pose", 10, this::poseDualWield), new AnimationController<>(this, "Dual Wield Attack", 10, this::attackDualWield));
    }

    protected PlayState poseBody(AnimationState<DynamicExampleEntity> state) {
        if (this.isWieldingTwoHandedWeapon()) {
            return PlayState.STOP;
        } else {
            if (this.m_20159_()) {
                state.setAnimation(DefaultAnimations.SIT);
            } else if (this.m_6047_()) {
                state.setAnimation(DefaultAnimations.SNEAK);
            }
            return PlayState.CONTINUE;
        }
    }

    protected PlayState predicateHandPose(InteractionHand hand, AnimationState<DynamicExampleEntity> state) {
        ItemStack heldStack = this.m_21120_(hand);
        if (!heldStack.isEmpty() && !this.isWieldingTwoHandedWeapon()) {
            Item handItem = heldStack.getItem();
            return this.m_21254_() && (handItem instanceof ShieldItem || handItem.getUseAnimation(heldStack) == UseAnim.BLOCK) ? state.setAndContinue(this.getLeftHand() == hand ? BLOCK_LEFT : BLOCK_RIGHT) : PlayState.STOP;
        } else {
            return PlayState.STOP;
        }
    }

    private PlayState poseDualWield(AnimationState<DynamicExampleEntity> state) {
        if (!this.isWieldingTwoHandedWeapon()) {
            return PlayState.STOP;
        } else {
            for (ItemStack heldStack : this.m_6167_()) {
                UseAnim useAnim = heldStack.getItem().getUseAnimation(heldStack);
                if (useAnim == UseAnim.BOW || useAnim == UseAnim.CROSSBOW) {
                    return state.setAndContinue(this.m_21526_() ? AIM_LEFT_HAND : AIM_RIGHT_HAND);
                }
                if (useAnim == UseAnim.SPEAR) {
                    return state.setAndContinue(this.m_21526_() ? SPEAR_LEFT_HAND : SPEAR_RIGHT_HAND);
                }
            }
            return PlayState.STOP;
        }
    }

    private <E extends GeoAnimatable> PlayState attackDualWield(AnimationState<E> state) {
        if (this.f_20911_ && this.isWieldingTwoHandedWeapon()) {
            for (ItemStack heldStack : this.m_6167_()) {
                if (heldStack.getItem().getUseAnimation(heldStack) == UseAnim.SPEAR) {
                    return state.setAndContinue(SPEAR_SWING);
                }
            }
            return PlayState.STOP;
        } else {
            return PlayState.STOP;
        }
    }

    public boolean isWieldingTwoHandedWeapon() {
        ItemStack mainHandStack = this.m_21205_();
        ItemStack offhandStack = this.m_21206_();
        if (!(mainHandStack.getItem() instanceof ProjectileWeaponItem) && !(offhandStack.getItem() instanceof ProjectileWeaponItem)) {
            UseAnim anim = mainHandStack.getUseAnimation();
            if (anim != UseAnim.BOW && anim != UseAnim.CROSSBOW && anim != UseAnim.SPEAR) {
                anim = offhandStack.getUseAnimation();
                return anim == UseAnim.BOW || anim == UseAnim.CROSSBOW || anim == UseAnim.SPEAR;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    protected InteractionHand getLeftHand() {
        return this.m_21526_() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    protected InteractionHand getRightHand() {
        return !this.m_21526_() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!this.m_9236_().isClientSide() && !stack.isEmpty()) {
            EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
            this.m_8061_(slot, stack.copy());
            player.m_213846_(Component.translatable("entity.geckolib.mutant_zombie.equip", stack.getDisplayName()));
            if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                this.triggerAnim(this.getLeftHand() == hand ? "Left Hand" : "Right Hand", "interact");
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6071_(player, hand);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}