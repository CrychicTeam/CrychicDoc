package net.mehvahdjukaar.moonlight.api.set.leaves;

import java.util.Collection;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.set.BlockTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import org.jetbrains.annotations.Nullable;

public class LeavesTypeRegistry extends BlockTypeRegistry<LeavesType> {

    public static final LeavesTypeRegistry INSTANCE = new LeavesTypeRegistry();

    public static final LeavesType OAK_TYPE = new LeavesType(new ResourceLocation("oak"), Blocks.OAK_LEAVES);

    public static Collection<LeavesType> getTypes() {
        return INSTANCE.getValues();
    }

    @Nullable
    public static LeavesType getValue(ResourceLocation name) {
        return INSTANCE.get(name);
    }

    public static LeavesType fromNBT(String name) {
        return INSTANCE.getFromNBT(name);
    }

    public LeavesTypeRegistry() {
        super(LeavesType.class, "leaves_type");
    }

    public LeavesType getDefaultType() {
        return OAK_TYPE;
    }

    @Override
    public Optional<LeavesType> detectTypeFromBlock(Block baseBlock, ResourceLocation baseRes) {
        String name = null;
        String path = baseRes.getPath();
        if (path.endsWith("_leaves")) {
            name = path.substring(0, path.length() - "_leaves".length());
        } else if (path.startsWith("leaves_")) {
            name = path.substring("leaves_".length());
        }
        String namespace = baseRes.getNamespace();
        if (name != null && !namespace.equals("securitycraft") && !path.contains("hanging") && baseBlock instanceof LeavesBlock) {
            ResourceLocation id = new ResourceLocation(namespace, name);
            return Optional.of(new LeavesType(id, baseBlock));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addTypeTranslations(AfterLanguageLoadEvent language) {
        this.getValues().forEach(w -> {
            if (language.isDefault()) {
                language.addEntry(w.getTranslationKey(), w.getReadableName());
            }
        });
    }
}