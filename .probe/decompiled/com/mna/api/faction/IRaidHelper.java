package com.mna.api.faction;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface IRaidHelper {

    boolean spawnRaidAt(Player var1, IFaction var2, int var3, Vec3 var4, boolean var5, MobEffectInstance... var6);
}