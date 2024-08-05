package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class ChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {

    private static final int EVENT_SET_OPEN_COUNT = 1;

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level p_155357_, BlockPos p_155358_, BlockState p_155359_) {
            ChestBlockEntity.playSound(p_155357_, p_155358_, p_155359_, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level p_155367_, BlockPos p_155368_, BlockState p_155369_) {
            ChestBlockEntity.playSound(p_155367_, p_155368_, p_155369_, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level p_155361_, BlockPos p_155362_, BlockState p_155363_, int p_155364_, int p_155365_) {
            ChestBlockEntity.this.signalOpenCount(p_155361_, p_155362_, p_155363_, p_155364_, p_155365_);
        }

        @Override
        protected boolean isOwnContainer(Player p_155355_) {
            if (!(p_155355_.containerMenu instanceof ChestMenu)) {
                return false;
            } else {
                Container $$1 = ((ChestMenu) p_155355_.containerMenu).getContainer();
                return $$1 == ChestBlockEntity.this || $$1 instanceof CompoundContainer && ((CompoundContainer) $$1).contains(ChestBlockEntity.this);
            }
        }
    };

    private final ChestLidController chestLidController = new ChestLidController();

    protected ChestBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    public ChestBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        this(BlockEntityType.CHEST, blockPos0, blockState1);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.chest");
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.m_142466_(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compoundTag0)) {
            ContainerHelper.loadAllItems(compoundTag0, this.items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.m_183515_(compoundTag0);
        if (!this.m_59634_(compoundTag0)) {
            ContainerHelper.saveAllItems(compoundTag0, this.items);
        }
    }

    public static void lidAnimateTick(Level level0, BlockPos blockPos1, BlockState blockState2, ChestBlockEntity chestBlockEntity3) {
        chestBlockEntity3.chestLidController.tickLid();
    }

    static void playSound(Level level0, BlockPos blockPos1, BlockState blockState2, SoundEvent soundEvent3) {
        ChestType $$4 = (ChestType) blockState2.m_61143_(ChestBlock.TYPE);
        if ($$4 != ChestType.LEFT) {
            double $$5 = (double) blockPos1.m_123341_() + 0.5;
            double $$6 = (double) blockPos1.m_123342_() + 0.5;
            double $$7 = (double) blockPos1.m_123343_() + 0.5;
            if ($$4 == ChestType.RIGHT) {
                Direction $$8 = ChestBlock.getConnectedDirection(blockState2);
                $$5 += (double) $$8.getStepX() * 0.5;
                $$7 += (double) $$8.getStepZ() * 0.5;
            }
            level0.playSound(null, $$5, $$6, $$7, soundEvent3, SoundSource.BLOCKS, 0.5F, level0.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public boolean triggerEvent(int int0, int int1) {
        if (int0 == 1) {
            this.chestLidController.shouldBeOpen(int1 > 0);
            return true;
        } else {
            return super.m_7531_(int0, int1);
        }
    }

    @Override
    public void startOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.incrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public void stopOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.decrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
        this.items = nonNullListItemStack0;
    }

    @Override
    public float getOpenNess(float float0) {
        return this.chestLidController.getOpenness(float0);
    }

    public static int getOpenCount(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockState $$2 = blockGetter0.getBlockState(blockPos1);
        if ($$2.m_155947_()) {
            BlockEntity $$3 = blockGetter0.getBlockEntity(blockPos1);
            if ($$3 instanceof ChestBlockEntity) {
                return ((ChestBlockEntity) $$3).openersCounter.getOpenerCount();
            }
        }
        return 0;
    }

    public static void swapContents(ChestBlockEntity chestBlockEntity0, ChestBlockEntity chestBlockEntity1) {
        NonNullList<ItemStack> $$2 = chestBlockEntity0.getItems();
        chestBlockEntity0.setItems(chestBlockEntity1.getItems());
        chestBlockEntity1.setItems($$2);
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return ChestMenu.threeRows(int0, inventory1, this);
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    protected void signalOpenCount(Level level0, BlockPos blockPos1, BlockState blockState2, int int3, int int4) {
        Block $$5 = blockState2.m_60734_();
        level0.blockEvent(blockPos1, $$5, 1, int4);
    }
}