package yesman.epicfight.api.animation.types;

import java.util.function.Function;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class SelectiveAnimation extends StaticAnimation {

    public static final TypeFlexibleHashMap.TypeKey<Integer> PREVIOUS_STATE = new TypeFlexibleHashMap.TypeKey<Integer>() {

        public Integer defaultValue() {
            return -1;
        }
    };

    private final Function<LivingEntityPatch<?>, Integer> selector;

    private final StaticAnimation[] animations;

    public SelectiveAnimation(Function<LivingEntityPatch<?>, Integer> selector, StaticAnimation... animations) {
        super(0.15F, false, "", null);
        this.selector = selector;
        this.animations = animations;
    }

    @Override
    public void begin(LivingEntityPatch<?> entitypatch) {
        super.begin(entitypatch);
        int result = (Integer) this.selector.apply(entitypatch);
        entitypatch.<Animator>getAnimator().playAnimation(this.animations[result], 0.0F);
        entitypatch.<Animator>getAnimator().putAnimationVariables(PREVIOUS_STATE, result);
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        super.tick(entitypatch);
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        entitypatch.<Animator>getAnimator().removeAnimationVariables(PREVIOUS_STATE);
    }

    @Override
    public boolean isMetaAnimation() {
        return true;
    }

    @Override
    public void loadAnimation(ResourceManager resourceManager) {
        for (StaticAnimation anim : this.animations) {
            anim.addEvents(AnimationProperty.StaticAnimationProperty.EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                int result = (Integer) this.selector.apply(entitypatch);
                if (entitypatch.<Animator>getAnimator().getAnimationVariables(PREVIOUS_STATE) != result) {
                    entitypatch.<Animator>getAnimator().playAnimation(this.animations[result], 0.0F);
                    entitypatch.<Animator>getAnimator().putAnimationVariables(PREVIOUS_STATE, result);
                }
            }, AnimationEvent.Side.BOTH));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Layer.Priority getPriority() {
        return this.animations[0].getPriority();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Layer.LayerType getLayerType() {
        return this.animations[0].getLayerType();
    }
}