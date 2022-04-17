package io.piral.feedservice.controller.assembler;

import io.piral.feedservice.controller.PiletController;
import io.piral.feedservice.controller.representation.PiletItem;
import io.piral.feedservice.controller.representation.PiletRepresentationModel;
import io.piral.feedservice.domain.model.Pilet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class PiletRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Set<Pilet>, PiletRepresentationModel> {

    @Value("${application.file-url}")
    private String fileUrlTemplate;

    public PiletRepresentationModelAssembler(){
        super(PiletController.class, PiletRepresentationModel.class);
    }

    @Override
    public PiletRepresentationModel toModel(Set<Pilet> entities) {
        return instantiateModel(entities);
    }

    @Override
    protected PiletRepresentationModel instantiateModel(Set<Pilet> entities) {
        PiletRepresentationModel representationModel = new PiletRepresentationModel();

        List<PiletItem> items = new ArrayList<>();
        for(Pilet entity : entities){
            String fileUrl = String.format(fileUrlTemplate, entity.getAppshell(), entity.getName(), entity.getPackageVersion());

            PiletItem item = PiletItem.builder()
                    .name(entity.getName())
                    .version(entity.getPackageVersion())
                    .link(fileUrl)
                    .requireRef(entity.getRequireRef())
                    .integrity(entity.getIntegrity())
                    .spec(entity.getType())
                    .build();

            items.add(item);
        }

        representationModel.setItems(items);

        return representationModel;
    }
}
