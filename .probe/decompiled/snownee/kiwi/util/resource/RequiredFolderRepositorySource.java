package snownee.kiwi.util.resource;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;

public class RequiredFolderRepositorySource extends FolderRepositorySource {

    private static final Logger LOGGER = LogUtils.getLogger();

    public RequiredFolderRepositorySource(Path pFolder, PackType pPackType, PackSource pPackSource) {
        super(pFolder, pPackType, pPackSource);
    }

    @Override
    public void loadPacks(Consumer<Pack> pOnLoad) {
        try {
            FileUtil.createDirectoriesSafe(this.f_10382_);
            m_247293_(this.f_10382_, false, (path, resourcesSupplier) -> {
                String s = m_246927_(path);
                Pack pack = Pack.readMetaAndCreate("file/" + s, Component.literal(s), true, resourcesSupplier, this.f_243749_, Pack.Position.TOP, this.f_10383_);
                if (pack != null) {
                    pOnLoad.accept(pack);
                }
            });
        } catch (IOException var3) {
            LOGGER.warn("Failed to list packs in {}", this.f_10382_, var3);
        }
    }
}