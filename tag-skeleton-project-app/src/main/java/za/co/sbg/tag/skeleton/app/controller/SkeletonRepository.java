package za.co.sbg.tag.skeleton.app.controller;

import za.co.sbg.tag.skeleton.app.entity.SkeletonName;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface SkeletonRepository {

    SkeletonName insert(String name, ZonedDateTime lastUpdatedDate);

    Optional<SkeletonName> findByName(String name);

    List<SkeletonName> findAll();

    List<SkeletonName> loadNamesByIsUpdated(boolean updated);

    SkeletonName updateNameByName(
            Long id,
            String name,
            ZonedDateTime lastUpdatedDate);

    void deleteByName(String name);
}
