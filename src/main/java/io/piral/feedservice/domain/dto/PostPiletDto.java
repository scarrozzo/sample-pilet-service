package io.piral.feedservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(name = "PostPilet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPiletDto implements Serializable {
    @Schema(description = "Pilet file", example = "example-pilet-1.0.4.tgz", required = true)
    @NotNull
    MultipartFile file;
}
