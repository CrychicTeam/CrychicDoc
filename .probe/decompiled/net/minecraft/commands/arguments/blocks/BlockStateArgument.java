package net.minecraft.commands.arguments.blocks;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class BlockStateArgument implements ArgumentType<BlockInput> {

    private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}");

    private final HolderLookup<Block> blocks;

    public BlockStateArgument(CommandBuildContext commandBuildContext0) {
        this.blocks = commandBuildContext0.holderLookup(Registries.BLOCK);
    }

    public static BlockStateArgument block(CommandBuildContext commandBuildContext0) {
        return new BlockStateArgument(commandBuildContext0);
    }

    public BlockInput parse(StringReader stringReader0) throws CommandSyntaxException {
        BlockStateParser.BlockResult $$1 = BlockStateParser.parseForBlock(this.blocks, stringReader0, true);
        return new BlockInput($$1.blockState(), $$1.properties().keySet(), $$1.nbt());
    }

    public static BlockInput getBlock(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (BlockInput) commandContextCommandSourceStack0.getArgument(string1, BlockInput.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return BlockStateParser.fillSuggestions(this.blocks, suggestionsBuilder1, false, true);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}