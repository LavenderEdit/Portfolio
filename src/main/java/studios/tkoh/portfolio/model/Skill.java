package studios.tkoh.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "skill", schema = "studiostkoh.portafolio")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Skill extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    private SkillCategory category;

    @Column(nullable = false, length = 80)
    private String name;

    private short level;

    @Column(length = 255)
    private String icon;

    private int sortOrder;
}
