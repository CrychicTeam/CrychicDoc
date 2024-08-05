package software.bernie.example.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.example.registry.BlockEntityRegistry;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FertilizerBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation FERTILIZER_ANIMS = RawAnimation.begin().thenPlay("fertilizer.deploy").thenLoop("fertilizer.idle");

    private static final RawAnimation BOTARIUM_ANIMS = RawAnimation.begin().thenPlay("botarium.deploy").thenLoop("botarium.idle");

    public FertilizerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FERTILIZER_BLOCK.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> state.getAnimatable().m_58904_().isRaining() ? state.setAndContinue(FERTILIZER_ANIMS) : state.setAndContinue(BOTARIUM_ANIMS)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}