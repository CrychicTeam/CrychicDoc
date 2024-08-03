package info.journeymap.shaded.org.eclipse.jetty.http;

public class CompressedContentFormat {

    public static final CompressedContentFormat GZIP = new CompressedContentFormat("gzip", ".gz");

    public static final CompressedContentFormat BR = new CompressedContentFormat("br", ".br");

    public static final CompressedContentFormat[] NONE = new CompressedContentFormat[0];

    public final String _encoding;

    public final String _extension;

    public final String _etag;

    public final String _etagQuote;

    public final PreEncodedHttpField _contentEncoding;

    public CompressedContentFormat(String encoding, String extension) {
        this._encoding = encoding;
        this._extension = extension;
        this._etag = "--" + encoding;
        this._etagQuote = this._etag + "\"";
        this._contentEncoding = new PreEncodedHttpField(HttpHeader.CONTENT_ENCODING, encoding);
    }

    public boolean equals(Object o) {
        if (!(o instanceof CompressedContentFormat)) {
            return false;
        } else {
            CompressedContentFormat ccf = (CompressedContentFormat) o;
            if (this._encoding == null && ccf._encoding != null) {
                return false;
            } else {
                return this._extension == null && ccf._extension != null ? false : this._encoding.equalsIgnoreCase(ccf._encoding) && this._extension.equalsIgnoreCase(ccf._extension);
            }
        }
    }

    public static boolean tagEquals(String etag, String tag) {
        if (etag.equals(tag)) {
            return true;
        } else {
            int dashdash = tag.indexOf("--");
            return dashdash > 0 ? etag.regionMatches(0, tag, 0, dashdash - 2) : false;
        }
    }
}