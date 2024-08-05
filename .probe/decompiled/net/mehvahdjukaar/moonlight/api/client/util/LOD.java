package net.mehvahdjukaar.moonlight.api.client.util;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LOD {

    public static final LOD MAX = new LOD(0.0);

    private final double distSq;

    public static final int BUFFER = 4;

    public static final int VERY_NEAR_DIST = 256;

    public static final int NEAR_DIST = 1024;

    public static final int NEAR_MED_DIST = 2304;

    public static final int MEDIUM_DIST = 4096;

    public static final int FAR_DIST = 9216;

    private LOD(double distance) {
        this.distSq = distance;
    }

    public LOD(Camera camera, BlockPos pos) {
        this(camera.getPosition(), pos);
    }

    public LOD(Vec3 cameraPos, BlockPos pos) {
        this(isScoping() ? 1.0 : Vec3.atCenterOf(pos).distanceToSqr(cameraPos));
    }

    public static boolean isScoping() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localplayer = minecraft.player;
        return localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.m_150108_();
    }

    public boolean isVeryNear() {
        return this.distSq < 256.0;
    }

    public boolean isNear() {
        return this.distSq < 1024.0;
    }

    public boolean isNearMed() {
        return this.distSq < 2304.0;
    }

    public boolean isMedium() {
        return this.distSq < 4096.0;
    }

    public boolean isFar() {
        return this.distSq < 9216.0;
    }

    @Deprecated(forRemoval = true)
    public static boolean isOutOfFocus(Vec3 cameraPos, BlockPos pos, float blockYaw) {
        return isOutOfFocus(cameraPos, pos, blockYaw, 0.0F, Direction.UP, 0.0F);
    }

    @Deprecated(forRemoval = true)
    public static boolean isOutOfFocus(Vec3 cameraPos, BlockPos pos, float blockYaw, float degMargin, Direction dir, float offset) {
        float relAngle = getRelativeAngle(cameraPos, pos, dir, offset);
        return isOutOfFocus(relAngle, blockYaw, degMargin);
    }

    @Deprecated(forRemoval = true)
    public static boolean isOutOfFocus(float relativeAngle, float blockYaw, float degMargin) {
        return Mth.degreesDifference(relativeAngle, blockYaw - 90.0F) > -degMargin;
    }

    public static float getRelativeAngle(Vec3 cameraPos, BlockPos pos) {
        return getRelativeAngle(cameraPos, pos, Direction.UP, 0.0F);
    }

    public static float getRelativeAngle(Vec3 cameraPos, BlockPos pos, Direction dir, float offset) {
        return (float) (Mth.atan2((double) (offset * (float) dir.getStepX()) + cameraPos.x - (double) ((float) pos.m_123341_() + 0.5F), (double) (offset * (float) dir.getStepZ()) + cameraPos.z - (double) ((float) pos.m_123343_() + 0.5F)) * 180.0 / Math.PI);
    }
}