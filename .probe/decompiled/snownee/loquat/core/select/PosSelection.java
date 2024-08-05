package snownee.loquat.core.select;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PosSelection {

    public BlockPos pos1;

    public BlockPos pos2;

    public PosSelection(BlockPos pos) {
        this(pos, pos);
    }

    public PosSelection(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public AABB toAABB() {
        return new AABB(Vec3.atCenterOf(this.pos1), Vec3.atCenterOf(this.pos2)).inflate(0.5);
    }
}