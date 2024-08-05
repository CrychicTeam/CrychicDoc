package vazkii.patchouli.client.book;

import java.util.Comparator;
import java.util.stream.Stream;

public abstract class AbstractReadStateHolder {

    EntryDisplayState readState;

    boolean readStateDirty = true;

    public EntryDisplayState getReadState() {
        if (this.readStateDirty) {
            this.readState = this.computeReadState();
            this.readStateDirty = false;
        }
        return this.readState;
    }

    public void markReadStateDirty() {
        this.readStateDirty = true;
    }

    protected abstract EntryDisplayState computeReadState();

    public static EntryDisplayState mostImportantState(Stream<EntryDisplayState> stream) {
        return (EntryDisplayState) stream.min(Comparator.comparingInt(Enum::ordinal)).orElse(EntryDisplayState.DEFAULT_TYPE);
    }
}