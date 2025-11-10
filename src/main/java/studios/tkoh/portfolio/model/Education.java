package studios.tkoh.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "education", schema = "studiostkoh.portafolio")
@Getter
@Setter
@NoArgsConstructor
public class Education extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;
    @Column(nullable = false)
    private String institution;
    @Column(nullable = false)
    private String degree;
    private String field;
    @Column(nullable = false)
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    @Column(columnDefinition = "TEXT")
    private String description;
}
