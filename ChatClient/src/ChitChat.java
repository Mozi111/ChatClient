import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.text.BadLocationException;

public class ChitChat {
	public static Osvezi glavna_fukncija;

	public static void main(String[] args) throws IOException, URISyntaxException, BadLocationException {
		ChatFrame chatFrame = new ChatFrame();
		glavna_fukncija = new Osvezi(chatFrame);
		glavna_fukncija.activate(); // Zažene funkcijo za pridobivanje sporočil
									// in prijavljenih uporabnikov.
		chatFrame.pack();

		Runtime.getRuntime().addShutdownHook(new Thread() // Če okno zapremo
															// preko Quit, nas
															// odjavimo
		{
			@Override
			public void run() {
				try {
					if (!chatFrame.ime.equals("")) { // Če okno zapremo, nas
														// odjavimo
						Klient.odjavi(chatFrame.link_uporabniki, chatFrame.ime);
						chatFrame.ime = "";
					}
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				glavna_fukncija.deactivate(); // Ustavimo program, ki osvežuje
												// sporočila in seznam
												// uporabnikov
			}
		});

		chatFrame.setVisible(true); // Prikaže okno.
	}

}
