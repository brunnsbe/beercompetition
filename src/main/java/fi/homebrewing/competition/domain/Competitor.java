package fi.homebrewing.competition.domain;


import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

@Entity
public class Competitor {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;
    @NotBlank(message = "{firstName.mandatory}")
    @Size(min = 1, max = 100)
    private String firstName;
    @NotBlank(message = "{lastName.mandatory}")
    @Size(min = 1, max = 100)
    private String lastName;
    @Nullable
    @Size(max = 100)
    private String groupName;
    @NotBlank(message = "{location.mandatory}")
    @Size(min = 1, max = 100)
    private String location;
    @Nullable
    @Size(max = 100)
    private String phoneNumber;
    @NotBlank(message = "{emailAddress.mandatory}")
    @Column(unique=true)
    @Email(message = "{emailAddress.not_valid}")
    @Size(min = 1, max = 100)
    private String emailAddress;

    @OneToMany(mappedBy = "competitor")
    private Set<Beer> beers;

    public Competitor() {
    }

    public Competitor(String firstName, String lastName, String location, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Transient
    public String getFullName() {
        return lastName
            + (firstName == null || firstName.isBlank() ? "" : ", " + firstName)
            + " (" + location + ")";
    }

    public Set<Beer> getBeers() {
        return beers;
    }

    public void setBeers(Set<Beer> beers) {
        this.beers = beers;
    }

    @Nullable
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(@Nullable String groupName) {
        this.groupName = groupName;
    }
}
