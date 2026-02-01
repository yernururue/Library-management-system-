package model;

public class Author {
    private int id;
    private String name;
    private int birthYear;
    private String nationality;
    public Author(int id, String name, int birthYear, String nationality) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public void bio() {
        System.out.println("Name: " + name +
                            "\nYear: " + birthYear +
                            "\nNationality: " + nationality);
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public int getBirthYear() {
        return birthYear;
    }
    public String getNationality() {
        return nationality;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        } this.name = name;
    }

    public void setBirthYear(int birthYear) {
        if (birthYear <0 || birthYear>2026) {
            throw new IllegalArgumentException("Invalid year.");
        } this.birthYear = birthYear;
    }

    public void setNationality(String nationality) {
        if (nationality == null || nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be empty.");
        } this.nationality = nationality;
    }
}
