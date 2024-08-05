package yesman.epicfight.world.capabilities.item;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class ShieldCapability extends CapabilityItem {

    protected ShieldCapability(CapabilityItem.Builder builder) {
        super(builder);
    }

    @Override
    public UseAnim getUseAnimation(LivingEntityPatch<?> entitypatch) {
        return UseAnim.NONE;
    }

    @Override
    public Map<LivingMotion, StaticAnimation> getLivingMotionModifier(LivingEntityPatch<?> playerdata, InteractionHand hand) {
        return ImmutableMap.of(LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);
    }
}