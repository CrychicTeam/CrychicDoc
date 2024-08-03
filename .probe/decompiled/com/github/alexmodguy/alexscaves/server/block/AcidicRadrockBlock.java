package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class AcidicRadrockBlock extends Block {

    public AcidicRadrockBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F).sound(ACSoundTypes.RADROCK));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState state, @Nullable BlockEntity entity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, state, entity, itemStack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0 && level.random.nextInt(3) == 0) {
            level.setBlockAndUpdate(blockPos, ACBlockRegistry.ACID.get().m_49966_());
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(1) == 0) {
            Direction direction = Direction.getRandom(randomSource);
            if (direction != Direction.UP) {
                BlockPos blockpos = blockPos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos);
                if (!blockState.m_60815_() || !blockstate.m_60783_(level, blockpos, direction.getOpposite())) {
                    double d0 = direction.getStepX() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepX() * 0.6;
                    double d1 = direction.getStepY() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepY() * 0.6;
                    double d2 = direction.getStepZ() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.6;
                    level.addParticle(ACParticleRegistry.ACID_DROP.get(), (double) blockPos.m_123341_() + d0, (double) blockPos.m_123342_() + d1, (double) blockPos.m_123343_() + d2, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}