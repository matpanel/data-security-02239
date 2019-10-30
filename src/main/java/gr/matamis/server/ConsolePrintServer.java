package gr.matamis.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ConsolePrintServer implements PrintServer {
    private final AuthenticationService authenticationService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Map<String, Queue<Pair<String, String>>> printerQueues;

    private ScheduledFuture<?> printerFuture;

    public ConsolePrintServer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.printerQueues = new HashMap<>();
        this.printerQueues.put("printer0", new ConcurrentLinkedDeque<>());
        this.printerQueues.put("printer1", new ConcurrentLinkedDeque<>());
    }

    @Override
    public void print(String filename, String printer, Credentials credentials) throws AuthenticationException {

        checkUserIsAuthenticated(credentials);

        Queue<Pair<String, String>> printerQueue = printerQueues.get(printer);
        if (printerQueue != null) {
            String username = credentials.getUsername();
            ImmutablePair<String, String> jobEntry = new ImmutablePair<>(username, filename);
            printerQueue.add(jobEntry);
        }

    }

    @Override
    public void queue(Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);
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
        }
    }

    @Override
    public void printerStatus(Credentials credentials) throws AuthenticationException {
        checkUserIsAuthenticated(credentials);
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
        for (Map.Entry<String, Queue<Pair<String, String>>> printerEntry : printerQueues.entrySet()) {

            Queue<Pair<String, String>> printerQueue = printerEntry.getValue();
            String printerName = printerEntry.getKey();
            Pair<String, String> jobEntry = printerQueue.poll();
            if (jobEntry != null) {
                System.out.println("Printing [" + jobEntry.getRight() + "] on printer [" + printerName + "] for user [" + jobEntry.getLeft() + "]");

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
