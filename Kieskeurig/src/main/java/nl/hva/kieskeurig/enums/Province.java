package nl.hva.kieskeurig.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Province {
    DRENTHE("Drenthe"),
    FLEVOLAND("Flevoland"),
    FRIESLAND("Friesland"),
    GELDERLAND("Gelderland"),
    GRONINGEN("Groningen"),
    LIMBURG("Limburg"),
    NOORD_BRABANT("Noord-Brabant"),
    NOORD_HOLLAND("Noord-Holland"),
    OVERIJSSEL("Overijssel"),
    UTRECHT("Utrecht"),
    ZEELAND("Zeeland"),
    ZUID_HOLLAND("Zuid-Holland");

    private final String displayName;
}
