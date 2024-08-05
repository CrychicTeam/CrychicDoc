package net.zanckor.questapi.api.file.quest.abstracquest;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;

public abstract class AbstractTargetType {

    public abstract MutableComponent handler(String var1, UserGoal var2, Player var3, ChatFormatting var4, ChatFormatting var5);

    public abstract String target(String var1);

    public abstract void renderTarget(PoseStack var1, int var2, int var3, double var4, double var6, UserGoal var8, String var9);
}