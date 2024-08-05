declare module "packages/extensions/net/minecraft/world/entity/Entity/$EntityExt" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityExt {


public static "spawn"(thiz: $Entity$Type, level: $Level$Type, x: double, y: double, z: double): void
public static "spawn"(thiz: $Entity$Type, level: $Level$Type, pos: $Vec3$Type, dataSupplier: $Function$Type<($Entity$Type), ($Entity$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityExt$Type = ($EntityExt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityExt_ = $EntityExt$Type;
}}
declare module "packages/extensions/net/minecraft/world/entity/item/ItemEntity/$ItemEntityExt" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $ItemEntityExt {


public static "of"(level: $Level$Type, stack: $ItemStack$Type): $ItemEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEntityExt$Type = ($ItemEntityExt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEntityExt_ = $ItemEntityExt$Type;
}}
declare module "packages/extensions/net/minecraft/world/item/ItemStack/$ItemStackExt" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackExt {


public static "serialize"(thiz: $ItemStack$Type): $CompoundTag
public static "canStack"(thiz: $ItemStack$Type, other: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackExt$Type = ($ItemStackExt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackExt_ = $ItemStackExt$Type;
}}
declare module "packages/extensions/net/minecraft/world/entity/LivingEntity/$LivingEntityExt" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingEntityExt {


public static "setMainHandItem"(thiz: $LivingEntity$Type, stack: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEntityExt$Type = ($LivingEntityExt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEntityExt_ = $LivingEntityExt$Type;
}}
