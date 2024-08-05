package org.violetmoon.quark.integration.claim;

import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.permission.BuiltinPermission;
import io.github.flemmli97.flan.claim.ClaimStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;

public final class FlanIntegration implements IClaimIntegration {

    @Override
    public boolean canBreak(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.BREAK, pos, true);
            } catch (Exception var5) {
                Quark.LOG.error("Failed call break block event: [Player: {}, Pos: {}]", player, pos, var5);
                return true;
            }
        }
    }

    @Override
    public boolean canPlace(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.PLACE, pos);
            } catch (Exception var5) {
                Quark.LOG.error("Failed call place block event: [Player: {}, Pos: {}]", player, pos, var5);
                return true;
            }
        }
    }

    @Override
    public boolean canReplace(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.PLACE, pos);
            } catch (Exception var5) {
                Quark.LOG.error("Failed call replace block event: [Player: {}, Pos: {}]", player, pos, var5);
                return true;
            }
        }
    }

    @Override
    public boolean canAttack(@NotNull Player player, @NotNull Entity victim) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(victim.blockPosition());
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.HURTANIMAL, victim.blockPosition());
            } catch (Exception var5) {
                Quark.LOG.error("Failed call attack entity event: [Player: {}, Victim: {}]", player, victim, var5);
                return true;
            }
        }
    }

    @Override
    public boolean canInteract(@NotNull Player player, @NotNull BlockPos targetPos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(targetPos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.INTERACTBLOCK, targetPos);
            } catch (Exception var5) {
                Quark.LOG.error("Failed call interact event: [Player: {}, Pos: {}]", player, targetPos, var5);
                return true;
            }
        }
    }
}