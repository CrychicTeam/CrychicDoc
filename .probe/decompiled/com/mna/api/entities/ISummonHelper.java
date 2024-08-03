package com.mna.api.entities;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface ISummonHelper {

    boolean checkIsSummon(Entity var1);

    @Nullable
    LivingEntity resolveSummoner(LivingEntity var1);

    boolean makeSummon(Mob var1, LivingEntity var2, int var3);

    boolean makeSummon(Mob var1, LivingEntity var2, boolean var3, int var4);

    List<Mob> getAllSummons(LivingEntity var1);

    boolean isEntityFriendly(Entity var1, LivingEntity var2);

    int getSummonCap(LivingEntity var1);

    int getEmberCap(LivingEntity var1);

    void setSummonBonus(LivingEntity var1, int var2);
}