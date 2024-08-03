package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.advancement.EatFoodTrigger;
import com.sihenzhang.crockpot.advancement.PiglinBarteringTrigger;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class CrockPotAdvancementProvider extends ForgeAdvancementProvider {

    public CrockPotAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, existingFileHelper, List.of(new CrockPotAdvancementProvider.GenImpl()));
    }

    protected static Component getTranslatableAdvancementTitle(String name) {
        return I18nUtils.createComponent("advancement", name);
    }

    protected static Component getTranslatableAdvancementDescription(String name) {
        return I18nUtils.createComponent("advancement", name + ".desc");
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItem, MinMaxBounds.Ints pCount) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItem).withCount(pCount).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(pPredicates);
    }

    protected static ConsumeItemTrigger.TriggerInstance use(ItemLike pItemLike) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static ConsumeItemTrigger.TriggerInstance use(TagKey<Item> pTag) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static ConsumeItemTrigger.TriggerInstance consumeTrigger(ItemPredicate pPredicate) {
        return ConsumeItemTrigger.TriggerInstance.usedItem(pPredicate);
    }

    protected static EatFoodTrigger.Instance eat(ItemLike pItemLike, MinMaxBounds.Ints pCount) {
        return eatFoodTrigger(ItemPredicate.Builder.item().of(pItemLike).build(), pCount);
    }

    protected static EatFoodTrigger.Instance eatFoodTrigger(ItemPredicate pPredicate, MinMaxBounds.Ints pCount) {
        return new EatFoodTrigger.Instance(ContextAwarePredicate.ANY, pPredicate, pCount);
    }

    protected static String getItemName(ItemLike pItemLike) {
        return ForgeRegistries.ITEMS.getKey(pItemLike.asItem()).getPath();
    }

    protected static String getSimpleAdvancementName(String name) {
        return "crockpot:" + name;
    }

    public static final class GenImpl implements ForgeAdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
            Advancement root = Advancement.Builder.advancement().display(CrockPotItems.CROCK_POT.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("root"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("root"), RLUtils.createRL("textures/gui/advancements/background.png"), FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.CROCK_POT.get()), CrockPotAdvancementProvider.has(CrockPotItems.CROCK_POT.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("root"));
            Advancement.Builder.advancement().parent(root).display(CrockPotItems.CANDY.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("candy"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("candy"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.CANDY.get()), CrockPotAdvancementProvider.use(CrockPotItems.CANDY.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("candy"));
            Advancement.Builder.advancement().parent(root).display(CrockPotItems.MEAT_BALLS.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("meat_balls"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("meat_balls"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.MEAT_BALLS.get()), CrockPotAdvancementProvider.eat(CrockPotItems.MEAT_BALLS.get(), MinMaxBounds.Ints.atLeast(40))).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("meat_balls"));
            Advancement.Builder.advancement().parent(root).display(CrockPotItems.MILK_BOTTLE.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("milk_bottle"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("milk_bottle"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.MILK_BOTTLE.get()), CrockPotAdvancementProvider.has(CrockPotItems.MILK_BOTTLE.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("milk_bottle"));
            Advancement.Builder.advancement().parent(root).display(CrockPotItems.SYRUP.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("syrup"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("syrup"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.SYRUP.get()), CrockPotAdvancementProvider.has(CrockPotItems.SYRUP.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("syrup"));
            Advancement.Builder.advancement().parent(root).display(CrockPotItems.WET_GOOP.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("wet_goop"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("wet_goop"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.WET_GOOP.get()), CrockPotAdvancementProvider.has(CrockPotItems.WET_GOOP.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("wet_goop"));
            Advancement advancedPot = Advancement.Builder.advancement().parent(root).display(CrockPotItems.PORTABLE_CROCK_POT.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("upgrade_pot"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("upgrade_pot"), null, FrameType.TASK, true, true, false).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.PORTABLE_CROCK_POT.get()), CrockPotAdvancementProvider.has(CrockPotItems.PORTABLE_CROCK_POT.get())).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("upgrade_pot"));
            Advancement.Builder.advancement().parent(advancedPot).display(CrockPotItems.AVAJ.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("avaj"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("avaj"), null, FrameType.CHALLENGE, true, true, true).addCriterion(CrockPotAdvancementProvider.getItemName(CrockPotItems.AVAJ.get()), CrockPotAdvancementProvider.has(CrockPotItems.AVAJ.get())).rewards(AdvancementRewards.Builder.experience(50)).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("avaj"));
            ContextAwarePredicate adultPiglin = ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build())).build());
            Advancement piglinBartering = Advancement.Builder.advancement().parent(advancedPot).display(CrockPotItems.NETHEROSIA.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("piglin_bartering"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("piglin_bartering"), null, FrameType.TASK, true, true, false).addCriterion("piglin_bartering", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(CrockPotItems.NETHEROSIA.get()).build(), adultPiglin)).addCriterion("piglin_bartering_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(CrockPotItems.NETHEROSIA.get()), adultPiglin)).requirements(RequirementsStrategy.OR).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("piglin_bartering"));
            Advancement.Builder.advancement().parent(piglinBartering).display(Items.WITHER_SKELETON_SKULL, CrockPotAdvancementProvider.getTranslatableAdvancementTitle("wither_skeleton_skull"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("wither_skeleton_skull"), null, FrameType.CHALLENGE, true, true, true).addCriterion(CrockPotAdvancementProvider.getItemName(Items.WITHER_SKELETON_SKULL), new PiglinBarteringTrigger.Instance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(Items.WITHER_SKELETON_SKULL).build())).rewards(AdvancementRewards.Builder.experience(50)).save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("wither_skeleton_skull"));
            Advancement.Builder gnawWillBeHappyBuilder = Advancement.Builder.advancement().parent(advancedPot).display(CrockPotItems.GNAWS_COIN.get(), CrockPotAdvancementProvider.getTranslatableAdvancementTitle("gnaw_will_be_happy"), CrockPotAdvancementProvider.getTranslatableAdvancementDescription("gnaw_will_be_happy"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(200).addLootTable(RLUtils.createRL("gnaws_coin")));
            ((List) CrockPotItems.FOODS_WITHOUT_AVAJ.get()).forEach(food -> gnawWillBeHappyBuilder.addCriterion(CrockPotAdvancementProvider.getItemName(food), CrockPotAdvancementProvider.use(food)));
            gnawWillBeHappyBuilder.save(consumer, CrockPotAdvancementProvider.getSimpleAdvancementName("gnaw_will_be_happy"));
        }
    }
}