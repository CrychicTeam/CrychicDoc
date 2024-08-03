package snownee.lychee.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeLootContextParamSets;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.input.ItemHolderCollection;
import snownee.lychee.core.post.Delay;
import snownee.lychee.core.post.PostAction;

public class LycheeContext extends EmptyContainer {

    private final RandomSource random;

    private final Map<LootContextParam<?>, Object> params;

    private final Level level;

    private LootContext cachedLootContext;

    public ActionRuntime runtime;

    public ItemHolderCollection itemHolders = ItemHolderCollection.EMPTY;

    @Nullable
    public JsonObject json;

    protected LycheeContext(RandomSource pRandom, Level level, Map<LootContextParam<?>, Object> pParams) {
        this.random = pRandom;
        this.level = level;
        this.params = pParams;
    }

    public boolean hasParam(LootContextParam<?> pParameter) {
        if (pParameter == LootContextParams.BLOCK_ENTITY) {
            this.lazyGetBlockEntity();
        }
        return this.params.containsKey(pParameter);
    }

    public <T> T getParam(LootContextParam<T> pParam) {
        if (pParam == LootContextParams.BLOCK_ENTITY) {
            this.lazyGetBlockEntity();
        }
        T t = (T) this.params.get(pParam);
        if (t == null) {
            throw new NoSuchElementException(pParam.getName().toString());
        } else {
            return t;
        }
    }

    public Map<LootContextParam<?>, Object> getParams() {
        return this.params;
    }

    @Nullable
    public <T> T getParamOrNull(LootContextParam<T> pParameter) {
        if (pParameter == LootContextParams.BLOCK_ENTITY) {
            this.lazyGetBlockEntity();
        }
        return (T) this.params.get(pParameter);
    }

    public RandomSource getRandom() {
        return this.random;
    }

    public Level getLevel() {
        return this.level;
    }

    public ServerLevel getServerLevel() {
        return (ServerLevel) this.level;
    }

