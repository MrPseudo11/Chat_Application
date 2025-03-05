import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    Socket socket;
    BufferedReader b;
    PrintWriter p;
    private JLabel client;
    private JTextArea message;
    private JTextField field;
    private JScrollPane scroll;

    Client(){
        try {
            System.out.println("Sending request to server ");
            socket = new Socket("172.30.10.105",7773);
            System.out.println("Connection is done");
            b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            p = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        createGui();
        handeling();
        startReading();
        //startWriting();
    }

    private void handeling() {
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
               if(e.getKeyCode()==10){
                   String cont = field.getText();
                   message.append("Me : "+cont+"\n");
                   p.println(cont);
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
        client = new JLabel(" Chat Application : Client end  ");
        Font f = new Font("Arial",Font.BOLD,20);
        client.setFont(f);
        client.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(client,BorderLayout.NORTH);

        // text area
        message = new JTextArea();
      //  this.add(message);
        scroll=new JScrollPane(message);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setVisible(true);
        this.add(scroll);
        // textfield
        field = new JTextField();
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
                   JOptionPane.showMessageDialog(this,"Server has terminated the session" );
                    socket.close();
                    break;
                }
                message.append("Server : "+msg +"\n");
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
//                    BufferedReader b1= new BufferedReader(new InputStreamReader(System.in));
//                    String b2= b1.readLine();
//                    p.println(b2);
//                    p.flush();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        };
//        new Thread(r2).start();
//    }
    public static void main(String[] args) {
        System.out.println("Client is started...");
        new Client();
    }
}
