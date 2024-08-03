package dev.ftb.mods.ftblibrary.util.client;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.util.CustomComponentParser;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftblibrary.util.TextComponentParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ClientTextComponentUtils {

    private static final Function<String, Component> DEFAULT_STRING_TO_COMPONENT = ClientTextComponentUtils::defaultStringToComponent;

    private static final List<CustomComponentParser> CUSTOM_COMPONENT_PARSERS = new ArrayList();

    public static void addCustomParser(CustomComponentParser function) {
        CUSTOM_COMPONENT_PARSERS.add(function);
    }

    public static Component parse(String s) {
        return TextComponentParser.parse(s, DEFAULT_STRING_TO_COMPONENT);
    }

    private static Component defaultStringToComponent(String s) {
        if (s.isEmpty()) {
            return Component.empty();
        } else {
            if (s.indexOf(58) != -1) {
                Map<String, String> map = StringUtils.splitProperties(s);
                for (CustomComponentParser parser : CUSTOM_COMPONENT_PARSERS) {
                    Component c = parser.parse(s, map);
                    if (c != null && c != Component.f_130760_) {
                        return c;
                    }
                }
                if (map.containsKey("image")) {
                    ImageComponent c = new ImageComponent();
                    c.image = Icon.getIcon((String) map.get("image"));
                    if (map.containsKey("width")) {
                        c.width = Integer.parseInt((String) map.get("width"));
                    }
                    if (map.containsKey("height")) {
                        c.height = Integer.parseInt((String) map.get("height"));
                    }
                    String var6 = ((String) map.getOrDefault("align", "center")).toLowerCase();
                    switch(var6) {
                        case "left":
                            c.align = 0;
                            break;
                        case "center":
                            c.align = 1;
                            break;
                        case "right":
                            c.align = 2;
                    }
                    c.fit = ((String) map.getOrDefault("fit", "false")).equals("true");
                    MutableComponent output = MutableComponent.create(c);
                    if (map.containsKey("text")) {
                        output.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, parse((String) map.get("text")))));
                    }
                    return output;
                }
                if (map.containsKey("open_url")) {
                    return parse((String) map.get("text")).copy().withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, (String) map.get("open_url"))));
                }
            }
            return parse(I18n.get(s));
        }
    }
}