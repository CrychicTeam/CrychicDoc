package info.journeymap.shaded.kotlin.spark.serialization;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Serializer {

    private Serializer next;

    public void setNext(Serializer serializer) {
        this.next = serializer;
    }

    public void processElement(OutputStream outputStream, Object element) throws IOException {
        if (this.canProcess(element)) {
            this.process(outputStream, element);
        } else if (this.next != null) {
            this.next.processElement(outputStream, element);
        }
    }

    public abstract boolean canProcess(Object var1);

    public abstract void process(OutputStream var1, Object var2) throws IOException;
}