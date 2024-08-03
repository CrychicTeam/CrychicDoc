package harmonised.pmmo.features.veinmining;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VeinShapeData {

    private Level level;

    private BlockPos center;

    private int maxBlocks;

    private final Map<BlockPos, VeinShapeData.Node> map = new HashMap();

    private final VeinShapeData.ShapeType mode;

    private final Direction face;

    public VeinShapeData(Level level, BlockPos center, int maxBlocks, VeinShapeData.ShapeType mode, Direction playerFacing) {
        this.level = level;
        this.center = center;
        this.maxBlocks = maxBlocks;
        this.mode = mode;
        this.face = playerFacing;
        this.map.put(center, new VeinShapeData.Node(0, false, false));
    }

    public Set<BlockPos> getVein() {
        Block block = this.level.getBlockState(this.center).m_60734_();
        return switch(this.mode) {
            case AOE ->
                {
                }
            case TUNNEL ->
                {
                }
            case BIG_TUNNEL ->
                {
                }
            default ->
                this.map.keySet();
        };
    }

    private void addNodesForRing(int ring, Block block) {
        Map<BlockPos, VeinShapeData.Node> ringMap = new HashMap();
        this.map.forEach((pos, node) -> {
            if (node.ring() == ring && !node.scanned() && !node.isTerminal()) {
                ringMap.put(pos, node);
            }
        });
        if (ringMap.isEmpty()) {
            this.maxBlocks = 0;
        } else {
            ringMap.forEach((pos, node) -> {
                this.map.put(pos, node.setScanned());
                Map<BlockPos, VeinShapeData.Node> newRingMap = new HashMap();
                label43: for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (this.maxBlocks <= 0) {
                                break label43;
                            }
                            BlockPos currentPos = pos.offset(x, y, z);
                            if (!this.map.containsKey(currentPos) && this.level.getBlockState(currentPos).m_60734_().equals(block)) {
                                newRingMap.put(currentPos, new VeinShapeData.Node(ring + 1, false, false));
                                this.maxBlocks--;
                            }
                        }
                    }
                }
                if (newRingMap.isEmpty()) {
                    this.map.put(pos, node.setScanned().setTerminal());
                } else {
                    this.map.putAll(newRingMap);
                }
            });
        }
    }

    private BlockPos stepShape(BlockPos from, BlockState centerState, boolean isBig) {
        boolean allFalse = true;
        if (this.level.getBlockState(from).m_60734_().equals(centerState.m_60734_())) {
            this.map.put(from, VeinShapeData.Node.NONE);
            this.maxBlocks--;
            allFalse = false;
        }
        if (this.maxBlocks > 0 && this.level.getBlockState(from.below()).m_60734_().equals(centerState.m_60734_())) {
            this.map.put(from.below(), VeinShapeData.Node.NONE);
            this.maxBlocks--;
            allFalse = false;
        }
        if (isBig) {
            if (this.maxBlocks > 0 && this.level.getBlockState(from.above()).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(from.above(), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, true)).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, true), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, true).above()).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, true).above(), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, true).below()).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, true).below(), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, false)).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, false), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, false).above()).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, false).above(), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
            if (this.maxBlocks > 0 && this.level.getBlockState(this.getAdjacent(from, false).below()).m_60734_().equals(centerState.m_60734_())) {
                this.map.put(this.getAdjacent(from, false).below(), VeinShapeData.Node.NONE);
                this.maxBlocks--;
                allFalse = false;
            }
        }
        return allFalse ? BlockPos.ZERO : from.relative(this.face);
    }

    private BlockPos getAdjacent(BlockPos pos, boolean left) {
        return switch(this.face) {
            case NORTH ->
                left ? pos.west() : pos.east();
            case SOUTH ->
                left ? pos.east() : pos.west();
            case WEST ->
                left ? pos.south() : pos.north();
            case EAST ->
                left ? pos.north() : pos.south();
            default ->
                pos;
        };
    }

    private static record Node(int ring, boolean scanned, boolean isTerminal) {

        public static VeinShapeData.Node NONE = new VeinShapeData.Node(0, false, false);

        public VeinShapeData.Node setScanned() {
            return new VeinShapeData.Node(this.ring(), true, this.isTerminal());
        }

        public VeinShapeData.Node setTerminal() {
            return new VeinShapeData.Node(this.ring(), this.scanned(), true);
        }
    }

    public static enum ShapeType {

        AOE, TUNNEL, BIG_TUNNEL
    }
}