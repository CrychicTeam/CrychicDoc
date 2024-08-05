package me.jellysquid.mods.sodium.client.gui.console;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.console.message.Message;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.util.ColorU8;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ConsoleRenderer {

    static final ConsoleRenderer INSTANCE = new ConsoleRenderer();

    private final LinkedList<ConsoleRenderer.ActiveMessage> activeMessages = new LinkedList();

    private static final EnumMap<MessageLevel, ConsoleRenderer.ColorPalette> COLORS = new EnumMap(MessageLevel.class);

    public void update(Console console, double currentTime) {
        this.purgeMessages(currentTime);
        this.pollMessages(console, currentTime);
    }

    private void purgeMessages(double currentTime) {
        this.activeMessages.removeIf(message -> currentTime > message.timestamp() + message.duration());
    }

    private void pollMessages(Console console, double currentTime) {
        Deque<Message> log = console.getMessageDrain();
        while (!log.isEmpty()) {
            this.activeMessages.add(ConsoleRenderer.ActiveMessage.create((Message) log.poll(), currentTime));
        }
    }

    public void draw(GuiGraphics context) {
        double currentTime = GLFW.glfwGetTime();
        Minecraft client = Minecraft.getInstance();
        PoseStack matrices = context.pose();
        matrices.pushPose();
        matrices.translate(0.0F, 0.0F, 1000.0F);
        int paddingWidth = 3;
        int paddingHeight = 1;
        ArrayList<ConsoleRenderer.MessageRender> renders = new ArrayList();
        int x = 4;
        int y = 4;
        for (ConsoleRenderer.ActiveMessage message : this.activeMessages) {
            double opacity = getMessageOpacity(message, currentTime);
            if (!(opacity < 0.025)) {
                List<FormattedCharSequence> lines = new ArrayList();
                int messageWidth = 270;
                StringSplitter textHandler = client.font.getSplitter();
                textHandler.splitLines(message.text(), messageWidth - 20, Style.EMPTY, (text, lastLineWrapped) -> lines.add(Language.getInstance().getVisualOrder(text)));
                int messageHeight = 9 * lines.size() + paddingHeight * 2;
                renders.add(new ConsoleRenderer.MessageRender(x, y, messageWidth, messageHeight, message.level(), lines, opacity));
                y += messageHeight;
            }
        }
        double mouseX = client.mouseHandler.xpos() / client.getWindow().getGuiScale();
        double mouseY = client.mouseHandler.ypos() / client.getWindow().getGuiScale();
        boolean hovered = false;
        for (ConsoleRenderer.MessageRender render : renders) {
            if (mouseX >= (double) render.x && mouseX < (double) (render.x + render.width) && mouseY >= (double) render.y && mouseY < (double) (render.y + render.height)) {
                hovered = true;
                break;
            }
        }
        for (ConsoleRenderer.MessageRender renderx : renders) {
            int xx = renderx.x();
            int yx = renderx.y();
            int width = renderx.width();
            int height = renderx.height();
            ConsoleRenderer.ColorPalette colors = (ConsoleRenderer.ColorPalette) COLORS.get(renderx.level());
            double opacity = renderx.opacity();
            if (hovered) {
                opacity *= 0.4;
            }
            context.fill(xx, yx, xx + width, yx + height, ColorARGB.withAlpha(colors.background(), weightAlpha(opacity)));
            context.fill(xx, yx, xx + 1, yx + height, ColorARGB.withAlpha(colors.foreground(), weightAlpha(opacity)));
            for (FormattedCharSequence line : renderx.lines()) {
                context.drawString(client.font, line, xx + paddingWidth + 3, yx + paddingHeight, ColorARGB.withAlpha(colors.text(), weightAlpha(opacity)), false);
                yx += 9;
            }
        }
        matrices.popPose();
    }

    private static double getMessageOpacity(ConsoleRenderer.ActiveMessage message, double time) {
        double midpoint = message.timestamp() + message.duration() / 2.0;
        if (time > midpoint) {
            return getFadeOutOpacity(message, time);
        } else {
            return time < midpoint ? getFadeInOpacity(message, time) : 1.0;
        }
    }

    private static double getFadeInOpacity(ConsoleRenderer.ActiveMessage message, double time) {
        double animationDuration = 0.25;
        double animationStart = message.timestamp();
        double animationEnd = message.timestamp() + animationDuration;
        return getAnimationProgress(time, animationStart, animationEnd);
    }

    private static double getFadeOutOpacity(ConsoleRenderer.ActiveMessage message, double time) {
        double animationDuration = Math.min(0.5, message.duration() * 0.2);
        double animationStart = message.timestamp() + message.duration() - animationDuration;
        double animationEnd = message.timestamp() + message.duration();
        return 1.0 - getAnimationProgress(time, animationStart, animationEnd);
    }

    private static double getAnimationProgress(double currentTime, double startTime, double endTime) {
        return Mth.clamp(Mth.inverseLerp(currentTime, startTime, endTime), 0.0, 1.0);
    }

    private static int weightAlpha(double scale) {
        return ColorU8.normalizedFloatToByte((float) scale);
    }

    static {
        COLORS.put(MessageLevel.INFO, new ConsoleRenderer.ColorPalette(ColorARGB.pack(255, 255, 255), ColorARGB.pack(15, 15, 15), ColorARGB.pack(15, 15, 15)));
        COLORS.put(MessageLevel.WARN, new ConsoleRenderer.ColorPalette(ColorARGB.pack(224, 187, 0), ColorARGB.pack(25, 21, 0), ColorARGB.pack(180, 150, 0)));
        COLORS.put(MessageLevel.SEVERE, new ConsoleRenderer.ColorPalette(ColorARGB.pack(220, 0, 0), ColorARGB.pack(25, 0, 0), ColorARGB.pack(160, 0, 0)));
    }

    private static record ActiveMessage(MessageLevel level, Component text, double duration, double timestamp) {

        public static ConsoleRenderer.ActiveMessage create(Message message, double timestamp) {
            MutableComponent text = message.text().copy().withStyle(style -> style.withFont(Minecraft.UNIFORM_FONT));
            return new ConsoleRenderer.ActiveMessage(message.level(), text, message.duration(), timestamp);
        }
    }

    private static record ColorPalette(int text, int background, int foreground) {
    }

    private static record MessageRender(int x, int y, int width, int height, MessageLevel level, List<FormattedCharSequence> lines, double opacity) {
    }
}