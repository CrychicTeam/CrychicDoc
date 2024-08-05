package com.mna.enchantments;

import com.mna.blocks.BlockInit;
import com.mna.blocks.sorcery.TransitoryTileBlock;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class TransitoryStep extends MAEnchantmentBase {

    private static final float BLOCK_MANA_COST = 10.0F;

    public TransitoryStep(Enchantment.Rarity rarityIn) {
        super(rarityIn, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return this.f_44672_.canEnchant(pStack.getItem());
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return true;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    public boolean apply(Player source, int enchantmentLevel) {
        MutableBoolean success = new MutableBoolean(false);
        BlockPos target = source.m_20183_().below().relative(source.m_6374_());
        if (source.m_9236_().m_46859_(target) && source.m_20096_() && !source.m_6144_()) {
            source.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (m.getCastingResource().hasEnoughAbsolute(source, 10.0F)) {
                    m.getCastingResource().consume(source, 10.0F);
                    source.m_9236_().setBlockAndUpdate(target, (BlockState) BlockInit.TRANSITORY_TILE.get().m_49966_().m_61124_(TransitoryTileBlock.DURATION, 1));
                    source.m_9236_().m_186460_(target, BlockInit.TRANSITORY_TILE.get(), 20);
                    success.setTrue();
                }
            });
        }
        return success.booleanValue();
    }
}