package fr.jima.service.server;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogWriter {
    public static File FILE = null;
    private static LogWriter INSTANCE = null;
    private final Lock mutex;
    private SimpleDateFormat dateFormat;

    private LogWriter() {
        mutex = new ReentrantLock(true);
        dateFormat = new SimpleDateFormat("[yyyy-MM-dd]");
    }

    public static LogWriter getInstance() {
        synchronized (LogWriter.class) {
            if (INSTANCE == null) {
                INSTANCE = new LogWriter();
            }
        }
        return INSTANCE;
    }

    public void writeLog(String log) {
        write(log, System.out);
    }

    public void writeError(String log) {
        write(log, System.err);
    }

    private void write(String log, PrintStream out) {

        // Here e take the mutex
        mutex.lock();

        // We add date
        log = dateFormat.format(new Date()) + " " + log;

        //We print on terminal
        out.println(log);

        //We write on file
        if (FILE != null) {
            writeFile(log, FILE);
        }

        //We release the mutex
        mutex.unlock();

    }

    private void writeFile(String msg, File file) {
        // TODO Later
//        Files.writeString(file.toString(),msg, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

}
