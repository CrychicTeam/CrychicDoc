package icyllis.modernui.util;

import icyllis.modernui.ModernUI;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;

public class PmxModel {

    public float mPmxVersion;

    public Charset mTextEncoding;

    public byte mAdditionalUV;

    public String mModelName;

    public String mModelNameEn;

    public String mModelComment;

    public String mModelCommentEn;

    public PmxModel.Vertex[] mVertices;

    @Nonnull
    public static PmxModel decode(@Nonnull FileChannel channel) throws IOException {
        ByteBuffer buf = channel.map(MapMode.READ_ONLY, 0L, channel.size());
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.rewind();
        PmxModel model = new PmxModel();
        model.read(buf);
        return model;
    }

    private void read(@Nonnull ByteBuffer buf) {
        if (buf.get() == 80 && buf.get() == 77 && buf.get() == 88 && buf.get() == 32) {
            this.mPmxVersion = buf.getFloat();
            if (this.mPmxVersion != 2.0F && this.mPmxVersion != 2.1F) {
                throw new IllegalStateException("Not PMX v2.0 or v2.1 but " + this.mPmxVersion);
            } else {
                byte[] settings = new byte[buf.get()];
                if (settings.length < 8) {
                    throw new IllegalStateException();
                } else {
                    buf.get(settings);
                    this.mTextEncoding = settings[0] == 0 ? StandardCharsets.UTF_16LE : StandardCharsets.UTF_8;
                    this.mAdditionalUV = settings[1];
                    this.mModelName = this.readText(buf);
                    this.mModelNameEn = this.readText(buf);
                    this.mModelComment = this.readText(buf);
                    this.mModelCommentEn = this.readText(buf);
                }
            }
        } else {
            throw new IllegalStateException("Not PMX format");
        }
    }

    @Nonnull
    private String readText(@Nonnull ByteBuffer buf) {
        int len = buf.getInt();
        if (len == 0) {
            return "";
        } else {
            byte[] bytes = new byte[len];
            buf.get(bytes);
            return new String(bytes, this.mTextEncoding);
        }
    }

    public void debug() {
        ModernUI.LOGGER.info(this.mModelName);
        ModernUI.LOGGER.info(this.mModelComment);
    }

    public static class Vertex {
    }
}