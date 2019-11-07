package gr.matamis.server;

import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ConsolePrintServer implements PrintServer {
    private final AuthenticationService authenticationService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Map<String, Deque<Integer>> printer2queue;
    private final Map<String, Integer> printer2counter;

    private final Map<String, Pair<String, String>> jobs;

    private ScheduledFuture<?> printerFuture;


    public ConsolePrintServer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        this.jobs = new HashMap<>();
        this.printer2queue = new HashMap<>();
        this.printer2counter = new HashMap<>();

        this.printer2queue.put("printer0", new ConcurrentLinkedDeque<>());
        this.printer2counter.put("printer0", 0);

        this.printer2queue.put("printer1", new ConcurrentLinkedDeque<>());
        this.printer2counter.put("printer1", 0);
    }

    @Override
    public void print(String filename, String printer, Credentials credentials) throws AuthenticationException {

        checkUserIsAuthenticated(credentials);

        Queue<Integer> printerQueue = printer2queue.get(printer);
        if (printerQueue != null) {
            String username = credentials.getUsername();
            ImmutablePair<String, String> jobEntry = new ImmutablePair<>(username, filename);

            Integer jobNumber = this.printer2counter.get(printer);
            this.printer2counter.put(printer, jobNumber + 1);

            jobs.put(printer + jobNumber, jobEntry);
            printerQueue.add(jobNumber);

        }

    }

    @Override
    public boolean topQueue(String printer, int job, Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);
        boolean success = false;
        Deque<Integer> printerQueue = printer2queue.get(printer);
        if (printerQueue != null && printerQueue.remove(job)) {
            printerQueue.addFirst(job);
            success = true;
        }
        return success;
    }

    @Override
    public String status(String printer, Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);

        Queue<Integer> printerQueue = printer2queue.get(printer);
        String result = null;
        if (printerQueue != null) {
            if (printerQueue.size() > 0) {
                System.out.println("Printer " + printer + " is busy.");
                result = "busy";
            } else {
                System.out.println("Printer " + printer + " is idle.");
                result = "idle";
            }
        }
        return result;
    }

    @Override
    public List<String> queue(String printer, Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);

        Queue<Integer> printerQueue = printer2queue.get(printer);
        List<String> jobList = null;
        if (printerQueue != null) {
            jobList = new ArrayList<>();
            System.out.println("Queue size for " + printer + " is " + printerQueue.size());
            for (Integer jobNumber : printerQueue) {
                Pair<String, String> jobEntry = jobs.get(printer + jobNumber);
                jobList.add(jobNumber + " " + jobEntry.getRight());
            }

        }

        return jobList;
    }

    @Override
    public void restart(Credentials credentials) throws AuthenticationException {


        checkUserIsAuthenticated(credentials);

        this.stop(credentials);
        this.start(credentials);
    }

    @Override
    public void start(Credentials credentials) throws AuthenticationException {

        checkUserIsAuthenticated(credentials);

        if (this.printerFuture == null) {

            System.out.println("Starting Server...");
            this.printerFuture = scheduler.scheduleWithFixedDelay(this::printJobs, 5, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void stop(Credentials credentials) throws AuthenticationException {

        checkUserIsAuthenticated(credentials);

        if (this.printerFuture != null) {
            this.printerFuture.cancel(true);
            this.printerFuture = null;
        }
    }

    @Override
    public void readConfig(String parameter, Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);

    }

    @Override
    public void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);

    }

    private void printJobs() {
        for (Map.Entry<String, Deque<Integer>> printerEntry : printer2queue.entrySet()) {

            Deque<Integer> printerQueue = printerEntry.getValue();
            String printerName = printerEntry.getKey();
            Integer jobNumber = printerQueue.pollFirst();
            if (jobNumber != null) {
                String jobKey = printerName + jobNumber;
                Pair<String, String> jobEntry = jobs.get(jobKey);
                System.out.println("Printing [" + jobEntry.getRight() + "] on printer [" + printerName + "] for user [" + jobEntry.getLeft() + "]");
                jobs.remove(jobKey);
            } else {
                System.out.println("Nothing found for printer [" + printerName + "]");
            }
        }
    }

    private void checkUserIsAuthenticated(Credentials credentials) throws AuthenticationException {
        if (!authenticationService.isAuthenticated(credentials)) {
            throw new AuthenticationException(credentials);
        }
    }
}
