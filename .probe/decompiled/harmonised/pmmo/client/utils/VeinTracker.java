package harmonised.pmmo.client.utils;

import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.veinmining.VeinShapeData;
import java.util.Collections;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.LogicalSide;

public class VeinTracker {

    private static Set<BlockPos> vein;

    public static BlockPos currentTarget;

    public static double currentCharge;

    public static VeinShapeData.ShapeType mode = VeinShapeData.ShapeType.AOE;

    public static void setTarget(BlockPos pos) {
        currentTarget = currentTarget == null ? pos : (currentTarget.equals(pos) ? BlockPos.ZERO : pos);
    }

    public static void nextMode() {
        mode = mode.ordinal() == VeinShapeData.ShapeType.values().length - 1 ? VeinShapeData.ShapeType.values()[0] : VeinShapeData.ShapeType.values()[mode.ordinal() + 1];
    }

    public static boolean isLookingAtVeinTarget(HitResult hitResult) {
        if (hitResult instanceof BlockHitResult && currentTarget != null) {
            BlockHitResult bhr = (BlockHitResult) hitResult;
            return currentTarget.equals(bhr.getBlockPos());
        } else {
            return false;
        }
    }

    public static Set<BlockPos> getVein() {
        return vein == null ? Collections.emptySet() : vein;
    }

    public static int getCurrentCharge() {
        return (int) currentCharge;
    }

    public static void updateVein(Player player) {
        Block block = player.m_9236_().getBlockState(currentTarget).m_60734_();
        int perBlock = Core.get(LogicalSide.CLIENT).getBlockConsume(block);
        int maxBlocks = perBlock <= 0 ? 0 : getCurrentCharge() / perBlock;
        vein = new VeinShapeData(player.m_9236_(), currentTarget, maxBlocks, mode, player.m_6350_()).getVein();
    }
}