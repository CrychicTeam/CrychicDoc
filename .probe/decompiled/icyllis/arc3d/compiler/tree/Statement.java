package icyllis.arc3d.compiler.tree;

public abstract class Statement extends Node {

    protected Statement(int position) {
        super(position);
    }

    public abstract Node.StatementKind getKind();

    public boolean isEmpty() {
        return false;
    }
}