package mezz.jei.api.recipe;

import java.util.Collection;
import java.util.stream.Stream;

public interface IRecipeLookup<R> {

    IRecipeLookup<R> limitFocus(Collection<? extends IFocus<?>> var1);

    IRecipeLookup<R> includeHidden();

    Stream<R> get();
}