package vazkii.patchouli.forge.xplat;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import net.minecraftforge.fml.ModContainer;
import vazkii.patchouli.xplat.XplatModContainer;

public class ForgeXplatModContainer implements XplatModContainer {

    private final ModContainer container;

    public ForgeXplatModContainer(ModContainer container) {
        this.container = container;
    }

    @Override
    public String getId() {
        return this.container.getModId();
    }

    @Override
    public String getName() {
        return this.container.getModInfo().getDisplayName();
    }

    @Override
    public Path getPath(String s) {
        return this.container.getModInfo().getOwningFile().getFile().findResource(new String[] { s });
    }

    @Override
    public List<Path> getRootPaths() {
        return Collections.singletonList(this.container.getModInfo().getOwningFile().getFile().getSecureJar().getRootPath());
    }
}