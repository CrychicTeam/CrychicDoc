package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

class XPDroppingInteraction implements FaucetTarget.BlState {

    private void dropXP(Level level, BlockPos pos, int bottles) {
        int i = Utils.getXPinaBottle(bottles, level.random);
        while (i > 0) {
            int xp = ExperienceOrb.getExperienceValue(i);
            i -= xp;
            ExperienceOrb orb = new ExperienceOrb(level, (double) pos.m_123341_() + 0.5, (double) ((float) pos.m_123342_() - 0.125F), (double) pos.m_123343_() + 0.5, xp);
            orb.m_20256_(new Vec3(0.0, 0.0, 0.0));
            level.m_7967_(orb);
        }
        float f = (level.random.nextFloat() - 0.5F) / 4.0F;
        level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.3F, 0.5F + f);
    }

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        if (state.m_60795_() && fluid.is(BuiltInSoftFluids.XP.get())) {
            this.dropXP(level, pos, minAmount);
            return minAmount;
        } else {
            return null;
        }
    }
}