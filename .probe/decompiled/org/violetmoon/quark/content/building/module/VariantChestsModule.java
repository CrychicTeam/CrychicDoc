package org.violetmoon.quark.content.building.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.building.block.VariantChestBlock;
import org.violetmoon.quark.content.building.block.VariantTrappedChestBlock;
import org.violetmoon.quark.content.building.block.be.VariantChestBlockEntity;
import org.violetmoon.quark.content.building.block.be.VariantTrappedChestBlockEntity;
import org.violetmoon.quark.content.building.client.render.be.VariantChestRenderer;
import org.violetmoon.quark.content.building.recipe.MixedExclusionRecipe;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorAbstractChestedHorse;
import org.violetmoon.zeta.client.SimpleWithoutLevelRenderer;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.event.play.entity.living.ZLivingDeath;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;
import org.violetmoon.zeta.util.VanillaWoods;
import org.violetmoon.zeta.util.handler.StructureBlockReplacementHandler;

@ZetaLoadModule(category = "building", antiOverlap = { "woodworks" })
public class VariantChestsModule extends ZetaModule {

    @Config(flag = "chest_reversion")
    private static boolean enableRevertingWoodenChests = true;

    protected final List<Block> regularChests = new ArrayList();

    protected final List<Block> trappedChests = new ArrayList();

    public static BlockEntityType<VariantChestBlockEntity> chestTEType;

    public static BlockEntityType<VariantTrappedChestBlockEntity> trappedChestTEType;

    @Config
    protected boolean replaceWorldgenChests = true;

    @Config(description = "Chests to put in structures. It's preferred to use worldgen tags for this. The format per entry is \"structure=chest\", where \"structure\" is a structure ID, and \"chest\" is a block ID, which must correspond to a standard chest block.")
    public List<String> structureChests = new ArrayList();

    protected final Map<ResourceLocation, Block> manualChestMappings = new HashMap();

    protected final Map<ResourceLocation, Block> manualTrappedChestMappings = new HashMap();

    protected final Map<TagKey<Structure>, Block> chestMappings = new HashMap();

    protected final Map<TagKey<Structure>, Block> trappedChestMappings = new HashMap();

    private static final String DONK_CHEST = "Quark:DonkChest";

    private static final ThreadLocal<ItemStack> WAIT_TO_REPLACE_CHEST = new ThreadLocal();

