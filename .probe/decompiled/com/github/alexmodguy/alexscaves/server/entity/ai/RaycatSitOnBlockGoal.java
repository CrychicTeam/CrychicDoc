package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class RaycatSitOnBlockGoal extends MoveToBlockGoal {

    private final RaycatEntity cat;

    public RaycatSitOnBlockGoal(RaycatEntity raycat, double speed) {
        super(raycat, speed, 8);
        this.cat = raycat;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.m_21837_(false);
        this.cat.setLayTime(0);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.cat.m_21837_(false);
        this.cat.setLayTime(0);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.m_21837_(this.m_25625_());
        this.cat.setLayTime(this.m_25625_() ? 10 : 0);
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockstate = level.m_8055_(pos);
            if (blockstate.m_60713_(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(level, pos) < 1;
            } else if (blockstate.m_204336_(ACTagRegistry.RAYCAT_SLEEPS_ON)) {
                return true;
            } else {
                return blockstate.m_60713_(Blocks.FURNACE) && blockstate.m_61143_(FurnaceBlock.f_48684_) ? true : blockstate.m_204338_(BlockTags.BEDS, stateBase -> (Boolean) stateBase.m_61145_(BedBlock.PART).map(bedPart -> bedPart != BedPart.HEAD).orElse(true));
            }
        }
    }
}