package ma.xproce.videoservice.managers;


import ma.xproce.videoservice.dto.CreatorRequest;
import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.mappers.VideoMapper;
import ma.xproce.videoservice.repositories.CreatorRepository;

import org.springframework.stereotype.Service;

@Service
public class CreatorManager {

    private final CreatorRepository creatorRepository;
    private final VideoMapper videoMapper;

    public CreatorManager(
            CreatorRepository creatorRepository,
            VideoMapper videoMapper) {

        this.creatorRepository = creatorRepository;
        this.videoMapper = videoMapper;
    }

    public Creator saveCreator(CreatorRequest request) {

        Creator creator = videoMapper.toCreator(request);

        return creatorRepository.save(creator);
    }

    public Creator findById(Long id) {

        return creatorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Creator " + id + " not found"
                        )
                );
    }
}
