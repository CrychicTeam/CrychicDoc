package fuzs.puzzleslib.impl.client.event;

import java.util.AbstractList;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;

final class ForgeButtonList extends AbstractList<AbstractWidget> {

    private final List<Renderable> renderables;

    ForgeButtonList(List<Renderable> renderables) {
        this.renderables = renderables;
    }

    public int size() {
        return (int) this.renderables.stream().filter(AbstractWidget.class::isInstance).count();
    }

    public AbstractWidget get(int index) {
        return (AbstractWidget) this.renderables.stream().filter(AbstractWidget.class::isInstance).skip((long) index).findFirst().map(AbstractWidget.class::cast).orElseThrow(() -> new IndexOutOfBoundsException(index));
    }
}