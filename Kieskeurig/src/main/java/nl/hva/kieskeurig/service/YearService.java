package nl.hva.kieskeurig.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class YearService {
    public List<String>getYears() {
        List<String> years = new ArrayList<>();
        String path = "./Kieskeurig/src/main/resources";

        File[] directories = new File(path).listFiles(File::isDirectory);

        if (directories != null) {
            for (File directory : directories) {
                String name = directory.getName();
                System.out.println(name);

                if (name.startsWith("Verkiezingsuitslag_Tweede_Kamer_")) {
                    String year = name.replace("Verkiezingsuitslag_Tweede_Kamer_", "");
                    years.add(year);
                    System.out.println(year);
                }
            }
        }

        return years;
    }
}
