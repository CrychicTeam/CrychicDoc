package icyllis.arc3d.compiler;

public class Mangler {

    private int mCounter;

    public String uniqueName(String baseName, SymbolTable symbolTable) {
        return baseName;
    }

    public void reset() {
        this.mCounter = 0;
    }
}