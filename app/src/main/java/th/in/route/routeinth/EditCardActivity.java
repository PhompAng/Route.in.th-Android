package th.in.route.routeinth;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import th.in.route.routeinth.app.DatabaseUtils;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.LocaleHelper;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.view.Card;
import th.in.route.routeinth.view.validator.BalanceRule;
import th.in.route.routeinth.view.validator.EditTextValidate;
import th.in.route.routeinth.view.validator.EmptyRule;

public class EditCardActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private UIDUtils uidUtils;
    private Card card;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.number)
    TextInputEditText number;
    @BindView(R.id.balance)
    TextInputEditText balance;
    @BindView(R.id.trip_balance)
    TextInputEditText tripBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        card = Parcels.unwrap(intent.getParcelableExtra("card"));

        setSupportActionBar(toolbar);
        setTitle(String.format(Locale.getDefault(), getString(R.string.editing_card), card.getSystem()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = DatabaseUtils.getDatabase().getReference();
        uidUtils = new UIDUtils(this);

        number.setText(card.getNumber());
        balance.setText(String.format(Locale.getDefault(), "%d", card.getBalance()));
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
            Integer balanceValidate = (new BalanceRule()).validate(balance, card.getSystem());
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
        int cardBalance = Integer.parseInt(balance.getText().toString());

        card.setNumber(cardNumber);
        card.setBalance(cardBalance);

        FirebaseUtils.updateCard(uidUtils.getUID(), card);
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
