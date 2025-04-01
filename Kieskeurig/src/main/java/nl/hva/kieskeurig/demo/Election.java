package nl.hva.kieskeurig.demo;

import nl.hva.kieskeurig.utils.xml.Transformer;

import java.util.Map;

/**
 * Just a very silly election class that only demonstrates that a {@link Transformer}
 * can return an instance of a class.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class Election {
    public Map<String, String> data;

    @Override
    public String toString() {
        return "The last information received is: %s".formatted(data);
    }
}
