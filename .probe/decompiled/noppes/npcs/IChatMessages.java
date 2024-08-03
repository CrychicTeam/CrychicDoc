package noppes.npcs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import noppes.npcs.entity.EntityNPCInterface;

public interface IChatMessages {

    void addMessage(String var1, EntityNPCInterface var2);

    void renderMessages(PoseStack var1, MultiBufferSource var2, float var3, boolean var4, int var5);
}