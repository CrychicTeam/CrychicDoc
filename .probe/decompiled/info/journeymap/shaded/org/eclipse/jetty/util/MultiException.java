package info.journeymap.shaded.org.eclipse.jetty.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiException extends Exception {

    private List<Throwable> nested;

    public MultiException() {
        super("Multiple exceptions");
    }

    public void add(Throwable e) {
        if (e == null) {
            throw new IllegalArgumentException();
        } else {
            if (this.nested == null) {
                this.initCause(e);
                this.nested = new ArrayList();
            } else {
                this.addSuppressed(e);
            }
            if (e instanceof MultiException) {
                MultiException me = (MultiException) e;
                this.nested.addAll(me.nested);
            } else {
                this.nested.add(e);
            }
        }
    }

    public int size() {
        return this.nested == null ? 0 : this.nested.size();
    }

    public List<Throwable> getThrowables() {
        return this.nested == null ? Collections.emptyList() : this.nested;
    }

    public Throwable getThrowable(int i) {
        return (Throwable) this.nested.get(i);
    }

    public void ifExceptionThrow() throws Exception {
        if (this.nested != null) {
            switch(this.nested.size()) {
                case 0:
                    return;
                case 1:
                    Throwable th = (Throwable) this.nested.get(0);
                    if (th instanceof Error) {
                        throw (Error) th;
                    } else if (th instanceof Exception) {
                        throw (Exception) th;
                    }
                default:
                    throw this;
            }
        }
    }

    public void ifExceptionThrowRuntime() throws Error {
        if (this.nested != null) {
            switch(this.nested.size()) {
                case 0:
                    return;
                case 1:
                    Throwable th = (Throwable) this.nested.get(0);
                    if (th instanceof Error) {
                        throw (Error) th;
                    } else {
                        if (th instanceof RuntimeException) {
                            throw (RuntimeException) th;
                        }
                        throw new RuntimeException(th);
                    }
                default:
                    throw new RuntimeException(this);
            }
        }
    }

    public void ifExceptionThrowMulti() throws MultiException {
        if (this.nested != null) {
            if (this.nested.size() > 0) {
                throw this;
            }
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(MultiException.class.getSimpleName());
        if (this.nested != null && this.nested.size() > 0) {
            str.append(this.nested);
        } else {
            str.append("[]");
        }
        return str.toString();
    }
}