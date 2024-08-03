package se.mickelus.tetra.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.data.DataManager;

@ParametersAreNonnullByDefault
public class ItemUpgradeRegistry {

    private static final Logger logger = LogManager.getLogger();

    public static ItemUpgradeRegistry instance;

    private final List<BiFunction<ItemStack, ItemStack, ItemStack>> replacementHooks;

    private List<ReplacementDefinition> replacementDefinitions;

    public ItemUpgradeRegistry() {
        instance = this;
        this.replacementHooks = new ArrayList();
        this.replacementDefinitions = Collections.emptyList();
        DataManager.instance.replacementData.onReload(() -> this.replacementDefinitions = (List<ReplacementDefinition>) DataManager.instance.replacementData.getData().values().stream().flatMap(Arrays::stream).filter(replacementDefinition -> replacementDefinition.predicate != null).collect(Collectors.toList()));
    }

    public void registerReplacementHook(BiFunction<ItemStack, ItemStack, ItemStack> hook) {
        this.replacementHooks.add(hook);
    }

    public ItemStack getReplacement(ItemStack itemStack) {
        for (ReplacementDefinition replacementDefinition : this.replacementDefinitions) {
            if (replacementDefinition.predicate.matches(itemStack)) {
                ItemStack replacementStack = replacementDefinition.itemStack.copy();
                replacementStack.setDamageValue(itemStack.getDamageValue());
                for (BiFunction<ItemStack, ItemStack, ItemStack> hook : this.replacementHooks) {
                    replacementStack = (ItemStack) hook.apply(itemStack, replacementStack);
                }
                return replacementStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemModule getModule(String key) {
        return ModuleRegistry.instance.getModule(new ResourceLocation("tetra", key));
    }

    public Collection<ItemModule> getAllModules() {
        return ModuleRegistry.instance.getAllModules();
    }
}