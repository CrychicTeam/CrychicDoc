package snownee.kiwi.block.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import snownee.kiwi.block.def.BlockDefinition;
import snownee.kiwi.block.def.SimpleBlockDefinition;
import snownee.kiwi.client.model.RetextureModel;
import snownee.kiwi.util.NBTHelper;

public abstract class RetextureBlockEntity extends ModBlockEntity {

    @Nullable
    protected Map<String, BlockDefinition> textures;

    protected ModelData modelData = ModelData.EMPTY;

    public RetextureBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos level, BlockState state, String... textureKeys) {
        super(tileEntityTypeIn, level, state);
        this.persistData = true;
        this.textures = textureKeys.length == 0 ? null : Maps.newHashMapWithExpectedSize(textureKeys.length);
        for (String key : textureKeys) {
            this.textures.put(key, null);
        }
    }

    public static void setTexture(Map<String, String> textures, String key, String path) {
        if (textures.containsKey(key)) {
            textures.put(key, path);
        }
    }

    public void setTexture(String key, BlockDefinition modelSupplier) {
        if (modelSupplier != null && this.isValidTexture(modelSupplier)) {
            setTexture(this.textures, key, modelSupplier);
        }
    }

    public boolean isValidTexture(BlockDefinition modelSupplier) {
        return true;
    }

    public static void setTexture(Map<String, BlockDefinition> textures, String key, BlockDefinition modelSupplier) {
        if (textures != null && textures.containsKey(key)) {
            textures.put(key, modelSupplier);
        }
    }

    public static void setTexture(Map<String, BlockDefinition> textures, String key, Item item) {
        Block block = Block.byItem(item);
        if (block != null) {
            setTexture(textures, key, SimpleBlockDefinition.of(block.defaultBlockState()));
        }
    }

    @Override
    public void refresh() {
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.requestModelDataUpdate();
        } else {
            super.refresh();
        }
    }

    public void onLoad() {
        super.requestModelDataUpdate();
    }

    public void requestModelDataUpdate() {
        if (this.textures != null) {
            super.requestModelDataUpdate();
            if (!this.f_58859_ && this.f_58857_ != null && this.f_58857_.isClientSide) {
                this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 8);
            }
        }
    }

    @Override
    protected void readPacketData(CompoundTag data) {
        if (data.contains("Overrides", 10)) {
            boolean shouldRefresh = readTextures(this.textures, data.getCompound("Overrides"), this::isValidTexture);
            if (shouldRefresh) {
                this.refresh();
            }
        }
    }

    public static boolean readTextures(Map<String, BlockDefinition> textures, CompoundTag data, Predicate<BlockDefinition> validator) {
        if (textures == null) {
            return false;
        } else {
            boolean shouldRefresh = false;
            NBTHelper helper = NBTHelper.of(data);
            for (String k : textures.keySet()) {
                CompoundTag v = helper.getTag(k);
                if (v != null) {
                    BlockDefinition supplier = BlockDefinition.fromNBT(v);
                    if ((supplier == null || validator.test(supplier)) && !Objects.equals(textures.get(k), supplier)) {
                        shouldRefresh = true;
                        textures.put(k, supplier);
                    }
                }
            }
            return shouldRefresh;
        }
    }

    @Override
    protected CompoundTag writePacketData(CompoundTag data) {
        writeTextures(this.textures, data);
        return data;
    }

    public static CompoundTag writeTextures(Map<String, BlockDefinition> textures, CompoundTag data) {
        if (textures != null) {
            NBTHelper tag = NBTHelper.of(data);
            textures.forEach((k, v) -> {
                if (v != null) {
                    CompoundTag compound = new CompoundTag();
                    v.save(compound);
                    compound.putString("Type", v.getFactory().getId());
                    tag.setTag("Overrides." + k, compound);
                }
            });
        }
        return data;
    }

    public ModelData getModelData() {
        if (this.textures != null && this.modelData == ModelData.EMPTY) {
            this.modelData = ModelData.builder().with(RetextureModel.TEXTURES, this.textures).build();
        }
        return this.modelData;
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor(BlockAndTintGetter level, int index) {
        return RetextureModel.getColor(this.textures, this.m_58900_(), level, this.f_58858_, index);
    }
}