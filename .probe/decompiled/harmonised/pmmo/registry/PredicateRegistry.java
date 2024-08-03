package harmonised.pmmo.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.RegistryUtil;
import java.util.function.BiPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PredicateRegistry {

    private LinkedListMultimap<String, BiPredicate<Player, ItemStack>> reqPredicates = LinkedListMultimap.create();

    private LinkedListMultimap<String, BiPredicate<Player, BlockEntity>> reqBreakPredicates = LinkedListMultimap.create();

    private LinkedListMultimap<String, BiPredicate<Player, Entity>> reqEntityPredicates = LinkedListMultimap.create();

    public void registerPredicate(ResourceLocation res, ReqType reqType, BiPredicate<Player, ItemStack> pred) {
        Preconditions.checkNotNull(pred);
        String condition = reqType.toString() + ";" + res.toString();
        this.reqPredicates.get(condition).add(pred);
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Predicate Registered: " + condition);
    }

    public void registerBreakPredicate(ResourceLocation res, ReqType reqType, BiPredicate<Player, BlockEntity> pred) {
        Preconditions.checkNotNull(pred);
        String condition = reqType.toString() + ";" + res.toString();
        this.reqBreakPredicates.get(condition).add(pred);
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Predicate Registered: " + condition);
    }

    public void registerEntityPredicate(ResourceLocation res, ReqType type, BiPredicate<Player, Entity> pred) {
        Preconditions.checkNotNull(pred);
        String condition = type.toString() + ";" + res.toString();
        this.reqEntityPredicates.get(condition).add(pred);
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Entity Predicate Regsitered: " + condition);
    }

    public boolean predicateExists(ResourceLocation res, ReqType type) {
        String key = type.toString() + ";" + res.toString();
        return this.reqPredicates.containsKey(key) || this.reqBreakPredicates.containsKey(key) || this.reqEntityPredicates.containsKey(key);
    }

    public boolean checkPredicateReq(Player player, ItemStack stack, ReqType jType) {
        if (!this.predicateExists(RegistryUtil.getId(stack), jType)) {
            return false;
        } else {
            for (BiPredicate<Player, ItemStack> pred : this.reqPredicates.get(jType.toString() + ";" + RegistryUtil.getId(stack).toString())) {
                if (!pred.test(player, stack)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean checkPredicateReq(Player player, BlockEntity tile, ReqType jType) {
        ResourceLocation res = RegistryUtil.getId(tile.getBlockState());
        if (!this.predicateExists(res, jType)) {
            return false;
        } else {
            for (BiPredicate<Player, BlockEntity> pred : this.reqBreakPredicates.get(jType.toString() + ";" + res.toString())) {
                if (!pred.test(player, tile)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean checkPredicateReq(Player player, Entity entity, ReqType type) {
        ResourceLocation res = RegistryUtil.getId(entity);
        if (!this.predicateExists(res, type)) {
            return false;
        } else {
            for (BiPredicate<Player, Entity> pred : this.reqEntityPredicates.get(type.toString() + ";" + res.toString())) {
                if (!pred.test(player, entity)) {
                    return false;
                }
            }
            return true;
        }
    }
}