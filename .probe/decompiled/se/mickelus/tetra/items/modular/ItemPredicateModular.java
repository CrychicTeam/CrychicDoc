package se.mickelus.tetra.items.modular;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.ImprovementData;

@ParametersAreNonnullByDefault
public class ItemPredicateModular extends ItemPredicate {

    private final Map<String, String> variants = new HashMap();

    private final Map<String, Integer> improvements = new HashMap();

    private String[][] modules = new String[0][0];

    public ItemPredicateModular(String[][] modules) {
        this.modules = modules;
    }

    public ItemPredicateModular(JsonObject jsonObject) {
        if (jsonObject.has("modules")) {
            JsonArray outerModules = jsonObject.getAsJsonArray("modules");
            this.modules = new String[outerModules.size()][];
            for (int i = 0; i < outerModules.size(); i++) {
                JsonArray innerModules = outerModules.get(i).getAsJsonArray();
                this.modules[i] = new String[innerModules.size()];
                for (int j = 0; j < innerModules.size(); j++) {
                    this.modules[i][j] = innerModules.get(j).getAsString();
                }
            }
        }
        if (jsonObject.has("variants")) {
            jsonObject.getAsJsonObject("variants").entrySet().forEach(entry -> this.variants.put((String) entry.getKey(), ((JsonElement) entry.getValue()).getAsString()));
        }
        if (jsonObject.has("improvements")) {
            jsonObject.getAsJsonObject("improvements").entrySet().forEach(entry -> this.improvements.put((String) entry.getKey(), ((JsonElement) entry.getValue()).getAsInt()));
        }
    }

    public boolean test(ItemStack itemStack, String slot) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof IModularItem)) {
            return true;
        } else if (this.modules.length > 0 && !this.hasAnyModule(itemStack, slot)) {
            return false;
        } else {
            return !this.variants.isEmpty() && !this.hasAnyVariant(itemStack, slot) ? false : this.improvements.isEmpty() || this.checkImprovements(itemStack, slot);
        }
    }

    private boolean hasAnyModule(ItemStack itemStack, String slot) {
        IModularItem item = (IModularItem) itemStack.getItem();
        if (slot != null) {
            ItemModule module = item.getModuleFromSlot(itemStack, slot);
            if (module != null) {
                for (String[] outer : this.modules) {
                    if (outer.length == 1 && outer[0].equals(module.getKey())) {
                        return true;
                    }
                }
            }
        } else {
            Collection<ItemModule> itemModules = item.getAllModules(itemStack);
            for (String[] outerx : this.modules) {
                for (int j = 0; j < outerx.length; j++) {
                    int matchCount = 0;
                    for (ItemModule module : itemModules) {
                        if (module.getKey().equals(outerx[j])) {
                            matchCount++;
                            break;
                        }
                    }
                    if (matchCount == outerx.length) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasAnyVariant(ItemStack itemStack, String slot) {
        IModularItem item = (IModularItem) itemStack.getItem();
        for (Entry<String, String> variant : this.variants.entrySet()) {
            String currentSlot = (String) variant.getValue();
            if (slot != null && "#slot".equals(currentSlot)) {
                currentSlot = slot;
            }
            ItemModule module = item.getModuleFromSlot(itemStack, currentSlot);
            if (module != null && ((String) variant.getKey()).equals(module.getVariantData(itemStack).key)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkImprovements(ItemStack itemStack, String slot) {
        IModularItem item = (IModularItem) itemStack.getItem();
        if (slot != null) {
            return (Boolean) CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class).map(module -> {
                if (this.hasDisallowedImprovements(module, itemStack)) {
                    return false;
                } else {
                    return this.improvements.entrySet().stream().allMatch(entry -> ((String) entry.getKey()).startsWith("!")) ? true : this.hasImprovements(module, itemStack);
                }
            }).orElse(false);
        } else {
            boolean hasDisallowed = Arrays.stream(item.getMajorModules(itemStack)).filter(Objects::nonNull).anyMatch(module -> this.hasDisallowedImprovements(module, itemStack));
            if (hasDisallowed) {
                return false;
            } else {
                return this.improvements.entrySet().stream().allMatch(entry -> ((String) entry.getKey()).startsWith("!")) ? true : Arrays.stream(item.getMajorModules(itemStack)).filter(Objects::nonNull).anyMatch(module -> this.hasImprovements(module, itemStack));
            }
        }
    }

    private boolean hasDisallowedImprovements(ItemModuleMajor module, ItemStack itemStack) {
        ImprovementData[] improvementData = module.getImprovements(itemStack);
        return this.improvements.entrySet().stream().filter(entry -> ((String) entry.getKey()).startsWith("!")).anyMatch(entry -> {
            for (ImprovementData data : improvementData) {
                if (((String) entry.getKey()).substring(1).equals(data.key) && ((Integer) entry.getValue() == -1 || (Integer) entry.getValue() == data.level)) {
                    return true;
                }
            }
            return false;
        });
    }

    private boolean hasImprovements(ItemModuleMajor module, ItemStack itemStack) {
        ImprovementData[] improvementData = module.getImprovements(itemStack);
        return this.improvements.entrySet().stream().filter(entry -> !((String) entry.getKey()).startsWith("!")).anyMatch(entry -> {
            for (ImprovementData data : improvementData) {
                if (((String) entry.getKey()).equals(data.key) && ((Integer) entry.getValue() == -1 || (Integer) entry.getValue() == data.level)) {
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return this.test(itemStack, null);
    }
}