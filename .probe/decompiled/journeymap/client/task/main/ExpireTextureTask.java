package journeymap.client.task.main;

import com.mojang.blaze3d.platform.TextureUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import journeymap.client.JourneymapClient;
import journeymap.client.texture.Texture;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class ExpireTextureTask implements IMainThreadTask {

    private static final int MAX_FAILS = 5;

    private static String NAME = "Tick." + MappingMonitorTask.class.getSimpleName();

    private static Logger LOGGER = Journeymap.getLogger();

    private final List<Texture> textures;

    private final int textureId;

    private volatile int fails;

    private ExpireTextureTask(int textureId) {
        this.textures = null;
        this.textureId = textureId;
    }

    private ExpireTextureTask(Texture texture) {
        this.textures = new ArrayList();
        this.textures.add(texture);
        this.textureId = -1;
    }

    private ExpireTextureTask(Collection<Texture> textureCollection) {
        this.textures = new ArrayList(textureCollection);
        this.textureId = -1;
    }

    public static void queue(int textureId) {
        if (textureId != -1) {
            JourneymapClient.getInstance().queueMainThreadTask(new ExpireTextureTask(textureId));
        }
    }

    public static void queue(Texture texture) {
        JourneymapClient.getInstance().queueMainThreadTask(new ExpireTextureTask(texture));
    }

    public static void queue(Collection<Texture> textureCollection) {
        JourneymapClient.getInstance().queueMainThreadTask(new ExpireTextureTask(textureCollection));
    }

    @Override
    public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
        boolean success = this.deleteTextures();
        if (!success && this.textures != null && !this.textures.isEmpty()) {
            this.fails++;
            LOGGER.warn("ExpireTextureTask.perform() couldn't delete textures: " + this.textures + ", fails: " + this.fails);
            if (this.fails <= 5) {
                return this;
            }
        }
        return null;
    }

    private boolean deleteTextures() {
        if (this.textureId != -1) {
            return this.deleteTexture(this.textureId);
        } else {
            Iterator<Texture> iter = this.textures.listIterator();
            while (iter.hasNext()) {
                Texture texture = (Texture) iter.next();
                if (texture == null) {
                    iter.remove();
                } else {
                    if (!this.deleteTexture(texture)) {
                        break;
                    }
                    iter.remove();
                }
            }
            return this.textures.isEmpty();
        }
    }

    private boolean deleteTexture(Texture texture) {
        boolean success = false;
        if (texture.getTextureId() != -1) {
            try {
                texture.release();
                texture.remove();
                success = true;
            } catch (Exception var4) {
                LOGGER.warn("Couldn't delete texture " + texture + ": " + var4);
            }
        } else {
            texture.remove();
            success = true;
        }
        return success;
    }

    private boolean deleteTexture(int textureId) {
        try {
            if (GLFW.glfwGetCurrentContext() == Minecraft.getInstance().getWindow().getWindow()) {
                TextureUtil.releaseTextureId(textureId);
                return true;
            }
        } catch (Exception var3) {
            LOGGER.warn("Couldn't delete textureId " + textureId + ": " + var3);
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }
}