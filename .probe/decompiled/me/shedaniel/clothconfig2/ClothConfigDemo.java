package me.shedaniel.clothconfig2;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class ClothConfigDemo {

    public static ConfigBuilder getConfigBuilderWithDemo() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Component.translatable("title.cloth-config.config"));
        builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/oak_planks.png"));
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory testing = builder.getOrCreateCategory(Component.translatable("category.cloth-config.testing"));
        testing.addEntry(entryBuilder.startKeyCodeField(Component.literal("Cool Key"), InputConstants.UNKNOWN).setDefaultValue(InputConstants.UNKNOWN).build());
        testing.addEntry(entryBuilder.startModifierKeyCodeField(Component.literal("Cool Modifier Key"), ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(79), Modifier.of(false, true, false))).setDefaultValue(ModifierKeyCode.of(InputConstants.Type.KEYSYM.getOrCreate(79), Modifier.of(false, true, false))).build());
        testing.addEntry(entryBuilder.startDoubleList(Component.literal("A list of Doubles"), Arrays.asList(1.0, 2.0, 3.0)).setDefaultValue(Arrays.asList(1.0, 2.0, 3.0)).build());
        testing.addEntry(entryBuilder.startLongList(Component.literal("A list of Longs"), Arrays.asList(1L, 2L, 3L)).setDefaultValue(Arrays.asList(1L, 2L, 3L)).setInsertButtonEnabled(false).build());
        testing.addEntry(entryBuilder.startStrList(Component.literal("A list of Strings"), Arrays.asList("abc", "xyz")).setTooltip(Component.literal("Yes this is some beautiful tooltip\nOh and this is the second line!")).setDefaultValue(Arrays.asList("abc", "xyz")).build());
        SubCategoryBuilder colors = entryBuilder.startSubCategory(Component.literal("Colors")).setExpanded(true);
        colors.add((AbstractConfigListEntry) entryBuilder.startColorField(Component.literal("A color field"), 65535).setDefaultValue(65535).build());
        colors.add((AbstractConfigListEntry) entryBuilder.startColorField(Component.literal("An alpha color field"), -16711681).setDefaultValue(-16711681).setAlphaMode(true).build());
        colors.add((AbstractConfigListEntry) entryBuilder.startColorField(Component.literal("An alpha color field"), -1).setDefaultValue(-65536).setAlphaMode(true).build());
        ???;
        colors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        colors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        SubCategoryBuilder innerColors = entryBuilder.startSubCategory(Component.literal("Inner Colors")).setExpanded(true);
        innerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        SubCategoryBuilder innerInnerColors = entryBuilder.startSubCategory(Component.literal("Inner Inner Colors")).setExpanded(true);
        innerInnerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerInnerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerInnerColors.add((AbstractConfigListEntry) entryBuilder.startDropdownMenu(Component.literal("lol apple"), DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(Items.APPLE), DropdownMenuBuilder.CellCreatorBuilder.ofItemObject()).setDefaultValue(Items.APPLE).setSelections((Iterable<Item>) BuiltInRegistries.ITEM.m_123024_().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new))).setSaveConsumer(item -> System.out.println("save this " + item)).build());
        innerColors.add((AbstractConfigListEntry) innerInnerColors.build());
        colors.add((AbstractConfigListEntry) innerColors.build());
        testing.addEntry(colors.build());
        testing.addEntry(entryBuilder.startDropdownMenu(Component.literal("Suggestion Random Int"), DropdownMenuBuilder.TopCellElementBuilder.of(10, s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var2x) {
                return null;
            }
        })).setDefaultValue(10).setSelections(Lists.newArrayList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })).build());
        testing.addEntry(entryBuilder.startDropdownMenu(Component.literal("Selection Random Int"), DropdownMenuBuilder.TopCellElementBuilder.of(10, s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var2x) {
                return null;
            }
        })).setDefaultValue(5).setSuggestionMode(false).setSelections(Lists.newArrayList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })).build());
        testing.addEntry(new NestedListListEntry(Component.literal("Nice"), Lists.newArrayList(new Pair[] { new Pair<>(10, 10), new Pair<>(20, 40) }), false, Optional::empty, list -> {
        }, () -> Lists.newArrayList(new Pair[] { new Pair<>(10, 10), new Pair<>(20, 40) }), entryBuilder.getResetButtonKey(), true, true, (elem, nestedListListEntry) -> {
            if (elem == null) {
                Pair<Integer, Integer> newDefaultElemValue = new Pair<>(10, 10);
                return new MultiElementListEntry<>(Component.literal("Pair"), newDefaultElemValue, Lists.newArrayList(new AbstractConfigListEntry[] { entryBuilder.startIntField(Component.literal("Left"), newDefaultElemValue.getLeft()).setDefaultValue(10).build(), entryBuilder.startIntField(Component.literal("Right"), newDefaultElemValue.getRight()).setDefaultValue(10).build() }), true);
            } else {
                return new MultiElementListEntry<>(Component.literal("Pair"), elem, Lists.newArrayList(new AbstractConfigListEntry[] { entryBuilder.startIntField(Component.literal("Left"), (Integer) elem.getLeft()).setDefaultValue(10).build(), entryBuilder.startIntField(Component.literal("Right"), (Integer) elem.getRight()).setDefaultValue(10).build() }), true);
            }
        }));
        SubCategoryBuilder depends = entryBuilder.startSubCategory(Component.literal("Dependencies")).setExpanded(true);
        BooleanListEntry dependency = entryBuilder.startBooleanToggle(Component.literal("A cool toggle"), false).setTooltip(Component.literal("Toggle me...")).build();
        depends.add((AbstractConfigListEntry) dependency);
        Collection<BooleanListEntry> toggles = new LinkedList();
        toggles.add(entryBuilder.startBooleanToggle(Component.literal("I only work when cool is toggled..."), true).setRequirement(Requirement.isTrue(dependency)).build());
        toggles.add(entryBuilder.startBooleanToggle(Component.literal("I only appear when cool is toggled..."), true).setDisplayRequirement(Requirement.isTrue(dependency)).build());
        depends.addAll(toggles);
        depends.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("I only work when cool matches both of these toggles ^^"), true).setRequirement(Requirement.all((Requirement[]) toggles.stream().map(toggle -> Requirement.matches(dependency, toggle)).toArray(Requirement[]::new))).build());
        SubCategoryBuilder dependantSub = entryBuilder.startSubCategory(Component.literal("Sub-categories can have requirements too...")).setRequirement(Requirement.isTrue(dependency));
        dependantSub.add((AbstractConfigListEntry) entryBuilder.startTextDescription(Component.literal("This sub category depends on Cool being toggled")).build());
        dependantSub.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("Example entry"), true).build());
        dependantSub.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("Another example..."), true).build());
        depends.add((AbstractConfigListEntry) dependantSub.build());
        depends.add((AbstractConfigListEntry) entryBuilder.startLongList(Component.literal("Even lists!"), Arrays.asList(1L, 2L, 3L)).setDefaultValue(Arrays.asList(1L, 2L, 3L)).setRequirement(Requirement.isTrue(dependency)).build());
        EnumListEntry<DependencyDemoEnum> enumDependency = entryBuilder.startEnumSelector(Component.literal("Select a good or bad option"), DependencyDemoEnum.class, DependencyDemoEnum.OKAY).build();
        depends.add((AbstractConfigListEntry) enumDependency);
        IntegerSliderEntry intDependency = entryBuilder.startIntSlider(Component.literal("Select something big or small"), 50, -100, 100).build();
        depends.add((AbstractConfigListEntry) intDependency);
        depends.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("I only work when a good option is chosen..."), true).setTooltip(Component.literal("Select good or better above")).setRequirement(Requirement.isValue(enumDependency, DependencyDemoEnum.EXCELLENT, DependencyDemoEnum.GOOD)).build());
        depends.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("I need a good option AND a cool toggle!"), true).setTooltip(Component.literal("Select good or better and also toggle cool")).setRequirement(Requirement.all(Requirement.isTrue(dependency), Requirement.isValue(enumDependency, DependencyDemoEnum.EXCELLENT, DependencyDemoEnum.GOOD))).build());
        depends.add((AbstractConfigListEntry) entryBuilder.startBooleanToggle(Component.literal("I only work when numbers are extreme!"), true).setTooltip(Component.literal("Move the slider...")).setRequirement(Requirement.any(() -> intDependency.getValue() < -70, () -> intDependency.getValue() > 70)).build());
        testing.addEntry(depends.build());
        testing.addEntry(entryBuilder.startTextDescription(Component.translatable("text.cloth-config.testing.1", Component.literal("ClothConfig").withStyle(s -> s.withBold(true).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(Util.make(new ItemStack(Items.PINK_WOOL), stack -> stack.setHoverName(Component.literal("(・∀・)")).enchant(Enchantments.BLOCK_EFFICIENCY, 10)))))), Component.translatable("text.cloth-config.testing.2").withStyle(s -> s.withColor(ChatFormatting.BLUE).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("https://shedaniel.gitbook.io/cloth-config/"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://shedaniel.gitbook.io/cloth-config/"))), Component.translatable("text.cloth-config.testing.3").withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, Utils.getConfigFolder().getParent().resolve("options.txt").toString()))))).build());
        builder.transparentBackground();
        return builder;
    }
}