package za.co.sbg.tag.skeleton.app.entity;

import java.time.ZonedDateTime;

public record SkeletonName(
        Long id,
        String name,
        boolean updated,
        ZonedDateTime lastUpdatedDate) {
}
