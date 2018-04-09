package customer;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Mike on 21.12.2017.
 */
public class NSUBuffet
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 5000);
        OrderShowerController orderShowerController = new OrderShowerController(socket);
        MainPageController mainPageController = new MainPageController(orderShowerController);
        MainPageShower mainPageShower = new MainPageShower(mainPageController);
        orderShowerController.setMainPageShower(mainPageShower);
        mainPageShower.go();
    }
}
