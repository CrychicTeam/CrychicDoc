package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.spirv.SPIRVCodeGenerator;
import icyllis.arc3d.compiler.tree.TranslationUnit;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ShaderCompiler {

    public static final String INVALID_TAG = "<INVALID>";

    public static final String POISON_TAG = "<POISON>";

    private final StringBuilder mErrorBuilder = new StringBuilder();

    private final ErrorHandler mErrorHandler = new ErrorHandler() {

        private void log(int start, int end, String msg) {
            boolean showLocation = false;
            char[] source = this.getSource();
            if (start != -1 && source != null) {
                int offset = Math.min(start, source.length);
                int line = 1;
                for (int i = 0; i < offset; i++) {
                    if (source[i] == '\n') {
                        line++;
                    }
                }
                showLocation = start < source.length;
                ShaderCompiler.this.mErrorBuilder.append(line).append(": ");
            }
            ShaderCompiler.this.mErrorBuilder.append(msg).append('\n');
            if (showLocation) {
                int lineStart = start;
                while (lineStart > 0 && source[lineStart - 1] != '\n') {
                    lineStart--;
                }
                for (int ix = lineStart; ix < source.length; ix++) {
                    switch(source[ix]) {
                        case '\u0000':
                            ShaderCompiler.this.mErrorBuilder.append(" ");
                            break;
                        case '\t':
                            ShaderCompiler.this.mErrorBuilder.append("    ");
                            break;
                        case '\n':
                            ix = source.length;
                            break;
                        default:
                            ShaderCompiler.this.mErrorBuilder.append(source[ix]);
                    }
                }
                ShaderCompiler.this.mErrorBuilder.append('\n');
                for (int ix = lineStart; ix < source.length && ix < end; ix++) {
                    switch(source[ix]) {
                        case '\t':
                            ShaderCompiler.this.mErrorBuilder.append(ix >= start ? "^^^^" : "    ");
                            break;
                        case '\n':
                            assert ix >= start;
                            ShaderCompiler.this.mErrorBuilder.append(end > ix + 1 ? "..." : "^");
                            ix = source.length;
                            break;
                        default:
                            ShaderCompiler.this.mErrorBuilder.append((char) (ix >= start ? '^' : ' '));
                    }
                }
                ShaderCompiler.this.mErrorBuilder.append('\n');
            }
        }

        @Override
        protected void handleError(int start, int end, String msg) {
            ShaderCompiler.this.mErrorBuilder.append("error: ");
            this.log(start, end, msg);
        }

        @Override
        protected void handleWarning(int start, int end, String msg) {
            ShaderCompiler.this.mErrorBuilder.append("warning: ");
            this.log(start, end, msg);
        }
    };

    private final Context mContext = new Context(this.mErrorHandler);

    private final Inliner mInliner = new Inliner();

    public Context getContext() {
        if (this.mContext.isActive()) {
            return this.mContext;
        } else {
            throw new IllegalStateException("DSL is not started");
        }
    }

    @Nullable
    public TranslationUnit parse(@Nonnull CharSequence source, @Nonnull ShaderKind kind, @Nonnull CompileOptions options, @Nonnull ModuleUnit parent) {
        return this.parse(toChars(source), kind, options, parent);
    }

    @Nullable
    public TranslationUnit parse(@Nonnull char[] source, @Nonnull ShaderKind kind, @Nonnull CompileOptions options, @Nonnull ModuleUnit parent) {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(parent);
        this.startContext(kind, options, parent, false, false, source);
        TranslationUnit var6;
        try {
            Parser parser = new Parser(this, kind, options, source);
            var6 = parser.parse(parent);
        } finally {
            this.endContext();
        }
        return var6;
    }

    @Nullable
    public ModuleUnit parseModule(@Nonnull CharSequence source, @Nonnull ShaderKind kind, @Nonnull ModuleUnit parent, boolean builtin) {
        return this.parseModule(toChars(source), kind, parent, builtin);
    }

    @Nullable
    public ModuleUnit parseModule(@Nonnull char[] source, @Nonnull ShaderKind kind, @Nonnull ModuleUnit parent, boolean builtin) {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(parent);
        CompileOptions options = new CompileOptions();
        this.startContext(kind, options, parent, builtin, true, source);
        ModuleUnit var7;
        try {
            Parser parser = new Parser(this, kind, options, source);
            var7 = parser.parseModule(parent);
        } finally {
            this.endContext();
        }
        return var7;
    }

    @Nullable
    public ByteBuffer generateSPIRV(@Nonnull TranslationUnit translationUnit, @Nonnull ShaderCaps shaderCaps) {
        this.startContext(translationUnit.getKind(), translationUnit.getOptions(), null, false, false, translationUnit.getSource());
        ByteBuffer var4;
        try {
            CodeGenerator generator = new SPIRVCodeGenerator(this, translationUnit, shaderCaps);
            var4 = generator.generateCode();
        } finally {
            this.endContext();
        }
        return var4;
    }

    @Nullable
    public ByteBuffer compileIntoSPIRV(@Nonnull CharSequence source, @Nonnull ShaderKind kind, @Nonnull ShaderCaps shaderCaps, @Nonnull CompileOptions options, @Nonnull ModuleUnit parent) {
        return this.compileIntoSPIRV(toChars(source), kind, shaderCaps, options, parent);
    }

    @Nullable
    public ByteBuffer compileIntoSPIRV(@Nonnull char[] source, @Nonnull ShaderKind kind, @Nonnull ShaderCaps shaderCaps, @Nonnull CompileOptions options, @Nonnull ModuleUnit parent) {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(parent);
        this.startContext(kind, options, parent, false, false, source);
        CodeGenerator generator;
        try {
            Parser parser = new Parser(this, kind, options, source);
            TranslationUnit translationUnit = parser.parse(parent);
            if (translationUnit != null) {
                generator = new SPIRVCodeGenerator(this, translationUnit, shaderCaps);
                return generator.generateCode();
            }
            generator = null;
        } finally {
            this.endContext();
        }
        return generator;
    }

    @Internal
    public void startContext(ShaderKind kind, CompileOptions options, ModuleUnit parent, boolean isBuiltin, boolean isModule, char[] source) {
        assert isModule || !isBuiltin;
        this.resetErrors();
        this.mContext.start(kind, options, parent, isBuiltin, isModule);
        this.mContext.getErrorHandler().setSource(source);
    }

    @Internal
    public void endContext() {
        this.mContext.end();
        this.mContext.getErrorHandler().setSource(null);
    }

    @Nonnull
    public static char[] toChars(@Nonnull CharSequence s) {
        if (s instanceof String) {
            return ((String) s).toCharArray();
        } else {
            int n = s.length();
            char[] chars = new char[n];
            getChars(s, chars, 0, n);
            return chars;
        }
    }

    @Nonnull
    public static char[] toChars(@Nonnull CharSequence... elements) {
        return toChars(elements, 0, elements.length);
    }

    @Nonnull
    public static char[] toChars(@Nonnull CharSequence[] elements, int start, int end) {
        Objects.checkFromToIndex(start, end, elements.length);
        if (start == end) {
            return new char[0];
        } else if (start + 1 == end) {
            return toChars(elements[start]);
        } else {
            int n = -1;
            for (int i = start; i < end; i++) {
                int len = elements[i].length();
                if (len != 0) {
                    n += len + 1;
                }
            }
            if (n == -1) {
                return new char[0];
            } else {
                char[] chars = new char[n];
                int p = 0;
                for (int ix = start; ix < end; ix++) {
                    CharSequence s = elements[ix];
                    int len = s.length();
                    if (len != 0) {
                        p += getChars(s, chars, p, len);
                        if (p < n) {
                            chars[p++] = '\n';
                        }
                    }
                }
                assert p == n;
                return chars;
            }
        }
    }

    @Nonnull
    public static char[] toChars(@Nonnull List<CharSequence> elements) {
        int size = elements.size();
        if (size == 0) {
            return new char[0];
        } else if (size == 1) {
            return toChars((CharSequence) elements.get(0));
        } else {
            int n = -1;
            for (int i = 0; i < size; i++) {
                int len = ((CharSequence) elements.get(i)).length();
                if (len != 0) {
                    n += len + 1;
                }
            }
            if (n == -1) {
                return new char[0];
            } else {
                char[] chars = new char[n];
                int p = 0;
                for (int ix = 0; ix < size; ix++) {
                    CharSequence s = (CharSequence) elements.get(ix);
                    int len = s.length();
                    if (len != 0) {
                        p += getChars(s, chars, p, len);
                        if (p < n) {
                            chars[p++] = '\n';
                        }
                    }
                }
                assert p == n;
                return chars;
            }
        }
    }

    private static int getChars(@Nonnull CharSequence s, @Nonnull char[] dst, int offset, int n) {
        if (s instanceof String) {
            ((String) s).getChars(0, n, dst, offset);
        } else if (s instanceof StringBuffer) {
            ((StringBuffer) s).getChars(0, n, dst, offset);
        } else if (s instanceof StringBuilder) {
            ((StringBuilder) s).getChars(0, n, dst, offset);
        } else if (s instanceof CharBuffer buf) {
            buf.get(buf.position(), dst, offset, n);
        } else {
            for (int i = 0; i < n; i++) {
                dst[offset++] = s.charAt(i);
            }
        }
        return n;
    }

    @Nonnull
    public String getErrorMessage() {
        return this.getErrorMessage(true);
    }

    @Nonnull
    public String getErrorMessage(boolean showCount) {
        if (!showCount) {
            return this.mErrorBuilder.toString();
        } else {
            int errors = this.errorCount();
            int warnings = this.warningCount();
            if (errors != 0 || warnings != 0) {
                int start = this.mErrorBuilder.length();
                this.mErrorBuilder.append(errors).append(" error");
                if (errors != 1) {
                    this.mErrorBuilder.append('s');
                }
                this.mErrorBuilder.append(", ").append(warnings).append(" warning");
                if (warnings != 1) {
                    this.mErrorBuilder.append('s');
                }
                this.mErrorBuilder.append('\n');
                String msg = this.mErrorBuilder.toString();
                this.mErrorBuilder.delete(start, this.mErrorBuilder.length());
                return msg;
            } else {
                assert this.mErrorBuilder.isEmpty();
                return "";
            }
        }
    }

    public ErrorHandler getErrorHandler() {
        return this.mErrorHandler;
    }

    public int errorCount() {
        return this.mErrorHandler.errorCount();
    }

    public int warningCount() {
        return this.mErrorHandler.warningCount();
    }

    private void resetErrors() {
        boolean trim = this.mErrorBuilder.length() > 8192;
        this.mErrorBuilder.setLength(0);
        if (trim) {
            this.mErrorBuilder.trimToSize();
        }
        this.mErrorHandler.reset();
    }
}