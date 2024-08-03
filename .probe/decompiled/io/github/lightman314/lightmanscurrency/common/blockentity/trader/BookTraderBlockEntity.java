package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IBookTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataBook;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BookTraderBlockEntity extends ItemTraderBlockEntity {

    public BookTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOOK_TRADER.get(), pos, state);
    }

    public BookTraderBlockEntity(BlockPos pos, BlockState state, int count) {
        super(ModBlockEntities.BOOK_TRADER.get(), pos, state, count);
    }

    @Nonnull
    @Override
    public ItemTraderData buildNewTrader() {
        return new ItemTraderDataBook(this.tradeCount, this.f_58857_, this.f_58858_);
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3f GetBookRenderPos(int tradeSlot) {
        return this.m_58900_().m_60734_() instanceof IBookTraderBlock block ? block.GetBookRenderPos(tradeSlot, this.m_58900_()) : new Vector3f(0.0F, 0.0F, 0.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public List<Quaternionf> GetBookRenderRot(int tradeSlot) {
        return (List<Quaternionf>) (this.m_58900_().m_60734_() instanceof IBookTraderBlock block ? block.GetBookRenderRot(tradeSlot, this.m_58900_()) : new ArrayList());
    }

    @OnlyIn(Dist.CLIENT)
    public float GetBookRenderScale(int tradeSlot) {
        return this.m_58900_().m_60734_() instanceof IBookTraderBlock block ? block.GetBookRenderScale(tradeSlot, this.m_58900_()) : 1.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public int maxRenderIndex() {
        return this.m_58900_().m_60734_() instanceof IBookTraderBlock block ? block.maxRenderIndex() : 0;
    }
}