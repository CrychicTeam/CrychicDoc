package icyllis.modernui.resources;

import icyllis.modernui.annotation.NonNull;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jetbrains.annotations.Contract;

public class ResourceParser {

    @Contract(pure = true)
    public static int FormatTypeNoEnumOrFlags(@NonNull String s) {
        return switch(s) {
            case "reference" ->
                1;
            case "string" ->
                2;
            case "integer" ->
                4;
            case "boolean" ->
                8;
            case "color" ->
                16;
            case "float" ->
                32;
            case "dimension" ->
                64;
            case "fraction" ->
                128;
            default ->
                0;
        };
    }

    @Contract(pure = true)
    public static int FormatType(@NonNull String s) {
        return switch(s) {
            case "enum" ->
                65536;
            case "flags" ->
                131072;
            default ->
                FormatTypeNoEnumOrFlags(s);
        };
    }

    public static int FormatAttribute(@NonNull String s) {
        int mask = 0;
        for (String part : s.split("\\|")) {
            String trim = part.trim();
            int type = FormatType(trim);
            if (type == 0) {
                return 0;
            }
            mask |= type;
        }
        return mask;
    }

    public boolean Parse(XMLStreamReader reader) {
        try {
            return reader.nextTag() == 1 && reader.getNamespaceURI() == null && reader.getLocalName().equals("resources") ? this.ParseResources(reader) : false;
        } catch (XMLStreamException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws XMLStreamException {
        boolean success = new ResourceParser().Parse(XMLInputFactory.newFactory().createXMLStreamReader(new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n</resources>\n")));
        System.out.println(success);
    }

    public boolean ParseResources(XMLStreamReader reader) {
        return true;
    }

    public boolean Attr(XMLStreamReader reader, ResourceParser.ParsedResource out) {
        return this.Attr0(reader, out, false);
    }

    public boolean Attr0(XMLStreamReader reader, ResourceParser.ParsedResource out_resource, boolean weak) {
        int type_mask = 0;
        String maybe = ResourceUtils.findAttribute(reader, "format");
        if (maybe != null) {
            type_mask = FormatAttribute(maybe);
            if (type_mask == 0) {
                return false;
            }
        }
        boolean hasMin = false;
        boolean hasMax = false;
        int min = 0;
        int max = 0;
        maybe = ResourceUtils.findAttribute(reader, "min");
        if (maybe != null) {
            try {
                min = Integer.decode(maybe);
                hasMin = true;
            } catch (NumberFormatException var12) {
                return false;
            }
        }
        maybe = ResourceUtils.findAttribute(reader, "max");
        if (maybe != null) {
            try {
                max = Integer.decode(maybe);
                hasMax = true;
            } catch (NumberFormatException var11) {
                return false;
            }
        }
        if ((hasMin || hasMax) && (type_mask & 4) == 0) {
            return false;
        } else {
            ResourceValues.Attribute attribute = new ResourceValues.Attribute(type_mask != 0 ? type_mask : '\uffff');
            if (hasMin) {
                attribute.min_int = min;
            }
            if (hasMax) {
                attribute.max_int = max;
            }
            attribute.weak = weak;
            out_resource.value = attribute;
            return true;
        }
    }

    public boolean DeclareStyleable(XMLStreamReader reader, ResourceParser.ParsedResource out_resource) {
        out_resource.name = new Resource.ResourceName();
        out_resource.name.setType(24);
        ResourceValues.Styleable styleable = new ResourceValues.Styleable();
        try {
            while (reader.nextTag() != 2) {
                String element_namespace = reader.getNamespaceURI();
                String element_name = reader.getLocalName();
                if (element_namespace != null || !element_name.equals("attr")) {
                    return false;
                }
                String maybe = ResourceUtils.findNonEmptyAttribute(reader, "name");
                if (maybe == null) {
                    return false;
                }
                ResourceValues.Reference ref = ResourceUtils.parseXmlAttributeName(maybe);
                ResourceParser.ParsedResource resource = new ResourceParser.ParsedResource();
                resource.name = ref.name;
                if (!this.Attr0(reader, resource, true)) {
                    return false;
                }
                if (styleable.entries == null) {
                    styleable.entries = new ArrayList();
                }
                styleable.entries.add(ref);
                ResourceValues.Attribute attr = (ResourceValues.Attribute) resource.value;
                if (attr.type_mask != 65535) {
                    if (out_resource.child_resources == null) {
                        out_resource.child_resources = new ArrayList();
                    }
                    out_resource.child_resources.add(resource);
                }
                if (reader.nextTag() != 2) {
                    return false;
                }
            }
        } catch (XMLStreamException var10) {
            return false;
        }
        out_resource.value = styleable;
        return true;
    }

    public static class ParsedResource {

        public Resource.ResourceName name;

        public int id;

        public boolean staged_api = false;

        public boolean allow_new = false;

        public ResourceValues.Value value;

        public List<ResourceParser.ParsedResource> child_resources;

        public String toString() {
            return "ParsedResource{name=" + this.name + ", id=" + this.id + ", staged_api=" + this.staged_api + ", allow_new=" + this.allow_new + ", value=" + this.value + ", child_resources=" + this.child_resources + "}";
        }
    }
}