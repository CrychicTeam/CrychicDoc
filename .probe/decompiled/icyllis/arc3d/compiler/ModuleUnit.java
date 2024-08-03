package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.TopLevelElement;
import java.util.ArrayList;

public final class ModuleUnit {

    ModuleUnit mParent;

    SymbolTable mSymbols;

    ArrayList<TopLevelElement> mElements;

    ModuleUnit() {
    }

    public String toString() {
        return "ModuleUnit{\nmParent=" + this.mParent + "\nmSymbols=" + this.mSymbols + "\nmElements=" + this.mElements + "}";
    }
}