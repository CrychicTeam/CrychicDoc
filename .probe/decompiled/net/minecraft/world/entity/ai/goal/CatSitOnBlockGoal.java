package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class CatSitOnBlockGoal extends MoveToBlockGoal {

    private final Cat cat;

    public CatSitOnBlockGoal(Cat cat0, double double1) {
        super(cat0, double1, 8);
        this.cat = cat0;
    }

    @Override
    public boolean canUse() {
        return this.cat.m_21824_() && !this.cat.m_21827_() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.m_21837_(false);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.cat.m_21837_(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.m_21837_(this.m_25625_());
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
        if (!levelReader0.isEmptyBlock(blockPos1.above())) {
            return false;
        } else {
            BlockState $$2 = levelReader0.m_8055_(blockPos1);
            if ($$2.m_60713_(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(levelReader0, blockPos1) < 1;
            } else {
                return $$2.m_60713_(Blocks.FURNACE) && $$2.m_61143_(FurnaceBlock.f_48684_) ? true : $$2.m_204338_(BlockTags.BEDS, p_25156_ -> (Boolean) p_25156_.m_61145_(BedBlock.PART).map(p_148084_ -> p_148084_ != BedPart.HEAD).orElse(true));
            }
        }
    }
}