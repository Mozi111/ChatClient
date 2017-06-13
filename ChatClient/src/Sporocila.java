import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ListModel;

public class Sporocila extends TimerTask {
	private ChatFrame chat;

	public Sporocila(ChatFrame chat) {
		this.chat = chat;
	}

	/**
	 * Activate the robot!
	 */
	public void activate() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}

	@Override
	public void run() {
		if (!chat.prejsnjivzdevek.equals("")) {
			List<Sporocilo> prejeta_sporocila;
			try {
				prejeta_sporocila = Klient.prejmi_sporocilo(chat.link_sporocila, chat.prejsnjivzdevek);
				for (Sporocilo sporocilo : prejeta_sporocila) {
					chat.addMessage(sporocilo.getSender(), sporocilo.getText());
				}
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Uporabnik> uporabniki;
		try {
			uporabniki = Klient.seznam_uporabnikov(chat.link);
			Object[] imena_uporabnikov = Klient.imena_uporabnikov(uporabniki).toArray();
			chat.prijavljeni_uporabniki.setListData(imena_uporabnikov);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
