package lio.playeranimatorapi.modifier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class CommonModifier {

    public ResourceLocation ID;

    public JsonObject data;

    public static Gson gson = new Gson();

    public static final Codec<CommonModifier> CODEC = Codec.list(Codec.STRING).comapFlatMap(CommonModifier::decode, CommonModifier::encode);

    public static final Codec<List<CommonModifier>> LIST_CODEC = Codec.list(CODEC).comapFlatMap(CommonModifier::decodeList, CommonModifier::encodeList);

    public static final CommonModifier nullModifer = new CommonModifier(null, null);

    public static final List<CommonModifier> emptyList = new ArrayList();

    public static List<String> encode(CommonModifier modifier) {
        List<String> list = new ArrayList();
        if (modifier != null) {
            list.add(modifier.ID.toString());
            if (modifier.data != null) {
                list.add(modifier.data.toString());
            }
        }
        return list;
    }

    public static DataResult<CommonModifier> decode(List<String> data) {
        if (data.size() == 2) {
            return DataResult.success(new CommonModifier(new ResourceLocation((String) data.get(0)), (JsonObject) gson.fromJson((String) data.get(1), TypeToken.get(JsonObject.class).getType())));
        } else {
            return data.size() == 1 ? DataResult.success(new CommonModifier(new ResourceLocation((String) data.get(0)), null)) : DataResult.success(nullModifer);
        }
    }

    public static List<CommonModifier> encodeList(List<CommonModifier> list) {
        return list != null ? list : emptyList;
    }

    public static DataResult<List<CommonModifier>> decodeList(List<CommonModifier> list) {
        return DataResult.success(list);
    }

    public CommonModifier(ResourceLocation ID, JsonObject json) {
        this.ID = ID;
        this.data = json;
    }
}