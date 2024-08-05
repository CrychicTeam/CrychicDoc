package journeymap.client.mod.vanilla;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import journeymap.client.JourneymapClient;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.properties.CoreProperties;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;

public final class VanillaBlockHandler implements IModBlockHandler {

    ListMultimap<MapColor, BlockFlag> materialFlags = ListMultimapBuilder.linkedHashKeys().arrayListValues().build();

    ListMultimap<Class<?>, BlockFlag> blockClassFlags = ListMultimapBuilder.linkedHashKeys().arrayListValues().build();

    ListMultimap<Block, BlockFlag> blockFlags = ListMultimapBuilder.linkedHashKeys().arrayListValues().build();

    HashMap<MapColor, Float> materialAlphas = new HashMap();

    HashMap<Block, Float> blockAlphas = new HashMap();

    HashMap<Class<?>, Float> blockClassAlphas = new HashMap();

    private boolean mapPlants;

    private boolean mapPlantShadows;

    private boolean mapCrops;

    public VanillaBlockHandler() {
        this.preInitialize();
    }

    private void preInitialize() {
        CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
        this.mapPlants = coreProperties.mapPlants.get();
        this.mapCrops = coreProperties.mapCrops.get();
        this.mapPlantShadows = coreProperties.mapPlantShadows.get();
        this.setFlags(Blocks.BARRIER, BlockFlag.Ignore);
        this.setFlags(Blocks.AIR, BlockFlag.Ignore);
        this.setFlags(Blocks.GLASS, 0.4F, BlockFlag.Transparency);
        this.setFlags(StainedGlassPaneBlock.class, 0.4F, BlockFlag.Transparency);
        this.setFlags(StainedGlassBlock.class, 0.4F, BlockFlag.Transparency);
        this.setFlags(Blocks.GRASS, BlockFlag.Grass);
        if (coreProperties.caveIgnoreGlass.get()) {
            this.setFlags(Blocks.GLASS, BlockFlag.OpenToSky);
        }
        this.setFlags(Blocks.LAVA, 1.0F, BlockFlag.NoShadow);
        this.setFlags(MapColor.WATER, 0.25F, BlockFlag.Water, BlockFlag.NoShadow);
        this.setFlags(MapColor.WOOD, BlockFlag.NoTopo);
        this.setFlags(MapColor.PLANT, BlockFlag.Plant, BlockFlag.NoTopo);
        this.setFlags(Blocks.BAMBOO, BlockFlag.NoTopo);
        this.setFlags(Blocks.BAMBOO_SAPLING, BlockFlag.NoTopo);
        this.materialAlphas.put(MapColor.ICE, 0.8F);
        this.setFlags(Blocks.IRON_BARS, 0.4F, BlockFlag.Transparency);
        this.setFlags(Blocks.FIRE, BlockFlag.NoShadow);
        this.setFlags(Blocks.LADDER, BlockFlag.OpenToSky);
        this.setFlags(Blocks.SNOW, BlockFlag.NoTopo, BlockFlag.NoShadow);
        this.setFlags(Blocks.TRIPWIRE, BlockFlag.Ignore);
        this.setFlags(Blocks.TRIPWIRE_HOOK, BlockFlag.Ignore);
        this.setFlags(Blocks.COBWEB, BlockFlag.OpenToSky, BlockFlag.NoShadow);
        this.setFlags(FenceBlock.class, 0.4F, BlockFlag.Transparency);
        this.setFlags(FenceGateBlock.class, 0.4F, BlockFlag.Transparency);
        this.setFlags(GrassBlock.class, BlockFlag.Grass);
        this.setFlags(LeavesBlock.class, BlockFlag.OpenToSky, BlockFlag.Foliage, BlockFlag.NoTopo);
        this.setFlags(RailBlock.class, BlockFlag.NoShadow, BlockFlag.NoTopo);
        this.setFlags(RedStoneWireBlock.class, BlockFlag.Ignore);
        this.setFlags(TorchBlock.class, BlockFlag.Ignore);
        this.setFlags(VineBlock.class, 0.2F, BlockFlag.OpenToSky, BlockFlag.Foliage, BlockFlag.NoShadow);
        this.setFlags(BushBlock.class, BlockFlag.Plant);
        this.setFlags(CactusBlock.class, BlockFlag.Plant, BlockFlag.NoTopo);
        this.setFlags(SugarCaneBlock.class, BlockFlag.Plant, BlockFlag.NoTopo);
        this.setFlags(SeagrassBlock.class, BlockFlag.Plant, BlockFlag.NoTopo);
    }

