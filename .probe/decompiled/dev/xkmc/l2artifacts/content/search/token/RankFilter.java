package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import java.util.Comparator;

@SerialClass
public class RankFilter extends ArtifactFilter<RankToken> {

    public RankFilter(IArtifactFilter parent, LangData desc) {
        super(parent, desc, RankToken.ALL_RANKS, (item, t) -> item.item().rank == t.rank());
    }

    @Override
    public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
        return Comparator.comparingInt(e -> -((BaseArtifact) e.item()).rank);
    }

    @Override
    public int getPriority(int j) {
        return this.allEntries.size() - j;
    }

    @Override
    public void prioritize(int ind) {
    }
}