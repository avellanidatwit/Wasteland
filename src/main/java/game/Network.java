package game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Class to handle server side operations
 *
 * @author santorsa
 * @category Networking
 */
public class Network {

	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Card.class);
		kryo.register(Deck.class);
		kryo.register(User.class);
		kryo.register(Request.class);
		kryo.register(Text.class);
		kryo.register(Status.class);
		kryo.register(ActionsLeft.class);
	}
}
