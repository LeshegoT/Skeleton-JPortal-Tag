package za.co.sbg.tag.skeleton.app.boundary.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SkeletonErrorResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkeletonErrorResponse {

    private String message;
}
