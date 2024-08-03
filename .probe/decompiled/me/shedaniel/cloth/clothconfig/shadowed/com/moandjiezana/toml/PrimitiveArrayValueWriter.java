package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.util.Collection;

class PrimitiveArrayValueWriter extends ArrayValueWriter {

    static final ValueWriter PRIMITIVE_ARRAY_VALUE_WRITER = new PrimitiveArrayValueWriter();

    @Override
    public boolean canWrite(Object value) {
        return isArrayish(value) && isArrayOfPrimitive(value);
    }

    @Override
    public void write(Object o, WriterContext context) {
        Collection<?> values = this.normalize(o);
        context.write('[');
        context.writeArrayDelimiterPadding();
        boolean first = true;
        ValueWriter firstWriter = null;
        for (Object value : values) {
            if (first) {
                firstWriter = ValueWriters.WRITERS.findWriterFor(value);
                first = false;
            } else {
                ValueWriter writer = ValueWriters.WRITERS.findWriterFor(value);
                if (writer != firstWriter) {
                    throw new IllegalStateException(context.getContextPath() + ": cannot write a heterogeneous array; first element was of type " + firstWriter + " but found " + writer);
                }
                context.write(", ");
            }
            ValueWriters.WRITERS.findWriterFor(value).write(value, context);
        }
        context.writeArrayDelimiterPadding();
        context.write(']');
    }

    private PrimitiveArrayValueWriter() {
    }

    public String toString() {
        return "primitive-array";
    }
}