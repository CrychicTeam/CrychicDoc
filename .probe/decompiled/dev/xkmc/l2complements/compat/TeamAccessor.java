package dev.xkmc.l2complements.compat;

import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

@ParametersAreNullableByDefault
public class TeamAccessor {

    public static final boolean PLAYER_HURT = false;

    public static boolean arePlayersInSameTeam(ServerPlayer a, ServerPlayer b) {
        if (a == null || b == null) {
            return false;
        } else {
            return !b.m_7307_(a) && !a.m_7307_(b) ? playerSameTeam(a.m_20148_(), b.m_20148_()) : true;
        }
    }

    public static boolean arePlayerAndEntityInSameTeam(ServerPlayer a, LivingEntity b) {
        if (a == null || b == null) {
            return false;
        } else if (!b.m_7307_(a) && !a.m_7307_(b)) {
            UUID bid = getPotentialOwner(b);
            return bid != null && playerSameTeam(a.m_20148_(), bid);
        } else {
            return true;
        }
    }

    public static boolean areEntitiesInSameTeam(LivingEntity a, LivingEntity b) {
        if (a == null || b == null) {
            return false;
        } else if (!b.m_7307_(a) && !a.m_7307_(b)) {
            UUID aid = getPotentialOwner(a);
            UUID bid = getPotentialOwner(b);
            return aid != null && bid != null ? playerSameTeam(aid, bid) : false;
        } else {
            return true;
        }
    }

    @Nullable
    public static UUID getPotentialOwner(Entity e) {
        if (e instanceof ServerPlayer) {
            return e.getUUID();
        } else {
            return e instanceof TamableAnimal a ? a.getOwnerUUID() : null;
        }
    }

    private static boolean playerSameTeam(UUID a, UUID b) {
        return true;
    }

    private static boolean ftbSameTeamUnsafe(UUID a, UUID b) {
        return false;
    }
}