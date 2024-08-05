package net.mehvahdjukaar.amendments.integration;

import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.permission.BuiltinPermission;
import io.github.flemmli97.flan.claim.ClaimStorage;
import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class FlanCompat {

    public static boolean canBreak(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.BREAK, pos, true);
            } catch (Exception var4) {
                Amendments.LOGGER.error("Failed call break block event: [Player: {}, Pos: {}]", player, pos, var4);
                return true;
            }
        }
    }

    public static boolean canPlace(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.PLACE, pos);
            } catch (Exception var4) {
                Amendments.LOGGER.error("Failed call place block event: [Player: {}, Pos: {}]", player, pos, var4);
                return true;
            }
        }
    }

    public static boolean canReplace(@NotNull Player player, @NotNull BlockPos pos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(pos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.PLACE, pos);
            } catch (Exception var4) {
                Amendments.LOGGER.error("Failed call replace block event: [Player: {}, Pos: {}]", player, pos, var4);
                return true;
            }
        }
    }

    public static boolean canAttack(@NotNull Player player, @NotNull Entity victim) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(victim.blockPosition());
                return victim instanceof ServerPlayer ? claim.canInteract((ServerPlayer) player, BuiltinPermission.HURTPLAYER, victim.blockPosition()) : claim.canInteract((ServerPlayer) player, BuiltinPermission.HURTANIMAL, victim.blockPosition());
            } catch (Exception var4) {
                Supplementaries.LOGGER.error("Failed call attack entity event: [Player: {}, Victim: {}]", player, victim, var4);
                return true;
            }
        }
    }

    public static boolean canInteract(@NotNull Player player, @NotNull BlockPos targetPos) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            try {
                ClaimStorage storage = ClaimStorage.get((ServerLevel) player.m_9236_());
                IPermissionContainer claim = storage.getForPermissionCheck(targetPos);
                return claim.canInteract((ServerPlayer) player, BuiltinPermission.INTERACTBLOCK, targetPos);
            } catch (Exception var4) {
                Amendments.LOGGER.error("Failed call interact event: [Player: {}, Pos: {}]", player, targetPos, var4);
                return true;
            }
        }
    }
}