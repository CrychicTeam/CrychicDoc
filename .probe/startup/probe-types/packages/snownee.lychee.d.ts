declare module "packages/snownee/lychee/core/post/$DropXp" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $DropXp extends $PostAction {
readonly "xp": integer
 "path": string

constructor(arg0: integer)

public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropXp$Type = ($DropXp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropXp_ = $DropXp$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$Not" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $Not extends $Record implements $ContextualCondition {

constructor(condition: $ContextualCondition$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "condition"(): $ContextualCondition
public "getDescription"(arg0: boolean): $MutableComponent
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Not$Type = ($Not);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Not_ = $Not$Type;
}}
declare module "packages/snownee/lychee/mixin/$ShapedRecipeAccess" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export interface $ShapedRecipeAccess {

 "getResult"(): $ItemStack
 "callMatches"(arg0: $CraftingContainer$Type, arg1: integer, arg2: integer, arg3: boolean): boolean
}

export namespace $ShapedRecipeAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapedRecipeAccess$Type = ($ShapedRecipeAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapedRecipeAccess_ = $ShapedRecipeAccess$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$IsSneaking" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $IsSneaking extends $Enum<($IsSneaking)> implements $ContextualCondition {
static readonly "INSTANCE": $IsSneaking


public static "values"(): ($IsSneaking)[]
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public static "valueOf"(arg0: string): $IsSneaking
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsSneaking$Type = (("instance")) | ($IsSneaking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsSneaking_ = $IsSneaking$Type;
}}
declare module "packages/snownee/lychee/compat/jei/$JEICompat$SlotType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $JEICompat$SlotType extends $Enum<($JEICompat$SlotType)> {
static readonly "NORMAL": $JEICompat$SlotType
static readonly "CHANCE": $JEICompat$SlotType
static readonly "CATALYST": $JEICompat$SlotType


public static "values"(): ($JEICompat$SlotType)[]
public static "valueOf"(arg0: string): $JEICompat$SlotType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEICompat$SlotType$Type = (("normal") | ("chance") | ("catalyst")) | ($JEICompat$SlotType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEICompat$SlotType_ = $JEICompat$SlotType$Type;
}}
declare module "packages/snownee/lychee/client/gui/$CustomLightingSettings$Builder" {
import {$CustomLightingSettings, $CustomLightingSettings$Type} from "packages/snownee/lychee/client/gui/$CustomLightingSettings"

export class $CustomLightingSettings$Builder {

constructor()

public "secondLightRotation"(arg0: float, arg1: float): $CustomLightingSettings$Builder
public "build"(): $CustomLightingSettings
public "firstLightRotation"(arg0: float, arg1: float): $CustomLightingSettings$Builder
public "doubleLight"(): $CustomLightingSettings$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomLightingSettings$Builder$Type = ($CustomLightingSettings$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomLightingSettings$Builder_ = $CustomLightingSettings$Builder$Type;
}}
declare module "packages/snownee/lychee/client/core/post/$IfPostActionRenderer" {
import {$If, $If$Type} from "packages/snownee/lychee/core/post/$If"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PostActionRenderer, $PostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$PostActionRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export class $IfPostActionRenderer implements $PostActionRenderer<($If)> {

constructor()

public "getBaseTooltips"(arg0: $If$Type): $List<($Component)>
public "getTooltips"(arg0: $If$Type): $List<($Component)>
public static "of"<T extends $PostAction>(arg0: $PostAction$Type): $PostActionRenderer<($If)>
public static "register"<T extends $PostAction>(arg0: $PostActionType$Type<($If$Type)>, arg1: $PostActionRenderer$Type<($If$Type)>): void
public "loadCatalystsInfo"(arg0: $If$Type, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
public "render"(arg0: $If$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public static "getTooltipsFromRandom"(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IfPostActionRenderer$Type = ($IfPostActionRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IfPostActionRenderer_ = $IfPostActionRenderer$Type;
}}
declare module "packages/snownee/lychee/core/post/$If" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$CompoundAction, $CompoundAction$Type} from "packages/snownee/lychee/core/post/$CompoundAction"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $If extends $PostAction implements $CompoundAction {
readonly "successEntries": ($PostAction)[]
readonly "failureEntries": ($PostAction)[]
readonly "canRepeat": boolean
readonly "hidden": boolean
readonly "preventSync": boolean
 "path": string

constructor(arg0: ($PostAction$Type)[], arg1: ($PostAction$Type)[])

public "isHidden"(): boolean
public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "onFailure"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getUsedPointers"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $Consumer$Type<($JsonPointer$Type)>): void
public "provideJsonInfo"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $JsonObject$Type): $JsonElement
public "getConsequenceTooltips"(arg0: $List$Type<($Component$Type)>, arg1: ($PostAction$Type)[], arg2: string): void
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getBlockOutputs"(): $List<($BlockPredicate)>
public "getChildActions"(): $Stream<($PostAction)>
public "getItemOutputs"(): $List<($ItemStack)>
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
get "blockOutputs"(): $List<($BlockPredicate)>
get "childActions"(): $Stream<($PostAction)>
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $If$Type = ($If);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $If_ = $If$Type;
}}
declare module "packages/snownee/lychee/$Lychee" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $Lychee {
static readonly "ID": string
static readonly "LOGGER": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lychee$Type = ($Lychee);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lychee_ = $Lychee$Type;
}}
declare module "packages/snownee/lychee/core/$LycheeContext$Builder" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LycheeContext$Builder<C extends $LycheeContext> {

constructor(arg0: $Level$Type)

public "create"(arg0: $LootContextParamSet$Type): C
public "setParams"(arg0: $Map$Type<($LootContextParam$Type<(any)>), (any)>): void
public "withRandom"(arg0: $RandomSource$Type): $LycheeContext$Builder<(C)>
public "withParameter"<T>(arg0: $LootContextParam$Type<(T)>, arg1: T): $LycheeContext$Builder<(C)>
public "getParameter"<T>(arg0: $LootContextParam$Type<(T)>): T
public "withOptionalParameter"<T>(arg0: $LootContextParam$Type<(T)>, arg1: T): $LycheeContext$Builder<(C)>
public "getOptionalParameter"<T>(arg0: $LootContextParam$Type<(T)>): T
public "withOptionalRandomSeed"(arg0: long): $LycheeContext$Builder<(C)>
public "withOptionalRandomSeed"(arg0: long, arg1: $RandomSource$Type): $LycheeContext$Builder<(C)>
set "params"(value: $Map$Type<($LootContextParam$Type<(any)>), (any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeContext$Builder$Type<C> = ($LycheeContext$Builder<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeContext$Builder_<C> = $LycheeContext$Builder$Type<(C)>;
}}
declare module "packages/snownee/lychee/fragment/$Fragments" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"

export class $Fragments extends $SimpleJsonResourceReloadListener {
static readonly "GSON": $Gson
static readonly "INSTANCE": $Fragments

constructor(arg0: string)

public "process"(arg0: $JsonElement$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Fragments$Type = ($Fragments);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Fragments_ = $Fragments$Type;
}}
declare module "packages/snownee/lychee/core/$ActionRuntime" {
import {$LinkedList, $LinkedList$Type} from "packages/java/util/$LinkedList"
import {$Delay$LycheeMarker, $Delay$LycheeMarker$Type} from "packages/snownee/lychee/core/post/$Delay$LycheeMarker"
import {$Job, $Job$Type} from "packages/snownee/lychee/core/$Job"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ActionRuntime$State, $ActionRuntime$State$Type} from "packages/snownee/lychee/core/$ActionRuntime$State"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $ActionRuntime {
 "doDefault": boolean
 "state": $ActionRuntime$State
readonly "jobs": $LinkedList<($Job)>
 "marker": $Delay$LycheeMarker

constructor()

public "run"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionRuntime$Type = ($ActionRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionRuntime_ = $ActionRuntime$Type;
}}
declare module "packages/snownee/lychee/client/core/post/$PostActionRenderer" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export interface $PostActionRenderer<T extends $PostAction> {

 "loadCatalystsInfo"(arg0: T, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
 "getBaseTooltips"(arg0: T): $List<($Component)>
 "render"(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
 "getTooltips"(arg0: T): $List<($Component)>
}

export namespace $PostActionRenderer {
const RENDERERS: $Map<($PostActionType<(any)>), ($PostActionRenderer<(any)>)>
const DEFAULT: $PostActionRenderer<($PostAction)>
function of<T>(arg0: $PostAction$Type): $PostActionRenderer<(T)>
function register<T>(arg0: $PostActionType$Type<(T)>, arg1: $PostActionRenderer$Type<(T)>): void
function getTooltipsFromRandom(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostActionRenderer$Type<T> = ($PostActionRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostActionRenderer_<T> = $PostActionRenderer$Type<(T)>;
}}
declare module "packages/snownee/lychee/compat/jei/$JEICompat" {
import {$IJeiHelpers, $IJeiHelpers$Type} from "packages/mezz/jei/api/helpers/$IJeiHelpers"
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$AllGuiTextures, $AllGuiTextures$Type} from "packages/snownee/lychee/client/gui/$AllGuiTextures"
import {$BaseJEICategory, $BaseJEICategory$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$JEIREI$CategoryCreationContext, $JEIREI$CategoryCreationContext$Type} from "packages/snownee/lychee/compat/$JEIREI$CategoryCreationContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$JEICompat$SlotType, $JEICompat$SlotType$Type} from "packages/snownee/lychee/compat/jei/$JEICompat$SlotType"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JEICompat implements $IModPlugin {
static readonly "UID": $ResourceLocation
static readonly "POST_ACTION": $IIngredientType<($PostAction)>
static readonly "CATEGORIES": $Map<($ResourceLocation), ($Map<($ResourceLocation), ($BaseJEICategory<(any), (any)>)>)>
static readonly "FACTORY_PROVIDERS": $List<($Consumer<($Map<($ResourceLocation), ($Function<($JEIREI$CategoryCreationContext), ($BaseJEICategory<(any), (any)>)>)>)>)>
static "RUNTIME": $IJeiRuntime
static "HELPERS": $IJeiHelpers
static "GUI": $IGuiHelper

constructor()

public static "slot"(arg0: $JEICompat$SlotType$Type): $IDrawable
public static "el"(arg0: $AllGuiTextures$Type): $IDrawable
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public static "addCategoryFactoryProvider"(arg0: $Consumer$Type<($Map$Type<($ResourceLocation$Type), ($Function$Type<($JEIREI$CategoryCreationContext$Type), ($BaseJEICategory$Type<(any), (any)>)>)>)>): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEICompat$Type = ($JEICompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEICompat_ = $JEICompat$Type;
}}
declare module "packages/snownee/lychee/crafting/$ShapedCraftingRecipe" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CraftingBookCategory, $CraftingBookCategory$Type} from "packages/net/minecraft/world/item/crafting/$CraftingBookCategory"
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$Pair, $Pair$Type} from "packages/snownee/lychee/util/$Pair"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ShapedRecipe, $ShapedRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapedRecipe"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$CraftingContext, $CraftingContext$Type} from "packages/snownee/lychee/crafting/$CraftingContext"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ShapedCraftingRecipe extends $ShapedRecipe implements $ILycheeRecipe<($CraftingContext)> {
static readonly "CONTAINER_WORLD_LOCATOR": $Cache<($Class<(any)>), ($Function<($CraftingContainer), ($Pair<($Vec3), ($Player)>)>)>
static readonly "MENU_WORLD_LOCATOR": $Cache<($Class<(any)>), ($Function<($AbstractContainerMenu), ($Pair<($Vec3), ($Player)>)>)>
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "pattern": string
readonly "width": integer
readonly "height": integer
readonly "result": $ItemStack

constructor(arg0: $ResourceLocation$Type, arg1: string, arg2: $CraftingBookCategory$Type, arg3: integer, arg4: integer, arg5: $NonNullList$Type<($Ingredient$Type)>, arg6: $ItemStack$Type, arg7: boolean)

public "getComment"(): string
public "showInRecipeViewer"(): boolean
public "getRemainingItems"(arg0: $CraftingContainer$Type): $NonNullList<($ItemStack)>
public "addAssemblingAction"(arg0: $PostAction$Type): void
public "getContextualHolder"(): $ContextualHolder
public "getSerializer"(): $RecipeSerializer<(any)>
public "isActionPath"(arg0: $JsonPointer$Type): boolean
public "getPostActions"(): $Stream<($PostAction)>
public "getActionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
public "getItemIndexes"(arg0: $JsonPointer$Type): $IntList
public "defaultItemPointer"(): $JsonPointer
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
public static "makeContext"(arg0: $CraftingContainer$Type, arg1: $Level$Type, arg2: integer, arg3: integer, arg4: boolean): $CraftingContext
public "addPostAction"(arg0: $PostAction$Type): void
public "matches"(arg0: $CraftingContainer$Type, arg1: $Level$Type): boolean
public "isSpecial"(): boolean
public "getAllActions"(): $Stream<($PostAction)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public "getItemIndexes"(arg0: $Reference$Type): $IntList
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public "lychee$getId"(): $ResourceLocation
public "showingActionsCount"(): integer
public "applyPostActions"(arg0: $LycheeContext$Type, arg1: integer): void
public "getBlockInputs"(): $List<($BlockPredicate)>
public "getBlockOutputs"(): $List<($BlockPredicate)>
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "comment"(): string
get "contextualHolder"(): $ContextualHolder
get "serializer"(): $RecipeSerializer<(any)>
get "postActions"(): $Stream<($PostAction)>
get "actionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
get "special"(): boolean
get "allActions"(): $Stream<($PostAction)>
get "blockInputs"(): $List<($BlockPredicate)>
get "blockOutputs"(): $List<($BlockPredicate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapedCraftingRecipe$Type = ($ShapedCraftingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapedCraftingRecipe_ = $ShapedCraftingRecipe$Type;
}}
declare module "packages/snownee/lychee/core/$EmptyContainer" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"

export class $EmptyContainer implements $Container {

constructor()

public "setChanged"(): void
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmptyContainer$Type = ($EmptyContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyContainer_ = $EmptyContainer$Type;
}}
declare module "packages/snownee/lychee/compat/rei/$SideBlockIcon" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RenderElement, $RenderElement$Type} from "packages/snownee/lychee/client/gui/$RenderElement"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $SideBlockIcon extends $RenderElement {
static readonly "EMPTY": $RenderElement

constructor(arg0: $ScreenElement$Type, arg1: $Supplier$Type<($BlockState$Type)>)

public "render"(arg0: $GuiGraphics$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SideBlockIcon$Type = ($SideBlockIcon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SideBlockIcon_ = $SideBlockIcon$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripParticleHandler" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DripParticleHandler$Simple, $DripParticleHandler$Simple$Type} from "packages/snownee/lychee/dripstone_dripping/$DripParticleHandler$Simple"

export interface $DripParticleHandler {

 "addParticle"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: double, arg4: double, arg5: double): void
 "getColor"(arg0: $ClientLevel$Type, arg1: $BlockState$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): integer
 "isGlowing"(arg0: $ClientLevel$Type, arg1: $BlockState$Type): boolean
}

export namespace $DripParticleHandler {
const SIMPLE_DUMMY: $DripParticleHandler$Simple
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripParticleHandler$Type = ($DripParticleHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripParticleHandler_ = $DripParticleHandler$Type;
}}
declare module "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$RandomBlockTickingRecipe, $RandomBlockTickingRecipe$Type} from "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RandomBlockTickingRecipe$Serializer extends $LycheeRecipe$Serializer<($RandomBlockTickingRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $RandomBlockTickingRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $RandomBlockTickingRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $RandomBlockTickingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomBlockTickingRecipe$Serializer$Type = ($RandomBlockTickingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomBlockTickingRecipe$Serializer_ = $RandomBlockTickingRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$Or" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $Or extends $ContextualHolder implements $ContextualCondition {

constructor()

public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Or$Type = ($Or);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Or_ = $Or$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$LightningChannelingRecipeCategory" {
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$LightningChannelingRecipe, $LightningChannelingRecipe$Type} from "packages/snownee/lychee/lightning_channeling/$LightningChannelingRecipe"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$ItemShapelessRecipeCategory, $ItemShapelessRecipeCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemShapelessRecipeCategory"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $LightningChannelingRecipeCategory extends $ItemShapelessRecipeCategory<($LightningChannelingRecipe)> {
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($ItemShapelessContext$Type), ($LightningChannelingRecipe$Type)>)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<($LightningChannelingRecipe$Type)>): $IDrawable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightningChannelingRecipeCategory$Type = ($LightningChannelingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightningChannelingRecipeCategory_ = $LightningChannelingRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/$RecipeTypes" {
import {$BlockExplodingContext, $BlockExplodingContext$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingContext"
import {$LightningChannelingRecipe, $LightningChannelingRecipe$Type} from "packages/snownee/lychee/lightning_channeling/$LightningChannelingRecipe"
import {$ItemExplodingRecipe, $ItemExplodingRecipe$Type} from "packages/snownee/lychee/item_exploding/$ItemExplodingRecipe"
import {$AnvilCraftingRecipe, $AnvilCraftingRecipe$Type} from "packages/snownee/lychee/anvil_crafting/$AnvilCraftingRecipe"
import {$RandomBlockTickingRecipeType, $RandomBlockTickingRecipeType$Type} from "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipeType"
import {$BlockCrushingRecipeType, $BlockCrushingRecipeType$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingRecipeType"
import {$BlockKeyRecipeType, $BlockKeyRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType"
import {$BlockInteractingRecipe, $BlockInteractingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockInteractingRecipe"
import {$AnvilContext, $AnvilContext$Type} from "packages/snownee/lychee/anvil_crafting/$AnvilContext"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ItemBurningRecipe, $ItemBurningRecipe$Type} from "packages/snownee/lychee/item_burning/$ItemBurningRecipe"
import {$DripstoneRecipeType, $DripstoneRecipeType$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipeType"
import {$ItemInsideRecipeType, $ItemInsideRecipeType$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipeType"
import {$ItemShapelessRecipeType, $ItemShapelessRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$ItemShapelessRecipeType"
import {$BlockClickingRecipe, $BlockClickingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockClickingRecipe"
import {$BlockExplodingRecipe, $BlockExplodingRecipe$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe"

export class $RecipeTypes {
static readonly "ALL": $Set<($LycheeRecipeType<(any), (any)>)>
static readonly "ITEM_BURNING": $LycheeRecipeType<($LycheeContext), ($ItemBurningRecipe)>
static readonly "ITEM_INSIDE": $ItemInsideRecipeType
static readonly "BLOCK_INTERACTING": $BlockKeyRecipeType<($LycheeContext), ($BlockInteractingRecipe)>
static readonly "BLOCK_CLICKING": $BlockKeyRecipeType<($LycheeContext), ($BlockClickingRecipe)>
static readonly "ANVIL_CRAFTING": $LycheeRecipeType<($AnvilContext), ($AnvilCraftingRecipe)>
static readonly "BLOCK_CRUSHING": $BlockCrushingRecipeType
static readonly "LIGHTNING_CHANNELING": $ItemShapelessRecipeType<($ItemShapelessContext), ($LightningChannelingRecipe)>
static readonly "ITEM_EXPLODING": $ItemShapelessRecipeType<($ItemShapelessContext), ($ItemExplodingRecipe)>
static readonly "BLOCK_EXPLODING": $BlockKeyRecipeType<($BlockExplodingContext), ($BlockExplodingRecipe)>
static readonly "RANDOM_BLOCK_TICKING": $RandomBlockTickingRecipeType
static readonly "DRIPSTONE_DRIPPING": $DripstoneRecipeType

constructor()

public static "register"<T extends $LycheeRecipeType<(any), (any)>>(arg0: T): T
public static "init"(): void
public static "buildCache"(): void
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
declare module "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$BlockCrushingRecipe, $BlockCrushingRecipe$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $BlockCrushingRecipe$Serializer extends $LycheeRecipe$Serializer<($BlockCrushingRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $BlockCrushingRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $BlockCrushingRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $BlockCrushingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCrushingRecipe$Serializer$Type = ($BlockCrushingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCrushingRecipe$Serializer_ = $BlockCrushingRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$LycheeCounter" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export interface $LycheeCounter {

 "lychee$getCount"(): integer
 "lychee$update"(arg0: $ResourceLocation$Type, arg1: $Recipe$Type<(any)>): void
 "lychee$setCount"(arg0: integer): void
 "lychee$setRecipeId"(arg0: $ResourceLocation$Type): void
 "lychee$getRecipeId"(): $ResourceLocation
}

export namespace $LycheeCounter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeCounter$Type = ($LycheeCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeCounter_ = $LycheeCounter$Type;
}}
declare module "packages/snownee/lychee/core/post/$Break" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $Break extends $PostAction {
static readonly "CLIENT_DUMMY": $Break
 "path": string

constructor()

public "isHidden"(): boolean
public "getType"(): $PostActionType<(any)>
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Break$Type = ($Break);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Break_ = $Break$Type;
}}
declare module "packages/snownee/lychee/core/$Job" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $Job {
 "action": $PostAction
 "times": integer

constructor(arg0: $PostAction$Type, arg1: integer)

public "apply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Job$Type = ($Job);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Job_ = $Job$Type;
}}
declare module "packages/snownee/lychee/util/$VecHelper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $VecHelper {
static readonly "CENTER_OF_ORIGIN": $Vec3

constructor()

public static "write"(arg0: $Vec3$Type, arg1: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $Vec3
public static "rotate"(arg0: $Vec3$Type, arg1: double, arg2: double, arg3: double): $Vec3
public static "rotate"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public static "rotate"(arg0: $Vec3$Type, arg1: double, arg2: $Direction$Axis$Type): $Vec3
public static "writeNBT"(arg0: $Vec3$Type): $ListTag
public static "readNBT"(arg0: $ListTag$Type): $Vec3
public static "axisAlingedPlaneOf"(arg0: $Vec3$Type): $Vec3
public static "axisAlingedPlaneOf"(arg0: $Direction$Type): $Vec3
public static "clampComponentWise"(arg0: $Vec3$Type, arg1: float): $Vec3
public static "intersect"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: $Direction$Axis$Type): (double)[]
public static "slerp"(arg0: float, arg1: $Vec3$Type, arg2: $Vec3$Type): $Vec3
public static "lookAt"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public static "project"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public static "rotateCentered"(arg0: $Vec3$Type, arg1: double, arg2: $Direction$Axis$Type): $Vec3
public static "onSameAxis"(arg0: $BlockPos$Type, arg1: $BlockPos$Type, arg2: $Direction$Axis$Type): boolean
public static "getCenterOf"(arg0: $Vec3i$Type): $Vec3
public static "clamp"(arg0: $Vec3$Type, arg1: float): $Vec3
public static "bezierDerivative"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: float): $Vec3
public static "mirror"(arg0: $Vec3$Type, arg1: $Mirror$Type): $Vec3
public static "projectToPlayerView"(arg0: $Vec3$Type, arg1: float): $Vec3
public static "bezier"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: float): $Vec3
public static "getCoordinate"(arg0: $Vec3i$Type, arg1: $Direction$Axis$Type): integer
public static "getCoordinate"(arg0: $Vec3$Type, arg1: $Direction$Axis$Type): float
public static "voxelSpace"(arg0: double, arg1: double, arg2: double): $Vec3
public static "mirrorCentered"(arg0: $Vec3$Type, arg1: $Mirror$Type): $Vec3
public static "writeNBTCompound"(arg0: $Vec3$Type): $CompoundTag
public static "componentMax"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public static "componentMin"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public static "intersectRanged"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: $Direction$Axis$Type): (double)[]
public static "intersectSphere"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: double): $Vec3
public static "readNBTCompound"(arg0: $CompoundTag$Type): $Vec3
public static "lerp"(arg0: float, arg1: $Vec3$Type, arg2: $Vec3$Type): $Vec3
public static "isVecPointingTowards"(arg0: $Vec3$Type, arg1: $Direction$Type): boolean
public static "offsetRandomly"(arg0: $Vec3$Type, arg1: $RandomSource$Type, arg2: float): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VecHelper$Type = ($VecHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VecHelper_ = $VecHelper$Type;
}}
declare module "packages/snownee/lychee/core/$ItemShapelessContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$RecipeMatcher, $RecipeMatcher$Type} from "packages/snownee/lychee/util/$RecipeMatcher"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $ItemShapelessContext extends $LycheeContext {
readonly "itemEntities": $List<($ItemEntity)>
 "filteredItems": $List<($ItemEntity)>
 "totalItems": integer
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public "getMatch"(): $RecipeMatcher<($ItemStack)>
public "setMatch"(arg0: $RecipeMatcher$Type<($ItemStack$Type)>): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "match"(): $RecipeMatcher<($ItemStack)>
set "match"(value: $RecipeMatcher$Type<($ItemStack$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessContext$Type = ($ItemShapelessContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessContext_ = $ItemShapelessContext$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$ItemInsideRecipeCategory" {
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$ItemAndBlockBaseCategory, $ItemAndBlockBaseCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemAndBlockBaseCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$ItemInsideRecipeType, $ItemInsideRecipeType$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipeType"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$ItemInsideRecipe, $ItemInsideRecipe$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipe"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $ItemInsideRecipeCategory extends $ItemAndBlockBaseCategory<($ItemShapelessContext), ($ItemInsideRecipe)> {
 "inputBlockRect": $Rect2i
 "methodRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $ItemInsideRecipeType$Type, arg1: $ScreenElement$Type)

public "getWidth"(): integer
public "drawExtra"(arg0: $ItemInsideRecipe$Type, arg1: $GuiGraphics$Type, arg2: double, arg3: double, arg4: integer): void
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInsideRecipeCategory$Type = ($ItemInsideRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInsideRecipeCategory_ = $ItemInsideRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/compat/kubejs/$LycheeKubeJSEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $LycheeKubeJSEvents {

}

export namespace $LycheeKubeJSEvents {
const GROUP: $EventGroup
const CLICKED_INFO_BADGE: $EventHandler
const CUSTOM_ACTION: $EventHandler
const CUSTOM_CONDITION: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeKubeJSEvents$Type = ($LycheeKubeJSEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeKubeJSEvents_ = $LycheeKubeJSEvents$Type;
}}
declare module "packages/snownee/lychee/client/gui/$GuiGameElement$GuiRenderBuilder" {
import {$ILightingSettings, $ILightingSettings$Type} from "packages/snownee/lychee/client/gui/$ILightingSettings"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$RenderElement, $RenderElement$Type} from "packages/snownee/lychee/client/gui/$RenderElement"

export class $GuiGameElement$GuiRenderBuilder extends $RenderElement {
static readonly "EMPTY": $RenderElement

constructor()

public "scale"(arg0: double): $GuiGameElement$GuiRenderBuilder
public "color"(arg0: integer): $GuiGameElement$GuiRenderBuilder
public "rotate"(arg0: double, arg1: double, arg2: double): $GuiGameElement$GuiRenderBuilder
public "rotateBlock"(arg0: double, arg1: double, arg2: double): $GuiGameElement$GuiRenderBuilder
public "atLocal"(arg0: double, arg1: double, arg2: double): $GuiGameElement$GuiRenderBuilder
public "lighting"(arg0: $ILightingSettings$Type): $GuiGameElement$GuiRenderBuilder
public "withRotationOffset"(arg0: $Vec3$Type): $GuiGameElement$GuiRenderBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiGameElement$GuiRenderBuilder$Type = ($GuiGameElement$GuiRenderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiGameElement$GuiRenderBuilder_ = $GuiGameElement$GuiRenderBuilder$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$IsWeather" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $IsWeather extends $Record implements $ContextualCondition {
static readonly "REGISTRY": $Map<(string), ($IsWeather)>
static "CLEAR": $IsWeather
static "RAIN": $IsWeather
static "THUNDER": $IsWeather

constructor(id: string, predicate: $Predicate$Type<($Level$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "id"(): string
public "getType"(): $ContextualConditionType<(any)>
public static "create"(arg0: string, arg1: $Predicate$Type<($Level$Type)>): $IsWeather
public "predicate"(): $Predicate<($Level)>
public "getDescription"(arg0: boolean): $MutableComponent
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsWeather$Type = ($IsWeather);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsWeather_ = $IsWeather$Type;
}}
declare module "packages/snownee/lychee/core/post/$CustomAction" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$CustomAction$Apply, $CustomAction$Apply$Type} from "packages/snownee/lychee/core/post/$CustomAction$Apply"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $CustomAction extends $PostAction {
readonly "data": $JsonObject
 "canRepeat": boolean
 "applyFunc": $CustomAction$Apply
 "path": string

constructor(arg0: $JsonObject$Type)

public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomAction$Type = ($CustomAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomAction_ = $CustomAction$Type;
}}
declare module "packages/snownee/lychee/mixin/$LootContextParamSetsAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LootContextParamSetsAccess {

}

export namespace $LootContextParamSetsAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextParamSetsAccess$Type = ($LootContextParamSetsAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextParamSetsAccess_ = $LootContextParamSetsAccess$Type;
}}
declare module "packages/snownee/lychee/mixin/$BlockPredicateAccess" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$StatePropertiesPredicate, $StatePropertiesPredicate$Type} from "packages/net/minecraft/advancements/critereon/$StatePropertiesPredicate"
import {$NbtPredicate, $NbtPredicate$Type} from "packages/net/minecraft/advancements/critereon/$NbtPredicate"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BlockPredicateAccess {

 "getProperties"(): $StatePropertiesPredicate
 "getTag"(): $TagKey<($Block)>
 "getNbt"(): $NbtPredicate
 "getBlocks"(): $Set<($Block)>
}

export namespace $BlockPredicateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPredicateAccess$Type = ($BlockPredicateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPredicateAccess_ = $BlockPredicateAccess$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$IsDifficulty" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$IntImmutableList, $IntImmutableList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntImmutableList"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $IsDifficulty extends $Record implements $ContextualCondition {

constructor(difficulties: $IntImmutableList$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public "difficulties"(): $IntImmutableList
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsDifficulty$Type = ($IsDifficulty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsDifficulty_ = $IsDifficulty$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$EntityHealth" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $EntityHealth extends $Record implements $ContextualCondition {

constructor(range: $MinMaxBounds$Doubles$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "range"(): $MinMaxBounds$Doubles
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHealth$Type = ($EntityHealth);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHealth_ = $EntityHealth$Type;
}}
declare module "packages/snownee/lychee/$LycheeRegistries$MappedRegistry" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $LycheeRegistries$MappedRegistry<T> extends $Record implements $Iterable<(T)> {

constructor(registry: $IForgeRegistry$Type<(T)>)

public "get"(arg0: $ResourceLocation$Type): T
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "iterator"(): $Iterator<(T)>
public "getKey"(arg0: T): $ResourceLocation
public "register"(arg0: $ResourceLocation$Type, arg1: T): void
public "key"(): $ResourceKey<($Registry<(T)>)>
public "registry"(): $IForgeRegistry<(T)>
public "spliterator"(): $Spliterator<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<T>;
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeRegistries$MappedRegistry$Type<T> = ($LycheeRegistries$MappedRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeRegistries$MappedRegistry_<T> = $LycheeRegistries$MappedRegistry$Type<(T)>;
}}
declare module "packages/snownee/lychee/client/core/post/$ItemStackPostActionRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ItemBasedPostActionRenderer, $ItemBasedPostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$ItemBasedPostActionRenderer"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PostActionRenderer, $PostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$PostActionRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export interface $ItemStackPostActionRenderer<T extends $PostAction> extends $ItemBasedPostActionRenderer<(T)> {

 "getBaseTooltips"(arg0: T): $List<($Component)>
 "getItem"(arg0: T): $ItemStack
 "render"(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
 "loadCatalystsInfo"(arg0: T, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
 "getTooltips"(arg0: T): $List<($Component)>

(arg0: T): $List<($Component)>
}

export namespace $ItemStackPostActionRenderer {
function of<T>(arg0: $PostAction$Type): $PostActionRenderer<(T)>
function register<T>(arg0: $PostActionType$Type<(T)>, arg1: $PostActionRenderer$Type<(T)>): void
function getTooltipsFromRandom(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackPostActionRenderer$Type<T> = ($ItemStackPostActionRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackPostActionRenderer_<T> = $ItemStackPostActionRenderer$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/post/$CustomAction$Apply" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export interface $CustomAction$Apply {

 "apply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void

(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
}

export namespace $CustomAction$Apply {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomAction$Apply$Type = ($CustomAction$Apply);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomAction$Apply_ = $CustomAction$Apply$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ChanceRecipe, $ChanceRecipe$Type} from "packages/snownee/lychee/core/recipe/$ChanceRecipe"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$DripstoneContext, $DripstoneContext$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneContext"

export class $DripstoneRecipe extends $LycheeRecipe<($DripstoneContext)> implements $BlockKeyRecipe<($DripstoneRecipe)>, $ChanceRecipe {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $DripstoneRecipe$Type): integer
public "matches"(arg0: $DripstoneContext$Type, arg1: $Level$Type): boolean
public static "on"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getBlock"(): $BlockPredicate
public static "safeTick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): boolean
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public static "getBlockAboveStalactite"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $BlockState
public "getChance"(): float
public "applyPostActions"(arg0: $LycheeContext$Type, arg1: integer): void
public "setChance"(arg0: float): void
public "getSourceBlock"(): $BlockPredicate
public "getBlockInputs"(): $List<($BlockPredicate)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "block"(): $BlockPredicate
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "chance"(): float
set "chance"(value: float)
get "sourceBlock"(): $BlockPredicate
get "blockInputs"(): $List<($BlockPredicate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneRecipe$Type = ($DripstoneRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneRecipe_ = $DripstoneRecipe$Type;
}}
declare module "packages/snownee/lychee/core/post/input/$PreventDefault" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $PreventDefault extends $PostAction {
static readonly "CLIENT_DUMMY": $PreventDefault
 "path": string

constructor()

public "isHidden"(): boolean
public "getType"(): $PostActionType<(any)>
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PreventDefault$Type = ($PreventDefault);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PreventDefault_ = $PreventDefault$Type;
}}
declare module "packages/snownee/lychee/util/json/$JsonPointer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"

export class $JsonPointer {
readonly "tokens": $List<(string)>

constructor(arg0: $Collection$Type<(string)>)
constructor(arg0: string)

public "parent"(): $JsonPointer
public "equals"(arg0: any): boolean
public "toString"(): string
public "append"(arg0: string): $JsonPointer
public "hashCode"(): integer
public "getInt"(arg0: integer): integer
public "size"(): integer
public "find"(arg0: $JsonElement$Type): $JsonElement
public "getString"(arg0: integer): string
public "isRoot"(): boolean
public "isSelfOrParentOf"(arg0: $List$Type<(string)>): boolean
get "root"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonPointer$Type = ($JsonPointer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonPointer_ = $JsonPointer$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$ContextualHolder" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $ContextualHolder {

constructor()

public "showingConditionsCount"(): integer
public "getConditionTooltips"(arg0: $List$Type<($Component$Type)>, arg1: integer, arg2: $Level$Type, arg3: $Player$Type): void
public "getConditions"(): $List<($ContextualCondition)>
public "parseConditions"(arg0: $JsonElement$Type): void
public "withCondition"(arg0: $ContextualCondition$Type): void
public "isSecretCondition"(arg0: integer): boolean
public "conditionsToNetwork"(arg0: $FriendlyByteBuf$Type): void
public "rawConditionsToJson"(): $JsonElement
public "conditionsFromNetwork"(arg0: $FriendlyByteBuf$Type): void
public "checkConditions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
get "conditions"(): $List<($ContextualCondition)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextualHolder$Type = ($ContextualHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextualHolder_ = $ContextualHolder$Type;
}}
declare module "packages/snownee/lychee/client/gui/$AllGuiTextures" {
import {$Color, $Color$Type} from "packages/snownee/lychee/util/$Color"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $AllGuiTextures extends $Enum<($AllGuiTextures)> implements $ScreenElement {
static readonly "JEI_SLOT": $AllGuiTextures
static readonly "JEI_CHANCE_SLOT": $AllGuiTextures
static readonly "JEI_CATALYST_SLOT": $AllGuiTextures
static readonly "JEI_ARROW": $AllGuiTextures
static readonly "JEI_LONG_ARROW": $AllGuiTextures
static readonly "JEI_DOWN_ARROW": $AllGuiTextures
static readonly "JEI_LIGHT": $AllGuiTextures
static readonly "JEI_QUESTION_MARK": $AllGuiTextures
static readonly "JEI_SHADOW": $AllGuiTextures
static readonly "BLOCKZAPPER_UPGRADE_RECIPE": $AllGuiTextures
static readonly "INFO": $AllGuiTextures
static readonly "LEFT_CLICK": $AllGuiTextures
static readonly "RIGHT_CLICK": $AllGuiTextures
readonly "location": $ResourceLocation
 "width": integer
 "height": integer
 "startX": integer
 "startY": integer


public static "values"(): ($AllGuiTextures)[]
public static "valueOf"(arg0: string): $AllGuiTextures
public "bind"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Color$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AllGuiTextures$Type = (("jei_slot") | ("jei_long_arrow") | ("blockzapper_upgrade_recipe") | ("jei_catalyst_slot") | ("jei_shadow") | ("right_click") | ("jei_question_mark") | ("jei_chance_slot") | ("left_click") | ("jei_arrow") | ("jei_down_arrow") | ("jei_light") | ("info")) | ($AllGuiTextures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AllGuiTextures_ = $AllGuiTextures$Type;
}}
declare module "packages/snownee/lychee/$PostActionTypes" {
import {$AnvilDamageChance, $AnvilDamageChance$Type} from "packages/snownee/lychee/core/post/$AnvilDamageChance"
import {$If, $If$Type} from "packages/snownee/lychee/core/post/$If"
import {$Delay, $Delay$Type} from "packages/snownee/lychee/core/post/$Delay"
import {$AddItemCooldown, $AddItemCooldown$Type} from "packages/snownee/lychee/core/post/$AddItemCooldown"
import {$CustomAction, $CustomAction$Type} from "packages/snownee/lychee/core/post/$CustomAction"
import {$PreventDefault, $PreventDefault$Type} from "packages/snownee/lychee/core/post/input/$PreventDefault"
import {$Explode, $Explode$Type} from "packages/snownee/lychee/core/post/$Explode"
import {$CycleStateProperty, $CycleStateProperty$Type} from "packages/snownee/lychee/core/post/$CycleStateProperty"
import {$Break, $Break$Type} from "packages/snownee/lychee/core/post/$Break"
import {$SetItem, $SetItem$Type} from "packages/snownee/lychee/core/post/input/$SetItem"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$DropItem, $DropItem$Type} from "packages/snownee/lychee/core/post/$DropItem"
import {$DamageItem, $DamageItem$Type} from "packages/snownee/lychee/core/post/input/$DamageItem"
import {$Execute, $Execute$Type} from "packages/snownee/lychee/core/post/$Execute"
import {$PlaceBlock, $PlaceBlock$Type} from "packages/snownee/lychee/core/post/$PlaceBlock"
import {$Hurt, $Hurt$Type} from "packages/snownee/lychee/core/post/$Hurt"
import {$MoveTowardsFace, $MoveTowardsFace$Type} from "packages/snownee/lychee/core/post/$MoveTowardsFace"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$DropXp, $DropXp$Type} from "packages/snownee/lychee/core/post/$DropXp"
import {$NBTPatch, $NBTPatch$Type} from "packages/snownee/lychee/core/post/input/$NBTPatch"

export class $PostActionTypes {
static readonly "DROP_ITEM": $PostActionType<($DropItem)>
static readonly "DROP_XP": $PostActionType<($DropXp)>
static readonly "EXECUTE": $PostActionType<($Execute)>
static readonly "PLACE": $PostActionType<($PlaceBlock)>
static readonly "DAMAGE_ITEM": $PostActionType<($DamageItem)>
static readonly "PREVENT_DEFAULT": $PostActionType<($PreventDefault)>
static readonly "ANVIL_DAMAGE_CHANCE": $PostActionType<($AnvilDamageChance)>
static readonly "RANDOM": $PostActionType<($RandomSelect)>
static readonly "EXPLODE": $PostActionType<($Explode)>
static readonly "HURT": $PostActionType<($Hurt)>
static readonly "DELAY": $PostActionType<($Delay)>
static readonly "BREAK": $PostActionType<($Break)>
static readonly "ADD_ITEM_COOLDOWN": $PostActionType<($AddItemCooldown)>
static readonly "CYCLE_STATE_PROPERTY": $PostActionType<($CycleStateProperty)>
static readonly "MOVE_TOWARDS_FACE": $PostActionType<($MoveTowardsFace)>
static readonly "SET_ITEM": $PostActionType<($SetItem)>
static readonly "NBT_PATCH": $PostActionType<($NBTPatch)>
static readonly "CUSTOM": $PostActionType<($CustomAction)>
static readonly "IF": $PostActionType<($If)>

constructor()

public static "register"<T extends $PostActionType<(any)>>(arg0: string, arg1: T): T
public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostActionTypes$Type = ($PostActionTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostActionTypes_ = $PostActionTypes$Type;
}}
declare module "packages/snownee/lychee/client/core/post/$ItemBasedPostActionRenderer" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PostActionRenderer, $PostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$PostActionRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export interface $ItemBasedPostActionRenderer<T extends $PostAction> extends $PostActionRenderer<(T)> {

 "getItem"(arg0: T): $ItemStack
 "render"(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
 "loadCatalystsInfo"(arg0: T, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
 "getBaseTooltips"(arg0: T): $List<($Component)>
 "getTooltips"(arg0: T): $List<($Component)>

(arg0: T): $ItemStack
}

export namespace $ItemBasedPostActionRenderer {
function of<T>(arg0: $PostAction$Type): $PostActionRenderer<(T)>
function register<T>(arg0: $PostActionType$Type<(T)>, arg1: $PostActionRenderer$Type<(T)>): void
function getTooltipsFromRandom(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBasedPostActionRenderer$Type<T> = ($ItemBasedPostActionRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBasedPostActionRenderer_<T> = $ItemBasedPostActionRenderer$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/contextual/$FallDistance" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $FallDistance extends $Record implements $ContextualCondition {

constructor(range: $MinMaxBounds$Doubles$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "range"(): $MinMaxBounds$Doubles
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FallDistance$Type = ($FallDistance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FallDistance_ = $FallDistance$Type;
}}
declare module "packages/snownee/lychee/item_inside/$ItemInsideRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ItemInsideRecipeType$Cache, $ItemInsideRecipeType$Cache$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipeType$Cache"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Object2FloatMap, $Object2FloatMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2FloatMap"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ItemShapelessRecipe, $ItemShapelessRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $ItemInsideRecipe extends $ItemShapelessRecipe<($ItemInsideRecipe)> implements $BlockKeyRecipe<($ItemInsideRecipe)> {
static readonly "MAX_INGREDIENTS": integer
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "matches"(arg0: $ItemShapelessContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getTime"(): integer
public "getBlock"(): $BlockPredicate
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "isSpecial"(): boolean
public "tickOrApply"(arg0: $ItemShapelessContext$Type): boolean
public "buildCache"(arg0: $Object2FloatMap$Type<($Item$Type)>, arg1: $List$Type<($ItemInsideRecipe$Type)>): $ItemInsideRecipeType$Cache
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "time"(): integer
get "block"(): $BlockPredicate
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "special"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInsideRecipe$Type = ($ItemInsideRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInsideRecipe_ = $ItemInsideRecipe$Type;
}}
declare module "packages/snownee/lychee/block_crushing/$BlockCrushingContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$FallingBlockEntity, $FallingBlockEntity$Type} from "packages/net/minecraft/world/entity/item/$FallingBlockEntity"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $BlockCrushingContext extends $ItemShapelessContext {
readonly "fallingBlock": $FallingBlockEntity
readonly "itemEntities": $List<($ItemEntity)>
 "filteredItems": $List<($ItemEntity)>
 "totalItems": integer
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCrushingContext$Type = ($BlockCrushingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCrushingContext_ = $BlockCrushingContext$Type;
}}
declare module "packages/snownee/lychee/client/gui/$RenderElement" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $RenderElement implements $ScreenElement {
static readonly "EMPTY": $RenderElement

constructor()

public static "of"(arg0: $ScreenElement$Type): $RenderElement
public "at"<T extends $RenderElement>(arg0: float, arg1: float): T
public "at"<T extends $RenderElement>(arg0: float, arg1: float, arg2: float): T
public "getY"(): float
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "render"(arg0: $GuiGraphics$Type): void
public "getZ"(): float
public "getX"(): float
public "getWidth"(): integer
public "getHeight"(): integer
public "withBounds"<T extends $RenderElement>(arg0: integer, arg1: integer): T
public "withAlpha"<T extends $RenderElement>(arg0: float): T
get "y"(): float
get "z"(): float
get "x"(): float
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderElement$Type = ($RenderElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderElement_ = $RenderElement$Type;
}}
declare module "packages/snownee/lychee/util/$CachedRenderingEntity" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CachedRenderingEntity<T extends $Entity> {


public static "of"<T extends $Entity>(arg0: T): $CachedRenderingEntity<(T)>
public "setScale"(arg0: float): void
public "render"(arg0: $PoseStack$Type, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): void
public "getScale"(): float
public "getEntity"(): T
public "setEntity"(arg0: T): void
public "earlySetLevel"(): T
public static "ofFactory"<T extends $Entity>(arg0: $Function$Type<($Level$Type), (T)>): $CachedRenderingEntity<(T)>
set "scale"(value: float)
get "scale"(): float
get "entity"(): T
set "entity"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedRenderingEntity$Type<T> = ($CachedRenderingEntity<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedRenderingEntity_<T> = $CachedRenderingEntity$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/contextual/$CustomCondition$Test" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export interface $CustomCondition$Test {

 "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer

(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
}

export namespace $CustomCondition$Test {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomCondition$Test$Type = ($CustomCondition$Test);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomCondition$Test_ = $CustomCondition$Test$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$CustomCondition" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CustomCondition$Test, $CustomCondition$Test$Type} from "packages/snownee/lychee/core/contextual/$CustomCondition$Test"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $CustomCondition implements $ContextualCondition {
readonly "data": $JsonObject
 "testFunc": $CustomCondition$Test
 "testInTooltipsFunc": $BiFunction<($Level), ($Player), ($InteractionResult)>

constructor(arg0: $JsonObject$Type)

public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomCondition$Type = ($CustomCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomCondition_ = $CustomCondition$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$BaseJEICategory$SlotLayoutFunction" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"

export interface $BaseJEICategory$SlotLayoutFunction<T> {

 "apply"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: integer, arg3: integer, arg4: integer): void

(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: integer, arg3: integer, arg4: integer): void
}

export namespace $BaseJEICategory$SlotLayoutFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseJEICategory$SlotLayoutFunction$Type<T> = ($BaseJEICategory$SlotLayoutFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseJEICategory$SlotLayoutFunction_<T> = $BaseJEICategory$SlotLayoutFunction$Type<(T)>;
}}
declare module "packages/snownee/lychee/compat/forge/$AlwaysTrueIngredient" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$AbstractIngredient, $AbstractIngredient$Type} from "packages/net/minecraftforge/common/crafting/$AbstractIngredient"
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$IIngredientSerializer, $IIngredientSerializer$Type} from "packages/net/minecraftforge/common/crafting/$IIngredientSerializer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AlwaysTrueIngredient extends $AbstractIngredient {
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList


public "test"(arg0: $ItemStack$Type): boolean
public "isSimple"(): boolean
public "toJson"(): $JsonElement
public "getSerializer"(): $IIngredientSerializer<(any)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
get "simple"(): boolean
get "serializer"(): $IIngredientSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlwaysTrueIngredient$Type = ($AlwaysTrueIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlwaysTrueIngredient_ = $AlwaysTrueIngredient$Type;
}}
declare module "packages/snownee/lychee/client/core/post/$CycleStatePropertyPostActionRenderer" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CycleStateProperty, $CycleStateProperty$Type} from "packages/snownee/lychee/core/post/$CycleStateProperty"
import {$PostActionRenderer, $PostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$PostActionRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export class $CycleStatePropertyPostActionRenderer implements $PostActionRenderer<($CycleStateProperty)> {

constructor()

public "render"(arg0: $CycleStateProperty$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public static "of"<T extends $PostAction>(arg0: $PostAction$Type): $PostActionRenderer<($CycleStateProperty)>
public static "register"<T extends $PostAction>(arg0: $PostActionType$Type<($CycleStateProperty$Type)>, arg1: $PostActionRenderer$Type<($CycleStateProperty$Type)>): void
public "loadCatalystsInfo"(arg0: $CycleStateProperty$Type, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
public "getBaseTooltips"(arg0: $CycleStateProperty$Type): $List<($Component)>
public static "getTooltipsFromRandom"(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
public "getTooltips"(arg0: $CycleStateProperty$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CycleStatePropertyPostActionRenderer$Type = ($CycleStatePropertyPostActionRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CycleStatePropertyPostActionRenderer_ = $CycleStatePropertyPostActionRenderer$Type;
}}
declare module "packages/snownee/lychee/compat/kubejs/$CustomActionEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$CustomAction, $CustomAction$Type} from "packages/snownee/lychee/core/post/$CustomAction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CustomActionEventJS extends $EventJS {
readonly "id": string
readonly "action": $CustomAction
readonly "recipe": $ILycheeRecipe<(any)>
readonly "patchContext": $ILycheeRecipe$NBTPatchContext
readonly "data": $Map<(any), (any)>

constructor(arg0: string, arg1: $CustomAction$Type, arg2: $ILycheeRecipe$Type<(any)>, arg3: $ILycheeRecipe$NBTPatchContext$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomActionEventJS$Type = ($CustomActionEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomActionEventJS_ = $CustomActionEventJS$Type;
}}
declare module "packages/snownee/lychee/client/gui/$CustomLightingSettings" {
import {$ILightingSettings, $ILightingSettings$Type} from "packages/snownee/lychee/client/gui/$ILightingSettings"
import {$CustomLightingSettings$Builder, $CustomLightingSettings$Builder$Type} from "packages/snownee/lychee/client/gui/$CustomLightingSettings$Builder"

export class $CustomLightingSettings implements $ILightingSettings {


public static "builder"(): $CustomLightingSettings$Builder
public "applyLighting"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomLightingSettings$Type = ($CustomLightingSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomLightingSettings_ = $CustomLightingSettings$Type;
}}
declare module "packages/snownee/lychee/core/def/$BoundsHelper" {
import {$DecimalFormat, $DecimalFormat$Type} from "packages/java/text/$DecimalFormat"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$MinMaxBounds, $MinMaxBounds$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds"

export class $BoundsHelper {
static "dfCommas": $DecimalFormat

constructor()

public static "getDescription"(arg0: $MinMaxBounds$Type<(any)>): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundsHelper$Type = ($BoundsHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundsHelper_ = $BoundsHelper$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$Location$Rule" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LocationPredicateAccess, $LocationPredicateAccess$Type} from "packages/snownee/lychee/mixin/$LocationPredicateAccess"

export interface $Location$Rule {

 "getName"(): string
 "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: integer, arg2: string, arg3: $LocationPredicateAccess$Type, arg4: $InteractionResult$Type): void
 "isAny"(arg0: $LocationPredicateAccess$Type): boolean
 "testClient"(arg0: $LocationPredicateAccess$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Vec3$Type): $InteractionResult
}

export namespace $Location$Rule {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Location$Rule$Type = ($Location$Rule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Location$Rule_ = $Location$Rule$Type;
}}
declare module "packages/snownee/lychee/core/def/$PropertiesPredicateHelper" {
import {$StatePropertiesPredicate$PropertyMatcher, $StatePropertiesPredicate$PropertyMatcher$Type} from "packages/net/minecraft/advancements/critereon/$StatePropertiesPredicate$PropertyMatcher"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$StatePropertiesPredicate, $StatePropertiesPredicate$Type} from "packages/net/minecraft/advancements/critereon/$StatePropertiesPredicate"

export class $PropertiesPredicateHelper {

constructor()

public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $StatePropertiesPredicate
public static "toNetwork"(arg0: $StatePropertiesPredicate$Type, arg1: $FriendlyByteBuf$Type): void
public static "findMatcher"(arg0: $StatePropertiesPredicate$Type, arg1: string): $StatePropertiesPredicate$PropertyMatcher
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertiesPredicateHelper$Type = ($PropertiesPredicateHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertiesPredicateHelper_ = $PropertiesPredicateHelper$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$ItemShapelessRecipeCategory" {
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ItemShapelessRecipe, $ItemShapelessRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe"
import {$BaseJEICategory, $BaseJEICategory$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $ItemShapelessRecipeCategory<T extends $ItemShapelessRecipe<(T)>> extends $BaseJEICategory<($ItemShapelessContext), (T)> {
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($ItemShapelessContext$Type), (T)>)

public "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessRecipeCategory$Type<T> = ($ItemShapelessRecipeCategory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessRecipeCategory_<T> = $ItemShapelessRecipeCategory$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/contextual/$Chance" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $Chance extends $Record implements $ContextualCondition {

constructor(chance: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public "chance"(): float
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Chance$Type = ($Chance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Chance_ = $Chance$Type;
}}
declare module "packages/snownee/lychee/random_block_ticking/$RandomlyTickable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RandomlyTickable {

 "lychee$setTickable"(arg0: boolean): void
 "lychee$isTickable"(): boolean
}

export namespace $RandomlyTickable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomlyTickable$Type = ($RandomlyTickable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomlyTickable_ = $RandomlyTickable$Type;
}}
declare module "packages/snownee/lychee/core/input/$ItemHolderCollection" {
import {$ItemHolder, $ItemHolder$Type} from "packages/snownee/lychee/core/input/$ItemHolder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"

export class $ItemHolderCollection {
static readonly "EMPTY": $ItemHolderCollection
readonly "tempList": $List<($ItemStack)>
readonly "ignoreConsumptionFlags": $BitSet

constructor(...arg0: ($ItemHolder$Type)[])

public "get"(arg0: integer): $ItemHolder
public "replace"(arg0: integer, arg1: $ItemStack$Type): $ItemHolder
public "size"(): integer
public "split"(arg0: integer, arg1: integer): $ItemHolder
public "postApply"(arg0: boolean, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemHolderCollection$Type = ($ItemHolderCollection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemHolderCollection_ = $ItemHolderCollection$Type;
}}
declare module "packages/snownee/lychee/lightning_channeling/$LightningChannelingRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LightningBolt, $LightningBolt$Type} from "packages/net/minecraft/world/entity/$LightningBolt"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ItemShapelessRecipe, $ItemShapelessRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LightningChannelingRecipe extends $ItemShapelessRecipe<($LightningChannelingRecipe)> {
static readonly "MAX_INGREDIENTS": integer
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public static "on"(arg0: $LightningBolt$Type, arg1: $List$Type<($Entity$Type)>): void
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "serializer"(): $LycheeRecipe$Serializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightningChannelingRecipe$Type = ($LightningChannelingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightningChannelingRecipe_ = $LightningChannelingRecipe$Type;
}}
declare module "packages/snownee/lychee/item_inside/$ItemInsideRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemShapelessRecipe$Serializer, $ItemShapelessRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe$Serializer"
import {$ItemInsideRecipe, $ItemInsideRecipe$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ItemInsideRecipe$Serializer extends $ItemShapelessRecipe$Serializer<($ItemInsideRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $ItemInsideRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $ItemInsideRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $ItemInsideRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInsideRecipe$Serializer$Type = ($ItemInsideRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInsideRecipe$Serializer_ = $ItemInsideRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/mixin/$StatePropertiesPredicateAccess" {
import {$StatePropertiesPredicate$PropertyMatcher, $StatePropertiesPredicate$PropertyMatcher$Type} from "packages/net/minecraft/advancements/critereon/$StatePropertiesPredicate$PropertyMatcher"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $StatePropertiesPredicateAccess {

 "getProperties"(): $List<($StatePropertiesPredicate$PropertyMatcher)>

(): $List<($StatePropertiesPredicate$PropertyMatcher)>
}

export namespace $StatePropertiesPredicateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatePropertiesPredicateAccess$Type = ($StatePropertiesPredicateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatePropertiesPredicateAccess_ = $StatePropertiesPredicateAccess$Type;
}}
declare module "packages/snownee/lychee/$LycheeLootContextParamSets" {
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"

export class $LycheeLootContextParamSets {
static readonly "ALL": $LootContextParamSet
static readonly "ITEM_BURNING": $LootContextParamSet
static readonly "ITEM_INSIDE": $LootContextParamSet
static readonly "BLOCK_INTERACTION": $LootContextParamSet
static readonly "ANVIL_CRAFTING": $LootContextParamSet
static readonly "BLOCK_CRUSHING": $LootContextParamSet
static readonly "LIGHTNING_CHANNELING": $LootContextParamSet
static readonly "ITEM_EXPLODING": $LootContextParamSet
static readonly "BLOCK_ONLY": $LootContextParamSet
static readonly "CRAFTING": $LootContextParamSet

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeLootContextParamSets$Type = ($LycheeLootContextParamSets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeLootContextParamSets_ = $LycheeLootContextParamSets$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$ItemAndBlockBaseCategory" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BaseJEICategory, $BaseJEICategory$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $ItemAndBlockBaseCategory<C extends $LycheeContext, T extends $LycheeRecipe<(C)>> extends $BaseJEICategory<(C), (T)> {
 "inputBlockRect": $Rect2i
 "methodRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $List$Type<($LycheeRecipeType$Type<(C), (T)>)>, arg1: $ScreenElement$Type)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<(T)>): $IDrawable
public "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIconBlock"(arg0: $List$Type<(T)>): $BlockState
public "getInputBlock"(arg0: T): $BlockPredicate
public "drawExtra"(arg0: T, arg1: $GuiGraphics$Type, arg2: double, arg3: double, arg4: integer): void
public "getRenderingBlock"(arg0: T): $BlockState
public "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
public "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getMethodDescription"(arg0: T): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAndBlockBaseCategory$Type<C, T> = ($ItemAndBlockBaseCategory<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAndBlockBaseCategory_<C, T> = $ItemAndBlockBaseCategory$Type<(C), (T)>;
}}
declare module "packages/snownee/lychee/client/gui/$ILightingSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ILightingSettings {

 "applyLighting"(): void

(): void
}

export namespace $ILightingSettings {
const DEFAULT_3D: $ILightingSettings
const DEFAULT_FLAT: $ILightingSettings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILightingSettings$Type = ($ILightingSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILightingSettings_ = $ILightingSettings$Type;
}}
declare module "packages/snownee/lychee/item_inside/$ItemInsideRecipeType$Cache" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemInsideRecipe, $ItemInsideRecipe$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipe"

export class $ItemInsideRecipeType$Cache extends $Record {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "recipe"(): $ItemInsideRecipe
public "ingredients"(): $List<($Set<($Item)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInsideRecipeType$Cache$Type = ($ItemInsideRecipeType$Cache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInsideRecipeType$Cache_ = $ItemInsideRecipeType$Cache$Type;
}}
declare module "packages/snownee/lychee/util/$GsonContextImpl" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$JsonSerializationContext, $JsonSerializationContext$Type} from "packages/com/google/gson/$JsonSerializationContext"
import {$JsonDeserializationContext, $JsonDeserializationContext$Type} from "packages/com/google/gson/$JsonDeserializationContext"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

export class $GsonContextImpl implements $JsonSerializationContext, $JsonDeserializationContext {

constructor(arg0: $Gson$Type)

public "deserialize"<R>(arg0: $JsonElement$Type, arg1: $Type$Type): R
public "serialize"(arg0: any): $JsonElement
public "serialize"(arg0: any, arg1: $Type$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GsonContextImpl$Type = ($GsonContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GsonContextImpl_ = $GsonContextImpl$Type;
}}
declare module "packages/snownee/lychee/mixin/$TransientCraftingContainerAccess" {
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $TransientCraftingContainerAccess {

 "getMenu"(): $AbstractContainerMenu

(): $AbstractContainerMenu
}

export namespace $TransientCraftingContainerAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransientCraftingContainerAccess$Type = ($TransientCraftingContainerAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransientCraftingContainerAccess_ = $TransientCraftingContainerAccess$Type;
}}
declare module "packages/snownee/lychee/compat/kubejs/$ClickedInfoBadgeEventJS" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ClientEventJS, $ClientEventJS$Type} from "packages/dev/latvian/mods/kubejs/client/$ClientEventJS"

export class $ClickedInfoBadgeEventJS extends $ClientEventJS {
readonly "recipe": $ILycheeRecipe<(any)>
readonly "button": integer

constructor(arg0: $ILycheeRecipe$Type<(any)>, arg1: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClickedInfoBadgeEventJS$Type = ($ClickedInfoBadgeEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClickedInfoBadgeEventJS_ = $ClickedInfoBadgeEventJS$Type;
}}
declare module "packages/snownee/lychee/mixin/$LocationPredicateAccess" {
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$FluidPredicate, $FluidPredicate$Type} from "packages/net/minecraft/advancements/critereon/$FluidPredicate"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$LightPredicate, $LightPredicate$Type} from "packages/net/minecraft/advancements/critereon/$LightPredicate"

export interface $LocationPredicateAccess {

 "getBlock"(): $BlockPredicate
 "getY"(): $MinMaxBounds$Doubles
 "getLight"(): $LightPredicate
 "getDimension"(): $ResourceKey<($Level)>
 "getBiome"(): $ResourceKey<($Biome)>
 "getZ"(): $MinMaxBounds$Doubles
 "getX"(): $MinMaxBounds$Doubles
 "getStructure"(): $ResourceKey<($Structure)>
 "getSmokey"(): boolean
 "getFluid"(): $FluidPredicate
}

export namespace $LocationPredicateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationPredicateAccess$Type = ($LocationPredicateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationPredicateAccess_ = $LocationPredicateAccess$Type;
}}
declare module "packages/snownee/lychee/compat/jei/$SideBlockIcon" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RenderElement, $RenderElement$Type} from "packages/snownee/lychee/client/gui/$RenderElement"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $SideBlockIcon extends $RenderElement {
static readonly "EMPTY": $RenderElement

constructor(arg0: $ScreenElement$Type, arg1: $Supplier$Type<($BlockState$Type)>)

public "render"(arg0: $GuiGraphics$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SideBlockIcon$Type = ($SideBlockIcon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SideBlockIcon_ = $SideBlockIcon$Type;
}}
declare module "packages/snownee/lychee/mixin/$TimeCheckAccess" {
import {$IntRange, $IntRange$Type} from "packages/net/minecraft/world/level/storage/loot/$IntRange"

export interface $TimeCheckAccess {

 "getValue"(): $IntRange
 "getPeriod"(): long
}

export namespace $TimeCheckAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeCheckAccess$Type = ($TimeCheckAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeCheckAccess_ = $TimeCheckAccess$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$CheckParam" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $CheckParam extends $Record implements $ContextualCondition {

constructor(key: string, value: any)

public "value"(): any
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "key"(): string
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckParam$Type = ($CheckParam);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckParam_ = $CheckParam$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$And" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $And extends $ContextualHolder implements $ContextualCondition {

constructor()

public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "getDescription"(arg0: boolean): $MutableComponent
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $And$Type = ($And);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $And_ = $And$Type;
}}
declare module "packages/snownee/lychee/compat/jei/ingredient/$PostActionIngredientHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $PostActionIngredientHelper implements $IIngredientHelper<($PostAction)> {

constructor()

public "getDisplayName"(arg0: $PostAction$Type): string
public "getDisplayModId"(arg0: $PostAction$Type): string
public "copyIngredient"(arg0: $PostAction$Type): $PostAction
public "getErrorInfo"(arg0: $PostAction$Type): string
public "getIngredientType"(): $IIngredientType<($PostAction)>
public "getUniqueId"(arg0: $PostAction$Type, arg1: $UidContext$Type): string
public "getResourceLocation"(arg0: $PostAction$Type): $ResourceLocation
public "getWildcardId"(arg0: $PostAction$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($PostAction$Type)>): $Optional<($ResourceLocation)>
public "getTagStream"(arg0: $PostAction$Type): $Stream<($ResourceLocation)>
public "getCheatItemStack"(arg0: $PostAction$Type): $ItemStack
public "isValidIngredient"(arg0: $PostAction$Type): boolean
public "normalizeIngredient"(arg0: $PostAction$Type): $PostAction
public "isIngredientOnServer"(arg0: $PostAction$Type): boolean
public "getColors"(arg0: $PostAction$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($PostAction)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostActionIngredientHelper$Type = ($PostActionIngredientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostActionIngredientHelper_ = $PostActionIngredientHelper$Type;
}}
declare module "packages/snownee/lychee/util/$CommonProxy$CustomActionListener" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$CustomAction, $CustomAction$Type} from "packages/snownee/lychee/core/post/$CustomAction"

export interface $CommonProxy$CustomActionListener {

 "on"(arg0: string, arg1: $CustomAction$Type, arg2: $ILycheeRecipe$Type<(any)>, arg3: $ILycheeRecipe$NBTPatchContext$Type): boolean

(arg0: string, arg1: $CustomAction$Type, arg2: $ILycheeRecipe$Type<(any)>, arg3: $ILycheeRecipe$NBTPatchContext$Type): boolean
}

export namespace $CommonProxy$CustomActionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonProxy$CustomActionListener$Type = ($CommonProxy$CustomActionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonProxy$CustomActionListener_ = $CommonProxy$CustomActionListener$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipeMod" {
import {$DripParticleHandler, $DripParticleHandler$Type} from "packages/snownee/lychee/dripstone_dripping/$DripParticleHandler"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockParticleOption, $BlockParticleOption$Type} from "packages/net/minecraft/core/particles/$BlockParticleOption"

export class $DripstoneRecipeMod {
static readonly "particleHandlers": $Cache<($Block), ($DripParticleHandler)>
static readonly "DRIPSTONE_DRIPPING": $ParticleType<($BlockParticleOption)>
static readonly "DRIPSTONE_FALLING": $ParticleType<($BlockParticleOption)>
static readonly "DRIPSTONE_SPLASH": $ParticleType<($BlockParticleOption)>

constructor()

public static "spawnDripParticle"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
public static "getParticleHandler"(arg0: $Level$Type, arg1: $BlockState$Type): $DripParticleHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneRecipeMod$Type = ($DripstoneRecipeMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneRecipeMod_ = $DripstoneRecipeMod$Type;
}}
declare module "packages/snownee/lychee/item_inside/$ItemInsideRecipeType" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$ItemInsideRecipe, $ItemInsideRecipe$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ItemInsideRecipeType extends $LycheeRecipeType<($ItemShapelessContext), ($ItemInsideRecipe)> {
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<($ItemInsideRecipe$Type)>, arg2: $LootContextParamSet$Type)

public "process"(arg0: $Entity$Type, arg1: $ItemStack$Type, arg2: $BlockPos$Type, arg3: $Vec3$Type): void
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInsideRecipeType$Type = ($ItemInsideRecipeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInsideRecipeType_ = $ItemInsideRecipeType$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $LycheeRecipe$Serializer<R extends $LycheeRecipe<(any)>> implements $RecipeSerializer<(R)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor(arg0: $Function$Type<($ResourceLocation$Type), (R)>)

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): R
public "fromJson"(arg0: R, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): R
public "fromNetwork"(arg0: R, arg1: $FriendlyByteBuf$Type): void
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: R): void
public static "parseIngredientOrAir"(arg0: $JsonElement$Type): $Ingredient
public static "actionsFromNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $Consumer$Type<($PostAction$Type)>): void
public static "actionsToNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $List$Type<($PostAction$Type)>): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: R): void
public "getRegistryName"(): $ResourceLocation
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): R
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeRecipe$Serializer$Type<R> = ($LycheeRecipe$Serializer<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeRecipe$Serializer_<R> = $LycheeRecipe$Serializer$Type<(R)>;
}}
declare module "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemShapelessRecipe, $ItemShapelessRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ItemShapelessRecipe$Serializer<T extends $ItemShapelessRecipe<(T)>> extends $LycheeRecipe$Serializer<(T)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor(arg0: $Function$Type<($ResourceLocation$Type), (T)>)

public "fromJson"(arg0: T, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: T, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: T): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessRecipe$Serializer$Type<T> = ($ItemShapelessRecipe$Serializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessRecipe$Serializer_<T> = $ItemShapelessRecipe$Serializer$Type<(T)>;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripstoneContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $DripstoneContext extends $LycheeContext {
readonly "source": $BlockState
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneContext$Type = ($DripstoneContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneContext_ = $DripstoneContext$Type;
}}
declare module "packages/snownee/lychee/compat/$IngredientInfo" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $IngredientInfo {
readonly "ingredient": $Ingredient
 "tooltips": $List<($Component)>
 "count": integer
 "isCatalyst": boolean

constructor(arg0: $Ingredient$Type)

public "addTooltip"(arg0: $Component$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientInfo$Type = ($IngredientInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientInfo_ = $IngredientInfo$Type;
}}
declare module "packages/snownee/lychee/interaction/$InteractionRecipeMod" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $InteractionRecipeMod {

constructor()

public static "useItemOn"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $InteractionResult
public static "clickItemOn"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockPos$Type, arg4: $Direction$Type): $InteractionResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionRecipeMod$Type = ($InteractionRecipeMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionRecipeMod_ = $InteractionRecipeMod$Type;
}}
declare module "packages/snownee/lychee/core/def/$IntBoundsHelper" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$IntRangeAccess, $IntRangeAccess$Type} from "packages/snownee/lychee/mixin/$IntRangeAccess"
import {$IntRange, $IntRange$Type} from "packages/net/minecraft/world/level/storage/loot/$IntRange"

export class $IntBoundsHelper {
static readonly "ONE": $MinMaxBounds$Ints

constructor()

public static "random"(arg0: $MinMaxBounds$Ints$Type, arg1: $RandomSource$Type): integer
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $MinMaxBounds$Ints
public static "toNetwork"(arg0: $MinMaxBounds$Ints$Type, arg1: $FriendlyByteBuf$Type): void
public static "toIntRange"(arg0: $MinMaxBounds$Ints$Type): $IntRange
public static "fromIntRange"(arg0: $IntRangeAccess$Type): $MinMaxBounds$Ints
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntBoundsHelper$Type = ($IntBoundsHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntBoundsHelper_ = $IntBoundsHelper$Type;
}}
declare module "packages/snownee/lychee/client/gui/$ScreenElement" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ScreenElement {

 "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void

(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}

export namespace $ScreenElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenElement$Type = ($ScreenElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenElement_ = $ScreenElement$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$Time" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $Time extends $Record implements $ContextualCondition {

constructor(value: $MinMaxBounds$Ints$Type, period: long)

public "value"(): $MinMaxBounds$Ints
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "test"(arg0: $LevelAccessor$Type): boolean
public "getType"(): $ContextualConditionType<(any)>
public "period"(): long
public "getDescription"(arg0: boolean): $MutableComponent
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Time$Type = ($Time);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Time_ = $Time$Type;
}}
declare module "packages/snownee/lychee/block_exploding/$BlockExplodingContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $BlockExplodingContext extends $LycheeContext {
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockExplodingContext$Type = ($BlockExplodingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockExplodingContext_ = $BlockExplodingContext$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ILycheeRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ILycheeRecipe<C extends $LycheeContext> {

 "getComment"(): string
 "showInRecipeViewer"(): boolean
 "getContextualHolder"(): $ContextualHolder
 "isActionPath"(arg0: $JsonPointer$Type): boolean
 "getPostActions"(): $Stream<($PostAction)>
 "getActionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
 "getItemIndexes"(arg0: $JsonPointer$Type): $IntList
 "getItemIndexes"(arg0: $Reference$Type): $IntList
 "defaultItemPointer"(): $JsonPointer
 "lychee$getId"(): $ResourceLocation
 "showingActionsCount"(): integer
 "applyPostActions"(arg0: $LycheeContext$Type, arg1: integer): void
 "getBlockInputs"(): $List<($BlockPredicate)>
 "getBlockOutputs"(): $List<($BlockPredicate)>
 "getAllActions"(): $Stream<($PostAction)>
}

export namespace $ILycheeRecipe {
const ITEM_IN: $JsonPointer
const ITEM_OUT: $JsonPointer
const RESULT: $JsonPointer
const POST: $JsonPointer
const patchContexts: $Map<($ResourceLocation), ($ILycheeRecipe$NBTPatchContext)>
function processActionGroup(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
function processActions(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
function filterHidden(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILycheeRecipe$Type<C> = ($ILycheeRecipe<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILycheeRecipe_<C> = $ILycheeRecipe$Type<(C)>;
}}
declare module "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ChanceRecipe, $ChanceRecipe$Type} from "packages/snownee/lychee/core/recipe/$ChanceRecipe"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $RandomBlockTickingRecipe extends $LycheeRecipe<($LycheeContext)> implements $BlockKeyRecipe<($RandomBlockTickingRecipe)>, $ChanceRecipe {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $RandomBlockTickingRecipe$Type): integer
public "matches"(arg0: $LycheeContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getBlock"(): $BlockPredicate
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "getChance"(): float
public "setChance"(arg0: float): void
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "block"(): $BlockPredicate
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "chance"(): float
set "chance"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomBlockTickingRecipe$Type = ($RandomBlockTickingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomBlockTickingRecipe_ = $RandomBlockTickingRecipe$Type;
}}
declare module "packages/snownee/lychee/mixin/$IntsAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntsAccess {

}

export namespace $IntsAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntsAccess$Type = ($IntsAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntsAccess_ = $IntsAccess$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/client/$DripstoneSplashParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$DripParticle, $DripParticle$Type} from "packages/net/minecraft/client/particle/$DripParticle"

export class $DripstoneSplashParticle extends $DripParticle {
 "isGlowing": boolean
 "x": double
 "y": double
 "z": double
 "xd": double
 "yd": double
 "zd": double
 "age": integer
 "rCol": float
 "gCol": float
 "bCol": float

constructor(arg0: $ClientLevel$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: $Fluid$Type)

public "tick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneSplashParticle$Type = ($DripstoneSplashParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneSplashParticle_ = $DripstoneSplashParticle$Type;
}}
declare module "packages/snownee/lychee/interaction/$BlockClickingRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$BlockInteractingRecipe, $BlockInteractingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockInteractingRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $BlockClickingRecipe extends $BlockInteractingRecipe {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "getType"(): $LycheeRecipeType<(any), (any)>
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "serializer"(): $LycheeRecipe$Serializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockClickingRecipe$Type = ($BlockClickingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockClickingRecipe_ = $BlockClickingRecipe$Type;
}}
declare module "packages/snownee/lychee/core/post/input/$NBTPatch" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$JsonPatch, $JsonPatch$Type} from "packages/snownee/lychee/util/json/$JsonPatch"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $NBTPatch extends $PostAction {
 "path": string

constructor(arg0: $JsonPatch$Type)

public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "getUsedPointers"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $Consumer$Type<($JsonPointer$Type)>): void
public "preApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: $ILycheeRecipe$NBTPatchContext$Type): void
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTPatch$Type = ($NBTPatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTPatch_ = $NBTPatch$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$ItemExplodingRecipeCategory" {
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ItemExplodingRecipe, $ItemExplodingRecipe$Type} from "packages/snownee/lychee/item_exploding/$ItemExplodingRecipe"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$ItemShapelessRecipeCategory, $ItemShapelessRecipeCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemShapelessRecipeCategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"

export class $ItemExplodingRecipeCategory extends $ItemShapelessRecipeCategory<($ItemExplodingRecipe)> {
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($ItemShapelessContext$Type), ($ItemExplodingRecipe$Type)>)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<($ItemExplodingRecipe$Type)>): $IDrawable
public "draw"(arg0: $ItemExplodingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemExplodingRecipeCategory$Type = ($ItemExplodingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemExplodingRecipeCategory_ = $ItemExplodingRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/core/$ActionRuntime$State" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ActionRuntime$State extends $Enum<($ActionRuntime$State)> {
static readonly "RUNNING": $ActionRuntime$State
static readonly "PAUSED": $ActionRuntime$State
static readonly "STOPPED": $ActionRuntime$State


public static "values"(): ($ActionRuntime$State)[]
public static "valueOf"(arg0: string): $ActionRuntime$State
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionRuntime$State$Type = (("running") | ("paused") | ("stopped")) | ($ActionRuntime$State);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionRuntime$State_ = $ActionRuntime$State$Type;
}}
declare module "packages/snownee/lychee/interaction/$BlockInteractingRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ItemAndBlockRecipe, $ItemAndBlockRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemAndBlockRecipe"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $BlockInteractingRecipe extends $ItemAndBlockRecipe<($LycheeContext)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "matches"(arg0: $LycheeContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "ingredients"(): $NonNullList<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInteractingRecipe$Type = ($BlockInteractingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInteractingRecipe_ = $BlockInteractingRecipe$Type;
}}
declare module "packages/snownee/lychee/client/gui/$RenderTypes" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard, $RenderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $RenderTypes extends $RenderStateShard {
static readonly "VIEW_SCALE_Z_EPSILON": float
static readonly "MAX_ENCHANTMENT_GLINT_SPEED_MILLIS": double
readonly "name": string
 "setupState": $Runnable
static readonly "NO_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "ADDITIVE_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "LIGHTNING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "GLINT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "CRUMBLING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "TRANSLUCENT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NO_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_MIPPED_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_BEACON_BEAM_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_DECAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_NO_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SHADOW_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_ALPHA_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_EYES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENERGY_SWIRL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LEASH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_WATER_MASK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LIGHTNING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRIPWIRE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_PORTAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_GATEWAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LINES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "BLOCK_SHEET_MIPPED": $RenderStateShard$TextureStateShard
static readonly "BLOCK_SHEET": $RenderStateShard$TextureStateShard
static readonly "NO_TEXTURE": $RenderStateShard$EmptyTextureStateShard
static readonly "DEFAULT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "ENTITY_GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "NO_LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "CULL": $RenderStateShard$CullStateShard
static readonly "NO_CULL": $RenderStateShard$CullStateShard
static readonly "NO_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "EQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "LEQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "GREATER_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "COLOR_DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "COLOR_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "NO_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "POLYGON_OFFSET_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "VIEW_OFFSET_Z_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "MAIN_TARGET": $RenderStateShard$OutputStateShard
static readonly "OUTLINE_TARGET": $RenderStateShard$OutputStateShard
static readonly "TRANSLUCENT_TARGET": $RenderStateShard$OutputStateShard
static readonly "PARTICLES_TARGET": $RenderStateShard$OutputStateShard
static readonly "WEATHER_TARGET": $RenderStateShard$OutputStateShard
static readonly "CLOUDS_TARGET": $RenderStateShard$OutputStateShard
static readonly "ITEM_ENTITY_TARGET": $RenderStateShard$OutputStateShard
static readonly "DEFAULT_LINE": $RenderStateShard$LineStateShard
static readonly "NO_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard
static readonly "OR_REVERSE_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard


public static "getFluid"(): $RenderType
get "fluid"(): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypes$Type = ($RenderTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypes_ = $RenderTypes$Type;
}}
declare module "packages/snownee/lychee/$RecipeSerializers" {
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$DripstoneRecipe, $DripstoneRecipe$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe"
import {$ShapedCraftingRecipe, $ShapedCraftingRecipe$Type} from "packages/snownee/lychee/crafting/$ShapedCraftingRecipe"
import {$LightningChannelingRecipe, $LightningChannelingRecipe$Type} from "packages/snownee/lychee/lightning_channeling/$LightningChannelingRecipe"
import {$ItemExplodingRecipe, $ItemExplodingRecipe$Type} from "packages/snownee/lychee/item_exploding/$ItemExplodingRecipe"
import {$AnvilCraftingRecipe, $AnvilCraftingRecipe$Type} from "packages/snownee/lychee/anvil_crafting/$AnvilCraftingRecipe"
import {$BlockCrushingRecipe, $BlockCrushingRecipe$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe"
import {$RandomBlockTickingRecipe, $RandomBlockTickingRecipe$Type} from "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipe"
import {$ItemInsideRecipe, $ItemInsideRecipe$Type} from "packages/snownee/lychee/item_inside/$ItemInsideRecipe"
import {$BlockInteractingRecipe, $BlockInteractingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockInteractingRecipe"
import {$ItemBurningRecipe, $ItemBurningRecipe$Type} from "packages/snownee/lychee/item_burning/$ItemBurningRecipe"
import {$BlockClickingRecipe, $BlockClickingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockClickingRecipe"
import {$BlockExplodingRecipe, $BlockExplodingRecipe$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe"

export class $RecipeSerializers {
static readonly "ITEM_BURNING": $LycheeRecipe$Serializer<($ItemBurningRecipe)>
static readonly "ITEM_INSIDE": $LycheeRecipe$Serializer<($ItemInsideRecipe)>
static readonly "BLOCK_INTERACTING": $LycheeRecipe$Serializer<($BlockInteractingRecipe)>
static readonly "BLOCK_CLICKING": $LycheeRecipe$Serializer<($BlockClickingRecipe)>
static readonly "ANVIL_CRAFTING": $LycheeRecipe$Serializer<($AnvilCraftingRecipe)>
static readonly "BLOCK_CRUSHING": $LycheeRecipe$Serializer<($BlockCrushingRecipe)>
static readonly "LIGHTNING_CHANNELING": $LycheeRecipe$Serializer<($LightningChannelingRecipe)>
static readonly "ITEM_EXPLODING": $LycheeRecipe$Serializer<($ItemExplodingRecipe)>
static readonly "BLOCK_EXPLODING": $LycheeRecipe$Serializer<($BlockExplodingRecipe)>
static readonly "RANDOM_BLOCK_TICKING": $LycheeRecipe$Serializer<($RandomBlockTickingRecipe)>
static readonly "DRIPSTONE_DRIPPING": $LycheeRecipe$Serializer<($DripstoneRecipe)>
static readonly "CRAFTING": $RecipeSerializer<($ShapedCraftingRecipe)>

constructor()

public static "register"<T extends $RecipeSerializer<(any)>>(arg0: string, arg1: T): T
public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSerializers$Type = ($RecipeSerializers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSerializers_ = $RecipeSerializers$Type;
}}
declare module "packages/snownee/lychee/mixin/$LocationCheckAccess" {
import {$LocationPredicate, $LocationPredicate$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $LocationCheckAccess {

 "getOffset"(): $BlockPos
 "getPredicate"(): $LocationPredicate
}

export namespace $LocationCheckAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationCheckAccess$Type = ($LocationCheckAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationCheckAccess_ = $LocationCheckAccess$Type;
}}
declare module "packages/snownee/lychee/anvil_crafting/$AnvilCraftingRecipe" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$AnvilContext, $AnvilContext$Type} from "packages/snownee/lychee/anvil_crafting/$AnvilContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnvilCraftingRecipe extends $LycheeRecipe<($AnvilContext)> implements $Comparable<($AnvilCraftingRecipe)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $AnvilCraftingRecipe$Type): integer
public "matches"(arg0: $AnvilContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getResultItem"(): $ItemStack
public "getMaterialCost"(): integer
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getRight"(): $Ingredient
public "getLeft"(): $Ingredient
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "addAssemblingAction"(arg0: $PostAction$Type): void
public "getIngredients"(): $NonNullList<($Ingredient)>
public "isActionPath"(arg0: $JsonPointer$Type): boolean
public "getActionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
public "getItemIndexes"(arg0: $JsonPointer$Type): $IntList
public "defaultItemPointer"(): $JsonPointer
public "assemble"(arg0: $AnvilContext$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getAllActions"(): $Stream<($PostAction)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "resultItem"(): $ItemStack
get "materialCost"(): integer
get "right"(): $Ingredient
get "left"(): $Ingredient
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "ingredients"(): $NonNullList<($Ingredient)>
get "actionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
get "allActions"(): $Stream<($PostAction)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilCraftingRecipe$Type = ($AnvilCraftingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilCraftingRecipe_ = $AnvilCraftingRecipe$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext" {
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$IntCollection, $IntCollection$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntCollection"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Object2IntMap, $Object2IntMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2IntMap"

export class $ILycheeRecipe$NBTPatchContext extends $Record {

constructor(template: $JsonObject$Type, usedIndexes: $IntCollection$Type, splits: $Object2IntMap$Type<($JsonPointer$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "template"(): $JsonObject
public "countTargets"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type): integer
public "countTargets"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $Reference$Type): integer
public "usedIndexes"(): $IntCollection
public "splits"(): $Object2IntMap<($JsonPointer)>
public "convertPath"(arg0: $JsonPointer$Type, arg1: $BiFunction$Type<(string), (string), (string)>): $JsonPointer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILycheeRecipe$NBTPatchContext$Type = ($ILycheeRecipe$NBTPatchContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILycheeRecipe$NBTPatchContext_ = $ILycheeRecipe$NBTPatchContext$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$LycheeRecipe" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LycheeRecipe<C extends $LycheeContext> extends $ContextualHolder implements $ILycheeRecipe<(C)>, $Recipe<(C)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "getType"(): $LycheeRecipeType<(any), (any)>
public "getComment"(): string
public "showInRecipeViewer"(): boolean
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "getContextualHolder"(): $ContextualHolder
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getPostActions"(): $Stream<($PostAction)>
public "getActionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
public "getItemIndexes"(arg0: $JsonPointer$Type): $IntList
public "assemble"(arg0: C, arg1: $RegistryAccess$Type): $ItemStack
public "addPostAction"(arg0: $PostAction$Type): void
public "getId"(): $ResourceLocation
public "getRandomRepeats"(arg0: integer, arg1: C): integer
public "tickOrApply"(arg0: C): boolean
public "getMaxRepeats"(): $MinMaxBounds$Ints
public "isActionPath"(arg0: $JsonPointer$Type): boolean
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public "getItemIndexes"(arg0: $Reference$Type): $IntList
public "defaultItemPointer"(): $JsonPointer
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public "lychee$getId"(): $ResourceLocation
public "showingActionsCount"(): integer
public "applyPostActions"(arg0: $LycheeContext$Type, arg1: integer): void
public "getBlockInputs"(): $List<($BlockPredicate)>
public "getBlockOutputs"(): $List<($BlockPredicate)>
public "getAllActions"(): $Stream<($PostAction)>
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
public "getRemainingItems"(arg0: C): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "showNotification"(): boolean
public "matches"(arg0: C, arg1: $Level$Type): boolean
public "isSpecial"(): boolean
public "getType"(): $ResourceLocation
public "replaceOutput"(match: $ReplacementMatch$Type, arg1: $OutputReplacement$Type): boolean
public "setGroup"(group: string): void
public "hasInput"(match: $ReplacementMatch$Type): boolean
public "getOrCreateId"(): $ResourceLocation
public "getSchema"(): $RecipeSchema
public "replaceInput"(match: $ReplacementMatch$Type, arg1: $InputReplacement$Type): boolean
public "hasOutput"(match: $ReplacementMatch$Type): boolean
public "getGroup"(): string
public "getMod"(): string
get "type"(): $LycheeRecipeType<(any), (any)>
get "comment"(): string
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "contextualHolder"(): $ContextualHolder
get "postActions"(): $Stream<($PostAction)>
get "actionGroups"(): $Map<($JsonPointer), ($List<($PostAction)>)>
get "id"(): $ResourceLocation
get "maxRepeats"(): $MinMaxBounds$Ints
get "blockInputs"(): $List<($BlockPredicate)>
get "blockOutputs"(): $List<($BlockPredicate)>
get "allActions"(): $Stream<($PostAction)>
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "special"(): boolean
get "type"(): $ResourceLocation
set "group"(value: string)
get "orCreateId"(): $ResourceLocation
get "schema"(): $RecipeSchema
get "group"(): string
get "mod"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeRecipe$Type<C> = ($LycheeRecipe<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeRecipe_<C> = $LycheeRecipe$Type<(C)>;
}}
declare module "packages/snownee/lychee/core/post/$MoveTowardsFace" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $MoveTowardsFace extends $PostAction {
readonly "factor": float
 "path": string

constructor(arg0: float)

public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoveTowardsFace$Type = ($MoveTowardsFace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoveTowardsFace_ = $MoveTowardsFace$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$BlockInteractionRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"
import {$BlockKeyRecipeType, $BlockKeyRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType"
import {$BlockInteractingRecipe, $BlockInteractingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockInteractingRecipe"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$ItemAndBlockBaseCategory, $ItemAndBlockBaseCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemAndBlockBaseCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $BlockInteractionRecipeCategory extends $ItemAndBlockBaseCategory<($LycheeContext), ($BlockInteractingRecipe)> {
 "inputBlockRect": $Rect2i
 "methodRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $List$Type<($BlockKeyRecipeType$Type<($LycheeContext$Type), ($BlockInteractingRecipe$Type)>)>, arg1: $ScreenElement$Type)

public "getWidth"(): integer
public "drawExtra"(arg0: $BlockInteractingRecipe$Type, arg1: $GuiGraphics$Type, arg2: double, arg3: double, arg4: integer): void
public "getMethodDescription"(arg0: $BlockInteractingRecipe$Type): $Component
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInteractionRecipeCategory$Type = ($BlockInteractionRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInteractionRecipeCategory_ = $BlockInteractionRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/compat/rei/display/$DisplayRecipeProvider" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"

export interface $DisplayRecipeProvider {

 "recipe"(): $ILycheeRecipe<(any)>

(): $ILycheeRecipe<(any)>
}

export namespace $DisplayRecipeProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayRecipeProvider$Type = ($DisplayRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayRecipeProvider_ = $DisplayRecipeProvider$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$CraftingRecipeCategoryExtension" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ICraftingCategoryExtension, $ICraftingCategoryExtension$Type} from "packages/mezz/jei/api/recipe/category/extensions/vanilla/crafting/$ICraftingCategoryExtension"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ICraftingGridHelper, $ICraftingGridHelper$Type} from "packages/mezz/jei/api/gui/ingredient/$ICraftingGridHelper"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CraftingRecipeCategoryExtension implements $ICraftingCategoryExtension {

constructor(arg0: $ILycheeRecipe$Type<(any)>)

public "getWidth"(): integer
public "getHeight"(): integer
public "drawInfo"(arg0: integer, arg1: integer, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ICraftingGridHelper$Type, arg2: $IFocusGroup$Type): void
public "getTooltipStrings"(arg0: double, arg1: double): $List<($Component)>
public "getRegistryName"(): $ResourceLocation
public "handleInput"(arg0: double, arg1: double, arg2: $InputConstants$Key$Type): boolean
get "width"(): integer
get "height"(): integer
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingRecipeCategoryExtension$Type = ($CraftingRecipeCategoryExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingRecipeCategoryExtension_ = $CraftingRecipeCategoryExtension$Type;
}}
declare module "packages/snownee/lychee/compat/$IngredientInfo$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $IngredientInfo$Type extends $Enum<($IngredientInfo$Type)> {
static readonly "NORMAL": $IngredientInfo$Type
static readonly "AIR": $IngredientInfo$Type
static readonly "ANY": $IngredientInfo$Type


public static "values"(): ($IngredientInfo$Type)[]
public static "valueOf"(arg0: string): $IngredientInfo$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientInfo$Type$Type = (("normal") | ("air") | ("any")) | ($IngredientInfo$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientInfo$Type_ = $IngredientInfo$Type$Type;
}}
declare module "packages/snownee/lychee/mixin/$ChunkMapAccess" {
import {$ChunkHolder, $ChunkHolder$Type} from "packages/net/minecraft/server/level/$ChunkHolder"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $ChunkMapAccess {

 "callGetChunks"(): $Iterable<($ChunkHolder)>

(): $Iterable<($ChunkHolder)>
}

export namespace $ChunkMapAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMapAccess$Type = ($ChunkMapAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMapAccess_ = $ChunkMapAccess$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ChanceRecipe" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ChanceRecipe {

 "getChance"(): float
 "setChance"(arg0: float): void
}

export namespace $ChanceRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChanceRecipe$Type = ($ChanceRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChanceRecipe_ = $ChanceRecipe$Type;
}}
declare module "packages/snownee/lychee/core/post/input/$SetItem" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $SetItem extends $PostAction {
readonly "stack": $ItemStack
readonly "target": $Reference
 "path": string

constructor(arg0: $ItemStack$Type, arg1: $Reference$Type)

public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "provideJsonInfo"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $JsonObject$Type): $JsonElement
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getItemOutputs"(): $List<($ItemStack)>
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetItem$Type = ($SetItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetItem_ = $SetItem$Type;
}}
declare module "packages/snownee/lychee/core/$ItemShapelessContext$Builder" {
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext$Builder, $LycheeContext$Builder$Type} from "packages/snownee/lychee/core/$LycheeContext$Builder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"

export class $ItemShapelessContext$Builder<C extends $ItemShapelessContext> extends $LycheeContext$Builder<(C)> {
readonly "itemEntities": $List<($ItemEntity)>

constructor(arg0: $Level$Type, arg1: $List$Type<($ItemEntity$Type)>)

public "create"(arg0: $LootContextParamSet$Type): C
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessContext$Builder$Type<C> = ($ItemShapelessContext$Builder<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessContext$Builder_<C> = $ItemShapelessContext$Builder$Type<(C)>;
}}
declare module "packages/snownee/lychee/client/gui/$UIRenderHelper$CustomRenderTarget" {
import {$Window, $Window$Type} from "packages/com/mojang/blaze3d/platform/$Window"
import {$RenderTarget, $RenderTarget$Type} from "packages/com/mojang/blaze3d/pipeline/$RenderTarget"

export class $UIRenderHelper$CustomRenderTarget extends $RenderTarget {
 "width": integer
 "height": integer
 "viewWidth": integer
 "viewHeight": integer
readonly "useDepth": boolean
 "frameBufferId": integer
 "filterMode": integer

constructor(arg0: boolean)

public static "create"(arg0: $Window$Type): $UIRenderHelper$CustomRenderTarget
public "renderWithAlpha"(arg0: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UIRenderHelper$CustomRenderTarget$Type = ($UIRenderHelper$CustomRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UIRenderHelper$CustomRenderTarget_ = $UIRenderHelper$CustomRenderTarget$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$DirectionCheck" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DirectionCheck implements $ContextualCondition {
static readonly "LOOKUP": $Map<(string), ($DirectionCheck)>


public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public static "create"(arg0: string, arg1: $Predicate$Type<($LycheeContext$Type)>): void
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionCheck$Type = ($DirectionCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionCheck_ = $DirectionCheck$Type;
}}
declare module "packages/snownee/lychee/mixin/$IntRangeAccess" {
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export interface $IntRangeAccess {

 "getMin"(): $NumberProvider
 "getMax"(): $NumberProvider
}

export namespace $IntRangeAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntRangeAccess$Type = ($IntRangeAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntRangeAccess_ = $IntRangeAccess$Type;
}}
declare module "packages/snownee/lychee/mixin/$ItemEntityAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ItemEntityAccess {

 "setHealth"(arg0: integer): void

(arg0: integer): void
}

export namespace $ItemEntityAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEntityAccess$Type = ($ItemEntityAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEntityAccess_ = $ItemEntityAccess$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$BlockExplodingRecipeCategory" {
import {$BlockExplodingContext, $BlockExplodingContext$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$ItemAndBlockBaseCategory, $ItemAndBlockBaseCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemAndBlockBaseCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$BlockExplodingRecipe, $BlockExplodingRecipe$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe"
import {$ScreenElement, $ScreenElement$Type} from "packages/snownee/lychee/client/gui/$ScreenElement"

export class $BlockExplodingRecipeCategory extends $ItemAndBlockBaseCategory<($BlockExplodingContext), ($BlockExplodingRecipe)> {
 "inputBlockRect": $Rect2i
 "methodRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($BlockExplodingContext$Type), ($BlockExplodingRecipe$Type)>, arg1: $ScreenElement$Type)

public "drawExtra"(arg0: $BlockExplodingRecipe$Type, arg1: $GuiGraphics$Type, arg2: double, arg3: double, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockExplodingRecipeCategory$Type = ($BlockExplodingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockExplodingRecipeCategory_ = $BlockExplodingRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/core/post/$Explode" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Explosion$BlockInteraction, $Explosion$BlockInteraction$Type} from "packages/net/minecraft/world/level/$Explosion$BlockInteraction"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $Explode extends $PostAction {
readonly "blockInteraction": $Explosion$BlockInteraction
readonly "offset": $BlockPos
readonly "fire": boolean
readonly "radius": float
readonly "step": float
 "path": string

constructor(arg0: $Explosion$BlockInteraction$Type, arg1: $BlockPos$Type, arg2: boolean, arg3: float, arg4: float)

public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Explode$Type = ($Explode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Explode_ = $Explode$Type;
}}
declare module "packages/snownee/lychee/core/post/$Execute" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $Execute extends $PostAction {
static readonly "DUMMY": $Execute
static readonly "DEFAULT_NAME": $Component
 "path": string

constructor(arg0: string, arg1: boolean, arg2: boolean)

public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Execute$Type = ($Execute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Execute_ = $Execute$Type;
}}
declare module "packages/snownee/lychee/core/post/$CycleStateProperty" {
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PlaceBlock, $PlaceBlock$Type} from "packages/snownee/lychee/core/post/$PlaceBlock"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $CycleStateProperty extends $PlaceBlock {
readonly "property": $Property<(any)>
readonly "block": $BlockPredicate
readonly "offset": $BlockPos
 "path": string

constructor(arg0: $BlockPredicate$Type, arg1: $BlockPos$Type, arg2: $Property$Type<(any)>)

public "getType"(): $PostActionType<(any)>
public static "findProperty"(arg0: $BlockPredicate$Type, arg1: string): $Property<(any)>
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CycleStateProperty$Type = ($CycleStateProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CycleStateProperty_ = $CycleStateProperty$Type;
}}
declare module "packages/snownee/lychee/core/post/$AddItemCooldown" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $AddItemCooldown extends $PostAction {
readonly "seconds": float
 "path": string

constructor(arg0: float)

public "isHidden"(): boolean
public "getType"(): $PostActionType<(any)>
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddItemCooldown$Type = ($AddItemCooldown);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddItemCooldown_ = $AddItemCooldown$Type;
}}
declare module "packages/snownee/lychee/fragment/$JsonFragment$Context" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JsonFragment$Context extends $Record {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getter"(): $Function<(string), ($JsonElement)>
public "vars"(): $Map<(string), ($JsonElement)>
public "putVars"(arg0: $Map$Type<(string), ($JsonElement$Type)>): void
public "removeVars"(arg0: $Map$Type<(string), ($JsonElement$Type)>): void
get "ter"(): $Function<(string), ($JsonElement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonFragment$Context$Type = ($JsonFragment$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonFragment$Context_ = $JsonFragment$Context$Type;
}}
declare module "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$LycheeContext$Builder, $LycheeContext$Builder$Type} from "packages/snownee/lychee/core/$LycheeContext$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Pair, $Pair$Type} from "packages/snownee/lychee/util/$Pair"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockKeyRecipeType<C extends $LycheeContext, T extends ($LycheeRecipe<(C)>) & ($BlockKeyRecipe<(any)>)> extends $LycheeRecipeType<(C), (T)> {
 "extractChance": boolean
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<(T)>, arg2: $LootContextParamSet$Type)

public "process"(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $BlockPos$Type, arg3: $Vec3$Type, arg4: $LycheeContext$Builder$Type<(C)>): $Optional<(T)>
public "process"(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $Supplier$Type<(C)>): $Pair<(C), (T)>
public "has"(arg0: $Block$Type): boolean
public "has"(arg0: $BlockState$Type): boolean
public "blockKeysToItems"(): $List<($ItemStack)>
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockKeyRecipeType$Type<C, T> = ($BlockKeyRecipeType<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockKeyRecipeType_<C, T> = $BlockKeyRecipeType$Type<(C), (T)>;
}}
declare module "packages/snownee/lychee/core/contextual/$ContextualConditionType" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ContextualConditionType<T extends $ContextualCondition> {

constructor()

public "fromJson"(arg0: $JsonObject$Type): T
public "toJson"(arg0: T, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $FriendlyByteBuf$Type): T
public "toNetwork"(arg0: T, arg1: $FriendlyByteBuf$Type): void
public "getRegistryName"(): $ResourceLocation
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextualConditionType$Type<T> = ($ContextualConditionType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextualConditionType_<T> = $ContextualConditionType$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/contextual/$Execute" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $Execute extends $Record implements $ContextualCondition {
static readonly "DEFAULT_RANGE": $MinMaxBounds$Ints
static readonly "DUMMY": $Execute

constructor(command: string, bounds: $MinMaxBounds$Ints$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "bounds"(): $MinMaxBounds$Ints
public "getType"(): $ContextualConditionType<(any)>
public "command"(): string
public "getDescription"(arg0: boolean): $MutableComponent
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Execute$Type = ($Execute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Execute_ = $Execute$Type;
}}
declare module "packages/snownee/lychee/fragment/$JsonFragment" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonFragment$Context, $JsonFragment$Context$Type} from "packages/snownee/lychee/fragment/$JsonFragment$Context"

export class $JsonFragment {

constructor()

public static "process"(arg0: $JsonElement$Type, arg1: $JsonFragment$Context$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonFragment$Type = ($JsonFragment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonFragment_ = $JsonFragment$Type;
}}
declare module "packages/snownee/lychee/util/$CommonProxy$CustomConditionListener" {
import {$CustomCondition, $CustomCondition$Type} from "packages/snownee/lychee/core/contextual/$CustomCondition"

export interface $CommonProxy$CustomConditionListener {

 "on"(arg0: string, arg1: $CustomCondition$Type): boolean

(arg0: string, arg1: $CustomCondition$Type): boolean
}

export namespace $CommonProxy$CustomConditionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonProxy$CustomConditionListener$Type = ($CommonProxy$CustomConditionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonProxy$CustomConditionListener_ = $CommonProxy$CustomConditionListener$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$BaseJEICategory" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BaseJEICategory$SlotLayoutFunction, $BaseJEICategory$SlotLayoutFunction$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory$SlotLayoutFunction"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $BaseJEICategory<C extends $LycheeContext, T extends $LycheeRecipe<(C)>> implements $IRecipeCategory<(T)> {
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<(C), (T)>)
constructor(arg0: $List$Type<($LycheeRecipeType$Type<(C), (T)>)>)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<(T)>): $IDrawable
public "getRecipeType"(): $RecipeType<(T)>
public "draw"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getIcon"(): $IDrawable
public static "addBlockIngredients"(arg0: $IRecipeLayoutBuilder$Type, arg1: $Iterable$Type<($BlockPredicate$Type)>, arg2: $RecipeIngredientRole$Type): void
public static "addBlockIngredients"(arg0: $IRecipeLayoutBuilder$Type, arg1: $LycheeRecipe$Type<(any)>): void
public "getTitle"(): $Component
public "handleInput"(arg0: T, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(arg0: T, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public static "getTooltipStrings"(arg0: $ILycheeRecipe$Type<(any)>, arg1: double, arg2: double, arg3: $Rect2i$Type): $List<($Component)>
public static "slotGroup"<T>(arg0: $IRecipeLayoutBuilder$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $List$Type<(T)>, arg5: $BaseJEICategory$SlotLayoutFunction$Type<(T)>): void
public static "actionSlot"(arg0: $IRecipeLayoutBuilder$Type, arg1: $PostAction$Type, arg2: integer, arg3: integer, arg4: integer): void
public "actionGroup"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: integer, arg3: integer): void
public "ingredientGroup"(arg0: $IRecipeLayoutBuilder$Type, arg1: T, arg2: integer, arg3: integer): void
public static "drawInfoBadge"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $GuiGraphics$Type, arg2: double, arg3: double, arg4: $Rect2i$Type): void
public "drawInfoBadge"(arg0: T, arg1: $GuiGraphics$Type, arg2: double, arg3: double): void
public static "hasInfoBadge"(arg0: $ILycheeRecipe$Type<(any)>): boolean
public "clickBlock"(arg0: $BlockState$Type, arg1: $InputConstants$Key$Type): boolean
public "getRegistryName"(arg0: T): $ResourceLocation
public "isHandled"(arg0: T): boolean
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
export type $BaseJEICategory$Type<C, T> = ($BaseJEICategory<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseJEICategory_<C, T> = $BaseJEICategory$Type<(C), (T)>;
}}
declare module "packages/snownee/lychee/core/input/$ItemHolder" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemHolder {

constructor()

public "get"(): $ItemStack
public "replace"(arg0: $ItemStack$Type, arg1: $Consumer$Type<($ItemStack$Type)>): $ItemHolder
public "split"(arg0: integer, arg1: $Consumer$Type<($ItemStack$Type)>): $ItemHolder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemHolder$Type = ($ItemHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemHolder_ = $ItemHolder$Type;
}}
declare module "packages/snownee/lychee/mixin/$NbtPredicateAccess" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export interface $NbtPredicateAccess {

 "getTag"(): $CompoundTag

(): $CompoundTag
}

export namespace $NbtPredicateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NbtPredicateAccess$Type = ($NbtPredicateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NbtPredicateAccess_ = $NbtPredicateAccess$Type;
}}
declare module "packages/snownee/lychee/client/gui/$UIRenderHelper" {
import {$UIRenderHelper$CustomRenderTarget, $UIRenderHelper$CustomRenderTarget$Type} from "packages/snownee/lychee/client/gui/$UIRenderHelper$CustomRenderTarget"
import {$Color, $Color$Type} from "packages/snownee/lychee/util/$Color"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AllGuiTextures, $AllGuiTextures$Type} from "packages/snownee/lychee/client/gui/$AllGuiTextures"
import {$Window, $Window$Type} from "packages/com/mojang/blaze3d/platform/$Window"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RenderTarget, $RenderTarget$Type} from "packages/com/mojang/blaze3d/pipeline/$RenderTarget"

export class $UIRenderHelper {
static "framebuffer": $UIRenderHelper$CustomRenderTarget

constructor()

public static "init"(): void
public static "flipForGuiRender"(arg0: $PoseStack$Type): void
public static "drawFramebuffer"(arg0: float): void
public static "drawColoredTexture"(arg0: $GuiGraphics$Type, arg1: $Color$Type, arg2: integer, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: integer, arg8: integer, arg9: integer, arg10: integer): void
public static "drawColoredTexture"(arg0: $GuiGraphics$Type, arg1: $Color$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "drawStretched"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $AllGuiTextures$Type): void
public static "swapAndBlitColor"(arg0: $RenderTarget$Type, arg1: $RenderTarget$Type): void
public static "drawCropped"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $AllGuiTextures$Type): void
public static "updateWindowSize"(arg0: $Window$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UIRenderHelper$Type = ($UIRenderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UIRenderHelper_ = $UIRenderHelper$Type;
}}
declare module "packages/snownee/lychee/core/def/$BlockPredicateHelper" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$NbtPredicate, $NbtPredicate$Type} from "packages/net/minecraft/advancements/critereon/$NbtPredicate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $BlockPredicateHelper {
static readonly "NBT_PREDICATE_DUMMY": $NbtPredicate
static "ITERABLE_PROPERTIES": $Set<($Property<(any)>)>

constructor()

public static "fromJson"(arg0: $JsonElement$Type): $BlockPredicate
public static "toJson"(arg0: $BlockPredicate$Type): $JsonElement
public static "getMatchedBlocks"(arg0: $BlockPredicate$Type): $Set<($Block)>
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $BlockPredicate
public static "toNetwork"(arg0: $BlockPredicate$Type, arg1: $FriendlyByteBuf$Type): void
public static "getMatchedItemStacks"(arg0: $BlockPredicate$Type): $List<($ItemStack)>
public static "getTooltips"(arg0: $BlockState$Type, arg1: $BlockPredicate$Type): $List<($Component)>
public static "getShowcaseBlockStates"(arg0: $BlockPredicate$Type): $List<($BlockState)>
public static "getShowcaseBlockStates"(arg0: $BlockPredicate$Type, arg1: $Collection$Type<($Property$Type<(any)>)>): $List<($BlockState)>
public static "fastMatch"(arg0: $BlockPredicate$Type, arg1: $LycheeContext$Type): boolean
public static "fastMatch"(arg0: $BlockPredicate$Type, arg1: $BlockState$Type, arg2: $Supplier$Type<($BlockEntity$Type)>): boolean
public static "anyBlockState"(arg0: $BlockPredicate$Type): $BlockState
public static "getMatchedFluids"(arg0: $BlockPredicate$Type): $Set<($Fluid)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPredicateHelper$Type = ($BlockPredicateHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPredicateHelper_ = $BlockPredicateHelper$Type;
}}
declare module "packages/snownee/lychee/core/post/$Delay" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $Delay extends $PostAction {
readonly "seconds": float
 "path": string

constructor(arg0: float)

public "getType"(): $PostActionType<(any)>
public "preventSync"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public static "makeMarker"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type): void
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Delay$Type = ($Delay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Delay_ = $Delay$Type;
}}
declare module "packages/snownee/lychee/mixin/$GameRendererAccess" {
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"

export interface $GameRendererAccess {

 "callGetFov"(arg0: $Camera$Type, arg1: float, arg2: boolean): double

(arg0: $Camera$Type, arg1: float, arg2: boolean): double
}

export namespace $GameRendererAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRendererAccess$Type = ($GameRendererAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRendererAccess_ = $GameRendererAccess$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/client/$ParticleFactories" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ParticleFactories {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleFactories$Type = ($ParticleFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleFactories_ = $ParticleFactories$Type;
}}
declare module "packages/snownee/lychee/mixin/$MixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(arg0: string, arg1: string): boolean
public "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinPlugin$Type = ($MixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinPlugin_ = $MixinPlugin$Type;
}}
declare module "packages/snownee/lychee/core/post/input/$DamageItem" {
import {$Reference, $Reference$Type} from "packages/snownee/lychee/core/$Reference"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $DamageItem extends $PostAction {
readonly "damage": integer
readonly "target": $Reference
 "path": string

constructor(arg0: integer, arg1: $Reference$Type)

public "isHidden"(): boolean
public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageItem$Type = ($DamageItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageItem_ = $DamageItem$Type;
}}
declare module "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipeType" {
import {$BlockKeyRecipeType, $BlockKeyRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RandomBlockTickingRecipe, $RandomBlockTickingRecipe$Type} from "packages/snownee/lychee/random_block_ticking/$RandomBlockTickingRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RandomBlockTickingRecipeType extends $BlockKeyRecipeType<($LycheeContext), ($RandomBlockTickingRecipe)> {
 "extractChance": boolean
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<($RandomBlockTickingRecipe$Type)>, arg2: $LootContextParamSet$Type)

public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomBlockTickingRecipeType$Type = ($RandomBlockTickingRecipeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomBlockTickingRecipeType_ = $RandomBlockTickingRecipeType$Type;
}}
declare module "packages/snownee/lychee/client/core/post/$PlaceBlockPostActionRenderer" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RandomSelect, $RandomSelect$Type} from "packages/snownee/lychee/core/post/$RandomSelect"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PostActionRenderer, $PostActionRenderer$Type} from "packages/snownee/lychee/client/core/post/$PostActionRenderer"
import {$PlaceBlock, $PlaceBlock$Type} from "packages/snownee/lychee/core/post/$PlaceBlock"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export class $PlaceBlockPostActionRenderer implements $PostActionRenderer<($PlaceBlock)> {

constructor()

public "render"(arg0: $PlaceBlock$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public static "of"<T extends $PostAction>(arg0: $PostAction$Type): $PostActionRenderer<($PlaceBlock)>
public static "register"<T extends $PostAction>(arg0: $PostActionType$Type<($PlaceBlock$Type)>, arg1: $PostActionRenderer$Type<($PlaceBlock$Type)>): void
public "loadCatalystsInfo"(arg0: $PlaceBlock$Type, arg1: $ILycheeRecipe$Type<(any)>, arg2: $List$Type<($IngredientInfo$Type)>): void
public "getBaseTooltips"(arg0: $PlaceBlock$Type): $List<($Component)>
public static "getTooltipsFromRandom"(arg0: $RandomSelect$Type, arg1: $PostAction$Type): $List<($Component)>
public "getTooltips"(arg0: $PlaceBlock$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceBlockPostActionRenderer$Type = ($PlaceBlockPostActionRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceBlockPostActionRenderer_ = $PlaceBlockPostActionRenderer$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$DripstoneRecipe, $DripstoneRecipe$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $DripstoneRecipe$Serializer extends $LycheeRecipe$Serializer<($DripstoneRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $DripstoneRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $DripstoneRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $DripstoneRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneRecipe$Serializer$Type = ($DripstoneRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneRecipe$Serializer_ = $DripstoneRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/compat/kubejs/$LycheeKubeJSPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $LycheeKubeJSPlugin extends $KubeJSPlugin {

constructor()

public "init"(): void
public "registerBindings"(arg0: $BindingsEvent$Type): void
public "registerEvents"(): void
public "registerTypeWrappers"(arg0: $ScriptType$Type, arg1: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeKubeJSPlugin$Type = ($LycheeKubeJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeKubeJSPlugin_ = $LycheeKubeJSPlugin$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$Location" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ContextualCondition, $ContextualCondition$Type} from "packages/snownee/lychee/core/contextual/$ContextualCondition"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LocationCheck, $LocationCheck$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LocationCheck"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Location$Rule, $Location$Rule$Type} from "packages/snownee/lychee/core/contextual/$Location$Rule"

export class $Location extends $Record implements $ContextualCondition {
static readonly "RULES": ($Location$Rule)[]

constructor(check: $LocationCheck$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
public "getType"(): $ContextualConditionType<(any)>
public "check"(): $LocationCheck
public "getDescription"(arg0: boolean): $MutableComponent
public "showingCount"(): integer
public "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
public "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
public "testClient"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Vec3$Type): $InteractionResult
public static "desc"(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
public static "parse"(arg0: $JsonObject$Type): $ContextualCondition
public "toJson"(): $JsonObject
public "makeDescriptionId"(arg0: boolean): string
get "type"(): $ContextualConditionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Location$Type = ($Location);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Location_ = $Location$Type;
}}
declare module "packages/snownee/lychee/mixin/$LootParamsBuilderAccess" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $LootParamsBuilderAccess {

 "getParams"(): $Map<($LootContextParam<(any)>), (any)>

(): $Map<($LootContextParam<(any)>), (any)>
}

export namespace $LootParamsBuilderAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootParamsBuilderAccess$Type = ($LootParamsBuilderAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootParamsBuilderAccess_ = $LootParamsBuilderAccess$Type;
}}
declare module "packages/snownee/lychee/item_exploding/$ItemExplodingRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ItemShapelessRecipe, $ItemShapelessRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ItemExplodingRecipe extends $ItemShapelessRecipe<($ItemExplodingRecipe)> {
static readonly "MAX_INGREDIENTS": integer
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public static "on"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: $List$Type<($Entity$Type)>, arg5: float): void
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "serializer"(): $LycheeRecipe$Serializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemExplodingRecipe$Type = ($ItemExplodingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemExplodingRecipe_ = $ItemExplodingRecipe$Type;
}}
declare module "packages/snownee/lychee/$LycheeConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LycheeConfig {
static "debug": boolean
static "enableFragment": boolean
static "dispenserFallableBlockPlacement": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeConfig$Type = ($LycheeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeConfig_ = $LycheeConfig$Type;
}}
declare module "packages/snownee/lychee/crafting/$ShapedCraftingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ShapedCraftingRecipe, $ShapedCraftingRecipe$Type} from "packages/snownee/lychee/crafting/$ShapedCraftingRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ShapedCraftingRecipe$Serializer implements $RecipeSerializer<($ShapedCraftingRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $ShapedCraftingRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $ShapedCraftingRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $ShapedCraftingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $ShapedCraftingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapedCraftingRecipe$Serializer$Type = ($ShapedCraftingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapedCraftingRecipe$Serializer_ = $ShapedCraftingRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ItemAndBlockRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemAndBlockRecipe, $ItemAndBlockRecipe$Type} from "packages/snownee/lychee/core/recipe/$ItemAndBlockRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ItemAndBlockRecipe$Serializer<T extends $ItemAndBlockRecipe<(any)>> extends $LycheeRecipe$Serializer<(T)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor(arg0: $Function$Type<($ResourceLocation$Type), (T)>)

public "fromJson"(arg0: T, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: T, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: T): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAndBlockRecipe$Serializer$Type<T> = ($ItemAndBlockRecipe$Serializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAndBlockRecipe$Serializer_<T> = $ItemAndBlockRecipe$Serializer$Type<(T)>;
}}
declare module "packages/snownee/lychee/mixin/$DoublesAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoublesAccess {

}

export namespace $DoublesAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoublesAccess$Type = ($DoublesAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoublesAccess_ = $DoublesAccess$Type;
}}
declare module "packages/snownee/lychee/compat/jei/ingredient/$PostActionIngredientRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $PostActionIngredientRenderer extends $Enum<($PostActionIngredientRenderer)> implements $IIngredientRenderer<($PostAction)> {
static readonly "INSTANCE": $PostActionIngredientRenderer


public static "values"(): ($PostActionIngredientRenderer)[]
public static "valueOf"(arg0: string): $PostActionIngredientRenderer
public "render"(arg0: $GuiGraphics$Type, arg1: $PostAction$Type): void
public "getTooltip"(arg0: $PostAction$Type, arg1: $TooltipFlag$Type): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $PostAction$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostActionIngredientRenderer$Type = (("instance")) | ($PostActionIngredientRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostActionIngredientRenderer_ = $PostActionIngredientRenderer$Type;
}}
declare module "packages/snownee/lychee/item_burning/$ItemBurningRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $ItemBurningRecipe extends $LycheeRecipe<($LycheeContext)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "matches"(arg0: $LycheeContext$Type, arg1: $Level$Type): boolean
public static "on"(arg0: $ItemEntity$Type): void
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getInput"(): $Ingredient
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "input"(): $Ingredient
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "ingredients"(): $NonNullList<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBurningRecipe$Type = ($ItemBurningRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBurningRecipe_ = $ItemBurningRecipe$Type;
}}
declare module "packages/snownee/lychee/util/$ClientProxy" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientProxy$RecipeViewerWidgetClickListener, $ClientProxy$RecipeViewerWidgetClickListener$Type} from "packages/snownee/lychee/util/$ClientProxy$RecipeViewerWidgetClickListener"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export interface $ClientProxy {

}

export namespace $ClientProxy {
const recipeViewerWidgetClickListeners: $List<($ClientProxy$RecipeViewerWidgetClickListener)>
function format(arg0: string, ...arg1: (any)[]): $MutableComponent
function init(): void
function getDimensionDisplayName(arg0: $ResourceKey$Type<($Level$Type)>): $MutableComponent
function getStructureDisplayName(arg0: $ResourceLocation$Type): $MutableComponent
function registerInfoBadgeClickListener(arg0: $ClientProxy$RecipeViewerWidgetClickListener$Type): void
function isSinglePlayer(): boolean
function postInfoBadgeClickEvent(arg0: $ILycheeRecipe$Type<(any)>, arg1: integer): boolean
function registerPostActionRenderers(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProxy$Type = ($ClientProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProxy_ = $ClientProxy$Type;
}}
declare module "packages/snownee/lychee/core/post/$Hurt" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $Hurt extends $PostAction {
readonly "damage": $MinMaxBounds$Doubles
readonly "source": $ResourceLocation
 "path": string

constructor(arg0: $MinMaxBounds$Doubles$Type, arg1: $ResourceLocation$Type)

public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Hurt$Type = ($Hurt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Hurt_ = $Hurt$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipeType" {
import {$BlockKeyRecipeType, $BlockKeyRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$DripstoneRecipe, $DripstoneRecipe$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$DripstoneContext, $DripstoneContext$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneContext"

export class $DripstoneRecipeType extends $BlockKeyRecipeType<($DripstoneContext), ($DripstoneRecipe)> {
 "extractChance": boolean
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<($DripstoneRecipe$Type)>, arg2: $LootContextParamSet$Type)

public "hasSource"(arg0: $Block$Type): boolean
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneRecipeType$Type = ($DripstoneRecipeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneRecipeType_ = $DripstoneRecipeType$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ItemShapelessRecipe" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $ItemShapelessRecipe<T extends $ItemShapelessRecipe<(T)>> extends $LycheeRecipe<($ItemShapelessContext)> implements $Comparable<(T)> {
static readonly "MAX_INGREDIENTS": integer
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: T): integer
public "matches"(arg0: $ItemShapelessContext$Type, arg1: $Level$Type): boolean
public "getIngredients"(): $NonNullList<($Ingredient)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "ingredients"(): $NonNullList<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessRecipe$Type<T> = ($ItemShapelessRecipe<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessRecipe_<T> = $ItemShapelessRecipe$Type<(T)>;
}}
declare module "packages/snownee/lychee/block_crushing/$BlockCrushingRecipeType" {
import {$BlockKeyRecipeType, $BlockKeyRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$BlockKeyRecipeType"
import {$BlockCrushingContext, $BlockCrushingContext$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingContext"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockCrushingRecipe, $BlockCrushingRecipe$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe"
import {$FallingBlockEntity, $FallingBlockEntity$Type} from "packages/net/minecraft/world/entity/item/$FallingBlockEntity"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $BlockCrushingRecipeType extends $BlockKeyRecipeType<($BlockCrushingContext), ($BlockCrushingRecipe)> {
 "extractChance": boolean
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<($BlockCrushingRecipe$Type)>, arg2: $LootContextParamSet$Type)

public "process"(arg0: $FallingBlockEntity$Type): void
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCrushingRecipeType$Type = ($BlockCrushingRecipeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCrushingRecipeType_ = $BlockCrushingRecipeType$Type;
}}
declare module "packages/snownee/lychee/util/$CommonProxy" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RegisterRecipeBookCategoriesEvent, $RegisterRecipeBookCategoriesEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterRecipeBookCategoriesEvent"
import {$ExplosionDamageCalculator, $ExplosionDamageCalculator$Type} from "packages/net/minecraft/world/level/$ExplosionDamageCalculator"
import {$CustomAction, $CustomAction$Type} from "packages/snownee/lychee/core/post/$CustomAction"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ParticleOptions$Deserializer, $ParticleOptions$Deserializer$Type} from "packages/net/minecraft/core/particles/$ParticleOptions$Deserializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockSource, $BlockSource$Type} from "packages/net/minecraft/core/$BlockSource"
import {$IngredientInfo$Type, $IngredientInfo$Type$Type} from "packages/snownee/lychee/compat/$IngredientInfo$Type"
import {$CustomCondition, $CustomCondition$Type} from "packages/snownee/lychee/core/contextual/$CustomCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$CommonProxy$CustomActionListener, $CommonProxy$CustomActionListener$Type} from "packages/snownee/lychee/util/$CommonProxy$CustomActionListener"
import {$BlockParticleOption, $BlockParticleOption$Type} from "packages/net/minecraft/core/particles/$BlockParticleOption"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$LycheeRegistries$MappedRegistry, $LycheeRegistries$MappedRegistry$Type} from "packages/snownee/lychee/$LycheeRegistries$MappedRegistry"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Explode, $Explode$Type} from "packages/snownee/lychee/core/post/$Explode"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$NewRegistryEvent, $NewRegistryEvent$Type} from "packages/net/minecraftforge/registries/$NewRegistryEvent"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$CommonProxy$CustomConditionListener, $CommonProxy$CustomConditionListener$Type} from "packages/snownee/lychee/util/$CommonProxy$CustomConditionListener"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $CommonProxy {
static "hasKiwi": boolean
static "hasDFLib": boolean

constructor()

public static "register"(arg0: $RegisterEvent$Type): void
public static "recipe"(arg0: $ResourceLocation$Type): $Recipe<(any)>
public static "jsonToTag"(arg0: $JsonElement$Type): $CompoundTag
public static "isPhysicalClient"(): boolean
public static "white"(arg0: charseq): $MutableComponent
public static "newRegistries"(arg0: $NewRegistryEvent$Type): void
public static "wrapNamespace"(arg0: string): string
public static "writeNullableRL"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): void
public static "interactionResult"(arg0: boolean): $InteractionResult
public static "makeDescriptionId"(arg0: string, arg1: $ResourceLocation$Type): string
public static "getCycledItem"<T>(arg0: $List$Type<(T)>, arg1: T, arg2: integer): T
public static "readNullableRL"(arg0: $FriendlyByteBuf$Type): $ResourceLocation
public static "capitaliseAllWords"(arg0: string): string
public static "dropItemStack"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: $ItemStack$Type, arg5: $Consumer$Type<($ItemEntity$Type)>): void
public static "isSimpleIngredient"(arg0: $Ingredient$Type): boolean
public static "tagToJson"(arg0: $CompoundTag$Type): $JsonObject
public static "tagElements"<T>(arg0: $Registry$Type<(T)>, arg1: $TagKey$Type<(T)>): $List<(T)>
public static "parseOffset"(arg0: $JsonObject$Type): $BlockPos
public static "clampPos"(arg0: $Vec3$Type, arg1: $BlockPos$Type): $Vec3
public static "getIngredientType"(arg0: $Ingredient$Type): $IngredientInfo$Type
public static "setRecipeManager"(arg0: $RecipeManager$Type, arg1: boolean): void
public static "itemstackToJson"(arg0: $ItemStack$Type, arg1: $JsonObject$Type): void
public static "getOnPos"(arg0: $Entity$Type): $BlockPos
public static "recipeManager"(): $RecipeManager
public static "recipes"<T extends $Recipe<(any)>>(arg0: $RecipeType$Type<(T)>): $Collection<(T)>
public static "dispensePlacement"(arg0: $BlockSource$Type, arg1: $ItemStack$Type, arg2: $Direction$Type): $ItemStack
public static "readRegistryId"<T>(arg0: $LycheeRegistries$MappedRegistry$Type<(T)>, arg1: $FriendlyByteBuf$Type): T
public static "readRegistryId"<T>(arg0: $Registry$Type<(T)>, arg1: $FriendlyByteBuf$Type): T
public static "writeRegistryId"<T>(arg0: $Registry$Type<(T)>, arg1: T, arg2: $FriendlyByteBuf$Type): void
public static "writeRegistryId"<T>(arg0: $LycheeRegistries$MappedRegistry$Type<(T)>, arg1: T, arg2: $FriendlyByteBuf$Type): void
public static "isModLoaded"(arg0: string): boolean
public static "chance"(arg0: float): string
public static "registerRecipeBookCategories"(arg0: $RegisterRecipeBookCategoriesEvent$Type): void
public static "registerCustomActionListener"(arg0: $CommonProxy$CustomActionListener$Type): void
public static "registerCustomConditionListener"(arg0: $CommonProxy$CustomConditionListener$Type): void
public static "postCustomConditionEvent"(arg0: string, arg1: $CustomCondition$Type): void
public static "postCustomActionEvent"(arg0: string, arg1: $CustomAction$Type, arg2: $ILycheeRecipe$Type<(any)>, arg3: $ILycheeRecipe$NBTPatchContext$Type): void
public static "registerParticleType"(arg0: $ParticleOptions$Deserializer$Type<($BlockParticleOption$Type)>): $ParticleType<($BlockParticleOption)>
public static "hasModdedDripParticle"(arg0: $FluidState$Type): boolean
public static "explode"(arg0: $Explode$Type, arg1: $ServerLevel$Type, arg2: $Vec3$Type, arg3: $Entity$Type, arg4: $DamageSource$Type, arg5: $ExplosionDamageCalculator$Type, arg6: float): void
get "physicalClient"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonProxy$Type = ($CommonProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonProxy_ = $CommonProxy$Type;
}}
declare module "packages/snownee/lychee/core/post/$DropItem" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $DropItem extends $PostAction {
readonly "stack": $ItemStack
 "path": string

constructor(arg0: $ItemStack$Type)

public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "provideJsonInfo"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $JsonObject$Type): $JsonElement
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getItemOutputs"(): $List<($ItemStack)>
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropItem$Type = ($DropItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropItem_ = $DropItem$Type;
}}
declare module "packages/snownee/lychee/$LycheeTags" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LycheeTags {
static readonly "FIRE_IMMUNE": $TagKey<($Item)>
static readonly "DISPENSER_PLACEMENT": $TagKey<($Item)>
static readonly "ITEM_EXPLODING_CATALYSTS": $TagKey<($Item)>
static readonly "BLOCK_EXPLODING_CATALYSTS": $TagKey<($Item)>
static readonly "EXTEND_BOX": $TagKey<($Block)>
static readonly "LIGHTNING_IMMUNE": $TagKey<($EntityType<(any)>)>
static readonly "LIGHTING_FIRE_IMMUNE": $TagKey<($EntityType<(any)>)>

constructor()

public static "init"(): void
public static "tag"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $TagKey<(T)>
public static "itemTag"(arg0: string): $TagKey<($Item)>
public static "entityTag"(arg0: string): $TagKey<($EntityType<(any)>)>
public static "blockTag"(arg0: string): $TagKey<($Block)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeTags$Type = ($LycheeTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeTags_ = $LycheeTags$Type;
}}
declare module "packages/snownee/lychee/util/json/$JsonPatch" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonPatch$Type, $JsonPatch$Type$Type} from "packages/snownee/lychee/util/json/$JsonPatch$Type"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"

export class $JsonPatch {
readonly "op": $JsonPatch$Type
 "path": $JsonPointer
 "from": $JsonPointer
 "value": $JsonElement

constructor(arg0: $JsonPatch$Type$Type, arg1: $JsonPointer$Type, arg2: $JsonPointer$Type, arg3: $JsonElement$Type)

public static "add"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonElement$Type): $JsonElement
public static "remove"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type): $JsonElement
public static "test"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonElement$Type): $JsonElement
public static "replace"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonElement$Type): $JsonElement
public "apply"(arg0: $JsonElement$Type): $JsonElement
public static "merge"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonElement$Type): $JsonElement
public static "copy"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonPointer$Type): $JsonElement
public static "parse"(arg0: $JsonObject$Type): $JsonPatch
public static "move"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonPointer$Type): $JsonElement
public "toJson"(): $JsonObject
public static "deepMerge"(arg0: $JsonElement$Type, arg1: $JsonPointer$Type, arg2: $JsonElement$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonPatch$Type = ($JsonPatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonPatch_ = $JsonPatch$Type;
}}
declare module "packages/snownee/lychee/$LycheeRegistries" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$NewRegistryEvent, $NewRegistryEvent$Type} from "packages/net/minecraftforge/registries/$NewRegistryEvent"
import {$LycheeRegistries$MappedRegistry, $LycheeRegistries$MappedRegistry$Type} from "packages/snownee/lychee/$LycheeRegistries$MappedRegistry"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $LycheeRegistries {
static "CONTEXTUAL": $LycheeRegistries$MappedRegistry<($ContextualConditionType<(any)>)>
static "POST_ACTION": $LycheeRegistries$MappedRegistry<($PostActionType<(any)>)>

constructor()

public static "init"(arg0: $NewRegistryEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeRegistries$Type = ($LycheeRegistries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeRegistries_ = $LycheeRegistries$Type;
}}
declare module "packages/snownee/lychee/mixin/$CraftingMenuAccess" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ContainerLevelAccess, $ContainerLevelAccess$Type} from "packages/net/minecraft/world/inventory/$ContainerLevelAccess"

export interface $CraftingMenuAccess {

 "getAccess"(): $ContainerLevelAccess
 "getPlayer"(): $Player
}

export namespace $CraftingMenuAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingMenuAccess$Type = ($CraftingMenuAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingMenuAccess_ = $CraftingMenuAccess$Type;
}}
declare module "packages/snownee/lychee/mixin/$PointedDripstoneBlockAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PointedDripstoneBlockAccess {

}

export namespace $PointedDripstoneBlockAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointedDripstoneBlockAccess$Type = ($PointedDripstoneBlockAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointedDripstoneBlockAccess_ = $PointedDripstoneBlockAccess$Type;
}}
declare module "packages/snownee/lychee/client/gui/$GuiGameElement" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGameElement$GuiRenderBuilder, $GuiGameElement$GuiRenderBuilder$Type} from "packages/snownee/lychee/client/gui/$GuiGameElement$GuiRenderBuilder"

export class $GuiGameElement {

constructor()

public static "of"(arg0: $Fluid$Type): $GuiGameElement$GuiRenderBuilder
public static "of"(arg0: $BlockState$Type): $GuiGameElement$GuiRenderBuilder
public static "of"(arg0: $ItemLike$Type): $GuiGameElement$GuiRenderBuilder
public static "of"(arg0: $ItemStack$Type): $GuiGameElement$GuiRenderBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiGameElement$Type = ($GuiGameElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiGameElement_ = $GuiGameElement$Type;
}}
declare module "packages/snownee/lychee/core/post/$PostAction" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$ContextualHolder, $ContextualHolder$Type} from "packages/snownee/lychee/core/contextual/$ContextualHolder"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $PostAction extends $ContextualHolder {
 "path": string

constructor()

public "toString"(): string
public "isHidden"(): boolean
public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $PostAction
public "getType"(): $PostActionType<(any)>
public static "parse"(arg0: $JsonObject$Type): $PostAction
public "getDisplayName"(): $Component
public "toJson"(): $JsonObject
public "preventSync"(): boolean
public static "parseActions"(arg0: $JsonElement$Type, arg1: $Consumer$Type<($PostAction$Type)>): void
public "onFailure"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getUsedPointers"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $Consumer$Type<($JsonPointer$Type)>): void
public "provideJsonInfo"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $JsonObject$Type): $JsonElement
public "preApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: $ILycheeRecipe$NBTPatchContext$Type): void
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getBlockOutputs"(): $List<($BlockPredicate)>
public "getItemOutputs"(): $List<($ItemStack)>
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
get "blockOutputs"(): $List<($BlockPredicate)>
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostAction$Type = ($PostAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostAction_ = $PostAction$Type;
}}
declare module "packages/snownee/lychee/item_burning/$ItemBurningRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemBurningRecipe, $ItemBurningRecipe$Type} from "packages/snownee/lychee/item_burning/$ItemBurningRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ItemBurningRecipe$Serializer extends $LycheeRecipe$Serializer<($ItemBurningRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $ItemBurningRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $ItemBurningRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $ItemBurningRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBurningRecipe$Serializer$Type = ($ItemBurningRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBurningRecipe$Serializer_ = $ItemBurningRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/core/post/$Delay$LycheeMarker" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Marker, $Marker$Type} from "packages/net/minecraft/world/entity/$Marker"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export interface $Delay$LycheeMarker {

 "getEntity"(): $Marker
 "lychee$setContext"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type): void
 "lychee$addDelay"(arg0: integer): void
 "lychee$getContext"(): $LycheeContext
}

export namespace $Delay$LycheeMarker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Delay$LycheeMarker$Type = ($Delay$LycheeMarker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Delay$LycheeMarker_ = $Delay$LycheeMarker$Type;
}}
declare module "packages/snownee/lychee/dripstone_dripping/$DripParticleHandler$Simple" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$DripParticleHandler, $DripParticleHandler$Type} from "packages/snownee/lychee/dripstone_dripping/$DripParticleHandler"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $DripParticleHandler$Simple extends $Record implements $DripParticleHandler {

constructor(color: integer, glowing: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "color"(): integer
public "addParticle"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: double, arg4: double, arg5: double): void
public "glowing"(): boolean
public "getColor"(arg0: $ClientLevel$Type, arg1: $BlockState$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): integer
public "isGlowing"(arg0: $ClientLevel$Type, arg1: $BlockState$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripParticleHandler$Simple$Type = ($DripParticleHandler$Simple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripParticleHandler$Simple_ = $DripParticleHandler$Simple$Type;
}}
declare module "packages/snownee/lychee/mixin/$RecipeManagerAccess" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $RecipeManagerAccess {

 "callByType"<C extends $Container, T extends $Recipe<(C)>>(arg0: $RecipeType$Type<(T)>): $Map<($ResourceLocation), ($Recipe<(C)>)>

(arg0: $RecipeType$Type<(T)>): $Map<($ResourceLocation), ($Recipe<(C)>)>
}

export namespace $RecipeManagerAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeManagerAccess$Type = ($RecipeManagerAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeManagerAccess_ = $RecipeManagerAccess$Type;
}}
declare module "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$BlockExplodingRecipe, $BlockExplodingRecipe$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $BlockExplodingRecipe$Serializer extends $LycheeRecipe$Serializer<($BlockExplodingRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $BlockExplodingRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $BlockExplodingRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $BlockExplodingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockExplodingRecipe$Serializer$Type = ($BlockExplodingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockExplodingRecipe$Serializer_ = $BlockExplodingRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$ItemBurningRecipeCategory" {
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$ItemAndBlockBaseCategory, $ItemAndBlockBaseCategory$Type} from "packages/snownee/lychee/compat/jei/category/$ItemAndBlockBaseCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$ItemBurningRecipe, $ItemBurningRecipe$Type} from "packages/snownee/lychee/item_burning/$ItemBurningRecipe"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $ItemBurningRecipeCategory extends $ItemAndBlockBaseCategory<($LycheeContext), ($ItemBurningRecipe)> {
 "inputBlockRect": $Rect2i
 "methodRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($LycheeContext$Type), ($ItemBurningRecipe$Type)>)

public "getIconBlock"(arg0: $List$Type<($ItemBurningRecipe$Type)>): $BlockState
public "getInputBlock"(arg0: $ItemBurningRecipe$Type): $BlockPredicate
public "getRenderingBlock"(arg0: $ItemBurningRecipe$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBurningRecipeCategory$Type = ($ItemBurningRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBurningRecipeCategory_ = $ItemBurningRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/util/$Pair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Pair<F, S> {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"<F, S>(arg0: F, arg1: S): $Pair<(F), (S)>
public "copy"(): $Pair<(F), (S)>
public "swap"(): $Pair<(S), (F)>
public "getFirst"(): F
public "getSecond"(): S
public "setSecond"(arg0: S): void
public "setFirst"(arg0: F): void
get "first"(): F
get "second"(): S
set "second"(value: S)
set "first"(value: F)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<F, S> = ($Pair<(F), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<F, S> = $Pair$Type<(F), (S)>;
}}
declare module "packages/snownee/lychee/core/def/$DoubleBoundsHelper" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"

export class $DoubleBoundsHelper {

constructor()

public static "random"(arg0: $MinMaxBounds$Doubles$Type, arg1: $RandomSource$Type): float
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $MinMaxBounds$Doubles
public static "toNetwork"(arg0: $MinMaxBounds$Doubles$Type, arg1: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleBoundsHelper$Type = ($DoubleBoundsHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleBoundsHelper_ = $DoubleBoundsHelper$Type;
}}
declare module "packages/snownee/lychee/compat/jei/category/$DripstoneRecipeCategory" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$DripstoneRecipe, $DripstoneRecipe$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$BaseJEICategory, $BaseJEICategory$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$DripstoneContext, $DripstoneContext$Type} from "packages/snownee/lychee/dripstone_dripping/$DripstoneContext"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $DripstoneRecipeCategory extends $BaseJEICategory<($DripstoneContext), ($DripstoneRecipe)> {
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($DripstoneContext$Type), ($DripstoneRecipe$Type)>)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<($DripstoneRecipe$Type)>): $IDrawable
public "draw"(arg0: $DripstoneRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "handleInput"(arg0: $DripstoneRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $DripstoneRecipe$Type, arg2: $IFocusGroup$Type): void
public "getTooltipStrings"(arg0: $DripstoneRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DripstoneRecipeCategory$Type = ($DripstoneRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DripstoneRecipeCategory_ = $DripstoneRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/anvil_crafting/$AnvilContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $AnvilContext extends $LycheeContext {
readonly "left": $ItemStack
readonly "right": $ItemStack
readonly "name": string
 "levelCost": integer
 "materialCost": integer
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilContext$Type = ($AnvilContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilContext_ = $AnvilContext$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$ItemAndBlockRecipe" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $ItemAndBlockRecipe<C extends $LycheeContext> extends $LycheeRecipe<(C)> implements $BlockKeyRecipe<($ItemAndBlockRecipe<(C)>)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $ItemAndBlockRecipe$Type<(C)>): integer
public "matches"(arg0: $LycheeContext$Type, arg1: $Level$Type): boolean
public "getInput"(): $Ingredient
public "getBlock"(): $BlockPredicate
public "getIngredients"(): $NonNullList<($Ingredient)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "input"(): $Ingredient
get "block"(): $BlockPredicate
get "ingredients"(): $NonNullList<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAndBlockRecipe$Type<C> = ($ItemAndBlockRecipe<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAndBlockRecipe_<C> = $ItemAndBlockRecipe$Type<(C)>;
}}
declare module "packages/snownee/lychee/core/post/$PlaceBlock" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $PlaceBlock extends $PostAction {
readonly "block": $BlockPredicate
readonly "offset": $BlockPos
 "path": string

constructor(arg0: $BlockPredicate$Type, arg1: $BlockPos$Type)

public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "canRepeat"(): boolean
public "getBlockOutputs"(): $List<($BlockPredicate)>
public "getItemOutputs"(): $List<($ItemStack)>
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
get "blockOutputs"(): $List<($BlockPredicate)>
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceBlock$Type = ($PlaceBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceBlock_ = $PlaceBlock$Type;
}}
declare module "packages/snownee/lychee/core/$LycheeContext" {
import {$Delay$LycheeMarker, $Delay$LycheeMarker$Type} from "packages/snownee/lychee/core/post/$Delay$LycheeMarker"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EmptyContainer, $EmptyContainer$Type} from "packages/snownee/lychee/core/$EmptyContainer"
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LycheeContext extends $EmptyContainer {
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "load"(arg0: $JsonObject$Type, arg1: $Delay$LycheeMarker$Type): $LycheeContext
public "save"(): $JsonObject
public "getLevel"(): $Level
public "getParams"(): $Map<($LootContextParam<(any)>), (any)>
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeParam"(arg0: $LootContextParam$Type<(any)>): void
public "enqueueActions"(arg0: $Stream$Type<($PostAction$Type)>, arg1: integer, arg2: boolean): void
public "hasParam"(arg0: $LootContextParam$Type<(any)>): boolean
public "toLootContext"(): $LootContext
public "getParamOrNull"<T>(arg0: $LootContextParam$Type<(T)>): T
public "lazyGetBlockEntity"(): void
public "getRandom"(): $RandomSource
public "getServerLevel"(): $ServerLevel
public "setParam"(arg0: $LootContextParam$Type<(any)>, arg1: any): void
public "getParam"<T>(arg0: $LootContextParam$Type<(T)>): T
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "level"(): $Level
get "params"(): $Map<($LootContextParam<(any)>), (any)>
get "containerSize"(): integer
get "random"(): $RandomSource
get "serverLevel"(): $ServerLevel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeContext$Type = ($LycheeContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeContext_ = $LycheeContext$Type;
}}
declare module "packages/snownee/lychee/core/post/$AnvilDamageChance" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $AnvilDamageChance extends $PostAction {
readonly "chance": float
 "path": string

constructor(arg0: float)

public "isHidden"(): boolean
public "getType"(): $PostActionType<(any)>
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilDamageChance$Type = ($AnvilDamageChance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilDamageChance_ = $AnvilDamageChance$Type;
}}
declare module "packages/snownee/lychee/core/recipe/$BlockKeyRecipe" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"

export interface $BlockKeyRecipe<T> extends $Comparable<(T)> {

 "getBlock"(): $BlockPredicate
 "compareTo"(arg0: T): integer
}

export namespace $BlockKeyRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockKeyRecipe$Type<T> = ($BlockKeyRecipe<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockKeyRecipe_<T> = $BlockKeyRecipe$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/$Reference" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"

export class $Reference {
static readonly "DEFAULT": $Reference

constructor()

public static "create"(arg0: string): $Reference
public static "fromJson"(arg0: $JsonObject$Type, arg1: string): $Reference
public static "toJson"(arg0: $Reference$Type, arg1: $JsonObject$Type, arg2: string): void
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $Reference
public static "toNetwork"(arg0: $Reference$Type, arg1: $FriendlyByteBuf$Type): void
public "getPointer"(): $JsonPointer
public "isPointer"(): boolean
get "pointer"(): $JsonPointer
get "pointer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reference$Type = ($Reference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reference_ = $Reference$Type;
}}
declare module "packages/snownee/lychee/block_crushing/$LycheeFallingBlockEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LycheeFallingBlockEntity {

 "lychee$matched"(): void
 "lychee$cancelDrop"(): void
 "lychee$anvilDamageChance"(arg0: float): void
}

export namespace $LycheeFallingBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeFallingBlockEntity$Type = ($LycheeFallingBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeFallingBlockEntity_ = $LycheeFallingBlockEntity$Type;
}}
declare module "packages/snownee/lychee/block_exploding/$BlockExplodingRecipe" {
import {$BlockExplodingContext, $BlockExplodingContext$Type} from "packages/snownee/lychee/block_exploding/$BlockExplodingContext"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $BlockExplodingRecipe extends $LycheeRecipe<($BlockExplodingContext)> implements $BlockKeyRecipe<($BlockExplodingRecipe)> {
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $BlockExplodingRecipe$Type): integer
public "matches"(arg0: $BlockExplodingContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getBlock"(): $BlockPredicate
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "block"(): $BlockPredicate
get "serializer"(): $LycheeRecipe$Serializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockExplodingRecipe$Type = ($BlockExplodingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockExplodingRecipe_ = $BlockExplodingRecipe$Type;
}}
declare module "packages/snownee/lychee/util/$RecipeMatcher" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $RecipeMatcher<T> {
 "inputs": $List<(T)>
 "tests": $List<(any)>
 "inputCapacity": (integer)[]
 "inputUsed": (integer)[]
 "use": ((integer)[])[]

constructor(arg0: $List$Type<(T)>, arg1: $List$Type<(any)>, arg2: (integer)[])

public static "findMatches"<T>(arg0: $List$Type<(T)>, arg1: $List$Type<(any)>, arg2: (integer)[]): $Optional<($RecipeMatcher<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeMatcher$Type<T> = ($RecipeMatcher<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeMatcher_<T> = $RecipeMatcher$Type<(T)>;
}}
declare module "packages/snownee/lychee/util/$Couple" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Pair, $Pair$Type} from "packages/snownee/lychee/util/$Pair"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Supplier, $Supplier$Type} from "packages/com/google/common/base/$Supplier"

export class $Couple<T> extends $Pair<(T), (T)> implements $Iterable<(T)> {


public "get"(arg0: boolean): T
public "replace"(arg0: $Function$Type<(T), (T)>): void
public "iterator"(): $Iterator<(T)>
public "map"<S>(arg0: $Function$Type<(T), (S)>): $Couple<(S)>
public "stream"(): $Stream<(T)>
public "set"(arg0: boolean, arg1: T): void
public "forEach"(arg0: $Consumer$Type<(any)>): void
public static "create"<T>(arg0: T, arg1: T): $Couple<(T)>
public static "create"<T>(arg0: $Supplier$Type<(T)>): $Couple<(T)>
public "mapWithContext"<S>(arg0: $BiFunction$Type<(T), (boolean), (S)>): $Couple<(S)>
public "mapWithParams"<S, R>(arg0: $BiFunction$Type<(T), (R), (S)>, arg1: $Couple$Type<(R)>): $Couple<(S)>
public "replaceWithContext"(arg0: $BiFunction$Type<(T), (boolean), (T)>): void
public "forEachWithParams"<S>(arg0: $BiConsumer$Type<(T), (S)>, arg1: $Couple$Type<(S)>): void
public "replaceWithParams"<S>(arg0: $BiFunction$Type<(T), (S), (T)>, arg1: $Couple$Type<(S)>): void
public "forEachWithContext"(arg0: $BiConsumer$Type<(T), (boolean)>): void
public static "createWithContext"<T>(arg0: $Function$Type<(boolean), (T)>): $Couple<(T)>
public "spliterator"(): $Spliterator<(T)>
[Symbol.iterator](): IterableIterator<T>;
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Couple$Type<T> = ($Couple<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Couple_<T> = $Couple$Type<(T)>;
}}
declare module "packages/snownee/lychee/core/def/$LocationPredicateHelper" {
import {$LocationPredicate, $LocationPredicate$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$LocationPredicate$Builder, $LocationPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$LocationPredicate$Builder"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export interface $LocationPredicateHelper {

 "lychee$setBiomeTag"(arg0: $TagKey$Type<($Biome$Type)>): void
 "lychee$getBiomeTag"(): $TagKey<($Biome)>
}

export namespace $LocationPredicateHelper {
function fromNetwork(arg0: $FriendlyByteBuf$Type): $LocationPredicate$Builder
function toNetwork(arg0: $LocationPredicate$Type, arg1: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocationPredicateHelper$Type = ($LocationPredicateHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocationPredicateHelper_ = $LocationPredicateHelper$Type;
}}
declare module "packages/snownee/lychee/util/$Color" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Couple, $Couple$Type} from "packages/snownee/lychee/util/$Couple"

export class $Color {
static readonly "TRANSPARENT_BLACK": $Color
static readonly "BLACK": $Color
static readonly "WHITE": $Color
static readonly "RED": $Color

constructor(arg0: integer, arg1: boolean)
constructor(arg0: integer)
constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)

public "setValue"(arg0: integer): $Color
public "copy"(): $Color
public "copy"(arg0: boolean): $Color
public "asVectorF"(): $Vector3f
public "setBlue"(arg0: integer): $Color
public "setBlue"(arg0: float): $Color
public "mixWith"(arg0: $Color$Type, arg1: float): $Color
public "getAlphaAsFloat"(): float
public "asVector"(): $Vec3
public "scaleAlpha"(arg0: float): $Color
public "modifyValue"(arg0: $UnaryOperator$Type<(integer)>): $Color
public static "rainbowColor"(arg0: integer): $Color
public static "generateFromLong"(arg0: long): $Color
public "getRGB"(): integer
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public "getAlpha"(): integer
public "brighter"(): $Color
public "darker"(): $Color
public "setAlpha"(arg0: integer): $Color
public "setAlpha"(arg0: float): $Color
public "setImmutable"(): $Color
public "getGreenAsFloat"(): float
public "getBlueAsFloat"(): float
public "getRedAsFloat"(): float
public static "mixColors"(arg0: $Couple$Type<($Color$Type)>, arg1: float): $Color
public static "mixColors"(arg0: integer, arg1: integer, arg2: float): integer
public static "mixColors"(arg0: $Color$Type, arg1: $Color$Type, arg2: float): $Color
public "setGreen"(arg0: float): $Color
public "setGreen"(arg0: integer): $Color
public "setRed"(arg0: integer): $Color
public "setRed"(arg0: float): $Color
set "value"(value: integer)
set "blue"(value: integer)
set "blue"(value: float)
get "alphaAsFloat"(): float
get "rGB"(): integer
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "alpha"(): integer
set "alpha"(value: integer)
set "alpha"(value: float)
get "greenAsFloat"(): float
get "blueAsFloat"(): float
get "redAsFloat"(): float
set "green"(value: float)
set "green"(value: integer)
set "red"(value: integer)
set "red"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Color$Type = ($Color);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Color_ = $Color$Type;
}}
declare module "packages/snownee/lychee/interaction/$BlockInteractingRecipe$Serializer" {
import {$BlockInteractingRecipe, $BlockInteractingRecipe$Type} from "packages/snownee/lychee/interaction/$BlockInteractingRecipe"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ItemAndBlockRecipe$Serializer, $ItemAndBlockRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$ItemAndBlockRecipe$Serializer"

export class $BlockInteractingRecipe$Serializer<T extends $BlockInteractingRecipe> extends $ItemAndBlockRecipe$Serializer<(T)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor(arg0: $Function$Type<($ResourceLocation$Type), (T)>)

public "fromJson"(arg0: T, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: T, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: T): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInteractingRecipe$Serializer$Type<T> = ($BlockInteractingRecipe$Serializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInteractingRecipe$Serializer_<T> = $BlockInteractingRecipe$Serializer$Type<(T)>;
}}
declare module "packages/snownee/lychee/mixin/$LightPredicateAccess" {
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"

export interface $LightPredicateAccess {

 "getComposite"(): $MinMaxBounds$Ints

(): $MinMaxBounds$Ints
}

export namespace $LightPredicateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightPredicateAccess$Type = ($LightPredicateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightPredicateAccess_ = $LightPredicateAccess$Type;
}}
declare module "packages/snownee/lychee/core/post/$PostActionType" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $PostActionType<T extends $PostAction> {

constructor()

public "fromJson"(arg0: $JsonObject$Type): T
public "toJson"(arg0: T, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $FriendlyByteBuf$Type): T
public "toNetwork"(arg0: T, arg1: $FriendlyByteBuf$Type): void
public "getRegistryName"(): $ResourceLocation
get "registryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostActionType$Type<T> = ($PostActionType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostActionType_<T> = $PostActionType$Type<(T)>;
}}
declare module "packages/snownee/lychee/compat/jei/category/$BlockCrushingRecipeCategory" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$BlockCrushingRecipe, $BlockCrushingRecipe$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe"
import {$BaseJEICategory, $BaseJEICategory$Type} from "packages/snownee/lychee/compat/jei/category/$BaseJEICategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$BlockCrushingContext, $BlockCrushingContext$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $BlockCrushingRecipeCategory extends $BaseJEICategory<($BlockCrushingContext), ($BlockCrushingRecipe)> {
static readonly "fallingBlockRect": $Rect2i
static readonly "landingBlockRect": $Rect2i
static readonly "width": integer
static readonly "height": integer
readonly "recipeTypes": $List<($LycheeRecipeType<(C), (T)>)>
 "icon": $IDrawable
 "initialRecipes": $List<(T)>
 "recipeType": $RecipeType<(T)>

constructor(arg0: $LycheeRecipeType$Type<($BlockCrushingContext$Type), ($BlockCrushingRecipe$Type)>)

public "createIcon"(arg0: $IGuiHelper$Type, arg1: $List$Type<($BlockCrushingRecipe$Type)>): $IDrawable
public "draw"(arg0: $BlockCrushingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "handleInput"(arg0: $BlockCrushingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $BlockCrushingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getTooltipStrings"(arg0: $BlockCrushingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
get "width"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCrushingRecipeCategory$Type = ($BlockCrushingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCrushingRecipeCategory_ = $BlockCrushingRecipeCategory$Type;
}}
declare module "packages/snownee/lychee/crafting/$CraftingContext" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ActionRuntime, $ActionRuntime$Type} from "packages/snownee/lychee/core/$ActionRuntime"
import {$ItemHolderCollection, $ItemHolderCollection$Type} from "packages/snownee/lychee/core/input/$ItemHolderCollection"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export class $CraftingContext extends $LycheeContext {
readonly "matchX": integer
readonly "matchY": integer
readonly "mirror": boolean
 "runtime": $ActionRuntime
 "itemHolders": $ItemHolderCollection
 "json": $JsonObject


public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingContext$Type = ($CraftingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingContext_ = $CraftingContext$Type;
}}
declare module "packages/snownee/lychee/core/post/$RandomSelect" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILycheeRecipe$NBTPatchContext, $ILycheeRecipe$NBTPatchContext$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe$NBTPatchContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$CompoundAction, $CompoundAction$Type} from "packages/snownee/lychee/core/post/$CompoundAction"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"
import {$PostActionType, $PostActionType$Type} from "packages/snownee/lychee/core/post/$PostActionType"

export class $RandomSelect extends $PostAction implements $CompoundAction {
readonly "entries": ($PostAction)[]
readonly "weights": (integer)[]
readonly "rolls": $MinMaxBounds$Ints
readonly "canRepeat": boolean
readonly "hidden": boolean
readonly "preventSync": boolean
readonly "totalWeight": integer
readonly "emptyWeight": integer
 "path": string

constructor(arg0: ($PostAction$Type)[], arg1: (integer)[], arg2: integer, arg3: integer, arg4: $MinMaxBounds$Ints$Type)

public "isHidden"(): boolean
public "validate"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $ILycheeRecipe$NBTPatchContext$Type): void
public "getType"(): $PostActionType<(any)>
public "getDisplayName"(): $Component
public "preventSync"(): boolean
public "getUsedPointers"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $Consumer$Type<($JsonPointer$Type)>): void
public "provideJsonInfo"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $JsonObject$Type): $JsonElement
public "canRepeat"(): boolean
public "doApply"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): void
public "getBlockOutputs"(): $List<($BlockPredicate)>
public "getChildActions"(): $Stream<($PostAction)>
public "getItemOutputs"(): $List<($ItemStack)>
get "hidden"(): boolean
get "type"(): $PostActionType<(any)>
get "displayName"(): $Component
get "blockOutputs"(): $List<($BlockPredicate)>
get "childActions"(): $Stream<($PostAction)>
get "itemOutputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomSelect$Type = ($RandomSelect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomSelect_ = $RandomSelect$Type;
}}
declare module "packages/snownee/lychee/compat/kubejs/$CustomConditionEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$CustomCondition, $CustomCondition$Type} from "packages/snownee/lychee/core/contextual/$CustomCondition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CustomConditionEventJS extends $EventJS {
readonly "id": string
readonly "condition": $CustomCondition
readonly "data": $Map<(any), (any)>

constructor(arg0: string, arg1: $CustomCondition$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomConditionEventJS$Type = ($CustomConditionEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomConditionEventJS_ = $CustomConditionEventJS$Type;
}}
declare module "packages/snownee/lychee/core/network/$SCustomLevelEventPacket" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $SCustomLevelEventPacket extends $PacketHandler {
static "I": $SCustomLevelEventPacket
 "id": $ResourceLocation

constructor()

public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
public static "sendItemParticles"(arg0: $ItemStack$Type, arg1: $ServerLevel$Type, arg2: $Vec3$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SCustomLevelEventPacket$Type = ($SCustomLevelEventPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SCustomLevelEventPacket_ = $SCustomLevelEventPacket$Type;
}}
declare module "packages/snownee/lychee/$ContextualConditionTypes" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$And, $And$Type} from "packages/snownee/lychee/core/contextual/$And"
import {$EntityHealth, $EntityHealth$Type} from "packages/snownee/lychee/core/contextual/$EntityHealth"
import {$Or, $Or$Type} from "packages/snownee/lychee/core/contextual/$Or"
import {$IsWeather, $IsWeather$Type} from "packages/snownee/lychee/core/contextual/$IsWeather"
import {$Not, $Not$Type} from "packages/snownee/lychee/core/contextual/$Not"
import {$FallDistance, $FallDistance$Type} from "packages/snownee/lychee/core/contextual/$FallDistance"
import {$Location, $Location$Type} from "packages/snownee/lychee/core/contextual/$Location"
import {$CheckParam, $CheckParam$Type} from "packages/snownee/lychee/core/contextual/$CheckParam"
import {$IsSneaking, $IsSneaking$Type} from "packages/snownee/lychee/core/contextual/$IsSneaking"
import {$Execute, $Execute$Type} from "packages/snownee/lychee/core/contextual/$Execute"
import {$CustomCondition, $CustomCondition$Type} from "packages/snownee/lychee/core/contextual/$CustomCondition"
import {$IsDifficulty, $IsDifficulty$Type} from "packages/snownee/lychee/core/contextual/$IsDifficulty"
import {$Chance, $Chance$Type} from "packages/snownee/lychee/core/contextual/$Chance"
import {$Time, $Time$Type} from "packages/snownee/lychee/core/contextual/$Time"
import {$DirectionCheck, $DirectionCheck$Type} from "packages/snownee/lychee/core/contextual/$DirectionCheck"

export class $ContextualConditionTypes {
static readonly "CHANCE": $ContextualConditionType<($Chance)>
static readonly "LOCATION": $ContextualConditionType<($Location)>
static readonly "DIFFICULTY": $ContextualConditionType<($IsDifficulty)>
static readonly "WEATHER": $ContextualConditionType<($IsWeather)>
static readonly "NOT": $ContextualConditionType<($Not)>
static readonly "OR": $ContextualConditionType<($Or)>
static readonly "AND": $ContextualConditionType<($And)>
static readonly "TIME": $ContextualConditionType<($Time)>
static readonly "EXECUTE": $ContextualConditionType<($Execute)>
static readonly "FALL_DISTANCE": $ContextualConditionType<($FallDistance)>
static readonly "ENTITY_HEALTH": $ContextualConditionType<($EntityHealth)>
static readonly "IS_SNEAKING": $ContextualConditionType<($IsSneaking)>
static readonly "DIRECTION": $ContextualConditionType<($DirectionCheck)>
static readonly "CHECK_PARAM": $ContextualConditionType<($CheckParam)>
static readonly "CUSTOM": $ContextualConditionType<($CustomCondition)>

constructor()

public static "register"<T extends $ContextualConditionType<(any)>>(arg0: string, arg1: T): T
public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextualConditionTypes$Type = ($ContextualConditionTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextualConditionTypes_ = $ContextualConditionTypes$Type;
}}
declare module "packages/snownee/lychee/core/contextual/$ContextualCondition" {
import {$ContextualConditionType, $ContextualConditionType$Type} from "packages/snownee/lychee/core/contextual/$ContextualConditionType"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GsonContextImpl, $GsonContextImpl$Type} from "packages/snownee/lychee/util/$GsonContextImpl"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"

export interface $ContextualCondition {

 "test"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $LycheeContext$Type, arg2: integer): integer
 "getType"(): $ContextualConditionType<(any)>
 "getDescription"(arg0: boolean): $MutableComponent
 "toJson"(): $JsonObject
 "makeDescriptionId"(arg0: boolean): string
 "showingCount"(): integer
 "appendTooltips"(arg0: $List$Type<($Component$Type)>, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: boolean): void
 "testInTooltips"(arg0: $Level$Type, arg1: $Player$Type): $InteractionResult
}

export namespace $ContextualCondition {
const predicateGson: $Gson
const gsonContext: $GsonContextImpl
function desc(arg0: $List$Type<($Component$Type)>, arg1: $InteractionResult$Type, arg2: integer, arg3: $MutableComponent$Type): void
function parse(arg0: $JsonObject$Type): $ContextualCondition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextualCondition$Type = ($ContextualCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextualCondition_ = $ContextualCondition$Type;
}}
declare module "packages/snownee/lychee/core/post/$CompoundAction" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export interface $CompoundAction {

 "getChildActions"(): $Stream<($PostAction)>

(): $Stream<($PostAction)>
}

export namespace $CompoundAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompoundAction$Type = ($CompoundAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompoundAction_ = $CompoundAction$Type;
}}
declare module "packages/snownee/lychee/compat/$JEIREI" {
import {$CachedRenderingEntity, $CachedRenderingEntity$Type} from "packages/snownee/lychee/util/$CachedRenderingEntity"
import {$PrimedTnt, $PrimedTnt$Type} from "packages/net/minecraft/world/entity/item/$PrimedTnt"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$JEIREI$CategoryCreationContext, $JEIREI$CategoryCreationContext$Type} from "packages/snownee/lychee/compat/$JEIREI$CategoryCreationContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ILightingSettings, $ILightingSettings$Type} from "packages/snownee/lychee/client/gui/$ILightingSettings"
import {$Pair, $Pair$Type} from "packages/snownee/lychee/util/$Pair"
import {$IngredientInfo, $IngredientInfo$Type} from "packages/snownee/lychee/compat/$IngredientInfo"

export class $JEIREI {
static "BLOCK_LIGHTING": $ILightingSettings
static "SIDE_ICON_LIGHTING": $ILightingSettings
static "FUSED_TNT_LIGHTING": $ILightingSettings
static readonly "TNT_ENTITY": $CachedRenderingEntity<($PrimedTnt)>

constructor()

public static "getMostUsedBlock"<T extends $LycheeRecipe<(any)>>(arg0: $List$Type<(T)>): $Pair<($BlockState), (integer)>
public static "renderTnt"(arg0: $GuiGraphics$Type, arg1: float, arg2: float): void
public static "generateShapelessInputs"(arg0: $LycheeRecipe$Type<(any)>): $List<($IngredientInfo)>
public static "composeCategoryIdentifier"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type): $ResourceLocation
public static "addIngredientTips"(arg0: $LycheeRecipe$Type<(any)>, arg1: $List$Type<($IngredientInfo$Type)>): void
public static "makeTitle"(arg0: $ResourceLocation$Type): $MutableComponent
public static "getRecipeTooltip"(arg0: $ILycheeRecipe$Type<(any)>): $List<($Component)>
public static "registerCategories"(arg0: $Predicate$Type<($ResourceLocation$Type)>, arg1: $BiConsumer$Type<($ResourceLocation$Type), ($JEIREI$CategoryCreationContext$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIREI$Type = ($JEIREI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIREI_ = $JEIREI$Type;
}}
declare module "packages/snownee/lychee/core/def/$TimeCheckHelper" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$TimeCheck, $TimeCheck$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$TimeCheck"

export class $TimeCheckHelper {

constructor()

public static "fromJson"(arg0: $JsonObject$Type): $TimeCheck
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeCheckHelper$Type = ($TimeCheckHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeCheckHelper_ = $TimeCheckHelper$Type;
}}
declare module "packages/snownee/lychee/client/gui/$FluidRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $FluidRenderer {

constructor()

public static "renderFluidBox"(arg0: $FluidState$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $MultiBufferSource$Type, arg8: $PoseStack$Type, arg9: integer, arg10: boolean): void
public static "renderFluidBox"(arg0: $FluidState$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $VertexConsumer$Type, arg8: $PoseStack$Type, arg9: integer, arg10: boolean): void
public static "getFluidBuilder"(arg0: $MultiBufferSource$Type): $VertexConsumer
public static "renderTiledFace"(arg0: $Direction$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $VertexConsumer$Type, arg7: $PoseStack$Type, arg8: integer, arg9: integer, arg10: $TextureAtlasSprite$Type, arg11: float): void
public static "renderStillTiledFace"(arg0: $Direction$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $VertexConsumer$Type, arg7: $PoseStack$Type, arg8: integer, arg9: integer, arg10: $TextureAtlasSprite$Type): void
public static "renderFlowingTiledFace"(arg0: $Direction$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $VertexConsumer$Type, arg7: $PoseStack$Type, arg8: integer, arg9: integer, arg10: $TextureAtlasSprite$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidRenderer$Type = ($FluidRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidRenderer_ = $FluidRenderer$Type;
}}
declare module "packages/snownee/lychee/compat/$JEIREI$CategoryCreationContext" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $JEIREI$CategoryCreationContext extends $Record {

constructor(group: $ResourceLocation$Type, recipes: $List$Type<($LycheeRecipe$Type<(any)>)>)

public "group"(): $ResourceLocation
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "recipes"(): $List<($LycheeRecipe<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIREI$CategoryCreationContext$Type = ($JEIREI$CategoryCreationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIREI$CategoryCreationContext_ = $JEIREI$CategoryCreationContext$Type;
}}
declare module "packages/snownee/lychee/block_crushing/$BlockCrushingRecipe" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockCrushingContext, $BlockCrushingContext$Type} from "packages/snownee/lychee/block_crushing/$BlockCrushingContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$BlockKeyRecipe, $BlockKeyRecipe$Type} from "packages/snownee/lychee/core/recipe/$BlockKeyRecipe"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$JsonPointer, $JsonPointer$Type} from "packages/snownee/lychee/util/json/$JsonPointer"
import {$PostAction, $PostAction$Type} from "packages/snownee/lychee/core/post/$PostAction"

export class $BlockCrushingRecipe extends $LycheeRecipe<($BlockCrushingContext)> implements $BlockKeyRecipe<($BlockCrushingRecipe)> {
static readonly "ANVIL": $BlockPredicate
 "ghost": boolean
 "hideInRecipeViewer": boolean
 "comment": string
 "group": string

constructor(arg0: $ResourceLocation$Type)

public "compareTo"(arg0: $BlockCrushingRecipe$Type): integer
public "matches"(arg0: $BlockCrushingContext$Type, arg1: $Level$Type): boolean
public "getType"(): $LycheeRecipeType<(any), (any)>
public "getBlock"(): $BlockPredicate
public "getSerializer"(): $LycheeRecipe$Serializer<(any)>
public "matchesFallingBlock"(arg0: $BlockState$Type, arg1: $CompoundTag$Type): boolean
public "getLandingBlock"(): $BlockPredicate
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getBlockInputs"(): $List<($BlockPredicate)>
public static "processActionGroup"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonPointer$Type, arg2: $List$Type<($PostAction$Type)>, arg3: $JsonObject$Type): $JsonElement
public static "processActions"(arg0: $ILycheeRecipe$Type<(any)>, arg1: $JsonObject$Type): void
public static "filterHidden"(arg0: $Stream$Type<($PostAction$Type)>): $Stream<($PostAction)>
get "type"(): $LycheeRecipeType<(any), (any)>
get "block"(): $BlockPredicate
get "serializer"(): $LycheeRecipe$Serializer<(any)>
get "landingBlock"(): $BlockPredicate
get "ingredients"(): $NonNullList<($Ingredient)>
get "blockInputs"(): $List<($BlockPredicate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCrushingRecipe$Type = ($BlockCrushingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCrushingRecipe_ = $BlockCrushingRecipe$Type;
}}
declare module "packages/snownee/lychee/util/json/$JsonPatch$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $JsonPatch$Type extends $Enum<($JsonPatch$Type)> {
static readonly "add": $JsonPatch$Type
static readonly "remove": $JsonPatch$Type
static readonly "replace": $JsonPatch$Type
static readonly "copy": $JsonPatch$Type
static readonly "move": $JsonPatch$Type
static readonly "test": $JsonPatch$Type
static readonly "merge": $JsonPatch$Type
static readonly "deep_merge": $JsonPatch$Type


public static "values"(): ($JsonPatch$Type)[]
public static "valueOf"(arg0: string): $JsonPatch$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonPatch$Type$Type = (("add") | ("move") | ("test") | ("merge") | ("replace") | ("deep_merge") | ("copy") | ("remove")) | ($JsonPatch$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonPatch$Type_ = $JsonPatch$Type$Type;
}}
declare module "packages/snownee/lychee/util/$ClientProxy$RecipeViewerWidgetClickListener" {
import {$ILycheeRecipe, $ILycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$ILycheeRecipe"

export interface $ClientProxy$RecipeViewerWidgetClickListener {

 "onClick"(arg0: $ILycheeRecipe$Type<(any)>, arg1: integer): boolean

(arg0: $ILycheeRecipe$Type<(any)>, arg1: integer): boolean
}

export namespace $ClientProxy$RecipeViewerWidgetClickListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProxy$RecipeViewerWidgetClickListener$Type = ($ClientProxy$RecipeViewerWidgetClickListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProxy$RecipeViewerWidgetClickListener_ = $ClientProxy$RecipeViewerWidgetClickListener$Type;
}}
declare module "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$LycheeContext, $LycheeContext$Type} from "packages/snownee/lychee/core/$LycheeContext"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $LycheeRecipeType<C extends $LycheeContext, T extends $LycheeRecipe<(C)>> implements $RecipeType<(T)> {
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<(T)>, arg2: $LootContextParamSet$Type)

public "toString"(): string
public "isEmpty"(): boolean
public "findFirst"(arg0: C, arg1: $Level$Type): $Optional<(T)>
public "tryMatch"<D extends $Container>(arg0: $Recipe$Type<(D)>, arg1: $Level$Type, arg2: D): $Optional<(T)>
public "getPreventDefaultDescription"(arg0: $LycheeRecipe$Type<(any)>): $Component
public "recipes"(): $List<(T)>
public "inViewerRecipes"(): $List<(T)>
public "updateEmptyState"(): void
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeRecipeType$Type<C, T> = ($LycheeRecipeType<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeRecipeType_<C, T> = $LycheeRecipeType$Type<(C), (T)>;
}}
declare module "packages/snownee/lychee/anvil_crafting/$AnvilCraftingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LycheeRecipe$Serializer, $LycheeRecipe$Serializer$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$AnvilCraftingRecipe, $AnvilCraftingRecipe$Type} from "packages/snownee/lychee/anvil_crafting/$AnvilCraftingRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $AnvilCraftingRecipe$Serializer extends $LycheeRecipe$Serializer<($AnvilCraftingRecipe)> {
static readonly "EMPTY_INGREDIENT": $Ingredient

constructor()

public "fromJson"(arg0: $AnvilCraftingRecipe$Type, arg1: $JsonObject$Type): void
public "fromNetwork"(arg0: $AnvilCraftingRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork0"(arg0: $FriendlyByteBuf$Type, arg1: $AnvilCraftingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilCraftingRecipe$Serializer$Type = ($AnvilCraftingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilCraftingRecipe$Serializer_ = $AnvilCraftingRecipe$Serializer$Type;
}}
declare module "packages/snownee/lychee/core/def/$NumberProviderHelper" {
import {$ConstantValue, $ConstantValue$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$ConstantValue"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $NumberProviderHelper {

constructor()

public static "requireConstant"(arg0: $NumberProvider$Type): void
public static "toConstant"(arg0: $NumberProvider$Type): integer
public static "fromConstant"(arg0: integer): $ConstantValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberProviderHelper$Type = ($NumberProviderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberProviderHelper_ = $NumberProviderHelper$Type;
}}
declare module "packages/snownee/lychee/$LycheeLootContextParams" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LycheeLootContextParams {
static readonly "ALL": $Map<(string), ($LootContextParam<(any)>)>
static readonly "BLOCK_POS": $LootContextParam<($BlockPos)>
static readonly "DIRECTION": $LootContextParam<($Direction)>

constructor()

public static "init"(): void
public static "trimRL"(arg0: string): string
public static "trimRL"(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LycheeLootContextParams$Type = ($LycheeLootContextParams);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LycheeLootContextParams_ = $LycheeLootContextParams$Type;
}}
declare module "packages/snownee/lychee/core/recipe/type/$ItemShapelessRecipeType" {
import {$LycheeRecipe, $LycheeRecipe$Type} from "packages/snownee/lychee/core/recipe/$LycheeRecipe"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$ItemShapelessContext, $ItemShapelessContext$Type} from "packages/snownee/lychee/core/$ItemShapelessContext"
import {$LycheeRecipeType, $LycheeRecipeType$Type} from "packages/snownee/lychee/core/recipe/type/$LycheeRecipeType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$ItemShapelessContext$Builder, $ItemShapelessContext$Builder$Type} from "packages/snownee/lychee/core/$ItemShapelessContext$Builder"

export class $ItemShapelessRecipeType<C extends $ItemShapelessContext, T extends $LycheeRecipe<(C)>> extends $LycheeRecipeType<(C), (T)> {
readonly "id": $ResourceLocation
 "categoryId": $ResourceLocation
readonly "clazz": $Class<(any)>
readonly "contextParamSet": $LootContextParamSet
 "requiresClient": boolean
 "compactInputs": boolean
 "canPreventConsumeInputs": boolean
 "hasStandaloneCategory": boolean
static readonly "DEFAULT_PREVENT_TIP": $Component

constructor(arg0: string, arg1: $Class$Type<(T)>, arg2: $LootContextParamSet$Type)

public static "process"<C extends $ItemShapelessContext, T extends $LycheeRecipe<(C)>>(arg0: $LycheeRecipeType$Type<(C), (T)>, arg1: $Iterable$Type<(T)>, arg2: C, arg3: $Predicate$Type<(T)>): void
public "process"(arg0: $Level$Type, arg1: $Stream$Type<($ItemEntity$Type)>, arg2: $Consumer$Type<($ItemShapelessContext$Builder$Type<(C)>)>): void
public "buildCache"(): void
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemShapelessRecipeType$Type<C, T> = ($ItemShapelessRecipeType<(C), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemShapelessRecipeType_<C, T> = $ItemShapelessRecipeType$Type<(C), (T)>;
}}
