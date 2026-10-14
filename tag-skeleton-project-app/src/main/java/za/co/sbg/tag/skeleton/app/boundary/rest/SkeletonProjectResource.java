package za.co.sbg.tag.skeleton.app.boundary.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import za.co.sbg.tag.skeleton.app.controller.SkeletonService;
import za.co.sbg.tag.skeleton.app.entity.SkeletonName;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@RequestScoped
@Path("name")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Skeleton names")
public class SkeletonProjectResource {

    @Inject
    private SkeletonService service;

    @GET
    @Operation(summary = "Get a stored skeleton name")
    @APIResponse(responseCode = "200", description = "Name found",
            content = @Content(schema = @Schema(
                    implementation = SkeletonProjectResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid name")
    @APIResponse(responseCode = "404", description = "Name not found")
    public Response get(
            @Parameter(required = true, description = "Name to retrieve")
            @QueryParam("name") String name) {

        if (isBlank(name)) {
            return badRequest("Query parameter 'name' is required");
        }

        return service.findByName(name)
                .map(this::toResponse)
                .map(value -> Response.ok(value).build())
                .orElseGet(() -> notFound(name));
    }

    @POST
    @Operation(summary = "Create a skeleton name")
    @APIResponse(responseCode = "201", description = "Name created",
            content = @Content(schema = @Schema(
                    implementation = SkeletonProjectResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid request")
    @APIResponse(responseCode = "409", description = "Name already exists")
    public Response create(
            @Parameter(required = true) @QueryParam("name") String name,
            @Parameter(required = true,
                    description = "ISO-8601 zoned date and time")
            @QueryParam("date") String date) {

        if (isBlank(name)) {
            return badRequest("Query parameter 'name' is required");
        }

        var parsedDate = parseDate(date);
        if (parsedDate == null) {
            return badRequest("Query parameter 'date' must be an ISO-8601 zoned date and time");
        }

        return service.create(name, parsedDate)
                .map(this::toResponse)
                .map(value -> Response.status(Response.Status.CREATED)
                        .entity(value)
                        .build())
                .orElseGet(() -> Response.status(Response.Status.CONFLICT)
                        .entity(new SkeletonErrorResponse(
                                "Name already exists: " + name))
                        .build());
    }

    @PUT
    @Operation(summary = "Update a stored skeleton name")
    @APIResponse(responseCode = "200", description = "Name updated",
            content = @Content(schema = @Schema(
                    implementation = SkeletonProjectResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid request")
    @APIResponse(responseCode = "404", description = "Name not found")
    public Response update(
            @Parameter(required = true) @QueryParam("name") String name,
            @RequestBody(required = true, content = @Content(schema = @Schema(
                    implementation = SkeletonProjectUpdateRequest.class)))
            SkeletonProjectUpdateRequest request) {

        if (isBlank(name)) {
            return badRequest("Query parameter 'name' is required");
        }

        if (request == null || request.getLastUpdatedDate() == null) {
            return badRequest("Request body field 'lastUpdatedDate' is required");
        }

        return service.update(name, request.getLastUpdatedDate())
                .map(this::toResponse)
                .map(value -> Response.ok(value).build())
                .orElseGet(() -> notFound(name));
    }

    @DELETE
    @Operation(summary = "Delete a stored skeleton name")
    @APIResponse(responseCode = "204", description = "Name deleted")
    @APIResponse(responseCode = "400", description = "Invalid name")
    @APIResponse(responseCode = "404", description = "Name not found")
    public Response delete(
            @Parameter(required = true) @QueryParam("name") String name) {

        if (isBlank(name)) {
            return badRequest("Query parameter 'name' is required");
        }

        return service.delete(name)
                ? Response.noContent().build()
                : notFound(name);
    }

    private SkeletonProjectResponse toResponse(SkeletonName value) {
        return new SkeletonProjectResponse(
                value.id(),
                value.name(),
                value.updated(),
                value.lastUpdatedDate());
    }

    private ZonedDateTime parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }

        try {
            return ZonedDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new SkeletonErrorResponse(message))
                .build();
    }

    private Response notFound(String name) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new SkeletonErrorResponse(
                        "Name not found: " + name))
                .build();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
