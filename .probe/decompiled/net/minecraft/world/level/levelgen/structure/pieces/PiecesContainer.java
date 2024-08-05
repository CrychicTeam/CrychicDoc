package net.minecraft.world.level.levelgen.structure.pieces;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.slf4j.Logger;

public record PiecesContainer(List<StructurePiece> f_192741_) {

    private final List<StructurePiece> pieces;

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation JIGSAW_RENAME = new ResourceLocation("jigsaw");

    private static final Map<ResourceLocation, ResourceLocation> RENAMES = ImmutableMap.builder().put(new ResourceLocation("nvi"), JIGSAW_RENAME).put(new ResourceLocation("pcp"), JIGSAW_RENAME).put(new ResourceLocation("bastionremnant"), JIGSAW_RENAME).put(new ResourceLocation("runtime"), JIGSAW_RENAME).build();

    public PiecesContainer(List<StructurePiece> f_192741_) {
        this.pieces = List.copyOf(f_192741_);
    }

    public boolean isEmpty() {
        return this.pieces.isEmpty();
    }

    public boolean isInsidePiece(BlockPos p_192752_) {
        for (StructurePiece $$1 : this.pieces) {
            if ($$1.getBoundingBox().isInside(p_192752_)) {
                return true;
            }
        }
        return false;
    }

    public Tag save(StructurePieceSerializationContext p_192750_) {
        ListTag $$1 = new ListTag();
        for (StructurePiece $$2 : this.pieces) {
            $$1.add($$2.createTag(p_192750_));
        }
        return $$1;
    }

    public static PiecesContainer load(ListTag p_192754_, StructurePieceSerializationContext p_192755_) {
        List<StructurePiece> $$2 = Lists.newArrayList();
        for (int $$3 = 0; $$3 < p_192754_.size(); $$3++) {
            CompoundTag $$4 = p_192754_.getCompound($$3);
            String $$5 = $$4.getString("id").toLowerCase(Locale.ROOT);
            ResourceLocation $$6 = new ResourceLocation($$5);
            ResourceLocation $$7 = (ResourceLocation) RENAMES.getOrDefault($$6, $$6);
            StructurePieceType $$8 = BuiltInRegistries.STRUCTURE_PIECE.get($$7);
            if ($$8 == null) {
                LOGGER.error("Unknown structure piece id: {}", $$7);
            } else {
                try {
                    StructurePiece $$9 = $$8.load(p_192755_, $$4);
                    $$2.add($$9);
                } catch (Exception var10) {
                    LOGGER.error("Exception loading structure piece with id {}", $$7, var10);
                }
            }
        }
        return new PiecesContainer($$2);
    }

    public BoundingBox calculateBoundingBox() {
        return StructurePiece.createBoundingBox(this.pieces.stream());
    }
}