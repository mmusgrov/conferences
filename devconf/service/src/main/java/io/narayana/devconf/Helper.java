package io.narayana.devconf;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.exceptions.ObjectStoreException;
import com.arjuna.ats.arjuna.objectstore.ParticipantStore;
import com.arjuna.ats.arjuna.objectstore.StoreManager;
import com.arjuna.ats.arjuna.objectstore.TxLog;
import com.arjuna.ats.arjuna.state.InputObjectState;
import com.arjuna.ats.arjuna.state.OutputObjectState;
import org.jboss.stm.Container;

import java.io.IOException;

public class Helper {
    static private String stmDemoUid = "0:ffffc0a80008:8ac3:5a0de48d:2";
    static private String type = "/STMDemos";

    public static TaxiService getTaxiService(String uid) {
        Container<TaxiService> taxiContainer = new Container<>(Container.TYPE.PERSISTENT, Container.MODEL.SHARED);

        return taxiContainer.clone(new TaxiServiceImpl(), new Uid(uid));
    }

    public static TaxiService getTaxiService() {
        String uid = Helper.readSharedUid();
        TaxiService taxiService;
        Container<TaxiService> taxiContainer = new Container<>(Container.TYPE.PERSISTENT, Container.MODEL.SHARED);

        if (uid == null) {

            taxiService = taxiContainer.create(new TaxiServiceImpl());

            uid = taxiContainer.getIdentifier(taxiService).toString();

            Helper.writeSharedUid(uid);

            // workaround JBTM-1732
/*            AtomicAction action = new AtomicAction();

            action.begin();
            flightService.makeBooking("");
            action.commit();*/

            System.out.printf("Created uid %s%n", uid);
        } else {
            taxiService = taxiContainer.clone(new TaxiServiceImpl(), new Uid(uid));
            System.out.printf("Using uid %s%n", uid);
        }

        return taxiService;
    }

    public static boolean writeSharedUid(String uid) {
        try {
            OutputObjectState oState = new OutputObjectState();
            oState.packString(uid);

            TxLog txLog = StoreManager.getCommunicationStore();
            txLog.write_committed( new Uid(stmDemoUid), type, oState);
            return true;
        } catch (IOException | ObjectStoreException e) {
            return false;
        }
    }

    public static String readSharedUid() {
        ParticipantStore participantStore = StoreManager.getCommunicationStore();
        try {
            InputObjectState iState = participantStore.read_committed(new Uid(stmDemoUid), type);

            if (iState != null)
                return iState.unpackString();
        } catch (ObjectStoreException | IOException ignore) {
        }

        return null;
    }

/*    static void sharing(Vertx vertx, String uid) {
        SharedData sd = vertx.sharedData();

        LocalMap<String, String> map = sd.getLocalMap("stm-demo");

        map.put("flight-uid", uid);
        sd.<String, String>getClusterWideMap("stm-demo", res -> {
            if (res.succeeded()) {
                AsyncMap<String, String> sharedMap = res.result();
                sharedMap.put("flight-uid", uid, resPut -> {
                    if (resPut.succeeded()) {
                        // Successfully put the value
                    } else {
                        // Something went wrong!
                    }
                });

            } else {
                // Something went wrong!
            }
        });
    }*/
}
