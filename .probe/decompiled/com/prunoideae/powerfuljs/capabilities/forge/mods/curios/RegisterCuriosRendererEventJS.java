package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import com.prunoideae.powerfuljs.PowerfulJS;
import com.prunoideae.powerfuljs.proxy.PowerfulJSCommon;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class RegisterCuriosRendererEventJS extends EventJS {

    public void register(Item item, Consumer<RendererCurios.RenderContext> renderer) {
        ((PowerfulJSCommon) PowerfulJS.PROXY.get()).runOnClient(() -> CuriosRendererRegistry.register(item, () -> new RendererCurios(renderer)));
    }
}