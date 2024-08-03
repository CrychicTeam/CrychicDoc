package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemMobSkull extends Item {

    private final EnumSkullType skull;

    public ItemMobSkull(EnumSkullType skull) {
        super(new Item.Properties().stacksTo(1));
        this.skull = skull;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        EntityMobSkull skull = new EntityMobSkull(IafEntityRegistry.MOB_SKULL.get(), context.getLevel());
        ItemStack stack = player.m_21120_(context.getHand());
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
        skull.m_7678_((double) offset.m_123341_() + 0.5, (double) offset.m_123342_(), (double) offset.m_123343_() + 0.5, 0.0F, 0.0F);
        float yaw = player.m_146908_();
        if (context.getClickedFace() != Direction.UP) {
            yaw = player.m_6350_().toYRot();
        }
        skull.setYaw(yaw);
        skull.setSkullType(this.skull);
        if (!context.getLevel().isClientSide) {
            context.getLevel().m_7967_(skull);
        }
        if (stack.hasCustomHoverName()) {
            skull.m_6593_(stack.getHoverName());
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}