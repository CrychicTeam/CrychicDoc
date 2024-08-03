package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public abstract class PngChunkTextVar extends PngChunkMultiple {

    protected String key;

    protected String val;

    public static final String KEY_Title = "Title";

    public static final String KEY_Author = "Author";

    public static final String KEY_Description = "Description";

    public static final String KEY_Copyright = "Copyright";

    public static final String KEY_Creation_Time = "Creation Time";

    public static final String KEY_Software = "Software";

    public static final String KEY_Disclaimer = "Disclaimer";

    public static final String KEY_Warning = "Warning";

    public static final String KEY_Source = "Source";

    public static final String KEY_Comment = "Comment";

    protected PngChunkTextVar(String id, ImageInfo info) {
        super(id, info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NONE;
    }

    public String getKey() {
        return this.key;
    }

    public String getVal() {
        return this.val;
    }

    public void setKeyVal(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public static class PngTxtInfo {

        public String title;

        public String author;

        public String description;

        public String creation_time;

        public String software;

        public String disclaimer;

        public String warning;

        public String source;

        public String comment;
    }
}