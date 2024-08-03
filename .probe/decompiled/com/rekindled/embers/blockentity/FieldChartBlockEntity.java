package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FieldChartBlockEntity extends BlockEntity implements ISoundController {

    public static final int SOUND_LOOP = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public FieldChartBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.FIELD_CHART_ENTITY.get(), pPos, pBlockState);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 0, -1), this.f_58858_.offset(2, 1, 2));
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FieldChartBlockEntity blockEntity) {
        blockEntity.handleSound();
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.FIELD_CHART_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1;
    }
}