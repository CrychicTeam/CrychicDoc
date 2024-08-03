package net.mehvahdjukaar.supplementaries.common.items;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.items.AltimeterItemRenderer;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AltimeterItem extends Item implements ICustomItemRendererProvider {

    public AltimeterItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide && (Boolean) ClientConfigs.Items.DEPTH_METER_CLICK.get()) {
            player.displayClientMessage(Component.translatable("message.supplementaries.altimeter", player.m_20183_().m_123342_()), true);
            player.m_6674_(usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return AltimeterItemRenderer::new;
    }
}