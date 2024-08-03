package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class NbtContents implements ComponentContents {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final boolean interpreting;

    private final Optional<Component> separator;

    private final String nbtPathPattern;

    private final DataSource dataSource;

    @Nullable
    protected final NbtPathArgument.NbtPath compiledNbtPath;

    public NbtContents(String string0, boolean boolean1, Optional<Component> optionalComponent2, DataSource dataSource3) {
        this(string0, compileNbtPath(string0), boolean1, optionalComponent2, dataSource3);
    }

    private NbtContents(String string0, @Nullable NbtPathArgument.NbtPath nbtPathArgumentNbtPath1, boolean boolean2, Optional<Component> optionalComponent3, DataSource dataSource4) {
        this.nbtPathPattern = string0;
        this.compiledNbtPath = nbtPathArgumentNbtPath1;
        this.interpreting = boolean2;
        this.separator = optionalComponent3;
        this.dataSource = dataSource4;
    }

    @Nullable
    private static NbtPathArgument.NbtPath compileNbtPath(String string0) {
        try {
            return new NbtPathArgument().parse(new StringReader(string0));
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    public String getNbtPath() {
        return this.nbtPathPattern;
    }

    public boolean isInterpreting() {
        return this.interpreting;
    }

    public Optional<Component> getSeparator() {
        return this.separator;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof NbtContents $$1 && this.dataSource.equals($$1.dataSource) && this.separator.equals($$1.separator) && this.interpreting == $$1.interpreting && this.nbtPathPattern.equals($$1.nbtPathPattern)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        int $$0 = this.interpreting ? 1 : 0;
        $$0 = 31 * $$0 + this.separator.hashCode();
        $$0 = 31 * $$0 + this.nbtPathPattern.hashCode();
        return 31 * $$0 + this.dataSource.hashCode();
    }

    public String toString() {
        return "nbt{" + this.dataSource + ", interpreting=" + this.interpreting + ", separator=" + this.separator + "}";
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack commandSourceStack0, @Nullable Entity entity1, int int2) throws CommandSyntaxException {
        if (commandSourceStack0 != null && this.compiledNbtPath != null) {
            Stream<String> $$3 = this.dataSource.getData(commandSourceStack0).flatMap(p_237417_ -> {
                try {
                    return this.compiledNbtPath.get(p_237417_).stream();
                } catch (CommandSyntaxException var3x) {
                    return Stream.empty();
                }
            }).map(Tag::m_7916_);
            if (this.interpreting) {
                Component $$4 = (Component) DataFixUtils.orElse(ComponentUtils.updateForEntity(commandSourceStack0, this.separator, entity1, int2), ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR);
                return (MutableComponent) $$3.flatMap(p_237408_ -> {
                    try {
                        MutableComponent $$4x = Component.Serializer.fromJson(p_237408_);
                        return Stream.of(ComponentUtils.updateForEntity(commandSourceStack0, $$4x, entity1, int2));
                    } catch (Exception var5x) {
                        LOGGER.warn("Failed to parse component: {}", p_237408_, var5x);
                        return Stream.of();
                    }
                }).reduce((p_237420_, p_237421_) -> p_237420_.append($$4).append(p_237421_)).orElseGet(Component::m_237119_);
            } else {
                return (MutableComponent) ComponentUtils.updateForEntity(commandSourceStack0, this.separator, entity1, int2).map(p_237415_ -> (MutableComponent) $$3.map(Component::m_237113_).reduce((p_237424_, p_237425_) -> p_237424_.append(p_237415_).append(p_237425_)).orElseGet(Component::m_237119_)).orElseGet(() -> Component.literal((String) $$3.collect(Collectors.joining(", "))));
            }
        } else {
            return Component.empty();
        }
    }
}