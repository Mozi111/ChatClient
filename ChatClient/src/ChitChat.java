import java.io.IOException;
import java.net.URISyntaxException;

public class ChitChat {

	public static void main(String[] args) throws IOException, URISyntaxException {
		ChatFrame chatFrame = new ChatFrame();
		//PrimeRobot robot = new PrimeRobot(chatFrame);
		Sporocila robot2 = new Sporocila(chatFrame);
		//robot.activate();
		robot2.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
