package net.mehvahdjukaar.supplementaries.common.block.blocks;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlazeRodBlock extends StickBlock {

    public BlazeRodBlock(BlockBehaviour.Properties properties) {
        super(properties, 0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, Boolean.FALSE)).m_61124_(AXIS_Y, true)).m_61124_(AXIS_X, false)).m_61124_(AXIS_Z, false));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity le && !EnchantmentHelper.hasFrostWalker(le) && (!(entity instanceof Player p) || !p.isCreative())) {
            entity.setSecondsOnFire(2);
        }
        super.m_141947_(world, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!((double) random.nextFloat() > 0.3)) {
            IntList list = new IntArrayList();
            if ((Boolean) state.m_61143_(AXIS_Y)) {
                list.add(0);
            }
            if ((Boolean) state.m_61143_(AXIS_X)) {
                list.add(1);
            }
            if ((Boolean) state.m_61143_(AXIS_Z)) {
                list.add(2);
            }
            int s = list.size();
            if (s > 0) {
                ParticleOptions particle = state.m_61143_(WATERLOGGED) ? ParticleTypes.BUBBLE : ParticleTypes.SMOKE;
                int c = list.getInt(random.nextInt(s));
                double x;
                double y;
                double z;
                switch(c) {
                    case 1:
                        y = (double) pos.m_123342_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                        x = (double) ((float) pos.m_123341_() + random.nextFloat());
                        z = (double) pos.m_123343_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                        break;
                    case 2:
                        y = (double) pos.m_123342_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                        z = (double) ((float) pos.m_123343_() + random.nextFloat());
                        x = (double) pos.m_123341_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                        break;
                    default:
                        x = (double) pos.m_123341_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                        y = (double) ((float) pos.m_123342_() + random.nextFloat());
                        z = (double) pos.m_123343_() + 0.5 - 0.125 + (double) random.nextFloat() * 0.25;
                }
                world.addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }
}