package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.OnInject;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@SerialClass
public abstract class ArtifactFilter<T extends IArtifactFeature> implements IArtifactFilter {

    public final List<T> allEntries;

    protected final Map<T, Integer> revMap = new HashMap();

    private final IArtifactFilter parent;

    private final IArtifactPredicate<T> func;

    private final LangData desc;

    @SerialField
    private boolean[] selected;

    @SerialField
    protected int[] item_priority;

    @SerialField
    protected int sort_priority;

    @Nullable
    private boolean[] availability;

    public ArtifactFilter(IArtifactFilter parent, LangData desc, Collection<T> reg, IArtifactPredicate<T> func) {
        this.parent = parent;
        this.sort_priority = parent instanceof ArtifactFilter a ? a.sort_priority + 1 : 1;
        this.allEntries = new ArrayList(reg);
        this.func = func;
        this.desc = desc;
        this.selected = new boolean[this.allEntries.size()];
        this.item_priority = new int[this.allEntries.size()];
        for (int i = 0; i < this.allEntries.size(); i++) {
            this.selected[i] = true;
            this.item_priority[i] = i + 1;
            this.revMap.put((IArtifactFeature) this.allEntries.get(i), i);
        }
    }

    public void toggle(int ind) {
        this.selected[ind] = this.selected[ind] ^ true;
        if (this.selected[ind]) {
            this.prioritize(ind);
        } else {
            this.item_priority[ind] = 0;
        }
        this.update();
    }

    public void prioritize(int ind) {
        this.item_priority[ind] = 0;
        List<T> list = new ArrayList(this.allEntries.stream().filter(e -> this.selected[this.revMap.get(e)]).toList());
        list.sort(Comparator.comparingInt(e -> this.item_priority[this.revMap.get(e)]));
        for (int i = 0; i < this.allEntries.size(); i++) {
            this.item_priority[i] = 0;
        }
        for (int i = 0; i < list.size(); i++) {
            this.item_priority[this.revMap.get(list.get(i))] = i + 1;
        }
    }

    public boolean getSelected(int i) {
        return this.selected[i];
    }

    public boolean getAvailability(int j) {
        if (this.availability != null) {
            return this.availability[j];
        } else {
            this.availability = new boolean[this.allEntries.size()];
            List<GenericItemStack<BaseArtifact>> list = this.parent.getAvailableArtifacts().toList();
            for (int i = 0; i < this.allEntries.size(); i++) {
                int I = i;
                this.availability[i] = list.stream().anyMatch(e -> this.func.test(e, (T) this.allEntries.get(I)));
            }
            return this.availability[j];
        }
    }

    private boolean isValid(GenericItemStack<BaseArtifact> item) {
        for (int i = 0; i < this.allEntries.size(); i++) {
            if (this.selected[i] && this.func.test(item, (T) this.allEntries.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public void update() {
        this.parent.update();
    }

    protected void clearCache() {
        this.availability = null;
    }

    @Override
    public Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts() {
        return this.parent.getAvailableArtifacts().filter(this::isValid);
    }

    public Component getDescription() {
        return this.desc.get().withStyle(ChatFormatting.GRAY);
    }

    public int getPriority(int j) {
        return this.item_priority[j];
    }

    public int priority() {
        return this.sort_priority;
    }

    @OnInject
    public void postInject() {
        int size = this.allEntries.size();
        if (this.item_priority.length < size) {
            this.item_priority = Arrays.copyOf(this.item_priority, size);
        }
        if (this.selected.length < size) {
            this.selected = Arrays.copyOf(this.selected, size);
        }
    }
}