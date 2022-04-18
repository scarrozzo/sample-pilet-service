package io.piral.feedservice.controller.representation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PiletItem {
    /*LocalDate lastModifiedDate;

    LocalTime lastModifiedTime;

    LocalDate createdDate;

    LocalTime createdTime;

    String creator;

    String modifier;

    String uid;*/

    String name;

    String version;

    String link;

    String requireRef;

    String integrity;

    String spec;
}
