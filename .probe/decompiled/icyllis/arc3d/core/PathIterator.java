package icyllis.arc3d.core;

public interface PathIterator {

    int FILL_NON_ZERO = 0;

    int FILL_EVEN_ODD = 1;

    int VERB_MOVE = 0;

    int VERB_LINE = 1;

    int VERB_QUAD = 2;

    int VERB_CUBIC = 4;

    int VERB_CLOSE = 5;

    int VERB_DONE = 6;

    int next(float[] var1, int var2);
}