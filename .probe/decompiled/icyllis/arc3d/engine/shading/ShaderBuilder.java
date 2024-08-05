package icyllis.arc3d.engine.shading;

import org.intellij.lang.annotations.PrintFormat;

public interface ShaderBuilder {

    void codeAppend(String var1);

    void codeAppendf(@PrintFormat String var1, Object... var2);

    void codePrependf(@PrintFormat String var1, Object... var2);

    String getMangledName(String var1);
}