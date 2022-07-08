package ConsumerProducer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Processor extends Thread {
    private final File taskFile;
    private final String readDirectory = "/Users/pedro/IdeaProjects/Verteilte_Anwendung/finalTasks";

    public Processor(File taskFile) {
        this.taskFile = taskFile;
    }

    public void ReadAndExecute(DataInputStream dis) throws IOException {
        //Processor reads from 'dis' and adds int values
        int a, b, sum;
        a = dis.readInt();
        b = dis.readInt();

        sum = a+b;

        LogHelper.printThreadLog(a + " + " + b + " = " + sum + "; Operation successfully completed at: " + GetTime.getDate());
    }

    @Override
    public void run() {
        //Processor saves files in final folder to a list, then reads, adds the variables from the top file on list then deletes it
        while (true) {
            Path workPath = Paths.get(this.readDirectory);
            try {
                Stream<Path> EveryFileInDir = Files.list(workPath);
                List<Path> allFilesList = EveryFileInDir.collect(Collectors.toList());
                while (!allFilesList.isEmpty()) {
                    Path nextTaskFilePath = allFilesList.remove(0);
                    String nextTaskFileName = nextTaskFilePath.toString();
                    FileInputStream is = new FileInputStream(nextTaskFileName);
                    DataInputStream dis = new DataInputStream(is);
                    ReadAndExecute(dis);
                    dis.close();
                    is.close();
                    sleep(50);
                    Files.delete(nextTaskFilePath);
                    }

                   LogHelper.printThreadLog("no more files to read , waiting a bit");

                   sleep(5000);
                   if (!allFilesList.isEmpty()) {
                       run();
                   }
                   LogHelper.printThreadLog("couldn't find any new files - return");
                   return;

            } catch (IOException | InterruptedException e) {
                LogHelper.printThreadLog("problem while writing file list - try again");
                throw new RuntimeException(e);
            }
        }
    }
}
