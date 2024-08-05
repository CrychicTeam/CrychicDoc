package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.extensions.compactnotation;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.constructor.Construct;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.error.YAMLException;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.introspector.Property;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.MappingNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.Node;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.NodeTuple;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.ScalarNode;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.SequenceNode;

public class CompactConstructor extends me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.constructor.Constructor {

    private static final Pattern GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");

    private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");

    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");

    private Construct compactConstruct;

    protected Object constructCompactFormat(ScalarNode node, CompactData data) {
        try {
            Object obj = this.createInstance(node, data);
            Map<String, Object> properties = new HashMap(data.getProperties());
            this.setProperties(obj, properties);
            return obj;
        } catch (Exception var5) {
            throw new YAMLException(var5);
        }
    }

    protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
        Class<?> clazz = this.getClassForName(data.getPrefix());
        Class<?>[] args = new Class[data.getArguments().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = String.class;
        }
        Constructor<?> c = clazz.getDeclaredConstructor(args);
        c.setAccessible(true);
        return c.newInstance(data.getArguments().toArray());
    }

    protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
        if (data == null) {
            throw new NullPointerException("Data for Compact Object Notation cannot be null.");
        } else {
            for (Entry<String, Object> entry : data.entrySet()) {
                String key = (String) entry.getKey();
                Property property = this.getPropertyUtils().getProperty(bean.getClass(), key);
                try {
                    property.set(bean, entry.getValue());
                } catch (IllegalArgumentException var8) {
                    throw new YAMLException("Cannot set property='" + key + "' with value='" + data.get(key) + "' (" + data.get(key).getClass() + ") in " + bean);
                }
            }
        }
    }

    public CompactData getCompactData(String scalar) {
        if (!scalar.endsWith(")")) {
            return null;
        } else if (scalar.indexOf(40) < 0) {
            return null;
        } else {
            Matcher m = FIRST_PATTERN.matcher(scalar);
            if (m.matches()) {
                String tag = m.group(1).trim();
                String content = m.group(3);
                CompactData data = new CompactData(tag);
                if (content.length() == 0) {
                    return data;
                } else {
                    String[] names = content.split("\\s*,\\s*");
                    for (int i = 0; i < names.length; i++) {
                        String section = names[i];
                        if (section.indexOf(61) < 0) {
                            data.getArguments().add(section);
                        } else {
                            Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
                            if (!sm.matches()) {
                                return null;
                            }
                            String name = sm.group(1);
                            String value = sm.group(2).trim();
                            data.getProperties().put(name, value);
                        }
                    }
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    private Construct getCompactConstruct() {
        if (this.compactConstruct == null) {
            this.compactConstruct = this.createCompactConstruct();
        }
        return this.compactConstruct;
    }

    protected Construct createCompactConstruct() {
        return new CompactConstructor.ConstructCompactObject();
    }

    @Override
    protected Construct getConstructor(Node node) {
        if (node instanceof MappingNode) {
            MappingNode mnode = (MappingNode) node;
            List<NodeTuple> list = mnode.getValue();
            if (list.size() == 1) {
                NodeTuple tuple = (NodeTuple) list.get(0);
                Node key = tuple.getKeyNode();
                if (key instanceof ScalarNode) {
                    ScalarNode scalar = (ScalarNode) key;
                    if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
                        return this.getCompactConstruct();
                    }
                }
            }
        } else if (node instanceof ScalarNode) {
            ScalarNode scalar = (ScalarNode) node;
            if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
                return this.getCompactConstruct();
            }
        }
        return super.getConstructor(node);
    }

    protected void applySequence(Object bean, List<?> value) {
        try {
            Property property = this.getPropertyUtils().getProperty(bean.getClass(), this.getSequencePropertyName(bean.getClass()));
            property.set(bean, value);
        } catch (Exception var4) {
            throw new YAMLException(var4);
        }
    }

    protected String getSequencePropertyName(Class<?> bean) {
        Set<Property> properties = this.getPropertyUtils().getProperties((Class<? extends Object>) bean);
        Iterator<Property> iterator = properties.iterator();
        while (iterator.hasNext()) {
            Property property = (Property) iterator.next();
            if (!List.class.isAssignableFrom(property.getType())) {
                iterator.remove();
            }
        }
        if (properties.size() == 0) {
            throw new YAMLException("No list property found in " + bean);
        } else if (properties.size() > 1) {
            throw new YAMLException("Many list properties found in " + bean + "; Please override getSequencePropertyName() to specify which property to use.");
        } else {
            return ((Property) properties.iterator().next()).getName();
        }
    }

    public class ConstructCompactObject extends me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.constructor.Constructor.ConstructMapping {

        @Override
        public void construct2ndStep(Node node, Object object) {
            MappingNode mnode = (MappingNode) node;
            NodeTuple nodeTuple = (NodeTuple) mnode.getValue().iterator().next();
            Node valueNode = nodeTuple.getValueNode();
            if (valueNode instanceof MappingNode) {
                valueNode.setType(object.getClass());
                this.constructJavaBean2ndStep((MappingNode) valueNode, object);
            } else {
                CompactConstructor.this.applySequence(object, CompactConstructor.this.constructSequence((SequenceNode) valueNode));
            }
        }

        @Override
        public Object construct(Node node) {
            ScalarNode tmpNode;
            if (node instanceof MappingNode) {
                MappingNode mnode = (MappingNode) node;
                NodeTuple nodeTuple = (NodeTuple) mnode.getValue().iterator().next();
                node.setTwoStepsConstruction(true);
                tmpNode = (ScalarNode) nodeTuple.getKeyNode();
            } else {
                tmpNode = (ScalarNode) node;
            }
            CompactData data = CompactConstructor.this.getCompactData(tmpNode.getValue());
            return data == null ? CompactConstructor.this.constructScalar(tmpNode) : CompactConstructor.this.constructCompactFormat(tmpNode, data);
        }
    }
}