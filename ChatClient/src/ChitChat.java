import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.text.BadLocationException;

public class ChitChat {
	public static Osvezi glavna_fukncija;

	public static void main(String[] args) throws IOException, URISyntaxException, BadLocationException {
		ChatFrame chatFrame = new ChatFrame();
		glavna_fukncija = new Osvezi(chatFrame);
		glavna_fukncija.activate(); // Zažene funkcijo za pridobivanje sporoèil in prijavljenih uporabnikov.
		chatFrame.pack();
		chatFrame.setSize(700,400);
		chatFrame.setVisible(true); // Prikaže okno.
	}

}
