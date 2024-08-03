declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$BlockFamilyProvider" {
import {$BlockModelBuilder, $BlockModelBuilder$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder"
import {$BlockModelGenerators$BlockFamilyProvider, $BlockModelGenerators$BlockFamilyProvider$Type} from "packages/net/minecraft/data/models/$BlockModelGenerators$BlockFamilyProvider"
import {$BlockFamily, $BlockFamily$Type} from "packages/net/minecraft/data/$BlockFamily"
import {$TextureMapping, $TextureMapping$Type} from "packages/net/minecraft/data/models/model/$TextureMapping"
import {$ModelTemplate, $ModelTemplate$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockModelBuilder$BlockFamilyProvider extends $BlockModelGenerators$BlockFamilyProvider {

constructor(arg0: $BlockModelBuilder$Type, arg1: $TextureMapping$Type)

public "sign"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "fence"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "wall"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "button"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "fullBlock"(arg0: $Block$Type, arg1: $ModelTemplate$Type): $BlockModelBuilder$BlockFamilyProvider
public "stairs"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "fenceGate"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "slab"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "pressurePlate"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "customFenceGate"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "fullBlockCopies"(...arg0: ($Block$Type)[]): $BlockModelBuilder$BlockFamilyProvider
public "generateFor"(arg0: $BlockFamily$Type): $BlockModelBuilder$BlockFamilyProvider
public "customFence"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModelBuilder$BlockFamilyProvider$Type = ($BlockModelBuilder$BlockFamilyProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModelBuilder$BlockFamilyProvider_ = $BlockModelBuilder$BlockFamilyProvider$Type;
}}
declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockModelBuilder$WoodProvider, $BlockModelBuilder$WoodProvider$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$WoodProvider"
import {$VariantProperties$Rotation, $VariantProperties$Rotation$Type} from "packages/net/minecraft/data/models/blockstates/$VariantProperties$Rotation"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$MultiVariantGenerator, $MultiVariantGenerator$Type} from "packages/net/minecraft/data/models/blockstates/$MultiVariantGenerator"
import {$PropertyDispatch, $PropertyDispatch$Type} from "packages/net/minecraft/data/models/blockstates/$PropertyDispatch"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FrontAndTop, $FrontAndTop$Type} from "packages/net/minecraft/core/$FrontAndTop"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$DoorHingeSide, $DoorHingeSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoorHingeSide"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$PropertyDispatch$C4, $PropertyDispatch$C4$Type} from "packages/net/minecraft/data/models/blockstates/$PropertyDispatch$C4"
import {$BlockModelBuilder$TintState, $BlockModelBuilder$TintState$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$TintState"
import {$BlockStateGenerator, $BlockStateGenerator$Type} from "packages/net/minecraft/data/models/blockstates/$BlockStateGenerator"
import {$TexturedModel$Provider, $TexturedModel$Provider$Type} from "packages/net/minecraft/data/models/model/$TexturedModel$Provider"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$BlockModelBuilder$BlockEntityModelGenerator, $BlockModelBuilder$BlockEntityModelGenerator$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$BlockEntityModelGenerator"
import {$MultiPartGenerator, $MultiPartGenerator$Type} from "packages/net/minecraft/data/models/blockstates/$MultiPartGenerator"
import {$ModelTemplate, $ModelTemplate$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate"
import {$Condition$TerminalCondition, $Condition$TerminalCondition$Type} from "packages/net/minecraft/data/models/blockstates/$Condition$TerminalCondition"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockModelBuilder$BlockFamilyProvider, $BlockModelBuilder$BlockFamilyProvider$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$BlockFamilyProvider"
import {$Variant, $Variant$Type} from "packages/net/minecraft/data/models/blockstates/$Variant"
import {$BlockModelGenerators, $BlockModelGenerators$Type} from "packages/net/minecraft/data/models/$BlockModelGenerators"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$DripstoneThickness, $DripstoneThickness$Type} from "packages/net/minecraft/world/level/block/state/properties/$DripstoneThickness"
import {$TextureMapping, $TextureMapping$Type} from "packages/net/minecraft/data/models/model/$TextureMapping"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$TexturedModel, $TexturedModel$Type} from "packages/net/minecraft/data/models/model/$TexturedModel"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BlockModelBuilder extends $BlockModelGenerators {
readonly "blockStateOutput": $Consumer<($BlockStateGenerator)>
readonly "modelOutput": $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>
readonly "skippedAutoModelsOutput": $Consumer<($Item)>
 "texturedModels": $Map<($Block), ($TexturedModel)>
static readonly "MULTIFACE_GENERATOR": $List<($Pair<($BooleanProperty), ($Function<($ResourceLocation), ($Variant)>)>)>

constructor(arg0: $Consumer$Type<($BlockStateGenerator$Type)>, arg1: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>, arg2: $Consumer$Type<($Item$Type)>)

public "family"(arg0: $Block$Type): $BlockModelBuilder$BlockFamilyProvider
public "createCrossBlockWithDefaultItem"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type, arg2: $TextureMapping$Type): void
public "createCrossBlockWithDefaultItem"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type): void
public "getBlockStateOutput"(): $Consumer<($BlockStateGenerator)>
public "getSkippedAutoModelsOutput"(): $Consumer<($Item)>
public static "m_176109_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $TextureMapping$Type, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>): $BlockStateGenerator
public static "m_236316_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $TextureMapping$Type, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>): $BlockStateGenerator
public static "m_124859_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): $MultiVariantGenerator
public static "m_124862_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $MultiVariantGenerator
public static "m_124875_"(): $PropertyDispatch
public static "m_176179_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $TextureMapping$Type, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>): $BlockStateGenerator
public "m_124524_"(arg0: $Block$Type): void
public static "m_124785_"(): $PropertyDispatch
public "m_124728_"(arg0: $Block$Type): void
public static "m_124822_"(): $PropertyDispatch
public "m_124517_"(arg0: $Item$Type): void
public "m_124797_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): void
public "m_124519_"(arg0: $Item$Type, arg1: $ResourceLocation$Type): void
public "m_124575_"(arg0: $Block$Type, arg1: string): void
public static "m_124727_"(): $PropertyDispatch
public static "m_124831_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): $MultiVariantGenerator
public static "m_124622_"(arg0: $BooleanProperty$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $PropertyDispatch
public "m_124786_"(arg0: $Block$Type): void
public "m_124823_"(arg0: $Block$Type): void
public static "m_124850_"(): $PropertyDispatch
public "m_276953_"(arg0: $Block$Type): void
public static "m_124688_"(arg0: $ResourceLocation$Type): ($Variant)[]
public static "m_236283_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $ResourceLocation$Type, arg7: $ResourceLocation$Type, arg8: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124884_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $BlockStateGenerator
public static "m_236304_"(arg0: $PropertyDispatch$C4$Type<($Direction$Type), ($DoubleBlockHalf$Type), ($DoorHingeSide$Type), (boolean)>, arg1: $DoubleBlockHalf$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type): $PropertyDispatch$C4<($Direction), ($DoubleBlockHalf), ($DoorHingeSide), (boolean)>
public static "m_247086_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124904_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124838_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124809_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $ResourceLocation$Type, arg5: boolean): $BlockStateGenerator
public static "m_257856_"(arg0: $Block$Type, arg1: $TextureMapping$Type, arg2: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>): $BlockStateGenerator
public static "m_124888_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124908_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124866_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124881_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): $BlockStateGenerator
public static "m_124924_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $BlockStateGenerator
public "m_124589_"(arg0: $Block$Type, arg1: $TexturedModel$Provider$Type, arg2: $TexturedModel$Provider$Type): void
public "m_124578_"(arg0: $Block$Type, arg1: string, arg2: $ModelTemplate$Type, arg3: $Function$Type<($ResourceLocation$Type), ($TextureMapping$Type)>): $ResourceLocation
public "m_124901_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): void
public static "m_124941_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $BlockStateGenerator
public "m_124744_"(arg0: $Block$Type, arg1: $TexturedModel$Provider$Type): void
public "m_124896_"(arg0: $Block$Type): void
public static "m_124928_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $BlockStateGenerator
public "m_124567_"(arg0: $Block$Type, arg1: $TextureMapping$Type, arg2: $ModelTemplate$Type): void
public "m_124936_"(arg0: $Block$Type): void
public "m_124916_"(arg0: $Block$Type): void
public "m_124960_"(arg0: $Block$Type): void
public "m_124533_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124730_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124953_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): void
public "m_124788_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124536_"(arg0: $Block$Type, arg1: $Block$Type, arg2: $Block$Type, arg3: $Block$Type, arg4: $Block$Type, arg5: $Block$Type, arg6: $Block$Type, arg7: $Block$Type): void
public "m_124968_"(arg0: $Block$Type): void
public "m_124974_"(arg0: $Block$Type): void
public "m_124530_"(arg0: $Block$Type, arg1: $Item$Type): void
public "m_272099_"(arg0: $Block$Type): void
public "m_124921_"(arg0: $Block$Type, arg1: $ResourceLocation$Type): void
public "m_124878_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_176217_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124777_"(arg0: $TexturedModel$Provider$Type, ...arg1: ($Block$Type)[]): void
public "m_124685_"(arg0: $TexturedModel$Provider$Type, ...arg1: ($Block$Type)[]): void
public "m_124980_"(arg0: $Block$Type): void
public "m_124977_"(arg0: $Block$Type): void
public "m_124511_"(arg0: integer): $List<($Variant)>
public "m_124947_"(): $PropertyDispatch
public "m_124553_"(arg0: $Block$Type, arg1: $Property$Type<(integer)>, ...arg2: (integer)[]): void
public static "m_124626_"<T extends $Comparable<(T)>>(arg0: $Property$Type<(T)>, arg1: T, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $PropertyDispatch
public "m_124583_"(arg0: $Block$Type, arg1: $Function$Type<($Block$Type), ($TextureMapping$Type)>): void
public "m_124856_"(arg0: $Block$Type, arg1: $TexturedModel$Provider$Type): void
public "m_176249_"(arg0: $Block$Type): void
public "m_176247_"(arg0: $Block$Type): void
public "m_124713_"(...arg0: ($Block$Type)[]): void
public "m_124983_"(arg0: $Block$Type): void
public "m_124549_"(arg0: $Block$Type, arg1: $Block$Type, arg2: $BiFunction$Type<($Block$Type), ($Block$Type), ($TextureMapping$Type)>): void
public "m_124986_"(arg0: $Block$Type): void
public "m_124992_"(arg0: $Block$Type): void
public "m_176116_"(arg0: $Direction$Type, arg1: $DripstoneThickness$Type): $Variant
public "m_124989_"(arg0: $Block$Type): void
public "m_176251_"(arg0: $Block$Type): void
public static "m_124682_"(arg0: $List$Type<($ResourceLocation$Type)>, arg1: $UnaryOperator$Type<($Variant$Type)>): $List<($Variant)>
public "m_124998_"(arg0: $Block$Type): $List<($ResourceLocation)>
public "m_125001_"(arg0: $Block$Type): $List<($ResourceLocation)>
public "m_124995_"(arg0: $Block$Type): $List<($ResourceLocation)>
public "m_124599_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $Variant$Type): void
public "m_125004_"(arg0: $Block$Type): void
public "m_124918_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_125007_"(arg0: $Block$Type): void
public "m_124938_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124603_"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $TextureMapping$Type): void
public "m_124950_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124676_"(arg0: integer, arg1: integer): $ResourceLocation
public "m_124513_"(arg0: integer, arg1: string, arg2: $TextureMapping$Type): $ResourceLocation
public "m_261107_"(arg0: $MultiPartGenerator$Type, arg1: $Condition$TerminalCondition$Type, arg2: $VariantProperties$Rotation$Type): void
public "m_176085_"(arg0: $Block$Type): void
public "m_260948_"(arg0: $MultiPartGenerator$Type, arg1: $Condition$TerminalCondition$Type, arg2: $VariantProperties$Rotation$Type, arg3: $BooleanProperty$Type, arg4: $ModelTemplate$Type, arg5: boolean): void
public "m_125010_"(arg0: $Block$Type): void
public "m_124970_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124962_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124635_"(arg0: $FrontAndTop$Type, arg1: $Variant$Type): $Variant
public "run"(): void
public "m_176244_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_124564_"(arg0: $Block$Type, arg1: $TextureMapping$Type): void
public "woodProvider"(arg0: $Block$Type): $BlockModelBuilder$WoodProvider
public "createPlant"(arg0: $Block$Type, arg1: $Block$Type, arg2: $BlockModelBuilder$TintState$Type): void
public "createCrossBlock"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type, arg2: $Property$Type<(integer)>, ...arg3: (integer)[]): void
public "createCrossBlock"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type, arg2: $TextureMapping$Type): void
public "createCrossBlock"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type): void
public "createDoublePlant"(arg0: $Block$Type, arg1: $BlockModelBuilder$TintState$Type): void
public "blockEntityModels"(arg0: $ResourceLocation$Type, arg1: $Block$Type): $BlockModelBuilder$BlockEntityModelGenerator
public "blockEntityModels"(arg0: $Block$Type, arg1: $Block$Type): $BlockModelBuilder$BlockEntityModelGenerator
public "createGrowingPlant"(arg0: $Block$Type, arg1: $Block$Type, arg2: $BlockModelBuilder$TintState$Type): void
public "setTexturedModels"(arg0: $Map$Type<($Block$Type), ($TexturedModel$Type)>): void
public "getTexturedModels"(): $Map<($Block), ($TexturedModel)>
public "getModelOutput"(): $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>
get "blockStateOutput"(): $Consumer<($BlockStateGenerator)>
get "skippedAutoModelsOutput"(): $Consumer<($Item)>
set "texturedModels"(value: $Map$Type<($Block$Type), ($TexturedModel$Type)>)
get "texturedModels"(): $Map<($Block), ($TexturedModel)>
get "modelOutput"(): $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModelBuilder$Type = ($BlockModelBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModelBuilder_ = $BlockModelBuilder$Type;
}}
declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$WoodProvider" {
import {$BlockModelBuilder, $BlockModelBuilder$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder"
import {$BlockModelGenerators$WoodProvider, $BlockModelGenerators$WoodProvider$Type} from "packages/net/minecraft/data/models/$BlockModelGenerators$WoodProvider"
import {$TextureMapping, $TextureMapping$Type} from "packages/net/minecraft/data/models/model/$TextureMapping"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockModelBuilder$WoodProvider extends $BlockModelGenerators$WoodProvider {

constructor(arg0: $BlockModelBuilder$Type, arg1: $TextureMapping$Type)

public "log"(arg0: $Block$Type): $BlockModelBuilder$WoodProvider
public "wood"(arg0: $Block$Type): $BlockModelBuilder$WoodProvider
public "logUVLocked"(arg0: $Block$Type): $BlockModelBuilder$WoodProvider
public "logWithHorizontal"(arg0: $Block$Type): $BlockModelBuilder$WoodProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModelBuilder$WoodProvider$Type = ($BlockModelBuilder$WoodProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModelBuilder$WoodProvider_ = $BlockModelBuilder$WoodProvider$Type;
}}
declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$ItemModelBuilder" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$TextureSlot, $TextureSlot$Type} from "packages/net/minecraft/data/models/model/$TextureSlot"
import {$ItemModelGenerators, $ItemModelGenerators$Type} from "packages/net/minecraft/data/models/$ItemModelGenerators"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelTemplate, $ModelTemplate$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$ArmorItem, $ArmorItem$Type} from "packages/net/minecraft/world/item/$ArmorItem"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemModelBuilder extends $ItemModelGenerators {
static readonly "TRIM_TYPE_PREDICATE_ID": $ResourceLocation
readonly "output": $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>

constructor(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<($JsonElement$Type)>)>)

