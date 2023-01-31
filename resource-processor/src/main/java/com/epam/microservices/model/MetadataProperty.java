package com.epam.microservices.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MetadataProperty {
    TITLE("Title", "dc:title"),
    ARTIST("Artist", "xmpDM:artist"),
    ALBUM("Album", "xmpDM:album"),
    DURATION("Duration", "xmpDM:duration"),
    RELEASE_DATE("Release Date", "xmpDM:releaseDate");

    private final String name;
    private final String tag;
}
