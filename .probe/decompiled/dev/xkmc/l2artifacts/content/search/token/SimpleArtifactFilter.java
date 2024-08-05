package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@SerialClass
public class SimpleArtifactFilter<T extends IArtifactFeature> extends ArtifactFilter<T> {

    private final IArtifactExtractor<T> func;

    private static <T> Collection<T> wrap(Iterable<T> in) {
        ArrayList<T> ans = new ArrayList();
        for (T t : in) {
            ans.add(t);
        }
        return ans;
    }

    public SimpleArtifactFilter(IArtifactFilter parent, LangData desc, Iterable<T> reg, IArtifactExtractor<T> func) {
        super(parent, desc, wrap(reg), (item, t) -> func.get(item.item()) == t);
        this.func = func;
    }

    @Override
    public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
        return Comparator.comparingInt(e -> this.item_priority[this.revMap.get(this.func.get((BaseArtifact) e.item()))]);
    }
}