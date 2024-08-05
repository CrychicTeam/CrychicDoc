package net.mehvahdjukaar.amendments.common.tile;

import java.util.Map;
import net.mehvahdjukaar.amendments.AmendmentsClient;
import net.mehvahdjukaar.amendments.integration.CaveEnhancementsCompat;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.CompatObjects;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CandleSkullBlockTile extends EnhancedSkullBlockTile {

    private BlockState candle = Blocks.AIR.defaultBlockState();

    private ResourceLocation waxTexture = null;

    public CandleSkullBlockTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType) ModRegistry.SKULL_CANDLE_TILE.get(), pWorldPosition, pBlockState);
    }

    public ResourceLocation getWaxTexture() {
        return this.waxTexture;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Candle", NbtUtils.writeBlockState(this.candle));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        BlockState c = null;
        if (tag.contains("Candle", 10)) {
            c = Utils.readBlockState(tag.getCompound("Candle"), this.f_58857_);
        }
        this.setCandle(c);
    }

    public BlockState getCandle() {
        return this.candle;
    }

    public void setCandle(BlockState candle) {
        this.candle = candle;
        if (PlatHelper.getPhysicalSide().isClient()) {
            this.waxTexture = null;
            if (this.candle != null) {
                this.waxTexture = (ResourceLocation) ((Map) AmendmentsClient.SKULL_CANDLES_TEXTURES.get()).get(this.candle.m_60734_());
            }
        }
    }

    public boolean tryAddingCandle(CandleBlock candle) {
        if (this.candle.m_60795_() || candle == this.candle.m_60734_() && (Integer) this.candle.m_61143_(CandleBlock.CANDLES) != 4) {
            if (this.candle.m_60795_()) {
                this.setCandle(candle.m_49966_());
            } else {
                this.candle.m_61122_(CandleBlock.CANDLES);
            }
            if (!this.f_58857_.isClientSide) {
                BlockState state = this.m_58900_();
                BlockState newState = Utils.replaceProperty(this.candle, state, CandleBlock.CANDLES);
                this.f_58857_.setBlockAndUpdate(this.f_58858_, newState);
                this.m_6596_();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void initialize(SkullBlockEntity oldTile, ItemStack stack, Player player, InteractionHand hand) {
        super.initialize(oldTile, stack, player, hand);
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CandleBlock candleBlock) {
            this.tryAddingCandle(candleBlock);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CandleSkullBlockTile e) {
        e.tick(level, pos, state);
        if (CompatHandler.CAVE_ENHANCEMENTS && e.candle.m_60713_((Block) CompatObjects.SPECTACLE_CANDLE.get())) {
            CaveEnhancementsCompat.tick(level, pos, state);
        }
    }
}