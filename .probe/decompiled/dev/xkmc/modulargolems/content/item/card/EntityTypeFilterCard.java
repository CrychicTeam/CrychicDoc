package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class EntityTypeFilterCard extends ClickEntityFilterCard<EntityType<?>> {

    private static final String KEY = "typeList";

    public EntityTypeFilterCard(Item.Properties properties) {
        super(properties);
    }

    protected EntityType<?> getValue(LivingEntity entity) {
        return entity.m_6095_();
    }

    protected Component getName(EntityType<?> entityType) {
        return entityType.getDescription();
    }

    @Override
    protected List<EntityType<?>> getList(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        List<EntityType<?>> ans = new ArrayList();
        if (tag != null && tag.contains("typeList")) {
            for (Tag e : tag.getList("typeList", 8)) {
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(e.getAsString()));
                if (type != null) {
                    ans.add(type);
                }
            }
            return ans;
        } else {
            return ans;
        }
    }

    @Override
    protected void setList(ItemStack stack, List<EntityType<?>> list) {
        ListTag tag = ItemCompoundTag.of(stack).getSubList("typeList", 8).getOrCreate();
        tag.clear();
        for (EntityType<?> type : list) {
            ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
            if (id != null) {
                tag.add(StringTag.valueOf(id.toString()));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        List<EntityType<?>> types = this.getList(stack);
        if (types.size() > 0 && !Screen.hasShiftDown()) {
            for (EntityType<?> e : types) {
                list.add(this.getName(e));
            }
            list.add(MGLangData.TARGET_SHIFT.get());
        } else {
            list.add(MGLangData.TARGET_TYPE_ADD.get());
            list.add(MGLangData.TARGET_TYPE_REMOVE.get());
            list.add(MGLangData.TARGET_REMOVE.get());
        }
    }
}