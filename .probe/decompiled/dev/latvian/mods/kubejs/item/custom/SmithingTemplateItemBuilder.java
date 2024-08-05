package dev.latvian.mods.kubejs.item.custom;

import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

public class SmithingTemplateItemBuilder extends ItemBuilder {

    private static final List<ResourceLocation> ARMOR_ICONS = SmithingTemplateItem.createTrimmableArmorIconList();

    private static final List<ResourceLocation> INGOTS_AND_CRYSTALS_ICONS = SmithingTemplateItem.createTrimmableMaterialIconList();

    private static final List<ResourceLocation> EQUIPMENT_ICONS = SmithingTemplateItem.createNetheriteUpgradeIconList();

    private static final List<ResourceLocation> TOOL_ICONS = List.of(SmithingTemplateItem.EMPTY_SLOT_HOE, SmithingTemplateItem.EMPTY_SLOT_AXE, SmithingTemplateItem.EMPTY_SLOT_SWORD, SmithingTemplateItem.EMPTY_SLOT_SHOVEL, SmithingTemplateItem.EMPTY_SLOT_PICKAXE);

    private static final List<ResourceLocation> CRYSTAL_ICONS = List.of(SmithingTemplateItem.EMPTY_SLOT_REDSTONE_DUST, SmithingTemplateItem.EMPTY_SLOT_QUARTZ, SmithingTemplateItem.EMPTY_SLOT_EMERALD, SmithingTemplateItem.EMPTY_SLOT_DIAMOND, SmithingTemplateItem.EMPTY_SLOT_LAPIS_LAZULI, SmithingTemplateItem.EMPTY_SLOT_AMETHYST_SHARD);

    private final Map<String, String> translations = new HashMap();

    public Component appliesToText = Component.literal("set with .appliesToDescription(string) on your smithing_template type item").withStyle(ChatFormatting.BLUE);

    public Component ingredientsText = Component.literal("set with .ingredientsDescription(string) on your smithing_template type item").withStyle(ChatFormatting.BLUE);

    public Component appliesToSlotDescriptionText = Component.literal("set with .appliesToSlotDescription(string) on your smithing_template type item");

    public Component ingredientSlotDescriptionText = Component.literal("set with .ingredientsSlotDescription(string) on your smithing_template type item");

    public final List<ResourceLocation> appliesToEmptyIcons = new ArrayList();

    public final List<ResourceLocation> ingredientsSlotEmptyIcons = new ArrayList();

    public SmithingTemplateItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets the description text that shows in the item tooltip to describe what it can be applied to.\nUsing 'Armor' or 'Diamond Equipment' will use the vanilla language keys so it is translated into other languages automatically.\nTHIS IS PURELY VISUAL\n\nIf you wish to apply non standard formatting (like change the colour) set the `ingredientsText` field.\n")
    public SmithingTemplateItemBuilder appliesTo(String text) {
        this.appliesToText = switch(text) {
            case "Armor" ->
                SmithingTemplateItem.ARMOR_TRIM_APPLIES_TO;
            case "Diamond Equipment" ->
                SmithingTemplateItem.NETHERITE_UPGRADE_APPLIES_TO;
            default ->
                this.defaultTranslateableTooltipComponent(text, "applies_to", true);
        };
        return this;
    }

    @Info("Sets the description text that shows in the item tooltip to describe what ingredients can be added.\nUsing 'Ingots & Crystals' or 'Netherite Ingot' will use the vanilla language keys so it is translated into other languages automatically.\nTHIS IS PURELY VISUAL\n\nIf you wish to apply non standard formatting (like change the colour) set the `ingredientsText` field.\n")
    public SmithingTemplateItemBuilder ingredients(String text) {
        this.ingredientsText = switch(text) {
            case "Ingots and Crystals", "Ingots & Crystals" ->
                SmithingTemplateItem.ARMOR_TRIM_INGREDIENTS;
            case "Netherite Ingot" ->
                SmithingTemplateItem.NETHERITE_UPGRADE_INGREDIENTS;
            default ->
                this.defaultTranslateableTooltipComponent(text, "ingredients", true);
        };
        return this;
    }

    @Info("Sets the description text that shows when you hover over the base item slot when this item is put in smithing table as a template.\nUsing 'Add a piece of armor' or 'Add diamond armor, weapon, or tool' will use the vanilla language keys so it is translated into other languages automatically.\n\nIf you wish to apply non standard formatting (like change the colour) set the `appliesToSlotDescriptionText` field.\n")
    public SmithingTemplateItemBuilder appliesToSlotDescription(String text) {
        this.appliesToSlotDescriptionText = switch(text) {
            case "Add a piece of armor" ->
                SmithingTemplateItem.ARMOR_TRIM_BASE_SLOT_DESCRIPTION;
            case "Add diamond armor, weapon, or tool" ->
                SmithingTemplateItem.NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION;
            default ->
                this.defaultTranslateableTooltipComponent(text, "base_slot_description", false);
        };
        return this;
    }

