package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.enums.Province;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartyService {
    public Map<String, Integer> getVotesPerPartyByElectionByProvince(String electionId, String province) {
        Map<String, Integer> map = new HashMap<>();

            for (Province value : Province.values()) {
                if (value.getDisplayName().equalsIgnoreCase(province)) {
                    String party = "VVD"; // Temp
                    int votes = 727; // Temp

                    map.put(party, votes);
                }
            }



        return map;
    }
}
