package chat;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public  class ChatWindow{
	private Window window;
	private Skin skin;
	private List messageList;
	private List nameList;
	private TextButton envoyer;
	private TextField tfMessage;
	
	public ChatWindow(String host){
		String[] tabUsers = {};
		String[] tabMessage = {};
					
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		tfMessage = new TextField("", skin);
		tfMessage.setMessageText("Saisir votre message");
		
		envoyer = new TextButton("Envoyer",skin);
		
		messageList = new List(tabUsers, skin);
		nameList = new List(tabMessage, skin);

		ScrollPane scrollPaneMessage = new ScrollPane(messageList,skin);
		scrollPaneMessage.setFlickScroll(true);

		ScrollPane scrollPaneUser = new ScrollPane(nameList, skin);
		
		scrollPaneUser.setFlickScroll(true);
		SplitPane splitPane = new SplitPane(scrollPaneMessage, scrollPaneUser, false, skin, "default-horizontal");

		window = new Window("Connexion", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(100, 100);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.row();
		window.add(splitPane).minWidth(100).expandX().fillX().minHeight(100);
		window.row();
		window.add(tfMessage).minWidth(100).expandX().fillX().colspan(10);
		window.row();
		window.add(envoyer);
		window.row();
		window.pack();

		

	}
	
	public Window getWindow(){
		return window;
	}
	
	public void setSendListener (final Runnable listener) {
		envoyer.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (getSendText().length() == 0) return;
				listener.run();
				tfMessage.setText("");
			}
		});
	}


	public String getSendText () {
		return tfMessage.getText().trim();
	}

	public void setNames (final String[] names) {
		// This listener is run on the client's update thread, which was started by client.start().
		// We must be careful to only interact with Swing components on the Swing event thread.
//		EventQueue.invokeLater(new Runnable() {
//			public void run () {
				nameList.setItems(names);
//			}
//		});
	}

	public void addMessage (final String message) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run () {
				ArrayList<String> listMessage= new ArrayList<String>();
				for(String it: messageList.getItems()){
					listMessage.add(it);
				}
				listMessage.add(message);
				messageList.setItems(listMessage.toArray());
//			}
//		});
	}
}