package icyllis.modernui.graphics;

import icyllis.arc3d.core.Blender;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GpuDevice;
import icyllis.arc3d.engine.SurfaceProxy;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.TextureProxy;
import icyllis.arc3d.engine.geom.DefaultGeoProc;
import icyllis.arc3d.opengl.GLBuffer;
import icyllis.arc3d.opengl.GLCommandBuffer;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.opengl.GLProgram;
import icyllis.arc3d.opengl.GLResourceProvider;
import icyllis.arc3d.opengl.GLSampler;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.arc3d.opengl.GLVertexArray;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.font.BakedGlyph;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.EmojiFont;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntStack;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

@NotThreadSafe
public final class GLSurfaceCanvas extends Canvas {

    private static volatile GLSurfaceCanvas sInstance;

    public static final int MATRIX_BLOCK_BINDING = 0;

    public static final byte DRAW_PRIM = 0;

    public static final byte DRAW_RECT = 1;

    public static final byte DRAW_IMAGE = 2;

    public static final byte DRAW_ROUND_RECT_FILL = 3;

    public static final byte DRAW_ROUND_IMAGE = 4;

    public static final byte DRAW_ROUND_RECT_STROKE = 5;

    public static final byte DRAW_CIRCLE_FILL = 6;

    public static final byte DRAW_CIRCLE_STROKE = 7;

    public static final byte DRAW_ARC_FILL = 8;

    public static final byte DRAW_ARC_STROKE = 9;

    public static final byte DRAW_BEZIER = 10;

    public static final byte DRAW_TEXT = 11;

    public static final byte DRAW_IMAGE_LAYER = 12;

    public static final byte DRAW_CLIP_PUSH = 13;

    public static final byte DRAW_CLIP_POP = 14;

    public static final byte DRAW_MATRIX = 15;

    public static final byte DRAW_SMOOTH = 16;

    public static final byte DRAW_LAYER_PUSH = 17;

    public static final byte DRAW_LAYER_POP = 18;

    public static final byte DRAW_CUSTOM = 19;

    public static final byte DRAW_GLOW_WAVE = 20;

    public static final byte DRAW_PIE_FILL = 21;

    public static final byte DRAW_PIE_STROKE = 22;

    public static final byte DRAW_ROUND_LINE_FILL = 23;

    public static final byte DRAW_ROUND_LINE_STROKE = 24;

    public static final byte DRAW_RECT_STROKE_BEVEL = 25;

    public static final byte DRAW_RECT_STROKE_ROUND = 26;

    public static final int MATRIX_UNIFORM_SIZE = 144;

    public static final int SMOOTH_UNIFORM_SIZE = 4;

    public static final int ARC_UNIFORM_SIZE = 24;

    public static final int BEZIER_UNIFORM_SIZE = 28;

    public static final int CIRCLE_UNIFORM_SIZE = 16;

    public static final int ROUND_RECT_UNIFORM_SIZE = 24;

    static final Pools.Pool<GLSurfaceCanvas.Save> sSavePool = Pools.newSynchronizedPool(60);

    static final Matrix4 RESET_MATRIX = Matrix4.makeTranslate(0.0F, 0.0F, -3000.0F);

    private GLProgram COLOR_FILL;

    private GLProgram COLOR_TEX;

    private GLProgram ROUND_RECT_FILL;

    private GLProgram ROUND_RECT_TEX;

    private GLProgram ROUND_RECT_STROKE;

    private GLProgram CIRCLE_FILL;

    private GLProgram CIRCLE_STROKE;

    private GLProgram ARC_FILL;

    private GLProgram ARC_STROKE;

    private GLProgram BEZIER_CURVE;

    private GLProgram ALPHA_TEX;

    private GLProgram COLOR_TEX_PRE;

    private GLProgram GLOW_WAVE;

    private GLProgram PIE_FILL;

    private GLProgram PIE_STROKE;

    private GLProgram ROUND_LINE_FILL;

    private GLProgram ROUND_LINE_STROKE;

    private GLProgram RECT_STROKE_BEVEL;

    private GLProgram RECT_STROKE_ROUND;

    private GLVertexArray POS_COLOR;

    private GLVertexArray POS_COLOR_TEX;

    private GLVertexArray POS_TEX;

    public static final int TEXTURE_RECT_VERTEX_SIZE = 20;

    private final ByteArrayList mDrawOps = new ByteArrayList();

    private final IntArrayList mDrawPrims = new IntArrayList();

    private GLBuffer mColorMeshVertexBuffer;

    private ByteBuffer mColorMeshStagingBuffer = MemoryUtil.memAlloc(16384);

    private boolean mColorMeshBufferResized = true;

    private GLBuffer mTextureMeshVertexBuffer;

    private ByteBuffer mTextureMeshStagingBuffer = MemoryUtil.memAlloc(4096);

    private boolean mTextureMeshBufferResized = true;

    public static final int MAX_GLYPH_INDEX_COUNT = 3072;

    private GLBuffer mGlyphVertexBuffer;

    private ByteBuffer mGlyphStagingBuffer = MemoryUtil.memAlloc(32768);

    private boolean mGlyphBufferResized = true;

    private final GLBuffer mGlyphIndexBuffer;

    private ByteBuffer mUniformRingBuffer = MemoryUtil.memAlloc(8192);

    private final GLSurfaceCanvas.UniformBuffer mMatrixUBO = new GLSurfaceCanvas.UniformBuffer();

    private final GLSurfaceCanvas.UniformBuffer mSmoothUBO = new GLSurfaceCanvas.UniformBuffer();

    private final GLSurfaceCanvas.UniformBuffer mArcUBO = new GLSurfaceCanvas.UniformBuffer();

    private final GLSurfaceCanvas.UniformBuffer mBezierUBO = new GLSurfaceCanvas.UniformBuffer();

    private final GLSurfaceCanvas.UniformBuffer mCircleUBO = new GLSurfaceCanvas.UniformBuffer();

    private final GLSurfaceCanvas.UniformBuffer mRoundRectUBO = new GLSurfaceCanvas.UniformBuffer();

    @SharedPtr
    private final GLSampler mLinearSampler;

    private final ByteBuffer mLayerImageMemory = MemoryUtil.memAlloc(80);

    private int mCurrTexture;

    private GLSampler mCurrSampler;

    private final IntList mClipRefs = new IntArrayList();

    private final IntList mLayerAlphas = new IntArrayList();

    private final IntStack mLayerStack = new IntArrayList(3);

    private final Queue<SurfaceView> mTextures = new ArrayDeque();

    private final List<GLSurfaceCanvas.DrawTextOp> mDrawTexts = new ArrayList();

    private final Queue<CustomDrawable.DrawHandler> mCustoms = new ArrayDeque();

    private final List<SurfaceProxy> mTexturesToClean = new ArrayList();

    private final Matrix4 mProjection = new Matrix4();

    private final FloatBuffer mProjectionUpload = MemoryUtil.memAllocFloat(16);

    private final GLDevice mDevice;

    private boolean mNeedsTexBinding;

    final ArrayDeque<GLSurfaceCanvas.Save> mSaves = new ArrayDeque();

    final Matrix4 mLastMatrix = new Matrix4();

    float mLastSmooth;

    int mWidth;

    int mHeight;

    final Rect2i mTmpRectI = new Rect2i();

    final Rect2f mTmpRectF = new Rect2f();

    private ImageInfo mInfo;

    private final float[] mTmpColor = new float[4];

    @RenderThread
    public GLSurfaceCanvas(GLDevice device) {
        this.mDevice = device;
        this.mMatrixUBO.allocate(144L);
        this.mSmoothUBO.allocate(4L);
        this.mArcUBO.allocate(24L);
        this.mBezierUBO.allocate(28L);
        this.mCircleUBO.allocate(16L);
        this.mRoundRectUBO.allocate(24L);
        this.mLinearSampler = (GLSampler) Objects.requireNonNull(device.getResourceProvider().findOrCreateCompatibleSampler(1188369), "Failed to create font sampler");
        ShortBuffer indices = MemoryUtil.memAllocShort(3072);
        int baseIndex = 0;
        for (int i = 0; i < 512; i++) {
            indices.put((short) baseIndex);
            indices.put((short) (baseIndex + 1));
            indices.put((short) (baseIndex + 2));
            indices.put((short) (baseIndex + 2));
            indices.put((short) (baseIndex + 1));
            indices.put((short) (baseIndex + 3));
            baseIndex += 4;
        }
        indices.flip();
        this.mGlyphIndexBuffer = (GLBuffer) Objects.requireNonNull(GLBuffer.make(device, indices.capacity(), 66), "Failed to create index buffer for glyph mesh");
        this.mGlyphIndexBuffer.updateData(0, indices.capacity(), MemoryUtil.memAddress(indices));
        MemoryUtil.memFree(indices);
        ModernUI.LOGGER.debug(MARKER, "Created glyph index buffer: {}, size: {}", this.mGlyphIndexBuffer.getHandle(), this.mGlyphIndexBuffer.getSize());
        this.mSaves.push(new GLSurfaceCanvas.Save());
        this.loadPipelines();
    }

    @RenderThread
    public static GLSurfaceCanvas initialize() {
        Core.checkRenderThread();
        if (sInstance == null) {
            sInstance = new GLSurfaceCanvas((GLDevice) Core.requireDirectContext().getDevice());
        }
        return sInstance;
    }

    public static GLSurfaceCanvas getInstance() {
        return sInstance;
    }

