package fi.homebrewing.competition.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

@Entity(name = "Competition")
@Table(name = "competition")
public class Competition {
    public Competition() {
    }

    public enum Type {
        COMMERCIAL,
        HOMEBREWING,
        MIXED,
        OTHER
    }

    public Competition(UUID id, String name, String comment, Type type) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.type = type;
    }

    public Competition(String name, String comment, Type type) {
        this(UUID.randomUUID(), name, comment, type);
    }

    @Id
    @GeneratedValue
    private UUID id;
    @NotBlank(message = "{name.mandatory}")
    private String name;
    @Nullable
    private String comment;

    @Enumerated(EnumType.STRING)
    private Type type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
