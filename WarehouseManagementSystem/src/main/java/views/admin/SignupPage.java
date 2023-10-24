package views.admin;

import javax.swing.*;
import java.awt .*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupPage {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public SignupPage(CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    JPanel createSignupPage() {
        JPanel signupPanel = new JPanel();


        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sign-up logic
                // If sign-up is successful, switch to the Login page
                cardLayout.show(cardPanel, "Login");
            }
        });

        return signupPanel;
    }
}
