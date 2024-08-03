package vazkii.patchouli.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.ItemStackUtil;

public class TooltipHandler {

    private static float lexiconLookupTime = 0.0F;

    public static void onTooltip(GuiGraphics graphics, ItemStack stack, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        int tooltipY = mouseY - 4;
        if (mc.player != null && !(mc.screen instanceof GuiBook)) {
            int lexSlot = -1;
            ItemStack lexiconStack = ItemStack.EMPTY;
            Pair<BookEntry, Integer> lexiconEntry = null;
            for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                ItemStack stackAt = mc.player.m_150109_().getItem(i);
                if (!stackAt.isEmpty()) {
                    Book book = ItemStackUtil.getBookFromStack(stackAt);
                    if (book != null) {
                        Pair<BookEntry, Integer> entry = book.getContents().getEntryForStack(stack);
                        if (entry != null && !((BookEntry) entry.getFirst()).isLocked()) {
                            lexiconStack = stackAt;
                            lexSlot = i;
                            lexiconEntry = entry;
                            break;
                        }
                    }
                }
            }
            if (lexSlot > -1) {
                int x = mouseX - 34;
                RenderSystem.disableDepthTest();
                graphics.fill(x - 4, tooltipY - 4, x + 20, tooltipY + 26, 1140850688);
                graphics.fill(x - 6, tooltipY - 6, x + 22, tooltipY + 28, 1140850688);
                if (PatchouliConfig.get().useShiftForQuickLookup() ? Screen.hasShiftDown() : Screen.hasControlDown()) {
                    lexiconLookupTime = lexiconLookupTime + ClientTicker.delta;
                    int cx = x + 8;
                    int cy = tooltipY + 8;
                    float r = 12.0F;
                    float requiredTime = (float) PatchouliConfig.get().quickLookupTime();
                    float angles = lexiconLookupTime / requiredTime * 360.0F;
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(770, 771);
                    BufferBuilder buf = Tesselator.getInstance().getBuilder();
                    buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    float a = 0.5F + 0.2F * ((float) Math.cos((double) (ClientTicker.total / 10.0F)) * 0.5F + 0.5F);
                    buf.m_5483_((double) cx, (double) cy, 0.0).color(0.0F, 0.5F, 0.0F, a).endVertex();
                    for (float ix = angles; ix > 0.0F; ix--) {
                        double rad = (double) ((ix - 90.0F) / 180.0F) * Math.PI;
                        buf.m_5483_((double) cx + Math.cos(rad) * (double) r, (double) cy + Math.sin(rad) * (double) r, 0.0).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
                    }
                    buf.m_5483_((double) cx, (double) cy, 0.0).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    Tesselator.getInstance().end();
                    RenderSystem.disableBlend();
                    if (lexiconLookupTime >= requiredTime) {
                        mc.player.m_150109_().selected = lexSlot;
                        int spread = (Integer) lexiconEntry.getSecond();
                        ClientBookRegistry.INSTANCE.displayBookGui(((BookEntry) lexiconEntry.getFirst()).getBook().id, ((BookEntry) lexiconEntry.getFirst()).getId(), spread * 2);
                    }
                } else {
                    lexiconLookupTime = 0.0F;
                }
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 300.0F);
                graphics.renderItem(lexiconStack, x, tooltipY);
                graphics.renderItemDecorations(mc.font, lexiconStack, x, tooltipY);
                graphics.pose().popPose();
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 500.0F);
                graphics.drawString(mc.font, "?", x + 10, tooltipY + 8, -1, true);
                graphics.pose().scale(0.5F, 0.5F, 1.0F);
                boolean mac = Minecraft.ON_OSX;
                Component key = Component.literal(PatchouliConfig.get().useShiftForQuickLookup() ? "Shift" : (mac ? "Cmd" : "Ctrl")).withStyle(ChatFormatting.BOLD);
                graphics.drawString(mc.font, key, (x + 10) * 2 - 16, (tooltipY + 8) * 2 + 20, -1, true);
                graphics.pose().popPose();
                RenderSystem.enableDepthTest();
            } else {
                lexiconLookupTime = 0.0F;
            }
        } else {
            lexiconLookupTime = 0.0F;
        }
    }
}