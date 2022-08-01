package fi.homebrewing.competition.domain;


import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

@Entity
public class Competitor {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Nullable
    private String firstName;
    @NotBlank(message = "{name.mandatory}")
    private String lastName;
    @NotBlank(message = "{location.mandatory}")
    private String location;
    @Nullable
    private String phoneNumber;
    @NotBlank(message = "{emailAddress.mandatory}")
    private String emailAddress;

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

    public void setFirstName(@Nullable String firstName) {
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
}
