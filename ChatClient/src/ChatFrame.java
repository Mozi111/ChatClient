import java.awt.BorderLayout;
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {

	private JTextPane message_output; // Okno s sporočili
	private JScrollPane output_drsnik; // Drsnik za okno s sporočili
	private StyledDocument glavno_okno; // Okno za javna sporočila
	private StyledDocument trenutno_okno; // Okno, ki je trenutno prikazano
	private Map<String, StyledDocument> okna = new HashMap<String, StyledDocument>(); // Slovar, ki hrani zasebna sporočila za vsakega uporabnika
	private JTextField message_input; // Polje za vnos sporočila
	private JTextField vzdevek_input; // Polje za vnos uporabniškega imena
	private JPanel vzdevek_panel;
	private JLabel vzdevek_napis; // Napis pred poljem za vnos.
	private JLabel status; // Napis, ki nam prikazuje trenuten status
							// (prijavljeni ali ne)
	private JPanel status_panel;
	private JButton nazaj; // Gumb, ki ga lahko uporabimo, da preidemo nazaj na javna sporočila
	private JButton poslji; // Gumb za posiljanje sporočil
	private JLabel prejemnik_napis; // Napis, ki izpisuje trenutnega prejemnika
	private String prejemnik_ime; // Ime prejemnika
	private JPanel prejemnik_panel;
	private JPanel input_panel; // Srednji panel
	private JPanel output_panel; // Spodnji panel
	
	private JButton prijavi; // Gumb za prijavo
	private JButton odjavi; // Gumb za odjavo
	public JList<Uporabnik> seznam_uporabnikov; // Seznam prijavljenih uporabnikov
	private JScrollPane uporabniki_drsnik; // Drsnik za seznam prijavljenih uporabnikov
	public String ime; // Naše ime (pod katerim smo prijavljeni)
	public String link_uporabniki; // Povezava (na kateri so uporabniki)
	public String link_sporocila; // Povezava (na kateri so sporočila)

	public ChatFrame() throws URISyntaxException, BadLocationException {
		super();
		this.setMinimumSize(new Dimension(700, 400)); // Najmanjša velikost okna
		link_uporabniki = "http://chitchat.andrej.com/users";
		link_sporocila = "http://chitchat.andrej.com/messages";
		setTitle("Facebook Messenger"); // Naslov okna
		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());

		vzdevek_panel = new JPanel();
		FlowLayout vzdevek_panel_layout = new FlowLayout(FlowLayout.CENTER);
		vzdevek_panel.setLayout(vzdevek_panel_layout);
		GridBagConstraints vzdevek_panel_constraints = new GridBagConstraints();
		vzdevek_panel_constraints.gridx = 0;
		vzdevek_panel_constraints.gridy = 0;
		vzdevek_panel_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(vzdevek_panel, vzdevek_panel_constraints);

		vzdevek_napis = new JLabel(); // Napis pred poljem za vnos.
		vzdevek_napis.setText("Vzdevek: ");
		vzdevek_panel.add(vzdevek_napis);

		status_panel = new JPanel();
		FlowLayout status_panel_layout = new FlowLayout(FlowLayout.CENTER);
		status_panel.setLayout(status_panel_layout);
		GridBagConstraints status_panel_constraints = new GridBagConstraints();
		status_panel_constraints.gridx = 0;
		status_panel_constraints.gridy = 1;
		status_panel_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(status_panel, status_panel_constraints);

		status = new JLabel(); // Napis, ki nam prikazuje trenuten status
								// (prijavljeni ali ne)
		status.setText("Niste prijavljeni.");
		status.setForeground(Color.red);
		ime = "";
		status_panel.add(status);

		vzdevek_input = new JTextField(40); // Polje za vnos uporabniškega imena
		vzdevek_input.setText(System.getProperty("user.name"));
		vzdevek_panel.add(vzdevek_input);
		vzdevek_input.addKeyListener(this);

		prijavi = new JButton(); // Gumb za prijavo
		prijavi.setText("Prijava");
		prijavi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (vzdevek_input.getText().equals("")) { // Ne dovolimo praznega imena
						vzdevek_input.setText(System.getProperty("user.name")); // Nadomestimo ga z imenom iz sistema
					}
					Klient.prijavi(link_uporabniki, vzdevek_input.getText()); // Poskusimo prijaviti novo ime
					if (Klient.imena_uporabnikov(Klient.prijavljeni_uporabniki(link_uporabniki)).contains(ime)) {
						Klient.odjavi(link_uporabniki, ime); // Odjavimo prejšnje ime, če smo bili prijavljeni
					}
					ime = vzdevek_input.getText();
					status.setText("Prijavljeni ste z imenom: " + ime); // Izpišemo v status.
					status.setForeground(Color.blue);
				} catch (HttpResponseException e1) {
					if (e1.getLocalizedMessage().equals("Forbidden")) { // Uporabnik s tem imenom že obstaja
						if (ime.equals("")) {
							status.setText("Uporabnik s tem imenom že obstaja. Niste prijavljeni.");
						} else {
							status.setText("Uporabnik s tem imenom že obstaja. Prijavljeni ste z imenom: " + ime);
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
			}
		});
		vzdevek_panel.add(prijavi);

		odjavi = new JButton(); // Gumb za odjavo
		odjavi.setText("Odjava");
		odjavi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!ime.equals("")) { // Preverimo, če sploh smo
											// prijavljeni
						String odziv = Klient.odjavi(link_uporabniki, ime);
						status.setText("Niste prijavljeni.");
						status.setForeground(Color.red);
						ime = "";
					}
				} catch (HttpResponseException e1) {
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
		vzdevek_panel.add(odjavi);
		
		output_panel = new JPanel();
		BorderLayout output_panel_layout = new BorderLayout();
		output_panel.setLayout(output_panel_layout);
		GridBagConstraints output_panel_constraints = new GridBagConstraints();
		output_panel_constraints.gridx = 0;
		output_panel_constraints.gridy = 2;
		output_panel_constraints.weightx = 1;
		output_panel_constraints.weighty = 1;
		output_panel_constraints.fill = GridBagConstraints.BOTH;
		pane.add(output_panel, output_panel_constraints);
		
		message_output = new JTextPane(); // Okno s sporočili
		message_output.setPreferredSize(new Dimension(600, 400)); // Da se nam ne odpre preveliko okno.
		output_drsnik = new JScrollPane(message_output); // Drsnik za okno s sporočili
		message_output.setEditable(false);
		output_panel.add(output_drsnik, BorderLayout.CENTER);
		glavno_okno = message_output.getStyledDocument();
		trenutno_okno = glavno_okno;

		seznam_uporabnikov = new JList<Uporabnik>(); // Seznam prijavljenih uporabnikov
		uporabniki_drsnik = new JScrollPane(seznam_uporabnikov); // Drsnik za seznam prijavljenih uporabnikov
		output_panel.add(uporabniki_drsnik, BorderLayout.LINE_END);

		seznam_uporabnikov.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Uporabnik> list = (JList<Uporabnik>) evt.getSource();
				if (list.getSelectedValue() != null) { // Preverimo, da nismo
														// kliknili v prazno
					prejemnik_napis.setText("Zasebno");
					prejemnik_napis.setForeground(Color.blue);
					prejemnik_ime = list.getSelectedValue().getUsername();
					trenutno_okno = okna.get(prejemnik_ime);
					if (trenutno_okno == null) { // Če okno za tega uporabnika
													// še ne obstaja, ga naredimo
						StyledDocument zasebno_okno = new DefaultStyledDocument();
						okna.put(prejemnik_ime, zasebno_okno);
						trenutno_okno = zasebno_okno;
						try {
							addMessage("Help",
									"Odprli se zasebno okno za pogovor z uporabnikom " + prejemnik_ime + ". "
									+ "Če želite odpreti nazaj javna sporočila, kliknite na gumb \"Javna sporočila\" pod "
									+ "seznamom prijavljenih uporabnikov.",
									new Date(), false, "");
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					message_output.setStyledDocument(trenutno_okno); // Odpremo okno za zasebna
																		// sporočila z izbranim uporabnikom.
				}
			}
		});
		
		input_panel = new JPanel();
		BorderLayout input_panel_layout = new BorderLayout();
		input_panel.setLayout(input_panel_layout);
		GridBagConstraints input_panel_constraints = new GridBagConstraints();
		input_panel_constraints.gridx = 0;
		input_panel_constraints.gridy = 3;
		input_panel_constraints.weightx = 1;
		input_panel_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(input_panel, input_panel_constraints);
		
		message_input = new JTextField(); // Polje za vnos sporočila
		input_panel.add(message_input, BorderLayout.CENTER);
		message_input.addKeyListener(this);
		
		prejemnik_panel = new JPanel();
		FlowLayout prejemnik_panel_layout = new FlowLayout();
		prejemnik_panel.setLayout(prejemnik_panel_layout);
		input_panel.add(prejemnik_panel, BorderLayout.LINE_END);
		
		prejemnik_napis = new JLabel(); 
		prejemnik_napis.setText("Javno");
		prejemnik_napis.setForeground(Color.red);
		prejemnik_panel.add(prejemnik_napis);
		prejemnik_ime = "Vsi";
		
		poslji = new JButton("Pošlji");
		prejemnik_panel.add(poslji);
		poslji.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				poslji_input();
			}
		});
		
		nazaj = new JButton("Javna sporočila"); // Gumb, ki izpisuje
													// trenutnega prejemnika in
													// ga lahko uporabimo, da
													// preidemo nazaj na javna
													// sporočila
		prejemnik_panel.add(nazaj);
		nazaj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prejemnik_napis.setText("Javno");
				prejemnik_napis.setForeground(Color.red);
				prejemnik_ime = "Vsi";
				trenutno_okno = glavno_okno;
				message_output.setStyledDocument(trenutno_okno); // Odpremo nazaj glavno okno
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				message_input.requestFocus(); // Polje za vnos sporočila ima
												// fokus, ko se okno odpre
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if (!ime.equals("")) { // Če okno zapremo, nas odjavimo
						Klient.odjavi(link_uporabniki, ime);
						ime = "";
					}
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ChitChat.glavna_fukncija.deactivate(); // Ustavimo program, ki
														// osvežuje sporočila in
														// seznam uporabnikov
			}
		});
		addMessage("Help",
				"Pozdravljeni! Prosim vpišite zaželjeno uporabniško ime in se prijavite. Če želite poslati zasebno sporočilo uporabniku, kliknite nanj"
						+ " v seznamu na desni. Če želite odpreti nazaj javna sporočila, kliknite na gumb \"Javna sporočila\" pod seznamom uporabnikov. Sporočilo vpišite v spodnje polje in pritisnite enter"
						+ " ali kliknite na gumb \"Pošlji\"."
						+ " Veliko zabave! (NAMIG: Poskusite besedilo končati z /r, /g, /y ali /b.)",
				new Date(), true, ""); // Objavi začetni pozdrav in kratka
										// navodila.
	}

	/**
	 * @param person
	 *            - the person sending the message
	 * @param message
	 *            - the message content
	 * @throws BadLocationException
	 */
	public void addMessage(String person, String message, Date poslano_ob, Boolean javno, String prejemnik)
			throws BadLocationException {
		Calendar koledar = Calendar.getInstance();
		koledar.setTime(poslano_ob);
		int ure = koledar.get(Calendar.HOUR_OF_DAY);
		int minute = koledar.get(Calendar.MINUTE);
		String cas = String.format("%d:%02d", ure, minute); // Uporabimo koledar, da bomo lahko izpisali
															// čas, ob katerem je bilo sporočilo poslano
		StyleContext context = new StyleContext();
		Style obicajen_stil = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(obicajen_stil, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setFontSize(obicajen_stil, 12);
		StyleConstants.setSpaceAbove(obicajen_stil, 4);
		StyleConstants.setSpaceBelow(obicajen_stil, 4); // Določimo stil besedila, v katerem bodo izpisana
														// sporočila.
		StyledDocument doc;
		if (javno) {
			doc = glavno_okno; // Če je sporočilo javno, ga bomo izpisali v
								// glavno okno
		} else {
			doc = trenutno_okno; // Če sporočilo ni javno, ga bomo izpisali v
									// zasebno okno, ki smo ga odprli
			if (person.equals("Help") && !prejemnik.equals("")){
				doc = okna.get(prejemnik); // Če smo ustvarili nov dokument in želimo v njem objaviti pomoč
			}
			if (!person.equals(ime) && !person.equals("Help") && trenutno_okno != okna.get(person)) {
				StyleConstants.setForeground(obicajen_stil, Color.red);
				StyledDocument alert = glavno_okno; // Če sporočila nismo poslali sami, se nam v glavnem oknu izpiše
													// obvestilo, da smo prejeli zasebno sporočilo
				alert.insertString(alert.getLength(), "Dobili ste sporočilo od " + person + "!\n", obicajen_stil);
				StyleConstants.setForeground(obicajen_stil, Color.black);
				doc = okna.get(person);
				
			}
			if (doc == null) { // Če zasebno okno še ne obstaja, ga naredimo.
				StyledDocument zasebno_okno = new DefaultStyledDocument();
				okna.put(person, zasebno_okno);
				doc = zasebno_okno;
				addMessage("Help",
						"Odprli se zasebno okno za pogovor z uporabnikom " + person + ". "
						+ "Če želite odpreti nazaj javna sporočila, kliknite na gumb \"Javna sporočila\" pod "
						+ "seznamom prijavljenih uporabnikov.",
						new Date(), false, person);
			}
		}
		String prvi_del = "[" + cas + "] "; // Prvi del sporočila, ki ga bomo
											// objavili (čas)
		doc.insertString(doc.getLength(), prvi_del, obicajen_stil);
		if (person.equals(ime) || person.equals("Help")){
			StyleConstants.setForeground(obicajen_stil, Color.red); // Ime uporabnika in ime Help bomo izpisali v rdeči barvi
		} else {
			StyleConstants.setForeground(obicajen_stil, Color.blue); // Ime obicajnega posiljatelja bomo objavili v modri barvi
		}
		doc.insertString(doc.getLength(), person, obicajen_stil);
		String ukaz = message.substring(message.length() - 2); // Če smo besedilo končali z
																// določenim ukazom, ga izvršimo
		Color barva = Color.black;
		if (ukaz.equals("/r")) {
			barva = Color.red;
			message = message.substring(0, message.length() - 2);
		} else if (ukaz.equals("/b")) {
			barva = Color.blue;
			message = message.substring(0, message.length() - 2);
		} else if (ukaz.equals("/g")) {
			barva = Color.green;
			message = message.substring(0, message.length() - 2);
		} else if (ukaz.equals("/y")) {
			barva = Color.yellow;
			message = message.substring(0, message.length() - 2);
		}
		StyleConstants.setForeground(obicajen_stil, barva);
		String drugi_del = ": " + message + "\n"; // Drugi del sporočila, ki ga bomo objavili (sporočilo
													// samo) v želeni barvi
		doc.insertString(doc.getLength(), drugi_del, obicajen_stil);
	}

	public JTextPane getOutput() {
		return message_output;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.message_input) {
			if (e.getKeyChar() == '\n') { // Ko pritisnemo Enter, želimo poslati
											// sporočilo
				poslji_input();
			}
		}
	}
	
	public void poslji_input() {
		if (!ime.equals("") && !this.message_input.getText().equals("")) { // Preverimo, da je uporabnik nekaj napisal in je prijavljen
			try {
				Boolean javno; // Izrazimo, če je sporočilo javno ali zasebno
				if (prejemnik_ime.equals("Vsi")) {
					javno = true;
				} else {
					javno = false;
				}
				Klient.poslji_sporocilo(link_sporocila, ime, this.message_input.getText(), javno, prejemnik_ime); // Pošljemo																								// sporočilo
				addMessage(ime, message_input.getText(), new Date(), javno, prejemnik_ime);
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			message_input.setText(""); // Izbrišemo tekst iz vnosnega polja
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
