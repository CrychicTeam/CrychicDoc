package portb.biggerstacks.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.StackSizeRules;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = { Dist.CLIENT }, modid = "biggerstacks")
public class ClientEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void showExactItemStackCount(ItemTooltipEvent event) {
        if (ClientConfig.enableNumberShortening.get()) {
            ItemStack stack = event.getItemStack();
            if (stack.getCount() > 1000) {
                event.getToolTip().add(1, Component.translatable("biggerstacks.exact.count", Component.literal(Constants.TOOLTIP_NUMBER_FORMAT.format((long) stack.getCount())).withStyle(ClientConfig.getNumberColour())).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @SubscribeEvent
    public static void forgetRuleset(ClientPlayerNetworkEvent.LoggingOut event) {
        if (event.getPlayer() != null) {
            StackSizeRules.setRuleSet(null);
        }
    }
}