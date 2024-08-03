package journeymap.client.mod;

import java.util.HashMap;
import java.util.Map.Entry;
import journeymap.client.mod.impl.Bibliocraft;
import journeymap.client.mod.impl.BiomesOPlenty;
import journeymap.client.mod.impl.ChinjufuMod;
import journeymap.client.mod.impl.CreateMod;
import journeymap.client.mod.impl.ImmersiveRailroading;
import journeymap.client.mod.impl.Pixelmon;
import journeymap.client.mod.impl.ProjectVibrant;
import journeymap.client.mod.impl.Streams;
import journeymap.client.mod.impl.TerraFirmaCraft;
import journeymap.client.mod.vanilla.MaterialBlockColorProxy;
import journeymap.client.mod.vanilla.VanillaBlockColorProxy;
import journeymap.client.mod.vanilla.VanillaBlockHandler;
import journeymap.client.mod.vanilla.VanillaBlockSpriteProxy;
import journeymap.client.model.BlockMD;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.log.LogFormatter;
import org.apache.logging.log4j.Logger;

public enum ModBlockDelegate {

    INSTANCE;

    private final Logger logger = Journeymap.getLogger();

    private final HashMap<String, Class<? extends IModBlockHandler>> handlerClasses = new HashMap();

    private final HashMap<String, IModBlockHandler> handlers = new HashMap(10);

    private VanillaBlockHandler commonBlockHandler;

    private IBlockColorProxy defaultBlockColorProxy;

    private IBlockSpritesProxy defaultBlockSpritesProxy;

    private IBlockColorProxy materialBlockColorProxy;

    private ModBlockDelegate() {
        this.reset();
    }

    public void reset() {
        this.commonBlockHandler = new VanillaBlockHandler();
        this.defaultBlockColorProxy = new VanillaBlockColorProxy();
        this.defaultBlockSpritesProxy = new VanillaBlockSpriteProxy();
        this.materialBlockColorProxy = new MaterialBlockColorProxy();
        this.handlerClasses.clear();
        this.handlerClasses.put("BiblioCraft", Bibliocraft.class);
        this.handlerClasses.put("BiomesOPlenty", BiomesOPlenty.class);
        this.handlerClasses.put("tfc", TerraFirmaCraft.class);
        this.handlerClasses.put("streams", Streams.class);
        this.handlerClasses.put("projectvibrantjourneys", ProjectVibrant.class);
        this.handlerClasses.put("Pixelmon", Pixelmon.class);
        this.handlerClasses.put("immersiverailroading", ImmersiveRailroading.class);
        this.handlerClasses.put("create", CreateMod.class);
        this.handlerClasses.put("chinjufumod", ChinjufuMod.class);
        for (Entry<String, Class<? extends IModBlockHandler>> entry : this.handlerClasses.entrySet()) {
            String modId = (String) entry.getKey();
            Class<? extends IModBlockHandler> handlerClass = (Class<? extends IModBlockHandler>) entry.getValue();
            if (LoaderHooks.isModLoaded(modId)) {
                modId = modId.toLowerCase();
                try {
                    this.handlers.put(modId, (IModBlockHandler) handlerClass.getDeclaredConstructor().newInstance());
                    this.logger.info("Custom modded block handling enabled for " + modId);
                } catch (Exception var6) {
                    this.logger.error(String.format("Couldn't initialize modded block handler for %s: %s", modId, LogFormatter.toPartialString(var6)));
                }
            }
        }
    }

    public void initialize(BlockMD blockMD) {
        if (this.commonBlockHandler == null) {
            this.reset();
        }
        blockMD.setBlockSpritesProxy(this.defaultBlockSpritesProxy);
        blockMD.setBlockColorProxy(this.defaultBlockColorProxy);
        this.initialize(this.commonBlockHandler, blockMD);
        IModBlockHandler modBlockHandler = (IModBlockHandler) this.handlers.get(blockMD.getBlockDomain().toLowerCase());
        if (modBlockHandler != null) {
            modBlockHandler.initialize(blockMD);
        }
        this.commonBlockHandler.postInitialize(blockMD);
    }

    private void initialize(IModBlockHandler handler, BlockMD blockMD) {
        try {
            handler.initialize(blockMD);
        } catch (Throwable var4) {
            this.logger.error(String.format("Couldn't initialize IModBlockHandler '%s' for %s: %s", handler.getClass(), blockMD, LogFormatter.toPartialString(var4)));
        }
    }

    public IModBlockHandler getCommonBlockHandler() {
        return this.commonBlockHandler;
    }

    public IBlockSpritesProxy getDefaultBlockSpritesProxy() {
        return this.defaultBlockSpritesProxy;
    }

    public IBlockColorProxy getDefaultBlockColorProxy() {
        return this.defaultBlockColorProxy;
    }

    public IBlockColorProxy getMaterialBlockColorProxy() {
        return this.materialBlockColorProxy;
    }
}