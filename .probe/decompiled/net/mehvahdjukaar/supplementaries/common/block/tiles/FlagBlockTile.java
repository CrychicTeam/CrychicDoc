package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.block.IColored;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlagBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class FlagBlockTile extends BlockEntity implements Nameable, IColored {

    public final float offset;

    @Nullable
    private Component name;

    private final DyeColor baseColor;

    @Nullable
    private ListTag itemPatterns;

    @Nullable
    private List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

    public FlagBlockTile(BlockPos pos, BlockState state) {
        this(pos, state, ((IColored) state.m_60734_()).getColor());
    }

    public FlagBlockTile(BlockPos pos, BlockState state, DyeColor color) {
        super((BlockEntityType<?>) ModRegistry.FLAG_TILE.get(), pos, state);
        this.baseColor = color;
        this.offset = 3.0F * (Mth.sin((float) this.f_58858_.m_123341_()) + Mth.sin((float) this.f_58858_.m_123343_()));
    }

    public void setCustomName(Component component) {
        this.name = component;
    }

    public List<Pair<Holder<BannerPattern>, DyeColor>> getPatterns() {
        if (this.patterns == null) {
            this.patterns = BannerBlockEntity.createPatterns(this.baseColor, this.itemPatterns);
        }
        return this.patterns;
    }

    public ItemStack getItem(BlockState state) {
        ItemStack itemstack = new ItemStack((ItemLike) ((Supplier) ModRegistry.FLAGS.get(this.baseColor)).get());
        if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            itemstack.getOrCreateTagElement("BlockEntityTag").put("Patterns", this.itemPatterns.copy());
        }
        if (this.name != null) {
            itemstack.setHoverName(this.name);
        }
        return itemstack;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.itemPatterns != null) {
            compound.put("Patterns", this.itemPatterns);
        }
        if (this.name != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        super.load(compoundNBT);
        if (compoundNBT.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundNBT.getString("CustomName"));
        }
        this.itemPatterns = compoundNBT.getList("Patterns", 10);
        this.patterns = null;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public AABB getRenderBoundingBox() {
        Direction dir = this.getDirection();
        return new AABB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75).expandTowards((double) ((float) dir.getStepX() * 1.35F), 0.0, (double) ((float) dir.getStepZ() * 1.35F)).move(this.f_58858_);
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(FlagBlock.FACING);
    }

    @Override
    public Component getName() {
        return (Component) (this.name != null ? this.name : Component.translatable("block.supplementaries.flag_" + this.baseColor.getName()));
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return this.baseColor;
    }
}