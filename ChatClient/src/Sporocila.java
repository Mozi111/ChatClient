import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.BadLocationException;

public class Sporocila extends TimerTask {
	private ChatFrame chat;
	private Timer timer;

	public Sporocila(ChatFrame chat) {
		this.chat = chat;
	}

	/**
	 * Activate the robot!
	 */
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	/**
	 * Deactivate the robot!
	 */
	public void deactivate() {
		timer.cancel();
		System.out.println("Robot deactivated");
	}

	@Override
	public void run() {
		if (!chat.prejsnjivzdevek.equals("")) {
			List<Sporocilo> prejeta_sporocila;
			try {
				prejeta_sporocila = Klient.prejmi_sporocilo(chat.link_sporocila, chat.prejsnjivzdevek);
				for (Sporocilo sporocilo : prejeta_sporocila) {
					chat.addMessage(sporocilo.getSender(), sporocilo.getText(), sporocilo.getSentAt(),
							sporocilo.getGlobal(), sporocilo.getRecipient());
				}
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<Uporabnik> uporabniki;
		try {
			uporabniki = Klient.seznam_uporabnikov(chat.link);
			ArrayList<Uporabnik> myArrayList = uporabniki;
			Uporabnik[] items = new Uporabnik[myArrayList.size()];
			myArrayList.toArray(items);
			chat.prijavljeni_uporabniki.setListData(items);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