    @Override
    public void initialize(BlockMD blockMD) {
        Block block = blockMD.getBlockState().m_60734_();
        MapColor mapColor = blockMD.getBlock().m_284356_();
        BlockState blockState = blockMD.getBlockState();
        if (blockState.m_60799_() == RenderShape.INVISIBLE && !(blockState.m_60734_() instanceof LiquidBlock)) {
            blockMD.addFlags(BlockFlag.Ignore);
        } else {
            blockMD.addFlags(this.materialFlags.get(mapColor));
            Float alpha = (Float) this.materialAlphas.get(mapColor);
            if (alpha != null) {
                blockMD.setAlpha(alpha);
            }
            if (this.blockFlags.containsKey(block)) {
                blockMD.addFlags(this.blockFlags.get(block));
            }
            alpha = (Float) this.blockAlphas.get(block);
            if (alpha != null) {
                blockMD.setAlpha(alpha);
            }
            for (Class<?> parentClass : this.blockClassFlags.keys()) {
                if (parentClass.isAssignableFrom(block.getClass())) {
                    blockMD.addFlags(this.blockClassFlags.get(parentClass));
                    alpha = (Float) this.blockClassAlphas.get(parentClass);
                    if (alpha != null) {
                        blockMD.setAlpha(alpha);
                    }
                    break;
                }
            }
            if (block instanceof LiquidBlock) {
                blockMD.addFlags(BlockFlag.Fluid, BlockFlag.NoShadow);
                blockMD.setAlpha(0.7F);
            }
            if (block instanceof BushBlock && blockMD.getBlockState().m_61147_().contains(DoublePlantBlock.HALF) && blockMD.getBlockState().m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                blockMD.addFlags(BlockFlag.Ignore);
            }
            if (block instanceof CropBlock) {
                blockMD.addFlags(BlockFlag.Crop);
            }
            if (block instanceof FlowerBlock || block instanceof FlowerPotBlock) {
                blockMD.setBlockColorProxy(FlowerBlockProxy.INSTANCE);
            }
            if (block instanceof BedBlock) {
                blockMD.setBlockColorProxy(BedBlockProxy.INSTANCE);
            }
            String uid = blockMD.getBlockId();
            if (uid.toLowerCase().contains("leaves")) {
                blockMD.removeFlags(BlockFlag.Plant);
                blockMD.addFlags(BlockFlag.OpenToSky, BlockFlag.Foliage, BlockFlag.NoTopo);
            }
            if (uid.toLowerCase().contains("log") || uid.toLowerCase().contains("wood")) {
                blockMD.addFlags(BlockFlag.NoTopo);
            }
            if (uid.equals("minecraft:bell")) {
                blockMD.setBlockColorProxy(ModBlockDelegate.INSTANCE.getMaterialBlockColorProxy());
            } else if (!blockMD.isVanillaBlock()) {
                if (uid.toLowerCase().contains("torch")) {
                    blockMD.addFlags(BlockFlag.Ignore);
                }
            }
        }
    }

    public void postInitialize(BlockMD blockMD) {
        if (blockMD.hasFlag(BlockFlag.Crop)) {
            blockMD.removeFlags(BlockFlag.Plant);
        }
        if (blockMD.hasAnyFlag(BlockMD.FlagsPlantAndCrop)) {
            if ((this.mapPlants || !blockMD.hasFlag(BlockFlag.Plant)) && (this.mapCrops || !blockMD.hasFlag(BlockFlag.Crop))) {
                if (!this.mapPlantShadows) {
                    blockMD.addFlags(BlockFlag.NoShadow);
                }
            } else {
                blockMD.addFlags(BlockFlag.Ignore);
            }
        }
        if (blockMD.isIgnore()) {
            blockMD.removeFlags(BlockMD.FlagsNormal);
        }
    }

    private void setFlags(MapColor material, BlockFlag... flags) {
        this.materialFlags.putAll(material, new ArrayList(Arrays.asList(flags)));
    }

    private void setFlags(MapColor material, Float alpha, BlockFlag... flags) {
        this.materialAlphas.put(material, alpha);
        this.setFlags(material, flags);
    }

    private void setFlags(Class parentClass, BlockFlag... flags) {
        this.blockClassFlags.putAll(parentClass, new ArrayList(Arrays.asList(flags)));
    }

    private void setFlags(Class parentClass, Float alpha, BlockFlag... flags) {
        this.blockClassAlphas.put(parentClass, alpha);
        this.setFlags(parentClass, flags);
    }

    private void setFlags(Block block, BlockFlag... flags) {
        this.blockFlags.putAll(block, new ArrayList(Arrays.asList(flags)));
    }

    private void setFlags(Block block, Float alpha, BlockFlag... flags) {
        this.blockAlphas.put(block, alpha);
        this.setFlags(block, flags);
    }
}