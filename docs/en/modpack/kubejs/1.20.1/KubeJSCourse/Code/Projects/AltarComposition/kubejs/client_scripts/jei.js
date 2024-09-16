JEIAddedEvents.registerCategories((event) => {
    event.custom("meng:block_test", (category) => {
        const jeiHelpers = category.getJeiHelpers()
        const guiHelper = category.getJeiHelpers().getGuiHelper();
        category
            .title("祭坛合成")
            .background(guiHelper.createDrawable(new ResourceLocation("meng", "jei/synthetic_orientation.png"), 0, 0, 150, 100))
            .icon(guiHelper.createDrawableItemStack(Item.of("meng:test_block")))
            .isRecipeHandled((recipe) => {
                return global["verifyRecipe"](jeiHelpers, recipe);
            })
            .handleLookup((builder, recipe, focuses) => {
                global["handleLookup"](jeiHelpers, builder, recipe, focuses);
            })
    });
});
global["verifyRecipe"] = (jeiHelpers, recipe) => {
    return !!(
        recipe?.data?.inputs !== undefined &&
        recipe?.data?.output !== undefined &&
        recipe?.data?.principal !== undefined
    );
};

global["handleLookup"] = (jeiHelpers, builder, recipe, focuses) => {
    builder.addSlot("INPUT", 35, 45).addItemStack(Item.of(recipe.data.principal)).setSlotName("input");
    let recipeItems = recipe.data.inputs
    for (let index = 0; index < 8; index++) {
        if (recipeItems == null) {
            recipeItems = "air"
        }
    }
    builder.addSlot("INPUT", 35, 15).addItemStack(Item.of(recipeItems[0])).setSlotName("input");
    builder.addSlot("INPUT", 35, 75).addItemStack(Item.of(recipeItems[1])).setSlotName("input");
    builder.addSlot("INPUT", 5, 45).addItemStack(Item.of(recipeItems[2])).setSlotName("input");
    builder.addSlot("INPUT", 65, 45).addItemStack(Item.of(recipeItems[3])).setSlotName("input");

    builder.addSlot("INPUT", 15, 25).addItemStack(Item.of(recipeItems[4])).setSlotName("input");
    builder.addSlot("INPUT", 15, 65).addItemStack(Item.of(recipeItems[5])).setSlotName("input");
    builder.addSlot("INPUT", 55, 25).addItemStack(Item.of(recipeItems[6])).setSlotName("input");
    builder.addSlot("INPUT", 55, 65).addItemStack(Item.of(recipeItems[7])).setSlotName("input");

    builder.addSlot("OUTPUT", 125, 45).addItemStack(Item.of(recipe.data.output)).setSlotName("output");

    builder.addInvisibleIngredients("OUTPUT").addItemStack(Item.of(recipe.data.output));
};

JEIAddedEvents.registerRecipes((event) => {
    let cb = event.custom("meng:block_test")
    for (const key of global.testRecipe) {
        cb.add({ output: key.outputItem, inputs: key.inputItems, principal: key.principalItem })
    }
});

JEIAddedEvents.registerRecipeCatalysts(jei =>{
    jei.data.addRecipeCatalyst("meng:test_block","meng:block_test")
})