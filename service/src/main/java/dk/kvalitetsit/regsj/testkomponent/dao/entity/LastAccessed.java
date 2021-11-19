package dk.kvalitetsit.regsj.testkomponent.dao.entity;

import java.time.LocalDateTime;

public class LastAccessed {
    private Long id;
    private LocalDateTime accessTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }
}
