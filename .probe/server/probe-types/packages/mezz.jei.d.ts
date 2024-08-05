declare module "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder" {
import {$IIngredientAcceptor, $IIngredientAcceptor$Type} from "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IRecipeSlotTooltipCallback, $IRecipeSlotTooltipCallback$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"

export interface $IRecipeSlotBuilder extends $IIngredientAcceptor<($IRecipeSlotBuilder)> {

 "addTooltipCallback"(arg0: $IRecipeSlotTooltipCallback$Type): $IRecipeSlotBuilder
 "setOverlay"(arg0: $IDrawable$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
 "setBackground"(arg0: $IDrawable$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
 "setFluidRenderer"(arg0: long, arg1: boolean, arg2: integer, arg3: integer): $IRecipeSlotBuilder
 "setSlotName"(arg0: string): $IRecipeSlotBuilder
 "setCustomRenderer"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $IIngredientRenderer$Type<(T)>): $IRecipeSlotBuilder
 "addIngredientsUnsafe"(arg0: $List$Type<(any)>): $IRecipeSlotBuilder
 "addIngredient"<I>(arg0: $IIngredientType$Type<(I)>, arg1: I): $IRecipeSlotBuilder
 "addItemStack"(arg0: $ItemStack$Type): $IRecipeSlotBuilder
 "addIngredients"(arg0: $Ingredient$Type): $IRecipeSlotBuilder
 "addIngredients"<I>(arg0: $IIngredientType$Type<(I)>, arg1: $List$Type<(I)>): $IRecipeSlotBuilder
 "addItemStacks"(arg0: $List$Type<($ItemStack$Type)>): $IRecipeSlotBuilder
 "addFluidStack"(arg0: $Fluid$Type, arg1: long): $IRecipeSlotBuilder
 "addFluidStack"(arg0: $Fluid$Type, arg1: long, arg2: $CompoundTag$Type): $IRecipeSlotBuilder
}

export namespace $IRecipeSlotBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeSlotBuilder$Type = ($IRecipeSlotBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeSlotBuilder_ = $IRecipeSlotBuilder$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$DragRouter" {
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"

export class $DragRouter {

constructor(...arg0: ($IDragHandler$Type)[])

public "handleGuiChange"(): void
public "startDrag"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "completeDrag"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "cancelDrag"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragRouter$Type = ($DragRouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragRouter_ = $DragRouter$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IJeiHelpers" {
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export interface $IJeiHelpers {

 "getFocusFactory"(): $IFocusFactory
 "getGuiHelper"(): $IGuiHelper
 "getRecipeType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
 "getModIdHelper"(): $IModIdHelper
 "getPlatformFluidHelper"(): $IPlatformFluidHelper<(any)>
 "getIngredientManager"(): $IIngredientManager
 "getAllRecipeTypes"(): $Stream<($RecipeType<(any)>)>
 "getColorHelper"(): $IColorHelper
 "getStackHelper"(): $IStackHelper
}

export namespace $IJeiHelpers {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiHelpers$Type = ($IJeiHelpers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiHelpers_ = $IJeiHelpers$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer" {
import {$IJeiConfigValueSerializer$IDeserializeResult, $IJeiConfigValueSerializer$IDeserializeResult$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer$IDeserializeResult"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $IJeiConfigValueSerializer<T> {

 "isValid"(arg0: T): boolean
 "deserialize"(arg0: string): $IJeiConfigValueSerializer$IDeserializeResult<(T)>
 "getValidValuesDescription"(): string
 "getAllValidValues"(): $Optional<($Collection<(T)>)>
 "serialize"(arg0: T): string
}

export namespace $IJeiConfigValueSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigValueSerializer$Type<T> = ($IJeiConfigValueSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigValueSerializer_<T> = $IJeiConfigValueSerializer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/brewing/$JeiBrewingRecipe" {
import {$BrewingRecipeUtil, $BrewingRecipeUtil$Type} from "packages/mezz/jei/library/plugins/vanilla/brewing/$BrewingRecipeUtil"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $JeiBrewingRecipe implements $IJeiBrewingRecipe {

constructor(arg0: $List$Type<($ItemStack$Type)>, arg1: $List$Type<($ItemStack$Type)>, arg2: $ItemStack$Type, arg3: $BrewingRecipeUtil$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getIngredients"(): $List<($ItemStack)>
public "getBrewingSteps"(): integer
public "getPotionInputs"(): $List<($ItemStack)>
public "getPotionOutput"(): $ItemStack
get "ingredients"(): $List<($ItemStack)>
get "brewingSteps"(): integer
get "potionInputs"(): $List<($ItemStack)>
get "potionOutput"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiBrewingRecipe$Type = ($JeiBrewingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiBrewingRecipe_ = $JeiBrewingRecipe$Type;
}}
declare module "packages/mezz/jei/common/config/sorting/$MappedSortingConfig" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$SortingConfig, $SortingConfig$Type} from "packages/mezz/jei/common/config/sorting/$SortingConfig"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISortingSerializer, $ISortingSerializer$Type} from "packages/mezz/jei/common/config/sorting/serializers/$ISortingSerializer"

export class $MappedSortingConfig<T, V> extends $SortingConfig<(V)> {

constructor(arg0: $Path$Type, arg1: $ISortingSerializer$Type<(V)>, arg2: $Function$Type<(T), (V)>)

public "getComparator"(arg0: $Collection$Type<(T)>): $Comparator<(T)>
public "getComparatorFromMappedValues"(arg0: $Collection$Type<(V)>): $Comparator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappedSortingConfig$Type<T, V> = ($MappedSortingConfig<(T), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappedSortingConfig_<T, V> = $MappedSortingConfig$Type<(T), (V)>;
}}
declare module "packages/mezz/jei/forge/$JustEnoughItemsClient" {
import {$PermanentEventSubscriptions, $PermanentEventSubscriptions$Type} from "packages/mezz/jei/forge/events/$PermanentEventSubscriptions"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/mezz/jei/forge/network/$NetworkHandler"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"

export class $JustEnoughItemsClient {

constructor(arg0: $NetworkHandler$Type, arg1: $PermanentEventSubscriptions$Type, arg2: $IServerConfig$Type)

public "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JustEnoughItemsClient$Type = ($JustEnoughItemsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JustEnoughItemsClient_ = $JustEnoughItemsClient$Type;
}}
declare module "packages/mezz/jei/common/network/$ClientPacketContext" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"

export class $ClientPacketContext extends $Record {

constructor(player: $LocalPlayer$Type, connection: $IConnectionToServer$Type, serverConfig: $IServerConfig$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "connection"(): $IConnectionToServer
public "player"(): $LocalPlayer
public "serverConfig"(): $IServerConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPacketContext$Type = ($ClientPacketContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPacketContext_ = $ClientPacketContext$Type;
}}
declare module "packages/mezz/jei/library/color/$MMCQ" {
import {$MMCQ$CMap, $MMCQ$CMap$Type} from "packages/mezz/jei/library/color/$MMCQ$CMap"

export class $MMCQ {

constructor()

public static "quantize"(arg0: ((integer)[])[], arg1: integer): $MMCQ$CMap
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$Type = ($MMCQ);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ_ = $MMCQ$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IStackHelper" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export interface $IStackHelper {

 "isEquivalent"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $UidContext$Type): boolean
 "getUniqueIdentifierForStack"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
}

export namespace $IStackHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStackHelper$Type = ($IStackHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStackHelper_ = $IStackHelper$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$IRecipeGuiLogic" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"

export interface $IRecipeGuiLogic {

 "getRecipeCatalysts"(arg0: $IRecipeCategory$Type<(any)>): $Stream<($ITypedIngredient<(any)>)>
 "getRecipeCatalysts"(): $Stream<($ITypedIngredient<(any)>)>
 "back"(): boolean
 "setFocus"(arg0: $IFocusGroup$Type): boolean
 "hasMultiplePages"(): boolean
 "getSelectedRecipeCategory"(): $IRecipeCategory<(any)>
 "hasMultipleCategories"(): boolean
 "previousRecipeCategory"(): void
 "previousPage"(): void
 "nextPage"(): void
 "getRecipeCategories"(): $List<($IRecipeCategory<(any)>)>
 "nextRecipeCategory"(): void
 "hasAllCategories"(): boolean
 "setRecipesPerPage"(arg0: integer): void
 "getRecipeLayouts"(): $List<($IRecipeLayoutDrawable<(any)>)>
 "clearHistory"(): void
 "getPageString"(): string
 "setCategoryFocus"(): boolean
 "setCategoryFocus"(arg0: $List$Type<($RecipeType$Type<(any)>)>): boolean
 "setRecipeCategory"(arg0: $IRecipeCategory$Type<(any)>): void
}

export namespace $IRecipeGuiLogic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeGuiLogic$Type = ($IRecipeGuiLogic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeGuiLogic_ = $IRecipeGuiLogic$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformScreenHelper" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeBookComponent, $RecipeBookComponent$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookComponent"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$RecipeBookTabButton, $RecipeBookTabButton$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookTabButton"
import {$RecipeUpdateListener, $RecipeUpdateListener$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeUpdateListener"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IPlatformScreenHelper {

 "getTabButtons"(arg0: $RecipeBookComponent$Type): $List<($RecipeBookTabButton)>
 "setFocused"(arg0: $AbstractWidget$Type, arg1: boolean): void
 "getBookArea"(arg0: $RecipeUpdateListener$Type): $ImmutableRect2i
 "getGuiLeft"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getGuiTop"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getXSize"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getYSize"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getSlotUnderMouse"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($Slot)>
}

export namespace $IPlatformScreenHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformScreenHelper$Type = ($IPlatformScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformScreenHelper_ = $IPlatformScreenHelper$Type;
}}
declare module "packages/mezz/jei/forge/platform/$PlatformHelper" {
import {$ScreenHelper, $ScreenHelper$Type} from "packages/mezz/jei/forge/platform/$ScreenHelper"
import {$IPlatformFluidHelperInternal, $IPlatformFluidHelperInternal$Type} from "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal"
import {$IngredientHelper, $IngredientHelper$Type} from "packages/mezz/jei/forge/platform/$IngredientHelper"
import {$RenderHelper, $RenderHelper$Type} from "packages/mezz/jei/forge/platform/$RenderHelper"
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformHelper"
import {$IPlatformRegistry, $IPlatformRegistry$Type} from "packages/mezz/jei/common/platform/$IPlatformRegistry"
import {$InputHelper, $InputHelper$Type} from "packages/mezz/jei/forge/platform/$InputHelper"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $PlatformHelper implements $IPlatformHelper {

constructor()

public "getFluidHelper"(): $IPlatformFluidHelperInternal<(any)>
public "getRenderHelper"(): $RenderHelper
public "getScreenHelper"(): $ScreenHelper
public "getIngredientHelper"(): $IngredientHelper
public "getInputHelper"(): $InputHelper
public "getRegistry"<T>(arg0: $ResourceKey$Type<(any)>): $IPlatformRegistry<(T)>
get "fluidHelper"(): $IPlatformFluidHelperInternal<(any)>
get "renderHelper"(): $RenderHelper
get "screenHelper"(): $ScreenHelper
get "ingredientHelper"(): $IngredientHelper
get "inputHelper"(): $InputHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformHelper$Type = ($PlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformHelper_ = $PlatformHelper$Type;
}}
declare module "packages/mezz/jei/forge/plugins/forge/$ForgeGuiPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $ForgeGuiPlugin implements $IModPlugin {

constructor()

public "onRuntimeUnavailable"(): void
public "getPluginUid"(): $ResourceLocation
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeGuiPlugin$Type = ($ForgeGuiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeGuiPlugin_ = $ForgeGuiPlugin$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/fuel/$FurnaceFuelCategory" {
import {$FurnaceVariantCategory, $FurnaceVariantCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$FurnaceVariantCategory"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IJeiFuelingRecipe, $IJeiFuelingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiFuelingRecipe"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $FurnaceFuelCategory extends $FurnaceVariantCategory<($IJeiFuelingRecipe)> {

constructor(arg0: $IGuiHelper$Type, arg1: $Textures$Type)

public "getRecipeType"(): $RecipeType<($IJeiFuelingRecipe)>
public "draw"(arg0: $IJeiFuelingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $IJeiFuelingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
get "recipeType"(): $RecipeType<($IJeiFuelingRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceFuelCategory$Type = ($FurnaceFuelCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceFuelCategory_ = $FurnaceFuelCategory$Type;
}}
declare module "packages/mezz/jei/library/color/$ColorName" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $ColorName extends $Record {

constructor(name: string, color: integer)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorName$Type = ($ColorName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorName_ = $ColorName$Type;
}}
declare module "packages/mezz/jei/gui/$PageNavigation" {
import {$IPaged, $IPaged$Type} from "packages/mezz/jei/gui/input/$IPaged"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageNavigation {

constructor(arg0: $IPaged$Type, arg1: boolean, arg2: $Textures$Type)

public "draw"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
public "updateBounds"(arg0: $ImmutableRect2i$Type): void
public "updatePageNumber"(): void
public "createInputHandler"(): $IUserInputHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageNavigation$Type = ($PageNavigation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageNavigation_ = $PageNavigation$Type;
}}
declare module "packages/mezz/jei/core/search/$SearchMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SearchMode extends $Enum<($SearchMode)> {
static readonly "ENABLED": $SearchMode
static readonly "REQUIRE_PREFIX": $SearchMode
static readonly "DISABLED": $SearchMode


public static "values"(): ($SearchMode)[]
public static "valueOf"(arg0: string): $SearchMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchMode$Type = (("require_prefix") | ("disabled") | ("enabled")) | ($SearchMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchMode_ = $SearchMode$Type;
}}
declare module "packages/mezz/jei/core/util/$WeakList" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export class $WeakList<T> {

constructor()

public "add"(arg0: T): void
public "isEmpty"(): boolean
public "forEach"(arg0: $Consumer$Type<(T)>): void
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeakList$Type<T> = ($WeakList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeakList_<T> = $WeakList$Type<(T)>;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformRecipeHelper" {
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export interface $IPlatformRecipeHelper {

 "getBase"(arg0: $SmithingRecipe$Type): $Ingredient
 "getWidth"<T extends $CraftingRecipe>(arg0: T): integer
 "getHeight"<T extends $CraftingRecipe>(arg0: T): integer
 "isHandled"(arg0: $SmithingRecipe$Type): boolean
 "getBrewingRecipes"(arg0: $IIngredientManager$Type, arg1: $IVanillaRecipeFactory$Type): $List<($IJeiBrewingRecipe)>
 "getAddition"(arg0: $SmithingRecipe$Type): $Ingredient
 "getRegistryNameForRecipe"(arg0: $Recipe$Type<(any)>): $Optional<($ResourceLocation)>
 "getTemplate"(arg0: $SmithingRecipe$Type): $Ingredient
}

export namespace $IPlatformRecipeHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformRecipeHelper$Type = ($IPlatformRecipeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformRecipeHelper_ = $IPlatformRecipeHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredientHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$DebugIngredient, $DebugIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredient"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $DebugIngredientHelper implements $IIngredientHelper<($DebugIngredient)> {

constructor()

public "getDisplayName"(arg0: $DebugIngredient$Type): string
public "copyIngredient"(arg0: $DebugIngredient$Type): $DebugIngredient
public "getErrorInfo"(arg0: $DebugIngredient$Type): string
public "getIngredientType"(): $IIngredientType<($DebugIngredient)>
public "getCheatItemStack"(arg0: $DebugIngredient$Type): $ItemStack
public "getUniqueId"(arg0: $DebugIngredient$Type, arg1: $UidContext$Type): string
public "getResourceLocation"(arg0: $DebugIngredient$Type): $ResourceLocation
public "getWildcardId"(arg0: $DebugIngredient$Type): string
public "getDisplayModId"(arg0: $DebugIngredient$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($DebugIngredient$Type)>): $Optional<($ResourceLocation)>
public "getTagStream"(arg0: $DebugIngredient$Type): $Stream<($ResourceLocation)>
public "isValidIngredient"(arg0: $DebugIngredient$Type): boolean
public "normalizeIngredient"(arg0: $DebugIngredient$Type): $DebugIngredient
public "isIngredientOnServer"(arg0: $DebugIngredient$Type): boolean
public "getColors"(arg0: $DebugIngredient$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($DebugIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugIngredientHelper$Type = ($DebugIngredientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugIngredientHelper_ = $DebugIngredientHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/$FluidSubtypeHandlerTest" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $FluidSubtypeHandlerTest<T> implements $IIngredientSubtypeInterpreter<(T)> {

constructor(arg0: $IIngredientTypeWithSubtypes$Type<($Fluid$Type), (T)>)

public "apply"(arg0: T, arg1: $UidContext$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidSubtypeHandlerTest$Type<T> = ($FluidSubtypeHandlerTest<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidSubtypeHandlerTest_<T> = $FluidSubtypeHandlerTest$Type<(T)>;
}}
declare module "packages/mezz/jei/forge/startup/$EventRegistration" {
import {$RuntimeEventSubscriptions, $RuntimeEventSubscriptions$Type} from "packages/mezz/jei/forge/events/$RuntimeEventSubscriptions"
import {$GuiEventHandler, $GuiEventHandler$Type} from "packages/mezz/jei/gui/events/$GuiEventHandler"
import {$JeiEventHandlers, $JeiEventHandlers$Type} from "packages/mezz/jei/gui/startup/$JeiEventHandlers"

export class $EventRegistration {

constructor()

public static "registerEvents"(arg0: $RuntimeEventSubscriptions$Type, arg1: $JeiEventHandlers$Type): void
public static "registerGuiHandler"(arg0: $RuntimeEventSubscriptions$Type, arg1: $GuiEventHandler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventRegistration$Type = ($EventRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventRegistration_ = $EventRegistration$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IJeiKeyMapping" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IJeiKeyMapping {

 "isUnbound"(): boolean
 "getTranslatedKeyMessage"(): $Component
 "isActiveAndMatches"(arg0: $InputConstants$Key$Type): boolean
}

export namespace $IJeiKeyMapping {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiKeyMapping$Type = ($IJeiKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiKeyMapping_ = $IJeiKeyMapping$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/$OutputSlotTooltipCallback" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IRecipeSlotTooltipCallback, $IRecipeSlotTooltipCallback$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $OutputSlotTooltipCallback implements $IRecipeSlotTooltipCallback {

constructor(arg0: $ResourceLocation$Type, arg1: $IModIdHelper$Type, arg2: $IIngredientManager$Type)

public "onTooltip"(arg0: $IRecipeSlotView$Type, arg1: $List$Type<($Component$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OutputSlotTooltipCallback$Type = ($OutputSlotTooltipCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OutputSlotTooltipCallback_ = $OutputSlotTooltipCallback$Type;
}}
declare module "packages/mezz/jei/api/constants/$ModIds" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModIds {
static readonly "JEI_ID": string
static readonly "JEI_NAME": string
static readonly "MINECRAFT_ID": string
static readonly "MINECRAFT_NAME": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIds$Type = ($ModIds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIds_ = $ModIds$Type;
}}
declare module "packages/mezz/jei/library/config/serializers/$ColorNameSerializer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ColorName, $ColorName$Type} from "packages/mezz/jei/library/color/$ColorName"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $ColorNameSerializer implements $IJeiConfigValueSerializer<($ColorName)> {
static readonly "INSTANCE": $ColorNameSerializer


public "isValid"(arg0: $ColorName$Type): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<($ColorName)>)>
public "serialize"(arg0: $ColorName$Type): string
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<($ColorName)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorNameSerializer$Type = ($ColorNameSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorNameSerializer_ = $ColorNameSerializer$Type;
}}
declare module "packages/mezz/jei/api/registration/$ISubtypeRegistration" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"

export interface $ISubtypeRegistration {

 "useNbtForSubtypes"(...arg0: ($Fluid$Type)[]): void
 "useNbtForSubtypes"(...arg0: ($Item$Type)[]): void
 "registerSubtypeInterpreter"(arg0: $Item$Type, arg1: $IIngredientSubtypeInterpreter$Type<($ItemStack$Type)>): void
 "registerSubtypeInterpreter"<B, I>(arg0: $IIngredientTypeWithSubtypes$Type<(B), (I)>, arg1: B, arg2: $IIngredientSubtypeInterpreter$Type<(I)>): void
}

export namespace $ISubtypeRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISubtypeRegistration$Type = ($ISubtypeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISubtypeRegistration_ = $ISubtypeRegistration$Type;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$BooleanSerializer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $BooleanSerializer implements $IJeiConfigValueSerializer<(boolean)> {
static readonly "INSTANCE": $BooleanSerializer


public "isValid"(arg0: boolean): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<(boolean)>)>
public "serialize"(arg0: boolean): string
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<(boolean)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanSerializer$Type = ($BooleanSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanSerializer_ = $BooleanSerializer$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredientRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$DebugIngredient, $DebugIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredient"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DebugIngredientRenderer implements $IIngredientRenderer<($DebugIngredient)> {

constructor(arg0: $IIngredientHelper$Type<($DebugIngredient$Type)>)

public "render"(arg0: $GuiGraphics$Type, arg1: $DebugIngredient$Type): void
public "getTooltip"(arg0: $DebugIngredient$Type, arg1: $TooltipFlag$Type): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $DebugIngredient$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugIngredientRenderer$Type = ($DebugIngredientRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugIngredientRenderer_ = $DebugIngredientRenderer$Type;
}}
declare module "packages/mezz/jei/common/$Internal" {
import {$JeiFeatures, $JeiFeatures$Type} from "packages/mezz/jei/common/$JeiFeatures"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IJeiClientConfigs, $IJeiClientConfigs$Type} from "packages/mezz/jei/common/config/$IJeiClientConfigs"

export class $Internal {


public static "getTextures"(): $Textures
public static "getJeiFeatures"(): $JeiFeatures
public static "getServerConnection"(): $IConnectionToServer
public static "getOptionalJeiClientConfigs"(): $Optional<($IJeiClientConfigs)>
public static "setJeiClientConfigs"(arg0: $IJeiClientConfigs$Type): void
public static "getJeiClientConfigs"(): $IJeiClientConfigs
public static "getClientToggleState"(): $IClientToggleState
public static "setServerConnection"(arg0: $IConnectionToServer$Type): void
public static "setKeyMappings"(arg0: $IInternalKeyMappings$Type): void
public static "getKeyMappings"(): $IInternalKeyMappings
get "textures"(): $Textures
get "jeiFeatures"(): $JeiFeatures
get "serverConnection"(): $IConnectionToServer
get "optionalJeiClientConfigs"(): $Optional<($IJeiClientConfigs)>
set "jeiClientConfigs"(value: $IJeiClientConfigs$Type)
get "jeiClientConfigs"(): $IJeiClientConfigs
get "clientToggleState"(): $IClientToggleState
set "serverConnection"(value: $IConnectionToServer$Type)
set "keyMappings"(value: $IInternalKeyMappings$Type)
get "keyMappings"(): $IInternalKeyMappings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Internal$Type = ($Internal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Internal_ = $Internal$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$IRecipeLogicStateListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IRecipeLogicStateListener {

 "onStateChange"(): void

(): void
}

export namespace $IRecipeLogicStateListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeLogicStateListener$Type = ($IRecipeLogicStateListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeLogicStateListener_ = $IRecipeLogicStateListener$Type;
}}
declare module "packages/mezz/jei/api/recipe/category/extensions/$IExtendableRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeCategoryExtension, $IRecipeCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryExtension"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IExtendableRecipeCategory<T, W extends $IRecipeCategoryExtension> extends $IRecipeCategory<(T)> {

 "addCategoryExtension"<R extends T>(arg0: $Class$Type<(any)>, arg1: $Function$Type<(R), (any)>): void
 "addCategoryExtension"<R extends T>(arg0: $Class$Type<(any)>, arg1: $Predicate$Type<(R)>, arg2: $Function$Type<(R), (any)>): void
 "getRecipeType"(): $RecipeType<(T)>
 "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
 "getWidth"(): integer
 "getHeight"(): integer
 "isHandled"(arg0: T): boolean
 "getIcon"(): $IDrawable
 "getTitle"(): $Component
 "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
 "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
 "getBackground"(): $IDrawable
 "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
 "getRegistryName"(arg0: T): $ResourceLocation
}

export namespace $IExtendableRecipeCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtendableRecipeCategory$Type<T, W> = ($IExtendableRecipeCategory<(T), (W)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtendableRecipeCategory_<T, W> = $IExtendableRecipeCategory$Type<(T), (W)>;
}}
declare module "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export interface $IRecipeCatalystRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "addRecipeCatalyst"<T>(arg0: $IIngredientType$Type<(T)>, arg1: T, ...arg2: ($RecipeType$Type<(any)>)[]): void
 "addRecipeCatalyst"(arg0: $ItemStack$Type, ...arg1: ($RecipeType$Type<(any)>)[]): void
 "getIngredientManager"(): $IIngredientManager
}

export namespace $IRecipeCatalystRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCatalystRegistration$Type = ($IRecipeCatalystRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCatalystRegistration_ = $IRecipeCatalystRegistration$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient$CrashType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ErrorIngredient$CrashType extends $Enum<($ErrorIngredient$CrashType)> {
static readonly "RenderBreakVertexBufferCrash": $ErrorIngredient$CrashType
static readonly "TooltipCrash": $ErrorIngredient$CrashType


public static "values"(): ($ErrorIngredient$CrashType)[]
public static "valueOf"(arg0: string): $ErrorIngredient$CrashType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorIngredient$CrashType$Type = (("renderbreakvertexbuffercrash") | ("tooltipcrash")) | ($ErrorIngredient$CrashType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorIngredient$CrashType_ = $ErrorIngredient$CrashType$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformInputHelper" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$IJeiKeyMappingCategoryBuilder, $IJeiKeyMappingCategoryBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingCategoryBuilder"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IPlatformInputHelper {

 "createKeyMappingCategoryBuilder"(arg0: string): $IJeiKeyMappingCategoryBuilder
 "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Key$Type): boolean
}

export namespace $IPlatformInputHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformInputHelper$Type = ($IPlatformInputHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformInputHelper_ = $IPlatformInputHelper$Type;
}}
declare module "packages/mezz/jei/common/config/$IngredientFilterConfig" {
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"

export class $IngredientFilterConfig implements $IIngredientFilterConfig {
readonly "modNameSearchMode": $Supplier<($SearchMode)>
readonly "tooltipSearchMode": $Supplier<($SearchMode)>
readonly "tagSearchMode": $Supplier<($SearchMode)>
readonly "colorSearchMode": $Supplier<($SearchMode)>
readonly "resourceLocationSearchMode": $Supplier<($SearchMode)>
readonly "searchAdvancedTooltips": $Supplier<(boolean)>

constructor(arg0: $IConfigSchemaBuilder$Type)

public "getTagSearchMode"(): $SearchMode
public "getColorSearchMode"(): $SearchMode
public "getModNameSearchMode"(): $SearchMode
public "getSearchAdvancedTooltips"(): boolean
public "getTooltipSearchMode"(): $SearchMode
public "getResourceLocationSearchMode"(): $SearchMode
get "tagSearchMode"(): $SearchMode
get "colorSearchMode"(): $SearchMode
get "modNameSearchMode"(): $SearchMode
get "searchAdvancedTooltips"(): boolean
get "tooltipSearchMode"(): $SearchMode
get "resourceLocationSearchMode"(): $SearchMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientFilterConfig$Type = ($IngredientFilterConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientFilterConfig_ = $IngredientFilterConfig$Type;
}}
declare module "packages/mezz/jei/gui/ghost/$GhostIngredientDragManager" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $GhostIngredientDragManager {

constructor(arg0: $IRecipeFocusSource$Type, arg1: $IScreenHelper$Type, arg2: $IIngredientManager$Type, arg3: $IClientToggleState$Type)

public "drawTooltips"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "drawOnForeground"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "stopDrag"(): void
public "createDragHandler"(): $IDragHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostIngredientDragManager$Type = ($GhostIngredientDragManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostIngredientDragManager_ = $GhostIngredientDragManager$Type;
}}
declare module "packages/mezz/jei/library/gui/elements/$DrawableBuilder" {
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"
import {$IDrawableBuilder, $IDrawableBuilder$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableBuilder"
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"

export class $DrawableBuilder implements $IDrawableBuilder {

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer)

public "trim"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IDrawableBuilder
public "build"(): $IDrawableStatic
public "addPadding"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IDrawableBuilder
public "setTextureSize"(arg0: integer, arg1: integer): $IDrawableBuilder
public "buildAnimated"(arg0: integer, arg1: $IDrawableAnimated$StartDirection$Type, arg2: boolean): $IDrawableAnimated
public "buildAnimated"(arg0: $ITickTimer$Type, arg1: $IDrawableAnimated$StartDirection$Type): $IDrawableAnimated
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableBuilder$Type = ($DrawableBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableBuilder_ = $DrawableBuilder$Type;
}}
declare module "packages/mezz/jei/common/util/$StackHelper" {
import {$ISubtypeManager, $ISubtypeManager$Type} from "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $StackHelper implements $IStackHelper {

constructor(arg0: $ISubtypeManager$Type)

public "isEquivalent"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $UidContext$Type): boolean
public "getUniqueIdentifierForStack"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
public static "getRegistryNameForStack"(arg0: $ItemStack$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackHelper$Type = ($StackHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackHelper_ = $StackHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredientListFactory" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ErrorIngredient, $ErrorIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient"

export class $ErrorIngredientListFactory {


public static "create"(): $Collection<($ErrorIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorIngredientListFactory$Type = ($ErrorIngredientListFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorIngredientListFactory_ = $ErrorIngredientListFactory$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientGridTooltipHelper" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $IngredientGridTooltipHelper {

constructor(arg0: $IIngredientManager$Type, arg1: $IIngredientFilterConfig$Type, arg2: $IClientToggleState$Type, arg3: $IModIdHelper$Type, arg4: $IInternalKeyMappings$Type, arg5: $IColorHelper$Type)

public "getTooltip"<T>(arg0: $ITypedIngredient$Type<(T)>, arg1: $IIngredientRenderer$Type<(T)>, arg2: $IIngredientHelper$Type<(T)>): $List<($Component)>
public "drawTooltip"<T>(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $ITypedIngredient$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientGridTooltipHelper$Type = ($IngredientGridTooltipHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientGridTooltipHelper_ = $IngredientGridTooltipHelper$Type;
}}
declare module "packages/mezz/jei/core/search/$PrefixInfo" {
import {$PrefixInfo$IModeGetter, $PrefixInfo$IModeGetter$Type} from "packages/mezz/jei/core/search/$PrefixInfo$IModeGetter"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ISearchStorage, $ISearchStorage$Type} from "packages/mezz/jei/core/search/$ISearchStorage"
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"
import {$PrefixInfo$IStringsGetter, $PrefixInfo$IStringsGetter$Type} from "packages/mezz/jei/core/search/$PrefixInfo$IStringsGetter"

export class $PrefixInfo<T> {

constructor(arg0: character, arg1: $PrefixInfo$IModeGetter$Type, arg2: $PrefixInfo$IStringsGetter$Type<(T)>, arg3: $Supplier$Type<($ISearchStorage$Type<(T)>)>)

public "toString"(): string
public "getStrings"(arg0: T): $Collection<(string)>
public "getPrefix"(): character
public "getMode"(): $SearchMode
public "createStorage"(): $ISearchStorage<(T)>
get "prefix"(): character
get "mode"(): $SearchMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixInfo$Type<T> = ($PrefixInfo<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixInfo_<T> = $PrefixInfo$Type<(T)>;
}}
declare module "packages/mezz/jei/core/search/$PrefixedSearchable" {
import {$ISearchable, $ISearchable$Type} from "packages/mezz/jei/core/search/$ISearchable"
import {$PrefixInfo, $PrefixInfo$Type} from "packages/mezz/jei/core/search/$PrefixInfo"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISearchStorage, $ISearchStorage$Type} from "packages/mezz/jei/core/search/$ISearchStorage"
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"

export class $PrefixedSearchable<T> implements $ISearchable<(T)> {

constructor(arg0: $ISearchStorage$Type<(T)>, arg1: $PrefixInfo$Type<(T)>)

public "getSearchStorage"(): $ISearchStorage<(T)>
public "getStrings"(arg0: T): $Collection<(string)>
public "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
public "getMode"(): $SearchMode
public "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
get "searchStorage"(): $ISearchStorage<(T)>
get "mode"(): $SearchMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixedSearchable$Type<T> = ($PrefixedSearchable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixedSearchable_<T> = $PrefixedSearchable$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder" {
import {$IConfigSchema, $IConfigSchema$Type} from "packages/mezz/jei/common/config/file/$IConfigSchema"
import {$IConfigCategoryBuilder, $IConfigCategoryBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigCategoryBuilder"

export interface $IConfigSchemaBuilder {

 "build"(): $IConfigSchema
 "addCategory"(arg0: string): $IConfigCategoryBuilder
}

export namespace $IConfigSchemaBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigSchemaBuilder$Type = ($IConfigSchemaBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigSchemaBuilder_ = $IConfigSchemaBuilder$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketJei" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $PacketJei {

constructor()

public "getPacketData"(): $Pair<($FriendlyByteBuf), (integer)>
get "packetData"(): $Pair<($FriendlyByteBuf), (integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketJei$Type = ($PacketJei);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketJei_ = $PacketJei$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformRenderHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ItemColors, $ItemColors$Type} from "packages/net/minecraft/client/color/item/$ItemColors"
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $IPlatformRenderHelper {

 "getItemColors"(): $ItemColors
 "renderTooltip"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: $List$Type<($Component$Type)>, arg3: $Optional$Type<($TooltipComponent$Type)>, arg4: integer, arg5: integer, arg6: $Font$Type, arg7: $ItemStack$Type): void
 "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ItemStack$Type): $Font
 "getMainImage"(arg0: $TextureAtlasSprite$Type): $Optional<($NativeImage)>
 "shouldRender"(arg0: $MobEffectInstance$Type): boolean
 "getParticleIcon"(arg0: $BakedModel$Type): $TextureAtlasSprite
}

export namespace $IPlatformRenderHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformRenderHelper$Type = ($IPlatformRenderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformRenderHelper_ = $IPlatformRenderHelper$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigValue" {
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export interface $IJeiConfigValue<T> {

 "getName"(): string
 "getValue"(): T
 "set"(arg0: T): boolean
 "getDefaultValue"(): T
 "getDescription"(): string
 "getSerializer"(): $IJeiConfigValueSerializer<(T)>
}

export namespace $IJeiConfigValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigValue$Type<T> = ($IJeiConfigValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigValue_<T> = $IJeiConfigValue$Type<(T)>;
}}
declare module "packages/mezz/jei/common/network/$ClientPacketData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ClientPacketContext, $ClientPacketContext$Type} from "packages/mezz/jei/common/network/$ClientPacketContext"

export class $ClientPacketData extends $Record {

constructor(buf: $FriendlyByteBuf$Type, context: $ClientPacketContext$Type)

public "context"(): $ClientPacketContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "buf"(): $FriendlyByteBuf
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPacketData$Type = ($ClientPacketData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPacketData_ = $ClientPacketData$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/brewing/$PotionSubtypeInterpreter" {
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $PotionSubtypeInterpreter implements $IIngredientSubtypeInterpreter<($ItemStack)> {
static readonly "INSTANCE": $PotionSubtypeInterpreter


public "apply"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionSubtypeInterpreter$Type = ($PotionSubtypeInterpreter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionSubtypeInterpreter_ = $PotionSubtypeInterpreter$Type;
}}
declare module "packages/mezz/jei/gui/input/$InputType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $InputType extends $Enum<($InputType)> {
static readonly "SIMULATE": $InputType
static readonly "EXECUTE": $InputType
static readonly "IMMEDIATE": $InputType


public static "values"(): ($InputType)[]
public static "valueOf"(arg0: string): $InputType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputType$Type = (("immediate") | ("simulate") | ("execute")) | ($InputType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputType_ = $InputType$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$RecipeRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$RecipeManagerInternal, $RecipeManagerInternal$Type} from "packages/mezz/jei/library/recipes/$RecipeManagerInternal"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RecipeRegistration implements $IRecipeRegistration {

constructor(arg0: $IJeiHelpers$Type, arg1: $IIngredientManager$Type, arg2: $IIngredientVisibility$Type, arg3: $IVanillaRecipeFactory$Type, arg4: $RecipeManagerInternal$Type)

public "getJeiHelpers"(): $IJeiHelpers
public "getVanillaRecipeFactory"(): $IVanillaRecipeFactory
public "getIngredientVisibility"(): $IIngredientVisibility
public "addRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $List$Type<(T)>): void
public "getIngredientManager"(): $IIngredientManager
public "addIngredientInfo"<T>(arg0: $List$Type<(T)>, arg1: $IIngredientType$Type<(T)>, ...arg2: ($Component$Type)[]): void
public "addIngredientInfo"<T>(arg0: T, arg1: $IIngredientType$Type<(T)>, ...arg2: ($Component$Type)[]): void
public "addItemStackInfo"(arg0: $ItemStack$Type, ...arg1: ($Component$Type)[]): void
public "addItemStackInfo"(arg0: $List$Type<($ItemStack$Type)>, ...arg1: ($Component$Type)[]): void
get "jeiHelpers"(): $IJeiHelpers
get "vanillaRecipeFactory"(): $IVanillaRecipeFactory
get "ingredientVisibility"(): $IIngredientVisibility
get "ingredientManager"(): $IIngredientManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeRegistration$Type = ($RecipeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeRegistration_ = $RecipeRegistration$Type;
}}
declare module "packages/mezz/jei/common/config/$IClientToggleState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IClientToggleState {

 "isEditModeEnabled"(): boolean
 "setBookmarkEnabled"(arg0: boolean): void
 "isOverlayEnabled"(): boolean
 "toggleCheatItemsEnabled"(): void
 "toggleBookmarkEnabled"(): void
 "isCheatItemsEnabled"(): boolean
 "isBookmarkOverlayEnabled"(): boolean
 "toggleEditModeEnabled"(): void
 "toggleOverlayEnabled"(): void
 "setCheatItemsEnabled"(arg0: boolean): void
}

export namespace $IClientToggleState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientToggleState$Type = ($IClientToggleState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientToggleState_ = $IClientToggleState$Type;
}}
declare module "packages/mezz/jei/gui/config/$ModNameSortingConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$MappedSortingConfig, $MappedSortingConfig$Type} from "packages/mezz/jei/common/config/sorting/$MappedSortingConfig"

export class $ModNameSortingConfig extends $MappedSortingConfig<($IListElementInfo<(any)>), (string)> {

constructor(arg0: $Path$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModNameSortingConfig$Type = ($ModNameSortingConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModNameSortingConfig_ = $ModNameSortingConfig$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformModHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPlatformModHelper {

 "getModNameForModId"(arg0: string): string
 "isInDev"(): boolean
}

export namespace $IPlatformModHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformModHelper$Type = ($IPlatformModHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformModHelper_ = $IPlatformModHelper$Type;
}}
declare module "packages/mezz/jei/core/search/$ISearchStorage" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $ISearchStorage<T> {

 "put"(arg0: string, arg1: T): void
 "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
 "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
 "statistics"(): string
}

export namespace $ISearchStorage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISearchStorage$Type<T> = ($ISearchStorage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISearchStorage_<T> = $ISearchStorage$Type<(T)>;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigCategory" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $IJeiConfigCategory {

 "getName"(): string
 "getConfigValues"(): $Collection<(any)>
}

export namespace $IJeiConfigCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigCategory$Type = ($IJeiConfigCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigCategory_ = $IJeiConfigCategory$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/anvil/$AnvilRecipeMaker" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IJeiAnvilRecipe, $IJeiAnvilRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $AnvilRecipeMaker {


public static "getAnvilRecipes"(arg0: $IVanillaRecipeFactory$Type, arg1: $IIngredientManager$Type): $List<($IJeiAnvilRecipe)>
public static "findLevelsCost"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilRecipeMaker$Type = ($AnvilRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilRecipeMaker_ = $AnvilRecipeMaker$Type;
}}
declare module "packages/mezz/jei/gui/config/$InternalKeyMappings" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"

export class $InternalKeyMappings implements $IInternalKeyMappings {

constructor(arg0: $Consumer$Type<($KeyMapping$Type)>)

public "getCloseRecipeGui"(): $IJeiKeyMapping
public "getCheatOneItem"(): $IJeiKeyMapping
public "getCheatItemStack"(): $IJeiKeyMapping
public "getShowRecipe"(): $IJeiKeyMapping
public "getShowUses"(): $IJeiKeyMapping
public "getPreviousSearch"(): $IJeiKeyMapping
public "getBookmark"(): $IJeiKeyMapping
public "getNextRecipePage"(): $IJeiKeyMapping
public "getFocusSearch"(): $IJeiKeyMapping
public "getNextCategory"(): $IJeiKeyMapping
public "getToggleOverlay"(): $IJeiKeyMapping
public "getToggleEditMode"(): $IJeiKeyMapping
public "getToggleCheatMode"(): $IJeiKeyMapping
public "getPreviousPage"(): $IJeiKeyMapping
public "getNextPage"(): $IJeiKeyMapping
public "getRecipeBack"(): $IJeiKeyMapping
public "getEnterKey"(): $IJeiKeyMapping
public "getEscapeKey"(): $IJeiKeyMapping
public "getCopyRecipeId"(): $IJeiKeyMapping
public "getRightClick"(): $IJeiKeyMapping
public "getNextSearch"(): $IJeiKeyMapping
public "getLeftClick"(): $IJeiKeyMapping
public "getToggleWildcardHideIngredient"(): $IJeiKeyMapping
public "getToggleCheatModeConfigButton"(): $IJeiKeyMapping
public "getToggleBookmarkOverlay"(): $IJeiKeyMapping
public "getPreviousCategory"(): $IJeiKeyMapping
public "getPreviousRecipePage"(): $IJeiKeyMapping
public "getToggleHideIngredient"(): $IJeiKeyMapping
public "getHoveredClearSearchBar"(): $IJeiKeyMapping
get "closeRecipeGui"(): $IJeiKeyMapping
get "cheatOneItem"(): $IJeiKeyMapping
get "cheatItemStack"(): $IJeiKeyMapping
get "showRecipe"(): $IJeiKeyMapping
get "showUses"(): $IJeiKeyMapping
get "previousSearch"(): $IJeiKeyMapping
get "bookmark"(): $IJeiKeyMapping
get "nextRecipePage"(): $IJeiKeyMapping
get "focusSearch"(): $IJeiKeyMapping
get "nextCategory"(): $IJeiKeyMapping
get "toggleOverlay"(): $IJeiKeyMapping
get "toggleEditMode"(): $IJeiKeyMapping
get "toggleCheatMode"(): $IJeiKeyMapping
get "previousPage"(): $IJeiKeyMapping
get "nextPage"(): $IJeiKeyMapping
get "recipeBack"(): $IJeiKeyMapping
get "enterKey"(): $IJeiKeyMapping
get "escapeKey"(): $IJeiKeyMapping
get "copyRecipeId"(): $IJeiKeyMapping
get "rightClick"(): $IJeiKeyMapping
get "nextSearch"(): $IJeiKeyMapping
get "leftClick"(): $IJeiKeyMapping
get "toggleWildcardHideIngredient"(): $IJeiKeyMapping
get "toggleCheatModeConfigButton"(): $IJeiKeyMapping
get "toggleBookmarkOverlay"(): $IJeiKeyMapping
get "previousCategory"(): $IJeiKeyMapping
get "previousRecipePage"(): $IJeiKeyMapping
get "toggleHideIngredient"(): $IJeiKeyMapping
get "hoveredClearSearchBar"(): $IJeiKeyMapping
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalKeyMappings$Type = ($InternalKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalKeyMappings_ = $InternalKeyMappings$Type;
}}
declare module "packages/mezz/jei/library/gui/$ScreenHelper" {
import {$GuiContainerHandlers, $GuiContainerHandlers$Type} from "packages/mezz/jei/library/gui/$GuiContainerHandlers"
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$IGlobalGuiHandler, $IGlobalGuiHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGlobalGuiHandler"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IScreenHandler, $IScreenHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IScreenHandler"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScreenHelper implements $IScreenHelper {

constructor(arg0: $IIngredientManager$Type, arg1: $List$Type<($IGlobalGuiHandler$Type)>, arg2: $GuiContainerHandlers$Type, arg3: $Map$Type<($Class$Type<(any)>), ($IGhostIngredientHandler$Type<(any)>)>, arg4: $Map$Type<($Class$Type<(any)>), ($IScreenHandler$Type<(any)>)>)

public "getGuiClickableArea"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: double, arg2: double): $Stream<($IGuiClickableArea)>
public "getGuiExclusionAreas"(arg0: $Screen$Type): $Stream<($Rect2i)>
public "getGhostIngredientHandler"<T extends $Screen>(arg0: T): $Optional<($IGhostIngredientHandler<(T)>)>
public "getClickableIngredientUnderMouse"(arg0: $Screen$Type, arg1: double, arg2: double): $Stream<($IClickableIngredient<(any)>)>
public "getGuiProperties"<T extends $Screen>(arg0: T): $Optional<($IGuiProperties)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHelper$Type = ($ScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHelper_ = $ScreenHelper$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$RecipeCategoryRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$JeiHelpers, $JeiHelpers$Type} from "packages/mezz/jei/library/runtime/$JeiHelpers"

export class $RecipeCategoryRegistration implements $IRecipeCategoryRegistration {

constructor(arg0: $JeiHelpers$Type)

public "getJeiHelpers"(): $IJeiHelpers
public "addRecipeCategories"(...arg0: ($IRecipeCategory$Type<(any)>)[]): void
public "getRecipeCategories"(): $List<($IRecipeCategory<(any)>)>
get "jeiHelpers"(): $IJeiHelpers
get "recipeCategories"(): $List<($IRecipeCategory<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryRegistration$Type = ($RecipeCategoryRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryRegistration_ = $RecipeCategoryRegistration$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/layout/builder/$IRecipeLayoutSlotSource" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IntSet, $IntSet$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntSet"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$RecipeSlots, $RecipeSlots$Type} from "packages/mezz/jei/library/gui/ingredients/$RecipeSlots"

export interface $IRecipeLayoutSlotSource {

 "getIngredientCount"(): integer
 "setRecipeSlots"(arg0: $RecipeSlots$Type, arg1: $IntSet$Type, arg2: $IIngredientVisibility$Type): void
 "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
 "getMatches"(arg0: $IFocusGroup$Type): $IntSet
 "getIngredientTypes"(): $Stream<($IIngredientType<(any)>)>
 "getRole"(): $RecipeIngredientRole
}

export namespace $IRecipeLayoutSlotSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeLayoutSlotSource$Type = ($IRecipeLayoutSlotSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeLayoutSlotSource_ = $IRecipeLayoutSlotSource$Type;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$EnumSerializer" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $EnumSerializer<T extends $Enum<(T)>> implements $IJeiConfigValueSerializer<(T)> {

constructor(arg0: $Class$Type<(T)>)

public "isValid"(arg0: T): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<(T)>)>
public "serialize"(arg0: T): string
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumSerializer$Type<T> = ($EnumSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumSerializer_<T> = $EnumSerializer$Type<(T)>;
}}
declare module "packages/mezz/jei/common/transfer/$TransferOperation" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $TransferOperation extends $Record {

constructor(inventorySlot: $Slot$Type, craftingSlot: $Slot$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "craftingSlot"(): $Slot
public "inventorySlot"(): $Slot
public static "readPacketData"(arg0: $FriendlyByteBuf$Type, arg1: $AbstractContainerMenu$Type): $TransferOperation
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferOperation$Type = ($TransferOperation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferOperation_ = $TransferOperation$Type;
}}
declare module "packages/mezz/jei/common/config/$ClientToggleState" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"

export class $ClientToggleState implements $IClientToggleState {

constructor()

public "isEditModeEnabled"(): boolean
public "setBookmarkEnabled"(arg0: boolean): void
public "isOverlayEnabled"(): boolean
public "toggleCheatItemsEnabled"(): void
public "toggleBookmarkEnabled"(): void
public "isCheatItemsEnabled"(): boolean
public "isBookmarkOverlayEnabled"(): boolean
public "toggleEditModeEnabled"(): void
public "toggleOverlayEnabled"(): void
public "setCheatItemsEnabled"(arg0: boolean): void
get "editModeEnabled"(): boolean
set "bookmarkEnabled"(value: boolean)
get "overlayEnabled"(): boolean
get "cheatItemsEnabled"(): boolean
get "bookmarkOverlayEnabled"(): boolean
set "cheatItemsEnabled"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientToggleState$Type = ($ClientToggleState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientToggleState_ = $ClientToggleState$Type;
}}
declare module "packages/mezz/jei/gui/input/$UserInput" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$UserInput$MouseClickable, $UserInput$MouseClickable$Type} from "packages/mezz/jei/gui/input/$UserInput$MouseClickable"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput$KeyPressable, $UserInput$KeyPressable$Type} from "packages/mezz/jei/gui/input/$UserInput$KeyPressable"
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"
import {$InputType, $InputType$Type} from "packages/mezz/jei/gui/input/$InputType"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$UserInput$MouseOverable, $UserInput$MouseOverable$Type} from "packages/mezz/jei/gui/input/$UserInput$MouseOverable"

export class $UserInput {

constructor(arg0: $InputConstants$Key$Type, arg1: double, arg2: double, arg3: integer, arg4: $InputType$Type)

public "isAllowedChatCharacter"(): boolean
public "toString"(): string
public "getKey"(): $InputConstants$Key
public "is"(arg0: $KeyMapping$Type): boolean
public "is"(arg0: $IJeiKeyMapping$Type): boolean
public "getMouseX"(): double
public "getMouseY"(): double
public static "fromVanilla"(arg0: integer, arg1: integer, arg2: integer, arg3: $InputType$Type): $UserInput
public static "fromVanilla"(arg0: double, arg1: double, arg2: integer, arg3: $InputType$Type): $Optional<($UserInput)>
public "isKeyboard"(): boolean
public "isSimulate"(): boolean
public "getClickState"(): $InputType
public "isMouse"(): boolean
public "callVanilla"(arg0: $UserInput$KeyPressable$Type): boolean
public "callVanilla"(arg0: $UserInput$MouseOverable$Type, arg1: $UserInput$MouseClickable$Type, arg2: $UserInput$KeyPressable$Type): boolean
public "callVanilla"(arg0: $UserInput$MouseOverable$Type, arg1: $UserInput$MouseClickable$Type): boolean
get "allowedChatCharacter"(): boolean
get "key"(): $InputConstants$Key
get "mouseX"(): double
get "mouseY"(): double
get "keyboard"(): boolean
get "simulate"(): boolean
get "clickState"(): $InputType
get "mouse"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserInput$Type = ($UserInput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserInput_ = $UserInput$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/$RecipesGuiDummy" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"

export class $RecipesGuiDummy implements $IRecipesGui {
static readonly "INSTANCE": $IRecipesGui

constructor()

public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
public "show"(arg0: $List$Type<($IFocus$Type<(any)>)>): void
public "showTypes"(arg0: $List$Type<($RecipeType$Type<(any)>)>): void
public "show"<V>(arg0: $IFocus$Type<(V)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipesGuiDummy$Type = ($RecipesGuiDummy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipesGuiDummy_ = $RecipesGuiDummy$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigFile" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $IJeiConfigFile {

 "getPath"(): $Path
 "getCategories"(): $List<(any)>
}

export namespace $IJeiConfigFile {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigFile$Type = ($IJeiConfigFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigFile_ = $IJeiConfigFile$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$GuiAreaInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"

export class $GuiAreaInputHandler implements $IUserInputHandler {

constructor(arg0: $IScreenHelper$Type, arg1: $IRecipesGui$Type, arg2: $IFocusFactory$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiAreaInputHandler$Type = ($GuiAreaInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiAreaInputHandler_ = $GuiAreaInputHandler$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$ProxyInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $ProxyInputHandler implements $IUserInputHandler {

constructor(arg0: $Supplier$Type<($IUserInputHandler$Type)>)

public "toString"(): string
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyInputHandler$Type = ($ProxyInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyInputHandler_ = $ProxyInputHandler$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformHelper" {
import {$IPlatformFluidHelperInternal, $IPlatformFluidHelperInternal$Type} from "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal"
import {$IPlatformModHelper, $IPlatformModHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformModHelper"
import {$IPlatformIngredientHelper, $IPlatformIngredientHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformIngredientHelper"
import {$IPlatformRegistry, $IPlatformRegistry$Type} from "packages/mezz/jei/common/platform/$IPlatformRegistry"
import {$IPlatformConfigHelper, $IPlatformConfigHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformConfigHelper"
import {$IPlatformRecipeHelper, $IPlatformRecipeHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformRecipeHelper"
import {$IPlatformInputHelper, $IPlatformInputHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformInputHelper"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$IPlatformRenderHelper, $IPlatformRenderHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformRenderHelper"
import {$IPlatformScreenHelper, $IPlatformScreenHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformScreenHelper"
import {$IPlatformItemStackHelper, $IPlatformItemStackHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformItemStackHelper"

export interface $IPlatformHelper {

 "getFluidHelper"(): $IPlatformFluidHelperInternal<(any)>
 "getRenderHelper"(): $IPlatformRenderHelper
 "getModHelper"(): $IPlatformModHelper
 "getScreenHelper"(): $IPlatformScreenHelper
 "getItemStackHelper"(): $IPlatformItemStackHelper
 "getRecipeHelper"(): $IPlatformRecipeHelper
 "getConfigHelper"(): $IPlatformConfigHelper
 "getIngredientHelper"(): $IPlatformIngredientHelper
 "getInputHelper"(): $IPlatformInputHelper
 "getRegistry"<T>(arg0: $ResourceKey$Type<(any)>): $IPlatformRegistry<(T)>
}

export namespace $IPlatformHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformHelper$Type = ($IPlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformHelper_ = $IPlatformHelper$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IIngredientSorter" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$IngredientFilter, $IngredientFilter$Type} from "packages/mezz/jei/gui/ingredients/$IngredientFilter"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export interface $IIngredientSorter {

 "getComparator"(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type): $Comparator<($IListElementInfo<(any)>)>
 "invalidateCache"(): void
 "doPreSort"(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type): void

(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type): $Comparator<($IListElementInfo<(any)>)>
}

export namespace $IIngredientSorter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientSorter$Type = ($IIngredientSorter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientSorter_ = $IIngredientSorter$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$RecipeCatalystRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"

export class $RecipeCatalystRegistration implements $IRecipeCatalystRegistration {

constructor(arg0: $IIngredientManager$Type, arg1: $IJeiHelpers$Type)

public "getRecipeCatalysts"(): $ImmutableListMultimap<($ResourceLocation), ($ITypedIngredient<(any)>)>
public "getJeiHelpers"(): $IJeiHelpers
public "addRecipeCatalyst"<T>(arg0: $IIngredientType$Type<(T)>, arg1: T, ...arg2: ($RecipeType$Type<(any)>)[]): void
public "getIngredientManager"(): $IIngredientManager
public "addRecipeCatalyst"(arg0: $ItemStack$Type, ...arg1: ($RecipeType$Type<(any)>)[]): void
get "recipeCatalysts"(): $ImmutableListMultimap<($ResourceLocation), ($ITypedIngredient<(any)>)>
get "jeiHelpers"(): $IJeiHelpers
get "ingredientManager"(): $IIngredientManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCatalystRegistration$Type = ($RecipeCatalystRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCatalystRegistration_ = $RecipeCatalystRegistration$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$RegisteredIngredients" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/mezz/jei/library/ingredients/$IngredientInfo"

export class $RegisteredIngredients {

constructor(arg0: $List$Type<($IngredientInfo$Type<(any)>)>)

public "getIngredientType"<V>(arg0: V): $Optional<($IIngredientType<(V)>)>
public "getIngredientType"<V>(arg0: $Class$Type<(any)>): $Optional<($IIngredientType<(V)>)>
public "getIngredientInfo"<V>(arg0: $IIngredientType$Type<(V)>): $IngredientInfo<(V)>
public "getIngredientTypes"(): $List<($IIngredientType<(any)>)>
get "ingredientTypes"(): $List<($IIngredientType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisteredIngredients$Type = ($RegisteredIngredients);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisteredIngredients_ = $RegisteredIngredients$Type;
}}
declare module "packages/mezz/jei/gui/search/$ElementSearch" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ElementPrefixParser$TokenInfo, $ElementPrefixParser$TokenInfo$Type} from "packages/mezz/jei/gui/search/$ElementPrefixParser$TokenInfo"
import {$ElementPrefixParser, $ElementPrefixParser$Type} from "packages/mezz/jei/gui/search/$ElementPrefixParser"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$IElementSearch, $IElementSearch$Type} from "packages/mezz/jei/gui/search/$IElementSearch"

export class $ElementSearch implements $IElementSearch {

constructor(arg0: $ElementPrefixParser$Type)

public "add"(arg0: $IListElementInfo$Type<(any)>): void
public "getSearchResults"(arg0: $ElementPrefixParser$TokenInfo$Type): $Set<($IListElementInfo<(any)>)>
public "logStatistics"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementSearch$Type = ($ElementSearch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementSearch_ = $ElementSearch$Type;
}}
declare module "packages/mezz/jei/core/search/$CombinedSearchables" {
import {$ISearchable, $ISearchable$Type} from "packages/mezz/jei/core/search/$ISearchable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"

export class $CombinedSearchables<T> implements $ISearchable<(T)> {

constructor()

public "addSearchable"(arg0: $ISearchable$Type<(T)>): void
public "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
public "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
public "getMode"(): $SearchMode
get "mode"(): $SearchMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedSearchables$Type<T> = ($CombinedSearchables<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedSearchables_<T> = $CombinedSearchables$Type<(T)>;
}}
declare module "packages/mezz/jei/common/network/packets/handlers/$ClientCheatPermissionHandler" {
import {$ClientPacketContext, $ClientPacketContext$Type} from "packages/mezz/jei/common/network/$ClientPacketContext"

export class $ClientCheatPermissionHandler {

constructor()

public static "handleHasCheatPermission"(arg0: $ClientPacketContext$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCheatPermissionHandler$Type = ($ClientCheatPermissionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCheatPermissionHandler_ = $ClientCheatPermissionHandler$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$RuntimeRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RuntimeRegistration implements $IRuntimeRegistration {

constructor(arg0: $IRecipeManager$Type, arg1: $IJeiHelpers$Type, arg2: $IEditModeConfig$Type, arg3: $IIngredientManager$Type, arg4: $IIngredientVisibility$Type, arg5: $IRecipeTransferManager$Type, arg6: $IScreenHelper$Type)

public "getRecipesGui"(): $IRecipesGui
public "getJeiHelpers"(): $IJeiHelpers
public "getScreenHelper"(): $IScreenHelper
public "getBookmarkOverlay"(): $IBookmarkOverlay
public "getIngredientVisibility"(): $IIngredientVisibility
public "getRecipeTransferManager"(): $IRecipeTransferManager
public "setIngredientFilter"(arg0: $IIngredientFilter$Type): void
public "setIngredientListOverlay"(arg0: $IIngredientListOverlay$Type): void
public "getIngredientFilter"(): $IIngredientFilter
public "getIngredientManager"(): $IIngredientManager
public "getEditModeConfig"(): $IEditModeConfig
public "setBookmarkOverlay"(arg0: $IBookmarkOverlay$Type): void
public "setRecipesGui"(arg0: $IRecipesGui$Type): void
public "getRecipeManager"(): $IRecipeManager
public "getIngredientListOverlay"(): $IIngredientListOverlay
get "recipesGui"(): $IRecipesGui
get "jeiHelpers"(): $IJeiHelpers
get "screenHelper"(): $IScreenHelper
get "bookmarkOverlay"(): $IBookmarkOverlay
get "ingredientVisibility"(): $IIngredientVisibility
get "recipeTransferManager"(): $IRecipeTransferManager
set "ingredientFilter"(value: $IIngredientFilter$Type)
set "ingredientListOverlay"(value: $IIngredientListOverlay$Type)
get "ingredientFilter"(): $IIngredientFilter
get "ingredientManager"(): $IIngredientManager
get "editModeConfig"(): $IEditModeConfig
set "bookmarkOverlay"(value: $IBookmarkOverlay$Type)
set "recipesGui"(value: $IRecipesGui$Type)
get "recipeManager"(): $IRecipeManager
get "ingredientListOverlay"(): $IIngredientListOverlay
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeRegistration$Type = ($RuntimeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeRegistration_ = $RuntimeRegistration$Type;
}}
declare module "packages/mezz/jei/forge/input/$JeiForgeKeyConflictContexts" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$IKeyConflictContext, $IKeyConflictContext$Type} from "packages/net/minecraftforge/client/settings/$IKeyConflictContext"

export class $JeiForgeKeyConflictContexts extends $Enum<($JeiForgeKeyConflictContexts)> implements $IKeyConflictContext {
static readonly "JEI_GUI_HOVER": $JeiForgeKeyConflictContexts
static readonly "JEI_GUI_HOVER_CHEAT_MODE": $JeiForgeKeyConflictContexts
static readonly "JEI_GUI_HOVER_CONFIG_BUTTON": $JeiForgeKeyConflictContexts
static readonly "JEI_GUI_HOVER_SEARCH": $JeiForgeKeyConflictContexts


public static "values"(): ($JeiForgeKeyConflictContexts)[]
public static "valueOf"(arg0: string): $JeiForgeKeyConflictContexts
public "isActive"(): boolean
public "conflicts"(arg0: $IKeyConflictContext$Type): boolean
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiForgeKeyConflictContexts$Type = (("jei_gui_hover_cheat_mode") | ("jei_gui_hover") | ("jei_gui_hover_search") | ("jei_gui_hover_config_button")) | ($JeiForgeKeyConflictContexts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiForgeKeyConflictContexts_ = $JeiForgeKeyConflictContexts$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$FurnaceVariantCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $FurnaceVariantCategory<T> implements $IRecipeCategory<(T)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<(T)>
public "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: T): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: T): $ResourceLocation
get "recipeType"(): $RecipeType<(T)>
get "width"(): integer
get "height"(): integer
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceVariantCategory$Type<T> = ($FurnaceVariantCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceVariantCategory_<T> = $FurnaceVariantCategory$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/$CraftingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ICraftingCategoryExtension, $ICraftingCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IExtendableRecipeCategory, $IExtendableRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IExtendableRecipeCategory"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $CraftingRecipeCategory implements $IExtendableRecipeCategory<($CraftingRecipe), ($ICraftingCategoryExtension)> {
static readonly "width": integer
static readonly "height": integer

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($CraftingRecipe)>
public "draw"(arg0: $CraftingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "addCategoryExtension"<R extends $CraftingRecipe>(arg0: $Class$Type<(any)>, arg1: $Function$Type<(R), (any)>): void
public "addCategoryExtension"<R extends $CraftingRecipe>(arg0: $Class$Type<(any)>, arg1: $Predicate$Type<(R)>, arg2: $Function$Type<(R), (any)>): void
public "isHandled"(arg0: $CraftingRecipe$Type): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "handleInput"(arg0: $CraftingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $CraftingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(arg0: $CraftingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $CraftingRecipe$Type): $ResourceLocation
public "getWidth"(): integer
public "getHeight"(): integer
get "recipeType"(): $RecipeType<($CraftingRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingRecipeCategory$Type = ($CraftingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingRecipeCategory_ = $CraftingRecipeCategory$Type;
}}
declare module "packages/mezz/jei/common/util/$ImmutableSize2i" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImmutableSize2i {
static readonly "EMPTY": $ImmutableSize2i

constructor(arg0: integer, arg1: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getArea"(): integer
get "width"(): integer
get "height"(): integer
get "area"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmutableSize2i$Type = ($ImmutableSize2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmutableSize2i_ = $ImmutableSize2i$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IEditModeConfig" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IEditModeConfig$HideMode, $IEditModeConfig$HideMode$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig$HideMode"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IEditModeConfig {

 "hideIngredientUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type): void
 "showIngredientUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type): void
 "getIngredientHiddenUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>): $Set<($IEditModeConfig$HideMode)>
 "isIngredientHiddenUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>): boolean
}

export namespace $IEditModeConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEditModeConfig$Type = ($IEditModeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEditModeConfig_ = $IEditModeConfig$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$AbstractJeiKeyMappingBuilder" {
import {$JeiKeyConflictContext, $JeiKeyConflictContext$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyConflictContext"
import {$JeiKeyModifier, $JeiKeyModifier$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyModifier"
import {$IJeiKeyMappingBuilder, $IJeiKeyMappingBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingBuilder"
import {$IJeiKeyMappingInternal, $IJeiKeyMappingInternal$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingInternal"

export class $AbstractJeiKeyMappingBuilder implements $IJeiKeyMappingBuilder {

constructor()

public "buildMouseRight"(): $IJeiKeyMappingInternal
public "buildMouseMiddle"(): $IJeiKeyMappingInternal
public "buildMouseLeft"(): $IJeiKeyMappingInternal
public "buildUnbound"(): $IJeiKeyMappingInternal
public "setContext"(arg0: $JeiKeyConflictContext$Type): $IJeiKeyMappingBuilder
public "buildKeyboardKey"(arg0: integer): $IJeiKeyMappingInternal
public "setModifier"(arg0: $JeiKeyModifier$Type): $IJeiKeyMappingBuilder
set "context"(value: $JeiKeyConflictContext$Type)
set "modifier"(value: $JeiKeyModifier$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractJeiKeyMappingBuilder$Type = ($AbstractJeiKeyMappingBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractJeiKeyMappingBuilder_ = $AbstractJeiKeyMappingBuilder$Type;
}}
declare module "packages/mezz/jei/gui/config/$IBookmarkConfig" {
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export interface $IBookmarkConfig {

 "loadBookmarks"(arg0: $IIngredientManager$Type, arg1: $BookmarkList$Type): void
 "saveBookmarks"(arg0: $IIngredientManager$Type, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>): void
}

export namespace $IBookmarkConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBookmarkConfig$Type = ($IBookmarkConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBookmarkConfig_ = $IBookmarkConfig$Type;
}}
declare module "packages/mezz/jei/core/util/$WeakConsumer" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export class $WeakConsumer<T> implements $Consumer<(T)> {

constructor(arg0: $Consumer$Type<(T)>)

public "accept"(arg0: T): void
public "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeakConsumer$Type<T> = ($WeakConsumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeakConsumer_<T> = $WeakConsumer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/gui/$CraftingGridHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotBuilder, $IRecipeSlotBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"

export class $CraftingGridHelper implements $ICraftingGridHelper {

constructor()

public "createAndSetOutputs"<T>(arg0: $IRecipeLayoutBuilder$Type, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<(T)>): $IRecipeSlotBuilder
public "setInputs"<T>(arg0: $List$Type<($IRecipeSlotBuilder$Type)>, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<($List$Type<(T)>)>, arg3: integer, arg4: integer): void
public "createAndSetInputs"<T>(arg0: $IRecipeLayoutBuilder$Type, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<($List$Type<(T)>)>, arg3: integer, arg4: integer): $List<($IRecipeSlotBuilder)>
public "createAndSetOutputs"(arg0: $IRecipeLayoutBuilder$Type, arg1: $List$Type<($ItemStack$Type)>): $IRecipeSlotBuilder
public "createAndSetInputs"(arg0: $IRecipeLayoutBuilder$Type, arg1: $List$Type<($List$Type<($ItemStack$Type)>)>, arg2: integer, arg3: integer): $List<($IRecipeSlotBuilder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGridHelper$Type = ($CraftingGridHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGridHelper_ = $CraftingGridHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/$VanillaRecipeFactory" {
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $VanillaRecipeFactory implements $IVanillaRecipeFactory {

constructor(arg0: $IIngredientManager$Type)

public "createBrewingRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $List$Type<($ItemStack$Type)>, arg2: $ItemStack$Type): $IJeiBrewingRecipe
public "createBrewingRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $ItemStack$Type, arg2: $ItemStack$Type): $IJeiBrewingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaRecipeFactory$Type = ($VanillaRecipeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaRecipeFactory_ = $VanillaRecipeFactory$Type;
}}
declare module "packages/mezz/jei/forge/network/$NetworkHandler" {
import {$ServerPacketRouter, $ServerPacketRouter$Type} from "packages/mezz/jei/common/network/$ServerPacketRouter"
import {$ClientPacketRouter, $ClientPacketRouter$Type} from "packages/mezz/jei/common/network/$ClientPacketRouter"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $NetworkHandler {

constructor(arg0: $ResourceLocation$Type, arg1: string)

public "registerServerPacketHandler"(arg0: $ServerPacketRouter$Type): void
public "registerClientPacketHandler"(arg0: $ClientPacketRouter$Type): void
public "getChannelId"(): $ResourceLocation
get "channelId"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandler$Type = ($NetworkHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandler_ = $NetworkHandler$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$IngredientManagerBuilder" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISubtypeManager, $ISubtypeManager$Type} from "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $IngredientManagerBuilder implements $IModIngredientRegistration {

constructor(arg0: $ISubtypeManager$Type, arg1: $IColorHelper$Type)

public "register"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>, arg2: $IIngredientHelper$Type<(V)>, arg3: $IIngredientRenderer$Type<(V)>): void
public "build"(): $IIngredientManager
public "getColorHelper"(): $IColorHelper
public "getSubtypeManager"(): $ISubtypeManager
get "colorHelper"(): $IColorHelper
get "subtypeManager"(): $ISubtypeManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientManagerBuilder$Type = ($IngredientManagerBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientManagerBuilder_ = $IngredientManagerBuilder$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/$RecipeLayout" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$RecipeSlots, $RecipeSlots$Type} from "packages/mezz/jei/library/gui/ingredients/$RecipeSlots"

export class $RecipeLayout<R> implements $IRecipeLayoutDrawable<(R)> {
static readonly "RECIPE_TRANSFER_BUTTON_SIZE": integer

constructor(arg0: $IRecipeCategory$Type<(R)>, arg1: $Collection$Type<($IRecipeCategoryDecorator$Type<(R)>)>, arg2: R, arg3: $IIngredientManager$Type, arg4: $IModIdHelper$Type, arg5: $Textures$Type)

public "moveRecipeTransferButton"(arg0: integer, arg1: integer): void
public "getRecipeCategory"(): $IRecipeCategory<(R)>
public "getRecipeSlots"(): $RecipeSlots
public "getRect"(): $Rect2i
public static "create"<T>(arg0: $IRecipeCategory$Type<(T)>, arg1: $Collection$Type<($IRecipeCategoryDecorator$Type<(T)>)>, arg2: T, arg3: $IFocusGroup$Type, arg4: $IIngredientManager$Type, arg5: $IIngredientVisibility$Type, arg6: $IModIdHelper$Type, arg7: $Textures$Type): $Optional<($IRecipeLayoutDrawable<(T)>)>
public "getRecipeTransferButtonArea"(): $Rect2i
public "getRecipeSlotUnderMouse"(arg0: double, arg1: double): $Optional<($IRecipeSlotDrawable)>
public "getIngredientUnderMouse"<T>(arg0: integer, arg1: integer, arg2: $IIngredientType$Type<(T)>): $Optional<(T)>
public "setPosition"(arg0: integer, arg1: integer): void
public "drawRecipe"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRecipeSlotsView"(): $IRecipeSlotsView
public "getRecipe"(): R
public "setShapeless"(): void
public "setShapeless"(arg0: integer, arg1: integer): void
public "drawOverlays"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getItemStackUnderMouse"(arg0: integer, arg1: integer): $Optional<($ItemStack)>
get "recipeCategory"(): $IRecipeCategory<(R)>
get "recipeSlots"(): $RecipeSlots
get "rect"(): $Rect2i
get "recipeTransferButtonArea"(): $Rect2i
get "recipeSlotsView"(): $IRecipeSlotsView
get "recipe"(): R
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeLayout$Type<R> = ($RecipeLayout<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeLayout_<R> = $RecipeLayout$Type<(R)>;
}}
declare module "packages/mezz/jei/library/focus/$FocusGroup" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $FocusGroup implements $IFocusGroup {
static readonly "EMPTY": $IFocusGroup


public "getAllFocuses"(): $List<($IFocus<(any)>)>
public "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $RecipeIngredientRole$Type): $Stream<($IFocus<(T)>)>
public "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<($IFocus<(T)>)>
public "getFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<(any)>)>
public "isEmpty"(): boolean
public static "create"(arg0: $Collection$Type<(any)>, arg1: $IIngredientManager$Type): $IFocusGroup
public "getItemStackFocuses"(): $Stream<($IFocus<($ItemStack)>)>
public "getItemStackFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<($ItemStack)>)>
get "allFocuses"(): $List<($IFocus<(any)>)>
get "empty"(): boolean
get "itemStackFocuses"(): $Stream<($IFocus<($ItemStack)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusGroup$Type = ($FocusGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusGroup_ = $FocusGroup$Type;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler" {
import {$IGhostIngredientHandler$Target, $IGhostIngredientHandler$Target$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler$Target"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IGhostIngredientHandler<T extends $Screen> {

 "shouldHighlightTargets"(): boolean
 "onComplete"(): void
 "getTargetsTyped"<I>(arg0: T, arg1: $ITypedIngredient$Type<(I)>, arg2: boolean): $List<($IGhostIngredientHandler$Target<(I)>)>
}

export namespace $IGhostIngredientHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGhostIngredientHandler$Type<T> = ($IGhostIngredientHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGhostIngredientHandler_<T> = $IGhostIngredientHandler$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/ingredients/$GuiIngredientProperties" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GuiIngredientProperties {

constructor()

public static "getWidth"(arg0: integer): integer
public static "getHeight"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiIngredientProperties$Type = ($GuiIngredientProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiIngredientProperties_ = $GuiIngredientProperties$Type;
}}
declare module "packages/mezz/jei/gui/bookmarks/$BookmarkList" {
import {$IBookmarkConfig, $IBookmarkConfig$Type} from "packages/mezz/jei/gui/config/$IBookmarkConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IIngredientGridSource, $IIngredientGridSource$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientGridSource$SourceListChangedListener, $IIngredientGridSource$SourceListChangedListener$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource$SourceListChangedListener"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $BookmarkList implements $IIngredientGridSource {

constructor(arg0: $IIngredientManager$Type, arg1: $IBookmarkConfig$Type, arg2: $IClientConfig$Type)

public "add"<T>(arg0: $ITypedIngredient$Type<(T)>): boolean
public "remove"<T>(arg0: $ITypedIngredient$Type<(T)>): boolean
public "isEmpty"(): boolean
public static "normalize"<T>(arg0: $IIngredientManager$Type, arg1: $ITypedIngredient$Type<(T)>): $Optional<($ITypedIngredient<(T)>)>
public "addToList"<T>(arg0: $ITypedIngredient$Type<(T)>, arg1: boolean): void
public "getIngredientList"(): $List<($ITypedIngredient<(any)>)>
public "addSourceListChangedListener"(arg0: $IIngredientGridSource$SourceListChangedListener$Type): void
public "notifyListenersOfChange"(): void
get "empty"(): boolean
get "ingredientList"(): $List<($ITypedIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkList$Type = ($BookmarkList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkList_ = $BookmarkList$Type;
}}
declare module "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"

export interface $IRecipeManagerPlugin {

 "getRecipeTypes"<V>(arg0: $IFocus$Type<(V)>): $List<($RecipeType<(any)>)>
 "getRecipes"<T, V>(arg0: $IRecipeCategory$Type<(T)>, arg1: $IFocus$Type<(V)>): $List<(T)>
 "getRecipes"<T>(arg0: $IRecipeCategory$Type<(T)>): $List<(T)>
}

export namespace $IRecipeManagerPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeManagerPlugin$Type = ($IRecipeManagerPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeManagerPlugin_ = $IRecipeManagerPlugin$Type;
}}
declare module "packages/mezz/jei/core/util/$SubString" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SubString {

constructor(arg0: string, arg1: integer, arg2: integer)
constructor(arg0: string, arg1: integer)
constructor(arg0: $SubString$Type)
constructor(arg0: string)

public "length"(): integer
public "toString"(): string
public "append"(arg0: character): $SubString
public "charAt"(arg0: integer): character
public "regionMatches"(arg0: $SubString$Type, arg1: integer): boolean
public "regionMatches"(arg0: integer, arg1: string, arg2: integer, arg3: integer): boolean
public "startsWith"(arg0: $SubString$Type): boolean
public "substring"(arg0: integer): $SubString
public "isEmpty"(): boolean
public "shorten"(arg0: integer): $SubString
public "isPrefix"(arg0: $SubString$Type): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubString$Type = ($SubString);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubString_ = $SubString$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/replacers/$SuspiciousStewRecipeMaker" {
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SuspiciousStewRecipeMaker {


public static "createRecipes"(): $List<($CraftingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SuspiciousStewRecipeMaker$Type = ($SuspiciousStewRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SuspiciousStewRecipeMaker_ = $SuspiciousStewRecipeMaker$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/$VanillaRecipes" {
import {$CampfireCookingRecipe, $CampfireCookingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CampfireCookingRecipe"
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$StonecutterRecipe, $StonecutterRecipe$Type} from "packages/net/minecraft/world/item/crafting/$StonecutterRecipe"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SmeltingRecipe, $SmeltingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmeltingRecipe"
import {$SmokingRecipe, $SmokingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmokingRecipe"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$BlastingRecipe, $BlastingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$BlastingRecipe"

export class $VanillaRecipes {

constructor(arg0: $IIngredientManager$Type)

public "getCampfireCookingRecipes"(arg0: $IRecipeCategory$Type<($CampfireCookingRecipe$Type)>): $List<($CampfireCookingRecipe)>
public "getStonecuttingRecipes"(arg0: $IRecipeCategory$Type<($StonecutterRecipe$Type)>): $List<($StonecutterRecipe)>
public "getSmithingRecipes"(arg0: $IRecipeCategory$Type<($SmithingRecipe$Type)>): $List<($SmithingRecipe)>
public "getCraftingRecipes"(arg0: $IRecipeCategory$Type<($CraftingRecipe$Type)>): $Map<(boolean), ($List<($CraftingRecipe)>)>
public "getFurnaceRecipes"(arg0: $IRecipeCategory$Type<($SmeltingRecipe$Type)>): $List<($SmeltingRecipe)>
public "getBlastingRecipes"(arg0: $IRecipeCategory$Type<($BlastingRecipe$Type)>): $List<($BlastingRecipe)>
public "getSmokingRecipes"(arg0: $IRecipeCategory$Type<($SmokingRecipe$Type)>): $List<($SmokingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaRecipes$Type = ($VanillaRecipes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaRecipes_ = $VanillaRecipes$Type;
}}
declare module "packages/mezz/jei/core/util/function/$CachedFunction" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export class $CachedFunction<T, R> implements $Function<(T), (R)> {

constructor(arg0: $Function$Type<(T), (R)>)

public "apply"(arg0: T): R
public static "identity"<T>(): $Function<(T), (T)>
public "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), (R)>
public "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedFunction$Type<T, R> = ($CachedFunction<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedFunction_<T, R> = $CachedFunction$Type<(T), (R)>;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeTransferButton" {
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GuiIconButtonSmall, $GuiIconButtonSmall$Type} from "packages/mezz/jei/gui/elements/$GuiIconButtonSmall"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $RecipeTransferButton extends $GuiIconButtonSmall {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $IDrawable$Type, arg1: $IRecipeLayoutDrawable$Type<(any)>, arg2: $Textures$Type, arg3: $Runnable$Type)

public "update"(arg0: $Rect2i$Type, arg1: $IRecipeTransferManager$Type, arg2: $AbstractContainerMenu$Type, arg3: $Player$Type): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onRelease"(arg0: double, arg1: double): void
public "drawToolTip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferButton$Type = ($RecipeTransferButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferButton_ = $RecipeTransferButton$Type;
}}
declare module "packages/mezz/jei/library/load/$PluginCallerTimer" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PluginCallerTimer implements $AutoCloseable {

constructor()

public "end"(): void
public "begin"(arg0: string, arg1: $ResourceLocation$Type): void
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginCallerTimer$Type = ($PluginCallerTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginCallerTimer_ = $PluginCallerTimer$Type;
}}
declare module "packages/mezz/jei/common/config/$GiveMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GiveMode extends $Enum<($GiveMode)> {
static readonly "INVENTORY": $GiveMode
static readonly "MOUSE_PICKUP": $GiveMode
static readonly "defaultGiveMode": $GiveMode


public static "values"(): ($GiveMode)[]
public static "valueOf"(arg0: string): $GiveMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GiveMode$Type = (("mouse_pickup") | ("inventory")) | ($GiveMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GiveMode_ = $GiveMode$Type;
}}
declare module "packages/mezz/jei/library/recipes/collect/$RecipeTypeData" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $RecipeTypeData<T> {

constructor(arg0: $IRecipeCategory$Type<(T)>, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>)

public "getRecipeCategoryCatalysts"(): $List<($ITypedIngredient<(any)>)>
public "getRecipeCategory"(): $IRecipeCategory<(T)>
public "getHiddenRecipes"(): $Set<(T)>
public "addRecipes"(arg0: $Collection$Type<(T)>): void
public "getRecipes"(): $List<(T)>
get "recipeCategoryCatalysts"(): $List<($ITypedIngredient<(any)>)>
get "recipeCategory"(): $IRecipeCategory<(T)>
get "hiddenRecipes"(): $Set<(T)>
get "recipes"(): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTypeData$Type<T> = ($RecipeTypeData<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTypeData_<T> = $RecipeTypeData$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/ingredients/$IListElement" {
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IListElement<V> {

 "setVisible"(arg0: boolean): void
 "isVisible"(): boolean
 "getTypedIngredient"(): $ITypedIngredient<(V)>
 "getOrderIndex"(): integer
}

export namespace $IListElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IListElement$Type<V> = ($IListElement<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IListElement_<V> = $IListElement$Type<(V)>;
}}
declare module "packages/mezz/jei/library/plugins/debug/$DebugCategoryDecorator" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $DebugCategoryDecorator<T> implements $IRecipeCategoryDecorator<(T)> {


public static "getInstance"<T>(): $DebugCategoryDecorator<(T)>
public "draw"(arg0: T, arg1: $IRecipeCategory$Type<(T)>, arg2: $IRecipeSlotsView$Type, arg3: $GuiGraphics$Type, arg4: double, arg5: double): void
public "decorateExistingTooltips"(arg0: $List$Type<($Component$Type)>, arg1: T, arg2: $IRecipeCategory$Type<(T)>, arg3: $IRecipeSlotsView$Type, arg4: double, arg5: double): $List<($Component)>
get "instance"(): $DebugCategoryDecorator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugCategoryDecorator$Type<T> = ($DebugCategoryDecorator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugCategoryDecorator_<T> = $DebugCategoryDecorator$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/overlay/$ScreenPropertiesCache" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ScreenPropertiesCache {

constructor(arg0: $IScreenHelper$Type)

public "updateScreen"(arg0: $Screen$Type, arg1: $Set$Type<($ImmutableRect2i$Type)>, arg2: $Runnable$Type): void
public "getGuiExclusionAreas"(): $Set<($ImmutableRect2i)>
public "hasValidScreen"(): boolean
public "getGuiProperties"(): $Optional<($IGuiProperties)>
get "guiExclusionAreas"(): $Set<($ImmutableRect2i)>
get "guiProperties"(): $Optional<($IGuiProperties)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenPropertiesCache$Type = ($ScreenPropertiesCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenPropertiesCache_ = $ScreenPropertiesCache$Type;
}}
declare module "packages/mezz/jei/common/gui/textures/$JeiSpriteUploader" {
import {$TextureAtlasHolder, $TextureAtlasHolder$Type} from "packages/net/minecraft/client/resources/$TextureAtlasHolder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TextureManager, $TextureManager$Type} from "packages/net/minecraft/client/renderer/texture/$TextureManager"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $JeiSpriteUploader extends $TextureAtlasHolder {

constructor(arg0: $TextureManager$Type)

public "m_118901_"(arg0: $ResourceLocation$Type): $TextureAtlasSprite
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiSpriteUploader$Type = ($JeiSpriteUploader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiSpriteUploader_ = $JeiSpriteUploader$Type;
}}
declare module "packages/mezz/jei/library/transfer/$BasicRecipeTransferInfo" {
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"

export class $BasicRecipeTransferInfo<C extends $AbstractContainerMenu, R> implements $IRecipeTransferInfo<(C), (R)> {

constructor(arg0: $Class$Type<(any)>, arg1: $MenuType$Type<(C)>, arg2: $RecipeType$Type<(R)>, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "getRecipeSlots"(arg0: C, arg1: R): $List<($Slot)>
public "getRecipeType"(): $RecipeType<(R)>
public "getMenuType"(): $Optional<($MenuType<(C)>)>
public "getContainerClass"(): $Class<(any)>
public "canHandle"(arg0: C, arg1: R): boolean
public "getInventorySlots"(arg0: C, arg1: R): $List<($Slot)>
public "requireCompleteSets"(arg0: C, arg1: R): boolean
public "getHandlingError"(arg0: C, arg1: R): $IRecipeTransferError
get "recipeType"(): $RecipeType<(R)>
get "menuType"(): $Optional<($MenuType<(C)>)>
get "containerClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicRecipeTransferInfo$Type<C, R> = ($BasicRecipeTransferInfo<(C), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicRecipeTransferInfo_<C, R> = $BasicRecipeTransferInfo$Type<(C), (R)>;
}}
declare module "packages/mezz/jei/gui/startup/$GuiConfigData" {
import {$ModNameSortingConfig, $ModNameSortingConfig$Type} from "packages/mezz/jei/gui/config/$ModNameSortingConfig"
import {$IBookmarkConfig, $IBookmarkConfig$Type} from "packages/mezz/jei/gui/config/$IBookmarkConfig"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IngredientTypeSortingConfig, $IngredientTypeSortingConfig$Type} from "packages/mezz/jei/gui/config/$IngredientTypeSortingConfig"

export class $GuiConfigData extends $Record {

constructor(bookmarkConfig: $IBookmarkConfig$Type, modNameSortingConfig: $ModNameSortingConfig$Type, ingredientTypeSortingConfig: $IngredientTypeSortingConfig$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(): $GuiConfigData
public "modNameSortingConfig"(): $ModNameSortingConfig
public "ingredientTypeSortingConfig"(): $IngredientTypeSortingConfig
public "bookmarkConfig"(): $IBookmarkConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiConfigData$Type = ($GuiConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiConfigData_ = $GuiConfigData$Type;
}}
declare module "packages/mezz/jei/core/collect/$Table" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$ImmutableTable, $ImmutableTable$Type} from "packages/com/google/common/collect/$ImmutableTable"

export class $Table<R, C, V> {

constructor(arg0: $Map$Type<(R), ($Map$Type<(C), (V)>)>, arg1: $Supplier$Type<($Map$Type<(C), (V)>)>)

public static "hashBasedTable"<R, C, V>(): $Table<(R), (C), (V)>
public "get"(arg0: R, arg1: C): V
public "put"(arg0: R, arg1: C, arg2: V): V
public "clear"(): void
public "contains"(arg0: R, arg1: C): boolean
public "computeIfAbsent"(arg0: R, arg1: C, arg2: $Supplier$Type<(V)>): V
public "getRow"(arg0: R): $Map<(C), (V)>
public "toImmutable"(): $ImmutableTable<(R), (C), (V)>
public static "identityHashBasedTable"<R, C, V>(): $Table<(R), (C), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Table$Type<R, C, V> = ($Table<(R), (C), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Table_<R, C, V> = $Table$Type<(R), (C), (V)>;
}}
declare module "packages/mezz/jei/api/constants/$RecipeTypes" {
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$StonecutterRecipe, $StonecutterRecipe$Type} from "packages/net/minecraft/world/item/crafting/$StonecutterRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IJeiAnvilRecipe, $IJeiAnvilRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe"
import {$SmokingRecipe, $SmokingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmokingRecipe"
import {$BlastingRecipe, $BlastingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$BlastingRecipe"
import {$CampfireCookingRecipe, $CampfireCookingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CampfireCookingRecipe"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$IJeiCompostingRecipe, $IJeiCompostingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiCompostingRecipe"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$SmeltingRecipe, $SmeltingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmeltingRecipe"
import {$IJeiIngredientInfoRecipe, $IJeiIngredientInfoRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiIngredientInfoRecipe"
import {$IJeiFuelingRecipe, $IJeiFuelingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiFuelingRecipe"

export class $RecipeTypes {
static readonly "CRAFTING": $RecipeType<($CraftingRecipe)>
static readonly "STONECUTTING": $RecipeType<($StonecutterRecipe)>
static readonly "SMELTING": $RecipeType<($SmeltingRecipe)>
static readonly "SMOKING": $RecipeType<($SmokingRecipe)>
static readonly "BLASTING": $RecipeType<($BlastingRecipe)>
static readonly "CAMPFIRE_COOKING": $RecipeType<($CampfireCookingRecipe)>
static readonly "FUELING": $RecipeType<($IJeiFuelingRecipe)>
static readonly "BREWING": $RecipeType<($IJeiBrewingRecipe)>
static readonly "ANVIL": $RecipeType<($IJeiAnvilRecipe)>
static readonly "SMITHING": $RecipeType<($SmithingRecipe)>
static readonly "COMPOSTING": $RecipeType<($IJeiCompostingRecipe)>
static readonly "INFORMATION": $RecipeType<($IJeiIngredientInfoRecipe)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTypes$Type = ($RecipeTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTypes_ = $RecipeTypes$Type;
}}
declare module "packages/mezz/jei/gui/startup/$JeiGuiStarter" {
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$JeiEventHandlers, $JeiEventHandlers$Type} from "packages/mezz/jei/gui/startup/$JeiEventHandlers"

export class $JeiGuiStarter {

constructor()

public static "start"(arg0: $IRuntimeRegistration$Type): $JeiEventHandlers
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiGuiStarter$Type = ($JeiGuiStarter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiGuiStarter_ = $JeiGuiStarter$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientListElementFactory" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IListElement, $IListElement$Type} from "packages/mezz/jei/gui/ingredients/$IListElement"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $IngredientListElementFactory {


public static "createList"<V>(arg0: $IIngredientManager$Type, arg1: $IIngredientType$Type<(V)>, arg2: $Collection$Type<(V)>): $List<($IListElement<(V)>)>
public static "createOrderedElement"<V>(arg0: $ITypedIngredient$Type<(V)>): $IListElement<(V)>
public static "createBaseList"(arg0: $IIngredientManager$Type): $NonNullList<($IListElement<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientListElementFactory$Type = ($IngredientListElementFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientListElementFactory_ = $IngredientListElementFactory$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$DisplayNameUtil" {
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"

export class $DisplayNameUtil {


public static "getLowercaseDisplayNameForSearch"<T>(arg0: T, arg1: $IIngredientHelper$Type<(T)>): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayNameUtil$Type = ($DisplayNameUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayNameUtil_ = $DisplayNameUtil$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableResource" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"

export class $DrawableResource implements $IDrawableStatic {

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableResource$Type = ($DrawableResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableResource_ = $DrawableResource$Type;
}}
declare module "packages/mezz/jei/gui/input/$IDragHandler" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $IDragHandler {

 "handleDragCanceled"(): void
 "handleDragStart"(arg0: $Screen$Type, arg1: $UserInput$Type): $Optional<($IDragHandler)>
 "handleDragComplete"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
}

export namespace $IDragHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDragHandler$Type = ($IDragHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDragHandler_ = $IDragHandler$Type;
}}
declare module "packages/mezz/jei/gui/input/$UserInput$MouseClickable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $UserInput$MouseClickable {

 "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean

(arg0: double, arg1: double, arg2: integer): boolean
}

export namespace $UserInput$MouseClickable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserInput$MouseClickable$Type = ($UserInput$MouseClickable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserInput$MouseClickable_ = $UserInput$MouseClickable$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IPlatformFluidHelper" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"

export interface $IPlatformFluidHelper<T> {

 "create"(arg0: $Fluid$Type, arg1: long): T
 "create"(arg0: $Fluid$Type, arg1: long, arg2: $CompoundTag$Type): T
 "bucketVolume"(): long
 "getFluidIngredientType"(): $IIngredientTypeWithSubtypes<($Fluid), (T)>
}

export namespace $IPlatformFluidHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformFluidHelper$Type<T> = ($IPlatformFluidHelper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformFluidHelper_<T> = $IPlatformFluidHelper$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/input/$IPaged" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPaged {

 "hasNext"(): boolean
 "hasPrevious"(): boolean
 "getPageCount"(): integer
 "previousPage"(): boolean
 "nextPage"(): boolean
 "getPageNumber"(): integer
}

export namespace $IPaged {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPaged$Type = ($IPaged);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPaged_ = $IPaged$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$ListElement" {
import {$IListElement, $IListElement$Type} from "packages/mezz/jei/gui/ingredients/$IListElement"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $ListElement<V> implements $IListElement<(V)> {

constructor(arg0: $ITypedIngredient$Type<(V)>, arg1: integer)

public "setVisible"(arg0: boolean): void
public "isVisible"(): boolean
public "getTypedIngredient"(): $ITypedIngredient<(V)>
public "getOrderIndex"(): integer
set "visible"(value: boolean)
get "visible"(): boolean
get "typedIngredient"(): $ITypedIngredient<(V)>
get "orderIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListElement$Type<V> = ($ListElement<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListElement_<V> = $ListElement$Type<(V)>;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientGrid$SlotInfo" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $IngredientGrid$SlotInfo extends $Record {

constructor(total: integer, blocked: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "total"(): integer
public "blocked"(): integer
public "percentBlocked"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientGrid$SlotInfo$Type = ($IngredientGrid$SlotInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientGrid$SlotInfo_ = $IngredientGrid$SlotInfo$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeGuiTab" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $RecipeGuiTab implements $IUserInputHandler {
static readonly "TAB_HEIGHT": integer
static readonly "TAB_WIDTH": integer

constructor(arg0: $Textures$Type, arg1: integer, arg2: integer)

public "draw"(arg0: boolean, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "isSelected"(arg0: $IRecipeCategory$Type<(any)>): boolean
public "getTooltip"(arg0: $IModIdHelper$Type): $List<($Component)>
public "isMouseOver"(arg0: double, arg1: double): boolean
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeGuiTab$Type = ($RecipeGuiTab);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeGuiTab_ = $RecipeGuiTab$Type;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigSerializer" {
import {$ConfigCategory, $ConfigCategory$Type} from "packages/mezz/jei/common/config/file/$ConfigCategory"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ConfigSerializer {

constructor()

public static "load"(arg0: $Path$Type, arg1: $List$Type<($ConfigCategory$Type)>): void
public static "save"(arg0: $Path$Type, arg1: $List$Type<($ConfigCategory$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializer$Type = ($ConfigSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializer_ = $ConfigSerializer$Type;
}}
declare module "packages/mezz/jei/api/forge/$ForgeTypes" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"

export class $ForgeTypes {
static readonly "FLUID_STACK": $IIngredientTypeWithSubtypes<($Fluid), ($FluidStack)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeTypes$Type = ($ForgeTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeTypes_ = $ForgeTypes$Type;
}}
declare module "packages/mezz/jei/api/ingredients/$IIngredientType" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $IIngredientType<T> {

 "getIngredientClass"(): $Class<(any)>
 "castIngredient"(arg0: any): $Optional<(T)>

(): $Class<(any)>
}

export namespace $IIngredientType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientType$Type<T> = ($IIngredientType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientType_<T> = $IIngredientType$Type<(T)>;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler$Target" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $IGhostIngredientHandler$Target<I> extends $Consumer<(I)> {

 "accept"(arg0: I): void
 "getArea"(): $Rect2i
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(I)>
}

export namespace $IGhostIngredientHandler$Target {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGhostIngredientHandler$Target$Type<I> = ($IGhostIngredientHandler$Target<(I)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGhostIngredientHandler$Target_<I> = $IGhostIngredientHandler$Target$Type<(I)>;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$DeserializeResult" {
import {$IJeiConfigValueSerializer$IDeserializeResult, $IJeiConfigValueSerializer$IDeserializeResult$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer$IDeserializeResult"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $DeserializeResult<T> implements $IJeiConfigValueSerializer$IDeserializeResult<(T)> {

constructor(arg0: T, arg1: $List$Type<(string)>)
constructor(arg0: T, arg1: string)
constructor(arg0: T)

public "getResult"(): $Optional<(T)>
public "getErrors"(): $List<(string)>
get "result"(): $Optional<(T)>
get "errors"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeserializeResult$Type<T> = ($DeserializeResult<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeserializeResult_<T> = $DeserializeResult$Type<(T)>;
}}
declare module "packages/mezz/jei/library/runtime/$JeiHelpers" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$GuiHelper, $GuiHelper$Type} from "packages/mezz/jei/library/gui/$GuiHelper"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JeiHelpers implements $IJeiHelpers {

constructor(arg0: $GuiHelper$Type, arg1: $IStackHelper$Type, arg2: $IModIdHelper$Type, arg3: $IFocusFactory$Type, arg4: $IColorHelper$Type, arg5: $IIngredientManager$Type)

public "setRecipeCategories"(arg0: $Collection$Type<($IRecipeCategory$Type<(any)>)>): void
public "getFocusFactory"(): $IFocusFactory
public "getGuiHelper"(): $IGuiHelper
public "getRecipeType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
public "getModIdHelper"(): $IModIdHelper
public "getPlatformFluidHelper"(): $IPlatformFluidHelper<(any)>
public "getIngredientManager"(): $IIngredientManager
public "getAllRecipeTypes"(): $Stream<($RecipeType<(any)>)>
public "getColorHelper"(): $IColorHelper
public "getStackHelper"(): $IStackHelper
set "recipeCategories"(value: $Collection$Type<($IRecipeCategory$Type<(any)>)>)
get "focusFactory"(): $IFocusFactory
get "guiHelper"(): $IGuiHelper
get "modIdHelper"(): $IModIdHelper
get "platformFluidHelper"(): $IPlatformFluidHelper<(any)>
get "ingredientManager"(): $IIngredientManager
get "allRecipeTypes"(): $Stream<($RecipeType<(any)>)>
get "colorHelper"(): $IColorHelper
get "stackHelper"(): $IStackHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiHelpers$Type = ($JeiHelpers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiHelpers_ = $JeiHelpers$Type;
}}
declare module "packages/mezz/jei/gui/filter/$FilterTextSource" {
import {$IFilterTextSource, $IFilterTextSource$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource"
import {$IFilterTextSource$Listener, $IFilterTextSource$Listener$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource$Listener"

export class $FilterTextSource implements $IFilterTextSource {

constructor()

public "getFilterText"(): string
public "setFilterText"(arg0: string): boolean
public "addListener"(arg0: $IFilterTextSource$Listener$Type): void
get "filterText"(): string
set "filterText"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterTextSource$Type = ($FilterTextSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterTextSource_ = $FilterTextSource$Type;
}}
declare module "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IRecipeSlotView {

 "getDisplayedItemStack"(): $Optional<($ItemStack)>
 "isEmpty"(): boolean
 "getAllIngredients"(): $Stream<($ITypedIngredient<(any)>)>
 "getDisplayedIngredient"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
 "getDisplayedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
 "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
 "drawHighlight"(arg0: $GuiGraphics$Type, arg1: integer): void
 "getItemStacks"(): $Stream<($ItemStack)>
 "getSlotName"(): $Optional<(string)>
 "getRole"(): $RecipeIngredientRole
}

export namespace $IRecipeSlotView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeSlotView$Type = ($IRecipeSlotView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeSlotView_ = $IRecipeSlotView$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$NullInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $NullInputHandler implements $IUserInputHandler {
static readonly "INSTANCE": $NullInputHandler


public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullInputHandler$Type = ($NullInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullInputHandler_ = $NullInputHandler$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$VanillaCategoryExtensionRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$ICraftingCategoryExtension, $ICraftingCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension"
import {$JeiHelpers, $JeiHelpers$Type} from "packages/mezz/jei/library/runtime/$JeiHelpers"
import {$IExtendableRecipeCategory, $IExtendableRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IExtendableRecipeCategory"

export class $VanillaCategoryExtensionRegistration implements $IVanillaCategoryExtensionRegistration {

constructor(arg0: $IExtendableRecipeCategory$Type<($CraftingRecipe$Type), ($ICraftingCategoryExtension$Type)>, arg1: $JeiHelpers$Type)

public "getJeiHelpers"(): $IJeiHelpers
public "getCraftingCategory"(): $IExtendableRecipeCategory<($CraftingRecipe), ($ICraftingCategoryExtension)>
get "jeiHelpers"(): $IJeiHelpers
get "craftingCategory"(): $IExtendableRecipeCategory<($CraftingRecipe), ($ICraftingCategoryExtension)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaCategoryExtensionRegistration$Type = ($VanillaCategoryExtensionRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaCategoryExtensionRegistration_ = $VanillaCategoryExtensionRegistration$Type;
}}
declare module "packages/mezz/jei/api/registration/$IRuntimeRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export interface $IRuntimeRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "getScreenHelper"(): $IScreenHelper
 "getIngredientVisibility"(): $IIngredientVisibility
 "getRecipeTransferManager"(): $IRecipeTransferManager
 "setIngredientFilter"(arg0: $IIngredientFilter$Type): void
 "setIngredientListOverlay"(arg0: $IIngredientListOverlay$Type): void
 "getIngredientManager"(): $IIngredientManager
 "getEditModeConfig"(): $IEditModeConfig
 "setBookmarkOverlay"(arg0: $IBookmarkOverlay$Type): void
 "setRecipesGui"(arg0: $IRecipesGui$Type): void
 "getRecipeManager"(): $IRecipeManager
}

export namespace $IRuntimeRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRuntimeRegistration$Type = ($IRuntimeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRuntimeRegistration_ = $IRuntimeRegistration$Type;
}}
declare module "packages/mezz/jei/forge/$JustEnoughItemsClientSafeRunner" {
import {$PermanentEventSubscriptions, $PermanentEventSubscriptions$Type} from "packages/mezz/jei/forge/events/$PermanentEventSubscriptions"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/mezz/jei/forge/network/$NetworkHandler"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"

export class $JustEnoughItemsClientSafeRunner {

constructor(arg0: $NetworkHandler$Type, arg1: $PermanentEventSubscriptions$Type, arg2: $IServerConfig$Type)

public "registerClient"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JustEnoughItemsClientSafeRunner$Type = ($JustEnoughItemsClientSafeRunner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JustEnoughItemsClientSafeRunner_ = $JustEnoughItemsClientSafeRunner$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableBlank" {
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"

export class $DrawableBlank implements $IDrawableStatic, $IDrawableAnimated {

constructor(arg0: integer, arg1: integer)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableBlank$Type = ($DrawableBlank);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableBlank_ = $DrawableBlank$Type;
}}
declare module "packages/mezz/jei/common/network/$ServerPacketContext" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"
import {$IConnectionToClient, $IConnectionToClient$Type} from "packages/mezz/jei/common/network/$IConnectionToClient"

export class $ServerPacketContext extends $Record {

constructor(player: $ServerPlayer$Type, serverConfig: $IServerConfig$Type, connection: $IConnectionToClient$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "connection"(): $IConnectionToClient
public "player"(): $ServerPlayer
public "serverConfig"(): $IServerConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPacketContext$Type = ($ServerPacketContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPacketContext_ = $ServerPacketContext$Type;
}}
declare module "packages/mezz/jei/common/util/$ServerConfigPathUtil" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $ServerConfigPathUtil {


public static "getWorldPath"(arg0: $Path$Type): $Optional<($Path)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerConfigPathUtil$Type = ($ServerConfigPathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerConfigPathUtil_ = $ServerConfigPathUtil$Type;
}}
declare module "packages/mezz/jei/core/collect/$ListMultiMap" {
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MultiMap, $MultiMap$Type} from "packages/mezz/jei/core/collect/$MultiMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ListMultiMap<K, V> extends $MultiMap<(K), (V), ($List<(V)>)> {

constructor(arg0: $Map$Type<(K), ($List$Type<(V)>)>, arg1: $Supplier$Type<($List$Type<(V)>)>)
constructor(arg0: $Supplier$Type<($List$Type<(V)>)>)
constructor()

public "toImmutable"(): $ImmutableListMultimap<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListMultiMap$Type<K, V> = ($ListMultiMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListMultiMap_<K, V> = $ListMultiMap$Type<(K), (V)>;
}}
declare module "packages/mezz/jei/library/util/$BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier {

 "getOutput"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): $ItemStack

(arg0: $ItemStack$Type, arg1: $ItemStack$Type): $ItemStack
}

export namespace $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier$Type = ($BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier_ = $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$DeleteItemInputHandler" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IIngredientGrid, $IIngredientGrid$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGrid"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $DeleteItemInputHandler implements $IUserInputHandler {

constructor(arg0: $IIngredientGrid$Type, arg1: $IClientToggleState$Type, arg2: $IClientConfig$Type, arg3: $IConnectionToServer$Type, arg4: $CheatUtil$Type)

public "shouldDeleteItemOnClick"(arg0: $Minecraft$Type, arg1: double, arg2: double): boolean
public "drawTooltips"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeleteItemInputHandler$Type = ($DeleteItemInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeleteItemInputHandler_ = $DeleteItemInputHandler$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IFocus" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IFocus<V> {

 "checkedCast"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<($IFocus<(T)>)>
 "getRole"(): $RecipeIngredientRole
 "getTypedValue"(): $ITypedIngredient<(V)>
}

export namespace $IFocus {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFocus$Type<V> = ($IFocus<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFocus_<V> = $IFocus$Type<(V)>;
}}
declare module "packages/mezz/jei/gui/recipes/$IOnClickHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IOnClickHandler {

 "onClick"(arg0: double, arg1: double): void

(arg0: double, arg1: double): void
}

export namespace $IOnClickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IOnClickHandler$Type = ($IOnClickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IOnClickHandler_ = $IOnClickHandler$Type;
}}
declare module "packages/mezz/jei/api/recipe/$RecipeType" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RecipeType<T> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(any)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"<T>(arg0: string, arg1: string, arg2: $Class$Type<(any)>): $RecipeType<(T)>
public "getUid"(): $ResourceLocation
public "getRecipeClass"(): $Class<(any)>
get "uid"(): $ResourceLocation
get "recipeClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeType$Type<T> = ($RecipeType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeType_<T> = $RecipeType$Type<(T)>;
}}
declare module "packages/mezz/jei/forge/platform/$ScreenHelper" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeBookComponent, $RecipeBookComponent$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookComponent"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$RecipeBookTabButton, $RecipeBookTabButton$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeBookTabButton"
import {$RecipeUpdateListener, $RecipeUpdateListener$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeUpdateListener"
import {$IPlatformScreenHelper, $IPlatformScreenHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformScreenHelper"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $ScreenHelper implements $IPlatformScreenHelper {

constructor()

public "getTabButtons"(arg0: $RecipeBookComponent$Type): $List<($RecipeBookTabButton)>
public "setFocused"(arg0: $AbstractWidget$Type, arg1: boolean): void
public "getBookArea"(arg0: $RecipeUpdateListener$Type): $ImmutableRect2i
public "getGuiLeft"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getGuiTop"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getXSize"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getYSize"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getSlotUnderMouse"(arg0: $AbstractContainerScreen$Type<(any)>): $Optional<($Slot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHelper$Type = ($ScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHelper_ = $ScreenHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$BlastingCategory" {
import {$AbstractCookingCategory, $AbstractCookingCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$AbstractCookingCategory"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$BlastingRecipe, $BlastingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$BlastingRecipe"

export class $BlastingCategory extends $AbstractCookingCategory<($BlastingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($BlastingRecipe)>
get "recipeType"(): $RecipeType<($BlastingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlastingCategory$Type = ($BlastingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlastingCategory_ = $BlastingCategory$Type;
}}
declare module "packages/mezz/jei/common/input/$ClickableIngredientInternal" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $ClickableIngredientInternal<V> implements $IClickableIngredientInternal<(V)> {

constructor(arg0: $ITypedIngredient$Type<(V)>, arg1: $ImmutableRect2i$Type, arg2: boolean, arg3: boolean)

public "getTypedIngredient"(): $ITypedIngredient<(V)>
public "getArea"(): $ImmutableRect2i
public "canClickToFocus"(): boolean
public "allowsCheating"(): boolean
get "typedIngredient"(): $ITypedIngredient<(V)>
get "area"(): $ImmutableRect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClickableIngredientInternal$Type<V> = ($ClickableIngredientInternal<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClickableIngredientInternal_<V> = $ClickableIngredientInternal$Type<(V)>;
}}
declare module "packages/mezz/jei/library/plugins/debug/$DebugRecipe" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DebugRecipe {

constructor()

public "checkHover"(arg0: double, arg1: double): boolean
public "getButton"(): $Button
public "getRegistryName"(): $ResourceLocation
get "button"(): $Button
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugRecipe$Type = ($DebugRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugRecipe_ = $DebugRecipe$Type;
}}
declare module "packages/mezz/jei/gui/elements/$GuiIconToggleButton" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $GuiIconToggleButton {

constructor(arg0: $IDrawable$Type, arg1: $IDrawable$Type, arg2: $Textures$Type)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "updateBounds"(arg0: $ImmutableRect2i$Type): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "drawTooltips"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "createInputHandler"(): $IUserInputHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiIconToggleButton$Type = ($GuiIconToggleButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiIconToggleButton_ = $GuiIconToggleButton$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/$InventoryEffectRendererGuiHandler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $InventoryEffectRendererGuiHandler<T extends $AbstractContainerMenu> implements $IGuiContainerHandler<($EffectRenderingInventoryScreen<(T)>)> {

constructor()

public "getGuiExtraAreas"(arg0: $EffectRenderingInventoryScreen$Type<(T)>): $List<($Rect2i)>
public "getGuiClickableAreas"(arg0: $EffectRenderingInventoryScreen$Type<(T)>, arg1: double, arg2: double): $Collection<($IGuiClickableArea)>
public "getClickableIngredientUnderMouse"(arg0: $EffectRenderingInventoryScreen$Type<(T)>, arg1: double, arg2: double): $Optional<($IClickableIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryEffectRendererGuiHandler$Type<T> = ($InventoryEffectRendererGuiHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryEffectRendererGuiHandler_<T> = $InventoryEffectRendererGuiHandler$Type<(T)>;
}}
declare module "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $IRecipeLayoutDrawable<R> {

 "getRecipeCategory"(): $IRecipeCategory<(R)>
 "getRect"(): $Rect2i
 "getRecipeTransferButtonArea"(): $Rect2i
 "getRecipeSlotUnderMouse"(arg0: double, arg1: double): $Optional<($IRecipeSlotDrawable)>
 "getItemStackUnderMouse"(arg0: integer, arg1: integer): $Optional<($ItemStack)>
 "getIngredientUnderMouse"<T>(arg0: integer, arg1: integer, arg2: $IIngredientType$Type<(T)>): $Optional<(T)>
 "setPosition"(arg0: integer, arg1: integer): void
 "drawRecipe"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
 "isMouseOver"(arg0: double, arg1: double): boolean
 "getRecipeSlotsView"(): $IRecipeSlotsView
 "getRecipe"(): R
 "drawOverlays"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}

export namespace $IRecipeLayoutDrawable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeLayoutDrawable$Type<R> = ($IRecipeLayoutDrawable<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeLayoutDrawable_<R> = $IRecipeLayoutDrawable$Type<(R)>;
}}
declare module "packages/mezz/jei/api/ingredients/subtypes/$UidContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $UidContext extends $Enum<($UidContext)> {
static readonly "Ingredient": $UidContext
static readonly "Recipe": $UidContext


public static "values"(): ($UidContext)[]
public static "valueOf"(arg0: string): $UidContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UidContext$Type = (("ingredient") | ("recipe")) | ($UidContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UidContext_ = $UidContext$Type;
}}
declare module "packages/mezz/jei/core/util/function/$LazySupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LazySupplier<T> implements $Supplier<(T)> {

constructor(arg0: $Supplier$Type<(T)>)

public "get"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LazySupplier$Type<T> = ($LazySupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LazySupplier_<T> = $LazySupplier$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/$IngredientGridConfig" {
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"
import {$HorizontalAlignment, $HorizontalAlignment$Type} from "packages/mezz/jei/common/util/$HorizontalAlignment"
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$NavigationVisibility, $NavigationVisibility$Type} from "packages/mezz/jei/common/util/$NavigationVisibility"
import {$VerticalAlignment, $VerticalAlignment$Type} from "packages/mezz/jei/common/util/$VerticalAlignment"

export class $IngredientGridConfig implements $IIngredientGridConfig {

constructor(arg0: string, arg1: $IConfigSchemaBuilder$Type, arg2: $HorizontalAlignment$Type)

public "drawBackground"(): boolean
public "getMaxRows"(): integer
public "getMinRows"(): integer
public "getMaxColumns"(): integer
public "getMinColumns"(): integer
public "getHorizontalAlignment"(): $HorizontalAlignment
public "getVerticalAlignment"(): $VerticalAlignment
public "getButtonNavigationVisibility"(): $NavigationVisibility
get "maxRows"(): integer
get "minRows"(): integer
get "maxColumns"(): integer
get "minColumns"(): integer
get "horizontalAlignment"(): $HorizontalAlignment
get "verticalAlignment"(): $VerticalAlignment
get "buttonNavigationVisibility"(): $NavigationVisibility
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientGridConfig$Type = ($IngredientGridConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientGridConfig_ = $IngredientGridConfig$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/$VanillaPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CraftingRecipeCategory, $CraftingRecipeCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/crafting/$CraftingRecipeCategory"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $VanillaPlugin implements $IModPlugin {

constructor()

public "getCraftingCategory"(): $Optional<($CraftingRecipeCategory)>
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "craftingCategory"(): $Optional<($CraftingRecipeCategory)>
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaPlugin$Type = ($VanillaPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaPlugin_ = $VanillaPlugin$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IEditModeConfig$HideMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IEditModeConfig$HideMode extends $Enum<($IEditModeConfig$HideMode)> {
static readonly "SINGLE": $IEditModeConfig$HideMode
static readonly "WILDCARD": $IEditModeConfig$HideMode


public static "values"(): ($IEditModeConfig$HideMode)[]
public static "valueOf"(arg0: string): $IEditModeConfig$HideMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEditModeConfig$HideMode$Type = (("single") | ("wildcard")) | ($IEditModeConfig$HideMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEditModeConfig$HideMode_ = $IEditModeConfig$HideMode$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$JeiMultiKeyMapping" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $JeiMultiKeyMapping implements $IJeiKeyMapping {

constructor(...arg0: ($IJeiKeyMapping$Type)[])

public "isUnbound"(): boolean
public "getTranslatedKeyMessage"(): $Component
public "isActiveAndMatches"(arg0: $InputConstants$Key$Type): boolean
get "unbound"(): boolean
get "translatedKeyMessage"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiMultiKeyMapping$Type = ($JeiMultiKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiMultiKeyMapping_ = $JeiMultiKeyMapping$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/$RecipeLayoutBuilder" {
import {$IIngredientAcceptor, $IIngredientAcceptor$Type} from "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$IIngredientSupplier, $IIngredientSupplier$Type} from "packages/mezz/jei/library/ingredients/$IIngredientSupplier"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IRecipeSlotBuilder, $IRecipeSlotBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$RecipeLayout, $RecipeLayout$Type} from "packages/mezz/jei/library/gui/recipes/$RecipeLayout"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RecipeLayoutBuilder implements $IRecipeLayoutBuilder, $IIngredientSupplier {

constructor(arg0: $IIngredientManager$Type, arg1: integer)

public "getIngredientStream"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $RecipeIngredientRole$Type): $Stream<(T)>
public "moveRecipeTransferButton"(arg0: integer, arg1: integer): void
public "setRecipeLayout"<R>(arg0: $RecipeLayout$Type<(R)>, arg1: $IFocusGroup$Type, arg2: $IIngredientVisibility$Type): void
public "addInvisibleIngredients"(arg0: $RecipeIngredientRole$Type): $IIngredientAcceptor<(any)>
public "isUsed"(): boolean
public "getIngredientTypes"(arg0: $RecipeIngredientRole$Type): $Stream<(any)>
public "setShapeless"(): void
public "setShapeless"(arg0: integer, arg1: integer): void
public "createFocusLink"(...arg0: ($IIngredientAcceptor$Type<(any)>)[]): void
public "addSlot"(arg0: $RecipeIngredientRole$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
get "used"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeLayoutBuilder$Type = ($RecipeLayoutBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeLayoutBuilder_ = $RecipeLayoutBuilder$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IModIdHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IModIdHelper {

 "getModNameForModId"(arg0: string): string
 "isDisplayingModNameEnabled"(): boolean
 "getFormattedModNameForModId"(arg0: string): string
 "addModNameToIngredientTooltip"<T>(arg0: $List$Type<($Component$Type)>, arg1: T, arg2: $IIngredientHelper$Type<(T)>): $List<($Component)>
 "addModNameToIngredientTooltip"<T>(arg0: $List$Type<($Component$Type)>, arg1: $ITypedIngredient$Type<(T)>): $List<($Component)>
}

export namespace $IModIdHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModIdHelper$Type = ($IModIdHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModIdHelper_ = $IModIdHelper$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketRequestCheatPermission" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $PacketRequestCheatPermission extends $PacketJei {

constructor()

public static "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketRequestCheatPermission$Type = ($PacketRequestCheatPermission);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketRequestCheatPermission_ = $PacketRequestCheatPermission$Type;
}}
declare module "packages/mezz/jei/api/recipe/$RecipeIngredientRole" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RecipeIngredientRole extends $Enum<($RecipeIngredientRole)> {
static readonly "INPUT": $RecipeIngredientRole
static readonly "OUTPUT": $RecipeIngredientRole
static readonly "CATALYST": $RecipeIngredientRole
static readonly "RENDER_ONLY": $RecipeIngredientRole


public static "values"(): ($RecipeIngredientRole)[]
public static "valueOf"(arg0: string): $RecipeIngredientRole
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeIngredientRole$Type = (("output") | ("input") | ("catalyst") | ("render_only")) | ($RecipeIngredientRole);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeIngredientRole_ = $RecipeIngredientRole$Type;
}}
declare module "packages/mezz/jei/common/util/$DeduplicatingRunner" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $DeduplicatingRunner {

constructor(arg0: $Runnable$Type, arg1: $Duration$Type, arg2: string)

public "run"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeduplicatingRunner$Type = ($DeduplicatingRunner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeduplicatingRunner_ = $DeduplicatingRunner$Type;
}}
declare module "packages/mezz/jei/common/util/$ExpandNewLineTextAcceptor" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$FormattedText$StyledContentConsumer, $FormattedText$StyledContentConsumer$Type} from "packages/net/minecraft/network/chat/$FormattedText$StyledContentConsumer"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"

export class $ExpandNewLineTextAcceptor implements $FormattedText$StyledContentConsumer<(void)> {

constructor()

public "addLinesTo"(arg0: $List$Type<($FormattedText$Type)>): void
public "accept"(arg0: $Style$Type, arg1: string): $Optional<(void)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpandNewLineTextAcceptor$Type = ($ExpandNewLineTextAcceptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpandNewLineTextAcceptor_ = $ExpandNewLineTextAcceptor$Type;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IScreenHandler" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $IScreenHandler<T extends $Screen> extends $Function<(T), ($IGuiProperties)> {

 "apply"(arg0: T): $IGuiProperties
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($IGuiProperties)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>

(arg0: T): $IGuiProperties
}

export namespace $IScreenHandler {
function identity<T>(): $Function<(T), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IScreenHandler$Type<T> = ($IScreenHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IScreenHandler_<T> = $IScreenHandler$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigSchemaBuilder" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"
import {$IConfigSchema, $IConfigSchema$Type} from "packages/mezz/jei/common/config/file/$IConfigSchema"
import {$IConfigCategoryBuilder, $IConfigCategoryBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigCategoryBuilder"

export class $ConfigSchemaBuilder implements $IConfigSchemaBuilder {

constructor(arg0: $Path$Type)

public "build"(): $IConfigSchema
public "addCategory"(arg0: string): $IConfigCategoryBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSchemaBuilder$Type = ($ConfigSchemaBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSchemaBuilder_ = $ConfigSchemaBuilder$Type;
}}
declare module "packages/mezz/jei/common/config/$ConfigManager" {
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IJeiConfigFile, $IJeiConfigFile$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigFile"

export class $ConfigManager implements $IJeiConfigManager {

constructor()

public "getConfigFiles"(): $Collection<($IJeiConfigFile)>
public "registerConfigFile"(arg0: $IJeiConfigFile$Type): void
get "configFiles"(): $Collection<($IJeiConfigFile)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigManager$Type = ($ConfigManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigManager_ = $ConfigManager$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IRecipeCategoriesLookup" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"

export interface $IRecipeCategoriesLookup {

 "includeHidden"(): $IRecipeCategoriesLookup
 "get"(): $Stream<($IRecipeCategory<(any)>)>
 "limitFocus"(arg0: $Collection$Type<(any)>): $IRecipeCategoriesLookup
 "limitTypes"(arg0: $Collection$Type<($RecipeType$Type<(any)>)>): $IRecipeCategoriesLookup
}

export namespace $IRecipeCategoriesLookup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCategoriesLookup$Type = ($IRecipeCategoriesLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCategoriesLookup_ = $IRecipeCategoriesLookup$Type;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGlobalGuiHandler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $IGlobalGuiHandler {

 "getGuiExtraAreas"(): $Collection<($Rect2i)>
 "getClickableIngredientUnderMouse"(arg0: double, arg1: double): $Optional<($IClickableIngredient<(any)>)>
}

export namespace $IGlobalGuiHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGlobalGuiHandler$Type = ($IGlobalGuiHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGlobalGuiHandler_ = $IGlobalGuiHandler$Type;
}}
declare module "packages/mezz/jei/gui/$GuiProperties" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $GuiProperties implements $IGuiProperties {

constructor(arg0: $Class$Type<(any)>, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public static "create"(arg0: $AbstractContainerScreen$Type<(any)>): $GuiProperties
public static "areEqual"(arg0: $IGuiProperties$Type, arg1: $IGuiProperties$Type): boolean
public "getScreenWidth"(): integer
public "getScreenHeight"(): integer
public "getScreenClass"(): $Class<(any)>
public "getGuiXSize"(): integer
public "getGuiYSize"(): integer
public "getGuiLeft"(): integer
public "getGuiTop"(): integer
public static "getScreenRectangle"(arg0: $IGuiProperties$Type): $ImmutableRect2i
public static "getGuiRight"(arg0: $IGuiProperties$Type): integer
public static "getGuiBottom"(arg0: $IGuiProperties$Type): integer
public static "getGuiRectangle"(arg0: $IGuiProperties$Type): $ImmutableRect2i
get "screenWidth"(): integer
get "screenHeight"(): integer
get "screenClass"(): $Class<(any)>
get "guiXSize"(): integer
get "guiYSize"(): integer
get "guiLeft"(): integer
get "guiTop"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiProperties$Type = ($GuiProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiProperties_ = $GuiProperties$Type;
}}
declare module "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$ICraftingCategoryExtension, $ICraftingCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension"
import {$IExtendableRecipeCategory, $IExtendableRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IExtendableRecipeCategory"

export interface $IVanillaCategoryExtensionRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "getCraftingCategory"(): $IExtendableRecipeCategory<($CraftingRecipe), ($ICraftingCategoryExtension)>
}

export namespace $IVanillaCategoryExtensionRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVanillaCategoryExtensionRegistration$Type = ($IVanillaCategoryExtensionRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVanillaCategoryExtensionRegistration_ = $IVanillaCategoryExtensionRegistration$Type;
}}
declare module "packages/mezz/jei/core/util/$ReflectionUtil" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export class $ReflectionUtil {

constructor()

public "getFieldWithClass"<T>(arg0: any, arg1: $Class$Type<(any)>): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionUtil$Type = ($ReflectionUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionUtil_ = $ReflectionUtil$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeCatalystBuilder" {
import {$RecipeMap, $RecipeMap$Type} from "packages/mezz/jei/library/recipes/collect/$RecipeMap"
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeCatalystBuilder {

constructor(arg0: $IIngredientManager$Type, arg1: $RecipeMap$Type)

public "addCategoryCatalysts"(arg0: $IRecipeCategory$Type<(any)>, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>): void
public "buildRecipeCategoryCatalysts"(): $ImmutableListMultimap<($IRecipeCategory<(any)>), ($ITypedIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCatalystBuilder$Type = ($RecipeCatalystBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCatalystBuilder_ = $RecipeCatalystBuilder$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$EditInputHandler" {
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$CombinedRecipeFocusSource, $CombinedRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$CombinedRecipeFocusSource"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $EditInputHandler implements $IUserInputHandler {

constructor(arg0: $CombinedRecipeFocusSource$Type, arg1: $IClientToggleState$Type, arg2: $IEditModeConfig$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditInputHandler$Type = ($EditInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditInputHandler_ = $EditInputHandler$Type;
}}
declare module "packages/mezz/jei/library/recipes/collect/$RecipeTypeDataMap" {
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$RecipeTypeData, $RecipeTypeData$Type} from "packages/mezz/jei/library/recipes/collect/$RecipeTypeData"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RecipeTypeDataMap {

constructor(arg0: $List$Type<($IRecipeCategory$Type<(any)>)>, arg1: $ImmutableListMultimap$Type<($IRecipeCategory$Type<(any)>), ($ITypedIngredient$Type<(any)>)>)

public "get"<T>(arg0: $Iterable$Type<(any)>, arg1: $RecipeType$Type<(T)>): $RecipeTypeData<(T)>
public "get"<T>(arg0: $RecipeType$Type<(T)>): $RecipeTypeData<(T)>
public "validate"(arg0: $RecipeType$Type<(any)>): void
public "getType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTypeDataMap$Type = ($RecipeTypeDataMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTypeDataMap_ = $RecipeTypeDataMap$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketSetHotbarItemStack" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PacketSetHotbarItemStack extends $PacketJei {

constructor(arg0: $ItemStack$Type, arg1: integer)

public static "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketSetHotbarItemStack$Type = ($PacketSetHotbarItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketSetHotbarItemStack_ = $PacketSetHotbarItemStack$Type;
}}
declare module "packages/mezz/jei/common/config/file/$IConfigSchema" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiConfigFile, $IJeiConfigFile$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigFile"
import {$FileWatcher, $FileWatcher$Type} from "packages/mezz/jei/common/config/file/$FileWatcher"
import {$ConfigManager, $ConfigManager$Type} from "packages/mezz/jei/common/config/$ConfigManager"

export interface $IConfigSchema extends $IJeiConfigFile {

 "register"(arg0: $FileWatcher$Type, arg1: $ConfigManager$Type): void
 "markDirty"(): void
 "loadIfNeeded"(): void
 "getPath"(): $Path
 "getCategories"(): $List<(any)>
}

export namespace $IConfigSchema {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigSchema$Type = ($IConfigSchema);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigSchema_ = $IConfigSchema$Type;
}}
declare module "packages/mezz/jei/library/load/$PluginLoader" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IModIdFormatConfig, $IModIdFormatConfig$Type} from "packages/mezz/jei/library/config/$IModIdFormatConfig"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$JeiHelpers, $JeiHelpers$Type} from "packages/mezz/jei/library/runtime/$JeiHelpers"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$ImmutableTable, $ImmutableTable$Type} from "packages/com/google/common/collect/$ImmutableTable"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$RecipeCategorySortingConfig, $RecipeCategorySortingConfig$Type} from "packages/mezz/jei/library/config/$RecipeCategorySortingConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$RecipeManager, $RecipeManager$Type} from "packages/mezz/jei/library/recipes/$RecipeManager"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$StartData, $StartData$Type} from "packages/mezz/jei/library/startup/$StartData"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$VanillaPlugin, $VanillaPlugin$Type} from "packages/mezz/jei/library/plugins/vanilla/$VanillaPlugin"

export class $PluginLoader {

constructor(arg0: $StartData$Type, arg1: $IModIdFormatConfig$Type, arg2: $IColorHelper$Type)

public "getJeiHelpers"(): $JeiHelpers
public "getIngredientManager"(): $IIngredientManager
public "createRecipeTransferHandlers"(arg0: $List$Type<($IModPlugin$Type)>): $ImmutableTable<($Class<(any)>), ($RecipeType<(any)>), ($IRecipeTransferHandler<(any), (any)>)>
public "createRecipeManager"(arg0: $List$Type<($IModPlugin$Type)>, arg1: $VanillaPlugin$Type, arg2: $RecipeCategorySortingConfig$Type, arg3: $IModIdHelper$Type, arg4: $IIngredientVisibility$Type): $RecipeManager
public "createGuiScreenHelper"(arg0: $List$Type<($IModPlugin$Type)>, arg1: $IJeiHelpers$Type): $IScreenHelper
get "jeiHelpers"(): $JeiHelpers
get "ingredientManager"(): $IIngredientManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginLoader$Type = ($PluginLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginLoader_ = $PluginLoader$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeGuiLogic" {
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IRecipeGuiLogic, $IRecipeGuiLogic$Type} from "packages/mezz/jei/gui/recipes/$IRecipeGuiLogic"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IRecipeLogicStateListener, $IRecipeLogicStateListener$Type} from "packages/mezz/jei/gui/recipes/$IRecipeLogicStateListener"

export class $RecipeGuiLogic implements $IRecipeGuiLogic {

constructor(arg0: $IRecipeManager$Type, arg1: $IRecipeTransferManager$Type, arg2: $IRecipeLogicStateListener$Type, arg3: $IFocusFactory$Type)

public "getRecipeCatalysts"(): $Stream<($ITypedIngredient<(any)>)>
public "getRecipeCatalysts"(arg0: $IRecipeCategory$Type<(any)>): $Stream<($ITypedIngredient<(any)>)>
public "back"(): boolean
public "setFocus"(arg0: $IFocusGroup$Type): boolean
public "hasMultiplePages"(): boolean
public "getSelectedRecipeCategory"(): $IRecipeCategory<(any)>
public "hasMultipleCategories"(): boolean
public "previousRecipeCategory"(): void
public "previousPage"(): void
public "nextPage"(): void
public "getRecipeCategories"(): $List<($IRecipeCategory<(any)>)>
public "nextRecipeCategory"(): void
public "hasAllCategories"(): boolean
public "setRecipesPerPage"(arg0: integer): void
public "getRecipeLayouts"(): $List<($IRecipeLayoutDrawable<(any)>)>
public "clearHistory"(): void
public "getPageString"(): string
public "setCategoryFocus"(arg0: $List$Type<($RecipeType$Type<(any)>)>): boolean
public "setCategoryFocus"(): boolean
public "setRecipeCategory"(arg0: $IRecipeCategory$Type<(any)>): void
get "recipeCatalysts"(): $Stream<($ITypedIngredient<(any)>)>
set "focus"(value: $IFocusGroup$Type)
get "selectedRecipeCategory"(): $IRecipeCategory<(any)>
get "recipeCategories"(): $List<($IRecipeCategory<(any)>)>
set "recipesPerPage"(value: integer)
get "recipeLayouts"(): $List<($IRecipeLayoutDrawable<(any)>)>
get "pageString"(): string
set "categoryFocus"(value: $List$Type<($RecipeType$Type<(any)>)>)
set "recipeCategory"(value: $IRecipeCategory$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeGuiLogic$Type = ($RecipeGuiLogic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeGuiLogic_ = $RecipeGuiLogic$Type;
}}
declare module "packages/mezz/jei/common/network/$ServerPacketData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ServerPacketContext, $ServerPacketContext$Type} from "packages/mezz/jei/common/network/$ServerPacketContext"

export class $ServerPacketData extends $Record {

constructor(buf: $FriendlyByteBuf$Type, context: $ServerPacketContext$Type)

public "context"(): $ServerPacketContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "buf"(): $FriendlyByteBuf
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPacketData$Type = ($ServerPacketData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPacketData_ = $ServerPacketData$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer$IDeserializeResult" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $IJeiConfigValueSerializer$IDeserializeResult<T> {

 "getResult"(): $Optional<(T)>
 "getErrors"(): $List<(string)>
}

export namespace $IJeiConfigValueSerializer$IDeserializeResult {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigValueSerializer$IDeserializeResult$Type<T> = ($IJeiConfigValueSerializer$IDeserializeResult<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigValueSerializer$IDeserializeResult_<T> = $IJeiConfigValueSerializer$IDeserializeResult$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/search/$ElementPrefixParser$TokenInfo" {
import {$PrefixInfo, $PrefixInfo$Type} from "packages/mezz/jei/core/search/$PrefixInfo"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"

export class $ElementPrefixParser$TokenInfo extends $Record {

constructor(token: string, prefixInfo: $PrefixInfo$Type<($IListElementInfo$Type<(any)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "token"(): string
public "prefixInfo"(): $PrefixInfo<($IListElementInfo<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementPrefixParser$TokenInfo$Type = ($ElementPrefixParser$TokenInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementPrefixParser$TokenInfo_ = $ElementPrefixParser$TokenInfo$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/anvil/$AnvilRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IJeiAnvilRecipe, $IJeiAnvilRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe"

export class $AnvilRecipe implements $IJeiAnvilRecipe {

constructor(arg0: $List$Type<($ItemStack$Type)>, arg1: $List$Type<($ItemStack$Type)>, arg2: $List$Type<($ItemStack$Type)>)

public "getOutputs"(): $List<($ItemStack)>
public "getLeftInputs"(): $List<($ItemStack)>
public "getRightInputs"(): $List<($ItemStack)>
get "outputs"(): $List<($ItemStack)>
get "leftInputs"(): $List<($ItemStack)>
get "rightInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilRecipe$Type = ($AnvilRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilRecipe_ = $AnvilRecipe$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeLookup" {
import {$IRecipeLookup, $IRecipeLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeLookup"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeManagerInternal, $RecipeManagerInternal$Type} from "packages/mezz/jei/library/recipes/$RecipeManagerInternal"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeLookup<R> implements $IRecipeLookup<(R)> {

constructor(arg0: $RecipeType$Type<(R)>, arg1: $RecipeManagerInternal$Type, arg2: $IIngredientManager$Type)

public "includeHidden"(): $IRecipeLookup<(R)>
public "get"(): $Stream<(R)>
public "limitFocus"(arg0: $Collection$Type<(any)>): $IRecipeLookup<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeLookup$Type<R> = ($RecipeLookup<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeLookup_<R> = $RecipeLookup$Type<(R)>;
}}
declare module "packages/mezz/jei/library/recipes/collect/$RecipeMap" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IIngredientSupplier, $IIngredientSupplier$Type} from "packages/mezz/jei/library/ingredients/$IIngredientSupplier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeMap {

constructor(arg0: $Comparator$Type<($RecipeType$Type<(any)>)>, arg1: $IIngredientManager$Type, arg2: $RecipeIngredientRole$Type)

public "addCatalystForCategory"(arg0: $RecipeType$Type<(any)>, arg1: string): void
public "isCatalystForRecipeCategory"<T>(arg0: $RecipeType$Type<(T)>, arg1: string): boolean
public "getRecipeTypes"(arg0: string): $Stream<($RecipeType<(any)>)>
public "addRecipe"<T>(arg0: $RecipeType$Type<(T)>, arg1: T, arg2: $IIngredientSupplier$Type): void
public "getRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: string): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeMap$Type = ($RecipeMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeMap_ = $RecipeMap$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$HighResolutionDrawable" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $HighResolutionDrawable implements $IDrawable {

constructor(arg0: $IDrawable$Type, arg1: integer)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "draw"(arg0: $GuiGraphics$Type): void
public "getWidth"(): integer
public "getHeight"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HighResolutionDrawable$Type = ($HighResolutionDrawable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HighResolutionDrawable_ = $HighResolutionDrawable$Type;
}}
declare module "packages/mezz/jei/forge/events/$EventSubscription" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $EventSubscription<T extends $Event> {


public static "register"<T extends $Event>(arg0: $IEventBus$Type, arg1: $Class$Type<(T)>, arg2: $Consumer$Type<(T)>): $EventSubscription<(T)>
public "unregister"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventSubscription$Type<T> = ($EventSubscription<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventSubscription_<T> = $EventSubscription$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredient" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"

export class $DebugIngredient {
static readonly "TYPE": $IIngredientType<($DebugIngredient)>

constructor(arg0: integer)

public "copy"(): $DebugIngredient
public "getNumber"(): integer
get "number"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugIngredient$Type = ($DebugIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugIngredient_ = $DebugIngredient$Type;
}}
declare module "packages/mezz/jei/core/search/$LimitedStringStorage" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISearchStorage, $ISearchStorage$Type} from "packages/mezz/jei/core/search/$ISearchStorage"

export class $LimitedStringStorage<T> implements $ISearchStorage<(T)> {

constructor()

public "put"(arg0: string, arg1: T): void
public "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
public "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
public "statistics"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LimitedStringStorage$Type<T> = ($LimitedStringStorage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LimitedStringStorage_<T> = $LimitedStringStorage$Type<(T)>;
}}
declare module "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryExtension" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IRecipeCategoryExtension {

 "drawInfo"(arg0: integer, arg1: integer, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
 "handleInput"(arg0: double, arg1: double, arg2: $InputConstants$Key$Type): boolean
 "getTooltipStrings"(arg0: double, arg1: double): $List<($Component)>
}

export namespace $IRecipeCategoryExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCategoryExtension$Type = ($IRecipeCategoryExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCategoryExtension_ = $IRecipeCategoryExtension$Type;
}}
declare module "packages/mezz/jei/common/util/$SafeIngredientUtil" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TooltipFlag$Default, $TooltipFlag$Default$Type} from "packages/net/minecraft/world/item/$TooltipFlag$Default"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $SafeIngredientUtil {


public static "render"<T>(arg0: $IIngredientManager$Type, arg1: $IIngredientRenderer$Type<(T)>, arg2: $GuiGraphics$Type, arg3: $ITypedIngredient$Type<(T)>): void
public static "getTooltip"<T>(arg0: $IIngredientManager$Type, arg1: $IIngredientRenderer$Type<(T)>, arg2: $ITypedIngredient$Type<(T)>): $List<($Component)>
public static "getTooltip"<T>(arg0: $IIngredientManager$Type, arg1: $IIngredientRenderer$Type<(T)>, arg2: $ITypedIngredient$Type<(T)>, arg3: $TooltipFlag$Default$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeIngredientUtil$Type = ($SafeIngredientUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeIngredientUtil_ = $SafeIngredientUtil$Type;
}}
declare module "packages/mezz/jei/common/gui/textures/$Textures" {
import {$DrawableNineSliceTexture, $DrawableNineSliceTexture$Type} from "packages/mezz/jei/common/gui/elements/$DrawableNineSliceTexture"
import {$HighResolutionDrawable, $HighResolutionDrawable$Type} from "packages/mezz/jei/common/gui/elements/$HighResolutionDrawable"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$JeiSpriteUploader, $JeiSpriteUploader$Type} from "packages/mezz/jei/common/gui/textures/$JeiSpriteUploader"

export class $Textures {

constructor(arg0: $JeiSpriteUploader$Type)

public "getBookmarkListSlotBackground"(): $DrawableNineSliceTexture
public "getBookmarkButtonDisabledIcon"(): $IDrawableStatic
public "getIngredientListSlotBackground"(): $DrawableNineSliceTexture
public "getBookmarkButtonEnabledIcon"(): $IDrawableStatic
public "getBookmarkListBackground"(): $DrawableNineSliceTexture
public "getRecipeBackground"(): $DrawableNineSliceTexture
public "getSearchBackground"(): $DrawableNineSliceTexture
public "getConfigButtonCheatIcon"(): $IDrawableStatic
public "getRecipeGuiBackground"(): $DrawableNineSliceTexture
public "getIngredientListBackground"(): $DrawableNineSliceTexture
public "getRecipeCatalystSlotBackground"(): $DrawableNineSliceTexture
public "getConfigButtonIcon"(): $IDrawableStatic
public "getArrowPrevious"(): $IDrawableStatic
public "getArrowNext"(): $IDrawableStatic
public "getRecipeTransfer"(): $IDrawableStatic
public "getSlotDrawable"(): $IDrawableStatic
public "getShapelessIcon"(): $HighResolutionDrawable
public "getTabSelected"(): $IDrawableStatic
public "getTabUnselected"(): $IDrawableStatic
public "getCatalystTab"(): $DrawableNineSliceTexture
public "getFlameIcon"(): $IDrawableStatic
public "getButtonForState"(arg0: boolean, arg1: boolean): $DrawableNineSliceTexture
public "getInfoIcon"(): $IDrawableStatic
public "getSpriteUploader"(): $JeiSpriteUploader
get "bookmarkListSlotBackground"(): $DrawableNineSliceTexture
get "bookmarkButtonDisabledIcon"(): $IDrawableStatic
get "ingredientListSlotBackground"(): $DrawableNineSliceTexture
get "bookmarkButtonEnabledIcon"(): $IDrawableStatic
get "bookmarkListBackground"(): $DrawableNineSliceTexture
get "recipeBackground"(): $DrawableNineSliceTexture
get "searchBackground"(): $DrawableNineSliceTexture
get "configButtonCheatIcon"(): $IDrawableStatic
get "recipeGuiBackground"(): $DrawableNineSliceTexture
get "ingredientListBackground"(): $DrawableNineSliceTexture
get "recipeCatalystSlotBackground"(): $DrawableNineSliceTexture
get "configButtonIcon"(): $IDrawableStatic
get "arrowPrevious"(): $IDrawableStatic
get "arrowNext"(): $IDrawableStatic
get "recipeTransfer"(): $IDrawableStatic
get "slotDrawable"(): $IDrawableStatic
get "shapelessIcon"(): $HighResolutionDrawable
get "tabSelected"(): $IDrawableStatic
get "tabUnselected"(): $IDrawableStatic
get "catalystTab"(): $DrawableNineSliceTexture
get "flameIcon"(): $IDrawableStatic
get "infoIcon"(): $IDrawableStatic
get "spriteUploader"(): $JeiSpriteUploader
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Textures$Type = ($Textures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Textures_ = $Textures$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/compostable/$CompostingRecipe" {
import {$IJeiCompostingRecipe, $IJeiCompostingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiCompostingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CompostingRecipe implements $IJeiCompostingRecipe {

constructor(arg0: $ItemStack$Type, arg1: float)

public "getInputs"(): $List<($ItemStack)>
public "getChance"(): float
get "inputs"(): $List<($ItemStack)>
get "chance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompostingRecipe$Type = ($CompostingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompostingRecipe_ = $CompostingRecipe$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IGuiHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$IDrawableBuilder, $IDrawableBuilder$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableBuilder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"

export interface $IGuiHelper {

 "createCraftingGridHelper"(): $ICraftingGridHelper
 "createAnimatedDrawable"(arg0: $IDrawableStatic$Type, arg1: integer, arg2: $IDrawableAnimated$StartDirection$Type, arg3: boolean): $IDrawableAnimated
 "getSlotDrawable"(): $IDrawableStatic
 "createDrawableIngredient"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): $IDrawable
 "createDrawableItemStack"(arg0: $ItemStack$Type): $IDrawable
 "createBlankDrawable"(arg0: integer, arg1: integer): $IDrawableStatic
 "createDrawable"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $IDrawableStatic
 "drawableBuilder"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $IDrawableBuilder
 "createTickTimer"(arg0: integer, arg1: integer, arg2: boolean): $ITickTimer
}

export namespace $IGuiHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiHelper$Type = ($IGuiHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiHelper_ = $IGuiHelper$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientManager" {
import {$IIngredientManager$IIngredientListener, $IIngredientManager$IIngredientListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager$IIngredientListener"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IIngredientManager {

 "getAllIngredients"<V>(arg0: $IIngredientType$Type<(V)>): $Collection<(V)>
 "getIngredientHelper"<V>(arg0: V): $IIngredientHelper<(V)>
 "getIngredientHelper"<V>(arg0: $IIngredientType$Type<(V)>): $IIngredientHelper<(V)>
 "getIngredientByUid"<V>(arg0: $IIngredientType$Type<(V)>, arg1: string): $Optional<(V)>
 "getAllItemStacks"(): $Collection<($ItemStack)>
 "getIngredientRenderer"<V>(arg0: $IIngredientType$Type<(V)>): $IIngredientRenderer<(V)>
 "getIngredientRenderer"<V>(arg0: V): $IIngredientRenderer<(V)>
 "removeIngredientsAtRuntime"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>): void
 "getRegisteredIngredientTypes"(): $Collection<($IIngredientType<(any)>)>
 "addIngredientsAtRuntime"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>): void
 "getIngredientTypeChecked"<V>(arg0: $Class$Type<(any)>): $Optional<($IIngredientType<(V)>)>
 "getIngredientTypeChecked"<V>(arg0: V): $Optional<($IIngredientType<(V)>)>
 "createTypedIngredient"<V>(arg0: V): $Optional<($ITypedIngredient<(V)>)>
 "createTypedIngredient"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): $Optional<($ITypedIngredient<(V)>)>
 "registerIngredientListener"(arg0: $IIngredientManager$IIngredientListener$Type): void
}

export namespace $IIngredientManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientManager$Type = ($IIngredientManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientManager_ = $IIngredientManager$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredientHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ErrorIngredient, $ErrorIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $ErrorIngredientHelper implements $IIngredientHelper<($ErrorIngredient)> {

constructor()

public "getDisplayName"(arg0: $ErrorIngredient$Type): string
public "copyIngredient"(arg0: $ErrorIngredient$Type): $ErrorIngredient
public "getErrorInfo"(arg0: $ErrorIngredient$Type): string
public "getIngredientType"(): $IIngredientType<($ErrorIngredient)>
public "getUniqueId"(arg0: $ErrorIngredient$Type, arg1: $UidContext$Type): string
public "getResourceLocation"(arg0: $ErrorIngredient$Type): $ResourceLocation
public "getWildcardId"(arg0: $ErrorIngredient$Type): string
public "getDisplayModId"(arg0: $ErrorIngredient$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($ErrorIngredient$Type)>): $Optional<($ResourceLocation)>
public "getTagStream"(arg0: $ErrorIngredient$Type): $Stream<($ResourceLocation)>
public "getCheatItemStack"(arg0: $ErrorIngredient$Type): $ItemStack
public "isValidIngredient"(arg0: $ErrorIngredient$Type): boolean
public "normalizeIngredient"(arg0: $ErrorIngredient$Type): $ErrorIngredient
public "isIngredientOnServer"(arg0: $ErrorIngredient$Type): boolean
public "getColors"(arg0: $ErrorIngredient$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($ErrorIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorIngredientHelper$Type = ($ErrorIngredientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorIngredientHelper_ = $ErrorIngredientHelper$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientLookupState" {
import {$FocusedRecipes, $FocusedRecipes$Type} from "packages/mezz/jei/gui/recipes/$FocusedRecipes"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"

export class $IngredientLookupState {


public "getFocuses"(): $IFocusGroup
public "setRecipeCategoryIndex"(arg0: integer): void
public static "createWithCategories"(arg0: $IRecipeManager$Type, arg1: $IFocusFactory$Type, arg2: $List$Type<($IRecipeCategory$Type<(any)>)>): $IngredientLookupState
public "getRecipeCategoryIndex"(): integer
public "previousRecipeCategory"(): void
public "getRecipeCategories"(): $List<($IRecipeCategory<(any)>)>
public "getRecipesPerPage"(): integer
public static "createWithFocus"(arg0: $IRecipeManager$Type, arg1: $IFocusGroup$Type): $IngredientLookupState
public "getRecipeIndex"(): integer
public "getFocusedRecipes"(): $FocusedRecipes<(any)>
public "setRecipeIndex"(arg0: integer): void
public "nextRecipeCategory"(): void
public "setRecipesPerPage"(arg0: integer): void
public "setRecipeCategory"(arg0: $IRecipeCategory$Type<(any)>): boolean
get "focuses"(): $IFocusGroup
set "recipeCategoryIndex"(value: integer)
get "recipeCategoryIndex"(): integer
get "recipeCategories"(): $List<($IRecipeCategory<(any)>)>
get "recipesPerPage"(): integer
get "recipeIndex"(): integer
get "focusedRecipes"(): $FocusedRecipes<(any)>
set "recipeIndex"(value: integer)
set "recipesPerPage"(value: integer)
set "recipeCategory"(value: $IRecipeCategory$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientLookupState$Type = ($IngredientLookupState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientLookupState_ = $IngredientLookupState$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientBlacklistInternal" {
import {$IIngredientManager$IIngredientListener, $IIngredientManager$IIngredientListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager$IIngredientListener"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IngredientBlacklistInternal$IListener, $IngredientBlacklistInternal$IListener$Type} from "packages/mezz/jei/library/ingredients/$IngredientBlacklistInternal$IListener"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $IngredientBlacklistInternal implements $IIngredientManager$IIngredientListener {

constructor()

public "isIngredientBlacklistedByApi"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IIngredientHelper$Type<(V)>): boolean
public "removeIngredientFromBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IIngredientHelper$Type<(V)>): void
public "addIngredientToBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IIngredientHelper$Type<(V)>): void
public "registerListener"(arg0: $IngredientBlacklistInternal$IListener$Type): void
public "onIngredientsAdded"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
public "onIngredientsRemoved"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientBlacklistInternal$Type = ($IngredientBlacklistInternal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientBlacklistInternal_ = $IngredientBlacklistInternal$Type;
}}
declare module "packages/mezz/jei/core/search/suffixtree/$Node" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$IntSummaryStatistics, $IntSummaryStatistics$Type} from "packages/java/util/$IntSummaryStatistics"

export class $Node<T> {


public "toString"(): string
public "getData"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
public "printTree"(arg0: $PrintWriter$Type, arg1: boolean): void
public "nodeSizeStats"(): $IntSummaryStatistics
public "nodeEdgeStats"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$Type<T> = ($Node<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node_<T> = $Node$Type<(T)>;
}}
declare module "packages/mezz/jei/library/util/$BrewingRecipeMakerCommon" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier, $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier$Type} from "packages/mezz/jei/library/util/$BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $BrewingRecipeMakerCommon {

constructor()

public static "getVanillaBrewingRecipes"(arg0: $IVanillaRecipeFactory$Type, arg1: $IIngredientManager$Type, arg2: $BrewingRecipeMakerCommon$IVanillaPotionOutputSupplier$Type): $Set<($IJeiBrewingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeMakerCommon$Type = ($BrewingRecipeMakerCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeMakerCommon_ = $BrewingRecipeMakerCommon$Type;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IJeiFuelingRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IJeiFuelingRecipe {

 "getBurnTime"(): integer
 "getInputs"(): $List<($ItemStack)>
}

export namespace $IJeiFuelingRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiFuelingRecipe$Type = ($IJeiFuelingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiFuelingRecipe_ = $IJeiFuelingRecipe$Type;
}}
declare module "packages/mezz/jei/library/focus/$FocusFactory" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"

export class $FocusFactory implements $IFocusFactory {

constructor(arg0: $IIngredientManager$Type)

public "getEmptyFocusGroup"(): $IFocusGroup
public "createFocusGroup"(arg0: $Collection$Type<(any)>): $IFocusGroup
public "createFocus"<V>(arg0: $RecipeIngredientRole$Type, arg1: $IIngredientType$Type<(V)>, arg2: V): $IFocus<(V)>
public "createFocus"<V>(arg0: $RecipeIngredientRole$Type, arg1: $ITypedIngredient$Type<(V)>): $IFocus<(V)>
get "emptyFocusGroup"(): $IFocusGroup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusFactory$Type = ($FocusFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusFactory_ = $FocusFactory$Type;
}}
declare module "packages/mezz/jei/forge/config/$ServerConfig" {
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"
import {$ModLoadingContext, $ModLoadingContext$Type} from "packages/net/minecraftforge/fml/$ModLoadingContext"

export class $ServerConfig implements $IServerConfig {


public static "register"(arg0: $ModLoadingContext$Type): $IServerConfig
public "isCheatModeEnabledForOp"(): boolean
public "isCheatModeEnabledForGive"(): boolean
public "isCheatModeEnabledForCreative"(): boolean
get "cheatModeEnabledForOp"(): boolean
get "cheatModeEnabledForGive"(): boolean
get "cheatModeEnabledForCreative"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerConfig$Type = ($ServerConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerConfig_ = $ServerConfig$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$SmokingCategory" {
import {$AbstractCookingCategory, $AbstractCookingCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$AbstractCookingCategory"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$SmokingRecipe, $SmokingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmokingRecipe"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $SmokingCategory extends $AbstractCookingCategory<($SmokingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($SmokingRecipe)>
get "recipeType"(): $RecipeType<($SmokingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmokingCategory$Type = ($SmokingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmokingCategory_ = $SmokingCategory$Type;
}}
declare module "packages/mezz/jei/core/util/$LimitedLogger" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Level, $Level$Type} from "packages/org/apache/logging/log4j/$Level"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $LimitedLogger {

constructor(arg0: $Logger$Type, arg1: $Duration$Type)

public "log"(arg0: $Level$Type, arg1: string, arg2: string, ...arg3: (any)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LimitedLogger$Type = ($LimitedLogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LimitedLogger_ = $LimitedLogger$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IJeiRuntime" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IJeiKeyMappings, $IJeiKeyMappings$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMappings"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export interface $IJeiRuntime {

 "getRecipesGui"(): $IRecipesGui
 "getJeiHelpers"(): $IJeiHelpers
 "getScreenHelper"(): $IScreenHelper
 "getBookmarkOverlay"(): $IBookmarkOverlay
 "getIngredientVisibility"(): $IIngredientVisibility
 "getRecipeTransferManager"(): $IRecipeTransferManager
 "getIngredientFilter"(): $IIngredientFilter
 "getIngredientManager"(): $IIngredientManager
 "getEditModeConfig"(): $IEditModeConfig
 "getConfigManager"(): $IJeiConfigManager
 "getRecipeManager"(): $IRecipeManager
 "getIngredientListOverlay"(): $IIngredientListOverlay
 "getKeyMappings"(): $IJeiKeyMappings
}

export namespace $IJeiRuntime {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiRuntime$Type = ($IJeiRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiRuntime_ = $IJeiRuntime$Type;
}}
declare module "packages/mezz/jei/library/ingredients/subtypes/$SubtypeManager" {
import {$ISubtypeManager, $ISubtypeManager$Type} from "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SubtypeInterpreters, $SubtypeInterpreters$Type} from "packages/mezz/jei/library/ingredients/subtypes/$SubtypeInterpreters"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $SubtypeManager implements $ISubtypeManager {

constructor(arg0: $SubtypeInterpreters$Type)

public "getSubtypeInfo"<T>(arg0: $IIngredientTypeWithSubtypes$Type<(any), (T)>, arg1: T, arg2: $UidContext$Type): string
public "getSubtypeInfo"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubtypeManager$Type = ($SubtypeManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubtypeManager_ = $SubtypeManager$Type;
}}
declare module "packages/mezz/jei/forge/input/$ForgeJeiKeyMappingBuilder" {
import {$JeiKeyConflictContext, $JeiKeyConflictContext$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyConflictContext"
import {$JeiKeyModifier, $JeiKeyModifier$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyModifier"
import {$AbstractJeiKeyMappingBuilder, $AbstractJeiKeyMappingBuilder$Type} from "packages/mezz/jei/common/input/keys/$AbstractJeiKeyMappingBuilder"
import {$IJeiKeyMappingBuilder, $IJeiKeyMappingBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingBuilder"
import {$IJeiKeyMappingInternal, $IJeiKeyMappingInternal$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingInternal"

export class $ForgeJeiKeyMappingBuilder extends $AbstractJeiKeyMappingBuilder {

constructor(arg0: string, arg1: string)

public "setContext"(arg0: $JeiKeyConflictContext$Type): $IJeiKeyMappingBuilder
public "buildKeyboardKey"(arg0: integer): $IJeiKeyMappingInternal
public "setModifier"(arg0: $JeiKeyModifier$Type): $IJeiKeyMappingBuilder
set "context"(value: $JeiKeyConflictContext$Type)
set "modifier"(value: $JeiKeyModifier$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeJeiKeyMappingBuilder$Type = ($ForgeJeiKeyMappingBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeJeiKeyMappingBuilder_ = $ForgeJeiKeyMappingBuilder$Type;
}}
declare module "packages/mezz/jei/gui/elements/$GuiIconButton" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiIconButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $IDrawable$Type, arg1: $Button$OnPress$Type, arg2: $Textures$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setHeight"(arg0: integer): void
public "updateBounds"(arg0: $ImmutableRect2i$Type): void
public "createInputHandler"(): $IUserInputHandler
set "height"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiIconButton$Type = ($GuiIconButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiIconButton_ = $GuiIconButton$Type;
}}
declare module "packages/mezz/jei/common/input/$IClickableIngredientInternal" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IClickableIngredientInternal<T> {

 "getTypedIngredient"(): $ITypedIngredient<(T)>
 "getArea"(): $ImmutableRect2i
 "canClickToFocus"(): boolean
 "allowsCheating"(): boolean
}

export namespace $IClickableIngredientInternal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClickableIngredientInternal$Type<T> = ($IClickableIngredientInternal<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClickableIngredientInternal_<T> = $IClickableIngredientInternal$Type<(T)>;
}}
declare module "packages/mezz/jei/library/util/$IngredientSupplierHelper" {
import {$IIngredientSupplier, $IIngredientSupplier$Type} from "packages/mezz/jei/library/ingredients/$IIngredientSupplier"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $IngredientSupplierHelper {


public static "getIngredientSupplier"<T>(arg0: T, arg1: $IRecipeCategory$Type<(T)>, arg2: $IIngredientManager$Type): $IIngredientSupplier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientSupplierHelper$Type = ($IngredientSupplierHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientSupplierHelper_ = $IngredientSupplierHelper$Type;
}}
declare module "packages/mezz/jei/gui/util/$GiveAmount" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $GiveAmount extends $Enum<($GiveAmount)> {
static readonly "ONE": $GiveAmount
static readonly "MAX": $GiveAmount


public static "values"(): ($GiveAmount)[]
public static "valueOf"(arg0: string): $GiveAmount
public "getAmountForStack"(arg0: $ItemStack$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GiveAmount$Type = (("max") | ("one")) | ($GiveAmount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GiveAmount_ = $GiveAmount$Type;
}}
declare module "packages/mezz/jei/forge/platform/$RenderHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ItemColors, $ItemColors$Type} from "packages/net/minecraft/client/color/item/$ItemColors"
import {$IPlatformRenderHelper, $IPlatformRenderHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformRenderHelper"
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $RenderHelper implements $IPlatformRenderHelper {

constructor()

public "getItemColors"(): $ItemColors
public "renderTooltip"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: $List$Type<($Component$Type)>, arg3: $Optional$Type<($TooltipComponent$Type)>, arg4: integer, arg5: integer, arg6: $Font$Type, arg7: $ItemStack$Type): void
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ItemStack$Type): $Font
public "getMainImage"(arg0: $TextureAtlasSprite$Type): $Optional<($NativeImage)>
public "shouldRender"(arg0: $MobEffectInstance$Type): boolean
public "getParticleIcon"(arg0: $BakedModel$Type): $TextureAtlasSprite
get "itemColors"(): $ItemColors
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHelper$Type = ($RenderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHelper_ = $RenderHelper$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableAnimated" {
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"

export class $DrawableAnimated implements $IDrawableAnimated {

constructor(arg0: $IDrawableStatic$Type, arg1: $ITickTimer$Type, arg2: $IDrawableAnimated$StartDirection$Type)
constructor(arg0: $IDrawableStatic$Type, arg1: integer, arg2: $IDrawableAnimated$StartDirection$Type, arg3: boolean)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableAnimated$Type = ($DrawableAnimated);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableAnimated_ = $DrawableAnimated$Type;
}}
declare module "packages/mezz/jei/library/gui/$GuiHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$IDrawableBuilder, $IDrawableBuilder$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableBuilder"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $GuiHelper implements $IGuiHelper {

constructor(arg0: $IIngredientManager$Type)

public "createCraftingGridHelper"(): $ICraftingGridHelper
public "createAnimatedDrawable"(arg0: $IDrawableStatic$Type, arg1: integer, arg2: $IDrawableAnimated$StartDirection$Type, arg3: boolean): $IDrawableAnimated
public "getSlotDrawable"(): $IDrawableStatic
public "createDrawableIngredient"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): $IDrawable
public "createBlankDrawable"(arg0: integer, arg1: integer): $IDrawableStatic
public "drawableBuilder"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $IDrawableBuilder
public "createTickTimer"(arg0: integer, arg1: integer, arg2: boolean): $ITickTimer
public "createDrawableItemStack"(arg0: $ItemStack$Type): $IDrawable
public "createDrawable"(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $IDrawableStatic
get "slotDrawable"(): $IDrawableStatic
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiHelper$Type = ($GuiHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiHelper_ = $GuiHelper$Type;
}}
declare module "packages/mezz/jei/gui/plugins/$JeiGuiPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JeiGuiPlugin implements $IModPlugin {

constructor()

public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiGuiPlugin$Type = ($JeiGuiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiGuiPlugin_ = $JeiGuiPlugin$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/layout/builder/$InvisibleRecipeLayoutSlotSource" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeLayoutSlotSource, $IRecipeLayoutSlotSource$Type} from "packages/mezz/jei/library/gui/recipes/layout/builder/$IRecipeLayoutSlotSource"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientAcceptor, $IIngredientAcceptor$Type} from "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IntSet, $IntSet$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntSet"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$RecipeSlots, $RecipeSlots$Type} from "packages/mezz/jei/library/gui/ingredients/$RecipeSlots"

export class $InvisibleRecipeLayoutSlotSource implements $IRecipeLayoutSlotSource, $IIngredientAcceptor<($InvisibleRecipeLayoutSlotSource)> {

constructor(arg0: $IIngredientManager$Type, arg1: $RecipeIngredientRole$Type)

public "getIngredientCount"(): integer
public "setRecipeSlots"(arg0: $RecipeSlots$Type, arg1: $IntSet$Type, arg2: $IIngredientVisibility$Type): void
public "addIngredient"<I>(arg0: $IIngredientType$Type<(I)>, arg1: I): $InvisibleRecipeLayoutSlotSource
public "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "getMatches"(arg0: $IFocusGroup$Type): $IntSet
public "getIngredientTypes"(): $Stream<($IIngredientType<(any)>)>
public "getRole"(): $RecipeIngredientRole
public "addItemStack"(arg0: $ItemStack$Type): $InvisibleRecipeLayoutSlotSource
public "addIngredients"(arg0: $Ingredient$Type): $InvisibleRecipeLayoutSlotSource
public "addItemStacks"(arg0: $List$Type<($ItemStack$Type)>): $InvisibleRecipeLayoutSlotSource
get "ingredientCount"(): integer
get "ingredientTypes"(): $Stream<($IIngredientType<(any)>)>
get "role"(): $RecipeIngredientRole
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvisibleRecipeLayoutSlotSource$Type = ($InvisibleRecipeLayoutSlotSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvisibleRecipeLayoutSlotSource_ = $InvisibleRecipeLayoutSlotSource$Type;
}}
declare module "packages/mezz/jei/library/render/$ItemStackRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ItemStackRenderer implements $IIngredientRenderer<($ItemStack)> {

constructor()

public "render"(arg0: $GuiGraphics$Type, arg1: $ItemStack$Type): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ItemStack$Type): $Font
public "getTooltip"(arg0: $ItemStack$Type, arg1: $TooltipFlag$Type): $List<($Component)>
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackRenderer$Type = ($ItemStackRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackRenderer_ = $ItemStackRenderer$Type;
}}
declare module "packages/mezz/jei/core/collect/$SetMultiMap" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MultiMap, $MultiMap$Type} from "packages/mezz/jei/core/collect/$MultiMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SetMultiMap<K, V> extends $MultiMap<(K), (V), ($Set<(V)>)> {

constructor(arg0: $Map$Type<(K), ($Set$Type<(V)>)>, arg1: $Supplier$Type<($Set$Type<(V)>)>)
constructor(arg0: $Supplier$Type<($Set$Type<(V)>)>)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetMultiMap$Type<K, V> = ($SetMultiMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetMultiMap_<K, V> = $SetMultiMap$Type<(K), (V)>;
}}
declare module "packages/mezz/jei/gui/startup/$JeiEventHandlers" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GuiEventHandler, $GuiEventHandler$Type} from "packages/mezz/jei/gui/events/$GuiEventHandler"
import {$ClientInputHandler, $ClientInputHandler$Type} from "packages/mezz/jei/gui/input/$ClientInputHandler"

export class $JeiEventHandlers extends $Record {

constructor(guiEventHandler: $GuiEventHandler$Type, clientInputHandler: $ClientInputHandler$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "guiEventHandler"(): $GuiEventHandler
public "clientInputHandler"(): $ClientInputHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiEventHandlers$Type = ($JeiEventHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiEventHandlers_ = $JeiEventHandlers$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/layout/builder/$RecipeSlotBuilder" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeLayoutSlotSource, $IRecipeLayoutSlotSource$Type} from "packages/mezz/jei/library/gui/recipes/layout/builder/$IRecipeLayoutSlotSource"
import {$IRecipeSlotTooltipCallback, $IRecipeSlotTooltipCallback$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IRecipeSlotBuilder, $IRecipeSlotBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$IntSet, $IntSet$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntSet"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$RecipeSlots, $RecipeSlots$Type} from "packages/mezz/jei/library/gui/ingredients/$RecipeSlots"

export class $RecipeSlotBuilder implements $IRecipeSlotBuilder, $IRecipeLayoutSlotSource {

constructor(arg0: $IIngredientManager$Type, arg1: $RecipeIngredientRole$Type, arg2: integer, arg3: integer, arg4: integer)

public "addIngredientsUnsafe"(arg0: $List$Type<(any)>): $IRecipeSlotBuilder
public "getIngredientCount"(): integer
public "setRecipeSlots"(arg0: $RecipeSlots$Type, arg1: $IntSet$Type, arg2: $IIngredientVisibility$Type): void
public "addIngredient"<I>(arg0: $IIngredientType$Type<(I)>, arg1: I): $IRecipeSlotBuilder
public "addTooltipCallback"(arg0: $IRecipeSlotTooltipCallback$Type): $IRecipeSlotBuilder
public "setOverlay"(arg0: $IDrawable$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
public "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "setBackground"(arg0: $IDrawable$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
public "setFluidRenderer"(arg0: long, arg1: boolean, arg2: integer, arg3: integer): $IRecipeSlotBuilder
public "getMatches"(arg0: $IFocusGroup$Type): $IntSet
public "getIngredientTypes"(): $Stream<($IIngredientType<(any)>)>
public "getRole"(): $RecipeIngredientRole
public "setSlotName"(arg0: string): $IRecipeSlotBuilder
public "setCustomRenderer"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $IIngredientRenderer$Type<(T)>): $IRecipeSlotBuilder
public "addItemStack"(arg0: $ItemStack$Type): $IRecipeSlotBuilder
public "addIngredients"(arg0: $Ingredient$Type): $IRecipeSlotBuilder
public "addItemStacks"(arg0: $List$Type<($ItemStack$Type)>): $IRecipeSlotBuilder
get "ingredientCount"(): integer
get "ingredientTypes"(): $Stream<($IIngredientType<(any)>)>
get "role"(): $RecipeIngredientRole
set "slotName"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSlotBuilder$Type = ($RecipeSlotBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSlotBuilder_ = $RecipeSlotBuilder$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$BookmarkInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$CombinedRecipeFocusSource, $CombinedRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$CombinedRecipeFocusSource"
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $BookmarkInputHandler implements $IUserInputHandler {

constructor(arg0: $CombinedRecipeFocusSource$Type, arg1: $BookmarkList$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkInputHandler$Type = ($BookmarkInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkInputHandler_ = $BookmarkInputHandler$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IRecipeTransferError$Type extends $Enum<($IRecipeTransferError$Type)> {
static readonly "INTERNAL": $IRecipeTransferError$Type
static readonly "USER_FACING": $IRecipeTransferError$Type
static readonly "COSMETIC": $IRecipeTransferError$Type
readonly "allowsTransfer": boolean


public static "values"(): ($IRecipeTransferError$Type)[]
public static "valueOf"(arg0: string): $IRecipeTransferError$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferError$Type$Type = (("internal") | ("user_facing") | ("cosmetic")) | ($IRecipeTransferError$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferError$Type_ = $IRecipeTransferError$Type$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IRecipeCatalystLookup" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IRecipeCatalystLookup {

 "includeHidden"(): $IRecipeCatalystLookup
 "get"<S>(arg0: $IIngredientType$Type<(S)>): $Stream<(S)>
 "get"(): $Stream<($ITypedIngredient<(any)>)>
 "getItemStack"(): $Stream<($ItemStack)>
}

export namespace $IRecipeCatalystLookup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCatalystLookup$Type = ($IRecipeCatalystLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCatalystLookup_ = $IRecipeCatalystLookup$Type;
}}
declare module "packages/mezz/jei/common/network/$ClientPacketRouter" {
import {$PacketIdClient, $PacketIdClient$Type} from "packages/mezz/jei/common/network/$PacketIdClient"
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"
import {$EnumMap, $EnumMap$Type} from "packages/java/util/$EnumMap"
import {$IClientPacketHandler, $IClientPacketHandler$Type} from "packages/mezz/jei/common/network/packets/$IClientPacketHandler"

export class $ClientPacketRouter {
readonly "clientHandlers": $EnumMap<($PacketIdClient), ($IClientPacketHandler)>

constructor(arg0: $IConnectionToServer$Type, arg1: $IServerConfig$Type)

public "onPacket"(arg0: $FriendlyByteBuf$Type, arg1: $LocalPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPacketRouter$Type = ($ClientPacketRouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPacketRouter_ = $ClientPacketRouter$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredientListFactory" {
import {$DebugIngredient, $DebugIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$DebugIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $DebugIngredientListFactory {


public static "create"(): $Collection<($DebugIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugIngredientListFactory$Type = ($DebugIngredientListFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugIngredientListFactory_ = $DebugIngredientListFactory$Type;
}}
declare module "packages/mezz/jei/core/util/$LoggedTimer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LoggedTimer {

constructor()

public "start"(arg0: string): void
public "stop"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoggedTimer$Type = ($LoggedTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoggedTimer_ = $LoggedTimer$Type;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigCategoryBuilder" {
import {$ConfigCategory, $ConfigCategory$Type} from "packages/mezz/jei/common/config/file/$ConfigCategory"
import {$ConfigValue, $ConfigValue$Type} from "packages/mezz/jei/common/config/file/$ConfigValue"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConfigSchema, $ConfigSchema$Type} from "packages/mezz/jei/common/config/file/$ConfigSchema"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IConfigCategoryBuilder, $IConfigCategoryBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigCategoryBuilder"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $ConfigCategoryBuilder implements $IConfigCategoryBuilder {

constructor(arg0: string)

public "getName"(): string
public "build"(arg0: $ConfigSchema$Type): $ConfigCategory
public "addValue"<T>(arg0: $ConfigValue$Type<(T)>): $Supplier<(T)>
public "addEnum"<T extends $Enum<(T)>>(arg0: string, arg1: T, arg2: string): $Supplier<(T)>
public "addInteger"(arg0: string, arg1: integer, arg2: integer, arg3: integer, arg4: string): $Supplier<(integer)>
public "addList"<T>(arg0: string, arg1: $List$Type<(T)>, arg2: $IJeiConfigValueSerializer$Type<($List$Type<(T)>)>, arg3: string): $Supplier<($List<(T)>)>
public "addBoolean"(arg0: string, arg1: boolean, arg2: string): $Supplier<(boolean)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCategoryBuilder$Type = ($ConfigCategoryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCategoryBuilder_ = $ConfigCategoryBuilder$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$ErrorIngredient$CrashType, $ErrorIngredient$CrashType$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient$CrashType"

export class $ErrorIngredient {
static readonly "TYPE": $IIngredientType<($ErrorIngredient)>

constructor(arg0: $ErrorIngredient$CrashType$Type)

public "getCrashType"(): $ErrorIngredient$CrashType
get "crashType"(): $ErrorIngredient$CrashType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorIngredient$Type = ($ErrorIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorIngredient_ = $ErrorIngredient$Type;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$SerializeResult" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $SerializeResult {

constructor(arg0: string, arg1: $List$Type<(string)>)
constructor(arg0: string, arg1: string)
constructor(arg0: string)

public "getResult"(): string
public "getErrors"(): $List<(string)>
get "result"(): string
get "errors"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializeResult$Type = ($SerializeResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializeResult_ = $SerializeResult$Type;
}}
declare module "packages/mezz/jei/library/config/$EditModeConfig" {
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IEditModeConfig$HideMode, $IEditModeConfig$HideMode$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig$HideMode"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$EditModeConfig$IListener, $EditModeConfig$IListener$Type} from "packages/mezz/jei/library/config/$EditModeConfig$IListener"
import {$EditModeConfig$ISerializer, $EditModeConfig$ISerializer$Type} from "packages/mezz/jei/library/config/$EditModeConfig$ISerializer"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $EditModeConfig implements $IEditModeConfig {

constructor(arg0: $EditModeConfig$ISerializer$Type, arg1: $IIngredientManager$Type)

public "addIngredientToConfigBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type, arg2: $IIngredientHelper$Type<(V)>): void
public "isIngredientOnConfigBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IIngredientHelper$Type<(V)>): boolean
public "isIngredientOnConfigBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type, arg2: $IIngredientHelper$Type<(V)>): boolean
public "registerListener"(arg0: $EditModeConfig$IListener$Type): void
public "hideIngredientUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type): void
public "showIngredientUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type): void
public "getIngredientHiddenUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>): $Set<($IEditModeConfig$HideMode)>
public "isIngredientHiddenUsingConfigFile"<V>(arg0: $ITypedIngredient$Type<(V)>): boolean
public "removeIngredientFromConfigBlacklist"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IEditModeConfig$HideMode$Type, arg2: $IIngredientHelper$Type<(V)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditModeConfig$Type = ($EditModeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditModeConfig_ = $EditModeConfig$Type;
}}
declare module "packages/mezz/jei/core/util/$PathUtil" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $PathUtil {

constructor()

public static "writeUsingTempFile"(arg0: $Path$Type, arg1: $Iterable$Type<(any)>): void
public static "sanitizePathName"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathUtil$Type = ($PathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathUtil_ = $PathUtil$Type;
}}
declare module "packages/mezz/jei/library/gui/$BookmarkOverlayDummy" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $BookmarkOverlayDummy implements $IBookmarkOverlay {
static "INSTANCE": $IBookmarkOverlay


public "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
public "getItemStackUnderMouse"(): $ItemStack
get "ingredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
get "itemStackUnderMouse"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkOverlayDummy$Type = ($BookmarkOverlayDummy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkOverlayDummy_ = $BookmarkOverlayDummy$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientSorter" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$ModNameSortingConfig, $ModNameSortingConfig$Type} from "packages/mezz/jei/gui/config/$ModNameSortingConfig"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IngredientFilter, $IngredientFilter$Type} from "packages/mezz/jei/gui/ingredients/$IngredientFilter"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientSorter, $IIngredientSorter$Type} from "packages/mezz/jei/gui/ingredients/$IIngredientSorter"
import {$IngredientTypeSortingConfig, $IngredientTypeSortingConfig$Type} from "packages/mezz/jei/gui/config/$IngredientTypeSortingConfig"

export class $IngredientSorter implements $IIngredientSorter {

constructor(arg0: $IClientConfig$Type, arg1: $ModNameSortingConfig$Type, arg2: $IngredientTypeSortingConfig$Type)

public "getComparator"(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type): $Comparator<($IListElementInfo<(any)>)>
public "invalidateCache"(): void
public "doPreSort"(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientSorter$Type = ($IngredientSorter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientSorter_ = $IngredientSorter$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketRecipeTransfer" {
import {$TransferOperation, $TransferOperation$Type} from "packages/mezz/jei/common/transfer/$TransferOperation"
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export class $PacketRecipeTransfer extends $PacketJei {
readonly "transferOperations": $Collection<($TransferOperation)>
readonly "craftingSlots": $Collection<($Slot)>
readonly "inventorySlots": $Collection<($Slot)>

constructor(arg0: $Collection$Type<($TransferOperation$Type)>, arg1: $Collection$Type<($Slot$Type)>, arg2: $Collection$Type<($Slot$Type)>, arg3: boolean, arg4: boolean)

public static "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketRecipeTransfer$Type = ($PacketRecipeTransfer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketRecipeTransfer_ = $PacketRecipeTransfer$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/ingredients/fluid/$FluidStackListFactory" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IPlatformRegistry, $IPlatformRegistry$Type} from "packages/mezz/jei/common/platform/$IPlatformRegistry"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $FluidStackListFactory {


public static "create"<T>(arg0: $IPlatformRegistry$Type<($Fluid$Type)>, arg1: $IPlatformFluidHelper$Type<(T)>): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackListFactory$Type = ($FluidStackListFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackListFactory_ = $FluidStackListFactory$Type;
}}
declare module "packages/mezz/jei/gui/input/$MouseUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MouseUtil {

constructor()

public static "getY"(): double
public static "getX"(): double
get "y"(): double
get "x"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseUtil$Type = ($MouseUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseUtil_ = $MouseUtil$Type;
}}
declare module "packages/mezz/jei/common/network/$ServerPacketRouter" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IServerPacketHandler, $IServerPacketHandler$Type} from "packages/mezz/jei/common/network/packets/$IServerPacketHandler"
import {$PacketIdServer, $PacketIdServer$Type} from "packages/mezz/jei/common/network/$PacketIdServer"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"
import {$EnumMap, $EnumMap$Type} from "packages/java/util/$EnumMap"
import {$IConnectionToClient, $IConnectionToClient$Type} from "packages/mezz/jei/common/network/$IConnectionToClient"

export class $ServerPacketRouter {
readonly "handlers": $EnumMap<($PacketIdServer), ($IServerPacketHandler)>

constructor(arg0: $IConnectionToClient$Type, arg1: $IServerConfig$Type)

public "onPacket"(arg0: $FriendlyByteBuf$Type, arg1: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPacketRouter$Type = ($ServerPacketRouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPacketRouter_ = $ServerPacketRouter$Type;
}}
declare module "packages/mezz/jei/library/color/$MMCQ$VBox" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MMCQ$VBox {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[])

public "toString"(): string
public "clone"(): $MMCQ$VBox
public "count"(arg0: boolean): integer
public "contains"(arg0: (integer)[]): boolean
public "avg"(arg0: boolean): (integer)[]
public "volume"(arg0: boolean): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$VBox$Type = ($MMCQ$VBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ$VBox_ = $MMCQ$VBox$Type;
}}
declare module "packages/mezz/jei/common/util/$TickTimer" {
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"

export class $TickTimer implements $ITickTimer {

constructor(arg0: integer, arg1: integer, arg2: boolean)

public static "getValue"(arg0: long, arg1: long, arg2: integer, arg3: integer, arg4: boolean): integer
public "getValue"(): integer
public "getMaxValue"(): integer
get "value"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickTimer$Type = ($TickTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickTimer_ = $TickTimer$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/$RecipeBookGuiHandler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$RecipeUpdateListener, $RecipeUpdateListener$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeUpdateListener"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $RecipeBookGuiHandler<C extends $AbstractContainerMenu, T extends ($AbstractContainerScreen<(C)>) & ($RecipeUpdateListener)> implements $IGuiContainerHandler<(T)> {

constructor()

public "getGuiExtraAreas"(arg0: T): $List<($Rect2i)>
public "getGuiClickableAreas"(arg0: T, arg1: double, arg2: double): $Collection<($IGuiClickableArea)>
public "getClickableIngredientUnderMouse"(arg0: T, arg1: double, arg2: double): $Optional<($IClickableIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeBookGuiHandler$Type<C, T> = ($RecipeBookGuiHandler<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeBookGuiHandler_<C, T> = $RecipeBookGuiHandler$Type<(C), (T)>;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGuiProperties" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $IGuiProperties {

 "getScreenWidth"(): integer
 "getScreenHeight"(): integer
 "getScreenClass"(): $Class<(any)>
 "getGuiXSize"(): integer
 "getGuiYSize"(): integer
 "getGuiLeft"(): integer
 "getGuiTop"(): integer
}

export namespace $IGuiProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiProperties$Type = ($IGuiProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiProperties_ = $IGuiProperties$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$GuiHandlerRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IGlobalGuiHandler, $IGlobalGuiHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGlobalGuiHandler"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$IScreenHandler, $IScreenHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IScreenHandler"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"

export class $GuiHandlerRegistration implements $IGuiHandlerRegistration {

constructor(arg0: $IJeiHelpers$Type)

public "getJeiHelpers"(): $IJeiHelpers
public "addGenericGuiContainerHandler"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: $IGuiContainerHandler$Type<(any)>): void
public "addGuiScreenHandler"<T extends $Screen>(arg0: $Class$Type<(T)>, arg1: $IScreenHandler$Type<(T)>): void
public "addGlobalGuiHandler"(arg0: $IGlobalGuiHandler$Type): void
public "addGuiContainerHandler"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: $IGuiContainerHandler$Type<(T)>): void
public "createGuiScreenHelper"(arg0: $IIngredientManager$Type): $IScreenHelper
public "addGhostIngredientHandler"<T extends $Screen>(arg0: $Class$Type<(T)>, arg1: $IGhostIngredientHandler$Type<(T)>): void
public "addRecipeClickArea"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: integer, arg2: integer, arg3: integer, arg4: integer, ...arg5: ($RecipeType$Type<(any)>)[]): void
get "jeiHelpers"(): $IJeiHelpers
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiHandlerRegistration$Type = ($GuiHandlerRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiHandlerRegistration_ = $GuiHandlerRegistration$Type;
}}
declare module "packages/mezz/jei/library/render/$FluidTankRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$IPlatformFluidHelperInternal, $IPlatformFluidHelperInternal$Type} from "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $FluidTankRenderer<T> implements $IIngredientRenderer<(T)> {

constructor(arg0: $IPlatformFluidHelperInternal$Type<(T)>)
constructor(arg0: $IPlatformFluidHelperInternal$Type<(T)>, arg1: long, arg2: boolean, arg3: integer, arg4: integer)

public "render"(arg0: $GuiGraphics$Type, arg1: T): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getTooltip"(arg0: T, arg1: $TooltipFlag$Type): $List<($Component)>
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: T): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidTankRenderer$Type<T> = ($FluidTankRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidTankRenderer_<T> = $FluidTankRenderer$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/search/$ElementPrefixParser" {
import {$PrefixInfo, $PrefixInfo$Type} from "packages/mezz/jei/core/search/$PrefixInfo"
import {$ElementPrefixParser$TokenInfo, $ElementPrefixParser$TokenInfo$Type} from "packages/mezz/jei/gui/search/$ElementPrefixParser$TokenInfo"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"

export class $ElementPrefixParser {
static readonly "NO_PREFIX": $PrefixInfo<($IListElementInfo<(any)>)>

constructor(arg0: $IIngredientManager$Type, arg1: $IIngredientFilterConfig$Type, arg2: $IColorHelper$Type)

public "parseToken"(arg0: string): $Optional<($ElementPrefixParser$TokenInfo)>
public "allPrefixInfos"(): $Collection<($PrefixInfo<($IListElementInfo<(any)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementPrefixParser$Type = ($ElementPrefixParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementPrefixParser_ = $ElementPrefixParser$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$NullDragHandler" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"

export class $NullDragHandler implements $IDragHandler {
static readonly "INSTANCE": $NullDragHandler


public "handleDragStart"(arg0: $Screen$Type, arg1: $UserInput$Type): $Optional<($IDragHandler)>
public "handleDragComplete"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "handleDragCanceled"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullDragHandler$Type = ($NullDragHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullDragHandler_ = $NullDragHandler$Type;
}}
declare module "packages/mezz/jei/api/$JeiPlugin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $JeiPlugin extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $JeiPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiPlugin$Type = ($JeiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiPlugin_ = $JeiPlugin$Type;
}}
declare module "packages/mezz/jei/common/config/$IngredientSortStage" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"

export class $IngredientSortStage extends $Enum<($IngredientSortStage)> {
static readonly "MOD_NAME": $IngredientSortStage
static readonly "INGREDIENT_TYPE": $IngredientSortStage
static readonly "ALPHABETICAL": $IngredientSortStage
static readonly "CREATIVE_MENU": $IngredientSortStage
static readonly "TAG": $IngredientSortStage
static readonly "ARMOR": $IngredientSortStage
static readonly "MAX_DURABILITY": $IngredientSortStage
static readonly "defaultStages": $List<($IngredientSortStage)>


public static "values"(): ($IngredientSortStage)[]
public static "valueOf"(arg0: string): $IngredientSortStage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientSortStage$Type = (("max_durability") | ("alphabetical") | ("armor") | ("creative_menu") | ("ingredient_type") | ("tag") | ("mod_name")) | ($IngredientSortStage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientSortStage_ = $IngredientSortStage$Type;
}}
declare module "packages/mezz/jei/forge/platform/$InputHelper" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$IPlatformInputHelper, $IPlatformInputHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformInputHelper"
import {$IJeiKeyMappingCategoryBuilder, $IJeiKeyMappingCategoryBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingCategoryBuilder"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $InputHelper implements $IPlatformInputHelper {

constructor()

public "createKeyMappingCategoryBuilder"(arg0: string): $IJeiKeyMappingCategoryBuilder
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Key$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputHelper$Type = ($InputHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputHelper_ = $InputHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/fuel/$FuelingRecipe" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IJeiFuelingRecipe, $IJeiFuelingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiFuelingRecipe"

export class $FuelingRecipe implements $IJeiFuelingRecipe {

constructor(arg0: $Collection$Type<($ItemStack$Type)>, arg1: integer)

public "getBurnTime"(): integer
public "getInputs"(): $List<($ItemStack)>
get "burnTime"(): integer
get "inputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelingRecipe$Type = ($FuelingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelingRecipe_ = $FuelingRecipe$Type;
}}
declare module "packages/mezz/jei/common/network/$PacketIdClient" {
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PacketIdClient extends $Enum<($PacketIdClient)> implements $IPacketId {
static readonly "CHEAT_PERMISSION": $PacketIdClient
static readonly "VALUES": ($PacketIdClient)[]


public static "values"(): ($PacketIdClient)[]
public static "valueOf"(arg0: string): $PacketIdClient
public "ordinal"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketIdClient$Type = (("cheat_permission")) | ($PacketIdClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketIdClient_ = $PacketIdClient$Type;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$IntegerSerializer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $IntegerSerializer implements $IJeiConfigValueSerializer<(integer)> {

constructor(arg0: integer, arg1: integer)

public "isValid"(arg0: integer): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<(integer)>)>
public "serialize"(arg0: integer): string
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<(integer)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerSerializer$Type = ($IntegerSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerSerializer_ = $IntegerSerializer$Type;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigValue" {
import {$IJeiConfigValue, $IJeiConfigValue$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValue"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigSchema, $IConfigSchema$Type} from "packages/mezz/jei/common/config/file/$IConfigSchema"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $ConfigValue<T> implements $IJeiConfigValue<(T)> {

constructor(arg0: string, arg1: T, arg2: $IJeiConfigValueSerializer$Type<(T)>, arg3: string)

public "getName"(): string
public "getValue"(): T
public "set"(arg0: T): boolean
public "getDefaultValue"(): T
public "getDescription"(): string
public "setSchema"(arg0: $IConfigSchema$Type): void
public "getSerializer"(): $IJeiConfigValueSerializer<(T)>
public "setFromSerializedValue"(arg0: string): $List<(string)>
get "name"(): string
get "value"(): T
get "defaultValue"(): T
get "description"(): string
set "schema"(value: $IConfigSchema$Type)
get "serializer"(): $IJeiConfigValueSerializer<(T)>
set "fromSerializedValue"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigValue$Type<T> = ($ConfigValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigValue_<T> = $ConfigValue$Type<(T)>;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientInfo" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"

export class $IngredientInfo<T> {

constructor(arg0: $IIngredientType$Type<(T)>, arg1: $Collection$Type<(T)>, arg2: $IIngredientHelper$Type<(T)>, arg3: $IIngredientRenderer$Type<(T)>)

public "removeIngredients"(arg0: $Collection$Type<(T)>): void
public "getAllIngredients"(): $Collection<(T)>
public "getIngredientType"(): $IIngredientType<(T)>
public "addIngredients"(arg0: $Collection$Type<(T)>): void
public "getIngredientHelper"(): $IIngredientHelper<(T)>
public "getIngredientByUid"(arg0: string): $Optional<(T)>
public "getIngredientRenderer"(): $IIngredientRenderer<(T)>
get "allIngredients"(): $Collection<(T)>
get "ingredientType"(): $IIngredientType<(T)>
get "ingredientHelper"(): $IIngredientHelper<(T)>
get "ingredientRenderer"(): $IIngredientRenderer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientInfo$Type<T> = ($IngredientInfo<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientInfo_<T> = $IngredientInfo$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/$JeiClientConfigs" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$FileWatcher, $FileWatcher$Type} from "packages/mezz/jei/common/config/file/$FileWatcher"
import {$ConfigManager, $ConfigManager$Type} from "packages/mezz/jei/common/config/$ConfigManager"
import {$IJeiClientConfigs, $IJeiClientConfigs$Type} from "packages/mezz/jei/common/config/$IJeiClientConfigs"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"

export class $JeiClientConfigs implements $IJeiClientConfigs {

constructor(arg0: $Path$Type)

public "register"(arg0: $FileWatcher$Type, arg1: $ConfigManager$Type): void
public "getIngredientListConfig"(): $IIngredientGridConfig
public "getBookmarkListConfig"(): $IIngredientGridConfig
public "getIngredientFilterConfig"(): $IIngredientFilterConfig
public "getClientConfig"(): $IClientConfig
get "ingredientListConfig"(): $IIngredientGridConfig
get "bookmarkListConfig"(): $IIngredientGridConfig
get "ingredientFilterConfig"(): $IIngredientFilterConfig
get "clientConfig"(): $IClientConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiClientConfigs$Type = ($JeiClientConfigs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiClientConfigs_ = $JeiClientConfigs$Type;
}}
declare module "packages/mezz/jei/common/$Constants" {
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Constants {
static readonly "TEXTURE_GUI_PATH": string
static readonly "TEXTURE_GUI_VANILLA": string
static readonly "RECIPE_GUI_VANILLA": $ResourceLocation
static readonly "UNIVERSAL_RECIPE_TRANSFER_TYPE": $RecipeType<(any)>
static readonly "LOCATION_JEI_GUI_TEXTURE_ATLAS": $ResourceLocation
static readonly "NETWORK_CHANNEL_ID": $ResourceLocation
static readonly "HIDDEN_INGREDIENT_TAG": $ResourceLocation


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$IJeiKeyMappingBuilder" {
import {$JeiKeyConflictContext, $JeiKeyConflictContext$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyConflictContext"
import {$JeiKeyModifier, $JeiKeyModifier$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyModifier"
import {$IJeiKeyMappingInternal, $IJeiKeyMappingInternal$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingInternal"

export interface $IJeiKeyMappingBuilder {

 "setContext"(arg0: $JeiKeyConflictContext$Type): $IJeiKeyMappingBuilder
 "buildKeyboardKey"(arg0: integer): $IJeiKeyMappingInternal
 "setModifier"(arg0: $JeiKeyModifier$Type): $IJeiKeyMappingBuilder
 "buildMouseRight"(): $IJeiKeyMappingInternal
 "buildMouseMiddle"(): $IJeiKeyMappingInternal
 "buildMouseLeft"(): $IJeiKeyMappingInternal
 "buildUnbound"(): $IJeiKeyMappingInternal
}

export namespace $IJeiKeyMappingBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiKeyMappingBuilder$Type = ($IJeiKeyMappingBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiKeyMappingBuilder_ = $IJeiKeyMappingBuilder$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientFilterApi" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$IFilterTextSource, $IFilterTextSource$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IngredientFilter, $IngredientFilter$Type} from "packages/mezz/jei/gui/ingredients/$IngredientFilter"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $IngredientFilterApi implements $IIngredientFilter {

constructor(arg0: $IngredientFilter$Type, arg1: $IFilterTextSource$Type)

public "getFilterText"(): string
public "setFilterText"(arg0: string): void
public "getFilteredIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
public "getFilteredItemStacks"(): $List<($ItemStack)>
get "filterText"(): string
set "filterText"(value: string)
get "filteredItemStacks"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientFilterApi$Type = ($IngredientFilterApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientFilterApi_ = $IngredientFilterApi$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IScreenHelper" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IScreenHelper {

 "getGuiClickableArea"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: double, arg2: double): $Stream<($IGuiClickableArea)>
 "getGuiExclusionAreas"(arg0: $Screen$Type): $Stream<($Rect2i)>
 "getGhostIngredientHandler"<T extends $Screen>(arg0: T): $Optional<($IGhostIngredientHandler<(T)>)>
 "getClickableIngredientUnderMouse"(arg0: $Screen$Type, arg1: double, arg2: double): $Stream<($IClickableIngredient<(any)>)>
 "getGuiProperties"<T extends $Screen>(arg0: T): $Optional<($IGuiProperties)>
}

export namespace $IScreenHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IScreenHelper$Type = ($IScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IScreenHelper_ = $IScreenHelper$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$TextFieldInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiTextFieldFilter, $GuiTextFieldFilter$Type} from "packages/mezz/jei/gui/input/$GuiTextFieldFilter"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $TextFieldInputHandler implements $IUserInputHandler {

constructor(arg0: $GuiTextFieldFilter$Type)

public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextFieldInputHandler$Type = ($TextFieldInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextFieldInputHandler_ = $TextFieldInputHandler$Type;
}}
declare module "packages/mezz/jei/common/config/$IClientConfig" {
import {$IngredientSortStage, $IngredientSortStage$Type} from "packages/mezz/jei/common/config/$IngredientSortStage"
import {$GiveMode, $GiveMode$Type} from "packages/mezz/jei/common/config/$GiveMode"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $IClientConfig {

 "isLowMemorySlowSearchEnabled"(): boolean
 "getGiveMode"(): $GiveMode
 "isAddingBookmarksToFrontEnabled"(): boolean
 "isCheatToHotbarUsingHotkeysEnabled"(): boolean
 "isCatchRenderErrorsEnabled"(): boolean
 "isLookupBlockTagsEnabled"(): boolean
 "isLookupFluidContentsEnabled"(): boolean
 "getIngredientSorterStages"(): $List<($IngredientSortStage)>
 "getMaxRecipeGuiHeight"(): integer
 "isCenterSearchBarEnabled"(): boolean
}

export namespace $IClientConfig {
const minRecipeGuiHeight: integer
const defaultRecipeGuiHeight: integer
const defaultCenterSearchBar: boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientConfig$Type = ($IClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientConfig_ = $IClientConfig$Type;
}}
declare module "packages/mezz/jei/library/load/$PluginCaller" {
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PluginCaller {

constructor()

public static "callOnPlugins"(arg0: string, arg1: $List$Type<($IModPlugin$Type)>, arg2: $Consumer$Type<($IModPlugin$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginCaller$Type = ($PluginCaller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginCaller_ = $PluginCaller$Type;
}}
declare module "packages/mezz/jei/forge/platform/$ModHelper" {
import {$IPlatformModHelper, $IPlatformModHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformModHelper"

export class $ModHelper implements $IPlatformModHelper {

constructor()

public "getModNameForModId"(arg0: string): string
public "isInDev"(): boolean
get "inDev"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModHelper$Type = ($ModHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModHelper_ = $ModHelper$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IIngredientSupplier" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export interface $IIngredientSupplier {

 "getIngredientStream"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $RecipeIngredientRole$Type): $Stream<(T)>
 "getIngredientTypes"(arg0: $RecipeIngredientRole$Type): $Stream<(any)>
}

export namespace $IIngredientSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientSupplier$Type = ($IIngredientSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientSupplier_ = $IIngredientSupplier$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientGrid" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IngredientGrid$SlotInfo, $IngredientGrid$SlotInfo$Type} from "packages/mezz/jei/gui/overlay/$IngredientGrid$SlotInfo"
import {$IIngredientGrid, $IIngredientGrid$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGrid"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$ImmutableSize2i, $ImmutableSize2i$Type} from "packages/mezz/jei/common/util/$ImmutableSize2i"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $IngredientGrid implements $IRecipeFocusSource, $IIngredientGrid {
static readonly "INGREDIENT_WIDTH": integer
static readonly "INGREDIENT_HEIGHT": integer

constructor(arg0: $IIngredientManager$Type, arg1: $IIngredientGridConfig$Type, arg2: $IEditModeConfig$Type, arg3: $IIngredientFilterConfig$Type, arg4: $IClientConfig$Type, arg5: $IClientToggleState$Type, arg6: $IModIdHelper$Type, arg7: $IConnectionToServer$Type, arg8: $IInternalKeyMappings$Type, arg9: $IColorHelper$Type, arg10: $CheatUtil$Type)

public "size"(): integer
public "set"(arg0: integer, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>): void
public "draw"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public static "calculateSize"(arg0: $IIngredientGridConfig$Type, arg1: $ImmutableRect2i$Type): $ImmutableSize2i
public static "calculateBlockedSlotPercentage"(arg0: $IIngredientGridConfig$Type, arg1: $ImmutableRect2i$Type, arg2: $Set$Type<($ImmutableRect2i$Type)>): $IngredientGrid$SlotInfo
public "getArea"(): $ImmutableRect2i
public "getVisibleIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "getInputHandler"(): $IUserInputHandler
public static "drawHighlight"(arg0: $GuiGraphics$Type, arg1: $ImmutableRect2i$Type): void
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "updateBounds"(arg0: $ImmutableRect2i$Type, arg1: $Set$Type<($ImmutableRect2i$Type)>): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public static "calculateBounds"(arg0: $IIngredientGridConfig$Type, arg1: $ImmutableRect2i$Type): $ImmutableRect2i
public "drawTooltips"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "hasRoom"(): boolean
get "area"(): $ImmutableRect2i
get "inputHandler"(): $IUserInputHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientGrid$Type = ($IngredientGrid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientGrid_ = $IngredientGrid$Type;
}}
declare module "packages/mezz/jei/library/gui/ingredients/$CycleTimer" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $CycleTimer {

constructor(arg0: integer)

public "getCycledItem"(arg0: $List$Type<($Optional$Type<($ITypedIngredient$Type<(any)>)>)>): $Optional<($ITypedIngredient<(any)>)>
public "onDraw"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CycleTimer$Type = ($CycleTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CycleTimer_ = $CycleTimer$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IRecipesGui" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"

export interface $IRecipesGui {

 "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
 "show"<V>(arg0: $IFocus$Type<(V)>): void
 "show"(arg0: $List$Type<($IFocus$Type<(any)>)>): void
 "showTypes"(arg0: $List$Type<($RecipeType$Type<(any)>)>): void
}

export namespace $IRecipesGui {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipesGui$Type = ($IRecipesGui);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipesGui_ = $IRecipesGui$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$IClientPacketHandler" {
import {$ClientPacketData, $ClientPacketData$Type} from "packages/mezz/jei/common/network/$ClientPacketData"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"

export interface $IClientPacketHandler {

 "readPacketData"(arg0: $ClientPacketData$Type): $CompletableFuture<(void)>

(arg0: $ClientPacketData$Type): $CompletableFuture<(void)>
}

export namespace $IClientPacketHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientPacketHandler$Type = ($IClientPacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientPacketHandler_ = $IClientPacketHandler$Type;
}}
declare module "packages/mezz/jei/common/network/$IConnectionToClient" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $IConnectionToClient {

 "sendPacketToClient"(arg0: $PacketJei$Type, arg1: $ServerPlayer$Type): void

(arg0: $PacketJei$Type, arg1: $ServerPlayer$Type): void
}

export namespace $IConnectionToClient {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConnectionToClient$Type = ($IConnectionToClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConnectionToClient_ = $IConnectionToClient$Type;
}}
declare module "packages/mezz/jei/api/helpers/$IColorHelper" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $IColorHelper {

 "getClosestColorName"(arg0: integer): string
 "getColors"(arg0: $ItemStack$Type, arg1: integer): $List<(integer)>
 "getColors"(arg0: $TextureAtlasSprite$Type, arg1: integer, arg2: integer): $List<(integer)>
}

export namespace $IColorHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IColorHelper$Type = ($IColorHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IColorHelper_ = $IColorHelper$Type;
}}
declare module "packages/mezz/jei/common/util/$VerticalAlignment" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $VerticalAlignment extends $Enum<($VerticalAlignment)> {
static readonly "TOP": $VerticalAlignment
static readonly "CENTER": $VerticalAlignment
static readonly "BOTTOM": $VerticalAlignment


public static "values"(): ($VerticalAlignment)[]
public static "valueOf"(arg0: string): $VerticalAlignment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VerticalAlignment$Type = (("top") | ("bottom") | ("center")) | ($VerticalAlignment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VerticalAlignment_ = $VerticalAlignment$Type;
}}
declare module "packages/mezz/jei/library/startup/$JeiStarter" {
import {$StartData, $StartData$Type} from "packages/mezz/jei/library/startup/$StartData"

export class $JeiStarter {

constructor(arg0: $StartData$Type)

public "start"(): void
public "stop"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiStarter$Type = ($JeiStarter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiStarter_ = $JeiStarter$Type;
}}
declare module "packages/mezz/jei/api/registration/$IAdvancedRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IJeiFeatures, $IJeiFeatures$Type} from "packages/mezz/jei/api/runtime/$IJeiFeatures"
import {$IRecipeManagerPlugin, $IRecipeManagerPlugin$Type} from "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin"

export interface $IAdvancedRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "getJeiFeatures"(): $IJeiFeatures
 "addRecipeCategoryDecorator"<T>(arg0: $RecipeType$Type<(T)>, arg1: $IRecipeCategoryDecorator$Type<(T)>): void
 "addRecipeManagerPlugin"(arg0: $IRecipeManagerPlugin$Type): void
}

export namespace $IAdvancedRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAdvancedRegistration$Type = ($IAdvancedRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAdvancedRegistration_ = $IAdvancedRegistration$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$UserInputRouter" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"

export class $UserInputRouter {

constructor(...arg0: ($IUserInputHandler$Type)[])

public "handleGuiChange"(): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserInputRouter$Type = ($UserInputRouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserInputRouter_ = $UserInputRouter$Type;
}}
declare module "packages/mezz/jei/library/runtime/$JeiRuntime" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IJeiKeyMappings, $IJeiKeyMappings$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMappings"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $JeiRuntime implements $IJeiRuntime {

constructor(arg0: $IRecipeManager$Type, arg1: $IIngredientManager$Type, arg2: $IIngredientVisibility$Type, arg3: $IJeiKeyMappings$Type, arg4: $IJeiHelpers$Type, arg5: $IScreenHelper$Type, arg6: $IRecipeTransferManager$Type, arg7: $IEditModeConfig$Type, arg8: $IIngredientListOverlay$Type, arg9: $IBookmarkOverlay$Type, arg10: $IRecipesGui$Type, arg11: $IIngredientFilter$Type, arg12: $IJeiConfigManager$Type)

public "getRecipesGui"(): $IRecipesGui
public "getJeiHelpers"(): $IJeiHelpers
public "getScreenHelper"(): $IScreenHelper
public "getBookmarkOverlay"(): $IBookmarkOverlay
public "getIngredientVisibility"(): $IIngredientVisibility
public "getRecipeTransferManager"(): $IRecipeTransferManager
public "getIngredientFilter"(): $IIngredientFilter
public "getIngredientManager"(): $IIngredientManager
public "getEditModeConfig"(): $IEditModeConfig
public "getConfigManager"(): $IJeiConfigManager
public "getRecipeManager"(): $IRecipeManager
public "getIngredientListOverlay"(): $IIngredientListOverlay
public "getKeyMappings"(): $IJeiKeyMappings
get "recipesGui"(): $IRecipesGui
get "jeiHelpers"(): $IJeiHelpers
get "screenHelper"(): $IScreenHelper
get "bookmarkOverlay"(): $IBookmarkOverlay
get "ingredientVisibility"(): $IIngredientVisibility
get "recipeTransferManager"(): $IRecipeTransferManager
get "ingredientFilter"(): $IIngredientFilter
get "ingredientManager"(): $IIngredientManager
get "editModeConfig"(): $IEditModeConfig
get "configManager"(): $IJeiConfigManager
get "recipeManager"(): $IRecipeManager
get "ingredientListOverlay"(): $IIngredientListOverlay
get "keyMappings"(): $IJeiKeyMappings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiRuntime$Type = ($JeiRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiRuntime_ = $JeiRuntime$Type;
}}
declare module "packages/mezz/jei/core/search/$PrefixInfo$IModeGetter" {
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"

export interface $PrefixInfo$IModeGetter {

 "getMode"(): $SearchMode

(): $SearchMode
}

export namespace $PrefixInfo$IModeGetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixInfo$IModeGetter$Type = ($PrefixInfo$IModeGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixInfo$IModeGetter_ = $PrefixInfo$IModeGetter$Type;
}}
declare module "packages/mezz/jei/library/config/serializers/$ChatFormattingSerializer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiConfigListValueSerializer, $IJeiConfigListValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigListValueSerializer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $ChatFormattingSerializer implements $IJeiConfigListValueSerializer<($ChatFormatting)> {
static readonly "INSTANCE": $ChatFormattingSerializer


public "isValid"(arg0: $List$Type<($ChatFormatting$Type)>): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<($List<($ChatFormatting)>)>)>
public "serialize"(arg0: $List$Type<($ChatFormatting$Type)>): string
public "getListValueSerializer"(): $IJeiConfigValueSerializer<($ChatFormatting)>
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<($List<($ChatFormatting)>)>)>
get "listValueSerializer"(): $IJeiConfigValueSerializer<($ChatFormatting)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatFormattingSerializer$Type = ($ChatFormattingSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatFormattingSerializer_ = $ChatFormattingSerializer$Type;
}}
declare module "packages/mezz/jei/library/color/$ColorGetter" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ColorGetter {

constructor()

public "getColors"(arg0: $ItemStack$Type, arg1: integer): $List<(integer)>
public "getColors"(arg0: $TextureAtlasSprite$Type, arg1: integer, arg2: integer): $List<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorGetter$Type = ($ColorGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorGetter_ = $ColorGetter$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformRegistry" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IPlatformRegistry<T> {

 "getValue"(arg0: integer): $Optional<(T)>
 "getValue"(arg0: $ResourceLocation$Type): $Optional<(T)>
 "contains"(arg0: T): boolean
 "getId"(arg0: T): integer
 "getRegistryName"(arg0: T): $Optional<($ResourceLocation)>
 "getValues"(): $Stream<(T)>
}

export namespace $IPlatformRegistry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformRegistry$Type<T> = ($IPlatformRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformRegistry_<T> = $IPlatformRegistry$Type<(T)>;
}}
declare module "packages/mezz/jei/core/util/function/$CachedSupplierTransformer" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CachedSupplierTransformer<T, R> implements $Supplier<(R)> {

constructor(arg0: $Supplier$Type<(T)>, arg1: $Function$Type<(T), (R)>)

public "get"(): R
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedSupplierTransformer$Type<T, R> = ($CachedSupplierTransformer<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedSupplierTransformer_<T, R> = $CachedSupplierTransformer$Type<(T), (R)>;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientListSlot" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$ElementRenderer, $ElementRenderer$Type} from "packages/mezz/jei/gui/overlay/$ElementRenderer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $IngredientListSlot {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer)

public "clear"(): void
public "getTypedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
public "getArea"(): $ImmutableRect2i
public "isBlocked"(): boolean
public "isMouseOver"(arg0: double, arg1: double): boolean
public "setBlocked"(arg0: boolean): void
public "getIngredientRenderer"(): $Optional<($ElementRenderer<(any)>)>
public "setIngredientRenderer"(arg0: $ElementRenderer$Type<(any)>): void
get "typedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
get "area"(): $ImmutableRect2i
get "blocked"(): boolean
set "blocked"(value: boolean)
get "ingredientRenderer"(): $Optional<($ElementRenderer<(any)>)>
set "ingredientRenderer"(value: $ElementRenderer$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientListSlot$Type = ($IngredientListSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientListSlot_ = $IngredientListSlot$Type;
}}
declare module "packages/mezz/jei/api/registration/$IRecipeTransferRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IRecipeTransferHandlerHelper, $IRecipeTransferHandlerHelper$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"

export interface $IRecipeTransferRegistration {

 "getTransferHelper"(): $IRecipeTransferHandlerHelper
 "getJeiHelpers"(): $IJeiHelpers
 "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>): void
 "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferHandler$Type<(C), (R)>, arg1: $RecipeType$Type<(R)>): void
 "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $Class$Type<(any)>, arg1: $MenuType$Type<(C)>, arg2: $RecipeType$Type<(R)>, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
 "addUniversalRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferHandler$Type<(C), (R)>): void
}

export namespace $IRecipeTransferRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferRegistration$Type = ($IRecipeTransferRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferRegistration_ = $IRecipeTransferRegistration$Type;
}}
declare module "packages/mezz/jei/gui/search/$ElementSearchLowMem" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ElementPrefixParser$TokenInfo, $ElementPrefixParser$TokenInfo$Type} from "packages/mezz/jei/gui/search/$ElementPrefixParser$TokenInfo"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$IElementSearch, $IElementSearch$Type} from "packages/mezz/jei/gui/search/$IElementSearch"

export class $ElementSearchLowMem implements $IElementSearch {

constructor()

public "add"(arg0: $IListElementInfo$Type<(any)>): void
public "getSearchResults"(arg0: $ElementPrefixParser$TokenInfo$Type): $Set<($IListElementInfo<(any)>)>
public "logStatistics"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementSearchLowMem$Type = ($ElementSearchLowMem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementSearchLowMem_ = $ElementSearchLowMem$Type;
}}
declare module "packages/mezz/jei/library/transfer/$RecipeTransferErrorMissingSlots" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$RecipeTransferErrorTooltip, $RecipeTransferErrorTooltip$Type} from "packages/mezz/jei/library/transfer/$RecipeTransferErrorTooltip"

export class $RecipeTransferErrorMissingSlots extends $RecipeTransferErrorTooltip {

constructor(arg0: $Component$Type, arg1: $Collection$Type<($IRecipeSlotView$Type)>)

public "showError"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $IRecipeSlotsView$Type, arg4: integer, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferErrorMissingSlots$Type = ($RecipeTransferErrorMissingSlots);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferErrorMissingSlots_ = $RecipeTransferErrorMissingSlots$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IFocusGroup" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IFocusGroup {

 "getItemStackFocuses"(): $Stream<($IFocus<($ItemStack)>)>
 "getItemStackFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<($ItemStack)>)>
 "getAllFocuses"(): $List<($IFocus<(any)>)>
 "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $RecipeIngredientRole$Type): $Stream<($IFocus<(T)>)>
 "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<($IFocus<(T)>)>
 "getFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<(any)>)>
 "isEmpty"(): boolean
}

export namespace $IFocusGroup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFocusGroup$Type = ($IFocusGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFocusGroup_ = $IFocusGroup$Type;
}}
declare module "packages/mezz/jei/common/config/file/$FileWatcherThread" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $FileWatcherThread extends $Thread {
static readonly "MIN_PRIORITY": integer
static readonly "NORM_PRIORITY": integer
static readonly "MAX_PRIORITY": integer

constructor(arg0: string)

public "run"(): void
public "addCallback"(arg0: $Path$Type, arg1: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileWatcherThread$Type = ($FileWatcherThread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileWatcherThread_ = $FileWatcherThread$Type;
}}
declare module "packages/mezz/jei/gui/input/$ClientInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$ICharTypedHandler, $ICharTypedHandler$Type} from "packages/mezz/jei/gui/input/$ICharTypedHandler"
import {$UserInputRouter, $UserInputRouter$Type} from "packages/mezz/jei/gui/input/handlers/$UserInputRouter"
import {$List, $List$Type} from "packages/java/util/$List"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$DragRouter, $DragRouter$Type} from "packages/mezz/jei/gui/input/handlers/$DragRouter"

export class $ClientInputHandler {

constructor(arg0: $List$Type<($ICharTypedHandler$Type)>, arg1: $UserInputRouter$Type, arg2: $DragRouter$Type, arg3: $IInternalKeyMappings$Type)

public "onKeyboardCharTypedPost"(arg0: $Screen$Type, arg1: character, arg2: integer): void
public "onKeyboardKeyPressedPre"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "onKeyboardKeyPressedPost"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "onKeyboardCharTypedPre"(arg0: $Screen$Type, arg1: character, arg2: integer): boolean
public "onInitGui"(): void
public "onGuiMouseClicked"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
public "onGuiMouseScroll"(arg0: double, arg1: double, arg2: double): boolean
public "onGuiMouseReleased"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientInputHandler$Type = ($ClientInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientInputHandler_ = $ClientInputHandler$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientManager" {
import {$IIngredientManager$IIngredientListener, $IIngredientManager$IIngredientListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager$IIngredientListener"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$RegisteredIngredients, $RegisteredIngredients$Type} from "packages/mezz/jei/library/ingredients/$RegisteredIngredients"

export class $IngredientManager implements $IIngredientManager {

constructor(arg0: $RegisteredIngredients$Type)

public "getAllIngredients"<V>(arg0: $IIngredientType$Type<(V)>): $Collection<(V)>
public "getIngredientHelper"<V>(arg0: V): $IIngredientHelper<(V)>
public "getIngredientHelper"<V>(arg0: $IIngredientType$Type<(V)>): $IIngredientHelper<(V)>
public "getIngredientByUid"<V>(arg0: $IIngredientType$Type<(V)>, arg1: string): $Optional<(V)>
public "getIngredientRenderer"<V>(arg0: $IIngredientType$Type<(V)>): $IIngredientRenderer<(V)>
public "getIngredientRenderer"<V>(arg0: V): $IIngredientRenderer<(V)>
public "removeIngredientsAtRuntime"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>): void
public "getRegisteredIngredientTypes"(): $Collection<($IIngredientType<(any)>)>
public "addIngredientsAtRuntime"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>): void
public "getIngredientTypeChecked"<V>(arg0: V): $Optional<($IIngredientType<(V)>)>
public "getIngredientTypeChecked"<V>(arg0: $Class$Type<(any)>): $Optional<($IIngredientType<(V)>)>
public "createTypedIngredient"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): $Optional<($ITypedIngredient<(V)>)>
public "registerIngredientListener"(arg0: $IIngredientManager$IIngredientListener$Type): void
public "getAllItemStacks"(): $Collection<($ItemStack)>
public "createTypedIngredient"<V>(arg0: V): $Optional<($ITypedIngredient<(V)>)>
get "registeredIngredientTypes"(): $Collection<($IIngredientType<(any)>)>
get "allItemStacks"(): $Collection<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientManager$Type = ($IngredientManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientManager_ = $IngredientManager$Type;
}}
declare module "packages/mezz/jei/library/gui/$GuiContainerHandlers" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $GuiContainerHandlers {

constructor()

public "add"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: $IGuiContainerHandler$Type<(any)>): void
public "getGuiExtraAreas"<C extends $AbstractContainerMenu, T extends $AbstractContainerScreen<(C)>>(arg0: T): $Stream<($Rect2i)>
public "getGuiClickableArea"<T extends $AbstractContainerScreen<(any)>>(arg0: T, arg1: double, arg2: double): $Stream<($IGuiClickableArea)>
public "getActiveGuiHandlerStream"<T extends $AbstractContainerScreen<(any)>>(arg0: T): $Stream<($IGuiContainerHandler<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiContainerHandlers$Type = ($GuiContainerHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiContainerHandlers_ = $GuiContainerHandlers$Type;
}}
declare module "packages/mezz/jei/common/util/$Translator" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $Translator {


public static "translateToLocalFormatted"(arg0: string, ...arg1: (any)[]): string
public static "toLowercaseWithLocale"(arg0: string): string
public static "translateToLocal"(arg0: string): string
public static "setLocaleSupplier"(arg0: $Supplier$Type<($Locale$Type)>): void
set "localeSupplier"(value: $Supplier$Type<($Locale$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Translator$Type = ($Translator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Translator_ = $Translator$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientVisibility" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientVisibility$IListener, $IIngredientVisibility$IListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility$IListener"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IIngredientVisibility {

 "registerListener"(arg0: $IIngredientVisibility$IListener$Type): void
 "isIngredientVisible"<V>(arg0: $ITypedIngredient$Type<(V)>): boolean
 "isIngredientVisible"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): boolean
}

export namespace $IIngredientVisibility {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientVisibility$Type = ($IIngredientVisibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientVisibility_ = $IIngredientVisibility$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientGridWithNavigation" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IIngredientGridSource, $IIngredientGridSource$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IngredientGrid, $IngredientGrid$Type} from "packages/mezz/jei/gui/overlay/$IngredientGrid"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$DrawableNineSliceTexture, $DrawableNineSliceTexture$Type} from "packages/mezz/jei/common/gui/elements/$DrawableNineSliceTexture"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $IngredientGridWithNavigation implements $IRecipeFocusSource {

constructor(arg0: $IIngredientGridSource$Type, arg1: $IngredientGrid$Type, arg2: $IClientToggleState$Type, arg3: $IClientConfig$Type, arg4: $IConnectionToServer$Type, arg5: $IIngredientGridConfig$Type, arg6: $DrawableNineSliceTexture$Type, arg7: $DrawableNineSliceTexture$Type, arg8: $Textures$Type, arg9: $CheatUtil$Type, arg10: $IScreenHelper$Type, arg11: $IIngredientManager$Type)

public "isEmpty"(): boolean
public "close"(): void
public "draw"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
public "getVisibleIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "updateBounds"(arg0: $ImmutableRect2i$Type, arg1: $Set$Type<($ImmutableRect2i$Type)>): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "drawTooltips"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "getBackgroundArea"(): $ImmutableRect2i
public "drawOnForeground"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "hasRoom"(): boolean
public "updateLayout"(arg0: boolean): void
public "createDragHandler"(): $IDragHandler
public "createInputHandler"(): $IUserInputHandler
get "empty"(): boolean
get "backgroundArea"(): $ImmutableRect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientGridWithNavigation$Type = ($IngredientGridWithNavigation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientGridWithNavigation_ = $IngredientGridWithNavigation$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientInfoRecipe" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiIngredientInfoRecipe, $IJeiIngredientInfoRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiIngredientInfoRecipe"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $IngredientInfoRecipe implements $IJeiIngredientInfoRecipe {
static readonly "recipeWidth": integer
static readonly "recipeHeight": integer
static readonly "lineSpacing": integer


public static "create"<T>(arg0: $IIngredientManager$Type, arg1: $List$Type<(T)>, arg2: $IIngredientType$Type<(T)>, ...arg3: ($Component$Type)[]): $List<($IJeiIngredientInfoRecipe)>
public "getDescription"(): $List<($FormattedText)>
public "getIngredients"(): $List<($ITypedIngredient<(any)>)>
get "description"(): $List<($FormattedText)>
get "ingredients"(): $List<($ITypedIngredient<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientInfoRecipe$Type = ($IngredientInfoRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientInfoRecipe_ = $IngredientInfoRecipe$Type;
}}
declare module "packages/mezz/jei/core/util/$Pair" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Pair<A, B> extends $Record {

constructor(first: A, second: B)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "first"(): A
public "second"(): B
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<A, B> = ($Pair<(A), (B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<A, B> = $Pair$Type<(A), (B)>;
}}
declare module "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IDrawableAnimated$StartDirection extends $Enum<($IDrawableAnimated$StartDirection)> {
static readonly "TOP": $IDrawableAnimated$StartDirection
static readonly "BOTTOM": $IDrawableAnimated$StartDirection
static readonly "LEFT": $IDrawableAnimated$StartDirection
static readonly "RIGHT": $IDrawableAnimated$StartDirection


public static "values"(): ($IDrawableAnimated$StartDirection)[]
public static "valueOf"(arg0: string): $IDrawableAnimated$StartDirection
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDrawableAnimated$StartDirection$Type = (("top") | ("left") | ("bottom") | ("right")) | ($IDrawableAnimated$StartDirection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDrawableAnimated$StartDirection_ = $IDrawableAnimated$StartDirection$Type;
}}
declare module "packages/mezz/jei/common/transfer/$BasicRecipeTransferHandlerServer" {
import {$TransferOperation, $TransferOperation$Type} from "packages/mezz/jei/common/transfer/$TransferOperation"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export class $BasicRecipeTransferHandlerServer {


public static "setItems"(arg0: $Player$Type, arg1: $List$Type<($TransferOperation$Type)>, arg2: $List$Type<($Slot$Type)>, arg3: $List$Type<($Slot$Type)>, arg4: boolean, arg5: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicRecipeTransferHandlerServer$Type = ($BasicRecipeTransferHandlerServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicRecipeTransferHandlerServer_ = $BasicRecipeTransferHandlerServer$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientSet" {
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AbstractSet, $AbstractSet$Type} from "packages/java/util/$AbstractSet"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $IngredientSet<V> extends $AbstractSet<(V)> {


public "getByUid"(arg0: string): $Optional<(V)>
public "add"(arg0: V): boolean
public "remove"(arg0: any): boolean
public "clear"(): void
public "size"(): integer
public "iterator"(): $Iterator<(V)>
public "contains"(arg0: any): boolean
public static "create"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $UidContext$Type): $IngredientSet<(V)>
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public "isEmpty"(): boolean
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientSet$Type<V> = ($IngredientSet<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientSet_<V> = $IngredientSet$Type<(V)>;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientVisibility$IListener" {
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IIngredientVisibility$IListener {

 "onIngredientVisibilityChanged"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void

(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void
}

export namespace $IIngredientVisibility$IListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientVisibility$IListener$Type = ($IIngredientVisibility$IListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientVisibility$IListener_ = $IIngredientVisibility$IListener$Type;
}}
declare module "packages/mezz/jei/common/transfer/$RecipeTransferUtil" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TransferOperation, $TransferOperation$Type} from "packages/mezz/jei/common/transfer/$TransferOperation"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$RecipeTransferOperationsResult, $RecipeTransferOperationsResult$Type} from "packages/mezz/jei/common/transfer/$RecipeTransferOperationsResult"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RecipeTransferUtil {


public static "getRecipeTransferOperations"(arg0: $IStackHelper$Type, arg1: $Map$Type<($Slot$Type), ($ItemStack$Type)>, arg2: $List$Type<($IRecipeSlotView$Type)>, arg3: $List$Type<($Slot$Type)>): $RecipeTransferOperationsResult
public static "transferRecipe"(arg0: $IRecipeTransferManager$Type, arg1: $AbstractContainerMenu$Type, arg2: $IRecipeLayoutDrawable$Type<(any)>, arg3: $Player$Type, arg4: boolean): boolean
public static "getTransferRecipeError"(arg0: $IRecipeTransferManager$Type, arg1: $AbstractContainerMenu$Type, arg2: $IRecipeLayoutDrawable$Type<(any)>, arg3: $Player$Type): $Optional<($IRecipeTransferError)>
public static "validateSlots"(arg0: $Player$Type, arg1: $Collection$Type<($TransferOperation$Type)>, arg2: $Collection$Type<($Slot$Type)>, arg3: $Collection$Type<($Slot$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferUtil$Type = ($RecipeTransferUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferUtil_ = $RecipeTransferUtil$Type;
}}
declare module "packages/mezz/jei/gui/events/$GuiEventHandler" {
import {$IngredientListOverlay, $IngredientListOverlay$Type} from "packages/mezz/jei/gui/overlay/$IngredientListOverlay"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BookmarkOverlay, $BookmarkOverlay$Type} from "packages/mezz/jei/gui/overlay/bookmarks/$BookmarkOverlay"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $GuiEventHandler {

constructor(arg0: $IScreenHelper$Type, arg1: $BookmarkOverlay$Type, arg2: $IngredientListOverlay$Type)

public "onDrawBackgroundPost"(arg0: $Screen$Type, arg1: $GuiGraphics$Type): void
public "renderCompactPotionIndicators"(): boolean
public "onDrawForeground"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "onGuiInit"(arg0: $Screen$Type): void
public "onDrawScreenPost"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "onGuiOpen"(arg0: $Screen$Type): void
public "onClientTick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiEventHandler$Type = ($GuiEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiEventHandler_ = $GuiEventHandler$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/replacers/$TippedArrowRecipeMaker" {
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $TippedArrowRecipeMaker {


public static "createRecipes"(arg0: $IStackHelper$Type): $List<($CraftingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TippedArrowRecipeMaker$Type = ($TippedArrowRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TippedArrowRecipeMaker_ = $TippedArrowRecipeMaker$Type;
}}
declare module "packages/mezz/jei/library/config/$IModIdFormatConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IModIdFormatConfig {

 "isModNameFormatOverrideActive"(): boolean
 "getModNameFormat"(): string
}

export namespace $IModIdFormatConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModIdFormatConfig$Type = ($IModIdFormatConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModIdFormatConfig_ = $IModIdFormatConfig$Type;
}}
declare module "packages/mezz/jei/forge/network/$ConnectionToClient" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/mezz/jei/forge/network/$NetworkHandler"
import {$IConnectionToClient, $IConnectionToClient$Type} from "packages/mezz/jei/common/network/$IConnectionToClient"

export class $ConnectionToClient implements $IConnectionToClient {

constructor(arg0: $NetworkHandler$Type)

public "sendPacketToClient"(arg0: $PacketJei$Type, arg1: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConnectionToClient$Type = ($ConnectionToClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConnectionToClient_ = $ConnectionToClient$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$ConfigButton" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$GuiIconToggleButton, $GuiIconToggleButton$Type} from "packages/mezz/jei/gui/elements/$GuiIconToggleButton"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"

export class $ConfigButton extends $GuiIconToggleButton {


public static "create"(arg0: $BooleanSupplier$Type, arg1: $IClientToggleState$Type, arg2: $Textures$Type, arg3: $IInternalKeyMappings$Type): $ConfigButton
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigButton$Type = ($ConfigButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigButton_ = $ConfigButton$Type;
}}
declare module "packages/mezz/jei/common/config/file/serializers/$ListSerializer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiConfigListValueSerializer, $IJeiConfigListValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigListValueSerializer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export class $ListSerializer<T> implements $IJeiConfigListValueSerializer<(T)> {

constructor(arg0: $IJeiConfigValueSerializer$Type<(T)>)

public "isValid"(arg0: $List$Type<(T)>): boolean
public "getValidValuesDescription"(): string
public "getAllValidValues"(): $Optional<($Collection<($List<(T)>)>)>
public "serialize"(arg0: $List$Type<(T)>): string
public "getListValueSerializer"(): $IJeiConfigValueSerializer<(T)>
get "validValuesDescription"(): string
get "allValidValues"(): $Optional<($Collection<($List<(T)>)>)>
get "listValueSerializer"(): $IJeiConfigValueSerializer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListSerializer$Type<T> = ($ListSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListSerializer_<T> = $ListSerializer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/gui/ingredients/$RecipeSlot" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeSlotTooltipCallback, $IRecipeSlotTooltipCallback$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RecipeSlot implements $IRecipeSlotView, $IRecipeSlotDrawable {

constructor(arg0: $IIngredientManager$Type, arg1: $RecipeIngredientRole$Type, arg2: integer, arg3: integer, arg4: integer)

public "addRenderOverride"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $IIngredientRenderer$Type<(T)>): void
public "drawHoverOverlays"(arg0: $GuiGraphics$Type): void
public "getRect"(): $Rect2i
public "isEmpty"(): boolean
public "set"(arg0: $List$Type<($Optional$Type<($ITypedIngredient$Type<(any)>)>)>, arg1: $Set$Type<(integer)>, arg2: $IIngredientVisibility$Type): void
public "draw"(arg0: $GuiGraphics$Type): void
public "getAllIngredients"(): $Stream<($ITypedIngredient<(any)>)>
public "getDisplayedIngredient"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
public "getDisplayedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
public "getTooltip"(): $List<($Component)>
public "addTooltipCallback"(arg0: $IRecipeSlotTooltipCallback$Type): void
public "setOverlay"(arg0: $IDrawable$Type): void
public "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "drawHighlight"(arg0: $GuiGraphics$Type, arg1: integer): void
public "setBackground"(arg0: $IDrawable$Type): void
public "getSlotName"(): $Optional<(string)>
public "getRole"(): $RecipeIngredientRole
public "setSlotName"(arg0: string): void
public "getDisplayedItemStack"(): $Optional<($ItemStack)>
public "getItemStacks"(): $Stream<($ItemStack)>
get "rect"(): $Rect2i
get "empty"(): boolean
get "allIngredients"(): $Stream<($ITypedIngredient<(any)>)>
get "displayedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
get "tooltip"(): $List<($Component)>
set "overlay"(value: $IDrawable$Type)
set "background"(value: $IDrawable$Type)
get "slotName"(): $Optional<(string)>
get "role"(): $RecipeIngredientRole
set "slotName"(value: string)
get "displayedItemStack"(): $Optional<($ItemStack)>
get "itemStacks"(): $Stream<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSlot$Type = ($RecipeSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSlot_ = $RecipeSlot$Type;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IJeiCompostingRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IJeiCompostingRecipe {

 "getInputs"(): $List<($ItemStack)>
 "getChance"(): float
}

export namespace $IJeiCompostingRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiCompostingRecipe$Type = ($IJeiCompostingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiCompostingRecipe_ = $IJeiCompostingRecipe$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientFilterApiDummy" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientFilter, $IIngredientFilter$Type} from "packages/mezz/jei/api/runtime/$IIngredientFilter"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $IngredientFilterApiDummy implements $IIngredientFilter {
static readonly "INSTANCE": $IIngredientFilter


public "getFilterText"(): string
public "setFilterText"(arg0: string): void
public "getFilteredIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
public "getFilteredItemStacks"(): $List<($ItemStack)>
get "filterText"(): string
set "filterText"(value: string)
get "filteredItemStacks"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientFilterApiDummy$Type = ($IngredientFilterApiDummy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientFilterApiDummy_ = $IngredientFilterApiDummy$Type;
}}
declare module "packages/mezz/jei/forge/startup/$ForgePluginFinder" {
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ForgePluginFinder {


public static "getModPlugins"(): $List<($IModPlugin)>
get "modPlugins"(): $List<($IModPlugin)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePluginFinder$Type = ($ForgePluginFinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePluginFinder_ = $ForgePluginFinder$Type;
}}
declare module "packages/mezz/jei/common/util/$ImmutableRect2i" {
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $ImmutableRect2i {
static readonly "EMPTY": $ImmutableRect2i

constructor(arg0: $Rect2i$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "contains"(arg0: double, arg1: double): boolean
public "intersects"(arg0: $ImmutableRect2i$Type): boolean
public "getY"(): integer
public "getX"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "addOffset"(arg0: integer, arg1: integer): $ImmutableRect2i
public "moveLeft"(arg0: integer): $ImmutableRect2i
public "moveRight"(arg0: integer): $ImmutableRect2i
public "toMutable"(): $Rect2i
public "keepBottom"(arg0: integer): $ImmutableRect2i
public "keepRight"(arg0: integer): $ImmutableRect2i
public "expandBy"(arg0: integer): $ImmutableRect2i
public "keepLeft"(arg0: integer): $ImmutableRect2i
public "cropBottom"(arg0: integer): $ImmutableRect2i
public "cropLeft"(arg0: integer): $ImmutableRect2i
public "cropTop"(arg0: integer): $ImmutableRect2i
public "cropRight"(arg0: integer): $ImmutableRect2i
public "keepTop"(arg0: integer): $ImmutableRect2i
public "insetBy"(arg0: integer): $ImmutableRect2i
public "matchWidthAndX"(arg0: $ImmutableRect2i$Type): $ImmutableRect2i
public "moveUp"(arg0: integer): $ImmutableRect2i
public "moveDown"(arg0: integer): $ImmutableRect2i
get "empty"(): boolean
get "y"(): integer
get "x"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmutableRect2i$Type = ($ImmutableRect2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmutableRect2i_ = $ImmutableRect2i$Type;
}}
declare module "packages/mezz/jei/api/gui/$ITickTimer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ITickTimer {

 "getValue"(): integer
 "getMaxValue"(): integer
}

export namespace $ITickTimer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITickTimer$Type = ($ITickTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITickTimer_ = $ITickTimer$Type;
}}
declare module "packages/mezz/jei/common/config/sorting/serializers/$SortingSerializers" {
import {$ISortingSerializer, $ISortingSerializer$Type} from "packages/mezz/jei/common/config/sorting/serializers/$ISortingSerializer"

export class $SortingSerializers {
static readonly "STRING": $ISortingSerializer<(string)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortingSerializers$Type = ($SortingSerializers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortingSerializers_ = $SortingSerializers$Type;
}}
declare module "packages/mezz/jei/library/color/$ColorUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorUtil {


public static "fastPerceptualColorDistanceSquared"(arg0: (integer)[], arg1: (integer)[]): double
public static "slowPerceptualColorDistanceSquared"(arg0: integer, arg1: integer): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorUtil$Type = ($ColorUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorUtil_ = $ColorUtil$Type;
}}
declare module "packages/mezz/jei/common/input/$ClickableIngredient" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $ClickableIngredient<V> implements $IClickableIngredient<(V)> {

constructor(arg0: $ITypedIngredient$Type<(V)>, arg1: $ImmutableRect2i$Type)

public "getTypedIngredient"(): $ITypedIngredient<(V)>
public "getArea"(): $Rect2i
get "typedIngredient"(): $ITypedIngredient<(V)>
get "area"(): $Rect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClickableIngredient$Type<V> = ($ClickableIngredient<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClickableIngredient_<V> = $ClickableIngredient$Type<(V)>;
}}
declare module "packages/mezz/jei/gui/input/$GuiContainerWrapper" {
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $GuiContainerWrapper implements $IRecipeFocusSource {

constructor(arg0: $IScreenHelper$Type)

public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiContainerWrapper$Type = ($GuiContainerWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiContainerWrapper_ = $GuiContainerWrapper$Type;
}}
declare module "packages/mezz/jei/api/$IModPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export interface $IModPlugin {

 "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
 "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
 "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
 "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
 "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
 "onRuntimeUnavailable"(): void
 "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
 "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
 "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
 "getPluginUid"(): $ResourceLocation
 "registerRecipes"(arg0: $IRecipeRegistration$Type): void
 "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
 "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
 "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
 "registerRuntime"(arg0: $IRuntimeRegistration$Type): void

(arg0: $ISubtypeRegistration$Type): void
}

export namespace $IModPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModPlugin$Type = ($IModPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModPlugin_ = $IModPlugin$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$ProxyDragHandler" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"

export class $ProxyDragHandler implements $IDragHandler {

constructor(arg0: $Supplier$Type<($IDragHandler$Type)>)

public "handleDragCanceled"(): void
public "handleDragStart"(arg0: $Screen$Type, arg1: $UserInput$Type): $Optional<($IDragHandler)>
public "handleDragComplete"(arg0: $Screen$Type, arg1: $UserInput$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyDragHandler$Type = ($ProxyDragHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyDragHandler_ = $ProxyDragHandler$Type;
}}
declare module "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter" {
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export interface $IIngredientSubtypeInterpreter<T> {

 "apply"(arg0: T, arg1: $UidContext$Type): string

(arg0: T, arg1: $UidContext$Type): string
}

export namespace $IIngredientSubtypeInterpreter {
const NONE: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientSubtypeInterpreter$Type<T> = ($IIngredientSubtypeInterpreter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientSubtypeInterpreter_<T> = $IIngredientSubtypeInterpreter$Type<(T)>;
}}
declare module "packages/mezz/jei/library/config/$EditModeConfig$ISerializer" {
import {$EditModeConfig, $EditModeConfig$Type} from "packages/mezz/jei/library/config/$EditModeConfig"

export interface $EditModeConfig$ISerializer {

 "load"(arg0: $EditModeConfig$Type): void
 "initialize"(arg0: $EditModeConfig$Type): void
 "save"(arg0: $EditModeConfig$Type): void
}

export namespace $EditModeConfig$ISerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditModeConfig$ISerializer$Type = ($EditModeConfig$ISerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditModeConfig$ISerializer_ = $EditModeConfig$ISerializer$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/stonecutting/$StoneCuttingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$StonecutterRecipe, $StonecutterRecipe$Type} from "packages/net/minecraft/world/item/crafting/$StonecutterRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $StoneCuttingRecipeCategory implements $IRecipeCategory<($StonecutterRecipe)> {
static readonly "width": integer
static readonly "height": integer

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($StonecutterRecipe)>
public "isHandled"(arg0: $StonecutterRecipe$Type): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $StonecutterRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $StonecutterRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "handleInput"(arg0: $StonecutterRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $StonecutterRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $StonecutterRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($StonecutterRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StoneCuttingRecipeCategory$Type = ($StoneCuttingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StoneCuttingRecipeCategory_ = $StoneCuttingRecipeCategory$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$AbstractCookingCategory" {
import {$FurnaceVariantCategory, $FurnaceVariantCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$FurnaceVariantCategory"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$AbstractCookingRecipe, $AbstractCookingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$AbstractCookingRecipe"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $AbstractCookingCategory<T extends $AbstractCookingRecipe> extends $FurnaceVariantCategory<(T)> {

constructor(arg0: $IGuiHelper$Type, arg1: $Block$Type, arg2: string, arg3: integer)

public "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "isHandled"(arg0: T): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCookingCategory$Type<T> = ($AbstractCookingCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCookingCategory_<T> = $AbstractCookingCategory$Type<(T)>;
}}
declare module "packages/mezz/jei/common/util/$MinecraftLocaleSupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $MinecraftLocaleSupplier implements $Supplier<($Locale)> {

constructor()

public "get"(): $Locale
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinecraftLocaleSupplier$Type = ($MinecraftLocaleSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinecraftLocaleSupplier_ = $MinecraftLocaleSupplier$Type;
}}
declare module "packages/mezz/jei/library/util/$RecipeErrorUtil" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeErrorUtil {


public static "getInfoFromRecipe"<T>(arg0: T, arg1: $IRecipeCategory$Type<(T)>, arg2: $IIngredientManager$Type): string
public static "getNameForRecipe"(arg0: any): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeErrorUtil$Type = ($RecipeErrorUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeErrorUtil_ = $RecipeErrorUtil$Type;
}}
declare module "packages/mezz/jei/library/config/$ColorNameConfig" {
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"

export class $ColorNameConfig {

constructor(arg0: $IConfigSchemaBuilder$Type)

public "getClosestColorName"(arg0: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorNameConfig$Type = ($ColorNameConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorNameConfig_ = $ColorNameConfig$Type;
}}
declare module "packages/mezz/jei/common/config/$DebugConfig" {
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"

export class $DebugConfig {


public static "create"(arg0: $IConfigSchemaBuilder$Type): void
public static "isDebugInputsEnabled"(): boolean
public static "isDebugModeEnabled"(): boolean
public static "isCrashingTestIngredientsEnabled"(): boolean
get "debugInputsEnabled"(): boolean
get "debugModeEnabled"(): boolean
get "crashingTestIngredientsEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugConfig$Type = ($DebugConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugConfig_ = $DebugConfig$Type;
}}
declare module "packages/mezz/jei/forge/platform/$IngredientHelper" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IPlatformIngredientHelper, $IPlatformIngredientHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformIngredientHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $IngredientHelper implements $IPlatformIngredientHelper {

constructor()

public "createNbtIngredient"(arg0: $ItemStack$Type, arg1: $IStackHelper$Type): $Ingredient
public "getPotionContainers"(): $List<($Ingredient)>
public "createShulkerDyeIngredient"(arg0: $DyeColor$Type): $Ingredient
get "potionContainers"(): $List<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientHelper$Type = ($IngredientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientHelper_ = $IngredientHelper$Type;
}}
declare module "packages/mezz/jei/library/config/$RecipeCategorySortingConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$MappedSortingConfig, $MappedSortingConfig$Type} from "packages/mezz/jei/common/config/sorting/$MappedSortingConfig"

export class $RecipeCategorySortingConfig extends $MappedSortingConfig<($RecipeType<(any)>), (string)> {

constructor(arg0: $Path$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategorySortingConfig$Type = ($RecipeCategorySortingConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategorySortingConfig_ = $RecipeCategorySortingConfig$Type;
}}
declare module "packages/mezz/jei/library/util/$RecipeUtil" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RecipeUtil {

constructor()

public static "getResultItem"(arg0: $Recipe$Type<(any)>): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeUtil$Type = ($RecipeUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeUtil_ = $RecipeUtil$Type;
}}
declare module "packages/mezz/jei/api/ingredients/$ITypedIngredient" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ITypedIngredient<T> {

 "getType"(): $IIngredientType<(T)>
 "getIngredient"<V>(arg0: $IIngredientType$Type<(V)>): $Optional<(V)>
 "getIngredient"(): T
 "getItemStack"(): $Optional<($ItemStack)>
}

export namespace $ITypedIngredient {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITypedIngredient$Type<T> = ($ITypedIngredient<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITypedIngredient_<T> = $ITypedIngredient$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/util/$CommandUtil" {
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GiveAmount, $GiveAmount$Type} from "packages/mezz/jei/gui/util/$GiveAmount"

export class $CommandUtil {

constructor(arg0: $IClientConfig$Type, arg1: $IConnectionToServer$Type)

public "setHotbarStack"(arg0: $ItemStack$Type, arg1: integer): void
public "giveStack"(arg0: $ItemStack$Type, arg1: $GiveAmount$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandUtil$Type = ($CommandUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandUtil_ = $CommandUtil$Type;
}}
declare module "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder" {
import {$IIngredientAcceptor, $IIngredientAcceptor$Type} from "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeSlotBuilder, $IRecipeSlotBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder"

export interface $IRecipeLayoutBuilder {

 "moveRecipeTransferButton"(arg0: integer, arg1: integer): void
 "addInvisibleIngredients"(arg0: $RecipeIngredientRole$Type): $IIngredientAcceptor<(any)>
 "setShapeless"(arg0: integer, arg1: integer): void
 "setShapeless"(): void
 "createFocusLink"(...arg0: ($IIngredientAcceptor$Type<(any)>)[]): void
 "addSlot"(arg0: $RecipeIngredientRole$Type, arg1: integer, arg2: integer): $IRecipeSlotBuilder
}

export namespace $IRecipeLayoutBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeLayoutBuilder$Type = ($IRecipeLayoutBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeLayoutBuilder_ = $IRecipeLayoutBuilder$Type;
}}
declare module "packages/mezz/jei/common/config/file/$IConfigCategoryBuilder" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export interface $IConfigCategoryBuilder {

 "addEnum"<T extends $Enum<(T)>>(arg0: string, arg1: T, arg2: string): $Supplier<(T)>
 "addInteger"(arg0: string, arg1: integer, arg2: integer, arg3: integer, arg4: string): $Supplier<(integer)>
 "addList"<T>(arg0: string, arg1: $List$Type<(T)>, arg2: $IJeiConfigValueSerializer$Type<($List$Type<(T)>)>, arg3: string): $Supplier<($List<(T)>)>
 "addBoolean"(arg0: string, arg1: boolean, arg2: string): $Supplier<(boolean)>
}

export namespace $IConfigCategoryBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigCategoryBuilder$Type = ($IConfigCategoryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigCategoryBuilder_ = $IConfigCategoryBuilder$Type;
}}
declare module "packages/mezz/jei/gui/input/$UserInput$MouseOverable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $UserInput$MouseOverable {

 "isMouseOver"(arg0: double, arg1: double): boolean

(arg0: double, arg1: double): boolean
}

export namespace $UserInput$MouseOverable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserInput$MouseOverable$Type = ($UserInput$MouseOverable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserInput$MouseOverable_ = $UserInput$MouseOverable$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/$JeiDebugPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JeiDebugPlugin implements $IModPlugin {

constructor()

public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiDebugPlugin$Type = ($JeiDebugPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiDebugPlugin_ = $JeiDebugPlugin$Type;
}}
declare module "packages/mezz/jei/gui/input/$IRecipeFocusSource" {
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export interface $IRecipeFocusSource {

 "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>

(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
}

export namespace $IRecipeFocusSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeFocusSource$Type = ($IRecipeFocusSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeFocusSource_ = $IRecipeFocusSource$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/brewing/$BrewingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $BrewingRecipeCategory implements $IRecipeCategory<($IJeiBrewingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($IJeiBrewingRecipe)>
public "draw"(arg0: $IJeiBrewingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $IJeiBrewingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $IJeiBrewingRecipe$Type): boolean
public "handleInput"(arg0: $IJeiBrewingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $IJeiBrewingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $IJeiBrewingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($IJeiBrewingRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeCategory$Type = ($BrewingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeCategory_ = $BrewingRecipeCategory$Type;
}}
declare module "packages/mezz/jei/core/search/suffixtree/$Edge" {
import {$Node, $Node$Type} from "packages/mezz/jei/core/search/suffixtree/$Node"
import {$SubString, $SubString$Type} from "packages/mezz/jei/core/util/$SubString"

export class $Edge<T> extends $SubString {

constructor(arg0: $SubString$Type, arg1: $Node$Type<(T)>)

public "getDest"(): $Node<(T)>
get "dest"(): $Node<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Edge$Type<T> = ($Edge<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Edge_<T> = $Edge$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeCatalysts" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"

export class $RecipeCatalysts implements $IRecipeFocusSource {

constructor(arg0: $Textures$Type, arg1: $IRecipeManager$Type)

public "isEmpty"(): boolean
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): $Optional<($IRecipeSlotDrawable)>
public "getWidth"(): integer
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "updateLayout"(arg0: $List$Type<($ITypedIngredient$Type<(any)>)>, arg1: $ImmutableRect2i$Type): void
get "empty"(): boolean
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCatalysts$Type = ($RecipeCatalysts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCatalysts_ = $RecipeCatalysts$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$GlobalInputHandler" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $GlobalInputHandler implements $IUserInputHandler {

constructor(arg0: $IClientToggleState$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalInputHandler$Type = ($GlobalInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalInputHandler_ = $GlobalInputHandler$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $IRecipeTransferManager {

 "getRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: C, arg1: $IRecipeCategory$Type<(R)>): $Optional<($IRecipeTransferHandler<(C), (R)>)>

(arg0: C, arg1: $IRecipeCategory$Type<(R)>): $Optional<($IRecipeTransferHandler<(C), (R)>)>
}

export namespace $IRecipeTransferManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferManager$Type = ($IRecipeTransferManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferManager_ = $IRecipeTransferManager$Type;
}}
declare module "packages/mezz/jei/library/color/$ColorThief" {
import {$MMCQ$CMap, $MMCQ$CMap$Type} from "packages/mezz/jei/library/color/$MMCQ$CMap"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $ColorThief {

constructor()

public static "getPalette"(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: boolean): ((integer)[])[]
public static "getColorMap"(arg0: $NativeImage$Type, arg1: integer, arg2: integer, arg3: boolean): $MMCQ$CMap
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorThief$Type = ($ColorThief);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorThief_ = $ColorThief$Type;
}}
declare module "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView" {
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $IRecipeSlotsView {

 "getSlotViews"(): $List<($IRecipeSlotView)>
 "getSlotViews"(arg0: $RecipeIngredientRole$Type): $List<($IRecipeSlotView)>
 "findSlotByName"(arg0: string): $Optional<($IRecipeSlotView)>
}

export namespace $IRecipeSlotsView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeSlotsView$Type = ($IRecipeSlotsView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeSlotsView_ = $IRecipeSlotsView$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeCategoriesLookup" {
import {$IRecipeCategoriesLookup, $IRecipeCategoriesLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCategoriesLookup"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$RecipeManagerInternal, $RecipeManagerInternal$Type} from "packages/mezz/jei/library/recipes/$RecipeManagerInternal"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeCategoriesLookup implements $IRecipeCategoriesLookup {

constructor(arg0: $RecipeManagerInternal$Type, arg1: $IIngredientManager$Type)

public "includeHidden"(): $IRecipeCategoriesLookup
public "get"(): $Stream<($IRecipeCategory<(any)>)>
public "limitFocus"(arg0: $Collection$Type<(any)>): $IRecipeCategoriesLookup
public "limitTypes"(arg0: $Collection$Type<($RecipeType$Type<(any)>)>): $IRecipeCategoriesLookup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoriesLookup$Type = ($RecipeCategoriesLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoriesLookup_ = $RecipeCategoriesLookup$Type;
}}
declare module "packages/mezz/jei/api/ingredients/$IIngredientHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export interface $IIngredientHelper<V> {

 "getDisplayName"(arg0: V): string
 "getWildcardId"(arg0: V): string
 "getDisplayModId"(arg0: V): string
 "getTagEquivalent"(arg0: $Collection$Type<(V)>): $Optional<($ResourceLocation)>
 "copyIngredient"(arg0: V): V
 "getTagStream"(arg0: V): $Stream<($ResourceLocation)>
 "getErrorInfo"(arg0: V): string
 "getIngredientType"(): $IIngredientType<(V)>
 "getCheatItemStack"(arg0: V): $ItemStack
 "getUniqueId"(arg0: V, arg1: $UidContext$Type): string
 "isValidIngredient"(arg0: V): boolean
 "normalizeIngredient"(arg0: V): V
 "isIngredientOnServer"(arg0: V): boolean
 "getResourceLocation"(arg0: V): $ResourceLocation
 "getColors"(arg0: V): $Iterable<(integer)>
}

export namespace $IIngredientHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientHelper$Type<V> = ($IIngredientHelper<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientHelper_<V> = $IIngredientHelper$Type<(V)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/anvil/$AnvilRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IJeiAnvilRecipe, $IJeiAnvilRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $AnvilRecipeCategory implements $IRecipeCategory<($IJeiAnvilRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($IJeiAnvilRecipe)>
public "draw"(arg0: $IJeiAnvilRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $IJeiAnvilRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $IJeiAnvilRecipe$Type): boolean
public "handleInput"(arg0: $IJeiAnvilRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $IJeiAnvilRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $IJeiAnvilRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($IJeiAnvilRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilRecipeCategory$Type = ($AnvilRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilRecipeCategory_ = $AnvilRecipeCategory$Type;
}}
declare module "packages/mezz/jei/forge/ingredients/$JeiIngredient" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $JeiIngredient extends $Ingredient {
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList

constructor(arg0: $ItemStack$Type, arg1: $IStackHelper$Type)

public "test"(arg0: $ItemStack$Type): boolean
public "isSimple"(): boolean
public "toJson"(): $JsonElement
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
get "simple"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiIngredient$Type = ($JeiIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiIngredient_ = $JeiIngredient$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$RecipeTransferRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IRecipeTransferHandlerHelper, $IRecipeTransferHandlerHelper$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$ImmutableTable, $ImmutableTable$Type} from "packages/com/google/common/collect/$ImmutableTable"
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $RecipeTransferRegistration implements $IRecipeTransferRegistration {

constructor(arg0: $IStackHelper$Type, arg1: $IRecipeTransferHandlerHelper$Type, arg2: $IJeiHelpers$Type, arg3: $IConnectionToServer$Type)

public "getTransferHelper"(): $IRecipeTransferHandlerHelper
public "getJeiHelpers"(): $IJeiHelpers
public "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferHandler$Type<(C), (R)>, arg1: $RecipeType$Type<(R)>): void
public "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>): void
public "addRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $Class$Type<(any)>, arg1: $MenuType$Type<(C)>, arg2: $RecipeType$Type<(R)>, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public "getRecipeTransferHandlers"(): $ImmutableTable<($Class<(any)>), ($RecipeType<(any)>), ($IRecipeTransferHandler<(any), (any)>)>
public "addUniversalRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferHandler$Type<(C), (R)>): void
get "transferHelper"(): $IRecipeTransferHandlerHelper
get "jeiHelpers"(): $IJeiHelpers
get "recipeTransferHandlers"(): $ImmutableTable<($Class<(any)>), ($RecipeType<(any)>), ($IRecipeTransferHandler<(any), (any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferRegistration$Type = ($RecipeTransferRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferRegistration_ = $RecipeTransferRegistration$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformConfigHelper" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $IPlatformConfigHelper {

 "getConfigScreen"(): $Optional<($Screen)>
 "createJeiConfigDir"(): $Path
 "getModConfigDir"(): $Path
}

export namespace $IPlatformConfigHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformConfigHelper$Type = ($IPlatformConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformConfigHelper_ = $IPlatformConfigHelper$Type;
}}
declare module "packages/mezz/jei/forge/network/$ConnectionToServer" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/mezz/jei/forge/network/$NetworkHandler"

export class $ConnectionToServer implements $IConnectionToServer {

constructor(arg0: $NetworkHandler$Type)

public "isJeiOnServer"(): boolean
public "sendPacketToServer"(arg0: $PacketJei$Type): void
get "jeiOnServer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConnectionToServer$Type = ($ConnectionToServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConnectionToServer_ = $ConnectionToServer$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableIngredient" {
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $DrawableIngredient<V> implements $IDrawable {

constructor(arg0: $IIngredientManager$Type, arg1: $ITypedIngredient$Type<(V)>, arg2: $IIngredientRenderer$Type<(V)>)

public "draw"(arg0: $GuiGraphics$Type): void
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableIngredient$Type<V> = ($DrawableIngredient<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableIngredient_<V> = $DrawableIngredient$Type<(V)>;
}}
declare module "packages/mezz/jei/common/transfer/$RecipeTransferErrorInternal" {
import {$IRecipeTransferError$Type, $IRecipeTransferError$Type$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError$Type"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $RecipeTransferErrorInternal implements $IRecipeTransferError {
static readonly "INSTANCE": $RecipeTransferErrorInternal


public "getType"(): $IRecipeTransferError$Type
public "getButtonHighlightColor"(): integer
public "showError"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $IRecipeSlotsView$Type, arg4: integer, arg5: integer): void
get "type"(): $IRecipeTransferError$Type
get "buttonHighlightColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferErrorInternal$Type = ($RecipeTransferErrorInternal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferErrorInternal_ = $RecipeTransferErrorInternal$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientListOverlay" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IIngredientListOverlay {

 "getVisibleIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
 "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
 "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
 "isListDisplayed"(): boolean
 "hasKeyboardFocus"(): boolean
}

export namespace $IIngredientListOverlay {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientListOverlay$Type = ($IIngredientListOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientListOverlay_ = $IIngredientListOverlay$Type;
}}
declare module "packages/mezz/jei/core/collect/$MultiMap" {
import {$ImmutableMultimap, $ImmutableMultimap$Type} from "packages/com/google/common/collect/$ImmutableMultimap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $MultiMap<K, V, T extends $Collection<(V)>> {

constructor(arg0: $Supplier$Type<(T)>)
constructor(arg0: $Map$Type<(K), (T)>, arg1: $Supplier$Type<(T)>)

public "remove"(arg0: K, arg1: V): boolean
public "get"(arg0: K): $Collection<(V)>
public "put"(arg0: K, arg1: V): boolean
public "clear"(): void
public "contains"(arg0: K, arg1: V): boolean
public "entrySet"(): $Set<($Map$Entry<(K), (T)>)>
public "containsKey"(arg0: K): boolean
public "keySet"(): $Set<(K)>
public "toImmutable"(): $ImmutableMultimap<(K), (V)>
public "allValues"(): $Collection<(V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiMap$Type<K, V, T> = ($MultiMap<(K), (V), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiMap_<K, V, T> = $MultiMap$Type<(K), (V), (T)>;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory" {
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IJeiAnvilRecipe, $IJeiAnvilRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe"

export interface $IVanillaRecipeFactory {

 "createAnvilRecipe"(arg0: $ItemStack$Type, arg1: $List$Type<($ItemStack$Type)>, arg2: $List$Type<($ItemStack$Type)>): $IJeiAnvilRecipe
 "createAnvilRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $List$Type<($ItemStack$Type)>, arg2: $List$Type<($ItemStack$Type)>): $IJeiAnvilRecipe
 "createBrewingRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $List$Type<($ItemStack$Type)>, arg2: $ItemStack$Type): $IJeiBrewingRecipe
 "createBrewingRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $ItemStack$Type, arg2: $ItemStack$Type): $IJeiBrewingRecipe
}

export namespace $IVanillaRecipeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVanillaRecipeFactory$Type = ($IVanillaRecipeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVanillaRecipeFactory_ = $IVanillaRecipeFactory$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeManager" {
import {$IRecipeCatalystLookup, $IRecipeCatalystLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCatalystLookup"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IRecipeLookup, $IRecipeLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeLookup"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$RecipeManagerInternal, $RecipeManagerInternal$Type} from "packages/mezz/jei/library/recipes/$RecipeManagerInternal"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"
import {$IRecipeCategoriesLookup, $IRecipeCategoriesLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCategoriesLookup"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RecipeManager implements $IRecipeManager {

constructor(arg0: $RecipeManagerInternal$Type, arg1: $IModIdHelper$Type, arg2: $IIngredientManager$Type, arg3: $Textures$Type, arg4: $IIngredientVisibility$Type)

public "hideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
public "createRecipeLookup"<R>(arg0: $RecipeType$Type<(R)>): $IRecipeLookup<(R)>
public "hideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
public "getRecipeType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
public "createRecipeCatalystLookup"(arg0: $RecipeType$Type<(any)>): $IRecipeCatalystLookup
public "unhideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
public "createRecipeLayoutDrawable"<T>(arg0: $IRecipeCategory$Type<(T)>, arg1: T, arg2: $IFocusGroup$Type): $Optional<($IRecipeLayoutDrawable<(T)>)>
public "createRecipeSlotDrawable"(arg0: $RecipeIngredientRole$Type, arg1: $List$Type<($Optional$Type<($ITypedIngredient$Type<(any)>)>)>, arg2: $Set$Type<(integer)>, arg3: integer, arg4: integer, arg5: integer): $IRecipeSlotDrawable
public "addRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $List$Type<(T)>): void
public "createRecipeCategoryLookup"(): $IRecipeCategoriesLookup
public "unhideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeManager$Type = ($RecipeManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeManager_ = $RecipeManager$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$TypedIngredient" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $TypedIngredient<T> implements $ITypedIngredient<(T)> {


public static "createAndFilterInvalid"<T>(arg0: $IIngredientManager$Type, arg1: $IIngredientType$Type<(T)>, arg2: T): $Optional<($ITypedIngredient<(T)>)>
public static "createAndFilterInvalid"<T>(arg0: $IIngredientManager$Type, arg1: T): $Optional<($ITypedIngredient<(any)>)>
public "toString"(): string
public "getType"(): $IIngredientType<(T)>
public static "deepCopy"<T>(arg0: $IIngredientManager$Type, arg1: $ITypedIngredient$Type<(T)>): $Optional<($ITypedIngredient<(T)>)>
public "getIngredient"(): T
public static "createUnvalidated"<T>(arg0: $IIngredientType$Type<(T)>, arg1: T): $ITypedIngredient<(T)>
public "getIngredient"<V>(arg0: $IIngredientType$Type<(V)>): $Optional<(V)>
public "getItemStack"(): $Optional<($ItemStack)>
get "type"(): $IIngredientType<(T)>
get "ingredient"(): T
get "itemStack"(): $Optional<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypedIngredient$Type<T> = ($TypedIngredient<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypedIngredient_<T> = $TypedIngredient$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/input/handlers/$FocusInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$CombinedRecipeFocusSource, $CombinedRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$CombinedRecipeFocusSource"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"

export class $FocusInputHandler implements $IUserInputHandler {

constructor(arg0: $CombinedRecipeFocusSource$Type, arg1: $IRecipesGui$Type, arg2: $IFocusFactory$Type, arg3: $IClientConfig$Type, arg4: $IIngredientManager$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusInputHandler$Type = ($FocusInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusInputHandler_ = $FocusInputHandler$Type;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"

export interface $IGuiClickableArea {

 "onClick"(arg0: $IFocusFactory$Type, arg1: $IRecipesGui$Type): void
 "getArea"(): $Rect2i
 "isTooltipEnabled"(): boolean
 "getTooltipStrings"(): $List<($Component)>
}

export namespace $IGuiClickableArea {
function createBasic(arg0: integer, arg1: integer, arg2: integer, arg3: integer, ...arg4: ($RecipeType$Type<(any)>)[]): $IGuiClickableArea
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiClickableArea$Type = ($IGuiClickableArea);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiClickableArea_ = $IGuiClickableArea$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/compostable/$CompostableRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IJeiCompostingRecipe, $IJeiCompostingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiCompostingRecipe"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $CompostableRecipeCategory implements $IRecipeCategory<($IJeiCompostingRecipe)> {
static readonly "width": integer
static readonly "height": integer

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($IJeiCompostingRecipe)>
public "draw"(arg0: $IJeiCompostingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $IJeiCompostingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $IJeiCompostingRecipe$Type): boolean
public "handleInput"(arg0: $IJeiCompostingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $IJeiCompostingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $IJeiCompostingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($IJeiCompostingRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompostableRecipeCategory$Type = ($CompostableRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompostableRecipeCategory_ = $CompostableRecipeCategory$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketGiveItemStack" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GiveMode, $GiveMode$Type} from "packages/mezz/jei/common/config/$GiveMode"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PacketGiveItemStack extends $PacketJei {

constructor(arg0: $ItemStack$Type, arg1: $GiveMode$Type)

public static "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketGiveItemStack$Type = ($PacketGiveItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketGiveItemStack_ = $PacketGiveItemStack$Type;
}}
declare module "packages/mezz/jei/common/$JeiFeatures" {
import {$IJeiFeatures, $IJeiFeatures$Type} from "packages/mezz/jei/api/runtime/$IJeiFeatures"

export class $JeiFeatures implements $IJeiFeatures {

constructor()

public "disableInventoryEffectRendererGuiHandler"(): void
public "getInventoryEffectRendererGuiHandlerEnabled"(): boolean
get "inventoryEffectRendererGuiHandlerEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiFeatures$Type = ($JeiFeatures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiFeatures_ = $JeiFeatures$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientVisibility" {
import {$EditModeConfig, $EditModeConfig$Type} from "packages/mezz/jei/library/config/$EditModeConfig"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IngredientBlacklistInternal, $IngredientBlacklistInternal$Type} from "packages/mezz/jei/library/ingredients/$IngredientBlacklistInternal"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$IIngredientVisibility$IListener, $IIngredientVisibility$IListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility$IListener"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $IngredientVisibility implements $IIngredientVisibility {

constructor(arg0: $IngredientBlacklistInternal$Type, arg1: $IClientToggleState$Type, arg2: $EditModeConfig$Type, arg3: $IIngredientManager$Type)

public "registerListener"(arg0: $IIngredientVisibility$IListener$Type): void
public "isIngredientVisible"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: $IIngredientHelper$Type<(V)>): boolean
public "isIngredientVisible"<V>(arg0: $ITypedIngredient$Type<(V)>): boolean
public "isIngredientVisible"<V>(arg0: $IIngredientType$Type<(V)>, arg1: V): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientVisibility$Type = ($IngredientVisibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientVisibility_ = $IngredientVisibility$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$AdvancedRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IJeiFeatures, $IJeiFeatures$Type} from "packages/mezz/jei/api/runtime/$IJeiFeatures"
import {$IRecipeManagerPlugin, $IRecipeManagerPlugin$Type} from "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin"

export class $AdvancedRegistration implements $IAdvancedRegistration {

constructor(arg0: $IJeiHelpers$Type, arg1: $IJeiFeatures$Type)

public "getJeiHelpers"(): $IJeiHelpers
public "getJeiFeatures"(): $IJeiFeatures
public "addRecipeCategoryDecorator"<T>(arg0: $RecipeType$Type<(T)>, arg1: $IRecipeCategoryDecorator$Type<(T)>): void
public "addRecipeManagerPlugin"(arg0: $IRecipeManagerPlugin$Type): void
public "getRecipeCategoryDecorators"(): $ImmutableListMultimap<($RecipeType<(any)>), ($IRecipeCategoryDecorator<(any)>)>
public "getRecipeManagerPlugins"(): $List<($IRecipeManagerPlugin)>
get "jeiHelpers"(): $IJeiHelpers
get "jeiFeatures"(): $IJeiFeatures
get "recipeCategoryDecorators"(): $ImmutableListMultimap<($RecipeType<(any)>), ($IRecipeCategoryDecorator<(any)>)>
get "recipeManagerPlugins"(): $List<($IRecipeManagerPlugin)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancedRegistration$Type = ($AdvancedRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancedRegistration_ = $AdvancedRegistration$Type;
}}
declare module "packages/mezz/jei/gui/input/$ICharTypedHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ICharTypedHandler {

 "onCharTyped"(arg0: character, arg1: integer): boolean
 "hasKeyboardFocus"(): boolean
}

export namespace $ICharTypedHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICharTypedHandler$Type = ($ICharTypedHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICharTypedHandler_ = $ICharTypedHandler$Type;
}}
declare module "packages/mezz/jei/library/focus/$Focus" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $Focus<V> implements $IFocus<(V)>, $IFocusGroup {

constructor(arg0: $RecipeIngredientRole$Type, arg1: $ITypedIngredient$Type<(V)>)

public static "checkOne"<V>(arg0: $IFocus$Type<(V)>, arg1: $IIngredientManager$Type): $Focus<(V)>
public "getAllFocuses"(): $List<($IFocus<(any)>)>
public "getFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<(any)>)>
public "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<($IFocus<(T)>)>
public "getFocuses"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $RecipeIngredientRole$Type): $Stream<($IFocus<(T)>)>
public "isEmpty"(): boolean
public "checkedCast"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<($IFocus<(T)>)>
public "getRole"(): $RecipeIngredientRole
public static "createFromApi"<V>(arg0: $IIngredientManager$Type, arg1: $RecipeIngredientRole$Type, arg2: $IIngredientType$Type<(V)>, arg3: V): $Focus<(V)>
public static "createFromApi"<V>(arg0: $IIngredientManager$Type, arg1: $RecipeIngredientRole$Type, arg2: $ITypedIngredient$Type<(V)>): $Focus<(V)>
public "getTypedValue"(): $ITypedIngredient<(V)>
public "getItemStackFocuses"(): $Stream<($IFocus<($ItemStack)>)>
public "getItemStackFocuses"(arg0: $RecipeIngredientRole$Type): $Stream<($IFocus<($ItemStack)>)>
get "allFocuses"(): $List<($IFocus<(any)>)>
get "empty"(): boolean
get "role"(): $RecipeIngredientRole
get "typedValue"(): $ITypedIngredient<(V)>
get "itemStackFocuses"(): $Stream<($IFocus<($ItemStack)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Focus$Type<V> = ($Focus<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Focus_<V> = $Focus$Type<(V)>;
}}
declare module "packages/mezz/jei/gui/input/handlers/$CombinedInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CombinedInputHandler implements $IUserInputHandler {

constructor(...arg0: ($IUserInputHandler$Type)[])
constructor(arg0: $List$Type<($IUserInputHandler$Type)>)

public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedInputHandler$Type = ($CombinedInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedInputHandler_ = $CombinedInputHandler$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformIngredientHelper" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export interface $IPlatformIngredientHelper {

 "createNbtIngredient"(arg0: $ItemStack$Type, arg1: $IStackHelper$Type): $Ingredient
 "getPotionContainers"(): $List<($Ingredient)>
 "createShulkerDyeIngredient"(arg0: $DyeColor$Type): $Ingredient
}

export namespace $IPlatformIngredientHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformIngredientHelper$Type = ($IPlatformIngredientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformIngredientHelper_ = $IPlatformIngredientHelper$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IIngredientGrid" {
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export interface $IIngredientGrid extends $IRecipeFocusSource {

 "isMouseOver"(arg0: double, arg1: double): boolean
 "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
}

export namespace $IIngredientGrid {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientGrid$Type = ($IIngredientGrid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientGrid_ = $IIngredientGrid$Type;
}}
declare module "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IIngredientAcceptor<THIS extends $IIngredientAcceptor<(THIS)>> {

 "addIngredientsUnsafe"(arg0: $List$Type<(any)>): THIS
 "addIngredient"<I>(arg0: $IIngredientType$Type<(I)>, arg1: I): THIS
 "addItemStack"(arg0: $ItemStack$Type): THIS
 "addIngredients"(arg0: $Ingredient$Type): THIS
 "addIngredients"<I>(arg0: $IIngredientType$Type<(I)>, arg1: $List$Type<(I)>): THIS
 "addItemStacks"(arg0: $List$Type<($ItemStack$Type)>): THIS
 "addFluidStack"(arg0: $Fluid$Type, arg1: long): THIS
 "addFluidStack"(arg0: $Fluid$Type, arg1: long, arg2: $CompoundTag$Type): THIS
}

export namespace $IIngredientAcceptor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientAcceptor$Type<THIS> = ($IIngredientAcceptor<(THIS)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientAcceptor_<THIS> = $IIngredientAcceptor$Type<(THIS)>;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientListRenderer" {
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IngredientListSlot, $IngredientListSlot$Type} from "packages/mezz/jei/gui/overlay/$IngredientListSlot"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $IngredientListRenderer {

constructor(arg0: $IEditModeConfig$Type, arg1: $IClientToggleState$Type, arg2: $IIngredientManager$Type)

public "add"(arg0: $IngredientListSlot$Type): void
public "clear"(): void
public "size"(): integer
public "set"(arg0: integer, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>): void
public "render"(arg0: $GuiGraphics$Type): void
public "getSlots"(): $Stream<($IngredientListSlot)>
get "slots"(): $Stream<($IngredientListSlot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientListRenderer$Type = ($IngredientListRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientListRenderer_ = $IngredientListRenderer$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"

export interface $IRecipeTransferHandlerHelper {

 "createUnregisteredRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>): $IRecipeTransferHandler<(C), (R)>
 "recipeTransferHasServerSupport"(): boolean
 "createUserErrorForMissingSlots"(arg0: $Component$Type, arg1: $Collection$Type<($IRecipeSlotView$Type)>): $IRecipeTransferError
 "createBasicRecipeTransferInfo"<C extends $AbstractContainerMenu, R>(arg0: $Class$Type<(any)>, arg1: $MenuType$Type<(C)>, arg2: $RecipeType$Type<(R)>, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $IRecipeTransferInfo<(C), (R)>
 "createInternalError"(): $IRecipeTransferError
 "createUserErrorWithTooltip"(arg0: $Component$Type): $IRecipeTransferError
 "createRecipeSlotsView"(arg0: $List$Type<($IRecipeSlotView$Type)>): $IRecipeSlotsView
}

export namespace $IRecipeTransferHandlerHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferHandlerHelper$Type = ($IRecipeTransferHandlerHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferHandlerHelper_ = $IRecipeTransferHandlerHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/brewing/$BrewingRecipeUtil" {
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $BrewingRecipeUtil {
static readonly "POTION": $ItemStack
static readonly "WATER_BOTTLE": $ItemStack

constructor(arg0: $IIngredientHelper$Type<($ItemStack$Type)>)

public "getBrewingSteps"(arg0: $ItemStack$Type): integer
public "addRecipe"(arg0: $List$Type<($ItemStack$Type)>, arg1: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeUtil$Type = ($BrewingRecipeUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeUtil_ = $BrewingRecipeUtil$Type;
}}
declare module "packages/mezz/jei/forge/platform/$ItemStackHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPlatformItemStackHelper, $IPlatformItemStackHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformItemStackHelper"

export class $ItemStackHelper implements $IPlatformItemStackHelper {

constructor()

public "getTestTooltip"(arg0: $Player$Type, arg1: $ItemStack$Type): $List<($Component)>
public "isBookEnchantable"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
public "getBurnTime"(arg0: $ItemStack$Type): integer
public "getCreatorModId"(arg0: $ItemStack$Type): $Optional<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackHelper$Type = ($ItemStackHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackHelper_ = $ItemStackHelper$Type;
}}
declare module "packages/mezz/jei/gui/util/$AlignmentUtil" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$HorizontalAlignment, $HorizontalAlignment$Type} from "packages/mezz/jei/common/util/$HorizontalAlignment"
import {$ImmutableSize2i, $ImmutableSize2i$Type} from "packages/mezz/jei/common/util/$ImmutableSize2i"
import {$VerticalAlignment, $VerticalAlignment$Type} from "packages/mezz/jei/common/util/$VerticalAlignment"

export class $AlignmentUtil {

constructor()

public static "align"(arg0: $ImmutableSize2i$Type, arg1: $ImmutableRect2i$Type, arg2: $HorizontalAlignment$Type, arg3: $VerticalAlignment$Type): $ImmutableRect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlignmentUtil$Type = ($AlignmentUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlignmentUtil_ = $AlignmentUtil$Type;
}}
declare module "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler" {
import {$IClickableIngredient, $IClickableIngredient$Type} from "packages/mezz/jei/api/runtime/$IClickableIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IGuiClickableArea, $IGuiClickableArea$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiClickableArea"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IGuiContainerHandler<T extends $AbstractContainerScreen<(any)>> {

 "getGuiExtraAreas"(arg0: T): $List<($Rect2i)>
 "getGuiClickableAreas"(arg0: T, arg1: double, arg2: double): $Collection<($IGuiClickableArea)>
 "getClickableIngredientUnderMouse"(arg0: T, arg1: double, arg2: double): $Optional<($IClickableIngredient<(any)>)>
}

export namespace $IGuiContainerHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiContainerHandler$Type<T> = ($IGuiContainerHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiContainerHandler_<T> = $IGuiContainerHandler$Type<(T)>;
}}
declare module "packages/mezz/jei/common/util/$StringUtil" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"

export class $StringUtil {


public static "removeChatFormatting"(arg0: string): string
public static "expandNewlines"(...arg0: ($Component$Type)[]): $List<($FormattedText)>
public static "stripStyling"(arg0: $Component$Type): $Component
public static "intsToString"(arg0: $Collection$Type<(integer)>): string
public static "truncateStringToWidth"(arg0: $Component$Type, arg1: integer, arg2: $Font$Type): $Component
public static "splitLines"(arg0: $List$Type<($FormattedText$Type)>, arg1: integer): $List<($FormattedText)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringUtil$Type = ($StringUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringUtil_ = $StringUtil$Type;
}}
declare module "packages/mezz/jei/library/transfer/$RecipeTransferHandlerHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferHandlerHelper, $IRecipeTransferHandlerHelper$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $RecipeTransferHandlerHelper implements $IRecipeTransferHandlerHelper {

constructor(arg0: $IStackHelper$Type)

public "createUnregisteredRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>): $IRecipeTransferHandler<(C), (R)>
public "recipeTransferHasServerSupport"(): boolean
public "createUserErrorForMissingSlots"(arg0: $Component$Type, arg1: $Collection$Type<($IRecipeSlotView$Type)>): $IRecipeTransferError
public "createBasicRecipeTransferInfo"<C extends $AbstractContainerMenu, R>(arg0: $Class$Type<(any)>, arg1: $MenuType$Type<(C)>, arg2: $RecipeType$Type<(R)>, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $IRecipeTransferInfo<(C), (R)>
public "createInternalError"(): $IRecipeTransferError
public "createUserErrorWithTooltip"(arg0: $Component$Type): $IRecipeTransferError
public "createRecipeSlotsView"(arg0: $List$Type<($IRecipeSlotView$Type)>): $IRecipeSlotsView
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferHandlerHelper$Type = ($RecipeTransferHandlerHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferHandlerHelper_ = $RecipeTransferHandlerHelper$Type;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigSchema" {
import {$ConfigCategory, $ConfigCategory$Type} from "packages/mezz/jei/common/config/file/$ConfigCategory"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigSchema, $IConfigSchema$Type} from "packages/mezz/jei/common/config/file/$IConfigSchema"
import {$FileWatcher, $FileWatcher$Type} from "packages/mezz/jei/common/config/file/$FileWatcher"
import {$ConfigManager, $ConfigManager$Type} from "packages/mezz/jei/common/config/$ConfigManager"
import {$ConfigCategoryBuilder, $ConfigCategoryBuilder$Type} from "packages/mezz/jei/common/config/file/$ConfigCategoryBuilder"

export class $ConfigSchema implements $IConfigSchema {

constructor(arg0: $Path$Type, arg1: $List$Type<($ConfigCategoryBuilder$Type)>)

public "register"(arg0: $FileWatcher$Type, arg1: $ConfigManager$Type): void
public "getPath"(): $Path
public "markDirty"(): void
public "getCategories"(): $List<($ConfigCategory)>
public "loadIfNeeded"(): void
get "path"(): $Path
get "categories"(): $List<($ConfigCategory)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSchema$Type = ($ConfigSchema);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSchema_ = $ConfigSchema$Type;
}}
declare module "packages/mezz/jei/common/network/$IPacketId" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPacketId {

 "ordinal"(): integer

(): integer
}

export namespace $IPacketId {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPacketId$Type = ($IPacketId);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPacketId_ = $IPacketId$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$IJeiKeyMappingInternal" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IJeiKeyMappingInternal extends $IJeiKeyMapping {

 "register"(arg0: $Consumer$Type<($KeyMapping$Type)>): $IJeiKeyMapping
 "isUnbound"(): boolean
 "getTranslatedKeyMessage"(): $Component
 "isActiveAndMatches"(arg0: $InputConstants$Key$Type): boolean
}

export namespace $IJeiKeyMappingInternal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiKeyMappingInternal$Type = ($IJeiKeyMappingInternal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiKeyMappingInternal_ = $IJeiKeyMappingInternal$Type;
}}
declare module "packages/mezz/jei/core/search/$PrefixInfo$IStringsGetter" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $PrefixInfo$IStringsGetter<T> {

 "getStrings"(arg0: T): $Collection<(string)>

(arg0: T): $Collection<(string)>
}

export namespace $PrefixInfo$IStringsGetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixInfo$IStringsGetter$Type<T> = ($PrefixInfo$IStringsGetter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixInfo$IStringsGetter_<T> = $PrefixInfo$IStringsGetter$Type<(T)>;
}}
declare module "packages/mezz/jei/core/search/$ISearchable" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"

export interface $ISearchable<T> {

 "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
 "getMode"(): $SearchMode
 "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
}

export namespace $ISearchable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISearchable$Type<T> = ($ISearchable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISearchable_<T> = $ISearchable$Type<(T)>;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientFilter" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IIngredientFilter {

 "getFilterText"(): string
 "setFilterText"(arg0: string): void
 "getFilteredIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
 "getFilteredItemStacks"(): $List<($ItemStack)>
}

export namespace $IIngredientFilter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientFilter$Type = ($IIngredientFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientFilter_ = $IIngredientFilter$Type;
}}
declare module "packages/mezz/jei/common/util/$ErrorUtil" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$CrashReport, $CrashReport$Type} from "packages/net/minecraft/$CrashReport"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $ErrorUtil {


public static "checkNotNull"(arg0: $Collection$Type<(any)>, arg1: string): void
public static "checkNotNull"<T>(arg0: T, arg1: string): void
public static "getItemStackInfo"(arg0: $ItemStack$Type): string
public static "createIngredientCrashReport"<T>(arg0: $Throwable$Type, arg1: string, arg2: $IIngredientManager$Type, arg3: $ITypedIngredient$Type<(T)>): $CrashReport
public static "assertMainThread"(): void
public static "checkNotEmpty"(arg0: $ItemStack$Type): void
public static "checkNotEmpty"(arg0: $ItemStack$Type, arg1: string): void
public static "checkNotEmpty"<T>(arg0: (T)[], arg1: string): void
public static "checkNotEmpty"(arg0: $Collection$Type<(any)>, arg1: string): void
public static "logIngredientCrash"<T>(arg0: $Throwable$Type, arg1: string, arg2: $IIngredientManager$Type, arg3: $ITypedIngredient$Type<(T)>): void
public static "getIngredientInfo"<T>(arg0: T, arg1: $IIngredientType$Type<(T)>, arg2: $IIngredientManager$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorUtil$Type = ($ErrorUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorUtil_ = $ErrorUtil$Type;
}}
declare module "packages/mezz/jei/library/load/$PluginHelper" {
import {$JeiInternalPlugin, $JeiInternalPlugin$Type} from "packages/mezz/jei/library/plugins/jei/$JeiInternalPlugin"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$VanillaPlugin, $VanillaPlugin$Type} from "packages/mezz/jei/library/plugins/vanilla/$VanillaPlugin"

export class $PluginHelper {

constructor()

public static "sortPlugins"(arg0: $List$Type<($IModPlugin$Type)>, arg1: $VanillaPlugin$Type, arg2: $JeiInternalPlugin$Type): void
public static "getPluginWithClass"<T extends $IModPlugin>(arg0: $Class$Type<(any)>, arg1: $List$Type<($IModPlugin$Type)>): $Optional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginHelper$Type = ($PluginHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginHelper_ = $PluginHelper$Type;
}}
declare module "packages/mezz/jei/library/transfer/$RecipeTransferErrorTooltip" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeTransferError$Type, $IRecipeTransferError$Type$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError$Type"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $RecipeTransferErrorTooltip implements $IRecipeTransferError {

constructor(arg0: $Component$Type)

public "getType"(): $IRecipeTransferError$Type
public "showError"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $IRecipeSlotsView$Type, arg4: integer, arg5: integer): void
public "getButtonHighlightColor"(): integer
get "type"(): $IRecipeTransferError$Type
get "buttonHighlightColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferErrorTooltip$Type = ($RecipeTransferErrorTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferErrorTooltip_ = $RecipeTransferErrorTooltip$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IRecipeLookup" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export interface $IRecipeLookup<R> {

 "includeHidden"(): $IRecipeLookup<(R)>
 "get"(): $Stream<(R)>
 "limitFocus"(arg0: $Collection$Type<(any)>): $IRecipeLookup<(R)>
}

export namespace $IRecipeLookup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeLookup$Type<R> = ($IRecipeLookup<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeLookup_<R> = $IRecipeLookup$Type<(R)>;
}}
declare module "packages/mezz/jei/common/util/$ChatUtil" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $ChatUtil {


public static "writeChatMessage"(arg0: $LocalPlayer$Type, arg1: string, arg2: $ChatFormatting$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatUtil$Type = ($ChatUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatUtil_ = $ChatUtil$Type;
}}
declare module "packages/mezz/jei/library/gui/$IngredientListOverlayDummy" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $IngredientListOverlayDummy implements $IIngredientListOverlay {
static readonly "INSTANCE": $IIngredientListOverlay


public "getVisibleIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
public "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
public "isListDisplayed"(): boolean
public "hasKeyboardFocus"(): boolean
get "ingredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
get "listDisplayed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientListOverlayDummy$Type = ($IngredientListOverlayDummy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientListOverlayDummy_ = $IngredientListOverlayDummy$Type;
}}
declare module "packages/mezz/jei/api/gui/drawable/$IDrawableBuilder" {
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"

export interface $IDrawableBuilder {

 "trim"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IDrawableBuilder
 "build"(): $IDrawableStatic
 "addPadding"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IDrawableBuilder
 "setTextureSize"(arg0: integer, arg1: integer): $IDrawableBuilder
 "buildAnimated"(arg0: integer, arg1: $IDrawableAnimated$StartDirection$Type, arg2: boolean): $IDrawableAnimated
 "buildAnimated"(arg0: $ITickTimer$Type, arg1: $IDrawableAnimated$StartDirection$Type): $IDrawableAnimated
}

export namespace $IDrawableBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDrawableBuilder$Type = ($IDrawableBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDrawableBuilder_ = $IDrawableBuilder$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$LimitedAreaInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $LimitedAreaInputHandler implements $IUserInputHandler {


public "toString"(): string
public static "create"(arg0: $IUserInputHandler$Type, arg1: $ImmutableRect2i$Type): $IUserInputHandler
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LimitedAreaInputHandler$Type = ($LimitedAreaInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LimitedAreaInputHandler_ = $LimitedAreaInputHandler$Type;
}}
declare module "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeSlotTooltipCallback, $IRecipeSlotTooltipCallback$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $IRecipeSlotDrawable extends $IRecipeSlotView {

 "drawHoverOverlays"(arg0: $GuiGraphics$Type): void
 "getRect"(): $Rect2i
 "draw"(arg0: $GuiGraphics$Type): void
 "getTooltip"(): $List<($Component)>
 "addTooltipCallback"(arg0: $IRecipeSlotTooltipCallback$Type): void
 "getDisplayedItemStack"(): $Optional<($ItemStack)>
 "isEmpty"(): boolean
 "getAllIngredients"(): $Stream<($ITypedIngredient<(any)>)>
 "getDisplayedIngredient"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
 "getDisplayedIngredient"(): $Optional<($ITypedIngredient<(any)>)>
 "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
 "drawHighlight"(arg0: $GuiGraphics$Type, arg1: integer): void
 "getItemStacks"(): $Stream<($ItemStack)>
 "getSlotName"(): $Optional<(string)>
 "getRole"(): $RecipeIngredientRole
}

export namespace $IRecipeSlotDrawable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeSlotDrawable$Type = ($IRecipeSlotDrawable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeSlotDrawable_ = $IRecipeSlotDrawable$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$FurnaceSmeltingCategory" {
import {$AbstractCookingCategory, $AbstractCookingCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$AbstractCookingCategory"
import {$SmeltingRecipe, $SmeltingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmeltingRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $FurnaceSmeltingCategory extends $AbstractCookingCategory<($SmeltingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($SmeltingRecipe)>
get "recipeType"(): $RecipeType<($SmeltingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FurnaceSmeltingCategory$Type = ($FurnaceSmeltingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FurnaceSmeltingCategory_ = $FurnaceSmeltingCategory$Type;
}}
declare module "packages/mezz/jei/library/helpers/$ModIdHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IModIdFormatConfig, $IModIdFormatConfig$Type} from "packages/mezz/jei/library/config/$IModIdFormatConfig"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $ModIdHelper implements $IModIdHelper {

constructor(arg0: $IModIdFormatConfig$Type, arg1: $IIngredientManager$Type)

public "getModNameForModId"(arg0: string): string
public "isDisplayingModNameEnabled"(): boolean
public "getFormattedModNameForModId"(arg0: string): string
public "addModNameToIngredientTooltip"<T>(arg0: $List$Type<($Component$Type)>, arg1: $ITypedIngredient$Type<(T)>): $List<($Component)>
public "addModNameToIngredientTooltip"<T>(arg0: $List$Type<($Component$Type)>, arg1: T, arg2: $IIngredientHelper$Type<(T)>): $List<($Component)>
get "displayingModNameEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIdHelper$Type = ($ModIdHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIdHelper_ = $ModIdHelper$Type;
}}
declare module "packages/mezz/jei/api/gui/drawable/$IDrawableStatic" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IDrawableStatic extends $IDrawable {

 "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
 "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
 "draw"(arg0: $GuiGraphics$Type): void
 "getWidth"(): integer
 "getHeight"(): integer
}

export namespace $IDrawableStatic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDrawableStatic$Type = ($IDrawableStatic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDrawableStatic_ = $IDrawableStatic$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$FocusedRecipes" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"

export class $FocusedRecipes<T> {


public "getRecipeCategory"(): $IRecipeCategory<(T)>
public static "create"<T>(arg0: $IFocusGroup$Type, arg1: $IRecipeManager$Type, arg2: $IRecipeCategory$Type<(T)>): $FocusedRecipes<(T)>
public "getRecipes"(): $List<(T)>
get "recipeCategory"(): $IRecipeCategory<(T)>
get "recipes"(): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusedRecipes$Type<T> = ($FocusedRecipes<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusedRecipes_<T> = $FocusedRecipes$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/$IJeiClientConfigs" {
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"

export interface $IJeiClientConfigs {

 "getIngredientListConfig"(): $IIngredientGridConfig
 "getBookmarkListConfig"(): $IIngredientGridConfig
 "getIngredientFilterConfig"(): $IIngredientFilterConfig
 "getClientConfig"(): $IClientConfig
}

export namespace $IJeiClientConfigs {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiClientConfigs$Type = ($IJeiClientConfigs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiClientConfigs_ = $IJeiClientConfigs$Type;
}}
declare module "packages/mezz/jei/forge/platform/$ConfigHelper" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IPlatformConfigHelper, $IPlatformConfigHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformConfigHelper"

export class $ConfigHelper implements $IPlatformConfigHelper {

constructor()

public "getConfigScreen"(): $Optional<($Screen)>
public "getModConfigDir"(): $Path
public "createJeiConfigDir"(): $Path
get "configScreen"(): $Optional<($Screen)>
get "modConfigDir"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHelper$Type = ($ConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHelper_ = $ConfigHelper$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientBlacklistInternal$IListener" {
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IngredientBlacklistInternal$IListener {

 "onIngredientVisibilityChanged"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void

(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void
}

export namespace $IngredientBlacklistInternal$IListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientBlacklistInternal$IListener$Type = ($IngredientBlacklistInternal$IListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientBlacklistInternal$IListener_ = $IngredientBlacklistInternal$IListener$Type;
}}
declare module "packages/mezz/jei/forge/events/$PermanentEventSubscriptions" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $PermanentEventSubscriptions {

constructor(arg0: $IEventBus$Type, arg1: $IEventBus$Type)

public "register"<T extends $Event>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PermanentEventSubscriptions$Type = ($PermanentEventSubscriptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PermanentEventSubscriptions_ = $PermanentEventSubscriptions$Type;
}}
declare module "packages/mezz/jei/common/util/$RectDebugger" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $RectDebugger {
static readonly "INSTANCE": $RectDebugger


public "add"(arg0: $ImmutableRect2i$Type, arg1: integer, arg2: string): void
public "draw"(arg0: $GuiGraphics$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RectDebugger$Type = ($RectDebugger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RectDebugger_ = $RectDebugger$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigListValueSerializer" {
import {$IJeiConfigValueSerializer$IDeserializeResult, $IJeiConfigValueSerializer$IDeserializeResult$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer$IDeserializeResult"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigValueSerializer, $IJeiConfigValueSerializer$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValueSerializer"

export interface $IJeiConfigListValueSerializer<T> extends $IJeiConfigValueSerializer<($List<(T)>)> {

 "getListValueSerializer"(): $IJeiConfigValueSerializer<(T)>
 "isValid"(arg0: $List$Type<(T)>): boolean
 "deserialize"(arg0: string): $IJeiConfigValueSerializer$IDeserializeResult<($List<(T)>)>
 "getValidValuesDescription"(): string
 "getAllValidValues"(): $Optional<($Collection<($List<(T)>)>)>
 "serialize"(arg0: $List$Type<(T)>): string
}

export namespace $IJeiConfigListValueSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigListValueSerializer$Type<T> = ($IJeiConfigListValueSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigListValueSerializer_<T> = $IJeiConfigListValueSerializer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/ingredients/fluid/$FluidIngredientHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"
import {$IPlatformFluidHelperInternal, $IPlatformFluidHelperInternal$Type} from "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ISubtypeManager, $ISubtypeManager$Type} from "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager"

export class $FluidIngredientHelper<T> implements $IIngredientHelper<(T)> {

constructor(arg0: $ISubtypeManager$Type, arg1: $IColorHelper$Type, arg2: $IPlatformFluidHelperInternal$Type<(T)>)

public "getDisplayName"(arg0: T): string
public "getWildcardId"(arg0: T): string
public "getTagEquivalent"(arg0: $Collection$Type<(T)>): $Optional<($ResourceLocation)>
public "copyIngredient"(arg0: T): T
public "getTagStream"(arg0: T): $Stream<($ResourceLocation)>
public "getErrorInfo"(arg0: T): string
public "getIngredientType"(): $IIngredientType<(T)>
public "getCheatItemStack"(arg0: T): $ItemStack
public "getUniqueId"(arg0: T, arg1: $UidContext$Type): string
public "normalizeIngredient"(arg0: T): T
public "getResourceLocation"(arg0: T): $ResourceLocation
public "getColors"(arg0: T): $Iterable<(integer)>
public "getDisplayModId"(arg0: T): string
public "isValidIngredient"(arg0: T): boolean
public "isIngredientOnServer"(arg0: T): boolean
get "ingredientType"(): $IIngredientType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidIngredientHelper$Type<T> = ($FluidIngredientHelper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidIngredientHelper_<T> = $FluidIngredientHelper$Type<(T)>;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IJeiAnvilRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IJeiAnvilRecipe {

 "getOutputs"(): $List<($ItemStack)>
 "getLeftInputs"(): $List<($ItemStack)>
 "getRightInputs"(): $List<($ItemStack)>
}

export namespace $IJeiAnvilRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiAnvilRecipe$Type = ($IJeiAnvilRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiAnvilRecipe_ = $IJeiAnvilRecipe$Type;
}}
declare module "packages/mezz/jei/gui/input/$IUserInputHandler" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $IUserInputHandler {

 "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
 "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
 "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>

(arg0: $InputConstants$Key$Type): void
}

export namespace $IUserInputHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IUserInputHandler$Type = ($IUserInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IUserInputHandler_ = $IUserInputHandler$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketDeletePlayerItem" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PacketDeletePlayerItem extends $PacketJei {

constructor(arg0: $ItemStack$Type)

public static "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketDeletePlayerItem$Type = ($PacketDeletePlayerItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketDeletePlayerItem_ = $PacketDeletePlayerItem$Type;
}}
declare module "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export interface $ISubtypeManager {

 "getSubtypeInfo"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
 "getSubtypeInfo"<T>(arg0: $IIngredientTypeWithSubtypes$Type<(any), (T)>, arg1: T, arg2: $UidContext$Type): string

(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
}

export namespace $ISubtypeManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISubtypeManager$Type = ($ISubtypeManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISubtypeManager_ = $ISubtypeManager$Type;
}}
declare module "packages/mezz/jei/library/gui/recipes/$ShapelessIcon" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ShapelessIcon {

constructor(arg0: $Textures$Type)

public "draw"(arg0: $GuiGraphics$Type): void
public "getIcon"(): $IDrawable
public "setPosition"(arg0: integer, arg1: integer): void
public "getTooltipStrings"(arg0: integer, arg1: integer): $List<($Component)>
get "icon"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapelessIcon$Type = ($ShapelessIcon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapelessIcon_ = $ShapelessIcon$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$JeiKeyConflictContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $JeiKeyConflictContext extends $Enum<($JeiKeyConflictContext)> {
static readonly "UNIVERSAL": $JeiKeyConflictContext
static readonly "GUI": $JeiKeyConflictContext
static readonly "IN_GAME": $JeiKeyConflictContext
static readonly "JEI_GUI_HOVER": $JeiKeyConflictContext
static readonly "JEI_GUI_HOVER_CHEAT_MODE": $JeiKeyConflictContext
static readonly "JEI_GUI_HOVER_CONFIG_BUTTON": $JeiKeyConflictContext
static readonly "JEI_GUI_HOVER_SEARCH": $JeiKeyConflictContext


public static "values"(): ($JeiKeyConflictContext)[]
public static "valueOf"(arg0: string): $JeiKeyConflictContext
public "isActive"(): boolean
public "conflicts"(arg0: $JeiKeyConflictContext$Type): boolean
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiKeyConflictContext$Type = (("in_game") | ("jei_gui_hover_cheat_mode") | ("jei_gui_hover") | ("jei_gui_hover_search") | ("gui") | ("jei_gui_hover_config_button") | ("universal")) | ($JeiKeyConflictContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiKeyConflictContext_ = $JeiKeyConflictContext$Type;
}}
declare module "packages/mezz/jei/gui/overlay/bookmarks/$BookmarkOverlay" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IBookmarkOverlay, $IBookmarkOverlay$Type} from "packages/mezz/jei/api/runtime/$IBookmarkOverlay"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IngredientGridWithNavigation, $IngredientGridWithNavigation$Type} from "packages/mezz/jei/gui/overlay/$IngredientGridWithNavigation"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $BookmarkOverlay implements $IRecipeFocusSource, $IBookmarkOverlay {

constructor(arg0: $BookmarkList$Type, arg1: $Textures$Type, arg2: $IngredientGridWithNavigation$Type, arg3: $IClientConfig$Type, arg4: $IClientToggleState$Type, arg5: $IScreenHelper$Type, arg6: $IConnectionToServer$Type, arg7: $IInternalKeyMappings$Type, arg8: $CheatUtil$Type)

public "updateScreen"(arg0: $Screen$Type, arg1: $Set$Type<($ImmutableRect2i$Type)>): void
public "drawScreen"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
public "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "drawTooltips"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "drawOnForeground"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "hasRoom"(): boolean
public "isListDisplayed"(): boolean
public "createDragHandler"(): $IDragHandler
public "createInputHandler"(): $IUserInputHandler
public "getItemStackUnderMouse"(): $ItemStack
get "ingredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
get "listDisplayed"(): boolean
get "itemStackUnderMouse"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkOverlay$Type = ($BookmarkOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkOverlay_ = $BookmarkOverlay$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$ListElementInfo" {
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IListElement, $IListElement$Type} from "packages/mezz/jei/gui/ingredients/$IListElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $ListElementInfo<V> implements $IListElementInfo<(V)> {


public "getName"(): string
public static "create"<V>(arg0: $IListElement$Type<(V)>, arg1: $IIngredientManager$Type, arg2: $IModIdHelper$Type): $Optional<($IListElementInfo<(V)>)>
public "getElement"(): $IListElement<(V)>
public "getTypedIngredient"(): $ITypedIngredient<(V)>
public "getResourceLocation"(): $ResourceLocation
public "getSortedIndex"(): integer
public "getTagStrings"(arg0: $IIngredientManager$Type): $Collection<(string)>
public "getModNameStrings"(): $Set<(string)>
public "getTooltipStrings"(arg0: $IIngredientFilterConfig$Type, arg1: $IIngredientManager$Type): $List<(string)>
public "getColors"(arg0: $IIngredientManager$Type): $Iterable<(integer)>
public "getTagIds"(arg0: $IIngredientManager$Type): $Stream<($ResourceLocation)>
public "setSortedIndex"(arg0: integer): void
public "getModNameForSorting"(): string
get "name"(): string
get "element"(): $IListElement<(V)>
get "typedIngredient"(): $ITypedIngredient<(V)>
get "resourceLocation"(): $ResourceLocation
get "sortedIndex"(): integer
get "modNameStrings"(): $Set<(string)>
set "sortedIndex"(value: integer)
get "modNameForSorting"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListElementInfo$Type<V> = ($ListElementInfo<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListElementInfo_<V> = $ListElementInfo$Type<(V)>;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableSprite" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$JeiSpriteUploader, $JeiSpriteUploader$Type} from "packages/mezz/jei/common/gui/textures/$JeiSpriteUploader"

export class $DrawableSprite implements $IDrawableStatic {

constructor(arg0: $JeiSpriteUploader$Type, arg1: $ResourceLocation$Type, arg2: integer, arg3: integer)

public "trim"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $DrawableSprite
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "draw"(arg0: $GuiGraphics$Type): void
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableSprite$Type = ($DrawableSprite);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableSprite_ = $DrawableSprite$Type;
}}
declare module "packages/mezz/jei/api/gui/drawable/$IDrawable" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IDrawable {

 "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
 "draw"(arg0: $GuiGraphics$Type): void
 "getWidth"(): integer
 "getHeight"(): integer
}

export namespace $IDrawable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDrawable$Type = ($IDrawable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDrawable_ = $IDrawable$Type;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformItemStackHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IPlatformItemStackHelper {

 "getTestTooltip"(arg0: $Player$Type, arg1: $ItemStack$Type): $List<($Component)>
 "isBookEnchantable"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
 "getBurnTime"(arg0: $ItemStack$Type): integer
 "getCreatorModId"(arg0: $ItemStack$Type): $Optional<(string)>
}

export namespace $IPlatformItemStackHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformItemStackHelper$Type = ($IPlatformItemStackHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformItemStackHelper_ = $IPlatformItemStackHelper$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$IServerPacketHandler" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ServerPacketData, $ServerPacketData$Type} from "packages/mezz/jei/common/network/$ServerPacketData"

export interface $IServerPacketHandler {

 "readPacketData"(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>

(arg0: $ServerPacketData$Type): $CompletableFuture<(void)>
}

export namespace $IServerPacketHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IServerPacketHandler$Type = ($IServerPacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IServerPacketHandler_ = $IServerPacketHandler$Type;
}}
declare module "packages/mezz/jei/common/util/$TagUtil" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$HolderSet$Named, $HolderSet$Named$Type} from "packages/net/minecraft/core/$HolderSet$Named"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $TagUtil {

constructor()

public static "getTagEquivalent"<VALUE, STACK>(arg0: $Collection$Type<(STACK)>, arg1: $Function$Type<(STACK), (VALUE)>, arg2: $Supplier$Type<($Stream$Type<($Pair$Type<($TagKey$Type<(VALUE)>), ($HolderSet$Named$Type<(VALUE)>)>)>)>): $Optional<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagUtil$Type = ($TagUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagUtil_ = $TagUtil$Type;
}}
declare module "packages/mezz/jei/library/plugins/jei/$JeiInternalPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JeiInternalPlugin implements $IModPlugin {

constructor()

public "getPluginUid"(): $ResourceLocation
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiInternalPlugin$Type = ($JeiInternalPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiInternalPlugin_ = $JeiInternalPlugin$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError" {
import {$IRecipeTransferError$Type, $IRecipeTransferError$Type$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError$Type"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $IRecipeTransferError {

 "getType"(): $IRecipeTransferError$Type
 "getButtonHighlightColor"(): integer
 "showError"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $IRecipeSlotsView$Type, arg4: integer, arg5: integer): void

(): $IRecipeTransferError$Type
}

export namespace $IRecipeTransferError {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferError$Type = ($IRecipeTransferError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferError_ = $IRecipeTransferError$Type;
}}
declare module "packages/mezz/jei/library/recipes/collect/$RecipeIngredientTable" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"

export class $RecipeIngredientTable {

constructor()

public "add"<V>(arg0: V, arg1: $RecipeType$Type<(V)>, arg2: $List$Type<(string)>): void
public "get"<V>(arg0: $RecipeType$Type<(V)>, arg1: string): $List<(V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeIngredientTable$Type = ($RecipeIngredientTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeIngredientTable_ = $RecipeIngredientTable$Type;
}}
declare module "packages/mezz/jei/api/registration/$IRecipeRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export interface $IRecipeRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "getVanillaRecipeFactory"(): $IVanillaRecipeFactory
 "getIngredientVisibility"(): $IIngredientVisibility
 "addRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $List$Type<(T)>): void
 "getIngredientManager"(): $IIngredientManager
 "addIngredientInfo"<T>(arg0: $List$Type<(T)>, arg1: $IIngredientType$Type<(T)>, ...arg2: ($Component$Type)[]): void
 "addIngredientInfo"<T>(arg0: T, arg1: $IIngredientType$Type<(T)>, ...arg2: ($Component$Type)[]): void
 "addItemStackInfo"(arg0: $ItemStack$Type, ...arg1: ($Component$Type)[]): void
 "addItemStackInfo"(arg0: $List$Type<($ItemStack$Type)>, ...arg1: ($Component$Type)[]): void
}

export namespace $IRecipeRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeRegistration$Type = ($IRecipeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeRegistration_ = $IRecipeRegistration$Type;
}}
declare module "packages/mezz/jei/gui/config/$IngredientTypeSortingConfig" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$MappedSortingConfig, $MappedSortingConfig$Type} from "packages/mezz/jei/common/config/sorting/$MappedSortingConfig"

export class $IngredientTypeSortingConfig extends $MappedSortingConfig<($IListElementInfo<(any)>), (string)> {

constructor(arg0: $Path$Type)

public static "getIngredientTypeString"(arg0: $IListElementInfo$Type<(any)>): string
public static "getIngredientTypeString"(arg0: $IIngredientType$Type<(any)>): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientTypeSortingConfig$Type = ($IngredientTypeSortingConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientTypeSortingConfig_ = $IngredientTypeSortingConfig$Type;
}}
declare module "packages/mezz/jei/api/recipe/$IRecipeManager" {
import {$IRecipeCatalystLookup, $IRecipeCatalystLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCatalystLookup"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeLookup, $IRecipeLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeLookup"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IRecipeLayoutDrawable, $IRecipeLayoutDrawable$Type} from "packages/mezz/jei/api/gui/$IRecipeLayoutDrawable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeSlotDrawable, $IRecipeSlotDrawable$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotDrawable"
import {$IRecipeCategoriesLookup, $IRecipeCategoriesLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCategoriesLookup"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IRecipeManager {

 "hideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
 "createRecipeLookup"<R>(arg0: $RecipeType$Type<(R)>): $IRecipeLookup<(R)>
 "hideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
 "getRecipeType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
 "createRecipeCatalystLookup"(arg0: $RecipeType$Type<(any)>): $IRecipeCatalystLookup
 "unhideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
 "createRecipeLayoutDrawable"<T>(arg0: $IRecipeCategory$Type<(T)>, arg1: T, arg2: $IFocusGroup$Type): $Optional<($IRecipeLayoutDrawable<(T)>)>
 "createRecipeSlotDrawable"(arg0: $RecipeIngredientRole$Type, arg1: $List$Type<($Optional$Type<($ITypedIngredient$Type<(any)>)>)>, arg2: $Set$Type<(integer)>, arg3: integer, arg4: integer, arg5: integer): $IRecipeSlotDrawable
 "addRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $List$Type<(T)>): void
 "createRecipeCategoryLookup"(): $IRecipeCategoriesLookup
 "unhideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
}

export namespace $IRecipeManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeManager$Type = ($IRecipeManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeManager_ = $IRecipeManager$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/ingredients/$ItemStackHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$StackHelper, $StackHelper$Type} from "packages/mezz/jei/common/util/$StackHelper"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $ItemStackHelper implements $IIngredientHelper<($ItemStack)> {

constructor(arg0: $StackHelper$Type, arg1: $IColorHelper$Type)

public "getDisplayName"(arg0: $ItemStack$Type): string
public "getWildcardId"(arg0: $ItemStack$Type): string
public "getDisplayModId"(arg0: $ItemStack$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($ItemStack$Type)>): $Optional<($ResourceLocation)>
public "copyIngredient"(arg0: $ItemStack$Type): $ItemStack
public "getTagStream"(arg0: $ItemStack$Type): $Stream<($ResourceLocation)>
public "getErrorInfo"(arg0: $ItemStack$Type): string
public "getIngredientType"(): $IIngredientType<($ItemStack)>
public "getCheatItemStack"(arg0: $ItemStack$Type): $ItemStack
public "getUniqueId"(arg0: $ItemStack$Type, arg1: $UidContext$Type): string
public "isValidIngredient"(arg0: $ItemStack$Type): boolean
public "normalizeIngredient"(arg0: $ItemStack$Type): $ItemStack
public "isIngredientOnServer"(arg0: $ItemStack$Type): boolean
public "getResourceLocation"(arg0: $ItemStack$Type): $ResourceLocation
public "getColors"(arg0: $ItemStack$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackHelper$Type = ($ItemStackHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackHelper_ = $ItemStackHelper$Type;
}}
declare module "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"

export interface $IRecipeCategoryRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "addRecipeCategories"(...arg0: ($IRecipeCategory$Type<(any)>)[]): void
}

export namespace $IRecipeCategoryRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCategoryRegistration$Type = ($IRecipeCategoryRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCategoryRegistration_ = $IRecipeCategoryRegistration$Type;
}}
declare module "packages/mezz/jei/gui/overlay/bookmarks/$BookmarkButton" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$GuiIconToggleButton, $GuiIconToggleButton$Type} from "packages/mezz/jei/gui/elements/$GuiIconToggleButton"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$BookmarkOverlay, $BookmarkOverlay$Type} from "packages/mezz/jei/gui/overlay/bookmarks/$BookmarkOverlay"

export class $BookmarkButton extends $GuiIconToggleButton {


public static "create"(arg0: $BookmarkOverlay$Type, arg1: $BookmarkList$Type, arg2: $Textures$Type, arg3: $IClientToggleState$Type, arg4: $IInternalKeyMappings$Type): $BookmarkButton
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkButton$Type = ($BookmarkButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkButton_ = $BookmarkButton$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/$CampfireCookingCategory" {
import {$CampfireCookingRecipe, $CampfireCookingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CampfireCookingRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$AbstractCookingCategory, $AbstractCookingCategory$Type} from "packages/mezz/jei/library/plugins/vanilla/cooking/$AbstractCookingCategory"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $CampfireCookingCategory extends $AbstractCookingCategory<($CampfireCookingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($CampfireCookingRecipe)>
public "draw"(arg0: $CampfireCookingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $CampfireCookingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
get "recipeType"(): $RecipeType<($CampfireCookingRecipe)>
get "background"(): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CampfireCookingCategory$Type = ($CampfireCookingCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CampfireCookingCategory_ = $CampfireCookingCategory$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$IJeiKeyMappingCategoryBuilder" {
import {$IJeiKeyMappingBuilder, $IJeiKeyMappingBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingBuilder"

export interface $IJeiKeyMappingCategoryBuilder {

 "createMapping"(arg0: string): $IJeiKeyMappingBuilder

(arg0: string): $IJeiKeyMappingBuilder
}

export namespace $IJeiKeyMappingCategoryBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiKeyMappingCategoryBuilder$Type = ($IJeiKeyMappingCategoryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiKeyMappingCategoryBuilder_ = $IJeiKeyMappingCategoryBuilder$Type;
}}
declare module "packages/mezz/jei/gui/filter/$IFilterTextSource$Listener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IFilterTextSource$Listener {

 "onChange"(arg0: string): void

(arg0: string): void
}

export namespace $IFilterTextSource$Listener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFilterTextSource$Listener$Type = ($IFilterTextSource$Listener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFilterTextSource$Listener_ = $IFilterTextSource$Listener$Type;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IJeiBrewingRecipe {

 "getIngredients"(): $List<($ItemStack)>
 "getBrewingSteps"(): integer
 "getPotionInputs"(): $List<($ItemStack)>
 "getPotionOutput"(): $ItemStack
}

export namespace $IJeiBrewingRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiBrewingRecipe$Type = ($IJeiBrewingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiBrewingRecipe_ = $IJeiBrewingRecipe$Type;
}}
declare module "packages/mezz/jei/gui/util/$CheatUtil" {
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $CheatUtil {

constructor(arg0: $IIngredientManager$Type)

public "getCheatItemStack"<T>(arg0: $IClickableIngredientInternal$Type<(T)>): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheatUtil$Type = ($CheatUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheatUtil_ = $CheatUtil$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeCatalystLookup" {
import {$IRecipeCatalystLookup, $IRecipeCatalystLookup$Type} from "packages/mezz/jei/api/recipe/$IRecipeCatalystLookup"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeManagerInternal, $RecipeManagerInternal$Type} from "packages/mezz/jei/library/recipes/$RecipeManagerInternal"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $RecipeCatalystLookup implements $IRecipeCatalystLookup {

constructor(arg0: $RecipeType$Type<(any)>, arg1: $RecipeManagerInternal$Type)

public "includeHidden"(): $IRecipeCatalystLookup
public "get"<V>(arg0: $IIngredientType$Type<(V)>): $Stream<(V)>
public "get"(): $Stream<($ITypedIngredient<(any)>)>
public "getItemStack"(): $Stream<($ItemStack)>
get "itemStack"(): $Stream<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCatalystLookup$Type = ($RecipeCatalystLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCatalystLookup_ = $RecipeCatalystLookup$Type;
}}
declare module "packages/mezz/jei/library/transfer/$BasicRecipeTransferHandler$InventoryState" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BasicRecipeTransferHandler$InventoryState extends $Record {

constructor(availableItemStacks: $Map$Type<($Slot$Type), ($ItemStack$Type)>, filledCraftSlotCount: integer, emptySlotCount: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "filledCraftSlotCount"(): integer
public "availableItemStacks"(): $Map<($Slot), ($ItemStack)>
public "hasRoom"(arg0: integer): boolean
public "emptySlotCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicRecipeTransferHandler$InventoryState$Type = ($BasicRecipeTransferHandler$InventoryState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicRecipeTransferHandler$InventoryState_ = $BasicRecipeTransferHandler$InventoryState$Type;
}}
declare module "packages/mezz/jei/common/input/keys/$JeiKeyModifier" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$JeiKeyConflictContext, $JeiKeyConflictContext$Type} from "packages/mezz/jei/common/input/keys/$JeiKeyConflictContext"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $JeiKeyModifier extends $Enum<($JeiKeyModifier)> {
static readonly "CONTROL_OR_COMMAND": $JeiKeyModifier
static readonly "SHIFT": $JeiKeyModifier
static readonly "ALT": $JeiKeyModifier
static readonly "NONE": $JeiKeyModifier


public static "values"(): ($JeiKeyModifier)[]
public static "valueOf"(arg0: string): $JeiKeyModifier
public "isActive"(arg0: $JeiKeyConflictContext$Type): boolean
public "getCombinedName"(arg0: $InputConstants$Key$Type): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiKeyModifier$Type = (("control_or_command") | ("shift") | ("alt") | ("none")) | ($JeiKeyModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiKeyModifier_ = $JeiKeyModifier$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"

export interface $IRecipeTransferInfo<C extends $AbstractContainerMenu, R> {

 "getRecipeSlots"(arg0: C, arg1: R): $List<($Slot)>
 "getRecipeType"(): $RecipeType<(R)>
 "getMenuType"(): $Optional<($MenuType<(C)>)>
 "getContainerClass"(): $Class<(any)>
 "requireCompleteSets"(arg0: C, arg1: R): boolean
 "getHandlingError"(arg0: C, arg1: R): $IRecipeTransferError
 "canHandle"(arg0: C, arg1: R): boolean
 "getInventorySlots"(arg0: C, arg1: R): $List<($Slot)>
}

export namespace $IRecipeTransferInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferInfo$Type<C, R> = ($IRecipeTransferInfo<(C), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferInfo_<C, R> = $IRecipeTransferInfo$Type<(C), (R)>;
}}
declare module "packages/mezz/jei/common/config/$IIngredientGridConfig" {
import {$HorizontalAlignment, $HorizontalAlignment$Type} from "packages/mezz/jei/common/util/$HorizontalAlignment"
import {$NavigationVisibility, $NavigationVisibility$Type} from "packages/mezz/jei/common/util/$NavigationVisibility"
import {$VerticalAlignment, $VerticalAlignment$Type} from "packages/mezz/jei/common/util/$VerticalAlignment"

export interface $IIngredientGridConfig {

 "drawBackground"(): boolean
 "getMaxRows"(): integer
 "getMinRows"(): integer
 "getMaxColumns"(): integer
 "getMinColumns"(): integer
 "getHorizontalAlignment"(): $HorizontalAlignment
 "getVerticalAlignment"(): $VerticalAlignment
 "getButtonNavigationVisibility"(): $NavigationVisibility
}

export namespace $IIngredientGridConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientGridConfig$Type = ($IIngredientGridConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientGridConfig_ = $IIngredientGridConfig$Type;
}}
declare module "packages/mezz/jei/api/registration/$IGuiHandlerRegistration" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IScreenHandler, $IScreenHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IScreenHandler"
import {$IGlobalGuiHandler, $IGlobalGuiHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGlobalGuiHandler"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$IGuiContainerHandler, $IGuiContainerHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiContainerHandler"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $IGuiHandlerRegistration {

 "getJeiHelpers"(): $IJeiHelpers
 "addGenericGuiContainerHandler"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: $IGuiContainerHandler$Type<(any)>): void
 "addGuiScreenHandler"<T extends $Screen>(arg0: $Class$Type<(T)>, arg1: $IScreenHandler$Type<(T)>): void
 "addGlobalGuiHandler"(arg0: $IGlobalGuiHandler$Type): void
 "addGuiContainerHandler"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: $IGuiContainerHandler$Type<(T)>): void
 "addRecipeClickArea"<T extends $AbstractContainerScreen<(any)>>(arg0: $Class$Type<(any)>, arg1: integer, arg2: integer, arg3: integer, arg4: integer, ...arg5: ($RecipeType$Type<(any)>)[]): void
 "addGhostIngredientHandler"<T extends $Screen>(arg0: $Class$Type<(T)>, arg1: $IGhostIngredientHandler$Type<(T)>): void
}

export namespace $IGuiHandlerRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiHandlerRegistration$Type = ($IGuiHandlerRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiHandlerRegistration_ = $IGuiHandlerRegistration$Type;
}}
declare module "packages/mezz/jei/common/platform/$Services" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Services {
static readonly "PLATFORM": $IPlatformHelper

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Services$Type = ($Services);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Services_ = $Services$Type;
}}
declare module "packages/mezz/jei/gui/elements/$GuiIconButtonSmall" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiIconButtonSmall extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IDrawable$Type, arg5: $Button$OnPress$Type, arg6: $Textures$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getArea"(): $ImmutableRect2i
get "area"(): $ImmutableRect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiIconButtonSmall$Type = ($GuiIconButtonSmall);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiIconButtonSmall_ = $GuiIconButtonSmall$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/replacers/$ShieldDecorationRecipeMaker" {
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ShieldDecorationRecipeMaker {


public static "createRecipes"(): $List<($CraftingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShieldDecorationRecipeMaker$Type = ($ShieldDecorationRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShieldDecorationRecipeMaker_ = $ShieldDecorationRecipeMaker$Type;
}}
declare module "packages/mezz/jei/common/config/sorting/serializers/$ISortingSerializer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ISortingSerializer<T> {

 "write"(arg0: $Path$Type, arg1: $List$Type<(T)>): void
 "read"(arg0: $Path$Type): $List<(T)>
}

export namespace $ISortingSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISortingSerializer$Type<T> = ($ISortingSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISortingSerializer_<T> = $ISortingSerializer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/$CategoryRecipeValidator" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $CategoryRecipeValidator<T extends $Recipe<(any)>> {

constructor(arg0: $IRecipeCategory$Type<(T)>, arg1: $IIngredientManager$Type, arg2: integer)

public "isRecipeValid"(arg0: T): boolean
public "isRecipeHandled"(arg0: T): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CategoryRecipeValidator$Type<T> = ($CategoryRecipeValidator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CategoryRecipeValidator_<T> = $CategoryRecipeValidator$Type<(T)>;
}}
declare module "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export interface $IPlatformFluidHelperInternal<T> extends $IPlatformFluidHelper<(T)> {

 "copy"(arg0: T): T
 "normalize"(arg0: T): T
 "getDisplayName"(arg0: T): $Component
 "getTag"(arg0: T): $Optional<($CompoundTag)>
 "getContainedFluid"(arg0: $ITypedIngredient$Type<(any)>): $Optional<(T)>
 "createRenderer"(arg0: long, arg1: boolean, arg2: integer, arg3: integer): $IIngredientRenderer<(T)>
 "getColorTint"(arg0: T): integer
 "getAmount"(arg0: T): long
 "getTooltip"(arg0: T, arg1: $TooltipFlag$Type): $List<($Component)>
 "getAllNbtSubtypeInterpreter"(): $IIngredientSubtypeInterpreter<(T)>
 "getStillFluidSprite"(arg0: T): $Optional<($TextureAtlasSprite)>
 "create"(arg0: $Fluid$Type, arg1: long): T
 "create"(arg0: $Fluid$Type, arg1: long, arg2: $CompoundTag$Type): T
 "bucketVolume"(): long
 "getFluidIngredientType"(): $IIngredientTypeWithSubtypes<($Fluid), (T)>
}

export namespace $IPlatformFluidHelperInternal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformFluidHelperInternal$Type<T> = ($IPlatformFluidHelperInternal<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformFluidHelperInternal_<T> = $IPlatformFluidHelperInternal$Type<(T)>;
}}
declare module "packages/mezz/jei/library/transfer/$PlayerRecipeTransferHandler" {
import {$InventoryMenu, $InventoryMenu$Type} from "packages/net/minecraft/world/inventory/$InventoryMenu"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$IRecipeTransferHandlerHelper, $IRecipeTransferHandlerHelper$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $PlayerRecipeTransferHandler implements $IRecipeTransferHandler<($InventoryMenu), ($CraftingRecipe)> {

constructor(arg0: $IRecipeTransferHandlerHelper$Type)

public "getRecipeType"(): $RecipeType<($CraftingRecipe)>
public "getMenuType"(): $Optional<($MenuType<($InventoryMenu)>)>
public "getContainerClass"(): $Class<(any)>
public "transferRecipe"(arg0: $InventoryMenu$Type, arg1: $CraftingRecipe$Type, arg2: $IRecipeSlotsView$Type, arg3: $Player$Type, arg4: boolean, arg5: boolean): $IRecipeTransferError
get "recipeType"(): $RecipeType<($CraftingRecipe)>
get "menuType"(): $Optional<($MenuType<($InventoryMenu)>)>
get "containerClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRecipeTransferHandler$Type = ($PlayerRecipeTransferHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRecipeTransferHandler_ = $PlayerRecipeTransferHandler$Type;
}}
declare module "packages/mezz/jei/forge/platform/$RegistryWrapper" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IPlatformRegistry, $IPlatformRegistry$Type} from "packages/mezz/jei/common/platform/$IPlatformRegistry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RegistryWrapper<T> implements $IPlatformRegistry<(T)> {


public "getValue"(arg0: integer): $Optional<(T)>
public "getValue"(arg0: $ResourceLocation$Type): $Optional<(T)>
public "contains"(arg0: T): boolean
public "getId"(arg0: T): integer
public static "getRegistry"<T, V>(arg0: $ResourceKey$Type<(any)>): $IPlatformRegistry<(T)>
public "getRegistryName"(arg0: T): $Optional<($ResourceLocation)>
public "getValues"(): $Stream<(T)>
get "values"(): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryWrapper$Type<T> = ($RegistryWrapper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryWrapper_<T> = $RegistryWrapper$Type<(T)>;
}}
declare module "packages/mezz/jei/library/load/$PluginCallerTimerRunnable" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PluginCallerTimerRunnable {

constructor(arg0: string, arg1: $ResourceLocation$Type)

public "stop"(): void
public "check"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginCallerTimerRunnable$Type = ($PluginCallerTimerRunnable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginCallerTimerRunnable_ = $PluginCallerTimerRunnable$Type;
}}
declare module "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeCategoryExtension, $IRecipeCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryExtension"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $ICraftingCategoryExtension extends $IRecipeCategoryExtension {

 "getWidth"(): integer
 "getHeight"(): integer
 "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ICraftingGridHelper$Type, arg2: $IFocusGroup$Type): void
 "getRegistryName"(): $ResourceLocation
 "drawInfo"(arg0: integer, arg1: integer, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
 "handleInput"(arg0: double, arg1: double, arg2: $InputConstants$Key$Type): boolean
 "getTooltipStrings"(arg0: double, arg1: double): $List<($Component)>

(): integer
}

export namespace $ICraftingCategoryExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICraftingCategoryExtension$Type = ($ICraftingCategoryExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICraftingCategoryExtension_ = $ICraftingCategoryExtension$Type;
}}
declare module "packages/mezz/jei/api/registration/$IModIngredientRegistration" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ISubtypeManager, $ISubtypeManager$Type} from "packages/mezz/jei/api/ingredients/subtypes/$ISubtypeManager"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"

export interface $IModIngredientRegistration {

 "register"<V>(arg0: $IIngredientType$Type<(V)>, arg1: $Collection$Type<(V)>, arg2: $IIngredientHelper$Type<(V)>, arg3: $IIngredientRenderer$Type<(V)>): void
 "getColorHelper"(): $IColorHelper
 "getSubtypeManager"(): $ISubtypeManager
}

export namespace $IModIngredientRegistration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModIngredientRegistration$Type = ($IModIngredientRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModIngredientRegistration_ = $IModIngredientRegistration$Type;
}}
declare module "packages/mezz/jei/core/util/$TextHistory" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$TextHistory$Direction, $TextHistory$Direction$Type} from "packages/mezz/jei/core/util/$TextHistory$Direction"

export class $TextHistory {

constructor()

public "add"(arg0: string): boolean
public "get"(arg0: $TextHistory$Direction$Type, arg1: string): $Optional<(string)>
public "getNext"(arg0: string): $Optional<(string)>
public "getPrevious"(arg0: string): $Optional<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextHistory$Type = ($TextHistory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextHistory_ = $TextHistory$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipesGui" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeManager, $IRecipeManager$Type} from "packages/mezz/jei/api/recipe/$IRecipeManager"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IFocusFactory, $IFocusFactory$Type} from "packages/mezz/jei/api/recipe/$IFocusFactory"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IRecipeLogicStateListener, $IRecipeLogicStateListener$Type} from "packages/mezz/jei/gui/recipes/$IRecipeLogicStateListener"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IRecipesGui, $IRecipesGui$Type} from "packages/mezz/jei/api/runtime/$IRecipesGui"
import {$IGuiProperties, $IGuiProperties$Type} from "packages/mezz/jei/api/gui/handlers/$IGuiProperties"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"

export class $RecipesGui extends $Screen implements $IRecipesGui, $IRecipeFocusSource, $IRecipeLogicStateListener {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $IRecipeManager$Type, arg1: $IRecipeTransferManager$Type, arg2: $IIngredientManager$Type, arg3: $IModIdHelper$Type, arg4: $IClientConfig$Type, arg5: $Textures$Type, arg6: $IInternalKeyMappings$Type, arg7: $IFocusFactory$Type)

public "isOpen"(): boolean
public "getProperties"(): $IGuiProperties
public "back"(): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "onClose"(): void
public "m_7856_"(): void
public "isMouseOver"(arg0: double, arg1: double): boolean
public "onStateChange"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getArea"(): $ImmutableRect2i
public "isPauseScreen"(): boolean
public "tick"(): void
public "getRecipeCatalystExtraWidth"(): integer
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<(T)>
public "show"(arg0: $List$Type<($IFocus$Type<(any)>)>): void
public "showTypes"(arg0: $List$Type<($RecipeType$Type<(any)>)>): void
public "show"<V>(arg0: $IFocus$Type<(V)>): void
get "open"(): boolean
get "properties"(): $IGuiProperties
get "area"(): $ImmutableRect2i
get "pauseScreen"(): boolean
get "recipeCatalystExtraWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipesGui$Type = ($RecipesGui);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipesGui_ = $RecipesGui$Type;
}}
declare module "packages/mezz/jei/api/recipe/category/$IRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $IRecipeCategory<T> {

 "getRecipeType"(): $RecipeType<(T)>
 "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
 "getWidth"(): integer
 "getHeight"(): integer
 "isHandled"(arg0: T): boolean
 "getIcon"(): $IDrawable
 "getTitle"(): $Component
 "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
 "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
 "getBackground"(): $IDrawable
 "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
 "getRegistryName"(arg0: T): $ResourceLocation
}

export namespace $IRecipeCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCategory$Type<T> = ($IRecipeCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCategory_<T> = $IRecipeCategory$Type<(T)>;
}}
declare module "packages/mezz/jei/forge/platform/$BrewingRecipeMaker" {
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $BrewingRecipeMaker {

constructor()

public static "getBrewingRecipes"(arg0: $IIngredientManager$Type, arg1: $IVanillaRecipeFactory$Type): $List<($IJeiBrewingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeMaker$Type = ($BrewingRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeMaker_ = $BrewingRecipeMaker$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/ingredients/$ItemStackListFactory" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$StackHelper, $StackHelper$Type} from "packages/mezz/jei/common/util/$StackHelper"

export class $ItemStackListFactory {

constructor()

public static "create"(arg0: $StackHelper$Type): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackListFactory$Type = ($ItemStackListFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackListFactory_ = $ItemStackListFactory$Type;
}}
declare module "packages/mezz/jei/core/search/suffixtree/$RootNode" {
import {$Node, $Node$Type} from "packages/mezz/jei/core/search/suffixtree/$Node"

export class $RootNode<T> extends $Node<(T)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RootNode$Type<T> = ($RootNode<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RootNode_<T> = $RootNode$Type<(T)>;
}}
declare module "packages/mezz/jei/core/$FieldsAndMethodsAreNonnullByDefault" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $FieldsAndMethodsAreNonnullByDefault extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $FieldsAndMethodsAreNonnullByDefault {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldsAndMethodsAreNonnullByDefault$Type = ($FieldsAndMethodsAreNonnullByDefault);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldsAndMethodsAreNonnullByDefault_ = $FieldsAndMethodsAreNonnullByDefault$Type;
}}
declare module "packages/mezz/jei/library/recipes/$ExtendableRecipeCategoryHelper" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeCategoryExtension, $IRecipeCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryExtension"

export class $ExtendableRecipeCategoryHelper<T, W extends $IRecipeCategoryExtension> {

constructor(arg0: $Class$Type<(any)>)

public "addRecipeExtensionFactory"<R extends T>(arg0: $Class$Type<(any)>, arg1: $Predicate$Type<(R)>, arg2: $Function$Type<(R), (any)>): void
public "getOptionalRecipeExtension"<R extends T>(arg0: R): $Optional<(W)>
public "getRecipeExtension"<R extends T>(arg0: R): W
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendableRecipeCategoryHelper$Type<T, W> = ($ExtendableRecipeCategoryHelper<(T), (W)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendableRecipeCategoryHelper_<T, W> = $ExtendableRecipeCategoryHelper$Type<(T), (W)>;
}}
declare module "packages/mezz/jei/common/gui/$TooltipRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $TooltipRenderer {


public static "drawHoveringText"<T>(arg0: $GuiGraphics$Type, arg1: $List$Type<($Component$Type)>, arg2: integer, arg3: integer, arg4: $ITypedIngredient$Type<(T)>, arg5: $IIngredientRenderer$Type<(T)>, arg6: $IIngredientManager$Type): void
public static "drawHoveringText"<T>(arg0: $GuiGraphics$Type, arg1: $List$Type<($Component$Type)>, arg2: integer, arg3: integer, arg4: $ITypedIngredient$Type<(T)>, arg5: $IIngredientManager$Type): void
public static "drawHoveringText"(arg0: $GuiGraphics$Type, arg1: $List$Type<($Component$Type)>, arg2: integer, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipRenderer$Type = ($TooltipRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipRenderer_ = $TooltipRenderer$Type;
}}
declare module "packages/mezz/jei/library/config/$EditModeConfig$IListener" {
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $EditModeConfig$IListener {

 "onIngredientVisibilityChanged"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void

(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void
}

export namespace $EditModeConfig$IListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditModeConfig$IListener$Type = ($EditModeConfig$IListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditModeConfig$IListener_ = $EditModeConfig$IListener$Type;
}}
declare module "packages/mezz/jei/library/gui/ingredients/$RendererOverrides" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"

export class $RendererOverrides {

constructor()

public "getIngredientHeight"(): integer
public "getIngredientWidth"(): integer
public "addOverride"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $IIngredientRenderer$Type<(T)>): void
public "getIngredientRenderer"<T>(arg0: $IIngredientType$Type<(T)>): $Optional<($IIngredientRenderer<(T)>)>
get "ingredientHeight"(): integer
get "ingredientWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RendererOverrides$Type = ($RendererOverrides);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RendererOverrides_ = $RendererOverrides$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IIngredientGridSource" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientGridSource$SourceListChangedListener, $IIngredientGridSource$SourceListChangedListener$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource$SourceListChangedListener"

export interface $IIngredientGridSource {

 "getIngredientList"(): $List<($ITypedIngredient<(any)>)>
 "addSourceListChangedListener"(arg0: $IIngredientGridSource$SourceListChangedListener$Type): void
}

export namespace $IIngredientGridSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientGridSource$Type = ($IIngredientGridSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientGridSource_ = $IIngredientGridSource$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/cooking/fuel/$FuelRecipeMaker" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiFuelingRecipe, $IJeiFuelingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiFuelingRecipe"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $FuelRecipeMaker {


public static "getFuelRecipes"(arg0: $IIngredientManager$Type): $List<($IJeiFuelingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelRecipeMaker$Type = ($FuelRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelRecipeMaker_ = $FuelRecipeMaker$Type;
}}
declare module "packages/mezz/jei/common/transfer/$RecipeTransferOperationsResult" {
import {$TransferOperation, $TransferOperation$Type} from "packages/mezz/jei/common/transfer/$TransferOperation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"

export class $RecipeTransferOperationsResult {
readonly "results": $List<($TransferOperation)>
readonly "missingItems": $List<($IRecipeSlotView)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferOperationsResult$Type = ($RecipeTransferOperationsResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferOperationsResult_ = $RecipeTransferOperationsResult$Type;
}}
declare module "packages/mezz/jei/library/recipes/collect/$IngredientToRecipesMap" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $IngredientToRecipesMap<R> {

constructor()

public "add"(arg0: R, arg1: $List$Type<(string)>): void
public "get"(arg0: string): $List<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientToRecipesMap$Type<R> = ($IngredientToRecipesMap<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientToRecipesMap_<R> = $IngredientToRecipesMap$Type<(R)>;
}}
declare module "packages/mezz/jei/gui/config/$BookmarkConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IBookmarkConfig, $IBookmarkConfig$Type} from "packages/mezz/jei/gui/config/$IBookmarkConfig"
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $BookmarkConfig implements $IBookmarkConfig {

constructor(arg0: $Path$Type)

public "loadBookmarks"(arg0: $IIngredientManager$Type, arg1: $BookmarkList$Type): void
public "saveBookmarks"(arg0: $IIngredientManager$Type, arg1: $List$Type<($ITypedIngredient$Type<(any)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookmarkConfig$Type = ($BookmarkConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookmarkConfig_ = $BookmarkConfig$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/$CraftingCategoryExtension" {
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ICraftingCategoryExtension, $ICraftingCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CraftingCategoryExtension<T extends $CraftingRecipe> implements $ICraftingCategoryExtension {

constructor(arg0: T)

public "getWidth"(): integer
public "getHeight"(): integer
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ICraftingGridHelper$Type, arg2: $IFocusGroup$Type): void
public "getRegistryName"(): $ResourceLocation
public "drawInfo"(arg0: integer, arg1: integer, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "handleInput"(arg0: double, arg1: double, arg2: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: double, arg1: double): $List<($Component)>
get "width"(): integer
get "height"(): integer
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingCategoryExtension$Type<T> = ($CraftingCategoryExtension<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingCategoryExtension_<T> = $CraftingCategoryExtension$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/overlay/$IngredientListOverlay" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IIngredientListOverlay, $IIngredientListOverlay$Type} from "packages/mezz/jei/api/runtime/$IIngredientListOverlay"
import {$IIngredientGridSource, $IIngredientGridSource$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IngredientGridWithNavigation, $IngredientGridWithNavigation$Type} from "packages/mezz/jei/gui/overlay/$IngredientGridWithNavigation"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$ICharTypedHandler, $ICharTypedHandler$Type} from "packages/mezz/jei/gui/input/$ICharTypedHandler"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IFilterTextSource, $IFilterTextSource$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CallbackInfo, $CallbackInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfo"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IDragHandler, $IDragHandler$Type} from "packages/mezz/jei/gui/input/$IDragHandler"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $IngredientListOverlay implements $IIngredientListOverlay, $IRecipeFocusSource, $ICharTypedHandler {

constructor(arg0: $IIngredientGridSource$Type, arg1: $IFilterTextSource$Type, arg2: $IScreenHelper$Type, arg3: $IngredientGridWithNavigation$Type, arg4: $IClientConfig$Type, arg5: $IClientToggleState$Type, arg6: $IConnectionToServer$Type, arg7: $Textures$Type, arg8: $IInternalKeyMappings$Type, arg9: $CheatUtil$Type)

public "onCharTyped"(arg0: character, arg1: integer): boolean
public "updateScreen"(arg0: $Screen$Type, arg1: $Set$Type<($ImmutableRect2i$Type)>): void
public "getVisibleIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
public "drawScreen"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
public "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
public "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
public "getIngredientUnderMouse"(arg0: double, arg1: double): $Stream<($IClickableIngredientInternal<(any)>)>
public "handler$zdm000$inject$renderOverlay"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float, arg5: $CallbackInfo$Type): void
public "handler$zdm000$inject$renderOverlay"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: $CallbackInfo$Type): void
public "drawTooltips"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "drawOnForeground"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "handleTick"(): void
public "isListDisplayed"(): boolean
public "hasKeyboardFocus"(): boolean
public "createDragHandler"(): $IDragHandler
public "createInputHandler"(): $IUserInputHandler
get "ingredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
get "listDisplayed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientListOverlay$Type = ($IngredientListOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientListOverlay_ = $IngredientListOverlay$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientSorterComparators" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$ModNameSortingConfig, $ModNameSortingConfig$Type} from "packages/mezz/jei/gui/config/$ModNameSortingConfig"
import {$IngredientSortStage, $IngredientSortStage$Type} from "packages/mezz/jei/common/config/$IngredientSortStage"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IngredientFilter, $IngredientFilter$Type} from "packages/mezz/jei/gui/ingredients/$IngredientFilter"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IngredientTypeSortingConfig, $IngredientTypeSortingConfig$Type} from "packages/mezz/jei/gui/config/$IngredientTypeSortingConfig"

export class $IngredientSorterComparators {

constructor(arg0: $IngredientFilter$Type, arg1: $IIngredientManager$Type, arg2: $ModNameSortingConfig$Type, arg3: $IngredientTypeSortingConfig$Type)

public "getDefault"(): $Comparator<($IListElementInfo<(any)>)>
public "getComparator"(arg0: $IngredientSortStage$Type): $Comparator<($IListElementInfo<(any)>)>
public "getComparator"(arg0: $List$Type<($IngredientSortStage$Type)>): $Comparator<($IListElementInfo<(any)>)>
public static "getItemStack"<V>(arg0: $IListElementInfo$Type<(V)>): $ItemStack
get "default"(): $Comparator<($IListElementInfo<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientSorterComparators$Type = ($IngredientSorterComparators);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientSorterComparators_ = $IngredientSorterComparators$Type;
}}
declare module "packages/mezz/jei/library/ingredients/$IngredientAcceptor" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientAcceptor, $IIngredientAcceptor$Type} from "packages/mezz/jei/api/gui/builder/$IIngredientAcceptor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IntSet, $IntSet$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntSet"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"

export class $IngredientAcceptor implements $IIngredientAcceptor<($IngredientAcceptor)> {

constructor(arg0: $IIngredientManager$Type)

public "addIngredient"<T>(arg0: $IIngredientType$Type<(T)>, arg1: T): $IngredientAcceptor
public "getAllIngredients"(): $List<($Optional<($ITypedIngredient<(any)>)>)>
public "getIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $Stream<(T)>
public "addFluidStack"(arg0: $Fluid$Type, arg1: long): $IngredientAcceptor
public "getMatches"(arg0: $IFocusGroup$Type, arg1: $RecipeIngredientRole$Type): $IntSet
public "getIngredientTypes"(): $Stream<($IIngredientType<(any)>)>
public "addItemStack"(arg0: $ItemStack$Type): $IngredientAcceptor
public "addIngredients"(arg0: $Ingredient$Type): $IngredientAcceptor
public "addItemStacks"(arg0: $List$Type<($ItemStack$Type)>): $IngredientAcceptor
get "allIngredients"(): $List<($Optional<($ITypedIngredient<(any)>)>)>
get "ingredientTypes"(): $Stream<($IIngredientType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientAcceptor$Type = ($IngredientAcceptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientAcceptor_ = $IngredientAcceptor$Type;
}}
declare module "packages/mezz/jei/library/recipes/$InternalRecipeManagerPlugin" {
import {$RecipeMap, $RecipeMap$Type} from "packages/mezz/jei/library/recipes/collect/$RecipeMap"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$RecipeTypeDataMap, $RecipeTypeDataMap$Type} from "packages/mezz/jei/library/recipes/collect/$RecipeTypeDataMap"
import {$IRecipeManagerPlugin, $IRecipeManagerPlugin$Type} from "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$EnumMap, $EnumMap$Type} from "packages/java/util/$EnumMap"

export class $InternalRecipeManagerPlugin implements $IRecipeManagerPlugin {

constructor(arg0: $IIngredientManager$Type, arg1: $RecipeTypeDataMap$Type, arg2: $EnumMap$Type<($RecipeIngredientRole$Type), ($RecipeMap$Type)>)

public "getRecipeTypes"<V>(arg0: $IFocus$Type<(V)>): $List<($RecipeType<(any)>)>
public "getRecipes"<T>(arg0: $IRecipeCategory$Type<(T)>): $List<(T)>
public "getRecipes"<T, V>(arg0: $IRecipeCategory$Type<(T)>, arg1: $IFocus$Type<(V)>): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalRecipeManagerPlugin$Type = ($InternalRecipeManagerPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalRecipeManagerPlugin_ = $InternalRecipeManagerPlugin$Type;
}}
declare module "packages/mezz/jei/common/util/$HorizontalAlignment" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $HorizontalAlignment extends $Enum<($HorizontalAlignment)> {
static readonly "LEFT": $HorizontalAlignment
static readonly "CENTER": $HorizontalAlignment
static readonly "RIGHT": $HorizontalAlignment


public static "values"(): ($HorizontalAlignment)[]
public static "valueOf"(arg0: string): $HorizontalAlignment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalAlignment$Type = (("left") | ("center") | ("right")) | ($HorizontalAlignment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalAlignment_ = $HorizontalAlignment$Type;
}}
declare module "packages/mezz/jei/forge/input/$ForgeJeiKeyMappingCategoryBuilder" {
import {$IJeiKeyMappingCategoryBuilder, $IJeiKeyMappingCategoryBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingCategoryBuilder"
import {$IJeiKeyMappingBuilder, $IJeiKeyMappingBuilder$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingBuilder"

export class $ForgeJeiKeyMappingCategoryBuilder implements $IJeiKeyMappingCategoryBuilder {

constructor(arg0: string)

public "createMapping"(arg0: string): $IJeiKeyMappingBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeJeiKeyMappingCategoryBuilder$Type = ($ForgeJeiKeyMappingCategoryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeJeiKeyMappingCategoryBuilder_ = $ForgeJeiKeyMappingCategoryBuilder$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeCategoryTab" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$RecipeGuiTab, $RecipeGuiTab$Type} from "packages/mezz/jei/gui/recipes/$RecipeGuiTab"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IRecipeGuiLogic, $IRecipeGuiLogic$Type} from "packages/mezz/jei/gui/recipes/$IRecipeGuiLogic"

export class $RecipeCategoryTab extends $RecipeGuiTab {
static readonly "TAB_HEIGHT": integer
static readonly "TAB_WIDTH": integer

constructor(arg0: $IRecipeGuiLogic$Type, arg1: $IRecipeCategory$Type<(any)>, arg2: $Textures$Type, arg3: integer, arg4: integer, arg5: $IIngredientManager$Type)

public "draw"(arg0: boolean, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "isSelected"(arg0: $IRecipeCategory$Type<(any)>): boolean
public "getTooltip"(arg0: $IModIdHelper$Type): $List<($Component)>
public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeCategoryTab$Type = ($RecipeCategoryTab);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeCategoryTab_ = $RecipeCategoryTab$Type;
}}
declare module "packages/mezz/jei/gui/filter/$IFilterTextSource" {
import {$IFilterTextSource$Listener, $IFilterTextSource$Listener$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource$Listener"

export interface $IFilterTextSource {

 "getFilterText"(): string
 "setFilterText"(arg0: string): boolean
 "addListener"(arg0: $IFilterTextSource$Listener$Type): void
}

export namespace $IFilterTextSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFilterTextSource$Type = ($IFilterTextSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFilterTextSource_ = $IFilterTextSource$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$IIngredientGridSource$SourceListChangedListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IIngredientGridSource$SourceListChangedListener {

 "onSourceListChanged"(): void

(): void
}

export namespace $IIngredientGridSource$SourceListChangedListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientGridSource$SourceListChangedListener$Type = ($IIngredientGridSource$SourceListChangedListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientGridSource$SourceListChangedListener_ = $IIngredientGridSource$SourceListChangedListener$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/$DebugGhostIngredientHandler" {
import {$IGhostIngredientHandler$Target, $IGhostIngredientHandler$Target$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler$Target"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $DebugGhostIngredientHandler<T extends $AbstractContainerScreen<(any)>> implements $IGhostIngredientHandler<(T)> {

constructor(arg0: $IIngredientManager$Type)

public "onComplete"(): void
public "getTargetsTyped"<I>(arg0: T, arg1: $ITypedIngredient$Type<(I)>, arg2: boolean): $List<($IGhostIngredientHandler$Target<(I)>)>
public "shouldHighlightTargets"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugGhostIngredientHandler$Type<T> = ($DebugGhostIngredientHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugGhostIngredientHandler_<T> = $DebugGhostIngredientHandler$Type<(T)>;
}}
declare module "packages/mezz/jei/common/network/$PacketIdServer" {
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PacketIdServer extends $Enum<($PacketIdServer)> implements $IPacketId {
static readonly "RECIPE_TRANSFER": $PacketIdServer
static readonly "DELETE_ITEM": $PacketIdServer
static readonly "GIVE_ITEM": $PacketIdServer
static readonly "SET_HOTBAR_ITEM": $PacketIdServer
static readonly "CHEAT_PERMISSION_REQUEST": $PacketIdServer
static readonly "VALUES": ($PacketIdServer)[]


public static "values"(): ($PacketIdServer)[]
public static "valueOf"(arg0: string): $PacketIdServer
public "ordinal"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketIdServer$Type = (("cheat_permission_request") | ("give_item") | ("delete_item") | ("set_hotbar_item") | ("recipe_transfer")) | ($PacketIdServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketIdServer_ = $PacketIdServer$Type;
}}
declare module "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotBuilder, $IRecipeSlotBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeSlotBuilder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ICraftingGridHelper {

 "createAndSetOutputs"<T>(arg0: $IRecipeLayoutBuilder$Type, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<(T)>): $IRecipeSlotBuilder
 "createAndSetOutputs"(arg0: $IRecipeLayoutBuilder$Type, arg1: $List$Type<($ItemStack$Type)>): $IRecipeSlotBuilder
 "setInputs"<T>(arg0: $List$Type<($IRecipeSlotBuilder$Type)>, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<($List$Type<(T)>)>, arg3: integer, arg4: integer): void
 "createAndSetInputs"<T>(arg0: $IRecipeLayoutBuilder$Type, arg1: $IIngredientType$Type<(T)>, arg2: $List$Type<($List$Type<(T)>)>, arg3: integer, arg4: integer): $List<($IRecipeSlotBuilder)>
 "createAndSetInputs"(arg0: $IRecipeLayoutBuilder$Type, arg1: $List$Type<($List$Type<($ItemStack$Type)>)>, arg2: integer, arg3: integer): $List<($IRecipeSlotBuilder)>
}

export namespace $ICraftingGridHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICraftingGridHelper$Type = ($ICraftingGridHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICraftingGridHelper_ = $ICraftingGridHelper$Type;
}}
declare module "packages/mezz/jei/gui/startup/$OverlayHelper" {
import {$IEditModeConfig, $IEditModeConfig$Type} from "packages/mezz/jei/api/runtime/$IEditModeConfig"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$BookmarkOverlay, $BookmarkOverlay$Type} from "packages/mezz/jei/gui/overlay/bookmarks/$BookmarkOverlay"
import {$IIngredientGridSource, $IIngredientGridSource$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"
import {$IngredientListOverlay, $IngredientListOverlay$Type} from "packages/mezz/jei/gui/overlay/$IngredientListOverlay"
import {$IFilterTextSource, $IFilterTextSource$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource"
import {$DrawableNineSliceTexture, $DrawableNineSliceTexture$Type} from "packages/mezz/jei/common/gui/elements/$DrawableNineSliceTexture"
import {$IIngredientGridConfig, $IIngredientGridConfig$Type} from "packages/mezz/jei/common/config/$IIngredientGridConfig"
import {$BookmarkList, $BookmarkList$Type} from "packages/mezz/jei/gui/bookmarks/$BookmarkList"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IScreenHelper, $IScreenHelper$Type} from "packages/mezz/jei/api/runtime/$IScreenHelper"
import {$IngredientGridWithNavigation, $IngredientGridWithNavigation$Type} from "packages/mezz/jei/gui/overlay/$IngredientGridWithNavigation"

export class $OverlayHelper {


public static "createIngredientGridWithNavigation"(arg0: $IIngredientGridSource$Type, arg1: $IIngredientManager$Type, arg2: $IIngredientGridConfig$Type, arg3: $IModIdHelper$Type, arg4: $DrawableNineSliceTexture$Type, arg5: $DrawableNineSliceTexture$Type, arg6: $IInternalKeyMappings$Type, arg7: $IEditModeConfig$Type, arg8: $IIngredientFilterConfig$Type, arg9: $IClientConfig$Type, arg10: $IClientToggleState$Type, arg11: $IConnectionToServer$Type, arg12: $Textures$Type, arg13: $IColorHelper$Type, arg14: $CheatUtil$Type, arg15: $IScreenHelper$Type): $IngredientGridWithNavigation
public static "createIngredientListOverlay"(arg0: $IIngredientManager$Type, arg1: $IScreenHelper$Type, arg2: $IIngredientGridSource$Type, arg3: $IFilterTextSource$Type, arg4: $IModIdHelper$Type, arg5: $IInternalKeyMappings$Type, arg6: $IIngredientGridConfig$Type, arg7: $IClientConfig$Type, arg8: $IClientToggleState$Type, arg9: $IEditModeConfig$Type, arg10: $IConnectionToServer$Type, arg11: $IIngredientFilterConfig$Type, arg12: $Textures$Type, arg13: $IColorHelper$Type, arg14: $CheatUtil$Type): $IngredientListOverlay
public static "createBookmarkOverlay"(arg0: $IIngredientManager$Type, arg1: $IScreenHelper$Type, arg2: $BookmarkList$Type, arg3: $IModIdHelper$Type, arg4: $IInternalKeyMappings$Type, arg5: $IIngredientGridConfig$Type, arg6: $IEditModeConfig$Type, arg7: $IIngredientFilterConfig$Type, arg8: $IClientConfig$Type, arg9: $IClientToggleState$Type, arg10: $IConnectionToServer$Type, arg11: $Textures$Type, arg12: $IColorHelper$Type, arg13: $CheatUtil$Type): $BookmarkOverlay
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverlayHelper$Type = ($OverlayHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverlayHelper_ = $OverlayHelper$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/compostable/$CompostingRecipeMaker" {
import {$IJeiCompostingRecipe, $IJeiCompostingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiCompostingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $CompostingRecipeMaker {

constructor()

public static "getRecipes"(arg0: $IIngredientManager$Type): $List<($IJeiCompostingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompostingRecipeMaker$Type = ($CompostingRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompostingRecipeMaker_ = $CompostingRecipeMaker$Type;
}}
declare module "packages/mezz/jei/gui/input/$UserInput$KeyPressable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $UserInput$KeyPressable {

 "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean

(arg0: integer, arg1: integer, arg2: integer): boolean
}

export namespace $UserInput$KeyPressable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserInput$KeyPressable$Type = ($UserInput$KeyPressable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserInput$KeyPressable_ = $UserInput$KeyPressable$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeManagerInternal" {
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeManagerPlugin, $IRecipeManagerPlugin$Type} from "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeCategoryDecorator, $IRecipeCategoryDecorator$Type} from "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator"
import {$RecipeCategorySortingConfig, $RecipeCategorySortingConfig$Type} from "packages/mezz/jei/library/config/$RecipeCategorySortingConfig"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $RecipeManagerInternal {

constructor(arg0: $List$Type<($IRecipeCategory$Type<(any)>)>, arg1: $ImmutableListMultimap$Type<($ResourceLocation$Type), ($ITypedIngredient$Type<(any)>)>, arg2: $ImmutableListMultimap$Type<($RecipeType$Type<(any)>), ($IRecipeCategoryDecorator$Type<(any)>)>, arg3: $IIngredientManager$Type, arg4: $List$Type<($IRecipeManagerPlugin$Type)>, arg5: $RecipeCategorySortingConfig$Type, arg6: $IIngredientVisibility$Type)

public "getRecipeCatalystStream"<T>(arg0: $RecipeType$Type<(T)>, arg1: boolean): $Stream<($ITypedIngredient<(any)>)>
public "getRecipeCategoriesForTypes"(arg0: $Collection$Type<($RecipeType$Type<(any)>)>, arg1: $IFocusGroup$Type, arg2: boolean): $Stream<($IRecipeCategory<(any)>)>
public "isCategoryHidden"(arg0: $IRecipeCategory$Type<(any)>, arg1: $IFocusGroup$Type): boolean
public "getRecipesStream"<T>(arg0: $RecipeType$Type<(T)>, arg1: $IFocusGroup$Type, arg2: boolean): $Stream<(T)>
public "hideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
public "hideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
public "getRecipeType"(arg0: $ResourceLocation$Type): $Optional<($RecipeType<(any)>)>
public "unhideRecipeCategory"(arg0: $RecipeType$Type<(any)>): void
public "addRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $List$Type<(T)>): void
public "unhideRecipes"<T>(arg0: $RecipeType$Type<(T)>, arg1: $Collection$Type<(T)>): void
public "getRecipeCategoryDecorators"<T>(arg0: $RecipeType$Type<(T)>): $List<($IRecipeCategoryDecorator<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeManagerInternal$Type = ($RecipeManagerInternal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeManagerInternal_ = $RecipeManagerInternal$Type;
}}
declare module "packages/mezz/jei/gui/recipes/$RecipeGuiTabs" {
import {$IPaged, $IPaged$Type} from "packages/mezz/jei/gui/input/$IPaged"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IRecipeGuiLogic, $IRecipeGuiLogic$Type} from "packages/mezz/jei/gui/recipes/$IRecipeGuiLogic"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"

export class $RecipeGuiTabs implements $IPaged {

constructor(arg0: $IRecipeGuiLogic$Type, arg1: $Textures$Type, arg2: $IIngredientManager$Type)

public "hasNext"(): boolean
public "hasPrevious"(): boolean
public "draw"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: $IModIdHelper$Type): void
public "getPageCount"(): integer
public "getInputHandler"(): $IUserInputHandler
public "previousPage"(): boolean
public "nextPage"(): boolean
public "getPageNumber"(): integer
public "initLayout"(arg0: $ImmutableRect2i$Type): void
get "pageCount"(): integer
get "inputHandler"(): $IUserInputHandler
get "pageNumber"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeGuiTabs$Type = ($RecipeGuiTabs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeGuiTabs_ = $RecipeGuiTabs$Type;
}}
declare module "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotTooltipCallback" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"

export interface $IRecipeSlotTooltipCallback {

 "onTooltip"(arg0: $IRecipeSlotView$Type, arg1: $List$Type<($Component$Type)>): void

(arg0: $IRecipeSlotView$Type, arg1: $List$Type<($Component$Type)>): void
}

export namespace $IRecipeSlotTooltipCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeSlotTooltipCallback$Type = ($IRecipeSlotTooltipCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeSlotTooltipCallback_ = $IRecipeSlotTooltipCallback$Type;
}}
declare module "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $IRecipeTransferHandler<C extends $AbstractContainerMenu, R> {

 "getRecipeType"(): $RecipeType<(R)>
 "getMenuType"(): $Optional<($MenuType<(C)>)>
 "getContainerClass"(): $Class<(any)>
 "transferRecipe"(arg0: C, arg1: R, arg2: $IRecipeSlotsView$Type, arg3: $Player$Type, arg4: boolean, arg5: boolean): $IRecipeTransferError
}

export namespace $IRecipeTransferHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeTransferHandler$Type<C, R> = ($IRecipeTransferHandler<(C), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeTransferHandler_<C, R> = $IRecipeTransferHandler$Type<(C), (R)>;
}}
declare module "packages/mezz/jei/common/input/$IInternalKeyMappings" {
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"
import {$IJeiKeyMappings, $IJeiKeyMappings$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMappings"

export interface $IInternalKeyMappings extends $IJeiKeyMappings {

 "getCloseRecipeGui"(): $IJeiKeyMapping
 "getCheatOneItem"(): $IJeiKeyMapping
 "getCheatItemStack"(): $IJeiKeyMapping
 "getShowRecipe"(): $IJeiKeyMapping
 "getShowUses"(): $IJeiKeyMapping
 "getPreviousSearch"(): $IJeiKeyMapping
 "getBookmark"(): $IJeiKeyMapping
 "getNextRecipePage"(): $IJeiKeyMapping
 "getFocusSearch"(): $IJeiKeyMapping
 "getNextCategory"(): $IJeiKeyMapping
 "getToggleOverlay"(): $IJeiKeyMapping
 "getToggleEditMode"(): $IJeiKeyMapping
 "getToggleCheatMode"(): $IJeiKeyMapping
 "getPreviousPage"(): $IJeiKeyMapping
 "getNextPage"(): $IJeiKeyMapping
 "getRecipeBack"(): $IJeiKeyMapping
 "getEnterKey"(): $IJeiKeyMapping
 "getEscapeKey"(): $IJeiKeyMapping
 "getCopyRecipeId"(): $IJeiKeyMapping
 "getRightClick"(): $IJeiKeyMapping
 "getNextSearch"(): $IJeiKeyMapping
 "getLeftClick"(): $IJeiKeyMapping
 "getToggleWildcardHideIngredient"(): $IJeiKeyMapping
 "getToggleCheatModeConfigButton"(): $IJeiKeyMapping
 "getToggleBookmarkOverlay"(): $IJeiKeyMapping
 "getPreviousCategory"(): $IJeiKeyMapping
 "getPreviousRecipePage"(): $IJeiKeyMapping
 "getToggleHideIngredient"(): $IJeiKeyMapping
 "getHoveredClearSearchBar"(): $IJeiKeyMapping
}

export namespace $IInternalKeyMappings {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IInternalKeyMappings$Type = ($IInternalKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IInternalKeyMappings_ = $IInternalKeyMappings$Type;
}}
declare module "packages/mezz/jei/common/network/packets/$PacketCheatPermission" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"
import {$ClientPacketData, $ClientPacketData$Type} from "packages/mezz/jei/common/network/$ClientPacketData"
import {$IPacketId, $IPacketId$Type} from "packages/mezz/jei/common/network/$IPacketId"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $PacketCheatPermission extends $PacketJei {

constructor(arg0: boolean)

public static "readPacketData"(arg0: $ClientPacketData$Type): $CompletableFuture<(void)>
public "getPacketId"(): $IPacketId
public "writePacketData"(arg0: $FriendlyByteBuf$Type): void
get "packetId"(): $IPacketId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketCheatPermission$Type = ($PacketCheatPermission);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketCheatPermission_ = $PacketCheatPermission$Type;
}}
declare module "packages/mezz/jei/gui/util/$MaximalRectangle" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export class $MaximalRectangle {


public static "getLargestRectangles"(arg0: $ImmutableRect2i$Type, arg1: $Collection$Type<($ImmutableRect2i$Type)>, arg2: integer): $Stream<($ImmutableRect2i)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaximalRectangle$Type = ($MaximalRectangle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaximalRectangle_ = $MaximalRectangle$Type;
}}
declare module "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $IIngredientTypeWithSubtypes<B, I> extends $IIngredientType<(I)> {

 "getBase"(arg0: I): B
 "getIngredientClass"(): $Class<(any)>
 "getIngredientBaseClass"(): $Class<(any)>
 "castIngredient"(arg0: any): $Optional<(I)>
}

export namespace $IIngredientTypeWithSubtypes {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientTypeWithSubtypes$Type<B, I> = ($IIngredientTypeWithSubtypes<(B), (I)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientTypeWithSubtypes_<B, I> = $IIngredientTypeWithSubtypes$Type<(B), (I)>;
}}
declare module "packages/mezz/jei/library/plugins/debug/$DebugRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$DebugRecipe, $DebugRecipe$Type} from "packages/mezz/jei/library/plugins/debug/$DebugRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $DebugRecipeCategory<F> implements $IRecipeCategory<($DebugRecipe)> {
static readonly "TYPE": $RecipeType<($DebugRecipe)>
static readonly "RECIPE_WIDTH": integer
static readonly "RECIPE_HEIGHT": integer

constructor(arg0: $IGuiHelper$Type, arg1: $IPlatformFluidHelper$Type<(F)>, arg2: $IIngredientManager$Type)

public "getRecipeType"(): $RecipeType<($DebugRecipe)>
public "draw"(arg0: $DebugRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "handleInput"(arg0: $DebugRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $DebugRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "setRuntime"(arg0: $IJeiRuntime$Type): void
public "getTooltipStrings"(arg0: $DebugRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $DebugRecipe$Type): $ResourceLocation
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $DebugRecipe$Type): boolean
get "recipeType"(): $RecipeType<($DebugRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
set "runtime"(value: $IJeiRuntime$Type)
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugRecipeCategory$Type<F> = ($DebugRecipeCategory<(F)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugRecipeCategory_<F> = $DebugRecipeCategory$Type<(F)>;
}}
declare module "packages/mezz/jei/api/recipe/$IFocusFactory" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IFocus, $IFocus$Type} from "packages/mezz/jei/api/recipe/$IFocus"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IFocusFactory {

 "getEmptyFocusGroup"(): $IFocusGroup
 "createFocusGroup"(arg0: $Collection$Type<(any)>): $IFocusGroup
 "createFocus"<V>(arg0: $RecipeIngredientRole$Type, arg1: $ITypedIngredient$Type<(V)>): $IFocus<(V)>
 "createFocus"<V>(arg0: $RecipeIngredientRole$Type, arg1: $IIngredientType$Type<(V)>, arg2: V): $IFocus<(V)>
}

export namespace $IFocusFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFocusFactory$Type = ($IFocusFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFocusFactory_ = $IFocusFactory$Type;
}}
declare module "packages/mezz/jei/common/network/$IConnectionToServer" {
import {$PacketJei, $PacketJei$Type} from "packages/mezz/jei/common/network/packets/$PacketJei"

export interface $IConnectionToServer {

 "isJeiOnServer"(): boolean
 "sendPacketToServer"(arg0: $PacketJei$Type): void
}

export namespace $IConnectionToServer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConnectionToServer$Type = ($IConnectionToServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConnectionToServer_ = $IConnectionToServer$Type;
}}
declare module "packages/mezz/jei/gui/ghost/$GhostIngredientReturning" {
import {$GhostIngredientDrag, $GhostIngredientDrag$Type} from "packages/mezz/jei/gui/ghost/$GhostIngredientDrag"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $GhostIngredientReturning<T> {


public static "create"<T>(arg0: $GhostIngredientDrag$Type<(T)>, arg1: double, arg2: double): $Optional<($GhostIngredientReturning<(T)>)>
public "drawItem"(arg0: $GuiGraphics$Type): void
public "isComplete"(): boolean
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostIngredientReturning$Type<T> = ($GhostIngredientReturning<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostIngredientReturning_<T> = $GhostIngredientReturning$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/ingredients/$IngredientFilter" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IModIdHelper, $IModIdHelper$Type} from "packages/mezz/jei/api/helpers/$IModIdHelper"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$IIngredientGridSource, $IIngredientGridSource$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource"
import {$IIngredientGridSource$SourceListChangedListener, $IIngredientGridSource$SourceListChangedListener$Type} from "packages/mezz/jei/gui/overlay/$IIngredientGridSource$SourceListChangedListener"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientSorter, $IIngredientSorter$Type} from "packages/mezz/jei/gui/ingredients/$IIngredientSorter"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"
import {$IIngredientManager$IIngredientListener, $IIngredientManager$IIngredientListener$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager$IIngredientListener"
import {$IFilterTextSource, $IFilterTextSource$Type} from "packages/mezz/jei/gui/filter/$IFilterTextSource"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IListElement, $IListElement$Type} from "packages/mezz/jei/gui/ingredients/$IListElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientVisibility, $IIngredientVisibility$Type} from "packages/mezz/jei/api/runtime/$IIngredientVisibility"

export class $IngredientFilter implements $IIngredientGridSource, $IIngredientManager$IIngredientListener {

constructor(arg0: $IFilterTextSource$Type, arg1: $IClientConfig$Type, arg2: $IIngredientFilterConfig$Type, arg3: $IIngredientManager$Type, arg4: $IIngredientSorter$Type, arg5: $NonNullList$Type<($IListElement$Type<(any)>)>, arg6: $IModIdHelper$Type, arg7: $IIngredientVisibility$Type, arg8: $IColorHelper$Type)

public "addIngredient"<V>(arg0: $IListElementInfo$Type<(V)>): void
public "updateHidden"(): void
public "invalidateCache"(): void
public "updateHiddenState"<V>(arg0: $IListElement$Type<(V)>): boolean
public "getIngredientList"(): $List<($ITypedIngredient<(any)>)>
public "onIngredientsAdded"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
public "getIngredientListPreSort"(arg0: $Comparator$Type<($IListElementInfo$Type<(any)>)>): $List<($IListElementInfo<(any)>)>
public "addSourceListChangedListener"(arg0: $IIngredientGridSource$SourceListChangedListener$Type): void
public "onIngredientsRemoved"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
public "getFilteredIngredients"<T>(arg0: $IIngredientType$Type<(T)>): $List<(T)>
public "getModNamesForSorting"(): $Set<(string)>
public "onIngredientVisibilityChanged"<V>(arg0: $ITypedIngredient$Type<(V)>, arg1: boolean): void
public "searchForMatchingElement"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $ITypedIngredient$Type<(V)>): $Optional<($IListElementInfo<(V)>)>
get "ingredientList"(): $List<($ITypedIngredient<(any)>)>
get "modNamesForSorting"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientFilter$Type = ($IngredientFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientFilter_ = $IngredientFilter$Type;
}}
declare module "packages/mezz/jei/common/config/file/$ConfigCategory" {
import {$ConfigValue, $ConfigValue$Type} from "packages/mezz/jei/common/config/file/$ConfigValue"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IJeiConfigCategory, $IJeiConfigCategory$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigCategory"

export class $ConfigCategory implements $IJeiConfigCategory {

constructor(arg0: string, arg1: $List$Type<($ConfigValue$Type<(any)>)>)

public "getName"(): string
public "getConfigValue"(arg0: string): $Optional<($ConfigValue<(any)>)>
public "getConfigValues"(): $Collection<($ConfigValue<(any)>)>
public "getValueNames"(): $Set<(string)>
get "name"(): string
get "configValues"(): $Collection<($ConfigValue<(any)>)>
get "valueNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCategory$Type = ($ConfigCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCategory_ = $ConfigCategory$Type;
}}
declare module "packages/mezz/jei/gui/overlay/$ElementRenderer" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export class $ElementRenderer<T> implements $IClickableIngredientInternal<(T)> {

constructor(arg0: $ITypedIngredient$Type<(T)>)

public "setPadding"(arg0: integer): void
public "getPadding"(): integer
public "getTypedIngredient"(): $ITypedIngredient<(T)>
public "getArea"(): $ImmutableRect2i
public "canClickToFocus"(): boolean
public "allowsCheating"(): boolean
public "setArea"(arg0: $ImmutableRect2i$Type): void
set "padding"(value: integer)
get "padding"(): integer
get "typedIngredient"(): $ITypedIngredient<(T)>
get "area"(): $ImmutableRect2i
set "area"(value: $ImmutableRect2i$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementRenderer$Type<T> = ($ElementRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementRenderer_<T> = $ElementRenderer$Type<(T)>;
}}
declare module "packages/mezz/jei/api/runtime/$IJeiFeatures" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IJeiFeatures {

 "disableInventoryEffectRendererGuiHandler"(): void

(): void
}

export namespace $IJeiFeatures {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiFeatures$Type = ($IJeiFeatures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiFeatures_ = $IJeiFeatures$Type;
}}
declare module "packages/mezz/jei/gui/ingredients/$IListElementInfo" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IListElement, $IListElement$Type} from "packages/mezz/jei/gui/ingredients/$IListElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$IIngredientFilterConfig, $IIngredientFilterConfig$Type} from "packages/mezz/jei/common/config/$IIngredientFilterConfig"

export interface $IListElementInfo<V> {

 "getName"(): string
 "getElement"(): $IListElement<(V)>
 "getTypedIngredient"(): $ITypedIngredient<(V)>
 "getResourceLocation"(): $ResourceLocation
 "getSortedIndex"(): integer
 "getTagStrings"(arg0: $IIngredientManager$Type): $Collection<(string)>
 "getModNameStrings"(): $Set<(string)>
 "getTooltipStrings"(arg0: $IIngredientFilterConfig$Type, arg1: $IIngredientManager$Type): $List<(string)>
 "getColors"(arg0: $IIngredientManager$Type): $Iterable<(integer)>
 "getTagIds"(arg0: $IIngredientManager$Type): $Stream<($ResourceLocation)>
 "setSortedIndex"(arg0: integer): void
 "getModNameForSorting"(): string
}

export namespace $IListElementInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IListElementInfo$Type<V> = ($IListElementInfo<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IListElementInfo_<V> = $IListElementInfo$Type<(V)>;
}}
declare module "packages/mezz/jei/common/config/file/$FileWatcher" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $FileWatcher {

constructor(arg0: string)

public "start"(): void
public "addCallback"(arg0: $Path$Type, arg1: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileWatcher$Type = ($FileWatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileWatcher_ = $FileWatcher$Type;
}}
declare module "packages/mezz/jei/common/util/$NavigationVisibility" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NavigationVisibility extends $Enum<($NavigationVisibility)> {
static readonly "ENABLED": $NavigationVisibility
static readonly "AUTO_HIDE": $NavigationVisibility
static readonly "DISABLED": $NavigationVisibility


public static "values"(): ($NavigationVisibility)[]
public static "valueOf"(arg0: string): $NavigationVisibility
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigationVisibility$Type = (("disabled") | ("auto_hide") | ("enabled")) | ($NavigationVisibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigationVisibility_ = $NavigationVisibility$Type;
}}
declare module "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IDrawableAnimated extends $IDrawable {

 "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
 "draw"(arg0: $GuiGraphics$Type): void
 "getWidth"(): integer
 "getHeight"(): integer
}

export namespace $IDrawableAnimated {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDrawableAnimated$Type = ($IDrawableAnimated);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDrawableAnimated_ = $IDrawableAnimated$Type;
}}
declare module "packages/mezz/jei/library/startup/$StartData" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"

export class $StartData extends $Record {

constructor(plugins: $List$Type<($IModPlugin$Type)>, serverConnection: $IConnectionToServer$Type, keyBindings: $IInternalKeyMappings$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "keyBindings"(): $IInternalKeyMappings
public "plugins"(): $List<($IModPlugin)>
public "serverConnection"(): $IConnectionToServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StartData$Type = ($StartData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StartData_ = $StartData$Type;
}}
declare module "packages/mezz/jei/api/ingredients/$IIngredientRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IIngredientRenderer<T> {

 "render"(arg0: $GuiGraphics$Type, arg1: T): void
 "getWidth"(): integer
 "getHeight"(): integer
 "getFontRenderer"(arg0: $Minecraft$Type, arg1: T): $Font
 "getTooltip"(arg0: T, arg1: $TooltipFlag$Type): $List<($Component)>
}

export namespace $IIngredientRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientRenderer$Type<T> = ($IIngredientRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientRenderer_<T> = $IIngredientRenderer$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/anvil/$SmithingRecipeCategory" {
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $SmithingRecipeCategory implements $IRecipeCategory<($SmithingRecipe)> {

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($SmithingRecipe)>
public "isHandled"(arg0: $SmithingRecipe$Type): boolean
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $SmithingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $SmithingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "handleInput"(arg0: $SmithingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $SmithingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $SmithingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($SmithingRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmithingRecipeCategory$Type = ($SmithingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmithingRecipeCategory_ = $SmithingRecipeCategory$Type;
}}
declare module "packages/mezz/jei/library/config/$ModIdFormatConfig" {
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"
import {$IModIdFormatConfig, $IModIdFormatConfig$Type} from "packages/mezz/jei/library/config/$IModIdFormatConfig"

export class $ModIdFormatConfig implements $IModIdFormatConfig {
static readonly "MOD_NAME_FORMAT_CODE": string

constructor(arg0: $IConfigSchemaBuilder$Type)

public "isModNameFormatOverrideActive"(): boolean
public "getModNameFormat"(): string
get "modNameFormatOverrideActive"(): boolean
get "modNameFormat"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIdFormatConfig$Type = ($ModIdFormatConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIdFormatConfig_ = $ModIdFormatConfig$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$OffsetDrawable" {
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $OffsetDrawable implements $IDrawable {


public static "create"(arg0: $IDrawable$Type, arg1: integer, arg2: integer): $IDrawable
public "draw"(arg0: $GuiGraphics$Type): void
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OffsetDrawable$Type = ($OffsetDrawable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OffsetDrawable_ = $OffsetDrawable$Type;
}}
declare module "packages/mezz/jei/library/ingredients/subtypes/$SubtypeInterpreters" {
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"

export class $SubtypeInterpreters {

constructor()

public "addInterpreter"<B, I>(arg0: $IIngredientTypeWithSubtypes$Type<(B), (I)>, arg1: B, arg2: $IIngredientSubtypeInterpreter$Type<(I)>): void
public "get"<B, I>(arg0: $IIngredientTypeWithSubtypes$Type<(B), (I)>, arg1: I): $Optional<($IIngredientSubtypeInterpreter<(I)>)>
public "contains"<B>(arg0: $IIngredientTypeWithSubtypes$Type<(B), (any)>, arg1: B): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubtypeInterpreters$Type = ($SubtypeInterpreters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubtypeInterpreters_ = $SubtypeInterpreters$Type;
}}
declare module "packages/mezz/jei/library/gui/ingredients/$RecipeSlotsView" {
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $RecipeSlotsView implements $IRecipeSlotsView {

constructor(arg0: $List$Type<(any)>)

public "getSlotViews"(arg0: $RecipeIngredientRole$Type): $List<($IRecipeSlotView)>
public "getSlotViews"(): $List<($IRecipeSlotView)>
public "findSlotByName"(arg0: string): $Optional<($IRecipeSlotView)>
get "slotViews"(): $List<($IRecipeSlotView)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSlotsView$Type = ($RecipeSlotsView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSlotsView_ = $RecipeSlotsView$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IBookmarkOverlay" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IBookmarkOverlay {

 "getItemStackUnderMouse"(): $ItemStack
 "getIngredientUnderMouse"<T>(arg0: $IIngredientType$Type<(T)>): T
 "getIngredientUnderMouse"(): $Optional<($ITypedIngredient<(any)>)>
}

export namespace $IBookmarkOverlay {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBookmarkOverlay$Type = ($IBookmarkOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBookmarkOverlay_ = $IBookmarkOverlay$Type;
}}
declare module "packages/mezz/jei/gui/input/$CombinedRecipeFocusSource" {
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IClickableIngredientInternal, $IClickableIngredientInternal$Type} from "packages/mezz/jei/common/input/$IClickableIngredientInternal"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"

export class $CombinedRecipeFocusSource {

constructor(...arg0: ($IRecipeFocusSource$Type)[])

public "getIngredientUnderMouse"(arg0: $UserInput$Type, arg1: $IInternalKeyMappings$Type): $Stream<($IClickableIngredientInternal<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedRecipeFocusSource$Type = ($CombinedRecipeFocusSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedRecipeFocusSource_ = $CombinedRecipeFocusSource$Type;
}}
declare module "packages/mezz/jei/api/constants/$VanillaTypes" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"

export class $VanillaTypes {
static readonly "ITEM_STACK": $IIngredientTypeWithSubtypes<($Item), ($ItemStack)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaTypes$Type = ($VanillaTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaTypes_ = $VanillaTypes$Type;
}}
declare module "packages/mezz/jei/common/gui/elements/$DrawableNineSliceTexture" {
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$JeiSpriteUploader, $JeiSpriteUploader$Type} from "packages/mezz/jei/common/gui/textures/$JeiSpriteUploader"

export class $DrawableNineSliceTexture {

constructor(arg0: $JeiSpriteUploader$Type, arg1: $ResourceLocation$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer)

public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "draw"(arg0: $GuiGraphics$Type, arg1: $ImmutableRect2i$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableNineSliceTexture$Type = ($DrawableNineSliceTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableNineSliceTexture_ = $DrawableNineSliceTexture$Type;
}}
declare module "packages/mezz/jei/core/search/suffixtree/$GeneralizedSuffixTree" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$ISearchStorage, $ISearchStorage$Type} from "packages/mezz/jei/core/search/$ISearchStorage"

export class $GeneralizedSuffixTree<T> implements $ISearchStorage<(T)> {

constructor()

public "put"(arg0: string, arg1: T): void
public "getAllElements"(arg0: $Consumer$Type<($Collection$Type<(T)>)>): void
public "getSearchResults"(arg0: string, arg1: $Consumer$Type<($Collection$Type<(T)>)>): void
public "statistics"(): string
public "printTree"(arg0: $PrintWriter$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneralizedSuffixTree$Type<T> = ($GeneralizedSuffixTree<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneralizedSuffixTree_<T> = $GeneralizedSuffixTree$Type<(T)>;
}}
declare module "packages/mezz/jei/api/runtime/$IJeiKeyMappings" {
import {$IJeiKeyMapping, $IJeiKeyMapping$Type} from "packages/mezz/jei/api/runtime/$IJeiKeyMapping"

export interface $IJeiKeyMappings {

 "getShowRecipe"(): $IJeiKeyMapping
 "getShowUses"(): $IJeiKeyMapping
}

export namespace $IJeiKeyMappings {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiKeyMappings$Type = ($IJeiKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiKeyMappings_ = $IJeiKeyMappings$Type;
}}
declare module "packages/mezz/jei/forge/events/$RuntimeEventSubscriptions" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $RuntimeEventSubscriptions {

constructor(arg0: $IEventBus$Type)

public "clear"(): void
public "isEmpty"(): boolean
public "register"<T extends $Event>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeEventSubscriptions$Type = ($RuntimeEventSubscriptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeEventSubscriptions_ = $RuntimeEventSubscriptions$Type;
}}
declare module "packages/mezz/jei/library/transfer/$BasicRecipeTransferHandler" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$IRecipeTransferHandlerHelper, $IRecipeTransferHandlerHelper$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandlerHelper"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeTransferInfo, $IRecipeTransferInfo$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferInfo"
import {$BasicRecipeTransferHandler$InventoryState, $BasicRecipeTransferHandler$InventoryState$Type} from "packages/mezz/jei/library/transfer/$BasicRecipeTransferHandler$InventoryState"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeSlotView, $IRecipeSlotView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotView"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IRecipeTransferError, $IRecipeTransferError$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferError"
import {$IStackHelper, $IStackHelper$Type} from "packages/mezz/jei/api/helpers/$IStackHelper"

export class $BasicRecipeTransferHandler<C extends $AbstractContainerMenu, R> implements $IRecipeTransferHandler<(C), (R)> {

constructor(arg0: $IConnectionToServer$Type, arg1: $IStackHelper$Type, arg2: $IRecipeTransferHandlerHelper$Type, arg3: $IRecipeTransferInfo$Type<(C), (R)>)

public "getRecipeType"(): $RecipeType<(R)>
public "getMenuType"(): $Optional<($MenuType<(C)>)>
public "getContainerClass"(): $Class<(any)>
public static "validateTransferInfo"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>, arg1: C, arg2: $List$Type<($Slot$Type)>, arg3: $List$Type<($Slot$Type)>): boolean
public "transferRecipe"(arg0: C, arg1: R, arg2: $IRecipeSlotsView$Type, arg3: $Player$Type, arg4: boolean, arg5: boolean): $IRecipeTransferError
public static "validateRecipeView"<C extends $AbstractContainerMenu, R>(arg0: $IRecipeTransferInfo$Type<(C), (R)>, arg1: C, arg2: $List$Type<($Slot$Type)>, arg3: $List$Type<($IRecipeSlotView$Type)>): boolean
public static "getInventoryState"<C extends $AbstractContainerMenu, R>(arg0: $Collection$Type<($Slot$Type)>, arg1: $Collection$Type<($Slot$Type)>, arg2: $Player$Type, arg3: C, arg4: $IRecipeTransferInfo$Type<(C), (R)>): $BasicRecipeTransferHandler$InventoryState
public static "slotIndexes"(arg0: $Collection$Type<($Slot$Type)>): $Set<(integer)>
get "recipeType"(): $RecipeType<(R)>
get "menuType"(): $Optional<($MenuType<(C)>)>
get "containerClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicRecipeTransferHandler$Type<C, R> = ($BasicRecipeTransferHandler<(C), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicRecipeTransferHandler_<C, R> = $BasicRecipeTransferHandler$Type<(C), (R)>;
}}
declare module "packages/mezz/jei/forge/platform/$RecipeHelper" {
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$IJeiBrewingRecipe, $IJeiBrewingRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiBrewingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IVanillaRecipeFactory, $IVanillaRecipeFactory$Type} from "packages/mezz/jei/api/recipe/vanilla/$IVanillaRecipeFactory"
import {$IPlatformRecipeHelper, $IPlatformRecipeHelper$Type} from "packages/mezz/jei/common/platform/$IPlatformRecipeHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RecipeHelper implements $IPlatformRecipeHelper {

constructor()

public "getBase"(arg0: $SmithingRecipe$Type): $Ingredient
public "getWidth"<T extends $CraftingRecipe>(arg0: T): integer
public "getHeight"<T extends $CraftingRecipe>(arg0: T): integer
public "isHandled"(arg0: $SmithingRecipe$Type): boolean
public "getBrewingRecipes"(arg0: $IIngredientManager$Type, arg1: $IVanillaRecipeFactory$Type): $List<($IJeiBrewingRecipe)>
public "getAddition"(arg0: $SmithingRecipe$Type): $Ingredient
public "getRegistryNameForRecipe"(arg0: $Recipe$Type<(any)>): $Optional<($ResourceLocation)>
public "getTemplate"(arg0: $SmithingRecipe$Type): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeHelper$Type = ($RecipeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeHelper_ = $RecipeHelper$Type;
}}
declare module "packages/mezz/jei/common/config/sorting/$SortingConfig" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ISortingSerializer, $ISortingSerializer$Type} from "packages/mezz/jei/common/config/sorting/serializers/$ISortingSerializer"

export class $SortingConfig<T> {

constructor(arg0: $Path$Type, arg1: $ISortingSerializer$Type<(T)>)

public "getComparator"<V>(arg0: $Collection$Type<(T)>, arg1: $Function$Type<(V), (T)>): $Comparator<(V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortingConfig$Type<T> = ($SortingConfig<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortingConfig_<T> = $SortingConfig$Type<(T)>;
}}
declare module "packages/mezz/jei/library/plugins/jei/info/$IngredientInfoRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$IJeiIngredientInfoRecipe, $IJeiIngredientInfoRecipe$Type} from "packages/mezz/jei/api/recipe/vanilla/$IJeiIngredientInfoRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $IngredientInfoRecipeCategory implements $IRecipeCategory<($IJeiIngredientInfoRecipe)> {

constructor(arg0: $IGuiHelper$Type, arg1: $Textures$Type)

public "getRecipeType"(): $RecipeType<($IJeiIngredientInfoRecipe)>
public "draw"(arg0: $IJeiIngredientInfoRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $IJeiIngredientInfoRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $IJeiIngredientInfoRecipe$Type): boolean
public "handleInput"(arg0: $IJeiIngredientInfoRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $IJeiIngredientInfoRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $IJeiIngredientInfoRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($IJeiIngredientInfoRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientInfoRecipeCategory$Type = ($IngredientInfoRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientInfoRecipeCategory_ = $IngredientInfoRecipeCategory$Type;
}}
declare module "packages/mezz/jei/forge/input/$ForgeUserInput" {
import {$ScreenEvent$MouseButtonReleased, $ScreenEvent$MouseButtonReleased$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonReleased"
import {$ScreenEvent$MouseButtonPressed, $ScreenEvent$MouseButtonPressed$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$ScreenEvent$KeyPressed, $ScreenEvent$KeyPressed$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyPressed"

export class $ForgeUserInput {


public static "fromEvent"(arg0: $ScreenEvent$MouseButtonReleased$Type): $Optional<($UserInput)>
public static "fromEvent"(arg0: $ScreenEvent$MouseButtonPressed$Type): $Optional<($UserInput)>
public static "fromEvent"(arg0: $ScreenEvent$KeyPressed$Type): $UserInput
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeUserInput$Type = ($ForgeUserInput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeUserInput_ = $ForgeUserInput$Type;
}}
declare module "packages/mezz/jei/api/recipe/category/extensions/$IRecipeCategoryDecorator" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export interface $IRecipeCategoryDecorator<T> {

 "draw"(arg0: T, arg1: $IRecipeCategory$Type<(T)>, arg2: $IRecipeSlotsView$Type, arg3: $GuiGraphics$Type, arg4: double, arg5: double): void
 "decorateExistingTooltips"(arg0: $List$Type<($Component$Type)>, arg1: T, arg2: $IRecipeCategory$Type<(T)>, arg3: $IRecipeSlotsView$Type, arg4: double, arg5: double): $List<($Component)>
}

export namespace $IRecipeCategoryDecorator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeCategoryDecorator$Type<T> = ($IRecipeCategoryDecorator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeCategoryDecorator_<T> = $IRecipeCategoryDecorator$Type<(T)>;
}}
declare module "packages/mezz/jei/gui/overlay/$ElementRenderersByType" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$ElementRenderer, $ElementRenderer$Type} from "packages/mezz/jei/gui/overlay/$ElementRenderer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $ElementRenderersByType {

constructor()

public "get"<T>(arg0: $IIngredientType$Type<(T)>): $Collection<($ElementRenderer<(T)>)>
public "put"<T>(arg0: $IIngredientType$Type<(T)>, arg1: $ElementRenderer$Type<(T)>): void
public "clear"(): void
public "getTypes"(): $Set<($IIngredientType<(any)>)>
get "types"(): $Set<($IIngredientType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementRenderersByType$Type = ($ElementRenderersByType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementRenderersByType_ = $ElementRenderersByType$Type;
}}
declare module "packages/mezz/jei/library/load/registration/$SubtypeRegistration" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SubtypeInterpreters, $SubtypeInterpreters$Type} from "packages/mezz/jei/library/ingredients/subtypes/$SubtypeInterpreters"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"

export class $SubtypeRegistration implements $ISubtypeRegistration {

constructor()

public "getInterpreters"(): $SubtypeInterpreters
public "useNbtForSubtypes"(...arg0: ($Item$Type)[]): void
public "useNbtForSubtypes"(...arg0: ($Fluid$Type)[]): void
public "registerSubtypeInterpreter"<B, I>(arg0: $IIngredientTypeWithSubtypes$Type<(B), (I)>, arg1: B, arg2: $IIngredientSubtypeInterpreter$Type<(I)>): void
public "registerSubtypeInterpreter"(arg0: $Item$Type, arg1: $IIngredientSubtypeInterpreter$Type<($ItemStack$Type)>): void
get "interpreters"(): $SubtypeInterpreters
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubtypeRegistration$Type = ($SubtypeRegistration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubtypeRegistration_ = $SubtypeRegistration$Type;
}}
declare module "packages/mezz/jei/forge/$JustEnoughItemsCommon" {
import {$PermanentEventSubscriptions, $PermanentEventSubscriptions$Type} from "packages/mezz/jei/forge/events/$PermanentEventSubscriptions"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/mezz/jei/forge/network/$NetworkHandler"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"

export class $JustEnoughItemsCommon {

constructor(arg0: $NetworkHandler$Type, arg1: $IServerConfig$Type)

public "register"(arg0: $PermanentEventSubscriptions$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JustEnoughItemsCommon$Type = ($JustEnoughItemsCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JustEnoughItemsCommon_ = $JustEnoughItemsCommon$Type;
}}
declare module "packages/mezz/jei/gui/ghost/$GhostIngredientDrag" {
import {$IGhostIngredientHandler$Target, $IGhostIngredientHandler$Target$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler$Target"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$List, $List$Type} from "packages/java/util/$List"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IGhostIngredientHandler, $IGhostIngredientHandler$Type} from "packages/mezz/jei/api/gui/handlers/$IGhostIngredientHandler"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IIngredientManager, $IIngredientManager$Type} from "packages/mezz/jei/api/runtime/$IIngredientManager"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $GhostIngredientDrag<T> {

constructor(arg0: $IGhostIngredientHandler$Type<(any)>, arg1: $List$Type<($IGhostIngredientHandler$Target$Type<(T)>)>, arg2: $IIngredientManager$Type, arg3: $IIngredientRenderer$Type<(T)>, arg4: $ITypedIngredient$Type<(T)>, arg5: double, arg6: double, arg7: $ImmutableRect2i$Type)

public "stop"(): void
public "getIngredient"(): $ITypedIngredient<(T)>
public "onClick"(arg0: $UserInput$Type): boolean
public "drawItem"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getIngredientManager"(): $IIngredientManager
public "getOrigin"(): $ImmutableRect2i
public static "drawTargets"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $List$Type<($Rect2i$Type)>): void
public "drawTargets"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public static "farEnoughToDraw"(arg0: $GhostIngredientDrag$Type<(any)>, arg1: double, arg2: double): boolean
public "getIngredientRenderer"(): $IIngredientRenderer<(T)>
get "ingredient"(): $ITypedIngredient<(T)>
get "ingredientManager"(): $IIngredientManager
get "origin"(): $ImmutableRect2i
get "ingredientRenderer"(): $IIngredientRenderer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostIngredientDrag$Type<T> = ($GhostIngredientDrag<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostIngredientDrag_<T> = $GhostIngredientDrag$Type<(T)>;
}}
declare module "packages/mezz/jei/library/recipes/$PluginManager" {
import {$RecipeTypeData, $RecipeTypeData$Type} from "packages/mezz/jei/library/recipes/collect/$RecipeTypeData"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IRecipeManagerPlugin, $IRecipeManagerPlugin$Type} from "packages/mezz/jei/api/recipe/advanced/$IRecipeManagerPlugin"

export class $PluginManager {

constructor(arg0: $IRecipeManagerPlugin$Type, arg1: $List$Type<($IRecipeManagerPlugin$Type)>)

public "getRecipeTypes"(arg0: $IFocusGroup$Type): $Stream<($RecipeType<(any)>)>
public "getRecipes"<T>(arg0: $RecipeTypeData$Type<(T)>, arg1: $IFocusGroup$Type, arg2: boolean): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PluginManager$Type = ($PluginManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PluginManager_ = $PluginManager$Type;
}}
declare module "packages/mezz/jei/library/plugins/vanilla/crafting/replacers/$ShulkerBoxColoringRecipeMaker" {
import {$CraftingRecipe, $CraftingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CraftingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ShulkerBoxColoringRecipeMaker {


public static "createRecipes"(): $List<($CraftingRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShulkerBoxColoringRecipeMaker$Type = ($ShulkerBoxColoringRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShulkerBoxColoringRecipeMaker_ = $ShulkerBoxColoringRecipeMaker$Type;
}}
declare module "packages/mezz/jei/forge/$JustEnoughItems" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $JustEnoughItems {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JustEnoughItems$Type = ($JustEnoughItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JustEnoughItems_ = $JustEnoughItems$Type;
}}
declare module "packages/mezz/jei/common/util/$MathUtil" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $MathUtil {


public static "divideCeil"(arg0: integer, arg1: integer): integer
public static "contains"(arg0: $Rect2i$Type, arg1: double, arg2: double): boolean
public static "distance"(arg0: $Vec2$Type, arg1: $Vec2$Type): double
public static "union"(arg0: $ImmutableRect2i$Type, arg1: $ImmutableRect2i$Type): $ImmutableRect2i
public static "intersects"(arg0: $Collection$Type<($ImmutableRect2i$Type)>, arg1: $ImmutableRect2i$Type): boolean
public static "centerTextArea"(arg0: $ImmutableRect2i$Type, arg1: $Font$Type, arg2: string): $ImmutableRect2i
public static "centerTextArea"(arg0: $ImmutableRect2i$Type, arg1: $Font$Type, arg2: $FormattedText$Type): $ImmutableRect2i
public static "centerArea"(arg0: $ImmutableRect2i$Type, arg1: integer, arg2: integer): $ImmutableRect2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtil$Type = ($MathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtil_ = $MathUtil$Type;
}}
declare module "packages/mezz/jei/api/runtime/config/$IJeiConfigManager" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IJeiConfigFile, $IJeiConfigFile$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigFile"

export interface $IJeiConfigManager {

 "getConfigFiles"(): $Collection<($IJeiConfigFile)>

(): $Collection<($IJeiConfigFile)>
}

export namespace $IJeiConfigManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiConfigManager$Type = ($IJeiConfigManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiConfigManager_ = $IJeiConfigManager$Type;
}}
declare module "packages/mezz/jei/library/recipes/$RecipeTransferManager" {
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IRecipeTransferHandler, $IRecipeTransferHandler$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferHandler"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$IRecipeTransferManager, $IRecipeTransferManager$Type} from "packages/mezz/jei/api/recipe/transfer/$IRecipeTransferManager"
import {$ImmutableTable, $ImmutableTable$Type} from "packages/com/google/common/collect/$ImmutableTable"

export class $RecipeTransferManager implements $IRecipeTransferManager {

constructor(arg0: $ImmutableTable$Type<($Class$Type<(any)>), ($RecipeType$Type<(any)>), ($IRecipeTransferHandler$Type<(any), (any)>)>)

public "getRecipeTransferHandler"<C extends $AbstractContainerMenu, R>(arg0: C, arg1: $IRecipeCategory$Type<(R)>): $Optional<($IRecipeTransferHandler<(C), (R)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTransferManager$Type = ($RecipeTransferManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTransferManager_ = $RecipeTransferManager$Type;
}}
declare module "packages/mezz/jei/api/runtime/$IIngredientManager$IIngredientListener" {
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IIngredientManager$IIngredientListener {

 "onIngredientsAdded"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
 "onIngredientsRemoved"<V>(arg0: $IIngredientHelper$Type<(V)>, arg1: $Collection$Type<($ITypedIngredient$Type<(V)>)>): void
}

export namespace $IIngredientManager$IIngredientListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientManager$IIngredientListener$Type = ($IIngredientManager$IIngredientListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientManager$IIngredientListener_ = $IIngredientManager$IIngredientListener$Type;
}}
declare module "packages/mezz/jei/gui/input/handlers/$CheatInputHandler" {
import {$IClientToggleState, $IClientToggleState$Type} from "packages/mezz/jei/common/config/$IClientToggleState"
import {$IInternalKeyMappings, $IInternalKeyMappings$Type} from "packages/mezz/jei/common/input/$IInternalKeyMappings"
import {$IConnectionToServer, $IConnectionToServer$Type} from "packages/mezz/jei/common/network/$IConnectionToServer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$UserInput, $UserInput$Type} from "packages/mezz/jei/gui/input/$UserInput"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$CheatUtil, $CheatUtil$Type} from "packages/mezz/jei/gui/util/$CheatUtil"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$IRecipeFocusSource, $IRecipeFocusSource$Type} from "packages/mezz/jei/gui/input/$IRecipeFocusSource"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CheatInputHandler implements $IUserInputHandler {

constructor(arg0: $IRecipeFocusSource$Type, arg1: $IClientToggleState$Type, arg2: $IClientConfig$Type, arg3: $IConnectionToServer$Type, arg4: $CheatUtil$Type)

public "handleUserInput"(arg0: $Screen$Type, arg1: $UserInput$Type, arg2: $IInternalKeyMappings$Type): $Optional<($IUserInputHandler)>
public "handleMouseClickedOut"(arg0: $InputConstants$Key$Type): void
public "handleMouseScrolled"(arg0: double, arg1: double, arg2: double): $Optional<($IUserInputHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheatInputHandler$Type = ($CheatInputHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheatInputHandler_ = $CheatInputHandler$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredientRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ErrorIngredient, $ErrorIngredient$Type} from "packages/mezz/jei/library/plugins/debug/ingredients/$ErrorIngredient"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ErrorIngredientRenderer implements $IIngredientRenderer<($ErrorIngredient)> {

constructor(arg0: $IIngredientHelper$Type<($ErrorIngredient$Type)>)

public "render"(arg0: $GuiGraphics$Type, arg1: $ErrorIngredient$Type): void
public "getTooltip"(arg0: $ErrorIngredient$Type, arg1: $TooltipFlag$Type): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ErrorIngredient$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorIngredientRenderer$Type = ($ErrorIngredientRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorIngredientRenderer_ = $ErrorIngredientRenderer$Type;
}}
declare module "packages/mezz/jei/common/config/$ClientConfig" {
import {$IngredientSortStage, $IngredientSortStage$Type} from "packages/mezz/jei/common/config/$IngredientSortStage"
import {$IConfigSchemaBuilder, $IConfigSchemaBuilder$Type} from "packages/mezz/jei/common/config/file/$IConfigSchemaBuilder"
import {$GiveMode, $GiveMode$Type} from "packages/mezz/jei/common/config/$GiveMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IClientConfig, $IClientConfig$Type} from "packages/mezz/jei/common/config/$IClientConfig"

export class $ClientConfig implements $IClientConfig {

constructor(arg0: $IConfigSchemaBuilder$Type)

/**
 * 
 * @deprecated
 */
public static "getInstance"(): $IClientConfig
public "isLowMemorySlowSearchEnabled"(): boolean
public "getGiveMode"(): $GiveMode
public "isAddingBookmarksToFrontEnabled"(): boolean
public "isCheatToHotbarUsingHotkeysEnabled"(): boolean
public "isCatchRenderErrorsEnabled"(): boolean
public "isLookupBlockTagsEnabled"(): boolean
public "isLookupFluidContentsEnabled"(): boolean
public "getIngredientSorterStages"(): $List<($IngredientSortStage)>
public "getMaxRecipeGuiHeight"(): integer
public "isCenterSearchBarEnabled"(): boolean
get "instance"(): $IClientConfig
get "lowMemorySlowSearchEnabled"(): boolean
get "giveMode"(): $GiveMode
get "addingBookmarksToFrontEnabled"(): boolean
get "cheatToHotbarUsingHotkeysEnabled"(): boolean
get "catchRenderErrorsEnabled"(): boolean
get "lookupBlockTagsEnabled"(): boolean
get "lookupFluidContentsEnabled"(): boolean
get "ingredientSorterStages"(): $List<($IngredientSortStage)>
get "maxRecipeGuiHeight"(): integer
get "centerSearchBarEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$Type = ($ClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig_ = $ClientConfig$Type;
}}
declare module "packages/mezz/jei/common/config/$IServerConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IServerConfig {

 "isCheatModeEnabledForOp"(): boolean
 "isCheatModeEnabledForGive"(): boolean
 "isCheatModeEnabledForCreative"(): boolean
}

export namespace $IServerConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IServerConfig$Type = ($IServerConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IServerConfig_ = $IServerConfig$Type;
}}
declare module "packages/mezz/jei/gui/input/$GuiTextFieldFilter" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$ImmutableRect2i, $ImmutableRect2i$Type} from "packages/mezz/jei/common/util/$ImmutableRect2i"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Textures, $Textures$Type} from "packages/mezz/jei/common/gui/textures/$Textures"
import {$TextHistory$Direction, $TextHistory$Direction$Type} from "packages/mezz/jei/core/util/$TextHistory$Direction"
import {$IUserInputHandler, $IUserInputHandler$Type} from "packages/mezz/jei/gui/input/$IUserInputHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$EditBox, $EditBox$Type} from "packages/net/minecraft/client/gui/components/$EditBox"

export class $GuiTextFieldFilter extends $EditBox {
static readonly "BACKWARDS": integer
static readonly "FORWARDS": integer
static readonly "DEFAULT_TEXT_COLOR": integer
readonly "font": $Font
 "displayPos": integer
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $Textures$Type, arg1: $BooleanSupplier$Type)

public "isMouseOver"(arg0: double, arg1: double): boolean
public "setFocused"(arg0: boolean): void
public "getHistory"(arg0: $TextHistory$Direction$Type): $Optional<(string)>
public "setValue"(arg0: string): void
public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "updateBounds"(arg0: $ImmutableRect2i$Type): void
public "createInputHandler"(): $IUserInputHandler
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
set "focused"(value: boolean)
set "value"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiTextFieldFilter$Type = ($GuiTextFieldFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiTextFieldFilter_ = $GuiTextFieldFilter$Type;
}}
declare module "packages/mezz/jei/library/gui/ingredients/$RecipeSlots" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RecipeSlot, $RecipeSlot$Type} from "packages/mezz/jei/library/gui/ingredients/$RecipeSlot"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $RecipeSlots {

constructor()

public "draw"(arg0: $GuiGraphics$Type): void
public "getView"(): $IRecipeSlotsView
public "getSlots"(): $List<($RecipeSlot)>
public "getHoveredSlot"(arg0: double, arg1: double): $Optional<($RecipeSlot)>
public "addSlot"(arg0: $RecipeSlot$Type): void
get "view"(): $IRecipeSlotsView
get "slots"(): $List<($RecipeSlot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSlots$Type = ($RecipeSlots);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSlots_ = $RecipeSlots$Type;
}}
declare module "packages/mezz/jei/common/util/$ServerCommandUtil" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GiveMode, $GiveMode$Type} from "packages/mezz/jei/common/config/$GiveMode"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IServerConfig, $IServerConfig$Type} from "packages/mezz/jei/common/config/$IServerConfig"
import {$ServerPacketContext, $ServerPacketContext$Type} from "packages/mezz/jei/common/network/$ServerPacketContext"

export class $ServerCommandUtil {


public static "mousePickupItemStack"(arg0: $Player$Type, arg1: $ItemStack$Type): void
public static "executeGive"(arg0: $ServerPacketContext$Type, arg1: $ItemStack$Type, arg2: $GiveMode$Type): void
public static "setHotbarSlot"(arg0: $ServerPacketContext$Type, arg1: $ItemStack$Type, arg2: integer): void
public static "canStack"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
public static "hasPermissionForCheatMode"(arg0: $ServerPlayer$Type, arg1: $IServerConfig$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerCommandUtil$Type = ($ServerCommandUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerCommandUtil_ = $ServerCommandUtil$Type;
}}
declare module "packages/mezz/jei/library/color/$ColorHelper" {
import {$ColorNameConfig, $ColorNameConfig$Type} from "packages/mezz/jei/library/config/$ColorNameConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IColorHelper, $IColorHelper$Type} from "packages/mezz/jei/api/helpers/$IColorHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ColorHelper implements $IColorHelper {

constructor(arg0: $ColorNameConfig$Type)

public "getClosestColorName"(arg0: integer): string
public "getColors"(arg0: $TextureAtlasSprite$Type, arg1: integer, arg2: integer): $List<(integer)>
public "getColors"(arg0: $ItemStack$Type, arg1: integer): $List<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorHelper$Type = ($ColorHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorHelper_ = $ColorHelper$Type;
}}
declare module "packages/mezz/jei/forge/startup/$StartEventObserver" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$PermanentEventSubscriptions, $PermanentEventSubscriptions$Type} from "packages/mezz/jei/forge/events/$PermanentEventSubscriptions"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $StartEventObserver implements $ResourceManagerReloadListener {

constructor(arg0: $Runnable$Type, arg1: $Runnable$Type)

public "register"(arg0: $PermanentEventSubscriptions$Type): void
public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StartEventObserver$Type = ($StartEventObserver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StartEventObserver_ = $StartEventObserver$Type;
}}
declare module "packages/mezz/jei/forge/platform/$FluidHelper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$IIngredientSubtypeInterpreter, $IIngredientSubtypeInterpreter$Type} from "packages/mezz/jei/api/ingredients/subtypes/$IIngredientSubtypeInterpreter"
import {$IPlatformFluidHelperInternal, $IPlatformFluidHelperInternal$Type} from "packages/mezz/jei/common/platform/$IPlatformFluidHelperInternal"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$IIngredientTypeWithSubtypes, $IIngredientTypeWithSubtypes$Type} from "packages/mezz/jei/api/ingredients/$IIngredientTypeWithSubtypes"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $FluidHelper implements $IPlatformFluidHelperInternal<($FluidStack)> {

constructor()

public "copy"(arg0: $FluidStack$Type): $FluidStack
public "normalize"(arg0: $FluidStack$Type): $FluidStack
public "getDisplayName"(arg0: $FluidStack$Type): $Component
public "getTag"(arg0: $FluidStack$Type): $Optional<($CompoundTag)>
public "getContainedFluid"(arg0: $ITypedIngredient$Type<(any)>): $Optional<($FluidStack)>
public "createRenderer"(arg0: long, arg1: boolean, arg2: integer, arg3: integer): $IIngredientRenderer<($FluidStack)>
public "getColorTint"(arg0: $FluidStack$Type): integer
public "bucketVolume"(): long
public "getAmount"(arg0: $FluidStack$Type): long
public "getTooltip"(arg0: $FluidStack$Type, arg1: $TooltipFlag$Type): $List<($Component)>
public "getAllNbtSubtypeInterpreter"(): $IIngredientSubtypeInterpreter<($FluidStack)>
public "getFluidIngredientType"(): $IIngredientTypeWithSubtypes<($Fluid), ($FluidStack)>
public "getStillFluidSprite"(arg0: $FluidStack$Type): $Optional<($TextureAtlasSprite)>
get "allNbtSubtypeInterpreter"(): $IIngredientSubtypeInterpreter<($FluidStack)>
get "fluidIngredientType"(): $IIngredientTypeWithSubtypes<($Fluid), ($FluidStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidHelper$Type = ($FluidHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidHelper_ = $FluidHelper$Type;
}}
declare module "packages/mezz/jei/core/util/$TextHistory$Direction" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TextHistory$Direction extends $Enum<($TextHistory$Direction)> {
static readonly "NEXT": $TextHistory$Direction
static readonly "PREVIOUS": $TextHistory$Direction


public static "values"(): ($TextHistory$Direction)[]
public static "valueOf"(arg0: string): $TextHistory$Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextHistory$Direction$Type = (("next") | ("previous")) | ($TextHistory$Direction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextHistory$Direction_ = $TextHistory$Direction$Type;
}}
declare module "packages/mezz/jei/library/plugins/debug/$DebugFocusRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$DebugRecipe, $DebugRecipe$Type} from "packages/mezz/jei/library/plugins/debug/$DebugRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $DebugFocusRecipeCategory<F> implements $IRecipeCategory<($DebugRecipe)> {
static readonly "TYPE": $RecipeType<($DebugRecipe)>
static readonly "RECIPE_WIDTH": integer
static readonly "RECIPE_HEIGHT": integer

constructor(arg0: $IGuiHelper$Type, arg1: $IPlatformFluidHelper$Type<(F)>)

public "getRecipeType"(): $RecipeType<($DebugRecipe)>
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $DebugRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $DebugRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $DebugRecipe$Type): boolean
public "handleInput"(arg0: $DebugRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $DebugRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $DebugRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($DebugRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugFocusRecipeCategory$Type<F> = ($DebugFocusRecipeCategory<(F)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugFocusRecipeCategory_<F> = $DebugFocusRecipeCategory$Type<(F)>;
}}
declare module "packages/mezz/jei/api/runtime/$IClickableIngredient" {
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export interface $IClickableIngredient<T> {

 "getTypedIngredient"(): $ITypedIngredient<(T)>
 "getArea"(): $Rect2i
}

export namespace $IClickableIngredient {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClickableIngredient$Type<T> = ($IClickableIngredient<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClickableIngredient_<T> = $IClickableIngredient$Type<(T)>;
}}
declare module "packages/mezz/jei/common/config/$IIngredientFilterConfig" {
import {$SearchMode, $SearchMode$Type} from "packages/mezz/jei/core/search/$SearchMode"

export interface $IIngredientFilterConfig {

 "getTagSearchMode"(): $SearchMode
 "getColorSearchMode"(): $SearchMode
 "getModNameSearchMode"(): $SearchMode
 "getSearchAdvancedTooltips"(): boolean
 "getTooltipSearchMode"(): $SearchMode
 "getResourceLocationSearchMode"(): $SearchMode
}

export namespace $IIngredientFilterConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIngredientFilterConfig$Type = ($IIngredientFilterConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIngredientFilterConfig_ = $IIngredientFilterConfig$Type;
}}
declare module "packages/mezz/jei/forge/input/$ForgeJeiKeyMapping" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IJeiKeyMappingInternal, $IJeiKeyMappingInternal$Type} from "packages/mezz/jei/common/input/keys/$IJeiKeyMappingInternal"

export class $ForgeJeiKeyMapping implements $IJeiKeyMappingInternal {

constructor(arg0: $KeyMapping$Type)

public "register"(arg0: $Consumer$Type<($KeyMapping$Type)>): $IJeiKeyMappingInternal
public "isUnbound"(): boolean
public "getTranslatedKeyMessage"(): $Component
public "isActiveAndMatches"(arg0: $InputConstants$Key$Type): boolean
get "unbound"(): boolean
get "translatedKeyMessage"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeJeiKeyMapping$Type = ($ForgeJeiKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeJeiKeyMapping_ = $ForgeJeiKeyMapping$Type;
}}
declare module "packages/mezz/jei/gui/search/$IElementSearch" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ElementPrefixParser$TokenInfo, $ElementPrefixParser$TokenInfo$Type} from "packages/mezz/jei/gui/search/$ElementPrefixParser$TokenInfo"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IListElementInfo, $IListElementInfo$Type} from "packages/mezz/jei/gui/ingredients/$IListElementInfo"

export interface $IElementSearch {

 "add"(arg0: $IListElementInfo$Type<(any)>): void
 "getSearchResults"(arg0: $ElementPrefixParser$TokenInfo$Type): $Set<($IListElementInfo<(any)>)>
 "getAllIngredients"(): $Collection<($IListElementInfo<(any)>)>
 "logStatistics"(): void
}

export namespace $IElementSearch {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IElementSearch$Type = ($IElementSearch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IElementSearch_ = $IElementSearch$Type;
}}
declare module "packages/mezz/jei/library/color/$MMCQ$CMap" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$MMCQ$VBox, $MMCQ$VBox$Type} from "packages/mezz/jei/library/color/$MMCQ$VBox"

export class $MMCQ$CMap {
readonly "vboxes": $ArrayList<($MMCQ$VBox)>

constructor()

public "size"(): integer
public "map"(arg0: (integer)[]): (integer)[]
public "push"(arg0: $MMCQ$VBox$Type): void
public "nearest"(arg0: (integer)[]): (integer)[]
public "palette"(): ((integer)[])[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$CMap$Type = ($MMCQ$CMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ$CMap_ = $MMCQ$CMap$Type;
}}
declare module "packages/mezz/jei/api/recipe/vanilla/$IJeiIngredientInfoRecipe" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITypedIngredient, $ITypedIngredient$Type} from "packages/mezz/jei/api/ingredients/$ITypedIngredient"

export interface $IJeiIngredientInfoRecipe {

 "getDescription"(): $List<($FormattedText)>
 "getIngredients"(): $List<($ITypedIngredient<(any)>)>
}

export namespace $IJeiIngredientInfoRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IJeiIngredientInfoRecipe$Type = ($IJeiIngredientInfoRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IJeiIngredientInfoRecipe_ = $IJeiIngredientInfoRecipe$Type;
}}
