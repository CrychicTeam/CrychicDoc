public void RecipesSchema(RecipeComponentFactoryRegistryEvent event) {
	event.register("bool", BooleanComponent.BOOLEAN);

	event.register("intNumber", NumberComponent.INT);
	event.register("longNumber", NumberComponent.LONG);
	event.register("floatNumber", NumberComponent.FLOAT);
	event.register("doubleNumber", NumberComponent.DOUBLE);

	event.register("anyIntNumber", NumberComponent.ANY_INT);
	event.register("anyLongNumber", NumberComponent.ANY_LONG);
	event.register("anyFloatNumber", NumberComponent.ANY_FLOAT);
	event.register("anyDoubleNumber", NumberComponent.ANY_DOUBLE);

	event.registerDynamic("intNumberRange", NumberComponent.DYNAMIC_INT);
	event.registerDynamic("longNumberRange", NumberComponent.DYNAMIC_LONG);
	event.registerDynamic("floatNumberRange", NumberComponent.DYNAMIC_FLOAT);
	event.registerDynamic("doubleNumberRange", NumberComponent.DYNAMIC_DOUBLE);

	event.register("anyString", StringComponent.ANY);
	event.register("nonEmptyString", StringComponent.NON_EMPTY);
	event.register("nonBlankString", StringComponent.NON_BLANK);
	event.register("id", StringComponent.ID);
	event.register("character", StringComponent.CHARACTER);
	event.registerDynamic("filteredString", StringComponent.DYNAMIC);

	event.register("inputItem", ItemComponents.INPUT);
	event.register("inputItemArray", ItemComponents.INPUT_ARRAY);
	event.register("unwrappedInputItemArray", ItemComponents.UNWRAPPED_INPUT_ARRAY);
	event.register("outputItem", ItemComponents.OUTPUT);
	event.register("outputItemArray", ItemComponents.OUTPUT_ARRAY);
	event.register("outputItemIdWithCount", ItemComponents.OUTPUT_ID_WITH_COUNT);

	event.register("inputFluid", FluidComponents.INPUT);
	event.register("inputFluidArray", FluidComponents.INPUT_ARRAY);
	event.register("inputFluidOrItem", FluidComponents.INPUT_OR_ITEM);
	event.register("inputFluidOrItemArray", FluidComponents.INPUT_OR_ITEM_ARRAY);
	event.register("outputFluid", FluidComponents.OUTPUT);
	event.register("outputFluidArray", FluidComponents.OUTPUT_ARRAY);
	event.register("outputFluidOrItem", FluidComponents.OUTPUT_OR_ITEM);
	event.register("outputFluidOrItemArray", FluidComponents.OUTPUT_OR_ITEM_ARRAY);

	event.register("inputBlock", BlockComponent.INPUT);
	event.register("outputBlock", BlockComponent.OUTPUT);
	event.register("otherBlock", BlockComponent.BLOCK);

	event.register("inputBlockState", BlockStateComponent.INPUT);
	event.register("outputBlockState", BlockStateComponent.OUTPUT);
	event.register("otherBlockState", BlockStateComponent.BLOCK);
	event.register("inputBlockStateString", BlockStateComponent.INPUT_STRING);
	event.register("outputBlockStateString", BlockStateComponent.OUTPUT_STRING);
	event.register("otherBlockStateString", BlockStateComponent.BLOCK_STRING);

	event.register("ticks", TimeComponent.TICKS);
	event.register("seconds", TimeComponent.SECONDS);
	event.register("minutes", TimeComponent.MINUTES);
	event.registerDynamic("time", TimeComponent.DYNAMIC);

	event.register("blockTag", TagKeyComponent.BLOCK);
	event.register("itemTag", TagKeyComponent.ITEM);
	event.register("fluidTag", TagKeyComponent.FLUID);
	event.register("entityTypeTag", TagKeyComponent.ENTITY_TYPE);
	event.register("biomeTag", TagKeyComponent.BIOME);
	event.registerDynamic("tag", TagKeyComponent.DYNAMIC);

	event.registerDynamic("registryObject", RegistryComponent.DYNAMIC);
	event.registerDynamic("enum", EnumComponent.DYNAMIC);
}