package th.in.route.routeinth.model.result;

/**
 * Created by phompang on 8/9/2016 AD.
 */
public class Input {
    public String origin;
    public String destination;
    public String card_type_bts;
    public String card_type_mrt;
    public String card_type_arl;

    @Override
    public String toString() {
        return "origin: " + origin + " destination: " + destination + "\n" +
                "bts: " + card_type_bts + "\n" +
                "mrt: " + card_type_mrt + "\n" +
                "arl: " + card_type_arl;
    }
}
