import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {

	private JTextArea output;
	private JTextField input;
	private JTextField vzdevek;
	private JPanel vzdevekpanel;
	private JLabel vzdeveknapis;
	private JLabel status;
	private JButton prejemnik;
	private JPanel statuspanel;
	private JScrollPane sp;
	private JButton prijavi;
	private JButton odjavi;
	private JPanel prejemnikpanel;
	public JList<Uporabnik> prijavljeni_uporabniki;
	private JScrollPane sp2;
	public String prejsnjivzdevek;
	public String link;
	public String link_sporocila;

	public ChatFrame() throws URISyntaxException {
		super();
		this.setMinimumSize(new Dimension(700, 400));
		link = "http://chitchat.andrej.com/users";
		link_sporocila = "http://chitchat.andrej.com/messages";
		setTitle("Facebook Messenger");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());

		this.vzdevekpanel = new JPanel();
		FlowLayout vzdevekpanelLayout = new FlowLayout(FlowLayout.CENTER);
		vzdevekpanel.setLayout(vzdevekpanelLayout);
		GridBagConstraints vzdevekpanelConstraint = new GridBagConstraints();
		vzdevekpanelConstraint.gridx = 0;
		vzdevekpanelConstraint.gridy = 0;
		vzdevekpanelConstraint.gridwidth = 2;
		vzdevekpanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(vzdevekpanel, vzdevekpanelConstraint);

		this.vzdeveknapis = new JLabel();
		vzdeveknapis.setText("Vzdevek: ");
		vzdevekpanel.add(vzdeveknapis);

		this.statuspanel = new JPanel();
		FlowLayout statuspanelLayout = new FlowLayout(FlowLayout.CENTER);
		statuspanel.setLayout(statuspanelLayout);
		GridBagConstraints statuspanelConstraint = new GridBagConstraints();
		statuspanelConstraint.gridx = 0;
		statuspanelConstraint.gridy = 1;
		statuspanelConstraint.gridwidth = 2;
		statuspanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(statuspanel, statuspanelConstraint);

		this.status = new JLabel();
		status.setText("Niste prijavljeni.");
		status.setForeground(Color.red);
		prejsnjivzdevek = "";
		statuspanel.add(status);

		this.vzdevek = new JTextField(40);
		this.vzdevek.setText(System.getProperty("user.name"));
		vzdevekpanel.add(vzdevek);
		vzdevek.addKeyListener(this);

		this.prijavi = new JButton();
		prijavi.setText("Prijava");
		prijavi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (vzdevek.getText().equals("")) {
						vzdevek.setText(System.getProperty("user.name"));
					}
					Klient.prijavi(link, vzdevek.getText());
					if (Klient.imena_uporabnikov(Klient.seznam_uporabnikov(link)).contains(prejsnjivzdevek)) {
						Klient.odjavi(link, prejsnjivzdevek);
					}
					prejsnjivzdevek = vzdevek.getText();
					status.setText("Prijavljeni ste z imenom: " + prejsnjivzdevek);
					status.setForeground(Color.blue);
				} catch (HttpResponseException e1) {
					System.out.println("Nekaj je narobe pri prijavi!");
					if (e1.getLocalizedMessage().equals("Forbidden")) {
						if (prejsnjivzdevek.equals("")) {
							status.setText("Uporabnik s tem imenom že obstaja. Niste prijavljeni.");
						} else {
							status.setText(
									"Uporabnik s tem imenom že obstaja. Prijavljeni ste z imenom: " + prejsnjivzdevek);
						}
						status.setForeground(Color.blue);
					}
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Prijavili ste se");
			}
		});
		vzdevekpanel.add(prijavi);

		this.odjavi = new JButton();
		odjavi.setText("Odjava");
		odjavi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String odziv = Klient.odjavi(link, prejsnjivzdevek);
					status.setText("Niste prijavljeni.");
					status.setForeground(Color.red);
					prejsnjivzdevek = "";
				} catch (HttpResponseException e1) {
					//System.out.println("Nekaj je narobe pri odjavi!");
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		vzdevekpanel.add(odjavi);

		this.output = new JTextArea(20, 30);
		this.sp = new JScrollPane(output);
		this.output.setEditable(false);
		this.output.setLineWrap(true);
		this.output.setWrapStyleWord(true);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 2;
		outputConstraint.weighty = 1;
		outputConstraint.weightx = 1;
		outputConstraint.fill = GridBagConstraints.BOTH;
		pane.add(sp, outputConstraint);

		this.prijavljeni_uporabniki = new JList<Uporabnik>();
		this.sp2 = new JScrollPane(prijavljeni_uporabniki);
		GridBagConstraints prijavljeni_uporabnikiConstraint = new GridBagConstraints();
		prijavljeni_uporabnikiConstraint.gridx = 1;
		prijavljeni_uporabnikiConstraint.gridy = 2;
		prijavljeni_uporabnikiConstraint.weighty = 0.3;
		prijavljeni_uporabnikiConstraint.weightx = 0.3;
		prijavljeni_uporabnikiConstraint.fill = GridBagConstraints.BOTH;
		pane.add(sp2, prijavljeni_uporabnikiConstraint);

		prijavljeni_uporabniki.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Uporabnik> list = (JList<Uporabnik>) evt.getSource();
				System.out.println("Clicked on the list");
				System.out.println(list.getSelectedValue());
				if (list.getSelectedValue() != null) {
					prejemnik.setText("Prejemnik: " + ((Uporabnik) list.getSelectedValue()).getUsername());
				}
			}
		});

		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 3;
		inputConstraint.weightx = 1;
		inputConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);

		this.prejemnikpanel = new JPanel();
		FlowLayout prejemnikpanelLayout = new FlowLayout();
		prejemnikpanel.setLayout(prejemnikpanelLayout);
		GridBagConstraints prejemnikpanelConstraint = new GridBagConstraints();
		prejemnikpanelConstraint.gridx = 1;
		prejemnikpanelConstraint.gridy = 3;
		prejemnikpanelConstraint.weightx = 0.3;
		prejemnikpanelConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(prejemnikpanel, prejemnikpanelConstraint);

		this.prejemnik = new JButton("Prejemnik: Vsi");
		prejemnikpanel.add(prejemnik);
		prejemnik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prejemnik.setText("Prejemnik: Vsi");
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				input.requestFocus();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if (!prejsnjivzdevek.equals("")) {
						Klient.odjavi(link, prejsnjivzdevek);
						prejsnjivzdevek = "";
					}
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ChitChat.robot2.deactivate();
				System.out.println("Izhod");
			}
		});
		this.addMessage("Help",
				"Pozdravljeni! Prosim vpišite zaželjeno uporabniško ime in se prijavite. Èe želite poslati zasebno sporoèilo uporabniku, kliknite nanj"
						+ " v seznamu na desni. Èe želite sporoèilo nameniti vsem, kliknite na gumb pod seznamom uporabnikov. Sporoèilo vpišite v spodnje polje in pritisnite enter."
						+ " Veliko zabave!",
				new Date(), true, "");
	}

	/**
	 * @param person
	 *            - the person sending the message
	 * @param message
	 *            - the message content
	 */
	public void addMessage(String person, String message, Date poslano_ob, Boolean javno, String prejemnik) {
		String chat = this.output.getText();
		Calendar koledar = Calendar.getInstance();
		koledar.setTime(poslano_ob);
		int ure = koledar.get(Calendar.HOUR_OF_DAY);
		int minute = koledar.get(Calendar.MINUTE);
		String cas = ure + ":" + minute;
		String javno_string;
		if (javno) {
			javno_string = "javno";
		} else {
			javno_string = "za: " + prejemnik;
		}
		this.output.setText(chat + "[" + cas + "] " + person + ": " + message + " (" + javno_string + ")\n");
	}

	public JTextArea getOutput() {
		return output;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				if (!prejsnjivzdevek.equals("") && !this.input.getText().equals("")) {
					try {
						String oseba_prejemnik = prejemnik.getText().substring("Prejemnik: ".length(),
								prejemnik.getText().length());
						Boolean javno;
						if (prejemnik.getText().equals("Prejemnik: Vsi")) {
							javno = true;
						} else {
							javno = false;
						}
						Klient.poslji_sporocilo(link_sporocila, prejsnjivzdevek, this.input.getText(), javno,
								oseba_prejemnik);
						this.addMessage(prejsnjivzdevek, this.input.getText(), new Date(), javno, oseba_prejemnik);
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.input.setText("");
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
