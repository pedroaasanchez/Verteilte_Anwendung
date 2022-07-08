package ConsumerProducer;

import org.junit.Test;
import java.io.*;

import static java.lang.Thread.sleep;


public class Tests {
    public File tmpFile = File.createTempFile("cmd", "txt");

    public Tests() throws IOException {
    }

    @Test
    public void FillerTest1() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        f.writeInt(1, 2);
    }

    @Test
    public void FillerTest2() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        f.writeInt(1, 2);

        f.writeInt(3, 4);

        f.writeInt(5, 6);

        f.writeInt(7, 8);

        f.writeInt(9, 0);
    }

    @Test
    public void ProcessorTest() throws InterruptedException {
        //only returns values if to-do-files exist
        Processor p = new Processor((this.tmpFile));
        p.start();
        sleep(10000);
    }

    @Test
    public void TCPTest() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        Producer p = new Producer(this.tmpFile);

        f.start();
        p.start();

        Thread.sleep(1000);
    }

    @Test
    public void OnlyProducer() throws InterruptedException {
        //shouldn't be able to work - no connection
        Producer prod = new Producer(tmpFile);
        prod.start();
        Thread.sleep(5000);
    }

    @Test
    public void ProducerFirst() throws InterruptedException, IOException {
        Filler f = new Filler(this.tmpFile);
        Producer prod = new Producer(tmpFile);
        prod.start();
        Thread.sleep(2000);

        f.start();

        Thread.sleep(1000);
    }

    @Test
    public void TwoProducersTest() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        f.start();
        sleep(50);
        for (int i=0; i<10; i++) {
            (new Thread(new Producer(this.tmpFile))).start();
            sleep(50);
        }
        Thread.sleep(8000);
    }
    @Test
    public void AllTogether() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        f.start();

        Producer prod = new Producer(tmpFile);
        prod.start();
        //Producer has to sleep to allow processor to receive the files
        Thread.sleep(1000);

        Processor proc = new Processor(tmpFile);
        proc.start();
        Thread.sleep(1000);

    }

    @Test
    public void AllTogetherButManyProducers() throws IOException, InterruptedException {
        Filler f = new Filler(this.tmpFile);
        f.start();
        Thread.sleep(2000);

        for (int i=0; i<20; i++) {
            (new Thread(new Producer(this.tmpFile))).start();
            Thread.sleep(50);
        }

        Thread.sleep(5000);
        Processor proc = new Processor(tmpFile);
        proc.start();
        Thread.sleep(10000);

    }
}
