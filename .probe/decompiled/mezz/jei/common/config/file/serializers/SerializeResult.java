package mezz.jei.common.config.file.serializers;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class SerializeResult {

    @Nullable
    private final String result;

    private final List<String> errors;

    public SerializeResult(String result) {
        this(result, List.of());
    }

    public SerializeResult(@Nullable String result, String error) {
        this(result, List.of(error));
    }

    public SerializeResult(@Nullable String result, List<String> errors) {
        this.result = result;
        this.errors = errors;
    }

    @Nullable
    public String getResult() {
        return this.result;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}