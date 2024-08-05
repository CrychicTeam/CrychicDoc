package org.violetmoon.quark.content.mobs.ai;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.base.util.MutableVectorHolder;
import org.violetmoon.quark.content.mobs.entity.Stoneling;

public class ActWaryGoal extends WaterAvoidingRandomStrollGoal {

    private final Stoneling stoneling;

    private final BooleanSupplier scaredBySuddenMovement;

    private final double range;

    private boolean startled;

    private final Map<Player, MutableVectorHolder> lastPositions = new WeakHashMap();

    private final Map<Player, MutableVectorHolder> lastSpeeds = new WeakHashMap();

    public ActWaryGoal(Stoneling stoneling, double speed, double range, BooleanSupplier scaredBySuddenMovement) {
        super(stoneling, speed, 1.0F);
        this.stoneling = stoneling;
        this.range = range;
        this.scaredBySuddenMovement = scaredBySuddenMovement;
    }

    private static void updateMotion(MutableVectorHolder holder, double x, double y, double z) {
        holder.x = x;
        holder.y = y;
        holder.z = z;
    }

    private static void updatePos(MutableVectorHolder holder, Entity entity) {
        Vec3 pos = entity.position();
        holder.x = pos.x;
        holder.y = pos.y;
        holder.z = pos.z;
    }

    private static MutableVectorHolder initPos(Player p) {
        MutableVectorHolder holder = new MutableVectorHolder();
        updatePos(holder, p);
        return holder;
    }

    public void startle() {
        this.startled = true;
    }

    public boolean isStartled() {
        return this.startled;
    }

    protected boolean shouldApplyPath() {
        return super.m_8036_();
    }

    @Override
    public void tick() {
        if (this.stoneling.m_21573_().isDone() && this.shouldApplyPath()) {
            this.m_8056_();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.stoneling.m_21573_().stop();
    }

    @Override
    public boolean canUse() {
        if (!this.startled && !this.stoneling.isPlayerMade()) {
            List<Player> playersAround = this.stoneling.m_9236_().m_6443_(Player.class, this.stoneling.m_20191_().inflate(this.range), playerx -> playerx != null && !playerx.getAbilities().instabuild && playerx.m_20280_(this.stoneling) < this.range * this.range);
            if (playersAround.isEmpty()) {
                return false;
            } else {
                for (Player player : playersAround) {
                    if (!player.m_20163_()) {
                        this.startled = true;
                        return false;
                    }
                    if (this.scaredBySuddenMovement.getAsBoolean()) {
                        MutableVectorHolder lastSpeed = (MutableVectorHolder) this.lastSpeeds.computeIfAbsent(player, p -> new MutableVectorHolder());
                        MutableVectorHolder lastPos = (MutableVectorHolder) this.lastPositions.computeIfAbsent(player, ActWaryGoal::initPos);
                        Vec3 pos = player.m_20182_();
                        double dX = pos.x - lastPos.x;
                        double dY = pos.y - lastPos.y;
                        double dZ = pos.z - lastPos.z;
                        double xDisplacement = dX - lastSpeed.x;
                        double yDisplacement = dY - lastSpeed.y;
                        double zDisplacement = dZ - lastSpeed.z;
                        updateMotion(lastSpeed, dX, dY, dZ);
                        updatePos(lastPos, player);
                        double displacementSq = xDisplacement * xDisplacement + yDisplacement * yDisplacement + zDisplacement * zDisplacement;
                        if (displacementSq < 0.01) {
                            return true;
                        }
                        this.startled = true;
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}