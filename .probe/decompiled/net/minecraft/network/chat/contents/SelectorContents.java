package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class SelectorContents implements ComponentContents {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String pattern;

    @Nullable
    private final EntitySelector selector;

    protected final Optional<Component> separator;

    public SelectorContents(String string0, Optional<Component> optionalComponent1) {
        this.pattern = string0;
        this.separator = optionalComponent1;
        this.selector = parseSelector(string0);
    }

    @Nullable
    private static EntitySelector parseSelector(String string0) {
        EntitySelector $$1 = null;
        try {
            EntitySelectorParser $$2 = new EntitySelectorParser(new StringReader(string0));
            $$1 = $$2.parse();
        } catch (CommandSyntaxException var3) {
            LOGGER.warn("Invalid selector component: {}: {}", string0, var3.getMessage());
        }
        return $$1;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Nullable
    public EntitySelector getSelector() {
        return this.selector;
    }

    public Optional<Component> getSeparator() {
        return this.separator;
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack commandSourceStack0, @Nullable Entity entity1, int int2) throws CommandSyntaxException {
        if (commandSourceStack0 != null && this.selector != null) {
            Optional<? extends Component> $$3 = ComponentUtils.updateForEntity(commandSourceStack0, this.separator, entity1, int2);
            return ComponentUtils.formatList(this.selector.findEntities(commandSourceStack0), $$3, Entity::m_5446_);
        } else {
            return Component.empty();
        }
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> formattedTextStyledContentConsumerT0, Style style1) {
        return formattedTextStyledContentConsumerT0.accept(style1, this.pattern);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> formattedTextContentConsumerT0) {
        return formattedTextContentConsumerT0.accept(this.pattern);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof SelectorContents $$1 && this.pattern.equals($$1.pattern) && this.separator.equals($$1.separator)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        int $$0 = this.pattern.hashCode();
        return 31 * $$0 + this.separator.hashCode();
    }

    public String toString() {
        return "pattern{" + this.pattern + "}";
    }
}