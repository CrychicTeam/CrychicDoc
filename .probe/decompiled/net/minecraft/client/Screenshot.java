package net.minecraft.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class Screenshot {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String SCREENSHOT_DIR = "screenshots";

    private int rowHeight;

    private final DataOutputStream outputStream;

    private final byte[] bytes;

    private final int width;

    private final int height;

    private File file;

    public static void grab(File file0, RenderTarget renderTarget1, Consumer<Component> consumerComponent2) {
        grab(file0, null, renderTarget1, consumerComponent2);
    }

    public static void grab(File file0, @Nullable String string1, RenderTarget renderTarget2, Consumer<Component> consumerComponent3) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> _grab(file0, string1, renderTarget2, consumerComponent3));
        } else {
            _grab(file0, string1, renderTarget2, consumerComponent3);
        }
    }

    private static void _grab(File file0, @Nullable String string1, RenderTarget renderTarget2, Consumer<Component> consumerComponent3) {
        NativeImage $$4 = takeScreenshot(renderTarget2);
        File $$5 = new File(file0, "screenshots");
        $$5.mkdir();
        File $$6;
        if (string1 == null) {
            $$6 = getFile($$5);
        } else {
            $$6 = new File($$5, string1);
        }
        Util.ioPool().execute(() -> {
            try {
                $$4.writeToFile($$6);
                Component $$3 = Component.literal($$6.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(p_168608_ -> p_168608_.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, $$6.getAbsolutePath())));
                consumerComponent3.accept(Component.translatable("screenshot.success", $$3));
            } catch (Exception var7) {
                LOGGER.warn("Couldn't save screenshot", var7);
                consumerComponent3.accept(Component.translatable("screenshot.failure", var7.getMessage()));
            } finally {
                $$4.close();
            }
        });
    }

    public static NativeImage takeScreenshot(RenderTarget renderTarget0) {
        int $$1 = renderTarget0.width;
        int $$2 = renderTarget0.height;
        NativeImage $$3 = new NativeImage($$1, $$2, false);
        RenderSystem.bindTexture(renderTarget0.getColorTextureId());
        $$3.downloadTexture(0, true);
        $$3.flipY();
        return $$3;
    }

    private static File getFile(File file0) {
        String $$1 = Util.getFilenameFormattedDateTime();
        int $$2 = 1;
        while (true) {
            File $$3 = new File(file0, $$1 + ($$2 == 1 ? "" : "_" + $$2) + ".png");
            if (!$$3.exists()) {
                return $$3;
            }
            $$2++;
        }
    }

    public Screenshot(File file0, int int1, int int2, int int3) throws IOException {
        this.width = int1;
        this.height = int2;
        this.rowHeight = int3;
        File $$4 = new File(file0, "screenshots");
        $$4.mkdir();
        String $$5 = "huge_" + Util.getFilenameFormattedDateTime();
        int $$6 = 1;
        while ((this.file = new File($$4, $$5 + ($$6 == 1 ? "" : "_" + $$6) + ".tga")).exists()) {
            $$6++;
        }
        byte[] $$7 = new byte[18];
        $$7[2] = 2;
        $$7[12] = (byte) (int1 % 256);
        $$7[13] = (byte) (int1 / 256);
        $$7[14] = (byte) (int2 % 256);
        $$7[15] = (byte) (int2 / 256);
        $$7[16] = 24;
        this.bytes = new byte[int1 * int3 * 3];
        this.outputStream = new DataOutputStream(new FileOutputStream(this.file));
        this.outputStream.write($$7);
    }

    public void addRegion(ByteBuffer byteBuffer0, int int1, int int2, int int3, int int4) {
        int $$5 = int3;
        int $$6 = int4;
        if (int3 > this.width - int1) {
            $$5 = this.width - int1;
        }
        if (int4 > this.height - int2) {
            $$6 = this.height - int2;
        }
        this.rowHeight = $$6;
        for (int $$7 = 0; $$7 < $$6; $$7++) {
            byteBuffer0.position((int4 - $$6) * int3 * 3 + $$7 * int3 * 3);
            int $$8 = (int1 + $$7 * this.width) * 3;
            byteBuffer0.get(this.bytes, $$8, $$5 * 3);
        }
    }

    public void saveRow() throws IOException {
        this.outputStream.write(this.bytes, 0, this.width * 3 * this.rowHeight);
    }

    public File close() throws IOException {
        this.outputStream.close();
        return this.file;
    }
}