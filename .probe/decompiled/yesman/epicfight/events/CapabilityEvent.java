package yesman.epicfight.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.provider.EntityPatchProvider;
import yesman.epicfight.world.capabilities.provider.ItemCapabilityProvider;
import yesman.epicfight.world.capabilities.provider.SkillCapabilityProvider;

@EventBusSubscriber(modid = "epicfight")
public class CapabilityEvent {

    @SubscribeEvent
    public static void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject() != null) {
            ItemCapabilityProvider prov = new ItemCapabilityProvider(event.getObject());
            if (prov.hasCapability()) {
                event.addCapability(new ResourceLocation("epicfight", "item_cap"), prov);
            }
        }
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        EntityPatch oldEntitypatch = EpicFightCapabilities.getEntityPatch(event.getObject(), EntityPatch.class);
        if (oldEntitypatch == null) {
            EntityPatchProvider prov = new EntityPatchProvider(event.getObject());
            if (prov.hasCapability()) {
                EntityPatch entitypatch = prov.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
                entitypatch.onConstructed(event.getObject());
                event.addCapability(new ResourceLocation("epicfight", "entity_cap"), prov);
                if (entitypatch instanceof PlayerPatch<?> playerpatch && event.getObject().getCapability(EpicFightCapabilities.CAPABILITY_SKILL).orElse(null) == null && playerpatch != null) {
                    SkillCapabilityProvider skillProvider = new SkillCapabilityProvider(playerpatch);
                    event.addCapability(new ResourceLocation("epicfight", "skill_cap"), skillProvider);
                }
            }
        }
    }
}