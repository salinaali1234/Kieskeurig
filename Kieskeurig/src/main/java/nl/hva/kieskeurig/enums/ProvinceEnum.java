package nl.hva.kieskeurig.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Enum that contains each province and their corresponding constituencies
 */
@Getter
@AllArgsConstructor
@ToString
public enum ProvinceEnum {
    DRENTHE("Drenthe", new String[]{"Assen"}),
    FLEVOLAND("Flevoland", new String[]{"Lelystad"}),
    FRIESLAND("Friesland (Fryslân)", new String[]{"Leeuwarden"}),
    GELDERLAND("Gelderland", new String[]{"Nijmegen", "Arnhem"}),
    GRONINGEN("Groningen", new String[]{"Groningen"}),
    LIMBURG("Limburg", new String[]{"Maastricht"}),
    NOORD_BRABANT("Noord-Brabant", new String[]{"Tilburg", "'s-Hertogenbosch"}),
    NOORD_HOLLAND("Noord-Holland", new String[]{"Amsterdam", "Haarlem", "Den Helder"}),
    OVERIJSSEL("Overijssel", new String[]{"Zwolle"}),
    UTRECHT("Utrecht", new String[]{"Utrecht"}),
    ZEELAND("Zeeland", new String[]{"Middelburg"}),
    ZUID_HOLLAND("Zuid-Holland", new String[]{"'s-Gravenhage", "Rotterdam", "Dordrecht", "Leiden"});

    private final String displayName;
    private final String[] constituencies;
}
