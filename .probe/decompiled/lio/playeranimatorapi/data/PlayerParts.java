package lio.playeranimatorapi.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PlayerParts {

    public static Codec<PlayerParts> CODEC = Codec.list(PlayerPart.CODEC).comapFlatMap(PlayerParts::readFromList, PlayerParts::toList).stable();

    public static final PlayerParts allEnabled = new PlayerParts();

    public PlayerPart body = new PlayerPart();

    public PlayerPart head = new PlayerPart();

    public PlayerPart torso = new PlayerPart();

    public PlayerPart rightArm = new PlayerPart();

    public PlayerPart leftArm = new PlayerPart();

    public PlayerPart rightLeg = new PlayerPart();

    public PlayerPart leftLeg = new PlayerPart();

    public PlayerPart rightItem = new PlayerPart();

    public PlayerPart leftItem = new PlayerPart();

    public static DataResult<PlayerParts> readFromList(List<PlayerPart> list) {
        if (list.size() != 9) {
            return DataResult.success(allEnabled);
        } else {
            PlayerParts parts = new PlayerParts();
            parts.body = (PlayerPart) list.get(0);
            parts.head = (PlayerPart) list.get(1);
            parts.torso = (PlayerPart) list.get(2);
            parts.rightArm = (PlayerPart) list.get(3);
            parts.leftArm = (PlayerPart) list.get(4);
            parts.rightLeg = (PlayerPart) list.get(5);
            parts.leftLeg = (PlayerPart) list.get(6);
            parts.rightItem = (PlayerPart) list.get(7);
            parts.leftItem = (PlayerPart) list.get(8);
            return DataResult.success(parts);
        }
    }

    public static List<PlayerPart> toList(PlayerParts parts) {
        List<PlayerPart> list = new ArrayList();
        if (parts == null) {
            return list;
        } else {
            list.add(parts.body);
            list.add(parts.head);
            list.add(parts.torso);
            list.add(parts.rightArm);
            list.add(parts.leftArm);
            list.add(parts.rightLeg);
            list.add(parts.leftLeg);
            list.add(parts.rightItem);
            list.add(parts.leftItem);
            return list;
        }
    }

    public static PlayerParts fromBigInteger(BigInteger n) {
        PlayerParts parts = new PlayerParts();
        try {
            String val = new BigInteger(n.toString(36), 36).toString(2);
            List<Boolean> list = new ArrayList();
            for (int i = 0; i < val.length(); i++) {
                list.add(val.charAt(i) == '1');
            }
            try {
                parts.body.x = (Boolean) list.get(0);
                parts.body.y = (Boolean) list.get(1);
                parts.body.z = (Boolean) list.get(2);
                parts.body.pitch = (Boolean) list.get(3);
                parts.body.yaw = (Boolean) list.get(4);
                parts.body.roll = (Boolean) list.get(5);
                parts.head.x = (Boolean) list.get(9);
                parts.head.y = (Boolean) list.get(10);
                parts.head.z = (Boolean) list.get(11);
                parts.head.pitch = (Boolean) list.get(12);
                parts.head.yaw = (Boolean) list.get(13);
                parts.head.roll = (Boolean) list.get(14);
                parts.torso.x = (Boolean) list.get(18);
                parts.torso.y = (Boolean) list.get(19);
                parts.torso.z = (Boolean) list.get(20);
                parts.torso.pitch = (Boolean) list.get(21);
                parts.torso.yaw = (Boolean) list.get(22);
                parts.torso.roll = (Boolean) list.get(23);
                parts.rightArm.x = (Boolean) list.get(27);
                parts.rightArm.y = (Boolean) list.get(28);
                parts.rightArm.z = (Boolean) list.get(29);
                parts.rightArm.pitch = (Boolean) list.get(30);
                parts.rightArm.yaw = (Boolean) list.get(31);
                parts.rightArm.roll = (Boolean) list.get(32);
                parts.rightArm.bend = (Boolean) list.get(33);
                parts.rightArm.bendDirection = (Boolean) list.get(34);
                parts.leftArm.x = (Boolean) list.get(36);
                parts.leftArm.y = (Boolean) list.get(37);
                parts.leftArm.z = (Boolean) list.get(38);
                parts.leftArm.pitch = (Boolean) list.get(39);
                parts.leftArm.yaw = (Boolean) list.get(40);
                parts.leftArm.roll = (Boolean) list.get(41);
                parts.leftArm.bend = (Boolean) list.get(42);
                parts.leftArm.bendDirection = (Boolean) list.get(43);
                parts.rightLeg.x = (Boolean) list.get(45);
                parts.rightLeg.y = (Boolean) list.get(46);
                parts.rightLeg.z = (Boolean) list.get(47);
                parts.rightLeg.pitch = (Boolean) list.get(48);
                parts.rightLeg.yaw = (Boolean) list.get(49);
                parts.rightLeg.roll = (Boolean) list.get(50);
                parts.rightLeg.bend = (Boolean) list.get(51);
                parts.rightLeg.bendDirection = (Boolean) list.get(52);
                parts.leftLeg.x = (Boolean) list.get(54);
                parts.leftLeg.y = (Boolean) list.get(55);
                parts.leftLeg.z = (Boolean) list.get(56);
                parts.leftLeg.pitch = (Boolean) list.get(57);
                parts.leftLeg.yaw = (Boolean) list.get(58);
                parts.leftLeg.roll = (Boolean) list.get(59);
                parts.leftLeg.bend = (Boolean) list.get(60);
                parts.leftLeg.bendDirection = (Boolean) list.get(61);
                parts.rightItem.x = (Boolean) list.get(63);
                parts.rightItem.y = (Boolean) list.get(64);
                parts.rightItem.z = (Boolean) list.get(65);
                parts.rightItem.pitch = (Boolean) list.get(66);
                parts.rightItem.yaw = (Boolean) list.get(67);
                parts.rightItem.roll = (Boolean) list.get(68);
                parts.rightItem.bend = (Boolean) list.get(69);
                parts.rightItem.bendDirection = (Boolean) list.get(70);
                parts.leftItem.x = (Boolean) list.get(72);
                parts.leftItem.y = (Boolean) list.get(73);
                parts.leftItem.z = (Boolean) list.get(74);
                parts.leftItem.pitch = (Boolean) list.get(75);
                parts.leftItem.yaw = (Boolean) list.get(76);
                parts.leftItem.roll = (Boolean) list.get(77);
                parts.leftItem.bend = (Boolean) list.get(78);
                parts.leftItem.bendDirection = (Boolean) list.get(79);
            } catch (IndexOutOfBoundsException var5) {
                return new PlayerParts();
            }
        } catch (NumberFormatException var6) {
        }
        return parts;
    }

    public static final PlayerParts allExceptHeadRot() {
        PlayerParts part = new PlayerParts();
        part.head.setPitch(false);
        part.head.setYaw(false);
        part.head.setRoll(false);
        return part;
    }
}