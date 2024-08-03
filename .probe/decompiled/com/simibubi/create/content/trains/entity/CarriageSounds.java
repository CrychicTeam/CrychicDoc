package com.simibubi.create.content.trains.entity;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CarriageSounds {

    CarriageContraptionEntity entity;

    LerpedFloat distanceFactor;

    LerpedFloat speedFactor;

    LerpedFloat approachFactor;

    LerpedFloat seatCrossfade;

    CarriageSounds.LoopingSound minecartEsqueSound;

    CarriageSounds.LoopingSound sharedWheelSound;

    CarriageSounds.LoopingSound sharedWheelSoundSeated;

    CarriageSounds.LoopingSound sharedHonkSound;

    Couple<SoundEvent> bogeySounds;

    SoundEvent closestBogeySound;

    boolean arrived;

    int tick;

    int prevSharedTick;

    public CarriageSounds(CarriageContraptionEntity entity) {
        this.entity = entity;
        this.bogeySounds = entity.getCarriage().bogeys.map(bogey -> bogey != null && bogey.getStyle() != null ? bogey.getStyle().getSoundType() : AllSoundEvents.TRAIN2.getMainEvent());
        this.closestBogeySound = this.bogeySounds.getFirst();
        this.distanceFactor = LerpedFloat.linear();
        this.speedFactor = LerpedFloat.linear();
        this.approachFactor = LerpedFloat.linear();
        this.seatCrossfade = LerpedFloat.linear();
        this.arrived = true;
    }

    public void tick(Carriage.DimensionalCarriageEntity dce) {
        Minecraft mc = Minecraft.getInstance();
        Entity camEntity = mc.cameraEntity;
        if (camEntity != null) {
            Vec3 leadingAnchor = dce.leadingAnchor();
            Vec3 trailingAnchor = dce.trailingAnchor();
            if (leadingAnchor != null && trailingAnchor != null) {
                this.tick++;
                Vec3 cam = camEntity.getEyePosition();
                Vec3 contraptionMotion = this.entity.m_20182_().subtract(this.entity.getPrevPositionVec());
                Vec3 combinedMotion = contraptionMotion.subtract(camEntity.getDeltaMovement());
                Train train = this.entity.getCarriage().train;
                if (this.arrived && contraptionMotion.length() > 0.01F) {
                    this.arrived = false;
                }
                if (this.arrived && this.entity.carriageIndex == 0) {
                    train.accumulatedSteamRelease /= 2.0F;
                }
                this.arrived = this.arrived | this.entity.isStalled();
                if (this.entity.carriageIndex == 0) {
                    train.accumulatedSteamRelease = (float) Math.min((double) train.accumulatedSteamRelease + Math.min(0.5, Math.abs(contraptionMotion.length() / 10.0)), 10.0);
                }
                Vec3 toBogey1 = leadingAnchor.subtract(cam);
                Vec3 toBogey2 = trailingAnchor.subtract(cam);
                double distance1 = toBogey1.length();
                double distance2 = toBogey2.length();
                Couple<CarriageBogey> bogeys = this.entity.getCarriage().bogeys;
                CarriageBogey relevantBogey = bogeys.get(distance1 > distance2);
                if (relevantBogey == null) {
                    relevantBogey = bogeys.getFirst();
                }
                if (relevantBogey != null) {
                    this.closestBogeySound = relevantBogey.getStyle().getSoundType();
                }
                Vec3 toCarriage = distance1 > distance2 ? toBogey2 : toBogey1;
                double distance = Math.min(distance1, distance2);
                Vec3 soundLocation = cam.add(toCarriage);
                double dot = toCarriage.normalize().dot(combinedMotion.normalize());
                this.speedFactor.chase(contraptionMotion.length(), 0.25, LerpedFloat.Chaser.exp(0.05F));
                this.distanceFactor.chase(Mth.clampedLerp(100.0, 0.0, (distance - 3.0) / 64.0), 0.25, LerpedFloat.Chaser.exp(50.0));
                this.approachFactor.chase(Mth.clampedLerp(50.0, 200.0, 0.5 * (dot + 1.0)), 0.25, LerpedFloat.Chaser.exp(10.0));
                this.seatCrossfade.chase(camEntity.getVehicle() instanceof CarriageContraptionEntity ? 1.0 : 0.0, 0.1F, LerpedFloat.Chaser.EXP);
                this.speedFactor.tickChaser();
                this.distanceFactor.tickChaser();
                this.approachFactor.tickChaser();
                this.seatCrossfade.tickChaser();
                this.minecartEsqueSound = this.playIfMissing(mc, this.minecartEsqueSound, AllSoundEvents.TRAIN.getMainEvent());
                this.sharedWheelSound = this.playIfMissing(mc, this.sharedWheelSound, this.closestBogeySound);
                this.sharedWheelSoundSeated = this.playIfMissing(mc, this.sharedWheelSoundSeated, AllSoundEvents.TRAIN3.getMainEvent());
                float volume = Math.min(Math.min(this.speedFactor.getValue(), this.distanceFactor.getValue() / 100.0F), this.approachFactor.getValue() / 300.0F + 0.0125F);
                if (this.entity.carriageIndex == 0) {
                    float v = volume * (1.0F - this.seatCrossfade.getValue() * 0.35F) * 0.75F;
                    if ((3 + this.tick) % 4 == 0) {
                        AllSoundEvents.STEAM.playAt(this.entity.m_9236_(), soundLocation, v * ((this.tick + 7) % 8 == 0 ? 0.75F : 0.45F), 1.17F, false);
                    }
                    if (this.tick % 16 == 0) {
                        AllSoundEvents.STEAM.playAt(this.entity.m_9236_(), soundLocation, v * 1.5F, 0.8F, false);
                    }
                }
                if (!this.arrived && this.speedFactor.getValue() < 0.002F && train.accumulatedSteamRelease > 1.0F) {
                    this.arrived = true;
                    float releaseVolume = train.accumulatedSteamRelease / 10.0F;
                    this.entity.m_9236_().playLocalSound(soundLocation.x, soundLocation.y, soundLocation.z, SoundEvents.LAVA_EXTINGUISH, SoundSource.NEUTRAL, 0.25F * releaseVolume, 0.78F, false);
                    this.entity.m_9236_().playLocalSound(soundLocation.x, soundLocation.y, soundLocation.z, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.NEUTRAL, 0.2F * releaseVolume, 1.5F, false);
                    AllSoundEvents.STEAM.playAt(this.entity.m_9236_(), soundLocation, 0.75F * releaseVolume, 0.5F, false);
                }
                float pitchModifier = (float) (this.entity.m_19879_() * 10 % 13) / 36.0F;
                volume = Math.min(volume, this.distanceFactor.getValue() / 800.0F);
                float pitch = Mth.clamp(this.speedFactor.getValue() * 2.0F + 0.25F, 0.75F, 1.95F) - pitchModifier;
                this.minecartEsqueSound.setPitch(pitch * 1.5F);
                volume = Math.min(volume, this.distanceFactor.getValue() / 1000.0F);
                for (Carriage carriage : train.carriages) {
                    Carriage.DimensionalCarriageEntity mainDCE = carriage.getDimensionalIfPresent(this.entity.m_9236_().dimension());
                    if (mainDCE != null) {
                        CarriageContraptionEntity mainEntity = (CarriageContraptionEntity) mainDCE.entity.get();
                        if (mainEntity != null) {
                            if (mainEntity.sounds == null) {
                                mainEntity.sounds = new CarriageSounds(mainEntity);
                            }
                            mainEntity.sounds.submitSharedSoundVolume(soundLocation, volume);
                            if (carriage != this.entity.getCarriage()) {
                                this.finalizeSharedVolume(0.0F);
                                return;
                            }
                            break;
                        }
                    }
                }
                if (train.honkTicks == 0) {
                    if (this.sharedHonkSound != null) {
                        this.sharedHonkSound.stopSound();
                        this.sharedHonkSound = null;
                    }
                } else {
                    train.honkTicks--;
                    train.determineHonk(this.entity.m_9236_());
                    if (train.lowHonk != null) {
                        boolean low = train.lowHonk;
                        float honkPitch = (float) Math.pow(2.0, (double) train.honkPitch / 12.0);
                        AllSoundEvents.SoundEntry endSound = !low ? AllSoundEvents.WHISTLE_TRAIN_MANUAL_END : AllSoundEvents.WHISTLE_TRAIN_MANUAL_LOW_END;
                        AllSoundEvents.SoundEntry continuousSound = !low ? AllSoundEvents.WHISTLE_TRAIN_MANUAL : AllSoundEvents.WHISTLE_TRAIN_MANUAL_LOW;
                        if (train.honkTicks == 5) {
                            endSound.playAt(mc.level, soundLocation, 1.0F, honkPitch, false);
                        }
                        if (train.honkTicks == 19) {
                            endSound.playAt(mc.level, soundLocation, 0.5F, honkPitch, false);
                        }
                        this.sharedHonkSound = this.playIfMissing(mc, this.sharedHonkSound, continuousSound.getMainEvent());
                        this.sharedHonkSound.setLocation(soundLocation);
                        float fadeout = Mth.clamp((float) (3 - train.honkTicks) / 3.0F, 0.0F, 1.0F);
                        float fadein = Mth.clamp((float) (train.honkTicks - 17) / 3.0F, 0.0F, 1.0F);
                        this.sharedHonkSound.setVolume(1.0F - fadeout - fadein);
                        this.sharedHonkSound.setPitch(honkPitch);
                    }
                }
            }
        }
    }

    private CarriageSounds.LoopingSound playIfMissing(Minecraft mc, CarriageSounds.LoopingSound loopingSound, SoundEvent sound) {
        if (loopingSound == null) {
            loopingSound = new CarriageSounds.LoopingSound(sound, SoundSource.NEUTRAL);
            mc.getSoundManager().play(loopingSound);
        }
        return loopingSound;
    }

    public void submitSharedSoundVolume(Vec3 location, float volume) {
        Minecraft mc = Minecraft.getInstance();
        this.minecartEsqueSound = this.playIfMissing(mc, this.minecartEsqueSound, AllSoundEvents.TRAIN.getMainEvent());
        this.sharedWheelSound = this.playIfMissing(mc, this.sharedWheelSound, this.closestBogeySound);
        this.sharedWheelSoundSeated = this.playIfMissing(mc, this.sharedWheelSoundSeated, AllSoundEvents.TRAIN3.getMainEvent());
        boolean approach = true;
        if (this.tick != this.prevSharedTick) {
            this.prevSharedTick = this.tick;
            approach = false;
        } else if (this.sharedWheelSound.getVolume() > volume) {
            return;
        }
        Vec3 currentLoc = new Vec3(this.minecartEsqueSound.m_7772_(), this.minecartEsqueSound.m_7780_(), this.minecartEsqueSound.m_7778_());
        Vec3 newLoc = approach ? currentLoc.add(location.subtract(currentLoc).scale(0.125)) : location;
        this.minecartEsqueSound.setLocation(newLoc);
        this.sharedWheelSound.setLocation(newLoc);
        this.sharedWheelSoundSeated.setLocation(newLoc);
        this.finalizeSharedVolume(volume);
    }

    public void finalizeSharedVolume(float volume) {
        float crossfade = this.seatCrossfade.getValue();
        this.minecartEsqueSound.setVolume((1.0F - crossfade * 0.65F) * volume / 2.0F);
        volume = Math.min(volume, Math.max((this.speedFactor.getValue() - 0.25F) / 4.0F + 0.01F, 0.0F));
        this.sharedWheelSoundSeated.setVolume(volume * crossfade);
        this.sharedWheelSound.setVolume(volume * (1.0F - crossfade) * 1.5F);
    }

    public void stop() {
        if (this.minecartEsqueSound != null) {
            this.minecartEsqueSound.stopSound();
        }
        if (this.sharedWheelSound != null) {
            this.sharedWheelSound.stopSound();
        }
        if (this.sharedWheelSoundSeated != null) {
            this.sharedWheelSoundSeated.stopSound();
        }
        if (this.sharedHonkSound != null) {
            this.sharedHonkSound.stopSound();
        }
    }

    class LoopingSound extends AbstractTickableSoundInstance {

        protected LoopingSound(SoundEvent soundEvent0, SoundSource soundSource1) {
            super(soundEvent0, soundSource1, SoundInstance.createUnseededRandom());
            this.f_119580_ = SoundInstance.Attenuation.LINEAR;
            this.f_119578_ = true;
            this.f_119579_ = 0;
            this.f_119573_ = 1.0E-4F;
        }

        @Override
        public void tick() {
        }

        public void setVolume(float volume) {
            this.f_119573_ = volume;
        }

        @Override
        public float getVolume() {
            return this.f_119573_;
        }

        public void setPitch(float pitch) {
            this.f_119574_ = pitch;
        }

        @Override
        public float getPitch() {
            return this.f_119574_;
        }

        public void setLocation(Vec3 location) {
            this.f_119575_ = location.x;
            this.f_119576_ = location.y;
            this.f_119577_ = location.z;
        }

        public void stopSound() {
            Minecraft.getInstance().getSoundManager().stop(this);
        }
    }
}