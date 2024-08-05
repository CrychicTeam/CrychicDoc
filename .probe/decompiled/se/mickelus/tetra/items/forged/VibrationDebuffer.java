package se.mickelus.tetra.items.forged;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class VibrationDebuffer {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.m_9236_().isClientSide && event.player.m_9236_().getGameTime() % 20L == 0L && this.hasApplicableItem(event.player)) {
            event.player.m_7292_(new MobEffectInstance(MobEffects.CONFUSION, 80, 1));
        }
    }

    private boolean hasApplicableItem(Player player) {
        Item mainHandItem = player.m_21205_().getItem();
        Item offHandItem = player.m_21206_().getItem();
        return EarthpiercerItem.instance.equals(mainHandItem) || EarthpiercerItem.instance.equals(offHandItem) || StonecutterItem.instance != null && (StonecutterItem.instance.equals(mainHandItem) || StonecutterItem.instance.equals(offHandItem));
    }
}