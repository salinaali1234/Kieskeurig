package nl.hva.kieskeurig.dto;

import java.util.Date;

public record ErrorResponse(String message, Date timesStamp) {
}
