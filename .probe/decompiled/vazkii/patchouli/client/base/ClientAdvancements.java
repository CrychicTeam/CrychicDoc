package vazkii.patchouli.client.base;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.mixin.client.AccessorClientAdvancements;

public class ClientAdvancements {

    private static boolean gotFirstAdvPacket = false;

    public static void onClientPacket() {
        if (!gotFirstAdvPacket) {
            ClientBookRegistry.INSTANCE.reload();
            gotFirstAdvPacket = true;
        } else {
            ClientBookRegistry.INSTANCE.reloadLocks(false);
        }
    }

    public static boolean hasDone(String advancement) {
        ResourceLocation id = ResourceLocation.tryParse(advancement);
        if (id != null) {
            ClientPacketListener conn = Minecraft.getInstance().getConnection();
            if (conn != null) {
                net.minecraft.client.multiplayer.ClientAdvancements cm = conn.getAdvancements();
                Advancement adv = cm.getAdvancements().get(id);
                if (adv != null) {
                    Map<Advancement, AdvancementProgress> progressMap = ((AccessorClientAdvancements) cm).getProgress();
                    AdvancementProgress progress = (AdvancementProgress) progressMap.get(adv);
                    return progress != null && progress.isDone();
                }
            }
        }
        return false;
    }

    public static void playerLogout() {
        gotFirstAdvPacket = false;
    }

    public static void sendBookToast(Book book) {
        ToastComponent gui = Minecraft.getInstance().getToasts();
        if (gui.getToast(ClientAdvancements.LexiconToast.class, book) == null) {
            gui.addToast(new ClientAdvancements.LexiconToast(book));
        }
    }

    public static class LexiconToast implements Toast {

        private final Book book;

        public LexiconToast(Book book) {
            this.book = book;
        }

        @NotNull
        public Book getToken() {
            return this.book;
        }

        @NotNull
        @Override
        public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
            RenderSystem.setShaderTexture(0, f_94893_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(f_94893_, 0, 0, 0, 32, 160, 32);
            Font font = toastGui.getMinecraft().font;
            graphics.drawString(font, Component.translatable(this.book.name), 30, 7, -11534256, false);
            graphics.drawString(font, Component.translatable("patchouli.gui.lexicon.toast.info"), 30, 17, -16777216, false);
            graphics.renderItem(this.book.getBookItem(), 8, 8);
            graphics.renderItemDecorations(font, this.book.getBookItem(), 8, 8);
            return delta >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
    }
}