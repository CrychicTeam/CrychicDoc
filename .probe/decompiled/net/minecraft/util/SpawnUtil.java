package net.minecraft.util;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SpawnUtil {

    public static <T extends Mob> Optional<T> trySpawnMob(EntityType<T> entityTypeT0, MobSpawnType mobSpawnType1, ServerLevel serverLevel2, BlockPos blockPos3, int int4, int int5, int int6, SpawnUtil.Strategy spawnUtilStrategy7) {
        BlockPos.MutableBlockPos $$8 = blockPos3.mutable();
        for (int $$9 = 0; $$9 < int4; $$9++) {
            int $$10 = Mth.randomBetweenInclusive(serverLevel2.f_46441_, -int5, int5);
            int $$11 = Mth.randomBetweenInclusive(serverLevel2.f_46441_, -int5, int5);
            $$8.setWithOffset(blockPos3, $$10, int6, $$11);
            if (serverLevel2.m_6857_().isWithinBounds($$8) && moveToPossibleSpawnPosition(serverLevel2, int6, $$8, spawnUtilStrategy7)) {
                T $$12 = (T) entityTypeT0.create(serverLevel2, null, null, $$8, mobSpawnType1, false, false);
                if ($$12 != null) {
                    if ($$12.checkSpawnRules(serverLevel2, mobSpawnType1) && $$12.checkSpawnObstruction(serverLevel2)) {
                        serverLevel2.m_47205_($$12);
                        return Optional.of($$12);
                    }
                    $$12.m_146870_();
                }
            }
        }
        return Optional.empty();
    }

    private static boolean moveToPossibleSpawnPosition(ServerLevel serverLevel0, int int1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, SpawnUtil.Strategy spawnUtilStrategy3) {
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos().set(blockPosMutableBlockPos2);
        BlockState $$5 = serverLevel0.m_8055_($$4);
        for (int $$6 = int1; $$6 >= -int1; $$6--) {
            blockPosMutableBlockPos2.move(Direction.DOWN);
            $$4.setWithOffset(blockPosMutableBlockPos2, Direction.UP);
            BlockState $$7 = serverLevel0.m_8055_(blockPosMutableBlockPos2);
            if (spawnUtilStrategy3.canSpawnOn(serverLevel0, blockPosMutableBlockPos2, $$7, $$4, $$5)) {
                blockPosMutableBlockPos2.move(Direction.UP);
                return true;
            }
            $$5 = $$7;
        }
        return false;
    }

    public interface Strategy {

        @Deprecated
        SpawnUtil.Strategy LEGACY_IRON_GOLEM = (p_289751_, p_289752_, p_289753_, p_289754_, p_289755_) -> !p_289753_.m_60713_(Blocks.COBWEB) && !p_289753_.m_60713_(Blocks.CACTUS) && !p_289753_.m_60713_(Blocks.GLASS_PANE) && !(p_289753_.m_60734_() instanceof StainedGlassPaneBlock) && !(p_289753_.m_60734_() instanceof StainedGlassBlock) && !(p_289753_.m_60734_() instanceof LeavesBlock) && !p_289753_.m_60713_(Blocks.CONDUIT) && !p_289753_.m_60713_(Blocks.ICE) && !p_289753_.m_60713_(Blocks.TNT) && !p_289753_.m_60713_(Blocks.GLOWSTONE) && !p_289753_.m_60713_(Blocks.BEACON) && !p_289753_.m_60713_(Blocks.SEA_LANTERN) && !p_289753_.m_60713_(Blocks.FROSTED_ICE) && !p_289753_.m_60713_(Blocks.TINTED_GLASS) && !p_289753_.m_60713_(Blocks.GLASS) ? (p_289755_.m_60795_() || p_289755_.m_278721_()) && (p_289753_.m_280296_() || p_289753_.m_60713_(Blocks.POWDER_SNOW)) : false;

        SpawnUtil.Strategy ON_TOP_OF_COLLIDER = (p_216416_, p_216417_, p_216418_, p_216419_, p_216420_) -> p_216420_.m_60812_(p_216416_, p_216419_).isEmpty() && Block.isFaceFull(p_216418_.m_60812_(p_216416_, p_216417_), Direction.UP);

        boolean canSpawnOn(ServerLevel var1, BlockPos var2, BlockState var3, BlockPos var4, BlockState var5);
    }
}