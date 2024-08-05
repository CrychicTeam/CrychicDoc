package snownee.loquat.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.util.CommonProxy;

@Mixin({ BlockItem.class })
public class BlockItemMixin {

    @Inject(method = { "place" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;") }, cancellable = true)
    private void loquat$place(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.m_43723_() != null) {
            if (RestrictInstance.of(context.m_43723_()).isRestricted(context.getClickedPos(), RestrictInstance.RestrictBehavior.PLACE)) {
                CommonProxy.notifyRestriction(context.m_43723_(), RestrictInstance.RestrictBehavior.PLACE);
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}