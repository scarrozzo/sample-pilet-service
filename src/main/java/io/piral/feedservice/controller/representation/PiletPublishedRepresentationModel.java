package io.piral.feedservice.controller.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(value = "piletPublished", collectionRelation = "piletsPublished")
public class PiletPublishedRepresentationModel extends RepresentationModel<PiletPublishedRepresentationModel> {
    Boolean success;
}
