package org.violetmoon.zetaimplforge.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import org.violetmoon.zeta.client.HumanoidArmorModelGetter;

public interface IZetaForgeItemStuff {

    void zeta$setBlockEntityWithoutLevelRenderer(BlockEntityWithoutLevelRenderer var1);

    void zeta$setHumanoidArmorModel(HumanoidArmorModelGetter var1);
}