package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class DynamicBrush extends Brush {

    public static final int MAX_RADIUS = 10;

    private boolean surface;

    public DynamicBrush(boolean surface) {
        super(1);
        this.surface = surface;
    }

    @Override
    Component getParamLabel(int paramIndex) {
        return Lang.translateDirect("generic.range");
    }

    @Override
    public TerrainTools[] getSupportedTools() {
        return this.surface ? new TerrainTools[] { TerrainTools.Overlay, TerrainTools.Replace, TerrainTools.Clear } : new TerrainTools[] { TerrainTools.Replace, TerrainTools.Clear };
    }

    @Override
    public boolean hasPlacementOptions() {
        return false;
    }

    @Override
    public boolean hasConnectivityOptions() {
        return true;
    }

    @Override
    int getMax(int paramIndex) {
        return 10;
    }

    @Override
    int getMin(int paramIndex) {
        return 1;
    }

    @Override
    public TerrainTools redirectTool(TerrainTools tool) {
        return tool == TerrainTools.Overlay ? TerrainTools.Place : super.redirectTool(tool);
    }

    @Override
    public Collection<BlockPos> addToGlobalPositions(LevelAccessor world, BlockPos targetPos, Direction targetFace, Collection<BlockPos> affectedPositions, TerrainTools usedTool) {
        boolean searchDiagonals = this.param1 == 0;
        boolean fuzzy = this.param2 == 0;
        boolean replace = usedTool != TerrainTools.Overlay;
        int searchRange = this.param0;
        Set<BlockPos> visited = new HashSet();
        List<BlockPos> frontier = new LinkedList();
        BlockState state = world.m_8055_(targetPos);
        List<BlockPos> offsets = new LinkedList();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if ((Math.abs(x) + Math.abs(y) + Math.abs(z) < 2 || searchDiagonals) && (targetFace.getAxis().choose(x, y, z) == 0 || !this.surface)) {
                        offsets.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        BlockPos startPos = replace ? targetPos : targetPos.relative(targetFace);
        frontier.add(startPos);
        while (!frontier.isEmpty()) {
            BlockPos currentPos = (BlockPos) frontier.remove(0);
            if (!visited.contains(currentPos)) {
                visited.add(currentPos);
                if (currentPos.m_123314_(startPos, (double) searchRange)) {
                    if (replace) {
                        BlockState stateToReplace = world.m_8055_(currentPos);
                        BlockState stateAboveStateToReplace = world.m_8055_(currentPos.relative(targetFace));
                        if (stateToReplace.m_60800_(world, currentPos) != -1.0F && (stateToReplace.m_60734_() == state.m_60734_() || fuzzy) && !stateToReplace.m_247087_() && (!BlockHelper.hasBlockSolidSide(stateAboveStateToReplace, world, currentPos.relative(targetFace), targetFace.getOpposite()) || !this.surface)) {
                            affectedPositions.add(currentPos);
                            for (BlockPos offset : offsets) {
                                frontier.add(currentPos.offset(offset));
                            }
                        }
                    } else {
                        BlockState stateToPlaceAt = world.m_8055_(currentPos);
                        BlockState stateToPlaceOn = world.m_8055_(currentPos.relative(targetFace.getOpposite()));
                        if (!stateToPlaceOn.m_247087_() && (stateToPlaceOn.m_60734_() == state.m_60734_() || fuzzy) && stateToPlaceAt.m_247087_()) {
                            affectedPositions.add(currentPos);
                            for (BlockPos offset : offsets) {
                                frontier.add(currentPos.offset(offset));
                            }
                        }
                    }
                }
            }
        }
        return affectedPositions;
    }
}