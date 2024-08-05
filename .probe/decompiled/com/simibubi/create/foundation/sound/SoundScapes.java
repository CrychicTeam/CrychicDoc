package com.simibubi.create.foundation.sound;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

public class SoundScapes {

    static final int MAX_AMBIENT_SOURCE_DISTANCE = 16;

    static final int UPDATE_INTERVAL = 5;

    static final int SOUND_VOLUME_ARG_MAX = 15;

    private static Map<SoundScapes.AmbienceGroup, Map<SoundScapes.PitchGroup, Set<BlockPos>>> counter = new IdentityHashMap();

    private static Map<Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup>, SoundScape> activeSounds = new HashMap();

    private static SoundScape kinetic(float pitch, SoundScapes.AmbienceGroup group) {
        return new SoundScape(pitch, group).continuous(SoundEvents.MINECART_INSIDE, 0.25F, 1.0F);
    }

    private static SoundScape cogwheel(float pitch, SoundScapes.AmbienceGroup group) {
        return new SoundScape(pitch, group).continuous(AllSoundEvents.COGS.getMainEvent(), 1.5F, 1.0F);
    }

    private static SoundScape crushing(float pitch, SoundScapes.AmbienceGroup group) {
        return new SoundScape(pitch, group).repeating(AllSoundEvents.CRUSHING_1.getMainEvent(), 1.545F, 0.75F, 1).repeating(AllSoundEvents.CRUSHING_2.getMainEvent(), 0.425F, 0.75F, 2).repeating(AllSoundEvents.CRUSHING_3.getMainEvent(), 2.0F, 1.75F, 2);
    }

    private static SoundScape milling(float pitch, SoundScapes.AmbienceGroup group) {
        return new SoundScape(pitch, group).repeating(AllSoundEvents.CRUSHING_1.getMainEvent(), 1.545F, 0.75F, 1).repeating(AllSoundEvents.CRUSHING_2.getMainEvent(), 0.425F, 0.75F, 2);
    }

    public static void play(SoundScapes.AmbienceGroup group, BlockPos pos, float pitch) {
        if (AllConfigs.client().enableAmbientSounds.get()) {
            if (!outOfRange(pos)) {
                addSound(group, pos, pitch);
            }
        }
    }

    public static void tick() {
        activeSounds.values().forEach(SoundScape::tick);
        if (AnimationTickHolder.getTicks() % 5 == 0) {
            boolean disable = !AllConfigs.client().enableAmbientSounds.get();
            Iterator<Entry<Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup>, SoundScape>> iterator = activeSounds.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup>, SoundScape> entry = (Entry<Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup>, SoundScape>) iterator.next();
                Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup> key = (Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup>) entry.getKey();
                SoundScape value = (SoundScape) entry.getValue();
                if (disable || getSoundCount(key.getFirst(), key.getSecond()) == 0) {
                    value.remove();
                    iterator.remove();
                }
            }
            counter.values().forEach(m -> m.values().forEach(Set::clear));
        }
    }

    private static void addSound(SoundScapes.AmbienceGroup group, BlockPos pos, float pitch) {
        SoundScapes.PitchGroup groupFromPitch = getGroupFromPitch(pitch);
        Set<BlockPos> set = (Set<BlockPos>) ((Map) counter.computeIfAbsent(group, ag -> new IdentityHashMap())).computeIfAbsent(groupFromPitch, pg -> new HashSet());
        set.add(pos);
        Pair<SoundScapes.AmbienceGroup, SoundScapes.PitchGroup> pair = Pair.of(group, groupFromPitch);
        activeSounds.computeIfAbsent(pair, $ -> {
            SoundScape soundScape = group.instantiate(pitch);
            soundScape.play();
            return soundScape;
        });
    }

    public static void invalidateAll() {
        counter.clear();
        activeSounds.forEach(($, sound) -> sound.remove());
        activeSounds.clear();
    }

    protected static boolean outOfRange(BlockPos pos) {
        return !getCameraPos().m_123314_(pos, 16.0);
    }

    protected static BlockPos getCameraPos() {
        Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
        return renderViewEntity == null ? BlockPos.ZERO : renderViewEntity.blockPosition();
    }

    public static int getSoundCount(SoundScapes.AmbienceGroup group, SoundScapes.PitchGroup pitchGroup) {
        return getAllLocations(group, pitchGroup).size();
    }

    public static Set<BlockPos> getAllLocations(SoundScapes.AmbienceGroup group, SoundScapes.PitchGroup pitchGroup) {
        return (Set<BlockPos>) ((Map) counter.getOrDefault(group, Collections.emptyMap())).getOrDefault(pitchGroup, Collections.emptySet());
    }

    public static SoundScapes.PitchGroup getGroupFromPitch(float pitch) {
        if ((double) pitch < 0.7) {
            return SoundScapes.PitchGroup.VERY_LOW;
        } else if ((double) pitch < 0.9) {
            return SoundScapes.PitchGroup.LOW;
        } else if ((double) pitch < 1.1) {
            return SoundScapes.PitchGroup.NORMAL;
        } else {
            return (double) pitch < 1.3 ? SoundScapes.PitchGroup.HIGH : SoundScapes.PitchGroup.VERY_HIGH;
        }
    }

    public static enum AmbienceGroup {

        KINETIC(SoundScapes::kinetic), COG(SoundScapes::cogwheel), CRUSHING(SoundScapes::crushing), MILLING(SoundScapes::milling);

        private BiFunction<Float, SoundScapes.AmbienceGroup, SoundScape> factory;

        private AmbienceGroup(BiFunction<Float, SoundScapes.AmbienceGroup, SoundScape> factory) {
            this.factory = factory;
        }

        public SoundScape instantiate(float pitch) {
            return (SoundScape) this.factory.apply(pitch, this);
        }
    }

    static enum PitchGroup {

        VERY_LOW, LOW, NORMAL, HIGH, VERY_HIGH
    }
}