package de.keksuccino.fancymenu.customization.placeholder.placeholders.server;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.customization.server.ServerCache;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerVersionPlaceholder extends Placeholder {

    public ServerVersionPlaceholder() {
        super("serverversion");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String ip = (String) dps.values.get("ip");
        if (ip != null) {
            ServerData sd = ServerCache.getServer(ip);
            if (sd != null) {
                if (sd.version != null) {
                    return sd.version.getString();
                }
                return "---";
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("ip");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.serverversion");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.serverversion.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.server");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("ip", "someserver.com:25565");
        return dps;
    }
}