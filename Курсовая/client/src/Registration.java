import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;


import static javax.swing.JOptionPane.showMessageDialog;

public class Registration {
    static JLabel l1,loginLable, passwordLable;
    static JTextField loginField;
    static JPasswordField p1, p2;
    public static void Registr(String host, int port) {

        JFrame frame1 = new JFrame("Registration");
        l1 = new JLabel("Регистрация");
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

        passwordLable= new JLabel("Подтвердить пароль:");
        passwordLable.setBounds(15, 160, 200, 30);
        frame1.add(passwordLable);
        p2 = new JPasswordField();
        p2.setBounds(15, 190, 200, 25);
        frame1.add(p2);

        JButton buttonRegistration = new JButton("зарегистрироваться");
        buttonRegistration.setBounds(15, 235, 200, 35);
        buttonRegistration.addActionListener(e -> {
            String login = loginField.getText().replaceAll("\\s", "");;
            char[] s3 = p1.getPassword();
            char[] s4 = p2.getPassword();
            String s8 = new String(s3);
            String s9 = new String(s4);
            if(s8.equals("") | s9.equals("") | Objects.equals(login, "")){
                showMessageDialog(null, "Все поля должны быть заполнены");
            }else{
                if(s8.equals(s9)){
                    try (Socket socket = new Socket(host, port)){

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        out.println("true");
                        out.println("true");
                        out.println(login);
                        out.println(s8);
                        out.flush();

                        if(Objects.equals(in.readLine(), "Registration successful")){
                            showMessageDialog(frame1, "Registration successful");
                            frame1.setVisible(false);
                            frame1.getContentPane().removeAll();
                            TimeClient.syncButton.setEnabled(true);

                        }else{
                            showMessageDialog(frame1,"Registration faild");

                        }
                        out.close();
                        in.close();
                    } catch (IOException e1) {
                        showMessageDialog(null, e1.getMessage());
                    }
                }else{
                    showMessageDialog(null, "Пароли должны совпадать");
                }
            }
        });
        buttonRegistration.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        frame1.add(buttonRegistration);
        frame1.setSize(250, 340);
        frame1.setLayout(null);
        frame1.setVisible(true);
    }

}
