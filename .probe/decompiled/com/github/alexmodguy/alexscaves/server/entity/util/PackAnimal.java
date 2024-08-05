package com.github.alexmodguy.alexscaves.server.entity.util;

import net.minecraft.world.entity.LivingEntity;

public interface PackAnimal {

    default boolean isPackFollower() {
        return this.getPriorPackMember() != null;
    }

    default boolean hasPackFollower() {
        return this.getAfterPackMember() != null;
    }

    default PackAnimal getPackLeader() {
        PackAnimal leader = this;
        while (leader.getPriorPackMember() != null && leader.getPriorPackMember() != this) {
            leader = leader.getPriorPackMember();
        }
        return leader;
    }

    default int getPackSize() {
        PackAnimal leader = this.getPackLeader();
        int i;
        for (i = 1; leader.getAfterPackMember() != null; i++) {
            leader = leader.getAfterPackMember();
        }
        return i;
    }

    default boolean isInPack(PackAnimal packAnimal) {
        for (PackAnimal leader = this.getPackLeader(); leader.getAfterPackMember() != null; leader = leader.getAfterPackMember()) {
            if (packAnimal.equals(leader)) {
                return true;
            }
        }
        return false;
    }

    default boolean isValidLeader(PackAnimal packLeader) {
        return !packLeader.isPackFollower() && ((LivingEntity) packLeader).isAlive();
    }

    PackAnimal getPriorPackMember();

    PackAnimal getAfterPackMember();

    void setPriorPackMember(PackAnimal var1);

    void setAfterPackMember(PackAnimal var1);

    default void joinPackOf(PackAnimal caravanHeadIn) {
        this.setPriorPackMember(caravanHeadIn);
        caravanHeadIn.setAfterPackMember(this);
        this.resetPackFlags();
    }

    default void leavePack() {
        if (this.getPriorPackMember() != null) {
            this.getPriorPackMember().setAfterPackMember(null);
        }
        this.setPriorPackMember(null);
        this.resetPackFlags();
    }

    default void resetPackFlags() {
    }
}