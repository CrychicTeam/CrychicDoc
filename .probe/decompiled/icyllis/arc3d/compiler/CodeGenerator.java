package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.TranslationUnit;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public abstract class CodeGenerator {

    protected final ShaderCompiler mCompiler;

    protected final TranslationUnit mTranslationUnit;

    public CodeGenerator(ShaderCompiler compiler, TranslationUnit translationUnit) {
        this.mCompiler = compiler;
        this.mTranslationUnit = translationUnit;
    }

    @Nullable
    public abstract ByteBuffer generateCode();

    protected Context getContext() {
        return this.mCompiler.getContext();
    }
}