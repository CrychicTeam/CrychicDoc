package team.lodestar.lodestone.handlers;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.placementassistance.IPlacementAssistant;

public class PlacementAssistantHandler {

    public static final ArrayList<IPlacementAssistant> ASSISTANTS = new ArrayList();

    public static int animationTick = 0;

    public static BlockHitResult target;

    public static void registerPlacementAssistants(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> DataHelper.getAll(new ArrayList(ForgeRegistries.BLOCKS.getValues()), b -> b instanceof IPlacementAssistant).forEach(i -> {
            IPlacementAssistant assistant = (IPlacementAssistant) i;
            ASSISTANTS.add(assistant);
        }));
    }

    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.m_9236_();
        if (level.isClientSide) {
            for (Pair<IPlacementAssistant, ItemStack> pair : findAssistants(level, player, event.getHitVec())) {
                IPlacementAssistant assistant = (IPlacementAssistant) pair.getFirst();
                BlockState state = level.getBlockState(event.getPos());
                assistant.onPlaceBlock(player, level, event.getHitVec(), state, (ItemStack) pair.getSecond());
            }
            animationTick = Math.max(0, animationTick - 5);
        }
    }

    public static void tick(Player player, HitResult hitResult) {
        label32: {
            Level level = player.m_9236_();
            List<Pair<IPlacementAssistant, ItemStack>> placementAssistants = findAssistants(level, player, hitResult);
            if (hitResult instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
                target = blockHitResult;
                Iterator var5 = placementAssistants.iterator();
                while (true) {
                    if (!var5.hasNext()) {
                        break label32;
                    }
                    Pair<IPlacementAssistant, ItemStack> pair = (Pair<IPlacementAssistant, ItemStack>) var5.next();
                    IPlacementAssistant assistant = (IPlacementAssistant) pair.getFirst();
                    BlockState state = level.getBlockState(blockHitResult.getBlockPos());
                    assistant.onObserveBlock(player, level, blockHitResult, state, (ItemStack) pair.getSecond());
                }
            }
            target = null;
        }
        if (target == null) {
            if (animationTick > 0) {
                animationTick = Math.max(animationTick - 2, 0);
            }
        } else {
            if (animationTick < 10) {
                animationTick++;
            }
        }
    }

    private static List<Pair<IPlacementAssistant, ItemStack>> findAssistants(Level level, Player player, HitResult hitResult) {
        return !(hitResult instanceof BlockHitResult) ? Collections.emptyList() : findAssistants(level, player);
    }

    private static List<Pair<IPlacementAssistant, ItemStack>> findAssistants(Level level, Player player) {
        if (level != null && player != null && !player.m_6144_()) {
            List<Pair<IPlacementAssistant, ItemStack>> matchingAssistants = new ArrayList();
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack held = player.m_21120_(hand);
                matchingAssistants.addAll((Collection) ASSISTANTS.stream().filter(s -> s.canAssist().test(held)).map(a -> Pair.of(a, held)).collect(Collectors.toCollection(ArrayList::new)));
            }
            return matchingAssistants;
        } else {
            return Collections.emptyList();
        }
    }

    public static float getCurrentAlpha() {
        return Math.min((float) animationTick / 10.0F, 1.0F);
    }
}