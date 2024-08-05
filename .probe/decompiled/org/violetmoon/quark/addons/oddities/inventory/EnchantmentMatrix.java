package org.violetmoon.quark.addons.oddities.inventory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.content.experimental.module.EnchantmentsBegoneModule;

public class EnchantmentMatrix {

    public static final int MATRIX_WIDTH = 5;

    public static final int MATRIX_HEIGHT = 5;

    private static final int PIECE_VARIANTS = 8;

    private static final String TAG_PIECES = "pieces";

    private static final String TAG_PIECE_ID = "id";

    private static final String TAG_BENCHED_PIECES = "benchedPieces";

    private static final String TAG_PLACED_PIECES = "placedPieces";

    private static final String TAG_COUNT = "count";

    private static final String TAG_TYPE_COUNT = "typeCount";

    private static final String TAG_INFLUENCED = "influenced";

    public final Map<Enchantment, Integer> totalValue = new HashMap();

    public final Map<Integer, EnchantmentMatrix.Piece> pieces = new HashMap();

    public List<Integer> benchedPieces = new ArrayList();

    public List<Integer> placedPieces = new ArrayList();

    public int[][] matrix;

    public int count;

    public int typeCount;

    private boolean influenced;

    public final boolean book;

    public final ItemStack target;

    public final RandomSource rng;

    public EnchantmentMatrix(ItemStack target, RandomSource rng) {
        this.target = target;
        this.rng = rng;
        this.book = target.getItem() == Items.BOOK;
        this.computeMatrix();
    }

    public boolean isInfluenced() {
        return this.influenced;
    }

    public boolean canGeneratePiece(Map<Enchantment, Integer> influences, int bookshelfPower, int enchantability) {
        if (enchantability == 0) {
            return false;
        } else if (!this.generatePiece(influences, bookshelfPower, this.book, true)) {
            return false;
        } else if (this.book) {
            if (!MatrixEnchantingModule.allowBooks) {
                return false;
            } else {
                int bookshelfCount = Math.max(0, Math.min(bookshelfPower - 1, MatrixEnchantingModule.maxBookshelves)) / 7;
                int maxCount = MatrixEnchantingModule.baseMaxPieceCountBook + bookshelfCount;
                return this.count < maxCount;
            }
        } else {
            int bookshelfCount = Math.min(bookshelfPower, MatrixEnchantingModule.maxBookshelves);
            int enchantabilityCount = Math.round((float) enchantability * ((float) bookshelfCount / (float) MatrixEnchantingModule.maxBookshelves));
            int maxCount = MatrixEnchantingModule.baseMaxPieceCount + (bookshelfCount + 1) / 2 + enchantabilityCount / 2;
            return this.count < maxCount;
        }
    }

    public boolean validateXp(Player player, int bookshelfPower) {
        return player.getAbilities().instabuild || player.experienceLevel >= this.getMinXpLevel(bookshelfPower) && player.experienceLevel >= this.getNewPiecePrice();
    }

    public int getMinXpLevel(int bookshelfPower) {
        float scale = (float) MatrixEnchantingModule.minLevelScaleFactor;
        int cutoff = MatrixEnchantingModule.minLevelCutoff;
        if (this.book) {
            return (int) ((double) Math.min(bookshelfPower, MatrixEnchantingModule.maxBookshelves) * MatrixEnchantingModule.minLevelScaleFactorBook);
        } else {
            return this.count > cutoff ? (int) ((float) cutoff * scale) - cutoff + this.count : (int) ((float) this.count * scale);
        }
    }

    public int getNewPiecePrice() {
        return 1 + (MatrixEnchantingModule.piecePriceScale == 0 ? 0 : this.count / MatrixEnchantingModule.piecePriceScale);
    }

