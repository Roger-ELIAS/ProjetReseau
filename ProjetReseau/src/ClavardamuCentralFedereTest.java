import java.util.ArrayList;

public class ClavardamuCentralFedereTest {

    ArrayList<ClavardamuCentralFedere> ccfList = new ArrayList<ClavardamuCentralFedere>();

    public static void main(String[] args) {
        ArrayList<ClavardamuCentralFedere> ccfList = new ArrayList<ClavardamuCentralFedere>();
        ccfList.add(new ClavardamuCentralFedere(12345));
        ccfList.add(new ClavardamuCentralFedere(12346));
        ccfList.add(new ClavardamuCentralFedere(12347));
        ccfList.add(new ClavardamuCentralFedere(12348));
        ccfList.add(new ClavardamuCentralFedere(12349));
        ccfList.add(new ClavardamuCentralFedere(12350));
        ccfList.add(new ClavardamuCentralFedere(12351));
        ccfList.add(new ClavardamuCentralFedere(12352));
        ccfList.add(new ClavardamuCentralFedere(12353));
        ccfList.add(new ClavardamuCentralFedere(12354));

        for(ClavardamuCentralFedere tmpFedere : ccfList){
            tmpFedere.startServer();
        }

        int i = 1;
        for(ClavardamuCentralFedere tmpFedere : ccfList){
            tmpFedere.interconnectServer("src/pairs" + i + ".cfg");
            ++i;
        }
    }
}
