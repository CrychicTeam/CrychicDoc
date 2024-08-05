package net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers;

import net.mehvahdjukaar.moonlight.api.map.markers.SimpleMapBlockMarker;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.ModMapMarkers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.NetherPortalBlock;
import org.jetbrains.annotations.Nullable;

public class NetherPortalMarker extends SimpleMapBlockMarker {

    public NetherPortalMarker() {
        super(ModMapMarkers.NETHER_PORTAL_DECORATION_TYPE);
    }

    @Nullable
    public static NetherPortalMarker getFromWorld(BlockGetter world, BlockPos pos) {
        if (!(world.getBlockState(pos).m_60734_() instanceof NetherPortalBlock) && !Utils.getID(world.getFluidState(pos).getType()).toString().equals("betterportals:portal_fluid")) {
            return null;
        } else {
            NetherPortalMarker m = new NetherPortalMarker();
            m.setPos(pos);
            return m;
        }
    }
}