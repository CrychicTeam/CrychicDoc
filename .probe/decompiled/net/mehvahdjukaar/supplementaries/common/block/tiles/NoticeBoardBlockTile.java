package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.IMapDisplay;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.common.inventories.NoticeBoardContainerMenu;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CCCompat;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NoticeBoardBlockTile extends ItemDisplayTile implements Nameable, IMapDisplay, ITextHolderProvider {

    private final TextHolder textHolder;

    private boolean isWaxed = false;

    private int pageNumber = 0;

    @Nullable
    private UUID playerWhoMayEdit;

    private String text = null;

    private float fontScale = 1.0F;

    private List<FormattedCharSequence> cachedPageLines = Collections.emptyList();

    private boolean needsVisualRefresh = true;

    private Material cachedPattern = null;

    private boolean isNormalItem = false;

    public NoticeBoardBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.NOTICE_BOARD_TILE.get(), pos, state);
        this.textHolder = new TextHolder(1, 90);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.notice_board");
    }

    @Override
    public void updateTileOnInventoryChanged() {
        boolean shouldHaveBook = !this.getDisplayedItem().isEmpty();
        BlockState state = this.m_58900_();
        if ((Boolean) state.m_61143_(BlockStateProperties.HAS_BOOK) != shouldHaveBook) {
            this.f_58857_.setBlock(this.f_58858_, (BlockState) state.m_61124_(BlockStateProperties.HAS_BOOK, shouldHaveBook), 2);
            if (shouldHaveBook) {
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.85F);
            } else {
                this.pageNumber = 0;
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.5F);
            }
        }
    }

    @Override
    public ItemStack getMapStack() {
        return this.getDisplayedItem();
    }

    @Override
    public void updateClientVisualsOnLoad() {
        ItemStack itemstack = this.getDisplayedItem();
        Item item = itemstack.getItem();
        this.cachedPattern = null;
        if (item instanceof BannerPatternItem bannerPatternItem) {
            this.cachedPattern = ModMaterials.getFlagMaterialForPatternItem(bannerPatternItem);
        }
        this.needsVisualRefresh = true;
        this.cachedPageLines = Collections.emptyList();
        this.text = null;
        this.updateText();
        this.isNormalItem = !isPageItem(itemstack.getItem());
    }

    public boolean isNormalItem() {
        return this.isNormalItem;
    }

    public void updateText() {
        ItemStack itemstack = this.getDisplayedItem();
        Item item = itemstack.getItem();
        CompoundTag com = itemstack.getTag();
        if ((!(item instanceof WrittenBookItem) || !WrittenBookItem.makeSureTagIsValid(com)) && (!(item instanceof WritableBookItem) || !WritableBookItem.makeSureTagIsValid(com))) {
            if (CompatHandler.COMPUTERCRAFT && CCCompat.isPrintedBook(item) && com != null) {
                int pages = CCCompat.getPages(itemstack);
                if (this.pageNumber >= pages) {
                    this.pageNumber %= pages;
                }
                String[] text = CCCompat.getText(itemstack);
                StringBuilder combined = new StringBuilder();
                for (int i = 0; i < 21; i++) {
                    int ind = this.pageNumber * 21 + i;
                    if (ind < text.length) {
                        combined.append(text[ind]);
                        combined.append(" ");
                    }
                }
                this.text = combined.toString();
            }
        } else {
            ListTag pages = com.getList("pages", 8).copy();
            if (!pages.isEmpty()) {
                if (this.pageNumber >= pages.size()) {
                    this.pageNumber = this.pageNumber % pages.size();
                }
                this.text = pages.getString(this.pageNumber);
            }
        }
    }

    @Override
    public void load(CompoundTag compound) {
        this.pageNumber = compound.getInt("PageNumber");
        this.textHolder.load(compound, this.f_58857_, this.f_58858_);
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("PageNumber", this.pageNumber);
        this.textHolder.save(tag);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new NoticeBoardContainerMenu(id, player, this);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.m_7983_() && ((Boolean) CommonConfigs.Building.NOTICE_BOARDS_UNRESTRICTED.get() || isPageItem(stack.getItem()));
    }

    public static boolean isPageItem(Item item) {
        return item.builtInRegistryHolder().is(ItemTags.LECTERN_BOOKS) || item instanceof MapItem || item instanceof BannerPatternItem || CompatHandler.COMPUTERCRAFT && CCCompat.isPrintedBook(item);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public boolean shouldSkipTileRenderer() {
        return (Boolean) this.m_58900_().m_61143_(NoticeBoardBlock.CULLED) || !(Boolean) this.m_58900_().m_61143_(NoticeBoardBlock.HAS_BOOK);
    }

    public Material getCachedPattern() {
        return this.cachedPattern;
    }

    public String getText() {
        return this.text;
    }

    public DyeColor getDyeColor() {
        return this.textHolder.getColor();
    }

    public boolean isGlowing() {
        return this.textHolder.hasGlowingText();
    }

    public boolean hasAntiqueInk() {
        return this.textHolder.hasAntiqueInk();
    }

    public float getFontScale() {
        return this.fontScale;
    }

    public void setFontScale(float s) {
        this.fontScale = s;
    }

    public void setCachedPageLines(List<FormattedCharSequence> l) {
        this.cachedPageLines = l;
    }

    public List<FormattedCharSequence> getCachedLines() {
        return this.cachedPageLines;
    }

    public boolean needsVisualUpdate() {
        if (this.needsVisualRefresh) {
            this.needsVisualRefresh = false;
            return true;
        } else {
            return false;
        }
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(NoticeBoardBlock.FACING);
    }

    public void turnPage() {
        this.pageNumber++;
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 1.45F);
        this.m_6596_();
    }

    public InteractionResult interact(Player player, InteractionHand handIn, BlockPos pos, BlockState state, BlockHitResult hit) {
        Level level = player.m_9236_();
        if (player.m_6144_() && !this.m_7983_() && player.m_21120_(handIn).isEmpty()) {
            ItemStack it = this.m_8016_(0);
            BlockPos newPos = pos.offset(((Direction) state.m_61143_(NoticeBoardBlock.FACING)).getNormal());
            ItemEntity drop = new ItemEntity(level, (double) newPos.m_123341_() + 0.5, (double) newPos.m_123342_() + 0.5, (double) newPos.m_123343_() + 0.5, it);
            drop.setDefaultPickUpDelay();
            level.m_7967_(drop);
            this.m_6596_();
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            if (hit.getDirection() == state.m_61143_(NoticeBoardBlock.FACING)) {
                InteractionResult res = super.interact(player, handIn);
                if (res.consumesAction()) {
                    return res;
                }
            }
            InteractionResult r = this.interactWithTextHolder(0, level, pos, state, player, handIn);
            if (r != InteractionResult.PASS) {
                return r;
            } else if (!(Boolean) CommonConfigs.Building.NOTICE_BOARD_GUI.get()) {
                return InteractionResult.PASS;
            } else {
                if (!level.isClientSide) {
                    this.tryOpeningEditGui((ServerPlayer) player, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    @Override
    public void setPlayerWhoMayEdit(@Nullable UUID uuid) {
        this.playerWhoMayEdit = uuid;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    @Override
    public TextHolder getTextHolder(int ind) {
        return this.textHolder;
    }

    @Override
    public void openScreen(Level level, BlockPos blockPos, Player player) {
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player, Direction direction) {
    }

    @Override
    public boolean shouldUseContainerMenu() {
        return true;
    }

    @Override
    public boolean isWaxed() {
        return this.isWaxed;
    }

    @Override
    public void setWaxed(boolean b) {
        this.isWaxed = b;
    }
}