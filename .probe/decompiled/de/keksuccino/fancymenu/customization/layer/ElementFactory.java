package de.keksuccino.fancymenu.customization.layer;

import de.keksuccino.fancymenu.customization.deep.AbstractDeepElement;
import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.customization.deep.DeepScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.deep.DeepScreenCustomizationLayerRegistry;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElementBuilder;
import de.keksuccino.fancymenu.customization.layout.Layout;
import de.keksuccino.fancymenu.customization.widget.WidgetMeta;
import de.keksuccino.fancymenu.customization.widget.identification.WidgetIdentifierHandler;
import de.keksuccino.fancymenu.util.ListUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ElementFactory {

    default void constructElementInstances(@Nullable String screenIdentifier, @Nullable List<WidgetMeta> vanillaWidgetMetaList, @NotNull List<Layout> layouts, @NotNull Layout.OrderedElementCollection normalElements, @Nullable List<VanillaWidgetElement> vanillaWidgetElements, @Nullable List<AbstractDeepElement> deepElements) {
        Map<WidgetMeta, List<VanillaWidgetElement>> unstackedVanillaButtonElements = new HashMap();
        Map<DeepElementBuilder<?, ?, ?>, List<AbstractDeepElement>> unstackedDeepElements = new HashMap();
        for (Layout layout : layouts) {
            Layout.OrderedElementCollection layoutElements = layout.buildElementInstances();
            normalElements.backgroundElements.addAll(layoutElements.backgroundElements);
            normalElements.foregroundElements.addAll(layoutElements.foregroundElements);
            if (deepElements != null) {
                for (AbstractDeepElement element : layout.buildDeepElementInstances()) {
                    if (!unstackedDeepElements.containsKey(element.builder)) {
                        unstackedDeepElements.put(element.builder, new ArrayList());
                    }
                    ((List) unstackedDeepElements.get(element.builder)).add(element);
                }
            }
            if (vanillaWidgetElements != null) {
                for (VanillaWidgetElement element : layout.buildVanillaButtonElementInstances()) {
                    WidgetMeta d = vanillaWidgetMetaList != null ? findWidgetMeta(element.getInstanceIdentifier(), vanillaWidgetMetaList) : null;
                    if (d != null) {
                        element.setVanillaWidget(d, element.anchorPoint == ElementAnchorPoints.VANILLA);
                        if (!unstackedVanillaButtonElements.containsKey(d)) {
                            unstackedVanillaButtonElements.put(d, new ArrayList());
                        }
                        ((List) unstackedVanillaButtonElements.get(d)).add(element);
                    }
                }
            }
        }
        if (deepElements != null) {
            DeepScreenCustomizationLayer deepScreenCustomizationLayer = screenIdentifier != null ? DeepScreenCustomizationLayerRegistry.getLayer(screenIdentifier) : null;
            if (deepScreenCustomizationLayer != null) {
                for (DeepElementBuilder<?, ?, ?> builder : deepScreenCustomizationLayer.getBuilders()) {
                    if (!unstackedDeepElements.containsKey(builder)) {
                        AbstractDeepElement elementx = builder.buildDefaultInstance();
                        unstackedDeepElements.put(builder, new ArrayList());
                        ((List) unstackedDeepElements.get(builder)).add(elementx);
                    }
                }
            }
            for (Entry<DeepElementBuilder<?, ?, ?>, List<AbstractDeepElement>> m : unstackedDeepElements.entrySet()) {
                if (!((List) m.getValue()).isEmpty()) {
                    if (((List) m.getValue()).size() > 1) {
                        AbstractDeepElement stacked = ((DeepElementBuilder) m.getKey()).stackElementsInternal(((DeepElementBuilder) m.getKey()).buildDefaultInstance(), (AbstractElement[]) ((List) m.getValue()).toArray(new AbstractDeepElement[0]));
                        if (stacked != null) {
                            deepElements.add(stacked);
                        }
                    } else {
                        deepElements.add((AbstractDeepElement) ((List) m.getValue()).get(0));
                    }
                }
            }
            if (deepScreenCustomizationLayer != null) {
                List<AbstractDeepElement> deepElementsOrdered = new ArrayList();
                for (DeepElementBuilder<?, ?, ?> b : deepScreenCustomizationLayer.getBuilders()) {
                    for (AbstractDeepElement e : deepElements) {
                        if (e.builder == b) {
                            deepElementsOrdered.add(e);
                            break;
                        }
                    }
                }
                deepElements.clear();
                deepElements.addAll(deepElementsOrdered);
            }
        }
        if (vanillaWidgetMetaList != null && vanillaWidgetElements != null) {
            for (WidgetMeta d : vanillaWidgetMetaList) {
                if (!unstackedVanillaButtonElements.containsKey(d)) {
                    VanillaWidgetElement elementx = VanillaWidgetElementBuilder.INSTANCE.buildDefaultInstance();
                    elementx.setVanillaWidget(d, true);
                    unstackedVanillaButtonElements.put(d, new ArrayList());
                    ((List) unstackedVanillaButtonElements.get(d)).add(elementx);
                }
            }
            for (Entry<WidgetMeta, List<VanillaWidgetElement>> mx : unstackedVanillaButtonElements.entrySet()) {
                if (!((List) mx.getValue()).isEmpty()) {
                    if (((List) mx.getValue()).size() > 1) {
                        VanillaWidgetElement stacked = VanillaWidgetElementBuilder.INSTANCE.stackElementsInternal(VanillaWidgetElementBuilder.INSTANCE.buildDefaultInstance(), (AbstractElement[]) ((List) mx.getValue()).toArray(new VanillaWidgetElement[0]));
                        if (stacked != null) {
                            if (stacked.anchorPoint == ElementAnchorPoints.VANILLA) {
                                stacked.mirrorVanillaWidgetSizeAndPosition();
                            }
                            vanillaWidgetElements.add(stacked);
                        }
                    } else {
                        if (((VanillaWidgetElement) ((List) mx.getValue()).get(0)).anchorPoint == ElementAnchorPoints.VANILLA) {
                            ((VanillaWidgetElement) ((List) mx.getValue()).get(0)).mirrorVanillaWidgetSizeAndPosition();
                        }
                        vanillaWidgetElements.add((VanillaWidgetElement) ((List) mx.getValue()).get(0));
                    }
                }
            }
        }
    }

    default void constructElementInstances(@Nullable String menuIdentifier, @Nullable List<WidgetMeta> vanillaWidgetMetaList, @NotNull Layout layout, @NotNull Layout.OrderedElementCollection normalElements, @Nullable List<VanillaWidgetElement> vanillaWidgetElements, @Nullable List<AbstractDeepElement> deepElements) {
        this.constructElementInstances(menuIdentifier, vanillaWidgetMetaList, ListUtils.of(layout), normalElements, vanillaWidgetElements, deepElements);
    }

    @Nullable
    private static WidgetMeta findWidgetMeta(@NotNull String identifier, @NotNull List<WidgetMeta> metas) {
        identifier = identifier.replace("vanillabtn:", "");
        for (WidgetMeta meta : metas) {
            if (WidgetIdentifierHandler.isIdentifierOfWidget(identifier, meta)) {
                return meta;
            }
        }
        return null;
    }
}