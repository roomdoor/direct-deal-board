package roomdoor.directdealboard.entity.listener;

import java.time.LocalDateTime;

public interface Auditable {

    String getCreatedAt();

    String getUpdatedAt();

    void setCreatedAt(String createdAt);

    void setUpdatedAt(String updatedAt);

}
