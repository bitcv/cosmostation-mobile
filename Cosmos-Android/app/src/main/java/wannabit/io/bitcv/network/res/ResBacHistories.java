package wannabit.io.bitcv.network.res;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.BacHistory;
import wannabit.io.bitcv.model.type.BnbHistory;
import wannabit.io.bitcv.model.type.Validator;

public class ResBacHistories implements  Parcelable{
    @SerializedName("code")
    public int code;

    @SerializedName("msg")
    public String msg;

    @SerializedName("data")
    public ArrayList<BacHistory> data;

    @SerializedName("total")
    public int total;


        public ResBacHistories() {
        }

        public ResBacHistories(ArrayList<BacHistory> data, int code , String msg, int total) {
            this.data = data;
            this.code = code;
            this.msg = msg;
            this.total = total;
        }

        protected ResBacHistories(Parcel in) {
            readFromParcel(in);
        }

        public void readFromParcel(Parcel in) {
            code = in.readInt();
            msg = in.readString();
            data = new ArrayList<BacHistory>();
            in.readTypedList(data, BacHistory.CREATOR);
            total = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(code);
            dest.writeString(msg);
           // dest.writeParcelableArray(data, flags);
            dest.writeTypedList(data);
            dest.writeInt(total);
        }

        public static final Creator<ResLcdValidators> CREATOR = new Creator<ResLcdValidators>() {
            @Override
            public ResLcdValidators createFromParcel(Parcel in) {
                return new ResLcdValidators(in);
            }

            @Override
            public ResLcdValidators[] newArray(int size) {
                return new ResLcdValidators[size];
            }
        };
    }

