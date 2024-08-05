package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.Locale;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.ClockBlock;
import net.mehvahdjukaar.supplementaries.common.items.AbstractMobContainerItem;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.IMobContainerProvider;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class JarBlockTile extends ItemDisplayTile implements IMobContainerProvider, ISoftFluidTankProvider, IExtraModelDataProvider {

    public static final ModelDataKey<SoftFluid> FLUID = ModBlockProperties.FLUID;

    public static final ModelDataKey<Float> FILL_LEVEL = ModBlockProperties.FILL_LEVEL;

    public final MobContainer mobContainer;

    public final SoftFluidTank fluidHolder;

    public JarBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.JAR_TILE.get(), pos, state, 12);
        int capacity = (Integer) CommonConfigs.Functional.JAR_CAPACITY.get();
        this.fluidHolder = SoftFluidTank.create(capacity);
        AbstractMobContainerItem item = (AbstractMobContainerItem) ModRegistry.JAR_ITEM.get();
        this.mobContainer = new MobContainer(item.getMobContainerWidth(), item.getMobContainerHeight(), true);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(FLUID, this.fluidHolder.getFluidValue()).with(FILL_LEVEL, this.fluidHolder.getHeight(1.0F));
    }

    @Override
    public void updateTileOnInventoryChanged() {
        this.f_58857_.updateNeighborsAt(this.f_58858_, this.m_58900_().m_60734_());
        int light = this.fluidHolder.getFluidValue().getLuminosity();
        if (light != (Integer) this.m_58900_().m_61143_(ModBlockProperties.LIGHT_LEVEL_0_15)) {
            this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(ModBlockProperties.LIGHT_LEVEL_0_15, light), 2);
        }
    }

    @Override
    public void updateClientVisualsOnLoad() {
        super.updateClientVisualsOnLoad();
        this.requestModelReload();
    }

    public boolean handleInteraction(Player player, InteractionHand hand, Level level, BlockPos pos) {
        ItemStack handStack = player.m_21120_(hand);
        ItemStack displayedStack = this.getDisplayedItem();
        if (this.canInteractWithSoftFluidTank() && this.fluidHolder.interactWithPlayer(player, hand, level, pos)) {
            return true;
        } else if (this.tryAddingItem(handStack, player, hand)) {
            return true;
        } else if (this.m_7983_() && this.fluidHolder.isEmpty() && this.mobContainer.interactWithBucket(handStack, level, player.m_20183_(), player, hand)) {
            return true;
        } else {
            if (!player.m_6144_() && (Boolean) CommonConfigs.Functional.JAR_EAT.get()) {
                if (this.fluidHolder.tryDrinkUpFluid(player, level)) {
                    return true;
                }
                if (displayedStack.isEdible() && player.canEat(false) && !player.isCreative()) {
                    player.eat(level, displayedStack);
                    return true;
                }
            }
            return this.handleExtractItem(player, hand);
        }
    }

    public ItemStack extractItem() {
        for (int j = this.m_6643_() - 1; j >= 0; j--) {
            ItemStack s = this.m_8020_(j);
            if (!s.isEmpty()) {
                this.m_8016_(j);
                return s;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean handleExtractItem(Player player, InteractionHand hand) {
        if (!player.m_21120_(hand).isEmpty()) {
            return false;
        } else {
            ItemStack extracted = this.extractItem();
            if (!extracted.isEmpty()) {
                Utils.swapItem(player, hand, extracted);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean tryAddingItem(ItemStack stack, @Nullable Player player, InteractionHand handIn) {
        ItemStack handStack = stack.copy();
        handStack.setCount(1);
        if (this.tryAddingItem(handStack)) {
            if (player != null) {
                ItemStack returnStack = ItemStack.EMPTY;
                Level level = player.m_9236_();
                level.playSound(player, this.f_58858_, (SoundEvent) ModSounds.JAR_COOKIE.get(), SoundSource.BLOCKS, 1.0F, 0.9F + level.random.nextFloat() * 0.1F);
                player.awardStat(Stats.ITEM_USED.get(handStack.getItem()));
                if (!player.isCreative()) {
                    Utils.swapItem(player, handIn, returnStack);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean tryAddingItem(ItemStack itemstack) {
        for (int i = 0; i < this.m_7086_().size(); i++) {
            if (this.canPlaceItem(i, itemstack)) {
                this.m_6836_(i, itemstack);
                return true;
            }
        }
        return false;
    }

    public void resetHolders() {
        this.fluidHolder.clear();
        this.mobContainer.clear();
        this.m_6520_(NonNullList.withSize(this.m_6643_(), ItemStack.EMPTY));
    }

    public boolean isPonyJar() {
        if (!this.m_8077_()) {
            return false;
        } else {
            Component c = this.m_7770_();
            return c != null && c.getString().toLowerCase(Locale.ROOT).contains("cum");
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        try {
            this.fluidHolder.load(compound);
        } catch (Exception var3) {
            Supplementaries.LOGGER.warn("Failed to load fluid container at {}:", this.m_58899_(), var3);
        }
        this.mobContainer.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        try {
            this.fluidHolder.save(tag);
        } catch (Exception var3) {
            Supplementaries.LOGGER.warn("Failed to save fluid container at {}:", this.m_58899_(), var3);
        }
        this.mobContainer.save(tag);
    }

    public boolean hasContent() {
        return !this.m_7983_() || !this.mobContainer.isEmpty() || !this.fluidHolder.isEmpty();
    }

    public boolean isFull() {
        return this.m_7086_().stream().noneMatch(ItemStack::m_41619_);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.supplementaries.jar");
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.m_8020_(index).getCount() < this.m_6893_() && CommonConfigs.Functional.JAR_COOKIES.get() && this.fluidHolder.isEmpty() && this.mobContainer.isEmpty() ? stack.is(ModTags.COOKIES) : false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public MobContainer getMobContainer() {
        return this.mobContainer;
    }

    @Override
    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(ClockBlock.FACING);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, JarBlockTile tile) {
        tile.mobContainer.tick(pLevel, pPos);
    }

    @Override
    public SoftFluidTank getSoftFluidTank() {
        return this.fluidHolder;
    }

    @Override
    public boolean canInteractWithSoftFluidTank() {
        return (Boolean) CommonConfigs.Functional.JAR_LIQUIDS.get() && this.m_7983_() && (this.mobContainer.isEmpty() || this.isPonyJar());
    }
}