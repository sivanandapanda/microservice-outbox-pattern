package com.example.shipment.messagelog;

import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
@Traced
public class MessageLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLog.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(value = Transactional.TxType.MANDATORY)
    public void processed(UUID eventId) {
        entityManager.persist(new ConsumedMessage(eventId, Instant.now()));
    }

    @Transactional(value = Transactional.TxType.MANDATORY)
    public boolean alreadyProcessed(UUID eventId) {
        LOGGER.debug("Looking for event with id {} in message log", eventId);
        return entityManager.find(ConsumedMessage.class, eventId) != null;
    }

}
