package dev.xkmc.l2backpack.init.data;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2screentracker", bus = Bus.MOD)
public enum BackpackKeys {

    OPEN("key.l2backpack.open", 66);

    public final String id;

    public final int key;

    public final KeyMapping map;

    private BackpackKeys(String id, int key) {
        this.id = id;
        this.key = key;
        this.map = new KeyMapping(id, key, "key.categories.l2mods");
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        for (BackpackKeys k : values()) {
            event.register(k.map);
        }
    }
}