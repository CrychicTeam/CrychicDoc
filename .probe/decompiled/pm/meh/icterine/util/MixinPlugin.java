package pm.meh.icterine.util;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import pm.meh.icterine.Common;

public class MixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, Boolean> CONDITIONS = ImmutableMap.of("pm.meh.icterine.mixin.AbstractContainerMenuMixin", Common.config.INITIALIZE_INVENTORY_LAST_SLOTS, "pm.meh.icterine.mixin.InventoryChangeTriggerInstanceMixin", Common.config.OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER || Common.config.CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH, "pm.meh.icterine.mixin.InventoryChangeTriggerMixin", Common.config.IGNORE_TRIGGERS_FOR_EMPTIED_STACKS || Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS || Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS, "pm.meh.icterine.mixin.ItemStackMixin", Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS || Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS, "pm.meh.icterine.mixin.AbstractContainerMenuMixinPlatform", Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS, "pm.meh.icterine.mixin.ItemPredicateMixin", Common.config.CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH);

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean status = (Boolean) CONDITIONS.getOrDefault(mixinClassName, true);
        LogHelper.debug((Supplier<String>) (() -> "Apply mixin %s: %s".formatted(mixinClassName, status)));
        return status;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}