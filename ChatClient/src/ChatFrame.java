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

	private JTextPane message_output; // Okno s sporoèili
	private JScrollPane output_drsnik; // Drsnik za okno s sporoèili
	private StyledDocument glavno_okno; // Okno za javna sporoèila
	private StyledDocument trenutno_okno; // Okno, ki je trenutno prikazano
	private Map<String, StyledDocument> okna = new HashMap<String,StyledDocument>(); // Slovar, ki hrani zasebna sporoèila za vsakega uporabnika
	private JTextField message_input; // Polje za vnos sporoèila
	private JTextField vzdevek_input; // Polje za vnos uporabniškega imena
	private JPanel vzdevek_panel;
	private JLabel vzdevek_napis; // Napis pred poljem za vnos.
	private JLabel status; // Napis, ki nam prikazuje trenuten status (prijavljeni ali ne)
	private JPanel status_panel;
	private JButton prejemnik; // Gumb, ki izpisuje trenutnega prejemnika in ga lahko uporabimo, da preidemo nazaj na javna sporoèila
	private JPanel prejemnik_panel;
	private JButton prijavi; // Gumb za prijavo
	private JButton odjavi; // Gumb za odjavo
	public JList<Uporabnik> seznam_uporabnikov; // Seznam prijavljenih uporabnikov
	private JScrollPane uporabniki_drsnik; // Drsnik za seznam prijavljenih uporabnikov
	public String ime; // Naše ime (pod katerim smo prijavljeni)
	public String link_uporabniki; // Povezava (na kateri so uporabniki)
	public String link_sporocila; // Povezava (na kateri so sporoèila)

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
		vzdevek_panel_constraints.gridwidth = 2;
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
		status_panel_constraints.gridwidth = 2;
		status_panel_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(status_panel, status_panel_constraints);

		status = new JLabel(); // Napis, ki nam prikazuje trenuten status (prijavljeni ali ne)
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
						Klient.odjavi(link_uporabniki, ime); // Odjavimo prejšnje ime, èe smo bili prijavljeni
					}
					ime = vzdevek_input.getText();
					status.setText("Prijavljeni ste z imenom: " + ime); // Izpišemo v status.
					status.setForeground(Color.blue);
				} catch (HttpResponseException e1) {
					if (e1.getLocalizedMessage().equals("Forbidden")) { // Uporabnik s tem imenom že obstaja
						if (ime.equals("")) {
							status.setText("Uporabnik s tem imenom že obstaja. Niste prijavljeni.");
						} else {
							status.setText(
									"Uporabnik s tem imenom že obstaja. Prijavljeni ste z imenom: " + ime);
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
					if (!ime.equals("")){ // Preverimo, èe sploh smo prijavljeni
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
	    
		message_output = new JTextPane(); // Okno s sporoèili
		output_drsnik = new JScrollPane(message_output); // Drsnik za okno s sporoèili
		message_output.setEditable(false);
		GridBagConstraints output_constraints = new GridBagConstraints();
		output_constraints.gridx = 0;
		output_constraints.gridy = 2;
		output_constraints.weighty = 1;
		output_constraints.weightx = 0.7;
		output_constraints.fill = GridBagConstraints.BOTH;
		pane.add(output_drsnik, output_constraints);
		glavno_okno = message_output.getStyledDocument();
		trenutno_okno = glavno_okno;

		seznam_uporabnikov = new JList<Uporabnik>(); // Seznam prijavljenih uporabnikov
		uporabniki_drsnik = new JScrollPane(seznam_uporabnikov); // Drsnik za seznam prijavljenih uporabnikov
		GridBagConstraints prijavljeni_uporabniki_constraints = new GridBagConstraints();
		prijavljeni_uporabniki_constraints.gridx = 1;
		prijavljeni_uporabniki_constraints.gridy = 2;
		prijavljeni_uporabniki_constraints.weighty = 1;
		prijavljeni_uporabniki_constraints.weightx = 0.3;
		prijavljeni_uporabniki_constraints.fill = GridBagConstraints.BOTH;
		pane.add(uporabniki_drsnik, prijavljeni_uporabniki_constraints);

		seznam_uporabnikov.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<Uporabnik> list = (JList<Uporabnik>) evt.getSource();
				if (list.getSelectedValue() != null) { //Preverimo, da nismo kliknili v prazno
					prejemnik.setText("Prejemnik: " + ((Uporabnik) list.getSelectedValue()).getUsername());
					trenutno_okno = okna.get(list.getSelectedValue().getUsername());
					if(trenutno_okno == null){ // Èe okno za tega uporabnika še ne obstaja, ga naredimo
						StyledDocument zasebno_okno = new DefaultStyledDocument();
						okna.put(list.getSelectedValue().getUsername(), zasebno_okno);
						trenutno_okno = zasebno_okno;
					}
					message_output.setStyledDocument(trenutno_okno); // Odpremo okno za zasebna sporoèila z izbranim uuporabnikom.
				}
			}
		});

		message_input = new JTextField(40); // Polje za vnos sporoèila
		GridBagConstraints input_constraints = new GridBagConstraints();
		input_constraints.gridx = 0;
		input_constraints.gridy = 3;
		input_constraints.weightx = 1;
		input_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(message_input, input_constraints);
		message_input.addKeyListener(this);

		prejemnik_panel = new JPanel();
		FlowLayout prejemnik_panel_layout = new FlowLayout();
		prejemnik_panel.setLayout(prejemnik_panel_layout);
		GridBagConstraints prejemnik_panel_constraints = new GridBagConstraints();
		prejemnik_panel_constraints.gridx = 1;
		prejemnik_panel_constraints.gridy = 3;
		prejemnik_panel_constraints.weightx = 0.3;
		prejemnik_panel_constraints.fill = GridBagConstraints.HORIZONTAL;
		pane.add(prejemnik_panel, prejemnik_panel_constraints);

		prejemnik = new JButton("Prejemnik: Vsi"); // Gumb, ki izpisuje trenutnega prejemnika in ga lahko uporabimo, da preidemo nazaj na javna sporoèila
		prejemnik_panel.add(prejemnik);
		prejemnik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prejemnik.setText("Prejemnik: Vsi");
				trenutno_okno = glavno_okno;
				message_output.setStyledDocument(trenutno_okno); // Odpremo nazaj glavno okno
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				message_input.requestFocus(); // Polje za vnos sporoèila ima fokus, ko se okno odpre
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if (!ime.equals("")) { // Èe okno zapremo, nas odjavimo
						Klient.odjavi(link_uporabniki, ime);
						ime = "";
					}
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ChitChat.glavna_fukncija.deactivate(); // Ustavimo program, ki osvežuje sporoèila in seznam uporabnikov
			}
		});
		addMessage("Help",
				"Pozdravljeni! Prosim vpišite zaželjeno uporabniško ime in se prijavite. Èe želite poslati zasebno sporoèilo uporabniku, kliknite nanj"
						+ " v seznamu na desni. Èe želite, da se za prejemnika zopet doloèi vse, kliknite na gumb pod seznamom uporabnikov. Sporoèilo vpišite v spodnje polje in pritisnite enter."
						+ " Veliko zabave! (NAMIG: Poskusite besedilo konèati z /r, /g, /y ali /b.)",
				new Date(), true, ""); // Objavi zaèetni pozdrav in kratka navodila.
	}

	/**
	 * @param person
	 *            - the person sending the message
	 * @param message
	 *            - the message content
	 * @throws BadLocationException 
	 */
	public void addMessage(String person, String message, Date poslano_ob, Boolean javno, String prejemnik) throws BadLocationException {
		Calendar koledar = Calendar.getInstance();
		koledar.setTime(poslano_ob);
		int ure = koledar.get(Calendar.HOUR_OF_DAY);
		int minute = koledar.get(Calendar.MINUTE);
		String cas = String.format("%d:%02d", ure, minute); // Uporabimo koledar, da bomo lahko izpisali èas, ob katerem je bilo sporoèilo poslano
		StyleContext context = new StyleContext();
		Style obicajen_stil = context.getStyle(StyleContext.DEFAULT_STYLE);
	    StyleConstants.setAlignment(obicajen_stil, StyleConstants.ALIGN_RIGHT);
	    StyleConstants.setFontSize(obicajen_stil, 12);
	    StyleConstants.setSpaceAbove(obicajen_stil, 4);
	    StyleConstants.setSpaceBelow(obicajen_stil, 4); // Doloèimo stil besedila, v katerem bodo izpisana sporoèila.
		StyledDocument doc;
	    if (javno) {
	    	doc = glavno_okno; // Èe je sporoèilo javno, ga bomo izpisali v glavno okno
		} else {
			doc = trenutno_okno; // Èe sporoèilo ni javno, ga bomo izpisali v zasebno okno
			if (!person.equals(ime)){
			    StyleConstants.setForeground(obicajen_stil, Color.red);
				StyledDocument alert = glavno_okno; // Èe sporoèila nismo poslali sami, se nam v glavnem oknu izpiše obvestilo, da smo prejeli zasebno sporoèilo
				alert.insertString(alert.getLength(), "Dobili ste sporoèilo od " + person +"!\n", obicajen_stil);
			    StyleConstants.setForeground(obicajen_stil, Color.black);
				doc = okna.get(person);
			}
			if(doc == null){ // Èe zasebno okno še ne obstaja, ga naredimo.
				StyledDocument zasebno_okno = new DefaultStyledDocument();
				okna.put(person, zasebno_okno);
				doc = zasebno_okno;
			}
		}
	    String prvi_del = "[" + cas + "] "; // Prvi del sporoèila, ki ga bomo objavili (èas)
		doc.insertString(doc.getLength(), prvi_del, obicajen_stil);
	    StyleConstants.setForeground(obicajen_stil, Color.blue); // Ime uporabnika bomo objavili v modri barvi
		doc.insertString(doc.getLength(), person, obicajen_stil);
		String ukaz = message.substring(message.length()-2); // Èe smo besedilo konèali z doloèenim ukazom, ga izvršimo
		Color barva = Color.black;
		if (ukaz.equals("/r")){
			barva = Color.red;
			message = message.substring(0, message.length()-2);
		} else if (ukaz.equals("/b")){
			barva = Color.blue;
			message = message.substring(0, message.length()-2);
		} else if (ukaz.equals("/g")){
			barva = Color.green;
			message = message.substring(0, message.length()-2);
		} else if (ukaz.equals("/y")){
			barva = Color.yellow;
			message = message.substring(0, message.length()-2);
		}
	    StyleConstants.setForeground(obicajen_stil, barva);
		String drugi_del = ": " + message + "\n"; // Drugi del sporoèila, ki ga bomo objavili (sporoèilo samo) v želeni barvi
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
			if (e.getKeyChar() == '\n') { // Ko pritisnemo Enter, želimo poslati sporoèilo
				if (!ime.equals("") && !this.message_input.getText().equals("")) { // Preverimo, da je uporabnik nekaj napisal in je prijavljen
					try {
						String oseba_prejemnik = prejemnik.getText().substring("Prejemnik: ".length(),
								prejemnik.getText().length()); // Izrazimo prejemnika
						Boolean javno; // Izrazimo, èe je sporoèilo javno ali zasebno
						if (prejemnik.getText().equals("Prejemnik: Vsi")) {
							javno = true;
						} else {
							javno = false;
						}
						Klient.poslji_sporocilo(link_sporocila, ime, this.message_input.getText(), javno,
								oseba_prejemnik); // Pošljemo sporoèilo
						this.addMessage(ime, this.message_input.getText(), new Date(), javno, oseba_prejemnik);
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.message_input.setText(""); // Izbrišemo tekst iz vnosnega polja
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
