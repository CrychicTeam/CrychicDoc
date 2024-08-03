package snownee.kiwi.mixin.customization.sit;

import java.util.Objects;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.customization.block.behavior.SitManager;

@Mixin({ Display.class })
public abstract class DisplayMixin extends Entity {

    public DisplayMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void kiwi$tick(CallbackInfo ci) {
        if (!this.m_9236_().isClientSide && SitManager.isSeatEntity(this)) {
            SitManager.tick((Display.BlockDisplay) Objects.requireNonNull(EntityType.BLOCK_DISPLAY.tryCast((Entity) this)));
        }
    }

    @Inject(method = { "renderState" }, at = { @At("HEAD") }, cancellable = true)
    private void kiwi$renderState(CallbackInfoReturnable<Display.RenderState> cir) {
        if (this.m_9236_().isClientSide && SitManager.isSeatEntity(this)) {
            cir.setReturnValue(null);
        }
    }
}