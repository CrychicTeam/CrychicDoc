package com.simibubi.create.content.kinetics.waterwheel;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class LargeWaterWheelBlockItem extends BlockItem {

    public LargeWaterWheelBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL) {
            return result;
        } else {
            Direction clickedFace = ctx.m_43719_();
            if (clickedFace.getAxis() != ((LargeWaterWheelBlock) this.m_40614_()).getAxisForPlacement(ctx)) {
                result = super.place(BlockPlaceContext.at(ctx, ctx.getClickedPos().relative(clickedFace), clickedFace));
            }
            if (result == InteractionResult.FAIL && ctx.m_43725_().isClientSide()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.showBounds(ctx));
            }
            return result;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void showBounds(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Direction.Axis axis = ((LargeWaterWheelBlock) this.m_40614_()).getAxisForPlacement(context);
        Vec3 contract = Vec3.atLowerCornerOf(Direction.get(Direction.AxisDirection.POSITIVE, axis).getNormal());
        if (context.m_43723_() instanceof LocalPlayer localPlayer) {
            CreateClient.OUTLINER.showAABB(Pair.of("waterwheel", pos), new AABB(pos).inflate(1.0).deflate(contract.x, contract.y, contract.z)).colored(-41620);
            Lang.translate("large_water_wheel.not_enough_space").color(-41620).sendStatus(localPlayer);
        }
    }
}