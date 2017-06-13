import java.util.Timer;
import java.util.TimerTask;

public class PrimeRobot extends TimerTask {
	private ChatFrame chat;
	private int k;
	
	private static boolean isPrime(int n) {
		for (int i = 2; i * i <= n; i++) {
			if (n % i == 0) { return false; }
		}
		return true;
	}

	public PrimeRobot(ChatFrame chat) {
		this.chat = chat;
		this.k = 2;
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
		if (isPrime(this.k)) {
			chat.addMessage("primer", Integer.toString(this.k) + " is prime");
		}
		this.k++;
	}
}
