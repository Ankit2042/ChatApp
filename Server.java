import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import javax.swing.*;




public class Server extends JFrame{
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;


    //Declear Components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);


    //constructor..
    public Server(){
        try {
            server = new ServerSocket(7778);
            System.out.println("server is ready to accept connection");
            System.out.println("wating...");
            socket = server.accept();

            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream()); 
            createGUI();
            handleEvents();
            startReading();
            StartWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }





private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
          
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
               
            }

            @Override
            public void keyReleased(KeyEvent e) {
              
               //System.out.println("Key released " + e.getKeyCode());
               if(e.getKeyCode()==10){
                //System.out.println("You have pressed enter button");
                String contentToSend = messageInput.getText();
                messagArea.append("Me : "+contentToSend+"\n");
                out.println(contentToSend); 
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
               }
            }
            
        });
    }





private void createGUI(){
    //GUI code

    this.setTitle("Server Messager[END]");
    this.setSize(600, 700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // coading for components

    heading.setFont(font);
    messagArea.setFont(font);
    messageInput.setFont(font);
    heading.setIcon(new ImageIcon("clogo.png"));
    heading.setHorizontalTextPosition(SwingConstants.RIGHT);
    heading.setVerticalTextPosition(SwingConstants.CENTER);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    messagArea.setEditable(false);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);
    // frame layout set

    this.setLayout(new BorderLayout());


    //adding the componets to frame
    this.add(heading,BorderLayout.NORTH);
    JScrollPane jScrollPane = new JScrollPane(messagArea);
    this.add(jScrollPane,BorderLayout.CENTER);
    this.add(messageInput,BorderLayout.SOUTH);



    this.setVisible(true);
}





    public void startReading(){
        //thread- read krke deta rahega

    
        Runnable r1=()->{
            System.out.println("reader started..");
            try {
                while(true){

                   
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : "+ msg);
                    messagArea.append("Client : "+ msg+"\n");
                }  
            }catch (Exception e) {
                System.out.println("connection is closed");
            }
      
            
    };

    new Thread(r1).start();
}







    public void StartWriting(){
        //thread - data user se lega aur send karega client tak
        Runnable r2= () ->{
            System.out.println("writer started");


            try {
                while(!socket.isClosed()){
                    
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
        System.out.println("This is server : going to start server");
        new Server();
    }
}