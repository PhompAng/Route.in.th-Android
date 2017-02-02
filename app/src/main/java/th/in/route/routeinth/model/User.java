package th.in.route.routeinth.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import th.in.route.routeinth.model.view.Card;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class User {
    private String uid;
    private String token;
    private Map<String, Card> cardMap;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Card> getCardMap() {
        return cardMap == null ? new HashMap<String, Card>():cardMap;
    }

    public void setCardMap(Map<String, Card> cardMap) {
        this.cardMap = cardMap;
    }

    public Set<String> getUnAddedCard() {
        Set<String> allCard = new HashSet<>();
        allCard.add("BTS");
        allCard.add("MRT");
        allCard.add("ARL");
        if (cardMap == null) {
            return allCard;
        }
        Set<String> addedCard = cardMap.keySet();
        allCard.removeAll(addedCard);
        return allCard;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
