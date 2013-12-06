
package chat;

import gameMechanic.Personnage;
import gameMechanic.Shaman;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int portTCP = 54555;
	static public final int portUDP = 54556;

	static public final short PLAYER = 1;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(ArrayList.class);
		kryo.register(SkillNumber.class);
		kryo.register(ConstantOrder.class);
		kryo.register(ArrayList.class);
//		kryo.register(Personnage.class);
		kryo.register(Shaman.class);
	}

	static public class RegisterName {
		public String name;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class ChatMessage {
		public String text;
	}
	
	static public class TestConnection{
		public String test;
	}

	static public class SkillNumber {
		public int skillId;		
	}
	
	static public class ConstantOrder{
		public int order;
		public static final int STARTGAME=1;
	}


}
