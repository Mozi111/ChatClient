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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.client.utils.URIBuilder;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {

	private JTextArea output;
	private JTextField input;
	private JTextField vzdevek;
	private JPanel vzdevekpanel;
	private JLabel vzdeveknapis;
	private JLabel status;
	private JPanel statuspanel;
	private JScrollPane sp;
	private JButton prijavi;
	private JButton odjavi;
	private JPanel output_uporabniki;
	public JList prijavljeni_uporabniki;
	private JScrollPane sp2;
	public String prejsnjivzdevek;
	public String link;
	public String link_sporocila;

	public ChatFrame() throws URISyntaxException {
		super();
		this.setMinimumSize(new Dimension(700,400));
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
					if (Klient.imena_uporabnikov(Klient.seznam_uporabnikov(link)).contains(prejsnjivzdevek)) {
						Klient.odjavi(link, prejsnjivzdevek);
					}
					if (vzdevek.getText().equals("")) {
						vzdevek.setText(System.getProperty("user.name"));
					}
					prejsnjivzdevek = vzdevek.getText();
					Klient.prijavi(link, prejsnjivzdevek);
					status.setText("Prijavljeni ste z imenom: " + prejsnjivzdevek);
					status.setForeground(Color.blue);
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
					Klient.odjavi(link, prejsnjivzdevek);
					status.setText("Niste prijavljeni.");
					status.setForeground(Color.red);
					prejsnjivzdevek = "";
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
				System.out.println("Odjavili ste se");
			}
		});
		vzdevekpanel.add(odjavi);
		
		this.output = new JTextArea(20, 30);
		this.sp = new JScrollPane(output);
		this.output.setEditable(false);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 2;
		outputConstraint.weighty = 1;
		outputConstraint.weightx = 1;
		outputConstraint.fill = GridBagConstraints.BOTH;
		pane.add(sp, outputConstraint);
		
		this.prijavljeni_uporabniki = new JList();
		this.sp2 = new JScrollPane(prijavljeni_uporabniki);
		GridBagConstraints prijavljeni_uporabnikiConstraint = new GridBagConstraints();
		prijavljeni_uporabnikiConstraint.gridx = 1;
		prijavljeni_uporabnikiConstraint.gridy = 2;
		prijavljeni_uporabnikiConstraint.weighty = 0.3;
		prijavljeni_uporabnikiConstraint.weightx = 0.3;
		prijavljeni_uporabnikiConstraint.fill = GridBagConstraints.BOTH;
		pane.add(sp2, prijavljeni_uporabnikiConstraint);

		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 3;
		inputConstraint.gridwidth = 2;
		inputConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);

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
				System.out.println("Izhod");
			}
		});
	}

	/**
	 * @param person
	 *            - the person sending the message
	 * @param message
	 *            - the message content
	 */
	public void addMessage(String person, String message) {
		String chat = this.output.getText();

		this.output.setText(chat + person + ": " + message + "\n");
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
				if (!prejsnjivzdevek.equals("")) {
					this.addMessage(this.vzdevek.getText(), this.input.getText());
					try {
						Klient.poslji_sporocilo(link_sporocila, prejsnjivzdevek, this.input.getText(), true);
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
