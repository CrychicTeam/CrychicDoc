package info.journeymap.shaded.org.eclipse.jetty.util;

public class Utf8StringBuilder extends Utf8Appendable {

    final StringBuilder _buffer = (StringBuilder) this._appendable;

    public Utf8StringBuilder() {
        super(new StringBuilder());
    }

    public Utf8StringBuilder(int capacity) {
        super(new StringBuilder(capacity));
    }

    @Override
    public int length() {
        return this._buffer.length();
    }

    @Override
    public void reset() {
        super.reset();
        this._buffer.setLength(0);
    }

    public StringBuilder getStringBuilder() {
        this.checkState();
        return this._buffer;
    }

    public String toString() {
        this.checkState();
        return this._buffer.toString();
    }
}