package net.mehvahdjukaar.supplementaries.common.utils;

import com.google.common.base.Suppliers;
import java.util.Calendar;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.TetraCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MiscUtils {

    public static final MiscUtils.Festivity FESTIVITY = MiscUtils.Festivity.get();

    private static final Supplier<ShulkerBoxBlockEntity> SHULKER_TILE = Suppliers.memoize(() -> new ShulkerBoxBlockEntity(BlockPos.ZERO, Blocks.SHULKER_BOX.defaultBlockState()));

    public static boolean showsHints(BlockGetter worldIn, TooltipFlag flagIn) {
        if (worldIn instanceof Level l && l.isClientSide) {
            return (Boolean) ClientConfigs.General.TOOLTIP_HINTS.get();
        }
        return false;
    }

    public static boolean isSword(Item i) {
        if (i.builtInRegistryHolder().is(ModTags.STATUE_SWORDS)) {
            return true;
        } else {
            return CompatHandler.TETRA && TetraCompat.isTetraSword(i) ? true : i instanceof SwordItem;
        }
    }

    public static boolean isTool(Item i) {
        if (i.builtInRegistryHolder().is(ModTags.STATUE_TOOLS)) {
            return true;
        } else {
            return CompatHandler.TETRA && TetraCompat.isTetraTool(i) ? true : i instanceof DiggerItem || i instanceof TridentItem;
        }
    }

    public static AABB getDirectionBB(BlockPos pos, Direction facing, int offset) {
        BlockPos endPos = pos.relative(facing, offset);
        switch(facing) {
            case NORTH:
                endPos = endPos.offset(1, 1, 0);
                break;
            case SOUTH:
                endPos = endPos.offset(1, 1, 1);
                pos = pos.offset(0, 0, 1);
                break;
            case UP:
                endPos = endPos.offset(1, 1, 1);
                pos = pos.offset(0, 1, 0);
                break;
            case EAST:
                endPos = endPos.offset(1, 1, 1);
                pos = pos.offset(1, 0, 0);
                break;
            case WEST:
                endPos = endPos.offset(0, 1, 1);
                break;
            case DOWN:
                endPos = endPos.offset(1, 0, 1);
        }
        return new AABB(pos, endPos);
    }

    public static boolean isAllowedInShulker(ItemStack stack, Level level) {
        ShulkerBoxBlockEntity te = (ShulkerBoxBlockEntity) SHULKER_TILE.get();
        te.m_142339_(level);
        boolean r = te.canPlaceItemThroughFace(0, stack, null);
        te.m_142339_(null);
        return r;
    }

    public static boolean withinDistanceDown(BlockPos pos, Vec3 vector, double distW, double distDown) {
        double dx = vector.x() - ((double) pos.m_123341_() + 0.5);
        double dy = vector.y() - ((double) pos.m_123342_() + 0.5);
        double dz = vector.z() - ((double) pos.m_123343_() + 0.5);
        double myDistW = dx * dx + dz * dz;
        return myDistW < distW * distW && dy < distW && dy > -distDown;
    }

    public static enum Festivity {

        NONE,
        HALLOWEEN,
        APRILS_FOOL,
        CHRISTMAS,
        EARTH_DAY,
        ST_VALENTINE,
        MY_BIRTHDAY,
        MOD_BIRTHDAY;

        public boolean isHalloween() {
            return this == HALLOWEEN;
        }

        public boolean isAprilsFool() {
            return this == APRILS_FOOL;
        }

        public boolean isStValentine() {
            return this == ST_VALENTINE;
        }

        public boolean isChristmas() {
            return this == CHRISTMAS;
        }

        public boolean isEarthDay() {
            return this == EARTH_DAY;
        }

        public boolean isBirthday() {
            return this == MOD_BIRTHDAY || this == MY_BIRTHDAY;
        }

        public float getCandyWrappingIndex() {
            return switch(this) {
                case HALLOWEEN ->
                    0.5F;
                case CHRISTMAS ->
                    1.0F;
                default ->
                    0.0F;
            };
        }

        private static MiscUtils.Festivity get() {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(2);
            int date = calendar.get(5);
            if ((month != 9 || date < 29) && (month != 10 || date > 1)) {
                if (month == 3 && date == 1) {
                    return APRILS_FOOL;
                } else if (month == 1 && date == 14) {
                    return ST_VALENTINE;
                } else if (month == 3 && date == 22) {
                    return EARTH_DAY;
                } else if (month == 11 && date >= 20) {
                    return CHRISTMAS;
                } else if (month == 1 && date == 7) {
                    return MY_BIRTHDAY;
                } else {
                    return month == 9 && date == 9 ? MOD_BIRTHDAY : NONE;
                }
            } else {
                return HALLOWEEN;
            }
        }
    }
}