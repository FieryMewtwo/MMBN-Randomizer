package rand.prod;

import rand.ByteStream;
import rand.lib.ChipLibrary;
import rand.obj.Reward;

public class BN6RewardProducer extends RewardProducer {
    public BN6RewardProducer(ChipLibrary library) {
        super(library);
    }

    @Override
    public Reward readFromStream(ByteStream stream) {
        int value = stream.readUInt16();
        switch (value >> 14) {
            case 0:
                return new Reward(this.library.getElement(value & 0x1FF),
                        (byte)((value >> 9) & 0x1F));
            case 1:
                return new Reward(Reward.Type.ZENNY, value & 0x3FFF);
            case 2:
                return new Reward(Reward.Type.HP, value & 0x3FFF);
            case 3:
                return new Reward(Reward.Type.BUGFRAG, value & 0x3FFF);
        }
        return null;
    }

    @Override
    public void writeToStream(ByteStream stream, Reward reward) {
        int value = 0;
        switch (reward.getType()) {
            case CHIP:
                value |= reward.getChip().index() & 0x1FF;
                value |= (reward.getCode() & 0x1F) << 9;
                break;
            case ZENNY:
                value |= 1 << 14;
                value |= reward.getAmount() & 0x3FFF;
                break;
            case HP:
                value |= 2 << 14;
                value |= reward.getAmount() & 0x3FFF;
                break;
            case BUGFRAG:
                value |= 3 << 14;
                value |= reward.getAmount() & 0x3FFF;
                break;
        }
        stream.writeUInt16(value);
    }
}
