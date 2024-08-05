package harmonised.pmmo.core.nbt;

import com.mojang.brigadier.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class PathReader {

    public static List<String> getNBTValues(String path, CompoundTag nbt) {
        List<String> nodes = parsePath(path);
        return (List<String>) (!nbt.isEmpty() && nbt != null ? evaluateCompound(nodes, nbt) : new ArrayList());
    }

    private static List<String> parsePath(String path) {
        List<String> nodes = new ArrayList();
        StringReader reader = new StringReader(path);
        String element = "";
        while (reader.canRead()) {
            if (reader.peek() == '.') {
                nodes.add(element);
                element = "";
                reader.read();
            } else {
                element = element + reader.read();
            }
        }
        nodes.add(element);
        return nodes;
    }

    private static List<String> evaluateCompound(List<String> nodes, CompoundTag nbt) {
        List<String> list = new ArrayList();
        if (!nbt.isEmpty() && nbt != null) {
            String nodeEntry = (String) nodes.get(0);
            if (!isList(nodeEntry) && isQualifiedNode(nodeEntry) && !isQualifiedCompound(nodeEntry, nbt)) {
                return list;
            } else {
                if (isList(nodeEntry)) {
                    nodes.remove(0);
                    list.addAll(evaluateList(nodes, nodeEntry, (ListTag) nbt.get(rawNode(nodeEntry))));
                } else if (isCompound(nodeEntry)) {
                    nodes.remove(0);
                    list.addAll(evaluateCompound(nodes, nbt.getCompound(rawNode(nodeEntry))));
                } else {
                    Tag value = nbt.get(rawNode(nodeEntry));
                    if (value != null) {
                        list.add(value.getAsString());
                    }
                }
                return list;
            }
        } else {
            return list;
        }
    }

    private static List<String> evaluateList(List<String> nodes, String node, ListTag lnbt) {
        List<String> list = new ArrayList();
        if (lnbt == null) {
            return list;
        } else {
            int index = getListIndex(node);
            if (index == -2) {
                index = getQualifiedIndex(node, lnbt);
            }
            if (index >= -1 && index < lnbt.size()) {
                if (index == -1) {
                    for (int l = 0; l < lnbt.size(); l++) {
                        if (lnbt.get(0) instanceof CompoundTag) {
                            list.addAll(evaluateCompound(new ArrayList(nodes), lnbt.getCompound(l)));
                        } else if (lnbt.get(0) instanceof ListTag) {
                            list.addAll(evaluateList(new ArrayList(nodes), getListParameters((String) nodes.get(0)), lnbt.getList(l)));
                        } else {
                            list.add(lnbt.get(l).getAsString());
                        }
                    }
                } else if (lnbt.get(0) instanceof CompoundTag) {
                    list.addAll(evaluateCompound(nodes, lnbt.getCompound(index)));
                } else if (lnbt.get(0) instanceof ListTag) {
                    list.addAll(evaluateList(nodes, getListParameters((String) nodes.get(0)), lnbt.getList(index)));
                } else {
                    list.add(lnbt.get(index).getAsString());
                }
                return list;
            } else {
                return list;
            }
        }
    }

    private static boolean isList(String node) {
        return node.contains("[");
    }

    private static boolean isCompound(String node) {
        return node.contains("{");
    }

    private static boolean isQualifiedNode(String node) {
        return node.contains(":") && !node.contains("{}");
    }

    private static boolean isQualifiedCompound(String node, CompoundTag nbt) {
        String root = rawNode(node);
        String key = node.substring(node.indexOf("{") + 1, Math.max(0, node.indexOf(":"))).replaceAll("\"", "");
        String value = node.substring(node.indexOf(":") + 1, Math.max(0, node.indexOf("}"))).replaceAll("\"", "");
        return nbt.contains(root) && nbt.getCompound(root).contains(key) && nbt.getCompound(root).get(key).getAsString().equalsIgnoreCase(value);
    }

    private static int getListIndex(String node) {
        String rawIndex = getListParameters(node);
        try {
            return rawIndex.isEmpty() ? -1 : Integer.valueOf(rawIndex);
        } catch (NumberFormatException var3) {
            return -2;
        }
    }

    private static int getQualifiedIndex(String param, ListTag lnbt) {
        if (!isCompound(param)) {
            return -2;
        } else if (!(lnbt.get(0) instanceof CompoundTag)) {
            return -2;
        } else {
            String key = param.substring(param.indexOf("{") + 1, param.indexOf(":"));
            String value = param.substring(param.indexOf(":") + 1, param.indexOf("}"));
            value = rawValue(value);
            for (int i = 0; i < lnbt.size(); i++) {
                CompoundTag element = lnbt.getCompound(i);
                if (element.contains(key) && element.get(key).getAsString().equalsIgnoreCase(value)) {
                    return i;
                }
            }
            return -2;
        }
    }

    private static String getListParameters(String node) {
        if (isList(node)) {
            int beginIndex = node.indexOf("[") + 1;
            int endIndex = node.indexOf("]");
            return node.substring(beginIndex, endIndex);
        } else {
            return "";
        }
    }

    private static String rawNode(String node) {
        if (isList(node)) {
            return node.substring(0, node.indexOf("["));
        } else {
            return isCompound(node) ? node.substring(0, node.indexOf("{")) : node;
        }
    }

    private static String rawValue(String val) {
        int firstIndex = val.indexOf("\"");
        return val.substring(firstIndex + 1, val.indexOf("\"", firstIndex + 1));
    }
}