package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2library.util.code.GenericItemStack;

@FunctionalInterface
public interface IArtifactPredicate<T> {

    boolean test(GenericItemStack<BaseArtifact> var1, T var2);
}