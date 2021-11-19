package dk.kvalitetsit.regsj.testkomponent.dao;

import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;

import java.util.Optional;

public interface LastAccessedDao {
    void insert(LastAccessed lastAccessed);

    Optional<LastAccessed> getLatest();
}