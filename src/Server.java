import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends JFrame {
    ServerSocket server ;
    Socket socket;
    BufferedReader b;
    PrintWriter p;
    private JLabel Server;
    private JTextArea message;
    private JTextField field;
    private JScrollPane scroll;

    Server(){
        try {
            server = new ServerSocket(7773);
            System.out.println("server is ready to acceot connection");
            System.out.println("waiting...");
            socket = server.accept();
            b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            p = new PrintWriter(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        createGui();
        handling();
         startReading();
     // startWriting();
    }

    private void handling() {
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String content = field.getText();
                    message.append("Me : " + content+"\n");
                    p.println(content);
                    p.flush();
                    field.setText("");
                }
            }
        });
    }

    private void createGui() {
        BorderLayout b4 = new BorderLayout();
        this.setLayout(b4);
        this.setSize(600,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Heading done
        Server = new JLabel(" Chat Application Server End ");
        Font f = new Font("Arial",Font.BOLD,20);
        Server.setFont(f);
        Server.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(Server,BorderLayout.NORTH);

//        ImageIcon i =new ImageIcon("op.png");
//        JLabel j1 = new JLabel(i);
//        j1.setSize(300,300);
//        j1.setVisible(true);
//        this.add(j1);
        // text areaC:\Users\dell\IdeaProjects\JAVA_DEVELOPER_PROJECTS\Project_2\src
        message = new JTextArea();
        scroll=new JScrollPane(message);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setVisible(true);
        this.add(scroll);
//        this.add(message);
        message.setEditable(false);

        // textfield
        field = new JTextField("type here ");
        field.setFont(f);
        this.add(field,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    public void startReading(){
        Runnable r1 =()->{ try {
            System.out.println("Reader strated..");
            while (true) {
                String msg = b.readLine();
                if (msg.equals("exit")) {
                    JOptionPane.showMessageDialog(this,"client terminated the chat");
                    socket.close();
                    break;
                }
                message.append("Client : "+msg+"\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r1).start();
    }
//    public void startWriting(){
//        Runnable r2 =()->{
//            System.out.println("Writer is started : ");
//            try {
//                while (true){
//                    handling();
//                    System.out.println();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        };
//        new Thread(r2).start();
   // }
    public static void main(String[] args) {
        System.out.println("Server is started...");
        new Server();
    }

}
