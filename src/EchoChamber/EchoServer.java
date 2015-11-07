package EchoChamber;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ToNio on 6/11/2015.
 */

/**
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */
@ServerEndpoint("/EchoChamber/echo")
public class EchoServer
{
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was
     * successful.
     */

    private static Set<Session> clients = new HashSet<Session>();

    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println(session.getId() + " has opened a connection!");
        try
        {
            clients.add( session );
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        System.out.println("Message from " + session.getId() + ": " + message);
        try
        {
            for(Session client : clients)
                client.getBasicRemote().sendText(message);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * The user closes the connection.
     * <p>
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session)
    {
        System.out.println("Session " + session.getId() + " has ended");
    }
}
