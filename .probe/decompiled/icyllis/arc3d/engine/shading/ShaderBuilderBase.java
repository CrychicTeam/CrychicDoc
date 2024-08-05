package icyllis.arc3d.engine.shading;

import icyllis.arc3d.engine.ShaderVar;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public abstract class ShaderBuilderBase implements ShaderBuilder {

    protected static final int EXTENSIONS = 0;

    protected static final int DEFINITIONS = 1;

    protected static final int LAYOUT_QUALIFIERS = 2;

    protected static final int UNIFORMS = 3;

    protected static final int INPUTS = 4;

    protected static final int OUTPUTS = 5;

    protected static final int FUNCTIONS = 6;

    protected static final int CODE = 7;

    protected static final int PREALLOC = 14;

    protected final PipelineBuilder mPipelineBuilder;

    protected final StringBuilder[] mShaderStrings = new StringBuilder[14];

    private final HashSet<String> mExtensions = new HashSet();

    protected int mCodeIndex;

    private Formatter mCodeFormatter;

    private Formatter mCodeFormatterPre;

    private boolean mFinished;

    public ShaderBuilderBase(PipelineBuilder pipelineBuilder) {
        this.mPipelineBuilder = pipelineBuilder;
        for (int i = 0; i <= 7; i++) {
            this.mShaderStrings[i] = new StringBuilder();
        }
        this.extensions().append("#version 450\n");
        this.mCodeIndex = 7;
        this.codeAppend("void main() {\n");
    }

    @Override
    public void codeAppend(String str) {
        this.code().append(str);
    }

    @Override
    public void codeAppendf(String format, Object... args) {
        if (this.mCodeFormatter == null) {
            this.mCodeFormatter = new Formatter(this.code(), Locale.ROOT);
        }
        this.mCodeFormatter.format(Locale.ROOT, format, args);
    }

    @Override
    public void codePrependf(String format, Object... args) {
        if (this.mCodeFormatterPre == null) {
            this.mCodeFormatterPre = new Formatter(new ShaderBuilderBase.Prependable(this.code()), Locale.ROOT);
        }
        this.mCodeFormatterPre.format(Locale.ROOT, format, args);
    }

    public void declAppend(ShaderVar var) {
        var.appendDecl(this.code());
        this.codeAppend(";\n");
    }

    @Override
    public String getMangledName(String baseName) {
        return this.mPipelineBuilder.nameVariable('\u0000', baseName);
    }

    protected final void nextStage() {
        assert !this.mFinished;
        this.mShaderStrings[++this.mCodeIndex] = new StringBuilder();
        this.mCodeFormatter = null;
        this.mCodeFormatterPre = null;
    }

    protected final void deleteStage() {
        assert !this.mFinished;
        this.mShaderStrings[this.mCodeIndex--] = null;
        this.mCodeFormatter = null;
        this.mCodeFormatterPre = null;
    }

    protected final StringBuilder extensions() {
        assert !this.mFinished;
        return this.mShaderStrings[0];
    }

    protected final StringBuilder definitions() {
        assert !this.mFinished;
        return this.mShaderStrings[1];
    }

    protected final StringBuilder layoutQualifiers() {
        assert !this.mFinished;
        return this.mShaderStrings[2];
    }

    protected final StringBuilder uniforms() {
        assert !this.mFinished;
        return this.mShaderStrings[3];
    }

    protected final StringBuilder inputs() {
        assert !this.mFinished;
        return this.mShaderStrings[4];
    }

    protected final StringBuilder outputs() {
        assert !this.mFinished;
        return this.mShaderStrings[5];
    }

    protected final StringBuilder functions() {
        assert !this.mFinished;
        return this.mShaderStrings[6];
    }

    protected final StringBuilder code() {
        assert !this.mFinished;
        return this.mShaderStrings[this.mCodeIndex];
    }

    public void addExtension(@Nullable String extensionName) {
        if (extensionName != null) {
            if (this.mExtensions.add(extensionName)) {
                this.extensions().append("#extension ").append(extensionName).append(": require\n");
            }
        }
    }

    public final void finish() {
        if (!this.mFinished) {
            this.onFinish();
            this.code().append("}");
            this.mFinished = true;
        }
    }

    public final CharSequence[] getStrings() {
        return this.mShaderStrings;
    }

    public final int getCount() {
        return this.mCodeIndex + 1;
    }

    @Nonnull
    public final ByteBuffer toUTF8() {
        this.finish();
        int len = 0;
        for (int i = 0; i <= this.mCodeIndex; i++) {
            StringBuilder shaderString = this.mShaderStrings[i];
            len += shaderString.length();
        }
        ByteBuffer buffer = BufferUtils.createByteBuffer(len);
        len = 0;
        for (int i = 0; i <= this.mCodeIndex; i++) {
            StringBuilder shaderString = this.mShaderStrings[i];
            len += MemoryUtil.memUTF8(shaderString, false, buffer, len);
        }
        assert len == buffer.capacity() && len == buffer.remaining();
        return buffer;
    }

    public final String toString() {
        this.finish();
        return (String) Arrays.stream(this.getStrings(), 0, this.getCount()).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
    }

    protected abstract void onFinish();

    private static class Prependable implements Appendable {

        private final StringBuilder mBuilder;

        public Prependable(StringBuilder builder) {
            this.mBuilder = builder;
        }

        public Appendable append(CharSequence csq) {
            this.mBuilder.insert(0, csq);
            return this;
        }

        public Appendable append(CharSequence csq, int start, int end) {
            this.mBuilder.insert(0, csq, start, end);
            return this;
        }

        public Appendable append(char c) {
            this.mBuilder.insert(0, c);
            return this;
        }
    }
}