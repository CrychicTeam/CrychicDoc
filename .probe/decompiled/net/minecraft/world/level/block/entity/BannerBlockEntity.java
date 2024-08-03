package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BannerBlockEntity extends BlockEntity implements Nameable {

    public static final int MAX_PATTERNS = 6;

    public static final String TAG_PATTERNS = "Patterns";

    public static final String TAG_PATTERN = "Pattern";

    public static final String TAG_COLOR = "Color";

    @Nullable
    private Component name;

    private DyeColor baseColor;

    @Nullable
    private ListTag itemPatterns;

    @Nullable
    private List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

    public BannerBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BANNER, blockPos0, blockState1);
        this.baseColor = ((AbstractBannerBlock) blockState1.m_60734_()).getColor();
    }

    public BannerBlockEntity(BlockPos blockPos0, BlockState blockState1, DyeColor dyeColor2) {
        this(blockPos0, blockState1);
        this.baseColor = dyeColor2;
    }

    @Nullable
    public static ListTag getItemPatterns(ItemStack itemStack0) {
        ListTag $$1 = null;
        CompoundTag $$2 = BlockItem.getBlockEntityData(itemStack0);
        if ($$2 != null && $$2.contains("Patterns", 9)) {
            $$1 = $$2.getList("Patterns", 10).copy();
        }
        return $$1;
    }

    public void fromItem(ItemStack itemStack0, DyeColor dyeColor1) {
        this.baseColor = dyeColor1;
        this.fromItem(itemStack0);
    }

    public void fromItem(ItemStack itemStack0) {
        this.itemPatterns = getItemPatterns(itemStack0);
        this.patterns = null;
        this.name = itemStack0.hasCustomHoverName() ? itemStack0.getHoverName() : null;
    }

    @Override
    public Component getName() {
        return (Component) (this.name != null ? this.name : Component.translatable("block.minecraft.banner"));
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    public void setCustomName(Component component0) {
        this.name = component0;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (this.itemPatterns != null) {
            compoundTag0.put("Patterns", this.itemPatterns);
        }
        if (this.name != null) {
            compoundTag0.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag0.getString("CustomName"));
        }
        this.itemPatterns = compoundTag0.getList("Patterns", 10);
        this.patterns = null;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public static int getPatternCount(ItemStack itemStack0) {
        CompoundTag $$1 = BlockItem.getBlockEntityData(itemStack0);
        return $$1 != null && $$1.contains("Patterns") ? $$1.getList("Patterns", 10).size() : 0;
    }

    public List<Pair<Holder<BannerPattern>, DyeColor>> getPatterns() {
        if (this.patterns == null) {
            this.patterns = createPatterns(this.baseColor, this.itemPatterns);
        }
        return this.patterns;
    }

    public static List<Pair<Holder<BannerPattern>, DyeColor>> createPatterns(DyeColor dyeColor0, @Nullable ListTag listTag1) {
        List<Pair<Holder<BannerPattern>, DyeColor>> $$2 = Lists.newArrayList();
        $$2.add(Pair.of(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(BannerPatterns.BASE), dyeColor0));
        if (listTag1 != null) {
            for (int $$3 = 0; $$3 < listTag1.size(); $$3++) {
                CompoundTag $$4 = listTag1.getCompound($$3);
                Holder<BannerPattern> $$5 = BannerPattern.byHash($$4.getString("Pattern"));
                if ($$5 != null) {
                    int $$6 = $$4.getInt("Color");
                    $$2.add(Pair.of($$5, DyeColor.byId($$6)));
                }
            }
        }
        return $$2;
    }

    public static void removeLastPattern(ItemStack itemStack0) {
        CompoundTag $$1 = BlockItem.getBlockEntityData(itemStack0);
        if ($$1 != null && $$1.contains("Patterns", 9)) {
            ListTag $$2 = $$1.getList("Patterns", 10);
            if (!$$2.isEmpty()) {
                $$2.remove($$2.size() - 1);
                if ($$2.isEmpty()) {
                    $$1.remove("Patterns");
                }
                BlockItem.setBlockEntityData(itemStack0, BlockEntityType.BANNER, $$1);
            }
        }
    }

    public ItemStack getItem() {
        ItemStack $$0 = new ItemStack(BannerBlock.byColor(this.baseColor));
        if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            CompoundTag $$1 = new CompoundTag();
            $$1.put("Patterns", this.itemPatterns.copy());
            BlockItem.setBlockEntityData($$0, this.m_58903_(), $$1);
        }
        if (this.name != null) {
            $$0.setHoverName(this.name);
        }
        return $$0;
    }

    public DyeColor getBaseColor() {
        return this.baseColor;
    }
}