package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class TeleportUtil {

    public static boolean teleport(Player player, LivingEntity target, boolean back) {
        if (player.m_9236_().isClientSide) {
            return true;
        } else {
            Vec3 tPos = target.m_20182_();
            double reach = player.m_21133_(ForgeMod.ENTITY_REACH.get()) - 0.5;
            Vec3 end;
            if (back) {
                end = RayTraceUtil.getRayTerm(tPos, target.m_146909_(), target.m_146908_(), -reach);
            } else {
                end = RayTraceUtil.getRayTerm(tPos, player.m_146909_(), player.m_146908_(), -reach);
            }
            BlockPos pos = BlockPos.containing(end);
            AABB aabb = player.m_20191_();
            for (int i = 0; i < 5; i++) {
                BlockPos iPos = pos.above(i);
                Vec3 cen = new Vec3((double) iPos.m_123341_() + 0.5, (double) (iPos.m_123342_() + 1), (double) iPos.m_123343_() + 0.5);
                AABB iab = aabb.move(cen.subtract(aabb.getCenter()));
                if (player.m_9236_().m_45756_(player, iab)) {
                    player.m_6021_((double) iPos.m_123341_() + 0.5, (double) iPos.m_123342_(), (double) iPos.m_123343_() + 0.5);
                    player.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                    player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    player.m_9236_().m_220407_(GameEvent.TELEPORT, iPos, GameEvent.Context.of(player));
                    return true;
                }
            }
            return false;
        }
    }
}