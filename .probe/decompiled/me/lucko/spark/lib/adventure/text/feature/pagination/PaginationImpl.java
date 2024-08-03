package me.lucko.spark.lib.adventure.text.feature.pagination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.examination.string.StringExaminer;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

final class PaginationImpl<T> implements Examinable, Pagination<T> {

    private static final int LINE_CHARACTER_LENGTH = 1;

    private final int width;

    private final int resultsPerPage;

    private final Pagination.Renderer renderer;

    private final char lineCharacter;

    private final Style lineStyle;

    private final char previousPageButtonCharacter;

    private final Style previousPageButtonStyle;

    private final char nextPageButtonCharacter;

    private final Style nextPageButtonStyle;

    private final Component title;

    private final Pagination.Renderer.RowRenderer<T> rowRenderer;

    private final Pagination.PageCommandFunction pageCommand;

    PaginationImpl(final int width, final int resultsPerPage, @NotNull final Pagination.Renderer renderer, final char lineCharacter, @NotNull final Style lineStyle, final char previousPageButtonCharacter, @NotNull final Style previousPageButtonStyle, final char nextPageButtonCharacter, @NotNull final Style nextPageButtonStyle, @NotNull final Component title, @NotNull final Pagination.Renderer.RowRenderer<T> rowRenderer, @NotNull final Pagination.PageCommandFunction pageCommand) {
        this.width = width;
        this.resultsPerPage = resultsPerPage;
        this.renderer = renderer;
        this.lineCharacter = lineCharacter;
        this.lineStyle = lineStyle;
        this.previousPageButtonCharacter = previousPageButtonCharacter;
        this.previousPageButtonStyle = previousPageButtonStyle;
        this.nextPageButtonCharacter = nextPageButtonCharacter;
        this.nextPageButtonStyle = nextPageButtonStyle;
        this.title = title;
        this.rowRenderer = rowRenderer;
        this.pageCommand = pageCommand;
    }

    @NotNull
    @Override
    public List<Component> render(@NotNull final Collection<? extends T> content, final int page) {
        if (content.isEmpty()) {
            return Collections.singletonList(this.renderer.renderEmpty());
        } else {
            int pages = pages(this.resultsPerPage, content.size());
            if (!pageInRange(page, pages)) {
                return Collections.singletonList(this.renderer.renderUnknownPage(page, pages));
            } else {
                List<Component> components = new ArrayList();
                components.add(this.renderHeader(page, pages));
                Paginator.forEachPageEntry(content, this.resultsPerPage, page, (value, index) -> components.addAll(this.rowRenderer.renderRow((T) value, index)));
                components.add(this.renderFooter(page, pages));
                return Collections.unmodifiableList(components);
            }
        }
    }

    private Component renderHeader(final int page, final int pages) {
        Component header = this.renderer.renderHeader(this.title, page, pages);
        Component dashes = this.line(header);
        return Component.text().append(dashes).append(header).append(dashes).build();
    }

    private Component renderFooter(final int page, final int pages) {
        if (page == 1 && page == pages) {
            return this.line(this.width);
        } else {
            Component buttons = this.renderFooterButtons(page, pages);
            Component dashes = this.line(buttons);
            return Component.text().append(dashes).append(buttons).append(dashes).build();
        }
    }

    private Component renderFooterButtons(final int page, final int pages) {
        boolean hasPreviousPage = page > 1 && pages > 1;
        boolean hasNextPage = page < pages && page == 1 || hasPreviousPage && page > 1 && page != pages;
        TextComponent.Builder buttons = Component.text();
        if (hasPreviousPage) {
            buttons.append(this.renderer.renderPreviousPageButton(this.previousPageButtonCharacter, this.previousPageButtonStyle, ClickEvent.runCommand(this.pageCommand.pageCommand(page - 1))));
            if (hasNextPage) {
                buttons.append(this.line(8));
            }
        }
        if (hasNextPage) {
            buttons.append(this.renderer.renderNextPageButton(this.nextPageButtonCharacter, this.nextPageButtonStyle, ClickEvent.runCommand(this.pageCommand.pageCommand(page + 1))));
        }
        return buttons.build();
    }

    @NotNull
    private Component line(@NotNull final Component component) {
        return this.line((this.width - length(component)) / 2);
    }

    @NotNull
    private Component line(final int characters) {
        return Component.text(repeat(String.valueOf(this.lineCharacter), characters), this.lineStyle);
    }

    static int length(@NotNull final Component component) {
        int length = 0;
        if (component instanceof TextComponent) {
            length += ((TextComponent) component).content().length();
        }
        for (Component child : component.children()) {
            length += length(child);
        }
        return length;
    }

    @NotNull
    static String repeat(@NotNull final String character, final int count) {
        return String.join("", Collections.nCopies(count, character));
    }

    static int pages(final int pageSize, final int count) {
        int pages = count / pageSize + 1;
        return count % pageSize == 0 ? pages - 1 : pages;
    }

    static boolean pageInRange(final int page, final int pages) {
        return page > 0 && page <= pages;
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("width", this.width), ExaminableProperty.of("resultsPerPage", this.resultsPerPage), ExaminableProperty.of("renderer", this.renderer), ExaminableProperty.of("lineCharacter", this.lineCharacter), ExaminableProperty.of("lineStyle", this.lineStyle), ExaminableProperty.of("previousPageButtonCharacter", this.previousPageButtonCharacter), ExaminableProperty.of("previousPageButtonStyle", this.previousPageButtonStyle), ExaminableProperty.of("nextPageButtonCharacter", this.nextPageButtonCharacter), ExaminableProperty.of("nextPageButtonStyle", this.nextPageButtonStyle), ExaminableProperty.of("title", this.title), ExaminableProperty.of("rowRenderer", this.rowRenderer), ExaminableProperty.of("pageCommand", this.pageCommand));
    }

    public String toString() {
        return StringExaminer.simpleEscaping().examine(this);
    }

    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            PaginationImpl<?> that = (PaginationImpl<?>) other;
            if (this.width != that.width) {
                return false;
            } else if (this.resultsPerPage != that.resultsPerPage) {
                return false;
            } else if (this.lineCharacter != that.lineCharacter) {
                return false;
            } else if (this.previousPageButtonCharacter != that.previousPageButtonCharacter) {
                return false;
            } else if (this.nextPageButtonCharacter != that.nextPageButtonCharacter) {
                return false;
            } else if (!this.renderer.equals(that.renderer)) {
                return false;
            } else if (!this.lineStyle.equals(that.lineStyle)) {
                return false;
            } else if (!this.previousPageButtonStyle.equals(that.previousPageButtonStyle)) {
                return false;
            } else if (!this.nextPageButtonStyle.equals(that.nextPageButtonStyle)) {
                return false;
            } else if (!this.title.equals(that.title)) {
                return false;
            } else {
                return !this.rowRenderer.equals(that.rowRenderer) ? false : this.pageCommand.equals(that.pageCommand);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.width;
        result = 31 * result + this.resultsPerPage;
        result = 31 * result + this.renderer.hashCode();
        result = 31 * result + this.lineCharacter;
        result = 31 * result + this.lineStyle.hashCode();
        result = 31 * result + this.previousPageButtonCharacter;
        result = 31 * result + this.previousPageButtonStyle.hashCode();
        result = 31 * result + this.nextPageButtonCharacter;
        result = 31 * result + this.nextPageButtonStyle.hashCode();
        result = 31 * result + this.title.hashCode();
        result = 31 * result + this.rowRenderer.hashCode();
        return 31 * result + this.pageCommand.hashCode();
    }
}