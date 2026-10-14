package za.co.sbg.tag.skeleton.app.boundary.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(name = "SkeletonNameResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkeletonProjectResponse {

    private Long identifier;
    private String displayName;
    private boolean previouslySeen;
    private ZonedDateTime modifiedAt;
}
