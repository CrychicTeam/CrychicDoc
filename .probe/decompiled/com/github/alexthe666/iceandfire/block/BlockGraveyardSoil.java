package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class BlockGraveyardSoil extends Block {

    public BlockGraveyardSoil() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(5.0F, 1.0F).randomTicks());
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!worldIn.f_46443_) {
            if (!worldIn.isAreaLoaded(pos, 3)) {
                return;
            }
            if (!worldIn.m_46461_() && !worldIn.m_8055_(pos.above()).m_60815_() && rand.nextInt(9) == 0 && worldIn.m_46791_() != Difficulty.PEACEFUL) {
                int checkRange = 32;
                int k = worldIn.m_45976_(EntityGhost.class, new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + 1), (double) (pos.m_123343_() + 1)).inflate((double) checkRange)).size();
                if (k < 10) {
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(worldIn);
                    ghost.m_19890_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), ThreadLocalRandom.current().nextFloat() * 360.0F, 0.0F);
                    if (!worldIn.f_46443_) {
                        ghost.finalizeSpawn(worldIn, worldIn.m_6436_(pos), MobSpawnType.SPAWNER, null, null);
                        worldIn.addFreshEntity(ghost);
                    }
                    ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                    ghost.m_21446_(pos, 16);
                }
            }
        }
    }
}