package icyllis.modernui.animation;

class ViscousFluidInterpolator implements TimeInterpolator {

    private static final float VISCOUS_FLUID_SCALE = 8.0F;

    private static final float VISCOUS_FLUID_NORMALIZE = 1.0F / viscousFluid(1.0F);

    private static final float VISCOUS_FLUID_OFFSET = 1.0F - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0F);

    private static final float START = 0.36787945F;

    private static float viscousFluid(float x) {
        x *= 8.0F;
        if (x < 1.0F) {
            x -= 1.0F - (float) Math.exp((double) (-x));
        } else {
            x = 1.0F - (float) Math.exp((double) (1.0F - x));
            x = 0.36787945F + x * 0.63212055F;
        }
        return x;
    }

    @Override
    public float getInterpolation(float progress) {
        float v = VISCOUS_FLUID_NORMALIZE * viscousFluid(progress);
        return v > 0.0F ? v + VISCOUS_FLUID_OFFSET : v;
    }
}