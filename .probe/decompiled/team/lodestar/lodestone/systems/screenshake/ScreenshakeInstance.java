package team.lodestar.lodestone.systems.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;

public class ScreenshakeInstance {

    public int progress;

    public final int duration;

    public float intensity1;

    public float intensity2;

    public float intensity3;

    public Easing intensityCurveStartEasing = Easing.LINEAR;

    public Easing intensityCurveEndEasing = Easing.LINEAR;

    public ScreenshakeInstance(int duration) {
        this.duration = duration;
    }

    public ScreenshakeInstance setIntensity(float intensity) {
        return this.setIntensity(intensity, intensity);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2) {
        return this.setIntensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakeInstance setIntensity(float intensity1, float intensity2, float intensity3) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakeInstance setEasing(Easing easing) {
        return this.setEasing(easing, easing);
    }

    public ScreenshakeInstance setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    public float updateIntensity(Camera camera, RandomSource random) {
        this.progress++;
        float percentage = (float) this.progress / (float) this.duration;
        if (this.intensity2 != this.intensity3) {
            return percentage >= 0.5F ? Mth.lerp(this.intensityCurveEndEasing.ease(percentage - 0.5F, 0.0F, 1.0F, 0.5F), this.intensity2, this.intensity1) : Mth.lerp(this.intensityCurveStartEasing.ease(percentage, 0.0F, 1.0F, 0.5F), this.intensity1, this.intensity2);
        } else {
            return Mth.lerp(this.intensityCurveStartEasing.ease(percentage, 0.0F, 1.0F, 1.0F), this.intensity1, this.intensity2);
        }
    }
}