package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import java.util.Locale;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StatueBlock;
import net.mehvahdjukaar.supplementaries.common.utils.Credits;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StatueBlockTile extends ItemDisplayTile {

    @Nullable
    private GameProfile playerSkin = null;

    private StatueBlockTile.StatuePose pose = StatueBlockTile.StatuePose.STANDING;

    private boolean isWaving = false;

    private BlockState candle = null;

    public StatueBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.STATUE_TILE.get(), pos, state);
    }

    @Override
    public void setCustomName(Component name) {
        super.m_58638_(name);
        this.updateName();
    }

    public StatueBlockTile.StatuePose getPose() {
        return this.pose;
    }

    public boolean isWaving() {
        return this.isWaving;
    }

    public BlockState hasCandle() {
        return this.candle;
    }

    @Nullable
    public GameProfile getPlayerSkin() {
        return this.playerSkin;
    }

    protected void setPlayerSkin(@Nullable GameProfile input) {
        if (this.playerSkin == null) {
            synchronized (this) {
                this.playerSkin = input;
            }
            SkullBlockEntity.updateGameprofile(this.playerSkin, gameProfile -> {
                this.playerSkin = gameProfile;
                this.m_6596_();
            });
        }
    }

    private void updateName() {
        if (this.m_8077_()) {
            String name = this.m_7770_().getString().toLowerCase(Locale.ROOT);
            Pair<UUID, String> profile = (Pair<UUID, String>) Credits.INSTANCE.statues().get(name);
            if (profile != null) {
                this.setPlayerSkin(new GameProfile((UUID) profile.getFirst(), (String) profile.getSecond()));
            }
        } else {
            this.playerSkin = null;
        }
    }

    @Override
    public void updateClientVisualsOnLoad() {
        this.updateName();
        ItemStack stack = this.getDisplayedItem();
        this.pose = StatueBlockTile.StatuePose.getPose(stack);
        this.isWaving = (Boolean) this.m_58900_().m_61143_(StatueBlock.POWERED);
        if (this.pose == StatueBlockTile.StatuePose.CANDLE) {
            Block b = ((BlockItem) stack.getItem()).getBlock();
            if (!(b instanceof CandleBlock)) {
                b = Blocks.CANDLE;
            }
            this.candle = (BlockState) b.defaultBlockState().m_61124_(CandleBlock.LIT, true);
        }
    }

    @Override
    public void updateTileOnInventoryChanged() {
        boolean flag = StatueBlockTile.StatuePose.getPose(this.getDisplayedItem()) == StatueBlockTile.StatuePose.CANDLE;
        if (flag != (Boolean) this.m_58900_().m_61143_(StatueBlock.LIT)) {
            this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(StatueBlock.LIT, flag));
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.m_7013_(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.supplementaries.statue");
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(NoticeBoardBlock.FACING);
    }

    public static enum StatuePose {

        STANDING,
        HOLDING,
        CANDLE,
        SWORD,
        TOOL,
        GLOBE,
        SEPIA_GLOBE;

        public static StatueBlockTile.StatuePose getPose(ItemStack stack) {
            if (stack.isEmpty()) {
                return STANDING;
            } else {
                Item i = stack.getItem();
                if (MiscUtils.isSword(i)) {
                    return SWORD;
                } else if (MiscUtils.isTool(i)) {
                    return TOOL;
                } else if (i == ModRegistry.GLOBE_ITEM.get()) {
                    return GLOBE;
                } else if (i == ModRegistry.GLOBE_SEPIA_ITEM.get()) {
                    return SEPIA_GLOBE;
                } else {
                    return stack.is(ItemTags.CANDLES) ? CANDLE : HOLDING;
                }
            }
        }

        public boolean isGlobe() {
            return this == GLOBE || this == SEPIA_GLOBE;
        }
    }
}