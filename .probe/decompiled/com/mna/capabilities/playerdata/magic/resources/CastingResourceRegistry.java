package com.mna.capabilities.playerdata.magic.resources;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.api.capabilities.resource.ICastingResourceRegistry;
import com.mna.api.events.CastingResourceRegistrationEvent;
import java.security.InvalidParameterException;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class CastingResourceRegistry implements ICastingResourceRegistry {

    private HashMap<ResourceLocation, Class<? extends ICastingResource>> _registry = new HashMap();

    public static final CastingResourceRegistry Instance = new CastingResourceRegistry();

    private void registerDefaults() {
        this.register(CastingResourceIDs.MANA, Mana.class);
        this.register(CastingResourceIDs.SOULS, Souls.class);
        this.register(CastingResourceIDs.COUNCIL_MANA, CouncilMana.class);
        this.register(CastingResourceIDs.BRIMSTONE, Brimstone.class);
        this.register(CastingResourceIDs.SUMMER_FIRE, SummerFire.class);
        this.register(CastingResourceIDs.WINTER_ICE, WinterIce.class);
    }

    @SubscribeEvent
    public static void RequestRegistration(FMLLoadCompleteEvent event) {
        Instance.registerDefaults();
        MinecraftForge.EVENT_BUS.post(new CastingResourceRegistrationEvent(Instance));
        ManaAndArtifice.instance.proxy.sendCastingResourceGuiEvents();
    }

    @Override
    public void register(ResourceLocation identifier, Class<? extends ICastingResource> clazz) {
        if (identifier != null) {
            if (this._registry.containsKey(identifier)) {
                throw new InvalidParameterException("The Casting Resource Identifier " + identifier.toString() + "is already in use.");
            } else {
                this._registry.put(identifier, clazz);
            }
        }
    }

    public Class<? extends ICastingResource> getRegisteredClass(ResourceLocation rLoc) {
        return (Class<? extends ICastingResource>) this._registry.getOrDefault(rLoc, (Class) this._registry.get(CastingResourceIDs.MANA));
    }
}