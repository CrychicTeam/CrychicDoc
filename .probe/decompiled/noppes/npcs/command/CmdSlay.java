package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdSlay {

    static Map<String, Class<?>> slayMap = new LinkedHashMap();

    private static Map<String, Class<?>> getSlay(Level level) {
        if (!slayMap.isEmpty()) {
            return slayMap;
        } else {
            slayMap.put("all", LivingEntity.class);
            slayMap.put("mobs", Monster.class);
            slayMap.put("animals", Animal.class);
            slayMap.put("items", ItemEntity.class);
            slayMap.put("xporbs", ExperienceOrb.class);
            slayMap.put("npcs", EntityNPCInterface.class);
            for (ResourceLocation resource : ForgeRegistries.ENTITY_TYPES.getKeys()) {
                EntityType<?> ent = ForgeRegistries.ENTITY_TYPES.getValue(resource);
                if (ent.getCategory() != MobCategory.MISC) {
                    String name = ent.getDescriptionId();
                    try {
                        Entity e = ent.create(level);
                        e.remove(Entity.RemovalReason.DISCARDED);
                        Class<? extends Entity> cls = e.getClass();
                        if (!EntityNPCInterface.class.isAssignableFrom(cls) && LivingEntity.class.isAssignableFrom(cls)) {
                            slayMap.put(name.toLowerCase(), cls);
                        }
                    } catch (Throwable var7) {
                    }
                }
            }
            slayMap.remove("monster");
            slayMap.remove("mob");
            return slayMap;
        }
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("slay").requires(source -> source.hasPermission(4))).then(Commands.argument("type", StringArgumentType.word()).then(Commands.argument("range", IntegerArgumentType.integer(1)).executes(context -> {
            ArrayList<Class<?>> toDelete = new ArrayList();
            boolean deleteNPCs = false;
            String delete = StringArgumentType.getString(context, "type");
            Class<?> cls = (Class<?>) getSlay(((CommandSourceStack) context.getSource()).getLevel()).get(delete);
            if (cls != null) {
                toDelete.add(cls);
            }
            if (delete.equals("mobs")) {
                toDelete.add(Ghast.class);
                toDelete.add(EnderDragon.class);
            }
            if (delete.equals("npcs")) {
                deleteNPCs = true;
            }
            int count = 0;
            int range = IntegerArgumentType.getInteger(context, "range");
            AABB box = new AABB(((CommandSourceStack) context.getSource()).getPosition(), ((CommandSourceStack) context.getSource()).getPosition().add(1.0, 1.0, 1.0)).inflate((double) range, (double) range, (double) range);
            for (Entity entity : ((CommandSourceStack) context.getSource()).getLevel().m_45976_(LivingEntity.class, box)) {
                if (!(entity instanceof Player) && (!(entity instanceof TamableAnimal) || !((TamableAnimal) entity).isTame()) && (!(entity instanceof EntityNPCInterface) || deleteNPCs) && delete(entity, toDelete)) {
                    count++;
                }
            }
            if (toDelete.contains(ExperienceOrb.class)) {
                for (Entity entityx : ((CommandSourceStack) context.getSource()).getLevel().m_45976_(ExperienceOrb.class, box)) {
                    entityx.setRemoved(Entity.RemovalReason.DISCARDED);
                    count++;
                }
            }
            if (toDelete.contains(ItemEntity.class)) {
                for (Entity entityx : ((CommandSourceStack) context.getSource()).getLevel().m_45976_(ItemEntity.class, box)) {
                    entityx.setRemoved(Entity.RemovalReason.DISCARDED);
                    count++;
                }
            }
            int finalCount = count;
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable(finalCount + " entities deleted"), false);
            return 1;
        })));
    }

    private static boolean delete(Entity entity, ArrayList<Class<?>> toDelete) {
        for (Class<?> delete : toDelete) {
            if ((delete != Animal.class || !(entity instanceof Horse)) && delete.isAssignableFrom(entity.getClass())) {
                entity.setRemoved(Entity.RemovalReason.DISCARDED);
                return true;
            }
        }
        return false;
    }
}