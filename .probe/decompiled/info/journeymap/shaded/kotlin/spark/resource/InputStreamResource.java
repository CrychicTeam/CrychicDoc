package info.journeymap.shaded.kotlin.spark.resource;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamResource {

    InputStream getInputStream() throws IOException;
}