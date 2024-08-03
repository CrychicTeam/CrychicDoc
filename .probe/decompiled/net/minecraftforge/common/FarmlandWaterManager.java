package net.minecraftforge.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ticket.AABBTicket;
import net.minecraftforge.common.ticket.ChunkTicketManager;
import net.minecraftforge.common.ticket.ITicketManager;
import net.minecraftforge.common.ticket.SimpleTicket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarmlandWaterManager {

    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("forge.debugFarmlandWaterManager", "false"));

    private static final Map<LevelReader, Map<ChunkPos, ChunkTicketManager<Vec3>>> customWaterHandler = new WeakHashMap();

    private static final Logger LOGGER = LogManager.getLogger();

    public static <T extends SimpleTicket<Vec3>> T addCustomTicket(Level level, T ticket, ChunkPos masterChunk, ChunkPos... additionalChunks) {
        Preconditions.checkArgument(!level.isClientSide, "Water region is only determined server-side");
        Map<ChunkPos, ChunkTicketManager<Vec3>> ticketMap = (Map<ChunkPos, ChunkTicketManager<Vec3>>) customWaterHandler.computeIfAbsent(level, id -> new MapMaker().weakValues().makeMap());
        ChunkTicketManager<Vec3>[] additionalTickets = new ChunkTicketManager[additionalChunks.length];
        for (int i = 0; i < additionalChunks.length; i++) {
            additionalTickets[i] = (ChunkTicketManager<Vec3>) ticketMap.computeIfAbsent(additionalChunks[i], ChunkTicketManager::new);
        }
        ticket.setManager((ITicketManager<Vec3>) ticketMap.computeIfAbsent(masterChunk, ChunkTicketManager::new), additionalTickets);
        ticket.validate();
        return ticket;
    }

    public static AABBTicket addAABBTicket(Level level, AABB aabb) {
        if (DEBUG) {
            LOGGER.info("FarmlandWaterManager: New AABBTicket, aabb={}", aabb);
        }
        ChunkPos leftUp = new ChunkPos((int) aabb.minX >> 4, (int) aabb.minZ >> 4);
        ChunkPos rightDown = new ChunkPos((int) aabb.maxX >> 4, (int) aabb.maxZ >> 4);
        Set<ChunkPos> posSet = new HashSet();
        for (int x = leftUp.x; x <= rightDown.x; x++) {
            for (int z = leftUp.z; z <= rightDown.z; z++) {
                posSet.add(new ChunkPos(x, z));
            }
        }
        ChunkPos masterPos = null;
        double masterDistance = Double.MAX_VALUE;
        for (ChunkPos pos : posSet) {
            double distToCenter = getDistanceSq(pos, aabb.getCenter());
            if (distToCenter < masterDistance) {
                if (DEBUG) {
                    LOGGER.info("FarmlandWaterManager: New better pos then {}: {}, prev dist {}, new dist {}", masterPos, pos, masterDistance, distToCenter);
                }
                masterPos = pos;
                masterDistance = distToCenter;
            }
        }
        posSet.remove(masterPos);
        if (DEBUG) {
            LOGGER.info("FarmlandWaterManager: {} center pos, {} dummy posses. Dist to center {}", masterPos, posSet.toArray(new ChunkPos[0]), masterDistance);
        }
        return addCustomTicket(level, new AABBTicket(aabb), masterPos, (ChunkPos[]) posSet.toArray(new ChunkPos[0]));
    }

    private static double getDistanceSq(ChunkPos pos, Vec3 vec3d) {
        double d0 = (double) (pos.x * 16 + 8);
        double d1 = (double) (pos.z * 16 + 8);
        double d2 = d0 - vec3d.x;
        double d3 = d1 - vec3d.z;
        return d2 * d2 + d3 * d3;
    }

    public static boolean hasBlockWaterTicket(LevelReader level, BlockPos pos) {
        ChunkTicketManager<Vec3> ticketManager = getTicketManager(new ChunkPos(pos.m_123341_() >> 4, pos.m_123343_() >> 4), level);
        if (ticketManager != null) {
            Vec3 posAsVec3d = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
            for (SimpleTicket<Vec3> ticket : ticketManager.getTickets()) {
                if (ticket.matches(posAsVec3d)) {
                    return true;
                }
            }
        }
        return false;
    }

    static void removeTickets(ChunkAccess chunk) {
        ChunkTicketManager<Vec3> ticketManager = getTicketManager(chunk.getPos(), chunk.getWorldForge());
        if (ticketManager != null) {
            if (DEBUG) {
                LOGGER.info("FarmlandWaterManager: got tickets {} at {} before", ticketManager.getTickets().size(), ticketManager.pos);
            }
            ticketManager.getTickets().removeIf(next -> next.unload(ticketManager));
            if (DEBUG) {
                LOGGER.info("FarmlandWaterManager: got tickets {} at {} after", ticketManager.getTickets().size(), ticketManager.pos);
            }
        }
    }

    private static ChunkTicketManager<Vec3> getTicketManager(ChunkPos pos, LevelReader level) {
        Preconditions.checkArgument(!level.isClientSide(), "Water region is only determined server-side");
        Map<ChunkPos, ChunkTicketManager<Vec3>> ticketMap = (Map<ChunkPos, ChunkTicketManager<Vec3>>) customWaterHandler.get(level);
        return ticketMap == null ? null : (ChunkTicketManager) ticketMap.get(pos);
    }
}