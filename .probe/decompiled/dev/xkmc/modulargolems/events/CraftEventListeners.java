package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "modulargolems", bus = Bus.FORGE)
public class CraftEventListeners {

    @SubscribeEvent
    public static void onAnvilCraft(AnvilUpdateEvent event) {
        ItemStack stack = event.getLeft();
        ItemStack block = event.getRight();
        if (stack.getItem() instanceof GolemPart<?, ?> part && part.count <= block.getCount()) {
            Optional<ResourceLocation> mat = GolemMaterial.getMaterial(block);
            if (mat.isPresent()) {
                ItemStack new_stack = stack.copy();
                GolemPart.setMaterial(new_stack, (ResourceLocation) mat.get());
                event.setOutput(new_stack);
                event.setMaterialCost(part.count);
                event.setCost(1);
            }
        }
        if (stack.getItem() instanceof GolemHolder<?, ?> holder) {
            if (block.getItem() instanceof UpgradeItem upgrade) {
                appendUpgrade(event, holder, upgrade);
            } else {
                fixGolem(event, holder, stack);
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilFinish(AnvilRepairEvent event) {
        if (!event.getEntity().m_9236_().isClientSide()) {
            ItemStack stack = event.getLeft();
            ItemStack block = event.getRight();
            if (stack.getItem() instanceof GolemPart<?, ?> part && part.count <= block.getCount()) {
                Optional<ResourceLocation> mat = GolemMaterial.getMaterial(block);
                mat.ifPresent(rl -> GolemTriggers.PART_CRAFT.trigger((ServerPlayer) event.getEntity(), rl));
            }
            if (stack.getItem() instanceof GolemHolder<?, ?> holder) {
                if (block.getItem() instanceof UpgradeItem) {
                    ItemStack result = event.getOutput();
                    ArrayList<GolemMaterial> mats = GolemHolder.getMaterial(result);
                    ArrayList<UpgradeItem> upgrades = GolemHolder.getUpgrades(result);
                    int remaining = holder.getRemaining(mats, upgrades);
                    int total = upgrades.size();
                    GolemTriggers.UPGRADE_APPLY.trigger((ServerPlayer) event.getEntity(), block, remaining, total);
                } else {
                    ArrayList<GolemMaterial> mats = GolemHolder.getMaterial(stack);
                    GolemType<? extends AbstractGolemEntity<?, ? extends IGolemPart<?>>, ? extends IGolemPart<?>> type = (GolemType<? extends AbstractGolemEntity<?, ? extends IGolemPart<?>>, ? extends IGolemPart<?>>) holder.getEntityType();
                    IGolemPart<?> part = type.getBodyPart();
                    if (mats.size() <= part.ordinal()) {
                        return;
                    }
                    GolemMaterial mat = (GolemMaterial) mats.get(part.ordinal());
                    Ingredient ing = (Ingredient) GolemMaterialConfig.get().ingredients.get(mat.id());
                    if (ing == null || !ing.test(block)) {
                        return;
                    }
                    GolemTriggers.ANVIL_FIX.trigger((ServerPlayer) event.getEntity(), mat.id());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGrindStone(GrindstoneEvent.OnPlaceItem event) {
        if (event.getTopItem().getItem() instanceof GolemHolder) {
            ItemStack copy = event.getTopItem().copy();
            if (!GolemHolder.getUpgrades(copy).isEmpty()) {
                copy.getOrCreateTag().remove("golem_upgrades");
                event.setOutput(copy);
                event.setXp(0);
            }
        }
    }

    private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void fixGolem(AnvilUpdateEvent event, GolemHolder<T, P> holder, ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("golem_entity")) {
            float max = GolemHolder.getMaxHealth(stack);
            float health = GolemHolder.getHealth(stack);
            if (!(health >= max)) {
                ArrayList<GolemMaterial> mats = GolemHolder.getMaterial(stack);
                GolemType<T, P> type = holder.getEntityType();
                P part = type.getBodyPart();
                if (mats.size() > part.ordinal()) {
                    GolemMaterial mat = (GolemMaterial) mats.get(part.ordinal());
                    Ingredient ing = (Ingredient) GolemMaterialConfig.get().ingredients.get(mat.id());
                    ItemStack repairStack = event.getRight();
                    if (ing != null && ing.test(repairStack)) {
                        int maxFix = Math.min(repairStack.getCount(), (int) Math.ceil((double) ((max - health) / max * 4.0F)));
                        event.setMaterialCost(maxFix);
                        event.setCost(maxFix);
                        ItemStack result = stack.copy();
                        GolemHolder.setHealth(result, Math.min(max, health + max / 4.0F * (float) maxFix));
                        event.setOutput(result);
                    }
                }
            }
        }
    }

    private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void appendUpgrade(AnvilUpdateEvent event, GolemHolder<T, P> holder, UpgradeItem upgrade) {
        ItemStack stack = event.getLeft();
        ArrayList<UpgradeItem> upgrades = GolemHolder.getUpgrades(stack);
        ItemStack result = appendUpgrade(stack, holder, upgrade);
        if (!result.isEmpty()) {
            event.setOutput(result);
            event.setCost(Math.min(39, 4 * (1 + upgrades.size())));
            event.setMaterialCost(1);
        }
    }

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> ItemStack appendUpgrade(ItemStack stack, GolemHolder<T, P> holder, UpgradeItem upgrade) {
        if (!upgrade.fitsOn(holder.getEntityType())) {
            return ItemStack.EMPTY;
        } else {
            ArrayList<GolemMaterial> mats = GolemHolder.getMaterial(stack);
            ArrayList<UpgradeItem> upgrades = GolemHolder.getUpgrades(stack);
            ArrayList<UpgradeItem> copy = new ArrayList(upgrades);
            copy.add(upgrade);
            int remaining = holder.getRemaining(mats, copy);
            if (remaining < 0) {
                return ItemStack.EMPTY;
            } else {
                HashMap<GolemModifier, Integer> map = GolemMaterial.collectModifiers(GolemHolder.getMaterial(stack), upgrades);
                for (ModifierInstance e : upgrade.get()) {
                    if ((Integer) map.getOrDefault(e.mod(), 0) >= e.mod().maxLevel) {
                        return ItemStack.EMPTY;
                    }
                }
                ItemStack result = stack.copy();
                GolemHolder.addUpgrade(result, upgrade);
                return result;
            }
        }
    }
}