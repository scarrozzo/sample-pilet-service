package io.piral.feedservice.controller;

import io.piral.feedservice.controller.assembler.PiletPublishedRepresentationModelAssembler;
import io.piral.feedservice.controller.assembler.PiletRepresentationModelAssembler;
import io.piral.feedservice.controller.representation.PiletPublishedRepresentationModel;
import io.piral.feedservice.controller.representation.PiletRepresentationModel;
import io.piral.feedservice.domain.dto.PostPiletDto;
import io.piral.feedservice.domain.model.File;
import io.piral.feedservice.domain.model.Pilet;
import io.piral.feedservice.domain.service.PiletService;
import io.piral.feedservice.exception.DownloadFileException;
import io.piral.feedservice.exception.ServiceError;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    private PiletPublishedRepresentationModelAssembler piletPublishedRepresentationModelAssembler;

    @PostMapping(value = "/{appshell}", consumes = {"multipart/form-data"})
    public ResponseEntity<PiletPublishedRepresentationModel> publishPilet(@ModelAttribute @Valid PostPiletDto postPiletDto,
                                                                          @PathVariable("appshell") String appshell,
                                                                          @RequestParam(value = "force", required = false, defaultValue = "false") boolean forceUpload) {
        Pilet pilet = piletService.createPilet(postPiletDto, appshell, forceUpload);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping().path(PathConfig.PILETS + "/{uid}").build()
                .expand(pilet.getUid()).toUri();

        return ok().body(piletPublishedRepresentationModelAssembler.toModel(pilet));
    }

    @GetMapping(value = "/{appshell}")
    public ResponseEntity<PiletRepresentationModel> getPilets(@PathVariable("appshell") String appshell) {
        Set<Pilet> pilets = piletService.getPilets(appshell);

        return ok().body(piletRepresentationModelAssembler.toModel(pilets));
    }


    @GetMapping(value = "/files/{appshell}/{name}/{version}/{filename}")
    public ResponseEntity<?> getPiletFiles(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("appshell") String appshell, @PathVariable("name") String piletName,
                                      @PathVariable("version") String piletVersion, @PathVariable("filename") String fileName) {

        File file = piletService.getPiletFile(appshell, piletName, piletVersion, fileName);
        byte[] contentBytes = file.getFileContent().getBytes(StandardCharsets.UTF_8);

        response.setHeader(HttpHeaders.CONTENT_TYPE, new Tika().detect(contentBytes, file.getFileName()));
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentBytes.length));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBytes);

        try {
            IOUtils.copy(byteArrayInputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("Error while serving image.", e);
            throw new DownloadFileException(ServiceError.E0002.getMessage(), e);
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                log.error("Cannot close input stream.", e);
            }
        }

        return ok().build();
    }

}
