package com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.block;

import com.almostreliable.summoningrituals.compat.viewer.rei.AlmostREI;
import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class BlockReferenceDefinition implements EntryDefinition<BlockReference> {

    private final REIBlockReferenceRenderer renderer;

    public BlockReferenceDefinition(int size) {
        this.renderer = new REIBlockReferenceRenderer(size);
    }

    public Class<BlockReference> getValueType() {
        return BlockReference.class;
    }

    public EntryType<BlockReference> getType() {
        return AlmostREI.BLOCK_REFERENCE;
    }

    public EntryRenderer<BlockReference> getRenderer() {
        return this.renderer;
    }

    @Nullable
    public ResourceLocation getIdentifier(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return Platform.getId(blockReference.getDisplayState().m_60734_());
    }

    public boolean isEmpty(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return false;
    }

    public BlockReference copy(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return BlockReference.fromJson(blockReference.toJson());
    }

    public BlockReference normalize(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return this.copy(entry, blockReference);
    }

    public BlockReference wildcard(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return this.copy(entry, blockReference);
    }

    public long hash(EntryStack<BlockReference> entry, BlockReference blockReference, ComparisonContext context) {
        int code = Platform.getId(blockReference.getDisplayState().m_60734_()).hashCode();
        code = 31 * code + blockReference.getDisplayState().hashCode();
        return (long) code;
    }

    public boolean equals(BlockReference blockReference1, BlockReference blockReference2, ComparisonContext context) {
        return blockReference1.test(blockReference2.getDisplayState());
    }

    @Nullable
    public EntrySerializer<BlockReference> getSerializer() {
        return null;
    }

    public Component asFormattedText(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return blockReference.getDisplayState().m_60734_().getName();
    }

    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<BlockReference> entry, BlockReference blockReference) {
        return Platform.getTagsFor(blockReference.getDisplayState().m_60734_());
    }
}