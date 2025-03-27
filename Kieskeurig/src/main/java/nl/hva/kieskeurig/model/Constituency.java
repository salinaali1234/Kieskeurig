package nl.hva.kieskeurig.model;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Constituency {

    private int id;
    private String name;

    public Constituency(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    public int getId() {return id;}
    public String getName() {return name;}


}
