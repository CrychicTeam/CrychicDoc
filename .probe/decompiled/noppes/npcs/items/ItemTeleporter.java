package noppes.npcs.items;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;

public class ItemTeleporter extends Item {

    public ItemTeleporter() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!level.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        } else {
            CustomNpcs.proxy.openGui(player, EnumGuiType.NpcDimensions);
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        }
    }

    public boolean onEntitySwing(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.m_9236_().isClientSide) {
            return true;
        } else {
            float f = livingEntity.m_146909_();
            float f1 = livingEntity.m_146908_();
            Vec3 vector3d = livingEntity.m_20299_(1.0F);
            float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
            float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
            float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
            float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
            float f6 = f3 * f4;
            float f7 = f2 * f4;
            double d0 = 80.0;
            Vec3 vector3d1 = vector3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
            HitResult movingobjectposition = livingEntity.m_9236_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, livingEntity));
            if (movingobjectposition == null) {
                return true;
            } else {
                Vec3 vec32 = livingEntity.m_20252_(f);
                boolean flag = false;
                float f9 = 1.0F;
                List list = livingEntity.m_9236_().m_45933_(livingEntity, livingEntity.m_20191_().inflate(vec32.x * d0, vec32.y * d0, vec32.z * d0).inflate((double) f9, (double) f9, (double) f9));
                for (int i = 0; i < list.size(); i++) {
                    Entity entity = (Entity) list.get(i);
                    if (entity.canBeCollidedWith()) {
                        float f10 = entity.getPickRadius();
                        AABB axisalignedbb = entity.getBoundingBox().inflate((double) f10, (double) f10, (double) f10);
                        if (axisalignedbb.contains(vector3d)) {
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    return true;
                } else {
                    if (movingobjectposition.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) movingobjectposition).getBlockPos();
                        while (livingEntity.m_9236_().getBlockState(pos).m_60734_() != Blocks.AIR) {
                            pos = pos.above();
                        }
                        livingEntity.m_6021_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.0F), (double) ((float) pos.m_123343_() + 0.5F));
                    }
                    return true;
                }
            }
        }
    }
}