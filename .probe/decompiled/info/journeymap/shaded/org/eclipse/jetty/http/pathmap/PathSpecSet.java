package info.journeymap.shaded.org.eclipse.jetty.http.pathmap;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.function.Predicate;

public class PathSpecSet extends AbstractSet<String> implements Predicate<String> {

    private final PathMappings<Boolean> specs = new PathMappings<>();

    public boolean test(String s) {
        return this.specs.getMatch(s) != null;
    }

    public int size() {
        return this.specs.size();
    }

    private PathSpec asPathSpec(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof PathSpec) {
            return (PathSpec) o;
        } else {
            return o instanceof String ? PathMappings.asPathSpec((String) o) : PathMappings.asPathSpec(o.toString());
        }
    }

    public boolean add(String s) {
        return this.specs.put(PathMappings.asPathSpec(s), Boolean.TRUE);
    }

    public boolean remove(Object o) {
        return this.specs.remove(this.asPathSpec(o));
    }

    public void clear() {
        this.specs.reset();
    }

    public Iterator<String> iterator() {
        final Iterator<MappedResource<Boolean>> iterator = this.specs.iterator();
        return new Iterator<String>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public String next() {
                return ((MappedResource) iterator.next()).getPathSpec().getDeclaration();
            }
        };
    }
}