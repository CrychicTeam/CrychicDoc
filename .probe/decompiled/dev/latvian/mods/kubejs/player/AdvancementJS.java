package dev.latvian.mods.kubejs.player;

import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class AdvancementJS {

    public final Advancement advancement;

    public AdvancementJS(Advancement a) {
        this.advancement = a;
    }

    public boolean equals(Object o) {
        return o == this || o instanceof AdvancementJS && this.advancement.equals(((AdvancementJS) o).advancement);
    }

    public int hashCode() {
        return this.advancement.hashCode();
    }

    public String toString() {
        return this.getId().toString();
    }

    public ResourceLocation id() {
        return this.getId();
    }

    public ResourceLocation getId() {
        return this.advancement.getId();
    }

    @Nullable
    public AdvancementJS getParent() {
        return this.advancement.getParent() == null ? null : new AdvancementJS(this.advancement.getParent());
    }

    public Set<AdvancementJS> getChildren() {
        Set<AdvancementJS> set = new LinkedHashSet();
        for (Advancement a : this.advancement.getChildren()) {
            set.add(new AdvancementJS(a));
        }
        return set;
    }

    public void addChild(AdvancementJS a) {
        this.advancement.addChild(a.advancement);
    }

    public Component getDisplayText() {
        return this.advancement.getChatComponent();
    }

    public boolean hasDisplay() {
        return this.advancement.getDisplay() != null;
    }

    public Component getTitle() {
        return (Component) (this.advancement.getDisplay() != null ? this.advancement.getDisplay().getTitle() : Component.empty());
    }

    public Component getDescription() {
        return (Component) (this.advancement.getDisplay() != null ? this.advancement.getDisplay().getDescription() : Component.empty());
    }
}