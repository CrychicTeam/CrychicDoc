package net.mehvahdjukaar.amendments.common;

import java.util.Random;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SwayingAnimation extends SwingAnimation {

    protected static float maxSwingAngle = 45.0F;

    protected static float minSwingAngle = 2.5F;

    protected static float maxPeriod = 25.0F;

    protected static float angleDamping = 150.0F;

    protected static float periodDamping = 100.0F;

    private int animationCounter = 800 + new Random().nextInt(80);

    private boolean inv = false;

    public SwayingAnimation(Function<BlockState, Vector3f> getRotationAxis) {
        super(getRotationAxis);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        this.animationCounter++;
        double timer = (double) this.animationCounter;
        if ((Boolean) pState.m_61143_(WaterBlock.WATERLOGGED)) {
            timer /= 2.0;
        }
        this.prevAngle = this.angle;
        float a = minSwingAngle;
        float k = 0.01F;
        if (timer < 800.0) {
            a = (float) Math.max((double) maxSwingAngle * Math.exp(-(timer / (double) angleDamping)), (double) minSwingAngle);
            k = (float) Math.max((Math.PI * 2) * (double) ((float) Math.exp(-(timer / (double) periodDamping))), 0.01F);
        }
        this.angle = a * Mth.cos((float) (timer / (double) maxPeriod - (double) k));
        this.angle = this.angle * (this.inv ? -1.0F : 1.0F);
    }

    @Override
    public float getAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevAngle, this.angle);
    }

    @Override
    public void reset() {
        this.animationCounter = 800;
    }

    @Override
    public boolean hitByEntity(Entity entity, BlockState state, BlockPos pos) {
        Vec3 mot = entity.getDeltaMovement();
        if (mot.length() > 0.05) {
            Vec3 norm = new Vec3(mot.x, 0.0, mot.z).normalize();
            Vec3 vec = new Vec3(this.getRotationAxis(state));
            double dot = norm.dot(vec);
            if (dot != 0.0) {
                this.inv = dot < 0.0;
            }
            if (Math.abs(dot) > 0.4) {
                if (this.animationCounter > 10) {
                    Player player = entity instanceof Player p ? p : null;
                    entity.level().playSound(player, pos, state.m_60827_().getHitSound(), SoundSource.BLOCKS, 0.75F, 1.5F);
                }
                this.animationCounter = 0;
            }
        }
        return true;
    }
}