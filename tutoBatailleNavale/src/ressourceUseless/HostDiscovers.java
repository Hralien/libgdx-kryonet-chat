package ressourceUseless;

import java.io.IOException;
import java.net.InetAddress;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class HostDiscovers implements ApplicationListener
{
	static public int tcpPort = 54555, udpPort = 54777;
	@Override
	public void create()
	{
		Gdx.app.log("UDPTEST", "STARTED");
		
//		DiscoverServerTest();		
		DiscoverServerTestUDPTCP();
	}
	
	public void DiscoverServerTest()
	{
		final Server broadcastServer = new Server();
		broadcastServer.start();
		try
		{
			broadcastServer.bind(0, udpPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		broadcastServer.addListener(new Listener()
		{
			public void connected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "UDPSERVER: Client connected");
			}
			public void disconnected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "UDPSERVER: Client disconnected");
			}
		});

		final Server server = new Server();
		server.start();
		try
		{
			//just for tcp server
			server.bind(tcpPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		server.addListener(new Listener()
		{
			public void connected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "TCPSERVER: Client connected");
			}
			public void disconnected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "TCPSERVER: Client disconnected");
			}
		});

		Client client = new Client();
		InetAddress host = client.discoverHost(udpPort, 2000);
		if (host == null)
		{
			Gdx.app.log("UDPTEST", "No servers found.");
			return;
		}
		else
			Gdx.app.log("UDPTEST", "Client: discovered host "+host.getHostAddress());

		
		client.addListener(new Listener()
		{
			public void connected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "Client: Client connected");
			}
			public void received(Connection connection, Object object)
			{
				Gdx.app.log("UDPTEST", "Client: Client received");
			}
			public void disconnected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "Client: Client disconnected");
			}
			
			
		});
		
		client.start();
		try
		{
			client.connect(2000, host, tcpPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void DiscoverServerTestUDPTCP()
	{
		final Server server = new Server();
		server.start();
		try
		{
			//just for tcp server
			server.bind(54555,udpPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		server.addListener(new Listener()
		{
			public void connected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "SERVER: Client connected");
			}
			public void disconnected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "SERVER: Client disconnected");
			}
		});

		Client client = new Client();
		InetAddress host = client.discoverHost(udpPort, 2000);
		if (host == null)
		{
			Gdx.app.log("UDPTEST", "No servers found.");
			return;
		}
		else
			Gdx.app.log("UDPTEST", "Client: discovered host "+host.getHostAddress());

		
		client.addListener(new Listener()
		{
			public void connected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "Client: Client connected");
			}
			public void received(Connection connection, Object object)
			{
				Gdx.app.log("UDPTEST", "Client: Client received");
			}
			public void disconnected(Connection connection)
			{
				Gdx.app.log("UDPTEST", "Client: Client disconnected");
			}
			
			
		});
		
		client.start();
		try
		{
			client.connect(2000, host, tcpPort, udpPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
}
