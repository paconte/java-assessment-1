package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The approach to this solution is to read each document once. From that point of view the complexity is O(n).
 * The strategy to read each document is the following:
 *     A data structure docTF contains the term frequency
 *     A data structure idfHelper contains how many documents contains a term
 *     To avoid reading documents more than once the data structure documentsScanned is used
 * Every document is read in a single thread from a thread of pools, while reading each document, the above data
 * structures are updated.
 *
 * Why is the approach not object-oriented? Encapsulating code into classes increases the lines of codes, and eventually
 * will increase the number of files. The idea is to have everything in a single file and use the java concurrent
 * library data structures and methods which access them. Without comments this file will be around 160 lines of code.
 */
public class Exercise3 {

    /**
     * This map contains the term frequency per document, example:
     *  doc1.txt -> "hello", 0.5
     *           -> "bye"  , 0.1
     *  doc2.txt -> "hello", 0.2
     *           -> "bye"  , 0.2
     */
    private static Map<String, Map<String, Double>> docTF = new ConcurrentHashMap<>();

    /**
     * This document contains how many documents have a term, example:
     * ("hello", 5), ("bye", 3) => "hello" is in 5 documents, and "bye" in 3 documents
     */
    private static Map<String, Integer> idfHelper = new ConcurrentHashMap<>();

    /**
     * This set contains the already read documents, to avoid to read documents more than once.
     * NOTE that this data structure will need a lock to be accessed.
     */
    private static Set<Path> documentsScanned = new HashSet<>();

    /**
     * This executor will be used to read documents, instead of creating a single thread per document, we use a pool of
     * threads
     */
    public static ExecutorService executorService;

    /**
     * This scheduler will be used to print the ranking every P seconds
     */
    public static ScheduledExecutorService printScheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * This scheduler will be used to read the directory for new documents to be read
     */
    public static ScheduledExecutorService readerScheduler = Executors.newSingleThreadScheduledExecutor();


    /**
     * Method to tokenize the natural language. It should be used for tokenize the input of the user when calling the
     * executable and when reading the documents.
     *
     * You can customize how to tokenize or clean your data in this method.
     *
     * @param str The text to be tokenized
     * @return An array of tokens
     */
    public static String[] cleanData(String str) {
        return str.toLowerCase().split("\\s+");
    }

    /**
     * The ranking class only groups ranking related methods, please note that the class is not necessary as all methods
     * are static.
     *
     * Every time getRanking() is called, it calculates on the fly the tf-idf for each document and afterwards sort the
     * data structure (Map). A possible? optimization would be to have a Sorted Tree Map to void sorting the map for
     * every call.
     */
    public class Ranking {

