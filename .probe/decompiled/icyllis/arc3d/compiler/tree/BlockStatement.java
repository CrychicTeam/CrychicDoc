package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Position;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public final class BlockStatement extends Statement {

    private List<Statement> mStatements;

    private boolean mScoped;

    public BlockStatement(int position, List<Statement> statements, boolean scoped) {
        super(position);
        this.mStatements = statements;
        this.mScoped = scoped;
    }

    public static Statement make(int pos, List<Statement> statements, boolean scoped) {
        if (scoped) {
            return new BlockStatement(pos, statements, true);
        } else if (statements.isEmpty()) {
            return new EmptyStatement(pos);
        } else {
            if (statements.size() > 1) {
                Statement foundStatement = null;
                for (Statement stmt : statements) {
                    if (!stmt.isEmpty()) {
                        if (foundStatement != null) {
                            return new BlockStatement(pos, statements, scoped);
                        }
                        foundStatement = stmt;
                    }
                }
                if (foundStatement != null) {
                    return foundStatement;
                }
            }
            return (Statement) statements.get(0);
        }
    }

    public static BlockStatement makeBlock(int pos, List<Statement> statements) {
        return new BlockStatement(pos, statements, true);
    }

    public static Statement makeCompound(Statement before, Statement after) {
        if (before == null || before.isEmpty()) {
            return after;
        } else if (after != null && !after.isEmpty()) {
            if (before instanceof BlockStatement block && !block.isScoped()) {
                block.getStatements().add(after);
                return before;
            }
            int pos = Position.range(before.getStartOffset(), after.getEndOffset());
            List<Statement> statements = new ArrayList(2);
            statements.add(before);
            statements.add(after);
            return make(pos, statements, false);
        } else {
            return before;
        }
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.BLOCK;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        if (visitor.visitBlock(this)) {
            return true;
        } else {
            for (Statement stmt : this.mStatements) {
                if (stmt.accept(visitor)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isEmpty() {
        for (Statement stmt : this.mStatements) {
            if (!stmt.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Statement> getStatements() {
        return this.mStatements;
    }

    public void setStatements(List<Statement> statements) {
        this.mStatements = statements;
    }

    public boolean isScoped() {
        return this.mScoped;
    }

    public void setScoped(boolean scoped) {
        this.mScoped = scoped;
    }

    @Nonnull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean isScoped = this.isScoped() || this.isEmpty();
        if (isScoped) {
            result.append("{");
        }
        for (Statement stmt : this.mStatements) {
            result.append("\n");
            result.append(stmt.toString());
        }
        result.append(isScoped ? "\n}\n" : "\n");
        return result.toString();
    }
}