package com.simibubi.create.content.trains.track;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.foundation.utility.AttachedRegistry;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.ForgeRegistries;

public class AllPortalTracks {

    private static final AttachedRegistry<Block, AllPortalTracks.PortalTrackProvider> PORTAL_BEHAVIOURS = new AttachedRegistry<>(ForgeRegistries.BLOCKS);

    public static void registerIntegration(ResourceLocation block, AllPortalTracks.PortalTrackProvider provider) {
        PORTAL_BEHAVIOURS.register(block, provider);
    }

    public static void registerIntegration(Block block, AllPortalTracks.PortalTrackProvider provider) {
        PORTAL_BEHAVIOURS.register(block, provider);
    }

    public static boolean isSupportedPortal(BlockState state) {
        return PORTAL_BEHAVIOURS.get(state.m_60734_()) != null;
    }

    public static Pair<ServerLevel, BlockFace> getOtherSide(ServerLevel level, BlockFace inboundTrack) {
        BlockPos portalPos = inboundTrack.getConnectedPos();
        BlockState portalState = level.m_8055_(portalPos);
        AllPortalTracks.PortalTrackProvider provider = PORTAL_BEHAVIOURS.get(portalState.m_60734_());
        return provider == null ? null : (Pair) provider.apply(Pair.of(level, inboundTrack));
    }

    public static void registerDefaults() {
        registerIntegration(Blocks.NETHER_PORTAL, AllPortalTracks::nether);
        if (Mods.AETHER.isLoaded()) {
            registerIntegration(new ResourceLocation("aether", "aether_portal"), AllPortalTracks::aether);
        }
    }

    private static Pair<ServerLevel, BlockFace> nether(Pair<ServerLevel, BlockFace> inbound) {
        return standardPortalProvider(inbound, Level.OVERWORLD, Level.NETHER, ServerLevel::m_8871_);
    }

    private static Pair<ServerLevel, BlockFace> aether(Pair<ServerLevel, BlockFace> inbound) {
        ResourceKey<Level> aetherLevelKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("aether", "the_aether"));
        return standardPortalProvider(inbound, Level.OVERWORLD, aetherLevelKey, level -> {
            try {
                return (ITeleporter) Class.forName("com.aetherteam.aether.block.portal.AetherPortalForcer").getDeclaredConstructor(ServerLevel.class, boolean.class).newInstance(level, true);
            } catch (Exception var2) {
                var2.printStackTrace();
                return level.getPortalForcer();
            }
        });
    }

    public static Pair<ServerLevel, BlockFace> standardPortalProvider(Pair<ServerLevel, BlockFace> inbound, ResourceKey<Level> firstDimension, ResourceKey<Level> secondDimension, Function<ServerLevel, ITeleporter> customPortalForcer) {
        ServerLevel level = inbound.getFirst();
        ResourceKey<Level> resourcekey = level.m_46472_() == secondDimension ? firstDimension : secondDimension;
        MinecraftServer minecraftserver = level.getServer();
        ServerLevel otherLevel = minecraftserver.getLevel(resourcekey);
        if (otherLevel != null && minecraftserver.isNetherEnabled()) {
            BlockFace inboundTrack = inbound.getSecond();
            BlockPos portalPos = inboundTrack.getConnectedPos();
            BlockState portalState = level.m_8055_(portalPos);
            ITeleporter teleporter = (ITeleporter) customPortalForcer.apply(otherLevel);
            SuperGlueEntity probe = new SuperGlueEntity(level, new AABB(portalPos));
            probe.m_146922_(inboundTrack.getFace().toYRot());
            probe.setPortalEntrancePos();
            PortalInfo portalinfo = teleporter.getPortalInfo(probe, otherLevel, probe::m_7937_);
            if (portalinfo == null) {
                return null;
            } else {
                BlockPos otherPortalPos = BlockPos.containing(portalinfo.pos);
                BlockState otherPortalState = otherLevel.m_8055_(otherPortalPos);
                if (otherPortalState.m_60734_() != portalState.m_60734_()) {
                    return null;
                } else {
                    Direction targetDirection = inboundTrack.getFace();
                    if (targetDirection.getAxis() == otherPortalState.m_61143_(BlockStateProperties.HORIZONTAL_AXIS)) {
                        targetDirection = targetDirection.getClockWise();
                    }
                    BlockPos otherPos = otherPortalPos.relative(targetDirection);
                    return Pair.of(otherLevel, new BlockFace(otherPos, targetDirection.getOpposite()));
                }
            }
        } else {
            return null;
        }
    }

    @FunctionalInterface
    public interface PortalTrackProvider extends UnaryOperator<Pair<ServerLevel, BlockFace>> {
    }
}