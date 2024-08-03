package dev.ftb.mods.ftbxmodcompat.ftbfiltersystem.kubejs;

import dev.architectury.event.EventResult;
import dev.ftb.mods.ftbfiltersystem.api.event.CustomFilterEvent;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.item.ItemStack;

public class FFSKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        CustomFilterEvent.MATCH_ITEM.register(FFSKubeJSPlugin::onCustomFilter);
    }

    @Override
    public void registerEvents() {
        FFSEvents.EVENT_GROUP.register();
    }

    private static EventResult onCustomFilter(ItemStack stack, String eventId, String extraData) {
        return FFSEvents.CUSTOM_FILTER.post(ScriptType.SERVER, eventId, new CustomFilterEventJS(stack, extraData)).arch();
    }
}