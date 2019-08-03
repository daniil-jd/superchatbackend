package ru.itpark.entity.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itpark.entity.UserEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registration_token")
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationTokenEntity {
    @Id
    private String token;
    @ManyToOne(cascade = {CascadeType.MERGE}) // <- FIX
    private UserEntity user;
    private LocalDateTime created;
}
