package org.violetmoon.zeta.client.event.play;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderLiving extends IZetaPlayEvent {

    Entity getEntity();

    PoseStack getPoseStack();

    public interface PostLowest extends ZRenderLiving {
    }

    public interface PreHighest extends ZRenderLiving {
    }
}