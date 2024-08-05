package net.minecraft.client.resources.sounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class SimpleSoundInstance extends AbstractSoundInstance {

    public SimpleSoundInstance(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3, RandomSource randomSource4, BlockPos blockPos5) {
        this(soundEvent0, soundSource1, float2, float3, randomSource4, (double) blockPos5.m_123341_() + 0.5, (double) blockPos5.m_123342_() + 0.5, (double) blockPos5.m_123343_() + 0.5);
    }

    public static SimpleSoundInstance forUI(SoundEvent soundEvent0, float float1) {
        return forUI(soundEvent0, float1, 0.25F);
    }

    public static SimpleSoundInstance forUI(Holder<SoundEvent> holderSoundEvent0, float float1) {
        return forUI(holderSoundEvent0.value(), float1);
    }

    public static SimpleSoundInstance forUI(SoundEvent soundEvent0, float float1, float float2) {
        return new SimpleSoundInstance(soundEvent0.getLocation(), SoundSource.MASTER, float2, float1, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }

    public static SimpleSoundInstance forMusic(SoundEvent soundEvent0) {
        return new SimpleSoundInstance(soundEvent0.getLocation(), SoundSource.MUSIC, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }

    public static SimpleSoundInstance forRecord(SoundEvent soundEvent0, Vec3 vec1) {
        return new SimpleSoundInstance(soundEvent0, SoundSource.RECORDS, 4.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, vec1.x, vec1.y, vec1.z);
    }

    public static SimpleSoundInstance forLocalAmbience(SoundEvent soundEvent0, float float1, float float2) {
        return new SimpleSoundInstance(soundEvent0.getLocation(), SoundSource.AMBIENT, float2, float1, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }

    public static SimpleSoundInstance forAmbientAddition(SoundEvent soundEvent0) {
        return forLocalAmbience(soundEvent0, 1.0F, 1.0F);
    }

    public static SimpleSoundInstance forAmbientMood(SoundEvent soundEvent0, RandomSource randomSource1, double double2, double double3, double double4) {
        return new SimpleSoundInstance(soundEvent0, SoundSource.AMBIENT, 1.0F, 1.0F, randomSource1, false, 0, SoundInstance.Attenuation.LINEAR, double2, double3, double4);
    }

    public SimpleSoundInstance(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3, RandomSource randomSource4, double double5, double double6, double double7) {
        this(soundEvent0, soundSource1, float2, float3, randomSource4, false, 0, SoundInstance.Attenuation.LINEAR, double5, double6, double7);
    }

    private SimpleSoundInstance(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3, RandomSource randomSource4, boolean boolean5, int int6, SoundInstance.Attenuation soundInstanceAttenuation7, double double8, double double9, double double10) {
        this(soundEvent0.getLocation(), soundSource1, float2, float3, randomSource4, boolean5, int6, soundInstanceAttenuation7, double8, double9, double10, false);
    }

    public SimpleSoundInstance(ResourceLocation resourceLocation0, SoundSource soundSource1, float float2, float float3, RandomSource randomSource4, boolean boolean5, int int6, SoundInstance.Attenuation soundInstanceAttenuation7, double double8, double double9, double double10, boolean boolean11) {
        super(resourceLocation0, soundSource1, randomSource4);
        this.f_119573_ = float2;
        this.f_119574_ = float3;
        this.f_119575_ = double8;
        this.f_119576_ = double9;
        this.f_119577_ = double10;
        this.f_119578_ = boolean5;
        this.f_119579_ = int6;
        this.f_119580_ = soundInstanceAttenuation7;
        this.f_119582_ = boolean11;
    }
}