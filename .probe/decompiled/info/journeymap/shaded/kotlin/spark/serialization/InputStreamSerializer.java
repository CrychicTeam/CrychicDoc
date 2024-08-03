package info.journeymap.shaded.kotlin.spark.serialization;

import info.journeymap.shaded.kotlin.spark.utils.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class InputStreamSerializer extends Serializer {

    @Override
    public boolean canProcess(Object element) {
        return element instanceof InputStream;
    }

    @Override
    public void process(OutputStream outputStream, Object element) throws IOException {
        IOUtils.copy((InputStream) element, outputStream);
    }
}