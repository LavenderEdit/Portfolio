package studios.tkoh.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
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
@Table(name = "project", schema = "studiostkoh.portafolio", indexes = {
    @Index(name = "idx_project_featured", columnList = "profile_id, featured, sort_order")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_project_profile_slug", columnNames = {"profile_id", "slug"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Project extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    private Profile profile;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(nullable = false, length = 160)
    private String slug;

    @Column(nullable = false, length = 280)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String repoUrl;

    private String liveUrl;

    private String coverImage;

    private java.time.LocalDate startDate;

    private java.time.LocalDate endDate;

    private boolean featured;

    private int sortOrder;

    @Column(name = "project_status", nullable = false, length = 30)
    private String projectStatus = "PUBLISHED";

    @Column(name = "meta_title", length = 160)
    private String metaTitle;

    @Column(name = "meta_description", length = 300)
    private String metaDescription;

    @Column(name = "og_image")
    private String ogImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_skill",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
    @OrderBy("sortOrder ASC, name ASC")
    @ToString.Exclude
    private Set<Skill> skills = new LinkedHashSet<>();
}
