package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.Comparator;
import java.util.stream.Stream;

public interface IArtifactFilter {

    Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts();

    void update();

    Comparator<GenericItemStack<BaseArtifact>> getComparator();
}