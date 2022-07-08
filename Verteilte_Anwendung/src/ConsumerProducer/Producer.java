package ConsumerProducer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Producer extends Thread {

    private Socket clientSocket;

    private final File file;

    Producer(File producerFile) {
        this.file = producerFile;

    }

    public static void main(String[] args) throws IOException {
        //main - for non-test scenarios
        Runnable Producer = new Producer(File.createTempFile("cmd", "txt"));
        Thread thread = new Thread(Producer);
        thread.start();
    }

    public void ConnectToServer() throws IOException, InterruptedException {
        sleep(500);
        while (true) {
            this.clientSocket = new Socket("localhost", 6666);
            OutputStream os = clientSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            if (this.clientSocket != null) {
                LogHelper.printThreadLog("ClientConnected");
                Random rand = new Random();
                int upperbound = 10000;
                int a = rand.nextInt(upperbound);
                int b = rand.nextInt(upperbound);
                //sends random numbers to server (Filler)
                dos.writeInt(a);
                sleep(50);
                dos.writeInt(b);

                LogHelper.printThreadLog("Producer - Successfully sent to Filler: " + a + ", " + b);

                clientSocket.close();
                break;
            }
        }
    }


    @Override
    public void run() {
        while (true) {
            this.clientSocket = null;
            try {
                ConnectToServer();
            } catch (IOException e) {
                LogHelper.printThreadLog("couldn't connect to Server - trying again...");
                run();
            } catch (InterruptedException e) {
                LogHelper.printThreadLog("fatal - try again");
                break;
            }
            //if producer connects, leave loop
            break;
        }
    }
}