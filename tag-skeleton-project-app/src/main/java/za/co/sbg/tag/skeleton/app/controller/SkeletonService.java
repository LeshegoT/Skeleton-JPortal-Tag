package za.co.sbg.tag.skeleton.app.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import za.co.sbg.tag.platform.messaging.lifecycle.bus.MessageBus;
import za.co.sbg.tag.skeleton.app.entity.SkeletonName;
import za.co.sbg.tag.skeleton.messages.events.OutgoingTextMessageV1;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SkeletonService {

    @Inject
    private SkeletonRepository repository;

    @Inject
    private MessageBus messageBus;

    @Transactional
    public SkeletonName saveOrUpdate(
            String name,
            ZonedDateTime lastUpdatedDate) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        if (lastUpdatedDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        return repository.findByName(name)
                .map(existing -> repository.updateNameByName(
                        existing.id(),
                        name,
                        lastUpdatedDate))
                .orElseGet(() -> repository.insert(
                        name,
                        lastUpdatedDate));
    }

    public Optional<SkeletonName> findByName(String name) {
        validateName(name);
        return repository.findByName(name);
    }

    @Transactional
    public Optional<SkeletonName> create(
            String name,
            ZonedDateTime lastUpdatedDate) {

        validate(name, lastUpdatedDate);

        if (repository.findByName(name).isPresent()) {
            return Optional.empty();
        }

        var created = repository.insert(name, lastUpdatedDate);
        publish(created);
        return Optional.of(created);
    }

    @Transactional
    public Optional<SkeletonName> update(
            String name,
            ZonedDateTime lastUpdatedDate) {

        validate(name, lastUpdatedDate);

        return repository.findByName(name)
                .map(existing -> repository.updateNameByName(
                        existing.id(),
                        name,
                        lastUpdatedDate))
                .map(updated -> {
                    publish(updated);
                    return updated;
                });
    }

    @Transactional
    public boolean delete(String name) {
        validateName(name);

        var existing = repository.findByName(name);
        if (existing.isEmpty()) {
            return false;
        }

        repository.deleteByName(name);
        publish(existing.orElseThrow());
        return true;
    }

    private void publish(SkeletonName skeletonName) {
        var outgoing = OutgoingTextMessageV1.builder()
                .reference(skeletonName.id() == null
                        ? skeletonName.name()
                        : skeletonName.id().toString())
                .name(skeletonName.name())
                .date(skeletonName.lastUpdatedDate())
                .randomAlphaNumeric(UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8))
                .build();

        messageBus.publish(outgoing);
    }

    private void validate(
            String name,
            ZonedDateTime lastUpdatedDate) {

        validateName(name);

        if (lastUpdatedDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }
}
