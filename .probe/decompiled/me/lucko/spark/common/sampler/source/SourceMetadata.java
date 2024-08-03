package me.lucko.spark.common.sampler.source;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import me.lucko.spark.proto.SparkSamplerProtos;

public class SourceMetadata {

    private final String name;

    private final String version;

    private final String author;

    public static <T> List<SourceMetadata> gather(Collection<T> sources, Function<? super T, String> nameFunction, Function<? super T, String> versionFunction, Function<? super T, String> authorFunction) {
        Builder<SourceMetadata> builder = ImmutableList.builder();
        for (T source : sources) {
            String name = (String) nameFunction.apply(source);
            String version = (String) versionFunction.apply(source);
            String author = (String) authorFunction.apply(source);
            SourceMetadata metadata = new SourceMetadata(name, version, author);
            builder.add(metadata);
        }
        return builder.build();
    }

    public SourceMetadata(String name, String version, String author) {
        this.name = name;
        this.version = version;
        this.author = author;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getAuthor() {
        return this.author;
    }

    public SparkSamplerProtos.SamplerMetadata.SourceMetadata toProto() {
        return SparkSamplerProtos.SamplerMetadata.SourceMetadata.newBuilder().setName(this.name).setVersion(this.version).build();
    }
}