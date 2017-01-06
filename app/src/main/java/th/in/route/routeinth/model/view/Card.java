package th.in.route.routeinth.model.view;

/**
 * Created by phompang on 1/3/2017 AD.
 */

public class Card {
    private String name;
    private String number;
    private double balance;
    private int trip_balance;
    private String system;
    private String type;

    public Card() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTrip_balance() {
        return trip_balance;
    }

    public void setTrip_balance(int trip_balance) {
        this.trip_balance = trip_balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
