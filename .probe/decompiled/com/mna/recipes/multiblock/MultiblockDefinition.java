package com.mna.recipes.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.blocks.tile.IMultiblockDefinition;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.multiblock.block_matchers.ChalkBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.ExactBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.IBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.PedestalBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.RefractionLensBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.StairsBlockMatcher;
import com.mna.recipes.multiblock.block_matchers.StatelessBlockMatcher;
import com.mna.tools.BlockUtils;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiblockDefinition extends AMRecipeBase implements IMultiblockDefinition {

    private static final ArrayList<IBlockMatcher> blockMatchers = new ArrayList();

    public static final IBlockMatcher defaultMatcher = new ExactBlockMatcher();

    public static final IBlockMatcher stairsMatcher = new StairsBlockMatcher();

    public static final IBlockMatcher statelessMatcher = new StatelessBlockMatcher();

    public static final IBlockMatcher chalkMatcher = new ChalkBlockMatcher();

    public static final IBlockMatcher refractionLensMatcher = new RefractionLensBlockMatcher();

    public static final IBlockMatcher pedestalMatcher = new PedestalBlockMatcher();

    private ItemStack guiStack = ItemStack.EMPTY;

    boolean isValid = false;

    boolean hasLoaded = false;

    boolean hasRequested = false;

    boolean isSymmetrical = false;

    private ResourceLocation structure;

    List<BlockState> blockStates = new ArrayList();

    MultiblockConfiguration structureBlocks;

    ArrayList<MultiblockConfiguration> variations = new ArrayList();

    HashMap<ResourceLocation, Integer> specialBlockMatchersByBlock = new HashMap();

    HashMap<Long, Integer> specialBlockMatchersByOffset = new HashMap();

    public MultiblockDefinition(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    protected void parseExtraJson(JsonObject object) {
        this.isValid = true;
        if (object.has("structure")) {
            this.structure = new ResourceLocation(object.get("structure").getAsString());
            if (object.has("matchers")) {
                JsonArray rawBlockChecks = object.get("matchers").getAsJsonArray();
                rawBlockChecks.forEach(e -> {
                    if (e.isJsonObject()) {
                        JsonObject elem = e.getAsJsonObject();
                        if (elem.has("matcher") && (elem.has("offset") || elem.has("block"))) {
                            ResourceLocation matcher = new ResourceLocation(elem.get("matcher").getAsString());
                            int index = -1;
                            for (int i = 0; i < blockMatchers.size(); i++) {
                                if (((IBlockMatcher) blockMatchers.get(i)).getId().toString().equals(matcher.toString())) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index > -1) {
                                if (elem.has("offset")) {
                                    long offset = elem.get("offset").getAsLong();
                                    this.specialBlockMatchersByOffset.put(offset, index);
                                } else {
                                    ResourceLocation block = new ResourceLocation(elem.get("block").getAsString());
                                    this.specialBlockMatchersByBlock.put(block, index);
                                }
                            } else {
                                ManaAndArtifice.LOGGER.warn("Misconfigured special block match in " + this.m_6423_().toString() + ": matcher '" + matcher.toString() + "' could not be resolved.");
                            }
                        } else {
                            ManaAndArtifice.LOGGER.warn("Misconfigured special block match in " + this.m_6423_().toString() + ": missing matcher and (offset or block id)");
                        }
                    }
                });
            }
            if (object.has("symmetrical")) {
                this.isSymmetrical = object.get("symmetrical").getAsBoolean();
            }
            if (object.has("replacements")) {
                JsonArray replacements = object.get("replacements").getAsJsonArray();
                replacements.forEach(r -> {
                    if (r.isJsonObject()) {
                        JsonObject replacement = r.getAsJsonObject();
                        MultiblockConfiguration variation = MultiblockConfiguration.parseVariation(replacement, this.blockStates);
                        if (variation.getIsValid()) {
                            this.variations.add(variation);
                        }
                    }
                });
            }
        } else {
            this.isValid = false;
        }
    }

    private boolean tryLoadStructure(Level world) {
        if (!this.hasLoaded) {
            if (world instanceof ServerLevel) {
                if (!this.isValid) {
                    return false;
                }
                this.structureBlocks = MultiblockConfiguration.loadStructure(((ServerLevel) world).getStructureManager(), this.structure, this.blockStates);
                this.hasLoaded = this.structureBlocks.getIsValid();
            } else if (!this.hasRequested) {
                this.structureBlocks = MultiblockConfiguration.createDummyStructure();
                ClientMessageDispatcher.sendMultiblockSyncRequestMessage(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_6423_());
                this.hasRequested = true;
            }
        }
        return this.hasLoaded;
    }

    @Override
    public boolean spawn(ServerLevel world, BlockPos origin) {
        return this.spawn(world, origin, Rotation.NONE, true);
    }

    @Override
    public boolean spawn(ServerLevel world, BlockPos origin, Rotation rotation, boolean centerOffset) {
        if (!this.isValid) {
            return false;
        } else if (!this.tryLoadStructure(world)) {
            return false;
        } else {
            if (centerOffset) {
                origin = origin.subtract(new Vec3i(this.getSize().getX() / 2, 0, this.getSize().getZ() / 2));
            }
            this.structureBlocks.resetMatchData();
            this.structureBlocks.rotate(rotation);
            this.variations.forEach(v -> {
                v.resetMatchData();
                v.rotate(rotation);
            });
            BlockPos fOrigin = origin;
            this.structureBlocks.getOffsets().forEach(packedOffset -> {
                BlockPos offset = BlockPos.of(packedOffset);
                BlockPos worldPos = fOrigin.offset(offset);
                int blockStateIndex = this.structureBlocks.getBlockAt(offset);
                if (blockStateIndex >= 0 && blockStateIndex < this.blockStates.size()) {
                    BlockState desiredState = (BlockState) this.blockStates.get(blockStateIndex);
                    world.m_46597_(worldPos, desiredState);
                } else {
                    this.isValid = false;
                }
            });
            return true;
        }
    }

    @Override
    public boolean match(Level world, BlockPos origin) {
        return this.match(world, origin, Rotation.NONE, true);
    }

    @Override
    public boolean match(Level world, BlockPos origin, Rotation rotation, boolean centerOffset) {
        Pair<Integer, Integer> matchCount = this.matchWithCount(world, origin, rotation, centerOffset);
        return ((Integer) matchCount.getFirst()).equals(matchCount.getSecond());
    }

    @Override
    public Pair<Integer, Integer> matchWithCount(Level world, BlockPos origin, Rotation rotation, boolean centerOffset) {
        if (!this.isValid) {
            return new Pair(0, 1);
        } else if (!this.tryLoadStructure(world)) {
            return new Pair(0, 1);
        } else if (world.isClientSide && !this.hasLoaded) {
            return new Pair(0, 1);
        } else {
            if (centerOffset) {
                origin = origin.subtract(new Vec3i(this.getSize().getX() / 2, 0, this.getSize().getZ() / 2));
            }
            this.structureBlocks.resetMatchData();
            this.structureBlocks.rotate(rotation);
            this.variations.forEach(v -> {
                v.resetMatchData();
                v.rotate(rotation);
            });
            BlockPos fOrigin = origin;
            this.structureBlocks.getOffsets().forEach(packedOffset -> {
                BlockPos offset = BlockPos.of(packedOffset);
                BlockPos worldPos = fOrigin.offset(offset);
                BlockState worldState = world.getBlockState(worldPos);
                int blockStateIndex = this.structureBlocks.getBlockAt(offset);
                if (blockStateIndex >= 0 && blockStateIndex < this.blockStates.size()) {
                    BlockState desiredState = (BlockState) this.blockStates.get(blockStateIndex);
                    if (this.matchBlockState(world, offset, worldPos, worldState, desiredState)) {
                        this.structureBlocks.markMatch(offset);
                    }
                    this.variations.forEach(variation -> {
                        int variationStateIndex = variation.getBlockAt(offset);
                        if (variationStateIndex >= 0 && blockStateIndex < this.blockStates.size()) {
                            BlockState variationState = (BlockState) this.blockStates.get(variationStateIndex);
                            if (this.matchBlockState(world, offset, worldPos, worldState, variationState)) {
                                variation.markMatch(offset);
                            }
                        }
                    });
                } else {
                    this.isValid = false;
                }
            });
            this.variations.forEach(v -> v.computeMatch());
            List<MultiblockConfiguration> matchedVariations = (List<MultiblockConfiguration>) this.variations.stream().filter(v -> v.matched()).collect(Collectors.toList());
            return this.structureBlocks.matchCount(matchedVariations);
        }
    }

    @Override
    public int getBlockCount() {
        return this.isValid && this.hasLoaded ? this.structureBlocks.countBlocks() : 1;
    }

    @Nullable
    @Override
    public HashMap<BlockPos, BlockState> getMissingBlocks(Level world, BlockPos origin, Rotation rotation, boolean centerOffset) {
        if (!this.tryLoadStructure(world)) {
            return null;
        } else if (world.isClientSide && !this.hasLoaded) {
            return null;
        } else if (!this.isValid) {
            return null;
        } else {
            if (centerOffset) {
                origin = origin.subtract(new Vec3i(this.getSize().getX() / 2, 0, this.getSize().getZ() / 2));
            }
            HashMap<BlockPos, BlockState> missingBlocks = new HashMap();
            BlockPos fOrigin = origin;
            this.structureBlocks.getOffsets().forEach(packedOffset -> {
                BlockPos offset = BlockPos.of(packedOffset);
                BlockPos worldPos = fOrigin.offset(offset);
                BlockState worldState = world.getBlockState(worldPos);
                int blockStateIndex = this.structureBlocks.getBlockAt(offset);
                if (blockStateIndex >= 0 && blockStateIndex < this.blockStates.size()) {
                    BlockState desiredState = (BlockState) this.blockStates.get(blockStateIndex);
                    if (!this.matchBlockState(world, offset, worldPos, worldState, desiredState)) {
                        missingBlocks.put(worldPos, desiredState);
                    }
                } else {
                    this.isValid = false;
                }
            });
            this.variations.stream().forEach(v -> v.getOffsets().forEach(packedOffset -> {
                BlockPos offset = BlockPos.of(packedOffset);
                BlockPos worldPos = fOrigin.offset(offset);
                BlockState worldState = world.getBlockState(worldPos);
                int blockStateIndex = v.getBlockAt(offset);
                if (blockStateIndex >= 0 && blockStateIndex < this.blockStates.size()) {
                    BlockState desiredState = (BlockState) this.blockStates.get(blockStateIndex);
                    if (this.matchBlockState(world, offset, worldPos, worldState, desiredState)) {
                        missingBlocks.remove(worldPos);
                    }
                } else {
                    this.isValid = false;
                }
            }));
            return missingBlocks;
        }
    }

    @Override
    public List<String> getMatchedVariations() {
        return (List<String>) this.variations.stream().filter(v -> v.matched()).map(v -> v.identifier).collect(Collectors.toList());
    }

    @Override
    public boolean isSymmetrical() {
        return this.isSymmetrical;
    }

    private boolean matchBlockState(Level world, BlockPos offset, BlockPos worldPos, BlockState existing, BlockState desired) {
        int matcherIndex = -1;
        if (this.specialBlockMatchersByOffset.containsKey(offset.asLong())) {
            matcherIndex = (Integer) this.specialBlockMatchersByOffset.get(offset.asLong());
        } else if (this.specialBlockMatchersByBlock.containsKey(ForgeRegistries.BLOCKS.getKey(desired.m_60734_()))) {
            matcherIndex = (Integer) this.specialBlockMatchersByBlock.get(ForgeRegistries.BLOCKS.getKey(desired.m_60734_()));
        }
        if (matcherIndex > -1) {
            return ((IBlockMatcher) blockMatchers.get(matcherIndex)).match(world, offset, worldPos, desired, existing, true);
        } else {
            return desired.m_60734_() instanceof StairBlock ? stairsMatcher.match(world, offset, worldPos, desired, existing, true) : defaultMatcher.match(world, offset, worldPos, desired, existing, true);
        }
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return false;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.MULTIBLOCK_RECIPE_SERIALIZER.get();
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        return this.getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        if (this.guiStack.isEmpty()) {
            this.guiStack = new ItemStack(Items.KNOWLEDGE_BOOK);
            this.guiStack.setHoverName(Component.translatable("mna:multiblock_recipe").append(Component.translatable(this.m_6423_().toString())));
        }
        return this.guiStack;
    }

    @Override
    public ResourceLocation getStructurePath() {
        return this.structure;
    }

    @Override
    public Vec3i getSize() {
        return !this.hasLoaded ? Vec3i.ZERO : this.structureBlocks.getSize();
    }

    @Override
    public void setStructurePath(ResourceLocation path) {
        this.structure = path;
    }

    @Override
    public HashMap<ResourceLocation, Integer> getSpecialBlockMatchersByBlock() {
        return this.specialBlockMatchersByBlock;
    }

    @Override
    public void setSpecialBlockMatchersByBlock(HashMap<ResourceLocation, Integer> matchers) {
        this.specialBlockMatchersByBlock = matchers;
    }

    @Override
    public HashMap<Long, Integer> getSpecialBlockMatchersByOffset() {
        return this.specialBlockMatchersByOffset;
    }

    @Override
    public void setSpecialBlockMatchersByOffset(HashMap<Long, Integer> matchers) {
        this.specialBlockMatchersByOffset = matchers;
    }

    public CompoundTag serializeVariations() {
        CompoundTag data = new CompoundTag();
        ListTag list = new ListTag();
        for (MultiblockConfiguration conf : this.variations) {
            list.add(conf.serialize());
        }
        data.put("list", list);
        return data;
    }

    public void deserializeVariations(CompoundTag data) {
        if (data.contains("list")) {
            ListTag list = data.getList("list", 10);
            this.variations.clear();
            list.forEach(e -> {
                CompoundTag entry = (CompoundTag) e;
                MultiblockConfiguration conf = MultiblockConfiguration.deserialize(entry);
                if (conf.getIsValid()) {
                    this.variations.add(conf);
                }
            });
        }
    }

    public CompoundTag serializeCoreBlocks(ServerLevel world) {
        if (!this.isValid) {
            return new CompoundTag();
        } else if (!this.tryLoadStructure(world)) {
            return new CompoundTag();
        } else {
            CompoundTag data = new CompoundTag();
            data.put("offsets", this.structureBlocks.serialize());
            ListTag states = new ListTag();
            this.blockStates.forEach(state -> states.add(NbtUtils.writeBlockState(state)));
            data.put("states", states);
            return data;
        }
    }

    public void deserializeCoreBlocks(CompoundTag data) {
        this.blockStates = new ArrayList();
        if (data.contains("offsets")) {
            this.structureBlocks = MultiblockConfiguration.deserialize(data.getCompound("offsets"));
        }
        if (data.contains("states", 9)) {
            ListTag states = (ListTag) data.get("states");
            states.forEach(a -> this.blockStates.add(BlockUtils.readBlockState((CompoundTag) a)));
        }
        this.isValid = this.structureBlocks.getIsValid() && this.blockStates.size() > 0;
        this.hasLoaded = true;
    }

    public void setBlockStates(List<BlockState> states) {
        this.blockStates = states;
    }

    public void setStructure(MultiblockConfiguration mainStructure) {
        this.structureBlocks = mainStructure;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.MULTIBLOCK_TYPE.get();
    }

    @Nullable
    public List<List<ItemStack>> getItemsList(Level world) {
        if (!this.tryLoadStructure(world)) {
            return null;
        } else if (world.isClientSide && !this.hasLoaded) {
            return null;
        } else {
            HashMap<Block, Integer> blockCounts = new HashMap();
            this.structureBlocks.structureBlocks.entrySet().forEach(e -> {
                BlockState state = (BlockState) this.blockStates.get((Integer) ((Pair) e.getValue()).getFirst());
                if (!state.m_60795_()) {
                    blockCounts.put(state.m_60734_(), (Integer) blockCounts.getOrDefault(state.m_60734_(), 0) + 1);
                }
            });
            List<List<ItemStack>> blocksList = new ArrayList();
            blockCounts.entrySet().forEach(e -> {
                List<ItemStack> valid = this.getValidItemsFor((Block) e.getKey());
                valid.forEach(s -> s.setCount((Integer) e.getValue()));
                valid = (List<ItemStack>) valid.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
                if (valid.size() > 0) {
                    blocksList.add(valid);
                }
            });
            return blocksList;
        }
    }

    private ArrayList<ItemStack> getValidItemsFor(Block block) {
        int matcherIndex = (Integer) this.specialBlockMatchersByBlock.getOrDefault(ForgeRegistries.BLOCKS.getKey(block), -1);
        if (matcherIndex > -1) {
            return ((IBlockMatcher) blockMatchers.get(matcherIndex)).getValidItems(block);
        } else {
            ArrayList<ItemStack> out = new ArrayList();
            out.add(new ItemStack(block));
            return out;
        }
    }

    @Override
    public List<BlockPos> getPositions(ResourceLocation blockType, @Nullable Predicate<BlockState> predicate) {
        ArrayList<BlockPos> positions = new ArrayList();
        this.structureBlocks.structureBlocks.forEach((l, p) -> {
            BlockState state = (BlockState) this.blockStates.get((Integer) p.getFirst());
            if (state != null) {
                if (predicate != null && !predicate.test(state)) {
                    return;
                }
                if (!ForgeRegistries.BLOCKS.getKey(state.m_60734_()).equals(blockType)) {
                    return;
                }
                positions.add(BlockPos.of(l));
            }
        });
        return positions;
    }

    @Override
    public List<BlockPos> getPositions(List<ResourceLocation> blockTypes, @Nullable Predicate<BlockState> predicate) {
        ArrayList<BlockPos> positions = new ArrayList();
        this.structureBlocks.structureBlocks.forEach((l, p) -> {
            BlockState state = (BlockState) this.blockStates.get((Integer) p.getFirst());
            if (state != null) {
                if (predicate != null && !predicate.test(state)) {
                    return;
                }
                if (blockTypes.stream().noneMatch(s -> ForgeRegistries.BLOCKS.getKey(state.m_60734_()).equals(s))) {
                    return;
                }
                positions.add(BlockPos.of(l));
            }
        });
        return positions;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }

    static {
        blockMatchers.add(stairsMatcher);
        blockMatchers.add(statelessMatcher);
        blockMatchers.add(refractionLensMatcher);
        blockMatchers.add(pedestalMatcher);
        blockMatchers.add(chalkMatcher);
    }
}