package th.in.route.routeinth.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.R;
import th.in.route.routeinth.model.view.Card;

/**
 * Created by phompang on 1/7/2017 AD.
 */

public class AddValueDialog extends DialogFragment {
    public static final String ARG_CARD = "card";

    private Card card;

    public static android.support.v4.app.DialogFragment newInstance(Card card) {
        AddValueDialog dialog = new AddValueDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, Parcels.wrap(card));
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            card = Parcels.unwrap(getArguments().getParcelable(ARG_CARD));
        }
    }

    @BindView(R.id.picker)
    NumberPicker numberPicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_value, null);
        ButterKnife.bind(this, v);

        int minValue = 0;
        int maxValue= 0;
        int step = 0;
        switch (card.getSystem()){
            case "BTS":
                minValue = 100;
                maxValue = 4000 - card.getBalance();
                step = 100;
                break;
            case "MRT":
                minValue = 100;
                maxValue = 2000 - card.getBalance();
                step = 100;
                break;
            case "ARL":
                minValue = 20;
                maxValue = 3000 - card.getBalance();
                step = 10;
                break;

        }
        
        final String[] valueSet;
        if (maxValue < minValue) {
            valueSet = new String[1];
            valueSet[0] = "0";
        } else {
            valueSet = new String[(maxValue-minValue)/step +1];
            int tmp = minValue;
            for (int i = 0; i < valueSet.length; i++) {
                valueSet[i] = String.valueOf(tmp);
                tmp += step;
            }
        }

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(valueSet.length-1);
        numberPicker.setDisplayedValues(valueSet);

        numberPicker.setWrapSelectorWheel(true);
        builder.setTitle("Select Value")
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddValueDialog.this.getDialog().cancel();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(ARG_CARD, Parcels.wrap(card));
                        intent.putExtra("value", Integer.parseInt(valueSet[numberPicker.getValue()]));
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                });

        return builder.create();
    }
}
