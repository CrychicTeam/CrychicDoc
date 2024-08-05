package journeymap.client.log;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.service.webmap.Webmap;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.version.VersionCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import org.apache.logging.log4j.Level;

public class ChatLog {

    static final List<MutableComponent> announcements = Collections.synchronizedList(new LinkedList());

    public static boolean enableAnnounceMod = false;

    private static boolean initialized = false;

    public static void queueAnnouncement(Component chat) {
        MutableComponent wrap = Component.translatable("jm.common.chat_announcement", chat);
        announcements.add(wrap);
    }

    public static void announceURL(String message, String url) {
        MutableComponent chat = Constants.getStringTextComponent(message);
        chat.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        chat.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Constants.getStringTextComponent(url))));
        queueAnnouncement(chat);
    }

    public static void announceFile(String message, File file) {
        MutableComponent chat = Constants.getStringTextComponent(message);
        try {
            String path = file.getCanonicalPath();
            chat.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path)));
            chat.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Constants.getStringTextComponent(path))));
        } catch (Exception var4) {
            Journeymap.getLogger().warn("Couldn't build ClickEvent for file: " + LogFormatter.toString(var4));
        }
        queueAnnouncement(chat);
    }

    public static void announceI18N(String key, Object... parms) {
        String text = Constants.getString(key, parms);
        MutableComponent chat = Constants.getStringTextComponent(text);
        queueAnnouncement(chat);
    }

    public static void announceError(String text) {
        ChatLog.ErrorChat chat = new ChatLog.ErrorChat(text);
        queueAnnouncement(chat);
    }

    public static void showChatAnnouncements(Minecraft mc) {
        if (!initialized) {
            enableAnnounceMod = JourneymapClient.getInstance().getCoreProperties().announceMod.get();
            announceMod(enableAnnounceMod);
            VersionCheck.getVersionIsCurrent();
            initialized = true;
        }
        while (!announcements.isEmpty()) {
            MutableComponent message = (MutableComponent) announcements.remove(0);
            if (message != null) {
                try {
                    mc.gui.getChat().addMessage(message);
                } catch (Exception var7) {
                    Journeymap.getLogger().error("Could not display announcement in chat: " + LogFormatter.toString(var7));
                } finally {
                    Level logLevel = ((TranslatableContents) message.getContents()).getArgs()[0] instanceof ChatLog.ErrorChat ? Level.ERROR : Level.INFO;
                    Journeymap.getLogger().log(logLevel, StringUtil.stripColor(message.getString()));
                }
            }
        }
    }

    public static void announceMod(boolean forced) {
        if (enableAnnounceMod || forced) {
            String keyName = JourneymapClient.getInstance().getKeyEvents().getHandler().kbFullscreenToggle.m_90863_().getString().toUpperCase();
            if (JourneymapClient.getInstance().getWebMapProperties().enabled.get()) {
                try {
                    Webmap webServer = JourneymapClient.getInstance().getJmServer();
                    String port = webServer.getPort() == 80 ? "" : ":" + webServer.getPort();
                    String message = Constants.getString("jm.common.webserver_and_mapgui_ready", keyName, port);
                    announceURL(message, "http://localhost" + port);
                } catch (Throwable var5) {
                    Journeymap.getLogger().error("Couldn't check webserver: " + LogFormatter.toString(var5));
                }
            } else {
                announceI18N("jm.common.mapgui_only_ready", keyName);
            }
            if (!JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get()) {
                announceI18N("jm.common.enable_mapping_false_text");
            }
            enableAnnounceMod = false;
        }
    }

    private static class ErrorChat implements Component {

        String text;

        public ErrorChat(String text) {
            this.text = text;
        }

        @Override
        public Style getStyle() {
            return Style.EMPTY;
        }

        @Override
        public ComponentContents getContents() {
            return new LiteralContents(this.text);
        }

        @Override
        public List<Component> getSiblings() {
            return Lists.newArrayList();
        }

        @Override
        public FormattedCharSequence getVisualOrderText() {
            return FormattedCharSequence.EMPTY;
        }
    }
}