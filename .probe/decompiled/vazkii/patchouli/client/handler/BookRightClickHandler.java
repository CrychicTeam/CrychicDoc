package vazkii.patchouli.client.handler;

import com.mojang.blaze3d.platform.Window;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.ItemStackUtil;

public class BookRightClickHandler {

    public static void onRenderHUD(GuiGraphics graphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ItemStack bookStack = player.m_21205_();
        if (mc.screen == null) {
            Book book = ItemStackUtil.getBookFromStack(bookStack);
            if (book != null) {
                Pair<BookEntry, Integer> hover = getHoveredEntry(book);
                if (hover != null) {
                    BookEntry entry = (BookEntry) hover.getFirst();
                    if (!entry.isLocked()) {
                        Window window = mc.getWindow();
                        int x = window.getGuiScaledWidth() / 2 + 3;
                        int y = window.getGuiScaledHeight() / 2 + 3;
                        entry.getIcon().render(graphics, x, y);
                        graphics.pose().pushPose();
                        graphics.pose().translate(0.0F, 0.0F, 10.0F);
                        graphics.pose().scale(0.5F, 0.5F, 1.0F);
                        graphics.renderItem(bookStack, (x + 8) * 2, (y + 8) * 2);
                        graphics.renderItemDecorations(mc.font, bookStack, (x + 8) * 2, (y + 8) * 2);
                        graphics.pose().popPose();
                        graphics.drawString(mc.font, entry.getName(), x + 18, y + 3, 16777215, false);
                        graphics.pose().pushPose();
                        graphics.pose().scale(0.75F, 0.75F, 1.0F);
                        Component s = Component.translatable("patchouli.gui.lexicon." + (player.m_6144_() ? "view" : "sneak")).withStyle(ChatFormatting.ITALIC);
                        graphics.drawString(mc.font, s, (int) ((float) (x + 18) / 0.75F), (int) ((float) (y + 14) / 0.75F), 12303291, false);
                        graphics.pose().popPose();
                    }
                }
            }
        }
    }

    public static InteractionResult onRightClick(Player player, Level world, InteractionHand hand, BlockHitResult hit) {
        ItemStack bookStack = player.m_21205_();
        if (world.isClientSide && player.m_6144_()) {
            Book book = ItemStackUtil.getBookFromStack(bookStack);
            if (book != null) {
                Pair<BookEntry, Integer> hover = getHoveredEntry(book);
                if (hover != null) {
                    int page = (Integer) hover.getSecond() * 2;
                    book.getContents().setTopEntry(((BookEntry) hover.getFirst()).getId(), page);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    private static Pair<BookEntry, Integer> getHoveredEntry(Book book) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.hitResult instanceof BlockHitResult hit) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = mc.level.m_8055_(pos);
            Block block = state.m_60734_();
            ItemStack picked = block.getCloneItemStack(mc.level, pos, state);
            if (!picked.isEmpty()) {
                return book.getContents().getEntryForStack(picked);
            }
        }
        return null;
    }
}