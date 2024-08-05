package snownee.jade.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.Jade;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ElementHelper;
import snownee.jade.overlay.DisplayHelper;

public class Tooltip implements ITooltip {

    public final List<Tooltip.Line> lines = new ArrayList();

    public boolean sneakyDetails;

    @Override
    public void clear() {
        this.lines.clear();
    }

    @Override
    public void append(int index, IElement element) {
        if (element.getTag() == null) {
            element.tag(ElementHelper.INSTANCE.currentUid());
        }
        if (!this.isEmpty() && index != this.size()) {
            Tooltip.Line lastLine = (Tooltip.Line) this.lines.get(index);
            lastLine.getAlignedElements(element.getAlignment()).add(element);
        } else {
            this.add(element);
        }
    }

    @Override
    public IElementHelper getElementHelper() {
        return IElementHelper.get();
    }

    @Override
    public int size() {
        return this.lines.size();
    }

    @Override
    public void add(int index, IElement element) {
        if (element.getTag() == null) {
            element.tag(ElementHelper.INSTANCE.currentUid());
        }
        Tooltip.Line line = new Tooltip.Line();
        line.getAlignedElements(element.getAlignment()).add(element);
        this.lines.add(index, line);
    }

    @Override
    public List<IElement> get(ResourceLocation tag) {
        List<IElement> elements = Lists.newArrayList();
        for (Tooltip.Line line : this.lines) {
            line.left.stream().filter(e -> Objects.equal(tag, e.getTag())).forEach(elements::add);
            line.right.stream().filter(e -> Objects.equal(tag, e.getTag())).forEach(elements::add);
        }
        return elements;
    }

    @Override
    public List<IElement> get(int index, IElement.Align align) {
        Tooltip.Line line = (Tooltip.Line) this.lines.get(index);
        return line.getAlignedElements(align);
    }

    @Override
    public void remove(ResourceLocation tag) {
        Iterator<Tooltip.Line> iterator = this.lines.iterator();
        while (iterator.hasNext()) {
            Tooltip.Line line = (Tooltip.Line) iterator.next();
            line.left.removeIf(e -> Objects.equal(tag, e.getTag()));
            line.right.removeIf(e -> Objects.equal(tag, e.getTag()));
            if (line.left.isEmpty() && line.right.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public static void drawBorder(GuiGraphics guiGraphics, float x, float y, IElement element) {
        if (Jade.CONFIG.get().getGeneral().isDebug()) {
            Vec2 translate = element.getTranslation();
            Vec2 size = element.getCachedSize();
            DisplayHelper.INSTANCE.drawBorder(guiGraphics, x, y, x + size.x, y + size.y, 1.0F, -1996554240, true);
            if (!Vec2.ZERO.equals(translate)) {
                DisplayHelper.INSTANCE.drawBorder(guiGraphics, x + translate.x, y + translate.y, x + translate.x + size.x, y + translate.y + size.y, 1.0F, -2013265665, true);
            }
        }
    }

    @Override
    public String getMessage() {
        List<String> msgs = Lists.newArrayList();
        for (Tooltip.Line line : this.lines) {
            msgs.add(Joiner.on(' ').join(Stream.concat(line.left.stream(), line.right.stream()).filter(e -> !Identifiers.CORE_MOD_NAME.equals(e.getTag())).map(IElement::getCachedMessage).filter(java.util.Objects::nonNull).toList()));
        }
        return Joiner.on('\n').join(msgs);
    }

    public static class Line {

        private final List<IElement> left = new ArrayList();

        private final List<IElement> right = new ArrayList(0);

        private Vec2 size;

        public List<IElement> getAlignedElements(IElement.Align align) {
            return align == IElement.Align.LEFT ? this.left : this.right;
        }

        public Vec2 getSize() {
            if (this.size == null) {
                float width = 0.0F;
                float height = 0.0F;
                for (IElement element : this.left) {
                    Vec2 elementSize = element.getCachedSize();
                    width += elementSize.x;
                    height = Math.max(height, elementSize.y);
                }
                for (IElement element : this.right) {
                    Vec2 elementSize = element.getCachedSize();
                    width += elementSize.x;
                    height = Math.max(height, elementSize.y);
                }
                this.size = new Vec2(width, height);
            }
            return this.size;
        }

        public void render(GuiGraphics guiGraphics, float x, float y, float maxWidth, float y2) {
            float ox = maxWidth;
            float oy = y;
            for (int i = this.right.size() - 1; i >= 0; i--) {
                IElement element = (IElement) this.right.get(i);
                Vec2 translate = element.getTranslation();
                Vec2 size = element.getCachedSize();
                ox -= size.x;
                Tooltip.drawBorder(guiGraphics, ox, oy, element);
                element.render(guiGraphics, ox + translate.x, oy + translate.y, x + size.x + translate.x, y + y2 + translate.y);
            }
            maxWidth = ox;
            ox = x;
            for (int i = 0; i < this.left.size(); i++) {
                IElement element = (IElement) this.left.get(i);
                Vec2 translate = element.getTranslation();
                Vec2 size = element.getCachedSize();
                Tooltip.drawBorder(guiGraphics, ox, oy, element);
                element.render(guiGraphics, ox + translate.x, oy + translate.y, (i == this.left.size() - 1 ? maxWidth : ox + size.x) + translate.x, y + y2 + translate.y);
                ox += size.x;
            }
        }
    }
}