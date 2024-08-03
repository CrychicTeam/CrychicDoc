package snownee.loquat.mixin;

import java.util.function.BooleanSupplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.loquat.core.AreaManager;
import snownee.loquat.duck.AreaManagerContainer;

@Mixin({ ServerLevel.class })
public class ServerLevelMixin implements AreaManagerContainer {

    @Unique
    private AreaManager loquat$areaManager;

    @Override
    public AreaManager loquat$getAreaManager() {
        return this.loquat$areaManager;
    }

    @Override
    public void loquat$setAreaManager(AreaManager areaManager) {
        this.loquat$areaManager = areaManager;
    }

    @Inject(at = { @At("HEAD") }, method = { "close" })
    private void loquat$onClose(CallbackInfo ci) {
        this.loquat$areaManager = null;
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raids;tick()V") }, method = { "tick" })
    private void loquat$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (this.loquat$areaManager != null) {
            this.loquat$areaManager.tick();
        }
    }

    @Inject(method = { "addPlayer" }, at = { @At("RETURN") })
    private void loquat$addPlayer(ServerPlayer player, CallbackInfo ci) {
        if (this.loquat$areaManager != null) {
            this.loquat$areaManager.onPlayerAdded(player);
        }
    }
}