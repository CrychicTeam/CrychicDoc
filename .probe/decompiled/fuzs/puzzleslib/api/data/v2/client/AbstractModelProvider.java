package fuzs.puzzleslib.api.data.v2.client;

import fuzs.puzzleslib.api.data.v2.client.model.ModItemModelProvider;
import fuzs.puzzleslib.api.data.v2.core.ForgeDataProviderContext;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

@Deprecated
public abstract class AbstractModelProvider extends BlockStateProvider {

    private final ModItemModelProvider itemModels;

    public AbstractModelProvider(ForgeDataProviderContext context) {
        this(context.getModId(), context.getPackOutput(), context.getFileHelper());
    }

    public AbstractModelProvider(String modId, PackOutput packOutput, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
        this.itemModels = new ModItemModelProvider(packOutput, modId, fileHelper, this);
    }

    public final ModItemModelProvider itemModels() {
        return this.itemModels;
    }

    @Override
    protected abstract void registerStatesAndModels();

    public void simpleExistingBlock(Block block) {
        this.simpleBlock(block, this.existingBlockModel(block));
    }

    public void simpleExistingBlockWithItem(Block block) {
        ModelFile.ExistingModelFile model = this.existingBlockModel(block);
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }

    public ModelFile.ExistingModelFile existingBlockModel(Block block) {
        return new ModelFile.ExistingModelFile(this.blockTexture(block), this.models().existingFileHelper);
    }

    public void builtInBlock(Block block, Block particleTexture) {
        this.builtInBlock(block, this.blockTexture(particleTexture));
    }

    public void builtInBlock(Block block, ResourceLocation particleTexture) {
        this.simpleBlock(block, this.models().getBuilder(this.name(block)).texture("particle", particleTexture));
    }

    public void cubeBottomTopBlock(Block block) {
        this.cubeBottomTopBlock(block, this.extend(this.blockTexture(block), "_side"), this.extend(this.blockTexture(block), "_bottom"), this.extend(this.blockTexture(block), "_top"));
        this.itemModels().withExistingParent(this.name(block), this.extendKey(block, "block"));
    }

    public void cubeBottomTopBlock(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        this.simpleBlock(block, this.models().cubeBottomTop(this.name(block), side, bottom, top));
    }

    @Override
    public ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    @Override
    public String name(Block block) {
        return this.key(block).getPath();
    }

    public ResourceLocation extendKey(Block block, String... extensions) {
        ResourceLocation loc = this.key(block);
        extensions = (String[]) ArrayUtils.add(extensions, loc.getPath());
        return new ResourceLocation(loc.getNamespace(), String.join("/", extensions));
    }

    @Override
    public ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}