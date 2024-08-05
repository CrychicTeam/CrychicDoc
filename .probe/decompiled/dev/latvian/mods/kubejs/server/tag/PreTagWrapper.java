package dev.latvian.mods.kubejs.server.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public class PreTagWrapper extends TagWrapper {

    public final PreTagEventJS preEvent;

    public final ResourceLocation id;

    public PreTagWrapper(PreTagEventJS e, ResourceLocation i) {
        super(e, i, null);
        this.preEvent = e;
        this.id = i;
    }

    @Override
    public TagWrapper add(Object... filters) {
        this.preEvent.actions.add(new PreTagWrapper.AddAction(this.id, filters));
        return this;
    }

    @Override
    public TagWrapper remove(Object... filters) {
        this.preEvent.actions.add(new PreTagWrapper.RemoveAction(this.id, filters));
        return this;
    }

    @Override
    public TagWrapper removeAll() {
        this.preEvent.actions.add(new PreTagWrapper.RemoveAllAction(this.id));
        return this;
    }

    @Override
    public List<ResourceLocation> getObjectIds() {
        this.preEvent.invalid = true;
        return new ArrayList(0);
    }

    public static record AddAction(ResourceLocation tag, Object[] filters) implements Consumer<TagEventJS> {

        public void accept(TagEventJS e) {
            e.add(this.tag, this.filters);
        }
    }

    public static record RemoveAction(ResourceLocation tag, Object[] filters) implements Consumer<TagEventJS> {

        public void accept(TagEventJS e) {
            e.remove(this.tag, this.filters);
        }
    }

    public static record RemoveAllAction(ResourceLocation tag) implements Consumer<TagEventJS> {

        public void accept(TagEventJS e) {
            e.removeAll(this.tag);
        }
    }
}