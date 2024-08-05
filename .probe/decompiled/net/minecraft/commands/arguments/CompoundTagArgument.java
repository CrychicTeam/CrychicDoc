package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

public class CompoundTagArgument implements ArgumentType<CompoundTag> {

    private static final Collection<String> EXAMPLES = Arrays.asList("{}", "{foo=bar}");

    private CompoundTagArgument() {
    }

    public static CompoundTagArgument compoundTag() {
        return new CompoundTagArgument();
    }

    public static <S> CompoundTag getCompoundTag(CommandContext<S> commandContextS0, String string1) {
        return (CompoundTag) commandContextS0.getArgument(string1, CompoundTag.class);
    }

    public CompoundTag parse(StringReader stringReader0) throws CommandSyntaxException {
        return new TagParser(stringReader0).readStruct();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}