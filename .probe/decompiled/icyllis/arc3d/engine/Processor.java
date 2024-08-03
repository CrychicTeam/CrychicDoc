package icyllis.arc3d.engine;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class Processor {

    public static final int Null_ClassID = 0;

    public static final int CircularRRect_Geom_ClassID = 1;

    public static final int Circle_Geom_ClassID = 2;

    public static final int RoundRect_GeoProc_ClassID = 3;

    public static final int DefaultGeoProc_ClassID = 4;

    public static final int SDFRect_GeoProc_ClassID = 5;

    public static final int Hard_XferProc_ClassID = 6;

    protected final int mClassID;

    protected Processor(int classID) {
        this.mClassID = classID;
    }

    @Nonnull
    public abstract String name();

    public final int classID() {
        return this.mClassID;
    }
}