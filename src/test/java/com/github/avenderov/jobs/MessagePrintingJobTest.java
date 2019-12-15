package com.github.avenderov.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class MessagePrintingJobTest extends AbstractSpringBootTest {

    @Autowired
    private MessagePrintingJob underTest;

    @Test
    void shouldNotBeScheduled() {
        assertThat(underTest.isScheduled()).isFalse();
    }

    @Test
    void shouldGenerateMessageWhenCalled() {
        // given
        final var messageConsumer = new CapturingMessageConsumer();
        final var job = new MessagePrintingJob(Optional.empty(), Duration.ZERO, messageConsumer);

        // when
        job.run();

        // then
        assertThat(messageConsumer.message).isEqualTo("Hello, World!");
    }

    private static class CapturingMessageConsumer implements Consumer<String> {

        private String message;

        @Override
        public void accept(final String message) {
            this.message = message;
        }
    }
}
