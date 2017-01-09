package th.in.route.routeinth.view.validator;

import android.support.design.widget.TextInputEditText;

/**
 * Created by phompang on 1/9/2017 AD.
 */

public class BalanceRule implements EditTextValidate {
    @Override
    public TextInputEditText validate(TextInputEditText v) {
        return null;
    }

    public Integer validate(TextInputEditText v, String system) {
        switch (system) {
            case "BTS":
                if (Integer.parseInt(v.getText().toString()) > 4000) {
                    return 4000;
                }
                return null;
            case "MRT":
                if (Integer.parseInt(v.getText().toString()) > 2000) {
                    return 2000;
                }
                return null;
            case "ARL":
                if (Integer.parseInt(v.getText().toString()) > 3000) {
                    return 3000;
                }
                return null;
        }
        return null;
    }
}