public "m_236321_"(arg0: $Item$Type): void
public "m_125088_"(arg0: $Item$Type, arg1: $ModelTemplate$Type): void
public "m_125084_"(arg0: $Item$Type, arg1: $Item$Type, arg2: $ModelTemplate$Type): void
public "m_236323_"(arg0: $Item$Type): void
public "m_125091_"(arg0: $Item$Type, arg1: string, arg2: $ModelTemplate$Type): void
public "m_266494_"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): void
public "m_267826_"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): void
public "m_266316_"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public "m_266208_"(arg0: $ArmorItem$Type): void
public "m_266576_"(arg0: $ResourceLocation$Type, arg1: $Map$Type<($TextureSlot$Type), ($ResourceLocation$Type)>, arg2: $ArmorMaterial$Type): $JsonObject
public "run"(): void
public "getOutput"(): $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>
get "output"(): $BiConsumer<($ResourceLocation), ($Supplier<($JsonElement)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelBuilder$Type = ($ItemModelBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelBuilder_ = $ItemModelBuilder$Type;
}}
declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$BlockEntityModelGenerator" {
import {$BlockModelBuilder, $BlockModelBuilder$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelTemplate, $ModelTemplate$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockModelGenerators$BlockEntityModelGenerator, $BlockModelGenerators$BlockEntityModelGenerator$Type} from "packages/net/minecraft/data/models/$BlockModelGenerators$BlockEntityModelGenerator"

