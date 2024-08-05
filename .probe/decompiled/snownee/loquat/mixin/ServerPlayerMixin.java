package snownee.loquat.mixin;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.loquat.Hooks;
import snownee.loquat.LoquatConfig;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.RestrictInstance;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.duck.LoquatServerPlayer;

@Mixin({ ServerPlayer.class })
public class ServerPlayerMixin implements LoquatServerPlayer {

    @Unique
    private final Set<Area> loquat$areasIn = Sets.newHashSet();

    @Unique
    private RestrictInstance loquat$restriction;

    @Unique
    private Vec3 loquat$lastGoodPos;

    @Inject(method = { "doTick" }, at = { @At("HEAD") })
    private void loquat$doTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) this;
        if (this.loquat$lastGoodPos != null && Hooks.checkServerPlayerRestriction(player, this)) {
            if (player.m_20159_()) {
                player.stopRiding();
            }
            player.teleportTo(this.loquat$lastGoodPos.x, this.loquat$lastGoodPos.y, this.loquat$lastGoodPos.z);
        } else {
            this.loquat$lastGoodPos = player.m_20182_();
        }
        if (player.f_19797_ % LoquatConfig.positionCheckInterval == 0) {
            Hooks.tickServerPlayer(player, this);
        }
    }

    @Inject(method = { "teleportTo(DDD)V" }, at = { @At("HEAD") }, cancellable = true)
    private void loquat$teleportTo(double x, double y, double z, CallbackInfo ci) {
        if (Hooks.teleportServerPlayer((ServerPlayer) this, this, x, y, z)) {
            ci.cancel();
        }
    }

    @Override
    public Set<Area> loquat$getAreasIn() {
        return this.loquat$areasIn;
    }

    @Override
    public RestrictInstance loquat$getRestrictionInstance() {
        if (this.loquat$restriction == null) {
            ServerPlayer player = (ServerPlayer) this;
            this.loquat$restriction = AreaManager.of(player.serverLevel()).getOrCreateRestrictInstance(player.m_6302_());
        }
        return this.loquat$restriction;
    }

    @Override
    public void loquat$setLastGoodPos(Vec3 pos) {
        this.loquat$lastGoodPos = pos;
    }

    @Nullable
    @Override
    public Vec3 loquat$getLastGoodPos() {
        return this.loquat$lastGoodPos;
    }

    @Override
    public void loquat$reset() {
        this.loquat$areasIn.clear();
        this.loquat$restriction = null;
        this.loquat$lastGoodPos = null;
        ServerPlayer player = (ServerPlayer) this;
        SelectionManager.of(player).reset(player);
    }
}