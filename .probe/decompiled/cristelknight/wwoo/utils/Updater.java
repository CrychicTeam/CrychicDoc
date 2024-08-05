package cristelknight.wwoo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cristelknight.wwoo.EEExpectPlatform;
import cristelknight.wwoo.ExpandedEcosphere;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.cristellib.CristelLibExpectPlatform;
import net.cristellib.util.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

public class Updater {

    private Optional<Update> info;

    private final String currentVersion;

    private boolean isBig = false;

    private int newUpdates;

    private static final String s = "[Expanded Ecosphere Updater]";

    public Updater(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void checkForUpdates() {
        try {
            InputStream in = new URL("https://github.com/Cristelknight999/CristelknightUpdateChecker/releases/download/1.0/update.json").openStream();
            label102: {
                label103: {
                    try {
                        String updateIndex;
                        try {
                            updateIndex = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject().get(getReleaseTarget()).getAsString();
                        } catch (NullPointerException var10) {
                            ExpandedEcosphere.LOGGER.warn("[Expanded Ecosphere Updater] This version doesn't have an update index, skipping");
                            this.info = Optional.empty();
                            break label102;
                        }
                        JsonObject object = JsonParser.parseReader(new InputStreamReader(new URL(updateIndex).openStream())).getAsJsonObject();
                        List<Update> newUpdate = new ArrayList();
                        for (JsonElement element : object.getAsJsonArray("versions")) {
                            Update u = (Update) new Gson().fromJson(element, Update.class);
                            boolean isForge = CristelLibExpectPlatform.getPlatform().equals(Platform.FORGE);
                            if ((!isForge || !u.modDownloadFO.isEmpty()) && (isForge || !u.modDownloadFA.isEmpty()) && EEExpectPlatform.isNewer(this.currentVersion, u.semanticVersion)) {
                                newUpdate.add(u);
                                if (u.isBig) {
                                    this.isBig = true;
                                }
                            }
                        }
                        if (newUpdate.isEmpty()) {
                            this.info = Optional.empty();
                            ExpandedEcosphere.LOGGER.info("[Expanded Ecosphere Updater] Found no updates");
                            break label103;
                        }
                        this.newUpdates = newUpdate.size();
                        String isBigString = this.isBig ? " important" : "";
                        ExpandedEcosphere.LOGGER.warn("[Expanded Ecosphere Updater] Found an" + isBigString + " update!");
                        this.info = newUpdate.stream().findFirst();
                    } catch (Throwable var11) {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Throwable var9) {
                                var11.addSuppressed(var9);
                            }
                        }
                        throw var11;
                    }
                    if (in != null) {
                        in.close();
                    }
                    return;
                }
                if (in != null) {
                    in.close();
                }
                return;
            }
            if (in != null) {
                in.close();
            }
            return;
        } catch (FileNotFoundException var12) {
            ExpandedEcosphere.LOGGER.warn("[Expanded Ecosphere Updater] Unable to download " + var12.getMessage());
        } catch (IOException var13) {
            ExpandedEcosphere.LOGGER.warn("[Expanded Ecosphere Updater] Failed to get update info!", var13);
        }
        this.info = Optional.empty();
    }

    public boolean isBig() {
        return this.isBig;
    }

    public Optional<Component> getUpdateMessage() {
        Update update = (Update) this.info.orElse(null);
        if (update == null) {
            return Optional.empty();
        } else {
            String string = this.isBig ? "expanded_ecosphere.config.text.newUpdateBig" : "expanded_ecosphere.config.text.newUpdate";
            Component component1 = Component.translatable("expanded_ecosphere.config.text.newUpdates", this.newUpdates, this.newUpdates > 1 ? Util.translatableText("multiple") : Util.translatableText("null")).withStyle(ChatFormatting.GRAY);
            Component component = Component.translatable(string, Util.translatableText("ch").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.UNDERLINE).withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, update.modDownloadFA))), component1);
            return Optional.of(component);
        }
    }

    public static String getReleaseTarget() {
        return SharedConstants.getCurrentVersion().isStable() ? SharedConstants.getCurrentVersion().getName() : "1.20.2";
    }
}