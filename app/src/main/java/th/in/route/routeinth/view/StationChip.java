package th.in.route.routeinth.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import th.in.route.routeinth.R;
import th.in.route.routeinth.app.DpiUtils;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/23/2016 AD.
 */

public class StationChip extends TextView {
    private Station station;

    public StationChip(Context context) {
        super(context);
        setup();
    }

    public StationChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public StationChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public StationChip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup() {
        if (station == null) {
            setBackgroundResource(R.drawable.arl_chip);
            setGravity(Gravity.CENTER);
            setElevation(DpiUtils.toPixels(1, getResources().getDisplayMetrics()));
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        } else {
            setText(station.getCode());
            switch (station.getKey().charAt(0)) {
                case 'A':
                    setBackgroundResource(R.drawable.arl_chip);
                    break;
                case 'B':
                    if (station.getKey().charAt(1) == 'W' || station.getKey().charAt(1) == 'S') {
                        setBackgroundResource(R.drawable.bts_silom_chip);
                    } else {
                        setBackgroundResource(R.drawable.bts_sukhumvit_chip);
                    }
                    break;
                case 'M':
                    setBackgroundResource(R.drawable.mrt_chip);
                    break;
            }
        }
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
        setup();
    }
}
