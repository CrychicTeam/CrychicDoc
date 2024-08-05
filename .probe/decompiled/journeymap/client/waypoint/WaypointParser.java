package journeymap.client.waypoint;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.apache.commons.lang3.StringUtils;

public class WaypointParser {

    public static String[] QUOTES = new String[] { "'", "\"" };

    public static Pattern PATTERN = Pattern.compile("(\\w+\\s*:\\s*-?[\\w\\d\\s'\"]+,\\s*)+(\\w+\\s*:\\s*-?[\\w\\d\\s'\"]+)", 2);

    private static Pattern TEXT_BETWEEN_QUOTES = Pattern.compile("\".*?\"|'.*?'|`.*`");

    public static List<String> getWaypointStrings(String line) {
        List<String> list = null;
        String[] candidates = StringUtils.substringsBetween(line, "[", "]");
        if (candidates != null) {
            for (String candidate : candidates) {
                if (PATTERN.matcher(candidate).find() && parse(candidate) != null) {
                    if (list == null) {
                        list = new ArrayList(1);
                    }
                    list.add("[" + candidate + "]");
                }
            }
        }
        return list;
    }

    public static List<Waypoint> getWaypoints(String line) {
        List<Waypoint> list = null;
        String[] candidates = StringUtils.substringsBetween(line, "[", "]");
        if (candidates != null) {
            for (String candidate : candidates) {
                if (PATTERN.matcher(candidate).find()) {
                    Waypoint waypoint = parse(candidate);
                    if (waypoint != null) {
                        if (list == null) {
                            list = new ArrayList(1);
                        }
                        list.add(waypoint);
                    }
                }
            }
        }
        return list;
    }

    public static Waypoint parse(String original) {
        String[] quotedVals = null;
        String raw = original.replaceAll("[\\[\\]]", "");
        for (String quoteChar : QUOTES) {
            if (raw.contains(quoteChar)) {
                quotedVals = StringUtils.substringsBetween(raw, quoteChar, quoteChar);
                if (quotedVals != null) {
                    for (int i = 0; i < quotedVals.length; i++) {
                        raw = raw.replaceAll(TEXT_BETWEEN_QUOTES.pattern(), "__TEMP_" + i);
                    }
                }
            }
        }
        Integer x = null;
        Integer y = 63;
        Integer z = null;
        String dim = null;
        String name = null;
        boolean showDeviation = false;
        for (String part : raw.split(",")) {
            if (part.contains(":")) {
                String[] prop = part.split(":");
                if (prop.length == 2) {
                    String key = prop[0].trim().toLowerCase();
                    String val = prop[1].trim();
                    try {
                        if ("x".equals(key)) {
                            x = Integer.parseInt(val);
                        } else if ("y".equals(key)) {
                            y = Integer.parseInt(val);
                        } else if ("z".equals(key)) {
                            z = Integer.parseInt(val);
                        } else if ("dim".equals(key)) {
                            dim = val;
                        } else if ("name".equals(key)) {
                            name = val;
                        }
                        if ("showDeviation".equals(key)) {
                            showDeviation = Boolean.parseBoolean(val);
                        }
                    } catch (Exception var17) {
                        Journeymap.getLogger().warn("Bad format in waypoint text part: " + part + ": " + var17);
                    }
                }
            }
        }
        if (x != null && z != null) {
            if (name != null && quotedVals != null) {
                for (int i = 0; i < quotedVals.length; i++) {
                    String val = quotedVals[i];
                    name = name.replaceAll("__TEMP_" + i, val);
                }
            }
            if (name == null) {
                name = String.format("%s,%s", x, z);
            }
            Random r = new Random();
            if (dim == null) {
                if (Minecraft.getInstance().level == null) {
                    dim = "minecraft:overwrold";
                } else {
                    dim = DimensionHelper.getDimKeyName(Minecraft.getInstance().level.m_46472_());
                }
            }
            return new Waypoint(name, new BlockPos(x, y, z), new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)), Waypoint.Type.Normal, dim, showDeviation);
        } else {
            return null;
        }
    }

    public static Component parseChatForWaypoints(Component component, String unformattedText) {
        List<String> matches = getWaypointStrings(unformattedText);
        Component response = null;
        if (matches != null) {
            boolean changed = false;
            if (!(component.getContents() instanceof TranslatableContents contents)) {
                if (component instanceof MutableComponent) {
                    response = addWaypointMarkup(component.getString(), matches);
                    if (response != null) {
                        changed = true;
                    }
                } else {
                    Journeymap.getLogger().warn("No implementation for handling waypoints in ITextComponent " + component.getClass());
                }
            } else {
                Object[] formatArgs = contents.getArgs();
                for (int i = 0; i < formatArgs.length && !matches.isEmpty(); i++) {
                    if (formatArgs[i] instanceof Component) {
                        Component arg = (Component) formatArgs[i];
                        Component result = addWaypointMarkup(arg.getString(), matches);
                        if (result != null) {
                            formatArgs[i] = result;
                            changed = true;
                        }
                    } else if (formatArgs[i] instanceof String) {
                        String arg = (String) formatArgs[i];
                        Component result = addWaypointMarkup(arg, matches);
                        if (result != null) {
                            formatArgs[i] = result;
                            changed = true;
                        }
                    }
                }
                if (changed) {
                    response = Component.translatable(contents.getKey(), formatArgs);
                }
            }
            if (!changed) {
                Journeymap.getLogger().warn(String.format("Matched waypoint in chat but failed to update message for %s : %s\n%s", component.getClass(), component.getString(), Component.Serializer.toJson(component)));
            }
        }
        return response;
    }

    private static Component addWaypointMarkup(String text, List<String> matches) {
        List<Component> newParts = new ArrayList();
        int index = 0;
        boolean matched = false;
        Iterator<String> iterator = matches.iterator();
        while (iterator.hasNext()) {
            String match = (String) iterator.next();
            if (text.contains(match)) {
                int start = text.indexOf(match);
                if (start > index) {
                    newParts.add(Constants.getStringTextComponent(text.substring(index, start)));
                }
                matched = true;
                MutableComponent clickable = Constants.getStringTextComponent(match);
                clickable.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jm wpedit " + match)));
                MutableComponent hover = Constants.getStringTextComponent("JourneyMap: ");
                hover.withStyle(style -> style.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)));
                MutableComponent hover2 = Constants.getStringTextComponent("Click to create Waypoint.\nCtrl+Click to view on map.");
                hover2.withStyle(style -> style.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA)));
                hover.append(hover2);
                clickable.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)));
                clickable.withStyle(style -> style.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA)));
                newParts.add(clickable);
                index = start + match.length();
                iterator.remove();
            }
        }
        if (!matched) {
            return null;
        } else {
            if (index < text.length() - 1) {
                newParts.add(Constants.getStringTextComponent(text.substring(index, text.length())));
            }
            if (newParts.isEmpty()) {
                return null;
            } else {
                MutableComponent replacement = Constants.getStringTextComponent("");
                for (Component sib : newParts) {
                    replacement.append(sib);
                }
                return replacement;
            }
        }
    }
}