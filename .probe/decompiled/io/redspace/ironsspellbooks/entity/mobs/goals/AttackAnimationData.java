package io.redspace.ironsspellbooks.entity.mobs.goals;

public class AttackAnimationData {

    public final int lengthInTicks;

    public final String animationId;

    public final int[] attackTimestamps;

    public AttackAnimationData(int lengthInTicks, String animationId, int... attackTimestamps) {
        this.animationId = animationId;
        this.lengthInTicks = lengthInTicks;
        this.attackTimestamps = attackTimestamps;
    }

    public boolean isHitFrame(int tickCount) {
        for (int i : this.attackTimestamps) {
            if (tickCount == this.lengthInTicks - i) {
                return true;
            }
        }
        return false;
    }

    public boolean isSingleHit() {
        return this.attackTimestamps.length == 1;
    }
}