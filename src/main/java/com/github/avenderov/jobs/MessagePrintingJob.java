package com.github.avenderov.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

@Component
public class MessagePrintingJob extends AbstractFixedDelayJob {

    private static final Logger LOG = LoggerFactory.getLogger(MessagePrintingJob.class);

    private final Consumer<String> messageConsumer;

    @Autowired
    public MessagePrintingJob(
        @Qualifier("messagePrintingJobExecutor") final Optional<ScheduledExecutorService> executorServiceOpt) {
        this(executorServiceOpt, Duration.ofSeconds(5L), new LoggingMessageConsumer());
    }

    MessagePrintingJob(final Optional<ScheduledExecutorService> executorServiceOpt,
                       final Duration delay,
                       final Consumer<String> messageConsumer) {
        super(executorServiceOpt, delay);

        this.messageConsumer = messageConsumer;
    }

    @Override
    public void run() {
        messageConsumer.accept("Hello, World!");
    }

    private static class LoggingMessageConsumer implements Consumer<String> {

        @Override
        public void accept(final String message) {
            LOG.info(message);
        }
    }
}
