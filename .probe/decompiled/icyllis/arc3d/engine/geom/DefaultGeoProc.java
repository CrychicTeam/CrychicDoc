package icyllis.arc3d.engine.geom;

import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.KeyBuilder;
import icyllis.arc3d.engine.ShaderCaps;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DefaultGeoProc extends GeometryProcessor {

    public static final int FLAG_COLOR_ATTRIBUTE = 1;

    public static final int FLAG_TEX_COORD_ATTRIBUTE = 2;

    public static final GeometryProcessor.Attribute POSITION = new GeometryProcessor.Attribute("Pos", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute COLOR = new GeometryProcessor.Attribute("Color", (byte) 17, (byte) 16);

    public static final GeometryProcessor.Attribute TEX_COORD = new GeometryProcessor.Attribute("UV", (byte) 1, (byte) 14);

    public static final GeometryProcessor.AttributeSet VERTEX_ATTRIBS = GeometryProcessor.AttributeSet.makeImplicit(POSITION, COLOR, TEX_COORD);

    private final int mFlags;

    public DefaultGeoProc(int flags) {
        super(4);
        this.mFlags = flags;
        int mask = 1;
        if ((flags & 1) != 0) {
            mask |= 2;
        }
        if ((flags & 2) != 0) {
            mask |= 4;
        }
        this.setVertexAttributes(mask);
    }

    @Nonnull
    @Override
    public String name() {
        return "Default_GeomProc";
    }

    @Override
    public byte primitiveType() {
        return 0;
    }

    @Override
    public void appendToKey(@Nonnull KeyBuilder b) {
        b.addBits(3, this.mFlags, "gpFlags");
    }

    @Nonnull
    @Override
    public GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps caps) {
        return null;
    }

    @Override
    protected GeometryProcessor.AttributeSet allVertexAttributes() {
        return VERTEX_ATTRIBS;
    }

    @Nullable
    @Override
    protected GeometryProcessor.AttributeSet allInstanceAttributes() {
        return null;
    }
}