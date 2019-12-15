package com.github.avenderov.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractFixedDelayJob implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFixedDelayJob.class);

    private final AtomicBoolean scheduled = new AtomicBoolean(false);

    private final Optional<ScheduledExecutorService> executorServiceOpt;

    private final Duration delay;

    protected AbstractFixedDelayJob(final Optional<ScheduledExecutorService> executorServiceOpt, final Duration delay) {
        this.executorServiceOpt = executorServiceOpt;
        this.delay = delay;
    }

    @PostConstruct
    public void schedule() {
        executorServiceOpt.ifPresentOrElse(this::scheduleJob, this::warnNoScheduler);
    }

    public final boolean isScheduled() {
        return scheduled.get();
    }

    private void scheduleJob(final ScheduledExecutorService executorService) {
        final var delayInMillis = delay.toMillis();
        executorService.scheduleWithFixedDelay(
            new ErrorHandlingRunnable(this), delayInMillis, delayInMillis, TimeUnit.MILLISECONDS);
        scheduled.set(true);
    }

    private void warnNoScheduler() {
        LOG.warn("Executor service was not provided. Job name='{}' will not be scheduled",
            this.getClass().getSimpleName());
    }

    private static class ErrorHandlingRunnable implements Runnable {

        private static final Logger LOG = LoggerFactory.getLogger(ErrorHandlingRunnable.class);

        private final Runnable delegate;

        ErrorHandlingRunnable(final Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                delegate.run();
            } catch (Throwable t) {
                LOG.error("Unhandled exception occurred in a job", t);
            }
        }
    }
}
