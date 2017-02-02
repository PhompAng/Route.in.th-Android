package th.in.route.routeinth.app;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phompang on 2/2/2017 AD.
 */

public class FacilitiesUtils {
    private Map<String, List<String>> facilitiesMap;
    private static FacilitiesUtils sFacilitiesUtils;

    private FacilitiesUtils() {
        facilitiesMap = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("facilities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    GenericTypeIndicator<List<String>> indicator = new GenericTypeIndicator<List<String>>(){};
                    List<String> facilities = snapshot.getValue(indicator);
                    facilitiesMap.put(snapshot.getKey(), facilities);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static FacilitiesUtils getInstance() {
        if (sFacilitiesUtils == null) {
            sFacilitiesUtils = new FacilitiesUtils();
        }
        return sFacilitiesUtils;
    }


    public Map<String, List<String>> getFacilitiesMap() {
        return facilitiesMap;
    }
}
