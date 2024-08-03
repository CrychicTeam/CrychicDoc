package de.keksuccino.fancymenu.util.resource.resources.texture.fma;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.MathUtils;
import de.keksuccino.fancymenu.util.file.FileUtils;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FmaDecoder implements Closeable {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().create();

    private static final File TEMP_DIR = FileUtils.createDirectory(new File(FancyMenu.TEMP_DATA_DIR, "/decoded_fma_images"));

    @Nullable
    protected ZipFile zipFile = null;

    @Nullable
    protected FmaDecoder.FmaMetadata metadata = null;

    @NotNull
    protected final List<AutoCloseable> closeables = new ArrayList();

    @NotNull
    protected final List<String> orderedFramePaths = new ArrayList();

    @NotNull
    protected final List<String> orderedIntroFramePaths = new ArrayList();

    protected final String identifier = ScreenCustomization.generateUniqueIdentifier();

    protected final File decodingTempDir = FileUtils.createDirectory(new File(TEMP_DIR, "/" + this.identifier));

    public void read(@NotNull InputStream in) throws IOException {
        if (this.zipFile != null) {
            throw new IllegalStateException("The decoder is already reading a file!");
        } else {
            try {
                this.zipFile = this.putCloseable(new ZipFile(this.putCloseable(new SeekableInMemoryByteChannel(in.readAllBytes()))));
                this.decodeImage();
                this.readMetadata();
                this.readFramePaths();
                this.readIntroFramePaths();
            } catch (Exception var3) {
                CloseableUtils.closeQuietly(in);
                CloseableUtils.closeQuietly(this);
                this.zipFile = null;
                this.metadata = null;
                throw new IOException(var3);
            }
            CloseableUtils.closeQuietly(in);
        }
    }

    public void read(@NotNull File fmaFile) throws IOException {
        if (this.zipFile != null) {
            throw new IllegalStateException("The decoder is already reading a file!");
        } else {
            try {
                this.zipFile = this.putCloseable(new ZipFile(fmaFile));
                this.decodeImage();
                this.readMetadata();
                this.readFramePaths();
                this.readIntroFramePaths();
            } catch (Exception var3) {
                CloseableUtils.closeQuietly(this);
                this.zipFile = null;
                this.metadata = null;
                throw new IOException(var3);
            }
        }
    }

    protected void decodeImage() throws IOException {
        Objects.requireNonNull(this.zipFile);
        try {
            new Expander().expand(this.zipFile, this.decodingTempDir);
        } catch (Exception var2) {
            throw new IOException(var2);
        }
    }

    protected void readFramePaths() throws IOException {
        Objects.requireNonNull(this.zipFile);
        this.orderedFramePaths.clear();
        try {
            File framesDir = new File(this.decodingTempDir, "/frames");
            if (!framesDir.isDirectory()) {
                throw new FileNotFoundException("No frames directory found in FMA file!");
            } else {
                File[] frames = (File[]) Objects.requireNonNull(framesDir.listFiles());
                for (File frame : frames) {
                    String name = frame.getAbsolutePath();
                    if (name.toLowerCase().endsWith(".png")) {
                        String withoutExtension = Files.getNameWithoutExtension(name);
                        if (MathUtils.isInteger(withoutExtension)) {
                            this.orderedFramePaths.add(name);
                        } else {
                            LOGGER.error("[FANCYMENU] Invalid PNG frame found in FMA file!", new IllegalStateException("Frame file name is not a valid number: " + frame.getName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Invalid frame found in FMA file!", new IllegalStateException("Frame file is not a valid PNG image: " + frame.getName()));
                    }
                }
                this.orderedFramePaths.sort((o1, o2) -> {
                    int i1 = Integer.parseInt(Files.getNameWithoutExtension(o1));
                    int i2 = Integer.parseInt(Files.getNameWithoutExtension(o2));
                    return Integer.compare(i1, i2);
                });
            }
        } catch (Exception var9) {
            throw new IOException(var9);
        }
    }

    protected void readIntroFramePaths() throws IOException {
        Objects.requireNonNull(this.zipFile);
        this.orderedIntroFramePaths.clear();
        try {
            File framesDir = new File(this.decodingTempDir, "/intro_frames");
            if (framesDir.isDirectory()) {
                File[] frames = (File[]) Objects.requireNonNull(framesDir.listFiles());
                for (File frame : frames) {
                    String name = frame.getAbsolutePath();
                    if (name.toLowerCase().endsWith(".png")) {
                        String withoutExtension = Files.getNameWithoutExtension(name);
                        if (MathUtils.isInteger(withoutExtension)) {
                            this.orderedIntroFramePaths.add(name);
                        } else {
                            LOGGER.error("[FANCYMENU] Invalid PNG intro frame found in FMA file!", new IllegalStateException("Frame file name is not a valid number: " + frame.getName()));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Invalid intro frame found in FMA file!", new IllegalStateException("Frame file is not a valid PNG image: " + frame.getName()));
                    }
                }
                this.orderedIntroFramePaths.sort((o1, o2) -> {
                    int i1 = Integer.parseInt(Files.getNameWithoutExtension(o1));
                    int i2 = Integer.parseInt(Files.getNameWithoutExtension(o2));
                    return Integer.compare(i1, i2);
                });
            }
        } catch (Exception var9) {
            throw new IOException(var9);
        }
    }

    protected void readMetadata() throws IOException {
        try {
            File metadata = new File(this.decodingTempDir, "/metadata.json");
            if (!metadata.isFile()) {
                throw new FileNotFoundException("No metadata.json found in FMA file! Unable to read metadata!");
            } else {
                List<String> metadataLines = FileUtils.readTextLinesFrom(metadata);
                StringBuilder metadataString = new StringBuilder();
                for (String line : metadataLines) {
                    metadataString.append(line).append("\n");
                }
                this.metadata = (FmaDecoder.FmaMetadata) Objects.requireNonNull((FmaDecoder.FmaMetadata) GSON.fromJson(metadataString.toString(), FmaDecoder.FmaMetadata.class), "Unable to parse metadata.json of FMA file!");
            }
        } catch (Exception var6) {
            throw new IOException(var6);
        }
    }

    public int getFrameCount() {
        return this.orderedFramePaths.size();
    }

    public int getIntroFrameCount() {
        return this.orderedIntroFramePaths.size();
    }

    public boolean hasIntroFrames() {
        return this.getIntroFrameCount() > 0;
    }

    @Nullable
    public FmaDecoder.FmaMetadata getMetadata() {
        return this.metadata;
    }

    @Nullable
    public InputStream getFrame(int index) throws IOException {
        Objects.requireNonNull(this.zipFile);
        if (this.orderedFramePaths.isEmpty()) {
            return null;
        } else if (this.getFrameCount() - 1 < index) {
            return null;
        } else {
            try {
                File frame = new File((String) this.orderedFramePaths.get(index));
                if (!frame.isFile()) {
                    throw new FileNotFoundException("Frame file of FMA not found: " + frame.getAbsolutePath());
                } else {
                    return org.apache.commons.io.FileUtils.openInputStream(frame);
                }
            } catch (Exception var3) {
                throw new IOException(var3);
            }
        }
    }

    @Nullable
    public InputStream getFirstFrame() throws IOException {
        return this.getFrame(0);
    }

    @Nullable
    public BufferedImage getFirstFrameAsBufferedImage() throws IOException {
        InputStream in = this.getFirstFrame();
        try {
            return in != null ? ImageIO.read(in) : null;
        } catch (Exception var3) {
            throw new IOException(var3);
        }
    }

    @Nullable
    public InputStream getIntroFrame(int index) throws IOException {
        Objects.requireNonNull(this.zipFile);
        if (this.orderedIntroFramePaths.isEmpty()) {
            return null;
        } else if (this.getIntroFrameCount() - 1 < index) {
            return null;
        } else {
            try {
                File frame = new File((String) this.orderedIntroFramePaths.get(index));
                if (!frame.isFile()) {
                    throw new FileNotFoundException("Intro frame file of FMA not found: " + frame.getAbsolutePath());
                } else {
                    return org.apache.commons.io.FileUtils.openInputStream(frame);
                }
            } catch (Exception var3) {
                throw new IOException(var3);
            }
        }
    }

    @Nullable
    public InputStream getBackgroundImage() throws IOException {
        Objects.requireNonNull(this.zipFile);
        File backgroundImage = new File(this.decodingTempDir, "/background.png");
        if (!backgroundImage.isFile()) {
            return null;
        } else {
            try {
                return org.apache.commons.io.FileUtils.openInputStream(backgroundImage);
            } catch (Exception var3) {
                throw new IOException(var3);
            }
        }
    }

    protected <T extends AutoCloseable> T putCloseable(@NotNull T closeable) {
        this.closeables.add(closeable);
        return closeable;
    }

    public void close() throws IOException {
        this.closeables.forEach(CloseableUtils::closeQuietly);
        this.closeables.clear();
        this.orderedFramePaths.clear();
        this.orderedIntroFramePaths.clear();
        org.apache.commons.io.FileUtils.deleteQuietly(this.decodingTempDir);
    }

    public static class FmaMetadata {

        protected int loop_count;

        protected long frame_time;

        protected long frame_time_intro;

        protected Map<Integer, Long> custom_frame_times;

        protected Map<Integer, Long> custom_frame_times_intro;

        public int getLoopCount() {
            return this.loop_count;
        }

        public long getFrameTime() {
            return this.frame_time;
        }

        public long getFrameTimeIntro() {
            return this.frame_time_intro;
        }

        @Nullable
        public Map<Integer, Long> getCustomFrameTimes() {
            return this.custom_frame_times;
        }

        @Nullable
        public Map<Integer, Long> getCustomFrameTimesIntro() {
            return this.custom_frame_times_intro;
        }

        public long getFrameTimeForFrame(int frame, boolean isIntroFrame) {
            if (isIntroFrame) {
                if (this.custom_frame_times_intro != null && this.custom_frame_times_intro.containsKey(frame)) {
                    return (Long) this.custom_frame_times_intro.get(frame);
                }
            } else if (this.custom_frame_times != null && this.custom_frame_times.containsKey(frame)) {
                return (Long) this.custom_frame_times.get(frame);
            }
            return isIntroFrame ? this.getFrameTimeIntro() : this.getFrameTime();
        }
    }
}