package th.in.route.routeinth.model.result;

import java.util.List;

import th.in.route.routeinth.model.system.Detail;

/**
 * Created by phompang on 8/9/2016 AD.
 */
public class Result {
    public Detail origin;
    public Detail destination;
    public CardType card_type_bts;
    public CardType card_type_mrt;
    public CardType card_type_arl;
    public List<String> route;
    public int BTS_same_line;
    public List<Route> object_route;
    public Fare fare;

}
