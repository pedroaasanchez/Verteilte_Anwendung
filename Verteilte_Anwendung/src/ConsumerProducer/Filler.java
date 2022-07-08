package ConsumerProducer;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Filler extends Thread {

    private File tempFile = File.createTempFile("cmd", "txt");
    //private static File File;
    private final String FinalPath = "/Users/pedro/IdeaProjects/Verteilte_Anwendung/finalTasks";

    public Filler(File cmdFile) throws IOException {
        this.tempFile = cmdFile;
    }

    public static void main(String[] args) throws IOException {
        //main - for non-test scenarios
        Runnable Filler = new Filler(File.createTempFile("cmd", "txt"));
        Thread thread = new Thread(Filler);
        thread.start();
    }

    public void connectTCP (Filler f) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(6666);
        InputStream is;
        System.out.println("Socket open, hearing...");

        //Filler stays always awake, listening... - in case a Producer tries to reach it
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LogHelper.printThreadLog("Connection accepted");

            is = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(is);



            //gets numbers from dis and saves to variables
            int a, b;
            a = dis.readInt();
            sleep(50);
            b = dis.readInt();

            //sends variables to writeInt();, prints values for control
            f.writeInt(a, b);
            sleep(100);
        }
    }

    public void writeInt(int a, int b) throws IOException, InterruptedException {
        //writes from OutputStream into file, calls RenameFile();
        LogHelper.printThreadLog("Filler - writing: " + a + ", " + b);

        FileOutputStream fos = new FileOutputStream(this.tempFile);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeInt(a);
        dos.writeInt(b);
        fos.close();
        dos.close();

        renameFile();
        sleep(50);
    }

    public void renameFile() {
        //renames file and moves it to a new directory - it uses current time to ensure every file has a unique name
        long time = GetTime.getIt();

        Path oldFile
                = Paths.get(String.valueOf(this.tempFile));
        Path finalPath
                = Paths.get(this.FinalPath);
        try {
            Files.move(oldFile, oldFile.resolveSibling(
                    finalPath + "/File" + time + ".txt"));
            LogHelper.printThreadLog("File Successfully Renamed and Moved");
        } catch (IOException e) {
            LogHelper.printThreadLog("operation failed");
        }
    }

    public void run(){
        //creates object and calls TCP connection
        try {
        Filler f = new Filler(this.tempFile);
            f.connectTCP(f);
        } catch (IOException | InterruptedException e) {
            LogHelper.printThreadLog("something is not right with the socket - trying again...");
        }
    }
}
