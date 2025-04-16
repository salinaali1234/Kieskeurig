package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.static_var.Province;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartyService {
    public Map<String, Integer> getVotesPerPartyByElectionByProvince(String electionId, String province) {
        Map<String, Integer> map = new HashMap<>();

        try {
            // Iterate through every static variable from the Province class
            for (Field field : Province.class.getDeclaredFields()) {
                String fieldValue = field.get(null).toString();

                if (fieldValue.equalsIgnoreCase(province)) {
                    String party = "VVD"; // Temp
                    int votes = 727; // Temp

                    map.put(party, votes);
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();  // Handle the exception
        }

        return map;
    }
}
