import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.text.BadLocationException;

public class ChitChat {
	public static Sporocila robot2;

	public static void main(String[] args) throws IOException, URISyntaxException, BadLocationException {
		ChatFrame chatFrame = new ChatFrame();
		// PrimeRobot robot = new PrimeRobot(chatFrame);
		robot2 = new Sporocila(chatFrame);
		// robot.activate();
		robot2.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
