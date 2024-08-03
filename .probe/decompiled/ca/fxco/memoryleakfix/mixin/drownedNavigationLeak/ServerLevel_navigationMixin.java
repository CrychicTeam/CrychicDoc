package ca.fxco.memoryleakfix.mixin.drownedNavigationLeak;

import ca.fxco.memoryleakfix.config.MinecraftRequirement;
import ca.fxco.memoryleakfix.config.VersionRange;
import ca.fxco.memoryleakfix.extensions.ExtendDrowned;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MinecraftRequirement({ @VersionRange(minVersion = "1.16.3", maxVersion = "1.16.5") })
@Mixin({ ServerLevel.class })
public abstract class ServerLevel_navigationMixin {

    @Shadow
    @Final
    private Set<PathNavigation> navigations;

    @Inject(method = { "onEntityRemoved" }, at = { @At("RETURN") })
    private void removeAllPossibleDrownedNavigations(Entity entity, CallbackInfo ci) {
        if (entity instanceof ExtendDrowned) {
            ((ExtendDrowned) entity).memoryLeakFix$onRemoveNavigation(this.navigations);
        }
    }
}