package th.in.route.routeinth.view.validator;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;

/**
 * Created by phompang on 1/7/2017 AD.
 */

public class EmptyRule implements EditTextValidate {
    @Override
    public TextInputEditText validate(TextInputEditText v) {
        if (TextUtils.isEmpty(v.getText().toString())) {
            return v;
        } else {
            return null;
        }
    }
}
