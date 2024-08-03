package pie.ilikepiefoo.compat.jade;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class ITooltipWrapper {

    public final ITooltip tooltip;

    public ITooltipWrapper(ITooltip tooltip) {
        this.tooltip = tooltip;
    }

    public static ITooltipWrapper of(ITooltip tooltip) {
        return new ITooltipWrapper(tooltip);
    }

    public ITooltip getTooltip() {
        return this.tooltip;
    }

    public void clear() {
        this.tooltip.clear();
    }

    public int size() {
        return this.tooltip.size();
    }

    public boolean isEmpty() {
        return this.tooltip.isEmpty();
    }

    public void add(Component component) {
        this.tooltip.add(component);
    }

    public void add(IElement element) {
        this.tooltip.add(element);
    }

    public void add(Component component, ResourceLocation tag) {
        this.tooltip.add(component, tag);
    }

    public void add(int index, Component component) {
        this.tooltip.add(index, component);
    }

    public void add(int index, Component component, ResourceLocation tag) {
        this.tooltip.add(index, component, tag);
    }

    public void addAll(List<Component> components) {
        this.tooltip.addAll(components);
    }

    public void addElements(List<IElement> elements) {
        this.tooltip.add(elements);
    }

    public void add(int index, List<IElement> elements) {
        this.tooltip.add(index, elements);
    }

    public void add(int i, IElement iElement) {
        this.tooltip.add(i, iElement);
    }

    public void append(Component component) {
        this.tooltip.append(component);
    }

    public void append(Component component, ResourceLocation tag) {
        this.tooltip.append(component, tag);
    }

    public void append(IElement element) {
        this.tooltip.append(element);
    }

    public void append(int index, List<IElement> elements) {
        this.tooltip.append(index, elements);
    }

    public void append(int i, IElement iElement) {
        this.tooltip.append(i, iElement);
    }

    public void remove(ResourceLocation resourceLocation) {
        this.tooltip.remove(resourceLocation);
    }

    public IElementHelper getElementHelper() {
        return this.tooltip.getElementHelper();
    }

    public List<IElement> get(ResourceLocation resourceLocation) {
        return this.tooltip.get(resourceLocation);
    }

    public List<IElement> get(int i, IElement.Align align) {
        return this.tooltip.get(i, align);
    }

    public String getMessage() {
        return this.tooltip.getMessage();
    }
}