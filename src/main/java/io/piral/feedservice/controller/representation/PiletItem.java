package io.piral.feedservice.controller.representation;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PiletItem {
    private LocalDate lastModifiedDate;

    private LocalTime lastModifiedTime;

    private LocalDate createdDate;

    private LocalTime createdTime;

    private String creator;

    private String modifier;

    private String uid;

    String name;

    String version;

    String link;

    String requireRef;

    String integrity;

    String spec;
}
