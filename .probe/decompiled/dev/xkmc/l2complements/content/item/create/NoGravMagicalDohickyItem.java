package dev.xkmc.l2complements.content.item.create;

import dev.xkmc.l2complements.content.item.misc.TooltipItem;
import dev.xkmc.l2complements.init.data.LangData;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NoGravMagicalDohickyItem extends TooltipItem {

    public NoGravMagicalDohickyItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties, sup);
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level world = entity.m_9236_();
        Vec3 pos = entity.m_20182_();
        CompoundTag persistentData = entity.getPersistentData();
        if (world.isClientSide) {
            if (world.random.nextFloat() < this.getIdleParticleChance(entity)) {
                Vec3 ppos = offsetRandomly(pos, world.random, 0.5F);
                world.addParticle(ParticleTypes.END_ROD, ppos.x, pos.y, ppos.z, 0.0, -0.1F, 0.0);
            }
            if (entity.m_20067_() && !persistentData.getBoolean("PlayEffects")) {
                Vec3 basemotion = new Vec3(0.0, 1.0, 0.0);
                world.addParticle(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                for (int i = 0; i < 20; i++) {
                    Vec3 motion = offsetRandomly(basemotion, world.random, 1.0F);
                    world.addParticle(ParticleTypes.WITCH, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                    world.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                }
                persistentData.putBoolean("PlayEffects", true);
            }
            return false;
        } else {
            entity.m_146915_(true);
            entity.m_20242_(true);
            if (!persistentData.contains("JustCreated")) {
                return false;
            } else {
                this.onCreated(entity, persistentData);
                return false;
            }
        }
    }

    protected float getIdleParticleChance(ItemEntity entity) {
        return (float) Mth.clamp(entity.getItem().getCount() - 10, 5, 100) / 64.0F;
    }

    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        entity.lifespan = 6000;
        persistentData.remove("JustCreated");
        entity.m_20225_(true);
    }

    public static Vec3 offsetRandomly(Vec3 vec, RandomSource r, float radius) {
        return new Vec3(vec.x + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius), vec.y + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius), vec.z + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(LangData.IDS.FLOAT.get().withStyle(ChatFormatting.GRAY));
    }
}