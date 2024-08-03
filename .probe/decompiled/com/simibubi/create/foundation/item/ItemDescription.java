package com.simibubi.create.foundation.item;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public record ItemDescription(ImmutableList<Component> lines, ImmutableList<Component> linesOnShift, ImmutableList<Component> linesOnCtrl) {

    private static final Map<Item, Supplier<String>> CUSTOM_TOOLTIP_KEYS = new IdentityHashMap();

    @Nullable
    public static ItemDescription create(Item item, TooltipHelper.Palette palette) {
        return create(getTooltipTranslationKey(item), palette);
    }

    @Nullable
    public static ItemDescription create(String translationKey, TooltipHelper.Palette palette) {
        if (!canFillBuilder(translationKey + ".summary")) {
            return null;
        } else {
            ItemDescription.Builder builder = new ItemDescription.Builder(palette);
            fillBuilder(builder, translationKey);
            return builder.build();
        }
    }

    public static boolean canFillBuilder(String translationKey) {
        return I18n.exists(translationKey);
    }

    public static void fillBuilder(ItemDescription.Builder builder, String translationKey) {
        String summaryKey = translationKey + ".summary";
        if (I18n.exists(summaryKey)) {
            builder.addSummary(I18n.get(summaryKey));
        }
        for (int i = 1; i < 100; i++) {
            String conditionKey = translationKey + ".condition" + i;
            String behaviourKey = translationKey + ".behaviour" + i;
            if (!I18n.exists(conditionKey)) {
                break;
            }
            builder.addBehaviour(I18n.get(conditionKey), I18n.get(behaviourKey));
        }
        for (int i = 1; i < 100; i++) {
            String controlKey = translationKey + ".control" + i;
            String actionKey = translationKey + ".action" + i;
            if (!I18n.exists(controlKey)) {
                break;
            }
            builder.addAction(I18n.get(controlKey), I18n.get(actionKey));
        }
    }

    public static void useKey(Item item, Supplier<String> supplier) {
        CUSTOM_TOOLTIP_KEYS.put(item, supplier);
    }

    public static void useKey(ItemLike item, String string) {
        useKey(item.asItem(), () -> string);
    }

    public static void referKey(ItemLike item, Supplier<? extends ItemLike> otherItem) {
        useKey(item.asItem(), () -> ((ItemLike) otherItem.get()).asItem().getDescriptionId());
    }

    public static String getTooltipTranslationKey(Item item) {
        return CUSTOM_TOOLTIP_KEYS.containsKey(item) ? (String) ((Supplier) CUSTOM_TOOLTIP_KEYS.get(item)).get() + ".tooltip" : item.getDescriptionId() + ".tooltip";
    }

    public ImmutableList<Component> getCurrentLines() {
        if (Screen.hasShiftDown()) {
            return this.linesOnShift;
        } else {
            return Screen.hasControlDown() ? this.linesOnCtrl : this.lines;
        }
    }

    public static class Builder {

        protected final TooltipHelper.Palette palette;

        protected final List<String> summary = new ArrayList();

        protected final List<Pair<String, String>> behaviours = new ArrayList();

        protected final List<Pair<String, String>> actions = new ArrayList();

        public Builder(TooltipHelper.Palette palette) {
            this.palette = palette;
        }

        public ItemDescription.Builder addSummary(String summaryLine) {
            this.summary.add(summaryLine);
            return this;
        }

        public ItemDescription.Builder addBehaviour(String condition, String behaviour) {
            this.behaviours.add(Pair.of(condition, behaviour));
            return this;
        }

        public ItemDescription.Builder addAction(String condition, String action) {
            this.actions.add(Pair.of(condition, action));
            return this;
        }

        public ItemDescription build() {
            List<Component> lines = new ArrayList();
            List<Component> linesOnShift = new ArrayList();
            List<Component> linesOnCtrl = new ArrayList();
            for (String summaryLine : this.summary) {
                linesOnShift.addAll(TooltipHelper.cutStringTextComponent(summaryLine, this.palette));
            }
            if (!this.behaviours.isEmpty()) {
                linesOnShift.add(Components.immutableEmpty());
            }
            for (Pair<String, String> behaviourPair : this.behaviours) {
                String condition = (String) behaviourPair.getLeft();
                String behaviour = (String) behaviourPair.getRight();
                linesOnShift.add(Components.literal(condition).withStyle(ChatFormatting.GRAY));
                linesOnShift.addAll(TooltipHelper.cutStringTextComponent(behaviour, this.palette.primary(), this.palette.highlight(), 1));
            }
            for (Pair<String, String> actionPair : this.actions) {
                String condition = (String) actionPair.getLeft();
                String action = (String) actionPair.getRight();
                linesOnCtrl.add(Components.literal(condition).withStyle(ChatFormatting.GRAY));
                linesOnCtrl.addAll(TooltipHelper.cutStringTextComponent(action, this.palette.primary(), this.palette.highlight(), 1));
            }
            boolean hasDescription = !linesOnShift.isEmpty();
            boolean hasControls = !linesOnCtrl.isEmpty();
            if (hasDescription || hasControls) {
                String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
                String[] holdCtrl = Lang.translateDirect("tooltip.holdForControls", "$").getString().split("\\$");
                MutableComponent keyShift = Lang.translateDirect("tooltip.keyShift");
                MutableComponent keyCtrl = Lang.translateDirect("tooltip.keyCtrl");
                for (List<Component> list : Arrays.asList(lines, linesOnShift, linesOnCtrl)) {
                    boolean shift = list == linesOnShift;
                    boolean ctrl = list == linesOnCtrl;
                    if (holdDesc.length == 2 && holdCtrl.length == 2) {
                        if (hasControls) {
                            MutableComponent tabBuilder = Components.empty();
                            tabBuilder.append(Components.literal(holdCtrl[0]).withStyle(ChatFormatting.DARK_GRAY));
                            tabBuilder.append(keyCtrl.m_6879_().withStyle(ctrl ? ChatFormatting.WHITE : ChatFormatting.GRAY));
                            tabBuilder.append(Components.literal(holdCtrl[1]).withStyle(ChatFormatting.DARK_GRAY));
                            list.add(0, tabBuilder);
                        }
                        if (hasDescription) {
                            MutableComponent tabBuilder = Components.empty();
                            tabBuilder.append(Components.literal(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
                            tabBuilder.append(keyShift.m_6879_().withStyle(shift ? ChatFormatting.WHITE : ChatFormatting.GRAY));
                            tabBuilder.append(Components.literal(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
                            list.add(0, tabBuilder);
                        }
                        if (shift || ctrl) {
                            list.add(hasDescription && hasControls ? 2 : 1, Components.immutableEmpty());
                        }
                    } else {
                        list.add(0, Components.literal("Invalid lang formatting!"));
                    }
                }
            }
            if (!hasDescription) {
                linesOnCtrl.clear();
                linesOnShift.addAll(lines);
            }
            if (!hasControls) {
                linesOnCtrl.clear();
                linesOnCtrl.addAll(lines);
            }
            return new ItemDescription(ImmutableList.copyOf(lines), ImmutableList.copyOf(linesOnShift), ImmutableList.copyOf(linesOnCtrl));
        }
    }

    public static class Modifier implements TooltipModifier {

        protected final Item item;

        protected final TooltipHelper.Palette palette;

        protected String cachedLanguage;

        protected ItemDescription description;

        public Modifier(Item item, TooltipHelper.Palette palette) {
            this.item = item;
            this.palette = palette;
        }

        @Override
        public void modify(ItemTooltipEvent context) {
            if (this.checkLocale()) {
                this.description = ItemDescription.create(this.item, this.palette);
            }
            if (this.description != null) {
                context.getToolTip().addAll(1, this.description.getCurrentLines());
            }
        }

        protected boolean checkLocale() {
            String currentLanguage = Minecraft.getInstance().getLanguageManager().getSelected();
            if (!currentLanguage.equals(this.cachedLanguage)) {
                this.cachedLanguage = currentLanguage;
                return true;
            } else {
                return false;
            }
        }
    }
}