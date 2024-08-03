package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.BookTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IBookTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BookTraderBlock extends TraderBlockRotatable implements IBookTraderBlock {

    public static final int BOOK_COUNT = 10;

    public BookTraderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new BookTraderBlockEntity(pos, state, 10);
    }

    @Override
    protected BlockEntityType<?> traderType() {
        return ModBlockEntities.BOOK_TRADER.get();
    }

    @Override
    public Vector3f GetBookRenderPos(int tradeSlot, BlockState state) {
        Direction facing = this.getFacing(state);
        Vector3f right = IRotatableBlock.getRightVect(facing);
        Vector3f up = MathUtil.getYP();
        Vector3f forward = IRotatableBlock.getForwardVect(facing);
        Vector3f offset = IRotatableBlock.getOffsetVect(facing);
        float xPos = (float) (tradeSlot % 5) * 3.0F / 16.0F - 0.3125F;
        float yPos = tradeSlot < 5 ? 1.0625F : 0.5625F;
        return MathUtil.VectorAdd(offset, MathUtil.VectorMult(right, xPos), MathUtil.VectorMult(up, yPos), MathUtil.VectorMult(forward, 0.5F));
    }

    @Override
    public List<Quaternionf> GetBookRenderRot(int tradeSlot, BlockState state) {
        List<Quaternionf> rotation = new ArrayList();
        int facing = this.getFacing(state).get2DDataValue();
        rotation.add(MathUtil.fromAxisAngleDegree(MathUtil.getYP(), (float) facing * -90.0F));
        return rotation;
    }

    @Override
    public int maxRenderIndex() {
        return 10;
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER_BOOK.asTooltip(10);
    }
}