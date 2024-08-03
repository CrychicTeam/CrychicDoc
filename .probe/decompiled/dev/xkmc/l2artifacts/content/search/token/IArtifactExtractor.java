package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;

public interface IArtifactExtractor<T extends IArtifactFeature> {

    T get(BaseArtifact var1);
}