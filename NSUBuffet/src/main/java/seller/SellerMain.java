package seller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by miketurch on 22.12.17.
 */
public class SellerMain
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("192.168.1.14", 5000);
        ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());

        SellerShowerController sellerShowerController = new SellerShowerController(writer,reader, Integer.parseInt(args[0]));
        SellerShower sellerShower = new SellerShower(sellerShowerController);
        sellerShowerController.setSellerShower(sellerShower);
        sellerShower.go();
    }
}
