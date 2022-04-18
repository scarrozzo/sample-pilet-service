package io.piral.feedservice.command;

import io.piral.feedservice.domain.dto.PostPiletDto;
import io.piral.feedservice.domain.factory.PiletFactory;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.domain.service.PiletService;
import io.piral.feedservice.exception.ConflictException;
import io.piral.feedservice.exception.ServiceError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Scope("prototype")
@Component
public class CreatePiletCommand extends BaseTransactionalCommand<Pilet>{

    private final PostPiletDto dto;
    private final String appshell;
    private boolean forceUpload;

    @Autowired
    private PiletService piletService;

    @Autowired
    private PiletFactory piletFactory;

    public CreatePiletCommand(final PostPiletDto dto, final String appshell, boolean forceUpload) {
        this.dto = dto;
        this.appshell = appshell;
        this.forceUpload = forceUpload;
    }

    @Override
    protected Pilet doExecute() {
        Pilet pilet = piletFactory.createPilet(dto, appshell);
        Pilet existingPilet = null;

        try {
            existingPilet = piletService.getPilet(pilet.getAppshell(), pilet.getName(), pilet.getPackageVersion());
        } catch (EntityNotFoundException ex){
            log.debug("Pilet for (appshell={}, name={}, packageVersion={}) not exists.", pilet.getAppshell(), pilet.getName(), pilet.getPackageVersion());
        }

        if (forceUpload && existingPilet != null) {
            piletService.deletePilet(existingPilet, true);
            log.info("Force upload pilet si true. Pilet with id {} deleted.", existingPilet.getId());
        } else if(existingPilet != null){
            throw new ConflictException(ServiceError.E0004.getMessage());
        }

        pilet = piletService.savePilet(pilet);

        return pilet;
    }

}
