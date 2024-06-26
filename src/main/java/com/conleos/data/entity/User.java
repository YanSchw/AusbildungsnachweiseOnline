package com.conleos.data.entity;

import com.conleos.common.Role;
import com.conleos.data.service.UserService;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import jakarta.persistence.*;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Entity for creating and loading user data

@Entity
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "username")
    String username;
    @Column(name = "passwordHash")
    String passwordHash;
    @Column(name = "role")
    Role role = Role.Trainee;
    @Column(name = "firstname")
    String firstName;
    @Column(name = "lastname")
    String lastName;
    @Column(name = "birthday")
    LocalDate birthday;
    @Column(name = "email")
    String email;

    // Trainee data only
    @ElementCollection(fetch = FetchType.EAGER)
    List<Long> assigneeIDs = new ArrayList<>();
    @Column(name = "startDate")
    LocalDate startDate;

    // Instructor data only
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "blobImg", columnDefinition = "BLOB")
    private byte[] signatureImage;

    public User(String username, String passwordHash, Role role, String firstName, String lastName, LocalDate birthday) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    protected User() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<User> getAssignees() {
        return new ArrayList<>(assigneeIDs.stream().map(aLong -> UserService.getInstance().getUserByID(aLong)).toList());
    }

    public void setAssignees(List<User> assignees) {
        this.assigneeIDs = new ArrayList<>(assignees.stream().map(User::getId).toList());
    }

    public void addAssignee(User assignee) {
        this.assigneeIDs.add(assignee.getId());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User other) {
            return this.getId().equals(other.getId());
        }
        return false;
    }

    public List<Long> getAssigneeIds() {
        assigneeIDs.removeIf(id -> UserService.getInstance().getUserByID(id) == null);
        return this.assigneeIDs;
    }

    public void setAssigneeIds(List<Long> assigneeIDs) {
        this.assigneeIDs = assigneeIDs;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public Image generateSignImage() {
        if (getSignatureImage() == null) {
            return null;
        }

        StreamResource sr = new StreamResource("signature", () -> {
            return new ByteArrayInputStream(getSignatureImage());
        });
        sr.setContentType("image/png");
        return new Image(sr, "signature");
    }

}