    private void loadPipelines() {
        GLVertexArray aPosColor = GLVertexArray.make(this.mDevice, new DefaultGeoProc(1));
        DefaultGeoProc gp = new DefaultGeoProc(3);
        assert gp.vertexStride() == 20;
        GLVertexArray aPosColorUV = GLVertexArray.make(this.mDevice, gp);
        GLVertexArray aPosUV = GLVertexArray.make(this.mDevice, new DefaultGeoProc(2));
        Objects.requireNonNull(aPosColor);
        Objects.requireNonNull(aPosColorUV);
        Objects.requireNonNull(aPosUV);
        boolean compat = !this.mDevice.getCaps().hasDSASupport();
        int posColor = this.createStage("pos_color.vert", compat);
        int posColorTex = this.createStage("pos_color_tex.vert", compat);
        int posTex = this.createStage("pos_tex.vert", compat);
        int colorFill = this.createStage("color_fill.frag", compat);
        int colorTex = this.createStage("color_tex.frag", compat);
        int roundRectFill = this.createStage("round_rect_fill.frag", compat);
        int roundRectTex = this.createStage("round_rect_tex.frag", compat);
        int roundRectStroke = this.createStage("round_rect_stroke.frag", compat);
        int circleFill = this.createStage("circle_fill.frag", compat);
        int circleStroke = this.createStage("circle_stroke.frag", compat);
        int arcFill = this.createStage("arc_fill.frag", compat);
        int arcStroke = this.createStage("arc_stroke.frag", compat);
        int quadBezier = this.createStage("quadratic_bezier.frag", compat);
        int alphaTex = this.createStage("alpha_tex.frag", compat);
        int colorTexPre = this.createStage("color_tex_pre.frag", compat);
        int glowWave = this.createStage("glow_wave.frag", compat);
        int pieFill = this.createStage("pie_fill.frag", compat);
        int pieStroke = this.createStage("pie_stroke.frag", compat);
        int roundLineFill = this.createStage("round_line_fill.frag", compat);
        int roundLineStroke = this.createStage("round_line_stroke.frag", compat);
        int rectStrokeBevel = this.createStage("rect_stroke_bevel.frag", compat);
        int rectStrokeRound = this.createStage("rect_stroke_round.frag", compat);
        int pColorFill = this.createProgram(posColor, colorFill);
        int pColorTex = this.createProgram(posColorTex, colorTex);
        int pRoundRectFill = this.createProgram(posColor, roundRectFill);
        int pRoundRectTex = this.createProgram(posColorTex, roundRectTex);
        int pRoundRectStroke = this.createProgram(posColor, roundRectStroke);
        int pCircleFill = this.createProgram(posColor, circleFill);
        int pCircleStroke = this.createProgram(posColor, circleStroke);
        int pArcFill = this.createProgram(posColor, arcFill);
        int pArcStroke = this.createProgram(posColor, arcStroke);
        int pBezierCurve = this.createProgram(posColor, quadBezier);
        int pAlphaTex = this.createProgram(posTex, alphaTex);
        int pColorTexPre = this.createProgram(posColorTex, colorTexPre);
        int pGlowWave = this.createProgram(posColor, glowWave);
        int pPieFill = this.createProgram(posColor, pieFill);
        int pPieStroke = this.createProgram(posColor, pieStroke);
        int pRoundLineFill = this.createProgram(posColor, roundLineFill);
        int pRoundLineStroke = this.createProgram(posColor, roundLineStroke);
        int pRectStrokeBevel = this.createProgram(posColor, rectStrokeBevel);
        int pRectStrokeRound = this.createProgram(posColor, rectStrokeRound);
        boolean success = pColorFill != 0 && pColorTex != 0 && pRoundRectFill != 0 && pRoundRectTex != 0 && pRoundRectStroke != 0 && pCircleFill != 0 && pCircleStroke != 0 && pArcFill != 0 && pArcStroke != 0 && pBezierCurve != 0 && pAlphaTex != 0 && pColorTexPre != 0 && pGlowWave != 0 && pPieFill != 0 && pPieStroke != 0 && pRoundLineFill != 0 && pRoundLineStroke != 0 && pRectStrokeBevel != 0 && pRectStrokeRound != 0;
        if (!success) {
            throw new RuntimeException("Failed to link shader programs");
        } else {
            if (compat) {
                this.bindProgramMatrixBlock(pColorFill);
                this.bindProgramFragLocation(pColorFill);
                this.bindProgramMatrixBlock(pColorTex);
                this.bindProgramFragLocation(pColorTex);
                this.bindProgramMatrixBlock(pColorTexPre);
                this.bindProgramFragLocation(pColorTexPre);
                this.bindProgramMatrixBlock(pAlphaTex);
                this.bindProgramFragLocation(pAlphaTex);
                this.bindProgramMatrixBlock(pGlowWave);
                this.bindProgramFragLocation(pGlowWave);
                this.bindProgramMatrixBlock(pArcFill);
                this.bindProgramFragLocation(pArcFill);
                this.bindProgramArcBlock(pArcFill);
                this.bindProgramSmoothBlock(pArcFill);
                this.bindProgramMatrixBlock(pArcStroke);
                this.bindProgramFragLocation(pArcStroke);
                this.bindProgramArcBlock(pArcStroke);
                this.bindProgramSmoothBlock(pArcStroke);
                this.bindProgramMatrixBlock(pPieFill);
                this.bindProgramFragLocation(pPieFill);
                this.bindProgramArcBlock(pPieFill);
                this.bindProgramSmoothBlock(pPieFill);
                this.bindProgramMatrixBlock(pPieStroke);
                this.bindProgramFragLocation(pPieStroke);
                this.bindProgramArcBlock(pPieStroke);
                this.bindProgramSmoothBlock(pPieStroke);
                this.bindProgramMatrixBlock(pBezierCurve);
                this.bindProgramFragLocation(pBezierCurve);
                int c3 = GLCore.glGetUniformBlockIndex(pBezierCurve, "PaintBlock");
                GLCore.glUniformBlockBinding(pBezierCurve, c3, 3);
                this.bindProgramSmoothBlock(pBezierCurve);
                this.bindProgramMatrixBlock(pCircleFill);
                this.bindProgramFragLocation(pCircleFill);
                this.bindProgramCircleBlock(pCircleFill);
                this.bindProgramSmoothBlock(pCircleFill);
                this.bindProgramMatrixBlock(pCircleStroke);
                this.bindProgramFragLocation(pCircleStroke);
                this.bindProgramCircleBlock(pCircleStroke);
                this.bindProgramSmoothBlock(pCircleStroke);
                this.bindProgramMatrixBlock(pRoundLineFill);
                this.bindProgramFragLocation(pRoundLineFill);
                this.bindProgramRoundRectBlock(pRoundLineFill);
                this.bindProgramSmoothBlock(pRoundLineFill);
                this.bindProgramMatrixBlock(pRoundLineStroke);
                this.bindProgramFragLocation(pRoundLineStroke);
                this.bindProgramRoundRectBlock(pRoundLineStroke);
                this.bindProgramSmoothBlock(pRoundLineStroke);
                this.bindProgramMatrixBlock(pRoundRectFill);
                this.bindProgramFragLocation(pRoundRectFill);
                this.bindProgramRoundRectBlock(pRoundRectFill);
                this.bindProgramSmoothBlock(pRoundRectFill);
                this.bindProgramMatrixBlock(pRoundRectStroke);
                this.bindProgramFragLocation(pRoundRectStroke);
                this.bindProgramRoundRectBlock(pRoundRectStroke);
                this.bindProgramSmoothBlock(pRoundRectStroke);
                this.bindProgramMatrixBlock(pRectStrokeBevel);
                this.bindProgramFragLocation(pRectStrokeBevel);
                this.bindProgramRoundRectBlock(pRectStrokeBevel);
                this.bindProgramSmoothBlock(pRectStrokeBevel);
                this.bindProgramMatrixBlock(pRectStrokeRound);
                this.bindProgramFragLocation(pRectStrokeRound);
                this.bindProgramRoundRectBlock(pRectStrokeRound);
                this.bindProgramSmoothBlock(pRectStrokeRound);
                this.bindProgramMatrixBlock(pRoundRectTex);
                this.bindProgramFragLocation(pRoundRectTex);
                this.bindProgramRoundRectBlock(pRoundRectTex);
                this.bindProgramSmoothBlock(pRoundRectTex);
                this.mNeedsTexBinding = true;
            }
            this.COLOR_FILL = new GLProgram(this.mDevice, pColorFill);
            this.COLOR_TEX = new GLProgram(this.mDevice, pColorTex);
            this.ROUND_RECT_FILL = new GLProgram(this.mDevice, pRoundRectFill);
            this.ROUND_RECT_TEX = new GLProgram(this.mDevice, pRoundRectTex);
            this.ROUND_RECT_STROKE = new GLProgram(this.mDevice, pRoundRectStroke);
            this.CIRCLE_FILL = new GLProgram(this.mDevice, pCircleFill);
            this.CIRCLE_STROKE = new GLProgram(this.mDevice, pCircleStroke);
            this.ARC_FILL = new GLProgram(this.mDevice, pArcFill);
            this.ARC_STROKE = new GLProgram(this.mDevice, pArcStroke);
            this.BEZIER_CURVE = new GLProgram(this.mDevice, pBezierCurve);
            this.ALPHA_TEX = new GLProgram(this.mDevice, pAlphaTex);
            this.COLOR_TEX_PRE = new GLProgram(this.mDevice, pColorTexPre);
            this.GLOW_WAVE = new GLProgram(this.mDevice, pGlowWave);
            this.PIE_FILL = new GLProgram(this.mDevice, pPieFill);
            this.PIE_STROKE = new GLProgram(this.mDevice, pPieStroke);
            this.ROUND_LINE_FILL = new GLProgram(this.mDevice, pRoundLineFill);
            this.ROUND_LINE_STROKE = new GLProgram(this.mDevice, pRoundLineStroke);
            this.RECT_STROKE_BEVEL = new GLProgram(this.mDevice, pRectStrokeBevel);
            this.RECT_STROKE_ROUND = new GLProgram(this.mDevice, pRectStrokeRound);
            this.POS_COLOR = aPosColor;
            this.POS_COLOR_TEX = aPosColorUV;
            this.POS_TEX = aPosUV;
            ModernUI.LOGGER.info("Loaded OpenGL canvas shaders, compatibility mode: " + compat);
        }
    }

