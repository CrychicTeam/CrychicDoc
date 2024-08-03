declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$BooleanConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $BooleanConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanConverter$Type = ($BooleanConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanConverter_ = $BooleanConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$Representer" {
import {$DumperOptions, $DumperOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$TypeDescription, $TypeDescription$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$PropertyUtils, $PropertyUtils$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$SafeRepresenter, $SafeRepresenter$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$SafeRepresenter"

export class $Representer extends $SafeRepresenter {

constructor(arg0: $DumperOptions$Type)

public "setPropertyUtils"(arg0: $PropertyUtils$Type): void
public "addTypeDescription"(arg0: $TypeDescription$Type): $TypeDescription
set "propertyUtils"(value: $PropertyUtils$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Representer$Type = ($Representer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Representer_ = $Representer$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$DynamicShapedRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$DynamicShapedRecipe, $DynamicShapedRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$DynamicShapedRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $DynamicShapedRecipe$Serializer<T extends $DynamicShapedRecipe> implements $RecipeSerializer<(T)> {

constructor()

public static "fromJson"(arg0: $DynamicShapedRecipe$Type, arg1: $JsonObject$Type): void
public static "fromNetwork"(arg0: $DynamicShapedRecipe$Type, arg1: $FriendlyByteBuf$Type): void
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: T): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): T
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): T
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicShapedRecipe$Serializer$Type<T> = ($DynamicShapedRecipe$Serializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicShapedRecipe$Serializer_<T> = $DynamicShapedRecipe$Serializer$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$ConstructorException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"

export class $ConstructorException extends $MarkedYAMLException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorException$Type = ($ConstructorException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorException_ = $ConstructorException$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KBlockTemplate$Type<T extends $KBlockTemplate> extends $Record {

constructor(codec: $Function$Type<($MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>), ($Codec$Type<(T)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "codec"(): $Function<($MapCodec<($Optional<($KMaterial)>)>), ($Codec<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockTemplate$Type$Type<T> = ($KBlockTemplate$Type<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockTemplate$Type_<T> = $KBlockTemplate$Type$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/shape/$HorizontalShape" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$AbstractHorizontalShape, $AbstractHorizontalShape$Type} from "packages/snownee/kiwi/customization/shape/$AbstractHorizontalShape"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $HorizontalShape extends $Record implements $AbstractHorizontalShape {

constructor(shapes: ($VoxelShape$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $ShapeGenerator$Type): $ShapeGenerator
public "getDirection"(arg0: $BlockState$Type): $Direction
public "shapes"(): ($VoxelShape)[]
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalShape$Type = ($HorizontalShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalShape_ = $HorizontalShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$EmitterException" {
import {$YAMLException, $YAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $EmitterException extends $YAMLException {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmitterException$Type = ($EmitterException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmitterException_ = $EmitterException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/resolver/$Resolver" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $Resolver {
static readonly "BOOL": $Pattern
static readonly "FLOAT": $Pattern
static readonly "INT": $Pattern
static readonly "MERGE": $Pattern
static readonly "NULL": $Pattern
static readonly "EMPTY": $Pattern
static readonly "TIMESTAMP": $Pattern
static readonly "VALUE": $Pattern
static readonly "YAML": $Pattern

constructor()

public "resolve"(arg0: $NodeId$Type, arg1: string, arg2: boolean): $Tag
public "addImplicitResolver"(arg0: $Tag$Type, arg1: $Pattern$Type, arg2: string): void
public "addImplicitResolver"(arg0: $Tag$Type, arg1: $Pattern$Type, arg2: string, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Resolver$Type = ($Resolver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Resolver_ = $Resolver$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$FunctionDictionaryIfc" {
import {$FunctionIfc, $FunctionIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc"

export interface $FunctionDictionaryIfc {

 "getFunction"(arg0: string): $FunctionIfc
 "addFunction"(arg0: string, arg1: $FunctionIfc$Type): void
 "hasFunction"(arg0: string): boolean
}

export namespace $FunctionDictionaryIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionDictionaryIfc$Type = ($FunctionDictionaryIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionDictionaryIfc_ = $FunctionDictionaryIfc$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$LogFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $LogFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogFunction$Type = ($LogFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogFunction_ = $LogFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$MethodProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PropertyDescriptor, $PropertyDescriptor$Type} from "packages/java/beans/$PropertyDescriptor"
import {$GenericProperty, $GenericProperty$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty"

export class $MethodProperty extends $GenericProperty {

constructor(arg0: $PropertyDescriptor$Type)

public "get"(arg0: any): any
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
public "isReadable"(): boolean
public "isWritable"(): boolean
get "annotations"(): $List<($Annotation)>
get "readable"(): boolean
get "writable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodProperty$Type = ($MethodProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodProperty_ = $MethodProperty$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcosRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcosRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcosRFunction$Type = ($AcosRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcosRFunction_ = $AcosRFunction$Type;
}}
declare module "packages/snownee/kiwi/network/$KPacketTarget" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $KPacketTarget {

 "internal"(): $PacketDistributor$PacketTarget
 "send"(arg0: $PacketHandler$Type, arg1: $Consumer$Type<($FriendlyByteBuf$Type)>): void

(arg0: $MinecraftServer$Type): $KPacketTarget
}

export namespace $KPacketTarget {
function all(arg0: $MinecraftServer$Type): $KPacketTarget
function tracking(arg0: $ServerLevel$Type, arg1: $BlockPos$Type): $KPacketTarget
function tracking(arg0: $Entity$Type): $KPacketTarget
function tracking(arg0: $BlockEntity$Type): $KPacketTarget
function tracking(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type): $KPacketTarget
function world(arg0: $ServerLevel$Type): $KPacketTarget
function around(arg0: $ServerLevel$Type, arg1: $Vec3i$Type, arg2: double): $KPacketTarget
function around(arg0: $ServerLevel$Type, arg1: $Vec3$Type, arg2: double): $KPacketTarget
function allExcept(arg0: $ServerPlayer$Type): $KPacketTarget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KPacketTarget$Type = ($KPacketTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KPacketTarget_ = $KPacketTarget$Type;
}}
declare module "packages/snownee/kiwi/recipe/$FullBlockIngredient$Serializer" {
import {$FullBlockIngredient, $FullBlockIngredient$Type} from "packages/snownee/kiwi/recipe/$FullBlockIngredient"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IIngredientSerializer, $IIngredientSerializer$Type} from "packages/net/minecraftforge/common/crafting/$IIngredientSerializer"

export class $FullBlockIngredient$Serializer implements $IIngredientSerializer<($FullBlockIngredient)> {

constructor()

public "write"(arg0: $FriendlyByteBuf$Type, arg1: $FullBlockIngredient$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullBlockIngredient$Serializer$Type = ($FullBlockIngredient$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullBlockIngredient$Serializer_ = $FullBlockIngredient$Serializer$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BuilderRule" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export interface $BuilderRule {

 "matches"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $BlockState$Type): boolean
 "apply"(arg0: $UseOnContext$Type, arg1: $List$Type<($BlockPos$Type)>): void
 "relatedBlocks"(): $Stream<($Block)>
 "searchPositions"(arg0: $UseOnContext$Type): $List<($BlockPos)>
}

export namespace $BuilderRule {
const CODEC: $Codec<($BuilderRule)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderRule$Type = ($BuilderRule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderRule_ = $BuilderRule$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$CoalesceFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CoalesceFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoalesceFunction$Type = ($CoalesceFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoalesceFunction_ = $CoalesceFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BlockSpread" {
import {$FacingLimitation, $FacingLimitation$Type} from "packages/snownee/kiwi/customization/builder/$FacingLimitation"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockSpread$Type, $BlockSpread$Type$Type} from "packages/snownee/kiwi/customization/builder/$BlockSpread$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$StatePropertiesPredicate, $StatePropertiesPredicate$Type} from "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockSpread extends $Record {

constructor(type: $BlockSpread$Type$Type, statePropertiesPredicate: $Optional$Type<($StatePropertiesPredicate$Type)>, facingLimitation: $FacingLimitation$Type, maxDistance: integer)

public "type"(): $BlockSpread$Type
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "collect"(arg0: $UseOnContext$Type, arg1: $Predicate$Type<($Block$Type)>): $List<($BlockPos)>
public "collect"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $Player$Type, arg3: $Predicate$Type<($Block$Type)>): $List<($BlockPos)>
public "maxDistance"(): integer
public "facingLimitation"(): $FacingLimitation
public "statePropertiesPredicate"(): $Optional<($StatePropertiesPredicate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockSpread$Type = ($BlockSpread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockSpread_ = $BlockSpread$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler$Compound" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CanSurviveHandler, $CanSurviveHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $CanSurviveHandler$Compound extends $Record implements $CanSurviveHandler {

constructor(any: boolean, handlers: $List$Type<($CanSurviveHandler$Type)>)

public "isSensitiveSide"(arg0: $BlockState$Type, arg1: $Direction$Type): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "handlers"(): $List<($CanSurviveHandler)>
public "any"(): boolean
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public static "checkFace"(arg0: $DirectionProperty$Type): $CanSurviveHandler
public static "checkCeiling"(): $CanSurviveHandler
public static "checkFloor"(): $CanSurviveHandler
public static "all"(arg0: $List$Type<($CanSurviveHandler$Type)>): $CanSurviveHandler$Compound
public static "any"(arg0: $List$Type<($CanSurviveHandler$Type)>): $CanSurviveHandler$Compound
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CanSurviveHandler$Compound$Type = ($CanSurviveHandler$Compound);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CanSurviveHandler$Compound_ = $CanSurviveHandler$Compound$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixMultiplicationOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixMultiplicationOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixMultiplicationOperator$Type = ($InfixMultiplicationOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixMultiplicationOperator_ = $InfixMultiplicationOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CosRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CosRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosRFunction$Type = ($CosRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosRFunction_ = $CosRFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$SlotLink$TagTest" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$TagTestOperator, $TagTestOperator$Type} from "packages/snownee/kiwi/customization/placement/$TagTestOperator"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SlotLink$TagTest extends $Record {
static readonly "CODEC": $Codec<($SlotLink$TagTest)>

constructor(key: string, operator: $TagTestOperator$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "key"(): string
public "operator"(): $TagTestOperator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotLink$TagTest$Type = ($SlotLink$TagTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotLink$TagTest_ = $SlotLink$TagTest$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixPowerOfOperator" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixPowerOfOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
public "getPrecedence"(arg0: $ExpressionConfiguration$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixPowerOfOperator$Type = ($InfixPowerOfOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixPowerOfOperator_ = $InfixPowerOfOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeNowFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeNowFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeNowFunction$Type = ($DateTimeNowFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeNowFunction_ = $DateTimeNowFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$ExportCreativeTabsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $ExportCreativeTabsCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExportCreativeTabsCommand$Type = ($ExportCreativeTabsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExportCreativeTabsCommand_ = $ExportCreativeTabsCommand$Type;
}}
declare module "packages/snownee/kiwi/util/codec/$JavaOps" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ListBuilder, $ListBuilder$Type} from "packages/com/mojang/serialization/$ListBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecordBuilder, $RecordBuilder$Type} from "packages/com/mojang/serialization/$RecordBuilder"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$MapLike, $MapLike$Type} from "packages/com/mojang/serialization/$MapLike"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JavaOps implements $DynamicOps<(any)> {
static readonly "INSTANCE": $JavaOps


public "remove"(arg0: any, arg1: string): any
public "toString"(): string
public "empty"(): any
public "emptyList"(): any
public "getByteBuffer"(arg0: any): $DataResult<($ByteBuffer)>
public "getMap"(arg0: any): $DataResult<($MapLike<(any)>)>
public "createMap"(arg0: $Stream$Type<($Pair$Type<(any), (any)>)>): any
public "createMap"(arg0: $Map$Type<(any), (any)>): any
public "emptyMap"(): any
public "createLong"(arg0: long): any
public "createString"(arg0: string): any
public "getList"(arg0: any): $DataResult<($Consumer<($Consumer<(any)>)>)>
public "createList"(arg0: $Stream$Type<(any)>): any
public "getBooleanValue"(arg0: any): $DataResult<(boolean)>
public "getStringValue"(arg0: any): $DataResult<(string)>
public "mapBuilder"(): $RecordBuilder<(any)>
public "createFloat"(arg0: float): any
public "createDouble"(arg0: double): any
public "getStream"(arg0: any): $DataResult<($Stream<(any)>)>
public "getNumberValue"(arg0: any): $DataResult<(number)>
public "createBoolean"(arg0: boolean): any
public "createInt"(arg0: integer): any
public "createShort"(arg0: short): any
public "createByte"(arg0: byte): any
public "createLongList"(arg0: $LongStream$Type): any
public "mergeToMap"(arg0: any, arg1: any, arg2: any): $DataResult<(any)>
public "mergeToMap"(arg0: any, arg1: $MapLike$Type<(any)>): $DataResult<(any)>
public "mergeToMap"(arg0: any, arg1: $Map$Type<(any), (any)>): $DataResult<(any)>
public "getIntStream"(arg0: any): $DataResult<($IntStream)>
public "createIntList"(arg0: $IntStream$Type): any
public "mergeToList"(arg0: any, arg1: $List$Type<(any)>): $DataResult<(any)>
public "mergeToList"(arg0: any, arg1: any): $DataResult<(any)>
public "getLongStream"(arg0: any): $DataResult<($LongStream)>
public "createByteList"(arg0: $ByteBuffer$Type): any
public "createNumeric"(arg0: number): any
public "getMapValues"(arg0: any): $DataResult<($Stream<($Pair<(any), (any)>)>)>
public "convertTo"<U>(arg0: $DynamicOps$Type<(U)>, arg1: any): U
public "getMapEntries"(arg0: any): $DataResult<($Consumer<($BiConsumer<(any), (any)>)>)>
public "get"(arg0: any, arg1: string): $DataResult<(any)>
public "update"(arg0: any, arg1: string, arg2: $Function$Type<(any), (any)>): any
public "set"(arg0: any, arg1: string, arg2: any): any
public "mergeToPrimitive"(arg0: any, arg1: any): $DataResult<(any)>
public "compressMaps"(): boolean
public "getNumberValue"(arg0: any, arg1: number): number
public "getGeneric"(arg0: any, arg1: any): $DataResult<(any)>
public "listBuilder"(): $ListBuilder<(any)>
public "withDecoder"<E>(arg0: $Decoder$Type<(E)>): $Function<(any), ($DataResult<($Pair<(E), (any)>)>)>
public "updateGeneric"(arg0: any, arg1: any, arg2: $Function$Type<(any), (any)>): any
public "convertList"<U>(arg0: $DynamicOps$Type<(U)>, arg1: any): U
public "convertMap"<U>(arg0: $DynamicOps$Type<(U)>, arg1: any): U
public "withEncoder"<E>(arg0: $Encoder$Type<(E)>): $Function<(E), ($DataResult<(any)>)>
public "withParser"<E>(arg0: $Decoder$Type<(E)>): $Function<(any), ($DataResult<(E)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JavaOps$Type = ($JavaOps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JavaOps_ = $JavaOps$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SecFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SecFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecFunction$Type = ($SecFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecFunction_ = $SecFunction$Type;
}}
declare module "packages/snownee/kiwi/util/$ClientProxy$Context" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $ClientProxy$Context extends $Record {

constructor(loading: boolean, modEventBus: $IEventBus$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "modEventBus"(): $IEventBus
public "loading"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProxy$Context$Type = ($ClientProxy$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProxy$Context_ = $ClientProxy$Context$Type;
}}
declare module "packages/snownee/kiwi/$ModuleInfo" {
import {$PostInitEvent, $PostInitEvent$Type} from "packages/snownee/kiwi/loader/event/$PostInitEvent"
import {$ModContext, $ModContext$Type} from "packages/snownee/kiwi/$ModContext"
import {$ClientInitEvent, $ClientInitEvent$Type} from "packages/snownee/kiwi/loader/event/$ClientInitEvent"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$ServerInitEvent, $ServerInitEvent$Type} from "packages/snownee/kiwi/loader/event/$ServerInitEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GroupSetting, $GroupSetting$Type} from "packages/snownee/kiwi/$GroupSetting"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$InitEvent, $InitEvent$Type} from "packages/snownee/kiwi/loader/event/$InitEvent"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"
import {$NamedEntry, $NamedEntry$Type} from "packages/snownee/kiwi/$NamedEntry"

export class $ModuleInfo {
readonly "module": $AbstractModule
readonly "context": $ModContext
 "groupSetting": $GroupSetting

constructor(arg0: $ResourceLocation$Type, arg1: $AbstractModule$Type, arg2: $ModContext$Type)

public "register"(arg0: any, arg1: $ResourceLocation$Type, arg2: any, arg3: $Field$Type): void
public "init"(arg0: $InitEvent$Type): void
public "clientInit"(arg0: $ClientInitEvent$Type): void
public "serverInit"(arg0: $ServerInitEvent$Type): void
public "getRegistryEntries"<T>(arg0: any): $Stream<($NamedEntry<(T)>)>
public "postInit"(arg0: $PostInitEvent$Type): void
public "handleRegister"(arg0: any): void
public "preInit"(): void
public "getRegistries"<T>(arg0: any): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleInfo$Type = ($ModuleInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleInfo_ = $ModuleInfo$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/util/$PlatformFeatureDetector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformFeatureDetector {

constructor()

public "isRunningOnAndroid"(): boolean
get "runningOnAndroid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformFeatureDetector$Type = ($PlatformFeatureDetector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformFeatureDetector_ = $PlatformFeatureDetector$Type;
}}
declare module "packages/snownee/kiwi/recipe/$EmptyInventory" {
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

export class $EmptyInventory implements $Container {

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
export type $EmptyInventory$Type = ($EmptyInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyInventory_ = $EmptyInventory$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$Version" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$Version extends $Enum<($DumperOptions$Version)> {
static readonly "V1_0": $DumperOptions$Version
static readonly "V1_1": $DumperOptions$Version


public "toString"(): string
public static "values"(): ($DumperOptions$Version)[]
public static "valueOf"(arg0: string): $DumperOptions$Version
public "major"(): integer
public "minor"(): integer
public "getRepresentation"(): string
get "representation"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$Version$Type = (("v1_0") | ("v1_1")) | ($DumperOptions$Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$Version_ = $DumperOptions$Version$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$AliasToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $AliasToken extends $Token {

constructor(arg0: string, arg1: $Mark$Type, arg2: $Mark$Type)

public "getValue"(): string
public "getTokenId"(): $Token$ID
get "value"(): string
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AliasToken$Type = ($AliasToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AliasToken_ = $AliasToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcotHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcotHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcotHFunction$Type = ($AcotHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcotHFunction_ = $AcotHFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$CycleVariantsComponent" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$LayeredComponent, $LayeredComponent$Type} from "packages/snownee/kiwi/customization/block/component/$LayeredComponent"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $CycleVariantsComponent extends $Record implements $KBlockComponent, $LayeredComponent {
static readonly "CODEC": $Codec<($CycleVariantsComponent)>

constructor(property: $IntegerProperty$Type, rightClickToCycle: boolean)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: integer): $CycleVariantsComponent
public static "create"(arg0: integer, arg1: boolean): $CycleVariantsComponent
public "maxValue"(): integer
public "property"(): $IntegerProperty
public "minValue"(): integer
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "getLayerProperty"(): $IntegerProperty
public "getDefaultLayer"(): integer
public "rightClickToCycle"(): boolean
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
get "layerProperty"(): $IntegerProperty
get "defaultLayer"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CycleVariantsComponent$Type = ($CycleVariantsComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CycleVariantsComponent_ = $CycleVariantsComponent$Type;
}}
declare module "packages/snownee/kiwi/util/$LocalizableItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $LocalizableItem {

 "getDisplayName"(): $Component
 "getDescription"(): $Component

(): $Component
}

export namespace $LocalizableItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalizableItem$Type = ($LocalizableItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalizableItem_ = $LocalizableItem$Type;
}}
declare module "packages/snownee/kiwi/block/def/$BlockDefinition$Factory" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockDefinition, $BlockDefinition$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export interface $BlockDefinition$Factory<T extends $BlockDefinition> {

 "getId"(): string
 "fromNBT"(arg0: $CompoundTag$Type): T
 "fromBlock"(arg0: $BlockState$Type, arg1: $BlockEntity$Type, arg2: $LevelReader$Type, arg3: $BlockPos$Type): T
 "fromItem"(arg0: $ItemStack$Type, arg1: $BlockPlaceContext$Type): T
}

export namespace $BlockDefinition$Factory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDefinition$Factory$Type<T> = ($BlockDefinition$Factory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDefinition$Factory_<T> = $BlockDefinition$Factory$Type<(T)>;
}}
declare module "packages/snownee/kiwi/data/$DataModule" {
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$RetextureRecipe, $RetextureRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$RetextureRecipe"
import {$KiwiGO, $KiwiGO$Type} from "packages/snownee/kiwi/$KiwiGO"
import {$KiwiShapelessRecipe, $KiwiShapelessRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$KiwiShapelessRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$NoContainersShapedRecipe, $NoContainersShapedRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$NoContainersShapedRecipe"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"

export class $DataModule extends $AbstractModule {
static readonly "SHAPED_NO_CONTAINERS": $KiwiGO<($RecipeSerializer<($NoContainersShapedRecipe)>)>
static readonly "RETEXTURE": $KiwiGO<($RecipeSerializer<($RetextureRecipe)>)>
static readonly "SHAPELESS": $KiwiGO<($RecipeSerializer<($KiwiShapelessRecipe)>)>
 "uid": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataModule$Type = ($DataModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataModule_ = $DataModule$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$InfixOperator" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InfixOperator extends $Annotation {

 "precedence"(): integer
 "leftAssociative"(): boolean
 "operandsLazy"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $InfixOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixOperator$Type = ($InfixOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixOperator_ = $InfixOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/internal/$Logger" {
import {$Logger$Level, $Logger$Level$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/internal/$Logger$Level"

export class $Logger {


public static "getLogger"(arg0: string): $Logger
public "warn"(arg0: string): void
public "isLoggable"(arg0: $Logger$Level$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Logger$Type = ($Logger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Logger_ = $Logger$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceTarget" {
import {$PlaceTarget$Type, $PlaceTarget$Type$Type} from "packages/snownee/kiwi/customization/placement/$PlaceTarget$Type"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceTarget extends $Record {
static readonly "CODEC": $Codec<($PlaceTarget)>

constructor(type: $PlaceTarget$Type$Type, id: $ResourceLocation$Type)

public "type"(): $PlaceTarget$Type
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: string): $PlaceTarget
public "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceTarget$Type = ($PlaceTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceTarget_ = $PlaceTarget$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$DirectionalShape" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $DirectionalShape extends $Record implements $ShapeGenerator {

constructor(shapes: ($VoxelShape$Type)[], property: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $ShapeGenerator$Type, arg1: string): $ShapeGenerator
public "property"(): string
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "shapes"(): ($VoxelShape)[]
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionalShape$Type = ($DirectionalShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionalShape_ = $DirectionalShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixEqualsOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixEqualsOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixEqualsOperator$Type = ($InfixEqualsOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixEqualsOperator_ = $InfixEqualsOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CotHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CotHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CotHFunction$Type = ($CotHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CotHFunction_ = $CotHFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ItemDefinitionProperties$PartialVanillaProperties, $ItemDefinitionProperties$PartialVanillaProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties$PartialVanillaProperties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemDefinitionProperties extends $Record {

constructor(colorProvider: $Optional$Type<($ResourceLocation$Type)>, vanillaProperties: $ItemDefinitionProperties$PartialVanillaProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "merge"(arg0: $ItemDefinitionProperties$Type): $ItemDefinitionProperties
public static "empty"(): $ItemDefinitionProperties
public "colorProvider"(): $Optional<($ResourceLocation)>
public static "mapCodec"(): $MapCodec<($ItemDefinitionProperties)>
public static "mapCodecField"(): $MapCodec<($Optional<($ItemDefinitionProperties)>)>
public "vanillaProperties"(): $ItemDefinitionProperties$PartialVanillaProperties
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDefinitionProperties$Type = ($ItemDefinitionProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDefinitionProperties_ = $ItemDefinitionProperties$Type;
}}
declare module "packages/snownee/kiwi/mixin/$AxeItemAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AxeItemAccess {

}

export namespace $AxeItemAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AxeItemAccess$Type = ($AxeItemAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AxeItemAccess_ = $AxeItemAccess$Type;
}}
declare module "packages/snownee/kiwi/util/$KHolder" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $KHolder<T> extends $Record {

constructor(key: $ResourceLocation$Type, value: T)

public "value"(): T
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "key"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KHolder$Type<T> = ($KHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KHolder_<T> = $KHolder$Type<(T)>;
}}
declare module "packages/snownee/kiwi/block/def/$SimpleBlockDefinition" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockDefinition, $BlockDefinition$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockDefinition$Factory, $BlockDefinition$Factory$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition$Factory"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $SimpleBlockDefinition implements $BlockDefinition {
static readonly "TYPE": string
readonly "state": $BlockState


public "toString"(): string
public static "of"(arg0: $BlockState$Type): $SimpleBlockDefinition
public "getFactory"(): $BlockDefinition$Factory<(any)>
public "save"(arg0: $CompoundTag$Type): void
public static "reload"(): void
public "getDescription"(): $Component
public "canOcclude"(): boolean
public "place"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "getSoundType"(): $SoundType
public "getColor"(arg0: $BlockState$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type, arg3: integer): integer
public "getBlockState"(): $BlockState
public "renderMaterial"(arg0: $Direction$Type): $Material
public "model"(): $BakedModel
public "getRenderTypes"(): $ChunkRenderTypeSet
public "createItem"(arg0: $HitResult$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Player$Type): $ItemStack
public static "getCamo"(arg0: $BlockDefinition$Type): $BlockDefinition
public "getCamoDefinition"(): $BlockDefinition
public static "registerFactory"(arg0: $BlockDefinition$Factory$Type<(any)>): void
public static "fromNBT"(arg0: $CompoundTag$Type): $BlockDefinition
public "canRenderInLayer"(arg0: $RenderType$Type): boolean
public "getLightEmission"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): integer
public static "fromBlock"(arg0: $BlockState$Type, arg1: $BlockEntity$Type, arg2: $LevelReader$Type, arg3: $BlockPos$Type): $BlockDefinition
public static "fromItem"(arg0: $ItemStack$Type, arg1: $BlockPlaceContext$Type): $BlockDefinition
public "modelData"(): $ModelData
get "factory"(): $BlockDefinition$Factory<(any)>
get "description"(): $Component
get "soundType"(): $SoundType
get "blockState"(): $BlockState
get "renderTypes"(): $ChunkRenderTypeSet
get "camoDefinition"(): $BlockDefinition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleBlockDefinition$Type = ($SimpleBlockDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleBlockDefinition_ = $SimpleBlockDefinition$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeFormatFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeFormatFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
public "validatePreEvaluation"(arg0: $Token$Type, ...arg1: ($EvaluationValue$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeFormatFunction$Type = ($DateTimeFormatFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeFormatFunction_ = $DateTimeFormatFunction$Type;
}}
declare module "packages/snownee/kiwi/$AbstractModule" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$KiwiGO, $KiwiGO$Type} from "packages/snownee/kiwi/$KiwiGO"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockEntityType$BlockEntitySupplier, $BlockEntityType$BlockEntitySupplier$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$BlockEntitySupplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $AbstractModule {
 "uid": $ResourceLocation

constructor()

public static "tag"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: string): $TagKey<(T)>
public static "itemTag"(arg0: string, arg1: string): $TagKey<($Item)>
public static "entityTag"(arg0: string, arg1: string): $TagKey<($EntityType<(any)>)>
public static "blockTag"(arg0: string, arg1: string): $TagKey<($Block)>
public static "fluidTag"(arg0: string, arg1: string): $TagKey<($Fluid)>
public static "blockEntity"<T extends $BlockEntity>(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $Type$Type<(any)>, ...arg2: ($Supplier$Type<(any)>)[]): $KiwiGO<($BlockEntityType<(T)>)>
public static "blockEntity"<T extends $BlockEntity>(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $Type$Type<(any)>, arg2: $Class$Type<(any)>): $KiwiGO<($BlockEntityType<(T)>)>
/**
 * 
 * @deprecated
 */
public static "blockEntity"<T extends $BlockEntity>(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $Type$Type<(any)>, arg2: $TagKey$Type<($Block$Type)>): $KiwiGO<($BlockEntityType<(T)>)>
public "RL"(arg0: string): $ResourceLocation
public static "itemCategory"(arg0: string, arg1: string, arg2: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTab$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModule$Type = ($AbstractModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModule_ = $AbstractModule$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$ItemButton$Builder" {
import {$ItemButton, $ItemButton$Type} from "packages/snownee/kiwi/customization/builder/$ItemButton"
import {$Button$Builder, $Button$Builder$Type} from "packages/net/minecraft/client/gui/components/$Button$Builder"

export class $ItemButton$Builder extends $Button$Builder {


public "build"(): $ItemButton
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemButton$Builder$Type = ($ItemButton$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemButton$Builder_ = $ItemButton$Builder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CscFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CscFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CscFunction$Type = ($CscFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CscFunction_ = $CscFunction$Type;
}}
declare module "packages/snownee/kiwi/$KiwiGO" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $KiwiGO<T> implements $Supplier<(T)> {

constructor(arg0: $Supplier$Type<(T)>)

public "get"(): T
public "key"(): $ResourceLocation
public "is"(arg0: $BlockState$Type): boolean
public "is"(arg0: $ItemStack$Type): boolean
public "is"(arg0: any): boolean
public "getOrCreate"(): T
public "setKey"(arg0: $ResourceLocation$Type): void
public "registry"(): any
public "defaultBlockState"(): $BlockState
public "itemStack"(arg0: integer): $ItemStack
public "itemStack"(): $ItemStack
get "orCreate"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiGO$Type<T> = ($KiwiGO<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiGO_<T> = $KiwiGO$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token$TokenType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Token$TokenType extends $Enum<($Token$TokenType)> {
static readonly "BRACE_OPEN": $Token$TokenType
static readonly "BRACE_CLOSE": $Token$TokenType
static readonly "COMMA": $Token$TokenType
static readonly "STRING_LITERAL": $Token$TokenType
static readonly "NUMBER_LITERAL": $Token$TokenType
static readonly "VARIABLE_OR_CONSTANT": $Token$TokenType
static readonly "INFIX_OPERATOR": $Token$TokenType
static readonly "PREFIX_OPERATOR": $Token$TokenType
static readonly "POSTFIX_OPERATOR": $Token$TokenType
static readonly "FUNCTION": $Token$TokenType
static readonly "FUNCTION_PARAM_START": $Token$TokenType
static readonly "ARRAY_OPEN": $Token$TokenType
static readonly "ARRAY_CLOSE": $Token$TokenType
static readonly "ARRAY_INDEX": $Token$TokenType
static readonly "STRUCTURE_SEPARATOR": $Token$TokenType


public static "values"(): ($Token$TokenType)[]
public static "valueOf"(arg0: string): $Token$TokenType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$TokenType$Type = (("infix_operator") | ("postfix_operator") | ("array_open") | ("array_close") | ("brace_open") | ("string_literal") | ("comma") | ("prefix_operator") | ("variable_or_constant") | ("brace_close") | ("structure_separator") | ("function") | ("number_literal") | ("function_param_start") | ("array_index")) | ($Token$TokenType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token$TokenType_ = $Token$TokenType$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$Atan2Function" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $Atan2Function extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Atan2Function$Type = ($Atan2Function);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Atan2Function_ = $Atan2Function$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$PanelLayout" {
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Rect2i, $Rect2i$Type} from "packages/net/minecraft/client/renderer/$Rect2i"

export class $PanelLayout {

constructor(arg0: integer)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "update"(): void
public "bounds"(): $Rect2i
public "bind"(arg0: $Screen$Type, arg1: $Vector2i$Type, arg2: $Vector2f$Type): void
public "addWidget"(arg0: $AbstractWidget$Type): void
public "getAnchoredPos"(): $Vector2i
public "widgets"(): $List<($AbstractWidget)>
get "anchoredPos"(): $Vector2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PanelLayout$Type = ($PanelLayout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PanelLayout_ = $PanelLayout$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties$PartialVanillaProperties" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$FoodProperties, $FoodProperties$Type} from "packages/net/minecraft/world/food/$FoodProperties"

export class $ItemDefinitionProperties$PartialVanillaProperties extends $Record {
static readonly "MAP_CODEC": $MapCodec<($ItemDefinitionProperties$PartialVanillaProperties)>

constructor(maxStackSize: $Optional$Type<(integer)>, maxDamage: $Optional$Type<(integer)>, craftingRemainingItem: $Optional$Type<($ResourceKey$Type<($Item$Type)>)>, food: $Optional$Type<($FoodProperties$Type)>, rarity: $Optional$Type<($Rarity$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "merge"(arg0: $ItemDefinitionProperties$PartialVanillaProperties$Type): $ItemDefinitionProperties$PartialVanillaProperties
public "maxStackSize"(): $Optional<(integer)>
public "food"(): $Optional<($FoodProperties)>
public "craftingRemainingItem"(): $Optional<($ResourceKey<($Item)>)>
public "maxDamage"(): $Optional<(integer)>
public "rarity"(): $Optional<($Rarity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDefinitionProperties$PartialVanillaProperties$Type = ($ItemDefinitionProperties$PartialVanillaProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDefinitionProperties$PartialVanillaProperties_ = $ItemDefinitionProperties$PartialVanillaProperties$Type;
}}
declare module "packages/snownee/kiwi/util/resource/$OneTimeLoader" {
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Resource, $Resource$Type} from "packages/net/minecraft/server/packs/resources/$Resource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $OneTimeLoader {

constructor()

public static "load"<T>(arg0: $ResourceManager$Type, arg1: string, arg2: $Codec$Type<(T)>, arg3: $OneTimeLoader$Context$Type): $Map<($ResourceLocation), (T)>
public static "loadFile"<T>(arg0: $ResourceManager$Type, arg1: string, arg2: $ResourceLocation$Type, arg3: $Codec$Type<(T)>, arg4: $OneTimeLoader$Context$Type): T
public static "parseFile"<T>(arg0: $ResourceLocation$Type, arg1: $Resource$Type, arg2: $Codec$Type<(T)>, arg3: $OneTimeLoader$Context$Type): $DataResult<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OneTimeLoader$Type = ($OneTimeLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OneTimeLoader_ = $OneTimeLoader$Type;
}}
declare module "packages/snownee/kiwi/config/$ClothConfigIntegration" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ClothConfigIntegration {

constructor()

public static "create"(arg0: $Screen$Type, arg1: string): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigIntegration$Type = ($ClothConfigIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigIntegration_ = $ClothConfigIntegration$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$ItemCodecs" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemCodecs {
static readonly "ITEM_PROPERTIES_KEY": string
static readonly "SIMPLE_ITEM_FACTORY": $Function<($Item$Properties), ($Item)>
static readonly "ITEM": $MapCodec<($Item)>

constructor()

public static "get"(arg0: $ResourceLocation$Type): $MapCodec<($Item)>
public static "register"(arg0: $ResourceLocation$Type, arg1: $MapCodec$Type<(any)>): void
public static "propertiesCodec"<I extends $Item>(): $RecordCodecBuilder<(I), ($Item$Properties)>
public static "simpleCodec"<I extends $Item>(arg0: $Function$Type<($Item$Properties$Type), (I)>): $MapCodec<(I)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemCodecs$Type = ($ItemCodecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemCodecs_ = $ItemCodecs$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$SimpleItemTemplate" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SimpleItemTemplate extends $KItemTemplate {

constructor(arg0: $Optional$Type<($ItemDefinitionProperties$Type)>, arg1: string)

public "clazz"(): string
public "type"(): $KItemTemplate$Type<(any)>
public "toString"(): string
public "resolve"(arg0: $ResourceLocation$Type): void
public static "directCodec"(): $Codec<($SimpleItemTemplate)>
public "createItem"(arg0: $ResourceLocation$Type, arg1: $Item$Properties$Type, arg2: $JsonObject$Type): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleItemTemplate$Type = ($SimpleItemTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleItemTemplate_ = $SimpleItemTemplate$Type;
}}
declare module "packages/snownee/kiwi/network/$IPacketHandler" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$KiwiPacket$Direction, $KiwiPacket$Direction$Type} from "packages/snownee/kiwi/network/$KiwiPacket$Direction"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $IPacketHandler {

 "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
 "getDirection"(): $KiwiPacket$Direction

(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
}

export namespace $IPacketHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPacketHandler$Type = ($IPacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPacketHandler_ = $IPacketHandler$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$Constant" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Constant {
static readonly "LINEBR": $Constant
static readonly "NULL_OR_LINEBR": $Constant
static readonly "NULL_BL_LINEBR": $Constant
static readonly "NULL_BL_T_LINEBR": $Constant
static readonly "NULL_BL_T": $Constant
static readonly "URI_CHARS": $Constant
static readonly "ALPHA": $Constant


public "has"(arg0: integer, arg1: string): boolean
public "has"(arg0: integer): boolean
public "hasNo"(arg0: integer): boolean
public "hasNo"(arg0: integer, arg1: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constant$Type = ($Constant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constant_ = $Constant$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$KBlockSettings$Builder" {
import {$GlassType, $GlassType$Type} from "packages/snownee/kiwi/customization/block/$GlassType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$CanSurviveHandler, $CanSurviveHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockShapeType, $BlockShapeType$Type} from "packages/snownee/kiwi/customization/shape/$BlockShapeType"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"

export class $KBlockSettings$Builder {


public "get"(): $BlockBehaviour$Properties
public "component"(arg0: $KBlockComponent$Type): $KBlockSettings$Builder
public "shape"(arg0: $BlockShapeType$Type, arg1: $ShapeGenerator$Type): $KBlockSettings$Builder
public "configure"(arg0: $Consumer$Type<($BlockBehaviour$Properties$Type)>): $KBlockSettings$Builder
public "getAnalogOutputSignal"(): $ToIntFunction<($BlockState)>
public "noOcclusion"(): $KBlockSettings$Builder
public "noCollision"(): $KBlockSettings$Builder
public "directional"(): $KBlockSettings$Builder
public "hasComponent"(arg0: $KBlockComponent$Type$Type<(any)>): boolean
public "horizontal"(): $KBlockSettings$Builder
public "customPlacement"(): $KBlockSettings$Builder
public "glassType"(arg0: $GlassType$Type): $KBlockSettings$Builder
public "canSurviveHandler"(arg0: $CanSurviveHandler$Type): $KBlockSettings$Builder
public "removeComponent"(arg0: $KBlockComponent$Type$Type<(any)>): $KBlockSettings$Builder
public "waterLoggable"(): $KBlockSettings$Builder
get "analogOutputSignal"(): $ToIntFunction<($BlockState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockSettings$Builder$Type = ($KBlockSettings$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockSettings$Builder_ = $KBlockSettings$Builder$Type;
}}
declare module "packages/snownee/kiwi/loader/$Platform" {
import {$Platform$Type, $Platform$Type$Type} from "packages/snownee/kiwi/loader/$Platform$Type"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $Platform {


public static "isDataGen"(): boolean
public static "getPlatform"(): $Platform$Type
public static "isPhysicalClient"(): boolean
public static "isProduction"(): boolean
public static "getServer"(): $MinecraftServer
public static "getVersionNumber"(arg0: string): integer
public static "getPlatformSeries"(): $Platform$Type
public static "getConfigDir"(): $Path
public static "isModLoaded"(arg0: string): boolean
get "dataGen"(): boolean
get "platform"(): $Platform$Type
get "physicalClient"(): boolean
get "production"(): boolean
get "server"(): $MinecraftServer
get "platformSeries"(): $Platform$Type
get "configDir"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Platform$Type = ($Platform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Platform_ = $Platform$Type;
}}
declare module "packages/snownee/kiwi/customization/$KiwiPackResourceManager" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BufferedReader, $BufferedReader$Type} from "packages/java/io/$BufferedReader"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$CloseableResourceManager, $CloseableResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$CloseableResourceManager"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Resource, $Resource$Type} from "packages/net/minecraft/server/packs/resources/$Resource"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KiwiPackResourceManager implements $CloseableResourceManager {

constructor(arg0: $List$Type<($PackResources$Type)>)

public "close"(): void
public "getResourceStack"(arg0: $ResourceLocation$Type): $List<($Resource)>
public "listResources"(arg0: string, arg1: $Predicate$Type<($ResourceLocation$Type)>): $Map<($ResourceLocation), ($Resource)>
public "getNamespaces"(): $Set<(string)>
public "getResource"(arg0: $ResourceLocation$Type): $Optional<($Resource)>
public "listPacks"(): $Stream<($PackResources)>
public "listResourceStacks"(arg0: string, arg1: $Predicate$Type<($ResourceLocation$Type)>): $Map<($ResourceLocation), ($List<($Resource)>)>
public "getResourceOrThrow"(arg0: $ResourceLocation$Type): $Resource
public static "fromMap"(arg0: $Map$Type<($ResourceLocation$Type), ($Resource$Type)>): $ResourceProvider
public "openAsReader"(arg0: $ResourceLocation$Type): $BufferedReader
public "open"(arg0: $ResourceLocation$Type): $InputStream
get "namespaces"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiPackResourceManager$Type = ($KiwiPackResourceManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiPackResourceManager_ = $KiwiPackResourceManager$Type;
}}
declare module "packages/snownee/kiwi/util/$PlayerUtil" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $PlayerUtil {


public static "tryPlace"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Direction$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockState$Type, arg6: $ItemStack$Type, arg7: boolean, arg8: boolean): $BlockPos
public static "tryPlace"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Direction$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockState$Type, arg6: $ItemStack$Type, arg7: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerUtil$Type = ($PlayerUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerUtil_ = $PlayerUtil$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type" {
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KItemTemplate$Type<T extends $KItemTemplate> extends $Record {

constructor(codec: $Supplier$Type<($Codec$Type<(T)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "codec"(): $Supplier<($Codec<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemTemplate$Type$Type<T> = ($KItemTemplate$Type<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemTemplate$Type_<T> = $KItemTemplate$Type$Type<(T)>;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$NoContainersShapedRecipe" {
import {$ShapedRecipe, $ShapedRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapedRecipe"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export class $NoContainersShapedRecipe extends $ShapedRecipe {
readonly "width": integer
readonly "height": integer
readonly "result": $ItemStack

constructor(arg0: $ShapedRecipe$Type)

public "getRemainingItems"(arg0: $CraftingContainer$Type): $NonNullList<($ItemStack)>
public "getSerializer"(): $RecipeSerializer<(any)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoContainersShapedRecipe$Type = ($NoContainersShapedRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoContainersShapedRecipe_ = $NoContainersShapedRecipe$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$FlowMappingEndToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $FlowMappingEndToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowMappingEndToken$Type = ($FlowMappingEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowMappingEndToken_ = $FlowMappingEndToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$DocumentStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $DocumentStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentStartToken$Type = ($DocumentStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentStartToken_ = $DocumentStartToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$FieldProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$GenericProperty, $GenericProperty$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty"

export class $FieldProperty extends $GenericProperty {

constructor(arg0: $Field$Type)

public "get"(arg0: any): any
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
get "annotations"(): $List<($Annotation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldProperty$Type = ($FieldProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldProperty_ = $FieldProperty$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$OperatorDictionaryIfc" {
import {$OperatorIfc, $OperatorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc"

export interface $OperatorDictionaryIfc {

 "addOperator"(arg0: string, arg1: $OperatorIfc$Type): void
 "getPrefixOperator"(arg0: string): $OperatorIfc
 "hasPostfixOperator"(arg0: string): boolean
 "getPostfixOperator"(arg0: string): $OperatorIfc
 "getInfixOperator"(arg0: string): $OperatorIfc
 "hasPrefixOperator"(arg0: string): boolean
 "hasInfixOperator"(arg0: string): boolean
}

export namespace $OperatorDictionaryIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OperatorDictionaryIfc$Type = ($OperatorDictionaryIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OperatorDictionaryIfc_ = $OperatorDictionaryIfc$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$FlowSequenceEndToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $FlowSequenceEndToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowSequenceEndToken$Type = ($FlowSequenceEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowSequenceEndToken_ = $FlowSequenceEndToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "is"(arg0: $Event$ID$Type): boolean
public "getStartMark"(): $Mark
public "getEndMark"(): $Mark
public "getEventId"(): $Event$ID
get "startMark"(): $Mark
get "endMark"(): $Mark
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type = ($Event);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_ = $Event$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/util/$ArrayUtils" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $ArrayUtils {


public static "toUnmodifiableList"<E>(arg0: (E)[]): $List<(E)>
public static "toUnmodifiableCompositeList"<E>(arg0: (E)[], arg1: (E)[]): $List<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayUtils$Type = ($ArrayUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayUtils_ = $ArrayUtils$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Tag {
static readonly "PREFIX": string
static readonly "YAML": $Tag
static readonly "MERGE": $Tag
static readonly "SET": $Tag
static readonly "PAIRS": $Tag
static readonly "OMAP": $Tag
static readonly "BINARY": $Tag
static readonly "INT": $Tag
static readonly "FLOAT": $Tag
static readonly "TIMESTAMP": $Tag
static readonly "BOOL": $Tag
static readonly "NULL": $Tag
static readonly "STR": $Tag
static readonly "SEQ": $Tag
static readonly "MAP": $Tag
static readonly "standardTags": $Set<($Tag)>
static readonly "COMMENT": $Tag

constructor(arg0: string)
constructor(arg0: $Class$Type<(any)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): string
public "startsWith"(arg0: string): boolean
public "matches"(arg0: $Class$Type<(any)>): boolean
public "getClassName"(): string
public "isSecondary"(): boolean
public "isCustomGlobal"(): boolean
public "isCompatible"(arg0: $Class$Type<(any)>): boolean
get "value"(): string
get "className"(): string
get "secondary"(): boolean
get "customGlobal"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tag$Type = ($Tag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tag_ = $Tag$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$ParserImpl" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Parser, $Parser$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$Parser"
import {$Scanner, $Scanner$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$Scanner"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"
import {$StreamReader, $StreamReader$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/reader/$StreamReader"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $ParserImpl implements $Parser {

constructor(arg0: $StreamReader$Type, arg1: $LoaderOptions$Type)
constructor(arg0: $Scanner$Type)

public "checkEvent"(arg0: $Event$ID$Type): boolean
public "peekEvent"(): $Event
public "getEvent"(): $Event
get "event"(): $Event
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserImpl$Type = ($ParserImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserImpl_ = $ParserImpl$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/string/$StringStartsWithFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $StringStartsWithFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringStartsWithFunction$Type = ($StringStartsWithFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringStartsWithFunction_ = $StringStartsWithFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$Preparation" {
import {$PlaceChoices, $PlaceChoices$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$KBlockDefinition, $KBlockDefinition$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockDefinition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlaceChoices$Preparation extends $Record {

constructor(choices: $Map$Type<($ResourceLocation$Type), ($PlaceChoices$Type)>, byTemplate: $Map$Type<($KBlockTemplate$Type), ($KHolder$Type<($PlaceChoices$Type)>)>, byBlock: $Map$Type<($ResourceLocation$Type), ($KHolder$Type<($PlaceChoices$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Supplier$Type<($Map$Type<($ResourceLocation$Type), ($PlaceChoices$Type)>)>, arg1: $Map$Type<($ResourceLocation$Type), ($KBlockTemplate$Type)>): $PlaceChoices$Preparation
public "choices"(): $Map<($ResourceLocation), ($PlaceChoices)>
public "attachChoicesB"(): integer
public "attachChoicesA"(arg0: $Block$Type, arg1: $KBlockDefinition$Type): boolean
public "byBlock"(): $Map<($ResourceLocation), ($KHolder<($PlaceChoices)>)>
public "byTemplate"(): $Map<($KBlockTemplate), ($KHolder<($PlaceChoices)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Preparation$Type = ($PlaceChoices$Preparation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$Preparation_ = $PlaceChoices$Preparation$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$DynamicShapedRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$CustomRecipe, $CustomRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CustomRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CraftingBookCategory, $CraftingBookCategory$Type} from "packages/net/minecraft/world/item/crafting/$CraftingBookCategory"

export class $DynamicShapedRecipe extends $CustomRecipe {
 "pattern": string
 "differentInputs": boolean
 "recipeOutput": $ItemStack

constructor(arg0: $ResourceLocation$Type, arg1: $CraftingBookCategory$Type)

public "matches"(arg0: $CraftingContainer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): boolean
public "matches"(arg0: $CraftingContainer$Type, arg1: $Level$Type): boolean
public "search"(arg0: $CraftingContainer$Type): (integer)[]
public "item"(arg0: character, arg1: $CraftingContainer$Type, arg2: (integer)[]): $ItemStack
public "items"(arg0: character, arg1: $CraftingContainer$Type, arg2: (integer)[]): $List<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getRecipeHeight"(): integer
public "getRecipeWidth"(): integer
get "ingredients"(): $NonNullList<($Ingredient)>
get "serializer"(): $RecipeSerializer<(any)>
get "recipeHeight"(): integer
get "recipeWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicShapedRecipe$Type = ($DynamicShapedRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicShapedRecipe_ = $DynamicShapedRecipe$Type;
}}
declare module "packages/snownee/kiwi/util/$BlockPredicateHelper" {
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $BlockPredicateHelper {

constructor()

public static "fastMatch"(arg0: $BlockPredicate$Type, arg1: $BlockState$Type): boolean
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
declare module "packages/snownee/kiwi/contributor/impl/client/model/$FoxTailModel" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$AgeableListModel, $AgeableListModel$Type} from "packages/net/minecraft/client/model/$AgeableListModel"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $FoxTailModel<T extends $LivingEntity> extends $AgeableListModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $PlayerModel$Type<($AbstractClientPlayer$Type)>, arg1: $LayerDefinition$Type)

public static "create"(): $LayerDefinition
public "setupAnim"(arg0: T, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoxTailModel$Type<T> = ($FoxTailModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoxTailModel_<T> = $FoxTailModel$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Token$ID extends $Enum<($Token$ID)> {
static readonly "Alias": $Token$ID
static readonly "Anchor": $Token$ID
static readonly "BlockEnd": $Token$ID
static readonly "BlockEntry": $Token$ID
static readonly "BlockMappingStart": $Token$ID
static readonly "BlockSequenceStart": $Token$ID
static readonly "Directive": $Token$ID
static readonly "DocumentEnd": $Token$ID
static readonly "DocumentStart": $Token$ID
static readonly "FlowEntry": $Token$ID
static readonly "FlowMappingEnd": $Token$ID
static readonly "FlowMappingStart": $Token$ID
static readonly "FlowSequenceEnd": $Token$ID
static readonly "FlowSequenceStart": $Token$ID
static readonly "Key": $Token$ID
static readonly "Scalar": $Token$ID
static readonly "StreamEnd": $Token$ID
static readonly "StreamStart": $Token$ID
static readonly "Tag": $Token$ID
static readonly "Value": $Token$ID
static readonly "Whitespace": $Token$ID
static readonly "Comment": $Token$ID
static readonly "Error": $Token$ID


public "toString"(): string
public static "values"(): ($Token$ID)[]
public static "valueOf"(arg0: string): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$ID$Type = (("blockentry") | ("flowsequencestart") | ("blocksequencestart") | ("streamend") | ("error") | ("blockend") | ("flowmappingend") | ("flowentry") | ("flowsequenceend") | ("directive") | ("blockmappingstart") | ("scalar") | ("documentend") | ("anchor") | ("flowmappingstart") | ("alias") | ("streamstart") | ("comment") | ("tag") | ("documentstart") | ("whitespace") | ("value") | ("key")) | ($Token$ID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token$ID_ = $Token$ID$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ShapeRef" {
import {$BakingContext, $BakingContext$Type} from "packages/snownee/kiwi/customization/shape/$BakingContext"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ShapeRef implements $UnbakedShape {

constructor(arg0: $ResourceLocation$Type)

public "id"(): $ResourceLocation
public "isResolved"(): boolean
public "dependencies"(): $Stream<($UnbakedShape)>
public "bake"(arg0: $BakingContext$Type): $ShapeGenerator
public "bindValue"(arg0: $BakingContext$Type): boolean
get "resolved"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapeRef$Type = ($ShapeRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapeRef_ = $ShapeRef$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$KBlockSettings$MoreInfo" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

/**
 * 
 * @deprecated
 */
export class $KBlockSettings$MoreInfo extends $Record {

constructor(shape: $ResourceLocation$Type, collisionShape: $ResourceLocation$Type, interactionShape: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "shape"(): $ResourceLocation
public "interactionShape"(): $ResourceLocation
public "collisionShape"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockSettings$MoreInfo$Type = ($KBlockSettings$MoreInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockSettings$MoreInfo_ = $KBlockSettings$MoreInfo$Type;
}}
declare module "packages/snownee/kiwi/util/$BitBufferHelper" {
import {$ByteBuf, $ByteBuf$Type} from "packages/io/netty/buffer/$ByteBuf"

export class $BitBufferHelper {
readonly "write": boolean

constructor(arg0: $ByteBuf$Type, arg1: boolean)

public "end"(): void
public "source"(arg0: $ByteBuf$Type): void
public "readBoolean"(): boolean
public "writeBoolean"(arg0: boolean): void
public "writeBits"(arg0: integer, arg1: integer): void
public "readBits"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BitBufferHelper$Type = ($BitBufferHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BitBufferHelper_ = $BitBufferHelper$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$UnTrustedTagInspector" {
import {$TagInspector, $TagInspector$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TagInspector"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $UnTrustedTagInspector implements $TagInspector {

constructor()

public "isGlobalTagAllowed"(arg0: $Tag$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnTrustedTagInspector$Type = ($UnTrustedTagInspector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnTrustedTagInspector_ = $UnTrustedTagInspector$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$ConvertScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientTooltipPositioner, $ClientTooltipPositioner$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipPositioner"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$CConvertItemPacket$Group, $CConvertItemPacket$Group$Type} from "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Group"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ConvertScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $Slot$Type, arg2: integer, arg3: $List$Type<($CConvertItemPacket$Group$Type)>)

public "isClosing"(): boolean
public "onClose"(): void
public "setTooltipForNextRenderPass"(arg0: $List$Type<($FormattedCharSequence$Type)>, arg1: $ClientTooltipPositioner$Type, arg2: boolean): void
public static "renderLingering"(arg0: $GuiGraphics$Type): void
public static "tickLingering"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isPauseScreen"(): boolean
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
get "closing"(): boolean
get "pauseScreen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConvertScreen$Type = ($ConvertScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConvertScreen_ = $ConvertScreen$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$DirectionalComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $DirectionalComponent extends $Record implements $KBlockComponent {
static readonly "FACING": $DirectionProperty
static readonly "CODEC": $Codec<($DirectionalComponent)>

constructor(oppose: boolean)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "getInstance"(arg0: boolean): $DirectionalComponent
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "oppose"(): boolean
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionalComponent$Type = ($DirectionalComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionalComponent_ = $DirectionalComponent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$PostfixOperator" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $PostfixOperator extends $Annotation {

 "precedence"(): integer
 "leftAssociative"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $PostfixOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostfixOperator$Type = ($PostfixOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostfixOperator_ = $PostfixOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BlockSpread$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BlockSpread$Type extends $Enum<($BlockSpread$Type)> {
static readonly "PLANE_XZ": $BlockSpread$Type
static readonly "PLANE_XYZ": $BlockSpread$Type


public static "values"(): ($BlockSpread$Type)[]
public static "valueOf"(arg0: string): $BlockSpread$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockSpread$Type$Type = (("plane_xyz") | ("plane_xz")) | ($BlockSpread$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockSpread$Type_ = $BlockSpread$Type$Type;
}}
declare module "packages/snownee/kiwi/item/$ModItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModItem extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public static "addTip"(arg0: $ItemStack$Type, arg1: $List$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModItem$Type = ($ModItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModItem_ = $ModItem$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$ReloadBlockSettingsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $ReloadBlockSettingsCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadBlockSettingsCommand$Type = ($ReloadBlockSettingsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadBlockSettingsCommand_ = $ReloadBlockSettingsCommand$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$ZoneIdConverter" {
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $ZoneIdConverter {


public static "convert"(arg0: $Token$Type, arg1: string): $ZoneId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneIdConverter$Type = ($ZoneIdConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneIdConverter_ = $ZoneIdConverter$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$FrontAndTopShape" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $FrontAndTopShape extends $Record implements $ShapeGenerator {

constructor(floor: $ShapeGenerator$Type, ceiling: $ShapeGenerator$Type, wall: $ShapeGenerator$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "floor"(): $ShapeGenerator
public static "create"(arg0: $ShapeGenerator$Type, arg1: $ShapeGenerator$Type, arg2: $ShapeGenerator$Type): $ShapeGenerator
public "ceiling"(): $ShapeGenerator
public "wall"(): $ShapeGenerator
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrontAndTopShape$Type = ($FrontAndTopShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrontAndTopShape_ = $FrontAndTopShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$SequenceStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$CollectionStartEvent, $CollectionStartEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"

export class $SequenceStartEvent extends $CollectionStartEvent {

constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$FlowStyle$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceStartEvent$Type = ($SequenceStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceStartEvent_ = $SequenceStartEvent$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$BlockFaceType" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export interface $PlaceChoices$BlockFaceType extends $BiPredicate<($UseOnContext), ($Direction)> {

 "test"(arg0: $UseOnContext$Type, arg1: $Direction$Type): boolean
 "or"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<($UseOnContext), ($Direction)>
 "negate"(): $BiPredicate<($UseOnContext), ($Direction)>
 "and"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<($UseOnContext), ($Direction)>

(arg0: $UseOnContext$Type, arg1: $Direction$Type): boolean
}

export namespace $PlaceChoices$BlockFaceType {
const ANY: $PlaceChoices$BlockFaceType
const HORIZONTAL: $PlaceChoices$BlockFaceType
const VERTICAL: $PlaceChoices$BlockFaceType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$BlockFaceType$Type = ($PlaceChoices$BlockFaceType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$BlockFaceType_ = $PlaceChoices$BlockFaceType$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BeanAccess extends $Enum<($BeanAccess)> {
static readonly "DEFAULT": $BeanAccess
static readonly "FIELD": $BeanAccess
static readonly "PROPERTY": $BeanAccess


public static "values"(): ($BeanAccess)[]
public static "valueOf"(arg0: string): $BeanAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeanAccess$Type = (("default") | ("field") | ("property")) | ($BeanAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeanAccess_ = $BeanAccess$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$ImplicitTuple" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImplicitTuple {

constructor(arg0: boolean, arg1: boolean)

public "toString"(): string
public "bothFalse"(): boolean
public "canOmitTagInPlainScalar"(): boolean
public "canOmitTagInNonPlainScalar"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImplicitTuple$Type = ($ImplicitTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImplicitTuple_ = $ImplicitTuple$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$KBlockComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export interface $KBlockComponent {

 "type"(): $KBlockComponent$Type<(any)>
 "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
 "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
 "hasAnalogOutputSignal"(): boolean
 "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
 "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
 "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
 "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
 "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
 "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
 "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
 "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
 "registerDefaultState"(arg0: $BlockState$Type): $BlockState
}

export namespace $KBlockComponent {
const DIRECT_CODEC: $Codec<($KBlockComponent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockComponent$Type = ($KBlockComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockComponent_ = $KBlockComponent$Type;
}}
declare module "packages/snownee/kiwi/client/$TooltipEvents" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $TooltipEvents {
static readonly "disableDebugTooltipCommand": string


public static "globalTooltip"(arg0: $ItemStack$Type, arg1: $List$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void
public static "debugTooltip"(arg0: $ItemStack$Type, arg1: $List$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipEvents$Type = ($TooltipEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipEvents_ = $TooltipEvents$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$ScalarEvent" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$ImplicitTuple, $ImplicitTuple$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$ImplicitTuple"
import {$NodeEvent, $NodeEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$NodeEvent"

export class $ScalarEvent extends $NodeEvent {

constructor(arg0: string, arg1: string, arg2: $ImplicitTuple$Type, arg3: string, arg4: $Mark$Type, arg5: $Mark$Type, arg6: $DumperOptions$ScalarStyle$Type)

public "getValue"(): string
public "getTag"(): string
public "getImplicit"(): $ImplicitTuple
public "getEventId"(): $Event$ID
public "getScalarStyle"(): $DumperOptions$ScalarStyle
public "isPlain"(): boolean
get "value"(): string
get "tag"(): string
get "implicit"(): $ImplicitTuple
get "eventId"(): $Event$ID
get "scalarStyle"(): $DumperOptions$ScalarStyle
get "plain"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarEvent$Type = ($ScalarEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarEvent_ = $ScalarEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$FlowSequenceStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $FlowSequenceStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowSequenceStartToken$Type = ($FlowSequenceStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowSequenceStartToken_ = $FlowSequenceStartToken$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$UseHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $UseHandler {

 "use"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $Level$Type, arg3: $InteractionHand$Type, arg4: $BlockHitResult$Type): $InteractionResult

(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $Level$Type, arg3: $InteractionHand$Type, arg4: $BlockHitResult$Type): $InteractionResult
}

export namespace $UseHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseHandler$Type = ($UseHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseHandler_ = $UseHandler$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$Yaml" {
import {$Resolver, $Resolver$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/resolver/$Resolver"
import {$BaseConstructor, $BaseConstructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BeanAccess, $BeanAccess$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$DumperOptions, $DumperOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$TypeDescription, $TypeDescription$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Representer, $Representer$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$Representer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $Yaml {

constructor(arg0: $BaseConstructor$Type, arg1: $Representer$Type, arg2: $DumperOptions$Type, arg3: $LoaderOptions$Type)
constructor(arg0: $BaseConstructor$Type, arg1: $Representer$Type, arg2: $DumperOptions$Type)
constructor(arg0: $Representer$Type, arg1: $DumperOptions$Type)
constructor(arg0: $BaseConstructor$Type, arg1: $Representer$Type, arg2: $DumperOptions$Type, arg3: $Resolver$Type)
constructor(arg0: $BaseConstructor$Type, arg1: $Representer$Type, arg2: $DumperOptions$Type, arg3: $LoaderOptions$Type, arg4: $Resolver$Type)
constructor()
constructor(arg0: $LoaderOptions$Type)
constructor(arg0: $Representer$Type)
constructor(arg0: $BaseConstructor$Type)
constructor(arg0: $BaseConstructor$Type, arg1: $Representer$Type)
constructor(arg0: $DumperOptions$Type)

public "getName"(): string
public "toString"(): string
public "load"<T>(arg0: string): T
public "load"<T>(arg0: $Reader$Type): T
public "load"<T>(arg0: $InputStream$Type): T
public "setName"(arg0: string): void
public "parse"(arg0: $Reader$Type): $Iterable<($Event)>
public "compose"(arg0: $Reader$Type): $Node
public "loadAll"(arg0: $Reader$Type): $Iterable<(any)>
public "loadAll"(arg0: string): $Iterable<(any)>
public "loadAll"(arg0: $InputStream$Type): $Iterable<(any)>
public "loadAs"<T>(arg0: string, arg1: $Class$Type<(any)>): T
public "loadAs"<T>(arg0: $InputStream$Type, arg1: $Class$Type<(any)>): T
public "loadAs"<T>(arg0: $Reader$Type, arg1: $Class$Type<(any)>): T
public "dumpAll"(arg0: $Iterator$Type<(any)>): string
public "dumpAll"(arg0: $Iterator$Type<(any)>, arg1: $Writer$Type): void
public "dumpAs"(arg0: any, arg1: $Tag$Type, arg2: $DumperOptions$FlowStyle$Type): string
public "represent"(arg0: any): $Node
public "dumpAsMap"(arg0: any): string
public "addTypeDescription"(arg0: $TypeDescription$Type): void
public "composeAll"(arg0: $Reader$Type): $Iterable<($Node)>
public "setBeanAccess"(arg0: $BeanAccess$Type): void
public "serialize"(arg0: $Node$Type): $List<($Event)>
public "serialize"(arg0: $Node$Type, arg1: $Writer$Type): void
public "addImplicitResolver"(arg0: $Tag$Type, arg1: $Pattern$Type, arg2: string): void
public "addImplicitResolver"(arg0: $Tag$Type, arg1: $Pattern$Type, arg2: string, arg3: integer): void
public "dump"(arg0: any): string
public "dump"(arg0: any, arg1: $Writer$Type): void
get "name"(): string
set "name"(value: string)
set "beanAccess"(value: $BeanAccess$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Yaml$Type = ($Yaml);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Yaml_ = $Yaml$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/layer/$PlanetLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $PlanetLayer extends $CosmeticLayer {
static readonly "ALL_LAYERS": $Collection<($CosmeticLayer)>
readonly "f_117344_": $RenderLayerParent<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlanetLayer$Type = ($PlanetLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlanetLayer_ = $PlanetLayer$Type;
}}
declare module "packages/snownee/kiwi/config/$ConfigHandler$Value" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"

export class $ConfigHandler$Value<T> {
readonly "defValue": T
 "field": $Field
 "value": T
 "requiresRestart": boolean
 "translation": string
 "min": double
 "max": double
readonly "path": string

constructor(arg0: string, arg1: $Field$Type, arg2: T, arg3: string)

public "get"(): T
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "accept"(arg0: any): void
public "getType"(): $Class<(any)>
get "type"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHandler$Value$Type<T> = ($ConfigHandler$Value<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHandler$Value_<T> = $ConfigHandler$Value$Type<(T)>;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/layer/$SantaHatLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $SantaHatLayer extends $CosmeticLayer {
static readonly "ALL_LAYERS": $Collection<($CosmeticLayer)>
readonly "f_117344_": $RenderLayerParent<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SantaHatLayer$Type = ($SantaHatLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SantaHatLayer_ = $SantaHatLayer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NodeId extends $Enum<($NodeId)> {
static readonly "scalar": $NodeId
static readonly "sequence": $NodeId
static readonly "mapping": $NodeId
static readonly "anchor": $NodeId


public static "values"(): ($NodeId)[]
public static "valueOf"(arg0: string): $NodeId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeId$Type = (("sequence") | ("scalar") | ("mapping") | ("anchor")) | ($NodeId);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeId_ = $NodeId$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DurationToMillisFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DurationToMillisFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DurationToMillisFunction$Type = ($DurationToMillisFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DurationToMillisFunction_ = $DurationToMillisFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$TagTuple" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TagTuple {

constructor(arg0: string, arg1: string)

public "getHandle"(): string
public "getSuffix"(): string
get "handle"(): string
get "suffix"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagTuple$Type = ($TagTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagTuple_ = $TagTuple$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$CanSurviveHandler$Compound, $CanSurviveHandler$Compound$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler$Compound"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export interface $CanSurviveHandler {

 "isSensitiveSide"(arg0: $BlockState$Type, arg1: $Direction$Type): boolean
 "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
}

export namespace $CanSurviveHandler {
function checkFace(arg0: $DirectionProperty$Type): $CanSurviveHandler
function checkCeiling(): $CanSurviveHandler
function checkFloor(): $CanSurviveHandler
function all(arg0: $List$Type<($CanSurviveHandler$Type)>): $CanSurviveHandler$Compound
function any(arg0: $List$Type<($CanSurviveHandler$Type)>): $CanSurviveHandler$Compound
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CanSurviveHandler$Type = ($CanSurviveHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CanSurviveHandler_ = $CanSurviveHandler$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$SumFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SumFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SumFunction$Type = ($SumFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SumFunction_ = $SumFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$GlassType" {
import {$KiwiModule$RenderLayer$Layer, $KiwiModule$RenderLayer$Layer$Type} from "packages/snownee/kiwi/$KiwiModule$RenderLayer$Layer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $GlassType extends $Record {
static readonly "CLEAR": $GlassType
static readonly "CUSTOM_CLEAR": $GlassType
static readonly "TRANSLUCENT": $GlassType
static readonly "QUARTZ": $GlassType
static readonly "TOUGHENED": $GlassType
static readonly "HOLLOW_STEEL": $GlassType

constructor(name: string, skipRendering: boolean, shadeBrightness: float, renderType: $KiwiModule$RenderLayer$Layer$Type)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "renderType"(): $KiwiModule$RenderLayer$Layer
public "shadeBrightness"(): float
public "skipRendering"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlassType$Type = ($GlassType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlassType_ = $GlassType$Type;
}}
declare module "packages/snownee/kiwi/util/resource/$RequiredFolderRepositorySource" {
import {$FolderRepositorySource, $FolderRepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$FolderRepositorySource"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$PackSource, $PackSource$Type} from "packages/net/minecraft/server/packs/repository/$PackSource"
import {$Pack, $Pack$Type} from "packages/net/minecraft/server/packs/repository/$Pack"

export class $RequiredFolderRepositorySource extends $FolderRepositorySource {
readonly "folder": $Path
readonly "packType": $PackType
readonly "packSource": $PackSource

constructor(arg0: $Path$Type, arg1: $PackType$Type, arg2: $PackSource$Type)

public "loadPacks"(arg0: $Consumer$Type<($Pack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequiredFolderRepositorySource$Type = ($RequiredFolderRepositorySource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequiredFolderRepositorySource_ = $RequiredFolderRepositorySource$Type;
}}
declare module "packages/snownee/kiwi/util/$ClientProxy" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$ClientProxy$Context, $ClientProxy$Context$Type} from "packages/snownee/kiwi/util/$ClientProxy$Context"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$SmartKey, $SmartKey$Type} from "packages/snownee/kiwi/util/$SmartKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $ClientProxy {

constructor()

public static "getSlotUnderMouse"(arg0: $AbstractContainerScreen$Type<(any)>): $Slot
public static "pushScreen"(arg0: $Minecraft$Type, arg1: $Screen$Type): void
public static "registerColors"(arg0: $ClientProxy$Context$Type, arg1: $List$Type<($Pair$Type<($Block$Type), ($BlockColor$Type)>)>, arg2: $List$Type<($Pair$Type<($Item$Type), ($ItemColor$Type)>)>): void
public static "afterRegisterSmartKey"(arg0: $SmartKey$Type): void
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
declare module "packages/snownee/kiwi/recipe/crafting/$KiwiShapelessRecipe" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ShapelessRecipe, $ShapelessRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapelessRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export class $KiwiShapelessRecipe extends $ShapelessRecipe {
readonly "group": string
readonly "result": $ItemStack
readonly "ingredients": $NonNullList<($Ingredient)>

constructor(arg0: $ShapelessRecipe$Type, arg1: boolean)

public "getRemainingItems"(arg0: $CraftingContainer$Type): $NonNullList<($ItemStack)>
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "matches"(arg0: $CraftingContainer$Type, arg1: $Level$Type): boolean
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiShapelessRecipe$Type = ($KiwiShapelessRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiShapelessRecipe_ = $KiwiShapelessRecipe$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$BuiltInItemTemplate" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BuiltInItemTemplate extends $KItemTemplate {

constructor(arg0: $Optional$Type<($ItemDefinitionProperties$Type)>, arg1: $Optional$Type<($ResourceLocation$Type)>)

public "type"(): $KItemTemplate$Type<(any)>
public "toString"(): string
public "resolve"(arg0: $ResourceLocation$Type): void
public "key"(): $Optional<($ResourceLocation)>
public static "directCodec"(): $Codec<($BuiltInItemTemplate)>
public "createItem"(arg0: $ResourceLocation$Type, arg1: $Item$Properties$Type, arg2: $JsonObject$Type): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltInItemTemplate$Type = ($BuiltInItemTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltInItemTemplate_ = $BuiltInItemTemplate$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$MinFunction" {
import {$AbstractMinMaxFunction, $AbstractMinMaxFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$AbstractMinMaxFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $MinFunction extends $AbstractMinMaxFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinFunction$Type = ($MinFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinFunction_ = $MinFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$BlockShapeType" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $BlockShapeType extends $Enum<($BlockShapeType)> implements $StringRepresentable {
static readonly "MAIN": $BlockShapeType
static readonly "COLLISION": $BlockShapeType
static readonly "INTERACTION": $BlockShapeType
static readonly "VALUES": $List<($BlockShapeType)>


public static "values"(): ($BlockShapeType)[]
public static "valueOf"(arg0: string): $BlockShapeType
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockShapeType$Type = (("collision") | ("interaction") | ("main")) | ($BlockShapeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockShapeType_ = $BlockShapeType$Type;
}}
declare module "packages/snownee/kiwi/$KiwiModule$RenderLayer$Layer" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KiwiModule$RenderLayer$Layer extends $Enum<($KiwiModule$RenderLayer$Layer)> {
static readonly "CUTOUT_MIPPED": $KiwiModule$RenderLayer$Layer
static readonly "CUTOUT": $KiwiModule$RenderLayer$Layer
static readonly "TRANSLUCENT": $KiwiModule$RenderLayer$Layer
 "value": any


public static "values"(): ($KiwiModule$RenderLayer$Layer)[]
public static "valueOf"(arg0: string): $KiwiModule$RenderLayer$Layer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiModule$RenderLayer$Layer$Type = (("translucent") | ("cutout") | ("cutout_mipped")) | ($KiwiModule$RenderLayer$Layer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiModule$RenderLayer$Layer_ = $KiwiModule$RenderLayer$Layer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$Emitable" {
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Emitable {

 "emit"(arg0: $Event$Type): void

(arg0: $Event$Type): void
}

export namespace $Emitable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Emitable$Type = ($Emitable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Emitable_ = $Emitable$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$DocumentEndToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $DocumentEndToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEndToken$Type = ($DocumentEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEndToken_ = $DocumentEndToken$Type;
}}
declare module "packages/snownee/kiwi/schedule/$ITicker" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ITicker {

 "destroy"(): void
}

export namespace $ITicker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITicker$Type = ($ITicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITicker_ = $ITicker$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameters" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$FunctionParameter, $FunctionParameter$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameter"

export interface $FunctionParameters extends $Annotation {

 "value"(): ($FunctionParameter)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $FunctionParameters {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionParameters$Type = ($FunctionParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionParameters_ = $FunctionParameters$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$MaxFunction" {
import {$AbstractMinMaxFunction, $AbstractMinMaxFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$AbstractMinMaxFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $MaxFunction extends $AbstractMinMaxFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaxFunction$Type = ($MaxFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaxFunction_ = $MaxFunction$Type;
}}
declare module "packages/snownee/kiwi/block/$IKiwiBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$MapColor, $MapColor$Type} from "packages/net/minecraft/world/level/material/$MapColor"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$PushReaction, $PushReaction$Type} from "packages/net/minecraft/world/level/material/$PushReaction"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$TreeConfiguration, $TreeConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$TreeConfiguration"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ToolAction, $ToolAction$Type} from "packages/net/minecraftforge/common/$ToolAction"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$IForgeBlock, $IForgeBlock$Type} from "packages/net/minecraftforge/common/extensions/$IForgeBlock"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$SignalGetter, $SignalGetter$Type} from "packages/net/minecraft/world/level/$SignalGetter"
import {$BlockPathTypes, $BlockPathTypes$Type} from "packages/net/minecraft/world/level/pathfinder/$BlockPathTypes"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IPlantable, $IPlantable$Type} from "packages/net/minecraftforge/common/$IPlantable"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export interface $IKiwiBlock extends $IForgeBlock {

 "getName"(arg0: $ItemStack$Type): $MutableComponent
 "getCloneItemStack"(arg0: $BlockState$Type, arg1: $HitResult$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: $Player$Type): $ItemStack
 "createItem"(arg0: $Item$Properties$Type): $BlockItem
 "rotate"(arg0: $BlockState$Type, arg1: $LevelAccessor$Type, arg2: $BlockPos$Type, arg3: $Rotation$Type): $BlockState
 "isSlimeBlock"(arg0: $BlockState$Type): boolean
 "addLandingEffects"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: $LivingEntity$Type, arg5: integer): boolean
 "isPortalFrame"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): boolean
 "getExpDrop"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $RandomSource$Type, arg3: $BlockPos$Type, arg4: integer, arg5: integer): integer
 "getBlockPathType"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Mob$Type): $BlockPathTypes
 "hidesNeighborFace"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type, arg4: $Direction$Type): boolean
 "canSustainPlant"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type, arg4: $IPlantable$Type): boolean
 "isBurning"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): boolean
 "isLadder"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $LivingEntity$Type): boolean
 "canHarvestBlock"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Player$Type): boolean
 "isFertile"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): boolean
 "isConduitFrame"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $BlockPos$Type): boolean
 "onTreeGrow"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BiConsumer$Type<($BlockPos$Type), ($BlockState$Type)>, arg3: $RandomSource$Type, arg4: $BlockPos$Type, arg5: $TreeConfiguration$Type): boolean
 "isValidSpawn"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $SpawnPlacements$Type$Type, arg4: $EntityType$Type<(any)>): boolean
 "onNeighborChange"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $BlockPos$Type): void
 "getWeakChanges"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
 "getRespawnPosition"(arg0: $BlockState$Type, arg1: $EntityType$Type<(any)>, arg2: $LevelReader$Type, arg3: $BlockPos$Type, arg4: float, arg5: $LivingEntity$Type): $Optional<($Vec3)>
 "getExplosionResistance"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Explosion$Type): float
 "getEnchantPowerBonus"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): float
 "canDropFromExplosion"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Explosion$Type): boolean
 "shouldDisplayFluidOverlay"(arg0: $BlockState$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type, arg3: $FluidState$Type): boolean
 "getToolModifiedState"(arg0: $BlockState$Type, arg1: $UseOnContext$Type, arg2: $ToolAction$Type, arg3: boolean): $BlockState
 "getBeaconColorMultiplier"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $BlockPos$Type): (float)[]
 "getPistonPushReaction"(arg0: $BlockState$Type): $PushReaction
 "getAdjacentBlockPathType"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Mob$Type, arg4: $BlockPathTypes$Type): $BlockPathTypes
 "makesOpenTrapdoorAboveClimbable"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type): boolean
 "getStateAtViewpoint"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Vec3$Type): $BlockState
 "onDestroyedByPlayer"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: boolean, arg5: $FluidState$Type): boolean
 "supportsExternalFaceHiding"(arg0: $BlockState$Type): boolean
 "getSoundType"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): $SoundType
 "getFriction"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): float
 "isScaffolding"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $LivingEntity$Type): boolean
 "isBed"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): boolean
 "setBedOccupied"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $LivingEntity$Type, arg4: boolean): void
 "getBedDirection"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): $Direction
 "addRunningEffects"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): boolean
 "getLightEmission"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): integer
 "onBlockStateChange"(arg0: $LevelReader$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type): void
 "shouldCheckWeakPower"(arg0: $BlockState$Type, arg1: $SignalGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
 "isFlammable"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
 "isFireSource"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
 "canEntityDestroy"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): boolean
 "onBlockExploded"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Explosion$Type): void
 "canStickTo"(arg0: $BlockState$Type, arg1: $BlockState$Type): boolean
 "getFireSpreadSpeed"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
 "getFlammability"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
 "onCaughtFire"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Direction$Type, arg4: $LivingEntity$Type): void
 "isStickyBlock"(arg0: $BlockState$Type): boolean
 "canConnectRedstone"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
 "canBeHydrated"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $FluidState$Type, arg4: $BlockPos$Type): boolean
 "getMapColor"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $MapColor$Type): $MapColor
 "getAppearance"(arg0: $BlockState$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type, arg4: $BlockState$Type, arg5: $BlockPos$Type): $BlockState
 "collisionExtendsVertically"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): boolean

(arg0: $ItemStack$Type): $MutableComponent
}

export namespace $IKiwiBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IKiwiBlock$Type = ($IKiwiBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IKiwiBlock_ = $IKiwiBlock$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$BlockDefinitionProperties, $BlockDefinitionProperties$Type} from "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$KBlockTemplate$Type, $KBlockTemplate$Type$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KBlockTemplate {


public "type"(): $KBlockTemplate$Type<(any)>
public "resolve"(arg0: $ResourceLocation$Type): void
public "properties"(): $Optional<($BlockDefinitionProperties)>
public static "codec"(arg0: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $Codec<($KBlockTemplate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockTemplate$Type = ($KBlockTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockTemplate_ = $KBlockTemplate$Type;
}}
declare module "packages/snownee/kiwi/mixin/$HoeItemAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $HoeItemAccess {

}

export namespace $HoeItemAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HoeItemAccess$Type = ($HoeItemAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HoeItemAccess_ = $HoeItemAccess$Type;
}}
declare module "packages/snownee/kiwi/network/$KiwiPacket$Direction" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KiwiPacket$Direction extends $Enum<($KiwiPacket$Direction)> {
static readonly "PLAY_TO_SERVER": $KiwiPacket$Direction
static readonly "PLAY_TO_CLIENT": $KiwiPacket$Direction
static readonly "LOGIN_TO_SERVER": $KiwiPacket$Direction
static readonly "LOGIN_TO_CLIENT": $KiwiPacket$Direction


public static "values"(): ($KiwiPacket$Direction)[]
public static "valueOf"(arg0: string): $KiwiPacket$Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiPacket$Direction$Type = (("play_to_server") | ("login_to_client") | ("play_to_client") | ("login_to_server")) | ($KiwiPacket$Direction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiPacket$Direction_ = $KiwiPacket$Direction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DurationParseFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DurationParseFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DurationParseFunction$Type = ($DurationParseFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DurationParseFunction_ = $DurationParseFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$PrefixNotOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $PrefixNotOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixNotOperator$Type = ($PrefixNotOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixNotOperator_ = $PrefixNotOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixAndOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixAndOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixAndOperator$Type = ($InfixAndOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixAndOperator_ = $InfixAndOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$BuiltInBlockTemplate" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$BlockDefinitionProperties, $BlockDefinitionProperties$Type} from "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate$Type, $KBlockTemplate$Type$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BuiltInBlockTemplate extends $KBlockTemplate {

constructor(arg0: $Optional$Type<($BlockDefinitionProperties$Type)>, arg1: $Optional$Type<($ResourceLocation$Type)>)

public "type"(): $KBlockTemplate$Type<(any)>
public "toString"(): string
public "resolve"(arg0: $ResourceLocation$Type): void
public "key"(): $Optional<($ResourceLocation)>
public static "directCodec"(arg0: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $Codec<($BuiltInBlockTemplate)>
public "createBlock"(arg0: $ResourceLocation$Type, arg1: $BlockBehaviour$Properties$Type, arg2: $JsonObject$Type): $Block
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltInBlockTemplate$Type = ($BuiltInBlockTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltInBlockTemplate_ = $BuiltInBlockTemplate$Type;
}}
declare module "packages/snownee/kiwi/recipe/$AlternativesIngredient" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$AbstractIngredient, $AbstractIngredient$Type} from "packages/net/minecraftforge/common/crafting/$AbstractIngredient"
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$IIngredientSerializer, $IIngredientSerializer$Type} from "packages/net/minecraftforge/common/crafting/$IIngredientSerializer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$AlternativesIngredient$Serializer, $AlternativesIngredient$Serializer$Type} from "packages/snownee/kiwi/recipe/$AlternativesIngredient$Serializer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"

export class $AlternativesIngredient extends $AbstractIngredient {
static readonly "ID": $ResourceLocation
static readonly "SERIALIZER": $AlternativesIngredient$Serializer
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList

constructor(arg0: $JsonArray$Type)

public "test"(arg0: $ItemStack$Type): boolean
public "internal"(): $Ingredient
public "isSimple"(): boolean
public "toJson"(): $JsonElement
public "isEmpty"(): boolean
public "getSerializer"(): $IIngredientSerializer<(any)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
get "simple"(): boolean
get "empty"(): boolean
get "serializer"(): $IIngredientSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlternativesIngredient$Type = ($AlternativesIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlternativesIngredient_ = $AlternativesIngredient$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$Parser" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Parser {

 "checkEvent"(arg0: $Event$ID$Type): boolean
 "peekEvent"(): $Event
 "getEvent"(): $Event
}

export namespace $Parser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$Type = ($Parser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser_ = $Parser$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Constructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$TypeDescription, $TypeDescription$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$SafeConstructor, $SafeConstructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $Constructor extends $SafeConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(arg0: string, arg1: $LoaderOptions$Type)
constructor(arg0: $TypeDescription$Type, arg1: $Collection$Type<($TypeDescription$Type)>, arg2: $LoaderOptions$Type)
constructor(arg0: $TypeDescription$Type, arg1: $LoaderOptions$Type)
constructor(arg0: $LoaderOptions$Type)
constructor(arg0: $Class$Type<(any)>, arg1: $LoaderOptions$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constructor$Type = ($Constructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constructor_ = $Constructor$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$KMaterial" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$MapColor, $MapColor$Type} from "packages/net/minecraft/world/level/material/$MapColor"
import {$NoteBlockInstrument, $NoteBlockInstrument$Type} from "packages/net/minecraft/world/level/block/state/properties/$NoteBlockInstrument"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KMaterial extends $Record {
static readonly "DIRECT_CODEC": $Codec<($KMaterial)>

constructor(destroyTime: float, explosionResistance: float, soundType: $SoundType$Type, defaultMapColor: $MapColor$Type, instrument: $NoteBlockInstrument$Type, requiresCorrectToolForDrops: boolean, ignitedByLava: boolean, igniteOdds: integer, burnOdds: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "instrument"(): $NoteBlockInstrument
public "ignitedByLava"(): boolean
public "destroyTime"(): float
public "explosionResistance"(): float
public "requiresCorrectToolForDrops"(): boolean
public "soundType"(): $SoundType
public "defaultMapColor"(): $MapColor
public "igniteOdds"(): integer
public "burnOdds"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KMaterial$Type = ($KMaterial);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KMaterial_ = $KMaterial$Type;
}}
declare module "packages/snownee/kiwi/recipe/$AlternativesIngredientBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AlternativesIngredient, $AlternativesIngredient$Type} from "packages/snownee/kiwi/recipe/$AlternativesIngredient"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $AlternativesIngredientBuilder {

constructor()

public "add"(arg0: $ItemLike$Type): $AlternativesIngredientBuilder
public "add"(arg0: $TagKey$Type<($Item$Type)>): $AlternativesIngredientBuilder
public "add"(arg0: string): $AlternativesIngredientBuilder
public "add"(arg0: $Ingredient$Type): $AlternativesIngredientBuilder
public static "of"(): $AlternativesIngredientBuilder
public "build"(): $AlternativesIngredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlternativesIngredientBuilder$Type = ($AlternativesIngredientBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlternativesIngredientBuilder_ = $AlternativesIngredientBuilder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TagInspector" {
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export interface $TagInspector {

 "isGlobalTagAllowed"(arg0: $Tag$Type): boolean

(arg0: $Tag$Type): boolean
}

export namespace $TagInspector {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagInspector$Type = ($TagInspector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagInspector_ = $TagInspector$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/env/$EnvScalarConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$TypeDescription, $TypeDescription$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Constructor, $Constructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $EnvScalarConstructor extends $Constructor {
static readonly "ENV_TAG": $Tag
static readonly "ENV_FORMAT": $Pattern
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor()
constructor(arg0: $TypeDescription$Type, arg1: $Collection$Type<($TypeDescription$Type)>, arg2: $LoaderOptions$Type)

public "apply"(arg0: string, arg1: string, arg2: string, arg3: string): string
public "getEnv"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnvScalarConstructor$Type = ($EnvScalarConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnvScalarConstructor_ = $EnvScalarConstructor$Type;
}}
declare module "packages/snownee/kiwi/inventory/$InvHandlerWrapper" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $InvHandlerWrapper implements $Container {

constructor(arg0: $IItemHandlerModifiable$Type)

public "setChanged"(): void
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
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
export type $InvHandlerWrapper$Type = ($InvHandlerWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvHandlerWrapper_ = $InvHandlerWrapper$Type;
}}
declare module "packages/snownee/kiwi/customization/compat/jei/$JEICompat" {
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

export class $JEICompat implements $IModPlugin {
static readonly "ID": $ResourceLocation

constructor()

public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
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
export type $JEICompat$Type = ($JEICompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEICompat_ = $JEICompat$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/reader/$UnicodeReader" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $UnicodeReader extends $Reader {

constructor(arg0: $InputStream$Type)

public "read"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "close"(): void
public "getEncoding"(): string
get "encoding"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeReader$Type = ($UnicodeReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeReader_ = $UnicodeReader$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$NodeEvent, $NodeEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$NodeEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"

export class $CollectionStartEvent extends $NodeEvent {

constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$FlowStyle$Type)

public "getTag"(): string
public "getFlowStyle"(): $DumperOptions$FlowStyle
public "getImplicit"(): boolean
public "isFlow"(): boolean
get "tag"(): string
get "flowStyle"(): $DumperOptions$FlowStyle
get "implicit"(): boolean
get "flow"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionStartEvent$Type = ($CollectionStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionStartEvent_ = $CollectionStartEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MissingEnvironmentVariableException" {
import {$YAMLException, $YAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $MissingEnvironmentVariableException extends $YAMLException {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MissingEnvironmentVariableException$Type = ($MissingEnvironmentVariableException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MissingEnvironmentVariableException_ = $MissingEnvironmentVariableException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $CollectionNode<T> extends $Node {

constructor(arg0: $Tag$Type, arg1: $Mark$Type, arg2: $Mark$Type, arg3: $DumperOptions$FlowStyle$Type)

public "getValue"(): $List<(T)>
public "getFlowStyle"(): $DumperOptions$FlowStyle
public "setFlowStyle"(arg0: $DumperOptions$FlowStyle$Type): void
public "setEndMark"(arg0: $Mark$Type): void
get "value"(): $List<(T)>
get "flowStyle"(): $DumperOptions$FlowStyle
set "flowStyle"(value: $DumperOptions$FlowStyle$Type)
set "endMark"(value: $Mark$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionNode$Type<T> = ($CollectionNode<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionNode_<T> = $CollectionNode$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node" {
import {$CommentLine, $CommentLine$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentLine"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $Node {

constructor(arg0: $Tag$Type, arg1: $Mark$Type, arg2: $Mark$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getType"(): $Class<(any)>
public "getTag"(): $Tag
public "setType"(arg0: $Class$Type<(any)>): void
public "getAnchor"(): string
public "getStartMark"(): $Mark
public "getNodeId"(): $NodeId
public "getInLineComments"(): $List<($CommentLine)>
public "getEndMark"(): $Mark
public "getBlockComments"(): $List<($CommentLine)>
public "getEndComments"(): $List<($CommentLine)>
public "setInLineComments"(arg0: $List$Type<($CommentLine$Type)>): void
public "setEndComments"(arg0: $List$Type<($CommentLine$Type)>): void
public "setBlockComments"(arg0: $List$Type<($CommentLine$Type)>): void
public "setAnchor"(arg0: string): void
public "isTwoStepsConstruction"(): boolean
public "useClassConstructor"(): boolean
public "setUseClassConstructor"(arg0: boolean): void
public "setTwoStepsConstruction"(arg0: boolean): void
public "setTag"(arg0: $Tag$Type): void
get "type"(): $Class<(any)>
get "tag"(): $Tag
set "type"(value: $Class$Type<(any)>)
get "anchor"(): string
get "startMark"(): $Mark
get "nodeId"(): $NodeId
get "inLineComments"(): $List<($CommentLine)>
get "endMark"(): $Mark
get "blockComments"(): $List<($CommentLine)>
get "endComments"(): $List<($CommentLine)>
set "inLineComments"(value: $List$Type<($CommentLine$Type)>)
set "endComments"(value: $List$Type<($CommentLine$Type)>)
set "blockComments"(value: $List$Type<($CommentLine$Type)>)
set "anchor"(value: string)
get "twoStepsConstruction"(): boolean
set "twoStepsConstruction"(value: boolean)
set "tag"(value: $Tag$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$Type = ($Node);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node_ = $Node$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getStartMark"(): $Mark
public "getEndMark"(): $Mark
public "getTokenId"(): $Token$ID
get "startMark"(): $Mark
get "endMark"(): $Mark
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$Type = ($Token);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token_ = $Token$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$BlockEndToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $BlockEndToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEndToken$Type = ($BlockEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEndToken_ = $BlockEndToken$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceMatchResult" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$SlotLink$MatchResult, $SlotLink$MatchResult$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$MatchResult"

export class $PlaceMatchResult extends $Record implements $Comparable<($PlaceMatchResult)> {

constructor(blockState: $BlockState$Type, interest: integer, links: $List$Type<($SlotLink$MatchResult$Type)>, offsets: $List$Type<($Vec3i$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $PlaceMatchResult$Type): integer
public "offsets"(): $List<($Vec3i)>
public "interest"(): integer
public "blockState"(): $BlockState
public "links"(): $List<($SlotLink$MatchResult)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceMatchResult$Type = ($PlaceMatchResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceMatchResult_ = $PlaceMatchResult$Type;
}}
declare module "packages/snownee/kiwi/mixin/forge/$ItemColorsAccess" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ItemColorsAccess {

 "getItemColors"(): $Map<($Holder$Reference<($Item)>), ($ItemColor)>

(): $Map<($Holder$Reference<($Item)>), ($ItemColor)>
}

export namespace $ItemColorsAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemColorsAccess$Type = ($ItemColorsAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemColorsAccess_ = $ItemColorsAccess$Type;
}}
declare module "packages/snownee/kiwi/handler/$ExtractOnlyItemHandler" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"

export class $ExtractOnlyItemHandler<T extends $IItemHandler> implements $IItemHandler, $Supplier<(T)> {

constructor(arg0: T)

public "get"(): T
public "getSlots"(): integer
public "getStackInSlot"(arg0: integer): $ItemStack
public "insertItem"(arg0: integer, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
public "getSlotLimit"(arg0: integer): integer
public "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
public "isItemValid"(arg0: integer, arg1: $ItemStack$Type): boolean
public "kjs$self"(): $IItemHandler
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "getSlots"(): integer
public "getStackInSlot"(i: integer): $ItemStack
public "insertItem"(i: integer, itemStack: $ItemStack$Type, b: boolean): $ItemStack
public "isMutable"(): boolean
public "extractItem"(i: integer, i1: integer, b: boolean): $ItemStack
public "isItemValid"(i: integer, itemStack: $ItemStack$Type): boolean
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(i: integer): integer
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "setChanged"(): void
public "asContainer"(): $Container
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "getHeight"(): integer
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "getWidth"(): integer
public "clear"(): void
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "slots"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "allItems"(): $List<($ItemStack)>
get "height"(): integer
get "width"(): integer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtractOnlyItemHandler$Type<T> = ($ExtractOnlyItemHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtractOnlyItemHandler_<T> = $ExtractOnlyItemHandler$Type<(T)>;
}}
declare module "packages/snownee/kiwi/command/$KiwiCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$Commands$CommandSelection, $Commands$CommandSelection$Type} from "packages/net/minecraft/commands/$Commands$CommandSelection"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $KiwiCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiCommand$Type = ($KiwiCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiCommand_ = $KiwiCommand$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$PrefixMinusOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $PrefixMinusOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixMinusOperator$Type = ($PrefixMinusOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixMinusOperator_ = $PrefixMinusOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GlassType, $GlassType$Type} from "packages/snownee/kiwi/customization/block/$GlassType"
import {$KiwiModule$RenderLayer$Layer, $KiwiModule$RenderLayer$Layer$Type} from "packages/snownee/kiwi/$KiwiModule$RenderLayer$Layer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CanSurviveHandler, $CanSurviveHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$BlockDefinitionProperties$PartialVanillaProperties, $BlockDefinitionProperties$PartialVanillaProperties$Type} from "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties$PartialVanillaProperties"

export class $BlockDefinitionProperties extends $Record {

constructor(components: $List$Type<($Either$Type<($KBlockComponent$Type), (string)>)>, material: $Optional$Type<($KMaterial$Type)>, glassType: $Optional$Type<($GlassType$Type)>, renderType: $Optional$Type<($KiwiModule$RenderLayer$Layer$Type)>, colorProvider: $Optional$Type<($ResourceLocation$Type)>, shape: $Optional$Type<($ResourceLocation$Type)>, collisionShape: $Optional$Type<($ResourceLocation$Type)>, interactionShape: $Optional$Type<($ResourceLocation$Type)>, canSurviveHandler: $Optional$Type<($CanSurviveHandler$Type)>, vanillaProperties: $BlockDefinitionProperties$PartialVanillaProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "merge"(arg0: $BlockDefinitionProperties$Type): $BlockDefinitionProperties
public "shape"(): $Optional<($ResourceLocation)>
public "components"(): $List<($Either<($KBlockComponent), (string)>)>
public "colorProvider"(): $Optional<($ResourceLocation)>
public "renderType"(): $Optional<($KiwiModule$RenderLayer$Layer)>
public static "mapCodec"(arg0: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $MapCodec<($BlockDefinitionProperties)>
public static "mapCodecField"(arg0: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $MapCodec<($Optional<($BlockDefinitionProperties)>)>
public "glassType"(): $Optional<($GlassType)>
public "material"(): $Optional<($KMaterial)>
public "canSurviveHandler"(): $Optional<($CanSurviveHandler)>
public "interactionShape"(): $Optional<($ResourceLocation)>
public "vanillaProperties"(): $BlockDefinitionProperties$PartialVanillaProperties
public "collisionShape"(): $Optional<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDefinitionProperties$Type = ($BlockDefinitionProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDefinitionProperties_ = $BlockDefinitionProperties$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$MappingEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionEndEvent, $CollectionEndEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $MappingEndEvent extends $CollectionEndEvent {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingEndEvent$Type = ($MappingEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingEndEvent_ = $MappingEndEvent$Type;
}}
declare module "packages/snownee/kiwi/$KiwiTabBuilder" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"

export class $KiwiTabBuilder extends $CreativeModeTab$Builder {
static readonly "BUILDERS": $List<($KiwiTabBuilder)>
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type)

public "build"(): $CreativeModeTab
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiTabBuilder$Type = ($KiwiTabBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiTabBuilder_ = $KiwiTabBuilder$Type;
}}
declare module "packages/snownee/kiwi/util/$VanillaActions" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $VanillaActions {


public static "registerCompostable"(arg0: float, arg1: $ItemLike$Type): void
public static "registerAxeConversion"(arg0: $Block$Type, arg1: $Block$Type): void
public static "registerVillagerFood"(arg0: $ItemLike$Type, arg1: integer): void
public static "registerShovelConversion"(arg0: $Block$Type, arg1: $BlockState$Type): void
public static "registerHoeConversion"(arg0: $Block$Type, arg1: $Pair$Type<($Predicate$Type<($UseOnContext$Type)>), ($Consumer$Type<($UseOnContext$Type)>)>): void
public static "registerVillagerPickupable"(arg0: $ItemLike$Type): void
public static "registerVillagerCompostable"(arg0: $ItemLike$Type): void
public static "setFireInfo"(arg0: $Block$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaActions$Type = ($VanillaActions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaActions_ = $VanillaActions$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixLessOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixLessOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixLessOperator$Type = ($InfixLessOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixLessOperator_ = $InfixLessOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/item/$MultipleBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MultipleBlockItem extends $BlockItem {
static readonly "CODEC": $MapCodec<($MultipleBlockItem)>
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

constructor(arg0: $List$Type<($Pair$Type<(string), ($Block$Type)>)>, arg1: $Item$Properties$Type)

public "getBlock"(arg0: string): $Block
public "removeFromBlockToItemMap"(arg0: $Map$Type<($Block$Type), ($Item$Type)>, arg1: $Item$Type): void
public "registerBlocks"(arg0: $Map$Type<($Block$Type), ($Item$Type)>, arg1: $Item$Type): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultipleBlockItem$Type = ($MultipleBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultipleBlockItem_ = $MultipleBlockItem$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TrustedTagInspector" {
import {$TagInspector, $TagInspector$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TagInspector"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $TrustedTagInspector implements $TagInspector {

constructor()

public "isGlobalTagAllowed"(arg0: $Tag$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrustedTagInspector$Type = ($TrustedTagInspector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrustedTagInspector_ = $TrustedTagInspector$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$ScalarToken" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $ScalarToken extends $Token {

constructor(arg0: string, arg1: $Mark$Type, arg2: $Mark$Type, arg3: boolean)
constructor(arg0: string, arg1: boolean, arg2: $Mark$Type, arg3: $Mark$Type, arg4: $DumperOptions$ScalarStyle$Type)

public "getValue"(): string
public "getPlain"(): boolean
public "getTokenId"(): $Token$ID
public "getStyle"(): $DumperOptions$ScalarStyle
get "value"(): string
get "plain"(): boolean
get "tokenId"(): $Token$ID
get "style"(): $DumperOptions$ScalarStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarToken$Type = ($ScalarToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarToken_ = $ScalarToken$Type;
}}
declare module "packages/snownee/kiwi/config/$KiwiConfigManager" {
import {$ConfigHandler, $ConfigHandler$Type} from "packages/snownee/kiwi/config/$ConfigHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ConfigHandler$Value, $ConfigHandler$Value$Type} from "packages/snownee/kiwi/config/$ConfigHandler$Value"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KiwiConfigManager {
static readonly "allConfigs": $List<($ConfigHandler)>
static readonly "modules": $Map<($ResourceLocation), ($ConfigHandler$Value<(boolean)>)>

constructor()

public static "register"(arg0: $ConfigHandler$Type): void
public static "init"(): void
public static "defineModules"(arg0: string, arg1: $ConfigHandler$Type, arg2: boolean): void
public static "getHandler"(arg0: $Class$Type<(any)>): $ConfigHandler
public static "refresh"(): void
public static "refresh"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiConfigManager$Type = ($KiwiConfigManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiConfigManager_ = $KiwiConfigManager$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$HorizontalAxisComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $HorizontalAxisComponent extends $Record implements $KBlockComponent {
static readonly "AXIS": $EnumProperty<($Direction$Axis)>
static readonly "CODEC": $Codec<($HorizontalAxisComponent)>

constructor(oppose: boolean)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "getInstance"(arg0: boolean): $HorizontalAxisComponent
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "oppose"(): boolean
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalAxisComponent$Type = ($HorizontalAxisComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalAxisComponent_ = $HorizontalAxisComponent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$SqrtFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SqrtFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SqrtFunction$Type = ($SqrtFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SqrtFunction_ = $SqrtFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixGreaterOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixGreaterOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixGreaterOperator$Type = ($InfixGreaterOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixGreaterOperator_ = $InfixGreaterOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$MapBasedDataAccessor" {
import {$DataAccessorIfc, $DataAccessorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$DataAccessorIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"

export class $MapBasedDataAccessor implements $DataAccessorIfc {

constructor()

public "getData"(arg0: string): $EvaluationValue
public "setData"(arg0: string, arg1: $EvaluationValue$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapBasedDataAccessor$Type = ($MapBasedDataAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapBasedDataAccessor_ = $MapBasedDataAccessor$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AsinFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AsinFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsinFunction$Type = ($AsinFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsinFunction_ = $AsinFunction$Type;
}}
declare module "packages/snownee/kiwi/recipe/$EvalCondition" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export class $EvalCondition implements $ICondition {
static readonly "ID": $ResourceLocation

constructor(arg0: string)

public "test"(arg0: $ICondition$IContext$Type): boolean
public "getID"(): $ResourceLocation
public static "shouldRegisterEntry"(arg0: $JsonElement$Type): boolean
get "iD"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvalCondition$Type = ($EvalCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvalCondition_ = $EvalCondition$Type;
}}
declare module "packages/snownee/kiwi/customization/item/$ItemFundamentals" {
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$KItemDefinition, $KItemDefinition$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemDefinition"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$ConfiguredItemTemplate, $ConfiguredItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$ConfiguredItemTemplate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemFundamentals extends $Record {

constructor(templates: $Map$Type<($ResourceLocation$Type), ($KItemTemplate$Type)>, items: $Map$Type<($ResourceLocation$Type), ($KItemDefinition$Type)>, blockItemTemplate: $ConfiguredItemTemplate$Type, defaultProperties: $ItemDefinitionProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "reload"(arg0: $ResourceManager$Type, arg1: $OneTimeLoader$Context$Type, arg2: boolean): $ItemFundamentals
public "items"(): $Map<($ResourceLocation), ($KItemDefinition)>
public "templates"(): $Map<($ResourceLocation), ($KItemTemplate)>
public "addDefaultBlockItem"(arg0: $ResourceLocation$Type): void
public "defaultProperties"(): $ItemDefinitionProperties
public "blockItemTemplate"(): $ConfiguredItemTemplate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemFundamentals$Type = ($ItemFundamentals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemFundamentals_ = $ItemFundamentals$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$Limit" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ParsedProtoTag, $ParsedProtoTag$Type} from "packages/snownee/kiwi/customization/placement/$ParsedProtoTag"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceChoices$Limit extends $Record {
static readonly "CODEC": $Codec<($PlaceChoices$Limit)>

constructor(type: string, tags: $List$Type<($ParsedProtoTag$Type)>)

public "type"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $BlockState$Type, arg1: $BlockState$Type): boolean
public "tags"(): $List<($ParsedProtoTag)>
public "testFace"(arg0: $BlockState$Type, arg1: $Direction$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Limit$Type = ($PlaceChoices$Limit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$Limit_ = $PlaceChoices$Limit$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$KItemTemplates" {
import {$BuiltInItemTemplate, $BuiltInItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$BuiltInItemTemplate"
import {$KiwiGO, $KiwiGO$Type} from "packages/snownee/kiwi/$KiwiGO"
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$SimpleItemTemplate, $SimpleItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$SimpleItemTemplate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockItemTemplate, $BlockItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$BlockItemTemplate"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"

export class $KItemTemplates extends $AbstractModule {
static readonly "SIMPLE": $KiwiGO<($KItemTemplate$Type<($SimpleItemTemplate)>)>
static readonly "BUILT_IN": $KiwiGO<($KItemTemplate$Type<($BuiltInItemTemplate)>)>
static readonly "BLOCK": $KiwiGO<($KItemTemplate$Type<($BlockItemTemplate)>)>
 "uid": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemTemplates$Type = ($KItemTemplates);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemTemplates_ = $KItemTemplates$Type;
}}
declare module "packages/snownee/kiwi/block/$ModBlock" {
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IKiwiBlock, $IKiwiBlock$Type} from "packages/snownee/kiwi/block/$IKiwiBlock"

export class $ModBlock extends $Block implements $IKiwiBlock {
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public static "pick"(arg0: $BlockState$Type, arg1: $HitResult$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: $Player$Type): $ItemStack
public "getCloneItemStack"(arg0: $BlockState$Type, arg1: $HitResult$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: $Player$Type): $ItemStack
public "getName"(arg0: $ItemStack$Type): $MutableComponent
public "createItem"(arg0: $Item$Properties$Type): $BlockItem
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlock$Type = ($ModBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlock_ = $ModBlock$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$StringProperty" {
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"

export class $StringProperty extends $Property<(string)> {

constructor(arg0: string, arg1: $Collection$Type<(string)>)

public "getName"(arg0: string): string
public "equals"(arg0: any): boolean
public static "convert"(arg0: $EnumProperty$Type<(any)>): $StringProperty
public "getPossibleValues"(): $Collection<(string)>
public "getValue"(arg0: string): $Optional<(string)>
public "generateHashCode"(): integer
get "possibleValues"(): $Collection<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringProperty$Type = ($StringProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringProperty_ = $StringProperty$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$SlotLink$MatchResult" {
import {$SlotLink, $SlotLink$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SlotLink$ResultAction, $SlotLink$ResultAction$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$ResultAction"

export class $SlotLink$MatchResult extends $Record {

constructor(link: $SlotLink$Type, isUpright: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "link"(): $SlotLink
public "isUpright"(): boolean
public "onUnlinkTo"(): $SlotLink$ResultAction
public "onLinkFrom"(): $SlotLink$ResultAction
public "onLinkTo"(): $SlotLink$ResultAction
get "upright"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotLink$MatchResult$Type = ($SlotLink$MatchResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotLink$MatchResult_ = $SlotLink$MatchResult$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ShapeStorage" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Supplier, $Supplier$Type} from "packages/com/google/common/base/$Supplier"

export class $ShapeStorage {

constructor(arg0: $Map$Type<($ResourceLocation$Type), ($ShapeGenerator$Type)>)

public "get"(arg0: $ResourceLocation$Type): $ShapeGenerator
public "transform"(arg0: $ShapeGenerator$Type, arg1: any, arg2: $UnaryOperator$Type<($ShapeGenerator$Type)>): $ShapeGenerator
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public static "reload"(arg0: $Supplier$Type<($Map$Type<($ResourceLocation$Type), ($UnbakedShape$Type)>)>): $ShapeStorage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapeStorage$Type = ($ShapeStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapeStorage_ = $ShapeStorage$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SinRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SinRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinRFunction$Type = ($SinRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinRFunction_ = $SinRFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$BaseException" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $BaseException extends $Exception {

constructor(arg0: integer, arg1: integer, arg2: string, arg3: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getMessage"(): string
public "getTokenString"(): string
public "getStartPosition"(): integer
public "getEndPosition"(): integer
get "message"(): string
get "tokenString"(): string
get "startPosition"(): integer
get "endPosition"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseException$Type = ($BaseException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseException_ = $BaseException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$CeilingFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CeilingFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CeilingFunction$Type = ($CeilingFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CeilingFunction_ = $CeilingFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate$PropertyMatcher" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $StatePropertiesPredicate$PropertyMatcher extends $Record {

constructor(key: string, value: $Either$Type<($Set$Type<(string)>), ($MinMaxBounds$Ints$Type)>)

public "value"(): $Either<($Set<(string)>), ($MinMaxBounds$Ints)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $BlockState$Type): boolean
public "key"(): string
public "smartTest"(arg0: $BlockState$Type, arg1: $BlockState$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatePropertiesPredicate$PropertyMatcher$Type = ($StatePropertiesPredicate$PropertyMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatePropertiesPredicate$PropertyMatcher_ = $StatePropertiesPredicate$PropertyMatcher$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactData" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CompactData {

constructor(arg0: string)

public "toString"(): string
public "getProperties"(): $Map<(string), (string)>
public "getPrefix"(): string
public "getArguments"(): $List<(string)>
get "properties"(): $Map<(string), (string)>
get "prefix"(): string
get "arguments"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactData$Type = ($CompactData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactData_ = $CompactData$Type;
}}
declare module "packages/snownee/kiwi/block/entity/$ModBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ModBlockEntity extends $BlockEntity {
 "persistData": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "refresh"(): void
public "getUpdateTag"(): $CompoundTag
public "getUpdatePacket"(): $Packet<($ClientGamePacketListener)>
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
get "updateTag"(): $CompoundTag
get "updatePacket"(): $Packet<($ClientGamePacketListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockEntity$Type = ($ModBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockEntity_ = $ModBlockEntity$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$SafeRepresenter" {
import {$DumperOptions, $DumperOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$BaseRepresenter, $BaseRepresenter$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$BaseRepresenter"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $SafeRepresenter extends $BaseRepresenter {

constructor(arg0: $DumperOptions$Type)

public "setTimeZone"(arg0: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "addClassTag"(arg0: $Class$Type<(any)>, arg1: $Tag$Type): $Tag
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeRepresenter$Type = ($SafeRepresenter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeRepresenter_ = $SafeRepresenter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/composer/$ComposerException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"

export class $ComposerException extends $MarkedYAMLException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComposerException$Type = ($ComposerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComposerException_ = $ComposerException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ShuntingYardConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ASTNode, $ASTNode$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ASTNode"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $ShuntingYardConverter {

constructor(arg0: string, arg1: $List$Type<($Token$Type)>, arg2: $ExpressionConfiguration$Type)

public "toAbstractSyntaxTree"(): $ASTNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShuntingYardConverter$Type = ($ShuntingYardConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShuntingYardConverter_ = $ShuntingYardConverter$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$SimplePropertiesComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $SimplePropertiesComponent extends $Record implements $KBlockComponent {
static readonly "SINGLE_CODEC": $Codec<($Pair<($Property<(any)>), (string)>)>
static readonly "CODEC": $Codec<($SimplePropertiesComponent)>

constructor(useShapeForLightOcclusion: boolean, properties: $List$Type<($Pair$Type<($Property$Type<(any)>), (string)>)>)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "properties"(): $List<($Pair<($Property<(any)>), (string)>)>
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "useShapeForLightOcclusion"(): boolean
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimplePropertiesComponent$Type = ($SimplePropertiesComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimplePropertiesComponent_ = $SimplePropertiesComponent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$LineBreak" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$LineBreak extends $Enum<($DumperOptions$LineBreak)> {
static readonly "WIN": $DumperOptions$LineBreak
static readonly "MAC": $DumperOptions$LineBreak
static readonly "UNIX": $DumperOptions$LineBreak


public "toString"(): string
public static "values"(): ($DumperOptions$LineBreak)[]
public static "valueOf"(arg0: string): $DumperOptions$LineBreak
public "getString"(): string
public static "getPlatformLineBreak"(): $DumperOptions$LineBreak
get "string"(): string
get "platformLineBreak"(): $DumperOptions$LineBreak
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$LineBreak$Type = (("win") | ("mac") | ("unix")) | ($DumperOptions$LineBreak);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$LineBreak_ = $DumperOptions$LineBreak$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$BlockMappingStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $BlockMappingStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockMappingStartToken$Type = ($BlockMappingStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockMappingStartToken_ = $BlockMappingStartToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$Atan2RFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $Atan2RFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Atan2RFunction$Type = ($Atan2RFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Atan2RFunction_ = $Atan2RFunction$Type;
}}
declare module "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context" {
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"

export class $OneTimeLoader$Context {

constructor()

public "getExpression"(arg0: string): $Expression
public "addDisabledNamespace"(arg0: string): void
public "isNamespaceDisabled"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OneTimeLoader$Context$Type = ($OneTimeLoader$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OneTimeLoader$Context_ = $OneTimeLoader$Context$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BuilderRules" {
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$BuilderRule, $BuilderRule$Type} from "packages/snownee/kiwi/customization/builder/$BuilderRule"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $BuilderRules {

constructor()

public static "get"(arg0: $ResourceLocation$Type): $BuilderRule
public static "find"(arg0: $Block$Type): $Collection<($KHolder<($BuilderRule)>)>
public static "all"(): $Collection<($KHolder<($BuilderRule)>)>
public static "reload"(arg0: $ResourceManager$Type, arg1: $OneTimeLoader$Context$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderRules$Type = ($BuilderRules);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderRules_ = $BuilderRules$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$CompactData, $CompactData$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactData"
import {$Constructor, $Constructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $CompactConstructor extends $Constructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(arg0: $LoaderOptions$Type)
constructor()

public "getCompactData"(arg0: string): $CompactData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactConstructor$Type = ($CompactConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactConstructor_ = $CompactConstructor$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$Log10Function" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $Log10Function extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Log10Function$Type = ($Log10Function);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Log10Function_ = $Log10Function$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentLine" {
import {$CommentType, $CommentType$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentType"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$CommentEvent, $CommentEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CommentEvent"

export class $CommentLine {

constructor(arg0: $CommentEvent$Type)
constructor(arg0: $Mark$Type, arg1: $Mark$Type, arg2: string, arg3: $CommentType$Type)

public "toString"(): string
public "getValue"(): string
public "getStartMark"(): $Mark
public "getEndMark"(): $Mark
public "getCommentType"(): $CommentType
get "value"(): string
get "startMark"(): $Mark
get "endMark"(): $Mark
get "commentType"(): $CommentType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentLine$Type = ($CommentLine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentLine_ = $CommentLine$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$SimpleBlockTemplate" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$BlockDefinitionProperties, $BlockDefinitionProperties$Type} from "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$KBlockTemplate$Type, $KBlockTemplate$Type$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SimpleBlockTemplate extends $KBlockTemplate {

constructor(arg0: $Optional$Type<($BlockDefinitionProperties$Type)>, arg1: string)

public "clazz"(): string
public "type"(): $KBlockTemplate$Type<(any)>
public "toString"(): string
public "resolve"(arg0: $ResourceLocation$Type): void
public static "directCodec"(arg0: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $Codec<($SimpleBlockTemplate)>
public "createBlock"(arg0: $ResourceLocation$Type, arg1: $BlockBehaviour$Properties$Type, arg2: $JsonObject$Type): $Block
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleBlockTemplate$Type = ($SimpleBlockTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleBlockTemplate_ = $SimpleBlockTemplate$Type;
}}
declare module "packages/snownee/kiwi/build/$KiwiAnnotationProcessor" {
import {$SourceVersion, $SourceVersion$Type} from "packages/javax/lang/model/$SourceVersion"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$RoundEnvironment, $RoundEnvironment$Type} from "packages/javax/annotation/processing/$RoundEnvironment"
import {$ProcessingEnvironment, $ProcessingEnvironment$Type} from "packages/javax/annotation/processing/$ProcessingEnvironment"
import {$AbstractProcessor, $AbstractProcessor$Type} from "packages/javax/annotation/processing/$AbstractProcessor"

export class $KiwiAnnotationProcessor extends $AbstractProcessor {

constructor()

public "init"(arg0: $ProcessingEnvironment$Type): void
public "process"(arg0: $Set$Type<(any)>, arg1: $RoundEnvironment$Type): boolean
public "getSupportedSourceVersion"(): $SourceVersion
get "supportedSourceVersion"(): $SourceVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiAnnotationProcessor$Type = ($KiwiAnnotationProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiAnnotationProcessor_ = $KiwiAnnotationProcessor$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$NodeEvent" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $NodeEvent extends $Event {

constructor(arg0: string, arg1: $Mark$Type, arg2: $Mark$Type)

public "getAnchor"(): string
get "anchor"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeEvent$Type = ($NodeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeEvent_ = $NodeEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Tokenizer" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $Tokenizer {

constructor(arg0: string, arg1: $ExpressionConfiguration$Type)

public "parse"(): $List<($Token)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tokenizer$Type = ($Tokenizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tokenizer_ = $Tokenizer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$DocumentStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DocumentStartEvent extends $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type, arg2: boolean, arg3: $DumperOptions$Version$Type, arg4: $Map$Type<(string), (string)>)

public "getVersion"(): $DumperOptions$Version
public "getEventId"(): $Event$ID
public "getExplicit"(): boolean
public "getTags"(): $Map<(string), (string)>
get "version"(): $DumperOptions$Version
get "eventId"(): $Event$ID
get "explicit"(): boolean
get "tags"(): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentStartEvent$Type = ($DocumentStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentStartEvent_ = $DocumentStartEvent$Type;
}}
declare module "packages/snownee/kiwi/datagen/$GameObjectLookup$OptionalEntry" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $GameObjectLookup$OptionalEntry<T> extends $Record {

constructor(object: T, optional: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "object"(): T
public "optional"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameObjectLookup$OptionalEntry$Type<T> = ($GameObjectLookup$OptionalEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameObjectLookup$OptionalEntry_<T> = $GameObjectLookup$OptionalEntry$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$ScannerImpl" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Scanner, $Scanner$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$Scanner"
import {$StreamReader, $StreamReader$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/reader/$StreamReader"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $ScannerImpl implements $Scanner {
static readonly "ESCAPE_REPLACEMENTS": $Map<(character), (string)>
static readonly "ESCAPE_CODES": $Map<(character), (integer)>

constructor(arg0: $StreamReader$Type, arg1: $LoaderOptions$Type)

public "getToken"(): $Token
public "checkToken"(...arg0: ($Token$ID$Type)[]): boolean
public "peekToken"(): $Token
get "token"(): $Token
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScannerImpl$Type = ($ScannerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScannerImpl_ = $ScannerImpl$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$SlotLink$Preparation" {
import {$SlotLink, $SlotLink$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlaceSlotProvider$Preparation, $PlaceSlotProvider$Preparation$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Preparation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SlotLink$Preparation extends $Record {

constructor(slotLinks: $Map$Type<($ResourceLocation$Type), ($SlotLink$Type)>, slotProviders: $PlaceSlotProvider$Preparation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Supplier$Type<($Map$Type<($ResourceLocation$Type), ($SlotLink$Type)>)>, arg1: $PlaceSlotProvider$Preparation$Type): $SlotLink$Preparation
public "finish"(): void
public "slotProviders"(): $PlaceSlotProvider$Preparation
public "slotLinks"(): $Map<($ResourceLocation), ($SlotLink)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotLink$Preparation$Type = ($SlotLink$Preparation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotLink$Preparation_ = $SlotLink$Preparation$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$SimpleKey" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $SimpleKey {

constructor(arg0: integer, arg1: boolean, arg2: integer, arg3: integer, arg4: integer, arg5: $Mark$Type)

public "toString"(): string
public "getIndex"(): integer
public "getLine"(): integer
public "isRequired"(): boolean
public "getTokenNumber"(): integer
public "getMark"(): $Mark
public "getColumn"(): integer
get "index"(): integer
get "line"(): integer
get "required"(): boolean
get "tokenNumber"(): integer
get "mark"(): $Mark
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleKey$Type = ($SimpleKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleKey_ = $SimpleKey$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/string/$StringEndsWithFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $StringEndsWithFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringEndsWithFunction$Type = ($StringEndsWithFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringEndsWithFunction_ = $StringEndsWithFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$MapBasedOperatorDictionary" {
import {$OperatorDictionaryIfc, $OperatorDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$OperatorDictionaryIfc"
import {$OperatorIfc, $OperatorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $MapBasedOperatorDictionary implements $OperatorDictionaryIfc {

constructor()

public "addOperator"(arg0: string, arg1: $OperatorIfc$Type): void
public "getPrefixOperator"(arg0: string): $OperatorIfc
public static "ofOperators"(...arg0: ($Map$Entry$Type<(string), ($OperatorIfc$Type)>)[]): $OperatorDictionaryIfc
public "getPostfixOperator"(arg0: string): $OperatorIfc
public "getInfixOperator"(arg0: string): $OperatorIfc
public "hasPostfixOperator"(arg0: string): boolean
public "hasPrefixOperator"(arg0: string): boolean
public "hasInfixOperator"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapBasedOperatorDictionary$Type = ($MapBasedOperatorDictionary);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapBasedOperatorDictionary_ = $MapBasedOperatorDictionary$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/reader/$StreamReader" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $StreamReader {

constructor(arg0: string)
constructor(arg0: $Reader$Type)

public "prefix"(arg0: integer): string
public "peek"(arg0: integer): integer
public "peek"(): integer
public "getIndex"(): integer
public static "isPrintable"(arg0: string): boolean
public static "isPrintable"(arg0: integer): boolean
public "getLine"(): integer
public "forward"(arg0: integer): void
public "forward"(): void
public "prefixForward"(arg0: integer): string
public "getMark"(): $Mark
public "getColumn"(): integer
get "index"(): integer
get "line"(): integer
get "mark"(): $Mark
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamReader$Type = ($StreamReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamReader_ = $StreamReader$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$FlowStyle extends $Enum<($DumperOptions$FlowStyle)> {
static readonly "FLOW": $DumperOptions$FlowStyle
static readonly "BLOCK": $DumperOptions$FlowStyle
static readonly "AUTO": $DumperOptions$FlowStyle


public "toString"(): string
public static "values"(): ($DumperOptions$FlowStyle)[]
public static "valueOf"(arg0: string): $DumperOptions$FlowStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$FlowStyle$Type = (("auto") | ("block") | ("flow")) | ($DumperOptions$FlowStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$FlowStyle_ = $DumperOptions$FlowStyle$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/layer/$FoxTailLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $FoxTailLayer extends $CosmeticLayer {
static readonly "ALL_LAYERS": $Collection<($CosmeticLayer)>
readonly "f_117344_": $RenderLayerParent<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoxTailLayer$Type = ($FoxTailLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoxTailLayer_ = $FoxTailLayer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AtanHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AtanHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtanHFunction$Type = ($AtanHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtanHFunction_ = $AtanHFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/util/$UriEncoder" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $UriEncoder {

constructor()

public static "decode"(arg0: string): string
public static "decode"(arg0: $ByteBuffer$Type): string
public static "encode"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UriEncoder$Type = ($UriEncoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UriEncoder_ = $UriEncoder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$Emitter" {
import {$DumperOptions, $DumperOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Emitable, $Emitable$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$Emitable"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"

export class $Emitter implements $Emitable {
static readonly "MIN_INDENT": integer
static readonly "MAX_INDENT": integer

constructor(arg0: $Writer$Type, arg1: $DumperOptions$Type)

public "emit"(arg0: $Event$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Emitter$Type = ($Emitter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Emitter_ = $Emitter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$AbstractConstruct" {
import {$Construct, $Construct$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Construct"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $AbstractConstruct implements $Construct {

constructor()

public "construct2ndStep"(arg0: $Node$Type, arg1: any): void
public "construct"(arg0: $Node$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractConstruct$Type = ($AbstractConstruct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractConstruct_ = $AbstractConstruct$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/model/$SantaHatModel" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$AgeableListModel, $AgeableListModel$Type} from "packages/net/minecraft/client/model/$AgeableListModel"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $SantaHatModel<T extends $LivingEntity> extends $AgeableListModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $PlayerModel$Type<($AbstractClientPlayer$Type)>, arg1: $LayerDefinition$Type)

public static "create"(): $LayerDefinition
public "setupAnim"(arg0: T, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SantaHatModel$Type<T> = ($SantaHatModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SantaHatModel_<T> = $SantaHatModel$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties$PartialVanillaProperties" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockBehaviour$OffsetType, $BlockBehaviour$OffsetType$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$OffsetType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockBehaviour$StatePredicate, $BlockBehaviour$StatePredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StatePredicate"
import {$PushReaction, $PushReaction$Type} from "packages/net/minecraft/world/level/material/$PushReaction"
import {$BlockBehaviour$StateArgumentPredicate, $BlockBehaviour$StateArgumentPredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StateArgumentPredicate"

export class $BlockDefinitionProperties$PartialVanillaProperties extends $Record {
static readonly "MAP_CODEC": $MapCodec<($BlockDefinitionProperties$PartialVanillaProperties)>

constructor(noCollision: $Optional$Type<(boolean)>, isRandomlyTicking: $Optional$Type<(boolean)>, lightEmission: $Optional$Type<(integer)>, dynamicShape: $Optional$Type<(boolean)>, noOcclusion: $Optional$Type<(boolean)>, pushReaction: $Optional$Type<($PushReaction$Type)>, offsetType: $Optional$Type<($BlockBehaviour$OffsetType$Type)>, replaceable: $Optional$Type<(boolean)>, isValidSpawn: $Optional$Type<($BlockBehaviour$StateArgumentPredicate$Type<($EntityType$Type<(any)>)>)>, isRedstoneConductor: $Optional$Type<($BlockBehaviour$StatePredicate$Type)>, isSuffocating: $Optional$Type<($BlockBehaviour$StatePredicate$Type)>, isViewBlocking: $Optional$Type<($BlockBehaviour$StatePredicate$Type)>, hasPostProcess: $Optional$Type<($BlockBehaviour$StatePredicate$Type)>, emissiveRendering: $Optional$Type<($BlockBehaviour$StatePredicate$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "merge"(arg0: $BlockDefinitionProperties$PartialVanillaProperties$Type): $BlockDefinitionProperties$PartialVanillaProperties
public "isRedstoneConductor"(): $Optional<($BlockBehaviour$StatePredicate)>
public "lightEmission"(): $Optional<(integer)>
public "isViewBlocking"(): $Optional<($BlockBehaviour$StatePredicate)>
public "hasPostProcess"(): $Optional<($BlockBehaviour$StatePredicate)>
public "offsetType"(): $Optional<($BlockBehaviour$OffsetType)>
public "emissiveRendering"(): $Optional<($BlockBehaviour$StatePredicate)>
public "dynamicShape"(): $Optional<(boolean)>
public "pushReaction"(): $Optional<($PushReaction)>
public "noOcclusion"(): $Optional<(boolean)>
public "isSuffocating"(): $Optional<($BlockBehaviour$StatePredicate)>
public "noCollision"(): $Optional<(boolean)>
public "isValidSpawn"(): $Optional<($BlockBehaviour$StateArgumentPredicate<($EntityType<(any)>)>)>
public "replaceable"(): $Optional<(boolean)>
public "isRandomlyTicking"(): $Optional<(boolean)>
get "redstoneConductor"(): boolean
get "viewBlocking"(): boolean
get "suffocating"(): boolean
get "validSpawn"(): boolean
get "randomlyTicking"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDefinitionProperties$PartialVanillaProperties$Type = ($BlockDefinitionProperties$PartialVanillaProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDefinitionProperties$PartialVanillaProperties_ = $BlockDefinitionProperties$PartialVanillaProperties$Type;
}}
declare module "packages/snownee/kiwi/$LoadingContext" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $LoadingContext {
readonly "id": $ResourceLocation

constructor(arg0: $ResourceLocation$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadingContext$Type = ($LoadingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadingContext_ = $LoadingContext$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$DateTimeConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $DateTimeConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "parseDateTime"(arg0: string, arg1: $ZoneId$Type, arg2: $List$Type<($DateTimeFormatter$Type)>): $Instant
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeConverter$Type = ($DateTimeConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeConverter_ = $DateTimeConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BeanAccess, $BeanAccess$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess"
import {$Property, $Property$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property"

export class $PropertyUtils {

constructor()

public "getProperty"(arg0: $Class$Type<(any)>, arg1: string): $Property
public "getProperty"(arg0: $Class$Type<(any)>, arg1: string, arg2: $BeanAccess$Type): $Property
public "getProperties"(arg0: $Class$Type<(any)>): $Set<($Property)>
public "getProperties"(arg0: $Class$Type<(any)>, arg1: $BeanAccess$Type): $Set<($Property)>
public "setBeanAccess"(arg0: $BeanAccess$Type): void
public "isAllowReadOnlyProperties"(): boolean
public "setAllowReadOnlyProperties"(arg0: boolean): void
public "setSkipMissingProperties"(arg0: boolean): void
public "isSkipMissingProperties"(): boolean
set "beanAccess"(value: $BeanAccess$Type)
get "allowReadOnlyProperties"(): boolean
set "allowReadOnlyProperties"(value: boolean)
set "skipMissingProperties"(value: boolean)
get "skipMissingProperties"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyUtils$Type = ($PropertyUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyUtils_ = $PropertyUtils$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$AbstractHorizontalShape" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export interface $AbstractHorizontalShape extends $ShapeGenerator {

 "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
 "getDirection"(arg0: $BlockState$Type): $Direction
 "shapes"(): ($VoxelShape)[]
}

export namespace $AbstractHorizontalShape {
function unit(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractHorizontalShape$Type = ($AbstractHorizontalShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractHorizontalShape_ = $AbstractHorizontalShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token" {
import {$FunctionIfc, $FunctionIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc"
import {$Token$TokenType, $Token$TokenType$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token$TokenType"
import {$OperatorIfc, $OperatorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc"

export class $Token {

constructor(arg0: integer, arg1: string, arg2: $Token$TokenType$Type, arg3: $FunctionIfc$Type, arg4: $OperatorIfc$Type)
constructor(arg0: integer, arg1: string, arg2: $Token$TokenType$Type)
constructor(arg0: integer, arg1: string, arg2: $Token$TokenType$Type, arg3: $FunctionIfc$Type)
constructor(arg0: integer, arg1: string, arg2: $Token$TokenType$Type, arg3: $OperatorIfc$Type)

public "getFunctionDefinition"(): $FunctionIfc
public "getOperatorDefinition"(): $OperatorIfc
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): string
public "getType"(): $Token$TokenType
public "getStartPosition"(): integer
get "functionDefinition"(): $FunctionIfc
get "operatorDefinition"(): $OperatorIfc
get "value"(): string
get "type"(): $Token$TokenType
get "startPosition"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$Type = ($Token);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token_ = $Token$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/string/$StringLowerFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $StringLowerFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringLowerFunction$Type = ($StringLowerFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringLowerFunction_ = $StringLowerFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeTodayFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeTodayFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeTodayFunction$Type = ($DateTimeTodayFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeTodayFunction_ = $DateTimeTodayFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/compat/jade/$JadeCompat" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $JadeCompat implements $IWailaPlugin {

constructor()

public "registerClient"(arg0: $IWailaClientRegistration$Type): void
public "register"(arg0: $IWailaCommonRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeCompat$Type = ($JadeCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeCompat_ = $JadeCompat$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcosFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcosFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcosFunction$Type = ($AcosFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcosFunction_ = $AcosFunction$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/$KiwiTierProvider" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JsonTierProvider, $JsonTierProvider$Type} from "packages/snownee/kiwi/contributor/impl/$JsonTierProvider"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $KiwiTierProvider extends $JsonTierProvider {
static readonly "GSON": $Gson
static readonly "CODEC": $Codec<($Map<(string), ($List<(string)>)>)>

constructor()

public "createRenderer"(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>, arg1: string): $CosmeticLayer
public "getTiers"(): $Set<(string)>
public "getPlayerTiers"(arg0: string): $Set<(string)>
public "getRenderableTiers"(): $List<(string)>
get "tiers"(): $Set<(string)>
get "renderableTiers"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiTierProvider$Type = ($KiwiTierProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiTierProvider_ = $KiwiTierProvider$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$DefaultEvaluationValueConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$EvaluationValueConverterIfc, $EvaluationValueConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$EvaluationValueConverterIfc"

export class $DefaultEvaluationValueConverter implements $EvaluationValueConverterIfc {

constructor()

public "convertObject"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultEvaluationValueConverter$Type = ($DefaultEvaluationValueConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultEvaluationValueConverter_ = $DefaultEvaluationValueConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$DirectiveToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $DirectiveToken<T> extends $Token {

constructor(arg0: string, arg1: $List$Type<(T)>, arg2: $Mark$Type, arg3: $Mark$Type)

public "getName"(): string
public "getValue"(): $List<(T)>
public "getTokenId"(): $Token$ID
get "name"(): string
get "value"(): $List<(T)>
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectiveToken$Type<T> = ($DirectiveToken<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectiveToken_<T> = $DirectiveToken$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/shape/$UnbakedShape" {
import {$BakingContext, $BakingContext$Type} from "packages/snownee/kiwi/customization/shape/$BakingContext"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export interface $UnbakedShape {

 "dependencies"(): $Stream<($UnbakedShape)>
 "bake"(arg0: $BakingContext$Type): $ShapeGenerator
}

export namespace $UnbakedShape {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnbakedShape$Type = ($UnbakedShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnbakedShape_ = $UnbakedShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$ValueToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $ValueToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueToken$Type = ($ValueToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueToken_ = $ValueToken$Type;
}}
declare module "packages/snownee/kiwi/schedule/$Scheduler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$ITicker, $ITicker$Type} from "packages/snownee/kiwi/schedule/$ITicker"
import {$ClientPlayerNetworkEvent$LoggingOut, $ClientPlayerNetworkEvent$LoggingOut$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingOut"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Task, $Task$Type} from "packages/snownee/kiwi/schedule/$Task"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"

export class $Scheduler extends $SavedData {
static readonly "ID": string
static readonly "INSTANCE": $Scheduler


public static "add"(arg0: $Task$Type<(any)>): void
public static "remove"(arg0: $Task$Type<(any)>): void
public static "load"(arg0: $CompoundTag$Type): $Scheduler
public static "clear"(): void
public static "register"(arg0: $ResourceLocation$Type, arg1: $Class$Type<(any)>): void
public static "tick"<T extends $ITicker>(arg0: T): void
public static "deserialize"(arg0: $CompoundTag$Type): $Task<(any)>
public static "serverStopped"(arg0: $ServerStoppedEvent$Type): void
public "serialize"(arg0: $Task$Type<(any)>): $CompoundTag
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "isDirty"(): boolean
public static "clientLoggedOut"(arg0: $ClientPlayerNetworkEvent$LoggingOut$Type): void
get "dirty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Scheduler$Type = ($Scheduler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Scheduler_ = $Scheduler$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration$ExpressionConfigurationBuilder" {
import {$OperatorDictionaryIfc, $OperatorDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$OperatorDictionaryIfc"
import {$DataAccessorIfc, $DataAccessorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$DataAccessorIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$FunctionDictionaryIfc, $FunctionDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$FunctionDictionaryIfc"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$EvaluationValueConverterIfc, $EvaluationValueConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$EvaluationValueConverterIfc"
import {$MathContext, $MathContext$Type} from "packages/java/math/$MathContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ExpressionConfiguration$ExpressionConfigurationBuilder {


public "toString"(): string
public "build"(): $ExpressionConfiguration
public "stripTrailingZeros"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "zoneId"(arg0: $ZoneId$Type): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "decimalPlacesResult"(arg0: integer): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "singleQuoteStringLiteralsAllowed"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "implicitMultiplicationAllowed"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "evaluationValueConverter"(arg0: $EvaluationValueConverterIfc$Type): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "allowOverwriteConstants"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "decimalPlacesRounding"(arg0: integer): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "dataAccessorSupplier"(arg0: $Supplier$Type<($DataAccessorIfc$Type)>): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "mathContext"(arg0: $MathContext$Type): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "functionDictionary"(arg0: $FunctionDictionaryIfc$Type): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "operatorDictionary"(arg0: $OperatorDictionaryIfc$Type): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "defaultConstants"(arg0: $Map$Type<(string), ($EvaluationValue$Type)>): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "arraysAllowed"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "structuresAllowed"(arg0: boolean): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "powerOfPrecedence"(arg0: integer): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "dateTimeFormatters"(arg0: $List$Type<($DateTimeFormatter$Type)>): $ExpressionConfiguration$ExpressionConfigurationBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpressionConfiguration$ExpressionConfigurationBuilder$Type = ($ExpressionConfiguration$ExpressionConfigurationBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpressionConfiguration$ExpressionConfigurationBuilder_ = $ExpressionConfiguration$ExpressionConfigurationBuilder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription" {
import {$PropertySubstitute, $PropertySubstitute$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertySubstitute"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$PropertyUtils, $PropertyUtils$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Property, $Property$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $TypeDescription {

constructor(arg0: $Class$Type<(any)>, arg1: $Class$Type<(any)>)
constructor(arg0: $Class$Type<(any)>)
constructor(arg0: $Class$Type<(any)>, arg1: string)
constructor(arg0: $Class$Type<(any)>, arg1: $Tag$Type, arg2: $Class$Type<(any)>)
constructor(arg0: $Class$Type<(any)>, arg1: $Tag$Type)

public "getProperty"(arg0: string): $Property
public "toString"(): string
public "newInstance"(arg0: string, arg1: $Node$Type): any
public "newInstance"(arg0: $Node$Type): any
public "setProperty"(arg0: any, arg1: string, arg2: any): boolean
public "getProperties"(): $Set<($Property)>
public "getType"(): $Class<(any)>
public "getTag"(): $Tag
public "addPropertyParameters"(arg0: string, ...arg1: ($Class$Type<(any)>)[]): void
/**
 * 
 * @deprecated
 */
public "putListPropertyType"(arg0: string, arg1: $Class$Type<(any)>): void
public "substituteProperty"(arg0: $PropertySubstitute$Type): void
public "substituteProperty"(arg0: string, arg1: $Class$Type<(any)>, arg2: string, arg3: string, ...arg4: ($Class$Type<(any)>)[]): void
public "setupPropertyType"(arg0: string, arg1: $Node$Type): boolean
/**
 * 
 * @deprecated
 */
public "putMapPropertyType"(arg0: string, arg1: $Class$Type<(any)>, arg2: $Class$Type<(any)>): void
public "setIncludes"(...arg0: (string)[]): void
public "setPropertyUtils"(arg0: $PropertyUtils$Type): void
public "finalizeConstruction"(arg0: any): any
public "setExcludes"(...arg0: (string)[]): void
get "properties"(): $Set<($Property)>
get "type"(): $Class<(any)>
get "tag"(): $Tag
set "includes"(value: (string)[])
set "propertyUtils"(value: $PropertyUtils$Type)
set "excludes"(value: (string)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDescription$Type = ($TypeDescription);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDescription_ = $TypeDescription$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$ScalarNode" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $ScalarNode extends $Node {

constructor(arg0: $Tag$Type, arg1: string, arg2: $Mark$Type, arg3: $Mark$Type, arg4: $DumperOptions$ScalarStyle$Type)
constructor(arg0: $Tag$Type, arg1: boolean, arg2: string, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$ScalarStyle$Type)

public "toString"(): string
public "getValue"(): string
public "getNodeId"(): $NodeId
public "getScalarStyle"(): $DumperOptions$ScalarStyle
public "isPlain"(): boolean
get "value"(): string
get "nodeId"(): $NodeId
get "scalarStyle"(): $DumperOptions$ScalarStyle
get "plain"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarNode$Type = ($ScalarNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarNode_ = $ScalarNode$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$EvaluationValueConverterIfc" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"

export interface $EvaluationValueConverterIfc {

 "convertObject"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue

(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
}

export namespace $EvaluationValueConverterIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvaluationValueConverterIfc$Type = ($EvaluationValueConverterIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvaluationValueConverterIfc_ = $EvaluationValueConverterIfc$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export interface $ConverterIfc {

 "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
 "canConvert"(arg0: any): boolean
 "illegalArgument"(arg0: any): $IllegalArgumentException
}

export namespace $ConverterIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConverterIfc$Type = ($ConverterIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConverterIfc_ = $ConverterIfc$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixLessEqualsOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixLessEqualsOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixLessEqualsOperator$Type = ($InfixLessEqualsOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixLessEqualsOperator_ = $InfixLessEqualsOperator$Type;
}}
declare module "packages/snownee/kiwi/config/$KiwiConfig" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$KiwiConfig$ConfigType, $KiwiConfig$ConfigType$Type} from "packages/snownee/kiwi/config/$KiwiConfig$ConfigType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $KiwiConfig extends $Annotation {

 "type"(): $KiwiConfig$ConfigType
 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $KiwiConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiConfig$Type = ($KiwiConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiConfig_ = $KiwiConfig$Type;
}}
declare module "packages/snownee/kiwi/loader/event/$PostInitEvent" {
import {$ParallelEvent, $ParallelEvent$Type} from "packages/snownee/kiwi/loader/event/$ParallelEvent"
import {$ParallelDispatchEvent, $ParallelDispatchEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$ParallelDispatchEvent"

export class $PostInitEvent extends $ParallelEvent {

constructor(arg0: $ParallelDispatchEvent$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostInitEvent$Type = ($PostInitEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostInitEvent_ = $PostInitEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$DataAccessorIfc" {
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"

export interface $DataAccessorIfc {

 "getData"(arg0: string): $EvaluationValue
 "setData"(arg0: string, arg1: $EvaluationValue$Type): void
}

export namespace $DataAccessorIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataAccessorIfc$Type = ($DataAccessorIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataAccessorIfc_ = $DataAccessorIfc$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$StreamEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $StreamEndEvent extends $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamEndEvent$Type = ($StreamEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamEndEvent_ = $StreamEndEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition$FunctionParameterDefinitionBuilder" {
import {$FunctionParameterDefinition, $FunctionParameterDefinition$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition"

export class $FunctionParameterDefinition$FunctionParameterDefinitionBuilder {


public "name"(arg0: string): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
public "toString"(): string
public "build"(): $FunctionParameterDefinition
public "isLazy"(arg0: boolean): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
public "isVarArg"(arg0: boolean): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
public "nonZero"(arg0: boolean): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
public "nonNegative"(arg0: boolean): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionParameterDefinition$FunctionParameterDefinitionBuilder$Type = ($FunctionParameterDefinition$FunctionParameterDefinitionBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionParameterDefinition$FunctionParameterDefinitionBuilder_ = $FunctionParameterDefinition$FunctionParameterDefinitionBuilder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue$DataType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EvaluationValue$DataType extends $Enum<($EvaluationValue$DataType)> {
static readonly "STRING": $EvaluationValue$DataType
static readonly "NUMBER": $EvaluationValue$DataType
static readonly "BOOLEAN": $EvaluationValue$DataType
static readonly "DATE_TIME": $EvaluationValue$DataType
static readonly "DURATION": $EvaluationValue$DataType
static readonly "ARRAY": $EvaluationValue$DataType
static readonly "STRUCTURE": $EvaluationValue$DataType
static readonly "EXPRESSION_NODE": $EvaluationValue$DataType
static readonly "NULL": $EvaluationValue$DataType


public static "values"(): ($EvaluationValue$DataType)[]
public static "valueOf"(arg0: string): $EvaluationValue$DataType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvaluationValue$DataType$Type = (("duration") | ("number") | ("boolean") | ("string") | ("date_time") | ("null") | ("array") | ("expression_node") | ("structure")) | ($EvaluationValue$DataType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvaluationValue$DataType_ = $EvaluationValue$DataType$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Side" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceSlotProvider$Side extends $Record {
static readonly "CODEC": $Codec<($PlaceSlotProvider$Side)>

constructor(tag: $List$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "tag"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceSlotProvider$Side$Type = ($PlaceSlotProvider$Side);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceSlotProvider$Side_ = $PlaceSlotProvider$Side$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameter" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $FunctionParameter extends $Annotation {

 "name"(): string
 "isLazy"(): boolean
 "isVarArg"(): boolean
 "nonZero"(): boolean
 "nonNegative"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $FunctionParameter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionParameter$Type = ($FunctionParameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionParameter_ = $FunctionParameter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$Escaper" {
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"

export interface $Escaper {

 "escape"(arg0: string): string
 "escape"(arg0: $Appendable$Type): $Appendable
}

export namespace $Escaper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Escaper$Type = ($Escaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Escaper_ = $Escaper$Type;
}}
declare module "packages/snownee/kiwi/mixin/forge/$BlockColorsAccess" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $BlockColorsAccess {

 "getBlockColors"(): $Map<($Holder$Reference<($Block)>), ($BlockColor)>

(): $Map<($Holder$Reference<($Block)>), ($BlockColor)>
}

export namespace $BlockColorsAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockColorsAccess$Type = ($BlockColorsAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockColorsAccess_ = $BlockColorsAccess$Type;
}}
declare module "packages/snownee/kiwi/loader/$KiwiConfiguration" {
import {$KiwiAnnotationData, $KiwiAnnotationData$Type} from "packages/snownee/kiwi/$KiwiAnnotationData"
import {$List, $List$Type} from "packages/java/util/$List"

export class $KiwiConfiguration {
 "optionals": $List<($KiwiAnnotationData)>
 "conditions": $List<($KiwiAnnotationData)>
 "modules": $List<($KiwiAnnotationData)>
 "packets": $List<($KiwiAnnotationData)>
 "configs": $List<($KiwiAnnotationData)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiConfiguration$Type = ($KiwiConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiConfiguration_ = $KiwiConfiguration$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SinHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SinHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinHFunction$Type = ($SinHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinHFunction_ = $SinHFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$LayeredComponent" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"

export interface $LayeredComponent {

 "getLayerProperty"(): $IntegerProperty
 "getDefaultLayer"(): integer
}

export namespace $LayeredComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayeredComponent$Type = ($LayeredComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayeredComponent_ = $LayeredComponent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$NumberAnchorGenerator" {
import {$AnchorGenerator, $AnchorGenerator$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $NumberAnchorGenerator implements $AnchorGenerator {

constructor(arg0: integer)

public "nextAnchor"(arg0: $Node$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberAnchorGenerator$Type = ($NumberAnchorGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberAnchorGenerator_ = $NumberAnchorGenerator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/resolver/$ResolverTuple" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $ResolverTuple {

constructor(arg0: $Tag$Type, arg1: $Pattern$Type, arg2: integer)

public "toString"(): string
public "getTag"(): $Tag
public "getLimit"(): integer
public "getRegexp"(): $Pattern
get "tag"(): $Tag
get "limit"(): integer
get "regexp"(): $Pattern
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResolverTuple$Type = ($ResolverTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResolverTuple_ = $ResolverTuple$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$SequenceEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionEndEvent, $CollectionEndEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $SequenceEndEvent extends $CollectionEndEvent {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceEndEvent$Type = ($SequenceEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceEndEvent_ = $SequenceEndEvent$Type;
}}
declare module "packages/snownee/kiwi/mixin/$TagsProviderAccess" {
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$TagBuilder, $TagBuilder$Type} from "packages/net/minecraft/tags/$TagBuilder"

export interface $TagsProviderAccess<T> {

 "callGetOrCreateRawBuilder"(arg0: $TagKey$Type<(T)>): $TagBuilder
 "getModId"(): string
 "getRegistryKey"(): $ResourceKey<(any)>
}

export namespace $TagsProviderAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagsProviderAccess$Type<T> = ($TagsProviderAccess<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagsProviderAccess_<T> = $TagsProviderAccess$Type<(T)>;
}}
declare module "packages/snownee/kiwi/item/$ItemCategoryFiller" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ItemCategoryFiller {

 "fillItemCategory"(arg0: $CreativeModeTab$Type, arg1: $FeatureFlagSet$Type, arg2: boolean, arg3: $List$Type<($ItemStack$Type)>): void

(arg0: $CreativeModeTab$Type, arg1: $FeatureFlagSet$Type, arg2: boolean, arg3: $List$Type<($ItemStack$Type)>): void
}

export namespace $ItemCategoryFiller {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemCategoryFiller$Type = ($ItemCategoryFiller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemCategoryFiller_ = $ItemCategoryFiller$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$AliasEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$NodeEvent, $NodeEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$NodeEvent"

export class $AliasEvent extends $NodeEvent {

constructor(arg0: string, arg1: $Mark$Type, arg2: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AliasEvent$Type = ($AliasEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AliasEvent_ = $AliasEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SinFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SinFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinFunction$Type = ($SinFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinFunction_ = $SinFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixPlusOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixPlusOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixPlusOperator$Type = ($InfixPlusOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixPlusOperator_ = $InfixPlusOperator$Type;
}}
declare module "packages/snownee/kiwi/client/model/$RetextureModel" {
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ItemOverrides, $ItemOverrides$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemOverrides"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$IDynamicBakedModel, $IDynamicBakedModel$Type} from "packages/net/minecraftforge/client/model/$IDynamicBakedModel"
import {$BlockGeometryBakingContext, $BlockGeometryBakingContext$Type} from "packages/net/minecraftforge/client/model/geometry/$BlockGeometryBakingContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ModelProperty, $ModelProperty$Type} from "packages/net/minecraftforge/client/model/data/$ModelProperty"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$ItemTransforms, $ItemTransforms$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemTransforms"
import {$BlockDefinition, $BlockDefinition$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $RetextureModel implements $IDynamicBakedModel {
static "TEXTURES": $ModelProperty<($Map<(string), ($BlockDefinition)>)>

constructor(arg0: $ModelBaker$Type, arg1: $ModelState$Type, arg2: $ResourceLocation$Type, arg3: $BlockGeometryBakingContext$Type, arg4: string, arg5: boolean)

public "usesBlockLight"(): boolean
public "isGui3d"(): boolean
public "getParticleIcon"(): $TextureAtlasSprite
public "getModel"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>): $BakedModel
public static "getColor"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>, arg1: $BlockState$Type, arg2: $BlockAndTintGetter$Type, arg3: $BlockPos$Type, arg4: integer): integer
public "isCustomRenderer"(): boolean
public "getOverrides"(): $ItemOverrides
public "getTransforms"(): $ItemTransforms
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type, arg3: $ModelData$Type, arg4: $RenderType$Type): $List<($BakedQuad)>
public "useAmbientOcclusion"(): boolean
public "getParticleIcon"(arg0: $ModelData$Type): $TextureAtlasSprite
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type): $List<($BakedQuad)>
public "useAmbientOcclusion"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type): boolean
public "getRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "getRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type, arg2: $ModelData$Type): $ChunkRenderTypeSet
public "getRenderPasses"(arg0: $ItemStack$Type, arg1: boolean): $List<($BakedModel)>
public "applyTransform"(arg0: $ItemDisplayContext$Type, arg1: $PoseStack$Type, arg2: boolean): $BakedModel
public "getModelData"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ModelData$Type): $ModelData
public "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
get "gui3d"(): boolean
get "particleIcon"(): $TextureAtlasSprite
get "customRenderer"(): boolean
get "overrides"(): $ItemOverrides
get "transforms"(): $ItemTransforms
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RetextureModel$Type = ($RetextureModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RetextureModel_ = $RetextureModel$Type;
}}
declare module "packages/snownee/kiwi/util/$WrappedBlockGetter" {
import {$ModelDataManager, $ModelDataManager$Type} from "packages/net/minecraftforge/client/model/data/$ModelDataManager"
import {$LevelLightEngine, $LevelLightEngine$Type} from "packages/net/minecraft/world/level/lighting/$LevelLightEngine"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ClipBlockStateContext, $ClipBlockStateContext$Type} from "packages/net/minecraft/world/level/$ClipBlockStateContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ColorResolver, $ColorResolver$Type} from "packages/net/minecraft/world/level/$ColorResolver"
import {$ClipContext, $ClipContext$Type} from "packages/net/minecraft/world/level/$ClipContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $WrappedBlockGetter implements $BlockAndTintGetter {

constructor()

public "setLevel"(arg0: $BlockAndTintGetter$Type): void
public "getBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getFluidState"(arg0: $BlockPos$Type): $FluidState
public "getLightEngine"(): $LevelLightEngine
public "getHeight"(): integer
public "getBlockTint"(arg0: $BlockPos$Type, arg1: $ColorResolver$Type): integer
public "getShade"(arg0: $Direction$Type, arg1: boolean): float
public "getMinBuildHeight"(): integer
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "canSeeSky"(arg0: $BlockPos$Type): boolean
public "getRawBrightness"(arg0: $BlockPos$Type, arg1: integer): integer
public "getBrightness"(arg0: $LightLayer$Type, arg1: $BlockPos$Type): integer
public "getBlockEntity"<T extends $BlockEntity>(arg0: $BlockPos$Type, arg1: $BlockEntityType$Type<(T)>): $Optional<(T)>
public "getBlockStates"(arg0: $AABB$Type): $Stream<($BlockState)>
public "getMaxLightLevel"(): integer
public "isBlockInLine"(arg0: $ClipBlockStateContext$Type): $BlockHitResult
public "clipWithInteractionOverride"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $BlockPos$Type, arg3: $VoxelShape$Type, arg4: $BlockState$Type): $BlockHitResult
public "getLightEmission"(arg0: $BlockPos$Type): integer
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public "getBlockFloorHeight"(arg0: $VoxelShape$Type, arg1: $Supplier$Type<($VoxelShape$Type)>): double
public "getBlockFloorHeight"(arg0: $BlockPos$Type): double
public "clip"(arg0: $ClipContext$Type): $BlockHitResult
public "getShade"(arg0: float, arg1: float, arg2: float, arg3: boolean): float
public "getSectionsCount"(): integer
public "isOutsideBuildHeight"(arg0: integer): boolean
public "getMinSection"(): integer
public "getMaxSection"(): integer
public "getSectionIndexFromSectionY"(arg0: integer): integer
public "getSectionYFromSectionIndex"(arg0: integer): integer
public "getSectionIndex"(arg0: integer): integer
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
public "isOutsideBuildHeight"(arg0: $BlockPos$Type): boolean
public "getMaxBuildHeight"(): integer
public "getExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getModelDataManager"(): $ModelDataManager
set "level"(value: $BlockAndTintGetter$Type)
get "lightEngine"(): $LevelLightEngine
get "height"(): integer
get "minBuildHeight"(): integer
get "maxLightLevel"(): integer
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "maxBuildHeight"(): integer
get "modelDataManager"(): $ModelDataManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrappedBlockGetter$Type = ($WrappedBlockGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrappedBlockGetter_ = $WrappedBlockGetter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$EvaluationValue$DataType, $EvaluationValue$DataType$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue$DataType"
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"
import {$ASTNode, $ASTNode$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ASTNode"
import {$MathContext, $MathContext$Type} from "packages/java/math/$MathContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EvaluationValue implements $Comparable<($EvaluationValue)> {

/**
 * 
 * @deprecated
 */
constructor(arg0: double, arg1: $MathContext$Type)
constructor(arg0: any, arg1: $ExpressionConfiguration$Type)
/**
 * 
 * @deprecated
 */
constructor(arg0: any)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $EvaluationValue$Type): integer
public static "booleanValue"(arg0: boolean): $EvaluationValue
public "getValue"(): any
public static "stringValue"(arg0: string): $EvaluationValue
public static "numberValue"(arg0: $BigDecimal$Type): $EvaluationValue
public static "nullValue"(): $EvaluationValue
public "getBooleanValue"(): boolean
public "getStringValue"(): string
public static "dateTimeValue"(arg0: $Instant$Type): $EvaluationValue
public "getArrayValue"(): $List<($EvaluationValue)>
public "getDataType"(): $EvaluationValue$DataType
public "getDateTimeValue"(): $Instant
public "isDateTimeValue"(): boolean
public static "arrayValue"(arg0: $List$Type<(any)>): $EvaluationValue
public "isExpressionNode"(): boolean
public "isStructureValue"(): boolean
public static "structureValue"(arg0: $Map$Type<(any), (any)>): $EvaluationValue
public "isStringValue"(): boolean
public "isDurationValue"(): boolean
public "getStructureValue"(): $Map<(string), ($EvaluationValue)>
public static "numberOfString"(arg0: string, arg1: $MathContext$Type): $EvaluationValue
public "getDurationValue"(): $Duration
public "isBooleanValue"(): boolean
public "getExpressionNode"(): $ASTNode
public "getNumberValue"(): $BigDecimal
public "isArrayValue"(): boolean
public "isNumberValue"(): boolean
public "isNullValue"(): boolean
public static "expressionNodeValue"(arg0: $ASTNode$Type): $EvaluationValue
public static "durationValue"(arg0: $Duration$Type): $EvaluationValue
get "value"(): any
get "dataType"(): $EvaluationValue$DataType
get "expressionNode"(): boolean
get "expressionNode"(): $ASTNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvaluationValue$Type = ($EvaluationValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvaluationValue_ = $EvaluationValue$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$TagToken" {
import {$TagTuple, $TagTuple$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$TagTuple"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $TagToken extends $Token {

constructor(arg0: $TagTuple$Type, arg1: $Mark$Type, arg2: $Mark$Type)

public "getValue"(): $TagTuple
public "getTokenId"(): $Token$ID
get "value"(): $TagTuple
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagToken$Type = ($TagToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagToken_ = $TagToken$Type;
}}
declare module "packages/snownee/kiwi/config/$ConfigHandler" {
import {$KiwiConfig$ConfigType, $KiwiConfig$ConfigType$Type} from "packages/snownee/kiwi/config/$KiwiConfig$ConfigType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$ConfigHandler$Value, $ConfigHandler$Value$Type} from "packages/snownee/kiwi/config/$ConfigHandler$Value"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigHandler {
static readonly "FILE_EXTENSION": string

constructor(arg0: string, arg1: string, arg2: $KiwiConfig$ConfigType$Type, arg3: $Class$Type<(any)>, arg4: boolean)

public "get"<T>(arg0: string): $ConfigHandler$Value<(T)>
public "init"(): void
public "save"(): void
public "getType"(): $KiwiConfig$ConfigType
public "getFileName"(): string
public "refresh"(): void
public "getClazz"(): $Class<(any)>
public "hasModules"(): boolean
public "setHasModules"(arg0: boolean): void
public "getValueMap"(): $Map<(string), ($ConfigHandler$Value<(any)>)>
public "define"<T>(arg0: string, arg1: T, arg2: $Field$Type, arg3: string): $ConfigHandler$Value<(T)>
public "getTranslationKey"(): string
public "getModId"(): string
get "type"(): $KiwiConfig$ConfigType
get "fileName"(): string
get "clazz"(): $Class<(any)>
get "valueMap"(): $Map<(string), ($ConfigHandler$Value<(any)>)>
get "translationKey"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHandler$Type = ($ConfigHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHandler_ = $ConfigHandler$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export interface $OperatorIfc {

 "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
 "getPrecedence"(arg0: $ExpressionConfiguration$Type): integer
 "getPrecedence"(): integer
 "isLeftAssociative"(): boolean
 "isOperandLazy"(): boolean
 "isInfix"(): boolean
 "isPrefix"(): boolean
 "isPostfix"(): boolean
}

export namespace $OperatorIfc {
const OPERATOR_PRECEDENCE_OR: integer
const OPERATOR_PRECEDENCE_AND: integer
const OPERATOR_PRECEDENCE_EQUALITY: integer
const OPERATOR_PRECEDENCE_COMPARISON: integer
const OPERATOR_PRECEDENCE_ADDITIVE: integer
const OPERATOR_PRECEDENCE_MULTIPLICATIVE: integer
const OPERATOR_PRECEDENCE_POWER: integer
const OPERATOR_PRECEDENCE_UNARY: integer
const OPERATOR_PRECEDENCE_POWER_HIGHER: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OperatorIfc$Type = ($OperatorIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OperatorIfc_ = $OperatorIfc$Type;
}}
declare module "packages/snownee/kiwi/customization/duck/$KBlockProperties" {
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"

export interface $KBlockProperties {

 "kiwi$setSettings"(arg0: $KBlockSettings$Type): void
 "kiwi$getSettings"(): $KBlockSettings
}

export namespace $KBlockProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockProperties$Type = ($KBlockProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockProperties_ = $KBlockProperties$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider" {
import {$PlaceTarget, $PlaceTarget$Type} from "packages/snownee/kiwi/customization/placement/$PlaceTarget"
import {$PlaceSlotProvider$Slot, $PlaceSlotProvider$Slot$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Slot"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceSlotProvider extends $Record {
static readonly "TAG_PATTERN": $Predicate<(string)>
static readonly "TAG_CODEC": $Codec<(string)>
static readonly "CODEC": $Codec<($PlaceSlotProvider)>

constructor(target: $List$Type<($PlaceTarget$Type)>, transformWith: $Optional$Type<(string)>, tag: $List$Type<(string)>, slots: $List$Type<($PlaceSlotProvider$Slot$Type)>)

public "equals"(arg0: any): boolean
public "target"(): $List<($PlaceTarget)>
public "toString"(): string
public "hashCode"(): integer
public "slots"(): $List<($PlaceSlotProvider$Slot)>
public "tag"(): $List<(string)>
public "transformWith"(): $Optional<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceSlotProvider$Type = ($PlaceSlotProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceSlotProvider_ = $PlaceSlotProvider$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$MappingNode" {
import {$NodeTuple, $NodeTuple$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeTuple"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$CollectionNode, $CollectionNode$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $MappingNode extends $CollectionNode<($NodeTuple)> {

constructor(arg0: $Tag$Type, arg1: boolean, arg2: $List$Type<($NodeTuple$Type)>, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$FlowStyle$Type)
constructor(arg0: $Tag$Type, arg1: $List$Type<($NodeTuple$Type)>, arg2: $DumperOptions$FlowStyle$Type)

public "toString"(): string
public "getValue"(): $List<($NodeTuple)>
public "setValue"(arg0: $List$Type<($NodeTuple$Type)>): void
public "setMerged"(arg0: boolean): void
public "getNodeId"(): $NodeId
public "setOnlyKeyType"(arg0: $Class$Type<(any)>): void
public "setTypes"(arg0: $Class$Type<(any)>, arg1: $Class$Type<(any)>): void
public "isMerged"(): boolean
get "value"(): $List<($NodeTuple)>
set "value"(value: $List$Type<($NodeTuple$Type)>)
set "merged"(value: boolean)
get "nodeId"(): $NodeId
set "onlyKeyType"(value: $Class$Type<(any)>)
get "merged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingNode$Type = ($MappingNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingNode_ = $MappingNode$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$KItemDefinition" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$ConfiguredItemTemplate, $ConfiguredItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$ConfiguredItemTemplate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KItemSettings$Builder, $KItemSettings$Builder$Type} from "packages/snownee/kiwi/customization/item/$KItemSettings$Builder"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KItemDefinition extends $Record {

constructor(template: $ConfiguredItemTemplate$Type, properties: $ItemDefinitionProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "properties"(): $ItemDefinitionProperties
public "template"(): $ConfiguredItemTemplate
public "createSettings"(arg0: $ResourceLocation$Type): $KItemSettings$Builder
public static "codec"(arg0: $Map$Type<($ResourceLocation$Type), ($KItemTemplate$Type)>): $Codec<($KItemDefinition)>
public "createItem"(arg0: $ResourceLocation$Type): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemDefinition$Type = ($KItemDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemDefinition_ = $KItemDefinition$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$FlowMappingStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $FlowMappingStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowMappingStartToken$Type = ($FlowMappingStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowMappingStartToken_ = $FlowMappingStartToken$Type;
}}
declare module "packages/snownee/kiwi/network/$KiwiPacket" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$KiwiPacket$Direction, $KiwiPacket$Direction$Type} from "packages/snownee/kiwi/network/$KiwiPacket$Direction"

export interface $KiwiPacket extends $Annotation {

 "value"(): string
 "dir"(): $KiwiPacket$Direction
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $KiwiPacket {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiPacket$Type = ($KiwiPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiPacket_ = $KiwiPacket$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Slot" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$StatePropertiesPredicate, $StatePropertiesPredicate$Type} from "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$PlaceSlotProvider$Side, $PlaceSlotProvider$Side$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Side"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlaceSlotProvider$Slot extends $Record {
static readonly "CODEC": $Codec<($PlaceSlotProvider$Slot)>

constructor(when: $List$Type<($StatePropertiesPredicate$Type)>, transformWith: $Optional$Type<(string)>, tag: $List$Type<(string)>, sides: $Map$Type<($Direction$Type), ($PlaceSlotProvider$Side$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "tag"(): $List<(string)>
public "when"(): $List<($StatePropertiesPredicate)>
public "transformWith"(): $Optional<(string)>
public "sides"(): $Map<($Direction), ($PlaceSlotProvider$Side)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceSlotProvider$Slot$Type = ($PlaceSlotProvider$Slot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceSlotProvider$Slot_ = $PlaceSlotProvider$Slot$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$EmitterState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EmitterState {

 "expect"(): void

(): void
}

export namespace $EmitterState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmitterState$Type = ($EmitterState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmitterState_ = $EmitterState$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$SlotLink$ResultAction" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SlotLink$ResultAction extends $Record {
static readonly "MAP_CODEC": $MapCodec<($SlotLink$ResultAction)>
static readonly "CODEC": $Codec<($SlotLink$ResultAction)>

constructor(setProperties: $Map$Type<(string), (string)>, reflow: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "apply"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $BlockState
public "setProperties"(): $Map<(string), (string)>
public "reflow"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotLink$ResultAction$Type = ($SlotLink$ResultAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotLink$ResultAction_ = $SlotLink$ResultAction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeTuple" {
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $NodeTuple {

constructor(arg0: $Node$Type, arg1: $Node$Type)

public "toString"(): string
public "getKeyNode"(): $Node
public "getValueNode"(): $Node
get "keyNode"(): $Node
get "valueNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeTuple$Type = ($NodeTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeTuple_ = $NodeTuple$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$FactFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $FactFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FactFunction$Type = ($FactFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FactFunction_ = $FactFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$ParsedProtoTag" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ParsedProtoTag extends $Record {
static readonly "CODEC": $Codec<($ParsedProtoTag)>

constructor(prefix: string, key: string, value: string)

public "value"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: string): $ParsedProtoTag
public "prefix"(): string
public "resolve"(arg0: $BlockState$Type, arg1: $Rotation$Type): $ParsedProtoTag
public "resolve"(arg0: $BlockState$Type): $ParsedProtoTag
public "key"(): string
public "isResolved"(): boolean
public "prefixedKey"(): string
get "resolved"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParsedProtoTag$Type = ($ParsedProtoTag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParsedProtoTag_ = $ParsedProtoTag$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/biz/base64Coder/$Base64Coder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Base64Coder {


public static "encodeString"(arg0: string): string
public static "decode"(arg0: (character)[]): (byte)[]
public static "decode"(arg0: (character)[], arg1: integer, arg2: integer): (byte)[]
public static "decode"(arg0: string): (byte)[]
public static "encode"(arg0: (byte)[]): (character)[]
public static "encode"(arg0: (byte)[], arg1: integer): (character)[]
public static "encode"(arg0: (byte)[], arg1: integer, arg2: integer): (character)[]
public static "decodeLines"(arg0: string): (byte)[]
public static "encodeLines"(arg0: (byte)[], arg1: integer, arg2: integer, arg3: integer, arg4: string): string
public static "encodeLines"(arg0: (byte)[]): string
public static "decodeString"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Base64Coder$Type = ($Base64Coder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Base64Coder_ = $Base64Coder$Type;
}}
declare module "packages/snownee/kiwi/loader/$Initializer" {
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $Initializer {

constructor()

public static "globalTooltip"(arg0: $ItemTooltipEvent$Type): void
public static "debugTooltip"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Initializer$Type = ($Initializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Initializer_ = $Initializer$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$ConsumableComponent" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$LayeredComponent, $LayeredComponent$Type} from "packages/snownee/kiwi/customization/block/component/$LayeredComponent"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$FoodProperties, $FoodProperties$Type} from "packages/net/minecraft/world/food/$FoodProperties"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $ConsumableComponent extends $Record implements $KBlockComponent, $LayeredComponent {
static readonly "CODEC": $Codec<($ConsumableComponent)>

constructor(property: $IntegerProperty$Type, food: $Optional$Type<($FoodProperties$Type)>, stat: $Optional$Type<($ResourceKey$Type<($ResourceLocation$Type)>)>)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: integer, arg1: integer, arg2: $Optional$Type<($FoodProperties$Type)>, arg3: $Optional$Type<($ResourceKey$Type<($ResourceLocation$Type)>)>): $ConsumableComponent
public "maxValue"(): integer
public "stat"(): $Optional<($ResourceKey<($ResourceLocation)>)>
public "property"(): $IntegerProperty
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "minValue"(): integer
public "food"(): $Optional<($FoodProperties)>
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "getLayerProperty"(): $IntegerProperty
public "getDefaultLayer"(): integer
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
get "layerProperty"(): $IntegerProperty
get "defaultLayer"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConsumableComponent$Type = ($ConsumableComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConsumableComponent_ = $ConsumableComponent$Type;
}}
declare module "packages/snownee/kiwi/loader/$AnnotatedTypeLoader" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$KiwiConfiguration, $KiwiConfiguration$Type} from "packages/snownee/kiwi/loader/$KiwiConfiguration"

export class $AnnotatedTypeLoader implements $Supplier<($KiwiConfiguration)> {
readonly "modId": string

constructor(arg0: string)

public "get"(): $KiwiConfiguration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedTypeLoader$Type = ($AnnotatedTypeLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedTypeLoader_ = $AnnotatedTypeLoader$Type;
}}
declare module "packages/snownee/kiwi/util/$ColorProviderUtil" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ColorProviderUtil {

constructor()

public static "delegate"(arg0: $Item$Type): $ItemColor
public static "delegate"(arg0: $Block$Type): $BlockColor
public static "delegateItemFallback"(arg0: $Block$Type): $ItemColor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorProviderUtil$Type = ($ColorProviderUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorProviderUtil_ = $ColorProviderUtil$Type;
}}
declare module "packages/snownee/kiwi/contributor/network/$CSetCosmeticPacket" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $CSetCosmeticPacket extends $PacketHandler {
static "I": $CSetCosmeticPacket
 "id": $ResourceLocation

constructor()

public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
public static "send"(arg0: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CSetCosmeticPacket$Type = ($CSetCosmeticPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CSetCosmeticPacket_ = $CSetCosmeticPacket$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CosFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CosFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosFunction$Type = ($CosFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosFunction_ = $CosFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomizationMetadata" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ImmutableListMultimap, $ImmutableListMultimap$Type} from "packages/com/google/common/collect/$ImmutableListMultimap"
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CustomizationMetadata extends $Record {
static readonly "CODEC": $Codec<($CustomizationMetadata)>

constructor(registryOrder: $ImmutableListMultimap$Type<(string), (string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $Map$Type<(string), ($List$Type<(string)>)>): $CustomizationMetadata
public static "loadMap"(arg0: $ResourceManager$Type, arg1: $OneTimeLoader$Context$Type): $Map<(string), ($CustomizationMetadata)>
public static "sortedForEach"<T>(arg0: $Map$Type<(string), ($CustomizationMetadata$Type)>, arg1: string, arg2: $Map$Type<($ResourceLocation$Type), (T)>, arg3: $BiConsumer$Type<($ResourceLocation$Type), (T)>): void
public "registryOrder"(): $ImmutableListMultimap<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationMetadata$Type = ($CustomizationMetadata);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationMetadata_ = $CustomizationMetadata$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$UnbakedShapeCodec" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $UnbakedShapeCodec implements $Codec<($UnbakedShape)> {

constructor()

public "directDecode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $UnbakedShape
public static "encodeVoxelShape"(arg0: $VoxelShape$Type): string
public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<($UnbakedShape), (T)>)>
public "encode"<T>(arg0: $UnbakedShape$Type, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<($UnbakedShape$Type)>, arg1: $MapDecoder$Type<($UnbakedShape$Type)>, arg2: $Supplier$Type<(string)>): $MapCodec<($UnbakedShape)>
public static "of"<A>(arg0: $MapEncoder$Type<($UnbakedShape$Type)>, arg1: $MapDecoder$Type<($UnbakedShape$Type)>): $MapCodec<($UnbakedShape)>
public static "of"<A>(arg0: $Encoder$Type<($UnbakedShape$Type)>, arg1: $Decoder$Type<($UnbakedShape$Type)>, arg2: string): $Codec<($UnbakedShape)>
public static "of"<A>(arg0: $Encoder$Type<($UnbakedShape$Type)>, arg1: $Decoder$Type<($UnbakedShape$Type)>): $Codec<($UnbakedShape)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: $UnbakedShape$Type): $Codec<($UnbakedShape)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: $UnbakedShape$Type): $Codec<($UnbakedShape)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: $UnbakedShape$Type): $Codec<($UnbakedShape)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: $UnbakedShape$Type): $Codec<($UnbakedShape)>
public static "unit"<A>(arg0: $Supplier$Type<($UnbakedShape$Type)>): $Codec<($UnbakedShape)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<($UnbakedShape)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($UnbakedShape)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($UnbakedShape)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<($UnbakedShape)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<($UnbakedShape)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<($UnbakedShape)>)>
public "optionalFieldOf"(arg0: string, arg1: $UnbakedShape$Type, arg2: $Lifecycle$Type): $MapCodec<($UnbakedShape)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: $UnbakedShape$Type, arg3: $Lifecycle$Type): $MapCodec<($UnbakedShape)>
public "optionalFieldOf"(arg0: string, arg1: $UnbakedShape$Type): $MapCodec<($UnbakedShape)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<($UnbakedShape$Type)>): $Codec<($UnbakedShape)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<($UnbakedShape)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<($UnbakedShape)>)>
public "stable"(): $Codec<($UnbakedShape)>
public static "empty"<A>(): $MapEncoder<($UnbakedShape)>
public static "error"<A>(arg0: string): $Encoder<($UnbakedShape)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $UnbakedShape$Type): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<($UnbakedShape), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($UnbakedShape)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($UnbakedShape)>
public "boxed"(): $Decoder$Boxed<($UnbakedShape)>
public "simple"(): $Decoder$Simple<($UnbakedShape)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<($UnbakedShape)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<($UnbakedShape)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<($UnbakedShape)>
public "terminal"(): $Decoder$Terminal<($UnbakedShape)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnbakedShapeCodec$Type = ($UnbakedShapeCodec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnbakedShapeCodec_ = $UnbakedShapeCodec$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$PrefixPlusOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $PrefixPlusOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixPlusOperator$Type = ($PrefixPlusOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixPlusOperator_ = $PrefixPlusOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$NotFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $NotFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotFunction$Type = ($NotFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotFunction_ = $NotFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AtanRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AtanRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtanRFunction$Type = ($AtanRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtanRFunction_ = $AtanRFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$FloorFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $FloorFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloorFunction$Type = ($FloorFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloorFunction_ = $FloorFunction$Type;
}}
declare module "packages/snownee/kiwi/block/entity/$RetextureBlockEntity" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Predicate, $Predicate$Type} from "packages/com/google/common/base/$Predicate"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$ModBlockEntity, $ModBlockEntity$Type} from "packages/snownee/kiwi/block/entity/$ModBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockDefinition, $BlockDefinition$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RetextureBlockEntity extends $ModBlockEntity {
 "persistData": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type, ...arg3: (string)[])

public "isValidTexture"(arg0: $BlockDefinition$Type): boolean
public "refresh"(): void
public "onLoad"(): void
public static "setTexture"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>, arg1: string, arg2: $Item$Type): void
public static "setTexture"(arg0: $Map$Type<(string), (string)>, arg1: string, arg2: string): void
public "setTexture"(arg0: string, arg1: $BlockDefinition$Type): void
public static "setTexture"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>, arg1: string, arg2: $BlockDefinition$Type): void
public "getColor"(arg0: $BlockAndTintGetter$Type, arg1: integer): integer
public static "writeTextures"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>, arg1: $CompoundTag$Type): $CompoundTag
public "requestModelDataUpdate"(): void
public static "readTextures"(arg0: $Map$Type<(string), ($BlockDefinition$Type)>, arg1: $CompoundTag$Type, arg2: $Predicate$Type<($BlockDefinition$Type)>): boolean
public "getModelData"(): $ModelData
get "modelData"(): $ModelData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RetextureBlockEntity$Type = ($RetextureBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RetextureBlockEntity_ = $RetextureBlockEntity$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$AnchorGenerator, $AnchorGenerator$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$DumperOptions$NonPrintableStyle, $DumperOptions$NonPrintableStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$NonPrintableStyle"
import {$DumperOptions$LineBreak, $DumperOptions$LineBreak$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$LineBreak"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DumperOptions {

constructor()

public "setTimeZone"(arg0: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "getVersion"(): $DumperOptions$Version
public "setVersion"(arg0: $DumperOptions$Version$Type): void
public "setWidth"(arg0: integer): void
public "setIndent"(arg0: integer): void
public "setProcessComments"(arg0: boolean): void
public "isProcessComments"(): boolean
public "setSplitLines"(arg0: boolean): void
public "setCanonical"(arg0: boolean): void
public "setAllowUnicode"(arg0: boolean): void
public "setIndicatorIndent"(arg0: integer): void
public "isAllowUnicode"(): boolean
public "isCanonical"(): boolean
public "setPrettyFlow"(arg0: boolean): void
public "isPrettyFlow"(): boolean
public "isExplicitStart"(): boolean
public "getAnchorGenerator"(): $AnchorGenerator
public "getLineBreak"(): $DumperOptions$LineBreak
public "setLineBreak"(arg0: $DumperOptions$LineBreak$Type): void
public "isExplicitEnd"(): boolean
public "setAnchorGenerator"(arg0: $AnchorGenerator$Type): void
public "setTags"(arg0: $Map$Type<(string), (string)>): void
public "setExplicitStart"(arg0: boolean): void
public "getSplitLines"(): boolean
public "setExplicitEnd"(arg0: boolean): void
public "getWidth"(): integer
public "getIndicatorIndent"(): integer
public "setDefaultScalarStyle"(arg0: $DumperOptions$ScalarStyle$Type): void
public "isAllowReadOnlyProperties"(): boolean
public "getDefaultFlowStyle"(): $DumperOptions$FlowStyle
public "getIndentWithIndicator"(): boolean
public "getDefaultScalarStyle"(): $DumperOptions$ScalarStyle
public "setAllowReadOnlyProperties"(arg0: boolean): void
public "setDefaultFlowStyle"(arg0: $DumperOptions$FlowStyle$Type): void
public "getIndent"(): integer
public "getTags"(): $Map<(string), (string)>
public "setNonPrintableStyle"(arg0: $DumperOptions$NonPrintableStyle$Type): void
public "setIndentWithIndicator"(arg0: boolean): void
public "getMaxSimpleKeyLength"(): integer
public "setMaxSimpleKeyLength"(arg0: integer): void
public "getNonPrintableStyle"(): $DumperOptions$NonPrintableStyle
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
get "version"(): $DumperOptions$Version
set "version"(value: $DumperOptions$Version$Type)
set "width"(value: integer)
set "indent"(value: integer)
set "processComments"(value: boolean)
get "processComments"(): boolean
set "splitLines"(value: boolean)
set "canonical"(value: boolean)
set "allowUnicode"(value: boolean)
set "indicatorIndent"(value: integer)
get "allowUnicode"(): boolean
get "canonical"(): boolean
set "prettyFlow"(value: boolean)
get "prettyFlow"(): boolean
get "explicitStart"(): boolean
get "anchorGenerator"(): $AnchorGenerator
get "lineBreak"(): $DumperOptions$LineBreak
set "lineBreak"(value: $DumperOptions$LineBreak$Type)
get "explicitEnd"(): boolean
set "anchorGenerator"(value: $AnchorGenerator$Type)
set "tags"(value: $Map$Type<(string), (string)>)
set "explicitStart"(value: boolean)
get "splitLines"(): boolean
set "explicitEnd"(value: boolean)
get "width"(): integer
get "indicatorIndent"(): integer
set "defaultScalarStyle"(value: $DumperOptions$ScalarStyle$Type)
get "allowReadOnlyProperties"(): boolean
get "defaultFlowStyle"(): $DumperOptions$FlowStyle
get "indentWithIndicator"(): boolean
get "defaultScalarStyle"(): $DumperOptions$ScalarStyle
set "allowReadOnlyProperties"(value: boolean)
set "defaultFlowStyle"(value: $DumperOptions$FlowStyle$Type)
get "indent"(): integer
get "tags"(): $Map<(string), (string)>
set "nonPrintableStyle"(value: $DumperOptions$NonPrintableStyle$Type)
set "indentWithIndicator"(value: boolean)
get "maxSimpleKeyLength"(): integer
set "maxSimpleKeyLength"(value: integer)
get "nonPrintableStyle"(): $DumperOptions$NonPrintableStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$Type = ($DumperOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions_ = $DumperOptions$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$ParserException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ParserException extends $MarkedYAMLException {

constructor(arg0: string, arg1: $Mark$Type, arg2: string, arg3: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserException$Type = ($ParserException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserException_ = $ParserException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$SerializerException" {
import {$YAMLException, $YAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $SerializerException extends $YAMLException {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializerException$Type = ($SerializerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializerException_ = $SerializerException$Type;
}}
declare module "packages/snownee/kiwi/util/codec/$CustomizationCodecs" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$WoodType, $WoodType$Type} from "packages/net/minecraft/world/level/block/state/properties/$WoodType"
import {$MapColor, $MapColor$Type} from "packages/net/minecraft/world/level/material/$MapColor"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PushReaction, $PushReaction$Type} from "packages/net/minecraft/world/level/material/$PushReaction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$FoodProperties, $FoodProperties$Type} from "packages/net/minecraft/world/food/$FoodProperties"
import {$GlassType, $GlassType$Type} from "packages/snownee/kiwi/customization/block/$GlassType"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ArmorItem$Type, $ArmorItem$Type$Type} from "packages/net/minecraft/world/item/$ArmorItem$Type"
import {$BlockBehaviour$StateArgumentPredicate, $BlockBehaviour$StateArgumentPredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StateArgumentPredicate"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$BlockBehaviour$OffsetType, $BlockBehaviour$OffsetType$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$OffsetType"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$NoteBlockInstrument, $NoteBlockInstrument$Type} from "packages/net/minecraft/world/level/block/state/properties/$NoteBlockInstrument"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$PressurePlateBlock$Sensitivity, $PressurePlateBlock$Sensitivity$Type} from "packages/net/minecraft/world/level/block/$PressurePlateBlock$Sensitivity"
import {$KiwiModule$RenderLayer$Layer, $KiwiModule$RenderLayer$Layer$Type} from "packages/snownee/kiwi/$KiwiModule$RenderLayer$Layer"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockBehaviour$StatePredicate, $BlockBehaviour$StatePredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StatePredicate"
import {$WeatheringCopper$WeatherState, $WeatheringCopper$WeatherState$Type} from "packages/net/minecraft/world/level/block/$WeatheringCopper$WeatherState"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockSetType, $BlockSetType$Type} from "packages/net/minecraft/world/level/block/state/properties/$BlockSetType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CustomizationCodecs {
static readonly "SOUND_TYPES": $BiMap<($ResourceLocation), ($SoundType)>
static readonly "SOUND_TYPE_CODEC": $Codec<($SoundType)>
static readonly "INSTRUMENTS": $BiMap<(string), ($NoteBlockInstrument)>
static readonly "INSTRUMENT_CODEC": $Codec<($NoteBlockInstrument)>
static readonly "MAP_COLORS": $BiMap<(string), ($MapColor)>
static readonly "MAP_COLOR_CODEC": $Codec<($MapColor)>
static readonly "GLASS_TYPES": $BiMap<($ResourceLocation), ($GlassType)>
static readonly "GLASS_TYPE_CODEC": $Codec<($GlassType)>
static readonly "PUSH_REACTION": $Codec<($PushReaction)>
static readonly "RENDER_TYPE": $Codec<($KiwiModule$RenderLayer$Layer)>
static readonly "OFFSET_TYPE": $Codec<($BlockBehaviour$OffsetType)>
static readonly "STATE_PREDICATE": $Codec<($BlockBehaviour$StatePredicate)>
static readonly "DIRECTION": $Codec<($Direction)>
static readonly "INT_BOUNDS": $Codec<($MinMaxBounds$Ints)>
static readonly "BLOCK_PREDICATE": $Codec<($BlockPredicate)>
static readonly "BLOCK_SET_TYPE": $Codec<($BlockSetType)>
static readonly "WOOD_TYPE": $Codec<($WoodType)>
static readonly "SENSITIVITIES": $BiMap<(string), ($PressurePlateBlock$Sensitivity)>
static readonly "SENSITIVITY_CODEC": $Codec<($PressurePlateBlock$Sensitivity)>
static readonly "WEATHER_STATE": $Codec<($WeatheringCopper$WeatherState)>
static readonly "MOB_EFFECT_INSTANCE": $Codec<($MobEffectInstance)>
static readonly "POSSIBLE_EFFECT": $Codec<($Pair<($MobEffectInstance), (float)>)>
static readonly "FOOD": $Codec<($FoodProperties)>
static readonly "RARITIES": $BiMap<(string), ($Rarity)>
static readonly "RARITY_CODEC": $Codec<($Rarity)>
static readonly "ARMOR_TYPE": $Codec<($ArmorItem$Type)>
static readonly "CUSTOM_ARMOR_MATERIALS": $BiMap<($ResourceLocation), ($ArmorMaterial)>
static readonly "ARMOR_MATERIAL": $Codec<($ArmorMaterial)>

constructor()

public static "strictOptionalField"<A>(arg0: $Codec$Type<(A)>, arg1: string): $MapCodec<($Optional<(A)>)>
public static "strictOptionalField"<A>(arg0: $Codec$Type<(A)>, arg1: string, arg2: A): $MapCodec<(A)>
public static "stateArgumentPredicate"<T>(): $Codec<($BlockBehaviour$StateArgumentPredicate<(T)>)>
public static "never"<T>(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: T): boolean
public static "always"<T>(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: T): boolean
public static "simpleByNameCodec"<K, V>(arg0: $BiMap$Type<(K), (V)>, arg1: $Codec$Type<(K)>): $Codec<(V)>
public static "simpleByNameCodec"<T>(arg0: $Map$Type<($ResourceLocation$Type), (T)>): $Codec<(T)>
public static "simpleByNameCodec"<T>(arg0: $BiMap$Type<(string), (T)>): $Codec<(T)>
public static "compactList"<T>(arg0: $Codec$Type<(T)>): $Codec<($List<(T)>)>
public static "withAlternative"<T>(arg0: $Codec$Type<(T)>, arg1: $Codec$Type<(any)>): $Codec<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationCodecs$Type = ($CustomizationCodecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationCodecs_ = $CustomizationCodecs$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcotRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcotRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcotRFunction$Type = ($AcotRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcotRFunction_ = $AcotRFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$BlockCodecs" {
import {$WitherRoseBlock, $WitherRoseBlock$Type} from "packages/net/minecraft/world/level/block/$WitherRoseBlock"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$FenceGateBlock, $FenceGateBlock$Type} from "packages/net/minecraft/world/level/block/$FenceGateBlock"
import {$WeatheringCopperStairBlock, $WeatheringCopperStairBlock$Type} from "packages/net/minecraft/world/level/block/$WeatheringCopperStairBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$TrapDoorBlock, $TrapDoorBlock$Type} from "packages/net/minecraft/world/level/block/$TrapDoorBlock"
import {$WeatheringCopperSlabBlock, $WeatheringCopperSlabBlock$Type} from "packages/net/minecraft/world/level/block/$WeatheringCopperSlabBlock"
import {$PressurePlateBlock, $PressurePlateBlock$Type} from "packages/net/minecraft/world/level/block/$PressurePlateBlock"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BedBlock, $BedBlock$Type} from "packages/net/minecraft/world/level/block/$BedBlock"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DropExperienceBlock, $DropExperienceBlock$Type} from "packages/net/minecraft/world/level/block/$DropExperienceBlock"
import {$ButtonBlock, $ButtonBlock$Type} from "packages/net/minecraft/world/level/block/$ButtonBlock"
import {$DoorBlock, $DoorBlock$Type} from "packages/net/minecraft/world/level/block/$DoorBlock"
import {$WeatheringCopperFullBlock, $WeatheringCopperFullBlock$Type} from "packages/net/minecraft/world/level/block/$WeatheringCopperFullBlock"
import {$StairBlock, $StairBlock$Type} from "packages/net/minecraft/world/level/block/$StairBlock"
import {$SandBlock, $SandBlock$Type} from "packages/net/minecraft/world/level/block/$SandBlock"
import {$MushroomBlock, $MushroomBlock$Type} from "packages/net/minecraft/world/level/block/$MushroomBlock"
import {$BrushableBlock, $BrushableBlock$Type} from "packages/net/minecraft/world/level/block/$BrushableBlock"
import {$FlowerPotBlock, $FlowerPotBlock$Type} from "packages/net/minecraft/world/level/block/$FlowerPotBlock"
import {$FlowerBlock, $FlowerBlock$Type} from "packages/net/minecraft/world/level/block/$FlowerBlock"

export class $BlockCodecs {
static readonly "BLOCK_PROPERTIES_KEY": string
static readonly "SIMPLE_BLOCK_FACTORY": $Function<($BlockBehaviour$Properties), ($Block)>
static readonly "BLOCK": $MapCodec<($Block)>
static readonly "STAIR": $MapCodec<($StairBlock)>
static readonly "DOOR": $MapCodec<($DoorBlock)>
static readonly "TRAPDOOR": $MapCodec<($TrapDoorBlock)>
static readonly "FENCE_GATE": $MapCodec<($FenceGateBlock)>
static readonly "SAND": $MapCodec<($SandBlock)>
static readonly "DROP_EXPERIENCE": $MapCodec<($DropExperienceBlock)>
static readonly "MUSHROOM": $MapCodec<($MushroomBlock)>
static readonly "FLOWER": $MapCodec<($FlowerBlock)>
static readonly "FLOWER_POT": $MapCodec<($FlowerPotBlock)>
static readonly "WITHER_ROSE": $MapCodec<($WitherRoseBlock)>
static readonly "BUTTON": $MapCodec<($ButtonBlock)>
static readonly "PRESSURE_PLATE": $MapCodec<($PressurePlateBlock)>
static readonly "WEATHERING_COPPER_FULL": $MapCodec<($WeatheringCopperFullBlock)>
static readonly "WEATHERING_COPPER_SLAB": $MapCodec<($WeatheringCopperSlabBlock)>
static readonly "WEATHERING_COPPER_STAIR": $MapCodec<($WeatheringCopperStairBlock)>
static readonly "BED": $MapCodec<($BedBlock)>
static readonly "BRUSHABLE": $MapCodec<($BrushableBlock)>

constructor()

public static "notImplemented"<O, A>(arg0: O): A
public static "get"(arg0: $ResourceLocation$Type): $MapCodec<($Block)>
public static "register"(arg0: $ResourceLocation$Type, arg1: $MapCodec$Type<(any)>): void
public static "propertiesCodec"<B extends $Block>(): $RecordCodecBuilder<(B), ($BlockBehaviour$Properties)>
public static "simpleCodec"<B extends $Block>(arg0: $Function$Type<($BlockBehaviour$Properties$Type), (B)>): $MapCodec<(B)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCodecs$Type = ($BlockCodecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCodecs_ = $BlockCodecs$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$ReplaceBuilderRule" {
import {$BuilderRule, $BuilderRule$Type} from "packages/snownee/kiwi/customization/builder/$BuilderRule"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockSpread, $BlockSpread$Type} from "packages/snownee/kiwi/customization/builder/$BlockSpread"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockFamily, $BlockFamily$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ReplaceBuilderRule extends $Record implements $BuilderRule {

constructor(families: $Map$Type<($BlockFamily$Type), (any)>, spread: $BlockSpread$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "matches"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $BlockState$Type): boolean
public "apply"(arg0: $UseOnContext$Type, arg1: $List$Type<($BlockPos$Type)>): void
public "spread"(): $BlockSpread
public "relatedBlocks"(): $Stream<($Block)>
public "families"(): $Map<($BlockFamily), (any)>
public "searchPositions"(arg0: $UseOnContext$Type): $List<($BlockPos)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplaceBuilderRule$Type = ($ReplaceBuilderRule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplaceBuilderRule_ = $ReplaceBuilderRule$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AtanFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AtanFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtanFunction$Type = ($AtanFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtanFunction_ = $AtanFunction$Type;
}}
declare module "packages/snownee/kiwi/$KiwiCommonConfig" {
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KiwiCommonConfig {
static "vars": $Map<(string), (any)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiCommonConfig$Type = ($KiwiCommonConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiCommonConfig_ = $KiwiCommonConfig$Type;
}}
declare module "packages/snownee/kiwi/mixin/$FireBlockAccess" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $FireBlockAccess {

 "callSetFlammable"(arg0: $Block$Type, arg1: integer, arg2: integer): void

(arg0: $Block$Type, arg1: integer, arg2: integer): void
}

export namespace $FireBlockAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireBlockAccess$Type = ($FireBlockAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireBlockAccess_ = $FireBlockAccess$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlacementSystem" {
import {$PlaceMatchResult, $PlaceMatchResult$Type} from "packages/snownee/kiwi/customization/placement/$PlaceMatchResult"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$PlaceSlot, $PlaceSlot$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlot"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlacementSystem {

constructor()

public static "isDebugEnabled"(arg0: $Player$Type): boolean
public static "onBlockPlaced"(arg0: $BlockPlaceContext$Type): void
public static "onPlace"(arg0: $BlockItem$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public static "removeDebugBlocks"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "onBlockRemoved"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type): void
public static "getPlaceMatchResultAt"(arg0: $BlockState$Type, arg1: $Map$Type<($Direction$Type), ($Collection$Type<($PlaceSlot$Type)>)>, arg2: integer): $PlaceMatchResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlacementSystem$Type = ($PlacementSystem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlacementSystem_ = $PlacementSystem$Type;
}}
declare module "packages/snownee/kiwi/$KiwiModule" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $KiwiModule extends $Annotation {

 "value"(): string
 "dependencies"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $KiwiModule {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiModule$Type = ($KiwiModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiModule_ = $KiwiModule$Type;
}}
declare module "packages/snownee/kiwi/customization/network/$CApplyBuilderRulePacket" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$BuilderRule, $BuilderRule$Type} from "packages/snownee/kiwi/customization/builder/$BuilderRule"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CApplyBuilderRulePacket extends $PacketHandler {
static "I": $CApplyBuilderRulePacket
 "id": $ResourceLocation

constructor()

public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
public static "send"(arg0: $UseOnContext$Type, arg1: $KHolder$Type<($BuilderRule$Type)>, arg2: $List$Type<($BlockPos$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CApplyBuilderRulePacket$Type = ($CApplyBuilderRulePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CApplyBuilderRulePacket_ = $CApplyBuilderRulePacket$Type;
}}
declare module "packages/snownee/kiwi/customization/block/family/$BlockFamilies" {
import {$BlockFamily, $BlockFamily$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $BlockFamilies {

constructor()

public static "findQuickSwitch"(arg0: $Item$Type, arg1: boolean): $List<($KHolder<($BlockFamily)>)>
public static "findByStonecutterSource"(arg0: $Item$Type): $Collection<($KHolder<($BlockFamily)>)>
public static "get"(arg0: $ResourceLocation$Type): $BlockFamily
public static "find"(arg0: $Item$Type): $Collection<($KHolder<($BlockFamily)>)>
public static "all"(): $Collection<($KHolder<($BlockFamily)>)>
public static "getConvertRatio"(arg0: $Item$Type): float
public static "reloadResources"(arg0: $ResourceManager$Type, arg1: $OneTimeLoader$Context$Type): void
public static "reloadTags"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFamilies$Type = ($BlockFamilies);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFamilies_ = $BlockFamilies$Type;
}}
declare module "packages/snownee/kiwi/block/def/$BlockDefinition" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$BlockDefinition$Factory, $BlockDefinition$Factory$Type} from "packages/snownee/kiwi/block/def/$BlockDefinition$Factory"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $BlockDefinition {

 "getFactory"(): $BlockDefinition$Factory<(any)>
 "save"(arg0: $CompoundTag$Type): void
 "getDescription"(): $Component
 "getCamoDefinition"(): $BlockDefinition
 "canOcclude"(): boolean
 "place"(arg0: $Level$Type, arg1: $BlockPos$Type): void
 "getSoundType"(): $SoundType
 "canRenderInLayer"(arg0: $RenderType$Type): boolean
 "getLightEmission"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): integer
 "getColor"(arg0: $BlockState$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type, arg3: integer): integer
 "getBlockState"(): $BlockState
 "renderMaterial"(arg0: $Direction$Type): $Material
 "model"(): $BakedModel
 "getRenderTypes"(): $ChunkRenderTypeSet
 "createItem"(arg0: $HitResult$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Player$Type): $ItemStack
 "modelData"(): $ModelData
}

export namespace $BlockDefinition {
const MAP: $Map<(string), ($BlockDefinition$Factory<(any)>)>
const FACTORIES: $List<($BlockDefinition$Factory<(any)>)>
function getCamo(arg0: $BlockDefinition$Type): $BlockDefinition
function registerFactory(arg0: $BlockDefinition$Factory$Type<(any)>): void
function fromNBT(arg0: $CompoundTag$Type): $BlockDefinition
function fromBlock(arg0: $BlockState$Type, arg1: $BlockEntity$Type, arg2: $LevelReader$Type, arg3: $BlockPos$Type): $BlockDefinition
function fromItem(arg0: $ItemStack$Type, arg1: $BlockPlaceContext$Type): $BlockDefinition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockDefinition$Type = ($BlockDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockDefinition_ = $BlockDefinition$Type;
}}
declare module "packages/snownee/kiwi/$KiwiModules" {
import {$ModContext, $ModContext$Type} from "packages/snownee/kiwi/$ModContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ModuleInfo, $ModuleInfo$Type} from "packages/snownee/kiwi/$ModuleInfo"
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"

export class $KiwiModules {


public static "add"(arg0: $ResourceLocation$Type, arg1: $AbstractModule$Type, arg2: $ModContext$Type): void
public static "get"(arg0: $ResourceLocation$Type): $ModuleInfo
public static "get"(): $Collection<($ModuleInfo)>
public static "clear"(): void
public static "isLoaded"(arg0: $ResourceLocation$Type): boolean
public static "fire"(arg0: $Consumer$Type<($ModuleInfo$Type)>): void
public static "handleRegister"(arg0: $RegisterEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiModules$Type = ($KiwiModules);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiModules_ = $KiwiModules$Type;
}}
declare module "packages/snownee/kiwi/util/$MultilineTooltip" {
import {$Tooltip, $Tooltip$Type} from "packages/net/minecraft/client/gui/components/$Tooltip"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MultilineTooltip {

constructor()

public static "create"(arg0: $List$Type<($Component$Type)>, arg1: $List$Type<($Component$Type)>): $Tooltip
public static "create"(arg0: $List$Type<($Component$Type)>): $Tooltip
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultilineTooltip$Type = ($MultilineTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultilineTooltip_ = $MultilineTooltip$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$KBlockUtils" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$Interner, $Interner$Type} from "packages/com/google/common/collect/$Interner"

export interface $KBlockUtils {

 "componentsGetStateForPlacement"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): $BlockState
 "componentsUpdateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
}

export namespace $KBlockUtils {
const PROPERTY_INTERNER: $Interner<($Property<(any)>)>
const COMMON_PROPERTIES: $BiMap<(string), ($Property<(any)>)>
function getProperty(arg0: $BlockState$Type, arg1: string): $Property<(any)>
function getValueString<T>(arg0: $BlockState$Type, arg1: string): string
function internProperty<T>(arg0: T): T
function setValueByString<T>(arg0: $BlockState$Type, arg1: string, arg2: string): $BlockState
function getNameByValue<T>(arg0: $Property$Type<(T)>, arg1: any): string
function generateCommonProperties(): $BiMap<(string), ($Property<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockUtils$Type = ($KBlockUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockUtils_ = $KBlockUtils$Type;
}}
declare module "packages/snownee/kiwi/datagen/$GameObjectLookup" {
import {$GameObjectLookup$OptionalEntry, $GameObjectLookup$OptionalEntry$Type} from "packages/snownee/kiwi/datagen/$GameObjectLookup$OptionalEntry"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $GameObjectLookup {

}

export namespace $GameObjectLookup {
function all<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>, arg1: string): $Stream<(T)>
function fromModules<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>, ...arg1: (string)[]): $Stream<($GameObjectLookup$OptionalEntry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameObjectLookup$Type = ($GameObjectLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameObjectLookup_ = $GameObjectLookup$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixGreaterEqualsOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixGreaterEqualsOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixGreaterEqualsOperator$Type = ($InfixGreaterEqualsOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixGreaterEqualsOperator_ = $InfixGreaterEqualsOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CotFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CotFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CotFunction$Type = ($CotFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CotFunction_ = $CotFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcosHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcosHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcosHFunction$Type = ($AcosHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcosHFunction_ = $AcosHFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$TanFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $TanFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TanFunction$Type = ($TanFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TanFunction_ = $TanFunction$Type;
}}
declare module "packages/snownee/kiwi/recipe/$TryParseCondition" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

/**
 * 
 * @deprecated
 */
export class $TryParseCondition implements $ICondition {

constructor(arg0: $JsonElement$Type)

public "test"(arg0: $ICondition$IContext$Type): boolean
public "getID"(): $ResourceLocation
public static "getIngredient"(arg0: $JsonElement$Type, arg1: $ICondition$IContext$Type): $Ingredient
public static "shouldRegisterEntry"(arg0: $JsonElement$Type): boolean
get "iD"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TryParseCondition$Type = ($TryParseCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TryParseCondition_ = $TryParseCondition$Type;
}}
declare module "packages/snownee/kiwi/util/$InventoryUtil" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"

export class $InventoryUtil {


public static "consumeItemStack"(arg0: $IItemHandler$Type, arg1: $ItemStack$Type, arg2: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryUtil$Type = ($InventoryUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryUtil_ = $InventoryUtil$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/util/$EnumUtils" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $EnumUtils {

constructor()

public static "findEnumInsensitiveCase"<T extends $Enum<(T)>>(arg0: $Class$Type<(T)>, arg1: string): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumUtils$Type = ($EnumUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumUtils_ = $EnumUtils$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AcotFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AcotFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AcotFunction$Type = ($AcotFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AcotFunction_ = $AcotFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$Interests" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$StatePropertiesPredicate, $StatePropertiesPredicate$Type} from "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceChoices$Interests extends $Record {
static readonly "CODEC": $Codec<($PlaceChoices$Interests)>

constructor(when: $StatePropertiesPredicate$Type, bonus: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "when"(): $StatePropertiesPredicate
public "bonus"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Interests$Type = ($PlaceChoices$Interests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$Interests_ = $PlaceChoices$Interests$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ShapeGenerator" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export interface $ShapeGenerator {

 "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape

(arg0: $VoxelShape$Type): $ShapeGenerator
}

export namespace $ShapeGenerator {
function unit(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapeGenerator$Type = ($ShapeGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapeGenerator_ = $ShapeGenerator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorAnnotationNotFoundException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $OperatorAnnotationNotFoundException extends $RuntimeException {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OperatorAnnotationNotFoundException$Type = ($OperatorAnnotationNotFoundException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OperatorAnnotationNotFoundException_ = $OperatorAnnotationNotFoundException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CotRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CotRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CotRFunction$Type = ($CotRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CotRFunction_ = $CotRFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$DurationConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $DurationConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DurationConverter$Type = ($DurationConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DurationConverter_ = $DurationConverter$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$BlockItemTemplate" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BlockItemTemplate extends $KItemTemplate {

constructor(arg0: $Optional$Type<($ItemDefinitionProperties$Type)>, arg1: $Optional$Type<($ResourceLocation$Type)>, arg2: string)

public "clazz"(): string
public "type"(): $KItemTemplate$Type<(any)>
public "toString"(): string
public "resolve"(arg0: $ResourceLocation$Type): void
public "block"(): $Optional<($ResourceLocation)>
public static "directCodec"(): $Codec<($BlockItemTemplate)>
public "createItem"(arg0: $ResourceLocation$Type, arg1: $Item$Properties$Type, arg2: $JsonObject$Type): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockItemTemplate$Type = ($BlockItemTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockItemTemplate_ = $BlockItemTemplate$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$UnicodeEscaper" {
import {$Escaper, $Escaper$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$Escaper"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"

export class $UnicodeEscaper implements $Escaper {

constructor()

public "escape"(arg0: $Appendable$Type): $Appendable
public "escape"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeEscaper$Type = ($UnicodeEscaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeEscaper_ = $UnicodeEscaper$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixOrOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixOrOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixOrOperator$Type = ($InfixOrOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixOrOperator_ = $InfixOrOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$NumberConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $NumberConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberConverter$Type = ($NumberConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberConverter_ = $NumberConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration" {
import {$OperatorDictionaryIfc, $OperatorDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$OperatorDictionaryIfc"
import {$FunctionIfc, $FunctionIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc"
import {$DataAccessorIfc, $DataAccessorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$DataAccessorIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$ExpressionConfiguration$ExpressionConfigurationBuilder, $ExpressionConfiguration$ExpressionConfigurationBuilder$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration$ExpressionConfigurationBuilder"
import {$FunctionDictionaryIfc, $FunctionDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$FunctionDictionaryIfc"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OperatorIfc, $OperatorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$EvaluationValueConverterIfc, $EvaluationValueConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$EvaluationValueConverterIfc"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$MathContext, $MathContext$Type} from "packages/java/math/$MathContext"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $ExpressionConfiguration {
static readonly "StandardConstants": $Map<(string), ($EvaluationValue)>
static readonly "DECIMAL_PLACES_ROUNDING_UNLIMITED": integer
static readonly "DEFAULT_MATH_CONTEXT": $MathContext


public static "builder"(): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "getZoneId"(): $ZoneId
public "toBuilder"(): $ExpressionConfiguration$ExpressionConfigurationBuilder
public "getFunctionDictionary"(): $FunctionDictionaryIfc
public "isStructuresAllowed"(): boolean
public "isSingleQuoteStringLiteralsAllowed"(): boolean
public "getOperatorDictionary"(): $OperatorDictionaryIfc
public "isImplicitMultiplicationAllowed"(): boolean
public "getPowerOfPrecedence"(): integer
public "isAllowOverwriteConstants"(): boolean
public "getDecimalPlacesRounding"(): integer
public "getDataAccessorSupplier"(): $Supplier<($DataAccessorIfc)>
public "getDefaultConstants"(): $Map<(string), ($EvaluationValue)>
public "getDecimalPlacesResult"(): integer
public "getEvaluationValueConverter"(): $EvaluationValueConverterIfc
public "withAdditionalFunctions"(...arg0: ($Map$Entry$Type<(string), ($FunctionIfc$Type)>)[]): $ExpressionConfiguration
public "isStripTrailingZeros"(): boolean
public "withAdditionalOperators"(...arg0: ($Map$Entry$Type<(string), ($OperatorIfc$Type)>)[]): $ExpressionConfiguration
public "getMathContext"(): $MathContext
public "isArraysAllowed"(): boolean
public "getDateTimeFormatters"(): $List<($DateTimeFormatter)>
public static "defaultConfiguration"(): $ExpressionConfiguration
get "zoneId"(): $ZoneId
get "functionDictionary"(): $FunctionDictionaryIfc
get "structuresAllowed"(): boolean
get "singleQuoteStringLiteralsAllowed"(): boolean
get "operatorDictionary"(): $OperatorDictionaryIfc
get "implicitMultiplicationAllowed"(): boolean
get "powerOfPrecedence"(): integer
get "allowOverwriteConstants"(): boolean
get "decimalPlacesRounding"(): integer
get "dataAccessorSupplier"(): $Supplier<($DataAccessorIfc)>
get "defaultConstants"(): $Map<(string), ($EvaluationValue)>
get "decimalPlacesResult"(): integer
get "evaluationValueConverter"(): $EvaluationValueConverterIfc
get "stripTrailingZeros"(): boolean
get "mathContext"(): $MathContext
get "arraysAllowed"(): boolean
get "dateTimeFormatters"(): $List<($DateTimeFormatter)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpressionConfiguration$Type = ($ExpressionConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpressionConfiguration_ = $ExpressionConfiguration$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$AbstractMinMaxFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"

export class $AbstractMinMaxFunction extends $AbstractFunction {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractMinMaxFunction$Type = ($AbstractMinMaxFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractMinMaxFunction_ = $AbstractMinMaxFunction$Type;
}}
declare module "packages/snownee/kiwi/recipe/$ModuleLoadedCondition" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ICondition, $ICondition$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition"

export class $ModuleLoadedCondition implements $ICondition {
static readonly "ID": $ResourceLocation

constructor(arg0: $ResourceLocation$Type)

public "test"(arg0: $ICondition$IContext$Type): boolean
public "getID"(): $ResourceLocation
public static "shouldRegisterEntry"(arg0: $JsonElement$Type): boolean
get "iD"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleLoadedCondition$Type = ($ModuleLoadedCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleLoadedCondition_ = $ModuleLoadedCondition$Type;
}}
declare module "packages/snownee/kiwi/schedule/impl/$GlobalTicker" {
import {$ITicker, $ITicker$Type} from "packages/snownee/kiwi/schedule/$ITicker"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$TickEvent$Phase, $TickEvent$Phase$Type} from "packages/net/minecraftforge/event/$TickEvent$Phase"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $GlobalTicker extends $Enum<($GlobalTicker)> implements $ITicker {
static readonly "PRE_SERVER": $GlobalTicker
static readonly "POST_SERVER": $GlobalTicker
static readonly "PRE_CLIENT": $GlobalTicker
static readonly "POST_CLIENT": $GlobalTicker


public static "get"(arg0: $LogicalSide$Type, arg1: $TickEvent$Phase$Type): $GlobalTicker
public static "values"(): ($GlobalTicker)[]
public static "valueOf"(arg0: string): $GlobalTicker
public static "onTickServer"(arg0: $TickEvent$ServerTickEvent$Type): void
public static "onTickClient"(arg0: $TickEvent$ClientTickEvent$Type): void
public "destroy"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalTicker$Type = (("pre_client") | ("post_client") | ("pre_server") | ("post_server")) | ($GlobalTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalTicker_ = $GlobalTicker$Type;
}}
declare module "packages/snownee/kiwi/$RegistryLookup" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegistryLookup {
readonly "registries": $Map<($Class<(any)>), (any)>
readonly "cache": $Cache<($Class<(any)>), ($Optional<(any)>)>

constructor()

public "findRegistry"(arg0: any): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryLookup$Type = ($RegistryLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryLookup_ = $RegistryLookup$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$AnchorToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $AnchorToken extends $Token {

constructor(arg0: string, arg1: $Mark$Type, arg2: $Mark$Type)

public "getValue"(): string
public "getTokenId"(): $Token$ID
get "value"(): string
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorToken$Type = ($AnchorToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorToken_ = $AnchorToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$StreamStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $StreamStartEvent extends $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamStartEvent$Type = ($StreamStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamStartEvent_ = $StreamStartEvent$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomizationServiceFinder" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export class $CustomizationServiceFinder {
static readonly "PACK_DIRECTORY": $Path

constructor()

public static "shouldEnable"(arg0: $List$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationServiceFinder$Type = ($CustomizationServiceFinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationServiceFinder_ = $CustomizationServiceFinder$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$Serializer" {
import {$Resolver, $Resolver$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/resolver/$Resolver"
import {$DumperOptions, $DumperOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Emitable, $Emitable$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$Emitable"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $Serializer {

constructor(arg0: $Emitable$Type, arg1: $Resolver$Type, arg2: $DumperOptions$Type, arg3: $Tag$Type)

public "close"(): void
public "open"(): void
public "serialize"(arg0: $Node$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Serializer$Type = ($Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Serializer_ = $Serializer$Type;
}}
declare module "packages/snownee/kiwi/contributor/client/$CosmeticLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Cache, $Cache$Type} from "packages/com/google/common/cache/$Cache"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"

export class $CosmeticLayer extends $RenderLayer<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)> {
static readonly "ALL_LAYERS": $Collection<($CosmeticLayer)>
readonly "f_117344_": $RenderLayerParent<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "getCache"(): $Cache<(string), ($RenderLayer<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>)>
public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
get "cache"(): $Cache<(string), ($RenderLayer<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosmeticLayer$Type = ($CosmeticLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosmeticLayer_ = $CosmeticLayer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ArrayConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $ArrayConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayConverter$Type = ($ArrayConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayConverter_ = $ArrayConverter$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$ReloadFamiliesAndRulesCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $ReloadFamiliesAndRulesCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadFamiliesAndRulesCommand$Type = ($ReloadFamiliesAndRulesCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadFamiliesAndRulesCommand_ = $ReloadFamiliesAndRulesCommand$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$KBlockComponents" {
import {$MouldingComponent, $MouldingComponent$Type} from "packages/snownee/kiwi/customization/block/component/$MouldingComponent"
import {$KiwiGO, $KiwiGO$Type} from "packages/snownee/kiwi/$KiwiGO"
import {$FrontAndTopComponent, $FrontAndTopComponent$Type} from "packages/snownee/kiwi/customization/block/component/$FrontAndTopComponent"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CycleVariantsComponent, $CycleVariantsComponent$Type} from "packages/snownee/kiwi/customization/block/component/$CycleVariantsComponent"
import {$HorizontalComponent, $HorizontalComponent$Type} from "packages/snownee/kiwi/customization/block/component/$HorizontalComponent"
import {$ConsumableComponent, $ConsumableComponent$Type} from "packages/snownee/kiwi/customization/block/component/$ConsumableComponent"
import {$DirectionalComponent, $DirectionalComponent$Type} from "packages/snownee/kiwi/customization/block/component/$DirectionalComponent"
import {$HorizontalAxisComponent, $HorizontalAxisComponent$Type} from "packages/snownee/kiwi/customization/block/component/$HorizontalAxisComponent"
import {$WaterLoggableComponent, $WaterLoggableComponent$Type} from "packages/snownee/kiwi/customization/block/component/$WaterLoggableComponent"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$StackableComponent, $StackableComponent$Type} from "packages/snownee/kiwi/customization/block/component/$StackableComponent"
import {$SimplePropertiesComponent, $SimplePropertiesComponent$Type} from "packages/snownee/kiwi/customization/block/component/$SimplePropertiesComponent"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"

export class $KBlockComponents extends $AbstractModule {
static readonly "DIRECTIONAL": $KiwiGO<($KBlockComponent$Type<($DirectionalComponent)>)>
static readonly "HORIZONTAL": $KiwiGO<($KBlockComponent$Type<($HorizontalComponent)>)>
static readonly "HORIZONTAL_AXIS": $KiwiGO<($KBlockComponent$Type<($HorizontalAxisComponent)>)>
static readonly "FRONT_AND_TOP": $KiwiGO<($KBlockComponent$Type<($FrontAndTopComponent)>)>
static readonly "MOULDING": $KiwiGO<($KBlockComponent$Type<($MouldingComponent)>)>
static readonly "WATER_LOGGABLE": $KiwiGO<($KBlockComponent$Type<($WaterLoggableComponent)>)>
static readonly "CONSUMABLE": $KiwiGO<($KBlockComponent$Type<($ConsumableComponent)>)>
static readonly "STACKABLE": $KiwiGO<($KBlockComponent$Type<($StackableComponent)>)>
static readonly "CYCLE_VARIANTS": $KiwiGO<($KBlockComponent$Type<($CycleVariantsComponent)>)>
static readonly "SIMPLE_PROPERTIES": $KiwiGO<($KBlockComponent$Type<($SimplePropertiesComponent)>)>
 "uid": $ResourceLocation

constructor()

public static "getSimpleInstance"(arg0: $KBlockComponent$Type$Type<(any)>): $KBlockComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockComponents$Type = ($KBlockComponents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockComponents_ = $KBlockComponents$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$UseHandler, $UseHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$UseHandler"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockBehaviorRegistry {


public static "getInstance"(): $BlockBehaviorRegistry
public "onUseBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $InteractionResult
public "addUseHandler"(arg0: $UseHandler$Type): void
public "setContext"(arg0: $Block$Type): void
get "instance"(): $BlockBehaviorRegistry
set "context"(value: $Block$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockBehaviorRegistry$Type = ($BlockBehaviorRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockBehaviorRegistry_ = $BlockBehaviorRegistry$Type;
}}
declare module "packages/snownee/kiwi/block/entity/$TagBasedBlockEntityType" {
import {$BlockEntityType$BlockEntitySupplier, $BlockEntityType$BlockEntitySupplier$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$BlockEntitySupplier"
import {$ShulkerBoxBlockEntity, $ShulkerBoxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ShulkerBoxBlockEntity"
import {$BeehiveBlockEntity, $BeehiveBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeehiveBlockEntity"
import {$ComparatorBlockEntity, $ComparatorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ComparatorBlockEntity"
import {$DynamicLightHandlerHolder, $DynamicLightHandlerHolder$Type} from "packages/dev/lambdaurora/lambdynlights/accessor/$DynamicLightHandlerHolder"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DropperBlockEntity, $DropperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DropperBlockEntity"
import {$BrewingStandBlockEntity, $BrewingStandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrewingStandBlockEntity"
import {$DaylightDetectorBlockEntity, $DaylightDetectorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DaylightDetectorBlockEntity"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$HangingSignBlockEntity, $HangingSignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HangingSignBlockEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SignBlockEntity, $SignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SignBlockEntity"
import {$JukeboxBlockEntity, $JukeboxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JukeboxBlockEntity"
import {$SculkShriekerBlockEntity, $SculkShriekerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkShriekerBlockEntity"
import {$EnchantmentTableBlockEntity, $EnchantmentTableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnchantmentTableBlockEntity"
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$CalibratedSculkSensorBlockEntity, $CalibratedSculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CalibratedSculkSensorBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChiseledBookShelfBlockEntity, $ChiseledBookShelfBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChiseledBookShelfBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BrushableBlockEntity, $BrushableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrushableBlockEntity"
import {$FurnaceBlockEntity, $FurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$FurnaceBlockEntity"
import {$DispenserBlockEntity, $DispenserBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DispenserBlockEntity"
import {$EnderChestBlockEntity, $EnderChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnderChestBlockEntity"
import {$SculkSensorBlockEntity, $SculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkSensorBlockEntity"
import {$BarrelBlockEntity, $BarrelBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BarrelBlockEntity"
import {$ChestBlockEntity, $ChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChestBlockEntity"
import {$BannerBlockEntity, $BannerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BannerBlockEntity"
import {$TheEndGatewayBlockEntity, $TheEndGatewayBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndGatewayBlockEntity"
import {$CommandBlockEntity, $CommandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CommandBlockEntity"
import {$BellBlockEntity, $BellBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BellBlockEntity"
import {$TrappedChestBlockEntity, $TrappedChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TrappedChestBlockEntity"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$SmokerBlockEntity, $SmokerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SmokerBlockEntity"
import {$BlastFurnaceBlockEntity, $BlastFurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlastFurnaceBlockEntity"
import {$DecoratedPotBlockEntity, $DecoratedPotBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DecoratedPotBlockEntity"
import {$PistonMovingBlockEntity, $PistonMovingBlockEntity$Type} from "packages/net/minecraft/world/level/block/piston/$PistonMovingBlockEntity"
import {$HopperBlockEntity, $HopperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HopperBlockEntity"
import {$TheEndPortalBlockEntity, $TheEndPortalBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndPortalBlockEntity"
import {$BeaconBlockEntity, $BeaconBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeaconBlockEntity"
import {$StructureBlockEntity, $StructureBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$StructureBlockEntity"
import {$SpawnerBlockEntity, $SpawnerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SpawnerBlockEntity"
import {$JigsawBlockEntity, $JigsawBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JigsawBlockEntity"
import {$ConduitBlockEntity, $ConduitBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ConduitBlockEntity"
import {$BedBlockEntity, $BedBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BedBlockEntity"
import {$SculkCatalystBlockEntity, $SculkCatalystBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkCatalystBlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SkullBlockEntity, $SkullBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SkullBlockEntity"
import {$LecternBlockEntity, $LecternBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$LecternBlockEntity"
import {$CampfireBlockEntity, $CampfireBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CampfireBlockEntity"

export class $TagBasedBlockEntityType<T extends $BlockEntity> extends $BlockEntityType<(T)> {
static readonly "FURNACE": $BlockEntityType<($FurnaceBlockEntity)>
static readonly "CHEST": $BlockEntityType<($ChestBlockEntity)>
static readonly "TRAPPED_CHEST": $BlockEntityType<($TrappedChestBlockEntity)>
static readonly "ENDER_CHEST": $BlockEntityType<($EnderChestBlockEntity)>
static readonly "JUKEBOX": $BlockEntityType<($JukeboxBlockEntity)>
static readonly "DISPENSER": $BlockEntityType<($DispenserBlockEntity)>
static readonly "DROPPER": $BlockEntityType<($DropperBlockEntity)>
static readonly "SIGN": $BlockEntityType<($SignBlockEntity)>
static readonly "HANGING_SIGN": $BlockEntityType<($HangingSignBlockEntity)>
static readonly "MOB_SPAWNER": $BlockEntityType<($SpawnerBlockEntity)>
static readonly "PISTON": $BlockEntityType<($PistonMovingBlockEntity)>
static readonly "BREWING_STAND": $BlockEntityType<($BrewingStandBlockEntity)>
static readonly "ENCHANTING_TABLE": $BlockEntityType<($EnchantmentTableBlockEntity)>
static readonly "END_PORTAL": $BlockEntityType<($TheEndPortalBlockEntity)>
static readonly "BEACON": $BlockEntityType<($BeaconBlockEntity)>
static readonly "SKULL": $BlockEntityType<($SkullBlockEntity)>
static readonly "DAYLIGHT_DETECTOR": $BlockEntityType<($DaylightDetectorBlockEntity)>
static readonly "HOPPER": $BlockEntityType<($HopperBlockEntity)>
static readonly "COMPARATOR": $BlockEntityType<($ComparatorBlockEntity)>
static readonly "BANNER": $BlockEntityType<($BannerBlockEntity)>
static readonly "STRUCTURE_BLOCK": $BlockEntityType<($StructureBlockEntity)>
static readonly "END_GATEWAY": $BlockEntityType<($TheEndGatewayBlockEntity)>
static readonly "COMMAND_BLOCK": $BlockEntityType<($CommandBlockEntity)>
static readonly "SHULKER_BOX": $BlockEntityType<($ShulkerBoxBlockEntity)>
static readonly "BED": $BlockEntityType<($BedBlockEntity)>
static readonly "CONDUIT": $BlockEntityType<($ConduitBlockEntity)>
static readonly "BARREL": $BlockEntityType<($BarrelBlockEntity)>
static readonly "SMOKER": $BlockEntityType<($SmokerBlockEntity)>
static readonly "BLAST_FURNACE": $BlockEntityType<($BlastFurnaceBlockEntity)>
static readonly "LECTERN": $BlockEntityType<($LecternBlockEntity)>
static readonly "BELL": $BlockEntityType<($BellBlockEntity)>
static readonly "JIGSAW": $BlockEntityType<($JigsawBlockEntity)>
static readonly "CAMPFIRE": $BlockEntityType<($CampfireBlockEntity)>
static readonly "BEEHIVE": $BlockEntityType<($BeehiveBlockEntity)>
static readonly "SCULK_SENSOR": $BlockEntityType<($SculkSensorBlockEntity)>
static readonly "CALIBRATED_SCULK_SENSOR": $BlockEntityType<($CalibratedSculkSensorBlockEntity)>
static readonly "SCULK_CATALYST": $BlockEntityType<($SculkCatalystBlockEntity)>
static readonly "SCULK_SHRIEKER": $BlockEntityType<($SculkShriekerBlockEntity)>
static readonly "CHISELED_BOOKSHELF": $BlockEntityType<($ChiseledBookShelfBlockEntity)>
static readonly "BRUSHABLE_BLOCK": $BlockEntityType<($BrushableBlockEntity)>
static readonly "DECORATED_POT": $BlockEntityType<($DecoratedPotBlockEntity)>
 "validBlocks": $Set<($Block)>

constructor(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $TagKey$Type<($Block$Type)>, arg2: $Type$Type<(any)>)

public "isValid"(arg0: $BlockState$Type): boolean
public static "cast"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
public static "cast"<T extends $Entity>(arg0: $EntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagBasedBlockEntityType$Type<T> = ($TagBasedBlockEntityType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagBasedBlockEntityType_<T> = $TagBasedBlockEntityType$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/internal/$Logger$Level" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Logger$Level extends $Enum<($Logger$Level)> {
static readonly "WARNING": $Logger$Level


public static "values"(): ($Logger$Level)[]
public static "valueOf"(arg0: string): $Logger$Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Logger$Level$Type = (("warning")) | ($Logger$Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Logger$Level_ = $Logger$Level$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$StreamEndToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $StreamEndToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamEndToken$Type = ($StreamEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamEndToken_ = $StreamEndToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$Represent" {
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $Represent {

 "representData"(arg0: any): $Node

(arg0: any): $Node
}

export namespace $Represent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Represent$Type = ($Represent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Represent_ = $Represent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$StringConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $StringConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringConverter$Type = ($StringConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringConverter_ = $StringConverter$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceTarget$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PlaceTarget$Type extends $Enum<($PlaceTarget$Type)> {
static readonly "TEMPLATE": $PlaceTarget$Type
static readonly "BLOCK": $PlaceTarget$Type
readonly "prefix": string


public static "values"(): ($PlaceTarget$Type)[]
public static "valueOf"(arg0: string): $PlaceTarget$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceTarget$Type$Type = (("template") | ("block")) | ($PlaceTarget$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceTarget$Type_ = $PlaceTarget$Type$Type;
}}
declare module "packages/snownee/kiwi/customization/item/$KItemSettings$Builder" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"

export class $KItemSettings$Builder {


public "get"(): $Item$Properties
public "configure"(arg0: $Consumer$Type<($Item$Properties$Type)>): $KItemSettings$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemSettings$Builder$Type = ($KItemSettings$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemSettings$Builder_ = $KItemSettings$Builder$Type;
}}
declare module "packages/snownee/kiwi/mixin/$MixinPlugin" {
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
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CommentEvent" {
import {$CommentType, $CommentType$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentType"
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $CommentEvent extends $Event {

constructor(arg0: $CommentType$Type, arg1: string, arg2: $Mark$Type, arg3: $Mark$Type)

public "getValue"(): string
public "getCommentType"(): $CommentType
public "getEventId"(): $Event$ID
get "value"(): string
get "commentType"(): $CommentType
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentEvent$Type = ($CommentEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentEvent_ = $CommentEvent$Type;
}}
declare module "packages/snownee/kiwi/mixin/$VillagerAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $VillagerAccess {

}

export namespace $VillagerAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerAccess$Type = ($VillagerAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerAccess_ = $VillagerAccess$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$KSitCommonConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KSitCommonConfig {
static "sitOnSlab": boolean
static "sitOnStairs": boolean
static "sitOnCarpet": boolean
static "sitOnBed": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KSitCommonConfig$Type = ($KSitCommonConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KSitCommonConfig_ = $KSitCommonConfig$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$KeyToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $KeyToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyToken$Type = ($KeyToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyToken_ = $KeyToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$StructureConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $StructureConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureConverter$Type = ($StructureConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureConverter_ = $StructureConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeToEpochFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeToEpochFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeToEpochFunction$Type = ($DateTimeToEpochFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeToEpochFunction_ = $DateTimeToEpochFunction$Type;
}}
declare module "packages/snownee/kiwi/$ModContext" {
import {$ModContainer, $ModContainer$Type} from "packages/net/minecraftforge/fml/$ModContainer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModContext {
static readonly "ALL_CONTEXTS": $Map<(string), ($ModContext)>
 "modContainer": $ModContainer


public static "get"(arg0: string): $ModContext
public "setActiveContainer"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModContext$Type = ($ModContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModContext_ = $ModContext$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixMinusOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixMinusOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixMinusOperator$Type = ($InfixMinusOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixMinusOperator_ = $InfixMinusOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StatePropertiesPredicate$PropertyMatcher, $StatePropertiesPredicate$PropertyMatcher$Type} from "packages/snownee/kiwi/customization/placement/$StatePropertiesPredicate$PropertyMatcher"

export class $StatePropertiesPredicate extends $Record implements $Predicate<($BlockState)> {
static readonly "CODEC": $Codec<($StatePropertiesPredicate)>

constructor(properties: $List$Type<($StatePropertiesPredicate$PropertyMatcher$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $BlockState$Type): boolean
public "properties"(): $List<($StatePropertiesPredicate$PropertyMatcher)>
public "smartTest"(arg0: $BlockState$Type, arg1: $BlockState$Type): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public "negate"(): $Predicate<($BlockState)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "isEqual"<T>(arg0: any): $Predicate<($BlockState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatePropertiesPredicate$Type = ($StatePropertiesPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatePropertiesPredicate_ = $StatePropertiesPredicate$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$FlowEntryToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $FlowEntryToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowEntryToken$Type = ($FlowEntryToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowEntryToken_ = $FlowEntryToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$NonPrintableStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$NonPrintableStyle extends $Enum<($DumperOptions$NonPrintableStyle)> {
static readonly "BINARY": $DumperOptions$NonPrintableStyle
static readonly "ESCAPE": $DumperOptions$NonPrintableStyle


public static "values"(): ($DumperOptions$NonPrintableStyle)[]
public static "valueOf"(arg0: string): $DumperOptions$NonPrintableStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$NonPrintableStyle$Type = (("binary") | ("escape")) | ($DumperOptions$NonPrintableStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$NonPrintableStyle_ = $DumperOptions$NonPrintableStyle$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$ItemButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientTooltipPositioner, $ClientTooltipPositioner$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipPositioner"
import {$ItemButton$Builder, $ItemButton$Builder$Type} from "packages/snownee/kiwi/customization/builder/$ItemButton$Builder"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $ItemButton extends $Button {
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


public "setTooltipPositioner"(arg0: $ClientTooltipPositioner$Type): void
public static "builder"(arg0: $ItemStack$Type, arg1: boolean, arg2: $Button$OnPress$Type): $ItemButton$Builder
public "getItem"(): $ItemStack
set "tooltipPositioner"(value: $ClientTooltipPositioner$Type)
get "item"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemButton$Type = ($ItemButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemButton_ = $ItemButton$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$KiwiShapelessRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$RecipeBuilder, $RecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$RecipeBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CraftingRecipeBuilder, $CraftingRecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$CraftingRecipeBuilder"
import {$RecipeCategory, $RecipeCategory$Type} from "packages/net/minecraft/data/recipes/$RecipeCategory"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $KiwiShapelessRecipeBuilder extends $CraftingRecipeBuilder implements $RecipeBuilder {

constructor(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer)

public "group"(arg0: string): $KiwiShapelessRecipeBuilder
public "requires"(arg0: $Ingredient$Type, arg1: integer): $KiwiShapelessRecipeBuilder
public "requires"(arg0: $Ingredient$Type): $KiwiShapelessRecipeBuilder
public "requires"(arg0: $ItemLike$Type, arg1: integer): $KiwiShapelessRecipeBuilder
public "requires"(arg0: $ItemLike$Type): $KiwiShapelessRecipeBuilder
public "requires"(arg0: $TagKey$Type<($Item$Type)>): $KiwiShapelessRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "unlockedBy"(arg0: string, arg1: $CriterionTriggerInstance$Type): $KiwiShapelessRecipeBuilder
public "getResult"(): $Item
public "noContainers"(): $KiwiShapelessRecipeBuilder
public static "shapeless"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type): $KiwiShapelessRecipeBuilder
public static "shapeless"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer): $KiwiShapelessRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string): void
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
get "result"(): $Item
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiShapelessRecipeBuilder$Type = ($KiwiShapelessRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiShapelessRecipeBuilder_ = $KiwiShapelessRecipeBuilder$Type;
}}
declare module "packages/snownee/kiwi/inventory/container/$ModSlot" {
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ModSlot extends $Slot {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $Container$Type, arg1: integer, arg2: integer, arg3: integer)

public "mayPlace"(arg0: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModSlot$Type = ($ModSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModSlot_ = $ModSlot$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Preparation" {
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ListMultimap, $ListMultimap$Type} from "packages/com/google/common/collect/$ListMultimap"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$KBlockDefinition, $KBlockDefinition$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockDefinition"
import {$PlaceSlotProvider, $PlaceSlotProvider$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider"
import {$PlaceSlot, $PlaceSlot$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlot"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Interner, $Interner$Type} from "packages/com/google/common/collect/$Interner"

export class $PlaceSlotProvider$Preparation extends $Record {

constructor(providers: $Map$Type<($ResourceLocation$Type), ($PlaceSlotProvider$Type)>, byTemplate: $ListMultimap$Type<($KBlockTemplate$Type), ($KHolder$Type<($PlaceSlotProvider$Type)>)>, byBlock: $ListMultimap$Type<($ResourceLocation$Type), ($KHolder$Type<($PlaceSlotProvider$Type)>)>, slots: $ListMultimap$Type<($Pair$Type<($BlockState$Type), ($Direction$Type)>), ($PlaceSlot$Type)>, slotInterner: $Interner$Type<($PlaceSlot$Type)>, accessedBlocks: $Set$Type<($Block$Type)>, knownPrimaryTags: $Set$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Supplier$Type<($Map$Type<($ResourceLocation$Type), ($PlaceSlotProvider$Type)>)>, arg1: $Map$Type<($ResourceLocation$Type), ($KBlockTemplate$Type)>): $PlaceSlotProvider$Preparation
public "register"(arg0: $BlockState$Type, arg1: $PlaceSlot$Type): void
public "slots"(): $ListMultimap<($Pair<($BlockState), ($Direction)>), ($PlaceSlot)>
public "providers"(): $Map<($ResourceLocation), ($PlaceSlotProvider)>
public "attachSlotsB"(): void
public "attachSlotsA"(arg0: $Block$Type, arg1: $KBlockDefinition$Type): void
public "byBlock"(): $ListMultimap<($ResourceLocation), ($KHolder<($PlaceSlotProvider)>)>
public "knownPrimaryTags"(): $Set<(string)>
public "accessedBlocks"(): $Set<($Block)>
public "byTemplate"(): $ListMultimap<($KBlockTemplate), ($KHolder<($PlaceSlotProvider)>)>
public "slotInterner"(): $Interner<($PlaceSlot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceSlotProvider$Preparation$Type = ($PlaceSlotProvider$Preparation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceSlotProvider$Preparation_ = $PlaceSlotProvider$Preparation$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$DegFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DegFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DegFunction$Type = ($DegFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DegFunction_ = $DegFunction$Type;
}}
declare module "packages/snownee/kiwi/$Categories" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Categories {
static readonly "BUILDING_BLOCKS": string
static readonly "COLORED_BLOCKS": string
static readonly "COMBAT": string
static readonly "FOOD_AND_DRINKS": string
static readonly "FUNCTIONAL_BLOCKS": string
static readonly "INGREDIENTS": string
static readonly "NATURAL_BLOCKS": string
static readonly "OP_BLOCKS": string
static readonly "REDSTONE_BLOCKS": string
static readonly "SPAWN_EGGS": string
static readonly "TOOLS_AND_UTILITIES": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Categories$Type = ($Categories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Categories_ = $Categories$Type;
}}
declare module "packages/snownee/kiwi/contributor/client/gui/$CosmeticScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CosmeticScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "onClose"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosmeticScreen$Type = ($CosmeticScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosmeticScreen_ = $CosmeticScreen$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertySubstitute" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property"

export class $PropertySubstitute extends $Property {

constructor(arg0: string, arg1: $Class$Type<(any)>, arg2: string, arg3: string, ...arg4: ($Class$Type<(any)>)[])
constructor(arg0: string, arg1: $Class$Type<(any)>, ...arg2: ($Class$Type<(any)>)[])

public "getName"(): string
public "get"(arg0: any): any
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
public "getType"(): $Class<(any)>
public "setDelegate"(arg0: $Property$Type): void
public "getActualTypeArguments"(): ($Class<(any)>)[]
public "isReadable"(): boolean
public "isWritable"(): boolean
public "setActualTypeArguments"(...arg0: ($Class$Type<(any)>)[]): void
public "setTargetType"(arg0: $Class$Type<(any)>): void
get "name"(): string
get "annotations"(): $List<($Annotation)>
get "type"(): $Class<(any)>
set "delegate"(value: $Property$Type)
get "actualTypeArguments"(): ($Class<(any)>)[]
get "readable"(): boolean
get "writable"(): boolean
set "actualTypeArguments"(value: ($Class$Type<(any)>)[])
set "targetType"(value: $Class$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertySubstitute$Type = ($PropertySubstitute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertySubstitute_ = $PropertySubstitute$Type;
}}
declare module "packages/snownee/kiwi/mixin/$RecipeManagerAccess" {
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"

export interface $RecipeManagerAccess {

 "getContext"(): $ICondition$IContext

(): $ICondition$IContext
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
declare module "packages/snownee/kiwi/customization/block/loader/$KBlockDefinition" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$BlockDefinitionProperties, $BlockDefinitionProperties$Type} from "packages/snownee/kiwi/customization/block/loader/$BlockDefinitionProperties"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$KBlockSettings$Builder, $KBlockSettings$Builder$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings$Builder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$ConfiguredBlockTemplate, $ConfiguredBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$ConfiguredBlockTemplate"
import {$ShapeStorage, $ShapeStorage$Type} from "packages/snownee/kiwi/customization/shape/$ShapeStorage"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KBlockDefinition extends $Record {

constructor(template: $ConfiguredBlockTemplate$Type, properties: $BlockDefinitionProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "properties"(): $BlockDefinitionProperties
public "template"(): $ConfiguredBlockTemplate
public "createSettings"(arg0: $ResourceLocation$Type, arg1: $ShapeStorage$Type): $KBlockSettings$Builder
public static "codec"(arg0: $Map$Type<($ResourceLocation$Type), ($KBlockTemplate$Type)>, arg1: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>): $Codec<($KBlockDefinition)>
public "createBlock"(arg0: $ResourceLocation$Type, arg1: $ShapeStorage$Type): $Block
public static "setConfiguringShape"(arg0: $Block$Type): void
set "configuringShape"(value: $Block$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockDefinition$Type = ($KBlockDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockDefinition_ = $KBlockDefinition$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$CustomClassLoaderConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Constructor, $Constructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $CustomClassLoaderConstructor extends $Constructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(arg0: $ClassLoader$Type, arg1: $LoaderOptions$Type)
constructor(arg0: $Class$Type<(any)>, arg1: $ClassLoader$Type, arg2: $LoaderOptions$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomClassLoaderConstructor$Type = ($CustomClassLoaderConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomClassLoaderConstructor_ = $CustomClassLoaderConstructor$Type;
}}
declare module "packages/snownee/kiwi/customization/block/family/$BlockFamilyInferrer" {
import {$BlockFamily, $BlockFamily$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockFamilyInferrer {
static readonly "IGNORE": $TagKey<($Block)>

constructor()

public "generate"(): $Collection<($KHolder<($BlockFamily)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFamilyInferrer$Type = ($BlockFamilyInferrer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFamilyInferrer_ = $BlockFamilyInferrer$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BuilderModePreview" {
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$BuilderRule, $BuilderRule$Type} from "packages/snownee/kiwi/customization/builder/$BuilderRule"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DebugRenderer$SimpleDebugRenderer, $DebugRenderer$SimpleDebugRenderer$Type} from "packages/net/minecraft/client/renderer/debug/$DebugRenderer$SimpleDebugRenderer"

export class $BuilderModePreview implements $DebugRenderer$SimpleDebugRenderer {
 "rule": $KHolder<($BuilderRule)>
 "pos": $BlockPos
 "positions": $List<($BlockPos)>

constructor()

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: double, arg3: double, arg4: double): void
public "clear"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderModePreview$Type = ($BuilderModePreview);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderModePreview_ = $BuilderModePreview$Type;
}}
declare module "packages/snownee/kiwi/contributor/$ContributorsClient" {
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$ClientPlayerNetworkEvent$LoggingOut, $ClientPlayerNetworkEvent$LoggingOut$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingOut"
import {$EntityRenderersEvent$AddLayers, $EntityRenderersEvent$AddLayers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$AddLayers"
import {$ClientPlayerNetworkEvent$LoggingIn, $ClientPlayerNetworkEvent$LoggingIn$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingIn"

export class $ContributorsClient {

constructor()

public static "onClientPlayerLoggedOut"(arg0: $ClientPlayerNetworkEvent$LoggingOut$Type): void
public static "onClientPlayerLoggedIn"(arg0: $ClientPlayerNetworkEvent$LoggingIn$Type): void
public static "addLayers"(arg0: $EntityRenderersEvent$AddLayers$Type): void
public static "onKeyInput"(arg0: $InputEvent$Key$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContributorsClient$Type = ($ContributorsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContributorsClient_ = $ContributorsClient$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$BlockFundamentals" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$SlotLink$Preparation, $SlotLink$Preparation$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$Preparation"
import {$PlaceChoices$Preparation, $PlaceChoices$Preparation$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Preparation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$KBlockDefinition, $KBlockDefinition$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockDefinition"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$OneTimeLoader$Context, $OneTimeLoader$Context$Type} from "packages/snownee/kiwi/util/resource/$OneTimeLoader$Context"
import {$PlaceSlotProvider$Preparation, $PlaceSlotProvider$Preparation$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Preparation"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KMaterial, $KMaterial$Type} from "packages/snownee/kiwi/customization/block/loader/$KMaterial"
import {$ShapeStorage, $ShapeStorage$Type} from "packages/snownee/kiwi/customization/shape/$ShapeStorage"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BlockFundamentals extends $Record {

constructor(materials: $Map$Type<($ResourceLocation$Type), ($KMaterial$Type)>, templates: $Map$Type<($ResourceLocation$Type), ($KBlockTemplate$Type)>, slotProviders: $PlaceSlotProvider$Preparation$Type, slotLinks: $SlotLink$Preparation$Type, placeChoices: $PlaceChoices$Preparation$Type, shapes: $ShapeStorage$Type, blocks: $Map$Type<($ResourceLocation$Type), ($KBlockDefinition$Type)>, materialCodec: $MapCodec$Type<($Optional$Type<($KMaterial$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "reload"(arg0: $ResourceManager$Type, arg1: $OneTimeLoader$Context$Type, arg2: boolean): $BlockFundamentals
public "blocks"(): $Map<($ResourceLocation), ($KBlockDefinition)>
public "materials"(): $Map<($ResourceLocation), ($KMaterial)>
public "templates"(): $Map<($ResourceLocation), ($KBlockTemplate)>
public "materialCodec"(): $MapCodec<($Optional<($KMaterial)>)>
public "slotProviders"(): $PlaceSlotProvider$Preparation
public "placeChoices"(): $PlaceChoices$Preparation
public "slotLinks"(): $SlotLink$Preparation
public "shapes"(): $ShapeStorage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFundamentals$Type = ($BlockFundamentals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFundamentals_ = $BlockFundamentals$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$KBlockSettings" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$PlaceChoices, $PlaceChoices$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$MapColor, $MapColor$Type} from "packages/net/minecraft/world/level/material/$MapColor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$BlockShapeType, $BlockShapeType$Type} from "packages/snownee/kiwi/customization/shape/$BlockShapeType"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$GlassType, $GlassType$Type} from "packages/snownee/kiwi/customization/block/$GlassType"
import {$KBlockSettings$Builder, $KBlockSettings$Builder$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings$Builder"
import {$CanSurviveHandler, $CanSurviveHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$ConfiguringShape, $ConfiguringShape$Type} from "packages/snownee/kiwi/customization/shape/$ConfiguringShape"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KBlockSettings {
readonly "customPlacement": boolean
readonly "glassType": $GlassType
readonly "canSurviveHandler": $CanSurviveHandler
readonly "analogOutputSignal": $ToIntFunction<($BlockState)>
readonly "components": $Map<($KBlockComponent$Type<(any)>), ($KBlockComponent)>
 "placeChoices": $PlaceChoices


public static "of"(arg0: any): $KBlockSettings
public static "builder"(): $KBlockSettings$Builder
public static "empty"(): $KBlockSettings
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getShape"(arg0: $BlockShapeType$Type): $ShapeGenerator
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getComponent"<T extends $KBlockComponent>(arg0: $KBlockComponent$Type$Type<(T)>): T
public "hasComponent"(arg0: $KBlockComponent$Type$Type<(any)>): boolean
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public static "copyProperties"(arg0: $Block$Type): $KBlockSettings$Builder
public static "copyProperties"(arg0: $Block$Type, arg1: $MapColor$Type): $KBlockSettings$Builder
public "getStateForPlacement"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): $BlockState
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public static "getGlassFaceShape"(arg0: $BlockState$Type, arg1: $Direction$Type): $VoxelShape
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "removeIfPossible"(arg0: $BlockShapeType$Type): $ConfiguringShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockSettings$Type = ($KBlockSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockSettings_ = $KBlockSettings$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent" {
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $CollectionEndEvent extends $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionEndEvent$Type = ($CollectionEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionEndEvent_ = $CollectionEndEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/reader/$ReaderException" {
import {$YAMLException, $YAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $ReaderException extends $YAMLException {

constructor(arg0: string, arg1: integer, arg2: integer, arg3: string)

public "getName"(): string
public "toString"(): string
public "getCodePoint"(): integer
public "getPosition"(): integer
get "name"(): string
get "codePoint"(): integer
get "position"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReaderException$Type = ($ReaderException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReaderException_ = $ReaderException$Type;
}}
declare module "packages/snownee/kiwi/contributor/$Contributors" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ITierProvider, $ITierProvider$Type} from "packages/snownee/kiwi/contributor/$ITierProvider"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Contributors extends $AbstractModule {
static readonly "REWARD_PROVIDERS": $Map<(string), ($ITierProvider)>
static readonly "PLAYER_COSMETICS": $Map<(string), ($ResourceLocation)>
 "uid": $ResourceLocation

constructor()

public static "registerTierProvider"(arg0: $ITierProvider$Type): void
public static "canPlayerUseCosmetic"(arg0: string, arg1: $ResourceLocation$Type): $CompletableFuture<(boolean)>
public static "changeCosmetic"(): void
public static "changeCosmetic"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
public static "changeCosmetic"(arg0: $Map$Type<(string), ($ResourceLocation$Type)>): void
public static "isRenderable"(arg0: $ResourceLocation$Type): boolean
public static "getTiers"(): $Set<($ResourceLocation)>
public static "getPlayerTiers"(arg0: string): $Set<($ResourceLocation)>
public static "isContributor"(arg0: string, arg1: $Player$Type): boolean
public static "isContributor"(arg0: string, arg1: $Player$Type, arg2: string): boolean
public static "isContributor"(arg0: string, arg1: string): boolean
public static "isContributor"(arg0: string, arg1: string, arg2: string): boolean
public static "getRenderableTiers"(): $Set<($ResourceLocation)>
get "tiers"(): $Set<($ResourceLocation)>
get "renderableTiers"(): $Set<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Contributors$Type = ($Contributors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Contributors_ = $Contributors$Type;
}}
declare module "packages/snownee/kiwi/recipe/$Simple" {
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

export class $Simple<C extends $Container> implements $Recipe<(C)> {

constructor(arg0: $ResourceLocation$Type)

public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "assemble"(arg0: C, arg1: $RegistryAccess$Type): $ItemStack
public "getId"(): $ResourceLocation
public "getRemainingItems"(arg0: C): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
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
get "id"(): $ResourceLocation
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "serializer"(): $RecipeSerializer<(any)>
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
export type $Simple$Type<C> = ($Simple<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Simple_<C> = $Simple$Type<(C)>;
}}
declare module "packages/snownee/kiwi/customization/block/component/$StackableComponent" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$LayeredComponent, $LayeredComponent$Type} from "packages/snownee/kiwi/customization/block/component/$LayeredComponent"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $StackableComponent extends $Record implements $KBlockComponent, $LayeredComponent {
static readonly "CODEC": $Codec<($StackableComponent)>

constructor(property: $IntegerProperty$Type)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: integer): $StackableComponent
public static "create"(arg0: integer, arg1: integer): $StackableComponent
public "maxValue"(): integer
public "property"(): $IntegerProperty
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "minValue"(): integer
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "getLayerProperty"(): $IntegerProperty
public "getDefaultLayer"(): integer
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
get "layerProperty"(): $IntegerProperty
get "defaultLayer"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackableComponent$Type = ($StackableComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackableComponent_ = $StackableComponent$Type;
}}
declare module "packages/snownee/kiwi/network/$Networking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$PacketDistributor, $PacketDistributor$Type} from "packages/net/minecraftforge/network/$PacketDistributor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IPacketHandler, $IPacketHandler$Type} from "packages/snownee/kiwi/network/$IPacketHandler"

export class $Networking {
static readonly "ALL_EXCEPT": $PacketDistributor<($ServerPlayer)>


public static "sendToPlayer"(arg0: $ServerPlayer$Type, arg1: $FriendlyByteBuf$Type): void
public static "processClass"(arg0: string, arg1: string): void
public static "registerHandler"(arg0: $ResourceLocation$Type, arg1: $IPacketHandler$Type): void
public static "send"(arg0: $PacketDistributor$PacketTarget$Type, arg1: $FriendlyByteBuf$Type): void
public static "sendToServer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Networking$Type = ($Networking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Networking_ = $Networking$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$EvaluationException" {
import {$BaseException, $BaseException$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$BaseException"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $EvaluationException extends $BaseException {

constructor(arg0: $Token$Type, arg1: string)

public static "ofUnsupportedDataTypeInOperation"(arg0: $Token$Type): $EvaluationException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvaluationException$Type = ($EvaluationException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvaluationException_ = $EvaluationException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/util/$ArrayStack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ArrayStack<T> {

constructor(arg0: integer)

public "clear"(): void
public "isEmpty"(): boolean
public "push"(arg0: T): void
public "pop"(): T
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayStack$Type<T> = ($ArrayStack<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayStack_<T> = $ArrayStack$Type<(T)>;
}}
declare module "packages/snownee/kiwi/util/$EnumUtil" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $EnumUtil {
static readonly "DIRECTIONS": ($Direction)[]
static readonly "HORIZONTAL_DIRECTIONS": ($Direction)[]
static "BLOCK_RENDER_TYPES": $Set<($RenderType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumUtil$Type = ($EnumUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumUtil_ = $EnumUtil$Type;
}}
declare module "packages/snownee/kiwi/handler/$Battery" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EnergyStorage, $EnergyStorage$Type} from "packages/net/minecraftforge/energy/$EnergyStorage"

export class $Battery extends $EnergyStorage {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "readFromNBT"(arg0: $CompoundTag$Type): $Battery
public "setEnergy"(arg0: integer): void
public "writeToNBT"(arg0: $CompoundTag$Type): $CompoundTag
public "receiveEnergy"(arg0: integer, arg1: boolean): integer
public "extractEnergy"(arg0: integer, arg1: boolean): integer
set "energy"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Battery$Type = ($Battery);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Battery_ = $Battery$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor" {
import {$TypeDescription, $TypeDescription$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$PropertyUtils, $PropertyUtils$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$Composer, $Composer$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/composer/$Composer"

export class $BaseConstructor {

constructor(arg0: $LoaderOptions$Type)

public "getData"(): any
public "checkData"(): boolean
public "getPropertyUtils"(): $PropertyUtils
public "setPropertyUtils"(arg0: $PropertyUtils$Type): void
public "getLoadingConfig"(): $LoaderOptions
public "getSingleData"(arg0: $Class$Type<(any)>): any
public "setComposer"(arg0: $Composer$Type): void
public "addTypeDescription"(arg0: $TypeDescription$Type): $TypeDescription
public "isAllowDuplicateKeys"(): boolean
public "isWrappedToRootException"(): boolean
public "setWrappedToRootException"(arg0: boolean): void
public "setAllowDuplicateKeys"(arg0: boolean): void
public "isExplicitPropertyUtils"(): boolean
public "isEnumCaseSensitive"(): boolean
public "setEnumCaseSensitive"(arg0: boolean): void
get "data"(): any
get "propertyUtils"(): $PropertyUtils
set "propertyUtils"(value: $PropertyUtils$Type)
get "loadingConfig"(): $LoaderOptions
set "composer"(value: $Composer$Type)
get "allowDuplicateKeys"(): boolean
get "wrappedToRootException"(): boolean
set "wrappedToRootException"(value: boolean)
set "allowDuplicateKeys"(value: boolean)
get "explicitPropertyUtils"(): boolean
get "enumCaseSensitive"(): boolean
set "enumCaseSensitive"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseConstructor$Type = ($BaseConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseConstructor_ = $BaseConstructor$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$AbsFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AbsFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbsFunction$Type = ($AbsFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbsFunction_ = $AbsFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DurationFromMillisFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DurationFromMillisFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DurationFromMillisFunction$Type = ($DurationFromMillisFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DurationFromMillisFunction_ = $DurationFromMillisFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/family/$BlockFamily$SwitchAttrs" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BlockFamily$SwitchAttrs extends $Record {
static readonly "CODEC": $Codec<($BlockFamily$SwitchAttrs)>
static readonly "DISABLED": $BlockFamily$SwitchAttrs

constructor(enabled: boolean, cascading: boolean, creativeOnly: boolean)

public "creativeOnly"(): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "enabled"(): boolean
public static "create"(arg0: boolean, arg1: boolean, arg2: boolean): $BlockFamily$SwitchAttrs
public "cascading"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFamily$SwitchAttrs$Type = ($BlockFamily$SwitchAttrs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFamily$SwitchAttrs_ = $BlockFamily$SwitchAttrs$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$ExportShapesCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $ExportShapesCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExportShapesCommand$Type = ($ExportShapesCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExportShapesCommand_ = $ExportShapesCommand$Type;
}}
declare module "packages/snownee/kiwi/item/$ModBlockItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$ItemCategoryFiller, $ItemCategoryFiller$Type} from "packages/snownee/kiwi/item/$ItemCategoryFiller"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModBlockItem extends $BlockItem implements $ItemCategoryFiller {
static readonly "INSTANT_UPDATE_TILES": $Set<($BlockEntityType<(any)>)>
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

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type)

public "getName"(arg0: $ItemStack$Type): $Component
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "fillItemCategory"(arg0: $CreativeModeTab$Type, arg1: $FeatureFlagSet$Type, arg2: boolean, arg3: $List$Type<($ItemStack$Type)>): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockItem$Type = ($ModBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockItem_ = $ModBlockItem$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CscRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CscRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CscRFunction$Type = ($CscRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CscRFunction_ = $CscRFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition" {
import {$FunctionParameterDefinition$FunctionParameterDefinitionBuilder, $FunctionParameterDefinition$FunctionParameterDefinitionBuilder$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition$FunctionParameterDefinitionBuilder"

export class $FunctionParameterDefinition {


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "builder"(): $FunctionParameterDefinition$FunctionParameterDefinitionBuilder
public "isLazy"(): boolean
public "isVarArg"(): boolean
public "isNonNegative"(): boolean
public "isNonZero"(): boolean
get "name"(): string
get "lazy"(): boolean
get "varArg"(): boolean
get "nonNegative"(): boolean
get "nonZero"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionParameterDefinition$Type = ($FunctionParameterDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionParameterDefinition_ = $FunctionParameterDefinition$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$ConfiguredBlockTemplate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockTemplate, $KBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfiguredBlockTemplate extends $Record {
static readonly "DEFAULT_JSON": $JsonObject

constructor(arg0: $KBlockTemplate$Type)
constructor(template: $KBlockTemplate$Type, json: $JsonObject$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "json"(): $JsonObject
public "template"(): $KBlockTemplate
public static "codec"(arg0: $Map$Type<($ResourceLocation$Type), ($KBlockTemplate$Type)>): $Codec<($ConfiguredBlockTemplate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguredBlockTemplate$Type = ($ConfiguredBlockTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguredBlockTemplate_ = $ConfiguredBlockTemplate$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/composer/$Composer" {
import {$Resolver, $Resolver$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/resolver/$Resolver"
import {$Parser, $Parser$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$Parser"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $Composer {

constructor(arg0: $Parser$Type, arg1: $Resolver$Type, arg2: $LoaderOptions$Type)

public "getNode"(): $Node
public "getSingleNode"(): $Node
public "checkNode"(): boolean
get "node"(): $Node
get "singleNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Composer$Type = ($Composer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Composer_ = $Composer$Type;
}}
declare module "packages/snownee/kiwi/customization/block/family/$StonecutterRecipeMaker" {
import {$BlockFamily, $BlockFamily$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$StonecutterRecipe, $StonecutterRecipe$Type} from "packages/net/minecraft/world/item/crafting/$StonecutterRecipe"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $StonecutterRecipeMaker {

constructor()

public static "invalidateCache"(): void
public static "appendRecipesFor"(arg0: $List$Type<($StonecutterRecipe$Type)>, arg1: $Container$Type): $List<($StonecutterRecipe)>
public static "makeRecipes"(arg0: string, arg1: $KHolder$Type<($BlockFamily$Type)>): $List<($StonecutterRecipe)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StonecutterRecipeMaker$Type = ($StonecutterRecipeMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StonecutterRecipeMaker_ = $StonecutterRecipeMaker$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$PrefixOperator" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $PrefixOperator extends $Annotation {

 "precedence"(): integer
 "leftAssociative"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $PrefixOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixOperator$Type = ($PrefixOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixOperator_ = $PrefixOperator$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ConfiguringShape" {
import {$BakingContext, $BakingContext$Type} from "packages/snownee/kiwi/customization/shape/$BakingContext"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockShapeType, $BlockShapeType$Type} from "packages/snownee/kiwi/customization/shape/$BlockShapeType"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $ConfiguringShape extends $ShapeGenerator, $UnbakedShape {

 "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
 "configure"(arg0: $Block$Type, arg1: $BlockShapeType$Type): void
 "dependencies"(): $Stream<($UnbakedShape)>
 "bake"(arg0: $BakingContext$Type): $ShapeGenerator

(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
}

export namespace $ConfiguringShape {
function unit(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguringShape$Type = ($ConfiguringShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguringShape_ = $ConfiguringShape$Type;
}}
declare module "packages/snownee/kiwi/util/$LerpedFloat$Chaser" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LerpedFloat$Chaser {

 "chase"(arg0: double, arg1: double, arg2: double): float

(arg0: double): $LerpedFloat$Chaser
}

export namespace $LerpedFloat$Chaser {
const IDLE: $LerpedFloat$Chaser
const EXP: $LerpedFloat$Chaser
const LINEAR: $LerpedFloat$Chaser
function exp(arg0: double): $LerpedFloat$Chaser
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LerpedFloat$Chaser$Type = ($LerpedFloat$Chaser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LerpedFloat$Chaser_ = $LerpedFloat$Chaser$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$MouldingShape" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $MouldingShape extends $Record implements $ShapeGenerator {

constructor(shapes: ($VoxelShape$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $ShapeGenerator$Type): $ShapeGenerator
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "shapes"(): ($VoxelShape)[]
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouldingShape$Type = ($MouldingShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouldingShape_ = $MouldingShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CosHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CosHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosHFunction$Type = ($CosHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosHFunction_ = $CosHFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/network/$SSyncPlaceCountPacket" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $SSyncPlaceCountPacket extends $PacketHandler {
static "I": $SSyncPlaceCountPacket
 "id": $ResourceLocation

constructor()

public static "sync"(arg0: $ServerPlayer$Type): void
public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSyncPlaceCountPacket$Type = ($SSyncPlaceCountPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSyncPlaceCountPacket_ = $SSyncPlaceCountPacket$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomizationRegistries" {
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$KBlockTemplate$Type, $KBlockTemplate$Type$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type"

export class $CustomizationRegistries {
static readonly "BLOCK_COMPONENT_KEY": $ResourceKey<($Registry<($KBlockComponent$Type<(any)>)>)>
static "BLOCK_COMPONENT": $Registry<($KBlockComponent$Type<(any)>)>
static readonly "BLOCK_TEMPLATE_KEY": $ResourceKey<($Registry<($KBlockTemplate$Type<(any)>)>)>
static "BLOCK_TEMPLATE": $Registry<($KBlockTemplate$Type<(any)>)>
static readonly "ITEM_TEMPLATE_KEY": $ResourceKey<($Registry<($KItemTemplate$Type<(any)>)>)>
static "ITEM_TEMPLATE": $Registry<($KItemTemplate$Type<(any)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationRegistries$Type = ($CustomizationRegistries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationRegistries_ = $CustomizationRegistries$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$BaseConstructor, $BaseConstructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions"

export class $SafeConstructor extends $BaseConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(arg0: $LoaderOptions$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeConstructor$Type = ($SafeConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeConstructor_ = $SafeConstructor$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$SitManager" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Display$BlockDisplay, $Display$BlockDisplay$Type} from "packages/net/minecraft/world/entity/$Display$BlockDisplay"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $SitManager {
static readonly "ENTITY_NAME": $Component
static readonly "VERTICAL_OFFSET": double

constructor()

public static "tick"(arg0: $Display$BlockDisplay$Type): void
public static "dismount"(arg0: $Entity$Type, arg1: $LivingEntity$Type): $Vec3
public static "clampRotation"(arg0: $Player$Type, arg1: $Entity$Type): void
public static "sit"(arg0: $Player$Type, arg1: $BlockHitResult$Type): boolean
public static "isSeatEntity"(arg0: $Entity$Type): boolean
public static "guessBlockFacing"(arg0: $BlockState$Type, arg1: $Player$Type): $Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SitManager$Type = ($SitManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SitManager_ = $SitManager$Type;
}}
declare module "packages/snownee/kiwi/customization/block/$BasicBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$KBlockUtils, $KBlockUtils$Type} from "packages/snownee/kiwi/customization/block/$KBlockUtils"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CheckedWaterloggedBlock, $CheckedWaterloggedBlock$Type} from "packages/snownee/kiwi/customization/block/$CheckedWaterloggedBlock"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $BasicBlock extends $Block implements $CheckedWaterloggedBlock, $KBlockUtils {
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
public "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
public "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
public static "getProperty"(arg0: $BlockState$Type, arg1: string): $Property<(any)>
public static "getValueString"<T extends $Comparable<(T)>>(arg0: $BlockState$Type, arg1: string): string
public static "internProperty"<T extends $Property<(any)>>(arg0: T): T
public static "setValueByString"<T extends $Comparable<(T)>>(arg0: $BlockState$Type, arg1: string, arg2: string): $BlockState
public static "getNameByValue"<T extends $Comparable<(T)>>(arg0: $Property$Type<(T)>, arg1: any): string
public static "generateCommonProperties"(): $BiMap<(string), ($Property<(any)>)>
public "componentsGetStateForPlacement"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): $BlockState
public "componentsUpdateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "getPickupSound"(): $Optional<($SoundEvent)>
public "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
get "pickupSound"(): $Optional<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicBlock$Type = ($BasicBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicBlock_ = $BasicBlock$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$ReloadSlotsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$BlockFundamentals, $BlockFundamentals$Type} from "packages/snownee/kiwi/customization/block/$BlockFundamentals"

export class $ReloadSlotsCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "reload"(arg0: $BlockFundamentals$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadSlotsCommand$Type = ($ReloadSlotsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadSlotsCommand_ = $ReloadSlotsCommand$Type;
}}
declare module "packages/snownee/kiwi/customization/block/family/$BlockFamily" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockFamily$SwitchAttrs, $BlockFamily$SwitchAttrs$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily$SwitchAttrs"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BlockFamily {
static readonly "CODEC": $Codec<($BlockFamily)>

constructor(arg0: boolean, arg1: $List$Type<($ResourceKey$Type<($Block$Type)>)>, arg2: $List$Type<($ResourceKey$Type<($Item$Type)>)>, arg3: $List$Type<($ResourceKey$Type<($Item$Type)>)>, arg4: boolean, arg5: $Optional$Type<($ResourceKey$Type<($Item$Type)>)>, arg6: integer, arg7: $BlockFamily$SwitchAttrs$Type)

public "toString"(): string
public "contains"(arg0: $Item$Type): boolean
public "items"(): $Stream<($Item)>
public "blocks"(): $Stream<($Block)>
public "blockHolders"(): $List<($Holder$Reference<($Block)>)>
public "switchAttrs"(): $BlockFamily$SwitchAttrs
public "stonecutterExchange"(): boolean
public "ingredientInViewer"(): $Ingredient
public "stonecutterSource"(): $Optional<($Holder$Reference<($Item)>)>
public "ingredient"(): $Ingredient
public "itemHolders"(): $List<($Holder$Reference<($Item)>)>
public "exchangeInputsInViewer"(): $List<($Holder$Reference<($Item)>)>
public "stonecutterSourceMultiplier"(): integer
public "stonecutterSourceIngredient"(): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockFamily$Type = ($BlockFamily);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockFamily_ = $BlockFamily$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$NoContainersShapedRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$NoContainersShapedRecipe, $NoContainersShapedRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$NoContainersShapedRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $NoContainersShapedRecipe$Serializer implements $RecipeSerializer<($NoContainersShapedRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $NoContainersShapedRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $NoContainersShapedRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $NoContainersShapedRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $NoContainersShapedRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoContainersShapedRecipe$Serializer$Type = ($NoContainersShapedRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoContainersShapedRecipe$Serializer_ = $NoContainersShapedRecipe$Serializer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction" {
import {$FunctionParameterDefinition, $FunctionParameterDefinition$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition"
import {$FunctionIfc, $FunctionIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"

export class $AbstractFunction implements $FunctionIfc {


public "hasVarArgs"(): boolean
public "getFunctionParameterDefinitions"(): $List<($FunctionParameterDefinition)>
public "validatePreEvaluation"(arg0: $Token$Type, ...arg1: ($EvaluationValue$Type)[]): void
public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
public "isParameterLazy"(arg0: integer): boolean
public "getCountOfNonVarArgParameters"(): integer
get "functionParameterDefinitions"(): $List<($FunctionParameterDefinition)>
get "countOfNonVarArgParameters"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFunction$Type = ($AbstractFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFunction_ = $AbstractFunction$Type;
}}
declare module "packages/snownee/kiwi/util/$VoxelUtil" {
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BooleanOp, $BooleanOp$Type} from "packages/net/minecraft/world/phys/shapes/$BooleanOp"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $VoxelUtil {


public static "combine"(...arg0: ($VoxelShape$Type)[]): $VoxelShape
public static "combine"(arg0: $Collection$Type<($VoxelShape$Type)>): $VoxelShape
public static "rotate"(arg0: $AABB$Type, arg1: $Rotation$Type): $AABB
public static "rotate"(arg0: $VoxelShape$Type, arg1: $UnaryOperator$Type<($AABB$Type)>): $VoxelShape
public static "rotate"(arg0: $AABB$Type, arg1: $Direction$Type): $AABB
public static "rotate"(arg0: $VoxelShape$Type, arg1: $Rotation$Type): $VoxelShape
public static "rotate"(arg0: $VoxelShape$Type, arg1: $Direction$Type): $VoxelShape
public static "exclude"(...arg0: ($VoxelShape$Type)[]): $VoxelShape
public static "setShape"(arg0: $VoxelShape$Type, arg1: ($VoxelShape$Type)[], arg2: boolean): void
public static "setShape"(arg0: $VoxelShape$Type, arg1: ($VoxelShape$Type)[], arg2: boolean, arg3: boolean): void
public static "setShape"(arg0: $VoxelShape$Type, arg1: ($VoxelShape$Type)[]): void
public static "rotateHorizontal"(arg0: $VoxelShape$Type, arg1: $Direction$Type): $VoxelShape
public static "rotateHorizontal"(arg0: $AABB$Type, arg1: $Direction$Type): $AABB
public static "isIsotropicHorizontally"(arg0: $VoxelShape$Type): boolean
public static "batchCombine"(arg0: $VoxelShape$Type, arg1: $BooleanOp$Type, arg2: boolean, ...arg3: ($VoxelShape$Type)[]): $VoxelShape
public static "batchCombine"(arg0: $VoxelShape$Type, arg1: $BooleanOp$Type, arg2: boolean, arg3: $Collection$Type<($VoxelShape$Type)>): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelUtil$Type = ($VoxelUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelUtil_ = $VoxelUtil$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CommentType extends $Enum<($CommentType)> {
static readonly "BLANK_LINE": $CommentType
static readonly "BLOCK": $CommentType
static readonly "IN_LINE": $CommentType


public static "values"(): ($CommentType)[]
public static "valueOf"(arg0: string): $CommentType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentType$Type = (("blank_line") | ("block") | ("in_line")) | ($CommentType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentType_ = $CommentType$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/layer/$SunnyMilkLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $SunnyMilkLayer extends $CosmeticLayer {
static readonly "ALL_LAYERS": $Collection<($CosmeticLayer)>
readonly "f_117344_": $RenderLayerParent<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)>

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SunnyMilkLayer$Type = ($SunnyMilkLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SunnyMilkLayer_ = $SunnyMilkLayer$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$BakingContext" {
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BakingContext {

 "getShape"(arg0: $ResourceLocation$Type): $ShapeGenerator

(arg0: $ResourceLocation$Type): $ShapeGenerator
}

export namespace $BakingContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakingContext$Type = ($BakingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakingContext_ = $BakingContext$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixModuloOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixModuloOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixModuloOperator$Type = ($InfixModuloOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixModuloOperator_ = $InfixModuloOperator$Type;
}}
declare module "packages/snownee/kiwi/util/$NBTHelper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"
import {$ListTag, $ListTag$Type} from "packages/net/minecraft/nbt/$ListTag"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$GameProfile, $GameProfile$Type} from "packages/com/mojang/authlib/$GameProfile"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $NBTHelper {


public "remove"(arg0: string): $NBTHelper
public "get"(): $CompoundTag
public "getBoolean"(arg0: string, arg1: boolean): boolean
public "getBoolean"(arg0: string): boolean
public "getByte"(arg0: string, arg1: byte): byte
public "getByte"(arg0: string): byte
public "getShort"(arg0: string): short
public "getShort"(arg0: string, arg1: short): short
public "getInt"(arg0: string, arg1: integer): integer
public "getInt"(arg0: string): integer
public "getLong"(arg0: string, arg1: long): long
public "getLong"(arg0: string): long
public "getFloat"(arg0: string): float
public "getFloat"(arg0: string, arg1: float): float
public "getDouble"(arg0: string, arg1: double): double
public "getDouble"(arg0: string): double
public static "of"(arg0: $ItemStack$Type): $NBTHelper
public static "of"(arg0: $CompoundTag$Type): $NBTHelper
public "keySet"(arg0: string): $Set<(string)>
public "setBoolean"(arg0: string, arg1: boolean): $NBTHelper
public "setByte"(arg0: string, arg1: byte): $NBTHelper
public "setShort"(arg0: string, arg1: short): $NBTHelper
public "setInt"(arg0: string, arg1: integer): $NBTHelper
public "setLong"(arg0: string, arg1: long): $NBTHelper
public "setFloat"(arg0: string, arg1: float): $NBTHelper
public "setDouble"(arg0: string, arg1: double): $NBTHelper
public static "create"(): $NBTHelper
public "getString"(arg0: string, arg1: string): string
public "getString"(arg0: string): string
public "getItem"(): $ItemStack
public "getTag"(arg0: string): $CompoundTag
public "getTag"(arg0: string, arg1: boolean): $CompoundTag
public "setPos"(arg0: string, arg1: $BlockPos$Type): $NBTHelper
public "getIntArray"(arg0: string): (integer)[]
public "setString"(arg0: string, arg1: string): $NBTHelper
public "hasTag"(arg0: string, arg1: integer): boolean
public "getBlockState"(arg0: string): $BlockState
public "getByteArray"(arg0: string): (byte)[]
public "getUUID"(arg0: string): $UUID
public "setBlockState"(arg0: string, arg1: $BlockState$Type): $NBTHelper
public "getPos"(arg0: string): $BlockPos
public "setTag"(arg0: string, arg1: $Tag$Type): $NBTHelper
public "setUUID"(arg0: string, arg1: $UUID$Type): $NBTHelper
public "getGameProfile"(arg0: string): $GameProfile
public "setGameProfile"(arg0: string, arg1: $GameProfile$Type): $NBTHelper
public "setIntArray"(arg0: string, arg1: (integer)[]): $NBTHelper
public "getTagList"(arg0: string, arg1: integer): $ListTag
public "setByteArray"(arg0: string, arg1: (byte)[]): $NBTHelper
get "item"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTHelper$Type = ($NBTHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTHelper_ = $NBTHelper$Type;
}}
declare module "packages/snownee/kiwi/util/$LerpedFloat" {
import {$LerpedFloat$Interpolator, $LerpedFloat$Interpolator$Type} from "packages/snownee/kiwi/util/$LerpedFloat$Interpolator"
import {$LerpedFloat$Chaser, $LerpedFloat$Chaser$Type} from "packages/snownee/kiwi/util/$LerpedFloat$Chaser"

export class $LerpedFloat {

constructor(arg0: $LerpedFloat$Interpolator$Type)

public "getValue"(arg0: float): float
public "getValue"(): float
public "setValue"(arg0: double): void
public "settled"(): boolean
public "startWithValue"(arg0: double): $LerpedFloat
public "tickChaser"(): void
public "chase"(arg0: double, arg1: double, arg2: $LerpedFloat$Chaser$Type): $LerpedFloat
public "updateChaseTarget"(arg0: float): void
public "updateChaseSpeed"(arg0: double): boolean
public "setValueNoUpdate"(arg0: double): void
public "forceNextSync"(): void
public "getChaseTarget"(): float
public static "linear"(): $LerpedFloat
get "value"(): float
set "value"(value: double)
set "valueNoUpdate"(value: double)
get "chaseTarget"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LerpedFloat$Type = ($LerpedFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LerpedFloat_ = $LerpedFloat$Type;
}}
declare module "packages/snownee/kiwi/customization/item/$KItemSettings" {
import {$KItemSettings$Builder, $KItemSettings$Builder$Type} from "packages/snownee/kiwi/customization/item/$KItemSettings$Builder"

export class $KItemSettings {


public static "builder"(): $KItemSettings$Builder
public static "empty"(): $KItemSettings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemSettings$Type = ($KItemSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemSettings_ = $KItemSettings$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$MapBasedFunctionDictionary" {
import {$FunctionIfc, $FunctionIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc"
import {$FunctionDictionaryIfc, $FunctionDictionaryIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$FunctionDictionaryIfc"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $MapBasedFunctionDictionary implements $FunctionDictionaryIfc {

constructor()

public "getFunction"(arg0: string): $FunctionIfc
public "addFunction"(arg0: string, arg1: $FunctionIfc$Type): void
public static "ofFunctions"(...arg0: ($Map$Entry$Type<(string), ($FunctionIfc$Type)>)[]): $FunctionDictionaryIfc
public "hasFunction"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapBasedFunctionDictionary$Type = ($MapBasedFunctionDictionary);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapBasedFunctionDictionary_ = $MapBasedFunctionDictionary$Type;
}}
declare module "packages/snownee/kiwi/schedule/impl/$SimpleGlobalTask" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$GlobalTicker, $GlobalTicker$Type} from "packages/snownee/kiwi/schedule/impl/$GlobalTicker"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"
import {$TickEvent$Phase, $TickEvent$Phase$Type} from "packages/net/minecraftforge/event/$TickEvent$Phase"
import {$Task, $Task$Type} from "packages/snownee/kiwi/schedule/$Task"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"

export class $SimpleGlobalTask extends $Task<($GlobalTicker)> implements $INBTSerializable<($CompoundTag)> {

constructor()
constructor(arg0: $LogicalSide$Type, arg1: $TickEvent$Phase$Type, arg2: $IntPredicate$Type)

public "tick"(arg0: $GlobalTicker$Type): boolean
public "shouldSave"(): boolean
public "deserializeNBT"(arg0: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleGlobalTask$Type = ($SimpleGlobalTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleGlobalTask_ = $SimpleGlobalTask$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$OperatorIfc, $OperatorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$OperatorIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AbstractOperator implements $OperatorIfc {


public "getPrecedence"(): integer
public "getPrecedence"(arg0: $ExpressionConfiguration$Type): integer
public "isLeftAssociative"(): boolean
public "isOperandLazy"(): boolean
public "isInfix"(): boolean
public "isPrefix"(): boolean
public "isPostfix"(): boolean
public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
get "precedence"(): integer
get "leftAssociative"(): boolean
get "operandLazy"(): boolean
get "infix"(): boolean
get "prefix"(): boolean
get "postfix"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractOperator$Type = ($AbstractOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractOperator_ = $AbstractOperator$Type;
}}
declare module "packages/snownee/kiwi/util/resource/$AlternativesFileToIdConverter" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Resource, $Resource$Type} from "packages/net/minecraft/server/packs/resources/$Resource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AlternativesFileToIdConverter {

constructor(arg0: string, arg1: $List$Type<(string)>)

public "fileToId"(arg0: $ResourceLocation$Type): $ResourceLocation
public "idToAllPossibleFiles"(arg0: $ResourceLocation$Type): $Stream<($ResourceLocation)>
public "listMatchingResourceStacks"(arg0: $ResourceManager$Type): $Map<($ResourceLocation), ($List<($Resource)>)>
public "listMatchingResources"(arg0: $ResourceManager$Type): $Map<($ResourceLocation), ($Resource)>
public "idToFile"(arg0: $ResourceLocation$Type): $ResourceLocation
public static "yamlOrJson"(arg0: string): $AlternativesFileToIdConverter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlternativesFileToIdConverter$Type = ($AlternativesFileToIdConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlternativesFileToIdConverter_ = $AlternativesFileToIdConverter$Type;
}}
declare module "packages/snownee/kiwi/recipe/$AlternativesIngredient$Serializer" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IIngredientSerializer, $IIngredientSerializer$Type} from "packages/net/minecraftforge/common/crafting/$IIngredientSerializer"
import {$AlternativesIngredient, $AlternativesIngredient$Type} from "packages/snownee/kiwi/recipe/$AlternativesIngredient"

export class $AlternativesIngredient$Serializer implements $IIngredientSerializer<($AlternativesIngredient)> {

constructor()

public "write"(arg0: $FriendlyByteBuf$Type, arg1: $AlternativesIngredient$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlternativesIngredient$Serializer$Type = ($AlternativesIngredient$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlternativesIngredient$Serializer_ = $AlternativesIngredient$Serializer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/representer/$BaseRepresenter" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$PropertyUtils, $PropertyUtils$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $BaseRepresenter {

constructor()

public "getPropertyUtils"(): $PropertyUtils
public "setPropertyUtils"(arg0: $PropertyUtils$Type): void
public "represent"(arg0: any): $Node
public "setDefaultScalarStyle"(arg0: $DumperOptions$ScalarStyle$Type): void
public "getDefaultFlowStyle"(): $DumperOptions$FlowStyle
public "getDefaultScalarStyle"(): $DumperOptions$ScalarStyle
public "isExplicitPropertyUtils"(): boolean
public "setDefaultFlowStyle"(arg0: $DumperOptions$FlowStyle$Type): void
get "propertyUtils"(): $PropertyUtils
set "propertyUtils"(value: $PropertyUtils$Type)
set "defaultScalarStyle"(value: $DumperOptions$ScalarStyle$Type)
get "defaultFlowStyle"(): $DumperOptions$FlowStyle
get "defaultScalarStyle"(): $DumperOptions$ScalarStyle
get "explicitPropertyUtils"(): boolean
set "defaultFlowStyle"(value: $DumperOptions$FlowStyle$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseRepresenter$Type = ($BaseRepresenter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseRepresenter_ = $BaseRepresenter$Type;
}}
declare module "packages/snownee/kiwi/$GroupSetting" {
import {$KiwiModule$Category, $KiwiModule$Category$Type} from "packages/snownee/kiwi/$KiwiModule$Category"
import {$ItemCategoryFiller, $ItemCategoryFiller$Type} from "packages/snownee/kiwi/item/$ItemCategoryFiller"

export class $GroupSetting {

constructor(arg0: (string)[], arg1: (string)[])

public "apply"(arg0: $ItemCategoryFiller$Type): void
public static "of"(arg0: $KiwiModule$Category$Type, arg1: $GroupSetting$Type): $GroupSetting
public "postApply"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GroupSetting$Type = ($GroupSetting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GroupSetting_ = $GroupSetting$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ExpressionNodeConverter" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$ConverterIfc, $ConverterIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/conversion/$ConverterIfc"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$IllegalArgumentException, $IllegalArgumentException$Type} from "packages/java/lang/$IllegalArgumentException"

export class $ExpressionNodeConverter implements $ConverterIfc {

constructor()

public "convert"(arg0: any, arg1: $ExpressionConfiguration$Type): $EvaluationValue
public "canConvert"(arg0: any): boolean
public "illegalArgument"(arg0: any): $IllegalArgumentException
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpressionNodeConverter$Type = ($ExpressionNodeConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpressionNodeConverter_ = $ExpressionNodeConverter$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$RadFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $RadFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RadFunction$Type = ($RadFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RadFunction_ = $RadFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices" {
import {$PlaceChoices$Interests, $PlaceChoices$Interests$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Interests"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$PlaceChoices$BlockFaceType, $PlaceChoices$BlockFaceType$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$BlockFaceType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlaceChoices$Limit, $PlaceChoices$Limit$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Limit"
import {$PlaceChoices$Alter, $PlaceChoices$Alter$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Alter"
import {$PlaceTarget, $PlaceTarget$Type} from "packages/snownee/kiwi/customization/placement/$PlaceTarget"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$PlaceChoices$Flow, $PlaceChoices$Flow$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Flow"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $PlaceChoices extends $Record {
static readonly "BLOCK_FACE_TYPES": $BiMap<(string), ($PlaceChoices$BlockFaceType)>
static readonly "CODEC": $Codec<($PlaceChoices)>

constructor(target: $List$Type<($PlaceTarget$Type)>, transformWith: $Optional$Type<(string)>, flow: $List$Type<($PlaceChoices$Flow$Type)>, alter: $List$Type<($PlaceChoices$Alter$Type)>, limit: $List$Type<($PlaceChoices$Limit$Type)>, interests: $List$Type<($PlaceChoices$Interests$Type)>, skippable: boolean)

public "equals"(arg0: any): boolean
public "target"(): $List<($PlaceTarget)>
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $BlockState$Type, arg1: $BlockState$Type): integer
public "limit"(): $List<($PlaceChoices$Limit)>
public "transformWith"(): $Optional<(string)>
public "interests"(): $List<($PlaceChoices$Interests)>
public "flow"(): $List<($PlaceChoices$Flow)>
public "getStateForPlacement"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $BlockState
public static "setTo"(arg0: $Block$Type, arg1: $KHolder$Type<($PlaceChoices$Type)>): void
public "skippable"(): boolean
public "alter"(): $List<($PlaceChoices$Alter)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Type = ($PlaceChoices);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices_ = $PlaceChoices$Type;
}}
declare module "packages/snownee/kiwi/util/$CachedSupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CachedSupplier<T> implements $Supplier<(T)> {

constructor(arg0: $Supplier$Type<(T)>)
constructor(arg0: $Supplier$Type<(T)>, arg1: T)

public "get"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedSupplier$Type<T> = ($CachedSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedSupplier_<T> = $CachedSupplier$Type<(T)>;
}}
declare module "packages/snownee/kiwi/config/$KiwiConfig$ConfigType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KiwiConfig$ConfigType extends $Enum<($KiwiConfig$ConfigType)> {
static readonly "COMMON": $KiwiConfig$ConfigType
static readonly "CLIENT": $KiwiConfig$ConfigType
static readonly "SERVER": $KiwiConfig$ConfigType


public static "values"(): ($KiwiConfig$ConfigType)[]
public static "valueOf"(arg0: string): $KiwiConfig$ConfigType
public "extension"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiConfig$ConfigType$Type = (("server") | ("common") | ("client")) | ($KiwiConfig$ConfigType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiConfig$ConfigType_ = $KiwiConfig$ConfigType$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$CommentToken" {
import {$CommentType, $CommentType$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentType"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $CommentToken extends $Token {

constructor(arg0: $CommentType$Type, arg1: string, arg2: $Mark$Type, arg3: $Mark$Type)

public "getValue"(): string
public "getCommentType"(): $CommentType
public "getTokenId"(): $Token$ID
get "value"(): string
get "commentType"(): $CommentType
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentToken$Type = ($CommentToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentToken_ = $CommentToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$MappingStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$CollectionStartEvent, $CollectionStartEvent$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"

export class $MappingStartEvent extends $CollectionStartEvent {

constructor(arg0: string, arg1: string, arg2: boolean, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$FlowStyle$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingStartEvent$Type = ($MappingStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingStartEvent_ = $MappingStartEvent$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$KCreativeTab" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KCreativeTab extends $Record {
static readonly "CODEC": $Codec<($KCreativeTab)>

constructor(order: integer, icon: $ResourceKey$Type<($Item$Type)>, insert: $Optional$Type<($ResourceKey$Type<($CreativeModeTab$Type)>)>, contents: $List$Type<($ResourceKey$Type<($Item$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "insert"(): $Optional<($ResourceKey<($CreativeModeTab)>)>
public static "create"(arg0: integer, arg1: $Optional$Type<($ResourceKey$Type<($Item$Type)>)>, arg2: $Optional$Type<($ResourceKey$Type<($CreativeModeTab$Type)>)>, arg3: $List$Type<($ResourceKey$Type<($Item$Type)>)>): $KCreativeTab
public "order"(): integer
public "contents"(): $List<($ResourceKey<($Item)>)>
public "icon"(): $ResourceKey<($Item)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KCreativeTab$Type = ($KCreativeTab);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KCreativeTab_ = $KCreativeTab$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/string/$StringUpperFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $StringUpperFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringUpperFunction$Type = ($StringUpperFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringUpperFunction_ = $StringUpperFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$Alter" {
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlaceChoices$AlterCondition, $PlaceChoices$AlterCondition$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$AlterCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceChoices$Alter extends $Record {
static readonly "CODEC": $Codec<($PlaceChoices$Alter)>

constructor(when: $List$Type<($PlaceChoices$AlterCondition$Type)>, use: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "when"(): $List<($PlaceChoices$AlterCondition)>
public "use"(): string
public "alter"(arg0: $BlockItem$Type, arg1: $BlockPlaceContext$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Alter$Type = ($PlaceChoices$Alter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$Alter_ = $PlaceChoices$Alter$Type;
}}
declare module "packages/snownee/kiwi/loader/event/$ClientInitEvent" {
import {$ParallelEvent, $ParallelEvent$Type} from "packages/snownee/kiwi/loader/event/$ParallelEvent"
import {$ParallelDispatchEvent, $ParallelDispatchEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$ParallelDispatchEvent"

export class $ClientInitEvent extends $ParallelEvent {

constructor(arg0: $ParallelDispatchEvent$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientInitEvent$Type = ($ClientInitEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientInitEvent_ = $ClientInitEvent$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$MouldingComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$StairsShape, $StairsShape$Type} from "packages/net/minecraft/world/level/block/state/properties/$StairsShape"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $MouldingComponent extends $Record implements $KBlockComponent {
static readonly "FACING": $DirectionProperty
static readonly "SHAPE": $EnumProperty<($StairsShape)>

constructor()

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "getInstance"(): $MouldingComponent
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
get "instance"(): $MouldingComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouldingComponent$Type = ($MouldingComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouldingComponent_ = $MouldingComponent$Type;
}}
declare module "packages/snownee/kiwi/loader/event/$InitEvent" {
import {$ParallelEvent, $ParallelEvent$Type} from "packages/snownee/kiwi/loader/event/$ParallelEvent"
import {$ParallelDispatchEvent, $ParallelDispatchEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$ParallelDispatchEvent"

export class $InitEvent extends $ParallelEvent {

constructor(arg0: $ParallelDispatchEvent$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InitEvent$Type = ($InitEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InitEvent_ = $InitEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$PackageCompactConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$CompactConstructor, $CompactConstructor$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactConstructor"

export class $PackageCompactConstructor extends $CompactConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackageCompactConstructor$Type = ($PackageCompactConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackageCompactConstructor_ = $PackageCompactConstructor$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$AnchorNode" {
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $AnchorNode extends $Node {

constructor(arg0: $Node$Type)

public "getNodeId"(): $NodeId
public "getRealNode"(): $Node
get "nodeId"(): $NodeId
get "realNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorNode$Type = ($AnchorNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorNode_ = $AnchorNode$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionIfc" {
import {$FunctionParameterDefinition, $FunctionParameterDefinition$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$FunctionParameterDefinition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export interface $FunctionIfc {

 "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
 "isParameterLazy"(arg0: integer): boolean
 "hasVarArgs"(): boolean
 "getFunctionParameterDefinitions"(): $List<($FunctionParameterDefinition)>
 "validatePreEvaluation"(arg0: $Token$Type, ...arg1: ($EvaluationValue$Type)[]): void
 "getCountOfNonVarArgParameters"(): integer
}

export namespace $FunctionIfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionIfc$Type = ($FunctionIfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionIfc_ = $FunctionIfc$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ChoicesShape" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ChoicesShape extends $Record implements $ShapeGenerator {

constructor(keys: $List$Type<(string)>, valueMap: $Map$Type<(string), ($ShapeGenerator$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "keys"(): $List<(string)>
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "valueMap"(): $Map<(string), ($ShapeGenerator)>
public static "chooseBooleanProperty"(arg0: $BooleanProperty$Type, arg1: $ShapeGenerator$Type, arg2: $ShapeGenerator$Type): $ShapeGenerator
public static "chooseOneProperty"<T extends $Comparable<(T)>>(arg0: $Property$Type<(T)>, arg1: $Map$Type<(T), ($ShapeGenerator$Type)>): $ShapeGenerator
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChoicesShape$Type = ($ChoicesShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChoicesShape_ = $ChoicesShape$Type;
}}
declare module "packages/snownee/kiwi/util/$SimulationBlockGetter" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$WrappedBlockGetter, $WrappedBlockGetter$Type} from "packages/snownee/kiwi/util/$WrappedBlockGetter"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $SimulationBlockGetter extends $WrappedBlockGetter {

constructor()

public "useSelfLight"(arg0: boolean): void
public "setOverrideLight"(arg0: integer): void
public "setBlockEntity"(arg0: $BlockEntity$Type): void
public "getBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "setPos"(arg0: $BlockPos$Type): void
public "getBrightness"(arg0: $LightLayer$Type, arg1: $BlockPos$Type): integer
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
set "overrideLight"(value: integer)
set "blockEntity"(value: $BlockEntity$Type)
set "pos"(value: $BlockPos$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimulationBlockGetter$Type = ($SimulationBlockGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimulationBlockGetter_ = $SimulationBlockGetter$Type;
}}
declare module "packages/snownee/kiwi/mixin/$WorkAtComposterAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $WorkAtComposterAccess {

}

export namespace $WorkAtComposterAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorkAtComposterAccess$Type = ($WorkAtComposterAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorkAtComposterAccess_ = $WorkAtComposterAccess$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$AlterCondition" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlaceChoices$BlockFaceType, $PlaceChoices$BlockFaceType$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$BlockFaceType"
import {$BlockPredicate, $BlockPredicate$Type} from "packages/net/minecraft/advancements/critereon/$BlockPredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ParsedProtoTag, $ParsedProtoTag$Type} from "packages/snownee/kiwi/customization/placement/$ParsedProtoTag"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlaceChoices$AlterCondition extends $Record {
static readonly "CODEC": $Codec<($PlaceChoices$AlterCondition)>

constructor(target: string, faces: $PlaceChoices$BlockFaceType$Type, block: $BlockPredicate$Type, tags: $List$Type<($ParsedProtoTag$Type)>)

public "equals"(arg0: any): boolean
public "target"(): string
public "toString"(): string
public "hashCode"(): integer
public "test"(arg0: $BlockPlaceContext$Type): boolean
public "block"(): $BlockPredicate
public "tags"(): $List<($ParsedProtoTag)>
public "faces"(): $PlaceChoices$BlockFaceType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$AlterCondition$Type = ($PlaceChoices$AlterCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$AlterCondition_ = $PlaceChoices$AlterCondition$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$SlotLink" {
import {$SlotLink$TagTest, $SlotLink$TagTest$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$TagTest"
import {$PlaceSlot, $PlaceSlot$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlot"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$SlotLink$ResultAction, $SlotLink$ResultAction$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$ResultAction"
import {$SlotLink$MatchResult, $SlotLink$MatchResult$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$MatchResult"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SlotLink extends $Record {
static readonly "PRIMARY_TAG_CODEC": $Codec<(string)>
static readonly "CODEC": $Codec<($SlotLink)>

constructor(from: string, to: string, interest: integer, testTag: $List$Type<($SlotLink$TagTest$Type)>, onLinkFrom: $SlotLink$ResultAction$Type, onLinkTo: $SlotLink$ResultAction$Type, onUnlinkFrom: $SlotLink$ResultAction$Type, onUnlinkTo: $SlotLink$ResultAction$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "matches"(arg0: $PlaceSlot$Type, arg1: $PlaceSlot$Type): boolean
public "to"(): string
public "from"(): string
public static "find"(arg0: $PlaceSlot$Type, arg1: $PlaceSlot$Type): $SlotLink
public static "find"(arg0: $Collection$Type<($PlaceSlot$Type)>, arg1: $Collection$Type<($PlaceSlot$Type)>): $SlotLink$MatchResult
public static "find"(arg0: $BlockState$Type, arg1: $BlockState$Type, arg2: $Direction$Type): $SlotLink$MatchResult
public static "create"(arg0: string, arg1: string, arg2: integer, arg3: $List$Type<($SlotLink$TagTest$Type)>, arg4: $Pair$Type<($SlotLink$ResultAction$Type), ($SlotLink$ResultAction$Type)>, arg5: $Pair$Type<($SlotLink$ResultAction$Type), ($SlotLink$ResultAction$Type)>): $SlotLink
public "interest"(): integer
public "testTag"(): $List<($SlotLink$TagTest)>
public "onUnlinkFrom"(): $SlotLink$ResultAction
public static "isUprightLink"(arg0: $PlaceSlot$Type, arg1: $PlaceSlot$Type): boolean
public "onUnlinkTo"(): $SlotLink$ResultAction
public "onLinkFrom"(): $SlotLink$ResultAction
public "onLinkTo"(): $SlotLink$ResultAction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotLink$Type = ($SlotLink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotLink_ = $SlotLink$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$Scanner" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export interface $Scanner {

 "getToken"(): $Token
 "checkToken"(...arg0: ($Token$ID$Type)[]): boolean
 "peekToken"(): $Token
}

export namespace $Scanner {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Scanner$Type = ($Scanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Scanner_ = $Scanner$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/$JsonTierProvider" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ITierProvider, $ITierProvider$Type} from "packages/snownee/kiwi/contributor/$ITierProvider"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export class $JsonTierProvider implements $ITierProvider {
static readonly "GSON": $Gson
static readonly "CODEC": $Codec<($Map<(string), ($List<(string)>)>)>

constructor(arg0: string, arg1: $Supplier$Type<($List$Type<(string)>)>)

public "load"(arg0: string): boolean
public "refresh"(): $CompletableFuture<(void)>
public "createRenderer"(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>, arg1: string): $CosmeticLayer
public "getTiers"(): $Set<(string)>
public "getPlayerTiers"(arg0: string): $Set<(string)>
public "getAuthor"(): string
public "getRenderableTiers"(): $List<(string)>
public "isContributor"(arg0: string): boolean
public "isContributor"(arg0: string, arg1: string): boolean
get "tiers"(): $Set<(string)>
get "author"(): string
get "renderableTiers"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonTierProvider$Type = ($JsonTierProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonTierProvider_ = $JsonTierProvider$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AsinRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AsinRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsinRFunction$Type = ($AsinRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsinRFunction_ = $AsinRFunction$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$KiwiShapelessRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$KiwiShapelessRecipe, $KiwiShapelessRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$KiwiShapelessRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $KiwiShapelessRecipe$Serializer implements $RecipeSerializer<($KiwiShapelessRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $KiwiShapelessRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $KiwiShapelessRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $KiwiShapelessRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $KiwiShapelessRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiShapelessRecipe$Serializer$Type = ($KiwiShapelessRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiShapelessRecipe$Serializer_ = $KiwiShapelessRecipe$Serializer$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomizationHooks" {
import {$GlassType, $GlassType$Type} from "packages/snownee/kiwi/customization/block/$GlassType"
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $CustomizationHooks {
static "kswitch": boolean


public static "init"(): void
public static "isEnabled"(): boolean
public static "isColorlessGlass"(arg0: $BlockState$Type): boolean
public static "getGlassType"(arg0: $BlockState$Type): $GlassType
public static "skipGlassRendering"(arg0: $BlockState$Type, arg1: $BlockState$Type, arg2: $Direction$Type): boolean
public static "initLoader"(arg0: $IEventBus$Type): void
public static "collectKiwiPacks"(): $ResourceManager
public static "getBlockNamespaces"(): $Set<(string)>
get "enabled"(): boolean
get "blockNamespaces"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationHooks$Type = ($CustomizationHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationHooks_ = $CustomizationHooks$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$IfFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $IfFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IfFunction$Type = ($IfFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IfFunction_ = $IfFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/string/$StringContains" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $StringContains extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringContains$Type = ($StringContains);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringContains_ = $StringContains$Type;
}}
declare module "packages/snownee/kiwi/$KiwiClientConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KiwiClientConfig {
static "contributorCosmetic": string
static "globalTooltip": boolean
static "noMicrosoftTelemetry": boolean
static "tagsTooltip": boolean
static "tagsTooltipTagsPerPage": integer
static "tagsTooltipAppendKeybindHint": boolean
static "nbtTooltip": boolean
static "debugTooltipMsg": boolean
static "suppressExperimentalWarning": boolean
static "exportBlocksMore": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiClientConfig$Type = ($KiwiClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiClientConfig_ = $KiwiClientConfig$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceChoices$Flow" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$SlotLink$ResultAction, $SlotLink$ResultAction$Type} from "packages/snownee/kiwi/customization/placement/$SlotLink$ResultAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlaceChoices$Limit, $PlaceChoices$Limit$Type} from "packages/snownee/kiwi/customization/placement/$PlaceChoices$Limit"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlaceChoices$Flow extends $Record {
static readonly "CODEC": $Codec<($PlaceChoices$Flow)>

constructor(when: $Map$Type<($Direction$Type), ($PlaceChoices$Limit$Type)>, action: $SlotLink$ResultAction$Type, end: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "end"(): boolean
public "action"(): $SlotLink$ResultAction
public "when"(): $Map<($Direction), ($PlaceChoices$Limit)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceChoices$Flow$Type = ($PlaceChoices$Flow);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceChoices$Flow_ = $PlaceChoices$Flow$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$KItemTemplate" {
import {$ItemDefinitionProperties, $ItemDefinitionProperties$Type} from "packages/snownee/kiwi/customization/item/loader/$ItemDefinitionProperties"
import {$KItemTemplate$Type, $KItemTemplate$Type$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate$Type"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KItemTemplate {


public "type"(): $KItemTemplate$Type<(any)>
public "resolve"(arg0: $ResourceLocation$Type): void
public "properties"(): $Optional<($ItemDefinitionProperties)>
public static "codec"(): $Codec<($KItemTemplate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KItemTemplate$Type = ($KItemTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KItemTemplate_ = $KItemTemplate$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ConfigureWallShape" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockShapeType, $BlockShapeType$Type} from "packages/snownee/kiwi/customization/shape/$BlockShapeType"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$BakingContext, $BakingContext$Type} from "packages/snownee/kiwi/customization/shape/$BakingContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ConfiguringShape, $ConfiguringShape$Type} from "packages/snownee/kiwi/customization/shape/$ConfiguringShape"

export class $ConfigureWallShape extends $Record implements $ConfiguringShape {

constructor(width: float, depth: float, wallPostHeight: float, wallMinY: float, wallLowHeight: float, wallTallHeight: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "depth"(): float
public "width"(): float
public "configure"(arg0: $Block$Type, arg1: $BlockShapeType$Type): void
public static "codec"(): $Codec<($ConfigureWallShape)>
public "wallTallHeight"(): float
public "wallMinY"(): float
public "wallPostHeight"(): float
public "wallLowHeight"(): float
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "dependencies"(): $Stream<($UnbakedShape)>
public "bake"(arg0: $BakingContext$Type): $ShapeGenerator
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigureWallShape$Type = ($ConfigureWallShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigureWallShape_ = $ConfigureWallShape$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomizationClient" {
import {$KItemDefinition, $KItemDefinition$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemDefinition"
import {$ClientProxy$Context, $ClientProxy$Context$Type} from "packages/snownee/kiwi/util/$ClientProxy$Context"
import {$SmartKey, $SmartKey$Type} from "packages/snownee/kiwi/util/$SmartKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KBlockDefinition, $KBlockDefinition$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockDefinition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CustomizationClient {
static "buildersButtonKey": $SmartKey

constructor()

public static "init"(): void
public static "afterRegister"(arg0: $Map$Type<($ResourceLocation$Type), ($KItemDefinition$Type)>, arg1: $Map$Type<($ResourceLocation$Type), ($KBlockDefinition$Type)>, arg2: $ClientProxy$Context$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizationClient$Type = ($CustomizationClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizationClient_ = $CustomizationClient$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$WaterLoggableComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $WaterLoggableComponent implements $KBlockComponent {


public "type"(): $KBlockComponent$Type<(any)>
public static "getInstance"(): $WaterLoggableComponent
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
get "instance"(): $WaterLoggableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaterLoggableComponent$Type = ($WaterLoggableComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaterLoggableComponent_ = $WaterLoggableComponent$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$FacingLimitation" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $FacingLimitation extends $Enum<($FacingLimitation)> implements $StringRepresentable {
static readonly "None": $FacingLimitation
static readonly "FrontAndBack": $FacingLimitation
static readonly "Side": $FacingLimitation


public static "values"(): ($FacingLimitation)[]
public "test"(arg0: $Direction$Type, arg1: $Direction$Type): boolean
public static "valueOf"(arg0: string): $FacingLimitation
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FacingLimitation$Type = (("side") | ("none") | ("frontandback")) | ($FacingLimitation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FacingLimitation_ = $FacingLimitation$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$RetextureRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$DynamicShapedRecipe$Serializer, $DynamicShapedRecipe$Serializer$Type} from "packages/snownee/kiwi/recipe/crafting/$DynamicShapedRecipe$Serializer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$RetextureRecipe, $RetextureRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$RetextureRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RetextureRecipe$Serializer extends $DynamicShapedRecipe$Serializer<($RetextureRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $RetextureRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $RetextureRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $RetextureRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RetextureRecipe$Serializer$Type = ($RetextureRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RetextureRecipe$Serializer_ = $RetextureRecipe$Serializer$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TrustedPrefixesTagInspector" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$TagInspector, $TagInspector$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TagInspector"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $TrustedPrefixesTagInspector implements $TagInspector {

constructor(arg0: $List$Type<(string)>)

public "isGlobalTagAllowed"(arg0: $Tag$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrustedPrefixesTagInspector$Type = ($TrustedPrefixesTagInspector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrustedPrefixesTagInspector_ = $TrustedPrefixesTagInspector$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceSlot" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PlaceSlotProvider$Preparation, $PlaceSlotProvider$Preparation$Type} from "packages/snownee/kiwi/customization/placement/$PlaceSlotProvider$Preparation"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ParsedProtoTag, $ParsedProtoTag$Type} from "packages/snownee/kiwi/customization/placement/$ParsedProtoTag"
import {$ImmutableSortedMap, $ImmutableSortedMap$Type} from "packages/com/google/common/collect/$ImmutableSortedMap"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $PlaceSlot extends $Record {
static readonly "TAG_COMPARATOR": $Comparator<(string)>

constructor(side: $Direction$Type, tags: $ImmutableSortedMap$Type<(string), (string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "find"(arg0: $BlockState$Type, arg1: $Direction$Type): $Collection<($PlaceSlot)>
public static "find"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: string): $Optional<($PlaceSlot)>
public "tags"(): $ImmutableSortedMap<(string), (string)>
public "side"(): $Direction
public "tagList"(): $List<(string)>
public "hasTag"(arg0: $ParsedProtoTag$Type): boolean
public static "blockCount"(): integer
public static "hasNoSlots"(arg0: $Block$Type): boolean
public static "renewData"(arg0: $PlaceSlotProvider$Preparation$Type): void
public "primaryTag"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceSlot$Type = ($PlaceSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceSlot_ = $PlaceSlot$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException" {
import {$YAMLException, $YAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $MarkedYAMLException extends $YAMLException {


public "toString"(): string
public "getMessage"(): string
public "getContext"(): string
public "getContextMark"(): $Mark
public "getProblem"(): string
public "getProblemMark"(): $Mark
get "message"(): string
get "context"(): string
get "contextMark"(): $Mark
get "problem"(): string
get "problemMark"(): $Mark
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MarkedYAMLException$Type = ($MarkedYAMLException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MarkedYAMLException_ = $MarkedYAMLException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined" {
import {$AbstractConstruct, $AbstractConstruct$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$AbstractConstruct"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $SafeConstructor$ConstructUndefined extends $AbstractConstruct {

constructor()

public "construct"(arg0: $Node$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeConstructor$ConstructUndefined$Type = ($SafeConstructor$ConstructUndefined);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeConstructor$ConstructUndefined_ = $SafeConstructor$ConstructUndefined$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ParseException" {
import {$BaseException, $BaseException$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$BaseException"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $ParseException extends $BaseException {

constructor(arg0: $Token$Type, arg1: string)
constructor(arg0: string, arg1: string)
constructor(arg0: integer, arg1: integer, arg2: string, arg3: string)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParseException$Type = ($ParseException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParseException_ = $ParseException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DurationNewFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DurationNewFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DurationNewFunction$Type = ($DurationNewFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DurationNewFunction_ = $DurationNewFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$TagTestOperator" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $TagTestOperator extends $Record {
static readonly "VALUES": $BiMap<(string), ($TagTestOperator)>
static readonly "EQUAL": $TagTestOperator
static readonly "CODEC": $Codec<($TagTestOperator)>

constructor(name: string, test: $BiPredicate$Type<(string), (string)>)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(): $BiPredicate<(string), (string)>
public static "register"(arg0: $TagTestOperator$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagTestOperator$Type = ($TagTestOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagTestOperator_ = $TagTestOperator$Type;
}}
declare module "packages/snownee/kiwi/$Kiwi" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RegistryLookup, $RegistryLookup$Type} from "packages/snownee/kiwi/$RegistryLookup"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Kiwi {
static readonly "ID": string
static readonly "registryLookup": $RegistryLookup
static readonly "LOGGER": $Logger
static "defaultOptions": $Map<($ResourceLocation), (boolean)>

constructor()

public static "id"(arg0: string): $ResourceLocation
public static "isLoaded"(arg0: $ResourceLocation$Type): boolean
public static "registerTab"(arg0: string, arg1: $ResourceKey$Type<($CreativeModeTab$Type)>): void
public static "areTagsUpdated"(): boolean
public static "onTagsUpdated"(): void
public static "registerRegistry"(arg0: $IForgeRegistry$Type<(any)>, arg1: $Class$Type<(any)>): void
public static "registerRegistry"(arg0: $Registry$Type<(any)>, arg1: $Class$Type<(any)>): void
public static "preInit"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Kiwi$Type = ($Kiwi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Kiwi_ = $Kiwi$Type;
}}
declare module "packages/snownee/kiwi/loader/event/$ServerInitEvent" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ServerInitEvent {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerInitEvent$Type = ($ServerInitEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerInitEvent_ = $ServerInitEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$VersionTagsTuple" {
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VersionTagsTuple {

constructor(arg0: $DumperOptions$Version$Type, arg1: $Map$Type<(string), (string)>)

public "toString"(): string
public "getVersion"(): $DumperOptions$Version
public "getTags"(): $Map<(string), (string)>
get "version"(): $DumperOptions$Version
get "tags"(): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VersionTagsTuple$Type = ($VersionTagsTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VersionTagsTuple_ = $VersionTagsTuple$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $KBlockComponent$Type<T extends $KBlockComponent> extends $Record {

constructor(codec: $Codec$Type<(T)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "codec"(): $Codec<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockComponent$Type$Type<T> = ($KBlockComponent$Type<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockComponent$Type_<T> = $KBlockComponent$Type$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/block/$CheckedWaterloggedBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$SimpleWaterloggedBlock, $SimpleWaterloggedBlock$Type} from "packages/net/minecraft/world/level/block/$SimpleWaterloggedBlock"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export interface $CheckedWaterloggedBlock extends $SimpleWaterloggedBlock {

 "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
 "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
 "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
 "getPickupSound"(): $Optional<($SoundEvent)>
 "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
}

export namespace $CheckedWaterloggedBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckedWaterloggedBlock$Type = ($CheckedWaterloggedBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckedWaterloggedBlock_ = $CheckedWaterloggedBlock$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$RoundFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $RoundFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RoundFunction$Type = ($RoundFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RoundFunction_ = $RoundFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentEventsCollector" {
import {$CommentLine, $CommentLine$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentLine"
import {$CommentType, $CommentType$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/comments/$CommentType"
import {$Parser, $Parser$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$Parser"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export class $CommentEventsCollector {

constructor(arg0: $Parser$Type, ...arg1: ($CommentType$Type)[])
constructor(arg0: $Queue$Type<($Event$Type)>, ...arg1: ($CommentType$Type)[])

public "isEmpty"(): boolean
public "consume"(): $List<($CommentLine)>
public "collectEvents"(arg0: $Event$Type): $Event
public "collectEvents"(): $CommentEventsCollector
public "collectEventsAndPoll"(arg0: $Event$Type): $Event
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentEventsCollector$Type = ($CommentEventsCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentEventsCollector_ = $CommentEventsCollector$Type;
}}
declare module "packages/snownee/kiwi/util/$KiwiEntityTypeBuilder" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$EntityType$EntityFactory, $EntityType$EntityFactory$Type} from "packages/net/minecraft/world/entity/$EntityType$EntityFactory"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FeatureFlag, $FeatureFlag$Type} from "packages/net/minecraft/world/flag/$FeatureFlag"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $KiwiEntityTypeBuilder<T extends $Entity> {


public "spawnRestriction"(arg0: $SpawnPlacements$Type$Type, arg1: $Heightmap$Types$Type, arg2: $SpawnPlacements$SpawnPredicate$Type<(T)>): $KiwiEntityTypeBuilder<(T)>
public "dimensions"(arg0: $EntityDimensions$Type): $KiwiEntityTypeBuilder<(T)>
public static "create"<T extends $Entity>(): $KiwiEntityTypeBuilder<(T)>
public "build"(): $EntityType<(T)>
public "disableSummon"(): $KiwiEntityTypeBuilder<(T)>
public "trackRangeChunks"(arg0: integer): $KiwiEntityTypeBuilder<(T)>
public "requiredFeatures"(...arg0: ($FeatureFlag$Type)[]): $KiwiEntityTypeBuilder<(T)>
public static "createLiving"<T extends $LivingEntity>(): $KiwiEntityTypeBuilder<(T)>
public static "createMob"<T extends $Entity>(): $KiwiEntityTypeBuilder<(T)>
public "trackedUpdateRate"(arg0: integer): $KiwiEntityTypeBuilder<(T)>
public "defaultAttributes"(arg0: $Supplier$Type<($AttributeSupplier$Builder$Type)>): $KiwiEntityTypeBuilder<(T)>
public "entityFactory"<N extends T>(arg0: $EntityType$EntityFactory$Type<(N)>): $KiwiEntityTypeBuilder<(N)>
public "disableSaving"(): $KiwiEntityTypeBuilder<(T)>
public "fireImmune"(): $KiwiEntityTypeBuilder<(T)>
public "spawnGroup"(arg0: $MobCategory$Type): $KiwiEntityTypeBuilder<(T)>
public "spawnableFarFromPlayer"(): $KiwiEntityTypeBuilder<(T)>
public "specificSpawnBlocks"(...arg0: ($Block$Type)[]): $KiwiEntityTypeBuilder<(T)>
public "forceTrackedVelocityUpdates"(arg0: boolean): $KiwiEntityTypeBuilder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiEntityTypeBuilder$Type<T> = ($KiwiEntityTypeBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiEntityTypeBuilder_<T> = $KiwiEntityTypeBuilder$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Entry" {
import {$BlockFamily, $BlockFamily$Type} from "packages/snownee/kiwi/customization/block/family/$BlockFamily"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KHolder, $KHolder$Type} from "packages/snownee/kiwi/util/$KHolder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $CConvertItemPacket$Entry extends $Record {

constructor(arg0: float)
constructor(ratio: float, steps: $List$Type<($Pair$Type<($KHolder$Type<($BlockFamily$Type)>), ($Item$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "steps"(): $List<($Pair<($KHolder<($BlockFamily)>), ($Item)>)>
public "item"(): $Item
public "ratio"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CConvertItemPacket$Entry$Type = ($CConvertItemPacket$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CConvertItemPacket$Entry_ = $CConvertItemPacket$Entry$Type;
}}
declare module "packages/snownee/kiwi/schedule/impl/$SimpleLevelTask" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"
import {$TickEvent$Phase, $TickEvent$Phase$Type} from "packages/net/minecraftforge/event/$TickEvent$Phase"
import {$LevelTicker, $LevelTicker$Type} from "packages/snownee/kiwi/schedule/impl/$LevelTicker"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Task, $Task$Type} from "packages/snownee/kiwi/schedule/$Task"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $SimpleLevelTask extends $Task<($LevelTicker)> implements $INBTSerializable<($CompoundTag)> {

constructor()
constructor(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $TickEvent$Phase$Type, arg2: $IntPredicate$Type)
constructor(arg0: $Level$Type, arg1: $TickEvent$Phase$Type, arg2: $IntPredicate$Type)

public "tick"(arg0: $LevelTicker$Type): boolean
public "shouldSave"(): boolean
public "deserializeNBT"(arg0: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleLevelTask$Type = ($SimpleLevelTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleLevelTask_ = $SimpleLevelTask$Type;
}}
declare module "packages/snownee/kiwi/customization/block/component/$HorizontalComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $HorizontalComponent extends $Record implements $KBlockComponent {
static readonly "FACING": $DirectionProperty
static readonly "CODEC": $Codec<($HorizontalComponent)>

constructor(oppose: boolean)

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "getInstance"(arg0: boolean): $HorizontalComponent
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "oppose"(): boolean
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalComponent$Type = ($HorizontalComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalComponent_ = $HorizontalComponent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $Property implements $Comparable<($Property)> {

constructor(arg0: string, arg1: $Class$Type<(any)>)

public "getName"(): string
public "get"(arg0: any): any
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Property$Type): integer
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
public "getType"(): $Class<(any)>
public "getActualTypeArguments"(): ($Class<(any)>)[]
public "isReadable"(): boolean
public "isWritable"(): boolean
get "name"(): string
get "annotations"(): $List<($Annotation)>
get "type"(): $Class<(any)>
get "actualTypeArguments"(): ($Class<(any)>)[]
get "readable"(): boolean
get "writable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Property$Type = ($Property);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Property_ = $Property$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/arithmetic/$InfixDivisionOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixDivisionOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixDivisionOperator$Type = ($InfixDivisionOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixDivisionOperator_ = $InfixDivisionOperator$Type;
}}
declare module "packages/snownee/kiwi/config/$ConfigUI" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ConfigUI extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ConfigUI {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigUI$Type = ($ConfigUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigUI_ = $ConfigUI$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$BlockEntryToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $BlockEntryToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntryToken$Type = ($BlockEntryToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntryToken_ = $BlockEntryToken$Type;
}}
declare module "packages/snownee/kiwi/util/$SmartKey" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SmartKey extends $KeyMapping {
static readonly "ALL": $Map<(string), ($KeyMapping)>
static readonly "CATEGORY_MOVEMENT": string
static readonly "CATEGORY_MISC": string
static readonly "CATEGORY_MULTIPLAYER": string
static readonly "CATEGORY_GAMEPLAY": string
static readonly "CATEGORY_INVENTORY": string
static readonly "CATEGORY_INTERFACE": string
static readonly "CATEGORY_CREATIVE": string
 "key": $InputConstants$Key
 "clickCount": integer


public "tick"(): void
public "setDown"(arg0: boolean): void
public "setDownWithResult"(arg0: boolean): boolean
set "down"(value: boolean)
set "downWithResult"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmartKey$Type = ($SmartKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmartKey_ = $SmartKey$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ASTNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $ASTNode {

constructor(arg0: $Token$Type, ...arg1: ($ASTNode$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getParameters"(): $List<($ASTNode)>
public "getToken"(): $Token
public "toJSON"(): string
get "parameters"(): $List<($ASTNode)>
get "token"(): $Token
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ASTNode$Type = ($ASTNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ASTNode_ = $ASTNode$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Mark implements $Serializable {

constructor(arg0: string, arg1: integer, arg2: integer, arg3: integer, arg4: (character)[], arg5: integer)
constructor(arg0: string, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: integer)

public "getName"(): string
public "toString"(): string
public "getIndex"(): integer
public "getBuffer"(): (integer)[]
public "get_snippet"(arg0: integer, arg1: integer): string
public "get_snippet"(): string
public "getLine"(): integer
public "getPointer"(): integer
public "getColumn"(): integer
get "name"(): string
get "index"(): integer
get "buffer"(): (integer)[]
get "_snippet"(): string
get "line"(): integer
get "pointer"(): integer
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mark$Type = ($Mark);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mark_ = $Mark$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$StreamStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $StreamStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamStartToken$Type = ($StreamStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamStartToken_ = $StreamStartToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$TanHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $TanHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TanHFunction$Type = ($TanHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TanHFunction_ = $TanHFunction$Type;
}}
declare module "packages/snownee/kiwi/contributor/$ITierProvider" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$CosmeticLayer, $CosmeticLayer$Type} from "packages/snownee/kiwi/contributor/client/$CosmeticLayer"

export interface $ITierProvider {

 "refresh"(): $CompletableFuture<(void)>
 "createRenderer"(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>, arg1: string): $CosmeticLayer
 "getTiers"(): $Set<(string)>
 "getPlayerTiers"(arg0: string): $Set<(string)>
 "getAuthor"(): string
 "isContributor"(arg0: string): boolean
 "isContributor"(arg0: string, arg1: string): boolean
 "getRenderableTiers"(): $List<(string)>
}

export namespace $ITierProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITierProvider$Type = ($ITierProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITierProvider_ = $ITierProvider$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/model/$PlanetModel" {
import {$AgeableListModel, $AgeableListModel$Type} from "packages/net/minecraft/client/model/$AgeableListModel"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $PlanetModel<T extends $LivingEntity> extends $AgeableListModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $LayerDefinition$Type)

public static "create"(): $LayerDefinition
public "setupAnim"(arg0: T, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlanetModel$Type<T> = ($PlanetModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlanetModel_<T> = $PlanetModel$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/block/component/$FrontAndTopComponent" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$StateDefinition$Builder, $StateDefinition$Builder$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition$Builder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$KBlockComponent$Type, $KBlockComponent$Type$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent$Type"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FrontAndTop, $FrontAndTop$Type} from "packages/net/minecraft/core/$FrontAndTop"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$KBlockComponent, $KBlockComponent$Type} from "packages/snownee/kiwi/customization/block/component/$KBlockComponent"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$BlockBehaviorRegistry, $BlockBehaviorRegistry$Type} from "packages/snownee/kiwi/customization/block/behavior/$BlockBehaviorRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $FrontAndTopComponent extends $Record implements $KBlockComponent {
static readonly "ORIENTATION": $EnumProperty<($FrontAndTop)>

constructor()

public "type"(): $KBlockComponent$Type<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "getInstance"(): $FrontAndTopComponent
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "getHorizontalFacing"(arg0: $BlockState$Type): $Direction
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getStateForPlacement"(arg0: $KBlockSettings$Type, arg1: $BlockState$Type, arg2: $BlockPlaceContext$Type): $BlockState
public "injectProperties"(arg0: $Block$Type, arg1: $StateDefinition$Builder$Type<($Block$Type), ($BlockState$Type)>): void
public "registerDefaultState"(arg0: $BlockState$Type): $BlockState
public "getAnalogOutputSignal"(arg0: $BlockState$Type): integer
public "hasAnalogOutputSignal"(): boolean
public "useShapeForLightOcclusion"(arg0: $BlockState$Type): boolean
public "canBeReplaced"(arg0: $BlockState$Type, arg1: $BlockPlaceContext$Type): boolean
public "addBehaviors"(arg0: $BlockBehaviorRegistry$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
get "instance"(): $FrontAndTopComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrontAndTopComponent$Type = ($FrontAndTopComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrontAndTopComponent_ = $FrontAndTopComponent$Type;
}}
declare module "packages/snownee/kiwi/customization/network/$CConvertItemPacket" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CConvertItemPacket$Entry, $CConvertItemPacket$Entry$Type} from "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Entry"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $CConvertItemPacket extends $PacketHandler {
static readonly "MAX_STEPS": integer
static "I": $CConvertItemPacket
 "id": $ResourceLocation

constructor()

public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
public static "send"(arg0: boolean, arg1: integer, arg2: $CConvertItemPacket$Entry$Type, arg3: $Item$Type, arg4: boolean): void
public static "playPickupSound"(arg0: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CConvertItemPacket$Type = ($CConvertItemPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CConvertItemPacket_ = $CConvertItemPacket$Type;
}}
declare module "packages/snownee/kiwi/mixin/$ShovelItemAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ShovelItemAccess {

}

export namespace $ShovelItemAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShovelItemAccess$Type = ($ShovelItemAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShovelItemAccess_ = $ShovelItemAccess$Type;
}}
declare module "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandlerCodec" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$CanSurviveHandler, $CanSurviveHandler$Type} from "packages/snownee/kiwi/customization/block/behavior/$CanSurviveHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $CanSurviveHandlerCodec implements $Codec<($CanSurviveHandler)> {

constructor()

public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<($CanSurviveHandler), (T)>)>
public "encode"<T>(arg0: $CanSurviveHandler$Type, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<($CanSurviveHandler$Type)>, arg1: $MapDecoder$Type<($CanSurviveHandler$Type)>, arg2: $Supplier$Type<(string)>): $MapCodec<($CanSurviveHandler)>
public static "of"<A>(arg0: $MapEncoder$Type<($CanSurviveHandler$Type)>, arg1: $MapDecoder$Type<($CanSurviveHandler$Type)>): $MapCodec<($CanSurviveHandler)>
public static "of"<A>(arg0: $Encoder$Type<($CanSurviveHandler$Type)>, arg1: $Decoder$Type<($CanSurviveHandler$Type)>, arg2: string): $Codec<($CanSurviveHandler)>
public static "of"<A>(arg0: $Encoder$Type<($CanSurviveHandler$Type)>, arg1: $Decoder$Type<($CanSurviveHandler$Type)>): $Codec<($CanSurviveHandler)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: $CanSurviveHandler$Type): $Codec<($CanSurviveHandler)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: $CanSurviveHandler$Type): $Codec<($CanSurviveHandler)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: $CanSurviveHandler$Type): $Codec<($CanSurviveHandler)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: $CanSurviveHandler$Type): $Codec<($CanSurviveHandler)>
public static "unit"<A>(arg0: $Supplier$Type<($CanSurviveHandler$Type)>): $Codec<($CanSurviveHandler)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<($CanSurviveHandler)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($CanSurviveHandler)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($CanSurviveHandler)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<($CanSurviveHandler)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<($CanSurviveHandler)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<($CanSurviveHandler)>)>
public "optionalFieldOf"(arg0: string, arg1: $CanSurviveHandler$Type, arg2: $Lifecycle$Type): $MapCodec<($CanSurviveHandler)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: $CanSurviveHandler$Type, arg3: $Lifecycle$Type): $MapCodec<($CanSurviveHandler)>
public "optionalFieldOf"(arg0: string, arg1: $CanSurviveHandler$Type): $MapCodec<($CanSurviveHandler)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<($CanSurviveHandler$Type)>): $Codec<($CanSurviveHandler)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<($CanSurviveHandler)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<($CanSurviveHandler)>)>
public "stable"(): $Codec<($CanSurviveHandler)>
public static "empty"<A>(): $MapEncoder<($CanSurviveHandler)>
public static "error"<A>(arg0: string): $Encoder<($CanSurviveHandler)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $CanSurviveHandler$Type): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<($CanSurviveHandler), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($CanSurviveHandler)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($CanSurviveHandler)>
public "boxed"(): $Decoder$Boxed<($CanSurviveHandler)>
public "simple"(): $Decoder$Simple<($CanSurviveHandler)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<($CanSurviveHandler)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<($CanSurviveHandler)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<($CanSurviveHandler)>
public "terminal"(): $Decoder$Terminal<($CanSurviveHandler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CanSurviveHandlerCodec$Type = ($CanSurviveHandlerCodec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CanSurviveHandlerCodec_ = $CanSurviveHandlerCodec$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$SequenceNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$NodeId, $NodeId$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$NodeId"
import {$CollectionNode, $CollectionNode$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $SequenceNode extends $CollectionNode<($Node)> {

constructor(arg0: $Tag$Type, arg1: boolean, arg2: $List$Type<($Node$Type)>, arg3: $Mark$Type, arg4: $Mark$Type, arg5: $DumperOptions$FlowStyle$Type)
constructor(arg0: $Tag$Type, arg1: $List$Type<($Node$Type)>, arg2: $DumperOptions$FlowStyle$Type)

public "toString"(): string
public "getValue"(): $List<($Node)>
public "setListType"(arg0: $Class$Type<(any)>): void
public "getNodeId"(): $NodeId
get "value"(): $List<($Node)>
set "listType"(value: $Class$Type<(any)>)
get "nodeId"(): $NodeId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceNode$Type = ($SequenceNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceNode_ = $SequenceNode$Type;
}}
declare module "packages/snownee/kiwi/$KiwiModule$Category" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $KiwiModule$Category extends $Annotation {

 "value"(): (string)[]
 "after"(): (string)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $KiwiModule$Category {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiModule$Category$Type = ($KiwiModule$Category);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiModule$Category_ = $KiwiModule$Category$Type;
}}
declare module "packages/snownee/kiwi/util/$LerpedFloat$Interpolator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LerpedFloat$Interpolator {

 "interpolate"(arg0: double, arg1: double, arg2: double): float

(arg0: double, arg1: double, arg2: double): float
}

export namespace $LerpedFloat$Interpolator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LerpedFloat$Interpolator$Type = ($LerpedFloat$Interpolator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LerpedFloat$Interpolator_ = $LerpedFloat$Interpolator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/emitter/$ScalarAnalysis" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ScalarAnalysis {

constructor(arg0: string, arg1: boolean, arg2: boolean, arg3: boolean, arg4: boolean, arg5: boolean, arg6: boolean)

public "isEmpty"(): boolean
public "getScalar"(): string
public "isMultiline"(): boolean
public "isAllowFlowPlain"(): boolean
public "isAllowBlockPlain"(): boolean
public "isAllowBlock"(): boolean
public "isAllowSingleQuoted"(): boolean
get "empty"(): boolean
get "scalar"(): string
get "multiline"(): boolean
get "allowFlowPlain"(): boolean
get "allowBlockPlain"(): boolean
get "allowBlock"(): boolean
get "allowSingleQuoted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarAnalysis$Type = ($ScalarAnalysis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarAnalysis_ = $ScalarAnalysis$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SecRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SecRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecRFunction$Type = ($SecRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecRFunction_ = $SecRFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$BlockSequenceStartToken" {
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Token$ID, $Token$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"

export class $BlockSequenceStartToken extends $Token {

constructor(arg0: $Mark$Type, arg1: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockSequenceStartToken$Type = ($BlockSequenceStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockSequenceStartToken_ = $BlockSequenceStartToken$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$Construct" {
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $Construct {

 "construct"(arg0: $Node$Type): any
 "construct2ndStep"(arg0: $Node$Type, arg1: any): void
}

export namespace $Construct {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Construct$Type = ($Construct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Construct_ = $Construct$Type;
}}
declare module "packages/snownee/kiwi/contributor/network/$SSyncCosmeticPacket" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$PacketHandler, $PacketHandler$Type} from "packages/snownee/kiwi/network/$PacketHandler"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SSyncCosmeticPacket extends $PacketHandler {
static "I": $SSyncCosmeticPacket
 "id": $ResourceLocation

constructor()

public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
public static "send"(arg0: $Map$Type<(string), ($ResourceLocation$Type)>, arg1: $ServerPlayer$Type, arg2: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSyncCosmeticPacket$Type = ($SSyncCosmeticPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSyncCosmeticPacket_ = $SSyncCosmeticPacket$Type;
}}
declare module "packages/snownee/kiwi/util/$KEval" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"

export class $KEval {

constructor()

public static "config"(): $ExpressionConfiguration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KEval$Type = ($KEval);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KEval_ = $KEval$Type;
}}
declare module "packages/snownee/kiwi/util/$NotNullByDefault" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $NotNullByDefault extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $NotNullByDefault {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotNullByDefault$Type = ($NotNullByDefault);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotNullByDefault_ = $NotNullByDefault$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$ConfigureCrossCollisionShape" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockShapeType, $BlockShapeType$Type} from "packages/snownee/kiwi/customization/shape/$BlockShapeType"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$UnbakedShape, $UnbakedShape$Type} from "packages/snownee/kiwi/customization/shape/$UnbakedShape"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$BakingContext, $BakingContext$Type} from "packages/snownee/kiwi/customization/shape/$BakingContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ConfiguringShape, $ConfiguringShape$Type} from "packages/snownee/kiwi/customization/shape/$ConfiguringShape"

export class $ConfigureCrossCollisionShape extends $Record implements $ConfiguringShape {

constructor(nodeWidth: float, extensionWidth: float, nodeHeight: float, extensionBottom: float, extensionHeight: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "configure"(arg0: $Block$Type, arg1: $BlockShapeType$Type): void
public static "codec"(): $Codec<($ConfigureCrossCollisionShape)>
public "extensionWidth"(): float
public "nodeWidth"(): float
public "extensionHeight"(): float
public "extensionBottom"(): float
public "nodeHeight"(): float
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "dependencies"(): $Stream<($UnbakedShape)>
public "bake"(arg0: $BakingContext$Type): $ShapeGenerator
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigureCrossCollisionShape$Type = ($ConfigureCrossCollisionShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigureCrossCollisionShape_ = $ConfigureCrossCollisionShape$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$PercentEscaper" {
import {$UnicodeEscaper, $UnicodeEscaper$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$UnicodeEscaper"

export class $PercentEscaper extends $UnicodeEscaper {
static readonly "SAFECHARS_URLENCODER": string
static readonly "SAFEPATHCHARS_URLENCODER": string
static readonly "SAFEQUERYSTRINGCHARS_URLENCODER": string

constructor(arg0: string, arg1: boolean)

public "escape"(arg0: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PercentEscaper$Type = ($PercentEscaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PercentEscaper_ = $PercentEscaper$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/parser/$Production" {
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Production {

 "produce"(): $Event

(): $Event
}

export namespace $Production {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Production$Type = ($Production);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Production_ = $Production$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeNewFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeNewFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
public "validatePreEvaluation"(arg0: $Token$Type, ...arg1: ($EvaluationValue$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeNewFunction$Type = ($DateTimeNewFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeNewFunction_ = $DateTimeNewFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$KBlockTemplates" {
import {$KiwiGO, $KiwiGO$Type} from "packages/snownee/kiwi/$KiwiGO"
import {$SimpleBlockTemplate, $SimpleBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$SimpleBlockTemplate"
import {$KBlockTemplate$Type, $KBlockTemplate$Type$Type} from "packages/snownee/kiwi/customization/block/loader/$KBlockTemplate$Type"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BuiltInBlockTemplate, $BuiltInBlockTemplate$Type} from "packages/snownee/kiwi/customization/block/loader/$BuiltInBlockTemplate"
import {$AbstractModule, $AbstractModule$Type} from "packages/snownee/kiwi/$AbstractModule"

export class $KBlockTemplates extends $AbstractModule {
static readonly "SIMPLE": $KiwiGO<($KBlockTemplate$Type<($SimpleBlockTemplate)>)>
static readonly "BUILT_IN": $KiwiGO<($KBlockTemplate$Type<($BuiltInBlockTemplate)>)>
 "uid": $ResourceLocation

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KBlockTemplates$Type = ($KBlockTemplates);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KBlockTemplates_ = $KBlockTemplates$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$SecHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $SecHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecHFunction$Type = ($SecHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecHFunction_ = $SecHFunction$Type;
}}
declare module "packages/snownee/kiwi/$NamedEntry" {
import {$GroupSetting, $GroupSetting$Type} from "packages/snownee/kiwi/$GroupSetting"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $NamedEntry<T> {
readonly "name": $ResourceLocation
readonly "entry": T
readonly "registry": any
readonly "field": $Field
 "groupSetting": $GroupSetting

constructor(arg0: $ResourceLocation$Type, arg1: T, arg2: any, arg3: $Field$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamedEntry$Type<T> = ($NamedEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamedEntry_<T> = $NamedEntry$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/basic/$RandomFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $RandomFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomFunction$Type = ($RandomFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomFunction_ = $RandomFunction$Type;
}}
declare module "packages/snownee/kiwi/customization/placement/$PlaceDebugRenderer" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$DebugRenderer$SimpleDebugRenderer, $DebugRenderer$SimpleDebugRenderer$Type} from "packages/net/minecraft/client/renderer/debug/$DebugRenderer$SimpleDebugRenderer"

export class $PlaceDebugRenderer implements $DebugRenderer$SimpleDebugRenderer {

constructor()

public static "getInstance"(): $PlaceDebugRenderer
public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: double, arg3: double, arg4: double): void
public "clear"(): void
get "instance"(): $PlaceDebugRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceDebugRenderer$Type = ($PlaceDebugRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceDebugRenderer_ = $PlaceDebugRenderer$Type;
}}
declare module "packages/snownee/kiwi/util/$Util" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MessageFormat, $MessageFormat$Type} from "packages/java/text/$MessageFormat"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"

export class $Util {
static readonly "MESSAGE_FORMAT": $MessageFormat
static readonly "DIRECTIONS": $List<($Direction)>
static readonly "HORIZONTAL_DIRECTIONS": $List<($Direction)>


public static "color"(arg0: integer): string
public static "trimRL"(arg0: $ResourceLocation$Type, arg1: string): string
public static "trimRL"(arg0: string): string
public static "trimRL"(arg0: $ResourceLocation$Type): string
public static "trimRL"(arg0: string, arg1: string): string
public static "dumpYaml"(arg0: any, arg1: $Writer$Type): void
public static "friendlyText"(arg0: string): string
public static "canPlayerBreak"(arg0: $Player$Type, arg1: $BlockState$Type, arg2: $BlockPos$Type): boolean
public static "getPickRange"(arg0: $Player$Type): float
public static "jsonList"(arg0: $JsonElement$Type, arg1: $Consumer$Type<($JsonElement$Type)>): void
public static "formatCompact"(arg0: long): string
public static "readNBTStrings"(arg0: $CompoundTag$Type, arg1: string, arg2: (string)[]): (string)[]
public static "getBlockDefName"(arg0: $ItemStack$Type, arg1: string): $Component
public static "formatComma"(arg0: long): string
public static "friendlyCompare"(arg0: string, arg1: string): integer
public static "writeNBTStrings"(arg0: $CompoundTag$Type, arg1: string, arg2: (string)[]): void
public static "loadYaml"<T>(arg0: string, arg1: $Class$Type<(any)>): T
public static "loadYaml"<T>(arg0: $Reader$Type, arg1: $Class$Type<(any)>): T
public static "setRecipeManager"(arg0: $RecipeManager$Type): void
public static "applyAlpha"(arg0: integer, arg1: float): integer
public static "onAttackEntity"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type, arg4: $EntityHitResult$Type): $InteractionResult
public static "getRecipeManager"(): $RecipeManager
public static "RL"(arg0: string): $ResourceLocation
public static "RL"(arg0: string, arg1: string): $ResourceLocation
public static "getRecipes"<C extends $Container, T extends $Recipe<(C)>>(arg0: $RecipeType$Type<(T)>): $List<(T)>
public static "displayClientMessage"(arg0: $Player$Type, arg1: boolean, arg2: string, ...arg3: (any)[]): void
set "recipeManager"(value: $RecipeManager$Type)
get "recipeManager"(): $RecipeManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Util$Type = ($Util);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Util_ = $Util$Type;
}}
declare module "packages/snownee/kiwi/customization/block/loader/$InjectedBlockPropertiesCodec" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$ThreadLocal, $ThreadLocal$Type} from "packages/java/lang/$ThreadLocal"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $InjectedBlockPropertiesCodec extends $Record implements $Codec<($BlockBehaviour$Properties)> {
static readonly "INJECTED": $ThreadLocal<($BlockBehaviour$Properties)>

constructor(delegate: $Codec$Type<($BlockBehaviour$Properties$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<($BlockBehaviour$Properties), (T)>)>
public "encode"<T>(arg0: $BlockBehaviour$Properties$Type, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "delegate"(): $Codec<($BlockBehaviour$Properties)>
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<($BlockBehaviour$Properties$Type)>, arg1: $MapDecoder$Type<($BlockBehaviour$Properties$Type)>, arg2: $Supplier$Type<(string)>): $MapCodec<($BlockBehaviour$Properties)>
public static "of"<A>(arg0: $MapEncoder$Type<($BlockBehaviour$Properties$Type)>, arg1: $MapDecoder$Type<($BlockBehaviour$Properties$Type)>): $MapCodec<($BlockBehaviour$Properties)>
public static "of"<A>(arg0: $Encoder$Type<($BlockBehaviour$Properties$Type)>, arg1: $Decoder$Type<($BlockBehaviour$Properties$Type)>, arg2: string): $Codec<($BlockBehaviour$Properties)>
public static "of"<A>(arg0: $Encoder$Type<($BlockBehaviour$Properties$Type)>, arg1: $Decoder$Type<($BlockBehaviour$Properties$Type)>): $Codec<($BlockBehaviour$Properties)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: $BlockBehaviour$Properties$Type): $Codec<($BlockBehaviour$Properties)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: $BlockBehaviour$Properties$Type): $Codec<($BlockBehaviour$Properties)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: $BlockBehaviour$Properties$Type): $Codec<($BlockBehaviour$Properties)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: $BlockBehaviour$Properties$Type): $Codec<($BlockBehaviour$Properties)>
public static "unit"<A>(arg0: $Supplier$Type<($BlockBehaviour$Properties$Type)>): $Codec<($BlockBehaviour$Properties)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<($BlockBehaviour$Properties)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($BlockBehaviour$Properties)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($BlockBehaviour$Properties)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<($BlockBehaviour$Properties)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<($BlockBehaviour$Properties)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<($BlockBehaviour$Properties)>)>
public "optionalFieldOf"(arg0: string, arg1: $BlockBehaviour$Properties$Type, arg2: $Lifecycle$Type): $MapCodec<($BlockBehaviour$Properties)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: $BlockBehaviour$Properties$Type, arg3: $Lifecycle$Type): $MapCodec<($BlockBehaviour$Properties)>
public "optionalFieldOf"(arg0: string, arg1: $BlockBehaviour$Properties$Type): $MapCodec<($BlockBehaviour$Properties)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<($BlockBehaviour$Properties$Type)>): $Codec<($BlockBehaviour$Properties)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<($BlockBehaviour$Properties)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<($BlockBehaviour$Properties)>)>
public "stable"(): $Codec<($BlockBehaviour$Properties)>
public static "empty"<A>(): $MapEncoder<($BlockBehaviour$Properties)>
public static "error"<A>(arg0: string): $Encoder<($BlockBehaviour$Properties)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $BlockBehaviour$Properties$Type): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<($BlockBehaviour$Properties), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($BlockBehaviour$Properties)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($BlockBehaviour$Properties)>
public "boxed"(): $Decoder$Boxed<($BlockBehaviour$Properties)>
public "simple"(): $Decoder$Simple<($BlockBehaviour$Properties)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<($BlockBehaviour$Properties)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<($BlockBehaviour$Properties)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<($BlockBehaviour$Properties)>
public "terminal"(): $Decoder$Terminal<($BlockBehaviour$Properties)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedBlockPropertiesCodec$Type = ($InjectedBlockPropertiesCodec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedBlockPropertiesCodec_ = $InjectedBlockPropertiesCodec$Type;
}}
declare module "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Group" {
import {$CConvertItemPacket$Entry, $CConvertItemPacket$Entry$Type} from "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Entry"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$LinkedHashSet, $LinkedHashSet$Type} from "packages/java/util/$LinkedHashSet"

export class $CConvertItemPacket$Group extends $Record {

constructor()
constructor(entries: $LinkedHashSet$Type<($CConvertItemPacket$Entry$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "entries"(): $LinkedHashSet<($CConvertItemPacket$Entry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CConvertItemPacket$Group$Type = ($CConvertItemPacket$Group);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CConvertItemPacket$Group_ = $CConvertItemPacket$Group$Type;
}}
declare module "packages/snownee/kiwi/recipe/$FullBlockIngredient" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$AbstractIngredient, $AbstractIngredient$Type} from "packages/net/minecraftforge/common/crafting/$AbstractIngredient"
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$FullBlockIngredient$Serializer, $FullBlockIngredient$Serializer$Type} from "packages/snownee/kiwi/recipe/$FullBlockIngredient$Serializer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $FullBlockIngredient extends $AbstractIngredient {
static readonly "ID": $ResourceLocation
static readonly "SERIALIZER": $FullBlockIngredient$Serializer
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList


public "test"(arg0: $ItemStack$Type): boolean
public "isSimple"(): boolean
public "toJson"(): $JsonElement
public "isEmpty"(): boolean
public "getSerializer"(): $FullBlockIngredient$Serializer
public static "isTextureBlock"(arg0: $ItemStack$Type): boolean
public static "isFullBlock"(arg0: $ItemStack$Type): boolean
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
get "simple"(): boolean
get "empty"(): boolean
get "serializer"(): $FullBlockIngredient$Serializer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FullBlockIngredient$Type = ($FullBlockIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FullBlockIngredient_ = $FullBlockIngredient$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression" {
import {$ExpressionConfiguration, $ExpressionConfiguration$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/config/$ExpressionConfiguration"
import {$DataAccessorIfc, $DataAccessorIfc$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$DataAccessorIfc"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$ASTNode, $ASTNode$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$ASTNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Expression {

constructor(arg0: $Expression$Type)
constructor(arg0: string, arg1: $ExpressionConfiguration$Type)
constructor(arg0: string)

public "validate"(): void
public "copy"(): $Expression
public "and"(arg0: string, arg1: any): $Expression
public "with"(arg0: string, arg1: any): $Expression
public "evaluate"(): $EvaluationValue
public "convertValue"(arg0: any): $EvaluationValue
public "convertDoubleValue"(arg0: double): $EvaluationValue
public "evaluateSubtree"(arg0: $ASTNode$Type): $EvaluationValue
public "getConfiguration"(): $ExpressionConfiguration
public "getAllASTNodes"(): $List<($ASTNode)>
public "getConstants"(): $Map<(string), ($EvaluationValue)>
public "getDataAccessor"(): $DataAccessorIfc
public "withValues"(arg0: $Map$Type<(string), (any)>): $Expression
public "getUsedVariables"(): $Set<(string)>
public "getUndefinedVariables"(): $Set<(string)>
public "getAbstractSyntaxTree"(): $ASTNode
public "getExpressionString"(): string
public "createExpressionNode"(arg0: string): $ASTNode
get "configuration"(): $ExpressionConfiguration
get "allASTNodes"(): $List<($ASTNode)>
get "constants"(): $Map<(string), ($EvaluationValue)>
get "dataAccessor"(): $DataAccessorIfc
get "usedVariables"(): $Set<(string)>
get "undefinedVariables"(): $Set<(string)>
get "abstractSyntaxTree"(): $ASTNode
get "expressionString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expression$Type = ($Expression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expression_ = $Expression$Type;
}}
declare module "packages/snownee/kiwi/schedule/$Task" {
import {$ITicker, $ITicker$Type} from "packages/snownee/kiwi/schedule/$ITicker"

export class $Task<T extends $ITicker> {

constructor()

public "tick"(arg0: T): boolean
public "ticker"(): T
public "shouldSave"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Task$Type<T> = ($Task<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Task_<T> = $Task$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/booleans/$InfixNotEqualsOperator" {
import {$AbstractOperator, $AbstractOperator$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/operators/$AbstractOperator"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $InfixNotEqualsOperator extends $AbstractOperator {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InfixNotEqualsOperator$Type = ($InfixNotEqualsOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InfixNotEqualsOperator_ = $InfixNotEqualsOperator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$CscHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $CscHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CscHFunction$Type = ($CscHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CscHFunction_ = $CscHFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Event$ID extends $Enum<($Event$ID)> {
static readonly "Alias": $Event$ID
static readonly "Comment": $Event$ID
static readonly "DocumentEnd": $Event$ID
static readonly "DocumentStart": $Event$ID
static readonly "MappingEnd": $Event$ID
static readonly "MappingStart": $Event$ID
static readonly "Scalar": $Event$ID
static readonly "SequenceEnd": $Event$ID
static readonly "SequenceStart": $Event$ID
static readonly "StreamEnd": $Event$ID
static readonly "StreamStart": $Event$ID


public static "values"(): ($Event$ID)[]
public static "valueOf"(arg0: string): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$ID$Type = (("mappingend") | ("scalar") | ("sequencestart") | ("documentend") | ("mappingstart") | ("alias") | ("streamstart") | ("comment") | ("sequenceend") | ("streamend") | ("documentstart")) | ($Event$ID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event$ID_ = $Event$ID$Type;
}}
declare module "packages/snownee/kiwi/mixin/$ShapedRecipeAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ShapedRecipeAccess {

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
declare module "packages/snownee/kiwi/$KiwiAnnotationData" {
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KiwiAnnotationData {

constructor(arg0: string, arg1: $Map$Type<(string), (any)>)

public "target"(): string
public "data"(): $Map<(string), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KiwiAnnotationData$Type = ($KiwiAnnotationData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KiwiAnnotationData_ = $KiwiAnnotationData$Type;
}}
declare module "packages/snownee/kiwi/util/$MathUtil" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $MathUtil {


public static "RGBtoHSV"(arg0: integer): $Vector3f
public static "fibonacciSphere"(arg0: $Vec3$Type, arg1: double, arg2: integer, arg3: boolean): $List<($Vec3)>
public static "posOnLine"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $Collection$Type<($BlockPos$Type)>): integer
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
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$ScalarStyle extends $Enum<($DumperOptions$ScalarStyle)> {
static readonly "DOUBLE_QUOTED": $DumperOptions$ScalarStyle
static readonly "SINGLE_QUOTED": $DumperOptions$ScalarStyle
static readonly "LITERAL": $DumperOptions$ScalarStyle
static readonly "FOLDED": $DumperOptions$ScalarStyle
static readonly "PLAIN": $DumperOptions$ScalarStyle


public "toString"(): string
public static "values"(): ($DumperOptions$ScalarStyle)[]
public "getChar"(): character
public static "valueOf"(arg0: string): $DumperOptions$ScalarStyle
public static "createStyle"(arg0: character): $DumperOptions$ScalarStyle
get "char"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$ScalarStyle$Type = (("double_quoted") | ("plain") | ("single_quoted") | ("folded") | ("literal")) | ($DumperOptions$ScalarStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$ScalarStyle_ = $DumperOptions$ScalarStyle$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator" {
import {$Node, $Node$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $AnchorGenerator {

 "nextAnchor"(arg0: $Node$Type): string

(arg0: $Node$Type): string
}

export namespace $AnchorGenerator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorGenerator$Type = ($AnchorGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorGenerator_ = $AnchorGenerator$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$AsinHFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $AsinHFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsinHFunction$Type = ($AsinHFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsinHFunction_ = $AsinHFunction$Type;
}}
declare module "packages/snownee/kiwi/block/entity/$InheritanceBlockEntityType" {
import {$BlockEntityType$BlockEntitySupplier, $BlockEntityType$BlockEntitySupplier$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$BlockEntitySupplier"
import {$ShulkerBoxBlockEntity, $ShulkerBoxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ShulkerBoxBlockEntity"
import {$BeehiveBlockEntity, $BeehiveBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeehiveBlockEntity"
import {$ComparatorBlockEntity, $ComparatorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ComparatorBlockEntity"
import {$DynamicLightHandlerHolder, $DynamicLightHandlerHolder$Type} from "packages/dev/lambdaurora/lambdynlights/accessor/$DynamicLightHandlerHolder"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DropperBlockEntity, $DropperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DropperBlockEntity"
import {$BrewingStandBlockEntity, $BrewingStandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrewingStandBlockEntity"
import {$DaylightDetectorBlockEntity, $DaylightDetectorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DaylightDetectorBlockEntity"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$HangingSignBlockEntity, $HangingSignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HangingSignBlockEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SignBlockEntity, $SignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SignBlockEntity"
import {$JukeboxBlockEntity, $JukeboxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JukeboxBlockEntity"
import {$SculkShriekerBlockEntity, $SculkShriekerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkShriekerBlockEntity"
import {$EnchantmentTableBlockEntity, $EnchantmentTableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnchantmentTableBlockEntity"
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$CalibratedSculkSensorBlockEntity, $CalibratedSculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CalibratedSculkSensorBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChiseledBookShelfBlockEntity, $ChiseledBookShelfBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChiseledBookShelfBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BrushableBlockEntity, $BrushableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrushableBlockEntity"
import {$FurnaceBlockEntity, $FurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$FurnaceBlockEntity"
import {$DispenserBlockEntity, $DispenserBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DispenserBlockEntity"
import {$EnderChestBlockEntity, $EnderChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnderChestBlockEntity"
import {$SculkSensorBlockEntity, $SculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkSensorBlockEntity"
import {$BarrelBlockEntity, $BarrelBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BarrelBlockEntity"
import {$ChestBlockEntity, $ChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChestBlockEntity"
import {$BannerBlockEntity, $BannerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BannerBlockEntity"
import {$TheEndGatewayBlockEntity, $TheEndGatewayBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndGatewayBlockEntity"
import {$CommandBlockEntity, $CommandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CommandBlockEntity"
import {$BellBlockEntity, $BellBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BellBlockEntity"
import {$TrappedChestBlockEntity, $TrappedChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TrappedChestBlockEntity"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SmokerBlockEntity, $SmokerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SmokerBlockEntity"
import {$BlastFurnaceBlockEntity, $BlastFurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlastFurnaceBlockEntity"
import {$DecoratedPotBlockEntity, $DecoratedPotBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DecoratedPotBlockEntity"
import {$PistonMovingBlockEntity, $PistonMovingBlockEntity$Type} from "packages/net/minecraft/world/level/block/piston/$PistonMovingBlockEntity"
import {$HopperBlockEntity, $HopperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HopperBlockEntity"
import {$TheEndPortalBlockEntity, $TheEndPortalBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndPortalBlockEntity"
import {$BeaconBlockEntity, $BeaconBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeaconBlockEntity"
import {$StructureBlockEntity, $StructureBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$StructureBlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$SpawnerBlockEntity, $SpawnerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SpawnerBlockEntity"
import {$JigsawBlockEntity, $JigsawBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JigsawBlockEntity"
import {$ConduitBlockEntity, $ConduitBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ConduitBlockEntity"
import {$BedBlockEntity, $BedBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BedBlockEntity"
import {$SculkCatalystBlockEntity, $SculkCatalystBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkCatalystBlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SkullBlockEntity, $SkullBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SkullBlockEntity"
import {$LecternBlockEntity, $LecternBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$LecternBlockEntity"
import {$CampfireBlockEntity, $CampfireBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CampfireBlockEntity"

export class $InheritanceBlockEntityType<T extends $BlockEntity> extends $BlockEntityType<(T)> {
static readonly "FURNACE": $BlockEntityType<($FurnaceBlockEntity)>
static readonly "CHEST": $BlockEntityType<($ChestBlockEntity)>
static readonly "TRAPPED_CHEST": $BlockEntityType<($TrappedChestBlockEntity)>
static readonly "ENDER_CHEST": $BlockEntityType<($EnderChestBlockEntity)>
static readonly "JUKEBOX": $BlockEntityType<($JukeboxBlockEntity)>
static readonly "DISPENSER": $BlockEntityType<($DispenserBlockEntity)>
static readonly "DROPPER": $BlockEntityType<($DropperBlockEntity)>
static readonly "SIGN": $BlockEntityType<($SignBlockEntity)>
static readonly "HANGING_SIGN": $BlockEntityType<($HangingSignBlockEntity)>
static readonly "MOB_SPAWNER": $BlockEntityType<($SpawnerBlockEntity)>
static readonly "PISTON": $BlockEntityType<($PistonMovingBlockEntity)>
static readonly "BREWING_STAND": $BlockEntityType<($BrewingStandBlockEntity)>
static readonly "ENCHANTING_TABLE": $BlockEntityType<($EnchantmentTableBlockEntity)>
static readonly "END_PORTAL": $BlockEntityType<($TheEndPortalBlockEntity)>
static readonly "BEACON": $BlockEntityType<($BeaconBlockEntity)>
static readonly "SKULL": $BlockEntityType<($SkullBlockEntity)>
static readonly "DAYLIGHT_DETECTOR": $BlockEntityType<($DaylightDetectorBlockEntity)>
static readonly "HOPPER": $BlockEntityType<($HopperBlockEntity)>
static readonly "COMPARATOR": $BlockEntityType<($ComparatorBlockEntity)>
static readonly "BANNER": $BlockEntityType<($BannerBlockEntity)>
static readonly "STRUCTURE_BLOCK": $BlockEntityType<($StructureBlockEntity)>
static readonly "END_GATEWAY": $BlockEntityType<($TheEndGatewayBlockEntity)>
static readonly "COMMAND_BLOCK": $BlockEntityType<($CommandBlockEntity)>
static readonly "SHULKER_BOX": $BlockEntityType<($ShulkerBoxBlockEntity)>
static readonly "BED": $BlockEntityType<($BedBlockEntity)>
static readonly "CONDUIT": $BlockEntityType<($ConduitBlockEntity)>
static readonly "BARREL": $BlockEntityType<($BarrelBlockEntity)>
static readonly "SMOKER": $BlockEntityType<($SmokerBlockEntity)>
static readonly "BLAST_FURNACE": $BlockEntityType<($BlastFurnaceBlockEntity)>
static readonly "LECTERN": $BlockEntityType<($LecternBlockEntity)>
static readonly "BELL": $BlockEntityType<($BellBlockEntity)>
static readonly "JIGSAW": $BlockEntityType<($JigsawBlockEntity)>
static readonly "CAMPFIRE": $BlockEntityType<($CampfireBlockEntity)>
static readonly "BEEHIVE": $BlockEntityType<($BeehiveBlockEntity)>
static readonly "SCULK_SENSOR": $BlockEntityType<($SculkSensorBlockEntity)>
static readonly "CALIBRATED_SCULK_SENSOR": $BlockEntityType<($CalibratedSculkSensorBlockEntity)>
static readonly "SCULK_CATALYST": $BlockEntityType<($SculkCatalystBlockEntity)>
static readonly "SCULK_SHRIEKER": $BlockEntityType<($SculkShriekerBlockEntity)>
static readonly "CHISELED_BOOKSHELF": $BlockEntityType<($ChiseledBookShelfBlockEntity)>
static readonly "BRUSHABLE_BLOCK": $BlockEntityType<($BrushableBlockEntity)>
static readonly "DECORATED_POT": $BlockEntityType<($DecoratedPotBlockEntity)>
 "validBlocks": $Set<($Block)>

constructor(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $Class$Type<(any)>, arg2: $Type$Type<(any)>)

public "isValid"(arg0: $BlockState$Type): boolean
public static "cast"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
public static "cast"<T extends $Entity>(arg0: $EntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InheritanceBlockEntityType$Type<T> = ($InheritanceBlockEntityType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InheritanceBlockEntityType_<T> = $InheritanceBlockEntityType$Type<(T)>;
}}
declare module "packages/snownee/kiwi/customization/command/$ExportBlocksCommand" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$KBlockSettings, $KBlockSettings$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$KBlockSettings$MoreInfo, $KBlockSettings$MoreInfo$Type} from "packages/snownee/kiwi/customization/block/$KBlockSettings$MoreInfo"

export class $ExportBlocksCommand {
static readonly "TEMPLATE_MAPPING": $Supplier<($Map<($Class<(any)>), (string)>)>

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "toYaml"<T>(arg0: $Codec$Type<(T)>, arg1: T, arg2: $UnaryOperator$Type<($JsonElement$Type)>): string
public static "putMoreInfo"(arg0: $KBlockSettings$Type, arg1: $KBlockSettings$MoreInfo$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExportBlocksCommand$Type = ($ExportBlocksCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExportBlocksCommand_ = $ExportBlocksCommand$Type;
}}
declare module "packages/snownee/kiwi/loader/$Platform$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Platform$Type extends $Enum<($Platform$Type)> {
static readonly "Vanilla": $Platform$Type
static readonly "Fabric": $Platform$Type
static readonly "Quilt": $Platform$Type
static readonly "Forge": $Platform$Type
static readonly "NeoForge": $Platform$Type
static readonly "Unknown": $Platform$Type


public static "values"(): ($Platform$Type)[]
public static "valueOf"(arg0: string): $Platform$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Platform$Type$Type = (("quilt") | ("fabric") | ("forge") | ("neoforge") | ("vanilla") | ("unknown")) | ($Platform$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Platform$Type_ = $Platform$Type$Type;
}}
declare module "packages/snownee/kiwi/customization/$CustomFeatureTags" {
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $CustomFeatureTags {
static readonly "SUSTAIN_PLANT": $TagKey<($Block)>
static readonly "SITTABLE": $TagKey<($Block)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomFeatureTags$Type = ($CustomFeatureTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomFeatureTags_ = $CustomFeatureTags$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/scanner/$ScannerException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ScannerException extends $MarkedYAMLException {

constructor(arg0: string, arg1: $Mark$Type, arg2: string, arg3: $Mark$Type, arg4: string)
constructor(arg0: string, arg1: $Mark$Type, arg2: string, arg3: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScannerException$Type = ($ScannerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScannerException_ = $ScannerException$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/$LoaderOptions" {
import {$TagInspector, $TagInspector$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/inspector/$TagInspector"

export class $LoaderOptions {

constructor()

public "setProcessComments"(arg0: boolean): $LoaderOptions
public "isProcessComments"(): boolean
public "getCodePointLimit"(): integer
public "getTagInspector"(): $TagInspector
public "setTagInspector"(arg0: $TagInspector$Type): void
public "setCodePointLimit"(arg0: integer): void
public "isAllowDuplicateKeys"(): boolean
public "isWrappedToRootException"(): boolean
public "setWrappedToRootException"(arg0: boolean): void
public "setAllowDuplicateKeys"(arg0: boolean): void
public "getAllowRecursiveKeys"(): boolean
public "isEnumCaseSensitive"(): boolean
public "setEnumCaseSensitive"(arg0: boolean): void
public "setMaxAliasesForCollections"(arg0: integer): void
public "setAllowRecursiveKeys"(arg0: boolean): void
public "getMaxAliasesForCollections"(): integer
public "setNestingDepthLimit"(arg0: integer): void
public "getNestingDepthLimit"(): integer
set "processComments"(value: boolean)
get "processComments"(): boolean
get "codePointLimit"(): integer
get "tagInspector"(): $TagInspector
set "tagInspector"(value: $TagInspector$Type)
set "codePointLimit"(value: integer)
get "allowDuplicateKeys"(): boolean
get "wrappedToRootException"(): boolean
set "wrappedToRootException"(value: boolean)
set "allowDuplicateKeys"(value: boolean)
get "allowRecursiveKeys"(): boolean
get "enumCaseSensitive"(): boolean
set "enumCaseSensitive"(value: boolean)
set "maxAliasesForCollections"(value: integer)
set "allowRecursiveKeys"(value: boolean)
get "maxAliasesForCollections"(): integer
set "nestingDepthLimit"(value: integer)
get "nestingDepthLimit"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoaderOptions$Type = ($LoaderOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoaderOptions_ = $LoaderOptions$Type;
}}
declare module "packages/snownee/kiwi/recipe/crafting/$RetextureRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$DynamicShapedRecipe, $DynamicShapedRecipe$Type} from "packages/snownee/kiwi/recipe/crafting/$DynamicShapedRecipe"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CraftingBookCategory, $CraftingBookCategory$Type} from "packages/net/minecraft/world/item/crafting/$CraftingBookCategory"

export class $RetextureRecipe extends $DynamicShapedRecipe {
 "pattern": string
 "differentInputs": boolean
 "recipeOutput": $ItemStack

constructor(arg0: $ResourceLocation$Type, arg1: $CraftingBookCategory$Type)

public "matches"(arg0: $CraftingContainer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RetextureRecipe$Type = ($RetextureRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RetextureRecipe_ = $RetextureRecipe$Type;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/datetime/$DateTimeParseFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $DateTimeParseFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeParseFunction$Type = ($DateTimeParseFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeParseFunction_ = $DateTimeParseFunction$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$Property, $Property$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property"

export class $GenericProperty extends $Property {

constructor(arg0: string, arg1: $Class$Type<(any)>, arg2: $Type$Type)

public "getActualTypeArguments"(): ($Class<(any)>)[]
get "actualTypeArguments"(): ($Class<(any)>)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericProperty$Type = ($GenericProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericProperty_ = $GenericProperty$Type;
}}
declare module "packages/snownee/kiwi/customization/item/loader/$ConfiguredItemTemplate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$KItemTemplate, $KItemTemplate$Type} from "packages/snownee/kiwi/customization/item/loader/$KItemTemplate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfiguredItemTemplate extends $Record {
static readonly "DEFAULT_JSON": $JsonObject

constructor(arg0: $KItemTemplate$Type)
constructor(template: $KItemTemplate$Type, json: $JsonObject$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "json"(): $JsonObject
public "template"(): $KItemTemplate
public static "codec"(arg0: $Map$Type<($ResourceLocation$Type), ($KItemTemplate$Type)>): $Codec<($ConfiguredItemTemplate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguredItemTemplate$Type = ($ConfiguredItemTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguredItemTemplate_ = $ConfiguredItemTemplate$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$DocumentEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/events/$Event"

export class $DocumentEndEvent extends $Event {

constructor(arg0: $Mark$Type, arg1: $Mark$Type, arg2: boolean)

public "getEventId"(): $Event$ID
public "getExplicit"(): boolean
get "eventId"(): $Event$ID
get "explicit"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEndEvent$Type = ($DocumentEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEndEvent_ = $DocumentEndEvent$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/error/$YAMLException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $YAMLException extends $RuntimeException {

constructor(arg0: string)
constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YAMLException$Type = ($YAMLException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YAMLException_ = $YAMLException$Type;
}}
declare module "packages/snownee/kiwi/customization/builder/$BuildersButton" {
import {$CConvertItemPacket$Group, $CConvertItemPacket$Group$Type} from "packages/snownee/kiwi/customization/network/$CConvertItemPacket$Group"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BuilderModePreview, $BuilderModePreview$Type} from "packages/snownee/kiwi/customization/builder/$BuilderModePreview"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BuildersButton {

constructor()

public static "onLongPress"(): boolean
public static "cancelRenderHighlight"(): boolean
public static "performUseItemOn"(arg0: $InteractionHand$Type, arg1: $BlockHitResult$Type): boolean
public static "onDoublePress"(): boolean
public static "isBuilderModeOn"(): boolean
public static "onShortPress"(): boolean
public static "findConvertGroups"(arg0: $Player$Type, arg1: $ItemStack$Type): $List<($CConvertItemPacket$Group)>
public static "getPreviewRenderer"(): $BuilderModePreview
public static "startDestroyBlock"(arg0: $BlockPos$Type, arg1: $Direction$Type): boolean
public static "renderDebugText"(arg0: $List$Type<(string)>, arg1: $List$Type<(string)>): void
get "builderModeOn"(): boolean
get "previewRenderer"(): $BuilderModePreview
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildersButton$Type = ($BuildersButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildersButton_ = $BuildersButton$Type;
}}
declare module "packages/snownee/kiwi/loader/event/$ParallelEvent" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ParallelDispatchEvent, $ParallelDispatchEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$ParallelDispatchEvent"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ParallelEvent {

constructor(arg0: $ParallelDispatchEvent$Type)

public "enqueueWork"(arg0: $Runnable$Type): $CompletableFuture<(void)>
public "enqueueWork"<T>(arg0: $Supplier$Type<(T)>): $CompletableFuture<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParallelEvent$Type = ($ParallelEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParallelEvent_ = $ParallelEvent$Type;
}}
declare module "packages/snownee/kiwi/schedule/impl/$LevelTicker" {
import {$LevelEvent$Unload, $LevelEvent$Unload$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Unload"
import {$ITicker, $ITicker$Type} from "packages/snownee/kiwi/schedule/$ITicker"
import {$TickEvent$Phase, $TickEvent$Phase$Type} from "packages/net/minecraftforge/event/$TickEvent$Phase"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"

export class $LevelTicker implements $ITicker {


public static "get"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $TickEvent$Phase$Type): $LevelTicker
public static "get"(arg0: $Level$Type, arg1: $TickEvent$Phase$Type): $LevelTicker
public "destroy"(): void
public "getLevel"(): $Level
public static "unloadLevel"(arg0: $LevelEvent$Unload$Type): void
public static "onTick"(arg0: $TickEvent$LevelTickEvent$Type): void
get "level"(): $Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelTicker$Type = ($LevelTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelTicker_ = $LevelTicker$Type;
}}
declare module "packages/snownee/kiwi/customization/command/$PrintFamiliesCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $PrintFamiliesCommand {

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintFamiliesCommand$Type = ($PrintFamiliesCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintFamiliesCommand_ = $PrintFamiliesCommand$Type;
}}
declare module "packages/snownee/kiwi/loader/$DevEnvAnnotatedTypeLoader" {
import {$AnnotatedTypeLoader, $AnnotatedTypeLoader$Type} from "packages/snownee/kiwi/loader/$AnnotatedTypeLoader"

export class $DevEnvAnnotatedTypeLoader extends $AnnotatedTypeLoader {
readonly "modId": string

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DevEnvAnnotatedTypeLoader$Type = ($DevEnvAnnotatedTypeLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DevEnvAnnotatedTypeLoader_ = $DevEnvAnnotatedTypeLoader$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$MissingProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/introspector/$Property"

export class $MissingProperty extends $Property {

constructor(arg0: string)

public "get"(arg0: any): any
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
public "getActualTypeArguments"(): ($Class<(any)>)[]
get "annotations"(): $List<($Annotation)>
get "actualTypeArguments"(): ($Class<(any)>)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MissingProperty$Type = ($MissingProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MissingProperty_ = $MissingProperty$Type;
}}
declare module "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$DuplicateKeyException" {
import {$ConstructorException, $ConstructorException$Type} from "packages/snownee/kiwi/shadowed/org/yaml/snakeyaml/constructor/$ConstructorException"

export class $DuplicateKeyException extends $ConstructorException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DuplicateKeyException$Type = ($DuplicateKeyException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DuplicateKeyException_ = $DuplicateKeyException$Type;
}}
declare module "packages/snownee/kiwi/network/$PacketHandler" {
import {$KPacketTarget, $KPacketTarget$Type} from "packages/snownee/kiwi/network/$KPacketTarget"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$KiwiPacket$Direction, $KiwiPacket$Direction$Type} from "packages/snownee/kiwi/network/$KiwiPacket$Direction"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$IPacketHandler, $IPacketHandler$Type} from "packages/snownee/kiwi/network/$IPacketHandler"

export class $PacketHandler implements $IPacketHandler {
 "id": $ResourceLocation

constructor()

/**
 * 
 * @deprecated
 */
public "send"(arg0: $PacketDistributor$PacketTarget$Type, arg1: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "send"(arg0: $ServerPlayer$Type, arg1: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "send"(arg0: $KPacketTarget$Type, arg1: $Consumer$Type<($FriendlyByteBuf$Type)>): void
/**
 * 
 * @deprecated
 */
public "sendAllExcept"(arg0: $ServerPlayer$Type, arg1: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "getDirection"(): $KiwiPacket$Direction
public "sendToServer"(arg0: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "receive"(arg0: $Function$Type<($Runnable$Type), ($CompletableFuture$Type<($FriendlyByteBuf$Type)>)>, arg1: $FriendlyByteBuf$Type, arg2: $ServerPlayer$Type): $CompletableFuture<($FriendlyByteBuf)>
get "direction"(): $KiwiPacket$Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketHandler$Type = ($PacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketHandler_ = $PacketHandler$Type;
}}
declare module "packages/snownee/kiwi/customization/duck/$KPlayer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KPlayer {

 "kiwi$setPlaceCount"(arg0: integer): void
 "kiwi$getPlaceCount"(): integer
 "kiwi$incrementPlaceCount"(): void
}

export namespace $KPlayer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KPlayer$Type = ($KPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KPlayer_ = $KPlayer$Type;
}}
declare module "packages/snownee/kiwi/customization/shape/$SixWayShape" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ShapeGenerator, $ShapeGenerator$Type} from "packages/snownee/kiwi/customization/shape/$ShapeGenerator"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $SixWayShape extends $Record implements $ShapeGenerator {

constructor(shapes: ($VoxelShape$Type)[], base: $VoxelShape$Type, trueDown: $VoxelShape$Type, falseDown: $VoxelShape$Type)

public "base"(): $VoxelShape
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "create"(arg0: $ShapeGenerator$Type, arg1: $ShapeGenerator$Type, arg2: $ShapeGenerator$Type): $ShapeGenerator
public "getShape"(arg0: $BlockState$Type, arg1: $CollisionContext$Type): $VoxelShape
public "shapes"(): ($VoxelShape)[]
public "falseDown"(): $VoxelShape
public "trueDown"(): $VoxelShape
public static "unit"(arg0: $VoxelShape$Type): $ShapeGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SixWayShape$Type = ($SixWayShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SixWayShape_ = $SixWayShape$Type;
}}
declare module "packages/snownee/kiwi/contributor/impl/client/model/$SunnyMilkModel" {
import {$AgeableListModel, $AgeableListModel$Type} from "packages/net/minecraft/client/model/$AgeableListModel"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $SunnyMilkModel<T extends $LivingEntity> extends $AgeableListModel<(T)> {
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $LayerDefinition$Type)

public static "create"(): $LayerDefinition
public "setupAnim"(arg0: T, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SunnyMilkModel$Type<T> = ($SunnyMilkModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SunnyMilkModel_<T> = $SunnyMilkModel$Type<(T)>;
}}
declare module "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/trigonometric/$TanRFunction" {
import {$AbstractFunction, $AbstractFunction$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/functions/$AbstractFunction"
import {$EvaluationValue, $EvaluationValue$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/data/$EvaluationValue"
import {$Expression, $Expression$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/$Expression"
import {$Token, $Token$Type} from "packages/snownee/kiwi/shadowed/com/ezylang/evalex/parser/$Token"

export class $TanRFunction extends $AbstractFunction {

constructor()

public "evaluate"(arg0: $Expression$Type, arg1: $Token$Type, ...arg2: ($EvaluationValue$Type)[]): $EvaluationValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TanRFunction$Type = ($TanRFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TanRFunction_ = $TanRFunction$Type;
}}
