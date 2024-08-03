package mezz.jei.gui.input.handlers;

import java.util.Optional;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.bookmarks.BookmarkList;
import mezz.jei.gui.input.CombinedRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class BookmarkInputHandler implements IUserInputHandler {

    private final CombinedRecipeFocusSource focusSource;

    private final BookmarkList bookmarkList;

    public BookmarkInputHandler(CombinedRecipeFocusSource focusSource, BookmarkList bookmarkList) {
        this.focusSource = focusSource;
        this.bookmarkList = bookmarkList;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        return input.is(keyBindings.getBookmark()) ? this.handleBookmark(input, keyBindings) : Optional.empty();
    }

    private Optional<IUserInputHandler> handleBookmark(UserInput input, IInternalKeyMappings keyBindings) {
        return this.focusSource.getIngredientUnderMouse(input, keyBindings).findFirst().flatMap(clicked -> {
            if (!input.isSimulate() && !this.bookmarkList.remove(clicked.getTypedIngredient()) && !this.bookmarkList.add(clicked.getTypedIngredient())) {
                return Optional.empty();
            } else {
                ImmutableRect2i area = clicked.getArea();
                IUserInputHandler handler = LimitedAreaInputHandler.create(this, area);
                return Optional.of(handler);
            }
        });
    }
}