    public LootContext toLootContext() {
        if (this.cachedLootContext == null) {
            this.lazyGetBlockEntity();
            LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) this.level);
            this.params.forEach((p, o) -> paramsBuilder.withParameter(p, o));
            LootContext.Builder builder = new LootContext.Builder(paramsBuilder.create(LycheeLootContextParamSets.ALL));
            this.cachedLootContext = builder.create(null);
        }
        return this.cachedLootContext;
    }

    public void lazyGetBlockEntity() {
        if (!this.params.containsKey(LootContextParams.BLOCK_ENTITY)) {
            BlockPos pos = this.getParamOrNull(LycheeLootContextParams.BLOCK_POS);
            if (pos == null) {
                pos = BlockPos.containing(this.getParam(LootContextParams.ORIGIN));
                this.setParam(LycheeLootContextParams.BLOCK_POS, pos);
            }
            BlockEntity blockEntity = this.level.getBlockEntity(pos);
            if (blockEntity != null) {
                this.setParam(LootContextParams.BLOCK_ENTITY, blockEntity);
            }
        }
    }

    public void setParam(LootContextParam<?> param, Object value) {
        this.params.put(param, value);
    }

    public void removeParam(LootContextParam<?> param) {
        this.params.remove(param);
    }

    @Override
    public int getContainerSize() {
        return this.itemHolders.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.itemHolders.get(index).get();
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.itemHolders.replace(index, stack);
    }

    public void enqueueActions(Stream<PostAction> actions, int times, boolean startNew) {
        if (this.runtime == null || startNew) {
            this.runtime = new ActionRuntime();
        }
        this.runtime.enqueue(actions, times);
    }

    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("doDefault", this.runtime.doDefault);
        JsonArray jobs = new JsonArray(this.runtime.jobs.size());
        JsonArray jobRepeats = new JsonArray(this.runtime.jobs.size());
        for (Job job : this.runtime.jobs) {
            jobs.add(job.action.toJson());
            jobRepeats.add(job.times);
        }
        jsonObject.add("jobs", jobs);
        jsonObject.add("jobRepeats", jobRepeats);
        if (this.json != null) {
            jsonObject.add("json", this.json);
        }
        return jsonObject;
    }

    public static LycheeContext load(JsonObject jsonObject, Delay.LycheeMarker marker) {
        LycheeContext.Builder<LycheeContext> builder = new LycheeContext.Builder<>(marker.getEntity().m_9236_());
        builder.withParameter(LootContextParams.ORIGIN, marker.getEntity().m_20182_());
        LycheeContext ctx = builder.create(LycheeLootContextParamSets.ALL);
        ctx.runtime = new ActionRuntime();
        ctx.runtime.doDefault = jsonObject.get("doDefault").getAsBoolean();
        JsonArray jobs = jsonObject.getAsJsonArray("jobs");
        JsonArray jobRepeats = jsonObject.getAsJsonArray("jobRepeats");
        List<Job> jobList = Lists.newArrayList();
        for (int i = 0; i < jobs.size(); i++) {
            Job job = new Job(PostAction.parse(jobs.get(i).getAsJsonObject()), jobRepeats.get(i).getAsInt());
            jobList.add(job);
        }
        ctx.runtime.jobs.addAll(0, jobList);
        ctx.runtime.marker = marker;
        if (jsonObject.has("json")) {
            ctx.json = jsonObject.getAsJsonObject("json");
        }
        return ctx;
    }

    public static class Builder<C extends LycheeContext> {

        protected Map<LootContextParam<?>, Object> params = Maps.newIdentityHashMap();

        protected Level level;

        protected RandomSource random;

        public Builder(Level level) {
            this.level = level;
        }

        public LycheeContext.Builder<C> withRandom(RandomSource pRandom) {
            this.random = pRandom;
            return this;
        }

        public LycheeContext.Builder<C> withOptionalRandomSeed(long pSeed) {
            if (pSeed != 0L) {
                this.random = RandomSource.create(pSeed);
            }
            return this;
        }

        public LycheeContext.Builder<C> withOptionalRandomSeed(long pSeed, RandomSource pRandom) {
            if (pSeed == 0L) {
                this.random = pRandom;
            } else {
                this.random = RandomSource.create(pSeed);
            }
            return this;
        }

        public <T> LycheeContext.Builder<C> withParameter(LootContextParam<T> pParameter, T pValue) {
            this.params.put(pParameter, pValue);
            return this;
        }

        public <T> LycheeContext.Builder<C> withOptionalParameter(LootContextParam<T> pParameter, @Nullable T pValue) {
            if (pValue == null) {
                this.params.remove(pParameter);
            } else {
                this.params.put(pParameter, pValue);
            }
            return this;
        }

        public <T> T getParameter(LootContextParam<T> pParameter) {
            T t = (T) this.params.get(pParameter);
            if (t == null) {
                throw new IllegalArgumentException("No parameter " + pParameter);
            } else {
                return t;
            }
        }

        @Nullable
        public <T> T getOptionalParameter(LootContextParam<T> pParameter) {
            return (T) this.params.get(pParameter);
        }

        protected void beforeCreate(LootContextParamSet pParameterSet) {
            Set<LootContextParam<?>> set1 = Sets.difference(pParameterSet.getRequired(), this.params.keySet());
            if (!set1.isEmpty()) {
                throw new IllegalArgumentException("Missing required parameters: " + set1);
            } else {
                if (this.random == null) {
                    this.random = RandomSource.create();
                }
            }
        }

        public C create(LootContextParamSet pParameterSet) {
            this.beforeCreate(pParameterSet);
            return (C) (new LycheeContext(this.random, this.level, this.params));
        }

        public void setParams(Map<LootContextParam<?>, Object> params) {
            this.params = params;
        }
    }
}