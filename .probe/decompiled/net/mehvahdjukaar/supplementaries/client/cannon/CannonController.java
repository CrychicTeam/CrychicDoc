package net.mehvahdjukaar.supplementaries.client.cannon;

import net.mehvahdjukaar.supplementaries.common.block.blocks.CannonBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSyncCannonPacket;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CannonController {

    @Nullable
    protected static CannonBlockTile cannon;

    private static CameraType lastCameraType;

    protected static HitResult hit;

    private static boolean firstTick = true;

    private static float yawIncrease;

    private static float pitchIncrease;

    private static boolean needsToUpdateServer;

    private static boolean preferShootingDown = true;

    @Nullable
    protected static CannonTrajectory trajectory;

    private static Vec3 lastCameraPos;

    private static float lastZoomOut = 0.0F;

    private static float lastCameraYaw = 0.0F;

    private static float lastCameraPitch = 0.0F;

    public static void activateCannonCamera(CannonBlockTile tile) {
        cannon = tile;
        firstTick = true;
        preferShootingDown = true;
        Minecraft mc = Minecraft.getInstance();
        lastCameraType = mc.options.getCameraType();
        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        mc.gui.setOverlayMessage(Component.translatable("message.supplementaries.cannon_maneuver", mc.options.keyShift.getTranslatedKeyMessage(), mc.options.keyAttack.getTranslatedKeyMessage()), false);
    }

    public static void turnOff() {
        cannon = null;
        lastCameraYaw = 0.0F;
        lastCameraPitch = 0.0F;
        lastZoomOut = 0.0F;
        lastCameraPos = null;
        if (lastCameraType != null) {
            Minecraft.getInstance().options.setCameraType(lastCameraType);
        }
    }

    public static boolean isActive() {
        return cannon != null;
    }

    public static boolean setupCamera(Camera camera, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick) {
        if (isActive()) {
            Vec3 centerCannonPos = cannon.m_58899_().getCenter();
            if (lastCameraPos == null) {
                lastCameraPos = camera.getPosition();
                lastCameraYaw = camera.getYRot();
                lastCameraPitch = camera.getXRot();
            }
            Vec3 targetCameraPos = centerCannonPos.add(0.0, 2.0, 0.0);
            float targetYRot = camera.getYRot() + yawIncrease;
            float targetXRot = Mth.clamp(camera.getXRot() + pitchIncrease, -90.0F, 90.0F);
            camera.setPosition(targetCameraPos);
            camera.setRotation(targetYRot, targetXRot);
            lastCameraPos = camera.getPosition();
            lastCameraYaw = camera.getYRot();
            lastCameraPitch = camera.getXRot();
            lastZoomOut = (float) camera.getMaxZoom(4.0);
            camera.move((double) (-lastZoomOut), 0.0, -1.0);
            yawIncrease = 0.0F;
            pitchIncrease = 0.0F;
            Vec3 lookDir2 = new Vec3(camera.getLookVector());
            float maxRange = 128.0F;
            Vec3 actualCameraPos = camera.getPosition();
            Vec3 endPos = actualCameraPos.add(lookDir2.scale((double) maxRange));
            hit = level.clip(new ClipContext(actualCameraPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity));
            Vec3 targetVector = hit.getLocation().subtract(cannon.m_58899_().getCenter());
            Vec2 target = new Vec2((float) Mth.length(targetVector.x, targetVector.z), (float) targetVector.y);
            target = target.add(target.normalized().scale(0.05F));
            float wantedCannonYaw = (float) Math.PI + (float) Mth.atan2(-targetVector.x, targetVector.z);
            CannonController.Restraint restraints = getPitchAndYawRestrains(cannon.m_58900_());
            trajectory = CannonTrajectory.findBest(target, cannon.getProjectileGravity(), cannon.getProjectileDrag(), (float) cannon.getFirePower(), preferShootingDown, restraints.minPitch, restraints.maxPitch);
            if (trajectory != null) {
                float followSpeed = 0.4F;
                cannon.setPitch(Mth.rotLerp(followSpeed, cannon.getPitch(1.0F), trajectory.pitch() * (180.0F / (float) Math.PI)));
                float yaw = Mth.rotLerp(followSpeed, cannon.getYaw(1.0F), wantedCannonYaw * (180.0F / (float) Math.PI));
                cannon.setYaw(Mth.clamp(yaw, restraints.minYaw, restraints.maxYaw));
            }
            return true;
        } else {
            return false;
        }
    }

    private static CannonController.Restraint getPitchAndYawRestrains(BlockState state) {
        return switch((Direction) state.m_61143_(CannonBlock.f_52588_)) {
            case NORTH ->
                new CannonController.Restraint(70.0F, 290.0F, -360.0F, 360.0F);
            case SOUTH ->
                new CannonController.Restraint(-110.0F, 110.0F, -360.0F, 360.0F);
            case EAST ->
                new CannonController.Restraint(-200.0F, 20.0F, -360.0F, 360.0F);
            case WEST ->
                new CannonController.Restraint(-20.0F, 200.0F, -360.0F, 360.0F);
            case UP ->
                new CannonController.Restraint(-360.0F, 360.0F, -200.0F, 20.0F);
            case DOWN ->
                new CannonController.Restraint(-360.0F, 360.0F, -20.0F, 200.0F);
        };
    }

    public static void onPlayerRotated(double yawAdd, double pitchAdd) {
        float scale = 0.2F;
        yawIncrease = (float) ((double) yawIncrease + yawAdd * (double) scale);
        pitchIncrease = (float) ((double) pitchIncrease + pitchAdd * (double) scale);
        if (yawAdd != 0.0 || pitchAdd != 0.0) {
            needsToUpdateServer = true;
        }
    }

    public static void onKeyPressed(int key, int action, int modifiers) {
        if (action == 1) {
            if (Minecraft.getInstance().options.keyShift.matches(key, action)) {
                turnOff();
            }
            if (Minecraft.getInstance().options.keyJump.matches(key, action)) {
                preferShootingDown = !preferShootingDown;
                needsToUpdateServer = true;
            }
        }
    }

    public static void onPlayerAttack(boolean attack) {
        if (attack && cannon != null && cannon.readyToFire()) {
            ModNetwork.CHANNEL.sendToServer(new ServerBoundSyncCannonPacket(cannon.getYaw(1.0F), cannon.getPitch(1.0F), cannon.getFirePower(), true, cannon.m_58899_()));
        }
    }

    public static void onInputUpdate(Input input) {
        if (firstTick) {
            firstTick = false;
            input.down = false;
            input.jumping = false;
            input.up = false;
            input.left = false;
            input.right = false;
            input.shiftKeyDown = false;
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
        }
    }

    public static void onClientTick(Minecraft mc) {
        if (isActive()) {
            ClientLevel level = mc.level;
            BlockPos pos = cannon.m_58899_();
            Player player = Minecraft.getInstance().player;
            float maxDist = 7.0F;
            if (level.m_7702_(pos) != cannon || cannon.m_58901_() || !(pos.m_203193_(player.m_20182_()) < (double) (maxDist * maxDist))) {
                turnOff();
            } else if (needsToUpdateServer) {
                needsToUpdateServer = false;
                ModNetwork.CHANNEL.sendToServer(new ServerBoundSyncCannonPacket(cannon.getYaw(0.0F), cannon.getPitch(0.0F), cannon.getFirePower(), false, cannon.m_58899_()));
            }
        }
    }

    private static record Restraint(float minYaw, float maxYaw, float minPitch, float maxPitch) {
    }
}