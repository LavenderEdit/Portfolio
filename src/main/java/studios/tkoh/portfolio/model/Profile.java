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
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import studios.tkoh.portfolio.audit.BaseEntity;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Table(name = "profile", schema = "studiostkoh.portafolio", uniqueConstraints = {
    @UniqueConstraint(name = "uk_profile_slug", columnNames = "slug"),
    @UniqueConstraint(name = "uk_profile_user_id", columnNames = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Profile extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false, length = 80)
    private String slug;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 160)
    private String headline;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bio;

    @Column(name = "contact_email", nullable = false, length = 160)
    private String contactEmail;

    @Column(length = 100)
    private String location;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "resume_url")
    private String resumeUrl;
}
