package ma.xproce.videoservice.repositories;


import ma.xproce.videoservice.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository
        extends JpaRepository<Video, Long> {
}
