package snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes;

import java.util.List;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.DumperOptions;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;

public class SequenceNode extends CollectionNode<Node> {

    private final List<Node> value;

    public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
        super(tag, startMark, endMark, flowStyle);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        } else {
            this.value = value;
            this.resolved = resolved;
        }
    }

    public SequenceNode(Tag tag, List<Node> value, DumperOptions.FlowStyle flowStyle) {
        this(tag, true, value, null, null, flowStyle);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.sequence;
    }

    @Override
    public List<Node> getValue() {
        return this.value;
    }

    public void setListType(Class<? extends Object> listType) {
        for (Node node : this.value) {
            node.setType(listType);
        }
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", value=" + this.getValue() + ")>";
    }
}