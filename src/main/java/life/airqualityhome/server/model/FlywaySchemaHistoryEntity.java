package life.airqualityhome.server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class FlywaySchemaHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String version;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String script;

    private Integer checksum;

    @Column(nullable = false)
    private String installedBy;

    @Column(nullable = false)
    private Instant installedOn;

    @Column(nullable = false)
    private Integer executionTime;

    @Column(nullable = false)
    private boolean success;
}
