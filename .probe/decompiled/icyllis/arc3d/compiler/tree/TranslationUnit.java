package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.BuiltinTypes;
import icyllis.arc3d.compiler.CompileOptions;
import icyllis.arc3d.compiler.ShaderKind;
import icyllis.arc3d.compiler.SymbolTable;
import icyllis.arc3d.compiler.analysis.SymbolUsage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nonnull;

public final class TranslationUnit extends Node implements Iterable<TopLevelElement> {

    private final char[] mSource;

    private final ShaderKind mKind;

    private final CompileOptions mOptions;

    private final boolean mIsBuiltin;

    private final boolean mIsModule;

    private final BuiltinTypes mTypes;

    private final SymbolTable mSymbolTable;

    private final ArrayList<TopLevelElement> mUniqueElements;

    private final ArrayList<TopLevelElement> mSharedElements;

    private final SymbolUsage mUsage;

    public TranslationUnit(int position, char[] source, ShaderKind kind, CompileOptions options, boolean isBuiltin, boolean isModule, BuiltinTypes types, SymbolTable symbolTable, ArrayList<TopLevelElement> uniqueElements) {
        super(position);
        this.mSource = source;
        this.mKind = kind;
        this.mOptions = options;
        this.mIsBuiltin = isBuiltin;
        this.mIsModule = isModule;
        this.mTypes = types;
        this.mSymbolTable = symbolTable;
        this.mUniqueElements = uniqueElements;
        this.mSharedElements = new ArrayList();
        this.mUsage = new SymbolUsage();
        this.mUsage.add(this);
    }

    public char[] getSource() {
        return this.mSource;
    }

    public ShaderKind getKind() {
        return this.mKind;
    }

    public CompileOptions getOptions() {
        return this.mOptions;
    }

    public boolean isBuiltin() {
        return this.mIsBuiltin;
    }

    public boolean isModule() {
        return this.mIsModule;
    }

    public BuiltinTypes getTypes() {
        return this.mTypes;
    }

    public SymbolTable getSymbolTable() {
        return this.mSymbolTable;
    }

    public ArrayList<TopLevelElement> getUniqueElements() {
        return this.mUniqueElements;
    }

    public ArrayList<TopLevelElement> getSharedElements() {
        return this.mSharedElements;
    }

    public SymbolUsage getUsage() {
        return this.mUsage;
    }

    @Nonnull
    public Iterator<TopLevelElement> iterator() {
        return new TranslationUnit.ElementIterator();
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        for (TopLevelElement e : this) {
            if (e.accept(visitor)) {
                return true;
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (TopLevelElement e : this) {
            s.append(e.toString());
            s.append('\n');
        }
        return s.toString();
    }

    private class ElementIterator implements Iterator<TopLevelElement> {

        private Iterator<TopLevelElement> mCurrIter = TranslationUnit.this.mSharedElements.iterator();

        private boolean mSharedEnded = false;

        public boolean hasNext() {
            this.forward();
            return this.mCurrIter.hasNext();
        }

        public TopLevelElement next() {
            this.forward();
            return (TopLevelElement) this.mCurrIter.next();
        }

        private void forward() {
            while (!this.mCurrIter.hasNext() && !this.mSharedEnded) {
                this.mCurrIter = TranslationUnit.this.mUniqueElements.iterator();
                this.mSharedEnded = true;
            }
        }
    }
}