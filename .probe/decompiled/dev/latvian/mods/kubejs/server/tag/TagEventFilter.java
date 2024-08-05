package dev.latvian.mods.kubejs.server.tag;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.ExtraCodecs;

public interface TagEventFilter {

    static TagEventFilter of(TagEventJS event, Object o) {
        if (o instanceof TagEventFilter) {
            return (TagEventFilter) o;
        } else if (o instanceof Collection<?> list) {
            List<TagEventFilter> filters = list.stream().map(o1 -> of(event, o1)).flatMap(TagEventFilter::unwrap).filter(f -> f != TagEventFilter.Empty.INSTANCE).toList();
            return (TagEventFilter) (filters.isEmpty() ? TagEventFilter.Empty.INSTANCE : (filters.size() == 1 ? (TagEventFilter) filters.get(0) : new TagEventFilter.Or(filters)));
        } else {
            Pattern regex = UtilsJS.parseRegex(o);
            if (regex != null) {
                return new TagEventFilter.RegEx(regex);
            } else {
                String s = o.toString().trim();
                if (!s.isEmpty()) {
                    return (TagEventFilter) (switch(s.charAt(0)) {
                        case '#' ->
                            new TagEventFilter.Tag(event.get(new ResourceLocation(s.substring(1))));
                        case '@' ->
                            new TagEventFilter.Namespace(s.substring(1));
                        default ->
                            new TagEventFilter.ID(new ResourceLocation(s));
                    });
                } else {
                    return TagEventFilter.Empty.INSTANCE;
                }
            }
        }
    }

    static TagEventFilter unwrap(TagEventJS event, Object[] array) {
        return array.length == 1 ? of(event, array[0]) : of(event, Arrays.asList(array));
    }

    boolean testElementId(ResourceLocation var1);

    default boolean testTagOrElementLocation(ExtraCodecs.TagOrElementLocation element) {
        return !element.tag() && this.testElementId(element.id());
    }

    default Stream<TagEventFilter> unwrap() {
        return Stream.of(this);
    }

    default int add(TagWrapper wrapper) {
        int count = 0;
        for (ResourceLocation id : wrapper.event.getElementIds()) {
            if (this.testElementId(id)) {
                wrapper.entries.add(new TagLoader.EntryWithSource(TagEntry.element(id), "KubeJS Custom Tags"));
                count++;
            }
        }
        return count;
    }

    default int remove(TagWrapper wrapper) {
        int count = 0;
        Iterator<TagLoader.EntryWithSource> itr = wrapper.entries.iterator();
        while (itr.hasNext()) {
            TagLoader.EntryWithSource it = (TagLoader.EntryWithSource) itr.next();
            if (!it.entry().tag && this.testElementId(it.entry().id)) {
                itr.remove();
                count++;
            }
        }
        return count;
    }

    public static class Empty implements TagEventFilter {

        public static final TagEventFilter.Empty INSTANCE = new TagEventFilter.Empty();

        @Override
        public boolean testElementId(ResourceLocation resourceLocation) {
            return false;
        }

        @Override
        public boolean testTagOrElementLocation(ExtraCodecs.TagOrElementLocation element) {
            return false;
        }

        @Override
        public int add(TagWrapper wrapper) {
            return 0;
        }

        @Override
        public int remove(TagWrapper wrapper) {
            return 0;
        }
    }

    public static record ID(ResourceLocation id) implements TagEventFilter {

        @Override
        public boolean testElementId(ResourceLocation id) {
            return this.id.equals(id);
        }

        @Override
        public int add(TagWrapper wrapper) {
            if (wrapper.event.getElementIds().contains(this.id)) {
                wrapper.entries.add(new TagLoader.EntryWithSource(TagEntry.element(this.id), "KubeJS Custom Tags"));
                return 1;
            } else {
                String msg = "No such element %s in registry %s".formatted(this.id, wrapper.event.registry);
                if (DevProperties.get().strictTags) {
                    throw new EmptyTagTargetException(msg);
                } else {
                    if (DevProperties.get().logSkippedTags) {
                        ConsoleJS.SERVER.warn(msg);
                    }
                    return 0;
                }
            }
        }
    }

    public static record Namespace(String namespace) implements TagEventFilter {

        @Override
        public boolean testElementId(ResourceLocation id) {
            return id.getNamespace().equals(this.namespace);
        }
    }

    public static record Or(List<TagEventFilter> filters) implements TagEventFilter {

        @Override
        public boolean testElementId(ResourceLocation resourceLocation) {
            for (TagEventFilter filter : this.filters) {
                if (filter.testElementId(resourceLocation)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean testTagOrElementLocation(ExtraCodecs.TagOrElementLocation element) {
            for (TagEventFilter filter : this.filters) {
                if (filter.testTagOrElementLocation(element)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Stream<TagEventFilter> unwrap() {
            return this.filters.stream();
        }

        @Override
        public int add(TagWrapper wrapper) {
            int count = 0;
            for (TagEventFilter filter : this.filters) {
                count += filter.add(wrapper);
            }
            return count;
        }

        @Override
        public int remove(TagWrapper wrapper) {
            int count = 0;
            for (TagEventFilter filter : this.filters) {
                count += filter.remove(wrapper);
            }
            return count;
        }
    }

    public static record RegEx(Pattern pattern) implements TagEventFilter {

        @Override
        public boolean testElementId(ResourceLocation id) {
            return this.pattern.matcher(id.toString()).find();
        }
    }

    public static record Tag(TagWrapper tag) implements TagEventFilter {

        @Override
        public boolean testElementId(ResourceLocation id) {
            return false;
        }

        @Override
        public boolean testTagOrElementLocation(ExtraCodecs.TagOrElementLocation element) {
            return element.tag() && this.tag.id.equals(element.id());
        }

        @Override
        public int add(TagWrapper wrapper) {
            wrapper.entries.add(new TagLoader.EntryWithSource(TagEntry.tag(this.tag.id), "KubeJS Custom Tags"));
            return 1;
        }

        @Override
        public int remove(TagWrapper wrapper) {
            int count = 0;
            Iterator<TagLoader.EntryWithSource> itr = wrapper.entries.iterator();
            while (itr.hasNext()) {
                TagLoader.EntryWithSource it = (TagLoader.EntryWithSource) itr.next();
                if (it.entry().tag && it.entry().id.equals(this.tag.id)) {
                    itr.remove();
                    count++;
                }
            }
            return count;
        }
    }
}