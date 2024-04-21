
import java.net.Socket;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {


    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declear conponent
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("roboto",Font.PLAIN,20);


    //constructor   
    public Client (){
        try {
            System.out.println("Sending request to server");
            socket = new Socket("192.168.43.67",7778);
            System.out.println("Connection done. ");

            
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream()); 
            createGUI();
            hangleEvents();
            startReading();
            StartWriting();

        } catch (Exception e) {
            
        }
    }







    private void hangleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
              
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
              
               // System.out.println("Key released "+  e.getKeyCode());
                if(e.getKeyCode()==10){
                    System.out.println("You have pressed enter button");
                    String contentToSend= messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });


    }






    private void createGUI(){
        //gui code...

        this.setTitle("Client Message[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coading for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.RIGHT);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
       
        
        //frame layout set 

        this.setLayout(new BorderLayout());


        //adding the componets to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);



    }

    // Start reading
    public void startReading(){
        //thread- read krke deta rahega

    
        Runnable r1=()->{
            System.out.println("reader started..");
            try {
                while(!socket.isClosed()){

                   
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                   // System.out.println("Server : "+ msg);
                   messageArea.append("server : "+ msg+"\n");
                }  
            }catch (Exception e) {
                System.out.println("connection is closed");
            }
      
    };

    new Thread(r1).start();
}






// Start Writing 
public void StartWriting(){
    //thread - data user se lega aur send karega client tak
    Runnable r2= () ->{
        System.out.println("writer started");


        try {
            while(true){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
            }

            System.out.println("connection is closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

  
    };
    new Thread(r2).start();
}
    public static void main(String[] args) {
        System.out.println("This is client....");
        new Client();
    }
}
