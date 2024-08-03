package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.content.entity.humanoid.skin.ClientProfileManager;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.SpecialRenderProfile;
import dev.xkmc.modulargolems.events.event.HumanoidSkinEvent;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "modulargolems", bus = Bus.FORGE)
public class GolemClientEventListeners {

    @SubscribeEvent
    public static void onHumanoidSkin(HumanoidSkinEvent event) {
        if (event.getStack().is(Items.PLAYER_HEAD)) {
            String name = event.getStack().getHoverName().getString();
            if (ResourceLocation.isValidResourceLocation(name)) {
                event.setSkin(new SpecialRenderProfile(true, new ResourceLocation(name)));
            }
        }
        if (event.getStack().is(Items.PIGLIN_HEAD)) {
            String name = event.getStack().getHoverName().getString();
            if (ResourceLocation.isValidResourceLocation(name)) {
                event.setSkin(new SpecialRenderProfile(false, new ResourceLocation(name)));
            }
        }
        if (event.getStack().is(MGTagGen.PLAYER_SKIN)) {
            event.setSkin(ClientProfileManager.get(event.getStack().getHoverName().getString()));
        }
    }
}