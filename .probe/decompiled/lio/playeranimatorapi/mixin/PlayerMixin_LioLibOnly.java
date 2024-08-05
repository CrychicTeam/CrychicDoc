package lio.playeranimatorapi.mixin;

import lio.playeranimatorapi.liolib.ModGeckoLibUtilsClient;
import net.liopyu.liolib.animatable.GeoEntity;
import net.liopyu.liolib.core.animatable.instance.AnimatableInstanceCache;
import net.liopyu.liolib.core.animation.AnimationController;
import net.liopyu.liolib.core.animation.AnimatableManager.ControllerRegistrar;
import net.liopyu.liolib.core.object.PlayState;
import net.liopyu.liolib.util.GeckoLibUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ Player.class })
public abstract class PlayerMixin_LioLibOnly implements GeoEntity {

    @Unique
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController[] { new AnimationController(this, "liosplayeranimatorapi", state -> PlayState.STOP).setOverrideEasingTypeFunction(player -> ModGeckoLibUtilsClient.getEasingTypeForID((Player) this)) });
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}