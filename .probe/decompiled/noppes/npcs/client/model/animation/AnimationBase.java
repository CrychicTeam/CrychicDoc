package noppes.npcs.client.model.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;

public interface AnimationBase {

    void animatePre(float var1, float var2, float var3, float var4, float var5, Entity var6, HumanoidModel var7, int var8);

    void animatePost(float var1, float var2, float var3, float var4, float var5, Entity var6, HumanoidModel var7, int var8);
}