package com.mna.recipes.rituals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IRitualRecipe;
import com.mna.api.rituals.IRitualReagent;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.items.ItemInit;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.multiblock.MultiblockConfiguration;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.rituals.MatchedRitual;
import com.mna.rituals.RitualReagent;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualRecipe extends AMRecipeBase implements IRitualRecipe {

    private long innerColor;

    private long outerColor;

    private long beamColor;

    private boolean connectBeam;

    private boolean displayIndexes;

    private boolean kittable;

    private boolean isValid;

    private int[][] pattern;

    private int[][] displayPattern;

    private RitualReagent[][] reagents;

    private String[] manaweave_patterns;

    private int RITUAL_SIZE;

    private ItemStack createdItem = ItemStack.EMPTY;

    private ItemStack guiItem = ItemStack.EMPTY;

    private String command;

    private MultiblockDefinition _cachedMultiblock;

    public RitualRecipe(ResourceLocation id) {
        super(id);
        this.connectBeam = true;
        this.displayIndexes = true;
        this.kittable = true;
        this.innerColor = 16777215L;
        this.outerColor = 65280L;
        this.beamColor = 16777215L;
    }

    @Override
    public ItemStack getResultItem() {
        if (this.createdItem.isEmpty()) {
            ItemStack output = new ItemStack(ItemInit.RUNE_RITUAL_METAL.get());
            output.setHoverName(Component.translatable(this.m_6423_().toString()));
            this.createdItem = output;
        }
        return this.createdItem;
    }

    @Override
    public void parseExtraJson(JsonObject json) {
        if (json.has("keys") && json.has("pattern") && json.has("reagents") && json.get("pattern").isJsonArray() && json.get("reagents").isJsonArray() && json.get("keys").isJsonObject()) {
            JsonArray patternData = json.get("pattern").getAsJsonArray();
            try {
                this.pattern = this.parsePatternArray(patternData);
                if (this.pattern.length == 0) {
                    this.isValid = false;
                    return;
                }
            } catch (Exception var12) {
                var12.printStackTrace();
                this.isValid = false;
                return;
            }
            if (json.has("displayPattern")) {
                JsonArray displayPatternData = json.get("displayPattern").getAsJsonArray();
                try {
                    this.displayPattern = this.parsePatternArray(displayPatternData);
                } catch (Exception var10) {
                    var10.printStackTrace();
                    this.displayPattern = this.pattern;
                    ManaAndArtifice.LOGGER.error("Malformed display pattern JSON for ritual recipe (" + this.m_6423_().toString() + ").  Falling back to main pattern for display.");
                }
            } else {
                this.displayPattern = this.pattern;
            }
            JsonObject reagentKeys = json.get("keys").getAsJsonObject();
            JsonArray reagentData = json.get("reagents").getAsJsonArray();
            try {
                if (!this.parseReagentArray(reagentData, reagentKeys)) {
                    this.isValid = false;
                    return;
                }
            } catch (Exception var11) {
                var11.printStackTrace();
                this.isValid = false;
                return;
            }
            if (json.has("manaweave")) {
                try {
                    JsonElement manaweave = json.get("manaweave");
                    this.parseManaweave(manaweave);
                } catch (Exception var9) {
                    var9.printStackTrace();
                    this.isValid = false;
                    return;
                }
            } else {
                this.manaweave_patterns = new String[0];
            }
            if (json.has("parameters")) {
                try {
                    JsonObject params = json.get("parameters").getAsJsonObject();
                    this.parseParameters(params);
                } catch (Exception var8) {
                    var8.printStackTrace();
                    this.isValid = false;
                    return;
                }
            }
            if (json.has("createsItem")) {
                try {
                    ResourceLocation output = new ResourceLocation(json.get("createsItem").getAsString());
                    Item item = ForgeRegistries.ITEMS.containsKey(output) ? ForgeRegistries.ITEMS.getValue(output) : null;
                    if (item != null) {
                        this.createdItem = new ItemStack(item);
                    }
                } catch (Exception var7) {
                    var7.printStackTrace();
                    this.isValid = false;
                    return;
                }
            }
            if (json.has("command")) {
                this.command = json.get("command").getAsString();
            }
            if (!this.validatePatternArray(this.pattern)) {
                ManaAndArtifice.LOGGER.error("Error parsing pattern " + this.m_6423_().toString() + ". Ritual pattern must be square.  Pattern size: " + this.RITUAL_SIZE + "x" + this.RITUAL_SIZE + ".");
                this.isValid = false;
            } else if (!this.validateDisplayPatternArray(this.displayPattern)) {
                ManaAndArtifice.LOGGER.error("Error parsing display pattern " + this.m_6423_().toString() + ". Ritual display pattern must be the same size as the main pattern array.  Pattern size: " + this.RITUAL_SIZE + "x" + this.RITUAL_SIZE + ".");
                this.isValid = false;
            } else if (!this.validateReagentArray(this.reagents, this.pattern)) {
                ManaAndArtifice.LOGGER.error("Error parsing pattern " + this.m_6423_().toString() + ". Reagent array must be the same size as the pattern array, and reagent locations must correspond to the same non-zero elements in the pattern array.");
                this.isValid = false;
            } else {
                this.isValid = true;
            }
        } else {
            ManaAndArtifice.LOGGER.error("Malformed JSON for ritual recipe (" + this.m_6423_().toString() + ").  Missing critical elements.  It was NOT loaded!");
            this.isValid = false;
        }
    }

    private void parseManaweave(JsonElement manaweave) {
        if (manaweave.isJsonArray()) {
            JsonArray manaweave_array = manaweave.getAsJsonArray();
            this.manaweave_patterns = new String[manaweave_array.size()];
            for (int i = 0; i < manaweave_array.size(); i++) {
                this.manaweave_patterns[i] = manaweave_array.get(i).getAsString();
            }
        } else {
            this.manaweave_patterns = new String[0];
        }
    }

    private void parseParameters(JsonObject parameters) {
        if (parameters.has("innerColor")) {
            this.innerColor = Long.decode(parameters.get("innerColor").getAsString());
        }
        if (parameters.has("outerColor")) {
            this.outerColor = Long.decode(parameters.get("outerColor").getAsString());
        }
        if (parameters.has("beamColor")) {
            this.beamColor = Long.decode(parameters.get("beamColor").getAsString());
        }
        if (parameters.has("connectBeam")) {
            this.connectBeam = parameters.get("connectBeam").getAsBoolean();
        }
        if (parameters.has("displayIndexes")) {
            this.displayIndexes = parameters.get("displayIndexes").getAsBoolean();
        }
        if (parameters.has("kittable")) {
            this.kittable = parameters.get("kittable").getAsBoolean();
        }
        if (parameters.has("tier")) {
            this.tier = parameters.get("tier").getAsInt();
        } else {
            this.tier = 0;
        }
    }

    private int[][] parsePatternArray(JsonArray patternData) {
        int[][] output = new int[patternData.size()][];
        for (int i = 0; i < patternData.size(); i++) {
            JsonElement subElem = patternData.get(i);
            if (!subElem.isJsonArray()) {
                ManaAndArtifice.LOGGER.error("Malformed pattern JSON for ritual recipe (" + this.m_6423_().toString() + ").  Sub element is not an array.");
                return new int[0][];
            }
            JsonArray subArray = subElem.getAsJsonArray();
            output[i] = new int[subArray.size()];
            for (int j = 0; j < subArray.size(); j++) {
                JsonElement elem = subArray.get(j);
                output[i][j] = elem.getAsInt();
            }
        }
        return output;
    }

    private boolean parseReagentArray(JsonArray reagentPatterns, JsonObject keyData) {
        Map<Character, RitualReagent> keys = new HashMap();
        if (reagentPatterns.size() != this.pattern.length) {
            return false;
        } else {
            this.reagents = new RitualReagent[reagentPatterns.size()][];
            for (int i = 0; i < reagentPatterns.size(); i++) {
                JsonElement elem = reagentPatterns.get(i);
                if (!elem.isJsonPrimitive()) {
                    return false;
                }
                char[] cArr = elem.getAsString().toCharArray();
                this.reagents[i] = new RitualReagent[cArr.length];
                for (int j = 0; j < cArr.length; j++) {
                    RitualReagent rr = null;
                    if (cArr[j] != ' ') {
                        if (!keys.containsKey(cArr[j])) {
                            String key = String.valueOf(cArr[j]);
                            if (!keyData.has(key)) {
                                ManaAndArtifice.LOGGER.error("Missing pattern map in ritual (" + this.m_6423_().toString() + ") - key '" + key + "' is undefined.");
                                return false;
                            }
                            JsonElement subElem = keyData.get(key);
                            if (subElem.isJsonObject()) {
                                JsonObject subObj = subElem.getAsJsonObject();
                                if (subObj.has("item")) {
                                    rr = new RitualReagent(subObj.get("item").getAsString());
                                    if (subObj.has("optional") && subObj.get("optional").getAsBoolean()) {
                                        rr.asOptional();
                                    }
                                    if (subObj.has("consume") && !subObj.get("consume").getAsBoolean()) {
                                        rr.noConsume();
                                    }
                                    if (subObj.has("manual_return") && subObj.get("manual_return").getAsBoolean()) {
                                        rr.manualReturn();
                                    }
                                    if (subObj.has("is_dynamic") && subObj.get("is_dynamic").getAsBoolean()) {
                                        rr.asDynamic();
                                    }
                                    if (subObj.has("dynamic_source") && subObj.get("dynamic_source").getAsBoolean()) {
                                        rr.asDynamicSource();
                                    }
                                    keys.put(cArr[j], rr);
                                }
                            }
                        } else {
                            rr = (RitualReagent) keys.get(cArr[j]);
                        }
                    }
                    this.reagents[i][j] = rr;
                }
            }
            return true;
        }
    }

    @Nullable
    private RitualReagent parseReagentKey(JsonObject key) {
        JsonElement itemObj = key.get("item");
        JsonElement optionalObj = key.get("optional");
        JsonElement consumeObj = key.get("consume");
        if (itemObj == null) {
            return null;
        } else {
            RitualReagent rr = new RitualReagent(itemObj.getAsString());
            if (optionalObj != null && optionalObj.getAsBoolean()) {
                rr.asOptional();
            }
            if (consumeObj != null && !consumeObj.getAsBoolean()) {
                rr.noConsume();
            }
            return rr;
        }
    }

    CompoundTag reagentsToNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("length", this.reagents.length);
        for (int i = 0; i < this.reagents.length; i++) {
            for (int j = 0; j < this.reagents[i].length; j++) {
                if (this.reagents[i][j] != null) {
                    CompoundTag reagentData = new CompoundTag();
                    this.reagents[i][j].writeToNBT(reagentData);
                    nbt.put("r" + i + "_" + j, reagentData);
                }
            }
        }
        return nbt;
    }

    void reagentsFromNBT(CompoundTag nbt) {
        if (nbt.contains("length")) {
            int length = nbt.getInt("length");
            this.reagents = new RitualReagent[length][];
            for (int i = 0; i < length; i++) {
                this.reagents[i] = new RitualReagent[length];
                for (int j = 0; j < length; j++) {
                    if (nbt.contains("r" + i + "_" + j)) {
                        CompoundTag reagentData = nbt.getCompound("r" + i + "_" + j);
                        this.reagents[i][j] = RitualReagent.fromNBT(reagentData);
                    } else {
                        this.reagents[i][j] = null;
                    }
                }
            }
            if (!this.validateReagentArray(this.reagents, this.pattern)) {
                throw new InvalidParameterException("Error parsing pattern " + this.m_6423_().toString() + ". Reagent array must be the same size as the pattern array, and reagent locations must correspond to the same non-zero elements in the pattern array.");
            }
        }
    }

    @Override
    public int[][] getPattern() {
        return this.pattern;
    }

    void setPattern(int[][] pattern) {
        this.pattern = pattern;
        if (!this.validatePatternArray(pattern)) {
            throw new InvalidParameterException("Error parsing pattern " + this.m_6423_().toString() + ". Ritual pattern must be square.  Pattern size: " + this.RITUAL_SIZE + "x" + this.RITUAL_SIZE + ".");
        } else {
            assert this.RITUAL_SIZE % 2 != 0;
            this.isValid = true;
        }
    }

    public int[][] getDisplayPattern() {
        return this.displayPattern;
    }

    void setDisplayPattern(int[][] displayPattern) {
        this.displayPattern = displayPattern;
    }

    @Override
    public IRitualReagent[][] getReagents() {
        return this.reagents;
    }

    void setReagents(RitualReagent[][] reagents) {
        this.reagents = reagents;
        if (!this.validateReagentArray(reagents, this.pattern)) {
            throw new InvalidParameterException("Error parsing pattern " + this.m_6423_().toString() + ". Reagent array must be the same size as the pattern array, and reagent locations must correspond to the same non-zero elements in the pattern array.");
        } else {
            this.isValid = true;
        }
    }

    @Override
    public int getLowerBound() {
        return (int) Math.floor((double) ((float) this.RITUAL_SIZE / 2.0F));
    }

    public RitualReagent[] AllReagents() {
        ArrayList<RitualReagent> list = new ArrayList();
        for (int i = 0; i < this.reagents.length; i++) {
            for (int j = 0; j < this.reagents[i].length; j++) {
                if (this.reagents[i][j] != null) {
                    list.add(this.reagents[i][j]);
                }
            }
        }
        RitualReagent[] output = new RitualReagent[list.size()];
        return (RitualReagent[]) list.toArray(output);
    }

    @Override
    public int countRunes() {
        int count = 0;
        for (int i = 0; i < this.pattern.length; i++) {
            for (int j = 0; j < this.pattern[i].length; j++) {
                if (this.pattern[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public long getInnerColor() {
        return this.innerColor;
    }

    public long getBeamColor() {
        return this.beamColor;
    }

    void setBeamColor(long beamColor) {
        this.beamColor = beamColor;
    }

    void setInnerColor(long innerColor) {
        this.innerColor = innerColor;
    }

    public long getOuterColor() {
        return this.outerColor;
    }

    void setOuterColor(long outerColor) {
        this.outerColor = outerColor;
    }

    public boolean getConnectBeam() {
        return this.connectBeam;
    }

    void setConnectBeam(boolean connectBeam) {
        this.connectBeam = connectBeam;
    }

    public boolean getIsKittable() {
        return this.kittable;
    }

    void setKittable(boolean kittable) {
        this.kittable = kittable;
    }

    public boolean getDisplayIndexes() {
        return this.displayIndexes;
    }

    public void setDisplayIndexes(boolean displayIndexes) {
        this.displayIndexes = displayIndexes;
    }

    @Override
    public String[] getManaweavePatterns() {
        return this.manaweave_patterns;
    }

    void setManaweavePatterns(String[] patterns) {
        this.manaweave_patterns = patterns;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        if (this.guiItem.isEmpty()) {
            ItemStack output = new ItemStack(ItemInit.RUNE_RITUAL_METAL.get());
            output.setHoverName(Component.translatable(this.m_6423_().toString()));
            output.getOrCreateTag().putBoolean("hideTier", true);
            this.guiItem = output;
        }
        return this.guiItem;
    }

    public MultiblockDefinition getAsMultiblock() {
        if (this._cachedMultiblock == null) {
            this._cachedMultiblock = new MultiblockDefinition(this.m_6423_());
            this._cachedMultiblock.setBlockStates(Arrays.asList(BlockInit.CHALK_RUNE.get().defaultBlockState()));
            HashMap<ResourceLocation, Integer> matchers = new HashMap();
            matchers.put(MultiblockDefinition.statelessMatcher.getId(), 0);
            this._cachedMultiblock.setSpecialBlockMatchersByBlock(matchers);
            MultiblockConfiguration conf = MultiblockConfiguration.createDummyStructure();
            this.getBlockPositions(BlockPos.ZERO, this.pattern, this.displayPattern, this.reagents).forEach(rbp -> conf.setBlockAt(rbp.getBlockPos(), 0));
            conf.validate();
            this._cachedMultiblock.setStructure(conf);
        }
        return this._cachedMultiblock;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public boolean hasCommand() {
        return !StringUtil.isNullOrEmpty(this.command);
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return this.getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.RITUAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RITUAL_TYPE.get();
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return true;
    }

    public static RitualRecipe find(Level world, ResourceLocation name) {
        return RitualRecipeHelper.GetRitualRecipe(world, name);
    }

    @Nullable
    public static MatchedRitual matchAnyInWorld(BlockPos center, Level world) {
        for (RitualRecipe ritual : RitualRecipeHelper.getAllRituals(world)) {
            NonNullList<RitualBlockPos> matchedPositions = ritual.matchInWorld(center, world);
            if (matchedPositions != null) {
                return new MatchedRitual(matchedPositions, ritual, center);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Direction getMatchedDirection(Level world, BlockPos center) {
        if (!this.isValid) {
            return null;
        } else if (this.matchInWorld(center, world, this.pattern)) {
            return Direction.NORTH;
        } else {
            int[][] rot90 = this.rotateNumberArrayCW(this.pattern);
            if (this.matchInWorld(center, world, rot90)) {
                return Direction.EAST;
            } else {
                int[][] rot180 = this.rotateNumberArrayCW(rot90);
                if (this.matchInWorld(center, world, rot180)) {
                    return Direction.SOUTH;
                } else {
                    int[][] rot270 = this.rotateNumberArrayCW(rot180);
                    return this.matchInWorld(center, world, rot270) ? Direction.WEST : null;
                }
            }
        }
    }

    public boolean isRuneAtOffset(int x, int y) {
        return x >= 0 && x < this.RITUAL_SIZE && y >= 0 && y < this.RITUAL_SIZE ? this.pattern[x][y] != 0 : false;
    }

    @Nullable
    public RitualReagent getReagentAtOffset(int x, int y) {
        return x >= 0 && x < this.RITUAL_SIZE && y >= 0 && y < this.RITUAL_SIZE ? this.reagents[x][y] : null;
    }

    public NonNullList<RitualBlockPos> matchInWorld(BlockPos center, Level world) {
        if (!this.isValid) {
            return null;
        } else if (this.matchInWorld(center, world, this.pattern)) {
            return this.getBlockPositions(center, this.pattern, this.displayPattern, this.reagents);
        } else {
            int[][] rot90 = this.rotateNumberArrayCW(this.pattern);
            int[][] dRot90 = this.rotateNumberArrayCW(this.displayPattern);
            RitualReagent[][] rot90rLoc = this.rotateReagentsCW(this.reagents);
            if (this.matchInWorld(center, world, rot90)) {
                return this.getBlockPositions(center, rot90, dRot90, rot90rLoc);
            } else {
                int[][] rot180 = this.rotateNumberArrayCW(rot90);
                int[][] dRot180 = this.rotateNumberArrayCW(dRot90);
                RitualReagent[][] rot180rLoc = this.rotateReagentsCW(rot90rLoc);
                if (this.matchInWorld(center, world, rot180)) {
                    return this.getBlockPositions(center, rot180, dRot180, rot180rLoc);
                } else {
                    int[][] rot270 = this.rotateNumberArrayCW(rot180);
                    int[][] dRot270 = this.rotateNumberArrayCW(dRot180);
                    RitualReagent[][] rot270rLoc = this.rotateReagentsCW(rot180rLoc);
                    return this.matchInWorld(center, world, rot270) ? this.getBlockPositions(center, rot270, dRot270, rot270rLoc) : null;
                }
            }
        }
    }

    private int[][] rotateNumberArrayCW(int[][] mat) {
        int M = mat.length;
        int N = mat[0].length;
        int[][] ret = new int[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M - 1 - r] = mat[r][c];
            }
        }
        return ret;
    }

    private RitualReagent[][] rotateReagentsCW(RitualReagent[][] mat) {
        int M = mat.length;
        int N = mat[0].length;
        RitualReagent[][] ret = new RitualReagent[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M - 1 - r] = mat[r][c];
            }
        }
        return ret;
    }

    private boolean validatePatternArray(int[][] array) {
        this.RITUAL_SIZE = array.length;
        for (int i = 0; i < array.length; i++) {
            if (array[i].length != this.RITUAL_SIZE) {
                return false;
            }
        }
        return true;
    }

    private boolean validateDisplayPatternArray(int[][] array) {
        if (array.length != this.pattern.length) {
            return false;
        } else {
            for (int i = 0; i < array.length; i++) {
                if (array[i].length != this.pattern[i].length) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean validateReagentArray(RitualReagent[][] array, int[][] pattern) {
        if (array.length != this.RITUAL_SIZE) {
            return false;
        } else {
            for (int i = 0; i < array.length; i++) {
                if (array[i].length != this.RITUAL_SIZE) {
                    return false;
                }
            }
            boolean dynamicSourceFound = false;
            for (int ix = 0; ix < array.length; ix++) {
                for (int j = 0; j < array[ix].length; j++) {
                    if (array[ix][j] != null) {
                        if (pattern[ix][j] == 0) {
                            return false;
                        }
                        if (array[ix][j].isDynamicSource()) {
                            if (dynamicSourceFound) {
                                ManaAndArtifice.LOGGER.error("Ritual recipe " + this.m_6423_().toString() + " has more than one dynamic source defined - there can only be one.");
                                this.isValid = false;
                                return false;
                            }
                            dynamicSourceFound = true;
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean matchInWorld(BlockPos center, Level world, int[][] pattern) {
        int bound = this.getLowerBound();
        for (int i = -bound; i <= bound; i++) {
            for (int j = -bound; j <= bound; j++) {
                boolean checkValue = world.getBlockState(center.offset(i, 0, j)).m_60734_() instanceof ChalkRuneBlock;
                if (pattern[i + bound][j + bound] == 0) {
                    if (checkValue) {
                        return false;
                    }
                } else if (!checkValue) {
                    return false;
                }
            }
        }
        return true;
    }

    private NonNullList<RitualBlockPos> getBlockPositions(BlockPos center, int[][] pattern, int[][] displayPattern, RitualReagent[][] reagents) {
        NonNullList<RitualBlockPos> positions = NonNullList.create();
        int bound = this.getLowerBound();
        for (int i = -bound; i <= bound; i++) {
            for (int j = -bound; j <= bound; j++) {
                if (pattern[i + bound][j + bound] != 0) {
                    positions.add(new RitualBlockPos(pattern[i + bound][j + bound], displayPattern[i + bound][j + bound], new BlockPos(center.m_123341_() + i, center.m_123342_(), center.m_123343_() + j), reagents[i + bound][j + bound]));
                }
            }
        }
        return positions;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }
}