package com.me.mygdxgame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Input.Keys;




public class JavaHelp implements UITrick{
	public Skin skin;

	public JavaHelp() {
		super();
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	}

	@Override
	public void showToast(CharSequence toastMessage, int toastDuration) {
		//		JOptionPane.showMessageDialog(new JFrame("Message"), toastMessage);
//		new Dialog("Some Dialog", skin, "dialog") {
//			protected void result (Object object) {
//			}
//		}.text("Server already created").button("Yes", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
	}

	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText) {
		//		JOptionPane.showMessageDialog(new JFrame(alertBoxTitle), alertBoxMessage);

	}
	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText, Stage stage) {
		new Dialog("Some Dialog", skin, "dialog") {
			protected void result (Object object) {
			}
		}.text("Server already created").button("Yes", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
	}
	@Override
	public void openUri(String uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMyList() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getConnectedWifi() {
		try {
			InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}



}
