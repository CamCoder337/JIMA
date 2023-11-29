package fr.jima.view;

import fr.jima.view.components.Combobox;
import fr.jima.view.components.TextField;
import fr.jima.view.components.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class View<E> extends JFrame {

    private static final long serialVersionUID = -7856952188558243410L;

    private JPanel contentPane;

//    private JTextField inputArea;
    private TextField inputArea;

    private JList<String> usersList;

    private JTextPane outputArea;

    private JLabel userNameLabel;

//    private JComboBox<E> cypherSelector;

    private Combobox<E> cypherSelector;

    private Button sendButton;

    private DefaultListModel<String> usersListModel;

    private DefaultComboBoxModel<E> cypherModel;


    /**
     * Create the frame.
     */
    public View() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 580, 461);
        contentPane = new JPanel();
        contentPane.setBackground(Color.lightGray);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        this.setBackground(Color.GRAY);

        inputArea = new TextField();
        inputArea.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();

        JScrollPane scrollPane_1 = new JScrollPane();

        userNameLabel = new JLabel("UserName");
        userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        userNameLabel.setIcon(new ImageIcon(View.class.getResource("/fr/jima/assets/user_icon.png")));

//        cypherSelector = new JComboBox<E>();
        cypherSelector = new Combobox<E>();
        cypherModel = new DefaultComboBoxModel<E>();
        cypherSelector.setModel(cypherModel);

//        sendButton = new JButton("Envoyer");
        sendButton = new Button("Send",Color.decode("#808080"),Color.white, 0, 0);
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(10)
                                .addComponent(inputArea, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cypherSelector, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(userNameLabel, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(userNameLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cypherSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(inputArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sendButton)))
        );

        usersList = new JList<String>();
        usersList.setModel(usersListModel = new DefaultListModel<String>());
        scrollPane_1.setViewportView(usersList);

        outputArea = new JTextPane();
        outputArea.setEditable(false);
        scrollPane.setViewportView(outputArea);
        contentPane.setLayout(gl_contentPane);
    }

    public JTextField getInputArea() {
        return inputArea;
    }

    public JList<String> getUsersList() {
        return usersList;
    }

    public DefaultListModel<String> getUsersListModel() {
        return usersListModel;
    }

    public JTextPane getOutputArea() {
        return outputArea;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public void appendOutput(final String txt) {
        // On s'assure que le lancement de cette m�thode se fasse dans l'EDT
        if (!EventQueue.isDispatchThread()) {
            // On r��xecute la m�thode dans l'EDT
            //EventQueue.invokeLater(() -> appendOutput(txt));
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    appendOutput(txt);
                }
            });
            // On arr�te l� ce traitement
            return;
        }
        outputArea.setText(outputArea.getText() + txt + "\n");
    }

    public JLabel getUserNameLabel() {
        return userNameLabel;
    }

    public DefaultComboBoxModel<E> getCypherModel() {
        return cypherModel;
    }

    public JComboBox<E> getCypherSelector() {
        return cypherSelector;
    }

}