        private synchronized static Map<String, Double> calculateTfIdf() {
            // calculate idf for each term
            final Map<String, Double> idf = new HashMap<>();
            for (var entry1 : idfHelper.entrySet()) {
                try {
                    double idfValue = entry1.getValue();
                    if (idfValue == 0.0)
                        idfValue = 1.0;
                    idf.put(entry1.getKey(), Math.log(docTF.size() / idfValue));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            // calculate sum(tt, doc) = idf(doc) * tf(tt, doc)
            final Map<String, Double> tfIdf = new HashMap<>();
            for (var entry1 : docTF.entrySet()) {
                Double tfidfSum = 0.0;
                for (var entry2: entry1.getValue().entrySet()) {
                    Double tfidf = idf.get(entry2.getKey()) * entry2.getValue();
                    tfidfSum += tfidf;
                }
                tfIdf.put(entry1.getKey(), tfidfSum);
            }

            return tfIdf;
        }

        private synchronized static Map<String, Double> sortRanking(Map<String, Double> tfIdf) {
            final Map<String, Double> sortedTfIdf = tfIdf.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            return sortedTfIdf;
        }

        public synchronized static Map<String, Double> getRanking() {
            return sortRanking(calculateTfIdf());
        }

        public synchronized static void print(int n) {
            int i = 1;
            final Map<String, Double> ranking = getRanking();
            System.out.println();
            for (var entry: ranking.entrySet()) {
                if (i > n) {return;} // exit when n entries have been printed
                System.out.println(entry.getKey() + " " + entry.getValue());
                i++;
            }
        }
    }

    private static void countLineTerms(String[] line, Map<String, Double> frequencyMap) {
        for (String word: line) {
            frequencyMap.computeIfPresent(word, (k, v) -> v + 1.0);
        }
    }

    /**
     * Updates the idfHelper which stores how many documents contains a given word.
     *
     * @param frequencyMap The term frequency (tf) of a single document.
     */
    private static void countTermAppearance(Map<String, Double> frequencyMap) {
        frequencyMap.forEach((term, tf) -> {
            if (tf > 0.0) // appearance of term t in document
                idfHelper.computeIfPresent(term, (key, value) -> value + 1);
        });
    }

    /**
     * Calculates the tf dividing the term count by the total number of words
     *
     * @param frequencyMap A map counting how many times a word is in a document
     * @param totalWords The total number of terms in a document
     */
    private static void calculateTF(Map<String, Double> frequencyMap, double totalWords) {
        frequencyMap.replaceAll((term, repetitions) -> repetitions / totalWords);
    }

    /**
     * Reads a document line per line. It tokenizes every line of the document and calculates the tf per each term.
     * It also updates the idfhelper map (how many documents contains a word) to later calculate the idf. Using this
     * technique every document is read only once.
     *
     * @param document The document path to read
     * @param terms The terms
     */
    public static void readDocument(Path document, Set<String> terms) {
        // init frequency Map
        Map<String, Double> frequencyMap = terms.stream().collect(Collectors.toMap(x -> x, x -> 0.0));

        // read document and fill data structures with tf and appearance
        int totalWords = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(document.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cleanedLine = cleanData(line);
                totalWords += cleanedLine.length;
                countLineTerms(cleanedLine, frequencyMap);
            }
            calculateTF(frequencyMap, totalWords);
            docTF.put(document.toString(), frequencyMap);
            countTermAppearance(frequencyMap);
        } catch (FileNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO: remove document from documentsScanned to try again
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method read all the documents of a given directory, consuming a thread from executorService
     * for each document.
     *
     * @param dir The path to the directory to read
     * @param terms The terms
     */
    private static void readDirectory(Path dir, Set<String> terms) {
        synchronized (documentsScanned) {
            try (Stream<Path> paths = Files.walk(dir)) {
                paths
                        .filter(x -> Files.isRegularFile(x))
                        .filter(x -> !documentsScanned.contains(x))
                        .forEach(doc -> executorService.submit(() -> {
                            documentsScanned.add(doc);
                            readDocument(doc, terms);
                        }));
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Main method for calling ./tdIdf -d dir -n 5 -p 300 -t "password try again"
     * @param args
     */
    public static void main (String[] args) {

        // the first parameter is the dir
        Path dir = Paths.get(args[0]);

        // the second parameter is the number of positions of the ranking to be displayed
        int n = Integer.parseInt(args[1]);

        // the third argument is the period
        int p = Integer.parseInt(args[2]);

        // the fourth argument are the terms of the tf-idf
        Set<String> terms = new HashSet<>(List.of(cleanData(args[3])));

        // init idfHelper with zero appearance of terms in the document set
        idfHelper = terms.stream().collect(Collectors.toMap(x -> x, x -> 0));

        // print the ranking every P seconds
        printScheduler.scheduleAtFixedRate(() -> Ranking.print(n), 0, p, TimeUnit.SECONDS);

        // read each document in its own thread every P/3 seconds
        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
        readerScheduler.scheduleAtFixedRate(() -> readDirectory(dir, terms), 0, p/3, TimeUnit.SECONDS);
    }
}