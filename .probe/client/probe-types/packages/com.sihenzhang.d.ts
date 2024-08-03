declare module "packages/com/sihenzhang/crockpot/data/$CrockPotSoundDefinitionsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$SoundDefinitionsProvider, $SoundDefinitionsProvider$Type} from "packages/net/minecraftforge/common/data/$SoundDefinitionsProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"

export class $CrockPotSoundDefinitionsProvider extends $SoundDefinitionsProvider {


public static "getSubtitleName"(arg0: string): string
public "registerSounds"(): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotSoundDefinitionsProvider$Type = ($CrockPotSoundDefinitionsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotSoundDefinitionsProvider_ = $CrockPotSoundDefinitionsProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/tag/$CrockPotItemTags" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $CrockPotItemTags {
static readonly "CROCK_POTS": $TagKey<($Item)>
static readonly "MILKMADE_HATS": $TagKey<($Item)>
static readonly "PARROT_EGGS": $TagKey<($Item)>
static readonly "CROPS_ASPARAGUS": $TagKey<($Item)>
static readonly "CROPS_CORN": $TagKey<($Item)>
static readonly "CROPS_EGGPLANT": $TagKey<($Item)>
static readonly "CROPS_GARLIC": $TagKey<($Item)>
static readonly "CROPS_ONION": $TagKey<($Item)>
static readonly "CROPS_PEPPER": $TagKey<($Item)>
static readonly "CROPS_TOMATO": $TagKey<($Item)>
static readonly "SEEDS_ASPARAGUS": $TagKey<($Item)>
static readonly "SEEDS_CORN": $TagKey<($Item)>
static readonly "SEEDS_EGGPLANT": $TagKey<($Item)>
static readonly "SEEDS_GARLIC": $TagKey<($Item)>
static readonly "SEEDS_ONION": $TagKey<($Item)>
static readonly "SEEDS_PEPPER": $TagKey<($Item)>
static readonly "SEEDS_TOMATO": $TagKey<($Item)>
static readonly "VEGETABLES": $TagKey<($Item)>
static readonly "VEGETABLES_BEETROOT": $TagKey<($Item)>
static readonly "VEGETABLES_CARROT": $TagKey<($Item)>
static readonly "VEGETABLES_POTATO": $TagKey<($Item)>
static readonly "VEGETABLES_PUMPKIN": $TagKey<($Item)>
static readonly "VEGETABLES_ASPARAGUS": $TagKey<($Item)>
static readonly "VEGETABLES_CORN": $TagKey<($Item)>
static readonly "VEGETABLES_EGGPLANT": $TagKey<($Item)>
static readonly "VEGETABLES_GARLIC": $TagKey<($Item)>
static readonly "VEGETABLES_ONION": $TagKey<($Item)>
static readonly "VEGETABLES_PEPPER": $TagKey<($Item)>
static readonly "VEGETABLES_TOMATO": $TagKey<($Item)>
static readonly "FRUITS": $TagKey<($Item)>
static readonly "FRUITS_APPLE": $TagKey<($Item)>
static readonly "RAW_BEEF": $TagKey<($Item)>
static readonly "RAW_CHICKEN": $TagKey<($Item)>
static readonly "RAW_MUTTON": $TagKey<($Item)>
static readonly "RAW_PORK": $TagKey<($Item)>
static readonly "RAW_RABBIT": $TagKey<($Item)>
static readonly "COOKED_BEEF": $TagKey<($Item)>
static readonly "COOKED_CHICKEN": $TagKey<($Item)>
static readonly "COOKED_MUTTON": $TagKey<($Item)>
static readonly "COOKED_PORK": $TagKey<($Item)>
static readonly "COOKED_RABBIT": $TagKey<($Item)>
static readonly "RAW_FISHES": $TagKey<($Item)>
static readonly "RAW_FISHES_COD": $TagKey<($Item)>
static readonly "RAW_FISHES_SALMON": $TagKey<($Item)>
static readonly "RAW_FISHES_TROPICAL_FISH": $TagKey<($Item)>
static readonly "COOKED_FISHES": $TagKey<($Item)>
static readonly "COOKED_FISHES_COD": $TagKey<($Item)>
static readonly "COOKED_FISHES_SALMON": $TagKey<($Item)>
static readonly "RAW_FROGS": $TagKey<($Item)>
static readonly "COOKED_FROGS": $TagKey<($Item)>
static readonly "CURIO": $TagKey<($Item)>
static readonly "HEAD": $TagKey<($Item)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotItemTags$Type = ($CrockPotItemTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotItemTags_ = $CrockPotItemTags$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCombinationAnd" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"

export class $RequirementCombinationAnd implements $IRequirement {

constructor(arg0: $IRequirement$Type, arg1: $IRequirement$Type)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getFirst"(): $IRequirement
public "getSecond"(): $IRequirement
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCombinationAnd
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCombinationAnd
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "first"(): $IRequirement
get "second"(): $IRequirement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCombinationAnd$Type = ($RequirementCombinationAnd);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCombinationAnd_ = $RequirementCombinationAnd$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CollectedDustItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$CrockPotBaseItem, $CrockPotBaseItem$Type} from "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CollectedDustItem extends $CrockPotBaseItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectedDustItem$Type = ($CollectedDustItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectedDustItem_ = $CollectedDustItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$CrockPotCookingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$CrockPotCookingRecipe, $CrockPotCookingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe"
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

export class $CrockPotCookingRecipeCategory implements $IRecipeCategory<($CrockPotCookingRecipe)> {
static readonly "RECIPE_TYPE": $RecipeType<($CrockPotCookingRecipe)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($CrockPotCookingRecipe)>
public "draw"(arg0: $CrockPotCookingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $CrockPotCookingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(arg0: $CrockPotCookingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $CrockPotCookingRecipe$Type): boolean
public "handleInput"(arg0: $CrockPotCookingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getRegistryName"(arg0: $CrockPotCookingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($CrockPotCookingRecipe)>
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
export type $CrockPotCookingRecipeCategory$Type = ($CrockPotCookingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipeCategory_ = $CrockPotCookingRecipeCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementMustContainIngredient" {
import {$RequirementMustContainIngredient, $RequirementMustContainIngredient$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementMustContainIngredient"
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RequirementMustContainIngredientLessThan, $RequirementMustContainIngredientLessThan$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementMustContainIngredientLessThan"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementMustContainIngredient extends $AbstractDrawableRequirement<($RequirementMustContainIngredient)> {

constructor(arg0: $RequirementMustContainIngredient$Type)
constructor(arg0: $RequirementMustContainIngredient$Type, arg1: $RequirementMustContainIngredientLessThan$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementMustContainIngredient$Type = ($DrawableRequirementMustContainIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementMustContainIngredient_ = $DrawableRequirementMustContainIngredient$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$AbstractCrockPotRecipeJS" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractCrockPotRecipeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCrockPotRecipeJS$Type = ($AbstractCrockPotRecipeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCrockPotRecipeJS_ = $AbstractCrockPotRecipeJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$CandyItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$CrockPotFoodBlockItem, $CrockPotFoodBlockItem$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodBlockItem"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CandyItem extends $CrockPotFoodBlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CandyItem$Type = ($CandyItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CandyItem_ = $CandyItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$ExplosionCraftingRecipe$Wrapper, $ExplosionCraftingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe$Wrapper"
import {$AbstractRecipe, $AbstractRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ExplosionCraftingRecipe extends $AbstractRecipe<($ExplosionCraftingRecipe$Wrapper)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $ItemStack$Type, arg3: float, arg4: boolean)

public "matches"(arg0: $ExplosionCraftingRecipe$Wrapper$Type, arg1: $Level$Type): boolean
public "getResult"(): $ItemStack
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredient"(): $Ingredient
public "getLossRate"(): float
public "isOnlyBlock"(): boolean
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $ExplosionCraftingRecipe$Wrapper$Type, arg1: $RegistryAccess$Type): $ItemStack
get "result"(): $ItemStack
get "ingredient"(): $Ingredient
get "lossRate"(): float
get "onlyBlock"(): boolean
get "ingredients"(): $NonNullList<($Ingredient)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingRecipe$Type = ($ExplosionCraftingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipe_ = $ExplosionCraftingRecipe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$ChickensFollowSeedsEvent" {
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"

export class $ChickensFollowSeedsEvent {

constructor()

public static "onChickenAppear"(arg0: $EntityJoinLevelEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChickensFollowSeedsEvent$Type = ($ChickensFollowSeedsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChickensFollowSeedsEvent_ = $ChickensFollowSeedsEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/entity/$ChargeableMob" {
import {$PowerableMob, $PowerableMob$Type} from "packages/net/minecraft/world/entity/$PowerableMob"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export interface $ChargeableMob extends $PowerableMob {

 "isPowered"(): boolean
 "getRemainingPersistentChargeTime"(): integer
 "setRemainingPersistentChargeTime"(arg0: integer): void
 "updatePersistentCharge"(): void
 "addPersistentChargeSaveData"(arg0: $CompoundTag$Type): void
 "startPersistentChargeTimer"(): void
 "readPersistentChargeSaveData"(arg0: $CompoundTag$Type): void
}

export namespace $ChargeableMob {
const TAG_CHARGE_TIME: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChargeableMob$Type = ($ChargeableMob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChargeableMob_ = $ChargeableMob$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties$Builder" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$FoodUseDuration, $FoodUseDuration$Type} from "packages/com/sihenzhang/crockpot/item/food/$FoodUseDuration"
import {$CrockPotFoodProperties, $CrockPotFoodProperties$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $CrockPotFoodProperties$Builder {

constructor()
constructor(arg0: integer, arg1: float)

public "build"(): $CrockPotFoodProperties
public "duration"(arg0: $FoodUseDuration$Type): $CrockPotFoodProperties$Builder
public "damage"(arg0: $ResourceKey$Type<($DamageType$Type)>, arg1: float): $CrockPotFoodProperties$Builder
public "heal"(arg0: float): $CrockPotFoodProperties$Builder
public "sound"(arg0: $SoundEvent$Type): $CrockPotFoodProperties$Builder
public "alwaysEat"(): $CrockPotFoodProperties$Builder
public "removeEffect"(arg0: $MobEffect$Type): $CrockPotFoodProperties$Builder
public "hideEffects"(): $CrockPotFoodProperties$Builder
public "craftRemainder"(arg0: $Item$Type): $CrockPotFoodProperties$Builder
public "meat"(): $CrockPotFoodProperties$Builder
public "effect"(arg0: $MobEffect$Type, arg1: integer, arg2: integer, arg3: float): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<(any)>, arg1: integer): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<($MobEffectInstance$Type)>): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<($MobEffectInstance$Type)>, arg1: float): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<(any)>, arg1: integer, arg2: integer, arg3: float): $CrockPotFoodProperties$Builder
public "effect"(arg0: $MobEffect$Type, arg1: integer): $CrockPotFoodProperties$Builder
public "effect"(arg0: $MobEffect$Type, arg1: integer, arg2: float): $CrockPotFoodProperties$Builder
public "effect"(arg0: $MobEffect$Type, arg1: integer, arg2: integer): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<(any)>, arg1: integer, arg2: float): $CrockPotFoodProperties$Builder
public "effect"(arg0: $Supplier$Type<(any)>, arg1: integer, arg2: integer): $CrockPotFoodProperties$Builder
public "rarity"(arg0: $Rarity$Type): $CrockPotFoodProperties$Builder
public "tooltip"(arg0: string, ...arg1: ($ChatFormatting$Type)[]): $CrockPotFoodProperties$Builder
public "tooltip"(arg0: string): $CrockPotFoodProperties$Builder
public "nutrition"(arg0: integer): $CrockPotFoodProperties$Builder
public "effectTooltip"(arg0: $Component$Type): $CrockPotFoodProperties$Builder
public "effectTooltip"(arg0: string): $CrockPotFoodProperties$Builder
public "effectTooltip"(arg0: string, ...arg1: ($ChatFormatting$Type)[]): $CrockPotFoodProperties$Builder
public "cooldown"(arg0: integer): $CrockPotFoodProperties$Builder
public "saturationMod"(arg0: float): $CrockPotFoodProperties$Builder
public "stacksTo"(arg0: integer): $CrockPotFoodProperties$Builder
public "drink"(): $CrockPotFoodProperties$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotFoodProperties$Builder$Type = ($CrockPotFoodProperties$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotFoodProperties$Builder_ = $CrockPotFoodProperties$Builder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$ExplosionCraftingEvent" {
import {$ExplosionEvent$Detonate, $ExplosionEvent$Detonate$Type} from "packages/net/minecraftforge/event/level/$ExplosionEvent$Detonate"

export class $ExplosionCraftingEvent {

constructor()

public static "onExplosionDetonate"(arg0: $ExplosionEvent$Detonate$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingEvent$Type = ($ExplosionCraftingEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingEvent_ = $ExplosionCraftingEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotBlockTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$BlockTagsProvider, $BlockTagsProvider$Type} from "packages/net/minecraftforge/common/data/$BlockTagsProvider"

export class $CrockPotBlockTagsProvider extends $BlockTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockTagsProvider$Type = ($CrockPotBlockTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockTagsProvider_ = $CrockPotBlockTagsProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/network/$PacketFoodCounter" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $PacketFoodCounter {

constructor(arg0: $CompoundTag$Type)

public static "handle"(arg0: $PacketFoodCounter$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public static "deserialize"(arg0: $FriendlyByteBuf$Type): $PacketFoodCounter
public static "serialize"(arg0: $PacketFoodCounter$Type, arg1: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketFoodCounter$Type = ($PacketFoodCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketFoodCounter_ = $PacketFoodCounter$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$UnknownCropsBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$AbstractCrockPotCropBlock, $AbstractCrockPotCropBlock$Type} from "packages/com/sihenzhang/crockpot/block/$AbstractCrockPotCropBlock"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $UnknownCropsBlock extends $AbstractCrockPotCropBlock {
static readonly "AGE": $IntegerProperty
static readonly "MAX_AGE": integer
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()

public "randomTick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "m_7959_"(): $IntegerProperty
public "getMaxAge"(): integer
public "growCrops"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): void
get "maxAge"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnknownCropsBlock$Type = ($UnknownCropsBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnknownCropsBlock_ = $UnknownCropsBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/capability/$FoodCounterProvider" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilitySerializable, $ICapabilitySerializable$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilitySerializable"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $FoodCounterProvider implements $ICapabilitySerializable<($CompoundTag)> {

constructor()

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodCounterProvider$Type = ($FoodCounterProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodCounterProvider_ = $FoodCounterProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$MilkWithBottleEvent" {
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"

export class $MilkWithBottleEvent {

constructor()

public static "onCowAndGoatInteract"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkWithBottleEvent$Type = ($MilkWithBottleEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkWithBottleEvent_ = $MilkWithBottleEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$AbstractFinishedRecipe" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $AbstractFinishedRecipe implements $FinishedRecipe {

constructor(arg0: $ResourceLocation$Type)

public "getAdvancementId"(): $ResourceLocation
public "getId"(): $ResourceLocation
public "serializeAdvancement"(): $JsonObject
public "serializeRecipeData"(arg0: $JsonObject$Type): void
public "getType"(): $RecipeSerializer<(any)>
public "serializeRecipe"(): $JsonObject
get "advancementId"(): $ResourceLocation
get "id"(): $ResourceLocation
get "type"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFinishedRecipe$Type = ($AbstractFinishedRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFinishedRecipe_ = $AbstractFinishedRecipe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$AddContentsToCreativeTabsEvent" {
import {$BuildCreativeModeTabContentsEvent, $BuildCreativeModeTabContentsEvent$Type} from "packages/net/minecraftforge/event/$BuildCreativeModeTabContentsEvent"

export class $AddContentsToCreativeTabsEvent {

constructor()

public static "buildContents"(arg0: $BuildCreativeModeTabContentsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddContentsToCreativeTabsEvent$Type = ($AddContentsToCreativeTabsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddContentsToCreativeTabsEvent_ = $AddContentsToCreativeTabsEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/$DrawableNineSliceResource" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableNineSliceResource implements $IDrawable {

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer, arg12: integer)

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
export type $DrawableNineSliceResource$Type = ($DrawableNineSliceResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableNineSliceResource_ = $DrawableNineSliceResource$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$BirdcageBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BaseEntityBlock, $BaseEntityBlock$Type} from "packages/net/minecraft/world/level/block/$BaseEntityBlock"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$RenderShape, $RenderShape$Type} from "packages/net/minecraft/world/level/block/$RenderShape"

export class $BirdcageBlock extends $BaseEntityBlock {
static readonly "LOWER_SHAPE_WITHOUT_BASE": $VoxelShape
static readonly "LOWER_SHAPE": $VoxelShape
static readonly "UPPER_SHAPE_WITHOUT_CHAIN": $VoxelShape
static readonly "UPPER_SHAPE": $VoxelShape
static readonly "HANGING_UPPER_SHAPE": $VoxelShape
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "HANGING": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getBlockEntity"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $BlockEntity
public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "setPlacedBy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $LivingEntity$Type, arg4: $ItemStack$Type): void
public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getRenderShape"(arg0: $BlockState$Type): $RenderShape
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public static "getConnectedDirection"(arg0: $BlockState$Type): $Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BirdcageBlock$Type = ($BirdcageBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BirdcageBlock_ = $BirdcageBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/entity/$ThrownParrotEgg" {
import {$ThrowableItemProjectile, $ThrowableItemProjectile$Type} from "packages/net/minecraft/world/entity/projectile/$ThrowableItemProjectile"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ThrownParrotEgg extends $ThrowableItemProjectile {
 "ownerUUID": $UUID
 "cachedOwner": $Entity
 "leftOwner": boolean
 "hasBeenShot": boolean
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

constructor(arg0: $Level$Type, arg1: $LivingEntity$Type)
constructor(arg0: $Level$Type, arg1: double, arg2: double, arg3: double)
constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "handleEntityEvent"(arg0: byte): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThrownParrotEgg$Type = ($ThrownParrotEgg);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThrownParrotEgg_ = $ThrownParrotEgg$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotBaseItem extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBaseItem$Type = ($CrockPotBaseItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBaseItem_ = $CrockPotBaseItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$PiglinBarteringRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$PiglinBarteringRecipe, $PiglinBarteringRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$PiglinBarteringRecipe"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $PiglinBarteringRecipeCategory implements $IRecipeCategory<($PiglinBarteringRecipe)> {
static readonly "RECIPE_TYPE": $RecipeType<($PiglinBarteringRecipe)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($PiglinBarteringRecipe)>
public "draw"(arg0: $PiglinBarteringRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $PiglinBarteringRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $PiglinBarteringRecipe$Type): boolean
public "handleInput"(arg0: $PiglinBarteringRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $PiglinBarteringRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $PiglinBarteringRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($PiglinBarteringRecipe)>
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
export type $PiglinBarteringRecipeCategory$Type = ($PiglinBarteringRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringRecipeCategory_ = $PiglinBarteringRecipeCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/advancement/$CrockPotCriteriaTriggers" {
import {$PiglinBarteringTrigger, $PiglinBarteringTrigger$Type} from "packages/com/sihenzhang/crockpot/advancement/$PiglinBarteringTrigger"
import {$EatFoodTrigger, $EatFoodTrigger$Type} from "packages/com/sihenzhang/crockpot/advancement/$EatFoodTrigger"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $CrockPotCriteriaTriggers {
static "PIGLIN_BARTERING_TRIGGER": $PiglinBarteringTrigger
static "EAT_FOOD_TRIGGER": $EatFoodTrigger

constructor()

public static "onCommonSetup"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCriteriaTriggers$Type = ($CrockPotCriteriaTriggers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCriteriaTriggers_ = $CrockPotCriteriaTriggers$Type;
}}
declare module "packages/com/sihenzhang/crockpot/advancement/$EatFoodTrigger$Instance" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SerializationContext, $SerializationContext$Type} from "packages/net/minecraft/advancements/critereon/$SerializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $EatFoodTrigger$Instance extends $AbstractCriterionTriggerInstance {

constructor(arg0: $ContextAwarePredicate$Type, arg1: $ItemPredicate$Type, arg2: $MinMaxBounds$Ints$Type)

public "matches"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type, arg2: integer): boolean
public "serializeToJson"(arg0: $SerializationContext$Type): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EatFoodTrigger$Instance$Type = ($EatFoodTrigger$Instance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EatFoodTrigger$Instance_ = $EatFoodTrigger$Instance$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$FoodValuesCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$FoodValuesDefinition$FoodCategoryMatchedItems, $FoodValuesDefinition$FoodCategoryMatchedItems$Type} from "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition$FoodCategoryMatchedItems"
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

export class $FoodValuesCategory implements $IRecipeCategory<($FoodValuesDefinition$FoodCategoryMatchedItems)> {
static readonly "RECIPE_TYPE": $RecipeType<($FoodValuesDefinition$FoodCategoryMatchedItems)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($FoodValuesDefinition$FoodCategoryMatchedItems)>
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $FoodValuesDefinition$FoodCategoryMatchedItems$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $FoodValuesDefinition$FoodCategoryMatchedItems$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $FoodValuesDefinition$FoodCategoryMatchedItems$Type): boolean
public "handleInput"(arg0: $FoodValuesDefinition$FoodCategoryMatchedItems$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $FoodValuesDefinition$FoodCategoryMatchedItems$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $FoodValuesDefinition$FoodCategoryMatchedItems$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($FoodValuesDefinition$FoodCategoryMatchedItems)>
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
export type $FoodValuesCategory$Type = ($FoodValuesCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesCategory_ = $FoodValuesCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CrockPotItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Parrot$Variant, $Parrot$Variant$Type} from "packages/net/minecraft/world/entity/animal/$Parrot$Variant"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotItems {
static readonly "ITEMS": $DeferredRegister<($Item)>
static readonly "CROCK_POT": $RegistryObject<($Item)>
static readonly "PORTABLE_CROCK_POT": $RegistryObject<($Item)>
static readonly "UNKNOWN_SEEDS": $RegistryObject<($Item)>
static readonly "ASPARAGUS_SEEDS": $RegistryObject<($Item)>
static readonly "ASPARAGUS": $RegistryObject<($Item)>
static readonly "CORN_SEEDS": $RegistryObject<($Item)>
static readonly "CORN": $RegistryObject<($Item)>
static readonly "POPCORN": $RegistryObject<($Item)>
static readonly "EGGPLANT_SEEDS": $RegistryObject<($Item)>
static readonly "EGGPLANT": $RegistryObject<($Item)>
static readonly "COOKED_EGGPLANT": $RegistryObject<($Item)>
static readonly "GARLIC_SEEDS": $RegistryObject<($Item)>
static readonly "GARLIC": $RegistryObject<($Item)>
static readonly "ONION_SEEDS": $RegistryObject<($Item)>
static readonly "ONION": $RegistryObject<($Item)>
static readonly "PEPPER_SEEDS": $RegistryObject<($Item)>
static readonly "PEPPER": $RegistryObject<($Item)>
static readonly "TOMATO_SEEDS": $RegistryObject<($Item)>
static readonly "TOMATO": $RegistryObject<($Item)>
static readonly "SEEDS": $Supplier<($Set<($Item)>)>
static readonly "CROPS": $Supplier<($Set<($Item)>)>
static readonly "COOKED_CROPS": $Supplier<($Set<($Item)>)>
static readonly "BIRDCAGE": $RegistryObject<($Item)>
static readonly "PARROT_EGGS": $Map<($Parrot$Variant), ($RegistryObject<($Item)>)>
static readonly "CROCK_POT_UPGRADE_SMITHING_TEMPLATE": $RegistryObject<($Item)>
static readonly "BLACKSTONE_DUST": $RegistryObject<($Item)>
static readonly "COLLECTED_DUST": $RegistryObject<($Item)>
static readonly "COOKED_EGG": $RegistryObject<($Item)>
static readonly "FROG_LEGS": $RegistryObject<($Item)>
static readonly "COOKED_FROG_LEGS": $RegistryObject<($Item)>
static readonly "HOGLIN_NOSE": $RegistryObject<($Item)>
static readonly "COOKED_HOGLIN_NOSE": $RegistryObject<($Item)>
static readonly "MILK_BOTTLE": $RegistryObject<($Item)>
static readonly "SYRUP": $RegistryObject<($Item)>
static readonly "VOLT_GOAT_HORN": $RegistryObject<($Item)>
static readonly "MILKMADE_HAT": $RegistryObject<($Item)>
static readonly "CREATIVE_MILKMADE_HAT": $RegistryObject<($Item)>
static readonly "GNAWS_COIN": $RegistryObject<($Item)>
static readonly "ASPARAGUS_SOUP": $RegistryObject<($Item)>
static readonly "AVAJ": $RegistryObject<($Item)>
static readonly "BACON_EGGS": $RegistryObject<($Item)>
static readonly "BONE_SOUP": $RegistryObject<($Item)>
static readonly "BONE_STEW": $RegistryObject<($Item)>
static readonly "BREAKFAST_SKILLET": $RegistryObject<($Item)>
static readonly "BUNNY_STEW": $RegistryObject<($Item)>
static readonly "CALIFORNIA_ROLL": $RegistryObject<($Item)>
static readonly "CANDY": $RegistryObject<($Item)>
static readonly "CEVICHE": $RegistryObject<($Item)>
static readonly "FISH_STICKS": $RegistryObject<($Item)>
static readonly "FISH_TACOS": $RegistryObject<($Item)>
static readonly "FLOWER_SALAD": $RegistryObject<($Item)>
static readonly "FROGGLE_BUNWICH": $RegistryObject<($Item)>
static readonly "FRUIT_MEDLEY": $RegistryObject<($Item)>
static readonly "GAZPACHO": $RegistryObject<($Item)>
static readonly "GLOW_BERRY_MOUSSE": $RegistryObject<($Item)>
static readonly "HONEY_HAM": $RegistryObject<($Item)>
static readonly "HONEY_NUGGETS": $RegistryObject<($Item)>
static readonly "HOT_CHILI": $RegistryObject<($Item)>
static readonly "HOT_COCOA": $RegistryObject<($Item)>
static readonly "ICE_CREAM": $RegistryObject<($Item)>
static readonly "ICED_TEA": $RegistryObject<($Item)>
static readonly "JAMMY_PRESERVES": $RegistryObject<($Item)>
static readonly "KABOBS": $RegistryObject<($Item)>
static readonly "MASHED_POTATOES": $RegistryObject<($Item)>
static readonly "MEAT_BALLS": $RegistryObject<($Item)>
static readonly "MONSTER_LASAGNA": $RegistryObject<($Item)>
static readonly "MONSTER_TARTARE": $RegistryObject<($Item)>
static readonly "MOQUECA": $RegistryObject<($Item)>
static readonly "MUSHY_CAKE": $RegistryObject<($Item)>
static readonly "NETHEROSIA": $RegistryObject<($Item)>
static readonly "PEPPER_POPPER": $RegistryObject<($Item)>
static readonly "PEROGIES": $RegistryObject<($Item)>
static readonly "PLAIN_OMELETTE": $RegistryObject<($Item)>
static readonly "POTATO_SOUFFLE": $RegistryObject<($Item)>
static readonly "POTATO_TORNADO": $RegistryObject<($Item)>
static readonly "POW_CAKE": $RegistryObject<($Item)>
static readonly "PUMPKIN_COOKIE": $RegistryObject<($Item)>
static readonly "RATATOUILLE": $RegistryObject<($Item)>
static readonly "SALMON_SUSHI": $RegistryObject<($Item)>
static readonly "SALSA": $RegistryObject<($Item)>
static readonly "SCOTCH_EGG": $RegistryObject<($Item)>
static readonly "SEAFOOD_GUMBO": $RegistryObject<($Item)>
static readonly "STUFFED_EGGPLANT": $RegistryObject<($Item)>
static readonly "SURF_N_TURF": $RegistryObject<($Item)>
static readonly "TAFFY": $RegistryObject<($Item)>
static readonly "TEA": $RegistryObject<($Item)>
static readonly "TROPICAL_BOUILLABAISSE": $RegistryObject<($Item)>
static readonly "TURKEY_DINNER": $RegistryObject<($Item)>
static readonly "VEG_STINGER": $RegistryObject<($Item)>
static readonly "VOLT_GOAT_JELLY": $RegistryObject<($Item)>
static readonly "WATERMELON_ICLE": $RegistryObject<($Item)>
static readonly "WET_GOOP": $RegistryObject<($Item)>
static readonly "FOODS_WITHOUT_AVAJ": $Supplier<($List<($Item)>)>
static readonly "VOLT_GOAT_SPAWN_EGG": $RegistryObject<($Item)>
static readonly "FOOD_CATEGORY_ITEMS": $Map<($FoodCategory), ($RegistryObject<($Item)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotItems$Type = ($CrockPotItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotItems_ = $CrockPotItems$Type;
}}
declare module "packages/com/sihenzhang/crockpot/inventory/$CrockPotMenu" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$CrockPotBlockEntity, $CrockPotBlockEntity$Type} from "packages/com/sihenzhang/crockpot/block/entity/$CrockPotBlockEntity"

export class $CrockPotMenu extends $AbstractContainerMenu {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: integer, arg1: $Inventory$Type, arg2: $CrockPotBlockEntity$Type)

public "getBlockEntity"(): $BlockEntity
public "stillValid"(arg0: $Player$Type): boolean
public "removed"(arg0: $Player$Type): void
public "getBurningProgress"(): integer
public "getCookingProgress"(): integer
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
get "blockEntity"(): $BlockEntity
get "burningProgress"(): integer
get "cookingProgress"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotMenu$Type = ($CrockPotMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotMenu_ = $CrockPotMenu$Type;
}}
declare module "packages/com/sihenzhang/crockpot/network/$NetworkManager" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkDirection, $NetworkDirection$Type} from "packages/net/minecraftforge/network/$NetworkDirection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $NetworkManager {
static "INSTANCE": $SimpleChannel


public static "sendToPlayer"<MSG>(arg0: $ServerPlayer$Type, arg1: MSG): void
public static "registerPacket"<MSG>(arg0: $Class$Type<(MSG)>, arg1: $BiConsumer$Type<(MSG), ($FriendlyByteBuf$Type)>, arg2: $Function$Type<($FriendlyByteBuf$Type), (MSG)>, arg3: $BiConsumer$Type<(MSG), ($Supplier$Type<($NetworkEvent$Context$Type)>)>, arg4: $NetworkDirection$Type): void
public static "sendToAll"<MSG>(arg0: MSG): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManager$Type = ($NetworkManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManager_ = $NetworkManager$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$ParrotFeedingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$ParrotFeedingRecipe, $ParrotFeedingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe"
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

export class $ParrotFeedingRecipeCategory implements $IRecipeCategory<($ParrotFeedingRecipe)> {
static readonly "RECIPE_TYPE": $RecipeType<($ParrotFeedingRecipe)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($ParrotFeedingRecipe)>
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ParrotFeedingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $ParrotFeedingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $ParrotFeedingRecipe$Type): boolean
public "handleInput"(arg0: $ParrotFeedingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $ParrotFeedingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $ParrotFeedingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($ParrotFeedingRecipe)>
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
export type $ParrotFeedingRecipeCategory$Type = ($ParrotFeedingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotFeedingRecipeCategory_ = $ParrotFeedingRecipeCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/mixin/$IPiglinAiMixin" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPiglinAiMixin {

}

export namespace $IPiglinAiMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPiglinAiMixin$Type = ($IPiglinAiMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPiglinAiMixin_ = $IPiglinAiMixin$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$CrockPotRecipeType" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $CrockPotRecipeType<T extends $Recipe<(any)>> implements $RecipeType<(T)> {

constructor(arg0: string)

public "toString"(): string
public static "simple"<T extends $Recipe<(any)>>(arg0: $ResourceLocation$Type): $RecipeType<(T)>
public static "register"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotRecipeType$Type<T> = ($CrockPotRecipeType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotRecipeType_<T> = $CrockPotRecipeType$Type<(T)>;
}}
declare module "packages/com/sihenzhang/crockpot/effect/$CrockPotEffects" {
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $CrockPotEffects {
static readonly "EFFECTS": $DeferredRegister<($MobEffect)>
static readonly "CHARGE": $RegistryObject<($MobEffect)>
static readonly "GNAWS_GIFT": $RegistryObject<($MobEffect)>
static readonly "OCEAN_AFFINITY": $RegistryObject<($MobEffect)>
static readonly "WELL_FED": $RegistryObject<($MobEffect)>
static readonly "WITHER_RESISTANCE": $RegistryObject<($MobEffect)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotEffects$Type = ($CrockPotEffects);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotEffects_ = $CrockPotEffects$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$JeiUtils" {
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"

export class $JeiUtils {


public static "getPagedItemStacks"(arg0: $List$Type<($ItemStack$Type)>, arg1: $IFocusGroup$Type, arg2: $RecipeIngredientRole$Type, arg3: integer): $List<($List<($ItemStack)>)>
public static "getItemsFromIngredientWithoutEmptyTag"(arg0: $Ingredient$Type): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiUtils$Type = ($JeiUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiUtils_ = $JeiUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe" {
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"

export class $AbstractRecipe<C extends $Container> implements $Recipe<(C)> {


public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getId"(): $ResourceLocation
public "isSpecial"(): boolean
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getRemainingItems"(arg0: C): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "showNotification"(): boolean
public "assemble"(arg0: C, arg1: $RegistryAccess$Type): $ItemStack
public "matches"(arg0: C, arg1: $Level$Type): boolean
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
get "id"(): $ResourceLocation
get "special"(): boolean
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "serializer"(): $RecipeSerializer<(any)>
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
export type $AbstractRecipe$Type<C> = ($AbstractRecipe<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipe_<C> = $AbstractRecipe$Type<(C)>;
}}
declare module "packages/com/sihenzhang/crockpot/integration/curios/$GnawsCoinCuriosCapabilityProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $GnawsCoinCuriosCapabilityProvider implements $ICapabilityProvider {

constructor(arg0: $ItemStack$Type, arg1: $CompoundTag$Type)

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GnawsCoinCuriosCapabilityProvider$Type = ($GnawsCoinCuriosCapabilityProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GnawsCoinCuriosCapabilityProvider_ = $GnawsCoinCuriosCapabilityProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementMustContainIngredient" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $RequirementMustContainIngredient implements $IRequirement {

constructor(arg0: $Ingredient$Type, arg1: integer)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public static "fromJson"(arg0: $JsonObject$Type): $RequirementMustContainIngredient
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementMustContainIngredient
public "getIngredient"(): $Ingredient
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "getQuantity"(): integer
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "ingredient"(): $Ingredient
get "quantity"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementMustContainIngredient$Type = ($RequirementMustContainIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementMustContainIngredient_ = $RequirementMustContainIngredient$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotBlockStateProvider" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$WallSide, $WallSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$WallSide"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

export class $CrockPotBlockStateProvider extends $BlockStateProvider {
static readonly "WALL_PROPS": $ImmutableMap<($Direction), ($Property<($WallSide)>)>

constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type)

public "crockPotBlock"(arg0: $Block$Type): void
public "foodBlock"(arg0: $Block$Type): void
public "customStageCrossBlock"(arg0: $Block$Type, arg1: $IntegerProperty$Type, arg2: $List$Type<(integer)>, ...arg3: ($Property$Type<(any)>)[]): void
public "customStageCropBlock"(arg0: $Block$Type, arg1: $IntegerProperty$Type, arg2: $List$Type<(integer)>, ...arg3: ($Property$Type<(any)>)[]): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockStateProvider$Type = ($CrockPotBlockStateProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockStateProvider_ = $CrockPotBlockStateProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$MathUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MathUtils {

constructor()

public static "fuzzyIsZero"(arg0: double): boolean
public static "fuzzyIsZero"(arg0: float): boolean
public static "fuzzyEquals"(arg0: double, arg1: double): boolean
public static "fuzzyEquals"(arg0: float, arg1: float): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtils$Type = ($MathUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtils_ = $MathUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/capability/$FoodCounterCapabilityHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$IFoodCounter, $IFoodCounter$Type} from "packages/com/sihenzhang/crockpot/capability/$IFoodCounter"
import {$LivingEntityUseItemEvent$Finish, $LivingEntityUseItemEvent$Finish$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEntityUseItemEvent$Finish"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FoodCounterCapabilityHandler {
static readonly "FOOD_COUNTER_CAPABILITY": $Capability<($IFoodCounter)>
static readonly "FOOD_COUNTER": $ResourceLocation

constructor()

public static "onFoodEaten"(arg0: $LivingEntityUseItemEvent$Finish$Type): void
public static "registerCaps"(arg0: $RegisterCapabilitiesEvent$Type): void
public static "syncFoodCounter"(arg0: $Player$Type): void
public static "onPlayerAppear"(arg0: $EntityJoinLevelEvent$Type): void
public static "onPlayerClone"(arg0: $PlayerEvent$Clone$Type): void
public static "attachPlayerCapability"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodCounterCapabilityHandler$Type = ($FoodCounterCapabilityHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodCounterCapabilityHandler_ = $FoodCounterCapabilityHandler$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCategoryMin" {
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RequirementCategoryMin, $RequirementCategoryMin$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMin"

export class $DrawableRequirementCategoryMin extends $AbstractDrawableRequirement<($RequirementCategoryMin)> {

constructor(arg0: $RequirementCategoryMin$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCategoryMin$Type = ($DrawableRequirementCategoryMin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCategoryMin_ = $DrawableRequirementCategoryMin$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$ModIntegrationJei" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
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

export class $ModIntegrationJei implements $IModPlugin {
static readonly "MOD_ID": string
static readonly "ICONS": $ResourceLocation

constructor()

public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIntegrationJei$Type = ($ModIntegrationJei);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIntegrationJei_ = $ModIntegrationJei$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCategoryMax" {
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$RequirementCategoryMax, $RequirementCategoryMax$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMax"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementCategoryMax extends $AbstractDrawableRequirement<($RequirementCategoryMax)> {

constructor(arg0: $RequirementCategoryMax$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCategoryMax$Type = ($DrawableRequirementCategoryMax);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCategoryMax_ = $DrawableRequirementCategoryMax$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/renderer/entity/layers/$MilkmadeHatLayer" {
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $MilkmadeHatLayer<T extends $LivingEntity, M extends $EntityModel<(T)>> extends $RenderLayer<(T), (M)> {

constructor(arg0: $RenderLayerParent$Type<(T), (M)>, arg1: $EntityModelSet$Type)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: T, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatLayer$Type<T, M> = ($MilkmadeHatLayer<(T), (M)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatLayer_<T, M> = $MilkmadeHatLayer$Type<(T), (M)>;
}}
declare module "packages/com/sihenzhang/crockpot/integration/theoneprobe/$ModIntegrationTheOneProbe" {
import {$InterModEnqueueEvent, $InterModEnqueueEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$InterModEnqueueEvent"

export class $ModIntegrationTheOneProbe {
static readonly "MOD_ID": string
static readonly "METHOD_NAME": string

constructor()

public static "sendIMCMessage"(arg0: $InterModEnqueueEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIntegrationTheOneProbe$Type = ($ModIntegrationTheOneProbe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIntegrationTheOneProbe_ = $ModIntegrationTheOneProbe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/model/$VoltGoatModel" {
import {$QuadrupedModel, $QuadrupedModel$Type} from "packages/net/minecraft/client/model/$QuadrupedModel"
import {$VoltGoat, $VoltGoat$Type} from "packages/com/sihenzhang/crockpot/entity/$VoltGoat"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"

export class $VoltGoatModel<T extends $VoltGoat> extends $QuadrupedModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $ModelPart$Type)

public static "createBodyLayer"(): $LayerDefinition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoltGoatModel$Type<T> = ($VoltGoatModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoltGoatModel_<T> = $VoltGoatModel$Type<(T)>;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ParrotFeedingRecipe, $ParrotFeedingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ParrotFeedingRecipe$Serializer implements $RecipeSerializer<($ParrotFeedingRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $ParrotFeedingRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $ParrotFeedingRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $ParrotFeedingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $ParrotFeedingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParrotFeedingRecipe$Serializer$Type = ($ParrotFeedingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotFeedingRecipe$Serializer_ = $ParrotFeedingRecipe$Serializer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/$CrockPotConfigs" {
import {$ForgeConfigSpec$DoubleValue, $ForgeConfigSpec$DoubleValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$DoubleValue"
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"

export class $CrockPotConfigs {
static readonly "COMMON_CONFIG": $ForgeConfigSpec
static readonly "CLIENT_CONFIG": $ForgeConfigSpec
static readonly "CROCK_POT_SPEED_MODIFIER": $ForgeConfigSpec$DoubleValue
static readonly "SHOW_FOOD_VALUES_TOOLTIP": $ForgeConfigSpec$BooleanValue
static readonly "SHOW_FOOD_EFFECTS_TOOLTIP": $ForgeConfigSpec$BooleanValue
static readonly "GNAWS_GIFT_HUNGER_OVERLAY": $ForgeConfigSpec$BooleanValue

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotConfigs$Type = ($CrockPotConfigs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotConfigs_ = $CrockPotConfigs$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$CrockPotRecipes" {
import {$PiglinBarteringRecipe, $PiglinBarteringRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$PiglinBarteringRecipe"
import {$CrockPotCookingRecipe, $CrockPotCookingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ExplosionCraftingRecipe, $ExplosionCraftingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe"
import {$ParrotFeedingRecipe, $ParrotFeedingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe"
import {$FoodValuesDefinition, $FoodValuesDefinition$Type} from "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPotRecipes {
static readonly "RECIPE_TYPES": $DeferredRegister<($RecipeType<(any)>)>
static readonly "RECIPE_SERIALIZERS": $DeferredRegister<($RecipeSerializer<(any)>)>
static readonly "CROCK_POT_COOKING": string
static readonly "EXPLOSION_CRAFTING": string
static readonly "FOOD_VALUES": string
static readonly "PARROT_FEEDING": string
static readonly "PIGLIN_BARTERING": string
static readonly "CROCK_POT_COOKING_RECIPE_TYPE": $RegistryObject<($RecipeType<($CrockPotCookingRecipe)>)>
static readonly "EXPLOSION_CRAFTING_RECIPE_TYPE": $RegistryObject<($RecipeType<($ExplosionCraftingRecipe)>)>
static readonly "FOOD_VALUES_RECIPE_TYPE": $RegistryObject<($RecipeType<($FoodValuesDefinition)>)>
static readonly "PARROT_FEEDING_RECIPE_TYPE": $RegistryObject<($RecipeType<($ParrotFeedingRecipe)>)>
static readonly "PIGLIN_BARTERING_RECIPE_TYPE": $RegistryObject<($RecipeType<($PiglinBarteringRecipe)>)>
static readonly "CROCK_POT_COOKING_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($CrockPotCookingRecipe)>)>
static readonly "EXPLOSION_CRAFTING_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($ExplosionCraftingRecipe)>)>
static readonly "FOOD_VALUES_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($FoodValuesDefinition)>)>
static readonly "PARROT_FEEDING_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($ParrotFeedingRecipe)>)>
static readonly "PIGLIN_BARTERING_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($PiglinBarteringRecipe)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotRecipes$Type = ($CrockPotRecipes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotRecipes_ = $CrockPotRecipes$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/$StepTickTimer" {
import {$ITickTimer, $ITickTimer$Type} from "packages/mezz/jei/api/gui/$ITickTimer"

export class $StepTickTimer implements $ITickTimer {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: boolean)

public "getValue"(): integer
public "getMaxValue"(): integer
get "value"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StepTickTimer$Type = ($StepTickTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StepTickTimer_ = $StepTickTimer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$ExplosionCraftingRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AbstractRecipeBuilder, $AbstractRecipeBuilder$Type} from "packages/com/sihenzhang/crockpot/data/recipes/$AbstractRecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $ExplosionCraftingRecipeBuilder extends $AbstractRecipeBuilder {

constructor(arg0: $ItemLike$Type, arg1: integer, arg2: $Ingredient$Type)

public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "lossRate"(arg0: float): $ExplosionCraftingRecipeBuilder
public "onlyBlock"(): $ExplosionCraftingRecipeBuilder
public "getResult"(): $Item
public static "explosionCrafting"(arg0: $ItemLike$Type, arg1: integer, arg2: $Ingredient$Type): $ExplosionCraftingRecipeBuilder
public static "explosionCrafting"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $ExplosionCraftingRecipeBuilder
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingRecipeBuilder$Type = ($ExplosionCraftingRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipeBuilder_ = $ExplosionCraftingRecipeBuilder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/entity/$CrockPotBlockEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $CrockPotBlockEntity extends $BlockEntity implements $MenuProvider {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getResult"(): $ItemStack
public "getDisplayName"(): $Component
public "isBurning"(): boolean
public "isValidIngredient"(arg0: $ItemStack$Type): boolean
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "startOpen"(arg0: $Player$Type): void
public "recheckOpen"(): void
public "stopOpen"(arg0: $Player$Type): void
public "load"(arg0: $CompoundTag$Type): void
public "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
public "getItemHandler"(): $ItemStackHandler
public "isCooking"(): boolean
public "getPotLevel"(): integer
public static "serverTick"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $CrockPotBlockEntity$Type): void
public "getRecipeWrapper"(): $CrockPotCookingRecipe$Wrapper
public static "isFuel"(arg0: $ItemStack$Type): boolean
public "getBurningProgress"(): float
public "getCookingProgress"(): float
public "getUpdateTag"(): $CompoundTag
public "getUpdatePacket"(): $Packet<($ClientGamePacketListener)>
get "result"(): $ItemStack
get "displayName"(): $Component
get "burning"(): boolean
get "itemHandler"(): $ItemStackHandler
get "cooking"(): boolean
get "potLevel"(): integer
get "recipeWrapper"(): $CrockPotCookingRecipe$Wrapper
get "burningProgress"(): float
get "cookingProgress"(): float
get "updateTag"(): $CompoundTag
get "updatePacket"(): $Packet<($ClientGamePacketListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockEntity$Type = ($CrockPotBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockEntity_ = $CrockPotBlockEntity$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$FoodValuesDefinition, $FoodValuesDefinition$Type} from "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $FoodValuesDefinition$Serializer implements $RecipeSerializer<($FoodValuesDefinition)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $FoodValuesDefinition
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $FoodValuesDefinition
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $FoodValuesDefinition$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $FoodValuesDefinition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValuesDefinition$Serializer$Type = ($FoodValuesDefinition$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesDefinition$Serializer_ = $FoodValuesDefinition$Serializer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$CornBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AbstractCrockPotDoubleCropBlock, $AbstractCrockPotDoubleCropBlock$Type} from "packages/com/sihenzhang/crockpot/block/$AbstractCrockPotDoubleCropBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CornBlock extends $AbstractCrockPotDoubleCropBlock {
static readonly "MAX_AGE": integer
static readonly "AGE": $IntegerProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()

public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CornBlock$Type = ($CornBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CornBlock_ = $CornBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$ParrotLayingEggsRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper, $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type} from "packages/com/sihenzhang/crockpot/integration/jei/$ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $ParrotLayingEggsRecipeCategory implements $IRecipeCategory<($ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper)> {
static readonly "RECIPE_TYPE": $RecipeType<($ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper)>
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type): boolean
public "handleInput"(arg0: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper)>
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
export type $ParrotLayingEggsRecipeCategory$Type = ($ParrotLayingEggsRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotLayingEggsRecipeCategory_ = $ParrotLayingEggsRecipeCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$EntityAttributeEvent" {
import {$EntityAttributeCreationEvent, $EntityAttributeCreationEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityAttributeCreationEvent"

export class $EntityAttributeEvent {

constructor()

public static "onAttributeCreate"(arg0: $EntityAttributeCreationEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributeEvent$Type = ($EntityAttributeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributeEvent_ = $EntityAttributeEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/base/$FoodValues" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FoodValues {


public "remove"(arg0: $FoodCategory$Type): void
public "get"(arg0: $FoodCategory$Type): float
public "put"(arg0: $FoodCategory$Type, arg1: float): void
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public static "of"(...arg0: (any)[]): $FoodValues
public static "of"(arg0: $Map$Type<($FoodCategory$Type), (float)>): $FoodValues
public static "of"(...arg0: ($Pair$Type<($FoodCategory$Type), (float)>)[]): $FoodValues
public static "merge"(arg0: $Collection$Type<($FoodValues$Type)>): $FoodValues
public "entrySet"(): $Set<($Pair<($FoodCategory), (float)>)>
public "set"(arg0: $FoodCategory$Type, arg1: float): $FoodValues
public static "create"(): $FoodValues
public "has"(arg0: $FoodCategory$Type): boolean
public static "fromJson"(arg0: $JsonElement$Type): $FoodValues
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $FoodValues
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValues$Type = ($FoodValues);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValues_ = $FoodValues$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$GoatConversionEvent" {
import {$EntityStruckByLightningEvent, $EntityStruckByLightningEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityStruckByLightningEvent"

export class $GoatConversionEvent {

constructor()

public static "onGoatStruckByLightning"(arg0: $EntityStruckByLightningEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GoatConversionEvent$Type = ($GoatConversionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GoatConversionEvent_ = $GoatConversionEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$PowCakeBlock" {
import {$CrockPotFoodBlock, $CrockPotFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$CrockPotFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $PowCakeBlock extends $CrockPotFoodBlock {
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PowCakeBlock$Type = ($PowCakeBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PowCakeBlock_ = $PowCakeBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$CrockPotFoodBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$HorizontalDirectionalBlock, $HorizontalDirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$HorizontalDirectionalBlock"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CrockPotFoodBlock extends $HorizontalDirectionalBlock {
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotFoodBlock$Type = ($CrockPotFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotFoodBlock_ = $CrockPotFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$MilkmadeHatRepairEvent" {
import {$AnvilUpdateEvent, $AnvilUpdateEvent$Type} from "packages/net/minecraftforge/event/$AnvilUpdateEvent"

export class $MilkmadeHatRepairEvent {

constructor()

public static "onAnvilUpdate"(arg0: $AnvilUpdateEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatRepairEvent$Type = ($MilkmadeHatRepairEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatRepairEvent_ = $MilkmadeHatRepairEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition$FoodCategoryMatchedItems" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $FoodValuesDefinition$FoodCategoryMatchedItems extends $Record {

constructor(category: $FoodCategory$Type, items: $Set$Type<($ItemStack$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "category"(): $FoodCategory
public "items"(): $Set<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValuesDefinition$FoodCategoryMatchedItems$Type = ($FoodValuesDefinition$FoodCategoryMatchedItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesDefinition$FoodCategoryMatchedItems_ = $FoodValuesDefinition$FoodCategoryMatchedItems$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/entity/$CrockPotBlockEntities" {
import {$BirdcageBlockEntity, $BirdcageBlockEntity$Type} from "packages/com/sihenzhang/crockpot/block/entity/$BirdcageBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$CrockPotBlockEntity, $CrockPotBlockEntity$Type} from "packages/com/sihenzhang/crockpot/block/entity/$CrockPotBlockEntity"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPotBlockEntities {
static readonly "BLOCK_ENTITIES": $DeferredRegister<($BlockEntityType<(any)>)>
static readonly "CROCK_POT_BLOCK_ENTITY": $RegistryObject<($BlockEntityType<($CrockPotBlockEntity)>)>
static readonly "BIRDCAGE_BLOCK_ENTITY": $RegistryObject<($BlockEntityType<($BirdcageBlockEntity)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockEntities$Type = ($CrockPotBlockEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockEntities_ = $CrockPotBlockEntities$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCategoryMinExclusive" {
import {$RequirementCategoryMinExclusive, $RequirementCategoryMinExclusive$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMinExclusive"
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementCategoryMinExclusive extends $AbstractDrawableRequirement<($RequirementCategoryMinExclusive)> {

constructor(arg0: $RequirementCategoryMinExclusive$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCategoryMinExclusive$Type = ($DrawableRequirementCategoryMinExclusive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCategoryMinExclusive_ = $DrawableRequirementCategoryMinExclusive$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper" {
import {$FoodValues, $FoodValues$Type} from "packages/com/sihenzhang/crockpot/base/$FoodValues"
import {$SimpleContainer, $SimpleContainer$Type} from "packages/net/minecraft/world/$SimpleContainer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CrockPotCookingRecipe$Wrapper extends $SimpleContainer {

constructor(arg0: $List$Type<($ItemStack$Type)>, arg1: $FoodValues$Type, arg2: integer)

public "getPotLevel"(): integer
public "getFoodValues"(): $FoodValues
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "potLevel"(): integer
get "foodValues"(): $FoodValues
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCookingRecipe$Wrapper$Type = ($CrockPotCookingRecipe$Wrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipe$Wrapper_ = $CrockPotCookingRecipe$Wrapper$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jade/$BirdcageProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $BirdcageProvider extends $Enum<($BirdcageProvider)> implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $BirdcageProvider


public static "values"(): ($BirdcageProvider)[]
public static "valueOf"(arg0: string): $BirdcageProvider
public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BirdcageProvider$Type = (("instance")) | ($BirdcageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BirdcageProvider_ = $BirdcageProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$CrockPot2StacksFoodBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$AbstractStackableFoodBlock, $AbstractStackableFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$AbstractStackableFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CrockPot2StacksFoodBlock extends $AbstractStackableFoodBlock {
static readonly "STACKS": $IntegerProperty
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getMaxStacks"(): integer
public "getStacksProperty"(): $IntegerProperty
get "maxStacks"(): integer
get "stacksProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPot2StacksFoodBlock$Type = ($CrockPot2StacksFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPot2StacksFoodBlock_ = $CrockPot2StacksFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$CrockPot6StacksFoodBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$AbstractStackableFoodBlock, $AbstractStackableFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$AbstractStackableFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CrockPot6StacksFoodBlock extends $AbstractStackableFoodBlock {
static readonly "STACKS": $IntegerProperty
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getMaxStacks"(): integer
public "getStacksProperty"(): $IntegerProperty
get "maxStacks"(): integer
get "stacksProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPot6StacksFoodBlock$Type = ($CrockPot6StacksFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPot6StacksFoodBlock_ = $CrockPot6StacksFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$ExplosionCraftingRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$ExplosionCraftingRecipe, $ExplosionCraftingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $ExplosionCraftingRecipeCategory implements $IRecipeCategory<($ExplosionCraftingRecipe)> {
static readonly "RECIPE_TYPE": $RecipeType<($ExplosionCraftingRecipe)>

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($ExplosionCraftingRecipe)>
public "draw"(arg0: $ExplosionCraftingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $ExplosionCraftingRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(arg0: $ExplosionCraftingRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $ExplosionCraftingRecipe$Type): boolean
public "handleInput"(arg0: $ExplosionCraftingRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getRegistryName"(arg0: $ExplosionCraftingRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($ExplosionCraftingRecipe)>
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
export type $ExplosionCraftingRecipeCategory$Type = ($ExplosionCraftingRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipeCategory_ = $ExplosionCraftingRecipeCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMaxExclusive" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"

export class $RequirementCategoryMaxExclusive implements $IRequirement {

constructor(arg0: $FoodCategory$Type, arg1: float)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getMax"(): float
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCategoryMaxExclusive
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCategoryMaxExclusive
public "getCategory"(): $FoodCategory
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "max"(): float
get "category"(): $FoodCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCategoryMaxExclusive$Type = ($RequirementCategoryMaxExclusive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCategoryMaxExclusive_ = $RequirementCategoryMaxExclusive$Type;
}}
declare module "packages/com/sihenzhang/crockpot/$CrockPot" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPot {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOGGER": $Logger
static readonly "CREATIVE_MODE_TABS": $DeferredRegister<($CreativeModeTab)>
static readonly "TAB": $RegistryObject<($CreativeModeTab)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPot$Type = ($CrockPot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPot_ = $CrockPot$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$ParrotEggItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Parrot$Variant, $Parrot$Variant$Type} from "packages/net/minecraft/world/entity/animal/$Parrot$Variant"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CrockPotBaseItem, $CrockPotBaseItem$Type} from "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ParrotEggItem extends $CrockPotBaseItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Parrot$Variant$Type)

public "getVariant"(): $Parrot$Variant
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
get "variant"(): $Parrot$Variant
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParrotEggItem$Type = ($ParrotEggItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotEggItem_ = $ParrotEggItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$FoodValuesDefinitionJS" {
import {$AbstractCrockPotRecipeJS, $AbstractCrockPotRecipeJS$Type} from "packages/com/sihenzhang/crockpot/integration/kubejs/$AbstractCrockPotRecipeJS"

export class $FoodValuesDefinitionJS extends $AbstractCrockPotRecipeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValuesDefinitionJS$Type = ($FoodValuesDefinitionJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesDefinitionJS_ = $FoodValuesDefinitionJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$AbstractStackableFoodBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$CrockPotFoodBlock, $CrockPotFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$CrockPotFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $AbstractStackableFoodBlock extends $CrockPotFoodBlock {
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getMaxStacks"(): integer
public "getStacksProperty"(): $IntegerProperty
get "maxStacks"(): integer
get "stacksProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractStackableFoodBlock$Type = ($AbstractStackableFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractStackableFoodBlock_ = $AbstractStackableFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCategoryMaxExclusive" {
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$RequirementCategoryMaxExclusive, $RequirementCategoryMaxExclusive$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMaxExclusive"

export class $DrawableRequirementCategoryMaxExclusive extends $AbstractDrawableRequirement<($RequirementCategoryMaxExclusive)> {

constructor(arg0: $RequirementCategoryMaxExclusive$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCategoryMaxExclusive$Type = ($DrawableRequirementCategoryMaxExclusive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCategoryMaxExclusive_ = $DrawableRequirementCategoryMaxExclusive$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$NetherosiaItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$CrockPotBaseItem, $CrockPotBaseItem$Type} from "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $NetherosiaItem extends $CrockPotBaseItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetherosiaItem$Type = ($NetherosiaItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetherosiaItem_ = $NetherosiaItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/base/$CrockPotDamageTypes" {
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $CrockPotDamageTypes {
static readonly "CANDY": $ResourceKey<($DamageType)>
static readonly "MONSTER_FOOD": $ResourceKey<($DamageType)>
static readonly "POW_CAKE": $ResourceKey<($DamageType)>
static readonly "SPICY": $ResourceKey<($DamageType)>
static readonly "TAFFY": $ResourceKey<($DamageType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotDamageTypes$Type = ($CrockPotDamageTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotDamageTypes_ = $CrockPotDamageTypes$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$CrockPotBlocks" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPotBlocks {
static readonly "BLOCKS": $DeferredRegister<($Block)>
static readonly "CROCK_POT": $RegistryObject<($Block)>
static readonly "PORTABLE_CROCK_POT": $RegistryObject<($Block)>
static readonly "BIRDCAGE": $RegistryObject<($Block)>
static readonly "UNKNOWN_CROPS": $RegistryObject<($Block)>
static readonly "ASPARAGUS": $RegistryObject<($Block)>
static readonly "CORN": $RegistryObject<($Block)>
static readonly "EGGPLANT": $RegistryObject<($Block)>
static readonly "GARLIC": $RegistryObject<($Block)>
static readonly "ONION": $RegistryObject<($Block)>
static readonly "PEPPER": $RegistryObject<($Block)>
static readonly "TOMATO": $RegistryObject<($Block)>
static readonly "ASPARAGUS_SOUP": $RegistryObject<($Block)>
static readonly "AVAJ": $RegistryObject<($Block)>
static readonly "BACON_EGGS": $RegistryObject<($Block)>
static readonly "BONE_SOUP": $RegistryObject<($Block)>
static readonly "BONE_STEW": $RegistryObject<($Block)>
static readonly "BREAKFAST_SKILLET": $RegistryObject<($Block)>
static readonly "BUNNY_STEW": $RegistryObject<($Block)>
static readonly "CALIFORNIA_ROLL": $RegistryObject<($Block)>
static readonly "CANDY": $RegistryObject<($Block)>
static readonly "CEVICHE": $RegistryObject<($Block)>
static readonly "FISH_STICKS": $RegistryObject<($Block)>
static readonly "FISH_TACOS": $RegistryObject<($Block)>
static readonly "FLOWER_SALAD": $RegistryObject<($Block)>
static readonly "FROGGLE_BUNWICH": $RegistryObject<($Block)>
static readonly "FRUIT_MEDLEY": $RegistryObject<($Block)>
static readonly "GAZPACHO": $RegistryObject<($Block)>
static readonly "GLOW_BERRY_MOUSSE": $RegistryObject<($Block)>
static readonly "HONEY_HAM": $RegistryObject<($Block)>
static readonly "HONEY_NUGGETS": $RegistryObject<($Block)>
static readonly "HOT_CHILI": $RegistryObject<($Block)>
static readonly "HOT_COCOA": $RegistryObject<($Block)>
static readonly "ICE_CREAM": $RegistryObject<($Block)>
static readonly "ICED_TEA": $RegistryObject<($Block)>
static readonly "JAMMY_PRESERVES": $RegistryObject<($Block)>
static readonly "KABOBS": $RegistryObject<($Block)>
static readonly "MASHED_POTATOES": $RegistryObject<($Block)>
static readonly "MEAT_BALLS": $RegistryObject<($Block)>
static readonly "MONSTER_LASAGNA": $RegistryObject<($Block)>
static readonly "MONSTER_TARTARE": $RegistryObject<($Block)>
static readonly "MOQUECA": $RegistryObject<($Block)>
static readonly "MUSHY_CAKE": $RegistryObject<($Block)>
static readonly "PEPPER_POPPER": $RegistryObject<($Block)>
static readonly "PEROGIES": $RegistryObject<($Block)>
static readonly "PLAIN_OMELETTE": $RegistryObject<($Block)>
static readonly "POTATO_SOUFFLE": $RegistryObject<($Block)>
static readonly "POTATO_TORNADO": $RegistryObject<($Block)>
static readonly "POW_CAKE": $RegistryObject<($Block)>
static readonly "PUMPKIN_COOKIE": $RegistryObject<($Block)>
static readonly "RATATOUILLE": $RegistryObject<($Block)>
static readonly "SALMON_SUSHI": $RegistryObject<($Block)>
static readonly "SALSA": $RegistryObject<($Block)>
static readonly "SCOTCH_EGG": $RegistryObject<($Block)>
static readonly "SEAFOOD_GUMBO": $RegistryObject<($Block)>
static readonly "STUFFED_EGGPLANT": $RegistryObject<($Block)>
static readonly "SURF_N_TURF": $RegistryObject<($Block)>
static readonly "TAFFY": $RegistryObject<($Block)>
static readonly "TEA": $RegistryObject<($Block)>
static readonly "TROPICAL_BOUILLABAISSE": $RegistryObject<($Block)>
static readonly "TURKEY_DINNER": $RegistryObject<($Block)>
static readonly "VEG_STINGER": $RegistryObject<($Block)>
static readonly "VOLT_GOAT_JELLY": $RegistryObject<($Block)>
static readonly "WATERMELON_ICLE": $RegistryObject<($Block)>
static readonly "WET_GOOP": $RegistryObject<($Block)>
static readonly "FOODS": $Supplier<($List<($Block)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlocks$Type = ($CrockPotBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlocks_ = $CrockPotBlocks$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$RangedItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $RangedItem {
readonly "item": $Item
readonly "min": integer
readonly "max": integer

constructor(arg0: $Item$Type, arg1: integer, arg2: integer)
constructor(arg0: $Item$Type, arg1: integer)

public "getInstance"(arg0: $RandomSource$Type): $ItemStack
public static "fromJson"(arg0: $JsonElement$Type): $RangedItem
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RangedItem
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "isRanged"(): boolean
get "ranged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RangedItem$Type = ($RangedItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RangedItem_ = $RangedItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$AbstractRecipe, $AbstractRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CrockPotCookingRecipe extends $AbstractRecipe<($CrockPotCookingRecipe$Wrapper)> {

constructor(arg0: $ResourceLocation$Type, arg1: $List$Type<($IRequirement$Type)>, arg2: $ItemStack$Type, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "matches"(arg0: $CrockPotCookingRecipe$Wrapper$Type, arg1: $Level$Type): boolean
public "getPriority"(): integer
public "getResult"(): $ItemStack
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getCookingTime"(): integer
public static "getRecipeFor"(arg0: $CrockPotCookingRecipe$Wrapper$Type, arg1: $Level$Type): $Optional<($CrockPotCookingRecipe)>
public "getWeight"(): integer
public "getToastSymbol"(): $ItemStack
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CrockPotCookingRecipe$Wrapper$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getPotLevel"(): integer
public "getRequirements"(): $List<($IRequirement)>
get "priority"(): integer
get "result"(): $ItemStack
get "cookingTime"(): integer
get "weight"(): integer
get "toastSymbol"(): $ItemStack
get "serializer"(): $RecipeSerializer<(any)>
get "potLevel"(): integer
get "requirements"(): $List<($IRequirement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCookingRecipe$Type = ($CrockPotCookingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipe_ = $CrockPotCookingRecipe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/curios/$ModIntegrationCurios" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $ModIntegrationCurios {
static readonly "MOD_ID": string

constructor()

public static "onClientSetupEvent"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIntegrationCurios$Type = ($ModIntegrationCurios);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIntegrationCurios_ = $ModIntegrationCurios$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$JsonUtils" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $JsonUtils {
static readonly "GSON": $Gson

constructor()

public static "getAsEnum"<E extends $Enum<(E)>>(arg0: $JsonObject$Type, arg1: string, arg2: $Class$Type<(E)>): E
public static "getAsIngredient"(arg0: $JsonObject$Type, arg1: string): $Ingredient
public static "getAsIngredient"(arg0: $JsonObject$Type, arg1: string, arg2: boolean): $Ingredient
public static "getAsItem"(arg0: $JsonObject$Type, arg1: string): $Item
public static "convertToItem"(arg0: $JsonElement$Type, arg1: string): $Item
public static "convertToEnum"<E extends $Enum<(E)>>(arg0: $JsonElement$Type, arg1: string, arg2: $Class$Type<(E)>): E
public static "convertToItemStack"(arg0: $JsonElement$Type, arg1: string): $ItemStack
public static "getAsItemStack"(arg0: $JsonObject$Type, arg1: string): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonUtils$Type = ($JsonUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonUtils_ = $JsonUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$CrockPotEffectsEvent" {
import {$MobEffectEvent$Applicable, $MobEffectEvent$Applicable$Type} from "packages/net/minecraftforge/event/entity/living/$MobEffectEvent$Applicable"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"
import {$MobEffectEvent$Added, $MobEffectEvent$Added$Type} from "packages/net/minecraftforge/event/entity/living/$MobEffectEvent$Added"
import {$PlayerInteractEvent$RightClickItem, $PlayerInteractEvent$RightClickItem$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickItem"

export class $CrockPotEffectsEvent {

constructor()

public static "onFoodRightClick"(arg0: $PlayerInteractEvent$RightClickItem$Type): void
public static "onWitherResistanceEffectAdded"(arg0: $MobEffectEvent$Added$Type): void
public static "onWitherEffectApply"(arg0: $MobEffectEvent$Applicable$Type): void
public static "onLivingEntityAttacked"(arg0: $LivingHurtEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotEffectsEvent$Type = ($CrockPotEffectsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotEffectsEvent_ = $CrockPotEffectsEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ExplosionCraftingRecipe, $ExplosionCraftingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $ExplosionCraftingRecipe$Serializer implements $RecipeSerializer<($ExplosionCraftingRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $ExplosionCraftingRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $ExplosionCraftingRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $ExplosionCraftingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $ExplosionCraftingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingRecipe$Serializer$Type = ($ExplosionCraftingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipe$Serializer_ = $ExplosionCraftingRecipe$Serializer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$PiglinBarteringRecipe$Serializer" {
import {$PiglinBarteringRecipe, $PiglinBarteringRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$PiglinBarteringRecipe"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $PiglinBarteringRecipe$Serializer implements $RecipeSerializer<($PiglinBarteringRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $PiglinBarteringRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $PiglinBarteringRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $PiglinBarteringRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $PiglinBarteringRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringRecipe$Serializer$Type = ($PiglinBarteringRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringRecipe$Serializer_ = $PiglinBarteringRecipe$Serializer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotRecipeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RecipeProvider, $RecipeProvider$Type} from "packages/net/minecraft/data/recipes/$RecipeProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"

export class $CrockPotRecipeProvider extends $RecipeProvider {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotRecipeProvider$Type = ($CrockPotRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotRecipeProvider_ = $CrockPotRecipeProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotAdvancementProvider" {
import {$ForgeAdvancementProvider, $ForgeAdvancementProvider$Type} from "packages/net/minecraftforge/common/data/$ForgeAdvancementProvider"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $CrockPotAdvancementProvider extends $ForgeAdvancementProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotAdvancementProvider$Type = ($CrockPotAdvancementProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotAdvancementProvider_ = $CrockPotAdvancementProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$AbstractCrockPotCropBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$CropBlock, $CropBlock$Type} from "packages/net/minecraft/world/level/block/$CropBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AbstractCrockPotCropBlock extends $CropBlock {
static readonly "MAX_AGE": integer
static readonly "AGE": $IntegerProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCrockPotCropBlock$Type = ($AbstractCrockPotCropBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCrockPotCropBlock_ = $AbstractCrockPotCropBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$MilkmadeHatItem" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CrockPotBaseItem, $CrockPotBaseItem$Type} from "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MilkmadeHatItem extends $CrockPotBaseItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "getEquipmentSlot"(arg0: $ItemStack$Type): $EquipmentSlot
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "isEnchantable"(arg0: $ItemStack$Type): boolean
public "isBookEnchantable"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
public "onArmorTick"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $Player$Type): void
public "canEquip"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Entity$Type): boolean
public "initCapabilities"(arg0: $ItemStack$Type, arg1: $CompoundTag$Type): $ICapabilityProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatItem$Type = ($MilkmadeHatItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatItem_ = $MilkmadeHatItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotItemTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$TagsProvider$TagLookup, $TagsProvider$TagLookup$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagLookup"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ItemTagsProvider, $ItemTagsProvider$Type} from "packages/net/minecraft/data/tags/$ItemTagsProvider"

export class $CrockPotItemTagsProvider extends $ItemTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $CompletableFuture$Type<($TagsProvider$TagLookup$Type<($Block$Type)>)>, arg3: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotItemTagsProvider$Type = ($CrockPotItemTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotItemTagsProvider_ = $CrockPotItemTagsProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$AnimalsFollowPowcakeEvent" {
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"

export class $AnimalsFollowPowcakeEvent {

constructor()

public static "onAnimalAppear"(arg0: $EntityJoinLevelEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimalsFollowPowcakeEvent$Type = ($AnimalsFollowPowcakeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimalsFollowPowcakeEvent_ = $AnimalsFollowPowcakeEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$CrockPotCookingRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AbstractRecipeBuilder, $AbstractRecipeBuilder$Type} from "packages/com/sihenzhang/crockpot/data/recipes/$AbstractRecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $CrockPotCookingRecipeBuilder extends $AbstractRecipeBuilder {

constructor(arg0: $ItemLike$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer)

public static "crockPotCooking"(arg0: $ItemLike$Type, arg1: integer, arg2: integer, arg3: integer): $CrockPotCookingRecipeBuilder
public static "crockPotCooking"(arg0: $ItemLike$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $CrockPotCookingRecipeBuilder
public "requirementCategoryMinExclusive"(arg0: $FoodCategory$Type, arg1: float): $CrockPotCookingRecipeBuilder
public "requirementCombinationAnd"(arg0: $IRequirement$Type, arg1: $IRequirement$Type): $CrockPotCookingRecipeBuilder
public "requirementCombinationOr"(arg0: $IRequirement$Type, arg1: $IRequirement$Type): $CrockPotCookingRecipeBuilder
public "requirementMustContainIngredient"(arg0: $Ingredient$Type, arg1: integer): $CrockPotCookingRecipeBuilder
public "requirementMustContainIngredient"(arg0: $Ingredient$Type): $CrockPotCookingRecipeBuilder
public "requirementCategoryMax"(arg0: $FoodCategory$Type, arg1: float): $CrockPotCookingRecipeBuilder
public "requirementCategoryMaxExclusive"(arg0: $FoodCategory$Type, arg1: float): $CrockPotCookingRecipeBuilder
public "requirementCategoryMin"(arg0: $FoodCategory$Type, arg1: float): $CrockPotCookingRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "weight"(arg0: integer): $CrockPotCookingRecipeBuilder
public "getResult"(): $Item
public "requirement"(arg0: $IRequirement$Type): $CrockPotCookingRecipeBuilder
public "requirementMustContainIngredientLessThan"(arg0: $Ingredient$Type): $CrockPotCookingRecipeBuilder
public "requirementMustContainIngredientLessThan"(arg0: $Ingredient$Type, arg1: integer): $CrockPotCookingRecipeBuilder
public "requirementWithAnyCategory"(arg0: $FoodCategory$Type): $CrockPotCookingRecipeBuilder
public "requirementWithoutCategory"(arg0: $FoodCategory$Type): $CrockPotCookingRecipeBuilder
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCookingRecipeBuilder$Type = ($CrockPotCookingRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipeBuilder_ = $CrockPotCookingRecipeBuilder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition" {
import {$AbstractRecipe, $AbstractRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FoodValuesDefinition$FoodCategoryMatchedItems, $FoodValuesDefinition$FoodCategoryMatchedItems$Type} from "packages/com/sihenzhang/crockpot/recipe/$FoodValuesDefinition$FoodCategoryMatchedItems"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$FoodValues, $FoodValues$Type} from "packages/com/sihenzhang/crockpot/base/$FoodValues"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $FoodValuesDefinition extends $AbstractRecipe<($Container)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Set$Type<($ResourceLocation$Type)>, arg2: $FoodValues$Type, arg3: boolean)

public static "getMatchedItems"(arg0: $FoodCategory$Type, arg1: $Level$Type): $Set<($ItemStack)>
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "isItem"(): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $Container$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getFoodValues"(): $FoodValues
public static "getFoodValues"(arg0: $ItemStack$Type, arg1: $Level$Type): $FoodValues
public "getNames"(): $Set<($ResourceLocation)>
public static "getFoodCategoryMatchedItemsList"(arg0: $Level$Type): $List<($FoodValuesDefinition$FoodCategoryMatchedItems)>
public "matches"(arg0: $Container$Type, arg1: $Level$Type): boolean
get "item"(): boolean
get "serializer"(): $RecipeSerializer<(any)>
get "foodValues"(): $FoodValues
get "names"(): $Set<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValuesDefinition$Type = ($FoodValuesDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesDefinition_ = $FoodValuesDefinition$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$GnawsCoinSoulboundEvent" {
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$LivingDropsEvent, $LivingDropsEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDropsEvent"

export class $GnawsCoinSoulboundEvent {

constructor()

public static "onPlayerDrops"(arg0: $LivingDropsEvent$Type): void
public static "onPlayerClone"(arg0: $PlayerEvent$Clone$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GnawsCoinSoulboundEvent$Type = ($GnawsCoinSoulboundEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GnawsCoinSoulboundEvent_ = $GnawsCoinSoulboundEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$ParrotFeedingRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AbstractRecipeBuilder, $AbstractRecipeBuilder$Type} from "packages/com/sihenzhang/crockpot/data/recipes/$AbstractRecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $ParrotFeedingRecipeBuilder extends $AbstractRecipeBuilder {

constructor(arg0: $ItemLike$Type, arg1: integer, arg2: integer, arg3: $Ingredient$Type)

public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "getResult"(): $Item
public static "parrotFeeding"(arg0: $Ingredient$Type, arg1: $ItemLike$Type): $ParrotFeedingRecipeBuilder
public static "parrotFeeding"(arg0: $Ingredient$Type, arg1: $ItemLike$Type, arg2: integer): $ParrotFeedingRecipeBuilder
public static "parrotFeeding"(arg0: $Ingredient$Type, arg1: $ItemLike$Type, arg2: integer, arg3: integer): $ParrotFeedingRecipeBuilder
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParrotFeedingRecipeBuilder$Type = ($ParrotFeedingRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotFeedingRecipeBuilder_ = $ParrotFeedingRecipeBuilder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementMustContainIngredientLessThan" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $RequirementMustContainIngredientLessThan implements $IRequirement {

constructor(arg0: $Ingredient$Type, arg1: integer)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public static "fromJson"(arg0: $JsonObject$Type): $RequirementMustContainIngredientLessThan
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementMustContainIngredientLessThan
public "getIngredient"(): $Ingredient
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public "getQuantity"(): integer
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "ingredient"(): $Ingredient
get "quantity"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementMustContainIngredientLessThan$Type = ($RequirementMustContainIngredientLessThan);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementMustContainIngredientLessThan_ = $RequirementMustContainIngredientLessThan$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AbstractDrawableRequirement<T extends $IRequirement> implements $IDrawable {


public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public static "createDrawable"(arg0: $IRequirement$Type): $AbstractDrawableRequirement<(any)>
public "getInvisibleInputs"(): $List<($ItemStack)>
public static "getDrawables"(arg0: $List$Type<($IRequirement$Type)>): $List<($AbstractDrawableRequirement<(any)>)>
public "draw"(arg0: $GuiGraphics$Type): void
public "getWidth"(): integer
public "getHeight"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDrawableRequirement$Type<T> = ($AbstractDrawableRequirement<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDrawableRequirement_<T> = $AbstractDrawableRequirement$Type<(T)>;
}}
declare module "packages/com/sihenzhang/crockpot/data/$DataGen" {
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"

export class $DataGen {

constructor()

public static "gatherData"(arg0: $GatherDataEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataGen$Type = ($DataGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataGen_ = $DataGen$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/renderer/entity/$EmptyRenderer" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EmptyRenderer<T extends $Entity> extends $EntityRenderer<(T)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "shouldRender"(arg0: T, arg1: $Frustum$Type, arg2: double, arg3: double, arg4: double): boolean
public "getTextureLocation"(arg0: T): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmptyRenderer$Type<T> = ($EmptyRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyRenderer_<T> = $EmptyRenderer$Type<(T)>;
}}
declare module "packages/com/sihenzhang/crockpot/advancement/$PiglinBarteringTrigger" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SimpleCriterionTrigger, $SimpleCriterionTrigger$Type} from "packages/net/minecraft/advancements/critereon/$SimpleCriterionTrigger"
import {$PiglinBarteringTrigger$Instance, $PiglinBarteringTrigger$Instance$Type} from "packages/com/sihenzhang/crockpot/advancement/$PiglinBarteringTrigger$Instance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PiglinBarteringTrigger extends $SimpleCriterionTrigger<($PiglinBarteringTrigger$Instance)> {

constructor()

public "trigger"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type): void
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringTrigger$Type = ($PiglinBarteringTrigger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringTrigger_ = $PiglinBarteringTrigger$Type;
}}
declare module "packages/com/sihenzhang/crockpot/entity/$CrockPotEntities" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Birdcage, $Birdcage$Type} from "packages/com/sihenzhang/crockpot/entity/$Birdcage"
import {$VoltGoat, $VoltGoat$Type} from "packages/com/sihenzhang/crockpot/entity/$VoltGoat"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$ThrownParrotEgg, $ThrownParrotEgg$Type} from "packages/com/sihenzhang/crockpot/entity/$ThrownParrotEgg"

export class $CrockPotEntities {
static readonly "ENTITIES": $DeferredRegister<($EntityType<(any)>)>
static readonly "BIRDCAGE": $RegistryObject<($EntityType<($Birdcage)>)>
static readonly "PARROT_EGG": $RegistryObject<($EntityType<($ThrownParrotEgg)>)>
static readonly "VOLT_GOAT": $RegistryObject<($EntityType<($VoltGoat)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotEntities$Type = ($CrockPotEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotEntities_ = $CrockPotEntities$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/$FoodValuesTooltip" {
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $FoodValuesTooltip {

constructor()

public static "onTooltip"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodValuesTooltip$Type = ($FoodValuesTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodValuesTooltip_ = $FoodValuesTooltip$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$AbstractCrockPotDoubleCropBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$AbstractCrockPotCropBlock, $AbstractCrockPotCropBlock$Type} from "packages/com/sihenzhang/crockpot/block/$AbstractCrockPotCropBlock"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $AbstractCrockPotDoubleCropBlock extends $AbstractCrockPotCropBlock {
static readonly "MAX_AGE": integer
static readonly "AGE": $IntegerProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()

public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public "randomTick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "growCrops"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): void
public "isUpperBlock"(arg0: $BlockState$Type): boolean
public "getMaxGrowthAge"(arg0: $BlockState$Type): integer
public "isValidBonemealTarget"(arg0: $LevelReader$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCrockPotDoubleCropBlock$Type = ($AbstractCrockPotDoubleCropBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCrockPotDoubleCropBlock_ = $AbstractCrockPotDoubleCropBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CreativeMilkmadeHatItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MilkmadeHatItem, $MilkmadeHatItem$Type} from "packages/com/sihenzhang/crockpot/item/$MilkmadeHatItem"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CreativeMilkmadeHatItem extends $MilkmadeHatItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "isFoil"(arg0: $ItemStack$Type): boolean
public "onArmorTick"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $Player$Type): void
public "initCapabilities"(arg0: $ItemStack$Type, arg1: $CompoundTag$Type): $ICapabilityProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeMilkmadeHatItem$Type = ($CreativeMilkmadeHatItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeMilkmadeHatItem_ = $CreativeMilkmadeHatItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$I18nUtils" {
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $I18nUtils {


public static "createComponent"(arg0: string, arg1: string, arg2: string, ...arg3: (any)[]): $MutableComponent
public static "createComponent"(arg0: string, arg1: string, arg2: string): $MutableComponent
public static "createComponent"(arg0: string, arg1: string, ...arg2: (any)[]): $MutableComponent
public static "createComponent"(arg0: string, arg1: string): $MutableComponent
public static "createTooltipComponent"(arg0: string, ...arg1: (any)[]): $MutableComponent
public static "createTooltipComponent"(arg0: string): $MutableComponent
public static "createIntegrationComponent"(arg0: string, arg1: string): $MutableComponent
public static "createIntegrationComponent"(arg0: string, arg1: string, ...arg2: (any)[]): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $I18nUtils$Type = ($I18nUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $I18nUtils_ = $I18nUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotGlobalLootModifierProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$GlobalLootModifierProvider, $GlobalLootModifierProvider$Type} from "packages/net/minecraftforge/common/data/$GlobalLootModifierProvider"

export class $CrockPotGlobalLootModifierProvider extends $GlobalLootModifierProvider {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotGlobalLootModifierProvider$Type = ($CrockPotGlobalLootModifierProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotGlobalLootModifierProvider_ = $CrockPotGlobalLootModifierProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotItemModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ItemModelBuilder, $ItemModelBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelBuilder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ItemModelProvider, $ItemModelProvider$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotItemModelProvider extends $ItemModelProvider {
static readonly "BLOCK_FOLDER": string
static readonly "ITEM_FOLDER": string
readonly "generatedModels": $Map<($ResourceLocation), (T)>
readonly "existingFileHelper": $ExistingFileHelper

constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type)

public "handheldItem"(arg0: string, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "simpleHandheldItem"(arg0: $Item$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "simpleHandheldItem"(arg0: $Item$Type): $ItemModelBuilder
public "item"(arg0: string, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "simpleItem"(arg0: $Item$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "simpleItem"(arg0: $Item$Type): $ItemModelBuilder
public "blockItem"(arg0: $Block$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "blockItem"(arg0: $Block$Type): $ItemModelBuilder
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotItemModelProvider$Type = ($CrockPotItemModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotItemModelProvider_ = $CrockPotItemModelProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/base/$CrockPotSoundEvents" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPotSoundEvents {
static readonly "SOUND_EVENTS": $DeferredRegister<($SoundEvent)>
static readonly "CROCK_POT_CLOSE_NAME": string
static readonly "CROCK_POT_OEPN_NAME": string
static readonly "CROCK_POT_FINISH_NAME": string
static readonly "CROCK_POT_RATTLE_NAME": string
static readonly "CROCK_POT_CLOSE": $RegistryObject<($SoundEvent)>
static readonly "CROCK_POT_OPEN": $RegistryObject<($SoundEvent)>
static readonly "CROCK_POT_FINISH": $RegistryObject<($SoundEvent)>
static readonly "CROCK_POT_RATTLE": $RegistryObject<($SoundEvent)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotSoundEvents$Type = ($CrockPotSoundEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotSoundEvents_ = $CrockPotSoundEvents$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/$DrawableFramed" {
import {$IDrawableAnimated, $IDrawableAnimated$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated"
import {$IDrawableStatic, $IDrawableStatic$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableStatic"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IDrawableAnimated$StartDirection, $IDrawableAnimated$StartDirection$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawableAnimated$StartDirection"

export class $DrawableFramed implements $IDrawableAnimated {

constructor(arg0: $IDrawableStatic$Type, arg1: integer, arg2: integer, arg3: $IDrawableAnimated$StartDirection$Type)

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
export type $DrawableFramed$Type = ($DrawableFramed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableFramed_ = $DrawableFramed$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CrockPotSeedsItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$ItemNameBlockItem, $ItemNameBlockItem$Type} from "packages/net/minecraft/world/item/$ItemNameBlockItem"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotSeedsItem extends $ItemNameBlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type)

public "interactLivingEntity"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $LivingEntity$Type, arg3: $InteractionHand$Type): $InteractionResult
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotSeedsItem$Type = ($CrockPotSeedsItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotSeedsItem_ = $CrockPotSeedsItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$CrockPotCookingRecipeJS" {
import {$AbstractCrockPotRecipeJS, $AbstractCrockPotRecipeJS$Type} from "packages/com/sihenzhang/crockpot/integration/kubejs/$AbstractCrockPotRecipeJS"

export class $CrockPotCookingRecipeJS extends $AbstractCrockPotRecipeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCookingRecipeJS$Type = ($CrockPotCookingRecipeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipeJS_ = $CrockPotCookingRecipeJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/loot/$CrockPotLootModifiers" {
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CrockPotLootModifiers {
static readonly "LOOT_MODIFIERS": $DeferredRegister<($Codec<(any)>)>
static readonly "ADD_ITEM": $RegistryObject<($Codec<(any)>)>
static readonly "ADD_ITEM_WITH_LOOTING_ENCHANT": $RegistryObject<($Codec<(any)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotLootModifiers$Type = ($CrockPotLootModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotLootModifiers_ = $CrockPotLootModifiers$Type;
}}
declare module "packages/com/sihenzhang/crockpot/advancement/$EatFoodTrigger" {
import {$EatFoodTrigger$Instance, $EatFoodTrigger$Instance$Type} from "packages/com/sihenzhang/crockpot/advancement/$EatFoodTrigger$Instance"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$SimpleCriterionTrigger, $SimpleCriterionTrigger$Type} from "packages/net/minecraft/advancements/critereon/$SimpleCriterionTrigger"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $EatFoodTrigger extends $SimpleCriterionTrigger<($EatFoodTrigger$Instance)> {

constructor()

public "trigger"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type, arg2: integer): void
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EatFoodTrigger$Type = ($EatFoodTrigger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EatFoodTrigger_ = $EatFoodTrigger$Type;
}}
declare module "packages/com/sihenzhang/crockpot/capability/$FoodCounter" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IFoodCounter, $IFoodCounter$Type} from "packages/com/sihenzhang/crockpot/capability/$IFoodCounter"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FoodCounter implements $IFoodCounter {

constructor()

public "clear"(): void
public "getCount"(arg0: $Item$Type): integer
public "asMap"(): $Map<($Item), (integer)>
public "addFood"(arg0: $Item$Type): void
public "hasEaten"(arg0: $Item$Type): boolean
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
public "setCount"(arg0: $Item$Type, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodCounter$Type = ($FoodCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodCounter_ = $FoodCounter$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jade/$ModIntegrationJade" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModIntegrationJade implements $IWailaPlugin {
static readonly "CROCK_POT": $ResourceLocation
static readonly "BIRDCAGE": $ResourceLocation

constructor()

public "register"(arg0: $IWailaCommonRegistration$Type): void
public "registerClient"(arg0: $IWailaClientRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIntegrationJade$Type = ($ModIntegrationJade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIntegrationJade_ = $ModIntegrationJade$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$AbstractRecipe, $AbstractRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RangedItem, $RangedItem$Type} from "packages/com/sihenzhang/crockpot/recipe/$RangedItem"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ParrotFeedingRecipe extends $AbstractRecipe<($Container)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $RangedItem$Type)

public "getResult"(): $RangedItem
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredient"(): $Ingredient
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $Container$Type, arg1: $RegistryAccess$Type): $ItemStack
public "matches"(arg0: $Container$Type, arg1: $Level$Type): boolean
get "result"(): $RangedItem
get "ingredient"(): $Ingredient
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParrotFeedingRecipe$Type = ($ParrotFeedingRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotFeedingRecipe_ = $ParrotFeedingRecipe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$SpawnRestrictionEvent" {
import {$SpawnPlacementRegisterEvent, $SpawnPlacementRegisterEvent$Type} from "packages/net/minecraftforge/event/entity/$SpawnPlacementRegisterEvent"

export class $SpawnRestrictionEvent {

constructor()

public static "onSpawnPlacementRegister"(arg0: $SpawnPlacementRegisterEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnRestrictionEvent$Type = ($SpawnRestrictionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnRestrictionEvent_ = $SpawnRestrictionEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/entity/$Birdcage" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Birdcage extends $Entity {
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "tick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Birdcage$Type = ($Birdcage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Birdcage_ = $Birdcage$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/curios/$MilkmadeHatCuriosCapabilityProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $MilkmadeHatCuriosCapabilityProvider implements $ICapabilityProvider {

constructor(arg0: $ItemStack$Type, arg1: $CompoundTag$Type)
constructor(arg0: $ItemStack$Type, arg1: $CompoundTag$Type, arg2: boolean)

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatCuriosCapabilityProvider$Type = ($MilkmadeHatCuriosCapabilityProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatCuriosCapabilityProvider_ = $MilkmadeHatCuriosCapabilityProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$RLUtils" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RLUtils {


public static "createRL"(arg0: string): $ResourceLocation
public static "createRL"(arg0: string, arg1: string): $ResourceLocation
public static "createForgeRL"(arg0: string): $ResourceLocation
public static "createVanillaRL"(arg0: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RLUtils$Type = ($RLUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RLUtils_ = $RLUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMax" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"

export class $RequirementCategoryMax implements $IRequirement {

constructor(arg0: $FoodCategory$Type, arg1: float)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getMax"(): float
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCategoryMax
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCategoryMax
public "getCategory"(): $FoodCategory
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "max"(): float
get "category"(): $FoodCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCategoryMax$Type = ($RequirementCategoryMax);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCategoryMax_ = $RequirementCategoryMax$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMin" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"

export class $RequirementCategoryMin implements $IRequirement {

constructor(arg0: $FoodCategory$Type, arg1: float)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getMin"(): float
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCategoryMin
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCategoryMin
public "getCategory"(): $FoodCategory
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "min"(): float
get "category"(): $FoodCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCategoryMin$Type = ($RequirementCategoryMin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCategoryMin_ = $RequirementCategoryMin$Type;
}}
declare module "packages/com/sihenzhang/crockpot/inventory/$CrockPotMenuTypes" {
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$CrockPotMenu, $CrockPotMenu$Type} from "packages/com/sihenzhang/crockpot/inventory/$CrockPotMenu"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $CrockPotMenuTypes {
static readonly "MENU_TYPES": $DeferredRegister<($MenuType<(any)>)>
static readonly "CROCK_POT_MENU_TYPE": $RegistryObject<($MenuType<($CrockPotMenu)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotMenuTypes$Type = ($CrockPotMenuTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotMenuTypes_ = $CrockPotMenuTypes$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/curios/renderer/$MilkmadeHatCurioRenderer" {
import {$SlotContext, $SlotContext$Type} from "packages/top/theillusivec4/curios/api/$SlotContext"
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$ICurioRenderer, $ICurioRenderer$Type} from "packages/top/theillusivec4/curios/api/client/$ICurioRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $MilkmadeHatCurioRenderer implements $ICurioRenderer {

constructor()

public "render"<T extends $LivingEntity, M extends $EntityModel<(T)>>(arg0: $ItemStack$Type, arg1: $SlotContext$Type, arg2: $PoseStack$Type, arg3: $RenderLayerParent$Type<(T), (M)>, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float): void
public static "followHeadRotations"(arg0: $LivingEntity$Type, ...arg1: ($ModelPart$Type)[]): void
public static "translateIfSneaking"(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
public static "followBodyRotations"(arg0: $LivingEntity$Type, ...arg1: ($HumanoidModel$Type<($LivingEntity$Type)>)[]): void
public static "rotateIfSneaking"(arg0: $PoseStack$Type, arg1: $LivingEntity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatCurioRenderer$Type = ($MilkmadeHatCurioRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatCurioRenderer_ = $MilkmadeHatCurioRenderer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$TagUtils" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $TagUtils {


public static "createVanillaBlockTag"(arg0: string): $TagKey<($Block)>
public static "createVanillaItemTag"(arg0: string): $TagKey<($Item)>
public static "createForgeBlockTag"(arg0: string): $TagKey<($Block)>
public static "createItemTag"(arg0: string, arg1: string): $TagKey<($Item)>
public static "createItemTag"(arg0: string): $TagKey<($Item)>
public static "createBlockTag"(arg0: string): $TagKey<($Block)>
public static "createBlockTag"(arg0: string, arg1: string): $TagKey<($Block)>
public static "createForgeItemTag"(arg0: string): $TagKey<($Item)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagUtils$Type = ($TagUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagUtils_ = $TagUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCombinationOr" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"

export class $RequirementCombinationOr implements $IRequirement {

constructor(arg0: $IRequirement$Type, arg1: $IRequirement$Type)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getFirst"(): $IRequirement
public "getSecond"(): $IRequirement
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCombinationOr
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCombinationOr
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "first"(): $IRequirement
get "second"(): $IRequirement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCombinationOr$Type = ($RequirementCombinationOr);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCombinationOr_ = $RequirementCombinationOr$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/renderer/entity/$VoltGoatRenderer" {
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$VoltGoat, $VoltGoat$Type} from "packages/com/sihenzhang/crockpot/entity/$VoltGoat"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MobRenderer, $MobRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$MobRenderer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VoltGoatModel, $VoltGoatModel$Type} from "packages/com/sihenzhang/crockpot/client/model/$VoltGoatModel"

export class $VoltGoatRenderer extends $MobRenderer<($VoltGoat), ($VoltGoatModel<($VoltGoat)>)> {
static readonly "LEASH_RENDER_STEPS": integer
 "model": M
 "layers": $List<($RenderLayer<(T), (M)>)>
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "getTextureLocation"(arg0: $VoltGoat$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoltGoatRenderer$Type = ($VoltGoatRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoltGoatRenderer_ = $VoltGoatRenderer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$PiglinBarteringRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$AbstractRecipe, $AbstractRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$AbstractRecipe"
import {$SimpleWeightedRandomList, $SimpleWeightedRandomList$Type} from "packages/net/minecraft/util/random/$SimpleWeightedRandomList"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RangedItem, $RangedItem$Type} from "packages/com/sihenzhang/crockpot/recipe/$RangedItem"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PiglinBarteringRecipe extends $AbstractRecipe<($Container)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Ingredient$Type, arg2: $SimpleWeightedRandomList$Type<($RangedItem$Type)>)

public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredient"(): $Ingredient
public "getWeightedResults"(): $SimpleWeightedRandomList<($RangedItem)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $Container$Type, arg1: $RegistryAccess$Type): $ItemStack
public "matches"(arg0: $Container$Type, arg1: $Level$Type): boolean
get "ingredient"(): $Ingredient
get "weightedResults"(): $SimpleWeightedRandomList<($RangedItem)>
get "ingredients"(): $NonNullList<($Ingredient)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringRecipe$Type = ($PiglinBarteringRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringRecipe_ = $PiglinBarteringRecipe$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$PiglinBarteringRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AbstractRecipeBuilder, $AbstractRecipeBuilder$Type} from "packages/com/sihenzhang/crockpot/data/recipes/$AbstractRecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $PiglinBarteringRecipeBuilder extends $AbstractRecipeBuilder {

constructor(arg0: $Ingredient$Type)

public "addResult"(arg0: $ItemLike$Type, arg1: integer): $PiglinBarteringRecipeBuilder
public "addResult"(arg0: $ItemLike$Type, arg1: integer, arg2: integer): $PiglinBarteringRecipeBuilder
public "addResult"(arg0: $ItemLike$Type, arg1: integer, arg2: integer, arg3: integer): $PiglinBarteringRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string): void
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public static "piglinBartering"(arg0: $Ingredient$Type): $PiglinBarteringRecipeBuilder
public "getResult"(): $Item
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringRecipeBuilder$Type = ($PiglinBarteringRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringRecipeBuilder_ = $PiglinBarteringRecipeBuilder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotDamageTypeTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$DamageTypeTagsProvider, $DamageTypeTagsProvider$Type} from "packages/net/minecraft/data/tags/$DamageTypeTagsProvider"

export class $CrockPotDamageTypeTagsProvider extends $DamageTypeTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotDamageTypeTagsProvider$Type = ($CrockPotDamageTypeTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotDamageTypeTagsProvider_ = $CrockPotDamageTypeTagsProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$NbtUtils" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $NbtUtils {

constructor()

public static "setLoreString"(arg0: $ItemStack$Type, arg1: string): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NbtUtils$Type = ($NbtUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NbtUtils_ = $NbtUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/$GnawsGiftHungerOverlay" {
import {$RenderGuiOverlayEvent$Pre, $RenderGuiOverlayEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Pre"
import {$RenderGuiOverlayEvent$Post, $RenderGuiOverlayEvent$Post$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Post"

export class $GnawsGiftHungerOverlay {

constructor()

public static "onClientSetupEvent"(arg0: $RenderGuiOverlayEvent$Pre$Type): void
public static "onRenderGuiOverlayPost"(arg0: $RenderGuiOverlayEvent$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GnawsGiftHungerOverlay$Type = ($GnawsGiftHungerOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GnawsGiftHungerOverlay_ = $GnawsGiftHungerOverlay$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/$CrockPotBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BaseEntityBlock, $BaseEntityBlock$Type} from "packages/net/minecraft/world/level/block/$BaseEntityBlock"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$RenderShape, $RenderShape$Type} from "packages/net/minecraft/world/level/block/$RenderShape"

export class $CrockPotBlock extends $BaseEntityBlock {
static readonly "FACING": $DirectionProperty
static readonly "LIT": $BooleanProperty
static readonly "OPEN": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: integer)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getRenderShape"(arg0: $BlockState$Type): $RenderShape
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "tick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "animateTick"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "getPotLevel"(): integer
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "potLevel"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlock$Type = ($CrockPotBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlock_ = $CrockPotBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/capability/$IFoodCounter" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $IFoodCounter extends $INBTSerializable<($CompoundTag)> {

 "clear"(): void
 "getCount"(arg0: $Item$Type): integer
 "asMap"(): $Map<($Item), (integer)>
 "addFood"(arg0: $Item$Type): void
 "hasEaten"(arg0: $Item$Type): boolean
 "setCount"(arg0: $Item$Type, arg1: integer): void
 "deserializeNBT"(arg0: $CompoundTag$Type): void
 "serializeNBT"(): $CompoundTag
}

export namespace $IFoodCounter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFoodCounter$Type = ($IFoodCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFoodCounter_ = $IFoodCounter$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/model/$MilkmadeHatModel" {
import {$AgeableListModel, $AgeableListModel$Type} from "packages/net/minecraft/client/model/$AgeableListModel"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $MilkmadeHatModel<T extends $LivingEntity> extends $AgeableListModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $ModelPart$Type)

public static "createLayer"(): $LayerDefinition
public "setupAnim"(arg0: T, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
public "prepareMobModel"(arg0: T, arg1: float, arg2: float, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MilkmadeHatModel$Type<T> = ($MilkmadeHatModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MilkmadeHatModel_<T> = $MilkmadeHatModel$Type<(T)>;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$FoodUseDuration" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FoodUseDuration extends $Enum<($FoodUseDuration)> {
static readonly "SUPER_FAST": $FoodUseDuration
static readonly "FAST": $FoodUseDuration
static readonly "NORMAL": $FoodUseDuration
static readonly "SLOW": $FoodUseDuration
static readonly "SUPER_SLOW": $FoodUseDuration
readonly "val": integer


public static "values"(): ($FoodUseDuration)[]
public static "valueOf"(arg0: string): $FoodUseDuration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodUseDuration$Type = (("super_fast") | ("normal") | ("fast") | ("super_slow") | ("slow")) | ($FoodUseDuration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodUseDuration_ = $FoodUseDuration$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$ItemUtils" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemUtils {

constructor()

public static "giveItemToPlayer"(arg0: $Player$Type, arg1: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemUtils$Type = ($ItemUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemUtils_ = $ItemUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RequirementType extends $Enum<($RequirementType)> {
static readonly "CATEGORY_MAX": $RequirementType
static readonly "CATEGORY_MAX_EXCLUSIVE": $RequirementType
static readonly "CATEGORY_MIN": $RequirementType
static readonly "CATEGORY_MIN_EXCLUSIVE": $RequirementType
static readonly "MUST_CONTAIN_INGREDIENT": $RequirementType
static readonly "MUST_CONTAIN_INGREDIENT_LESS_THAN": $RequirementType
static readonly "COMBINATION_AND": $RequirementType
static readonly "COMBINATION_OR": $RequirementType


public static "values"(): ($RequirementType)[]
public static "valueOf"(arg0: string): $RequirementType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementType$Type = (("category_max_exclusive") | ("combination_and") | ("must_contain_ingredient") | ("category_min") | ("must_contain_ingredient_less_than") | ("combination_or") | ("category_max") | ("category_min_exclusive")) | ($RequirementType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementType_ = $RequirementType$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodItem" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$CrockPotFoodProperties, $CrockPotFoodProperties$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$UseAnim, $UseAnim$Type} from "packages/net/minecraft/world/item/$UseAnim"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotFoodItem extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $CrockPotFoodProperties$Type)

public "getUseDuration"(arg0: $ItemStack$Type): integer
public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getUseAnimation"(arg0: $ItemStack$Type): $UseAnim
public "getDrinkingSound"(): $SoundEvent
public "getEatingSound"(): $SoundEvent
get "drinkingSound"(): $SoundEvent
get "eatingSound"(): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotFoodItem$Type = ($CrockPotFoodItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotFoodItem_ = $CrockPotFoodItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$ModIntegrationKubeJS" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModIntegrationKubeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModIntegrationKubeJS$Type = ($ModIntegrationKubeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModIntegrationKubeJS_ = $ModIntegrationKubeJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/$ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper {

constructor(arg0: $Ingredient$Type, arg1: integer, arg2: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type = ($ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper_ = $ParrotLayingEggsRecipeCategory$ParrotLayingEggsRecipeWrapper$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/gui/screen/$CrockPotScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CrockPotMenu, $CrockPotMenu$Type} from "packages/com/sihenzhang/crockpot/inventory/$CrockPotMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $CrockPotScreen extends $AbstractContainerScreen<($CrockPotMenu)> {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $CrockPotMenu$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotScreen$Type = ($CrockPotScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotScreen_ = $CrockPotScreen$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCombinationOr" {
import {$RequirementCombinationOr, $RequirementCombinationOr$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCombinationOr"
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementCombinationOr extends $AbstractDrawableRequirement<($RequirementCombinationOr)> {

constructor(arg0: $RequirementCombinationOr$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCombinationOr$Type = ($DrawableRequirementCombinationOr);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCombinationOr_ = $DrawableRequirementCombinationOr$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotEntityTypeTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$EntityTypeTagsProvider, $EntityTypeTagsProvider$Type} from "packages/net/minecraft/data/tags/$EntityTypeTagsProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $CrockPotEntityTypeTagsProvider extends $EntityTypeTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotEntityTypeTagsProvider$Type = ($CrockPotEntityTypeTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotEntityTypeTagsProvider_ = $CrockPotEntityTypeTagsProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$CrockPot4StacksFoodBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$AbstractStackableFoodBlock, $AbstractStackableFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$AbstractStackableFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CrockPot4StacksFoodBlock extends $AbstractStackableFoodBlock {
static readonly "STACKS": $IntegerProperty
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getMaxStacks"(): integer
public "getStacksProperty"(): $IntegerProperty
get "maxStacks"(): integer
get "stacksProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPot4StacksFoodBlock$Type = ($CrockPot4StacksFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPot4StacksFoodBlock_ = $CrockPot4StacksFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/recipes/$AbstractRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$RecipeBuilder, $RecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$RecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $AbstractRecipeBuilder implements $RecipeBuilder {

constructor()

public "unlockedBy"(arg0: string, arg1: $CriterionTriggerInstance$Type): $RecipeBuilder
public "group"(arg0: string): $RecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string): void
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
public "getResult"(): $Item
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRecipeBuilder$Type = ($AbstractRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipeBuilder_ = $AbstractRecipeBuilder$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/$ClientRegistry" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$EntityRenderersEvent$AddLayers, $EntityRenderersEvent$AddLayers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$AddLayers"
import {$EntityRenderersEvent$RegisterRenderers, $EntityRenderersEvent$RegisterRenderers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterRenderers"
import {$EntityRenderersEvent$RegisterLayerDefinitions, $EntityRenderersEvent$RegisterLayerDefinitions$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterLayerDefinitions"

export class $ClientRegistry {

constructor()

public static "onClientSetupEvent"(arg0: $FMLClientSetupEvent$Type): void
public static "onRegisterLayers"(arg0: $EntityRenderersEvent$RegisterLayerDefinitions$Type): void
public static "onAddLayers"(arg0: $EntityRenderersEvent$AddLayers$Type): void
public static "onRegisterRenderers"(arg0: $EntityRenderersEvent$RegisterRenderers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRegistry$Type = ($ClientRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRegistry_ = $ClientRegistry$Type;
}}
declare module "packages/com/sihenzhang/crockpot/effect/$CrockPotEffect" {
import {$MobEffectCategory, $MobEffectCategory$Type} from "packages/net/minecraft/world/effect/$MobEffectCategory"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $CrockPotEffect extends $MobEffect {

constructor(arg0: $MobEffectCategory$Type, arg1: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotEffect$Type = ($CrockPotEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotEffect_ = $CrockPotEffect$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$GnawsCoinItem" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$CrockPotBaseItem, $CrockPotBaseItem$Type} from "packages/com/sihenzhang/crockpot/item/$CrockPotBaseItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $GnawsCoinItem extends $CrockPotBaseItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "inventoryTick"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $Entity$Type, arg3: integer, arg4: boolean): void
public "isFoil"(arg0: $ItemStack$Type): boolean
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "isBookEnchantable"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
public "initCapabilities"(arg0: $ItemStack$Type, arg1: $CompoundTag$Type): $ICapabilityProvider
public "getEntityLifespan"(arg0: $ItemStack$Type, arg1: $Level$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GnawsCoinItem$Type = ($GnawsCoinItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GnawsCoinItem_ = $GnawsCoinItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/$ExplosionCraftingRecipe$Wrapper" {
import {$SimpleContainer, $SimpleContainer$Type} from "packages/net/minecraft/world/$SimpleContainer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ExplosionCraftingRecipe$Wrapper extends $SimpleContainer {

constructor(arg0: $ItemStack$Type, arg1: boolean)
constructor(arg0: $ItemStack$Type)

public "isFromBlock"(): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "fromBlock"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingRecipe$Wrapper$Type = ($ExplosionCraftingRecipe$Wrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipe$Wrapper_ = $ExplosionCraftingRecipe$Wrapper$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$PiglinBarteringRecipeJS" {
import {$AbstractCrockPotRecipeJS, $AbstractCrockPotRecipeJS$Type} from "packages/com/sihenzhang/crockpot/integration/kubejs/$AbstractCrockPotRecipeJS"

export class $PiglinBarteringRecipeJS extends $AbstractCrockPotRecipeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringRecipeJS$Type = ($PiglinBarteringRecipeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringRecipeJS_ = $PiglinBarteringRecipeJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo" {
import {$RecipeIngredientRole, $RecipeIngredientRole$Type} from "packages/mezz/jei/api/recipe/$RecipeIngredientRole"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AbstractDrawableRequirement$GuiItemStacksInfo {
 "role": $RecipeIngredientRole
 "stacks": $List<($ItemStack)>
 "x": integer
 "y": integer

constructor(arg0: $List$Type<($ItemStack$Type)>, arg1: integer, arg2: integer, arg3: boolean)
constructor(arg0: $List$Type<($ItemStack$Type)>, arg1: integer, arg2: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDrawableRequirement$GuiItemStacksInfo$Type = ($AbstractDrawableRequirement$GuiItemStacksInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDrawableRequirement$GuiItemStacksInfo_ = $AbstractDrawableRequirement$GuiItemStacksInfo$Type;
}}
declare module "packages/com/sihenzhang/crockpot/util/$StringUtils" {
import {$RangedItem, $RangedItem$Type} from "packages/com/sihenzhang/crockpot/recipe/$RangedItem"
import {$WeightedEntry$Wrapper, $WeightedEntry$Wrapper$Type} from "packages/net/minecraft/util/random/$WeightedEntry$Wrapper"

export class $StringUtils {

constructor()

public static "formatCountAndChance"(arg0: $WeightedEntry$Wrapper$Type<($RangedItem$Type)>, arg1: integer): string
public static "format"(arg0: double, arg1: string): string
public static "format"(arg0: float, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringUtils$Type = ($StringUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringUtils_ = $StringUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/entity/$BirdcageBlockEntity" {
import {$Birdcage, $Birdcage$Type} from "packages/com/sihenzhang/crockpot/entity/$Birdcage"
import {$ParrotFeedingRecipe, $ParrotFeedingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/$ParrotFeedingRecipe"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$FoodValues, $FoodValues$Type} from "packages/com/sihenzhang/crockpot/base/$FoodValues"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Parrot, $Parrot$Type} from "packages/net/minecraft/world/entity/animal/$Parrot"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export class $BirdcageBlockEntity extends $BlockEntity {
static readonly "OUTPUT_COOLDOWN": integer
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "isOnCooldown"(): boolean
public "fedByMeat"(arg0: $ItemStack$Type, arg1: $FoodValues$Type, arg2: $Parrot$Type): boolean
public "captureParrot"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Player$Type, arg3: $Parrot$Type, arg4: $Birdcage$Type, arg5: boolean): boolean
public "fedByRecipe"(arg0: $ItemStack$Type, arg1: $ParrotFeedingRecipe$Type, arg2: $RegistryAccess$Type, arg3: $Parrot$Type): boolean
public "getOutputBuffer"(): $Queue<($Pair<($ItemStack), (long)>)>
public static "serverTick"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BirdcageBlockEntity$Type): void
get "onCooldown"(): boolean
get "outputBuffer"(): $Queue<($Pair<($ItemStack), (long)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BirdcageBlockEntity$Type = ($BirdcageBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BirdcageBlockEntity_ = $BirdcageBlockEntity$Type;
}}
declare module "packages/com/sihenzhang/crockpot/entity/$VoltGoat" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$NeutralMob, $NeutralMob$Type} from "packages/net/minecraft/world/entity/$NeutralMob"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ChargeableMob, $ChargeableMob$Type} from "packages/com/sihenzhang/crockpot/entity/$ChargeableMob"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$LightningBolt, $LightningBolt$Type} from "packages/net/minecraft/world/entity/$LightningBolt"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$AgeableMob, $AgeableMob$Type} from "packages/net/minecraft/world/entity/$AgeableMob"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VoltGoat extends $Animal implements $ChargeableMob, $NeutralMob {
static readonly "BABY_START_AGE": integer
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public static "createAttributes"(): $AttributeSupplier$Builder
public "readAdditionalSaveData"(arg0: $CompoundTag$Type): void
public "addAdditionalSaveData"(arg0: $CompoundTag$Type): void
public "getEatingSound"(arg0: $ItemStack$Type): $SoundEvent
public "setYHeadRot"(arg0: float): void
public "setPersistentAngerTarget"(arg0: $UUID$Type): void
public "startPersistentAngerTimer"(): void
public "setRemainingPersistentAngerTime"(arg0: integer): void
public "getRemainingPersistentAngerTime"(): integer
public "getPersistentAngerTarget"(): $UUID
public "thunderHit"(arg0: $ServerLevel$Type, arg1: $LightningBolt$Type): void
public "getMaxHeadYRot"(): integer
public "mobInteract"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "getRemainingPersistentChargeTime"(): integer
public "setRemainingPersistentChargeTime"(arg0: integer): void
public static "checkVoltGoatSpawnRules"(arg0: $EntityType$Type<(any)>, arg1: $LevelAccessor$Type, arg2: $MobSpawnType$Type, arg3: $BlockPos$Type, arg4: $RandomSource$Type): boolean
public "setLastLightningBolt"(arg0: $UUID$Type): void
public "startPersistentChargeTimer"(): void
public "getBreedOffspring"(arg0: $ServerLevel$Type, arg1: $AgeableMob$Type): $AgeableMob
public "isPowered"(): boolean
public "updatePersistentCharge"(): void
public "addPersistentChargeSaveData"(arg0: $CompoundTag$Type): void
public "readPersistentChargeSaveData"(arg0: $CompoundTag$Type): void
public "setLastHurtByMob"(arg0: $LivingEntity$Type): void
public "setLastHurtByPlayer"(arg0: $Player$Type): void
public "getLastHurtByMob"(): $LivingEntity
public "canAttack"(arg0: $LivingEntity$Type): boolean
public "playerDied"(arg0: $Player$Type): void
public "addPersistentAngerSaveData"(arg0: $CompoundTag$Type): void
public "readPersistentAngerSaveData"(arg0: $Level$Type, arg1: $CompoundTag$Type): void
public "isAngryAt"(arg0: $LivingEntity$Type): boolean
public "updatePersistentAnger"(arg0: $ServerLevel$Type, arg1: boolean): void
public "forgetCurrentTargetAndRefreshUniversalAnger"(): void
public "isAngryAtAllPlayers"(arg0: $Level$Type): boolean
public "stopBeingAngry"(): void
public "isAngry"(): boolean
public "setTarget"(arg0: $LivingEntity$Type): void
public "getTarget"(): $LivingEntity
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
set "yHeadRot"(value: float)
set "persistentAngerTarget"(value: $UUID$Type)
set "remainingPersistentAngerTime"(value: integer)
get "remainingPersistentAngerTime"(): integer
get "persistentAngerTarget"(): $UUID
get "maxHeadYRot"(): integer
get "remainingPersistentChargeTime"(): integer
set "remainingPersistentChargeTime"(value: integer)
set "lastLightningBolt"(value: $UUID$Type)
get "powered"(): boolean
set "lastHurtByMob"(value: $LivingEntity$Type)
set "lastHurtByPlayer"(value: $Player$Type)
get "lastHurtByMob"(): $LivingEntity
get "angry"(): boolean
set "target"(value: $LivingEntity$Type)
get "target"(): $LivingEntity
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoltGoat$Type = ($VoltGoat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoltGoat_ = $VoltGoat$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCategoryMinExclusive" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IRequirement, $IRequirement$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement"
import {$FoodCategory, $FoodCategory$Type} from "packages/com/sihenzhang/crockpot/base/$FoodCategory"

export class $RequirementCategoryMinExclusive implements $IRequirement {

constructor(arg0: $FoodCategory$Type, arg1: float)

public "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
public "getMin"(): float
public static "fromJson"(arg0: $JsonObject$Type): $RequirementCategoryMinExclusive
public "toJson"(): $JsonElement
public static "fromNetwork"(arg0: $FriendlyByteBuf$Type): $RequirementCategoryMinExclusive
public "getCategory"(): $FoodCategory
public "toNetwork"(arg0: $FriendlyByteBuf$Type): void
public static "fromJson"(arg0: $JsonElement$Type): $IRequirement
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
public static "isEqual"<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
get "min"(): float
get "category"(): $FoodCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequirementCategoryMinExclusive$Type = ($RequirementCategoryMinExclusive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequirementCategoryMinExclusive_ = $RequirementCategoryMinExclusive$Type;
}}
declare module "packages/com/sihenzhang/crockpot/block/food/$CrockPot3StacksFoodBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$AbstractStackableFoodBlock, $AbstractStackableFoodBlock$Type} from "packages/com/sihenzhang/crockpot/block/food/$AbstractStackableFoodBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CrockPot3StacksFoodBlock extends $AbstractStackableFoodBlock {
static readonly "STACKS": $IntegerProperty
static readonly "SHAPE": $VoxelShape
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor()
constructor(arg0: $BlockBehaviour$Properties$Type)

public "getMaxStacks"(): integer
public "getStacksProperty"(): $IntegerProperty
get "maxStacks"(): integer
get "stacksProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPot3StacksFoodBlock$Type = ($CrockPot3StacksFoodBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPot3StacksFoodBlock_ = $CrockPot3StacksFoodBlock$Type;
}}
declare module "packages/com/sihenzhang/crockpot/loot/$AddItemModifier" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LootModifier, $LootModifier$Type} from "packages/net/minecraftforge/common/loot/$LootModifier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AddItemModifier extends $LootModifier {
static readonly "CODEC": $Supplier<($Codec<($AddItemModifier)>)>

constructor(arg0: ($LootItemCondition$Type)[], arg1: $Item$Type, arg2: integer)

public "codec"(): $Codec<(any)>
public static "getJson"<U>(arg0: $Dynamic$Type<(any)>): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddItemModifier$Type = ($AddItemModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddItemModifier_ = $AddItemModifier$Type;
}}
declare module "packages/com/sihenzhang/crockpot/advancement/$PiglinBarteringTrigger$Instance" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SerializationContext, $SerializationContext$Type} from "packages/net/minecraft/advancements/critereon/$SerializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $PiglinBarteringTrigger$Instance extends $AbstractCriterionTriggerInstance {

constructor(arg0: $ContextAwarePredicate$Type, arg1: $ItemPredicate$Type)

public "matches"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type): boolean
public "serializeToJson"(arg0: $SerializationContext$Type): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinBarteringTrigger$Instance$Type = ($PiglinBarteringTrigger$Instance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinBarteringTrigger$Instance_ = $PiglinBarteringTrigger$Instance$Type;
}}
declare module "packages/com/sihenzhang/crockpot/loot/$AddItemWithLootingEnchantModifier" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LootModifier, $LootModifier$Type} from "packages/net/minecraftforge/common/loot/$LootModifier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AddItemWithLootingEnchantModifier extends $LootModifier {
static readonly "CODEC": $Supplier<($Codec<($AddItemWithLootingEnchantModifier)>)>

constructor(arg0: ($LootItemCondition$Type)[], arg1: $Item$Type, arg2: integer, arg3: integer)

public "codec"(): $Codec<(any)>
public static "getJson"<U>(arg0: $Dynamic$Type<(any)>): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddItemWithLootingEnchantModifier$Type = ($AddItemWithLootingEnchantModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddItemWithLootingEnchantModifier_ = $AddItemWithLootingEnchantModifier$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$RegisterPacketsEvent" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $RegisterPacketsEvent {

constructor()

public static "onCommonSetup"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterPacketsEvent$Type = ($RegisterPacketsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterPacketsEvent_ = $RegisterPacketsEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodBlockItem" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$CrockPotFoodProperties, $CrockPotFoodProperties$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$UseAnim, $UseAnim$Type} from "packages/net/minecraft/world/item/$UseAnim"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotFoodBlockItem extends $BlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type, arg1: $CrockPotFoodProperties$Type)

public "getUseDuration"(arg0: $ItemStack$Type): integer
public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getUseAnimation"(arg0: $ItemStack$Type): $UseAnim
public "getDrinkingSound"(): $SoundEvent
public "getEatingSound"(): $SoundEvent
public "place"(arg0: $BlockPlaceContext$Type): $InteractionResult
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
get "drinkingSound"(): $SoundEvent
get "eatingSound"(): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotFoodBlockItem$Type = ($CrockPotFoodBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotFoodBlockItem_ = $CrockPotFoodBlockItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CrockPotCookingRecipe, $CrockPotCookingRecipe$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $CrockPotCookingRecipe$Serializer implements $RecipeSerializer<($CrockPotCookingRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $CrockPotCookingRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $CrockPotCookingRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $CrockPotCookingRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $CrockPotCookingRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotCookingRecipe$Serializer$Type = ($CrockPotCookingRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotCookingRecipe$Serializer_ = $CrockPotCookingRecipe$Serializer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/data/$CrockPotLootTableProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$LootTableProvider, $LootTableProvider$Type} from "packages/net/minecraft/data/loot/$LootTableProvider"

export class $CrockPotLootTableProvider extends $LootTableProvider {

constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotLootTableProvider$Type = ($CrockPotLootTableProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotLootTableProvider_ = $CrockPotLootTableProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/curios/$CuriosUtils" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CuriosUtils {

constructor()

public static "anyMatchInEquippedCurios"(arg0: $LivingEntity$Type, arg1: $TagKey$Type<($Item$Type)>): boolean
public static "anyMatchInEquippedCurios"(arg0: $LivingEntity$Type, arg1: $Item$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosUtils$Type = ($CuriosUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosUtils_ = $CuriosUtils$Type;
}}
declare module "packages/com/sihenzhang/crockpot/inventory/slot/$SlotCrockPotOutput" {
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"
import {$SlotItemHandler, $SlotItemHandler$Type} from "packages/net/minecraftforge/items/$SlotItemHandler"

export class $SlotCrockPotOutput extends $SlotItemHandler {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $IItemHandler$Type, arg1: integer, arg2: integer, arg3: integer)

public "mayPlace"(arg0: $ItemStack$Type): boolean
public "getMaxStackSize"(): integer
get "maxStackSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotCrockPotOutput$Type = ($SlotCrockPotOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotCrockPotOutput_ = $SlotCrockPotOutput$Type;
}}
declare module "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$IRequirement" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$CrockPotCookingRecipe$Wrapper, $CrockPotCookingRecipe$Wrapper$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/$CrockPotCookingRecipe$Wrapper"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export interface $IRequirement extends $Predicate<($CrockPotCookingRecipe$Wrapper)> {

 "toJson"(): $JsonElement
 "toNetwork"(arg0: $FriendlyByteBuf$Type): void
 "test"(arg0: $CrockPotCookingRecipe$Wrapper$Type): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
 "negate"(): $Predicate<($CrockPotCookingRecipe$Wrapper)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
}

export namespace $IRequirement {
function fromJson(arg0: $JsonElement$Type): $IRequirement
function fromNetwork(arg0: $FriendlyByteBuf$Type): $IRequirement
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($CrockPotCookingRecipe$Wrapper)>
function isEqual<T>(arg0: any): $Predicate<($CrockPotCookingRecipe$Wrapper)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRequirement$Type = ($IRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRequirement_ = $IRequirement$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties" {
import {$CrockPotFoodProperties$Builder, $CrockPotFoodProperties$Builder$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodProperties$Builder"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$UseAnim, $UseAnim$Type} from "packages/net/minecraft/world/item/$UseAnim"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $CrockPotFoodProperties {

constructor(arg0: $CrockPotFoodProperties$Builder$Type)

public static "builder"(arg0: integer, arg1: float): $CrockPotFoodProperties$Builder
public static "builder"(): $CrockPotFoodProperties$Builder
public "heal"(arg0: $Level$Type, arg1: $LivingEntity$Type): void
public "addCooldown"(arg0: $Item$Type, arg1: $Player$Type): void
public "removeEffects"(arg0: $Level$Type, arg1: $LivingEntity$Type): void
public "getUseDuration"(): integer
public "getTooltips"(): $List<($Component)>
public "getEffectTooltips"(arg0: boolean): $List<($Component)>
public "getUseAnimation"(): $UseAnim
public "hurt"(arg0: $Level$Type, arg1: $LivingEntity$Type): void
public "getSound"(): $SoundEvent
get "useDuration"(): integer
get "tooltips"(): $List<($Component)>
get "useAnimation"(): $UseAnim
get "sound"(): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotFoodProperties$Type = ($CrockPotFoodProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotFoodProperties_ = $CrockPotFoodProperties$Type;
}}
declare module "packages/com/sihenzhang/crockpot/event/$ComposterRecipeEvent" {
import {$FMLLoadCompleteEvent, $FMLLoadCompleteEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLLoadCompleteEvent"

export class $ComposterRecipeEvent {

constructor()

public static "onLoadComplete"(arg0: $FMLLoadCompleteEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComposterRecipeEvent$Type = ($ComposterRecipeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComposterRecipeEvent_ = $ComposterRecipeEvent$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementMustContainIngredientLessThan" {
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RequirementMustContainIngredientLessThan, $RequirementMustContainIngredientLessThan$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementMustContainIngredientLessThan"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementMustContainIngredientLessThan extends $AbstractDrawableRequirement<($RequirementMustContainIngredientLessThan)> {

constructor(arg0: $RequirementMustContainIngredientLessThan$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementMustContainIngredientLessThan$Type = ($DrawableRequirementMustContainIngredientLessThan);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementMustContainIngredientLessThan_ = $DrawableRequirementMustContainIngredientLessThan$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jade/$CrockPotProvider" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IPluginConfig, $IPluginConfig$Type} from "packages/snownee/jade/api/config/$IPluginConfig"
import {$BlockAccessor, $BlockAccessor$Type} from "packages/snownee/jade/api/$BlockAccessor"
import {$IBlockComponentProvider, $IBlockComponentProvider$Type} from "packages/snownee/jade/api/$IBlockComponentProvider"
import {$IElement, $IElement$Type} from "packages/snownee/jade/api/ui/$IElement"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITooltip, $ITooltip$Type} from "packages/snownee/jade/api/$ITooltip"
import {$IServerDataProvider, $IServerDataProvider$Type} from "packages/snownee/jade/api/$IServerDataProvider"

export class $CrockPotProvider implements $IBlockComponentProvider, $IServerDataProvider<($BlockAccessor)> {
static readonly "INSTANCE": $CrockPotProvider

constructor()

public "getUid"(): $ResourceLocation
public "appendServerData"(arg0: $CompoundTag$Type, arg1: $BlockAccessor$Type): void
public "appendTooltip"(arg0: $ITooltip$Type, arg1: $BlockAccessor$Type, arg2: $IPluginConfig$Type): void
public "getIcon"(arg0: $BlockAccessor$Type, arg1: $IPluginConfig$Type, arg2: $IElement$Type): $IElement
public "isRequired"(): boolean
public "enabledByDefault"(): boolean
public "getDefaultPriority"(): integer
get "uid"(): $ResourceLocation
get "required"(): boolean
get "defaultPriority"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotProvider$Type = ($CrockPotProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotProvider_ = $CrockPotProvider$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/model/geom/$CrockPotModelLayers" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"

export class $CrockPotModelLayers {
static readonly "MILKMADE_HAT": $ModelLayerLocation
static readonly "VOLT_GOAT": $ModelLayerLocation
static readonly "VOLT_GOAT_ARMOR": $ModelLayerLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotModelLayers$Type = ($CrockPotModelLayers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotModelLayers_ = $CrockPotModelLayers$Type;
}}
declare module "packages/com/sihenzhang/crockpot/base/$FoodCategory" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export class $FoodCategory extends $Enum<($FoodCategory)> {
static readonly "MEAT": $FoodCategory
static readonly "MONSTER": $FoodCategory
static readonly "FISH": $FoodCategory
static readonly "EGG": $FoodCategory
static readonly "FRUIT": $FoodCategory
static readonly "VEGGIE": $FoodCategory
static readonly "DAIRY": $FoodCategory
static readonly "SWEETENER": $FoodCategory
static readonly "FROZEN": $FoodCategory
static readonly "INEDIBLE": $FoodCategory
readonly "color": $TextColor


public static "values"(): ($FoodCategory)[]
public static "valueOf"(arg0: string): $FoodCategory
public static "getItemStack"(arg0: $FoodCategory$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodCategory$Type = (("veggie") | ("inedible") | ("egg") | ("fruit") | ("fish") | ("meat") | ("frozen") | ("sweetener") | ("dairy") | ("monster")) | ($FoodCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodCategory_ = $FoodCategory$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$IceCreamItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$CrockPotFoodBlockItem, $CrockPotFoodBlockItem$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodBlockItem"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $IceCreamItem extends $CrockPotFoodBlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IceCreamItem$Type = ($IceCreamItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IceCreamItem_ = $IceCreamItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/client/renderer/entity/layers/$VoltGoatPowerLayer" {
import {$VoltGoat, $VoltGoat$Type} from "packages/com/sihenzhang/crockpot/entity/$VoltGoat"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EnergySwirlLayer, $EnergySwirlLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$EnergySwirlLayer"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$VoltGoatModel, $VoltGoatModel$Type} from "packages/com/sihenzhang/crockpot/client/model/$VoltGoatModel"

export class $VoltGoatPowerLayer extends $EnergySwirlLayer<($VoltGoat), ($VoltGoatModel<($VoltGoat)>)> {

constructor(arg0: $RenderLayerParent$Type<($VoltGoat$Type), ($VoltGoatModel$Type<($VoltGoat$Type)>)>, arg1: $EntityModelSet$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoltGoatPowerLayer$Type = ($VoltGoatPowerLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoltGoatPowerLayer_ = $VoltGoatPowerLayer$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/kubejs/$ExplosionCraftingRecipeJS" {
import {$AbstractCrockPotRecipeJS, $AbstractCrockPotRecipeJS$Type} from "packages/com/sihenzhang/crockpot/integration/kubejs/$AbstractCrockPotRecipeJS"

export class $ExplosionCraftingRecipeJS extends $AbstractCrockPotRecipeJS {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionCraftingRecipeJS$Type = ($ExplosionCraftingRecipeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionCraftingRecipeJS_ = $ExplosionCraftingRecipeJS$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/$CrockPotBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrockPotBlockItem extends $BlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type)

public "getName"(arg0: $ItemStack$Type): $Component
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockItem$Type = ($CrockPotBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockItem_ = $CrockPotBlockItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/item/food/$FlowerSaladItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$CrockPotFoodBlockItem, $CrockPotFoodBlockItem$Type} from "packages/com/sihenzhang/crockpot/item/food/$CrockPotFoodBlockItem"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FlowerSaladItem extends $CrockPotFoodBlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowerSaladItem$Type = ($FlowerSaladItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowerSaladItem_ = $FlowerSaladItem$Type;
}}
declare module "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$DrawableRequirementCombinationAnd" {
import {$AbstractDrawableRequirement, $AbstractDrawableRequirement$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement"
import {$RequirementCombinationAnd, $RequirementCombinationAnd$Type} from "packages/com/sihenzhang/crockpot/recipe/cooking/requirement/$RequirementCombinationAnd"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractDrawableRequirement$GuiItemStacksInfo, $AbstractDrawableRequirement$GuiItemStacksInfo$Type} from "packages/com/sihenzhang/crockpot/integration/jei/gui/requirement/$AbstractDrawableRequirement$GuiItemStacksInfo"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DrawableRequirementCombinationAnd extends $AbstractDrawableRequirement<($RequirementCombinationAnd)> {

constructor(arg0: $RequirementCombinationAnd$Type)

public "getGuiItemStacksInfos"(arg0: integer, arg1: integer): $List<($AbstractDrawableRequirement$GuiItemStacksInfo)>
public "draw"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getInvisibleInputs"(): $List<($ItemStack)>
get "width"(): integer
get "height"(): integer
get "invisibleInputs"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableRequirementCombinationAnd$Type = ($DrawableRequirementCombinationAnd);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableRequirementCombinationAnd_ = $DrawableRequirementCombinationAnd$Type;
}}
declare module "packages/com/sihenzhang/crockpot/tag/$CrockPotBlockTags" {
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $CrockPotBlockTags {
static readonly "CROCK_POTS": $TagKey<($Block)>
static readonly "UNKNOWN_CROPS": $TagKey<($Block)>
static readonly "VOLT_GOATS_SPAWNABLE_ON": $TagKey<($Block)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrockPotBlockTags$Type = ($CrockPotBlockTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrockPotBlockTags_ = $CrockPotBlockTags$Type;
}}
