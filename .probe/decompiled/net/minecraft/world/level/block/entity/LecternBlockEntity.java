package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class LecternBlockEntity extends BlockEntity implements Clearable, MenuProvider {

    public static final int DATA_PAGE = 0;

    public static final int NUM_DATA = 1;

    public static final int SLOT_BOOK = 0;

    public static final int NUM_SLOTS = 1;

    private final Container bookAccess = new Container() {

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return LecternBlockEntity.this.book.isEmpty();
        }

        @Override
        public ItemStack getItem(int p_59580_) {
            return p_59580_ == 0 ? LecternBlockEntity.this.book : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int p_59582_, int p_59583_) {
            if (p_59582_ == 0) {
                ItemStack $$2 = LecternBlockEntity.this.book.split(p_59583_);
                if (LecternBlockEntity.this.book.isEmpty()) {
                    LecternBlockEntity.this.onBookItemRemove();
                }
                return $$2;
            } else {
                return ItemStack.EMPTY;
            }
        }

        @Override
        public ItemStack removeItemNoUpdate(int p_59590_) {
            if (p_59590_ == 0) {
                ItemStack $$1 = LecternBlockEntity.this.book;
                LecternBlockEntity.this.book = ItemStack.EMPTY;
                LecternBlockEntity.this.onBookItemRemove();
                return $$1;
            } else {
                return ItemStack.EMPTY;
            }
        }

        @Override
        public void setItem(int p_59585_, ItemStack p_59586_) {
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            LecternBlockEntity.this.m_6596_();
        }

        @Override
        public boolean stillValid(Player p_59588_) {
            return Container.stillValidBlockEntity(LecternBlockEntity.this, p_59588_) && LecternBlockEntity.this.hasBook();
        }

        @Override
        public boolean canPlaceItem(int p_59592_, ItemStack p_59593_) {
            return false;
        }

        @Override
        public void clearContent() {
        }
    };

    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int p_59600_) {
            return p_59600_ == 0 ? LecternBlockEntity.this.page : 0;
        }

        @Override
        public void set(int p_59602_, int p_59603_) {
            if (p_59602_ == 0) {
                LecternBlockEntity.this.setPage(p_59603_);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    ItemStack book = ItemStack.EMPTY;

    int page;

    private int pageCount;

    public LecternBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.LECTERN, blockPos0, blockState1);
    }

    public ItemStack getBook() {
        return this.book;
    }

    public boolean hasBook() {
        return this.book.is(Items.WRITABLE_BOOK) || this.book.is(Items.WRITTEN_BOOK);
    }

    public void setBook(ItemStack itemStack0) {
        this.setBook(itemStack0, null);
    }

    void onBookItemRemove() {
        this.page = 0;
        this.pageCount = 0;
        LecternBlock.resetBookState(null, this.m_58904_(), this.m_58899_(), this.m_58900_(), false);
    }

    public void setBook(ItemStack itemStack0, @Nullable Player player1) {
        this.book = this.resolveBook(itemStack0, player1);
        this.page = 0;
        this.pageCount = WrittenBookItem.getPageCount(this.book);
        this.m_6596_();
    }

    void setPage(int int0) {
        int $$1 = Mth.clamp(int0, 0, this.pageCount - 1);
        if ($$1 != this.page) {
            this.page = $$1;
            this.m_6596_();
            LecternBlock.signalPageChange(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public int getPage() {
        return this.page;
    }

    public int getRedstoneSignal() {
        float $$0 = this.pageCount > 1 ? (float) this.getPage() / ((float) this.pageCount - 1.0F) : 1.0F;
        return Mth.floor($$0 * 14.0F) + (this.hasBook() ? 1 : 0);
    }

    private ItemStack resolveBook(ItemStack itemStack0, @Nullable Player player1) {
        if (this.f_58857_ instanceof ServerLevel && itemStack0.is(Items.WRITTEN_BOOK)) {
            WrittenBookItem.resolveBookComponents(itemStack0, this.createCommandSourceStack(player1), player1);
        }
        return itemStack0;
    }

    private CommandSourceStack createCommandSourceStack(@Nullable Player player0) {
        String $$1;
        Component $$2;
        if (player0 == null) {
            $$1 = "Lectern";
            $$2 = Component.literal("Lectern");
        } else {
            $$1 = player0.getName().getString();
            $$2 = player0.getDisplayName();
        }
        Vec3 $$5 = Vec3.atCenterOf(this.f_58858_);
        return new CommandSourceStack(CommandSource.NULL, $$5, Vec2.ZERO, (ServerLevel) this.f_58857_, 2, $$1, $$2, this.f_58857_.getServer(), player0);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("Book", 10)) {
            this.book = this.resolveBook(ItemStack.of(compoundTag0.getCompound("Book")), null);
        } else {
            this.book = ItemStack.EMPTY;
        }
        this.pageCount = WrittenBookItem.getPageCount(this.book);
        this.page = Mth.clamp(compoundTag0.getInt("Page"), 0, this.pageCount - 1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (!this.getBook().isEmpty()) {
            compoundTag0.put("Book", this.getBook().save(new CompoundTag()));
            compoundTag0.putInt("Page", this.page);
        }
    }

    @Override
    public void clearContent() {
        this.setBook(ItemStack.EMPTY);
    }

    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        return new LecternMenu(int0, this.bookAccess, this.dataAccess);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.lectern");
    }
}