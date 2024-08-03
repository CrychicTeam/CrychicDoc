package me.lucko.spark.lib.adventure.text.feature.pagination;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@FunctionalInterface
public interface Pagination<T> {

    int WIDTH = 55;

    int RESULTS_PER_PAGE = 6;

    char LINE_CHARACTER = '-';

    Style LINE_STYLE = Style.style(NamedTextColor.DARK_GRAY);

    char PREVIOUS_PAGE_BUTTON_CHARACTER = '«';

    Style PREVIOUS_PAGE_BUTTON_STYLE = Style.style(NamedTextColor.RED, HoverEvent.showText((Component) Component.text("Previous Page", NamedTextColor.RED)));

    char NEXT_PAGE_BUTTON_CHARACTER = '»';

    Style NEXT_PAGE_BUTTON_STYLE = Style.style(NamedTextColor.GREEN, HoverEvent.showText((Component) Component.text("Next Page", NamedTextColor.GREEN)));

    Pagination.Renderer DEFAULT_RENDERER = new Pagination.Renderer() {

        public String toString() {
            return "Pagination.DEFAULT_RENDERER";
        }
    };

    @NotNull
    static Pagination.Builder builder() {
        return new PaginationBuilder();
    }

    @NotNull
    List<Component> render(@NotNull final Collection<? extends T> content, final int page);

    public interface Builder {

        @NotNull
        Pagination.Builder width(final int width);

        @NotNull
        Pagination.Builder resultsPerPage(@Range(from = 0L, to = 2147483647L) final int resultsPerPage);

        @NotNull
        Pagination.Builder renderer(@NotNull final Pagination.Renderer renderer);

        @NotNull
        Pagination.Builder line(@NotNull final Consumer<Pagination.Builder.CharacterAndStyle> line);

        @NotNull
        Pagination.Builder previousButton(@NotNull final Consumer<Pagination.Builder.CharacterAndStyle> previousButton);

        @NotNull
        Pagination.Builder nextButton(@NotNull final Consumer<Pagination.Builder.CharacterAndStyle> nextButton);

        @NotNull
        <T> Pagination<T> build(@NotNull final Component title, @NotNull final Pagination.Renderer.RowRenderer<T> rowRenderer, @NotNull final Pagination.PageCommandFunction pageCommand);

        public interface CharacterAndStyle {

            @NotNull
            Pagination.Builder.CharacterAndStyle character(final char character);

            @NotNull
            Pagination.Builder.CharacterAndStyle style(@NotNull final Style style);
        }
    }

    @FunctionalInterface
    public interface PageCommandFunction {

        @NotNull
        String pageCommand(final int page);
    }

    public interface Renderer {

        Component GRAY_LEFT_ROUND_BRACKET = Component.text("(", NamedTextColor.GRAY);

        Component GRAY_LEFT_SQUARE_BRACKET = Component.text("[", NamedTextColor.GRAY);

        Component GRAY_RIGHT_ROUND_BRACKET = Component.text(")", NamedTextColor.GRAY);

        Component GRAY_RIGHT_SQUARE_BRACKET = Component.text("]", NamedTextColor.GRAY);

        Component GRAY_FORWARD_SLASH = Component.text("/", NamedTextColor.GRAY);

        @NotNull
        default Component renderEmpty() {
            return Component.text("No results match.", NamedTextColor.GRAY);
        }

        @NotNull
        default Component renderUnknownPage(final int page, final int pages) {
            return Component.text("Unknown page selected. " + pages + " total pages.", NamedTextColor.GRAY);
        }

        @NotNull
        default Component renderHeader(@NotNull final Component title, final int page, final int pages) {
            return Component.text().append(Component.space()).append(title).append(Component.space()).append(GRAY_LEFT_ROUND_BRACKET).append(Component.text(page, NamedTextColor.WHITE)).append(GRAY_FORWARD_SLASH).append(Component.text(pages, NamedTextColor.WHITE)).append(GRAY_RIGHT_ROUND_BRACKET).append(Component.space()).build();
        }

        @NotNull
        default Component renderPreviousPageButton(final char character, @NotNull final Style style, @NotNull final ClickEvent clickEvent) {
            return Component.text().append(Component.space()).append(GRAY_LEFT_SQUARE_BRACKET).append(Component.text(character, style.clickEvent(clickEvent))).append(GRAY_RIGHT_SQUARE_BRACKET).append(Component.space()).build();
        }

        @NotNull
        default Component renderNextPageButton(final char character, @NotNull final Style style, @NotNull final ClickEvent clickEvent) {
            return Component.text().append(Component.space()).append(GRAY_LEFT_SQUARE_BRACKET).append(Component.text(character, style.clickEvent(clickEvent))).append(GRAY_RIGHT_SQUARE_BRACKET).append(Component.space()).build();
        }

        @FunctionalInterface
        public interface RowRenderer<T> {

            @NotNull
            Collection<Component> renderRow(@Nullable final T value, final int index);
        }
    }
}