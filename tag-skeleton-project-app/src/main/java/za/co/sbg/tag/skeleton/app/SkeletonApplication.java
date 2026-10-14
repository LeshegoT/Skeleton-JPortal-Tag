package za.co.sbg.tag.skeleton.app;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@ApplicationPath("/tag/skeleton")
@OpenAPIDefinition(info = @Info(
        title = "TAG Skeleton API",
        version = "1.0.0",
        description = "CRUD operations for the TAG skeleton onboarding service"))
@SecurityScheme(securitySchemeName = "Authorization", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SkeletonApplication extends Application {
}
