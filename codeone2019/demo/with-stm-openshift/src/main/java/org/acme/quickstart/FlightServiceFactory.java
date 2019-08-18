package org.acme.quickstart;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.exceptions.ObjectStoreException;
import com.arjuna.ats.arjuna.objectstore.ParticipantStore;
import com.arjuna.ats.arjuna.objectstore.StoreManager;
import com.arjuna.ats.arjuna.objectstore.TxLog;
import com.arjuna.ats.arjuna.state.InputObjectState;
import com.arjuna.ats.arjuna.state.OutputObjectState;
import org.jboss.stm.Container;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@ApplicationScoped
class FlightServiceFactory {
    private static String stmDemoUid = "0:ffffc0a80008:8ac3:5a0de48d:2";
    private static String type = "/STMDemos";

    private Container<FlightService> flightContainer;
    private FlightService flightService;
    private String uid;

    public FlightServiceFactory() {
        flightContainer = new Container<>(Container.TYPE.PERSISTENT, Container.MODEL.SHARED);

        uid = readSharedUid();

        if (uid == null) {
            flightService = flightContainer.create(new FlightServiceImpl());

            uid = flightContainer.getIdentifier(flightService).toString();

            writeSharedUid(uid);

            System.out.printf("Created uid %s%n", uid);
        } else {
            flightService = flightContainer.clone(new FlightServiceImpl(), new Uid(uid));

            System.out.printf("Using uid %s%n", uid);
        }
    }

    FlightService getInstance() {
        return flightService;
    }

    private void writeSharedUid(String uid) {
        try {
            OutputObjectState oState = new OutputObjectState();
            oState.packString(uid);

            TxLog txLog = StoreManager.getCommunicationStore();
            txLog.write_committed(new Uid(stmDemoUid), type, oState);
        } catch (IOException | ObjectStoreException e) {
            System.out.printf("WARNING: could not write transaction log: %s%n",
                    e.getMessage());
        }
    }

    private String readSharedUid() {
        ParticipantStore participantStore = StoreManager.getCommunicationStore();
        try {
            InputObjectState iState = participantStore.read_committed(
                    new Uid(stmDemoUid), type);

            if (iState != null) {
                return iState.unpackString();
            }
        } catch (ObjectStoreException | IOException ignore) {
        }

        return null;
    }

    String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return e.getMessage();
        }
    }
}
