package za.co.sbg.tag.skeleton.app.boundary.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "SkeletonNameUpdateRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkeletonProjectUpdateRequest {

    @Schema(required = true,
            example = "2026-10-08T10:00:00+02:00")
    private ZonedDateTime lastUpdatedDate;
}
