package dev.xkmc.modulargolems.content.entity.humanoid.skin;

import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.events.event.HumanoidSkinEvent;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class ClientSkinDispatch {

    @Nullable
    public static SpecialRenderSkin get(HumanoidGolemEntity entity) {
        CurioCompatRegistry curio = CurioCompatRegistry.get();
        if (curio == null) {
            return null;
        } else {
            ItemStack name = curio.getSkin(entity);
            if (name.isEmpty()) {
                return null;
            } else {
                HumanoidSkinEvent event = new HumanoidSkinEvent(entity, name);
                MinecraftForge.EVENT_BUS.post(event);
                return event.getSkin();
            }
        }
    }
}