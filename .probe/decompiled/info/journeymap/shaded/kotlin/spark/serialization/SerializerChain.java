package info.journeymap.shaded.kotlin.spark.serialization;

import java.io.IOException;
import java.io.OutputStream;

public final class SerializerChain {

    private Serializer root;

    public SerializerChain() {
        DefaultSerializer defaultSerializer = new DefaultSerializer();
        InputStreamSerializer inputStreamSerializer = new InputStreamSerializer();
        inputStreamSerializer.setNext(defaultSerializer);
        BytesSerializer bytesSerializer = new BytesSerializer();
        bytesSerializer.setNext(inputStreamSerializer);
        this.root = bytesSerializer;
    }

    public void process(OutputStream outputStream, Object element) throws IOException {
        this.root.processElement(outputStream, element);
    }
}