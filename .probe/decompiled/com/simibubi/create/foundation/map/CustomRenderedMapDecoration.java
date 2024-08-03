package com.simibubi.create.foundation.map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public interface CustomRenderedMapDecoration {

    void render(PoseStack var1, MultiBufferSource var2, boolean var3, int var4, MapItemSavedData var5, int var6);
}