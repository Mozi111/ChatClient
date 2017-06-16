import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sporocilo {
	private Boolean global;
	private String recipient;
	private String sender;
	private String text;
	private Date sentAt;

	private Sporocilo() {}

	/**
	 * @param global
	 * @param text
	 */
	public Sporocilo(Boolean global, String text) {
		super();
		this.global = global;
		this.text = text;
	}

	/**
	 * @param global
	 * @param recipient
	 * @param text
	 */
	public Sporocilo(Boolean global, String recipient, String text) {
		super();
		this.global = global;
		this.recipient = recipient;
		this.text = text;
	}

	/**
	 * @param global
	 * @param recipient
	 * @param sender
	 * @param text
	 * @param sentAt
	 */
	public Sporocilo(Boolean global, String recipient, String sender, String text, Date sentAt) {
		super();
		this.global = global;
		this.recipient = recipient;
		this.sender = sender;
		this.text = text;
		this.sentAt = sentAt;
	}

	@Override
	public String toString() {
		return "Sporocilo [global=" + global + ", recipient=" + recipient + ", sender=" + sender + ", text=" + text
				+ ", sent_at=" + sentAt + "]";
	}

	@JsonProperty("global")
	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@JsonProperty("sent_at")
	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

}
