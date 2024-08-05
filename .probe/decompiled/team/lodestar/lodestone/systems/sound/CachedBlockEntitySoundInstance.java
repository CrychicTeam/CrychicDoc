package team.lodestar.lodestone.systems.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class CachedBlockEntitySoundInstance<T extends LodestoneBlockEntity> extends LodestoneBlockEntitySoundInstance<T> {

    private static final Map<BlockPos, CachedBlockEntitySoundInstance<?>> ACTIVE_SOUNDS = new HashMap();

    public CachedBlockEntitySoundInstance(T blockEntity, Supplier<SoundEvent> soundEvent, float volume, float pitch) {
        super(blockEntity, (SoundEvent) soundEvent.get(), volume, pitch);
        BlockPos pos = blockEntity.m_58899_();
        this.f_119575_ = (double) ((float) pos.m_123341_() + 0.5F);
        this.f_119576_ = (double) ((float) pos.m_123342_() + 0.5F);
        this.f_119577_ = (double) ((float) pos.m_123343_() + 0.5F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_7801_()) {
            ACTIVE_SOUNDS.remove(this.blockEntity.m_58899_());
        }
    }

    public static void playSound(LodestoneBlockEntity blockEntity, CachedBlockEntitySoundInstance<?> sound) {
        BlockPos blockPos = blockEntity.m_58899_();
        if (ACTIVE_SOUNDS.containsKey(blockPos)) {
            CachedBlockEntitySoundInstance<?> existingSound = (CachedBlockEntitySoundInstance<?>) ACTIVE_SOUNDS.get(blockPos);
            if (!existingSound.f_119572_.equals(sound.f_119572_)) {
                existingSound.m_119609_();
                ACTIVE_SOUNDS.put(blockPos, sound);
            }
        } else {
            ACTIVE_SOUNDS.put(blockPos, sound);
        }
        Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
    }
}