package nl.hva.kieskeurig.model;

public class Vote {
    private String partyName;
    private int validVotes;

    public Vote(String partyName, int validVotes) {
        this.partyName = partyName;
        this.validVotes = validVotes;
    }

    public String getPartyName() {
        return partyName;
    }

    public int getValidVotes() {
        return validVotes;
    }
}
