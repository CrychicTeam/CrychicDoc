package me.jellysquid.mods.sodium.client.gui.options;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

public class OptionGroup {

    public static final OptionIdentifier<Void> DEFAULT_ID = OptionIdentifier.create("embeddium", "empty");

    private final ImmutableList<Option<?>> options;

    public final OptionIdentifier<Void> id;

    private OptionGroup(OptionIdentifier<Void> id, ImmutableList<Option<?>> options) {
        this.id = id;
        this.options = options;
    }

    public OptionIdentifier<Void> getId() {
        return this.id;
    }

    public static OptionGroup.Builder createBuilder() {
        return new OptionGroup.Builder();
    }

    public ImmutableList<Option<?>> getOptions() {
        return this.options;
    }

    public static class Builder {

        private final List<Option<?>> options = new ArrayList();

        private OptionIdentifier<Void> id;

        public OptionGroup.Builder setId(ResourceLocation id) {
            this.id = OptionIdentifier.create(id);
            return this;
        }

        public OptionGroup.Builder setId(OptionIdentifier<Void> id) {
            this.id = id;
            return this;
        }

        public OptionGroup.Builder add(Option<?> option) {
            this.options.add(option);
            return this;
        }

        public OptionGroup build() {
            if (this.options.isEmpty()) {
                SodiumClientMod.logger().warn("OptionGroup must contain at least one option. ignoring empty group...");
            }
            if (this.id == null) {
                this.id = OptionGroup.DEFAULT_ID;
            }
            OptionGroupConstructionEvent.BUS.post(new OptionGroupConstructionEvent(this.id, this.options));
            return new OptionGroup(this.id, ImmutableList.copyOf(this.options));
        }
    }
}