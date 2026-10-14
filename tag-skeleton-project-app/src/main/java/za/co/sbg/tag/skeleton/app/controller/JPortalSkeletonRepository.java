package za.co.sbg.tag.skeleton.app.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import za.co.sbg.tag.platform.persistence.jportal.controller.DBController;
import za.co.sbg.tag.skeleton.app.entity.SkeletonName;
import za.co.sbg.tag.skeleton.app.entity.anydb.Skeleton;
import za.co.sbg.tag.skeleton.app.entity.anydb.SkeletonDeleteByName;
import za.co.sbg.tag.skeleton.app.entity.anydb.SkeletonLoadNamesByIsUpdated;
import za.co.sbg.tag.skeleton.app.entity.anydb.SkeletonLoadNamesByIsUpdatedStruct;
import za.co.sbg.tag.skeleton.app.entity.anydb.SkeletonSelectByName;
import za.co.sbg.tag.skeleton.app.entity.anydb.SkeletonUpdateNameByName;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JPortalSkeletonRepository implements SkeletonRepository {

    @Inject
    private DBController dbController;

    @Override
    public SkeletonName insert(
            String name,
            ZonedDateTime lastUpdatedDate) {

        var entity = new Skeleton();
        entity.setName(name);
        entity.setIsUpdated(false);
        entity.setLastUpdatedDate(toTimestamp(lastUpdatedDate));

        dbController.insert(entity);

        return new SkeletonName(
                entity.getId(),
                entity.getName(),
                entity.getIsUpdated(),
                toZonedDateTime(entity.getLastUpdatedDate()));
    }

    @Override
    public Optional<SkeletonName> findByName(String name) {
        var query = new SkeletonSelectByName();
        query.setName(name);

        if (!dbController.exec(query)) {
            return Optional.empty();
        }

        return Optional.of(new SkeletonName(
                query.getId(),
                query.getName(),
                query.getIsUpdated(),
                toZonedDateTime(query.getLastUpdatedDate())));
    }

    @Override
    public List<SkeletonName> findAll() {
        Skeleton[] results = dbController.execReturningMulti(new Skeleton());

        return Arrays.stream(results)
                .map(entity -> new SkeletonName(
                        entity.getId(),
                        entity.getName(),
                        entity.getIsUpdated(),
                        toZonedDateTime(entity.getLastUpdatedDate())))
                .toList();
    }

    @Override
    public List<SkeletonName> loadNamesByIsUpdated(boolean updated) {
        var query = new SkeletonLoadNamesByIsUpdated();
        query.setIsUpdated(updated);

        SkeletonLoadNamesByIsUpdatedStruct[] results =
                dbController.execReturningMulti(query);

        return Arrays.stream(results)
                .map(entity -> new SkeletonName(
                        entity.getId(),
                        entity.getName(),
                        entity.getIsUpdated(),
                        toZonedDateTime(entity.getLastUpdatedDate())))
                .toList();
    }

    @Override
    public SkeletonName updateNameByName(
            Long id,
            String name,
            ZonedDateTime lastUpdatedDate) {

        var query = new SkeletonUpdateNameByName();
        query.setName(name);
        query.setIsUpdated(true);
        query.setLastUpdatedDate(toTimestamp(lastUpdatedDate));

        dbController.exec(query);

        return new SkeletonName(id, name, true, lastUpdatedDate);
    }

    @Override
    public void deleteByName(String name) {
        var query = new SkeletonDeleteByName();
        query.setName(name);
        dbController.exec(query);
    }

    private Timestamp toTimestamp(ZonedDateTime value) {
        return Timestamp.from(value.toInstant());
    }

    private ZonedDateTime toZonedDateTime(Timestamp value) {
        return value.toInstant().atZone(ZoneOffset.UTC);
    }
}
