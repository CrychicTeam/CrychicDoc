package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public record EntityDataSource(String f_237327_, @Nullable EntitySelector f_237328_) implements DataSource {

    private final String selectorPattern;

    @Nullable
    private final EntitySelector compiledSelector;

    public EntityDataSource(String p_237330_) {
        this(p_237330_, compileSelector(p_237330_));
    }

    public EntityDataSource(String f_237327_, @Nullable EntitySelector f_237328_) {
        this.selectorPattern = f_237327_;
        this.compiledSelector = f_237328_;
    }

    @Nullable
    private static EntitySelector compileSelector(String p_237336_) {
        try {
            EntitySelectorParser $$1 = new EntitySelectorParser(new StringReader(p_237336_));
            return $$1.parse();
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack p_237341_) throws CommandSyntaxException {
        if (this.compiledSelector != null) {
            List<? extends Entity> $$1 = this.compiledSelector.findEntities(p_237341_);
            return $$1.stream().map(NbtPredicate::m_57485_);
        } else {
            return Stream.empty();
        }
    }

    public String toString() {
        return "entity=" + this.selectorPattern;
    }

    public boolean equals(Object p_237339_) {
        if (this == p_237339_) {
            return true;
        } else {
            if (p_237339_ instanceof EntityDataSource $$1 && this.selectorPattern.equals($$1.selectorPattern)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return this.selectorPattern.hashCode();
    }
}