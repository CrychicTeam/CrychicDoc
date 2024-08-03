package org.violetmoon.quark.addons.oddities.block.be;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.inventory.EnchantmentMatrix;
import org.violetmoon.quark.addons.oddities.inventory.MatrixEnchantingMenu;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.addons.oddities.util.Influence;
import org.violetmoon.quark.api.IEnchantmentInfluencer;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class MatrixEnchantingTableBlockEntity extends AbstractEnchantingTableBlockEntity implements MenuProvider {

    public static final List<Block> CANDLES = Lists.newArrayList(new Block[] { Blocks.WHITE_CANDLE, Blocks.ORANGE_CANDLE, Blocks.MAGENTA_CANDLE, Blocks.LIGHT_BLUE_CANDLE, Blocks.YELLOW_CANDLE, Blocks.LIME_CANDLE, Blocks.PINK_CANDLE, Blocks.GRAY_CANDLE, Blocks.LIGHT_GRAY_CANDLE, Blocks.CYAN_CANDLE, Blocks.PURPLE_CANDLE, Blocks.BLUE_CANDLE, Blocks.BROWN_CANDLE, Blocks.GREEN_CANDLE, Blocks.RED_CANDLE, Blocks.BLACK_CANDLE });

    public static final int OPER_ADD = 0;

    public static final int OPER_PLACE = 1;

    public static final int OPER_REMOVE = 2;

    public static final int OPER_ROTATE = 3;

    public static final int OPER_MERGE = 4;

    public static final String TAG_STACK_MATRIX = "quark:enchantingMatrix";

    private static final String TAG_MATRIX = "matrix";

    private static final String TAG_MATRIX_UUID_LESS = "uuidLess";

    private static final String TAG_MATRIX_UUID_MOST = "uuidMost";

    private static final String TAG_CHARGE = "charge";

    public EnchantmentMatrix matrix;

    private boolean matrixDirty = false;

    public boolean clientMatrixDirty = false;

    private UUID matrixId;

    public final Map<Enchantment, Integer> influences = new HashMap();

    public int bookshelfPower;

    public int enchantability;

    public int charge;

    public MatrixEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
        super(MatrixEnchantingModule.blockEntityType, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MatrixEnchantingTableBlockEntity be) {
        be.tick();
    }

    @Override
    public void tick() {
        super.tick();
        ItemStack item = this.m_8020_(0);
        if (item.isEmpty()) {
            if (this.matrix != null) {
                this.matrixDirty = true;
                this.matrix = null;
            }
        } else {
            this.loadMatrix(item);
            if (this.f_58857_.getGameTime() % 20L == 0L || this.matrixDirty) {
                this.updateEnchantPower();
            }
        }
        if (this.charge <= 0 && !this.f_58857_.isClientSide) {
            ItemStack lapis = this.m_8020_(1);
            if (!lapis.isEmpty()) {
                lapis.shrink(1);
                this.charge = this.charge + MatrixEnchantingModule.chargePerLapis;
                this.sync();
            }
        }
        if (this.matrixDirty) {
            this.makeOutput();
            this.matrixDirty = false;
        }
    }

    public void onOperation(Player player, int operation, int arg0, int arg1, int arg2) {
        if (this.matrix != null) {
            switch(operation) {
                case 0:
                    this.apply(m -> this.generateAndPay(m, player));
                    break;
                case 1:
                    this.apply(m -> m.place(arg0, arg1, arg2));
                    break;
                case 2:
                    this.apply(m -> m.remove(arg0));
                    break;
                case 3:
                    this.apply(m -> m.rotate(arg0));
                    break;
                case 4:
                    this.apply(m -> m.merge(arg0, arg1));
            }
        }
    }

    public boolean isMatrixInfluenced() {
        return this.matrix.isInfluenced();
    }

    private void apply(Predicate<EnchantmentMatrix> oper) {
        if (oper.test(this.matrix)) {
            ItemStack item = this.m_8020_(0);
            this.commitMatrix(item);
        }
    }

    private boolean generateAndPay(EnchantmentMatrix matrix, Player player) {
        if (matrix.canGeneratePiece(this.influences, this.bookshelfPower, this.enchantability) && matrix.validateXp(player, this.bookshelfPower)) {
            boolean creative = player.getAbilities().instabuild;
            int cost = matrix.getNewPiecePrice();
            if ((this.charge > 0 || creative) && matrix.generatePiece(this.influences, this.bookshelfPower, this.m_8020_(0).is(Items.BOOK), false) && !creative) {
                player.giveExperienceLevels(-cost);
                this.charge = Math.max(this.charge - 1, 0);
            }
        }
        return true;
    }

    private void makeOutput() {
        if (!this.f_58857_.isClientSide) {
            this.m_6836_(2, ItemStack.EMPTY);
            ItemStack in = this.m_8020_(0);
            if (!in.isEmpty() && this.matrix != null && !this.matrix.placedPieces.isEmpty()) {
                ItemStack out = in.copy();
                boolean book = false;
                if (out.getItem() == Items.BOOK) {
                    out = new ItemStack(Items.ENCHANTED_BOOK);
                    book = true;
                }
                Map<Enchantment, Integer> enchantments = new HashMap();
                for (int i : this.matrix.placedPieces) {
                    EnchantmentMatrix.Piece p = (EnchantmentMatrix.Piece) this.matrix.pieces.get(i);
                    if (p != null && p.enchant != null) {
                        for (Enchantment o : enchantments.keySet()) {
                            if (o == p.enchant || !p.enchant.isCompatibleWith(o) || !o.isCompatibleWith(p.enchant)) {
                                return;
                            }
                        }
                        enchantments.put(p.enchant, p.level);
                    }
                }
                if (book) {
                    for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
                        EnchantedBookItem.addEnchantment(out, new EnchantmentInstance((Enchantment) e.getKey(), (Integer) e.getValue()));
                    }
                } else {
                    EnchantmentHelper.setEnchantments(enchantments, out);
                    out.removeTagKey("quark:enchantingMatrix");
                }
                this.m_6836_(2, out);
            }
        }
    }

    private void loadMatrix(ItemStack stack) {
        if (this.matrix == null || this.matrix.target != stack) {
            if (this.matrix != null) {
                this.matrixDirty = true;
            }
            this.matrix = null;
            if (stack.isEnchantable()) {
                this.matrix = new EnchantmentMatrix(stack, this.f_58857_.random);
                this.matrixDirty = true;
                this.makeUUID();
                if (ItemNBTHelper.verifyExistence(stack, "quark:enchantingMatrix")) {
                    CompoundTag cmp = ItemNBTHelper.getCompound(stack, "quark:enchantingMatrix", true);
                    if (cmp != null) {
                        this.matrix.readFromNBT(cmp);
                    }
                }
            }
        }
    }

    private void commitMatrix(ItemStack stack) {
        if (!this.f_58857_.isClientSide) {
            CompoundTag cmp = new CompoundTag();
            this.matrix.writeToNBT(cmp);
            ItemNBTHelper.setCompound(stack, "quark:enchantingMatrix", cmp);
            this.matrixDirty = true;
            this.makeUUID();
            this.sync();
            this.m_6596_();
        }
    }

    private void makeUUID() {
        if (!this.f_58857_.isClientSide) {
            this.matrixId = UUID.randomUUID();
        }
    }

    public void updateEnchantPower() {
        ItemStack item = this.m_8020_(0);
        this.influences.clear();
        if (!item.isEmpty()) {
            this.enchantability = Quark.ZETA.itemExtensions.get(item).getEnchantmentValueZeta(item);
        }
        boolean allowWater = MatrixEnchantingModule.allowUnderwaterEnchanting;
        boolean allowShort = MatrixEnchantingModule.allowShortBlockEnchanting;
        float power = 0.0F;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (this.isAirGap(j, k, allowWater, allowShort)) {
                    power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k * 2, 0, j * 2));
                    power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k * 2, 1, j * 2));
                    if (k != 0 && j != 0) {
                        power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k * 2, 0, j));
                        power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k * 2, 1, j));
                        power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k, 0, j * 2));
                        power += this.getEnchantPowerAt(this.f_58857_, this.f_58858_.offset(k, 1, j * 2));
                    }
                }
            }
        }
        this.bookshelfPower = Math.min((int) power, MatrixEnchantingModule.maxBookshelves);
    }

    private boolean isAirGap(int j, int k, boolean allowWater, boolean allowShortBlock) {
        if (j == 0 && k == 0) {
            return false;
        } else {
            BlockPos test = this.f_58858_.offset(k, 0, j);
            BlockPos testUp = test.above();
            return (this.f_58857_.m_46859_(test) || allowWater && this.f_58857_.getBlockState(test).m_60734_() == Blocks.WATER || allowShortBlock && isShortBlock(this.f_58857_, test)) && (this.f_58857_.m_46859_(testUp) || allowWater && this.f_58857_.getBlockState(testUp).m_60734_() == Blocks.WATER || allowShortBlock && isShortBlock(this.f_58857_, testUp));
        }
    }

    public static boolean isShortBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.m_60734_();
        VoxelShape shape = block.m_5940_(state, level, pos, CollisionContext.empty());
        if (shape.isEmpty()) {
            return true;
        } else {
            AABB bounds = shape.bounds();
            float f = 0.1875F;
            return bounds.minY == 0.0 && bounds.maxY <= (double) f || bounds.maxY == 1.0 && bounds.minY >= (double) (1.0F - f);
        }
    }

    private float getEnchantPowerAt(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (MatrixEnchantingModule.allowInfluencing) {
            IEnchantmentInfluencer influencer = getInfluencerFromBlock(state, world, pos);
            if (influencer != null) {
                int count = influencer.getInfluenceStack(world, pos, state);
                List<Enchantment> influencedEnchants = BuiltInRegistries.ENCHANTMENT.stream().filter(it -> influencer.influencesEnchantment(world, pos, state, it)).toList();
                List<Enchantment> dampenedEnchants = BuiltInRegistries.ENCHANTMENT.stream().filter(it -> influencer.dampensEnchantment(world, pos, state, it)).toList();
                if (!influencedEnchants.isEmpty() || !dampenedEnchants.isEmpty()) {
                    for (Enchantment e : influencedEnchants) {
                        int curr = (Integer) this.influences.getOrDefault(e, 0);
                        this.influences.put(e, curr + count);
                    }
                    for (Enchantment e : dampenedEnchants) {
                        int curr = (Integer) this.influences.getOrDefault(e, 0);
                        this.influences.put(e, curr - count);
                    }
                    return 1.0F;
                }
            }
        }
        return state.getEnchantPowerBonus(world, pos);
    }

    @Override
    public void writeSharedNBT(CompoundTag cmp) {
        super.writeSharedNBT(cmp);
        CompoundTag matrixCmp = new CompoundTag();
        if (this.matrix != null) {
            this.matrix.writeToNBT(matrixCmp);
            cmp.put("matrix", matrixCmp);
            if (this.matrixId != null) {
                cmp.putLong("uuidLess", this.matrixId.getLeastSignificantBits());
                cmp.putLong("uuidMost", this.matrixId.getMostSignificantBits());
            }
        }
        cmp.putInt("charge", this.charge);
    }

    @Override
    public void readSharedNBT(CompoundTag cmp) {
        super.readSharedNBT(cmp);
        if (cmp.contains("matrix")) {
            long least = cmp.getLong("uuidLess");
            long most = cmp.getLong("uuidMost");
            UUID newId = new UUID(most, least);
            if (!newId.equals(this.matrixId)) {
                CompoundTag matrixCmp = cmp.getCompound("matrix");
                this.matrixId = newId;
                this.matrix = new EnchantmentMatrix(this.m_8020_(0), RandomSource.create());
                this.matrix.readFromNBT(matrixCmp);
            }
            this.clientMatrixDirty = true;
        } else {
            this.matrix = null;
        }
        this.charge = cmp.getInt("charge");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new MatrixEnchantingMenu(id, inv, this);
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return this.m_7755_();
    }

    @Nullable
    public static IEnchantmentInfluencer getInfluencerFromBlock(BlockState state, Level world, BlockPos pos) {
        Block var4 = state.m_60734_();
        if (var4 instanceof IEnchantmentInfluencer) {
            return (IEnchantmentInfluencer) var4;
        } else {
            return (IEnchantmentInfluencer) (MatrixEnchantingModule.customInfluences.containsKey(state) ? (IEnchantmentInfluencer) MatrixEnchantingModule.customInfluences.get(state) : MatrixEnchantingTableBlockEntity.CandleInfluencer.forBlock(state.m_60734_(), world, pos));
        }
    }

    private static record CandleInfluencer(boolean inverted) implements IEnchantmentInfluencer {

        private static final MatrixEnchantingTableBlockEntity.CandleInfluencer INSTANCE = new MatrixEnchantingTableBlockEntity.CandleInfluencer(false);

        private static final MatrixEnchantingTableBlockEntity.CandleInfluencer INVERTED_INSTANCE = new MatrixEnchantingTableBlockEntity.CandleInfluencer(true);

        @Nullable
        public static MatrixEnchantingTableBlockEntity.CandleInfluencer forBlock(Block block, Level world, BlockPos pos) {
            if (MatrixEnchantingModule.candleInfluencingFailed) {
                return null;
            } else if (MatrixEnchantingTableBlockEntity.CANDLES.contains(block)) {
                if (MatrixEnchantingModule.soulCandlesInvert) {
                    BlockPos posBelow = pos.below();
                    BlockState below = world.getBlockState(posBelow);
                    if (below.m_204336_(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
                        return INVERTED_INSTANCE;
                    }
                    if (below.getEnchantPowerBonus(world, posBelow) > 0.0F) {
                        posBelow = posBelow.below();
                        below = world.getBlockState(posBelow);
                        if (below.m_204336_(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
                            return INVERTED_INSTANCE;
                        }
                    }
                }
                return INSTANCE;
            } else {
                return null;
            }
        }

        private DyeColor getColor(BlockState state) {
            if (!(Boolean) state.m_61143_(CandleBlock.LIT)) {
                return null;
            } else {
                int index = MatrixEnchantingTableBlockEntity.CANDLES.indexOf(state.m_60734_());
                return index >= 0 ? DyeColor.values()[index] : null;
            }
        }

        @Override
        public float[] getEnchantmentInfluenceColor(BlockGetter world, BlockPos pos, BlockState state) {
            DyeColor color = this.getColor(state);
            return color == null ? null : color.getTextureDiffuseColors();
        }

        @Nullable
        @Override
        public ParticleOptions getExtraParticleOptions(BlockGetter world, BlockPos pos, BlockState state) {
            return this.inverted && state.m_61143_(CandleBlock.LIT) ? ParticleTypes.SOUL : null;
        }

        @Override
        public double getExtraParticleChance(BlockGetter world, BlockPos pos, BlockState state) {
            return 0.25;
        }

        @Override
        public int getInfluenceStack(BlockGetter world, BlockPos pos, BlockState state) {
            return state.m_61143_(CandleBlock.LIT) ? (Integer) state.m_61143_(CandleBlock.CANDLES) : 0;
        }

        @Override
        public boolean influencesEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
            DyeColor color = this.getColor(state);
            if (color == null) {
                return false;
            } else {
                Influence influence = (Influence) MatrixEnchantingModule.candleInfluences.get(color);
                List<Enchantment> boosts = this.inverted ? influence.dampen() : influence.boost();
                return boosts.contains(enchantment);
            }
        }

        @Override
        public boolean dampensEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
            DyeColor color = this.getColor(state);
            if (color == null) {
                return false;
            } else {
                Influence influence = (Influence) MatrixEnchantingModule.candleInfluences.get(color);
                List<Enchantment> dampens = this.inverted ? influence.boost() : influence.dampen();
                return dampens.contains(enchantment);
            }
        }
    }
}