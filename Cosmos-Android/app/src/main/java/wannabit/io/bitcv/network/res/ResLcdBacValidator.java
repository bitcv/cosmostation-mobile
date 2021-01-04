package wannabit.io.bitcv.network.res;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Validator;

public class ResLcdBacValidator implements Parcelable {

    @SerializedName("Validator")
    public Validator validator;

    @SerializedName("addr_from_pubkey")
    public String addr_from_pubkey;

    @SerializedName("addr_bench32")
    public String addr_bench32;

    public ResLcdBacValidator() {
    }

    public ResLcdBacValidator(Validator validator, String addr_from_pubkey, String addr_bench32) {
        this.validator = validator;
        this.addr_from_pubkey = addr_from_pubkey;
        this.addr_bench32 = addr_bench32;
    }

    protected ResLcdBacValidator(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        addr_from_pubkey = in.readString();
        addr_bench32 = in.readString();
        validator = new Validator();
        validator = in.readParcelable(Validator.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addr_from_pubkey);
        dest.writeString(addr_bench32);
        dest.writeParcelable(validator, flags);
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
