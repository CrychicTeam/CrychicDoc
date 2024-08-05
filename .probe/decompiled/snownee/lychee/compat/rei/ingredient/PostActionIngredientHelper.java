package snownee.lychee.compat.rei.ingredient;

import java.util.Objects;
import java.util.stream.Stream;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.compat.rei.REICompat;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.util.CommonProxy;

public class PostActionIngredientHelper implements EntryDefinition<PostAction> {

    @Nullable
    public String getContainingNamespace(EntryStack<PostAction> entry, PostAction value) {
        String modid = value.getType().getRegistryName().getNamespace();
        return CommonProxy.wrapNamespace(modid);
    }

    public Class<PostAction> getValueType() {
        return PostAction.class;
    }

    public EntryType<PostAction> getType() {
        return REICompat.POST_ACTION;
    }

    public EntryRenderer<PostAction> getRenderer() {
        return PostActionIngredientRenderer.INSTANCE;
    }

    @Nullable
    public ResourceLocation getIdentifier(EntryStack<PostAction> entry, PostAction value) {
        return value.getType().getRegistryName();
    }

    public boolean isEmpty(EntryStack<PostAction> entry, PostAction value) {
        return false;
    }

    public PostAction copy(EntryStack<PostAction> entry, PostAction value) {
        return value;
    }

    public PostAction normalize(EntryStack<PostAction> entry, PostAction value) {
        return this.copy(entry, value);
    }

    public PostAction wildcard(EntryStack<PostAction> entry, PostAction value) {
        return this.copy(entry, value);
    }

    public long hash(EntryStack<PostAction> entry, PostAction value, ComparisonContext context) {
        return (long) Objects.hashCode(value);
    }

    public boolean equals(PostAction o1, PostAction o2, ComparisonContext context) {
        return Objects.equals(o1, o2);
    }

    @Nullable
    public EntrySerializer<PostAction> getSerializer() {
        return null;
    }

    public Component asFormattedText(EntryStack<PostAction> entry, PostAction value) {
        return value.getDisplayName();
    }

    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<PostAction> entry, PostAction value) {
        return Stream.of();
    }
}