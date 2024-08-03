package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.common.misc.mob_container.IMobContainerProvider;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelRenderer.class })
public abstract class ParrotMixin {

    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(method = { "notifyNearbyEntities" }, at = { @At("HEAD") })
    private void setPartying(Level worldIn, BlockPos pos, boolean isPartying, CallbackInfo info) {
        for (Player player : this.level.m_45976_(Player.class, new AABB(pos).inflate(3.0))) {
            CompoundTag l = player.getShoulderEntityLeft();
            if (l != null) {
                l.putBoolean("record_playing", isPartying);
            }
            CompoundTag r = player.getShoulderEntityRight();
            if (r != null) {
                r.putBoolean("record_playing", isPartying);
            }
        }
        int r = 3;
        BlockPos.MutableBlockPos mut = pos.mutable();
        for (int x = pos.m_123341_() - r; x < pos.m_123341_() + r; x++) {
            for (int y = pos.m_123342_() - r; y < pos.m_123342_() + r; y++) {
                for (int z = pos.m_123343_() - r; z < pos.m_123343_() + r; z++) {
                    BlockEntity container = worldIn.getBlockEntity(mut.set(x, y, z));
                    if (container instanceof IMobContainerProvider) {
                        IMobContainerProvider te = (IMobContainerProvider) container;
                        MobContainer containerx = te.getMobContainer();
                        if (containerx.getDisplayedMob() instanceof LivingEntity le) {
                            le.setRecordPlayingNearby(pos, isPartying);
                        }
                    }
                }
            }
        }
    }
}