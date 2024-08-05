declare module "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestReward" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export interface $IEnumQuestReward {

 "getReward"(): $AbstractReward
 "registerEnumReward"(arg0: $Class$Type<(any)>): void

(): $AbstractReward
}

export namespace $IEnumQuestReward {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumQuestReward$Type = ($IEnumQuestReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumQuestReward_ = $IEnumQuestReward$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$KillGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $KillGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KillGoal$Type = ($KillGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KillGoal_ = $KillGoal$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $AbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractGoal$Type = ($AbstractGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractGoal_ = $AbstractGoal$Type;
}}
declare module "packages/net/zanckor/questapi/util/$Timer" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export class $Timer {

constructor()

public static "canUseWithCooldown"(arg0: $UUID$Type, arg1: string, arg2: float): boolean
public static "clearTimer"(arg0: $UUID$Type, arg1: string): void
public static "remainingTime"(arg0: $UUID$Type, arg1: string): long
public static "existsTimer"(arg0: $UUID$Type, arg1: string): boolean
public static "updateCooldown"(arg0: $UUID$Type, arg1: string, arg2: float): void
public static "clearTimers"(arg0: $UUID$Type): void
public static "canUseWithTicker"(arg0: $UUID$Type, arg1: string, arg2: boolean, arg3: integer): boolean
public static "updateTicker"(arg0: $UUID$Type, arg1: string, arg2: integer): void
public static "calculateCooldownRemainder"(arg0: long): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Timer$Type = ($Timer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Timer_ = $Timer$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/npc/entity_type/codec/$EntityTypeDialog" {
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"

export class $EntityTypeDialog extends $FileAbstract {

constructor()

public "getId"(): string
public "getDialog_list"(): $List<(string)>
public "getEntity_type"(): $List<(string)>
get "id"(): string
get "dialog_list"(): $List<(string)>
get "entity_type"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeDialog$Type = ($EntityTypeDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeDialog_ = $EntityTypeDialog$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest" {
import {$ServerGoal, $ServerGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerGoal"
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$ServerRequirement, $ServerRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerRequirement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerReward, $ServerReward$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerReward"

export class $ServerQuest extends $FileAbstract {

constructor()

public "getId"(): string
public "getDescription"(): string
public "setDescription"(arg0: string): void
public "setGoalList"(arg0: $List$Type<($ServerGoal$Type)>): void
public "setRewards"(arg0: $List$Type<($ServerReward$Type)>): void
public "setHasTimeLimit"(arg0: boolean): void
public "setRequirements"(arg0: $List$Type<($ServerRequirement$Type)>): void
public "setTitle"(arg0: string): void
public "getTitle"(): string
public "setId"(arg0: string): void
public "getTimeLimitInSeconds"(): integer
public "setTimeLimitInSeconds"(arg0: integer): void
public "getRequirements"(): $List<($ServerRequirement)>
public "getRewards"(): $List<($ServerReward)>
public "getGoalList"(): $List<($ServerGoal)>
public "hasTimeLimit"(): boolean
get "id"(): string
get "description"(): string
set "description"(value: string)
set "goalList"(value: $List$Type<($ServerGoal$Type)>)
set "rewards"(value: $List$Type<($ServerReward$Type)>)
set "requirements"(value: $List$Type<($ServerRequirement$Type)>)
set "title"(value: string)
get "title"(): string
set "id"(value: string)
get "timeLimitInSeconds"(): integer
set "timeLimitInSeconds"(value: integer)
get "requirements"(): $List<($ServerRequirement)>
get "rewards"(): $List<($ServerReward)>
get "goalList"(): $List<($ServerGoal)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerQuest$Type = ($ServerQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerQuest_ = $ServerQuest$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/handler/$ServerHandler" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"

export class $ServerHandler {

constructor()

public static "requestDialog"(arg0: $ServerPlayer$Type, arg1: integer, arg2: $Enum$Type<(any)>, arg3: $UUID$Type, arg4: $Item$Type, arg5: $NpcType$Type): void
public static "questTimer"(arg0: $ServerLevel$Type): void
public static "questHandler"(arg0: $Enum$Type<(any)>, arg1: $ServerPlayer$Type, arg2: $LivingEntity$Type): void
public static "addQuest"(arg0: $Player$Type, arg1: $Enum$Type<(any)>, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerHandler$Type = ($ServerHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerHandler_ = $ServerHandler$Type;
}}
declare module "packages/net/zanckor/questapi/multiloader/platform/services/$IPlatformHelper" {
import {$PlatformEnum, $PlatformEnum$Type} from "packages/net/zanckor/questapi/multiloader/platform/services/$PlatformEnum"

export interface $IPlatformHelper {

 "getEnvironmentName"(): string
 "isDevelopmentEnvironment"(): boolean
 "getPlatform"(): $PlatformEnum
 "isModLoaded"(arg0: string): boolean
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
declare module "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"

export class $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractReward$Type = ($AbstractReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractReward_ = $AbstractReward$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/hud/$RenderQuestTracker" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractQuestTracked, $AbstractQuestTracked$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractQuestTracked"

export class $RenderQuestTracker extends $AbstractQuestTracked {

constructor()

public static "renderTitle"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: $Minecraft$Type, arg3: $UserQuest$Type): void
public "renderQuestTracked"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public static "renderQuests"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: $Minecraft$Type, arg3: $HashMap$Type<(string), ($List$Type<($UserGoal$Type)>)>): void
public static "renderQuestType"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: $Minecraft$Type, arg3: $HashMap$Type<(string), ($List$Type<($UserGoal$Type)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderQuestTracker$Type = ($RenderQuestTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderQuestTracker_ = $RenderQuestTracker$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$CollectNBTGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CollectNBTGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
public static "checkItemsNBT"(arg0: $UserGoal$Type, arg1: $ServerPlayer$Type, arg2: integer): boolean
public static "checkItemsNBT"(arg0: $UserGoal$Type, arg1: $ServerPlayer$Type, arg2: $List$Type<(integer)>): boolean
public static "removeItems"(arg0: $ServerPlayer$Type, arg1: $UserGoal$Type): void
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectNBTGoal$Type = ($CollectNBTGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectNBTGoal_ = $CollectNBTGoal$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$CollectGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CollectGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
public static "removeItems"(arg0: $ServerPlayer$Type, arg1: $UserGoal$Type): void
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectGoal$Type = ($CollectGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectGoal_ = $CollectGoal$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$LoadDialog" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $LoadDialog {

constructor()

public static "registerDialog"(arg0: $MinecraftServer$Type, arg1: string): void
public static "registerDatapackDialog"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadDialog$Type = ($LoadDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadDialog_ = $LoadDialog$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $AbstractDialogRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDialogRequirement$Type = ($AbstractDialogRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDialogRequirement_ = $AbstractDialogRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogoption/$DialogOpenDialog" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DialogOpenDialog extends $AbstractDialogOption {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogOpenDialog$Type = ($DialogOpenDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogOpenDialog_ = $DialogOpenDialog$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$KillEvent" {
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"

export class $KillEvent {

constructor()

public static "killQuest"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KillEvent$Type = ($KillEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KillEvent_ = $KillEvent$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$MoveToTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $MoveToTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoveToTargetType$Type = ($MoveToTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoveToTargetType_ = $MoveToTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/mod/client/event/$ClientEvent" {
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$RenderLivingEvent$Pre, $RenderLivingEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderLivingEvent$Pre"
import {$ClientPlayerNetworkEvent$LoggingIn, $ClientPlayerNetworkEvent$LoggingIn$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingIn"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $ClientEvent {

constructor()

public static "loadHashMaps"(arg0: $ClientPlayerNetworkEvent$LoggingIn$Type): void
public static "keyOpenScreen"(arg0: $InputEvent$Key$Type): void
public static "renderNPCQuestMarker"(arg0: $RenderLivingEvent$Pre$Type<(any), (any)>): void
public static "checkEntityTagIsValid"(arg0: $LivingEntity$Type): boolean
public static "checkEntityTypeIsValid"(arg0: $LivingEntity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEvent$Type = ($ClientEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEvent_ = $ClientEvent$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumdialog/$EnumDialogReq" {
import {$IEnumDialogReq, $IEnumDialogReq$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogReq"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"

export class $EnumDialogReq extends $Enum<($EnumDialogReq)> implements $IEnumDialogReq {
static readonly "QUEST": $EnumDialogReq
static readonly "DIALOG": $EnumDialogReq
static readonly "NONE": $EnumDialogReq


public static "values"(): ($EnumDialogReq)[]
public static "valueOf"(arg0: string): $EnumDialogReq
public "getDialogRequirement"(): $AbstractDialogRequirement
public "registerEnumDialogReq"(arg0: $Class$Type<(any)>): void
get "dialogRequirement"(): $AbstractDialogRequirement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumDialogReq$Type = (("dialog") | ("none") | ("quest")) | ($EnumDialogReq);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumDialogReq_ = $EnumDialogReq$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT" {
import {$EntityTypeTagDialog$EntityTypeTagDialogCondition, $EntityTypeTagDialog$EntityTypeTagDialogCondition$Type} from "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog$EntityTypeTagDialogCondition"

export class $EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT {

constructor(arg0: $EntityTypeTagDialog$EntityTypeTagDialogCondition$Type)

public "getValue"(): string
public "getTag"(): string
get "value"(): string
get "tag"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT$Type = ($EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT_ = $EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questreward/$CommandReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $CommandReward extends $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandReward$Type = ($CommandReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandReward_ = $CommandReward$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$InteractSpecificEntityEvent" {
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"

export class $InteractSpecificEntityEvent {

constructor()

public static "interactWithNPC"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractSpecificEntityEvent$Type = ($InteractSpecificEntityEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractSpecificEntityEvent_ = $InteractSpecificEntityEvent$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogrequirement/$QuestRequirement" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $QuestRequirement extends $AbstractDialogRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestRequirement$Type = ($QuestRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestRequirement_ = $QuestRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questreward/$LootTableReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $LootTableReward extends $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableReward$Type = ($LootTableReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableReward_ = $LootTableReward$Type;
}}
declare module "packages/net/zanckor/questapi/mod/server/startdialog/$StartDialog" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $StartDialog {

constructor()

public static "loadDialog"(arg0: $Player$Type, arg1: string, arg2: $Item$Type): void
public static "loadDialog"(arg0: $Player$Type, arg1: string, arg2: string): void
public static "loadDialog"(arg0: $Player$Type, arg1: string, arg2: $Entity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StartDialog$Type = ($StartDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StartDialog_ = $StartDialog$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerGoal" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $ServerGoal {

constructor()

public "getType"(): string
public "getTarget"(): string
public "setTarget"(arg0: string): void
public "setType"(arg0: string): void
public "setAmount"(arg0: integer): void
public "getAmount"(): integer
public "getAdditionalListData"(): $List<(any)>
public "getAdditionalStringData"(): string
public "setAdditionalClassData"(arg0: any): void
public "getAdditionalDoubleData"(): double
public "setAdditionalIntegerData"(arg0: integer): void
public "getAdditionalIntegerData"(): integer
public "setAdditionalStringData"(arg0: string): void
public "setAdditionalListData"(arg0: $List$Type<(any)>): void
public "setAdditionalDoubleData"(arg0: double): void
public "getAdditionalClassData"(): any
get "type"(): string
get "target"(): string
set "target"(value: string)
set "type"(value: string)
set "amount"(value: integer)
get "amount"(): integer
get "additionalListData"(): $List<(any)>
get "additionalStringData"(): string
set "additionalClassData"(value: any)
get "additionalDoubleData"(): double
set "additionalIntegerData"(value: integer)
get "additionalIntegerData"(): integer
set "additionalStringData"(value: string)
set "additionalListData"(value: $List$Type<(any)>)
set "additionalDoubleData"(value: double)
get "additionalClassData"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerGoal$Type = ($ServerGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerGoal_ = $ServerGoal$Type;
}}
declare module "packages/net/zanckor/questapi/api/data/$QuestDialogManager" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$List, $List$Type} from "packages/java/util/$List"

export class $QuestDialogManager {
static "currentDialog": $HashMap<($Player), (integer)>
static "currentGlobalDialog": $HashMap<($Player), (string)>
static "clientQuestByIDLocation": $HashMap<(string), ($Path)>
static "clientQuestTypeLocation": $HashMap<($Enum<(any)>), ($List<($Path)>)>
static "conversationLocation": $HashMap<(string), ($Path)>
static "conversationByEntityType": $HashMap<(string), ($List<(string)>)>
static "conversationByCompoundTag": $HashMap<(string), ($File)>
static "conversationByID": $HashMap<(string), ($Conversation)>

constructor()

public static "getDialogPathByCompoundTag"(arg0: string): $File
public static "registerDialogByCompoundTag"(): void
public static "removeQuest"(arg0: string, arg1: $Enum$Type<(any)>): void
public static "movePathQuest"(arg0: string, arg1: $Path$Type, arg2: $Enum$Type<(any)>): void
public static "getQuestTypePathLocation"(arg0: $Enum$Type<(any)>): $List<($Path)>
public static "registerDialogByEntityType"(): void
public static "getConversationPathLocation"(arg0: string): $Path
public static "getConversationByID"(arg0: string): $Conversation
public static "registerQuestTypeLocation"(arg0: $Enum$Type<(any)>, arg1: $Path$Type): void
public static "getQuestPathByID"(arg0: string): $Path
public static "registerQuestByID"(arg0: string, arg1: $Path$Type): void
public static "getConversationByEntityType"(arg0: string): $List<(string)>
public static "registerConversationLocation"(arg0: string, arg1: $Path$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestDialogManager$Type = ($QuestDialogManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestDialogManager_ = $QuestDialogManager$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal" {
import {$ServerGoal, $ServerGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerGoal"
import {$List, $List$Type} from "packages/java/util/$List"

export class $UserGoal {

constructor()

public "getType"(): string
public "getTarget"(): string
public "setTarget"(arg0: string): void
public "setType"(arg0: string): void
public "setAmount"(arg0: integer): void
public "getAmount"(): integer
public "getAdditionalListData"(): $List<(any)>
public "getTranslatableType"(): string
public "getAdditionalStringData"(): string
public "setAdditionalClassData"(arg0: any): void
public "getAdditionalDoubleData"(): double
public "setAdditionalIntegerData"(arg0: integer): void
public "getAdditionalIntegerData"(): integer
public "setAdditionalStringData"(arg0: string): void
public "setAdditionalListData"(arg0: $List$Type<(any)>): void
public "setAdditionalDoubleData"(arg0: double): void
public "incrementCurrentAmount"(arg0: integer): void
public "getAdditionalClassData"(): any
public "getCurrentAmount"(): integer
public "setCurrentAmount"(arg0: integer): void
public static "createQuestGoal"(arg0: $ServerGoal$Type, arg1: string): $UserGoal
public "setCurrent_amount"(arg0: integer): void
public "getCurrent_amount"(): integer
public "setTranslatableType"(arg0: string): void
get "type"(): string
get "target"(): string
set "target"(value: string)
set "type"(value: string)
set "amount"(value: integer)
get "amount"(): integer
get "additionalListData"(): $List<(any)>
get "translatableType"(): string
get "additionalStringData"(): string
set "additionalClassData"(value: any)
get "additionalDoubleData"(): double
set "additionalIntegerData"(value: integer)
get "additionalIntegerData"(): integer
set "additionalStringData"(value: string)
set "additionalListData"(value: $List$Type<(any)>)
set "additionalDoubleData"(value: double)
get "additionalClassData"(): any
get "currentAmount"(): integer
set "currentAmount"(value: integer)
set "current_amount"(value: integer)
get "current_amount"(): integer
set "translatableType"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserGoal$Type = ($UserGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserGoal_ = $UserGoal$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogrequirement/$NoneRequirement" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NoneRequirement extends $AbstractDialogRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoneRequirement$Type = ($NoneRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoneRequirement_ = $NoneRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$LoadDialogList" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $LoadDialogList {

constructor()

public static "registerNPCDialogList"(arg0: $MinecraftServer$Type, arg1: string): void
public static "registerDatapackDialogList"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadDialogList$Type = ($LoadDialogList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadDialogList_ = $LoadDialogList$Type;
}}
declare module "packages/net/zanckor/questapi/$ForgeQuestAPI" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeQuestAPI {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeQuestAPI$Type = ($ForgeQuestAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeQuestAPI_ = $ForgeQuestAPI$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/hud/$QuestTrackedTimer" {
import {$TickEvent, $TickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent"

export class $QuestTrackedTimer {

constructor()

public static "tickEvent"(arg0: $TickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestTrackedTimer$Type = ($QuestTrackedTimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestTrackedTimer_ = $QuestTrackedTimer$Type;
}}
declare module "packages/net/zanckor/questapi/util/$Mathematic" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"

export class $Mathematic {

constructor()

public static "numberBetween"(arg0: double, arg1: double, arg2: double): boolean
public static "simpleMatrixToVec3"(arg0: $Matrix4f$Type): $Vec3
public static "vec3NumberBetween"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: double, arg3: double): boolean
public static "numberRandomizerBetween"(arg0: integer, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mathematic$Type = ($Mathematic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mathematic_ = $Mathematic$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/button/$TextButton" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $TextButton extends $Button {
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

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: float, arg5: $Component$Type, arg6: integer, arg7: $Button$OnPress$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextButton$Type = ($TextButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextButton_ = $TextButton$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/dialog/$DialogScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$AbstractDialog, $AbstractDialog$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractDialog"
import {$List, $List$Type} from "packages/java/util/$List"

export class $DialogScreen extends $AbstractDialog {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Component$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isPauseScreen"(): boolean
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "modifyScreen"(arg0: integer, arg1: string, arg2: integer, arg3: $HashMap$Type<(integer), ($List$Type<(integer)>)>, arg4: $HashMap$Type<(integer), ($List$Type<(string)>)>, arg5: $UUID$Type, arg6: string, arg7: $Item$Type, arg8: $NpcType$Type): $Screen
get "pauseScreen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogScreen$Type = ($DialogScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogScreen_ = $DialogScreen$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/dialog/$MinimalistDialogScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$AbstractDialog, $AbstractDialog$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractDialog"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MinimalistDialogScreen extends $AbstractDialog {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Component$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isPauseScreen"(): boolean
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "modifyScreen"(arg0: integer, arg1: string, arg2: integer, arg3: $HashMap$Type<(integer), ($List$Type<(integer)>)>, arg4: $HashMap$Type<(integer), ($List$Type<(string)>)>, arg5: $UUID$Type, arg6: string, arg7: $Item$Type, arg8: $NpcType$Type): $Screen
get "pauseScreen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinimalistDialogScreen$Type = ($MinimalistDialogScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinimalistDialogScreen_ = $MinimalistDialogScreen$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$XPEvent" {
import {$PlayerXpEvent, $PlayerXpEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerXpEvent"

export class $XPEvent {

constructor()

public static "xpQuest"(arg0: $PlayerXpEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XPEvent$Type = ($XPEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XPEvent_ = $XPEvent$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$LoadQuest" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $LoadQuest {

constructor()

public static "registerQuest"(arg0: $MinecraftServer$Type, arg1: string): void
public static "registerDatapackQuest"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadQuest$Type = ($LoadQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadQuest_ = $LoadQuest$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/quest/$ToastPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ToastPacket {

constructor(arg0: string)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $ToastPacket$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToastPacket$Type = ($ToastPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToastPacket_ = $ToastPacket$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questrequirement/$NoneRequirement" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractQuestRequirement, $AbstractQuestRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement"

export class $NoneRequirement extends $AbstractQuestRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $ServerQuest$Type, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoneRequirement$Type = ($NoneRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoneRequirement_ = $NoneRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NPCDialog$QuestDialog, $NPCDialog$QuestDialog$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$QuestDialog"

export class $NPCDialog extends $FileAbstract {

constructor()

public "getIdentifier"(): string
public "setIdentifier"(arg0: string): void
public "getGlobal_id"(): string
public "setGlobal_id"(arg0: string): void
public "getDialog"(): $List<($NPCDialog$QuestDialog)>
public static "createDialog"(arg0: $Path$Type): $NPCDialog
get "identifier"(): string
set "identifier"(value: string)
get "global_id"(): string
set "global_id"(value: string)
get "dialog"(): $List<($NPCDialog$QuestDialog)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NPCDialog$Type = ($NPCDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NPCDialog_ = $NPCDialog$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$EntityUUIDTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $EntityUUIDTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityUUIDTargetType$Type = ($EntityUUIDTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityUUIDTargetType_ = $EntityUUIDTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/screen/$ModifyTrackedQuests" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ModifyTrackedQuests {

constructor(arg0: boolean, arg1: $UserQuest$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $ModifyTrackedQuests$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyTrackedQuests$Type = ($ModifyTrackedQuests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyTrackedQuests_ = $ModifyTrackedQuests$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogoption/$DialogCloseDialog" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DialogCloseDialog extends $AbstractDialogOption {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogCloseDialog$Type = ($DialogCloseDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogCloseDialog_ = $DialogCloseDialog$Type;
}}
declare module "packages/net/zanckor/questapi/api/screen/$NpcType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NpcType extends $Enum<($NpcType)> {
static readonly "UUID": $NpcType
static readonly "RESOURCE_LOCATION": $NpcType
static readonly "ITEM": $NpcType


public static "values"(): ($NpcType)[]
public static "valueOf"(arg0: string): $NpcType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NpcType$Type = (("item") | ("uuid") | ("resource_location")) | ($NpcType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NpcType_ = $NpcType$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$LoadTagDialogList" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $LoadTagDialogList {

constructor()

public static "registerNPCTagDialogList"(arg0: $MinecraftServer$Type, arg1: string): void
public static "registerDatapackTagDialogList"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadTagDialogList$Type = ($LoadTagDialogList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadTagDialogList_ = $LoadTagDialogList$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/util/$MCUtil" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MCUtil {

constructor()

public static "hasQuest"(arg0: string, arg1: $Path$Type): boolean
public static "getEntityLookinAt"(arg0: $Entity$Type, arg1: double): $Entity
public static "randomBetween"(arg0: double, arg1: double): integer
public static "moveFileToCompletedFolder"(arg0: $UserQuest$Type, arg1: $ServerPlayer$Type, arg2: $File$Type): void
public static "getEntityByUUID"(arg0: $ServerLevel$Type, arg1: $UUID$Type): $Entity
public static "writeDialogRead"(arg0: $Player$Type, arg1: string, arg2: integer): void
public static "addQuest"(arg0: $Player$Type, arg1: string): void
public static "findSlotMatchingItemStack"(arg0: $ItemStack$Type, arg1: $Inventory$Type): $List<(integer)>
public static "isReadDialog"(arg0: $Player$Type, arg1: string, arg2: integer): boolean
public static "moveFileToUncompletedFolder"(arg0: $Path$Type, arg1: $File$Type, arg2: $UserQuest$Type, arg3: $Enum$Type<(any)>): void
public static "getHitResult"(arg0: $Level$Type, arg1: $Player$Type, arg2: float): $BlockHitResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MCUtil$Type = ($MCUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MCUtil_ = $MCUtil$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/screen/$RequestActiveQuests" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $RequestActiveQuests {

constructor()
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $RequestActiveQuests$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequestActiveQuests$Type = ($RequestActiveQuests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequestActiveQuests_ = $RequestActiveQuests$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/$SendQuestPacket" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $SendQuestPacket {

constructor()

public static "ALL"(arg0: any): void
public static "DIMENSION"(arg0: $Player$Type, arg1: any): void
public static "TO_SERVER"(arg0: any): void
public static "NEAR"(arg0: $Player$Type, arg1: any, arg2: double): void
public static "TO_CLIENT"(arg0: $Player$Type, arg1: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SendQuestPacket$Type = ($SendQuestPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SendQuestPacket_ = $SendQuestPacket$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/register/$QuestTemplateRegistry" {
import {$IEnumDialogReq, $IEnumDialogReq$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogReq"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$AbstractQuestRequirement, $AbstractQuestRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement"
import {$IEnumDialogOption, $IEnumDialogOption$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogOption"
import {$IEnumTargetType, $IEnumTargetType$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumTargetType"
import {$IEnumQuestRequirement, $IEnumQuestRequirement$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestRequirement"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"
import {$IEnumQuestGoal, $IEnumQuestGoal$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestGoal"
import {$IEnumQuestReward, $IEnumQuestReward$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestReward"
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$AbstractGoal, $AbstractGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractGoal"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"

export class $QuestTemplateRegistry {

constructor()

public static "registerQuest"(arg0: $IEnumQuestGoal$Type): void
public static "registerReward"(arg0: $IEnumQuestReward$Type): void
public static "getDialogTemplate"(arg0: $Enum$Type<(any)>): $AbstractDialogOption
public static "getQuestTemplate"(arg0: $Enum$Type<(any)>): $AbstractGoal
public static "registerQuestRequirement"(arg0: $IEnumQuestRequirement$Type): void
public static "registerDialogOption"(arg0: $IEnumDialogOption$Type): void
public static "registerDialogRequirement"(arg0: $IEnumDialogReq$Type): void
public static "getQuestRequirement"(arg0: $Enum$Type<(any)>): $AbstractQuestRequirement
public static "getDialogRequirement"(arg0: $Enum$Type<(any)>): $AbstractDialogRequirement
public static "getQuestReward"(arg0: $Enum$Type<(any)>): $AbstractReward
public static "getAllGoals"(): $HashMap<($Enum<(any)>), ($AbstractGoal)>
public static "registerTargetType"(arg0: $IEnumTargetType$Type): void
public static "getTranslatableTargetType"(arg0: $Enum$Type<(any)>): $AbstractTargetType
get "allGoals"(): $HashMap<($Enum<(any)>), ($AbstractGoal)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestTemplateRegistry$Type = ($QuestTemplateRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestTemplateRegistry_ = $QuestTemplateRegistry$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$EntityTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $EntityTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTargetType$Type = ($EntityTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTargetType_ = $EntityTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumquest/$EnumQuestRequirement" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractQuestRequirement, $AbstractQuestRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement"
import {$IEnumQuestRequirement, $IEnumQuestRequirement$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestRequirement"

export class $EnumQuestRequirement extends $Enum<($EnumQuestRequirement)> implements $IEnumQuestRequirement {
static readonly "XP": $EnumQuestRequirement
static readonly "NONE": $EnumQuestRequirement


public static "values"(): ($EnumQuestRequirement)[]
public static "valueOf"(arg0: string): $EnumQuestRequirement
public "getRequirement"(): $AbstractQuestRequirement
public "registerEnumQuestReq"(arg0: $Class$Type<(any)>): void
get "requirement"(): $AbstractQuestRequirement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumQuestRequirement$Type = (("xp") | ("none")) | ($EnumQuestRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumQuestRequirement_ = $EnumQuestRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$DefaultTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $DefaultTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultTargetType$Type = ($DefaultTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultTargetType_ = $DefaultTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/util/$Util" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Util {
static "lastDialogPerConversation": $ConcurrentHashMap<(string), (integer)>

constructor()

public static "hasQuest"(arg0: string, arg1: $Path$Type): boolean
public static "moveFileToCompletedFolder"(arg0: $UserQuest$Type, arg1: $ServerPlayer$Type, arg2: $File$Type): void
public static "getEntityByUUID"(arg0: $ServerLevel$Type, arg1: $UUID$Type): $Entity
public static "findSlotMatchingItemStack"(arg0: $ItemStack$Type, arg1: $Inventory$Type): $List<(integer)>
public static "getFreeSlots"(arg0: $Player$Type): integer
public static "isQuestCompleted"(arg0: $UserQuest$Type): boolean
public static "createQuest"(arg0: $ServerQuest$Type, arg1: $Player$Type, arg2: $Path$Type): void
public static "isConversationCompleted"(arg0: string, arg1: $Player$Type): boolean
public static "moveFileToUncompletedFolder"(arg0: $Path$Type, arg1: $File$Type, arg2: $UserQuest$Type, arg3: $Enum$Type<(any)>): void
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
declare module "packages/net/zanckor/questapi/api/screen/$AbstractQuestTracked" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AbstractQuestTracked {

constructor()

public "renderQuestTracked"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractQuestTracked$Type = ($AbstractQuestTracked);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractQuestTracked_ = $AbstractQuestTracked$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$ReadConversation" {
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"

export class $ReadConversation extends $FileAbstract {

constructor()

public "getConversation"(arg0: string): $List<(integer)>
public "addConversation"(arg0: string, arg1: integer): void
public "setConversations"(arg0: $ConcurrentHashMap$Type<(string), ($List$Type<(integer)>)>): void
set "conversations"(value: $ConcurrentHashMap$Type<(string), ($List$Type<(integer)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadConversation$Type = ($ReadConversation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadConversation_ = $ReadConversation$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/datapack/$CompoundTagDialogJSONListener" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $CompoundTagDialogJSONListener extends $SimpleJsonResourceReloadListener {
static "datapackDialogPerCompoundTagList": $HashMap<(string), ($JsonObject)>

constructor(arg0: $Gson$Type, arg1: string)

public static "register"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompoundTagDialogJSONListener$Type = ($CompoundTagDialogJSONListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompoundTagDialogJSONListener_ = $CompoundTagDialogJSONListener$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$QuestRegistry" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $QuestRegistry {

constructor()

public static "registerQuest"(arg0: $MinecraftServer$Type, arg1: string): void
public static "registerDatapackQuest"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestRegistry$Type = ($QuestRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestRegistry_ = $QuestRegistry$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$InteractSpecificEntityGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $InteractSpecificEntityGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractSpecificEntityGoal$Type = ($InteractSpecificEntityGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractSpecificEntityGoal_ = $InteractSpecificEntityGoal$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumquest/$EnumGoalType" {
import {$IEnumQuestGoal, $IEnumQuestGoal$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestGoal"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractGoal, $AbstractGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractGoal"

export class $EnumGoalType extends $Enum<($EnumGoalType)> implements $IEnumQuestGoal {
static readonly "INTERACT_ENTITY": $EnumGoalType
static readonly "INTERACT_SPECIFIC_ENTITY": $EnumGoalType
static readonly "KILL": $EnumGoalType
static readonly "MOVE_TO": $EnumGoalType
static readonly "COLLECT": $EnumGoalType
static readonly "COLLECT_WITH_NBT": $EnumGoalType
static readonly "XP": $EnumGoalType


public static "values"(): ($EnumGoalType)[]
public static "valueOf"(arg0: string): $EnumGoalType
public "getQuest"(): $AbstractGoal
public "registerEnumGoal"(arg0: $Class$Type<(any)>): void
get "quest"(): $AbstractGoal
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumGoalType$Type = (("interact_entity") | ("move_to") | ("collect_with_nbt") | ("xp") | ("interact_specific_entity") | ("kill") | ("collect")) | ($EnumGoalType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumGoalType_ = $EnumGoalType$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$MoveToGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MoveToGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoveToGoal$Type = ($MoveToGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoveToGoal_ = $MoveToGoal$Type;
}}
declare module "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogOption" {
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $IEnumDialogOption {

 "registerEnumDialogOption"(arg0: $Class$Type<(any)>): void
 "getDialogOption"(): $AbstractDialogOption

(arg0: $Class$Type<(any)>): void
}

export namespace $IEnumDialogOption {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumDialogOption$Type = ($IEnumDialogOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumDialogOption_ = $IEnumDialogOption$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTargetType$Type = ($AbstractTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTargetType_ = $AbstractTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questreward/$XpReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $XpReward extends $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XpReward$Type = ($XpReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XpReward_ = $XpReward$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$MoveToEvent" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $MoveToEvent {

constructor()

public static "moveTo"(arg0: $UserQuest$Type, arg1: $ServerPlayer$Type): void
public static "moveToQuest"(arg0: $TickEvent$PlayerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoveToEvent$Type = ($MoveToEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoveToEvent_ = $MoveToEvent$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerReward" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $ServerReward {

constructor()

public "getType"(): string
public "getTag"(): string
public "setType"(arg0: string): void
public "setAmount"(arg0: integer): void
public "getAmount"(): integer
public "getAdditionalListData"(): $List<(any)>
public "getAdditionalStringData"(): string
public "setAdditionalClassData"(arg0: any): void
public "getAdditionalDoubleData"(): double
public "setAdditionalIntegerData"(arg0: integer): void
public "getAdditionalIntegerData"(): integer
public "setAdditionalStringData"(arg0: string): void
public "setAdditionalListData"(arg0: $List$Type<(any)>): void
public "setAdditionalDoubleData"(arg0: double): void
public "getAdditionalClassData"(): any
public "setTag"(arg0: string): void
get "type"(): string
get "tag"(): string
set "type"(value: string)
set "amount"(value: integer)
get "amount"(): integer
get "additionalListData"(): $List<(any)>
get "additionalStringData"(): string
set "additionalClassData"(value: any)
get "additionalDoubleData"(): double
set "additionalIntegerData"(value: integer)
get "additionalIntegerData"(): integer
set "additionalStringData"(value: string)
set "additionalListData"(value: $List$Type<(any)>)
set "additionalDoubleData"(value: double)
get "additionalClassData"(): any
set "tag"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerReward$Type = ($ServerReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerReward_ = $ServerReward$Type;
}}
declare module "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumTargetType" {
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $IEnumTargetType {

 "getTargetType"(): $AbstractTargetType
 "registerTargetType"(arg0: $Class$Type<(any)>): void

(): $AbstractTargetType
}

export namespace $IEnumTargetType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumTargetType$Type = ($IEnumTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumTargetType_ = $IEnumTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $AbstractDialogOption {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDialogOption$Type = ($AbstractDialogOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDialogOption_ = $AbstractDialogOption$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/quest/$QuestDataPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $QuestDataPacket {

constructor(arg0: $Enum$Type<(any)>, arg1: $Entity$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $QuestDataPacket$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestDataPacket$Type = ($QuestDataPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestDataPacket_ = $QuestDataPacket$Type;
}}
declare module "packages/net/zanckor/questapi/example/client/screen/questlog/$QuestLog" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractQuestLog, $AbstractQuestLog$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractQuestLog"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"

export class $QuestLog extends $AbstractQuestLog {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Component$Type)

public "renderTitle"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: $Minecraft$Type): void
public "onClose"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "renderQuestData"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type): void
public "renderGoals"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: $Minecraft$Type, arg3: $HashMap$Type<(string), ($List$Type<($UserGoal$Type)>)>): void
public "isPauseScreen"(): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "modifyScreen"(): $Screen
public "renderDescription"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type): void
get "pauseScreen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestLog$Type = ($QuestLog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestLog_ = $QuestLog$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/$FileAbstract" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FileAbstract {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileAbstract$Type = ($FileAbstract);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileAbstract_ = $FileAbstract$Type;
}}
declare module "packages/net/zanckor/questapi/$ClientForgeQuestAPI" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$RegisterGuiOverlaysEvent, $RegisterGuiOverlaysEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterGuiOverlaysEvent"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"

export class $ClientForgeQuestAPI {
static "questMenu": $KeyMapping

constructor()

public static "registerKey"(arg0: string, arg1: integer): $KeyMapping
public static "keyInit"(arg0: $RegisterKeyMappingsEvent$Type): void
public static "registerOverlays"(arg0: $RegisterGuiOverlaysEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientForgeQuestAPI$Type = ($ClientForgeQuestAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientForgeQuestAPI_ = $ClientForgeQuestAPI$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$InteractWEntityEvent" {
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"

export class $InteractWEntityEvent {

constructor()

public static "interactWithNPC"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractWEntityEvent$Type = ($InteractWEntityEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractWEntityEvent_ = $InteractWEntityEvent$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$ItemTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $ItemTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTargetType$Type = ($ItemTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTargetType_ = $ItemTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/screen/$OpenVanillaEntityScreen" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $OpenVanillaEntityScreen {

constructor(arg0: $UUID$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $OpenVanillaEntityScreen$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenVanillaEntityScreen$Type = ($OpenVanillaEntityScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenVanillaEntityScreen_ = $OpenVanillaEntityScreen$Type;
}}
declare module "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestGoal" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractGoal, $AbstractGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractGoal"

export interface $IEnumQuestGoal {

 "getQuest"(): $AbstractGoal
 "registerEnumGoal"(arg0: $Class$Type<(any)>): void

(): $AbstractGoal
}

export namespace $IEnumQuestGoal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumQuestGoal$Type = ($IEnumQuestGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumQuestGoal_ = $IEnumQuestGoal$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/config/client/$RendererConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $RendererConfig {
static readonly "BUILDER": $ForgeConfigSpec$Builder
static "SPEC": $ForgeConfigSpec
static "QUEST_MARK_UPDATE_COOLDOWN": $ForgeConfigSpec$ConfigValue<(integer)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RendererConfig$Type = ($RendererConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RendererConfig_ = $RendererConfig$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/util/$MCUtilClient" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MCUtilClient {

constructor()

public static "renderItem"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $PoseStack$Type): void
public static "createButton"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Component$Type, arg5: $Button$OnPress$Type): $Button
public static "renderText"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: double, arg3: double, arg4: float, arg5: float, arg6: integer, arg7: $List$Type<(string)>, arg8: $Font$Type): void
public static "renderText"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: double, arg3: double, arg4: float, arg5: float, arg6: integer, arg7: string, arg8: $Font$Type): void
public static "formatString"(arg0: string, arg1: string, arg2: $ChatFormatting$Type, arg3: $ChatFormatting$Type): $MutableComponent
public static "properNoun"(arg0: string): string
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: float, arg3: float, arg4: float, arg5: string, arg6: $Font$Type): void
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: float, arg3: float, arg4: float, arg5: $MutableComponent$Type, arg6: $MutableComponent$Type, arg7: $Font$Type): void
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: integer, arg3: float, arg4: float, arg5: float, arg6: string, arg7: $Font$Type): void
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: float, arg3: float, arg4: float, arg5: $MutableComponent$Type, arg6: $Font$Type): void
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: integer, arg3: float, arg4: float, arg5: float, arg6: $MutableComponent$Type, arg7: $Font$Type): void
public static "renderLine"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: integer, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $MutableComponent$Type, arg8: $Font$Type): void
public static "renderEntity"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $LivingEntity$Type): void
public static "renderEntity"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $LivingEntity$Type, arg5: $PoseStack$Type): void
public static "renderLines"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: float, arg3: integer, arg4: integer, arg5: string, arg6: $Font$Type): void
public static "renderLines"(arg0: $GuiGraphics$Type, arg1: $PoseStack$Type, arg2: float, arg3: integer, arg4: integer, arg5: $Component$Type, arg6: $Font$Type): void
public static "splitText"(arg0: string, arg1: $Font$Type, arg2: integer): $List<($List<($FormattedCharSequence)>)>
public static "getEntityByUUID"(arg0: $UUID$Type): $Entity
public static "playSound"(arg0: $SoundEvent$Type, arg1: float, arg2: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MCUtilClient$Type = ($MCUtilClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MCUtilClient_ = $MCUtilClient$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questreward/$QuestReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $QuestReward extends $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestReward$Type = ($QuestReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestReward_ = $QuestReward$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/screen/$UpdateQuestTracked" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $UpdateQuestTracked {

constructor(arg0: $UserQuest$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $UpdateQuestTracked$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateQuestTracked$Type = ($UpdateQuestTracked);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateQuestTracked_ = $UpdateQuestTracked$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumquest/$EnumQuestReward" {
import {$IEnumQuestReward, $IEnumQuestReward$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestReward"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $EnumQuestReward extends $Enum<($EnumQuestReward)> implements $IEnumQuestReward {
static readonly "ITEM": $EnumQuestReward
static readonly "COMMAND": $EnumQuestReward
static readonly "QUEST": $EnumQuestReward
static readonly "XP": $EnumQuestReward
static readonly "LEVEL": $EnumQuestReward
static readonly "POINTS": $EnumQuestReward
static readonly "LOOT_TABLE": $EnumQuestReward


public static "values"(): ($EnumQuestReward)[]
public static "valueOf"(arg0: string): $EnumQuestReward
public "getReward"(): $AbstractReward
public "registerEnumReward"(arg0: $Class$Type<(any)>): void
get "reward"(): $AbstractReward
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumQuestReward$Type = (("item") | ("level") | ("xp") | ("quest") | ("command") | ("loot_table") | ("points")) | ($EnumQuestReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumQuestReward_ = $EnumQuestReward$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"

export class $UserQuest extends $FileAbstract {

constructor()

public "getId"(): string
public "setCompleted"(arg0: boolean): void
public "isCompleted"(): boolean
public "getDescription"(): string
public "setDescription"(arg0: string): void
public "setTitle"(arg0: string): void
public "getTitle"(): string
public "setId"(arg0: string): void
public "getTimeLimitInSeconds"(): integer
public "setTimeLimitInSeconds"(arg0: integer): void
public "getQuestGoals"(): $List<($UserGoal)>
public "setTimeLimit"(arg0: boolean): void
public static "createQuest"(arg0: $ServerQuest$Type, arg1: $Path$Type): $UserQuest
public "setQuestGoals"(arg0: $List$Type<($UserGoal$Type)>): void
public "hasTimeLimit"(): boolean
get "id"(): string
set "completed"(value: boolean)
get "completed"(): boolean
get "description"(): string
set "description"(value: string)
set "title"(value: string)
get "title"(): string
set "id"(value: string)
get "timeLimitInSeconds"(): integer
set "timeLimitInSeconds"(value: integer)
get "questGoals"(): $List<($UserGoal)>
set "timeLimit"(value: boolean)
set "questGoals"(value: $List$Type<($UserGoal$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserQuest$Type = ($UserQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserQuest_ = $UserQuest$Type;
}}
declare module "packages/net/zanckor/questapi/api/enuminterface/enumquest/$IEnumQuestRequirement" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractQuestRequirement, $AbstractQuestRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement"

export interface $IEnumQuestRequirement {

 "getRequirement"(): $AbstractQuestRequirement
 "registerEnumQuestReq"(arg0: $Class$Type<(any)>): void

(): $AbstractQuestRequirement
}

export namespace $IEnumQuestRequirement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumQuestRequirement$Type = ($IEnumQuestRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumQuestRequirement_ = $IEnumQuestRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/dialogoption/$DisplayDialog" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DisplayDialog {

constructor(arg0: $Conversation$Type, arg1: string, arg2: integer, arg3: $Player$Type, arg4: $Item$Type)
constructor(arg0: $Conversation$Type, arg1: string, arg2: integer, arg3: $Player$Type, arg4: string)
constructor(arg0: $Conversation$Type, arg1: string, arg2: integer, arg3: $Player$Type, arg4: $Entity$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $DisplayDialog$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayDialog$Type = ($DisplayDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayDialog_ = $DisplayDialog$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumdialog/$EnumDialogReqStatus" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EnumDialogReqStatus extends $Enum<($EnumDialogReqStatus)> {
static readonly "NOT_OBTAINED": $EnumDialogReqStatus
static readonly "IN_PROGRESS": $EnumDialogReqStatus
static readonly "COMPLETED": $EnumDialogReqStatus
static readonly "READ": $EnumDialogReqStatus
static readonly "NOT_READ": $EnumDialogReqStatus


public static "values"(): ($EnumDialogReqStatus)[]
public static "valueOf"(arg0: string): $EnumDialogReqStatus
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumDialogReqStatus$Type = (("in_progress") | ("read") | ("not_read") | ("not_obtained") | ("completed")) | ($EnumDialogReqStatus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumDialogReqStatus_ = $EnumDialogReqStatus$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerRequirement" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $ServerRequirement {

constructor()

public "getType"(): string
public "setType"(arg0: string): void
public "getRequirements_max"(): integer
public "getRequirements_min"(): integer
public "getAdditionalListData"(): $List<(any)>
public "setRequirements_min"(arg0: integer): void
public "getAdditionalStringData"(): string
public "setAdditionalClassData"(arg0: any): void
public "getAdditionalDoubleData"(): double
public "setAdditionalIntegerData"(arg0: integer): void
public "getAdditionalIntegerData"(): integer
public "setAdditionalStringData"(arg0: string): void
public "setAdditionalListData"(arg0: $List$Type<(any)>): void
public "setAdditionalDoubleData"(arg0: double): void
public "setRequirements_max"(arg0: integer): void
public "getAdditionalClassData"(): any
get "type"(): string
set "type"(value: string)
get "requirements_max"(): integer
get "requirements_min"(): integer
get "additionalListData"(): $List<(any)>
set "requirements_min"(value: integer)
get "additionalStringData"(): string
set "additionalClassData"(value: any)
get "additionalDoubleData"(): double
set "additionalIntegerData"(value: integer)
get "additionalIntegerData"(): integer
set "additionalStringData"(value: string)
set "additionalListData"(value: $List$Type<(any)>)
set "additionalDoubleData"(value: double)
set "requirements_max"(value: integer)
get "additionalClassData"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerRequirement$Type = ($ServerRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerRequirement_ = $ServerRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/quest/$ActiveQuestList" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ActiveQuestList {

constructor(arg0: $UUID$Type)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $ActiveQuestList$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActiveQuestList$Type = ($ActiveQuestList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActiveQuestList_ = $ActiveQuestList$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/datapack/$QuestJSONListener" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $QuestJSONListener extends $SimpleJsonResourceReloadListener {
static "datapackQuestList": $HashMap<(string), ($JsonObject)>

constructor(arg0: $Gson$Type, arg1: string)

public static "register"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestJSONListener$Type = ($QuestJSONListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestJSONListener_ = $QuestJSONListener$Type;
}}
declare module "packages/net/zanckor/questapi/mod/server/event/$ServerEvent" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"
import {$File, $File$Type} from "packages/java/io/$File"
import {$PlayerEvent$PlayerLoggedOutEvent, $PlayerEvent$PlayerLoggedOutEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedOutEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$PlayerInteractEvent, $PlayerInteractEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent"

export class $ServerEvent {

constructor()

public static "timer"(arg0: $UserQuest$Type, arg1: $Player$Type, arg2: $File$Type, arg3: $Path$Type): void
public static "questWithTimer"(arg0: $TickEvent$PlayerTickEvent$Type): void
public static "openVanillaMenu"(arg0: $Player$Type): boolean
public static "loadHashMaps"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "loadDialogPerCompoundTag"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
public static "loadDialogOrAddQuestViaItem"(arg0: $PlayerInteractEvent$Type): void
public static "loadDialogPerEntityType"(arg0: $PlayerInteractEvent$EntityInteract$Type): void
public static "uncompletedQuestOnLogOut"(arg0: $PlayerEvent$PlayerLoggedOutEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEvent$Type = ($ServerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEvent_ = $ServerEvent$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog" {
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityTypeTagDialog$EntityTypeTagDialogCondition, $EntityTypeTagDialog$EntityTypeTagDialogCondition$Type} from "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog$EntityTypeTagDialogCondition"

export class $EntityTypeTagDialog extends $FileAbstract {

constructor()

public "getConditions"(): $List<($EntityTypeTagDialog$EntityTypeTagDialogCondition)>
public "getEntity_type"(): $List<(string)>
get "conditions"(): $List<($EntityTypeTagDialog$EntityTypeTagDialogCondition)>
get "entity_type"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeTagDialog$Type = ($EntityTypeTagDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeTagDialog_ = $EntityTypeTagDialog$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/$FolderManager" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"

export class $FolderManager {

constructor()

public static "createAPIFolder"(arg0: $Path$Type): void
public static "playerFolderManager"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "serverFolderManager"(arg0: $ServerAboutToStartEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FolderManager$Type = ($FolderManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FolderManager_ = $FolderManager$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/npcmarker/$ValidNPCMarker" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ValidNPCMarker {

constructor()
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $ValidNPCMarker$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValidNPCMarker$Type = ($ValidNPCMarker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValidNPCMarker_ = $ValidNPCMarker$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questreward/$ItemReward" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractReward, $AbstractReward$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractReward"

export class $ItemReward extends $AbstractReward {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $ServerQuest$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemReward$Type = ($ItemReward);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemReward_ = $ItemReward$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/dialogoption/$AddQuest" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$EnumDialogOption, $EnumDialogOption$Type} from "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumdialog/$EnumDialogOption"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AddQuest {

constructor(arg0: $EnumDialogOption$Type, arg1: integer)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $AddQuest$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddQuest$Type = ($AddQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddQuest_ = $AddQuest$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$QuestDialog" {
import {$NPCDialog$DialogOption, $NPCDialog$DialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$DialogOption"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NPCDialog$DialogRequirement, $NPCDialog$DialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$DialogRequirement"

export class $NPCDialog$QuestDialog {

constructor()

public "getId"(): integer
public "getOptions"(): $List<($NPCDialog$DialogOption)>
public "getServerRequirements"(): $NPCDialog$DialogRequirement
public "getDialogText"(): string
get "id"(): integer
get "options"(): $List<($NPCDialog$DialogOption)>
get "serverRequirements"(): $NPCDialog$DialogRequirement
get "dialogText"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NPCDialog$QuestDialog$Type = ($NPCDialog$QuestDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NPCDialog$QuestDialog_ = $NPCDialog$QuestDialog$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/dialogoption/$DialogRequestPacket" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$EnumDialogOption, $EnumDialogOption$Type} from "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumdialog/$EnumDialogOption"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"

export class $DialogRequestPacket {

constructor(arg0: $FriendlyByteBuf$Type)
constructor(arg0: $EnumDialogOption$Type, arg1: integer, arg2: $Entity$Type, arg3: $Item$Type, arg4: $NpcType$Type)

public static "handler"(arg0: $DialogRequestPacket$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
public "decodeNpcType"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogRequestPacket$Type = ($DialogRequestPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogRequestPacket_ = $DialogRequestPacket$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/handler/$ClientHandler" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"

export class $ClientHandler {
static "trackedQuestList": $List<($UserQuest)>
static "activeQuestList": $List<($UserQuest)>
static "availableEntityTypeForQuest": $List<($EntityType)>
static "availableEntityTagForQuest": $Map<(string), (string)>

constructor()

public static "modifyTrackedQuests"(arg0: boolean, arg1: $UserQuest$Type): void
public static "closeDialog"(): void
public static "removeQuest"(arg0: string): void
public static "setActiveQuestList"(arg0: $List$Type<($UserQuest$Type)>): void
public static "updateQuestTracked"(arg0: $UserQuest$Type): void
public static "displayDialog"(arg0: string, arg1: integer, arg2: string, arg3: integer, arg4: $HashMap$Type<(integer), ($List$Type<(integer)>)>, arg5: $HashMap$Type<(integer), ($List$Type<(string)>)>, arg6: $UUID$Type, arg7: string, arg8: $Item$Type, arg9: $NpcType$Type): void
public static "setAvailableEntityTypeForQuest"(arg0: $List$Type<(string)>, arg1: $Map$Type<(string), (string)>): void
public static "toastQuestCompleted"(arg0: string): void
set "activeQuestList"(value: $List$Type<($UserQuest$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientHandler$Type = ($ClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientHandler_ = $ClientHandler$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/quest/$TimerPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $TimerPacket {

constructor()
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $TimerPacket$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimerPacket$Type = ($TimerPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimerPacket_ = $TimerPacket$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogrequirement/$DialogRequirement" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DialogRequirement extends $AbstractDialogRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): boolean
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogRequirement$Type = ($DialogRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogRequirement_ = $DialogRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/api/registry/$ScreenRegistry" {
import {$AbstractQuestLog, $AbstractQuestLog$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractQuestLog"
import {$AbstractDialog, $AbstractDialog$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractDialog"
import {$AbstractQuestTracked, $AbstractQuestTracked$Type} from "packages/net/zanckor/questapi/api/screen/$AbstractQuestTracked"

export class $ScreenRegistry {

constructor()

public static "getQuestTrackedScreen"(arg0: string): $AbstractQuestTracked
public static "getDialogScreen"(arg0: string): $AbstractDialog
public static "getQuestLogScreen"(arg0: string): $AbstractQuestLog
public static "registerQuestLogScreen"(arg0: string, arg1: $AbstractQuestLog$Type): void
public static "registerDialogScreen"(arg0: string, arg1: $AbstractDialog$Type): void
public static "registerQuestTrackedScreen"(arg0: string, arg1: $AbstractQuestTracked$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenRegistry$Type = ($ScreenRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenRegistry_ = $ScreenRegistry$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NPCDialog$QuestDialog, $NPCDialog$QuestDialog$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$QuestDialog"

export class $Conversation extends $FileAbstract {

constructor()

public "getIdentifier"(): string
public "setConversationID"(arg0: string): void
public "setIdentifier"(arg0: string): void
public "getConversationID"(): string
public "getDialog"(): $List<($NPCDialog$QuestDialog)>
public static "createDialog"(arg0: $Path$Type, arg1: string): $Conversation
get "identifier"(): string
set "conversationID"(value: string)
set "identifier"(value: string)
get "conversationID"(): string
get "dialog"(): $List<($NPCDialog$QuestDialog)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Conversation$Type = ($Conversation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Conversation_ = $Conversation$Type;
}}
declare module "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogReq" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractDialogRequirement, $AbstractDialogRequirement$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogRequirement"

export interface $IEnumDialogReq {

 "registerEnumDialogReq"(arg0: $Class$Type<(any)>): void
 "getDialogRequirement"(): $AbstractDialogRequirement

(arg0: $Class$Type<(any)>): void
}

export namespace $IEnumDialogReq {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnumDialogReq$Type = ($IEnumDialogReq);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnumDialogReq_ = $IEnumDialogReq$Type;
}}
declare module "packages/net/zanckor/questapi/mod/$EventHandlerRegister" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $EventHandlerRegister {

constructor()

public static "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
public static "jsonListener"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerRegister$Type = ($EventHandlerRegister);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerRegister_ = $EventHandlerRegister$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$DialogRequirement" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NPCDialog$DialogRequirement {

constructor()

public "getType"(): string
public "getRequirement_status"(): string
public "getGlobal_id"(): string
public "getQuestId"(): string
public "getDialogId"(): integer
get "type"(): string
get "requirement_status"(): string
get "global_id"(): string
get "questId"(): string
get "dialogId"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NPCDialog$DialogRequirement$Type = ($NPCDialog$DialogRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NPCDialog$DialogRequirement_ = $NPCDialog$DialogRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/multiloader/platform/$ForgePlatformHelper" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/net/zanckor/questapi/multiloader/platform/services/$IPlatformHelper"
import {$PlatformEnum, $PlatformEnum$Type} from "packages/net/zanckor/questapi/multiloader/platform/services/$PlatformEnum"

export class $ForgePlatformHelper implements $IPlatformHelper {

constructor()

public "isDevelopmentEnvironment"(): boolean
public "getPlatform"(): $PlatformEnum
public "isModLoaded"(arg0: string): boolean
public "getEnvironmentName"(): string
get "developmentEnvironment"(): boolean
get "platform"(): $PlatformEnum
get "environmentName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePlatformHelper$Type = ($ForgePlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePlatformHelper_ = $ForgePlatformHelper$Type;
}}
declare module "packages/net/zanckor/questapi/util/$GsonManager" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$FileAbstract, $FileAbstract$Type} from "packages/net/zanckor/questapi/api/file/$FileAbstract"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $GsonManager {
static "gson": $Gson

constructor()

public static "getJsonClass"<T extends $FileAbstract>(arg0: $File$Type, arg1: $Class$Type<(T)>): $FileAbstract
public static "getJsonClass"<T extends $FileAbstract>(arg0: string, arg1: $Class$Type<(T)>): $FileAbstract
public static "writeJson"<T extends $FileAbstract>(arg0: $File$Type, arg1: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GsonManager$Type = ($GsonManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GsonManager_ = $GsonManager$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/dialogoption/$DialogAddQuest" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Conversation, $Conversation$Type} from "packages/net/zanckor/questapi/api/file/dialog/codec/$Conversation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DialogAddQuest extends $AbstractDialogOption {

constructor()

public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Item$Type): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: string): void
public "handler"(arg0: $Player$Type, arg1: $Conversation$Type, arg2: integer, arg3: $Entity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogAddQuest$Type = ($DialogAddQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogAddQuest_ = $DialogAddQuest$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/config/client/$ScreenConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $ScreenConfig {
static readonly "BUILDER": $ForgeConfigSpec$Builder
static "SPEC": $ForgeConfigSpec
static "QUEST_TRACKED_SCREEN": $ForgeConfigSpec$ConfigValue<(string)>
static "QUEST_LOG_SCREEN": $ForgeConfigSpec$ConfigValue<(string)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenConfig$Type = ($ScreenConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenConfig_ = $ScreenConfig$Type;
}}
declare module "packages/net/zanckor/questapi/api/screen/$AbstractDialog" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$NpcType, $NpcType$Type} from "packages/net/zanckor/questapi/api/screen/$NpcType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $AbstractDialog extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "modifyScreen"(arg0: integer, arg1: string, arg2: integer, arg3: $HashMap$Type<(integer), ($List$Type<(integer)>)>, arg4: $HashMap$Type<(integer), ($List$Type<(string)>)>, arg5: $UUID$Type, arg6: string, arg7: $Item$Type, arg8: $NpcType$Type): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDialog$Type = ($AbstractDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDialog_ = $AbstractDialog$Type;
}}
declare module "packages/net/zanckor/questapi/multiloader/platform/services/$PlatformEnum" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PlatformEnum extends $Enum<($PlatformEnum)> {
static readonly "FORGE": $PlatformEnum
static readonly "FABRIC": $PlatformEnum
static readonly "QUILT": $PlatformEnum


public static "values"(): ($PlatformEnum)[]
public static "valueOf"(arg0: string): $PlatformEnum
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformEnum$Type = (("quilt") | ("forge") | ("fabric")) | ($PlatformEnum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformEnum_ = $PlatformEnum$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/datapack/$EntityTypeDialogJSONListener" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $EntityTypeDialogJSONListener extends $SimpleJsonResourceReloadListener {
static "datapackDialogPerEntityTypeList": $HashMap<(string), ($JsonObject)>

constructor(arg0: $Gson$Type, arg1: string)

public static "register"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeDialogJSONListener$Type = ($EntityTypeDialogJSONListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeDialogJSONListener_ = $EntityTypeDialogJSONListener$Type;
}}
declare module "packages/net/zanckor/questapi/mod/server/command/$QuestCommand" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $QuestCommand {

constructor()

public static "removeQuest"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>, arg1: $UUID$Type, arg2: string): integer
public static "putQuestToItem"(arg0: $ItemStack$Type, arg1: string): integer
public static "putDialogToItem"(arg0: $ItemStack$Type, arg1: string): integer
public static "addQuest"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>, arg1: $UUID$Type, arg2: string): integer
public static "displayDialog"(arg0: $ServerPlayer$Type, arg1: string): integer
public static "createQuest"(arg0: $ServerQuest$Type, arg1: $Player$Type, arg2: $Path$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuestCommand$Type = ($QuestCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuestCommand_ = $QuestCommand$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/dialog/codec/$NPCDialog$DialogOption" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NPCDialog$DialogOption {

constructor()

public "getType"(): string
public "getText"(): string
public "getQuest_id"(): string
public "getGlobal_id"(): string
public "getDialog"(): integer
get "type"(): string
get "text"(): string
get "quest_id"(): string
get "global_id"(): string
get "dialog"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NPCDialog$DialogOption$Type = ($NPCDialog$DialogOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NPCDialog$DialogOption_ = $NPCDialog$DialogOption$Type;
}}
declare module "packages/net/zanckor/questapi/api/screen/$AbstractQuestLog" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AbstractQuestLog extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "modifyScreen"(): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractQuestLog$Type = ($AbstractQuestLog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractQuestLog_ = $AbstractQuestLog$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$XpGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $XpGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XpGoal$Type = ($XpGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XpGoal_ = $XpGoal$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/screen/$RemovedQuest" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $RemovedQuest {

constructor(arg0: string)
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $RemovedQuest$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemovedQuest$Type = ($RemovedQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemovedQuest_ = $RemovedQuest$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog$EntityTypeTagDialogCondition" {
import {$EntityTypeTagDialog, $EntityTypeTagDialog$Type} from "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog"
import {$LogicGate, $LogicGate$Type} from "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/gateenum/$LogicGate"
import {$EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT, $EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT$Type} from "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/codec/$EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT"
import {$List, $List$Type} from "packages/java/util/$List"

export class $EntityTypeTagDialog$EntityTypeTagDialogCondition {

constructor(arg0: $EntityTypeTagDialog$Type)

public "getDialog_list"(): $List<(string)>
public "getLogic_gate"(): $LogicGate
public "getEntity_type"(): $List<(string)>
public "getNbt"(): $List<($EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT)>
get "dialog_list"(): $List<(string)>
get "logic_gate"(): $LogicGate
get "entity_type"(): $List<(string)>
get "nbt"(): $List<($EntityTypeTagDialog$EntityTypeTagDialogCondition$EntityTypeTagDialogNBT)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeTagDialog$EntityTypeTagDialogCondition$Type = ($EntityTypeTagDialog$EntityTypeTagDialogCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeTagDialog$EntityTypeTagDialogCondition_ = $EntityTypeTagDialog$EntityTypeTagDialogCondition$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/$NetworkHandler" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $NetworkHandler {
static readonly "CHANNEL": $SimpleChannel

constructor()

public static "register"(): void
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
declare module "packages/net/zanckor/questapi/api/file/npc/entity_type_tag/gateenum/$LogicGate" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LogicGate extends $Enum<($LogicGate)> {
static readonly "OR": $LogicGate
static readonly "AND": $LogicGate


public static "values"(): ($LogicGate)[]
public static "valueOf"(arg0: string): $LogicGate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogicGate$Type = (("or") | ("and")) | ($LogicGate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogicGate_ = $LogicGate$Type;
}}
declare module "packages/net/zanckor/questapi/api/registry/$EnumRegistry" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $EnumRegistry {

constructor()

public static "registerQuestGoal"(arg0: $Class$Type<(any)>): void
public static "getEnum"(arg0: string, arg1: $List$Type<($Class$Type<(any)>)>): $Enum<(any)>
public static "registerQuestRequirement"(arg0: $Class$Type<(any)>): void
public static "registerDialogOption"(arg0: $Class$Type<(any)>): void
public static "registerDialogRequirement"(arg0: $Class$Type<(any)>): void
public static "getTargetType"(): $List<($Class<(any)>)>
public static "getQuestRequirement"(): $List<($Class<(any)>)>
public static "getDialogRequirement"(): $List<($Class<(any)>)>
public static "registerQuestReward"(arg0: $Class$Type<(any)>): void
public static "getQuestReward"(): $List<($Class<(any)>)>
public static "getQuestGoal"(): $List<($Class<(any)>)>
public static "registerTargetType"(arg0: $Class$Type<(any)>): void
public static "getDialogOption"(): $List<($Class<(any)>)>
get "targetType"(): $List<($Class<(any)>)>
get "questRequirement"(): $List<($Class<(any)>)>
get "dialogRequirement"(): $List<($Class<(any)>)>
get "questReward"(): $List<($Class<(any)>)>
get "questGoal"(): $List<($Class<(any)>)>
get "dialogOption"(): $List<($Class<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumRegistry$Type = ($EnumRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumRegistry_ = $EnumRegistry$Type;
}}
declare module "packages/net/zanckor/questapi/$CommonMain" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export class $CommonMain {
static "serverDirectory": $Path
static "questApi": $Path
static "playerData": $Path
static "serverQuests": $Path
static "serverDialogs": $Path
static "serverNPC": $Path
static "entity_type_list": $Path
static "compoundTag_List": $Path

constructor()

public static "init"(): void
public static "getFailedQuest"(arg0: $Path$Type): $Path
public static "getCompletedQuest"(arg0: $Path$Type): $Path
public static "getUserFolder"(arg0: $UUID$Type): $Path
public static "getActiveQuest"(arg0: $Path$Type): $Path
public static "getReadDialogs"(arg0: $Path$Type): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonMain$Type = ($CommonMain);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonMain_ = $CommonMain$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$AbstractGoal, $AbstractGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeAbstractGoal extends $AbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeAbstractGoal$Type = ($ForgeAbstractGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeAbstractGoal_ = $ForgeAbstractGoal$Type;
}}
declare module "packages/net/zanckor/questapi/example/server/eventhandler/questevent/$CollectEvent" {
import {$PlayerEvent$ItemCraftedEvent, $PlayerEvent$ItemCraftedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemCraftedEvent"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEvent$ItemSmeltedEvent, $PlayerEvent$ItemSmeltedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemSmeltedEvent"
import {$PlayerEvent$ItemPickupEvent, $PlayerEvent$ItemPickupEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemPickupEvent"

export class $CollectEvent {

constructor()

public static "runGoalHandler"(arg0: $ServerPlayer$Type): void
public static "CollectPickUpQuest"(arg0: $PlayerEvent$ItemPickupEvent$Type): void
public static "CollectCraftQuest"(arg0: $PlayerEvent$ItemSmeltedEvent$Type): void
public static "CollectCraftQuest"(arg0: $PlayerEvent$ItemCraftedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectEvent$Type = ($CollectEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectEvent_ = $CollectEvent$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/network/packet/dialogoption/$CloseDialog" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CloseDialog {

constructor()
constructor(arg0: $FriendlyByteBuf$Type)

public static "handler"(arg0: $CloseDialog$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "encodeBuffer"(arg0: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CloseDialog$Type = ($CloseDialog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CloseDialog_ = $CloseDialog$Type;
}}
declare module "packages/net/zanckor/questapi/mod/common/datapack/$DialogJSONListener" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $DialogJSONListener extends $SimpleJsonResourceReloadListener {
static "datapackDialogList": $HashMap<(string), ($JsonObject)>

constructor(arg0: $Gson$Type, arg1: string)

public static "register"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DialogJSONListener$Type = ($DialogJSONListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DialogJSONListener_ = $DialogJSONListener$Type;
}}
declare module "packages/net/zanckor/questapi/multiloader/platform/$Services" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/net/zanckor/questapi/multiloader/platform/services/$IPlatformHelper"
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
declare module "packages/net/zanckor/questapi/example/common/handler/questrequirement/$XpRequirement" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"
import {$AbstractQuestRequirement, $AbstractQuestRequirement$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement"

export class $XpRequirement extends $AbstractQuestRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $ServerQuest$Type, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XpRequirement$Type = ($XpRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XpRequirement_ = $XpRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/mod/core/filemanager/dialogquestregistry/enumdialog/$EnumDialogOption" {
import {$AbstractDialogOption, $AbstractDialogOption$Type} from "packages/net/zanckor/questapi/api/file/dialog/abstractdialog/$AbstractDialogOption"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IEnumDialogOption, $IEnumDialogOption$Type} from "packages/net/zanckor/questapi/api/enuminterface/enumdialog/$IEnumDialogOption"

export class $EnumDialogOption extends $Enum<($EnumDialogOption)> implements $IEnumDialogOption {
static readonly "OPEN_DIALOG": $EnumDialogOption
static readonly "CLOSE_DIALOG": $EnumDialogOption
static readonly "ADD_QUEST": $EnumDialogOption


public static "values"(): ($EnumDialogOption)[]
public static "valueOf"(arg0: string): $EnumDialogOption
public "getDialogOption"(): $AbstractDialogOption
public "registerEnumDialogOption"(arg0: $Class$Type<(any)>): void
get "dialogOption"(): $AbstractDialogOption
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumDialogOption$Type = (("add_quest") | ("open_dialog") | ("close_dialog")) | ($EnumDialogOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumDialogOption_ = $EnumDialogOption$Type;
}}
declare module "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractQuestRequirement" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerQuest, $ServerQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/server/$ServerQuest"

export class $AbstractQuestRequirement {

constructor()

public "handler"(arg0: $Player$Type, arg1: $ServerQuest$Type, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractQuestRequirement$Type = ($AbstractQuestRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractQuestRequirement_ = $AbstractQuestRequirement$Type;
}}
declare module "packages/net/zanckor/questapi/example/$ModExample" {
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"

export class $ModExample {

constructor()

public static "registerTemplates"(arg0: $ServerAboutToStartEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModExample$Type = ($ModExample);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModExample_ = $ModExample$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/targettype/$XPTargetType" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AbstractTargetType, $AbstractTargetType$Type} from "packages/net/zanckor/questapi/api/file/quest/abstracquest/$AbstractTargetType"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $XPTargetType extends $AbstractTargetType {

constructor()

public "target"(arg0: string): string
public "handler"(arg0: string, arg1: $UserGoal$Type, arg2: $Player$Type, arg3: $ChatFormatting$Type, arg4: $ChatFormatting$Type): $MutableComponent
public "renderTarget"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: double, arg4: double, arg5: $UserGoal$Type, arg6: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XPTargetType$Type = ($XPTargetType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XPTargetType_ = $XPTargetType$Type;
}}
declare module "packages/net/zanckor/questapi/example/common/handler/questgoal/$InteractEntityGoal" {
import {$UserQuest, $UserQuest$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserQuest"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ForgeAbstractGoal, $ForgeAbstractGoal$Type} from "packages/net/zanckor/questapi/mod/common/questhandler/$ForgeAbstractGoal"
import {$UserGoal, $UserGoal$Type} from "packages/net/zanckor/questapi/api/file/quest/codec/user/$UserGoal"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $InteractEntityGoal extends $ForgeAbstractGoal {

constructor()

public "handler"(arg0: $ServerPlayer$Type, arg1: $Entity$Type, arg2: $Gson$Type, arg3: $File$Type, arg4: $UserQuest$Type, arg5: integer, arg6: $Enum$Type<(any)>): void
public "enhancedCompleteQuest"(arg0: $ServerPlayer$Type, arg1: $File$Type, arg2: $UserGoal$Type): void
public "updateData"(arg0: $ServerPlayer$Type, arg1: $File$Type): void
public "getGoalType"(): $Enum<(any)>
get "goalType"(): $Enum<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractEntityGoal$Type = ($InteractEntityGoal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractEntityGoal_ = $InteractEntityGoal$Type;
}}
