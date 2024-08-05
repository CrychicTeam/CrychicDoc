package dev.xkmc.l2backpack.events;

import dev.xkmc.l2screentracker.click.ReadOnlyStackClickHandler;
import dev.xkmc.l2screentracker.init.L2STLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliItems;

public class PatchouliClickListener extends ReadOnlyStackClickHandler {

    public PatchouliClickListener() {
        super(new ResourceLocation("l2backpack", "patchouli"));
    }

    protected void handle(ServerPlayer player, ItemStack stack) {
        Book book = ItemModBook.getBook(stack);
        if (book != null) {
            PatchouliAPI.get().openBookGUI(player, book.id);
            SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
            player.m_5496_(sfx, 1.0F, (float) (0.7 + Math.random() * 0.4));
        }
    }

    public boolean isAllowed(ItemStack stack) {
        return stack.is(PatchouliItems.BOOK);
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (event.getItemStack().is(PatchouliItems.BOOK)) {
            event.getToolTip().add(L2STLangData.QUICK_ACCESS.get(new Object[0]).withStyle(ChatFormatting.GRAY));
        }
    }
}