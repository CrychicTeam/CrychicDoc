package icyllis.arc3d.compiler.tree;

public abstract class TopLevelElement extends Node {

    protected TopLevelElement(int position) {
        super(position);
    }

    public abstract Node.ElementKind getKind();
}