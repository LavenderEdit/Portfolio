package studios.tkoh.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studios.tkoh.portfolio.audit.BaseEntity;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Table(name = "profile", schema = "studiostkoh.portafolio")
@Getter
@Setter
@NoArgsConstructor
public class Profile extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80, unique = true)
    private String slug;
    @Column(nullable = false, length = 120)
    private String fullName;
    @Column(nullable = false, length = 160)
    private String headline;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String bio;
    @Column(nullable = false, length = 160)
    private String email;
    private String location;
    private String avatarUrl;
    private String resumeUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