    @Info("Sets the description text that shows when you hover over the ingredient slot when this item is put in smithing table as a template.\nUsing 'Add ingot or crystal' or 'Add Netherite Ingot' will use the vanilla language keys so it is translated into other languages automatically.\n\nIf you wish to apply non standard formatting (like change the colour) set the `ingredientSlotDescriptionText` field.\n")
    public SmithingTemplateItemBuilder ingredientsSlotDescription(String text) {
        this.ingredientSlotDescriptionText = switch(text) {
            case "Add ingot or crystal" ->
                SmithingTemplateItem.ARMOR_TRIM_BASE_SLOT_DESCRIPTION;
            case "Add Netherite Ingot" ->
                SmithingTemplateItem.NETHERITE_UPGRADE_BASE_SLOT_DESCRIPTION;
            default ->
                this.defaultTranslateableTooltipComponent(text, "ingredient_slot_description", false);
        };
        return this;
    }

    @Info("Adds the specified texture location to the list of base slot icons that the smithing table cycles through when this smithing template is put in.")
    public SmithingTemplateItemBuilder addAppliesToSlotIcon(ResourceLocation location) {
        this.appliesToEmptyIcons.add(location);
        return this;
    }

    @Info("Adds the specified texture location to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder addIngredientsSlotIcon(ResourceLocation location) {
        this.ingredientsSlotEmptyIcons.add(location);
        return this;
    }

    @Info("Adds all armor icons to the list of base slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder armorIcons() {
        this.appliesToEmptyIcons.addAll(ARMOR_ICONS);
        return this;
    }

    @Info("Adds all armor and basic tool icons to the list of base slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder equipmentIcons() {
        this.appliesToEmptyIcons.addAll(EQUIPMENT_ICONS);
        return this;
    }

    @Info("Adds all basic tool icons to the list of base slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder toolIcons() {
        this.appliesToEmptyIcons.addAll(TOOL_ICONS);
        return this;
    }

    @Info("Adds an ingot, dust, diamond, emerald, quartz, lapis lazuli and amethyst shard icons to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder ingotAndCrystalIcons() {
        this.ingredientsSlotEmptyIcons.addAll(INGOTS_AND_CRYSTALS_ICONS);
        return this;
    }

    @Info("Adds a dust, diamond, emerald, quartz, lapis lazuli and amethyst shard icons to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder crystalIcons() {
        this.ingredientsSlotEmptyIcons.addAll(CRYSTAL_ICONS);
        return this;
    }

    @Info("Adds an ingot to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder ingotIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_INGOT);
    }

    @Info("Adds a dust to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder dustIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_REDSTONE_DUST);
    }

    @Info("Adds an amethyst shard to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder shardIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_AMETHYST_SHARD);
    }

    @Info("Adds a diamond to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder diamondIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_DIAMOND);
    }

    @Info("Adds an emerald to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder emeraldIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_EMERALD);
    }

    @Info("Adds a quartz to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder quartzIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_QUARTZ);
    }

    @Info("Adds a lapis lazuli to the list of ingredient slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder lapisIcon() {
        return this.addIngredientsSlotIcon(SmithingTemplateItem.EMPTY_SLOT_LAPIS_LAZULI);
    }

    @Info("Adds a sword to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder swordIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_SWORD);
    }

    @Info("Adds a shovel to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder shovelIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_SHOVEL);
    }

    @Info("Adds a axe to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder axeIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_AXE);
    }

    @Info("Adds a pickaxe to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder pickaxeIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_PICKAXE);
    }

    @Info("Adds a hoe to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder hoeIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_HOE);
    }

    @Info("Adds a helmet to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder helmetIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_HELMET);
    }

    @Info("Adds a chestplate to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder chestplateIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_CHESTPLATE);
    }

    @Info("Adds leggings to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder leggingsIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_LEGGINGS);
    }

    @Info("Adds boots to the list of base item slot icons that the smithing table cycles through when this smithing template is put in")
    public SmithingTemplateItemBuilder bootsIcon() {
        return this.addAppliesToSlotIcon(SmithingTemplateItem.EMPTY_SLOT_BOOTS);
    }

    private Component defaultTranslateableTooltipComponent(String text, String type, boolean tooltipDescription) {
        String translationKey = this.makeTooltipDescriptionId(type);
        this.translations.put(translationKey, text);
        MutableComponent component = Component.translatable(translationKey);
        if (tooltipDescription) {
            component.withStyle(SmithingTemplateItem.DESCRIPTION_FORMAT);
        }
        return component;
    }

    private String makeTooltipDescriptionId(String type) {
        return this.getTranslationKeyGroup() + "." + this.id.getNamespace() + ".smithing_template." + this.id.getPath() + "." + type;
    }

    @Info("Sets the name for this smithing template.\nNote that the normal display name for all smithing templates is the same and cannot be changed, this instead sets the name in the tooltip (see vanilla smithing templates for what this looks like).\n\nThis will be overridden by a lang file if it exists.\n")
    public SmithingTemplateItemBuilder displayName(Component name) {
        super.displayName(name.copy().withStyle(SmithingTemplateItem.TITLE_FORMAT));
        return this;
    }

    @Override
    public void generateLang(LangEventJS lang) {
        super.generateLang(lang);
        lang.addAll(this.id.getNamespace(), this.translations);
    }

    public SmithingTemplateItem createObject() {
        return new SmithingTemplateItem(this.appliesToText, this.ingredientsText, (Component) Objects.requireNonNullElse(this.displayName, Component.translatable(this.getBuilderTranslationKey()).withStyle(SmithingTemplateItem.TITLE_FORMAT)), this.appliesToSlotDescriptionText, this.ingredientSlotDescriptionText, this.appliesToEmptyIcons, this.ingredientsSlotEmptyIcons);
    }
}