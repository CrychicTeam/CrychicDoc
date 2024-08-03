package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.core.LootEntry;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.util.LootContextUtils;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootContextJS {

    protected final LootContext context;

    private final ILootContextData data;

    private BlockContainerJS cachedBlockContainer;

    public LootContextJS(LootContext context) {
        this.context = context;
        this.data = context.getParamOrNull(LootJSParamSets.DATA);
        assert this.data != null;
    }

    public ResourceLocation getLootTableId() {
        return LootJSPlatform.INSTANCE.getQueriedLootTableId(this.context);
    }

    public LootContextType getType() {
        return this.data.getLootContextType();
    }

    public boolean isCanceled() {
        return this.data.isCanceled();
    }

    public void cancel() {
        this.data.setCanceled(true);
    }

    public Map<String, Object> getCustomData() {
        return this.data.getCustomData();
    }

    public Vec3 getPosition() {
        Vec3 vec = this.context.getParamOrNull(LootContextParams.ORIGIN);
        if (vec != null) {
            return vec;
        } else {
            Entity entity = this.context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (entity != null) {
                return entity.position();
            } else {
                ConsoleJS.SERVER.warn("No position found. This should not happen");
                return Vec3.ZERO;
            }
        }
    }

    public BlockPos getBlockPos() {
        Vec3 position = this.getPosition();
        return new BlockPos((int) position.x, (int) position.y, (int) position.z);
    }

    @Nullable
    public Entity getEntity() {
        return this.context.getParamOrNull(LootContextParams.THIS_ENTITY);
    }

    @Nullable
    public Entity getKillerEntity() {
        return this.context.getParamOrNull(LootContextParams.KILLER_ENTITY);
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return LootContextUtils.getPlayerOrNull(this.context);
    }

    @Nullable
    public DamageSource getDamageSource() {
        return this.context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
    }

    public ItemStack getTool() {
        ItemStack tool = this.context.getParamOrNull(LootContextParams.TOOL);
        return tool == null ? ItemStack.EMPTY : tool;
    }

    @Nullable
    public BlockContainerJS getDestroyedBlock() {
        if (this.cachedBlockContainer == null) {
            final BlockState blockStateInContext = this.context.getParamOrNull(LootContextParams.BLOCK_STATE);
            if (blockStateInContext == null) {
                return null;
            }
            BlockPos blockPos = this.getBlockPos();
            this.cachedBlockContainer = new BlockContainerJS(this.context.getLevel(), blockPos) {

                @Override
                public BlockState getBlockState() {
                    return blockStateInContext;
                }
            };
        }
        return this.cachedBlockContainer;
    }

    public boolean isExploded() {
        return this.context.hasParam(LootContextParams.EXPLOSION_RADIUS);
    }

    public float getExplosionRadius() {
        Float f = this.context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        return f != null ? f : 0.0F;
    }

    public ServerLevel getLevel() {
        return this.context.getLevel();
    }

    @Nullable
    public MinecraftServer getServer() {
        return this.getLevel().getServer();
    }

    public float getLuck() {
        return this.context.getLuck();
    }

    public int getLooting() {
        Entity killer = this.context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        return killer instanceof LivingEntity asLiving ? EnchantmentHelper.getMobLooting(asLiving) : 0;
    }

    public RandomSource getRandom() {
        return this.context.getRandom();
    }

    public LootContext getVanillaContext() {
        return this.context;
    }

    public int lootSize() {
        return this.data.getGeneratedLoot().size();
    }

    public void addLoot(LootEntry lootEntry) {
        ItemStack item = lootEntry.createItem(this.context);
        if (item != null) {
            this.data.getGeneratedLoot().add(item);
        }
    }

    public void removeLoot(ItemFilter itemFilter) {
        this.data.getGeneratedLoot().removeIf(itemFilter);
    }

    public List<ItemStack> findLoot(ItemFilter itemFilter) {
        return (List<ItemStack>) this.data.getGeneratedLoot().stream().filter(itemFilter).map(ItemStackJS::of).collect(Collectors.toList());
    }

    public List<ItemStack> getLoot() {
        return this.data.getGeneratedLoot();
    }

    public boolean hasLoot(ItemFilter ingredient) {
        return !this.findLoot(ingredient).isEmpty();
    }

    public void forEachLoot(Consumer<ItemStack> action) {
        this.data.getGeneratedLoot().forEach(itemStack -> action.accept(ItemStackJS.of(itemStack)));
    }
}