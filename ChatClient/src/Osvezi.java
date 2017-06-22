import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.BadLocationException;

public class Osvezi extends TimerTask {
	private ChatFrame chat;
	private Timer timer;

	public Osvezi(ChatFrame chat) {
		this.chat = chat;
	}

	/**
	 * Požene glavno funkcijo vsak interval.
	 */
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}

	/**
	 * Prekine z dejavnostjo.
	 */
	public void deactivate() {
		timer.cancel();
	}

	@Override
	public void run() {
		if (!chat.ime.equals("")) { // Če smo prijavljeni preveri, če nas čakajo
									// nova sporočila.
			List<Sporocilo> prejeta_sporocila;
			try {
				prejeta_sporocila = Klient.prejmi_sporocilo(chat.link_sporocila, chat.ime);
				for (Sporocilo sporocilo : prejeta_sporocila) {
					chat.addMessage(sporocilo.getSender(), sporocilo.getText(), sporocilo.getSentAt(),
							sporocilo.getGlobal(), sporocilo.getRecipient()); // Objavi
																				// nova
																				// sporočila.
				}
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ArrayList<Uporabnik> uporabniki; // Prejme seznam prijavljenih
											// uporabnikov.
		try {
			uporabniki = Klient.prijavljeni_uporabniki(chat.link_uporabniki);
			ArrayList<Uporabnik> myArrayList = uporabniki;
			Uporabnik[] items = new Uporabnik[myArrayList.size()];
			myArrayList.toArray(items);
			chat.seznam_uporabnikov.setListData(items); // Prikaže prijavljenje
														// uporabnike preko
														// JList.
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
