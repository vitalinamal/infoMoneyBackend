package prog.academy.infomoney.entity;

import jakarta.persistence.*;
import lombok.*;
import prog.academy.infomoney.enums.TokenType;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "_token")
public class Token implements Serializable {

    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String jwtToken;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @Column
    public boolean revoked;

    @Column
    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
