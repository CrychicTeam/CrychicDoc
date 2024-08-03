package harmonised.pmmo.features.fireworks;

import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.TagBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireworkHandler {

    public static final String FIREWORK_SKILL = "firework_skill";

    public static final Perk FIREWORK = Perk.begin().addDefaults(TagBuilder.start().withString("firework_skill", "none").build()).setStart((player, nbt) -> {
        BlockPos pos = player.m_20183_();
        spawnRocket(player.m_9236_(), new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()), nbt.getString("firework_skill"));
        return new CompoundTag();
    }).setDescription(LangProvider.PERK_FIREWORK_DESC.asComponent()).setStatus((p, nbt) -> List.of(LangProvider.PERK_FIREWORK_STATUS_1.asComponent(Component.translatable("pmmo." + nbt.getString("skill")).withStyle(CoreUtils.getSkillStyle(nbt.getString("skill")))))).build();

    public static void spawnRocket(Level world, Vec3 pos, String skill) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag fw = new CompoundTag();
        ListTag explosion = new ListTag();
        CompoundTag l = new CompoundTag();
        int[] colors = new int[] { CoreUtils.getSkillColor(skill) };
        l.putInt("Flicker", 1);
        l.putInt("Trail", 0);
        l.putInt("Type", 1);
        l.put("Colors", new IntArrayTag(colors));
        explosion.add(l);
        fw.put("Explosions", explosion);
        fw.putInt("Flight", 0);
        nbt.put("Fireworks", fw);
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
        itemStack.setTag(nbt);
        PMMOFireworkEntity fireworkRocketEntity = new PMMOFireworkEntity(world, pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5, itemStack);
        world.m_7967_(fireworkRocketEntity);
    }
}