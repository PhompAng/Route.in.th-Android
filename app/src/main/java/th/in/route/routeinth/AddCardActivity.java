package th.in.route.routeinth;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.model.view.Card;

public class AddCardActivity extends AppCompatActivity {

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> systemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"ARL", "BTS", "MRT"});
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Normal", "Adult", "Student", "Senior"});
        systemSpinner.setAdapter(systemAdapter);
        typeSpinner.setAdapter(typeAdapter);
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
        //TODO validate

        addCard();
    }

    private void addCard() {
        String cardNumber = number.getText().toString();
        String cardSystem = (String) systemSpinner.getSelectedItem();
        String cardType = (String) typeSpinner.getSelectedItem();
        double cardBalance = Double.parseDouble(balance.getText().toString());

        Card c = new Card();
        c.setNumber(cardNumber);
        c.setSystem(cardSystem);
        c.setType(cardType);
        c.setBalance(cardBalance);
        c.setName(cardSystem + " " + cardType + " Card");

        FirebaseUtils.addCard(getApplicationContext(), c);
        finish();
    }
}