package th.in.route.routeinth;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import th.in.route.routeinth.adapter.MyArrayAdapter;
import th.in.route.routeinth.app.DatabaseUtils;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.LocaleHelper;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.User;
import th.in.route.routeinth.model.view.Card;
import th.in.route.routeinth.view.validator.BalanceRule;
import th.in.route.routeinth.view.validator.EditTextValidate;
import th.in.route.routeinth.view.validator.EmptyRule;

public class AddCardActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private UIDUtils uidUtils;

    private MyArrayAdapter<String> systemAdapter;
    private MyArrayAdapter<String> typeAdapter;
    private Set<String> systemSet;
    private List<String> typeList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.number)
    TextInputEditText number;
    @BindView(R.id.system_spinner)
    Spinner systemSpinner;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;
    @BindView(R.id.balance)
    TextInputEditText balance;
    @BindView(R.id.trip_balance)
    TextInputEditText tripBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.add_card));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        systemSet = new HashSet<>();
        typeList = new ArrayList<>();
        typeList.add(getString(R.string.adult));
        typeList.add(getString(R.string.student));
        typeList.add(getString(R.string.senior));
        systemAdapter = new MyArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(systemSet));
        systemAdapter.setNotifyOnChange(true);
        typeAdapter = new MyArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        systemSpinner.setAdapter(systemAdapter);
        typeSpinner.setAdapter(typeAdapter);

        reference = DatabaseUtils.getDatabase().getReference();
        uidUtils = new UIDUtils(this);
        reference.child("users").child(uidUtils.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                systemAdapter.clear();
                systemAdapter.addAll(new ArrayList<>(user.getUnAddedCard()));
                systemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemSelected(R.id.system_spinner)
    public void systemChange() {
        typeAdapter.clear();
        typeList.clear();
        typeList.add(getString(R.string.adult));
        typeList.add(getString(R.string.student));
        if (systemSpinner.getSelectedItem().equals("MRT")) {
            typeList.add(getString(R.string.elder));
            typeList.add(getString(R.string.child));
        } else {
            typeList.add(getString(R.string.senior));
        }
        typeAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.submit)
    public void validate() {
        List<TextInputEditText> editTexts = new ArrayList<>();
        editTexts.add(balance);
        editTexts.add(number);

        boolean cancel = false;
        View focusView = null;

        EditTextValidate validator = new EmptyRule();

        for (TextInputEditText editText: editTexts) {
            editText.setError(null);
            if (validator.validate(editText) != null) {
                editText.setError(getString(R.string.required));
                cancel = true;
                focusView = editText;
            }
        }

        if (validator.validate(balance) == null) {
            Integer balanceValidate = (new BalanceRule()).validate(balance, (String) systemSpinner.getSelectedItem());
            if (balanceValidate != null) {
                balance.setError(String.format(Locale.getDefault(), getString(R.string.balance_exceed), balanceValidate));
                cancel = true;
                focusView = balance;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            addCard();
        }
    }

    private void addCard() {
        String cardNumber = number.getText().toString();
        String cardSystem = (String) systemSpinner.getSelectedItem();
        String cardType = (String) typeSpinner.getSelectedItem();
        int cardBalance = Integer.parseInt(balance.getText().toString());

        Card c = new Card();
        c.setNumber(cardNumber);
        c.setSystem(cardSystem);
        c.setType(cardType);
        c.setBalance(cardBalance);
        c.setName(cardSystem + " " + cardType + " Card");

        FirebaseUtils.addCard(uidUtils.getUID(), c);
        finish();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }
}
