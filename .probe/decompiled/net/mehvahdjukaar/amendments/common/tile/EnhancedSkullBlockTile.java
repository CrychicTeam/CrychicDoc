package net.mehvahdjukaar.amendments.common.tile;

import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnhancedSkullBlockTile extends BlockEntity {

    @Nullable
    protected SkullBlockEntity innerTile = null;

    public EnhancedSkullBlockTile(BlockEntityType type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.saveInnerTile("Skull", this.innerTile, tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.innerTile = this.loadInnerTile("Skull", this.innerTile, tag);
    }

    protected void saveInnerTile(String tagName, @Nullable SkullBlockEntity tile, CompoundTag tag) {
        if (tile != null) {
            tag.put(tagName + "State", NbtUtils.writeBlockState(tile.m_58900_()));
            tag.put(tagName, tile.m_187480_());
        }
    }

    @Nullable
    protected SkullBlockEntity loadInnerTile(String tagName, @Nullable SkullBlockEntity tile, CompoundTag tag) {
        if (tag.contains(tagName)) {
            BlockState state = Utils.readBlockState(tag.getCompound(tagName + "State"), this.f_58857_);
            CompoundTag tileTag = tag.getCompound(tagName);
            if (tile != null) {
                tile.load(tileTag);
                return tile;
            }
            BlockEntity newTile = BlockEntity.loadStatic(this.m_58899_(), state, tileTag);
            if (newTile instanceof SkullBlockEntity) {
                return (SkullBlockEntity) newTile;
            }
        }
        return null;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public ItemStack getSkullItem() {
        return this.innerTile != null ? new ItemStack(this.innerTile.m_58900_().m_60734_()) : ItemStack.EMPTY;
    }

    public void initialize(SkullBlockEntity oldTile, ItemStack stack, Player player, InteractionHand hand) {
        this.innerTile = (SkullBlockEntity) oldTile.m_58903_().create(this.m_58899_(), oldTile.m_58900_());
        if (this.innerTile != null) {
            this.innerTile.load(oldTile.m_187482_());
        }
    }

    @Nullable
    public BlockState getSkull() {
        return this.innerTile != null ? this.innerTile.m_58900_() : null;
    }

    @Nullable
    public BlockEntity getSkullTile() {
        return this.innerTile;
    }

    protected void tick(Level level, BlockPos pos, BlockState state) {
        if (this.innerTile != null) {
            BlockState b = this.innerTile.m_58900_();
            if (b instanceof EntityBlock eb) {
                eb.getTicker(level, b, this.innerTile.m_58903_());
            }
        }
    }
}