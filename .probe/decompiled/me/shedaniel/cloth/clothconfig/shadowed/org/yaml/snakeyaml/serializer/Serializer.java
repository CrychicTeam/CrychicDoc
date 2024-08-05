package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.DumperOptions;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.emitter.Emitable;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.AliasEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.DocumentEndEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.DocumentStartEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.ImplicitTuple;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.MappingEndEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.MappingStartEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.ScalarEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.SequenceEndEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.SequenceStartEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.StreamEndEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events.StreamStartEvent;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.AnchorNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.CollectionNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.MappingNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.Node;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.NodeId;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.NodeTuple;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.ScalarNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.SequenceNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.Tag;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.resolver.Resolver;

public final class Serializer {

    private final Emitable emitter;

    private final Resolver resolver;

    private boolean explicitStart;

    private boolean explicitEnd;

    private DumperOptions.Version useVersion;

    private Map<String, String> useTags;

    private Set<Node> serializedNodes;

    private Map<Node, String> anchors;

    private AnchorGenerator anchorGenerator;

    private Boolean closed;

    private Tag explicitRoot;

    public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
        this.emitter = emitter;
        this.resolver = resolver;
        this.explicitStart = opts.isExplicitStart();
        this.explicitEnd = opts.isExplicitEnd();
        if (opts.getVersion() != null) {
            this.useVersion = opts.getVersion();
        }
        this.useTags = opts.getTags();
        this.serializedNodes = new HashSet();
        this.anchors = new HashMap();
        this.anchorGenerator = opts.getAnchorGenerator();
        this.closed = null;
        this.explicitRoot = rootTag;
    }

    public void open() throws IOException {
        if (this.closed == null) {
            this.emitter.emit(new StreamStartEvent(null, null));
            this.closed = Boolean.FALSE;
        } else if (Boolean.TRUE.equals(this.closed)) {
            throw new SerializerException("serializer is closed");
        } else {
            throw new SerializerException("serializer is already opened");
        }
    }

    public void close() throws IOException {
        if (this.closed == null) {
            throw new SerializerException("serializer is not opened");
        } else {
            if (!Boolean.TRUE.equals(this.closed)) {
                this.emitter.emit(new StreamEndEvent(null, null));
                this.closed = Boolean.TRUE;
                this.serializedNodes.clear();
                this.anchors.clear();
            }
        }
    }

    public void serialize(Node node) throws IOException {
        if (this.closed == null) {
            throw new SerializerException("serializer is not opened");
        } else if (this.closed) {
            throw new SerializerException("serializer is closed");
        } else {
            this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
            this.anchorNode(node);
            if (this.explicitRoot != null) {
                node.setTag(this.explicitRoot);
            }
            this.serializeNode(node, null);
            this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
            this.serializedNodes.clear();
            this.anchors.clear();
        }
    }

    private void anchorNode(Node node) {
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode) node).getRealNode();
        }
        if (this.anchors.containsKey(node)) {
            String anchor = (String) this.anchors.get(node);
            if (null == anchor) {
                anchor = this.anchorGenerator.nextAnchor(node);
                this.anchors.put(node, anchor);
            }
        } else {
            this.anchors.put(node, node.getAnchor() != null ? this.anchorGenerator.nextAnchor(node) : null);
            switch(node.getNodeId()) {
                case sequence:
                    SequenceNode seqNode = (SequenceNode) node;
                    for (Node item : seqNode.getValue()) {
                        this.anchorNode(item);
                    }
                    break;
                case mapping:
                    MappingNode mnode = (MappingNode) node;
                    for (NodeTuple object : mnode.getValue()) {
                        Node key = object.getKeyNode();
                        Node value = object.getValueNode();
                        this.anchorNode(key);
                        this.anchorNode(value);
                    }
            }
        }
    }

    private void serializeNode(Node node, Node parent) throws IOException {
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode) node).getRealNode();
        }
        String tAlias = (String) this.anchors.get(node);
        if (this.serializedNodes.contains(node)) {
            this.emitter.emit(new AliasEvent(tAlias, null, null));
        } else {
            this.serializedNodes.add(node);
            switch(node.getNodeId()) {
                case sequence:
                    SequenceNode seqNode = (SequenceNode) node;
                    boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
                    this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
                    for (Node item : seqNode.getValue()) {
                        this.serializeNode(item, node);
                    }
                    this.emitter.emit(new SequenceEndEvent(null, null));
                    break;
                case scalar:
                    ScalarNode scalarNode = (ScalarNode) node;
                    Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
                    Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
                    ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
                    ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
                    this.emitter.emit(event);
                    break;
                default:
                    Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
                    boolean implicitM = node.getTag().equals(implicitTag);
                    this.emitter.emit(new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode) node).getFlowStyle()));
                    MappingNode mnode = (MappingNode) node;
                    for (NodeTuple row : mnode.getValue()) {
                        Node key = row.getKeyNode();
                        Node value = row.getValueNode();
                        this.serializeNode(key, mnode);
                        this.serializeNode(value, mnode);
                    }
                    this.emitter.emit(new MappingEndEvent(null, null));
            }
        }
    }
}