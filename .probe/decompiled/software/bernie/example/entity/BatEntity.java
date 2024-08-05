package software.bernie.example.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BatEntity extends PathfinderMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean isFlying = false;

    public BatEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        super.m_8099_();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            this.isFlying = !this.isFlying;
        }
        return super.m_7111_(player, hitPos, hand);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 10, state -> state.setAndContinue(this.isFlying ? DefaultAnimations.FLY : DefaultAnimations.IDLE)).setCustomInstructionKeyframeHandler(state -> {
            Player player = ClientUtils.getClientPlayer();
            if (player != null) {
                player.displayClientMessage(Component.literal("KeyFraming"), true);
            }
        }), DefaultAnimations.genericLivingController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}