package th.in.route.routeinth.model.view;

import org.parceler.Parcel;

/**
 * Created by phompang on 1/3/2017 AD.
 */

@Parcel
public class Card {
    String name;
    String number;
    int balance;
    int trip_balance;
    String system;
    String type;

    public Card() {
    }

    public Card(String system, int type) {
        this.system = system;
        switch (type) {
            case 1:
                this.type = "Adult";
                break;
            case 2:
                this.type = "Student";
                break;
            case 3:
                if (system.equals("MRT")) {
                    this.type = "Elder";
                } else {
                    this.type = "Senior";
                }
                break;
            case 4:
                this.type = "Child";
                break;
            default:
                this.type = "Normal";
                break;
        }
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getTrip_balance() {
        return trip_balance;
    }

    public void setTrip_balance(int trip_balance) {
        this.trip_balance = trip_balance;
    }

    public int getIntType() {
        switch (type) {
            case "Adult":
                return 1;
            case "Student":
                return 2;
            case "Elder":
            case "Senior":
                return 3;
            case "Child":
                return 4;
            default:
                return 0;
        }
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
