package se.mickelus.tetra.blocks.scroll;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.ArrayUtils;

@ParametersAreNonnullByDefault
public class ScrollTile extends BlockEntity {

    public static final String identifier = "scroll";

    @ObjectHolder(registryName = "block_entity_type", value = "tetra:scroll")
    public static BlockEntityType<ScrollTile> type;

    private ScrollData[] scrolls = new ScrollData[0];

    public ScrollTile(BlockPos blockPos0, BlockState blockState1) {
        super(type, blockPos0, blockState1);
    }

    public ScrollData[] getScrolls() {
        return this.scrolls;
    }

    public boolean addScroll(ItemStack itemStack) {
        if (this.scrolls.length < 6) {
            this.scrolls = (ScrollData[]) ArrayUtils.add(this.scrolls, ScrollData.read(itemStack));
            this.m_6596_();
            return true;
        } else {
            return false;
        }
    }

    public ResourceLocation[] getSchematics() {
        boolean isIntricate = this.isIntricate();
        return (ResourceLocation[]) Arrays.stream(this.scrolls).filter(data -> data.isIntricate == isIntricate).map(data -> data.schematics).flatMap(Collection::stream).toArray(ResourceLocation[]::new);
    }

    public ResourceLocation[] getCraftingEffects() {
        boolean isIntricate = this.isIntricate();
        return (ResourceLocation[]) Arrays.stream(this.scrolls).filter(data -> data.isIntricate == isIntricate).map(data -> data.craftingEffects).flatMap(Collection::stream).toArray(ResourceLocation[]::new);
    }

    public boolean isIntricate() {
        return !Arrays.stream(this.scrolls).anyMatch(data -> !data.isIntricate);
    }

    public CompoundTag[] getItemTags() {
        return (CompoundTag[]) Arrays.stream(this.scrolls).map(data -> new ScrollData[] { data }).map(data -> ScrollData.write(data, new CompoundTag())).toArray(CompoundTag[]::new);
    }

    public AABB getRenderBoundingBox() {
        return Shapes.block().bounds().move(this.f_58858_);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.scrolls = ScrollData.read(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ScrollData.write(this.scrolls, compound);
    }
}