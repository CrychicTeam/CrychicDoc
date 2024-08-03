package dev.xkmc.l2hostility.content.item.beacon;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2modularblock.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

public class HostilityBeaconBlock extends DelegateEntityBlockImpl implements BeaconBeamBlock {

    public static final BlockEntityBlockMethodImpl<HostilityBeaconBlockEntity> BE = new BlockEntityBlockMethodImpl(LHBlocks.BE_BEACON, HostilityBeaconBlockEntity.class);

    public HostilityBeaconBlock(BlockBehaviour.Properties p) {
        super(p, new BlockMethod[] { BE });
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.RED;
    }

    public static void buildModel(DataGenContext<Block, HostilityBeaconBlock> ctx, RegistrateBlockstateProvider pvd) {
        pvd.models().withExistingParent(ctx.getName(), "block/beacon").texture("particle", pvd.modLoc("block/beacon_glass")).texture("glass", pvd.modLoc("block/beacon_glass")).texture("obsidian", pvd.mcLoc("block/crying_obsidian")).texture("beacon", pvd.modLoc("block/beacon")).renderType("translucent");
        pvd.getMultipartBuilder((Block) ctx.get()).part().modelFile(pvd.models().getBuilder(ctx.getName() + "_base").parent(new ModelFile.UncheckedModelFile(new ResourceLocation("l2hostility", "block/beacon"))).texture("particle", pvd.modLoc("block/beacon_glass")).texture("obsidian", pvd.mcLoc("block/crying_obsidian")).texture("beacon", pvd.modLoc("block/beacon")).renderType("cutout")).addModel().end().part().modelFile(pvd.models().cubeAll(ctx.getName() + "_glass", pvd.modLoc("block/beacon_glass")).renderType("translucent")).addModel().end();
    }
}