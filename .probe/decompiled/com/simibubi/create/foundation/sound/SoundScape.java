package com.simibubi.create.foundation.sound;

import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

class SoundScape {

    List<ContinuousSound> continuous;

    List<RepeatingSound> repeating;

    private float pitch;

    private SoundScapes.AmbienceGroup group;

    private Vec3 meanPos;

    private SoundScapes.PitchGroup pitchGroup;

    public SoundScape(float pitch, SoundScapes.AmbienceGroup group) {
        this.pitchGroup = SoundScapes.getGroupFromPitch(pitch);
        this.pitch = pitch;
        this.group = group;
        this.continuous = new ArrayList();
        this.repeating = new ArrayList();
    }

    public SoundScape continuous(SoundEvent sound, float relativeVolume, float relativePitch) {
        return this.add(new ContinuousSound(sound, this, this.pitch * relativePitch, relativeVolume));
    }

    public SoundScape repeating(SoundEvent sound, float relativeVolume, float relativePitch, int delay) {
        return this.add(new RepeatingSound(sound, this, this.pitch * relativePitch, relativeVolume, delay));
    }

    public SoundScape add(ContinuousSound continuousSound) {
        this.continuous.add(continuousSound);
        return this;
    }

    public SoundScape add(RepeatingSound repeatingSound) {
        this.repeating.add(repeatingSound);
        return this;
    }

    public void play() {
        this.continuous.forEach(Minecraft.getInstance().getSoundManager()::m_120367_);
    }

    public void tick() {
        if (AnimationTickHolder.getTicks() % 5 == 0) {
            this.meanPos = null;
        }
        this.repeating.forEach(RepeatingSound::tick);
    }

    public void remove() {
        this.continuous.forEach(ContinuousSound::remove);
    }

    public Vec3 getMeanPos() {
        return this.meanPos == null ? (this.meanPos = this.determineMeanPos()) : this.meanPos;
    }

    private Vec3 determineMeanPos() {
        this.meanPos = Vec3.ZERO;
        int amount = 0;
        for (BlockPos blockPos : SoundScapes.getAllLocations(this.group, this.pitchGroup)) {
            this.meanPos = this.meanPos.add(VecHelper.getCenterOf(blockPos));
            amount++;
        }
        return amount == 0 ? this.meanPos : this.meanPos.scale((double) (1.0F / (float) amount));
    }

    public float getVolume() {
        Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
        float distanceMultiplier = 0.0F;
        if (renderViewEntity != null) {
            double distanceTo = renderViewEntity.position().distanceTo(this.getMeanPos());
            distanceMultiplier = (float) Mth.lerp(distanceTo / 16.0, 2.0, 0.0);
        }
        int soundCount = SoundScapes.getSoundCount(this.group, this.pitchGroup);
        float max = AllConfigs.client().ambientVolumeCap.getF();
        float argMax = 15.0F;
        return Mth.clamp((float) soundCount / (argMax * 10.0F), 0.025F, max) * distanceMultiplier;
    }
}