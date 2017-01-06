package th.in.route.routeinth.model;

import java.util.Map;

import th.in.route.routeinth.model.view.Card;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class User {
    private String uid;
    private Map<String, Card> cardMap;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Card> getCardMap() {
        return cardMap;
    }

    public void setCardMap(Map<String, Card> cardMap) {
        this.cardMap = cardMap;
    }
}
