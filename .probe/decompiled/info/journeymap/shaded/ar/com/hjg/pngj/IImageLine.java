package info.journeymap.shaded.ar.com.hjg.pngj;

public interface IImageLine {

    void readFromPngRaw(byte[] var1, int var2, int var3, int var4);

    void endReadFromPngRaw();

    void writeToPngRaw(byte[] var1);
}