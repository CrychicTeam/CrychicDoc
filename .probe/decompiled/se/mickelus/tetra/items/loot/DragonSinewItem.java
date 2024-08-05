package se.mickelus.tetra.items.loot;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class DragonSinewItem extends TetraItem {

    public static final String identifier = "dragon_sinew";

    static final Component tooltip = Component.translatable("item.tetra.dragon_sinew.description").withStyle(ChatFormatting.GRAY);

    private static final ResourceLocation dragonLootTable = new ResourceLocation("entities/ender_dragon");

    private static final ResourceLocation sinewLootTable = new ResourceLocation("tetra", "entities/ender_dragon_extended");

    public DragonSinewItem() {
        super(new Item.Properties());
        MinecraftForge.EVENT_BUS.register(new DragonSinewItem.LootTableHandler());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(DragonSinewItem.tooltip);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    public Entity createEntity(Level world, Entity entity, ItemStack itemstack) {
        entity.setNoGravity(true);
        return null;
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.m_20256_(entity.m_20184_().scale(0.8F));
        if (entity.m_9236_().isClientSide && entity.getAge() % 20 == 0) {
            entity.m_9236_().addParticle(ParticleTypes.DRAGON_BREATH, entity.m_20208_(0.2), entity.m_20187_() + 0.2, entity.m_20262_(0.2), (double) (entity.m_9236_().getRandom().nextFloat() * 0.02F - 0.01F), (double) (-0.01F - entity.m_9236_().getRandom().nextFloat() * 0.01F), (double) (entity.m_9236_().getRandom().nextFloat() * 0.02F - 0.01F));
        }
        return false;
    }

    public static class LootTableHandler {

        @SubscribeEvent
        public void onLootTableLoad(LootTableLoadEvent event) {
            if (event.getName().equals(DragonSinewItem.dragonLootTable)) {
                event.getTable().addPool(LootPool.lootPool().name("tetra:dragon_sinew").add(LootTableReference.lootTableReference(DragonSinewItem.sinewLootTable)).build());
            }
        }
    }
}