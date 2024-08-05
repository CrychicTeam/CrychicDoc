package com.blamejared.searchables.api.autcomplete;

import com.blamejared.searchables.api.SearchableType;
import com.blamejared.searchables.api.SearchablesConstants;
import com.blamejared.searchables.api.TokenRange;
import com.blamejared.searchables.api.formatter.FormattingVisitor;
import com.blamejared.searchables.mixin.AccessEditBox;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class AutoCompletingEditBox<T> extends EditBox {

    private final FormattingVisitor formattingVisitor;

    private final CompletionVisitor completionVisitor;

    private final AutoCompletingEditBox.DelegatingConsumers<String> responders = new AutoCompletingEditBox.DelegatingConsumers<>();

    private final AutoComplete<T> autoComplete;

    public AutoCompletingEditBox(Font font, int x, int y, int width, int height, Component message, SearchableType<T> type, Supplier<List<T>> entries) {
        this(font, x, y, width, height, null, message, type, entries);
    }

    public AutoCompletingEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox thisBox, Component message, SearchableType<T> type, Supplier<List<T>> entries) {
        super(font, x, y, width, height, thisBox, message);
        this.m_94199_(Integer.MAX_VALUE);
        this.formattingVisitor = new FormattingVisitor(type);
        this.completionVisitor = new CompletionVisitor();
        this.autoComplete = new AutoComplete<>(type, this, entries, x, y + 2 + height, width, 9 + 2);
        this.m_257771_(SearchablesConstants.COMPONENT_SEARCH);
        this.m_94149_(this.formattingVisitor);
        this.setResponder(this.responders);
        this.addResponder(this.formattingVisitor);
        this.addResponder(this.completionVisitor);
        this.addResponder(this.autoComplete);
    }

    @Override
    public boolean mouseClicked(double xpos, double ypos, int button) {
        if (this.m_93696_() && this.autoComplete.mouseClicked(xpos, ypos, button)) {
            return true;
        } else if ((this.m_5953_(xpos, ypos) || this.autoComplete().isMouseOver(xpos, ypos)) && button == 1) {
            this.m_94144_("");
            return true;
        } else {
            return super.m_6375_(xpos, ypos, button);
        }
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        switch(key) {
            case 257:
                this.autoComplete().insertSuggestion();
                return true;
            case 258:
            case 259:
            case 260:
            case 261:
            case 262:
            case 263:
            default:
                return super.keyPressed(key, scancode, mods);
            case 264:
                this.autoComplete().scrollDown();
                return true;
            case 265:
                this.autoComplete().scrollUp();
                return true;
            case 266:
                this.autoComplete.scrollUp(this.autoComplete().maxSuggestions());
                return true;
            case 267:
                this.autoComplete.scrollDown(this.autoComplete().maxSuggestions());
                return true;
        }
    }

    public void deleteChars(TokenRange range) {
        if (!this.m_94155_().isEmpty() && !range.isEmpty()) {
            String newValue = range.delete(this.m_94155_());
            if (this.getFilter().test(newValue)) {
                this.m_94144_(newValue);
                this.m_94192_(range.start());
            }
        }
    }

    public Predicate<String> getFilter() {
        return ((AccessEditBox) this).searchables$getFilter();
    }

    @Nullable
    public Consumer<String> getResponder() {
        return ((AccessEditBox) this).searchables$getResponder();
    }

    @Deprecated
    @Override
    public void setResponder(Consumer<String> responder) {
        if (this.getResponder() == null) {
            super.setResponder(this.responders);
        } else {
            this.addResponder(responder);
        }
    }

    public void addResponder(Consumer<String> responder) {
        this.responders.addConsumer(responder);
    }

    public FormattingVisitor formattingVisitor() {
        return this.formattingVisitor;
    }

    public CompletionVisitor completionVisitor() {
        return this.completionVisitor;
    }

    public AutoComplete<T> autoComplete() {
        return this.autoComplete;
    }

    private static class DelegatingConsumers<T> implements Consumer<T> {

        private final List<Consumer<T>> consumers = new ArrayList();

        public void accept(T t) {
            this.consumers.forEach(tConsumer -> tConsumer.accept(t));
        }

        public void addConsumer(Consumer<T> consumer) {
            this.consumers.add(consumer);
        }
    }
}