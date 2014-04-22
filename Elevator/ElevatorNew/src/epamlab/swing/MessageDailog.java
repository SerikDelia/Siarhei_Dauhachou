package epamlab.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageDailog extends JPanel {
	private static JTextArea textArea = new JTextArea(300, 25);

	public MessageDailog() {
		Dimension size = new Dimension(300, 25);
		setLayout(new BorderLayout());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrol = new JScrollPane(textArea);
		add(scrol, BorderLayout.CENTER);
		setSize(size);

		// TODO Auto-generated constructor stub
	}

	public static void doWriteIntoTextArea(String textForTextArea) {

		textArea.append(textForTextArea);
	}

}
