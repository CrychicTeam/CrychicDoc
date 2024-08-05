package net.mehvahdjukaar.amendments.common.block;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.mehvahdjukaar.amendments.common.recipe.DummyContainer;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.events.behaviors.CauldronConversion;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

public class BoilingWaterCauldronBlock extends LayeredCauldronBlock {

    public static final BooleanProperty BOILING = ModBlockProperties.BOILING;

    public BoilingWaterCauldronBlock(BlockBehaviour.Properties properties, Predicate<Biome.Precipitation> fillPredicate, Map<Item, CauldronInteraction> interactions) {
        super(properties, fillPredicate, interactions);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BOILING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BOILING);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (!level.isClientSide && this.m_151979_(state, pos, entity)) {
            if ((Boolean) state.m_61143_(BOILING) && entity instanceof LivingEntity) {
                entity.hurt(new DamageSource(ModRegistry.BOILING_DAMAGE.getHolder()), 1.0F);
            }
            if (entity.isOnFire()) {
                LiquidCauldronBlock.playExtinguishSound(level, pos, entity);
            }
            this.attemptStewCrafting(state, level, pos, entity);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BlockState s = super.m_7417_(state, direction, neighborState, level, currentPos, neighborPos);
        if (direction == Direction.DOWN) {
            boolean isFire = LiquidCauldronBlock.shouldBoil(neighborState, SoftFluidStack.of(BuiltInSoftFluids.WATER.getHolder()));
            s = (BlockState) s.m_61124_(BOILING, isFire);
        }
        return s;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (this.m_151979_(state, pos, entity)) {
            if (level.isClientSide) {
                LiquidCauldronBlock.playSplashAnimation(level, pos, entity, this.m_142446_(state), getWaterColor(state, level, pos, 1), 0);
            }
            super.m_142072_(level, state, pos, entity, 0.0F);
        } else {
            super.m_142072_(level, state, pos, entity, fallDistance);
        }
    }

    public static int getWaterColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int i) {
        return i == 1 && level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.m_214162_(state, level, pos, random);
        if ((Boolean) state.m_61143_(BOILING)) {
            LiquidCauldronBlock.playBubblingAnimation(level, pos, this.m_142446_(state), random, getWaterColor(state, level, pos, 1), 0);
        }
    }

    private void attemptStewCrafting(BlockState state, Level level, BlockPos pos, Entity entity) {
        if ((Boolean) state.m_61143_(BOILING) && entity instanceof ItemEntity && entity.tickCount % 10 == 0) {
            List<ItemEntity> entities = level.m_45976_(ItemEntity.class, new AABB((double) pos.m_123341_() + 0.125, (double) ((float) pos.m_123342_() + 0.375F), (double) pos.m_123343_() + 0.125, (double) pos.m_123341_() + 0.875, (double) pos.m_123342_() + this.m_142446_(state), (double) pos.m_123343_() + 0.875));
            List<ItemStack> ingredients = new ArrayList();
            for (ItemEntity e : entities) {
                ItemStack i = e.getItem();
                for (int c = 0; c < i.getCount(); c++) {
                    ingredients.add(i.copyWithCount(1));
                }
            }
            ingredients.add(Items.BOWL.getDefaultInstance());
            CraftingContainer container = DummyContainer.of(ingredients);
            for (CraftingRecipe r : level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, container, level)) {
                ItemStack result = r.m_5874_(container, level.registryAccess());
                if (!result.isEmpty()) {
                    Pair<SoftFluidStack, FluidContainerList.Category> fluid = SoftFluidStack.fromItem(result);
                    if (fluid != null) {
                        BlockState newState = CauldronConversion.getNewState(pos, level, (SoftFluidStack) fluid.getFirst());
                        if (newState != null) {
                            level.setBlockAndUpdate(pos, newState);
                            if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
                                int lev = (Integer) state.m_61143_(f_153514_);
                                int newLev = lev == 3 ? 4 : lev;
                                te.getSoftFluidTank().setFluid(((SoftFluidStack) fluid.getFirst()).copyWithCount(newLev));
                                te.setChanged();
                                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.9F, 0.6F);
                            }
                            clearUsedIngredients(entities, ingredients);
                        }
                    }
                }
            }
        }
    }

    private static void clearUsedIngredients(List<ItemEntity> entities, List<ItemStack> ingredients) {
        for (ItemStack v : ingredients) {
            Iterator<ItemEntity> iter = entities.iterator();
            while (iter.hasNext()) {
                ItemEntity e = (ItemEntity) iter.next();
                ItemStack itemEntityItem = e.getItem();
                if (ItemStack.isSameItemSameTags(itemEntityItem, v)) {
                    itemEntityItem.shrink(1);
                    if (itemEntityItem.isEmpty()) {
                        e.m_146870_();
                        iter.remove();
                    }
                }
            }
        }
    }
}