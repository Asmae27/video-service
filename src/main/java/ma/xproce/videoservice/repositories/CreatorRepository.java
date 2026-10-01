package ma.xproce.videoservice.repositories;


import ma.xproce.videoservice.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository
        extends JpaRepository<Creator, Long> {
}
