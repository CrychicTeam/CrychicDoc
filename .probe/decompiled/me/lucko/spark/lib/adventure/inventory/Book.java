package me.lucko.spark.lib.adventure.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.util.Buildable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface Book extends Buildable<Book, Book.Builder>, Examinable {

    @NotNull
    static Book book(@NotNull final Component title, @NotNull final Component author, @NotNull final Collection<Component> pages) {
        return new BookImpl(title, author, new ArrayList(pages));
    }

    @NotNull
    static Book book(@NotNull final Component title, @NotNull final Component author, @NotNull final Component... pages) {
        return book(title, author, Arrays.asList(pages));
    }

    @NotNull
    static Book.Builder builder() {
        return new BookImpl.BuilderImpl();
    }

    @NotNull
    Component title();

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book title(@NotNull final Component title);

    @NotNull
    Component author();

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book author(@NotNull final Component author);

    @NotNull
    @Unmodifiable
    List<Component> pages();

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default Book pages(@NotNull final Component... pages) {
        return this.pages(Arrays.asList(pages));
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book pages(@NotNull final List<Component> pages);

    @NotNull
    default Book.Builder toBuilder() {
        return builder().title(this.title()).author(this.author()).pages(this.pages());
    }

    public interface Builder extends AbstractBuilder<Book>, Buildable.Builder<Book> {

        @Contract("_ -> this")
        @NotNull
        Book.Builder title(@NotNull final Component title);

        @Contract("_ -> this")
        @NotNull
        Book.Builder author(@NotNull final Component author);

        @Contract("_ -> this")
        @NotNull
        Book.Builder addPage(@NotNull final Component page);

        @Contract("_ -> this")
        @NotNull
        Book.Builder pages(@NotNull final Component... pages);

        @Contract("_ -> this")
        @NotNull
        Book.Builder pages(@NotNull final Collection<Component> pages);

        @NotNull
        Book build();
    }
}