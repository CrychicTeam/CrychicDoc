package se.mickelus.tetra.items.modular.impl.bow;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class RangedFOVTransformer {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onFOVUpdate(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (player.m_6117_()) {
            ItemStack itemStack = player.m_21211_();
            CastOptional.cast(itemStack.getItem(), ModularBowItem.class).ifPresent(item -> {
                float progress = item.getProgress(itemStack, player);
                if (progress > 1.0F) {
                    progress = 1.0F;
                } else {
                    progress *= progress;
                }
                event.setNewFovModifier(event.getNewFovModifier() - progress * 0.15F);
            });
        }
        if (player.m_6047_()) {
            ItemStack itemStack = player.m_21205_();
            CastOptional.cast(itemStack.getItem(), ModularBowItem.class).ifPresent(item -> event.setNewFovModifier(event.getNewFovModifier() / this.getZoom(item, itemStack)));
        }
    }

    private float getZoom(IModularItem item, ItemStack itemStack) {
        return Math.max(1.0F, (float) item.getEffectLevel(itemStack, ItemEffect.zoom) / 10.0F);
    }
}