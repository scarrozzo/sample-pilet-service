package io.piral.feedservice.controller;

import io.piral.feedservice.controller.assembler.PiletRepresentationModelAssembler;
import io.piral.feedservice.controller.representation.PiletRepresentationModel;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.domain.service.PiletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@Tag(name = "Pilets API")
@RequestMapping(path = PathConfig.PILETS)
public class PiletController {

    @Autowired
    private PiletService piletService;

    @Autowired
    private PiletRepresentationModelAssembler piletRepresentationModelAssembler;

    /*@PostMapping(value = "/{appshell}", consumes = {"multipart/form-data"})
    public ResponseEntity<PiletRepresentationModel> publishPilet(@ModelAttribute @Valid PostPiletDto postPiletDto,
                         @PathVariable("appshell") String appshell,
                         @RequestParam(value = "force", required = false, defaultValue = "false") boolean forceUpload) {
        Pilet pilet = piletService.createPilet(postPiletDto, appshell, forceUpload);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping().path(PathConfig.PILETS + "/{uid}").build()
                .expand(pilet.getUid()).toUri();

        return ok().body(piletRepresentationModelAssembler.toModel(pilet));
    }*/

    @GetMapping(value = "/{appshell}")
    public ResponseEntity<PiletRepresentationModel> getPilets(@PathVariable("appshell") String appshell) {
        Set<Pilet> pilets = piletService.getPilets(appshell);

        return ok().body(piletRepresentationModelAssembler.toModel(pilets));
    }

}
