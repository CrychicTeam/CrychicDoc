package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CabinetBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);

    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            CabinetBlockEntity.this.playSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
            CabinetBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            CabinetBlockEntity.this.playSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
            CabinetBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState sta, int arg1, int arg2) {
        }

        @Override
        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu) p_155060_.containerMenu).getContainer();
                return container == CabinetBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CABINET.get(), pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        if (!this.m_59634_(compound)) {
            ContainerHelper.saveAllItems(compound, this.contents);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.contents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compound)) {
            ContainerHelper.loadAllItems(compound, this.contents);
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.contents = itemsIn;
    }

    @Override
    protected Component getDefaultName() {
        return TextUtils.getTranslation("container.cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    public void startOpen(Player pPlayer) {
        if (this.f_58857_ != null && !this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public void stopOpen(Player pPlayer) {
        if (this.f_58857_ != null && !this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    public void recheckOpen() {
        if (this.f_58857_ != null && !this.f_58859_) {
            this.openersCounter.recheckOpeners(this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    void updateBlockState(BlockState state, boolean open) {
        if (this.f_58857_ != null) {
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) state.m_61124_(CabinetBlock.OPEN, open), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (this.f_58857_ != null) {
            Vec3i cabinetFacingVector = ((Direction) state.m_61143_(CabinetBlock.FACING)).getNormal();
            double x = (double) this.f_58858_.m_123341_() + 0.5 + (double) cabinetFacingVector.getX() / 2.0;
            double y = (double) this.f_58858_.m_123342_() + 0.5 + (double) cabinetFacingVector.getY() / 2.0;
            double z = (double) this.f_58858_.m_123343_() + 0.5 + (double) cabinetFacingVector.getZ() / 2.0;
            this.f_58857_.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}