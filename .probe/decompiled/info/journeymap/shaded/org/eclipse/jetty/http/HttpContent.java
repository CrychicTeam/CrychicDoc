package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

public interface HttpContent {

    HttpField getContentType();

    String getContentTypeValue();

    String getCharacterEncoding();

    MimeTypes.Type getMimeType();

    HttpField getContentEncoding();

    String getContentEncodingValue();

    HttpField getContentLength();

    long getContentLengthValue();

    HttpField getLastModified();

    String getLastModifiedValue();

    HttpField getETag();

    String getETagValue();

    ByteBuffer getIndirectBuffer();

    ByteBuffer getDirectBuffer();

    Resource getResource();

    InputStream getInputStream() throws IOException;

    ReadableByteChannel getReadableByteChannel() throws IOException;

    void release();

    Map<CompressedContentFormat, ? extends HttpContent> getPrecompressedContents();

    public interface ContentFactory {

        HttpContent getContent(String var1, int var2) throws IOException;
    }
}