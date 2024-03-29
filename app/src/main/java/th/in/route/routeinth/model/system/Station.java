package th.in.route.routeinth.model.system;

import org.parceler.Parcel;

/**
 * Created by phompang on 12/22/2016 AD.
 */
@Parcel
public class Station {
    String th;
    String en;
    String code;
    String key;

    public Station() {
    }

    public Station(String th, String en, String code, String key) {
        this.setTh(th);
        this.setEn(en);
        this.setCode(code);
        this.setKey(key);
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

    @Override
    public String toString() {
        return getEn();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Station && getKey().equals(((Station) obj).getKey());

    }
}
