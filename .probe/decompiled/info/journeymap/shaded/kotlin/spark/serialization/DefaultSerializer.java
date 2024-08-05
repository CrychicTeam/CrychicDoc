package info.journeymap.shaded.kotlin.spark.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

class DefaultSerializer extends Serializer {

    @Override
    public boolean canProcess(Object element) {
        return true;
    }

    @Override
    public void process(OutputStream outputStream, Object element) throws IOException {
        try {
            outputStream.write(element.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException var4) {
            throw new IOException(var4);
        }
    }
}