export class $BlockModelBuilder$BlockEntityModelGenerator extends $BlockModelGenerators$BlockEntityModelGenerator {

constructor(arg0: $BlockModelBuilder$Type, arg1: $ResourceLocation$Type, arg2: $Block$Type)

public "create"(...arg0: ($Block$Type)[]): $BlockModelBuilder$BlockEntityModelGenerator
public "createWithCustomBlockItemModel"(arg0: $ModelTemplate$Type, ...arg1: ($Block$Type)[]): $BlockModelBuilder$BlockEntityModelGenerator
public "createWithoutBlockItem"(...arg0: ($Block$Type)[]): $BlockModelBuilder$BlockEntityModelGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModelBuilder$BlockEntityModelGenerator$Type = ($BlockModelBuilder$BlockEntityModelGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModelBuilder$BlockEntityModelGenerator_ = $BlockModelBuilder$BlockEntityModelGenerator$Type;
}}
declare module "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder$TintState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BlockModelBuilder$TintState extends $Enum<($BlockModelBuilder$TintState)> {
static readonly "TINTED": $BlockModelBuilder$TintState
static readonly "NOT_TINTED": $BlockModelBuilder$TintState


public static "values"(): ($BlockModelBuilder$TintState)[]
public static "valueOf"(arg0: string): $BlockModelBuilder$TintState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModelBuilder$TintState$Type = (("not_tinted") | ("tinted")) | ($BlockModelBuilder$TintState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModelBuilder$TintState_ = $BlockModelBuilder$TintState$Type;
}}
