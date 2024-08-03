package net.mehvahdjukaar.supplementaries.common.utils;

import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface BlockPredicate extends Predicate<BlockState> {

    Codec<BlockPredicate> CODEC = Codec.STRING.xmap(BlockPredicate::create, blockStatePredicate -> {
        if (blockStatePredicate instanceof BlockPredicate.Tag tp) {
            return "#" + tp.tag.location();
        } else if (blockStatePredicate instanceof BlockPredicate.Block bp) {
            return bp.id.toString();
        } else {
            throw new IllegalArgumentException("Must either be Tag or Block predicate");
        }
    });

    @NotNull
    static BlockPredicate create(String s) {
        if (s.startsWith("#")) {
            TagKey<net.minecraft.world.level.block.Block> tag = TagKey.create(Registries.BLOCK, new ResourceLocation(s.replace("#", "")));
            return new BlockPredicate.Tag(tag);
        } else {
            ResourceLocation id = new ResourceLocation(s);
            return new BlockPredicate.Block(id);
        }
    }

    public static record Block(ResourceLocation id) implements BlockPredicate {

        public boolean test(BlockState state) {
            return BuiltInRegistries.BLOCK.getKey(state.m_60734_()).equals(this.id);
        }
    }

    public static record Tag(TagKey<net.minecraft.world.level.block.Block> tag) implements BlockPredicate {

        public boolean test(BlockState state) {
            return state.m_204336_(this.tag);
        }
    }
}