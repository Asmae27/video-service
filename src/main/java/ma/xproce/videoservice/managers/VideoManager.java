package ma.xproce.videoservice.managers;


import ma.xproce.videoservice.dto.VideoRequest;
import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;
import ma.xproce.videoservice.mappers.VideoMapper;
import ma.xproce.videoservice.repositories.VideoRepository;

import org.springframework.stereotype.Service;

@Service
public class VideoManager {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoManager(
            VideoRepository videoRepository,
            VideoMapper videoMapper) {

        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
    }

    public Video saveVideo(
            VideoRequest request,
            Creator creator) {

        Video video = videoMapper.toVideo(request);

        video.setCreator(creator);

        return videoRepository.save(video);
    }

    public Video findById(Long id) {

        return videoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Video " + id + " not found"
                        )
                );
    }

    public Video updateVideo(Video video) {

        return videoRepository.save(video);
    }
}