    @LoadEvent
    public final void register(ZRegister event) {
        event.getRegistry().register(MixedExclusionRecipe.SERIALIZER, "mixed_exclusion", Registries.RECIPE_SERIALIZER);
        for (VanillaWoods.Wood s : VanillaWoods.ALL) {
            this.makeChestBlocks(s.name(), Blocks.CHEST, s.soundPlanks());
        }
        this.makeChestBlocks("nether_brick", Blocks.NETHER_BRICKS, null);
        this.makeChestBlocks("purpur", Blocks.PURPUR_BLOCK, null);
        this.makeChestBlocks("prismarine", Blocks.PRISMARINE, null);
        CreativeTabManager.daisyChain();
        for (Block regularChest : this.regularChests) {
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.FUNCTIONAL_BLOCKS, regularChest, Blocks.CHEST, false);
        }
        CreativeTabManager.endDaisyChain();
        CreativeTabManager.daisyChain();
        for (Block trappedChest : this.trappedChests) {
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.REDSTONE_BLOCKS, trappedChest, Blocks.TRAPPED_CHEST, false);
        }
        CreativeTabManager.endDaisyChain();
        StructureBlockReplacementHandler.addReplacement(this::getGenerationChestBlockState);
    }

    private void makeChestBlocks(String name, Block base, @Nullable SoundType sound) {
        this.makeChestBlocks(this, name, base, sound, BooleanSuppliers.TRUE);
    }

    private void makeChestBlocks(ZetaModule module, String name, Block base, @Nullable SoundType sound, BooleanSupplier condition) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.copy(base);
        if (sound != null) {
            props = props.sound(sound);
        }
        VariantChestBlock regularChest = new VariantChestBlock(name, module, () -> chestTEType, props).setCondition(condition);
        this.regularChests.add(regularChest);
        this.chestMappings.put(TagKey.create(Registries.STRUCTURE, new ResourceLocation("quark", name + "_chest_structures")), regularChest);
        VariantTrappedChestBlock trappedChest = new VariantTrappedChestBlock(name, module, () -> trappedChestTEType, props).setCondition(condition);
        this.trappedChests.add(trappedChest);
        this.trappedChestMappings.put(TagKey.create(Registries.STRUCTURE, new ResourceLocation("quark", name + "_chest_structures")), trappedChest);
        Quark.LOOTR_INTEGRATION.makeChestBlocks(module, name, base, condition, regularChest, trappedChest);
    }

    public static void makeChestBlocksExternal(ZetaModule module, String name, Block base, @Nullable SoundType sound, BooleanSupplier condition) {
        VariantChestsModule me = Quark.ZETA.modules.get(VariantChestsModule.class);
        me.makeChestBlocks(module, name, base, sound, () -> me.enabled && condition.getAsBoolean());
    }

    @LoadEvent
    public void postRegister(ZRegister.Post e) {
        chestTEType = BlockEntityType.Builder.<VariantChestBlockEntity>of(VariantChestBlockEntity::new, (Block[]) this.regularChests.toArray(new Block[0])).build(null);
        trappedChestTEType = BlockEntityType.Builder.<VariantTrappedChestBlockEntity>of(VariantTrappedChestBlockEntity::new, (Block[]) this.trappedChests.toArray(new Block[0])).build(null);
        Quark.ZETA.registry.register(chestTEType, "variant_chest", Registries.BLOCK_ENTITY_TYPE);
        Quark.ZETA.registry.register(trappedChestTEType, "variant_trapped_chest", Registries.BLOCK_ENTITY_TYPE);
        Quark.LOOTR_INTEGRATION.postRegister();
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        this.manualChestMappings.clear();
        this.manualTrappedChestMappings.clear();
        for (String s : new ArrayList(this.structureChests)) {
            String[] toks = s.split("=");
            if (toks.length == 2) {
                String left = toks[0];
                String right = toks[1];
                Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(right));
                if (block != Blocks.AIR) {
                    this.manualChestMappings.put(new ResourceLocation(left), block);
                    if (this.regularChests.contains(block)) {
                        Block trapped = (Block) this.trappedChests.get(this.regularChests.indexOf(block));
                        this.manualTrappedChestMappings.put(new ResourceLocation(left), trapped);
                    }
                }
            }
        }
    }

    private BlockState getGenerationChestBlockState(ServerLevelAccessor accessor, BlockState current, StructureBlockReplacementHandler.StructureHolder structure) {
        if (this.enabled && this.replaceWorldgenChests) {
            if (current.m_60734_() == Blocks.CHEST) {
                return this.replaceChestState(accessor, current, structure, this.chestMappings, this.manualChestMappings);
            }
            if (current.m_60734_() == Blocks.TRAPPED_CHEST) {
                return this.replaceChestState(accessor, current, structure, this.trappedChestMappings, this.manualTrappedChestMappings);
            }
        }
        return null;
    }

    @Nullable
    private BlockState replaceChestState(ServerLevelAccessor accessor, BlockState current, StructureBlockReplacementHandler.StructureHolder structure, Map<TagKey<Structure>, Block> mappings, Map<ResourceLocation, Block> manualMappings) {
        Holder<Structure> structureHolder = StructureBlockReplacementHandler.getStructure(accessor, structure);
        if (structureHolder != null) {
            for (TagKey<Structure> structureTagKey : mappings.keySet()) {
                if (structureHolder.is(structureTagKey)) {
                    Block block = (Block) mappings.get(structureTagKey);
                    return block.withPropertiesOf(current);
                }
            }
            Optional<Block> manualMapping = structureHolder.unwrapKey().map(ResourceKey::m_135782_).map(manualMappings::get);
            if (manualMapping.isPresent()) {
                return ((Block) manualMapping.get()).withPropertiesOf(current);
            }
        }
        return null;
    }

    @PlayEvent
    public void onClickEntity(ZPlayerInteract.EntityInteractSpecific event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        ItemStack held = player.m_21120_(event.getHand());
        if (!held.isEmpty() && target instanceof AbstractChestedHorse horse && !horse.hasChest() && held.getItem() != Items.CHEST && held.is(Tags.Items.CHESTS_WOODEN)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide));
            if (!target.level().isClientSide) {
                ItemStack copy = held.copy();
                copy.setCount(1);
                held.shrink(1);
                horse.getPersistentData().put("Quark:DonkChest", copy.serializeNBT());
                horse.setChest(true);
                horse.m_30625_();
                ((AccessorAbstractChestedHorse) horse).quark$playChestEquipsSound();
            }
        }
    }

    @PlayEvent
    public void onDeath(ZLivingDeath event) {
        if (event.getEntity() instanceof AbstractChestedHorse horse) {
            ItemStack chest = ItemStack.of(horse.getPersistentData().getCompound("Quark:DonkChest"));
            if (!chest.isEmpty() && horse.hasChest()) {
                WAIT_TO_REPLACE_CHEST.set(chest);
            }
        }
    }

    @PlayEvent
    public void onEntityJoinWorld(ZEntityJoinLevel event) {
        Entity target = event.getEntity();
        if (target instanceof ItemEntity item && item.getItem().getItem() == Items.CHEST) {
            ItemStack local = (ItemStack) WAIT_TO_REPLACE_CHEST.get();
            if (local != null && !local.isEmpty()) {
                ((ItemEntity) target).setItem(local);
            }
            WAIT_TO_REPLACE_CHEST.remove();
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends VariantChestsModule {

        @LoadEvent
        public final void clientSetup(ZClientSetup event) {
            BlockEntityRenderers.register(chestTEType, ctx -> new VariantChestRenderer(ctx, false));
            BlockEntityRenderers.register(trappedChestTEType, ctx -> new VariantChestRenderer(ctx, true));
            for (Block b : this.regularChests) {
                QuarkClient.ZETA_CLIENT.setBlockEntityWithoutLevelRenderer(b.asItem(), new SimpleWithoutLevelRenderer(chestTEType, b.defaultBlockState()));
            }
            for (Block b : this.trappedChests) {
                QuarkClient.ZETA_CLIENT.setBlockEntityWithoutLevelRenderer(b.asItem(), new SimpleWithoutLevelRenderer(trappedChestTEType, b.defaultBlockState()));
            }
            QuarkClient.LOOTR_INTEGRATION.clientSetup(event);
        }
    }

    public interface IVariantChest {

        String getTexturePath();

        default String getTextureFolder() {
            return "quark_variant_chests";
        }
    }
}