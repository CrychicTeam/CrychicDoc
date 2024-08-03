package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.recipe.IAlchemyRecipe;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

public class AlchemyPedestalTopBlockEntity extends AlchemyPedestalBlockEntity implements ISoundController {

    public int active = 0;

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public AlchemyPedestalTopBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ALCHEMY_PEDESTAL_TOP_ENTITY.get(), pPos, pBlockState);
        this.inventory = new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                AlchemyPedestalTopBlockEntity.this.m_6596_();
            }
        };
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AlchemyPedestalTopBlockEntity blockEntity) {
        blockEntity.handleSound();
        blockEntity.active--;
    }

    public IAlchemyRecipe.PedestalContents getContents() {
        ItemStack input = ItemStack.EMPTY;
        BlockEntity tile = this.f_58857_.getBlockEntity(this.f_58858_.below());
        if (tile instanceof AlchemyPedestalBlockEntity) {
            input = ((AlchemyPedestalBlockEntity) tile).inventory.getStackInSlot(0);
        }
        return new IAlchemyRecipe.PedestalContents(input, this.inventory.getStackInSlot(0));
    }

    public boolean isValid() {
        if (this.inventory.getStackInSlot(0).isEmpty()) {
            return false;
        } else {
            BlockEntity tile = this.f_58857_.getBlockEntity(this.f_58858_.below());
            return tile instanceof AlchemyPedestalBlockEntity ? !((AlchemyPedestalBlockEntity) tile).inventory.getStackInSlot(0).isEmpty() : false;
        }
    }

    public boolean isActive() {
        return this.active > 0;
    }

    public void setActive(int time) {
        this.active = time;
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.PEDESTAL_LOOP.get(), SoundSource.BLOCKS, true, 0.1F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 1.0F, (float) this.f_58858_.m_123343_() + 0.5F);
            default:
                this.soundsPlaying.add(id);
        }
    }

    @Override
    public void stopSound(int id) {
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        return id == 1 && this.isActive();
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", null));
    }
}