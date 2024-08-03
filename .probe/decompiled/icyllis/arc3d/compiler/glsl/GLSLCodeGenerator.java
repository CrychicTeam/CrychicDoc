package icyllis.arc3d.compiler.glsl;

import icyllis.arc3d.compiler.CodeGenerator;
import icyllis.arc3d.compiler.GLSLVersion;
import icyllis.arc3d.compiler.ShaderCaps;
import icyllis.arc3d.compiler.ShaderCompiler;
import icyllis.arc3d.compiler.TargetApi;
import icyllis.arc3d.compiler.tree.TranslationUnit;
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;

public final class GLSLCodeGenerator extends CodeGenerator {

    public final TargetApi mOutputTarget;

    public final GLSLVersion mOutputVersion;

    public GLSLCodeGenerator(@Nonnull ShaderCompiler compiler, @Nonnull TranslationUnit translationUnit, @Nonnull ShaderCaps shaderCaps) {
        super(compiler, translationUnit);
        this.mOutputTarget = (TargetApi) Objects.requireNonNullElse(shaderCaps.mTargetApi, TargetApi.OPENGL_4_5);
        this.mOutputVersion = (GLSLVersion) Objects.requireNonNullElse(shaderCaps.mGLSLVersion, GLSLVersion.GLSL_450);
    }

    @Nullable
    @Override
    public ByteBuffer generateCode() {
        return null;
    }
}