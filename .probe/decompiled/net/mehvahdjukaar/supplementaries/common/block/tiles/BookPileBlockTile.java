package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.SpriteCoordinateUnExpander;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BookPileBlock;
import net.mehvahdjukaar.supplementaries.common.block.placeable_book.BookType;
import net.mehvahdjukaar.supplementaries.common.block.placeable_book.PlaceableBookManager;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.integration.EnchantRedesignCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BookPileBlockTile extends ItemDisplayTile implements IExtraModelDataProvider {

    public final boolean horizontal;

    private float enchantPower = 0.0F;

    public final BookPileBlockTile.BooksList booksVisuals = new BookPileBlockTile.BooksList();

    public static final ModelDataKey<BookPileBlockTile.BooksList> BOOKS_KEY = ModBlockProperties.BOOKS_KEY;

    private static final RandomSource rand = RandomSource.create();

    public static final List<String> DEFAULT_COLORS = List.of("brown", "orange", "yellow", "red", "green", "lime", "cyan", "blue", "purple");

    public BookPileBlockTile(BlockPos pos, BlockState state) {
        this(pos, state, false);
    }

    public BookPileBlockTile(BlockPos pos, BlockState state, boolean horizontal) {
        super((BlockEntityType) ModRegistry.BOOK_PILE_TILE.get(), pos, state, 4);
        this.horizontal = horizontal;
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(BOOKS_KEY, this.booksVisuals);
    }

    private void displayRandomColoredBooks(int i) {
        for (int j = 0; j < i; j++) {
            int r = rand.nextInt(10);
            Item it;
            if (r < 2) {
                it = Items.ENCHANTED_BOOK;
            } else if (r < 3) {
                it = Items.WRITABLE_BOOK;
            } else {
                it = Items.BOOK;
            }
            ArrayList<BookType> col = PlaceableBookManager.getByItem(it.getDefaultInstance());
            this.booksVisuals.add(new BookPileBlockTile.VisualBook(it.getDefaultInstance(), this.f_58858_, j, col, null));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putFloat("EnchantPower", this.enchantPower);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.enchantPower = compound.getFloat("EnchantPower");
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.requestModelReload();
        }
    }

    @Override
    public void updateTileOnInventoryChanged() {
        int b = (int) this.m_7086_().stream().filter(ix -> !ix.isEmpty()).count();
        if (b != (Integer) this.m_58900_().m_61143_(BookPileBlock.BOOKS)) {
            if (b == 0) {
                if (this.f_59605_ != null) {
                    return;
                }
                this.f_58857_.removeBlock(this.f_58858_, false);
            } else {
                this.consolidateBookPile();
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(BookPileBlock.BOOKS, b), 2);
            }
        }
        this.enchantPower = 0.0F;
        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = this.m_8020_(i);
            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                if (CompatHandler.QUARK && CompatObjects.TOME.get() == item) {
                    this.enchantPower = (float) ((double) this.enchantPower + (Double) CommonConfigs.Tweaks.BOOK_POWER.get() / 4.0 * 2.0);
                } else if (item == Items.ENCHANTED_BOOK) {
                    this.enchantPower = (float) ((double) this.enchantPower + (Double) CommonConfigs.Tweaks.ENCHANTED_BOOK_POWER.get() / 4.0);
                } else {
                    this.enchantPower = (float) ((double) this.enchantPower + (Double) CommonConfigs.Tweaks.BOOK_POWER.get() / 4.0);
                }
            }
        }
    }

    private void consolidateBookPile() {
        boolean prevEmpty = false;
        for (int i = 0; i < 4; i++) {
            ItemStack it = this.m_8020_(i);
            if (it.isEmpty()) {
                prevEmpty = true;
            } else if (prevEmpty) {
                this.m_7086_().set(i - 1, it);
                this.m_7086_().set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void updateClientVisualsOnLoad() {
        this.booksVisuals.clear();
        List<BookType> colors = new ArrayList();
        for (String v : (List) ClientConfigs.Tweaks.BOOK_COLORS.get()) {
            BookType byName = PlaceableBookManager.getByName(v);
            if (!colors.contains(byName)) {
                colors.add(byName);
            }
        }
        for (int index = 0; index < 4; index++) {
            ItemStack stack = this.m_8020_(index);
            if (stack.isEmpty()) {
                break;
            }
            BookType last = index == 0 ? null : this.booksVisuals.get(index - 1).type;
            this.booksVisuals.add(index, new BookPileBlockTile.VisualBook(stack, this.f_58858_, index, colors, last));
        }
        if (this.booksVisuals.isEmpty()) {
            this.displayRandomColoredBooks((Integer) this.m_58900_().m_61143_(BookPileBlock.BOOKS));
        }
    }

    public float getEnchantPower() {
        return this.enchantPower;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.supplementaries.book_pile");
    }

    public static record BooksList(List<BookPileBlockTile.VisualBook> books) {

        public BooksList() {
            this(new ArrayList());
        }

        public void add(BookPileBlockTile.VisualBook visualBook) {
            this.books.add(visualBook);
        }

        public void add(int i, BookPileBlockTile.VisualBook visualBook) {
            this.books.add(i, visualBook);
        }

        public void clear() {
            this.books.clear();
        }

        public boolean isEmpty() {
            return this.books.isEmpty();
        }

        public BookPileBlockTile.VisualBook get(int i) {
            return (BookPileBlockTile.VisualBook) this.books.get(i);
        }

        public int size() {
            return this.books.size();
        }
    }

    public static class VisualBook {

        private final float yAngle;

        private final BookType type;

        private final ItemStack stack;

        public VisualBook(ItemStack bookStack, BlockPos pos, int index, List<BookType> colors, @Nullable BookType lastColor) {
            this.stack = bookStack;
            Random rand = new Random(pos.below(2).asLong());
            for (int j = 0; j < index; j++) {
                rand.nextInt();
            }
            Item item = bookStack.getItem();
            this.yAngle = (float) ((double) rand.nextInt(32) * Math.PI / 16.0);
            if (item instanceof BookItem) {
                if (lastColor == null) {
                    if (colors.isEmpty()) {
                        Supplementaries.error();
                        this.type = PlaceableBookManager.getByName("brown");
                        return;
                    }
                    this.type = (BookType) colors.get(rand.nextInt(colors.size()));
                } else {
                    List<BookType> c = colors.stream().filter(b -> b.looksGoodNextTo(lastColor)).toList();
                    if (c.isEmpty()) {
                        Supplementaries.error();
                        this.type = lastColor;
                    } else {
                        this.type = (BookType) c.get(rand.nextInt(c.size()));
                    }
                }
                colors.remove(this.type);
            } else {
                ArrayList<BookType> possibleTypes = PlaceableBookManager.getByItem(bookStack);
                if (possibleTypes.isEmpty()) {
                    Supplementaries.error();
                    this.type = PlaceableBookManager.getByName("brown");
                    return;
                }
                this.type = (BookType) possibleTypes.get(rand.nextInt(possibleTypes.size()));
            }
        }

        public VertexConsumer getBuilder(MultiBufferSource buffer) {
            if (this.type.hasGlint() && (Boolean) ClientConfigs.Tweaks.BOOK_GLINT.get()) {
                VertexConsumer foilBuilder = null;
                if (CompatHandler.ENCHANTEDBOOKREDESIGN) {
                    foilBuilder = EnchantRedesignCompat.getBookColoredFoil(this.stack, buffer);
                }
                if (foilBuilder == null) {
                    foilBuilder = new SpriteCoordinateUnExpander(buffer.getBuffer(RenderType.entityGlint()), ModMaterials.BOOK_GLINT_MATERIAL.sprite());
                }
                return foilBuilder;
            } else {
                return null;
            }
        }

        public float getAngle() {
            return this.yAngle;
        }

        public BookType getType() {
            return this.type;
        }
    }
}