package dk.kvalitetsit.regsj.testkomponent.dao;

import dk.kvalitetsit.regsj.testkomponent.dao.entity.LastAccessed;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class LastAccessedDaoImplTest extends AbstractDaoTest {
    @Autowired
    private LastAccessedDao lastAccessedDao;

    @Test
    public void testByMessageId() {
        var input = new LastAccessed();
        input.setAccessTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        lastAccessedDao.insert(input);

        var result = lastAccessedDao.getLatest();
        assertNotNull(result.isPresent());
        assertEquals(input.getAccessTime(), result.get().getAccessTime());
    }
}
