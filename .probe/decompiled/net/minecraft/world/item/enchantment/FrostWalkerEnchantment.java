package net.minecraft.world.item.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class FrostWalkerEnchantment extends Enchantment {

    public FrostWalkerEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.ARMOR_FEET, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return int0 * 10;
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 15;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public static void onEntityMoved(LivingEntity livingEntity0, Level level1, BlockPos blockPos2, int int3) {
        if (livingEntity0.m_20096_()) {
            BlockState $$4 = Blocks.FROSTED_ICE.defaultBlockState();
            int $$5 = Math.min(16, 2 + int3);
            BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
            for (BlockPos $$7 : BlockPos.betweenClosed(blockPos2.offset(-$$5, -1, -$$5), blockPos2.offset($$5, -1, $$5))) {
                if ($$7.m_203195_(livingEntity0.m_20182_(), (double) $$5)) {
                    $$6.set($$7.m_123341_(), $$7.m_123342_() + 1, $$7.m_123343_());
                    BlockState $$8 = level1.getBlockState($$6);
                    if ($$8.m_60795_()) {
                        BlockState $$9 = level1.getBlockState($$7);
                        if ($$9 == FrostedIceBlock.m_278844_() && $$4.m_60710_(level1, $$7) && level1.m_45752_($$4, $$7, CollisionContext.empty())) {
                            level1.setBlockAndUpdate($$7, $$4);
                            level1.m_186460_($$7, Blocks.FROSTED_ICE, Mth.nextInt(livingEntity0.getRandom(), 60, 120));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return super.checkCompatibility(enchantment0) && enchantment0 != Enchantments.DEPTH_STRIDER;
    }
}