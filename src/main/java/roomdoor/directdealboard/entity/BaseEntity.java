package roomdoor.directdealboard.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomdoor.directdealboard.entity.listener.Auditable;

@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Auditable {

	@CreatedDate
	@Column(columnDefinition = "datetime(6) default now(6)", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "datetime(6) default now(6)", nullable = false)
	private LocalDateTime updatedAt;
}
