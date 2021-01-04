package wannabit.io.bitcv.model.type;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
//{"id":87446,"from":"bac1gp5v87r34rameycnumfjuqrv6crjul2qmqz6hw",
// "to":"bac139fnsshg3rdt436yeal6dzk228mjr8eym6uupd","amount":"10",
// "msg_type":"bacchain\/MsgSend","height":4281404,"fee":"0.1",
// "trade_hash":"41de138306073f205b58349be3558cc352cc5a8b727074d51a170c193142d8d1",
// "state":0,"gas_wanted":100000000,"gas_used":19472,"coin":"BCV","log":"",
// "create_time":1609771987,"memo":"","ext":"",
// "icon":"https:\/\/bitcv.kingco.tech\/bac-chain-icon\/ex-transfer.png","title":"\u8f6c\u8d26"},
public class BacHistory implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("from")
    public String from;

    @SerializedName("to")
    public String to;

    @SerializedName("amount")
    public String amount;

    @SerializedName("msg_type")
    public String txType;

    @SerializedName("height")
    public int blockHeight;

    @SerializedName("fee")
    public String fee;

    @SerializedName("trade_hash")
    public String txHash;

    @SerializedName("state")
    public int state;

    @SerializedName("coin")
    public String coin;

    @SerializedName("create_time")
    public String timeStamp;

    @SerializedName("icon")
    public String icon;

    @SerializedName("title")
    public String title;



    public BacHistory() {
    }

    public BacHistory(int  id, String from, String to ,String amount, String txType, int blockHeight, String fee,
            String txHash, int state, String coin, String timeStamp, String icon,String title)
    {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.txType = txType;
        this.blockHeight = blockHeight;
        this.fee = fee;
        this.txHash = txHash;
        this.state = state;
        this.coin = coin;
        this.timeStamp = timeStamp;
        this.icon = icon ;
        this.title = title;
    }

    protected BacHistory(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        from = in.readString();
        to = in.readString();
        amount = in.readString();
        txType = in.readString();
        blockHeight = in.readInt();
        fee = in.readString();
        txHash = in.readString();
        state = in.readInt();
        coin = in.readString();
        timeStamp =  in.readString();
        icon = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(amount);
        dest.writeString(txType);
        dest.writeInt(blockHeight);
        dest.writeString(fee);
        dest.writeString(txHash);
        dest.writeInt(state);
        dest.writeString(coin);
        dest.writeString(timeStamp);
        dest.writeString(icon);
        dest.writeString(title);
    }

    public static final Creator<BacHistory> CREATOR = new Creator<BacHistory>() {
        @Override
        public BacHistory createFromParcel(Parcel in) {
            return new BacHistory(in);
        }

        @Override
        public BacHistory[] newArray(int size) {
            return new BacHistory[size];
        }
    };

}
