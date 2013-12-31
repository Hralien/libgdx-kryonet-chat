
package chat;


import java.util.ArrayList;

import m4ges.models.Aquamancien;
import m4ges.models.Necromancien;
import m4ges.models.Personnage;
import m4ges.models.Pyromancien;
import m4ges.models.Shaman;
import m4ges.util.Constants;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
		kryo.register(Constants.class);
		kryo.register(ArrayList.class);
		kryo.register(Personnage.class);
		kryo.register(Shaman.class);
		kryo.register(RequestName.class);
		kryo.register(Necromancien.class);
		kryo.register(Pyromancien.class);
		kryo.register(Aquamancien.class);
		kryo.register(byte[].class);
		
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
	
	static public class RequestName{
		public String name;
		
	}

}
