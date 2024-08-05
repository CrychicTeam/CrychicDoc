package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public interface RewardTypes {

    Map<ResourceLocation, RewardType> TYPES = new LinkedHashMap();

    RewardType ITEM = register(new ResourceLocation("ftbquests", "item"), ItemReward::new, () -> Icon.getIcon("minecraft:item/diamond"));

    RewardType CHOICE = register(new ResourceLocation("ftbquests", "choice"), ChoiceReward::new, () -> Icons.COLOR_RGB).setExcludeFromListRewards(true);

    RewardType RANDOM = register(new ResourceLocation("ftbquests", "random"), RandomReward::new, () -> Icons.DICE).setExcludeFromListRewards(true);

    RewardType LOOT = register(new ResourceLocation("ftbquests", "loot"), LootReward::new, () -> Icons.MONEY_BAG).setExcludeFromListRewards(true);

    RewardType COMMAND = register(new ResourceLocation("ftbquests", "command"), CommandReward::new, () -> Icon.getIcon("minecraft:block/command_block_back"));

    RewardType CUSTOM = register(new ResourceLocation("ftbquests", "custom"), CustomReward::new, () -> Icons.COLOR_HSB);

    RewardType XP = register(new ResourceLocation("ftbquests", "xp"), XPReward::new, () -> Icon.getIcon("minecraft:item/experience_bottle"));

    RewardType XP_LEVELS = register(new ResourceLocation("ftbquests", "xp_levels"), XPLevelsReward::new, () -> Icon.getIcon("minecraft:item/experience_bottle"));

    RewardType ADVANCEMENT = register(new ResourceLocation("ftbquests", "advancement"), AdvancementReward::new, () -> Icon.getIcon("minecraft:item/wheat"));

    RewardType TOAST = register(new ResourceLocation("ftbquests", "toast"), ToastReward::new, () -> Icon.getIcon("minecraft:item/oak_sign"));

    RewardType STAGE = register(new ResourceLocation("ftbquests", "gamestage"), StageReward::new, () -> Icons.CONTROLLER);

    static RewardType register(ResourceLocation name, RewardType.Provider p, Supplier<Icon> i) {
        return (RewardType) TYPES.computeIfAbsent(name, id -> new RewardType(id, p, i));
    }

    static void init() {
    }
}