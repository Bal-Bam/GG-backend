package com.gg.bal_bam.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LocationBasedRequest {

    private int offset;
    private int limit;
    private Double latitude;
    private Double longitude;
}
