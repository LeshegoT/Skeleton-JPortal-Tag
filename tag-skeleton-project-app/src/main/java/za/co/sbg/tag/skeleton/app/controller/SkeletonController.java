package za.co.sbg.tag.skeleton.app.controller;

import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.factory.Mappers;
import za.co.sbg.tag.skeleton.messages.commands.IncomingFixedLengthDelimitedMessageV1;
import za.co.sbg.tag.skeleton.messages.commands.IncomingJsonMessageV1;
import za.co.sbg.tag.skeleton.messages.events.OutgoingTextMessageV1;
import za.co.sbg.tag.skeleton.messages.events.OutgoingXmlMessageV1;

import java.time.ZoneId;

@ApplicationScoped
public class SkeletonController {

    private final SkeletonMapper mapper =
            Mappers.getMapper(SkeletonMapper.class);

    public OutgoingXmlMessageV1 processJson(
            IncomingJsonMessageV1 incoming,
            String countryIso) {

        validate(incoming.getRandom());

        if ("USA".equalsIgnoreCase(countryIso)
                && incoming.getDate() != null) {

            incoming.setDate(
                    incoming.getDate()
                            .withZoneSameInstant(
                                    ZoneId.of("America/New_York")
                            )
            );
        }

        return mapper.toOutgoingXml(incoming);
    }

    public OutgoingTextMessageV1 processFixedLength(
            IncomingFixedLengthDelimitedMessageV1 incoming) {

        validate(incoming.getRandom());

        return mapper.toOutgoingText(incoming);
    }

    private void validate(Integer random) {
        if (random == null || random < 0) {
            throw new IllegalArgumentException(
                    "Random value must be zero or greater"
            );
        }
    }
}