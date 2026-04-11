package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Course entity in the system and database.
 */
@Entity
@Table(name = "course")
public class Course {

    /**
     * The unique identifier of the Course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    /**
     * The name of the Course in English.
     */
    @Column(name = "name_en")
    private String nameEN;

    /**
     * The name of the Course in Finnish.
     */
    @Column(name = "name_fi")
    private String nameFI;

    /**
     * The name of the Course in Japanese.
     */
    @Column(name = "name_ja")
    private String nameJA;

    /**
     * The name of the Course in Greek.
     */
    @Column(name = "name_el")
    private String nameEL;

    /**
     * The unique identifier of the Course.
     */
    @Column(unique = true)
    private String identifier;

    /**
     * The status of the Course ("ACTIVE" or "ARCHIVED").
     */
    private String status;

    /**
     * The creation date and time of the Course.
     */
    private LocalDateTime created;

    /**
     * The date and time the Course was archived.
     */
    private LocalDateTime archived;

    /**
     * The Teacher entity to whom the Course belongs to.
     */
    @ManyToOne()
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher teacher;

    /**
     * The Attends instances associated with the Course.
     */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

    /**
     * Constructs a Course with localized names, identifier and Teacher.
     *
     * @param nameEN name in English
     * @param nameFI name in Finnish
     * @param nameJA name in Japanese
     * @param nameEL name in Greek
     * @param identifier the
     * @param teacher the Teacher entity that owns the Course
     */
    public Course(String nameEN, String nameFI, String nameJA, String nameEL, String identifier, Teacher teacher) {
        this.nameEN = nameEN;
        this.nameFI = nameFI;
        this.nameJA = nameJA;
        this.nameEL = nameEL;
        this.identifier = identifier;
        this.status = "ACTIVE";
        this.created = LocalDateTime.now();
        this.teacher = teacher;
    }

    /**
     * The default constructor for a Course.
     */
    public Course() {
    }

    /**
     * Gets the localized name of a Course.
     * @param lang the language code ("en", "fi", "ja", or "el")
     * @return localized name in the requested language, or English if the language is not supported
     */
    public String getName(String lang) {
        String name;
        switch (lang) {
            case "en":
                name = this.nameEN;
                break;
            case "fi":
                name = this.nameFI;
                break;
            case "ja":
                name = this.nameJA;
                break;
            case "el":
                name = this.nameEL;
                break;
            default:
                name = this.nameEN;
        }
        return name;
    }

    /**
     * Sets the localized name of a Course.
     * @param lang the language code ("en", "fi", "ja", or "el")
     */
    public void setName(String lang, String name) {
        switch (lang) {
            case "en":
                this.nameEN = name;
                break;
            case "fi":
                this.nameFI = name;
                break;
            case "ja":
                this.nameJA = name;
                break;
            case "el":
                this.nameEL = name;
                break;
            default:
                this.nameEN = name;
        }
    }

    /**
     * Gets the Attends instances associated with Course object.
     * @return list of Attends instances
     */
    public List<Attends> getAttends() {
        return attends;
    }

    /**
     * Sets the Attends instances associated with Course object.
     * @param attends a list of Attends instances
     */
    public void setAttends(List<Attends> attends) {
        this.attends = attends;
    }

    /**
     * Gets the unique id of a Course object.
     * @return the unique id of the Course instance
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique id of a Course object.
     * @param id the id to be set as int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of a Course object.
     * @return the unique identifier of the Course instance
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Sets the unique identifier of a Course object.
     * @param identifier the id to be set as String
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets the status of a Course object ("ACTIVE" or "ARCHIVED").
     * @return the status of the Course instance
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status of a Course object ("ACTIVE" or "ARCHIVED").
     * @param status the status to be set as String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the creation date and time of a Course object.
     * @return the creation datetime of the Course as a LocalDateTime object
     */
    public LocalDateTime getCreated() {
        return this.created;
    }

    /**
     * Sets the creation date and time of a Course object.
     * @param created the creation datetime to be set as a LocalDateTime object
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Gets the date and time of when a Course object was archived.
     * @return the archiving datetime of the Course as a LocalDateTime object
     */
    public LocalDateTime getArchived() {
        return this.archived;
    }

    /**
     * Sets the archiving date and time of a Course object.
     * @param archived the archiving datetime to be set as a LocalDateTime object
     */
    public void setArchived(LocalDateTime archived) {
        this.archived = archived;
    }

    /**
     * Gets the Teacher object that owns the Course.
     * @return the Teacher object that owns the Course
     */
    public Teacher getTeacher() {
        return this.teacher;
    }

    /**
     * Sets the Teacher object that owns the Course object.
     * @param teacher the Teacher object to be set as owner
     */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /**
     * Overrides the toString method. Returns Course's name in English.
     * @return the name of the Course in English
     */
    @Override
    public String toString() {
        return this.nameEN;
    }


}
