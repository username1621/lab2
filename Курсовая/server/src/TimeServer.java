import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
    private static JTextArea textArea;
    private static String url = "jdbc:postgresql://localhost:5432/users_data";
    private static String user = "postgres";
    private static String password = "";
    private static JFrame frame;
    private static Connection conn;
    
    public static void greateUI(){
        frame = new JFrame("Time Server");
        textArea = new JTextArea(20, 50);
        frame.add(new JScrollPane(textArea));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        textArea.append("Сервер запущен. Ожидание клиентов..."+"\n");
    }
    public static void main(String[] args) throws SQLException, IOException {
        greateUI();
        ServerSocket serverSocket = new ServerSocket(12345);
        conn = DriverManager.getConnection(url, user, password);
        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket, conn).start();
        }
    }
    static class ClientHandler extends Thread {
        private Socket socket;
        private Connection conn;
        public ClientHandler(Socket socket, Connection conn) {
            this.socket = socket;
            this.conn = conn;
        }
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                if(in.readLine().equals("TIME")){
                    out.println(new SimpleDateFormat("HH:mm:ss"));
                    textArea.append("Клиент подключен. Отправлено время: " + new Date() + "\n");
                }
                else{
                String tmp = in.readLine();
                String login = in.readLine();
                String password = in.readLine();
                if(tmp.equals("true")){
                    if (!checkUser(login, password)) {
                        String sql = "INSERT INTO users(login, password) VALUES(?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, login);
                        stmt.setString(2, password);
                        stmt.executeUpdate();
                        out.println("Registration successful");
                        out.println(new Date().toString());
                        textArea.append("Клиент подключен. Отправлено время: " + new Date() + "\n");
                    } else {
                        out.println("Registration failed");}

                    }else{
                        if (!checkUser(login, password)) {out.println("false");}else{out.println("true");
                            }
                    }
                }
            } catch (IOException | SQLException e) {
                textArea.append(e.getMessage());
           }
        }
    }
    public static boolean checkUser(String log, String password) {
        boolean result = false;
        String Select = "select * from users where login = ? and password = ?";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(Select)) {
            pst.setString(1, log);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) result = true;
        }catch (SQLException ex) {
            textArea.append(ex.getMessage());
        }
        return result;
    }
}
