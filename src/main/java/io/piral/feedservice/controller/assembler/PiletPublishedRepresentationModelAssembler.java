package io.piral.feedservice.controller.assembler;

import io.piral.feedservice.controller.PiletController;
import io.piral.feedservice.controller.representation.PiletPublishedRepresentationModel;
import io.piral.feedservice.domain.model.Pilet;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class PiletPublishedRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Pilet, PiletPublishedRepresentationModel>{

    public PiletPublishedRepresentationModelAssembler(){
        super(PiletController.class, PiletPublishedRepresentationModel.class);
    }

    @Override
    public PiletPublishedRepresentationModel toModel(Pilet entity) {
        return instantiateModel(entity);
    }

    @Override
    protected PiletPublishedRepresentationModel instantiateModel(Pilet entity) {
        PiletPublishedRepresentationModel representationModel = new PiletPublishedRepresentationModel();

        representationModel.setSuccess(Boolean.TRUE);

        return representationModel;
    }
}
