package life.airqualityhome.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;
}
