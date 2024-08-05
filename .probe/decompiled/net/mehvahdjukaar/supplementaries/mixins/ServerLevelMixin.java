package net.mehvahdjukaar.supplementaries.mixins;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.common.entities.dispenser_minecart.ILevelEventRedirect;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ ServerLevel.class })
public abstract class ServerLevelMixin extends Level implements ILevelEventRedirect {

    @Unique
    private boolean supplementaries$redirectLevelEvents = false;

    @Unique
    private Vec3 supplementaries$redirectedEntityPos = Vec3.ZERO;

    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Shadow
    @Nullable
    @Override
    public abstract Entity getEntity(int var1);

    @Override
    public void setRedirected(boolean redirected, Vec3 id) {
        this.supplementaries$redirectLevelEvents = redirected;
        this.supplementaries$redirectedEntityPos = id;
    }

    @Inject(method = { "levelEvent" }, at = { @At("HEAD") }, cancellable = true)
    private void levelEvent(Player pPlayer, int pType, BlockPos pPos, int pData, CallbackInfo ci) {
        if (this.supplementaries$redirectLevelEvents && ILevelEventRedirect.tryRedirect(this, pPlayer, this.supplementaries$redirectedEntityPos, pType, pPos, pData)) {
            ci.cancel();
        }
    }

    @Inject(method = { "findLightningTargetAround" }, at = { @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void unluckyLightning(BlockPos pos, CallbackInfoReturnable<BlockPos> cir, BlockPos blockPos) {
        if ((double) this.f_46441_.nextFloat() < 0.5 && (Boolean) CommonConfigs.Tweaks.BAD_LUCK_LIGHTNING.get()) {
            AABB aabb = new AABB(blockPos, new BlockPos(blockPos.m_123341_(), this.m_151558_(), blockPos.m_123343_())).inflate(16.0);
            List<LivingEntity> l = this.m_6443_(LivingEntity.class, aabb, e -> e != null && e.isAlive() && this.m_45527_(e.m_20183_()) && e.hasEffect(MobEffects.UNLUCK));
            if (!l.isEmpty()) {
                Collections.shuffle(l);
                cir.setReturnValue(((LivingEntity) l.get(this.f_46441_.nextInt(l.size()))).m_20183_());
            }
        }
    }
}