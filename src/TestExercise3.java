package src;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class TestExercise3 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-----------------------------------------");
        System.out.println("------------ Exercise 3 -----------------");
        System.out.println("-----------------------------------------");

        // terms to search
        String terms = "hola baby queso";

        // folder to look up for documents
        Path currentRelativePath = Paths.get("");
        String testFolder = currentRelativePath.toAbsolutePath() + "/testFiles";

        // execute and stop program
        String[] params = {testFolder, "10", "5", terms}; // ./tdIdf -d dir -n 5 -p 300 -t "password try again"
        Exercise3.main(params);
        TimeUnit.SECONDS.sleep(10);
        Exercise3.executorService.shutdown();
        Exercise3.printScheduler.shutdown();
        Exercise3.readerScheduler.shutdown();

        // check result
        var result = Exercise3.Ranking.getRanking();
        assert 0.06301338005090412 == result.get(testFolder + "/document1.txt");
        assert 0.0 == result.get(testFolder + "/document2.txt");
    }
}
