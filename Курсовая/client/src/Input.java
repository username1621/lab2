import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class Input {
    static JLabel l1,loginLable, passwordLable;
    static JTextField loginField;
    static JPasswordField p1;
    public static void Inp(String host, int port) {

        JFrame frame1 = new JFrame("Input");
        l1 = new JLabel("Вход");
        l1.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        l1.setBounds(15, 10, 200, 30);
        frame1.add(l1);

        loginLable = new JLabel("Логин:");
        loginLable.setBounds(15, 40, 200, 30);
        frame1.add(loginLable);
        loginField = new JTextField();
        loginField.setBounds(15, 70, 200, 30);
        frame1.add(loginField);


        passwordLable= new JLabel("Пароль:");
        passwordLable.setBounds(15, 100, 200, 30);
        frame1.add(passwordLable);
        p1 = new JPasswordField();
        p1.setBounds(15, 130, 200, 25);
        frame1.add(p1);
        JButton buttonRegistration = new JButton("вход");
        buttonRegistration.setBounds(15, 175, 200, 35);
        buttonRegistration.addActionListener(e -> {
            String login = loginField.getText().replaceAll("\\s", "");;
            char[] s3 = p1.getPassword();
            String password = new String(s3);
            if(password.equals("") | Objects.equals(login, "")){
                showMessageDialog(null, "Все поля должны быть заполнены");
            }else{
                try (Socket socket = new Socket(host, port)){
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.println("false");
                    out.println("false");
                    out.println(login);
                    out.println(password);
                    out.flush();
                    if(Objects.equals(in.readLine(), "false")){
                        showMessageDialog(frame1, "Not exsist");

                    }else{
                        showMessageDialog(frame1, "exsist");

                        TimeClient.syncButton.setEnabled(true);
                        frame1.setVisible(false);
                        frame1.getContentPane().removeAll();
                        out.close();
                        in.close();
                    }

                } catch (IOException e1) {
                    showMessageDialog(null, e1.getMessage());
                }
            }
        });
        buttonRegistration.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        frame1.add(buttonRegistration);
        frame1.setSize(250, 275);
        frame1.setLayout(null);
        frame1.setVisible(true);
    }
}
