package th.in.route.routeinth.model.view;

/**
 * Created by phompang on 2/1/2017 AD.
 */

public class Tweet {
    private long id;
    private String text;
    private TwitterUser user;
    private long timestamp_ms;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public long getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(long timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }
}
