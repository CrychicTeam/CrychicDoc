package top.theillusivec4.curios.common.slottype;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraftforge.fml.InterModComms.IMCMessage;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class LegacySlotManager {

    private static final Map<String, SlotType.Builder> IMC_BUILDERS = new HashMap();

    private static final Map<String, Set<String>> IDS_TO_MODS = new HashMap();

    public static Map<String, Set<String>> getIdsToMods() {
        return ImmutableMap.copyOf(IDS_TO_MODS);
    }

    public static Map<String, SlotType.Builder> getImcBuilders() {
        return ImmutableMap.copyOf(IMC_BUILDERS);
    }

    public static void buildImcSlotTypes(Stream<IMCMessage> register, Stream<IMCMessage> modify) {
        IMC_BUILDERS.clear();
        processImc(register, true);
        processImc(modify, false);
    }

    private static void processImc(Stream<IMCMessage> messages, boolean create) {
        TreeMap<String, List<SlotTypeMessage>> messageMap = new TreeMap();
        List<IMCMessage> messageList = messages.toList();
        messageList.forEach(msgx -> {
            Object messageObject = msgx.messageSupplier().get();
            if (messageObject instanceof SlotTypeMessage) {
                ((List) messageMap.computeIfAbsent(msgx.senderModId(), k -> new ArrayList())).add((SlotTypeMessage) messageObject);
            } else if (messageObject instanceof Iterable<?> iterable) {
                Iterator<?> iter = iterable.iterator();
                if (iter.hasNext()) {
                    Object firstChild = iter.next();
                    if (firstChild instanceof SlotTypeMessage) {
                        ((List) messageMap.computeIfAbsent(msgx.senderModId(), k -> new ArrayList())).add((SlotTypeMessage) firstChild);
                        iter.forEachRemaining(child -> ((List) messageMap.computeIfAbsent(msgx.senderModId(), k -> new ArrayList())).add((SlotTypeMessage) child));
                    }
                }
            }
        });
        for (Entry<String, List<SlotTypeMessage>> entry : messageMap.entrySet()) {
            String modId = (String) entry.getKey();
            for (SlotTypeMessage msg : (List) entry.getValue()) {
                String id = msg.getIdentifier();
                SlotType.Builder builder = (SlotType.Builder) IMC_BUILDERS.get(id);
                if (builder == null && create) {
                    builder = new SlotType.Builder(id);
                    IMC_BUILDERS.put(id, builder);
                    ((Set) IDS_TO_MODS.computeIfAbsent(id, k -> new HashSet())).add(modId);
                }
                if (builder != null) {
                    builder.size(msg.getSize()).useNativeGui(msg.isVisible()).hasCosmetic(msg.hasCosmetic());
                    SlotTypeMessage.Builder preset = (SlotTypeMessage.Builder) SlotTypePreset.findPreset(id).map(SlotTypePreset::getMessageBuilder).orElse(null);
                    SlotTypeMessage presetMsg = preset != null ? preset.build() : null;
                    if (msg.getIcon() == null && presetMsg != null && presetMsg.getIcon() != null) {
                        builder.icon(presetMsg.getIcon());
                    } else if (msg.getIcon() != null) {
                        builder.icon(msg.getIcon());
                    }
                    if (msg.getPriority() == null && presetMsg != null && presetMsg.getPriority() != null) {
                        builder.order(presetMsg.getPriority());
                    } else if (msg.getPriority() != null) {
                        builder.order(msg.getPriority());
                    }
                }
            }
        }
    }
}