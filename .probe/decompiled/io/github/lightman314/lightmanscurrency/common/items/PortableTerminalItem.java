package io.github.lightman314.lightmanscurrency.common.items;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.common.menus.providers.TerminalMenuProvider;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.SimpleValidator;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PortableTerminalItem extends TooltipItem {

    public PortableTerminalItem(Item.Properties properties) {
        super(properties.stacksTo(1), LCText.TOOLTIP_TERMINAL.asTooltip());
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (player instanceof ServerPlayer sp) {
            TerminalMenuProvider.OpenMenu(sp, SimpleValidator.NULL);
        }
        return InteractionResultHolder.success(player.m_21120_(hand));
    }
}