    public boolean generatePiece(Map<Enchantment, Integer> influences, int bookshelfPower, boolean isBook, boolean simulate) {
        EnchantmentMatrix.EnchantmentDataWrapper data = this.generateRandomEnchantment(influences, bookshelfPower, isBook, simulate);
        if (data == null) {
            return false;
        } else {
            int type = -1;
            for (EnchantmentMatrix.Piece p : this.pieces.values()) {
                if (p.enchant == data.f_44947_) {
                    type = p.type;
                }
            }
            if (type == -1) {
                type = this.typeCount % 8;
                if (!simulate) {
                    this.typeCount++;
                }
            }
            EnchantmentMatrix.Piece piece = new EnchantmentMatrix.Piece(data, type);
            piece.generateBlocks();
            if (!simulate) {
                this.pieces.put(this.count, piece);
                this.totalValue.put(piece.enchant, (Integer) this.totalValue.getOrDefault(piece.enchant, 0) + piece.getValue());
                this.benchedPieces.add(0, this.count);
                this.count++;
                if (this.book && this.count == 1) {
                    for (int i = 0; i < 2; i++) {
                        if (this.rng.nextBoolean()) {
                            this.count++;
                        }
                    }
                }
            }
            return true;
        }
    }

    private EnchantmentMatrix.EnchantmentDataWrapper generateRandomEnchantment(Map<Enchantment, Integer> influences, int bookshelfPower, boolean isBook, boolean simulate) {
        int level = this.book ? MatrixEnchantingModule.bookEnchantability + this.rng.nextInt(Math.max(1, bookshelfPower) * 2) : 0;
        List<EnchantmentMatrix.Piece> marked = (List<EnchantmentMatrix.Piece>) this.pieces.values().stream().filter(p -> p.marked).collect(Collectors.toList());
        List<EnchantmentMatrix.EnchantmentDataWrapper> validEnchants = new ArrayList();
        BuiltInRegistries.ENCHANTMENT.forEach(enchantment -> {
            String id = BuiltInRegistries.ENCHANTMENT.getKey(enchantment).toString();
            boolean isValid = true;
            if (enchantment.isTreasureOnly()) {
                isValid = MatrixEnchantingModule.allowTreasures || isBook && MatrixEnchantingModule.treasureWhitelist.contains(id);
            }
            if (isValid && !EnchantmentsBegoneModule.shouldBegone(enchantment) && !MatrixEnchantingModule.disallowedEnchantments.contains(id) && (enchantment.canEnchant(this.target) && enchantment.canApplyAtEnchantingTable(this.target) || this.book && enchantment.isAllowedOnBooks())) {
                int enchantLevel = 1;
                if (this.book) {
                    for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; i--) {
                        if (level >= enchantment.getMinCost(i) && level <= enchantment.getMaxCost(i)) {
                            enchantLevel = i;
                            break;
                        }
                    }
                }
                int valueAdded = getValue(enchantment, enchantLevel);
                int currentValue = (Integer) this.totalValue.getOrDefault(enchantment, 0);
                if (valueAdded + currentValue > getValue(enchantment, enchantment.getMaxLevel()) + getMaxXP(enchantment, enchantment.getMaxLevel())) {
                    return;
                }
                EnchantmentMatrix.EnchantmentDataWrapper wrapperx = new EnchantmentMatrix.EnchantmentDataWrapper(enchantment, enchantLevel);
                wrapperx.normalizeRarity(influences, marked);
                validEnchants.add(wrapperx);
            }
        });
        if (validEnchants.isEmpty()) {
            return null;
        } else {
            int total = 0;
            for (EnchantmentMatrix.EnchantmentDataWrapper wrapper : validEnchants) {
                total += wrapper.mutableWeight.val;
            }
            EnchantmentMatrix.EnchantmentDataWrapper ret = (EnchantmentMatrix.EnchantmentDataWrapper) WeightedRandom.getRandomItem(this.rng, validEnchants).orElse(null);
            if (!simulate && ret != null && influences.containsKey(ret.f_44947_) && (Integer) influences.get(ret.f_44947_) > 0) {
                this.influenced = true;
            }
            return ret;
        }
    }

    public boolean place(int id, int x, int y) {
        EnchantmentMatrix.Piece p = (EnchantmentMatrix.Piece) this.pieces.get(id);
        if (p != null && this.benchedPieces.contains(id) && this.canPlace(p, x, y)) {
            p.x = x;
            p.y = y;
            this.benchedPieces.remove(id);
            this.placedPieces.add(id);
            this.computeMatrix();
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(int id) {
        EnchantmentMatrix.Piece p = (EnchantmentMatrix.Piece) this.pieces.get(id);
        if (p != null && this.placedPieces.contains(id)) {
            this.placedPieces.remove(id);
            this.benchedPieces.add(id);
            this.computeMatrix();
            return true;
        } else {
            return false;
        }
    }

    public boolean rotate(int id) {
        EnchantmentMatrix.Piece p = (EnchantmentMatrix.Piece) this.pieces.get(id);
        if (p != null && this.benchedPieces.contains(id)) {
            p.rotate();
            return true;
        } else {
            return false;
        }
    }

    public boolean merge(int placed, int hover) {
        EnchantmentMatrix.Piece placedPiece = (EnchantmentMatrix.Piece) this.pieces.get(placed);
        EnchantmentMatrix.Piece hoveredPiece = (EnchantmentMatrix.Piece) this.pieces.get(hover);
        if (placedPiece != null && hoveredPiece != null && this.placedPieces.contains(placed) && this.benchedPieces.contains(hover)) {
            Enchantment enchant = placedPiece.enchant;
            if (hoveredPiece.enchant == enchant && placedPiece.level < enchant.getMaxLevel()) {
                placedPiece.xp = placedPiece.xp + hoveredPiece.getValue();
                for (int max = placedPiece.getMaxXP(); placedPiece.xp >= max && placedPiece.level < enchant.getMaxLevel(); max = placedPiece.getMaxXP()) {
                    placedPiece.level++;
                    placedPiece.xp -= max;
                }
                if (hoveredPiece.marked) {
                    placedPiece.marked = true;
                }
                this.benchedPieces.remove(hover);
                this.pieces.remove(hover);
                return true;
            }
        }
        return false;
    }

    public void writeToNBT(CompoundTag cmp) {
        ListTag list = new ListTag();
        for (Integer i : this.pieces.keySet()) {
            CompoundTag pieceTag = new CompoundTag();
            pieceTag.putInt("id", i);
            if (((EnchantmentMatrix.Piece) this.pieces.get(i)).enchant != null) {
                ((EnchantmentMatrix.Piece) this.pieces.get(i)).writeToNBT(pieceTag);
                list.add(pieceTag);
            }
        }
        cmp.put("pieces", list);
        cmp.putIntArray("benchedPieces", this.packList(this.benchedPieces));
        cmp.putIntArray("placedPieces", this.packList(this.placedPieces));
        cmp.putInt("count", this.count);
        cmp.putInt("typeCount", this.typeCount);
        cmp.putBoolean("influenced", this.influenced);
    }

    public void readFromNBT(CompoundTag cmp) {
        this.pieces.clear();
        this.totalValue.clear();
        ListTag plist = cmp.getList("pieces", cmp.getId());
        for (int i = 0; i < plist.size(); i++) {
            CompoundTag pieceTag = plist.getCompound(i);
            int id = pieceTag.getInt("id");
            EnchantmentMatrix.Piece piece = new EnchantmentMatrix.Piece();
            piece.readFromNBT(pieceTag);
            this.pieces.put(id, piece);
            this.totalValue.put(piece.enchant, (Integer) this.totalValue.getOrDefault(piece.enchant, 0) + piece.getValue());
        }
        this.benchedPieces = this.unpackList(cmp.getIntArray("benchedPieces"));
        this.placedPieces = this.unpackList(cmp.getIntArray("placedPieces"));
        this.count = cmp.getInt("count");
        this.typeCount = cmp.getInt("typeCount");
        this.influenced = cmp.getBoolean("influenced");
        this.computeMatrix();
    }

    private void computeMatrix() {
        this.matrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.matrix[i][j] = -1;
            }
        }
        for (Integer i : this.placedPieces) {
            EnchantmentMatrix.Piece p = (EnchantmentMatrix.Piece) this.pieces.get(i);
            for (int[] b : p.blocks) {
                this.matrix[p.x + b[0]][p.y + b[1]] = i;
            }
        }
    }

    public boolean canPlace(EnchantmentMatrix.Piece p, int x, int y) {
        for (int[] b : p.blocks) {
            int bx = b[0] + x;
            int by = b[1] + y;
            if (bx < 0 || by < 0 || bx >= 5 || by >= 5) {
                return false;
            }
            if (this.matrix[bx][by] != -1) {
                return false;
            }
        }
        return true;
    }

    private int[] packList(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (Integer) list.get(i);
        }
        return arr;
    }

    private List<Integer> unpackList(int[] arr) {
        List<Integer> list = new ArrayList(arr.length);
        for (int anArr : arr) {
            list.add(anArr);
        }
        return list;
    }

    public static int getMaxXP(Enchantment enchantment, int level) {
        if (level >= enchantment.getMaxLevel()) {
            return 0;
        } else {
            return switch(enchantment.getRarity()) {
                case COMMON ->
                    level;
                case UNCOMMON ->
                    level / 2 + 1;
                default ->
                    1;
            };
        }
    }

    public static int getValue(Enchantment enchantment, int level) {
        int total = 1;
        for (int i = 1; i < level; i++) {
            total += getMaxXP(enchantment, i);
        }
        return total;
    }

    private static class EnchantmentDataWrapper extends EnchantmentInstance {

        private boolean marked;

        private int influence;

        private final EnchantmentMatrix.MutableWeight mutableWeight = new EnchantmentMatrix.MutableWeight(this.f_44947_.getRarity().getWeight());

        public EnchantmentDataWrapper(Enchantment enchantmentObj, int enchLevel) {
            super(enchantmentObj, enchLevel);
        }

        public void normalizeRarity(Map<Enchantment, Integer> influences, List<EnchantmentMatrix.Piece> markedEnchants) {
            if (MatrixEnchantingModule.normalizeRarity) {
                switch(this.f_44947_.getRarity()) {
                    case COMMON:
                        this.mutableWeight.val = 80000;
                        break;
                    case UNCOMMON:
                        this.mutableWeight.val = 40000;
                        break;
                    case RARE:
                        this.mutableWeight.val = 25000;
                        break;
                    case VERY_RARE:
                        this.mutableWeight.val = 5000;
                }
                this.influence = Mth.clamp((Integer) influences.getOrDefault(this.f_44947_, 0), -MatrixEnchantingModule.influenceMax, MatrixEnchantingModule.influenceMax);
                float multiplier = 1.0F + (float) this.influence * (float) MatrixEnchantingModule.influencePower;
                this.mutableWeight.val = (int) ((float) this.mutableWeight.val * multiplier);
                boolean mark = true;
                for (EnchantmentMatrix.Piece other : markedEnchants) {
                    if (other.enchant != null) {
                        if (other.enchant == this.f_44947_) {
                            this.mutableWeight.val = (int) ((double) this.mutableWeight.val * MatrixEnchantingModule.dupeMultiplier);
                            mark = false;
                            break;
                        }
                        if (!other.enchant.isCompatibleWith(this.f_44947_) || !this.f_44947_.isCompatibleWith(other.enchant)) {
                            this.mutableWeight.val = (int) ((double) this.mutableWeight.val * MatrixEnchantingModule.incompatibleMultiplier);
                            mark = false;
                            break;
                        }
                    }
                }
                if (mark) {
                    this.marked = true;
                }
            }
        }

        @NotNull
        @Override
        public Weight getWeight() {
            return this.mutableWeight;
        }
    }

    private static class MutableWeight extends Weight {

        protected int val;

        public MutableWeight(int val) {
            super(val);
        }

        @Override
        public int asInt() {
            return this.val;
        }
    }

    public static class Piece {

        private static final int[][][] PIECE_TYPES = new int[][][] { { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { -1, 1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 1, -1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 0, -1 }, { -1, -1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } }, { { 0, 0 }, { -1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 } }, { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, { -1, -1 }, { 1, 1 } } };

        private static final String TAG_COLOR = "color";

        private static final String TAG_TYPE = "type";

        private static final String TAG_ENCHANTMENT = "enchant";

        private static final String TAG_LEVEL = "level";

        private static final String TAG_BLOCK_COUNT = "blockCount";

        private static final String TAG_BLOCK = "block";

        private static final String TAG_X = "x";

        private static final String TAG_Y = "y";

        private static final String TAG_XP = "xp";

        private static final String TAG_MARKED = "marked";

        private static final String TAG_INFLUENCE = "influence";

        public Enchantment enchant;

        public int level;

        public int color;

        public int type;

        public int x;

        public int y;

        public int xp;

        public int[][] blocks;

        public boolean marked;

        public int influence;

        public Piece() {
        }

        public Piece(EnchantmentMatrix.EnchantmentDataWrapper wrapper, int type) {
            this.enchant = wrapper.f_44947_;
            this.level = wrapper.f_44948_;
            this.marked = wrapper.marked;
            this.influence = wrapper.influence;
            this.type = type;
            Random rng = new Random((long) Objects.toString(BuiltInRegistries.ENCHANTMENT.getKey(this.enchant)).hashCode());
            float h = rng.nextFloat();
            float s = rng.nextFloat() * 0.2F + 0.8F;
            float b = rng.nextFloat() * 0.25F + 0.75F;
            this.color = Color.HSBtoRGB(h, s, b);
        }

        public void generateBlocks() {
            int type = (int) (Math.random() * (double) PIECE_TYPES.length);
            int[][] copyPieces = PIECE_TYPES[type];
            this.blocks = new int[copyPieces.length][2];
            for (int i = 0; i < this.blocks.length; i++) {
                this.blocks[i][0] = copyPieces[i][0];
                this.blocks[i][1] = copyPieces[i][1];
            }
            int rotations = (int) (Math.random() * 4.0);
            for (int i = 0; i < rotations; i++) {
                this.rotate();
            }
        }

        public void rotate() {
            for (int[] b : this.blocks) {
                int x = b[0];
                int y = b[1];
                b[0] = y;
                b[1] = -x;
            }
        }

        public int getMaxXP() {
            return EnchantmentMatrix.getMaxXP(this.enchant, this.level);
        }

        public int getValue() {
            return EnchantmentMatrix.getValue(this.enchant, this.level) + this.xp;
        }

        public void writeToNBT(CompoundTag cmp) {
            cmp.putInt("color", this.color);
            cmp.putInt("type", this.type);
            if (this.enchant != null) {
                cmp.putString("enchant", Objects.toString(BuiltInRegistries.ENCHANTMENT.getKey(this.enchant)));
            }
            cmp.putInt("level", this.level);
            cmp.putInt("x", this.x);
            cmp.putInt("y", this.y);
            cmp.putInt("xp", this.xp);
            cmp.putBoolean("marked", this.marked);
            cmp.putInt("influence", this.influence);
            cmp.putInt("blockCount", this.blocks.length);
            for (int i = 0; i < this.blocks.length; i++) {
                cmp.putIntArray("block" + i, this.blocks[i]);
            }
        }

        public void readFromNBT(CompoundTag cmp) {
            this.color = cmp.getInt("color");
            this.type = cmp.getInt("type");
            this.enchant = BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(cmp.getString("enchant")));
            this.level = cmp.getInt("level");
            this.x = cmp.getInt("x");
            this.y = cmp.getInt("y");
            this.xp = cmp.getInt("xp");
            this.marked = cmp.getBoolean("marked");
            this.influence = cmp.getInt("influence");
            this.blocks = new int[cmp.getInt("blockCount")][2];
            for (int i = 0; i < this.blocks.length; i++) {
                this.blocks[i] = cmp.getIntArray("block" + i);
            }
        }
    }
}