package net.blay09.mods.defaultoptions.api;

import java.util.function.Predicate;

public interface SimpleDefaultOptionsHandler extends DefaultOptionsHandler {

    SimpleDefaultOptionsHandler withCategory(DefaultOptionsCategory var1);

    SimpleDefaultOptionsHandler withSaveHandler(Runnable var1);

    SimpleDefaultOptionsHandler withLinePredicate(Predicate<String> var1);

    SimpleDefaultOptionsHandler withLoadStage(DefaultOptionsLoadStage var1);
}