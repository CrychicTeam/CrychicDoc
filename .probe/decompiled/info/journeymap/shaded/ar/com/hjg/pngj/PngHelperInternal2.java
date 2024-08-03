package info.journeymap.shaded.ar.com.hjg.pngj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

final class PngHelperInternal2 {

    static OutputStream ostreamFromFile(File f, boolean allowoverwrite) {
        FileOutputStream os = null;
        if (f.exists() && !allowoverwrite) {
            throw new PngjOutputException("File already exists: " + f);
        } else {
            try {
                return new FileOutputStream(f);
            } catch (Exception var4) {
                throw new PngjInputException("Could not open for write" + f, var4);
            }
        }
    }
}