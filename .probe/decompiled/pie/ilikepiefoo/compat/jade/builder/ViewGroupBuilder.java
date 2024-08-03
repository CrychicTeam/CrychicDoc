package pie.ilikepiefoo.compat.jade.builder;

import java.util.List;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.ViewGroup;

public class ViewGroupBuilder<OUT> {

    private List<OUT> elements = null;

    public List<OUT> getElements() {
        return this.elements;
    }

    public ViewGroupBuilder<OUT> setElements(List<OUT> elements) {
        this.elements = elements;
        return this;
    }

    public ViewGroupBuilder<OUT> add(OUT element) {
        return this.addElement(element);
    }

    public ViewGroupBuilder<OUT> addElement(OUT element) {
        this.elements.add(element);
        return this;
    }

    public ViewGroupBuilder<OUT> addAll(List<OUT> elements) {
        return this.addElements(elements);
    }

    public ViewGroupBuilder<OUT> addElements(List<OUT> elements) {
        this.elements.addAll(elements);
        return this;
    }

    public ViewGroupBuilder<OUT> clear() {
        this.elements.clear();
        return this;
    }

    public ViewGroup<OUT> buildCommon() {
        return this.elements == null ? null : new ViewGroup<>(this.elements);
    }

    public ClientViewGroup<OUT> buildClient() {
        return this.elements == null ? null : new ClientViewGroup<>(this.elements);
    }
}