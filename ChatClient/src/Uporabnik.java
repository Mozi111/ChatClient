import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Uporabnik {
	private String username;
	private Date lastActive;

	private Uporabnik() {}

	public Uporabnik(String username, Date lastActive) {
		this.username = username;
		this.lastActive = lastActive;
	}

	@Override
	public String toString() {
		long trenuten_cas = new Date().getTime();
		long zadnje_aktiven = lastActive.getTime();
		long razlika = trenuten_cas - zadnje_aktiven;
		long minute = TimeUnit.MILLISECONDS.toMinutes(razlika);
		String aktiven;
		if (minute < 5) {
			aktiven = "Aktiven"; // Èe je bil uporabnik aktiven v roku zadnjih 5 min.
		} else {
			aktiven = "Neaktiven"; // Èe ni bil.
		}
		return username + " - " + aktiven;
	}

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("last_active")
	public Date getLastActive() {
		return this.lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
}
