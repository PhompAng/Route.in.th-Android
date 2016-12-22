package th.in.route.routeinth.model.system;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class Station implements Parcelable {
    private String th;
    private String en;
    private String code;
    private String key;

    public Station(String th, String en, String code, String key) {
        this.setTh(th);
        this.setEn(en);
        this.setCode(code);
        this.setKey(key);
    }

    protected Station(Parcel in) {
        setTh(in.readString());
        setEn(in.readString());
        setCode(in.readString());
        setKey(in.readString());
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTh());
        parcel.writeString(getEn());
        parcel.writeString(getCode());
        parcel.writeString(getKey());
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
