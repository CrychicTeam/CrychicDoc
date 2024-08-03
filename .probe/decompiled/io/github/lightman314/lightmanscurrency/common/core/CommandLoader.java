package io.github.lightman314.lightmanscurrency.common.core;

import io.github.lightman314.lightmanscurrency.common.commands.CommandBalTop;
import io.github.lightman314.lightmanscurrency.common.commands.CommandBank;
import io.github.lightman314.lightmanscurrency.common.commands.CommandConfig;
import io.github.lightman314.lightmanscurrency.common.commands.CommandLCAdmin;
import io.github.lightman314.lightmanscurrency.common.commands.CommandPlayerTrading;
import io.github.lightman314.lightmanscurrency.common.commands.CommandTicket;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommandLoader {

    @SubscribeEvent
    public static void onCommandLoading(RegisterCommandsEvent event) {
        CommandLCAdmin.register(event.getDispatcher(), event.getBuildContext());
        CommandBalTop.register(event.getDispatcher());
        CommandPlayerTrading.register(event.getDispatcher());
        CommandTicket.register(event.getDispatcher());
        CommandBank.register(event.getDispatcher(), event.getBuildContext());
        CommandConfig.register(event.getDispatcher());
    }
}