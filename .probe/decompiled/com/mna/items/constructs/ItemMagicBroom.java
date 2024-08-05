package com.mna.items.constructs;

import com.mna.api.items.TieredItem;
import com.mna.blocks.tileentities.ManaweavingAltarTile;
import com.mna.entities.constructs.MagicBroom;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class ItemMagicBroom extends TieredItem {

    private int broomType;

    public ItemMagicBroom(int broomType) {
        super(new Item.Properties().stacksTo(1).setNoRepair());
        this.broomType = broomType;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            BlockEntity te = world.getBlockEntity(context.getClickedPos());
            if (te instanceof Container && !(te instanceof ManaweavingAltarTile)) {
                MagicBroom broom = new MagicBroom(world);
                Vec3 tePos = new Vec3((double) (te.getBlockPos().m_123341_() + context.getClickedFace().getStepX()) + 0.5, (double) (te.getBlockPos().m_123342_() + context.getClickedFace().getStepY()), (double) (te.getBlockPos().m_123343_() + context.getClickedFace().getStepZ()) + 0.5);
                broom.m_6034_(tePos.x, tePos.y, tePos.z);
                broom.setContainerPosition(te.getBlockPos());
                broom.setBroomType(this.broomType);
                world.m_7967_(broom);
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
        }
        return super.onItemUseFirst(stack, context);
    }
}