package io.piral.feedservice.controller.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(value = "pilet", collectionRelation = "pilets")
public class PiletRepresentationModel extends RepresentationModel<PiletRepresentationModel> {
    List<PiletItem> items;
}