    private int createStage(String entry, boolean compat) {
        int sp = entry.length() - 5;
        String path = "shaders/" + (compat ? entry.substring(0, sp) + "_330" + entry.substring(sp) : entry);
        int type;
        if (entry.endsWith(".vert")) {
            type = 35633;
        } else if (entry.endsWith(".frag")) {
            type = 35632;
        } else {
            if (!entry.endsWith(".geom")) {
                throw new RuntimeException();
            }
            type = 36313;
        }
        ByteBuffer source = null;
        try {
            InputStream stream = ModernUI.getInstance().getResourceStream("modernui", path);
            int var8;
            try {
                source = Core.readIntoNativeBuffer(stream).flip();
                var8 = GLCore.glCompileShader(type, source, this.mDevice.getPipelineStateCache().getStats(), this.mDevice.getContext().getErrorWriter());
            } catch (Throwable var16) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                    }
                }
                throw var16;
            }
            if (stream != null) {
                stream.close();
            }
            return var8;
        } catch (IOException var17) {
            ModernUI.LOGGER.error(MARKER, "Failed to get shader source {}:{}\n", "modernui", path, var17);
        } finally {
            MemoryUtil.memFree(source);
        }
        return 0;
    }

    public int createProgram(int... stages) {
        int program = GLCore.glCreateProgram();
        if (program == 0) {
            return 0;
        } else {
            for (int s : stages) {
                GLCore.glAttachShader(program, s);
            }
            GLCore.glLinkProgram(program);
            if (GLCore.glGetProgrami(program, 35714) == 0) {
                String log = GLCore.glGetProgramInfoLog(program, 8192);
                ModernUI.LOGGER.error(MARKER, "Failed to link shader program\n{}", log);
                GLCore.glDeleteProgram(program);
                return 0;
            } else {
                for (int s : stages) {
                    GLCore.glDetachShader(program, s);
                }
                return program;
            }
        }
    }

    private void bindProgramMatrixBlock(int program) {
        int c0 = GLCore.glGetUniformBlockIndex(program, "MatrixBlock");
        GLCore.glUniformBlockBinding(program, c0, 0);
    }

    private void bindProgramSmoothBlock(int program) {
        int c1 = GLCore.glGetUniformBlockIndex(program, "SmoothBlock");
        GLCore.glUniformBlockBinding(program, c1, 1);
    }

    private void bindProgramArcBlock(int program) {
        int c2 = GLCore.glGetUniformBlockIndex(program, "PaintBlock");
        GLCore.glUniformBlockBinding(program, c2, 2);
    }

    private void bindProgramCircleBlock(int program) {
        int c4 = GLCore.glGetUniformBlockIndex(program, "PaintBlock");
        GLCore.glUniformBlockBinding(program, c4, 4);
    }

    private void bindProgramRoundRectBlock(int program) {
        int c5 = GLCore.glGetUniformBlockIndex(program, "PaintBlock");
        GLCore.glUniformBlockBinding(program, c5, 5);
    }

    private void bindProgramFragLocation(int program) {
        GLCore.glBindFragDataLocation(program, 0, "fragColor");
    }

    private void bindProgramTexBinding(int program) {
        GLCore.glUseProgram(program);
        int u0 = GLCore.glGetUniformLocation(program, "u_Sampler");
        GLCore.glUniform1i(u0, 0);
    }

    public void reset(int width, int height) {
        while (this.mSaves.size() > 1) {
            sSavePool.release((GLSurfaceCanvas.Save) this.mSaves.poll());
        }
        GLSurfaceCanvas.Save s = (GLSurfaceCanvas.Save) this.mSaves.element();
        s.mClip.set(0, 0, width, height);
        s.mMatrix.set(RESET_MATRIX);
        s.mClipRef = 0;
        s.mColorBuf = 0;
        this.mLastMatrix.setZero();
        this.mLastSmooth = -1.0F;
        this.mWidth = width;
        this.mHeight = height;
        this.mDrawOps.clear();
        this.mClipRefs.clear();
        this.mLayerAlphas.clear();
        this.mDrawTexts.clear();
        this.mColorMeshStagingBuffer.clear();
        this.mTextureMeshStagingBuffer.clear();
        this.mUniformRingBuffer.clear();
        this.mInfo = ImageInfo.make(width, height, 6, 2, null);
    }

    @Override
    public int getSaveCount() {
        return this.mSaves.size();
    }

    @Nonnull
    @Override
    public Matrix4 getMatrix() {
        return ((GLSurfaceCanvas.Save) this.mSaves.element()).mMatrix;
    }

    @Nonnull
    GLSurfaceCanvas.Save getSave() {
        return (GLSurfaceCanvas.Save) this.mSaves.getFirst();
    }

    public void destroy() {
        this.COLOR_FILL.unref();
        this.COLOR_TEX.unref();
        this.ROUND_RECT_FILL.unref();
        this.ROUND_RECT_TEX.unref();
        this.ROUND_RECT_STROKE.unref();
        this.CIRCLE_FILL.unref();
        this.CIRCLE_STROKE.unref();
        this.ARC_FILL.unref();
        this.ARC_STROKE.unref();
        this.BEZIER_CURVE.unref();
        this.ALPHA_TEX.unref();
        this.COLOR_TEX_PRE.unref();
        this.GLOW_WAVE.unref();
        this.PIE_FILL.unref();
        this.PIE_STROKE.unref();
        this.ROUND_LINE_FILL.unref();
        this.ROUND_LINE_STROKE.unref();
        this.RECT_STROKE_BEVEL.unref();
        this.RECT_STROKE_ROUND.unref();
        this.POS_COLOR.unref();
        this.POS_COLOR_TEX.unref();
        this.POS_TEX.unref();
        this.mMatrixUBO.close();
        this.mSmoothUBO.close();
        this.mArcUBO.close();
        this.mBezierUBO.close();
        this.mCircleUBO.close();
        this.mRoundRectUBO.close();
        this.mLinearSampler.unref();
        this.mTextures.forEach(v -> {
            if (v.getSurface() != null) {
                v.getSurface().unref();
            }
        });
        this.mTextures.clear();
        this.mTexturesToClean.forEach(RefCnt::unref);
        this.mTexturesToClean.clear();
    }

    @RenderThread
    public void setProjection(@NonNull Matrix4 projection) {
        projection.store(this.mProjectionUpload.clear());
    }

    @NonNull
    @RenderThread
    public FloatBuffer getProjection() {
        return this.mProjectionUpload.rewind();
    }

    private GLVertexArray bindPipeline(GLProgram program, GLVertexArray vertexArray) {
        GLCommandBuffer cmdBuffer = this.mDevice.currentCommandBuffer();
        cmdBuffer.bindPipeline(program, vertexArray);
        return vertexArray;
    }

    @RenderThread
    public void bindSampler(GLSampler sampler) {
        if (this.mCurrSampler != sampler) {
            if (sampler != null) {
                GLCore.glBindSampler(0, sampler.getHandle());
            } else {
                GLCore.glBindSampler(0, 0);
            }
            this.mCurrSampler = sampler;
        }
    }

    @RenderThread
    public void bindTexture(int texture) {
        if (this.mCurrTexture != texture) {
            GLCore.glBindTexture(3553, texture);
            this.mCurrTexture = texture;
        }
    }

    private boolean bindNextTexture(boolean texSampling) {
        SurfaceView textureView = (SurfaceView) this.mTextures.remove();
        SurfaceProxy userTexture = textureView.getSurface();
        boolean success = true;
        if (!userTexture.isInstantiated()) {
            GLResourceProvider resourceProvider = this.mDevice.getResourceProvider();
            success = userTexture.doLazyInstantiation(resourceProvider);
        }
        if (success) {
            GLTexture glTexture = (GLTexture) Objects.requireNonNull(userTexture.getGpuTexture());
            if (texSampling) {
                this.bindSampler(null);
            } else {
                this.bindSampler(this.mLinearSampler);
            }
            this.bindTexture(glTexture.getHandle());
            if (glTexture.isMipmapped()) {
                this.mDevice.generateMipmaps(glTexture);
            }
        }
        this.mTexturesToClean.add(userTexture);
        return success;
    }

    @RenderThread
    public boolean executeRenderPass(@Nullable GLSurface framebuffer) {
        Core.checkRenderThread();
        Core.flushRenderCalls();
        if (framebuffer != null) {
            framebuffer.bindDraw();
            framebuffer.makeBuffers(this.mWidth, this.mHeight, false);
            framebuffer.clearColorBuffer();
            framebuffer.clearStencilBuffer();
        }
        if (this.mDrawOps.isEmpty()) {
            return false;
        } else if (this.getSaveCount() != 1) {
            throw new IllegalStateException("Unbalanced save-restore pair: " + this.getSaveCount());
        } else {
            this.mDevice.forceResetContext(4);
            this.mMatrixUBO.upload(0L, 64L, MemoryUtil.memAddress(this.mProjectionUpload.flip()));
            this.uploadVertexBuffers();
            if (this.mNeedsTexBinding) {
                this.bindProgramTexBinding(this.ALPHA_TEX.getProgram());
                this.bindProgramTexBinding(this.COLOR_TEX.getProgram());
                this.bindProgramTexBinding(this.COLOR_TEX_PRE.getProgram());
                this.bindProgramTexBinding(this.ROUND_RECT_TEX.getProgram());
                this.mNeedsTexBinding = false;
            }
            this.mMatrixUBO.bindBase(35345, 0);
            this.mSmoothUBO.bindBase(35345, 1);
            this.mArcUBO.bindBase(35345, 2);
            this.mBezierUBO.bindBase(35345, 3);
            this.mCircleUBO.bindBase(35345, 4);
            this.mRoundRectUBO.bindBase(35345, 5);
            GLCore.glStencilFunc(514, 0, 255);
            GLCore.glStencilMask(255);
            this.mDevice.setTextureUnit(0);
            GLCore.glActiveTexture(33984);
            GLCore.glBindSampler(0, 0);
            GLCore.glBindVertexArray(0);
            GLCore.glUseProgram(0);
            this.mCurrSampler = null;
            this.mCurrTexture = 0;
            long uniformDataPtr = MemoryUtil.memAddress(this.mUniformRingBuffer.flip());
            int posColorIndex = 0;
            int posColorTexIndex = 4;
            int primIndex = 0;
            int clipIndex = 0;
            int textIndex = 0;
            int textBaseVertex = 0;
            int alphaIndex = 0;
            int colorBuffer = 36064;
            GpuDevice.Stats stats = this.mDevice.getStats();
            int nDraws = 0;
            ByteListIterator i = this.mDrawOps.iterator();
            while (i.hasNext()) {
                int op = (Byte) i.next();
                switch(op) {
                    case 0:
                        this.bindPipeline(this.COLOR_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        int prim = this.mDrawPrims.getInt(primIndex++);
                        int n = prim & 65535;
                        GLCore.glDrawArrays(prim >> 16, posColorIndex, n);
                        nDraws++;
                        posColorIndex += n;
                        break;
                    case 1:
                        this.bindPipeline(this.COLOR_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 2:
                        this.bindPipeline(this.COLOR_TEX, this.POS_COLOR_TEX).bindVertexBuffer(this.mTextureMeshVertexBuffer, 0L);
                        if (this.bindNextTexture(false)) {
                            GLCore.glDrawArrays(5, posColorTexIndex, 4);
                            nDraws++;
                        }
                        posColorTexIndex += 4;
                        break;
                    case 3:
                        this.bindPipeline(this.ROUND_RECT_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 20L, uniformDataPtr);
                        uniformDataPtr += 20L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 4:
                        this.bindPipeline(this.ROUND_RECT_TEX, this.POS_COLOR_TEX).bindVertexBuffer(this.mTextureMeshVertexBuffer, 0L);
                        if (this.bindNextTexture(false)) {
                            this.mRoundRectUBO.upload(0L, 20L, uniformDataPtr);
                            GLCore.glDrawArrays(5, posColorTexIndex, 4);
                            nDraws++;
                        }
                        uniformDataPtr += 20L;
                        posColorTexIndex += 4;
                        break;
                    case 5:
                        this.bindPipeline(this.ROUND_RECT_STROKE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 6:
                        this.bindPipeline(this.CIRCLE_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mCircleUBO.upload(0L, 12L, uniformDataPtr);
                        uniformDataPtr += 12L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 7:
                        this.bindPipeline(this.CIRCLE_STROKE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mCircleUBO.upload(0L, 16L, uniformDataPtr);
                        uniformDataPtr += 16L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 8:
                        this.bindPipeline(this.ARC_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mArcUBO.upload(0L, 20L, uniformDataPtr);
                        uniformDataPtr += 20L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 9:
                        this.bindPipeline(this.ARC_STROKE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mArcUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 10:
                        this.bindPipeline(this.BEZIER_CURVE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mBezierUBO.upload(0L, 28L, uniformDataPtr);
                        uniformDataPtr += 28L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 11:
                        this.mMatrixUBO.upload(128L, 16L, uniformDataPtr);
                        uniformDataPtr += 16L;
                        GLSurfaceCanvas.DrawTextOp textOp = (GLSurfaceCanvas.DrawTextOp) this.mDrawTexts.get(textIndex++);
                        int limit = textOp.mVisibleGlyphCount;
                        if (limit == 0) {
                            break;
                        }
                        this.bindPipeline(this.ALPHA_TEX, this.POS_TEX);
                        this.POS_TEX.bindIndexBuffer(this.mGlyphIndexBuffer);
                        this.POS_TEX.bindVertexBuffer(this.mGlyphVertexBuffer, 0L);
                        this.bindTexture(textOp.mTexture);
                        this.bindSampler(this.mLinearSampler);
                        int lastPrim = 0;
                        while (lastPrim < limit) {
                            int primCount = Math.min(limit - lastPrim, 512);
                            GLCore.nglDrawElementsBaseVertex(4, primCount * 6, 5123, 0L, textBaseVertex);
                            nDraws++;
                            lastPrim += primCount;
                            textBaseVertex += primCount << 2;
                        }
                        break;
                    case 12:
                        this.bindPipeline(this.COLOR_TEX_PRE, this.POS_COLOR_TEX).bindVertexBuffer(this.mTextureMeshVertexBuffer, 0L);
                        if (this.bindNextTexture(true)) {
                            GLCore.glDrawArrays(5, posColorTexIndex, 4);
                            nDraws++;
                        }
                        posColorTexIndex += 4;
                        break;
                    case 13:
                        int clipRef = this.mClipRefs.getInt(clipIndex);
                        if (clipRef >= 0) {
                            GLCore.glStencilOp(7680, 7680, 7682);
                            GLCore.glColorMask(false, false, false, false);
                            this.bindPipeline(this.COLOR_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                            GLCore.glDrawArrays(5, posColorIndex, 4);
                            nDraws++;
                            posColorIndex += 4;
                            GLCore.glStencilOp(7680, 7680, 7680);
                            GLCore.glColorMask(true, true, true, true);
                        }
                        GLCore.glStencilFunc(514, Math.abs(clipRef), 255);
                        clipIndex++;
                        break;
                    case 14:
                        int clipRef = this.mClipRefs.getInt(clipIndex);
                        if (clipRef >= 0) {
                            GLCore.glStencilFunc(513, clipRef, 255);
                            GLCore.glStencilOp(7680, 7680, 7681);
                            GLCore.glColorMask(false, false, false, false);
                            this.bindPipeline(this.COLOR_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                            GLCore.glDrawArrays(5, posColorIndex, 4);
                            nDraws++;
                            posColorIndex += 4;
                            GLCore.glStencilOp(7680, 7680, 7680);
                            GLCore.glColorMask(true, true, true, true);
                        }
                        GLCore.glStencilFunc(514, Math.abs(clipRef), 255);
                        clipIndex++;
                        break;
                    case 15:
                        this.mMatrixUBO.upload(64L, 64L, uniformDataPtr);
                        uniformDataPtr += 64L;
                        break;
                    case 16:
                        this.mSmoothUBO.upload(0L, 4L, uniformDataPtr);
                        uniformDataPtr += 4L;
                        break;
                    case 17:
                        assert framebuffer != null;
                        this.mLayerStack.push(this.mLayerAlphas.getInt(alphaIndex));
                        framebuffer.setDrawBuffer(++colorBuffer);
                        framebuffer.clearColorBuffer();
                        alphaIndex++;
                        break;
                    case 18:
                        assert framebuffer != null;
                        GLTexture layer = framebuffer.getAttachedTexture(colorBuffer);
                        float alpha = (float) this.mLayerStack.popInt() / 255.0F;
                        this.putRectColorUV(this.mLayerImageMemory, 0.0F, 0.0F, (float) this.mWidth, (float) this.mHeight, 1.0F, 1.0F, 1.0F, alpha, 0.0F, (float) this.mHeight / (float) layer.getHeight(), (float) this.mWidth / (float) layer.getWidth(), 0.0F);
                        this.mLayerImageMemory.flip();
                        this.mTextureMeshVertexBuffer.updateData(0, this.mLayerImageMemory.remaining(), MemoryUtil.memAddress(this.mLayerImageMemory));
                        this.mLayerImageMemory.clear();
                        this.bindPipeline(this.COLOR_TEX_PRE, this.POS_COLOR_TEX).bindVertexBuffer(this.mTextureMeshVertexBuffer, 0L);
                        this.bindSampler(null);
                        this.bindTexture(layer.getHandle());
                        framebuffer.setDrawBuffer(--colorBuffer);
                        GLCore.glDrawArrays(5, 0, 4);
                        nDraws++;
                        break;
                    case 19:
                        CustomDrawable.DrawHandler drawable = (CustomDrawable.DrawHandler) this.mCustoms.remove();
                        drawable.draw(this.mDevice.getContext(), null);
                        drawable.close();
                        this.mDevice.forceResetContext(4);
                        GLCore.glBindSampler(0, 0);
                        this.mCurrSampler = null;
                        this.mCurrTexture = 0;
                        break;
                    case 20:
                        this.bindPipeline(this.GLOW_WAVE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mMatrixUBO.upload(128L, 4L, uniformDataPtr);
                        uniformDataPtr += 4L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 21:
                        this.bindPipeline(this.PIE_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mArcUBO.upload(0L, 20L, uniformDataPtr);
                        uniformDataPtr += 20L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 22:
                        this.bindPipeline(this.PIE_STROKE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mArcUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 23:
                        this.bindPipeline(this.ROUND_LINE_FILL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 20L, uniformDataPtr);
                        uniformDataPtr += 20L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 24:
                        this.bindPipeline(this.ROUND_LINE_STROKE, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 25:
                        this.bindPipeline(this.RECT_STROKE_BEVEL, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    case 26:
                        this.bindPipeline(this.RECT_STROKE_ROUND, this.POS_COLOR).bindVertexBuffer(this.mColorMeshVertexBuffer, 0L);
                        this.mRoundRectUBO.upload(0L, 24L, uniformDataPtr);
                        uniformDataPtr += 24L;
                        GLCore.glDrawArrays(5, posColorIndex, 4);
                        nDraws++;
                        posColorIndex += 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected draw op " + op);
                }
            }
            assert this.mLayerStack.isEmpty();
            assert this.mTextures.isEmpty();
            assert this.mCustoms.isEmpty();
            this.bindSampler(null);
            GLCore.glStencilFunc(519, 0, 255);
            this.mDrawOps.clear();
            this.mDrawPrims.clear();
            this.mClipRefs.clear();
            this.mLayerAlphas.clear();
            this.mDrawTexts.clear();
            this.mUniformRingBuffer.clear();
            for (int ix = 0; ix < this.mTexturesToClean.size(); ix++) {
                ((SurfaceProxy) this.mTexturesToClean.get(ix)).unref();
            }
            this.mTexturesToClean.clear();
            stats.incNumDraws(nDraws);
            stats.incRenderPasses();
            return true;
        }
    }

    @RenderThread
    private void uploadVertexBuffers() {
        if (this.mColorMeshBufferResized) {
            GLBuffer newBuffer = GLBuffer.make(this.mDevice, this.mColorMeshStagingBuffer.capacity(), 129);
            if (newBuffer == null) {
                throw new IllegalStateException("Failed to create color mesh buffer");
            }
            newBuffer.setLabel("ColorRectMesh");
            this.mColorMeshVertexBuffer = GLBuffer.move(this.mColorMeshVertexBuffer, newBuffer);
            ModernUI.LOGGER.info("Created new color mesh buffer: {}, size: {}", newBuffer.getHandle(), newBuffer.getSize());
            this.mColorMeshBufferResized = false;
        }
        if (this.mColorMeshStagingBuffer.position() > 0) {
            this.mColorMeshStagingBuffer.flip();
            this.mColorMeshVertexBuffer.updateData(0, this.mColorMeshStagingBuffer.remaining(), MemoryUtil.memAddress(this.mColorMeshStagingBuffer));
            this.mColorMeshStagingBuffer.clear();
        }
        int preserveForLayer = 80;
        if (this.mTextureMeshBufferResized) {
            GLBuffer newBuffer = GLBuffer.make(this.mDevice, this.mTextureMeshStagingBuffer.capacity() + preserveForLayer, 129);
            if (newBuffer == null) {
                throw new IllegalStateException("Failed to create texture mesh buffer");
            }
            newBuffer.setLabel("TextureRectMesh");
            this.mTextureMeshVertexBuffer = GLBuffer.move(this.mTextureMeshVertexBuffer, newBuffer);
            ModernUI.LOGGER.info("Created new texture mesh buffer: {}, size: {}", newBuffer.getHandle(), newBuffer.getSize());
            this.mTextureMeshBufferResized = false;
        }
        if (this.mTextureMeshStagingBuffer.position() > 0) {
            this.mTextureMeshStagingBuffer.flip();
            this.mTextureMeshVertexBuffer.updateData(preserveForLayer, this.mTextureMeshStagingBuffer.remaining(), MemoryUtil.memAddress(this.mTextureMeshStagingBuffer));
            this.mTextureMeshStagingBuffer.clear();
        }
        if (!this.mDrawTexts.isEmpty()) {
            for (GLSurfaceCanvas.DrawTextOp textOp : this.mDrawTexts) {
                textOp.writeMeshData(this);
            }
            if (this.mGlyphBufferResized) {
                GLBuffer newBuffer = GLBuffer.make(this.mDevice, this.mGlyphStagingBuffer.capacity(), 129);
                if (newBuffer == null) {
                    throw new IllegalStateException("Failed to create buffer for glyph mesh");
                }
                newBuffer.setLabel("GlyphMesh");
                this.mGlyphVertexBuffer = GLBuffer.move(this.mGlyphVertexBuffer, newBuffer);
                ModernUI.LOGGER.info("Created new glyph mesh buffer: {}, size: {}", newBuffer.getHandle(), newBuffer.getSize());
                this.mGlyphBufferResized = false;
            }
            if (this.mGlyphStagingBuffer.position() > 0) {
                this.mGlyphStagingBuffer.flip();
                this.mGlyphVertexBuffer.updateData(0, this.mGlyphStagingBuffer.remaining(), MemoryUtil.memAddress(this.mGlyphStagingBuffer));
                this.mGlyphStagingBuffer.clear();
            }
        }
    }

    private ByteBuffer checkColorMeshStagingBuffer(int minBytes) {
        if (this.mColorMeshStagingBuffer.remaining() < minBytes) {
            int newCap = grow(this.mColorMeshStagingBuffer.capacity());
            this.mColorMeshStagingBuffer = MemoryUtil.memRealloc(this.mColorMeshStagingBuffer, newCap);
            this.mColorMeshBufferResized = true;
            ModernUI.LOGGER.debug(MARKER, "Grow pos color buffer to {} bytes", newCap);
        }
        return this.mColorMeshStagingBuffer;
    }

    private ByteBuffer checkTextureMeshStagingBuffer() {
        if (this.mTextureMeshStagingBuffer.remaining() < 80) {
            int newCap = grow(this.mTextureMeshStagingBuffer.capacity());
            this.mTextureMeshStagingBuffer = MemoryUtil.memRealloc(this.mTextureMeshStagingBuffer, newCap);
            this.mTextureMeshBufferResized = true;
            ModernUI.LOGGER.debug(MARKER, "Grow pos color tex buffer to {} bytes", newCap);
        }
        return this.mTextureMeshStagingBuffer;
    }

    @RenderThread
    private ByteBuffer checkGlyphStagingBuffer() {
        if (this.mGlyphStagingBuffer.remaining() < 64) {
            int newCap = grow(this.mGlyphStagingBuffer.capacity());
            this.mGlyphStagingBuffer = MemoryUtil.memRealloc(this.mGlyphStagingBuffer, newCap);
            this.mGlyphBufferResized = true;
            ModernUI.LOGGER.debug(MARKER, "Grow pos tex buffer to {} bytes", newCap);
        }
        return this.mGlyphStagingBuffer;
    }

    private ByteBuffer checkUniformStagingBuffer() {
        if (this.mUniformRingBuffer.remaining() < 64) {
            int newCap = grow(this.mUniformRingBuffer.capacity());
            this.mUniformRingBuffer = MemoryUtil.memRealloc(this.mUniformRingBuffer, newCap);
            ModernUI.LOGGER.debug(MARKER, "Grow general uniform buffer to {} bytes", newCap);
        }
        return this.mUniformRingBuffer;
    }

    public int getNativeMemoryUsage() {
        return this.mColorMeshStagingBuffer.capacity() + this.mTextureMeshStagingBuffer.capacity() + this.mGlyphStagingBuffer.capacity() + this.mUniformRingBuffer.capacity();
    }

    private static int grow(int cap) {
        return cap + (cap >> 1);
    }

    private void drawMatrix() {
        this.drawMatrix(this.getMatrix());
    }

    private void drawMatrix(@NonNull Matrix4 matrix) {
        if (!matrix.isApproxEqual(this.mLastMatrix)) {
            this.mLastMatrix.set(matrix);
            ByteBuffer buf = this.checkUniformStagingBuffer();
            matrix.store(buf);
            buf.position(buf.position() + 64);
            this.mDrawOps.add((byte) 15);
        }
    }

    @Override
    public int save() {
        int saveCount = this.getSaveCount();
        GLSurfaceCanvas.Save s = sSavePool.acquire();
        if (s == null) {
            s = this.getSave().copy();
        } else {
            s.set(this.getSave());
        }
        this.mSaves.push(s);
        return saveCount;
    }

    @Override
    public int saveLayer(float left, float top, float right, float bottom, int alpha) {
        int saveCount = this.getSaveCount();
        GLSurfaceCanvas.Save s = sSavePool.acquire();
        if (s == null) {
            s = this.getSave().copy();
        } else {
            s.set(this.getSave());
        }
        this.mSaves.push(s);
        alpha = MathUtil.clamp(alpha, 0, 255);
        if (alpha == 0) {
            s.mClip.setEmpty();
        }
        if (alpha < 255 && s.mColorBuf < 2) {
            s.mColorBuf++;
            this.mLayerAlphas.add(alpha);
            this.mDrawOps.add((byte) 17);
        }
        return saveCount;
    }

    @Override
    public void restore() {
        if (this.mSaves.isEmpty()) {
            throw new IllegalStateException("Underflow in restore");
        } else {
            GLSurfaceCanvas.Save save = (GLSurfaceCanvas.Save) this.mSaves.poll();
            if (save.mClipRef != this.getSave().mClipRef) {
                this.restoreClipBatch(save.mClip);
            }
            if (save.mColorBuf != this.getSave().mColorBuf) {
                this.restoreLayer();
            }
            sSavePool.release(save);
        }
    }

    @Override
    public void restoreToCount(int saveCount) {
        if (saveCount < 1) {
            throw new IllegalArgumentException("Underflow in restoreToCount");
        } else {
            Deque<GLSurfaceCanvas.Save> stack = this.mSaves;
            GLSurfaceCanvas.Save lastPop = (GLSurfaceCanvas.Save) stack.pop();
            int topRef = lastPop.mClipRef;
            int topBuf = lastPop.mColorBuf;
            sSavePool.release(lastPop);
            while (stack.size() > saveCount) {
                lastPop = (GLSurfaceCanvas.Save) stack.pop();
                sSavePool.release(lastPop);
            }
            if (topRef != this.getSave().mClipRef) {
                this.restoreClipBatch(lastPop.mClip);
            }
            int bufDiff = topBuf - this.getSave().mColorBuf;
            while (bufDiff-- > 0) {
                this.restoreLayer();
            }
        }
    }

    private void restoreClipBatch(@NonNull Rect2i b) {
        if (b.isEmpty()) {
            this.mClipRefs.add(-this.getSave().mClipRef);
        } else {
            this.drawMatrix(RESET_MATRIX);
            this.putRectPMColor((float) b.mLeft, (float) b.mTop, (float) b.mRight, (float) b.mBottom, null);
            this.mClipRefs.add(this.getSave().mClipRef);
        }
        this.mDrawOps.add((byte) 14);
    }

    private void restoreLayer() {
        this.drawMatrix(RESET_MATRIX);
        this.mDrawOps.add((byte) 18);
        this.drawMatrix();
    }

    @Override
    public boolean clipRect(float left, float top, float right, float bottom) {
        GLSurfaceCanvas.Save save = this.getSave();
        if (right <= left || bottom <= top) {
            return !save.mClip.isEmpty();
        } else if (save.mClip.isEmpty()) {
            return false;
        } else {
            Rect2f temp = this.mTmpRectF;
            temp.set(left, top, right, bottom);
            temp.inset(-1.0F, -1.0F);
            this.getMatrix().mapRect(temp);
            Rect2i test = this.mTmpRectI;
            temp.roundOut(test);
            if (test.contains(save.mClip)) {
                return true;
            } else {
                boolean intersects = save.mClip.intersect(test);
                save.mClipRef++;
                if (intersects) {
                    this.drawMatrix();
                    this.putRectPMColor(left - 1.0F, top - 1.0F, right + 1.0F, bottom + 1.0F, null);
                    this.mClipRefs.add(save.mClipRef);
                } else {
                    this.mClipRefs.add(-save.mClipRef);
                    save.mClip.setEmpty();
                }
                this.mDrawOps.add((byte) 13);
                return intersects;
            }
        }
    }

    @Override
    public boolean quickReject(float left, float top, float right, float bottom) {
        if (!(right <= left) && !(bottom <= top)) {
            Rect2i clip = this.getSave().mClip;
            if (clip.isEmpty()) {
                return true;
            } else {
                Rect2f temp = this.mTmpRectF;
                temp.set(left, top, right, bottom);
                this.getMatrix().mapRect(temp);
                Rect2i test = this.mTmpRectI;
                temp.roundOut(test);
                return !Rect2i.intersects(clip, test);
            }
        } else {
            return true;
        }
    }

    private float[] load_premul_filter(@NonNull Paint paint) {
        float[] col = this.mTmpColor;
        float alpha = col[3] = paint.a();
        col[0] = paint.r() * alpha;
        col[1] = paint.g() * alpha;
        col[2] = paint.b() * alpha;
        if (paint.getColorFilter() != null) {
            paint.getColorFilter().filterColor4f(col, col);
        }
        return col;
    }

    private void putRectColor(float left, float top, float right, float bottom, @NonNull Paint paint) {
        this.putRectPMColor(left, top, right, bottom, this.load_premul_filter(paint));
    }

    private void putRectColorGrad(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL) {
        ByteBuffer buffer = this.checkColorMeshStagingBuffer(48);
        float alpha = (float) (colorLL >>> 24);
        float red = (float) (colorLL >> 16 & 0xFF) / 255.0F;
        float green = (float) (colorLL >> 8 & 0xFF) / 255.0F;
        float blue = (float) (colorLL & 0xFF) / 255.0F;
        byte r = (byte) ((int) (red * alpha + 0.5F));
        byte g = (byte) ((int) (green * alpha + 0.5F));
        byte b = (byte) ((int) (blue * alpha + 0.5F));
        byte a = (byte) ((int) (alpha + 0.5F));
        buffer.putFloat(left).putFloat(bottom).put(r).put(g).put(b).put(a);
        alpha = (float) (colorLR >>> 24);
        red = (float) (colorLR >> 16 & 0xFF) / 255.0F;
        green = (float) (colorLR >> 8 & 0xFF) / 255.0F;
        blue = (float) (colorLR & 0xFF) / 255.0F;
        r = (byte) ((int) (red * alpha + 0.5F));
        g = (byte) ((int) (green * alpha + 0.5F));
        b = (byte) ((int) (blue * alpha + 0.5F));
        a = (byte) ((int) (alpha + 0.5F));
        buffer.putFloat(right).putFloat(bottom).put(r).put(g).put(b).put(a);
        alpha = (float) (colorUL >>> 24);
        red = (float) (colorUL >> 16 & 0xFF) / 255.0F;
        green = (float) (colorUL >> 8 & 0xFF) / 255.0F;
        blue = (float) (colorUL & 0xFF) / 255.0F;
        r = (byte) ((int) (red * alpha + 0.5F));
        g = (byte) ((int) (green * alpha + 0.5F));
        b = (byte) ((int) (blue * alpha + 0.5F));
        a = (byte) ((int) (alpha + 0.5F));
        buffer.putFloat(left).putFloat(top).put(r).put(g).put(b).put(a);
        alpha = (float) (colorUR >>> 24);
        red = (float) (colorUR >> 16 & 0xFF) / 255.0F;
        green = (float) (colorUR >> 8 & 0xFF) / 255.0F;
        blue = (float) (colorUR & 0xFF) / 255.0F;
        r = (byte) ((int) (red * alpha + 0.5F));
        g = (byte) ((int) (green * alpha + 0.5F));
        b = (byte) ((int) (blue * alpha + 0.5F));
        a = (byte) ((int) (alpha + 0.5F));
        buffer.putFloat(right).putFloat(top).put(r).put(g).put(b).put(a);
    }

    private void putRectPMColor(float left, float top, float right, float bottom, float[] color) {
        ByteBuffer buffer = this.checkColorMeshStagingBuffer(48);
        byte r;
        byte g;
        byte b;
        byte a;
        if (color != null) {
            r = (byte) ((int) (color[0] * 255.0F + 0.5F));
            g = (byte) ((int) (color[1] * 255.0F + 0.5F));
            b = (byte) ((int) (color[2] * 255.0F + 0.5F));
            a = (byte) ((int) (color[3] * 255.0F + 0.5F));
        } else {
            r = -1;
            g = -1;
            b = -1;
            a = -1;
        }
        buffer.putFloat(left).putFloat(bottom).put(r).put(g).put(b).put(a);
        buffer.putFloat(right).putFloat(bottom).put(r).put(g).put(b).put(a);
        buffer.putFloat(left).putFloat(top).put(r).put(g).put(b).put(a);
        buffer.putFloat(right).putFloat(top).put(r).put(g).put(b).put(a);
    }

    private void putRectColorUV(float left, float top, float right, float bottom, @Nullable Paint paint, float u1, float v1, float u2, float v2) {
        ByteBuffer buffer = this.checkTextureMeshStagingBuffer();
        if (paint == null) {
            this.putRectColorUV(buffer, left, top, right, bottom, 1.0F, 1.0F, 1.0F, 1.0F, u1, v1, u2, v2);
        } else {
            this.putRectColorUV(buffer, left, top, right, bottom, paint.r(), paint.g(), paint.b(), paint.a(), u1, v1, u2, v2);
        }
    }

    private void putRectColorUV(@NonNull ByteBuffer buffer, float left, float top, float right, float bottom, float red, float green, float blue, float alpha, float u1, float v1, float u2, float v2) {
        float factor = alpha * 255.0F;
        byte r = (byte) ((int) (red * factor + 0.5F));
        byte g = (byte) ((int) (green * factor + 0.5F));
        byte b = (byte) ((int) (blue * factor + 0.5F));
        byte a = (byte) ((int) (factor + 0.5F));
        buffer.putFloat(left).putFloat(bottom).put(r).put(g).put(b).put(a).putFloat(u1).putFloat(v2);
        buffer.putFloat(right).putFloat(bottom).put(r).put(g).put(b).put(a).putFloat(u2).putFloat(v2);
        buffer.putFloat(left).putFloat(top).put(r).put(g).put(b).put(a).putFloat(u1).putFloat(v1);
        buffer.putFloat(right).putFloat(top).put(r).put(g).put(b).put(a).putFloat(u2).putFloat(v1);
    }

    @RenderThread
    private void putGlyph(@NonNull BakedGlyph glyph, float left, float top) {
        ByteBuffer buffer = this.checkGlyphStagingBuffer();
        left += (float) glyph.x;
        top += (float) glyph.y;
        float right = left + (float) glyph.width;
        float bottom = top + (float) glyph.height;
        buffer.putFloat(left).putFloat(bottom).putFloat(glyph.u1).putFloat(glyph.v2);
        buffer.putFloat(right).putFloat(bottom).putFloat(glyph.u2).putFloat(glyph.v2);
        buffer.putFloat(left).putFloat(top).putFloat(glyph.u1).putFloat(glyph.v1);
        buffer.putFloat(right).putFloat(top).putFloat(glyph.u2).putFloat(glyph.v1);
    }

    @RenderThread
    private void putGlyphScaled(@NonNull BakedGlyph glyph, float left, float top, float scale) {
        ByteBuffer buffer = this.checkGlyphStagingBuffer();
        left += (float) glyph.x * scale;
        top += (float) glyph.y * scale;
        float right = left + (float) glyph.width * scale;
        float bottom = top + (float) glyph.height * scale;
        buffer.putFloat(left).putFloat(bottom).putFloat(glyph.u1).putFloat(glyph.v2);
        buffer.putFloat(right).putFloat(bottom).putFloat(glyph.u2).putFloat(glyph.v2);
        buffer.putFloat(left).putFloat(top).putFloat(glyph.u1).putFloat(glyph.v1);
        buffer.putFloat(right).putFloat(top).putFloat(glyph.u2).putFloat(glyph.v1);
    }

    private void drawSmooth(float smooth) {
        if (smooth != this.mLastSmooth) {
            this.mLastSmooth = smooth;
            this.checkUniformStagingBuffer().putFloat(smooth);
            this.mDrawOps.add((byte) 16);
        }
    }

    @Override
    public void drawMesh(@NonNull Canvas.VertexMode mode, @NonNull FloatBuffer pos, @Nullable IntBuffer color, @Nullable FloatBuffer tex, @Nullable ShortBuffer indices, @Nullable Blender blender, @NonNull Paint paint) {
        int numVertices = pos.remaining() / 2;
        if (numVertices > 65535) {
            throw new IllegalArgumentException("Number of vertices is too big: " + numVertices);
        } else if (color != null && color.remaining() < numVertices) {
            throw new BufferUnderflowException();
        } else if (tex != null && tex.remaining() < numVertices * 2) {
            throw new BufferUnderflowException();
        } else if (tex == null && indices == null && blender == null) {
            int prim;
            switch(mode) {
                case POINTS:
                    if (numVertices < 1) {
                        return;
                    }
                    prim = 0;
                    break;
                case LINES:
                case LINE_STRIP:
                    if (numVertices < 2) {
                        return;
                    }
                    if (mode == Canvas.VertexMode.LINES) {
                        numVertices -= numVertices % 2;
                    }
                    prim = mode == Canvas.VertexMode.LINES ? 1 : 3;
                    break;
                default:
                    if (numVertices < 3) {
                        return;
                    }
                    if (mode == Canvas.VertexMode.TRIANGLES) {
                        numVertices -= numVertices % 3;
                    }
                    prim = mode == Canvas.VertexMode.TRIANGLES ? 4 : 5;
            }
            this.drawMatrix();
            this.mDrawOps.add((byte) 0);
            this.mDrawPrims.add(numVertices | prim << 16);
            ByteBuffer buffer = this.checkColorMeshStagingBuffer(12 * numVertices);
            if (color != null) {
                int pb = pos.position();
                int cb = color.position();
                for (int i = 0; i < numVertices; i++) {
                    int col = color.get(cb++);
                    byte a = (byte) (col >>> 24);
                    float factor = (float) (a & 255) / 255.0F;
                    byte r = (byte) ((int) ((float) (col >> 16 & 0xFF) * factor + 0.5F));
                    byte g = (byte) ((int) ((float) (col >> 8 & 0xFF) * factor + 0.5F));
                    byte b = (byte) ((int) ((float) (col & 0xFF) * factor + 0.5F));
                    buffer.putFloat(pos.get(pb++)).putFloat(pos.get(pb++)).put(r).put(g).put(b).put(a);
                }
            } else {
                float factor = paint.a();
                byte a = (byte) ((int) (factor * 255.0F + 0.5F));
                factor *= 255.0F;
                byte r = (byte) ((int) (paint.r() * factor + 0.5F));
                byte g = (byte) ((int) (paint.g() * factor + 0.5F));
                byte b = (byte) ((int) (paint.b() * factor + 0.5F));
                int pb = pos.position();
                for (int i = 0; i < numVertices; i++) {
                    buffer.putFloat(pos.get(pb++)).putFloat(pos.get(pb++)).put(r).put(g).put(b).put(a);
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void drawArc(float cx, float cy, float radius, float startAngle, float sweepAngle, @NonNull Paint paint) {
        if (!MathUtil.isApproxZero(sweepAngle) && !(radius < 1.0E-4F)) {
            if (sweepAngle >= 360.0F) {
                this.drawCircle(cx, cy, radius, paint);
            } else {
                sweepAngle %= 360.0F;
                float middleAngle = startAngle % 360.0F + sweepAngle * 0.5F;
                if (paint.getStyle() == 0) {
                    this.drawArcFill(cx, cy, radius, middleAngle, sweepAngle, paint);
                } else {
                    this.drawArcStroke(cx, cy, radius, middleAngle, sweepAngle, paint);
                }
            }
        }
    }

    private void drawArcFill(float cx, float cy, float radius, float middleAngle, float sweepAngle, @NonNull Paint paint) {
        if (!this.quickReject(cx - radius, cy - radius, cx + radius, cy + radius)) {
            this.drawMatrix();
            this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
            this.putRectColor(cx - radius, cy - radius, cx + radius, cy + radius, paint);
            this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(middleAngle).putFloat(sweepAngle).putFloat(radius);
            this.mDrawOps.add((byte) 8);
        }
    }

    private void drawArcStroke(float cx, float cy, float radius, float middleAngle, float sweepAngle, @NonNull Paint paint) {
        float strokeRadius = Math.min(radius, paint.getStrokeWidth() * 0.5F);
        if (!(strokeRadius < 1.0E-4F)) {
            float maxRadius = radius + strokeRadius;
            if (!this.quickReject(cx - maxRadius, cy - maxRadius, cx + maxRadius, cy + maxRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                this.putRectColor(cx - maxRadius, cy - maxRadius, cx + maxRadius, cy + maxRadius, paint);
                this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(middleAngle).putFloat(sweepAngle).putFloat(radius).putFloat(strokeRadius);
                this.mDrawOps.add((byte) 9);
            }
        }
    }

    @Override
    public void drawPie(float cx, float cy, float radius, float startAngle, float sweepAngle, @NonNull Paint paint) {
        if (!MathUtil.isApproxZero(sweepAngle) && !(radius < 1.0E-4F)) {
            if (sweepAngle >= 360.0F) {
                this.drawCircle(cx, cy, radius, paint);
            } else {
                sweepAngle %= 360.0F;
                float middleAngle = startAngle % 360.0F + sweepAngle * 0.5F;
                if (paint.getStyle() == 0) {
                    this.drawPieFill(cx, cy, radius, middleAngle, sweepAngle, paint);
                } else {
                    this.drawPieStroke(cx, cy, radius, middleAngle, sweepAngle, paint);
                }
            }
        }
    }

    private void drawPieFill(float cx, float cy, float radius, float middleAngle, float sweepAngle, @NonNull Paint paint) {
        if (!this.quickReject(cx - radius, cy - radius, cx + radius, cy + radius)) {
            this.drawMatrix();
            this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
            this.putRectColor(cx - radius, cy - radius, cx + radius, cy + radius, paint);
            this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(middleAngle).putFloat(sweepAngle).putFloat(radius);
            this.mDrawOps.add((byte) 21);
        }
    }

    private void drawPieStroke(float cx, float cy, float radius, float middleAngle, float sweepAngle, @NonNull Paint paint) {
        float strokeRadius = Math.min(radius, paint.getStrokeWidth() * 0.5F);
        if (!(strokeRadius < 1.0E-4F)) {
            float maxRadius = radius + strokeRadius;
            if (!this.quickReject(cx - maxRadius, cy - maxRadius, cx + maxRadius, cy + maxRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                this.putRectColor(cx - maxRadius, cy - maxRadius, cx + maxRadius, cy + maxRadius, paint);
                this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(middleAngle).putFloat(sweepAngle).putFloat(radius).putFloat(strokeRadius);
                this.mDrawOps.add((byte) 22);
            }
        }
    }

    @Override
    public void drawBezier(float x0, float y0, float x1, float y1, float x2, float y2, @NonNull Paint paint) {
        float strokeRadius = paint.getStrokeWidth() * 0.5F;
        if (!(strokeRadius < 1.0E-4F)) {
            float left = Math.min(Math.min(x0, x1), x2) - strokeRadius;
            float top = Math.min(Math.min(y0, y1), y2) - strokeRadius;
            float right = Math.max(Math.max(x0, x1), x2) + strokeRadius;
            float bottom = Math.max(Math.max(y0, y1), y2) + strokeRadius;
            if (!this.quickReject(left, top, right, bottom)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                this.putRectColor(left, top, right, bottom, paint);
                this.checkUniformStagingBuffer().putFloat(x0).putFloat(y0).putFloat(x1).putFloat(y1).putFloat(x2).putFloat(y2).putFloat(strokeRadius);
                this.mDrawOps.add((byte) 10);
            }
        }
    }

    @Override
    public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint) {
        if (!(radius < 1.0E-4F)) {
            if (paint.getStyle() == 0) {
                this.drawCircleFill(cx, cy, radius, paint);
            } else {
                this.drawCircleStroke(cx, cy, radius, paint);
            }
        }
    }

    private void drawCircleFill(float cx, float cy, float radius, @NonNull Paint paint) {
        if (!this.quickReject(cx - radius, cy - radius, cx + radius, cy + radius)) {
            this.drawMatrix();
            this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
            this.putRectColor(cx - radius - 1.0F, cy - radius - 1.0F, cx + radius + 1.0F, cy + radius + 1.0F, paint);
            this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(radius);
            this.mDrawOps.add((byte) 6);
        }
    }

    private void drawCircleStroke(float cx, float cy, float radius, @NonNull Paint paint) {
        float strokeRadius = Math.min(radius, paint.getStrokeWidth() * 0.5F);
        if (!(strokeRadius < 1.0E-4F)) {
            float maxRadius = radius + strokeRadius;
            if (!this.quickReject(cx - maxRadius, cy - maxRadius, cx + maxRadius, cy + maxRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                this.putRectColor(cx - maxRadius - 1.0F, cy - maxRadius - 1.0F, cx + maxRadius + 1.0F, cy + maxRadius + 1.0F, paint);
                this.checkUniformStagingBuffer().putFloat(cx).putFloat(cy).putFloat(radius - strokeRadius).putFloat(maxRadius);
                this.mDrawOps.add((byte) 7);
            }
        }
    }

    @Override
    public void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint) {
        if (!this.quickReject(left, top, right, bottom)) {
            if (paint.getStyle() == 0) {
                this.drawMatrix();
                this.putRectColor(left, top, right, bottom, paint);
                this.mDrawOps.add((byte) 1);
            } else {
                this.drawRectStroke(left, top, right, bottom, 0, 0, 0, 0, false, paint);
            }
        }
    }

    @Override
    public void drawRectGradient(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL, Paint paint) {
        if (!this.quickReject(left, top, right, bottom)) {
            if (paint.getStyle() == 0) {
                this.drawMatrix();
                this.putRectColorGrad(left, top, right, bottom, colorUL, colorUR, colorLR, colorLL);
                this.mDrawOps.add((byte) 1);
            } else {
                this.drawRectStroke(left, top, right, bottom, colorUL, colorUR, colorLR, colorLL, true, paint);
            }
        }
    }

    private void drawRectStroke(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL, boolean useGrad, @NonNull Paint paint) {
        float strokeRadius = paint.getStrokeWidth() * 0.5F;
        if (!(strokeRadius < 1.0E-4F)) {
            if (!this.quickReject(left - strokeRadius, top - strokeRadius, right + strokeRadius, bottom + strokeRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                if (useGrad) {
                    this.putRectColorGrad(left - strokeRadius - 1.0F, top - strokeRadius - 1.0F, right + strokeRadius + 1.0F, bottom + strokeRadius + 1.0F, colorUL, colorUR, colorLR, colorLL);
                } else {
                    this.putRectColor(left - strokeRadius - 1.0F, top - strokeRadius - 1.0F, right + strokeRadius + 1.0F, bottom + strokeRadius + 1.0F, paint);
                }
                ByteBuffer buffer = this.checkUniformStagingBuffer();
                buffer.putFloat(left);
                buffer.putFloat(top);
                buffer.putFloat(right);
                buffer.putFloat(bottom);
                buffer.putFloat(0.0F).putFloat(strokeRadius);
                this.mDrawOps.add((byte) (paint.getStrokeCap() == 4 ? 26 : 25));
            }
        }
    }

    public void drawGlowWave(float left, float top, float right, float bottom) {
        if (!this.quickReject(left, top, right, bottom)) {
            this.save();
            float aspect = (right - left) / (bottom - top);
            this.scale((right - left) * 0.5F, (right - left) * 0.5F, -1.0F - left / ((right - left) * 0.5F), (-1.0F - top / ((bottom - top) * 0.5F)) / aspect);
            this.drawMatrix();
            this.putRectPMColor(-1.0F, -1.0F / aspect, 1.0F, 1.0F / aspect, null);
            ByteBuffer buffer = this.checkUniformStagingBuffer();
            buffer.putFloat((float) GLFW.glfwGetTime());
            this.restore();
            this.mDrawOps.add((byte) 20);
        }
    }

    @Override
    public void drawImage(@NonNull Image image, float left, float top, @Nullable Paint paint) {
        SurfaceView view = image.asTextureView();
        if (view != null) {
            float right = left + (float) view.getWidth();
            float bottom = top + (float) view.getHeight();
            if (!this.quickReject(left, top, right, bottom)) {
                this.drawMatrix();
                this.putRectColorUV(left, top, right, bottom, paint, 0.0F, 0.0F, 1.0F, 1.0F);
                view.refSurface();
                this.mTextures.add(view);
                this.mDrawOps.add((byte) 2);
            }
        }
    }

    public void drawLayer(@NonNull GLTexture texture, float w, float h, float alpha, boolean flipY) {
        this.drawMatrix();
        this.putRectColorUV(this.checkTextureMeshStagingBuffer(), 0.0F, 0.0F, w, h, 1.0F, 1.0F, 1.0F, alpha, 0.0F, flipY ? h / (float) texture.getHeight() : 0.0F, w / (float) texture.getWidth(), flipY ? 0.0F : h / (float) texture.getHeight());
        texture.ref();
        this.mTextures.add(new SurfaceView(new TextureProxy(texture, 0)));
        this.mDrawOps.add((byte) 12);
    }

    @Override
    public void drawImage(@NonNull Image image, float srcLeft, float srcTop, float srcRight, float srcBottom, float dstLeft, float dstTop, float dstRight, float dstBottom, @Nullable Paint paint) {
        if (!this.quickReject(dstLeft, dstTop, dstRight, dstBottom)) {
            SurfaceView view = image.asTextureView();
            if (view != null) {
                srcLeft = Math.max(0.0F, srcLeft);
                srcTop = Math.max(0.0F, srcTop);
                int w = view.getWidth();
                int h = view.getHeight();
                srcRight = Math.min(srcRight, (float) w);
                srcBottom = Math.min(srcBottom, (float) h);
                if (!(srcRight <= srcLeft) && !(srcBottom <= srcTop)) {
                    this.drawMatrix();
                    this.putRectColorUV(dstLeft, dstTop, dstRight, dstBottom, paint, srcLeft / (float) w, srcTop / (float) h, srcRight / (float) w, srcBottom / (float) h);
                    view.refSurface();
                    this.mTextures.add(view);
                    this.mDrawOps.add((byte) 2);
                }
            }
        }
    }

    @Override
    public void drawLine(float startX, float startY, float stopX, float stopY, float thickness, @NonNull Paint paint) {
        float t = thickness * 0.5F;
        if (t <= 0.0F) {
            this.drawMatrix();
            this.mDrawOps.add((byte) 0);
            this.mDrawPrims.add(65538);
            ByteBuffer buffer = this.checkColorMeshStagingBuffer(24);
            float factor = paint.a();
            byte a = (byte) ((int) (factor * 255.0F + 0.5F));
            factor *= 255.0F;
            byte r = (byte) ((int) (paint.r() * factor + 0.5F));
            byte g = (byte) ((int) (paint.g() * factor + 0.5F));
            byte b = (byte) ((int) (paint.b() * factor + 0.5F));
            buffer.putFloat(startX).putFloat(startY).put(r).put(g).put(b).put(a);
            buffer.putFloat(stopX).putFloat(stopY).put(r).put(g).put(b).put(a);
        } else {
            float left = Math.min(startX, stopX) - t;
            float top = Math.min(startY, stopY) - t;
            float right = Math.max(startX, stopX) + t;
            float bottom = Math.max(startY, stopY) + t;
            if (paint.getStyle() == 0) {
                this.drawLineFill(startX, startY, stopX, stopY, t, paint, left, top, right, bottom);
            } else {
                this.drawLineStroke(startX, startY, stopX, stopY, t, paint, left, top, right, bottom);
            }
        }
    }

    private void drawLineFill(float startX, float startY, float stopX, float stopY, float radius, @NonNull Paint paint, float left, float top, float right, float bottom) {
        if (!this.quickReject(left, top, right, bottom)) {
            this.drawMatrix();
            this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
            this.putRectColor(left - 1.0F, top - 1.0F, right + 1.0F, bottom + 1.0F, paint);
            ByteBuffer buffer = this.checkUniformStagingBuffer();
            buffer.putFloat(startX).putFloat(startY).putFloat(stopX).putFloat(stopY);
            buffer.putFloat(radius);
            this.mDrawOps.add((byte) 23);
        }
    }

    private void drawLineStroke(float startX, float startY, float stopX, float stopY, float radius, @NonNull Paint paint, float left, float top, float right, float bottom) {
        float strokeRadius = Math.min(radius, paint.getStrokeWidth() * 0.5F);
        if (!(strokeRadius < 1.0E-4F)) {
            if (!this.quickReject(left - strokeRadius, top - strokeRadius, right + strokeRadius, bottom + strokeRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                this.putRectColor(left - strokeRadius - 1.0F, top - strokeRadius - 1.0F, right + strokeRadius + 1.0F, bottom + strokeRadius + 1.0F, paint);
                ByteBuffer buffer = this.checkUniformStagingBuffer();
                buffer.putFloat(startX).putFloat(startY).putFloat(stopX).putFloat(stopY);
                buffer.putFloat(radius).putFloat(strokeRadius);
                this.mDrawOps.add((byte) 24);
            }
        }
    }

    public void drawRoundLine(float startX, float startY, float stopX, float stopY, @NonNull Paint paint) {
        float t = paint.getStrokeWidth() * 0.5F;
        if (!(t < 1.0E-4F)) {
            if (MathUtil.isApproxEqual(startX, stopX)) {
                if (MathUtil.isApproxEqual(startY, stopY)) {
                    this.drawCircleFill(startX, startY, t, paint);
                } else {
                    float top = Math.min(startY, stopY);
                    float bottom = Math.max(startY, stopY);
                    this.drawRoundRectFill(startX - t, top - t, startX + t, bottom + t, 0, 0, 0, 0, false, t, 0, paint);
                }
            } else if (MathUtil.isApproxEqual(startY, stopY)) {
                float left = Math.min(startX, stopX);
                float right = Math.max(startX, stopX);
                this.drawRoundRectFill(left - t, startY - t, right + t, startY + t, 0, 0, 0, 0, false, t, 0, paint);
            } else {
                float cx = (stopX + startX) * 0.5F;
                float cy = (stopY + startY) * 0.5F;
                float ang = MathUtil.atan2(stopY - startY, stopX - startX);
                this.save();
                Matrix4 mat = this.getMatrix();
                mat.preTranslate(cx, cy, 0.0F);
                mat.preRotateZ((double) ang);
                mat.preTranslate(-cx, -cy, 0.0F);
                float sin = MathUtil.sin(-ang);
                float cos = MathUtil.cos(-ang);
                float left = (startX - cx) * cos - (startY - cy) * sin + cx;
                float right = (stopX - cx) * cos - (stopY - cy) * sin + cx;
                this.drawRoundRectFill(left - t, cy - t, right + t, cy + t, 0, 0, 0, 0, false, t, 0, paint);
                this.restore();
            }
        }
    }

    @Override
    public void drawRoundRect(float left, float top, float right, float bottom, float radius, int sides, @NonNull Paint paint) {
        radius = Math.min(radius, Math.min(right - left, bottom - top) * 0.5F);
        if (radius < 0.0F) {
            radius = 0.0F;
        }
        if (paint.getStyle() == 0) {
            this.drawRoundRectFill(left, top, right, bottom, 0, 0, 0, 0, false, radius, sides, paint);
        } else {
            this.drawRoundRectStroke(left, top, right, bottom, 0, 0, 0, 0, false, radius, sides, paint);
        }
    }

    @Override
    public void drawRoundRectGradient(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL, float radius, Paint paint) {
        radius = Math.min(radius, Math.min(right - left, bottom - top) * 0.5F);
        if (radius < 0.0F) {
            radius = 0.0F;
        }
        if (paint.getStyle() == 0) {
            this.drawRoundRectFill(left, top, right, bottom, colorUL, colorUR, colorLR, colorLL, true, radius, 0, paint);
        } else {
            this.drawRoundRectStroke(left, top, right, bottom, colorUL, colorUR, colorLR, colorLL, true, radius, 0, paint);
        }
    }

    private void drawRoundRectFill(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL, boolean useGrad, float radius, int sides, @NonNull Paint paint) {
        if (!this.quickReject(left, top, right, bottom)) {
            this.drawMatrix();
            this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
            if (useGrad) {
                this.putRectColorGrad(left - 1.0F, top - 1.0F, right + 1.0F, bottom + 1.0F, colorUL, colorUR, colorLR, colorLL);
            } else {
                this.putRectColor(left - 1.0F, top - 1.0F, right + 1.0F, bottom + 1.0F, paint);
            }
            ByteBuffer buffer = this.checkUniformStagingBuffer();
            if ((sides & 5) == 5) {
                buffer.putFloat(left);
            } else {
                buffer.putFloat(left + radius);
            }
            if ((sides & 80) == 80) {
                buffer.putFloat(top);
            } else {
                buffer.putFloat(top + radius);
            }
            if ((sides & 3) == 3) {
                buffer.putFloat(right);
            } else {
                buffer.putFloat(right - radius);
            }
            if ((sides & 48) == 48) {
                buffer.putFloat(bottom);
            } else {
                buffer.putFloat(bottom - radius);
            }
            buffer.putFloat(radius);
            this.mDrawOps.add((byte) 3);
        }
    }

    private void drawRoundRectStroke(float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL, boolean useGrad, float radius, int sides, @NonNull Paint paint) {
        float strokeRadius = Math.min(radius, paint.getStrokeWidth() * 0.5F);
        if (!(strokeRadius < 1.0E-4F)) {
            if (!this.quickReject(left - strokeRadius, top - strokeRadius, right + strokeRadius, bottom + strokeRadius)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(strokeRadius, paint.getSmoothWidth() / 2.0F));
                if (useGrad) {
                    this.putRectColorGrad(left - strokeRadius - 1.0F, top - strokeRadius - 1.0F, right + strokeRadius + 1.0F, bottom + strokeRadius + 1.0F, colorUL, colorUR, colorLR, colorLL);
                } else {
                    this.putRectColor(left - strokeRadius - 1.0F, top - strokeRadius - 1.0F, right + strokeRadius + 1.0F, bottom + strokeRadius + 1.0F, paint);
                }
                ByteBuffer buffer = this.checkUniformStagingBuffer();
                if ((sides & 5) == 5) {
                    buffer.putFloat(left);
                } else {
                    buffer.putFloat(left + radius);
                }
                if ((sides & 80) == 80) {
                    buffer.putFloat(top);
                } else {
                    buffer.putFloat(top + radius);
                }
                if ((sides & 3) == 3) {
                    buffer.putFloat(right);
                } else {
                    buffer.putFloat(right - radius);
                }
                if ((sides & 48) == 48) {
                    buffer.putFloat(bottom);
                } else {
                    buffer.putFloat(bottom - radius);
                }
                buffer.putFloat(radius).putFloat(strokeRadius);
                this.mDrawOps.add((byte) 5);
            }
        }
    }

    @Override
    public void drawRoundImage(@NonNull Image image, float left, float top, float radius, @NonNull Paint paint) {
        if (radius < 0.0F) {
            radius = 0.0F;
        }
        SurfaceView view = image.asTextureView();
        if (view != null) {
            float right = left + (float) view.getWidth();
            float bottom = top + (float) view.getHeight();
            if (!this.quickReject(left, top, right, bottom)) {
                this.drawMatrix();
                this.drawSmooth(Math.min(radius, paint.getSmoothWidth() / 2.0F));
                this.putRectColorUV(left, top, right, bottom, paint, 0.0F, 0.0F, 1.0F, 1.0F);
                this.checkUniformStagingBuffer().putFloat(left + radius - 1.0F).putFloat(top + radius - 1.0F).putFloat(right - radius + 1.0F).putFloat(bottom - radius + 1.0F).putFloat(radius);
                view.refSurface();
                this.mTextures.add(view);
                this.mDrawOps.add((byte) 4);
            }
        }
    }

    @Override
    public void drawGlyphs(@NonNull int[] glyphs, int glyphOffset, @NonNull float[] positions, int positionOffset, int glyphCount, @NonNull Font font, float x, float y, @NonNull Paint paint) {
        this.drawMatrix();
        GLSurfaceCanvas.DrawTextOp op = new GLSurfaceCanvas.DrawTextOp(glyphs, glyphOffset, positions, positionOffset, glyphCount, x, y, font, paint.getFontSize());
        this.mDrawTexts.add(op);
        float alpha = paint.a();
        ByteBuffer uniforms = this.checkUniformStagingBuffer();
        if (font instanceof EmojiFont) {
            uniforms.putFloat(alpha).putFloat(alpha).putFloat(alpha);
        } else {
            uniforms.putFloat(paint.r() * alpha).putFloat(paint.g() * alpha).putFloat(paint.b() * alpha);
        }
        uniforms.putFloat(alpha);
        this.mDrawOps.add((byte) 11);
    }

    @Override
    public void drawCustomDrawable(@NonNull CustomDrawable drawable, @Nullable Matrix4 matrix) {
        Matrix4 viewMatrix = this.getMatrix().clone();
        if (matrix != null) {
            viewMatrix.preConcat(matrix);
        }
        CustomDrawable.DrawHandler draw = drawable.snapDrawHandler(0, viewMatrix, this.getSave().mClip, this.mInfo);
        if (draw != null) {
            this.mCustoms.add(draw);
            this.mDrawOps.add((byte) 19);
        }
    }

    private static class DrawTextOp {

        private final int[] mGlyphs;

        private final int mGlyphOffset;

        private final float[] mPositions;

        private final int mPositionOffset;

        private final int mGlyphCount;

        private final float mOffsetX;

        private final float mOffsetY;

        private final Font mFont;

        private final int mFontSize;

        private final float mScaleFactor;

        private int mTexture;

        private int mVisibleGlyphCount;

        public DrawTextOp(int[] glyphs, int glyphOffset, float[] positions, int positionOffset, int glyphCount, float offsetX, float offsetY, Font font, int fontSize) {
            this.mGlyphs = glyphs;
            this.mGlyphOffset = glyphOffset;
            this.mPositions = positions;
            this.mPositionOffset = positionOffset;
            this.mGlyphCount = glyphCount;
            this.mOffsetX = offsetX;
            this.mOffsetY = offsetY;
            this.mFont = font;
            this.mFontSize = fontSize;
            if (font instanceof EmojiFont) {
                this.mScaleFactor = (float) fontSize / 64.0F;
            } else {
                this.mScaleFactor = 1.0F;
            }
        }

        private void writeMeshData(@NonNull GLSurfaceCanvas canvas) {
            GlyphManager glyphManager = GlyphManager.getInstance();
            int glyphOffset = this.mGlyphOffset;
            int positionOffset = this.mPositionOffset;
            int visibleGlyphCount = 0;
            for (int i = 0; i < this.mGlyphCount; i++) {
                BakedGlyph bakedGlyph = glyphManager.lookupGlyph(this.mFont, this.mFontSize, this.mGlyphs[glyphOffset++]);
                if (bakedGlyph != null) {
                    float x = this.mOffsetX + this.mPositions[positionOffset++];
                    float y = this.mOffsetY + this.mPositions[positionOffset++];
                    if (this.mScaleFactor != 1.0F) {
                        canvas.putGlyphScaled(bakedGlyph, x, y, this.mScaleFactor);
                    } else {
                        canvas.putGlyph(bakedGlyph, x, y);
                    }
                    visibleGlyphCount++;
                } else {
                    positionOffset += 2;
                }
            }
            this.mTexture = glyphManager.getCurrentTexture(this.mFont);
            this.mVisibleGlyphCount = visibleGlyphCount;
        }
    }

    static final class Save {

        final Rect2i mClip = new Rect2i();

        final Matrix4 mMatrix = Matrix4.identity();

        int mClipRef;

        int mColorBuf;

        void set(@Nonnull GLSurfaceCanvas.Save s) {
            this.mClip.set(s.mClip);
            this.mMatrix.set(s.mMatrix);
            this.mClipRef = s.mClipRef;
            this.mColorBuf = s.mColorBuf;
        }

        @Nonnull
        GLSurfaceCanvas.Save copy() {
            GLSurfaceCanvas.Save s = new GLSurfaceCanvas.Save();
            s.set(this);
            return s;
        }
    }

    public static class UniformBuffer implements AutoCloseable {

        private int mBuffer;

        public final int getBufferID() {
            if (this.mBuffer == 0) {
                this.mBuffer = GLCore.glGenBuffers();
            }
            return this.mBuffer;
        }

        public void bindBase(int target, int index) {
            GLCore.glBindBufferBase(target, index, this.getBufferID());
        }

        public void bindRange(int target, int index, long offset, long size) {
            GLCore.glBindBufferRange(target, index, this.getBufferID(), offset, size);
        }

        public void allocate(long size) {
            GLCore.glBindBuffer(35345, this.getBufferID());
            GLCore.nglBufferData(35345, size, 0L, 35048);
        }

        public void upload(long offset, long size, long data) {
            GLCore.glBindBuffer(35345, this.getBufferID());
            GLCore.nglBufferSubData(35345, offset, size, data);
        }

        public void close() {
            if (this.mBuffer != 0) {
                GLCore.glDeleteBuffers(this.mBuffer);
            }
            this.mBuffer = 0;
        }
    }
}